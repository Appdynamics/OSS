/*
 * Copyright (c) 2014, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.hk2.xml.lifecycle.config;

//import javax.validation.Payload;
//import javax.validation.constraints.NotNull;

// import com.oracle.weblogic.lifecycle.config.validators.ReferenceConstraint;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.glassfish.hk2.xml.api.annotations.XmlIdentifier;

import java.beans.PropertyVetoException;

// @ReferenceConstraint(skipDuringCreation=false, payload=Service.class)
public interface Service extends Payload, Auditable {

  @XmlAttribute(required=true /*, key=true */)
  // @NotNull
  String getId();
  void setId(String id) throws PropertyVetoException;

  @XmlAttribute(required=true /*, key=false */)
  @XmlIdentifier
  // @NotNull
  String getName();
  void setName(String value) throws PropertyVetoException;

  @XmlAttribute
  // @ReferenceConstraint.RemoteKey(message="{resourceref.invalid.configref}", type=Environment.class)
  String getEnvironmentRef();
  void setEnvironmentRef(String envName);
  
  @XmlAttribute
  String getServiceType();
  void setServiceType(String serviceType);

  @XmlAttribute
  String getIdentityDomain();
  void setIdentityDomain(String identityDomain);

  @XmlElement
  PDB getPdb();
  void setPdb(PDB pdb);
  
  /*
  @DuckTyped
  PDB createPDB(String name, String id, String status);
   
  @DuckTyped
  PDB deletePDB(PDB pdb);
  
  @DuckTyped
  PDB deletePDB(String pdbName);

  @DuckTyped
  Resources createResourcesIfNotFound();

  @Element
  Resources getResources();
  void setResources(Resources resources);

  @DuckTyped
  Environment getEnvironment();

  @DuckTyped
  Tenant getTenant();

  class Duck {

    public static PDB createPDB(final Service service, final String name, final String id, final String status)
        throws TransactionFailure {
      ConfigSupport.apply(new SingleConfigCode<Service>() {
        @Override
        public Object run(Service writeableService) throws TransactionFailure, PropertyVetoException {
          PDB pdb = writeableService.createChild(PDB.class);
          pdb.setName(name);
          pdb.setId(id);
          pdb.setPdbStatus(status);
          writeableService.setPdb(pdb);
          return pdb;
        }
      }, service);

      // read-only view
      return service.getPdb();
    }

    public static PDB deletePDB(final Service service, final PDB pdb) throws TransactionFailure {
      return (PDB) ConfigSupport.apply(new SingleConfigCode<Service>() {
        @Override
        public Object run(Service writeableService) throws TransactionFailure {
          writeableService.setPdb(null);
          return pdb;
        }
      }, service);
    }

    public static Resources createResourcesIfNotFound(final Service service) throws TransactionFailure{


        return (Resources)ConfigSupport.apply(new SingleConfigCode<Service>() {
          @Override
          public Object run(Service writeableService) throws TransactionFailure, PropertyVetoException {
              if(writeableService.getResources() == null){
                  Resources resources = writeableService.createChild(Resources.class);
                  writeableService.setResources(resources);
              }
            return writeableService.getResources();
          }
        }, service);

    }

    public static Environment getEnvironment(final Service service) throws TransactionFailure {
      LifecycleConfigBean bean = (LifecycleConfigBean) Dom.unwrap(service);
      ServiceLocator serviceLocator = bean.getHabitat();
      return serviceLocator.getService(Environment.class, service.getEnvironmentRef());
    }

    public static Tenant getTenant(final Service service) throws TransactionFailure {
      return service.getParent(Tenant.class);
    }
  }
  */
}
