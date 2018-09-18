/*************************************************************************
 * 
 * ADOBE CONFIDENTIAL
 * __________________
 * 
 *  [2002] - [2007] Adobe Systems Incorporated 
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 */
package flex.management.runtime.messaging.log;

import flex.management.BaseControlMBean;


/**
 * Defines the exposed properties and operations of the LogControl
 * 
 * @author majacobs
 *
 */
public interface LogControlMBean extends BaseControlMBean
{       
    public String[] getTargets();
    public String[] getTargetFilters(String targetId);
    public String[] getCategories();
    
    public Integer getTargetLevel(String searchId);
    public void changeTargetLevel(String searchId, String level);
    
    public void addFilterForTarget(String filter, String targetId);
    public void removeFilterForTarget(String filter, String targetId);
    
}
