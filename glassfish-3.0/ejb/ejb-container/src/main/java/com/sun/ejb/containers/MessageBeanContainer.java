/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.sun.ejb.containers;

import java.lang.reflect.Method;

import java.lang.reflect.InvocationTargetException;

import javax.ejb.*;

import javax.transaction.*;
import javax.transaction.xa.*;

import com.sun.appserv.connectors.internal.api.TransactedPoolManager;
import com.sun.ejb.*;
import com.sun.ejb.containers.util.pool.AbstractPool;
import com.sun.ejb.containers.util.pool.NonBlockingPool;
import com.sun.ejb.containers.util.pool.ObjectFactory;
import com.sun.enterprise.util.LocalStringManagerImpl;
import com.sun.enterprise.util.Utility;
import com.sun.appserv.connectors.internal.api.ResourceHandle;
import com.sun.appserv.connectors.internal.api.ConnectorConstants;
import com.sun.enterprise.deployment.EjbDescriptor;
import com.sun.enterprise.deployment.MethodDescriptor;
import com.sun.enterprise.deployment.EjbMessageBeanDescriptor;
import static com.sun.enterprise.deployment.LifecycleCallbackDescriptor.CallbackType;
import com.sun.enterprise.deployment.runtime.BeanPoolDescriptor;
import com.sun.enterprise.config.serverbeans.MdbContainer;
import com.sun.enterprise.admin.monitor.callflow.ComponentType;
import com.sun.ejb.monitoring.stats.EjbPoolStatsProvider;
import org.glassfish.ejb.api.MessageBeanProtocolManager;
import org.glassfish.ejb.api.ResourcesExceededException;
import org.glassfish.ejb.api.MessageBeanListener;
import org.glassfish.ejb.spi.MessageBeanClient;
import org.glassfish.ejb.spi.MessageBeanClientFactory;
import com.sun.ejb.monitoring.stats.EjbMonitoringStatsProvider;
import com.sun.ejb.monitoring.stats.MessageDrivenBeanStatsProvider;

import java.util.logging.*;

import com.sun.logging.*;

import org.glassfish.api.invocation.ComponentInvocation;

import static com.sun.ejb.containers.EJBContextImpl.BeanState;

/**
 * This class provides container functionality specific to message-driven EJBs.
 * At deployment time, one instance of the MessageDrivenBeanContainer is created
 * for each message-driven bean in an application.
 * <P>
 * The 3 states of a Message-driven EJB (an EJB can be in only 1 state at a
 * time): 1. POOLED : ready for invocations, no transaction in progress 2.
 * INVOKING : processing an invocation 3. DESTROYED : does not exist
 * 
 * A Message-driven Bean can hold open DB connections across invocations. It's
 * assumed that the Resource Manager can handle multiple incomplete transactions
 * on the same connection.
 * 
 * @author Kenneth Saks
 */
public final class MessageBeanContainer extends BaseContainer implements
		MessageBeanProtocolManager { //, MessageDrivenBeanStatsProvider {
	private static final Logger _logger = LogDomains.getLogger(
			MessageBeanContainer.class, LogDomains.MDB_LOGGER);

	private String appEJBName_;

	private static LocalStringManagerImpl localStrings = new LocalStringManagerImpl(
			MessageBeanContainer.class);

	// Message-bean instance states
	private static final int POOLED = 1, INVOKING = 2, DESTROYED = 3;

	private MessageBeanClient messageBeanClient_ = null;

	private AbstractPool messageBeanPool_ = null;

	private BeanPoolDescriptor beanPoolDesc_ = null;
	private int maxMessageBeanListeners_;
	private int numMessageBeanListeners_;

	// Property used to bootstrap message bean client factory for inbound
	// message delivery.
	private static final String MESSAGE_BEAN_CLIENT_FACTORY_PROP = "com.sun.enterprise.MessageBeanClientFactory";

	private static final String DEFAULT_MESSAGE_BEAN_CLIENT_FACTORY =
            ConnectorConstants.CONNECTOR_MESSAGE_BEAN_CLIENT_FACTORY;

	private static final int DEFAULT_RESIZE_QUANTITY = 1;
	private static final int DEFAULT_STEADY_SIZE = 10;
	private static final int DEFAULT_MAX_POOL_SIZE = 60;
	private static final int DEFAULT_IDLE_TIMEOUT = 600;
	private static final int MIN_IDLE_TIMEOUT = 1;

        // TODO : remove
	private int statMessageCount = 0;

	private TransactedPoolManager poolMgr;
	
	MessageBeanContainer(EjbDescriptor desc, ClassLoader loader)
			throws Exception {
		super(ContainerType.MESSAGE_DRIVEN, desc, loader);

		appEJBName_ = desc.getApplication().getRegistrationName() + ":"
				+ desc.getName();

		EjbMessageBeanDescriptor msgBeanDesc = (EjbMessageBeanDescriptor) desc;

		try {

			// Register the tx attribute for each method on MessageListener
			// interface. NOTE : These method objects MUST come from the
			// MessageListener interface, NOT the bean class itself. This
			// is because the message bean container clients do not have
			// access to the message bean class.
			Method[] msgListenerMethods = msgBeanDesc
					.getMessageListenerInterfaceMethods(loader);

			for (int i = 0; i < msgListenerMethods.length; i++) {
				Method next = msgListenerMethods[i];
				super.registerTxAttrForMethod(next, MethodDescriptor.EJB_BEAN);
			}
			
			poolMgr = EjbContainerUtilImpl.getInstance().getDefaultHabitat().getByContract(TransactedPoolManager.class);
			
			// NOTE : No need to register tx attribute for ejbTimeout. It's
			// done in BaseContainer intialization.
			// Message-driven beans can be timed objects.

			// Bootstrap message bean client factory. If the class name is
			// specified as a system property, that value takes precedence.
			// Otherwise use default client factory. The default is set to
			// a client factory that uses the S1AS 7 style JMS connection
			// consumer contracts. This will be changed once the Connector 1.5
			// implementation is ready.
			String factoryClassName = System.getProperty(MESSAGE_BEAN_CLIENT_FACTORY_PROP);
            MessageBeanClientFactory clientFactory = null;
            if(factoryClassName != null){
                Class clientFactoryClass = loader.loadClass(factoryClassName);
                clientFactory = (MessageBeanClientFactory) clientFactoryClass
                        .newInstance();
            } else {
                clientFactory = EjbContainerUtilImpl.getInstance().getDefaultHabitat().getComponent(
                        MessageBeanClientFactory.class, DEFAULT_MESSAGE_BEAN_CLIENT_FACTORY );
            }
            _logger.log(Level.FINE, "Using " + clientFactory.getClass().getName()
                    + " for message bean client factory in " + appEJBName_);


			// Create message bean pool before calling setup on
			// Message-bean client, since pool properties can be retrieved
			// through MessageBeanProtocolManager interface.
			createMessageBeanPool(msgBeanDesc);

			// Set resource limit for message bean listeners created through
			// Protocol Manager. For now, just use max pool size. However,
			// we might want to bump this up once the ejb timer service is
			// integrated.
			maxMessageBeanListeners_ = beanPoolDesc_.getMaxPoolSize();
			numMessageBeanListeners_ = 0;

			messageBeanClient_ = clientFactory
					.createMessageBeanClient(msgBeanDesc);

			messageBeanClient_.setup(this);

			registerMonitorableComponents(msgListenerMethods);

			createCallFlowAgent(ComponentType.MDB);
		} catch (Exception ex) {

			if (messageBeanClient_ != null) {
				messageBeanClient_.close();
			}

			_logger.log(Level.SEVERE,
					"containers.mdb.create_container_exception", new Object[] {
							desc.getName(), ex.toString() });
			_logger.log(Level.SEVERE, ex.getClass().getName(), ex);
			throw ex;
		}
	}

	protected void registerMonitorableComponents(Method[] msgListenerMethods) {
		//registryMediator.registerProvider(this);
		super.registerMonitorableComponents();
		super.populateMethodMonitorMap(msgListenerMethods);
                poolProbeListener = new EjbPoolStatsProvider(messageBeanPool_,
                        getContainerId(), containerInfo.appName, containerInfo.modName,
                        containerInfo.ejbName);
                poolProbeListener.register();
		// TODO registryMediator.registerProvider(messageBeanPool_);
		// super.setMonitorOn(mdbc.isMonitoringEnabled());
		_logger.log(Level.FINE, "[MessageBeanContainer] registered monitorable");
	}

    protected EjbMonitoringStatsProvider getMonitoringStatsProvider(
            String appName, String modName, String ejbName) {
        return new MessageDrivenBeanStatsProvider(getContainerId(), appName, modName, ejbName);
    }

    protected void initializeHome()
    	throws Exception {
    	//For MDBs this is no op
    }
    
	private void createMessageBeanPool(EjbMessageBeanDescriptor descriptor) {

		beanPoolDesc_ = descriptor.getIASEjbExtraDescriptors().getBeanPool();

		if (beanPoolDesc_ == null) {
			beanPoolDesc_ = new BeanPoolDescriptor();
		}

		MdbContainer mdbc = ejbContainerUtilImpl.getDefaultHabitat()
				.getComponent(MdbContainer.class);

		int maxPoolSize = beanPoolDesc_.getMaxPoolSize();
		if (maxPoolSize < 0) {
			maxPoolSize = stringToInt(mdbc.getMaxPoolSize(), appEJBName_,
					_logger);
		}
		maxPoolSize = validateValue(maxPoolSize, 1, -1, DEFAULT_MAX_POOL_SIZE,
				"max-pool-size", appEJBName_, _logger);
		beanPoolDesc_.setMaxPoolSize(maxPoolSize);

		int value = beanPoolDesc_.getSteadyPoolSize();
		if (value < 0) {
			value = stringToInt(mdbc.getSteadyPoolSize(), appEJBName_, _logger);
		}
		value = validateValue(value, 0, maxPoolSize, DEFAULT_STEADY_SIZE,
				"steady-pool-size", appEJBName_, _logger);
		beanPoolDesc_.setSteadyPoolSize(value);

		value = beanPoolDesc_.getPoolResizeQuantity();
		if (value < 0) {
			value = stringToInt(mdbc.getPoolResizeQuantity(), appEJBName_,
					_logger);
		}
		value = validateValue(value, 1, maxPoolSize, DEFAULT_RESIZE_QUANTITY,
				"pool-resize-quantity", appEJBName_, _logger);
		beanPoolDesc_.setPoolResizeQuantity(value);

		value = beanPoolDesc_.getPoolIdleTimeoutInSeconds();
		if (value <= 0) {
			value = stringToInt(mdbc.getIdleTimeoutInSeconds(), appEJBName_,
					_logger);
		}
		value = validateValue(value, MIN_IDLE_TIMEOUT, -1,
				DEFAULT_IDLE_TIMEOUT, "idle-timeout-in-seconds", appEJBName_,
				_logger);
		beanPoolDesc_.setPoolIdleTimeoutInSeconds(value);

		if (_logger.isLoggable(Level.FINE)) {
			_logger.log(Level.FINE, appEJBName_
					+ ": Setting message-driven bean pool max-pool-size="
					+ beanPoolDesc_.getMaxPoolSize() + ", steady-pool-size="
					+ beanPoolDesc_.getSteadyPoolSize()
					+ ", pool-resize-quantity="
					+ beanPoolDesc_.getPoolResizeQuantity()
					+ ", idle-timeout-in-seconds="
					+ beanPoolDesc_.getPoolIdleTimeoutInSeconds());
		}

		// Create a non-blocking pool of message bean instances.
		// The protocol manager implementation enforces a limit
		// on message bean resources independent of the pool.
		ObjectFactory objFactory = new MessageBeanContextFactory();
		messageBeanPool_ = new NonBlockingPool(getContainerId(), appEJBName_, objFactory,
				beanPoolDesc_.getSteadyPoolSize(), beanPoolDesc_
						.getPoolResizeQuantity(), beanPoolDesc_
						.getMaxPoolSize(), beanPoolDesc_
						.getPoolIdleTimeoutInSeconds(), loader);

	}

	protected static int stringToInt(String val, String appName, Logger logger) {
		int value = -1;
		try {
			value = Integer.parseInt(val);
		} catch (Exception e) {
			_logger.log(Level.WARNING, "containers.mdb.invalid_value",
					new Object[] { appName, new Integer(val), e.toString(),
							new Integer(0) });
			_logger.log(Level.WARNING, "", e);
		}
		return value;
	}

	// deft should always >= lowLimit
	protected int validateValue(int value, int lowLimit, int highLimit,
			int deft, String emsg, String appName, Logger logger) {

		if (value < lowLimit) {
			_logger.log(Level.WARNING, "containers.mdb.invalid_value",
					new Object[] { appName, new Integer(value), emsg,
							new Integer(lowLimit) });
			value = deft;
		}

		if ((highLimit >= 0) && (value > highLimit)) {
			_logger.log(Level.WARNING, "containers.mdb.invalid_value",
					new Object[] { appName, new Integer(value), emsg,
							new Integer(highLimit) });
			value = highLimit;
		}

		return value;
	}

	private boolean containerStartsTx(Method method) {
		int txMode = getTxAttr(method, MethodDescriptor.EJB_BEAN);

		return isEjbTimeoutMethod(method) ? ((txMode == TX_REQUIRES_NEW) || (txMode == TX_REQUIRED))
				: (txMode == TX_REQUIRED);
	}

	public String getMonitorAttributeValues() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("MESSAGEDRIVEN ");
		sbuf.append(appEJBName_);

		sbuf.append(messageBeanPool_.getAllAttrValues());
		sbuf.append("]");
		return sbuf.toString();
	}

	public boolean userTransactionMethodsAllowed(ComponentInvocation inv) {
		boolean utMethodsAllowed = false;
		if (isBeanManagedTran) {
			if (inv instanceof EjbInvocation) {
				EjbInvocation ejbInvocation = (EjbInvocation) inv;
				EJBContextImpl mdc = (EJBContextImpl) ejbInvocation.context;
				utMethodsAllowed = (mdc.isUnitialized() || mdc.isInEjbRemove()) ? false
						: true;
			}
		}
		return utMethodsAllowed;
	}

	public void setEJBHome(EJBHome ejbHome) throws Exception {
		throw new Exception("Can't set EJB Home on Message-driven bean");
	}

	public EJBObjectImpl getEJBObjectImpl(byte[] instanceKey) {
		throw new EJBException("No EJBObject for message-driven beans");
	}

	public EJBObjectImpl createEJBObjectImpl() throws CreateException {
		throw new EJBException("No EJBObject for message-driven beans");
	}

	void removeBean(EJBLocalRemoteObject ejbo, Method removeMethod,
			boolean local) throws RemoveException, EJBException {
		throw new EJBException("not used in message-driven beans");
	}

	/**
	 * Override callEJBTimeout from BaseContainer since delivery to message
	 * driven beans is a bit different from session/entity.
	 */
	boolean callEJBTimeout(RuntimeTimerState timerState,
			EJBTimerService timerService) throws Exception {

		boolean redeliver = false;

		// There is no resource associated with the delivery of the timeout.
		ResourceHandle nullResourceHandle = null;

		try {

                        Method method = getTimeoutMethod(timerState);
                        
			// Do pre-invoke logic for message bean with tx import = false
			// and a null resource handle.
			beforeMessageDelivery(method, MessageDeliveryType.Timer,
                    false, nullResourceHandle);

                        prepareEjbTimeoutParams((EjbInvocation) invocationManager.getCurrentInvocation(),
				timerState, timerService);

                        // Method arguments had been set already
			deliverMessage(null);

		} catch (Throwable t) {
			// A runtime exception thrown from ejbTimeout, independent of
			// its transactional setting(CMT, BMT, etc.), should result in
			// a redelivery attempt. The instance that threw the runtime
			// exception will be destroyed, as per the EJB spec.
			redeliver = true;

			_logger.log(Level.FINE, "ejbTimeout threw Runtime exception", t);

		} finally {
			if (!isBeanManagedTran
					&& (transactionManager.getStatus() == Status.STATUS_MARKED_ROLLBACK)) {
				redeliver = true;
				_logger.log(Level.FINE, "ejbTimeout called setRollbackOnly");
			}

			// Only call postEjbTimeout if there are no errors so far.
			if (!redeliver) {
				boolean successfulPostEjbTimeout = timerService
						.postEjbTimeout(timerState.getTimerId());
				redeliver = !successfulPostEjbTimeout;
			}

			// afterMessageDelivery takes care of postInvoke and postInvokeTx
			// processing. If any portion of that work fails, mark
			// timer for redelivery.
			boolean successfulAfterMessageDelivery = afterMessageDeliveryInternal(nullResourceHandle);
			if (!redeliver && !successfulAfterMessageDelivery) {
				redeliver = true;
			}
		}

		return redeliver;
	}

	/**
	 * Force destroy the EJB. Called from postInvokeTx. Note: EJB2.0 section
	 * 18.3.1 says that discarding an EJB means that no methods other than
	 * finalize() should be invoked on it.
	 */
	void forceDestroyBean(EJBContextImpl sc) {
		if (sc.getState() == BeanState.DESTROYED)
			return;

		// mark context as destroyed
		sc.setState(BeanState.DESTROYED);

		messageBeanPool_.destroyObject(sc);
	}

	// This particular preInvoke signature not used
	public void preInvoke(EjbInvocation inv) {
		throw new EJBException("preInvoke(Invocation) not supported");
	}

    private class MessageBeanContextFactory implements ObjectFactory {

		public Object create(Object param) {
			try {
				return createMessageDrivenEJB();
			} catch (CreateException ex) {
				throw new EJBException(ex);
			}
		}

		public void destroy(Object obj) {

			MessageBeanContextImpl beanContext = (MessageBeanContextImpl) obj;

			Object ejb = beanContext.getEJB();

			if (beanContext.getState() != EJBContextImpl.BeanState.DESTROYED) {

				// Called from pool implementation to reduce the pool size.
				// So we need to call ejb.ejbRemove() and
				// mark context as destroyed.
				EjbInvocation inv = null;

				try {
					// NOTE : Context class-loader is already set by Pool

					inv = createEjbInvocation(ejb, beanContext);

					inv.isMessageDriven = true;
					invocationManager.preInvoke(inv);

					beanContext.setInEjbRemove(true);
					interceptorManager.intercept(CallbackType.PRE_DESTROY,
							beanContext);

                    cleanupInstance(beanContext);
					ejbProbeNotifier.ejbBeanDestroyedEvent(getContainerId(),
                                                containerInfo.appName, containerInfo.modName,
                                                containerInfo.ejbName);
				} catch (Throwable t) {
					_logger.log(Level.SEVERE,
							"containers.mdb_preinvoke_exception_indestroy",
							new Object[] { appEJBName_, t.toString() });
					_logger.log(Level.SEVERE, t.getClass().getName(), t);
				} finally {
					beanContext.setInEjbRemove(false);
					if (inv != null) {
						invocationManager.postInvoke(inv);
					}
				}

				beanContext.setState(BeanState.DESTROYED);

			}

			// tell the TM to release resources held by the bean
			transactionManager.componentDestroyed(beanContext);

			// Message-driven beans can't have transactions across
			// invocations.
			beanContext.setTransaction(null);
		}

	}

	protected ComponentContext _getContext(EjbInvocation inv) {
		MessageBeanContextImpl context = null;
		try {
			context = (MessageBeanContextImpl) messageBeanPool_.getObject(null);
			context.setState(BeanState.INVOKING);
		} catch (Exception e) {
			throw new EJBException(e);
		}
		return context;
	}

	/**
	 * Return instance to a pooled state.
	 */
	public void releaseContext(EjbInvocation inv) {
		MessageBeanContextImpl beanContext = (MessageBeanContextImpl) inv.context;

		if (beanContext.getState() == BeanState.DESTROYED) {
			return;
		}

		beanContext.setState(BeanState.POOLED);

		// Message-driven beans can't have transactions across invocations.
		beanContext.setTransaction(null);

		// Update last access time so pool's time-based logic will work best
		beanContext.touch();

		messageBeanPool_.returnObject(beanContext);
	}

/** TODO
	public void appendStats(StringBuffer sbuf) {
		sbuf.append("\nMessageBeanContainer: ").append("CreateCount=").append(
				statCreateCount).append("; ").append("RemoveCount=").append(
				statRemoveCount).append("; ").append("MsgCount=").append(
				statMessageCount).append("; ");
		sbuf.append("]");
	}
**/

	// This particular postInvoke signature not used
	public void postInvoke(EjbInvocation inv) {
		throw new EJBException("postInvoke(Invocation) not supported "
				+ "in message-driven bean container");
	}

	/****************************************************************
	 * The following are implementation for methods required by the *
	 * MessageBeanProtocalManager interface. *
	 ****************************************************************/

	public MessageBeanListener createMessageBeanListener(ResourceHandle resource)
			throws ResourcesExceededException {

		boolean resourcesExceeded = false;

		synchronized (this) {
			if (numMessageBeanListeners_ < maxMessageBeanListeners_) {
				numMessageBeanListeners_++;
			} else {
				resourcesExceeded = true;
			}
		}

		if (resourcesExceeded) {
			ResourcesExceededException ree = new ResourcesExceededException(
					"Message Bean Resources " + "exceeded for message bean "
							+ appEJBName_);
			_logger.log(Level.FINE, "exceeded max of "
					+ maxMessageBeanListeners_, ree);
			throw ree;
		}

		//
		// Message bean context/instance creation is decoupled from
		// MessageBeanListener instance creation. This typically means
		// the message bean instances are instantiated lazily upon actual
		// message delivery. In addition, MessageBeanListener instances
		// are not pooled since they are currently very small objects without
		// much initialization overhead. This is the simplest approach since
		// there is minimal state to track between invocations and upon
		// error conditions such as message bean instance failure. However,
		// it could be optimized in the following ways :
		//
		// 1. Implement MessageBeanListener within MessageBeanContextImpl.
		// This reduces the number of objects created per thread of delivery.
		//
		// 2. Associate message bean context/instance with MessageBeanListener
		// across invocations. This saves one pool retrieval and one
		// pool replacement operation for each invocation.
		// 
		// 
		return new MessageBeanListenerImpl(this, resource);
	}

	public void destroyMessageBeanListener(MessageBeanListener listener) {
		synchronized (this) {
			numMessageBeanListeners_--;
		}
	}

	/**
	 * @param method
	 *            One of the methods used to deliver messages, e.g. onMessage
	 *            method for javax.jms.MessageListener. Note that if the
	 *            <code>method</code> is not one of the methods for message
	 *            delivery, the behavior of this method is not defined.
	 */
	public boolean isDeliveryTransacted(Method method) {
		return containerStartsTx(method);
	}

	public BeanPoolDescriptor getPoolDescriptor() {
		return beanPoolDesc_;
	}

	/**
	 * Instantiate and initialize a message-driven bean instance.
	 */
	private MessageBeanContextImpl createMessageDrivenEJB()
			throws CreateException {

		EjbInvocation inv = null;
		MessageBeanContextImpl context = null;
		ClassLoader originalClassLoader = null;
		boolean methodCalled = false;
		boolean methodCallFailed = false;

		try {
			// Set application class loader before invoking instance.
			originalClassLoader = Utility
					.setContextClassLoader(getClassLoader());

			// create new message-driven ejb
			Object ejb = ejbClass.newInstance();

			// create MessageDrivenContext and set it in the EJB
			context = new MessageBeanContextImpl(ejb, this);
			

			// java:comp/env lookups are allowed from here on...
			inv = createEjbInvocation(ejb, context);

			inv.isMessageDriven = true;
			invocationManager.preInvoke(inv);

			if (ejb instanceof MessageDrivenBean) {
				// setMessageDrivenContext will be called without a Tx
				// as required by the spec
				((MessageDrivenBean) ejb).setMessageDrivenContext(context);
			}

			// Perform injection right after where setMessageDrivenContext
			// would be called. This is important since injection methods
			// have the same "operations allowed" permissions as
			// setMessageDrivenContext.
			injectEjbInstance(ejb, context);
			

			// Set flag in context so UserTransaction can
			// be used from ejbCreate. Didn't want to add
			// a new state to lifecycle since that would
			// require either changing lots of code in
			// EJBContextImpl or re-implementing all the
			// context methods within MessageBeanContextImpl.
			context.setContextCalled();

			// Call ejbCreate OR @PostConstruct on the bean.
			interceptorManager.intercept(CallbackType.POST_CONSTRUCT, context);

			ejbProbeNotifier.ejbBeanCreatedEvent(getContainerId(),
                                containerInfo.appName, containerInfo.modName,
                                containerInfo.ejbName);

			// Set the state to POOLED after ejbCreate so that
			// EJBContext methods not allowed will throw exceptions
			context.setState(BeanState.POOLED);
		} catch (Throwable t) {
			_logger.log(Level.SEVERE, "containers.mdb.ejb_creation_exception",
					new Object[] { appEJBName_, t.toString() });
			if (t instanceof InvocationTargetException) {
				_logger.log(Level.SEVERE, t.getClass().getName(), t.getCause());
			}
			_logger.log(Level.SEVERE, t.getClass().getName(), t);

			CreateException ce = new CreateException(
					"Could not create Message-Driven EJB");
			ce.initCause(t);
			throw ce;

		} finally {
			if (originalClassLoader != null) {
				Utility.setContextClassLoader(originalClassLoader);
			}
			if (inv != null) {
				invocationManager.postInvoke(inv);
			}
		}

		return context;
	}

	/**
	 * Make the work performed by a message-bean instance's associated XA
	 * resource part of any global transaction
	 */
	private void registerMessageBeanResource(ResourceHandle resourceHandle)
			throws Exception {
		if (resourceHandle != null) {
			poolMgr.registerResource(resourceHandle);
		}
	}

	private void unregisterMessageBeanResource(ResourceHandle resourceHandle) {

		// resource handle may be null if preInvokeTx error caused
		// ResourceAllocator.destroyResource()
		if (resourceHandle != null) {
			poolMgr.unregisterResource(resourceHandle, XAResource.TMSUCCESS);
		}
	}

	void afterBegin(EJBContextImpl context) {
		// Message-driven Beans cannot implement SessionSynchronization!!
	}

	void beforeCompletion(EJBContextImpl context) {
		// Message-driven beans cannot implement SessionSynchronization!!
	}

	void afterCompletion(EJBContextImpl ctx, int status) {
		// Message-driven Beans cannot implement SessionSynchronization!!
	}

	// default
	public boolean passivateEJB(ComponentContext context) {
		return false;
	}

	// default
	public void activateEJB(Object ctx, Object instanceKey) {
	}

	/**
	 * Called when the application containing this message-bean has
     * successfully gotten through the initial load phase of each
     * module.  Now we can "turn on the spigot" and allow incoming
     * requests, which could result in the creation of message-bean
     * instances.
     * @param deploy true if this method is called during application deploy
	 */
	public void startApplication(boolean deploy) {
		super.startApplication(deploy);

		// Start delivery of messages to message bean instances.
		try {

			messageBeanClient_.start();

		} catch (Exception e) {

            _logger.log(Level.FINE, e.getClass().getName(), e);

            throw new RuntimeException("MessageBeanContainer.start failure for app " +
                appEJBName_, e);

		}
	}

	private void cleanupResources() {

		ASyncClientShutdownTask task = new ASyncClientShutdownTask(appEJBName_,
				messageBeanClient_, loader, messageBeanPool_);
		int timeout = 0;
		/*
		 * TODO try { timeout =
		 * ResourcesUtil.createInstance().getShutdownTimeout(); } catch
		 * (Throwable th) { _logger.log(Level.WARNING,
		 * "[MDBContainer] Got exception while trying " +
		 * " to get shutdown timeout", th); }
		 */
		try {
			boolean addedAsyncTask = false;
			if (timeout > 0) {
				try {
					ejbContainerUtilImpl.addWork(task);
					addedAsyncTask = true;
				} catch (Throwable th) {
					// Since we got an exception while trying to add the async
					// task
					// we will have to do the cleanup in the current thread
					// itself.
					addedAsyncTask = false;
					_logger
							.log(
									Level.WARNING,
									"[MDBContainer] Got exception while trying "
											+ "to add task to ContainerWorkPool. Will execute "
											+ "cleanupResources on current thread",
									th);
				}
			}

			if (addedAsyncTask) {
				synchronized (task) {
					if (!task.isDone()) {
						_logger.log(Level.FINE, "[MDBContainer] "
								+ "Going to wait for a maximum of " + timeout
								+ " seconds.");
						task.wait(timeout * 1000L);
					}

					if (!task.isDone()) {
						_logger.log(Level.WARNING,
								"[MDBContainer] ASync task has not finished. "
										+ "Giving up after " + timeout
										+ " seconds.");
					} else {
						_logger.log(Level.FINE,
								"[MDBContainer] ASync task has completed");
					}
				}
			} else {
				// Execute in the same thread
				_logger
						.log(Level.FINE,
								"[MDBContainer] Attempting to do cleanup()in current thread...");
				task.run();
				_logger.log(Level.WARNING,
						"[MDBContainer] Current thread done cleanup()... ");
			}
		} catch (InterruptedException inEx) {
			_logger.log(Level.SEVERE, "containers.mdb.cleanup_exception",
					new Object[] { appEJBName_, inEx.toString() });
		} catch (Exception ex) {
			_logger.log(Level.SEVERE, "containers.mdb.cleanup_exception",
					new Object[] { appEJBName_, ex.toString() });
		}
	}

	private static class ASyncClientShutdownTask implements Runnable {
		private boolean done = false;

		String appName;
		MessageBeanClient mdbClient;
		ClassLoader clsLoader;
		AbstractPool mdbPool;

		ASyncClientShutdownTask(String appName, MessageBeanClient mdbClient,
				ClassLoader loader, AbstractPool mdbPool) {
			this.appName = appName;
			this.mdbClient = mdbClient;
			this.clsLoader = loader;
			this.mdbPool = mdbPool;
		}

		public void run() {
			ClassLoader previousClassLoader = null;
			try {
				previousClassLoader = Utility.setContextClassLoader(clsLoader);
				// Cleanup the message bean client resources.
				mdbClient.close();
                _logger
                        .log(Level.FINE,
                                "[MDBContainer] ASync thread done with mdbClient.close()");
			} catch (Exception e) {
				_logger.log(Level.SEVERE, "containers.mdb.cleanup_exception",
						new Object[] { appName, e.toString() });
				_logger.log(Level.SEVERE, e.getClass().getName(), e);
			} finally {
                synchronized (this) {
                    this.done = true;
                    this.notify();
                }
				try {
					mdbPool.close();
				} catch (Exception ex) {
					_logger.log(Level.FINE, "Exception while closing pool", ex);
				}
				if (previousClassLoader != null) {
					Utility.setContextClassLoader(previousClassLoader);
				}
			}
		}

		public synchronized boolean isDone() {
			return this.done;
		}
	}

	/**
	 * Called by BaseContainer during container shutdown sequence
	 */
	protected void doConcreteContainerShutdown(boolean appBeingUndeployed) {
		_logger.log(Level.FINE, "containers.mdb.shutdown_cleanup_start",
				appEJBName_);
		monitorOn = false;
		cleanupResources();
		_logger.log(Level.FINE, "containers.mdb.shutdown_cleanup_end",
				appEJBName_);
	}

	/**
	 * Actual message delivery happens in three steps :
	 * 
	 * 1) beforeMessageDelivery(Message, MessageListener) This is our chance to
	 * make the message delivery itself part of the instance's global
	 * transaction.
	 * 
	 * 2) onMessage(Message, MessageListener) This is where the container
	 * delegates to the actual ejb instance's onMessage method.
	 * 
	 * 3) afterMessageDelivery(Message, MessageListener) Perform transaction
	 * cleanup and error handling.
	 * 
	 * We use the EjbInvocation manager's thread-specific state to track the
	 * invocation across these three calls.
	 * 
	 */

	public void beforeMessageDelivery(Method method, MessageDeliveryType deliveryType,
                                      boolean txImported, ResourceHandle resourceHandle) {

		if (containerState != CONTAINER_STARTED) { // i.e. no invocation
			String errorMsg = localStrings
					.getLocalString(
							"containers.mdb.invocation_closed",
							appEJBName_
									+ ": Message-driven bean invocation closed by container",
							new Object[] { appEJBName_ });

			throw new EJBException(errorMsg);
		}

		EjbInvocation invocation = createEjbInvocation();

		try {

			MessageBeanContextImpl context = (MessageBeanContextImpl) getContext(invocation);

            if( deliveryType == MessageDeliveryType.Timer ) {
                invocation.isTimerCallback = true;
            }
            
			// Set the context class loader here so that message producer will
			// have access to application class loader during message
			// processing.
			// The previous context class loader will be restored in
			// afterMessageDelivery.

			invocation.originalContextClassLoader = Utility
					.setContextClassLoader(getClassLoader());
			invocation.isMessageDriven = true;
			invocation.method = method;

			context.setState(BeanState.INVOKING);

			invocation.context = context;
			invocation.instance = context.getEJB();
			invocation.ejb = context.getEJB();
			invocation.container = this;

			// Message Bean Container only starts a new transaction if
			// there is no imported transaction and the message listener
			// method has tx attribute TX_REQUIRED or the ejbTimeout has
			// tx attribute TX_REQUIRES_NEW/TX_REQUIRED
			boolean startTx = false;
			if (!txImported) {
				startTx = containerStartsTx(method);
			}

			// keep track of whether tx was started for later.
			invocation.containerStartsTx = startTx;

			this.invocationManager.preInvoke(invocation);

			if (startTx) {
				// Register the session associated with the message-driven
				// bean's destination so the message delivery will be
				// part of the container-managed transaction.
				registerMessageBeanResource(resourceHandle);
			}

			preInvokeTx(invocation);

		} catch (Throwable c) {
			if (containerState != CONTAINER_STARTED) {
				_logger.log(Level.SEVERE, "containers.mdb.preinvoke_exception",
						new Object[] { appEJBName_, c.toString() });
				_logger.log(Level.SEVERE, c.getClass().getName(), c);
			}
			invocation.exception = c;
		}
	}

	public Object deliverMessage(Object[] params) throws Throwable {

		EjbInvocation invocation = null;
		boolean methodCalled = false; // for monitoring
		Object result = null;

		invocation = (EjbInvocation) invocationManager.getCurrentInvocation();

		if (invocation == null && _logger.isLoggable(Level.FINEST)) {
			if (containerState != CONTAINER_STARTED) {
				_logger.log(Level.FINEST, "No invocation in onMessage "
						+ " (container closing)");
			} else {
				_logger.log(Level.FINEST, "No invocation in onMessage : ");
			}
		}

		if ((invocation != null) && (invocation.exception == null)) {

			try {

				// NOTE : Application classloader already set in
				// beforeMessageDelivery

				methodCalled = true;
				if (ejbMethodStatsManager.isMethodMonitorOn()) {
					ejbMethodStatsManager.preInvoke(invocation.method);
				}

				if (isTimedObject()
						&& isEjbTimeoutMethod(invocation.method)) {
					invocation.beanMethod = invocation.method;
					intercept(invocation);
				} else {
					// invocation.beanMethod is the actual target method from
					// the bean class. The bean class is not required to be
					// a formal subtype of the message listener interface, so
					// we need to be careful to invoke through the bean class
					// method itself. This info is also returned from the
					// interceptor context info.

				        invocation.methodParams = params;

					invocation.beanMethod = invocation.ejb.getClass()
							.getMethod(invocation.method.getName(),
									invocation.method.getParameterTypes());

					result = super.intercept(invocation);
				}

			} catch (InvocationTargetException ite) {

				//
				// In EJB 2.1, message listener method signatures do not have
				// any restrictions on what kind of exceptions can be thrown.
				// This was not the case in J2EE 1.3, since JMS message driven
				// beans could only implement
				// void javax.jms.MessageListener.onMessage() , which does
				// not declare any exceptions.
				// 
				// In the J2EE 1.3 implementation, exceptions were only
				// propagated when the message driven bean was not configured
				// with CMT/Required transaction mode. This has been changed
				// due to the Connector 1.5 integration. Now, all exceptions
				// are propagated regardless of the tx mode. (18.2.2)
				// Application exceptions are propagated as is, while system
				// exceptions are wrapped in an EJBException.
				//
				// If an exception is thrown and there is a container-started
				// transaction, the semantics are the same as for other ejb
				// types whose business methods throw an exception.
				// Specifically, if the exception thrown is an Application
				// exception(defined in 18.2.1), it does not automatically
				// result in a rollback of the container-started transaction.
				// 

				Throwable cause = ite.getCause();
				// set cause on invocation , rather than the propagated
				// EJBException
				invocation.exception = cause;

				if (isSystemUncheckedException(cause)) {
					EJBException ejbEx = new EJBException(
							"message-driven bean method " + invocation.method
									+ " system exception");
					ejbEx.initCause(cause);
					cause = ejbEx;
				}
				throw cause;
			} catch (Throwable t) {
				EJBException ejbEx = new EJBException(
						"message-bean container dispatch error");
				ejbEx.initCause(t);
				invocation.exception = ejbEx;
				throw ejbEx;
			} finally {
				if (methodCalled && (ejbMethodStatsManager.isMethodMonitorOn())) {
					ejbMethodStatsManager.postInvoke(invocation.method,
							invocation.exception);
				}

				/*
				 * FIXME if ( AppVerification.doInstrument() ) {
				 * AppVerification.getInstrumentLogger().doInstrumentForEjb
				 * (getEjbDescriptor(), invocation.method,
				 * invocation.exception); }
				 */
			}

		} // End if -- invoke instance's onMessage method
		else {
			if (invocation == null) {
				String errorMsg = localStrings.getLocalString(
						"containers.mdb.invocation_closed", appEJBName_
								+ ": Message-driven bean invocation "
								+ "closed by container",
						new Object[] { appEJBName_ });
				throw new EJBException(errorMsg);
			} else {
				_logger.log(Level.SEVERE,
						"containers.mdb.invocation_exception", new Object[] {
								appEJBName_, invocation.exception.toString() });
				_logger.log(Level.SEVERE, invocation.exception.getClass()
						.getName(), invocation.exception);
				EJBException ejbEx = new EJBException();
				ejbEx.initCause(invocation.exception);
				throw ejbEx;
			}
		}

		return result;
	}

	public void afterMessageDelivery(ResourceHandle resourceHandle) {
		afterMessageDeliveryInternal(resourceHandle);
	}

	private boolean afterMessageDeliveryInternal(ResourceHandle resourceHandle) {
		// return value. assume failure until proven otherwise.
		boolean success = false;

		EjbInvocation invocation = null;

		invocation = (EjbInvocation) invocationManager.getCurrentInvocation();
		if (invocation == null) {
			_logger.log(Level.SEVERE, "containers.mdb.no_invocation",
					new Object[] { appEJBName_, "" });
		} else {
			MessageBeanContextImpl beanContext = (MessageBeanContextImpl) invocation.context;

			try {
				if (invocation.containerStartsTx) {
					// Unregister the session associated with
					// the message-driven bean's destination.
					unregisterMessageBeanResource(resourceHandle);
				}

				// counterpart of invocationManager.preInvoke
				invocationManager.postInvoke(invocation);

				// Commit/Rollback container-managed transaction.
				postInvokeTx(invocation);

				// Consider successful delivery. Commit failure will be
				// checked below.
				success = true;

				// TODO: Check if Tx existed / committed
                                ejbProbeNotifier.messageDeliveredEvent(getContainerId(),
                                        containerInfo.appName, containerInfo.modName,
                                        containerInfo.ejbName);

			} catch (Throwable ce) {
				_logger.log(Level.SEVERE,
						"containers.mdb.postinvoke_exception", new Object[] {
								appEJBName_, ce.toString() });
				_logger.log(Level.SEVERE, ce.getClass().getName(), ce);
			} finally {
				releaseContext(invocation);
			}

			// Reset original class loader
			Utility
					.setContextClassLoader(invocation.originalContextClassLoader);

			if (invocation.exception != null) {

				if (isSystemUncheckedException(invocation.exception)) {
					success = false;
				}

				// Log system exceptions by default and application exceptions
				// only when log level is FINE or higher.
				Level exLogLevel = isSystemUncheckedException(invocation.exception) ? Level.WARNING
						: Level.FINE;

				_logger.log(exLogLevel, "containers.mdb.invocation_exception",
						new Object[] { appEJBName_,
								invocation.exception.toString() });
				_logger.log(exLogLevel, invocation.exception.getClass()
						.getName(), invocation.exception);
			}
		}

		return success;
	}
	private boolean isEjbTimeoutMethod(Method m) {
		return schedules.containsKey(m) || m.equals(ejbTimeoutMethod);
	}

        // TODO : remove
	public long getMessageCount() {
		return statMessageCount;
	}

    public enum MessageDeliveryType { Message, Timer };

}
