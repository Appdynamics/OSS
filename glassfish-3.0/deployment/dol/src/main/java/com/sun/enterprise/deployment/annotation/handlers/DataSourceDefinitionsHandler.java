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
package com.sun.enterprise.deployment.annotation.handlers;

import org.jvnet.hk2.annotations.Service;
import org.glassfish.apf.HandlerProcessingResult;
import org.glassfish.apf.AnnotationInfo;
import org.glassfish.apf.AnnotationProcessorException;
import org.glassfish.apf.ResultType;
import org.glassfish.apf.impl.HandlerProcessingResultImpl;

import javax.annotation.sql.DataSourceDefinitions;
import javax.annotation.sql.DataSourceDefinition;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.HashSet;
import java.util.logging.Level;

import com.sun.enterprise.deployment.annotation.context.ResourceContainerContext;
import com.sun.enterprise.deployment.DataSourceDefinitionDescriptor;
import com.sun.enterprise.util.LocalStringManagerImpl;

/**
 * @author Jagadish Ramu
 */
@Service
public class DataSourceDefinitionsHandler extends AbstractResourceHandler {

    protected final static LocalStringManagerImpl localStrings =
            new LocalStringManagerImpl(AbstractHandler.class);
    
    public DataSourceDefinitionsHandler() {
    }

    /**
     * @return the annoation type this annotation handler is handling
     */
    public Class<? extends Annotation> getAnnotationType() {
        return DataSourceDefinitions.class;
    }

    protected HandlerProcessingResult processAnnotation(AnnotationInfo ainfo, ResourceContainerContext[] rcContexts)
            throws AnnotationProcessorException {
        DataSourceDefinitions defns = (DataSourceDefinitions) ainfo.getAnnotation();

        DataSourceDefinition values[] = defns.value();
        Set duplicates = new HashSet();
        if(values != null && values.length >0){
            for(DataSourceDefinition defn : values){
                String defnName = DataSourceDefinitionDescriptor.getJavaName(defn.name());

                if(duplicates.contains(defnName)){
                String localString = localStrings.getLocalString(
                        "enterprise.deployment.annotation.handlers.datasourcedefinitionsduplicates",
                        "@DataSourceDefinitions cannot have multiple definitions with same name : '{0}'",
                        defnName);
                    throw new IllegalStateException(localString);
                    /*
                    //TODO V3 should we throw exception or return failure result ?
                    return getFailureResult(ainfo, "@DataSourceDefinitions cannot have multiple" +
                            " definitions with same name [ "+defnName+" ]", true );
                    */
                }else{
                    duplicates.add(defnName);
                }
                DataSourceDefinitionHandler handler = new DataSourceDefinitionHandler();
                handler.processAnnotation(defn, ainfo, rcContexts);
            }
            duplicates.clear();
        }
        return getDefaultProcessedResult();
    }

    private HandlerProcessingResultImpl getFailureResult(AnnotationInfo element, String message, boolean doLog) {
        HandlerProcessingResultImpl result = new HandlerProcessingResultImpl();
        result.addResult(getAnnotationType(), ResultType.FAILED);
        if (doLog) {
            Class c = (Class) element.getAnnotatedElement();
            String className = c.getName();
            String localString = localStrings.getLocalString(
                    "enterprise.deployment.annotation.handlers.datasourcedefinitionsfailure",
                    "failed to handle annotation [ {0} ] on class [ {1} ] due to the following exception : ",
                    element.getAnnotation(), className);
            logger.log(Level.WARNING, localString, message);
        }
        return result;
    }

    public Class<? extends Annotation>[] getTypeDependencies() {
        return getEjbAndWebAnnotationTypes();
    }
}
