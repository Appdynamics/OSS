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

package org.glassfish.apf.impl;

import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.AnnotatedElement;

import org.glassfish.apf.ProcessingResult;
import org.glassfish.apf.ResultType;
import org.glassfish.apf.HandlerProcessingResult;

/**
 * Implementation of the ProcessingResult interface
 *
 * @author Jerome Dochez
 */
public class ProcessingResultImpl implements ProcessingResult {
    
    Map<AnnotatedElement, HandlerProcessingResult> results;
    ResultType overallResult = ResultType.UNPROCESSED;
    
    /** Creates a new instance of ProcessingResultImpl */
    public ProcessingResultImpl() {
        results = new HashMap<AnnotatedElement, HandlerProcessingResult>();
    }
    
    public void add(ProcessingResult pr) {
        
        Map<AnnotatedElement, HandlerProcessingResult> results = pr.getResults();
        for (AnnotatedElement element : results.keySet()) {
            add(element, results.get(element));
        }
    }
    
    public void add(AnnotatedElement element, HandlerProcessingResult elementResult) {

        if (elementResult.getOverallResult().compareTo(overallResult)>0) {
            overallResult = elementResult.getOverallResult();
        }
        if (results.containsKey(element)) {
            HandlerProcessingResultImpl previousResult = (HandlerProcessingResultImpl) results.get(element);
            previousResult.addAll(elementResult);
        } else {
            if (elementResult instanceof HandlerProcessingResultImpl) {
                results.put(element, elementResult);
            } else {
                HandlerProcessingResultImpl result = new HandlerProcessingResultImpl();
                result.addAll(elementResult);
                results.put(element, result);
            }
        }
    }
    
    public Map<AnnotatedElement,HandlerProcessingResult> getResults() {
        return results;
    }
    
    public ResultType getOverallResult(){
        return overallResult;
    }
}
