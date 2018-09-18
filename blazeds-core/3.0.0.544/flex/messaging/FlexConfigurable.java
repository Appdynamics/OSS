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
package flex.messaging;

import flex.messaging.config.ConfigMap;

/**
 * Components created in the Flex configuration environment can implement
 * the FlexConfigurable interface to get access to the configuration
 * properties like a regular component in the system.
 */
public interface FlexConfigurable
{
    /**
     * Initializes the component with configuration information.  The id parameter
     * contains an identity you can use in diagnostic messages to determine which
     * component's configuration this is.  The configMap parameter contains the
     * properties for configuring your component.
     */
    void initialize(String id, ConfigMap configMap);
}
