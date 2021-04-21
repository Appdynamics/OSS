/*
 * Copyright 2021 Ping Identity Corporation
 * All Rights Reserved.
 */
/*
 * Copyright 2020-2021 Ping Identity Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Copyright (C) 2021 Ping Identity Corporation
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (GPLv2 only)
 * or the terms of the GNU Lesser General Public License (LGPLv2.1 only)
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 */
package com.unboundid.ldap.sdk.experimental;



import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;



/**
 * This enum defines a set of message keys for messages in the
 * com.unboundid.ldap.sdk.experimental package, which correspond to messages in the
 * unboundid-ldapsdk-experimental.properties properties file.
 * <BR><BR>
 * This source file was generated from the properties file.
 * Do not edit it directly.
 */
enum ExperimentalMessages
{
  /**
   * The provided control cannot be decoded as a DirSync control because an error was encountered while attempting to parse the control value:  {0}
   */
  ERR_DIRSYNC_CONTROL_DECODE_ERROR("The provided control cannot be decoded as a DirSync control because an error was encountered while attempting to parse the control value:  {0}"),



  /**
   * The provided control cannot be decoded as a DirSync control because it does not have a value.
   */
  ERR_DIRSYNC_CONTROL_NO_VALUE("The provided control cannot be decoded as a DirSync control because it does not have a value."),



  /**
   * Unable to decode the provided entry ''{0}'' as an abandon access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be decoded as a valid integer.
   */
  ERR_LOGSCHEMA_DECODE_ABANDON_ID_ERROR("Unable to decode the provided entry ''{0}'' as an abandon access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be decoded as a valid integer."),



  /**
   * Unable to decode the provided entry ''{0}'' as an add access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid attribute change descriptor because it did not include an attribute name before the colon.
   */
  ERR_LOGSCHEMA_DECODE_ADD_CHANGE_MISSING_ATTR("Unable to decode the provided entry ''{0}'' as an add access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid attribute change descriptor because it did not include an attribute name before the colon."),



  /**
   * Unable to decode the provided entry ''{0}'' as an add access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid attribute change descriptor because it did not include a colon to separate the attribute name from the change type.
   */
  ERR_LOGSCHEMA_DECODE_ADD_CHANGE_MISSING_COLON("Unable to decode the provided entry ''{0}'' as an add access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid attribute change descriptor because it did not include a colon to separate the attribute name from the change type."),



  /**
   * Unable to decode the provided entry ''{0}'' as an add access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid attribute change descriptor because the character immediately after the plus sign was not a space to separate the change type from the value.
   */
  ERR_LOGSCHEMA_DECODE_ADD_CHANGE_NO_SPACE_AFTER_PLUS("Unable to decode the provided entry ''{0}'' as an add access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid attribute change descriptor because the character immediately after the plus sign was not a space to separate the change type from the value."),



  /**
   * Unable to decode the provided entry ''{0}'' as an add access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid attribute change descriptor because the character immediately after the colon was not a plus sign to indicate that the value was being added to the entry.
   */
  ERR_LOGSCHEMA_DECODE_ADD_CHANGE_TYPE_NOT_PLUS("Unable to decode the provided entry ''{0}'' as an add access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid attribute change descriptor because the character immediately after the colon was not a plus sign to indicate that the value was being added to the entry."),



  /**
   * Unable to decode the provided entry ''{0}'' as a bind access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be decoded as a valid integer.
   */
  ERR_LOGSCHEMA_DECODE_BIND_VERSION_ERROR("Unable to decode the provided entry ''{0}'' as a bind access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be decoded as a valid integer."),



  /**
   * Unable to decode the provided entry ''{0}'' as an access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be decoded as a valid generalized time.
   */
  ERR_LOGSCHEMA_DECODE_CANNOT_DECODE_TIME("Unable to decode the provided entry ''{0}'' as an access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be decoded as a valid generalized time."),



  /**
   * Unable to decode the provided entry ''{0}'' as a compare access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value could not be decoded as a valid attribute value assertion.
   */
  ERR_LOGSCHEMA_DECODE_COMPARE_AVA_ERROR("Unable to decode the provided entry ''{0}'' as a compare access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value could not be decoded as a valid attribute value assertion."),



  /**
   * Unable to decode the provided entry ''{0}'' as an access log entry as per the draft-chu-ldap-logschema-00 specification because an error occurred while attempting to decode an LDAP control contained in the {1} attribute:  {2}.
   */
  ERR_LOGSCHEMA_DECODE_CONTROL_ERROR("Unable to decode the provided entry ''{0}'' as an access log entry as per the draft-chu-ldap-logschema-00 specification because an error occurred while attempting to decode an LDAP control contained in the {1} attribute:  {2}."),



  /**
   * Unable to decode the provided entry ''{0}'' as a delete access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid old attribute descriptor because it did not include an attribute name before the colon.
   */
  ERR_LOGSCHEMA_DECODE_DELETE_OLD_ATTR_MISSING_ATTR("Unable to decode the provided entry ''{0}'' as a delete access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid old attribute descriptor because it did not include an attribute name before the colon."),



  /**
   * Unable to decode the provided entry ''{0}'' as a delete access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid old attribute descriptor because it did not include a colon to separate the attribute name from its value.
   */
  ERR_LOGSCHEMA_DECODE_DELETE_OLD_ATTR_MISSING_COLON("Unable to decode the provided entry ''{0}'' as a delete access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid old attribute descriptor because it did not include a colon to separate the attribute name from its value."),



  /**
   * Unable to decode the provided entry ''{0}'' as a delete access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid old attribute descriptor because it did not include a space immediately after the colon used to separate the attribute name from its value.
   */
  ERR_LOGSCHEMA_DECODE_DELETE_OLD_ATTR_MISSING_SPACE("Unable to decode the provided entry ''{0}'' as a delete access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid old attribute descriptor because it did not include a space immediately after the colon used to separate the attribute name from its value."),



  /**
   * Unable to decode the provided entry ''{0}'' as an extended access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' was not in the expected form for an extended operation.  For an extended operation, the {1} attribute must be the string ''extended'' immediately followed by the OID of the extended request.
   */
  ERR_LOGSCHEMA_DECODE_EXTENDED_MALFORMED_REQ_TYPE("Unable to decode the provided entry ''{0}'' as an extended access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' was not in the expected form for an extended operation.  For an extended operation, the {1} attribute must be the string ''extended'' immediately followed by the OID of the extended request."),



  /**
   * Unable to decode the provided entry ''{0}'' as an access log entry as per the draft-chu-ldap-logschema-00 specification because the provided entry was missing the required {1} attribute.
   */
  ERR_LOGSCHEMA_DECODE_MISSING_REQUIRED_ATTR("Unable to decode the provided entry ''{0}'' as an access log entry as per the draft-chu-ldap-logschema-00 specification because the provided entry was missing the required {1} attribute."),



  /**
   * Unable to decode the provided entry ''{0}'' as a modify access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid attribute change descriptor because it had an invalid change type indicator immediately after the colon.  The change type indicator must be one of +, -, =, or #.
   */
  ERR_LOGSCHEMA_DECODE_MODIFY_CHANGE_INVALID_CHANGE_TYPE("Unable to decode the provided entry ''{0}'' as a modify access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid attribute change descriptor because it had an invalid change type indicator immediately after the colon.  The change type indicator must be one of +, -, =, or #."),



  /**
   * Unable to decode the provided entry ''{0}'' as a modify access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid attribute change descriptor because it did not include an attribute name before the colon.
   */
  ERR_LOGSCHEMA_DECODE_MODIFY_CHANGE_MISSING_ATTR("Unable to decode the provided entry ''{0}'' as a modify access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid attribute change descriptor because it did not include an attribute name before the colon."),



  /**
   * Unable to decode the provided entry ''{0}'' as a modify access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid attribute change descriptor because it did not include a change type indicator (+, -, =, or #) immediately after the colon.
   */
  ERR_LOGSCHEMA_DECODE_MODIFY_CHANGE_MISSING_CHANGE_TYPE("Unable to decode the provided entry ''{0}'' as a modify access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid attribute change descriptor because it did not include a change type indicator (+, -, =, or #) immediately after the colon."),



  /**
   * Unable to decode the provided entry ''{0}'' as a modify access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid attribute change descriptor because it did not include a colon to separate the attribute name from the change type.
   */
  ERR_LOGSCHEMA_DECODE_MODIFY_CHANGE_MISSING_COLON("Unable to decode the provided entry ''{0}'' as a modify access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid attribute change descriptor because it did not include a colon to separate the attribute name from the change type."),



  /**
   * Unable to decode the provided entry ''{0}'' as a modify access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid attribute change descriptor because it did not include a space between the change type indicator and the value.
   */
  ERR_LOGSCHEMA_DECODE_MODIFY_CHANGE_MISSING_SPACE("Unable to decode the provided entry ''{0}'' as a modify access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid attribute change descriptor because it did not include a space between the change type indicator and the value."),



  /**
   * Unable to decode the provided entry ''{0}'' as a modify access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid attribute change descriptor because a change type of {3} requires an attribute value but none was provided.
   */
  ERR_LOGSCHEMA_DECODE_MODIFY_CHANGE_MISSING_VALUE("Unable to decode the provided entry ''{0}'' as a modify access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid attribute change descriptor because a change type of {3} requires an attribute value but none was provided."),



  /**
   * Unable to decode the provided entry ''{0}'' as a modify DN access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' was invalid.  The value must be either ''TRUE'' or ''FALSE''.
   */
  ERR_LOGSCHEMA_DECODE_MODIFY_DN_DELETE_OLD_RDN_ERROR("Unable to decode the provided entry ''{0}'' as a modify DN access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' was invalid.  The value must be either ''TRUE'' or ''FALSE''."),



  /**
   * Unable to decode the provided entry ''{0}'' as a modify access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid old attribute descriptor because it did not include an attribute name before the colon.
   */
  ERR_LOGSCHEMA_DECODE_MODIFY_OLD_ATTR_MISSING_ATTR("Unable to decode the provided entry ''{0}'' as a modify access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid old attribute descriptor because it did not include an attribute name before the colon."),



  /**
   * Unable to decode the provided entry ''{0}'' as a modify access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid old attribute descriptor because it did not include a colon to separate the attribute name from its value.
   */
  ERR_LOGSCHEMA_DECODE_MODIFY_OLD_ATTR_MISSING_COLON("Unable to decode the provided entry ''{0}'' as a modify access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid old attribute descriptor because it did not include a colon to separate the attribute name from its value."),



  /**
   * Unable to decode the provided entry ''{0}'' as a modify access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid old attribute descriptor because it did not include a space immediately after the colon used to separate the attribute name from its value.
   */
  ERR_LOGSCHEMA_DECODE_MODIFY_OLD_ATTR_MISSING_SPACE("Unable to decode the provided entry ''{0}'' as a modify access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid old attribute descriptor because it did not include a space immediately after the colon used to separate the attribute name from its value."),



  /**
   * Unable to decode the provided entry ''{0}'' as an access log entry as per the draft-chu-ldap-logschema-00 specification because the provided entry was missing the required {1} attribute to indicate the operation type.
   */
  ERR_LOGSCHEMA_DECODE_NO_OP_TYPE("Unable to decode the provided entry ''{0}'' as an access log entry as per the draft-chu-ldap-logschema-00 specification because the provided entry was missing the required {1} attribute to indicate the operation type."),



  /**
   * Unable to decode the provided entry ''{0}'' as an access log entry as per the draft-chu-ldap-logschema-00 specification because value ''{1}'' of attribute {2} could not be parsed as an integer.
   */
  ERR_LOGSCHEMA_DECODE_RESULT_CODE_ERROR("Unable to decode the provided entry ''{0}'' as an access log entry as per the draft-chu-ldap-logschema-00 specification because value ''{1}'' of attribute {2} could not be parsed as an integer."),



  /**
   * Unable to decode the provided entry ''{0}'' as a search access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' was invalid.  The value must be one of ''never'', ''searching'', ''finding'', or ''always''.
   */
  ERR_LOGSCHEMA_DECODE_SEARCH_DEREF_ERROR("Unable to decode the provided entry ''{0}'' as a search access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' was invalid.  The value must be one of ''never'', ''searching'', ''finding'', or ''always''."),



  /**
   * Unable to decode the provided entry ''{0}'' as a search access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid LDAP search filter.
   */
  ERR_LOGSCHEMA_DECODE_SEARCH_FILTER_ERROR("Unable to decode the provided entry ''{0}'' as a search access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid LDAP search filter."),



  /**
   * Unable to decode the provided entry ''{0}'' as a search access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid integer.
   */
  ERR_LOGSCHEMA_DECODE_SEARCH_INT_ERROR("Unable to decode the provided entry ''{0}'' as a search access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' could not be parsed as a valid integer."),



  /**
   * Unable to decode the provided entry ''{0}'' as a search access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' was invalid.  The value must be one of ''base'', ''one'', ''sub'', or ''subord''.
   */
  ERR_LOGSCHEMA_DECODE_SEARCH_SCOPE_ERROR("Unable to decode the provided entry ''{0}'' as a search access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' was invalid.  The value must be one of ''base'', ''one'', ''sub'', or ''subord''."),



  /**
   * Unable to decode the provided entry ''{0}'' as a search access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' was invalid.  The value must be either ''TRUE'' or ''FALSE''.
   */
  ERR_LOGSCHEMA_DECODE_SEARCH_TYPES_ONLY_ERROR("Unable to decode the provided entry ''{0}'' as a search access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} value ''{2}'' was invalid.  The value must be either ''TRUE'' or ''FALSE''."),



  /**
   * Unable to decode the provided entry ''{0}'' as an access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} attribute had a value of ''{2}'' that does not represent a recognized operation type.
   */
  ERR_LOGSCHEMA_DECODE_UNRECOGNIZED_OP_TYPE("Unable to decode the provided entry ''{0}'' as an access log entry as per the draft-chu-ldap-logschema-00 specification because the {1} attribute had a value of ''{2}'' that does not represent a recognized operation type."),



  /**
   * The provided control cannot be decoded as a no-op request control because it has a value.
   */
  ERR_NOOP_REQUEST_HAS_VALUE("The provided control cannot be decoded as a no-op request control because it has a value."),



  /**
   * The provided control cannot be decoded as a password policy request control because it has a value.
   */
  ERR_PWP_REQUEST_HAS_VALUE("The provided control cannot be decoded as a password policy request control because it has a value."),



  /**
   * The provided control cannot be decoded as a password policy response control because the error type element could not be decoded:  {0}
   */
  ERR_PWP_RESPONSE_CANNOT_DECODE_ERROR("The provided control cannot be decoded as a password policy response control because the error type element could not be decoded:  {0}"),



  /**
   * The provided control cannot be decoded as a password policy response control because the warning type element could not be decoded:  {0}
   */
  ERR_PWP_RESPONSE_CANNOT_DECODE_WARNING("The provided control cannot be decoded as a password policy response control because the warning type element could not be decoded:  {0}"),



  /**
   * The provided control cannot be decoded as a password policy response control because there were too many elements in the value sequence (expected between 0 and 2, got {0,number,0}).
   */
  ERR_PWP_RESPONSE_INVALID_ELEMENT_COUNT("The provided control cannot be decoded as a password policy response control because there were too many elements in the value sequence (expected between 0 and 2, got {0,number,0})."),



  /**
   * The provided control cannot be decoded as a password policy response control because it had an invalid error type ({0}).
   */
  ERR_PWP_RESPONSE_INVALID_ERROR_TYPE("The provided control cannot be decoded as a password policy response control because it had an invalid error type ({0})."),



  /**
   * The provided control cannot be decoded as a password policy response control because the value sequence contained an element with an invalid type ({0}).
   */
  ERR_PWP_RESPONSE_INVALID_TYPE("The provided control cannot be decoded as a password policy response control because the value sequence contained an element with an invalid type ({0})."),



  /**
   * The provided control cannot be decoded as a password policy response control because the warning type element had an invalid type ({0}).
   */
  ERR_PWP_RESPONSE_INVALID_WARNING_TYPE("The provided control cannot be decoded as a password policy response control because the warning type element had an invalid type ({0})."),



  /**
   * The provided control cannot be decoded as a password policy response control because the value sequence contained multiple error elements.
   */
  ERR_PWP_RESPONSE_MULTIPLE_ERROR("The provided control cannot be decoded as a password policy response control because the value sequence contained multiple error elements."),



  /**
   * The provided control cannot be decoded as a password policy response control because the value sequence contained multiple warning elements.
   */
  ERR_PWP_RESPONSE_MULTIPLE_WARNING("The provided control cannot be decoded as a password policy response control because the value sequence contained multiple warning elements."),



  /**
   * The provided control cannot be decoded as a password policy response control because it does not have a value.
   */
  ERR_PWP_RESPONSE_NO_VALUE("The provided control cannot be decoded as a password policy response control because it does not have a value."),



  /**
   * The provided control cannot be decoded as a password policy response control because the control value could not be decoded as a sequence:  {0}
   */
  ERR_PWP_RESPONSE_VALUE_NOT_SEQUENCE("The provided control cannot be decoded as a password policy response control because the control value could not be decoded as a sequence:  {0}"),



  /**
   * Active Directory DirSync Control
   */
  INFO_CONTROL_NAME_DIRSYNC("Active Directory DirSync Control"),



  /**
   * No-Op Request Control
   */
  INFO_CONTROL_NAME_NOOP_REQUEST("No-Op Request Control"),



  /**
   * Password Policy Request Control
   */
  INFO_CONTROL_NAME_PW_POLICY_REQUEST("Password Policy Request Control"),



  /**
   * Password Policy Response Control
   */
  INFO_CONTROL_NAME_PW_POLICY_RESPONSE("Password Policy Response Control");



  /**
   * Indicates whether the unit tests are currently running.
   */
  private static final boolean IS_WITHIN_UNIT_TESTS =
       Boolean.getBoolean("com.unboundid.ldap.sdk.RunningUnitTests") ||
       Boolean.getBoolean("com.unboundid.directory.server.RunningUnitTests");



  /**
   * A pre-allocated array of zero objects to use for messages
   * that do not require any arguments.
   */
  private static final Object[] NO_ARGS = new Object[0];



  /**
   * The resource bundle that will be used to load the properties file.
   */
  private static final ResourceBundle RESOURCE_BUNDLE;
  static
  {
    ResourceBundle rb = null;
    try
    {
      rb = ResourceBundle.getBundle("unboundid-ldapsdk-experimental");
    } catch (final Exception e) {}
    RESOURCE_BUNDLE = rb;
  }



  /**
   * The map that will be used to hold the unformatted message strings, indexed by property name.
   */
  private static final ConcurrentHashMap<ExperimentalMessages,String> MESSAGE_STRINGS = new ConcurrentHashMap<>(100);



  /**
   * The map that will be used to hold the message format objects, indexed by property name.
   */
  private static final ConcurrentHashMap<ExperimentalMessages,MessageFormat> MESSAGES = new ConcurrentHashMap<>(100);



  // The default text for this message
  private final String defaultText;



  /**
   * Creates a new message key.
   */
  private ExperimentalMessages(final String defaultText)
  {
    this.defaultText = defaultText;
  }



  /**
   * Retrieves a localized version of the message.
   * This method should only be used for messages that do not take any
   * arguments.
   *
   * @return  A localized version of the message.
   */
  public String get()
  {
    MessageFormat f = MESSAGES.get(this);
    if (f == null)
    {
      if (RESOURCE_BUNDLE == null)
      {
        f = new MessageFormat(defaultText);
      }
      else
      {
        try
        {
          f = new MessageFormat(RESOURCE_BUNDLE.getString(name()));
        }
        catch (final Exception e)
        {
          f = new MessageFormat(defaultText);
        }
      }
      MESSAGES.putIfAbsent(this, f);
    }

    final String formattedMessage;
    synchronized (f)
    {
      formattedMessage = f.format(NO_ARGS);
    }

    if (IS_WITHIN_UNIT_TESTS)
    {
      if (formattedMessage.contains("{0}") ||
          formattedMessage.contains("{0,number,0}") ||
          formattedMessage.contains("{1}") ||
          formattedMessage.contains("{1,number,0}") ||
          formattedMessage.contains("{2}") ||
          formattedMessage.contains("{2,number,0}") ||
          formattedMessage.contains("{3}") ||
          formattedMessage.contains("{3,number,0}") ||
          formattedMessage.contains("{4}") ||
          formattedMessage.contains("{4,number,0}") ||
          formattedMessage.contains("{5}") ||
          formattedMessage.contains("{5,number,0}") ||
          formattedMessage.contains("{6}") ||
          formattedMessage.contains("{6,number,0}") ||
          formattedMessage.contains("{7}") ||
          formattedMessage.contains("{7,number,0}") ||
          formattedMessage.contains("{8}") ||
          formattedMessage.contains("{8,number,0}") ||
          formattedMessage.contains("{9}") ||
          formattedMessage.contains("{9,number,0}") ||
          formattedMessage.contains("{10}") ||
          formattedMessage.contains("{10,number,0}"))
      {
        throw new IllegalArgumentException(
             "Message " + getClass().getName() + '.' + name() +
                  " contains an un-replaced token:  " + formattedMessage);
      }
    }

    return formattedMessage;
  }



  /**
   * Retrieves a localized version of the message.
   *
   * @param  args  The arguments to use to format the message.
   *
   * @return  A localized version of the message.
   */
  public String get(final Object... args)
  {
    MessageFormat f = MESSAGES.get(this);
    if (f == null)
    {
      if (RESOURCE_BUNDLE == null)
      {
        f = new MessageFormat(defaultText);
      }
      else
      {
        try
        {
          f = new MessageFormat(RESOURCE_BUNDLE.getString(name()));
        }
        catch (final Exception e)
        {
          f = new MessageFormat(defaultText);
        }
      }
      MESSAGES.putIfAbsent(this, f);
    }

    final String formattedMessage;
    synchronized (f)
    {
      formattedMessage = f.format(args);
    }

    if (IS_WITHIN_UNIT_TESTS)
    {
      if (formattedMessage.contains("{0}") ||
          formattedMessage.contains("{0,number,0}") ||
          formattedMessage.contains("{1}") ||
          formattedMessage.contains("{1,number,0}") ||
          formattedMessage.contains("{2}") ||
          formattedMessage.contains("{2,number,0}") ||
          formattedMessage.contains("{3}") ||
          formattedMessage.contains("{3,number,0}") ||
          formattedMessage.contains("{4}") ||
          formattedMessage.contains("{4,number,0}") ||
          formattedMessage.contains("{5}") ||
          formattedMessage.contains("{5,number,0}") ||
          formattedMessage.contains("{6}") ||
          formattedMessage.contains("{6,number,0}") ||
          formattedMessage.contains("{7}") ||
          formattedMessage.contains("{7,number,0}") ||
          formattedMessage.contains("{8}") ||
          formattedMessage.contains("{8,number,0}") ||
          formattedMessage.contains("{9}") ||
          formattedMessage.contains("{9,number,0}") ||
          formattedMessage.contains("{10}") ||
          formattedMessage.contains("{10,number,0}"))
      {
        throw new IllegalArgumentException(
             "Message " + getClass().getName() + '.' + name() +
                  " contains an un-replaced token:  " + formattedMessage);
      }
    }

    return formattedMessage;
  }



  /**
   * Retrieves a string representation of this message key.
   *
   * @return  A string representation of this message key.
   */
  @Override()
  public String toString()
  {
    String s = MESSAGE_STRINGS.get(this);
    if (s == null)
    {
      if (RESOURCE_BUNDLE == null)
      {
        s = defaultText;
      }
      else
      {
        try
        {
          s = RESOURCE_BUNDLE.getString(name());
        }
        catch (final Exception e)
        {
          s = defaultText;
        }
        MESSAGE_STRINGS.putIfAbsent(this, s);
      }
    }

    return s;
  }
}

