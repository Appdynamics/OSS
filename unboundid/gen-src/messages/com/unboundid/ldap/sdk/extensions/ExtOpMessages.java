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
package com.unboundid.ldap.sdk.extensions;



import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;



/**
 * This enum defines a set of message keys for messages in the
 * com.unboundid.ldap.sdk.extensions package, which correspond to messages in the
 * unboundid-ldapsdk-extop.properties properties file.
 * <BR><BR>
 * This source file was generated from the properties file.
 * Do not edit it directly.
 */
enum ExtOpMessages
{
  /**
   * Unable to decode the provided generic extended result as an aborted transaction result because it did not have a value.
   */
  ERR_ABORTED_TXN_NO_VALUE("Unable to decode the provided generic extended result as an aborted transaction result because it did not have a value."),



  /**
   * Cancel operations are not supported on connections operating in synchronous mode
   */
  ERR_CANCEL_NOT_SUPPORTED_IN_SYNCHRONOUS_MODE("Cancel operations are not supported on connections operating in synchronous mode"),



  /**
   * The provided extended request cannot be decoded as a cancel request because an error occurred while attempting to parse the value:  {0}
   */
  ERR_CANCEL_REQUEST_CANNOT_DECODE("The provided extended request cannot be decoded as a cancel request because an error occurred while attempting to parse the value:  {0}"),



  /**
   * The provided extended request cannot be decoded as a cancel request because it does not have a value.
   */
  ERR_CANCEL_REQUEST_NO_VALUE("The provided extended request cannot be decoded as a cancel request because it does not have a value."),



  /**
   * The provided extended request cannot be decoded as an end transaction request because an error occurred while attempting to parse the value:  {0}
   */
  ERR_END_TXN_REQUEST_CANNOT_DECODE("The provided extended request cannot be decoded as an end transaction request because an error occurred while attempting to parse the value:  {0}"),



  /**
   * The provided extended request cannot be decoded as an end transaction request because it does not have a value.
   */
  ERR_END_TXN_REQUEST_NO_VALUE("The provided extended request cannot be decoded as an end transaction request because it does not have a value."),



  /**
   * Unable to decode the message ID from the end transaction value sequence:  {0}
   */
  ERR_END_TXN_RESPONSE_CANNOT_DECODE_MSGID("Unable to decode the message ID from the end transaction value sequence:  {0}"),



  /**
   * Unable to decode the controls element of an updateControls sequence as an ASN.1 sequence:  {0}
   */
  ERR_END_TXN_RESPONSE_CONTROLS_ELEMENT_NOT_SEQUENCE("Unable to decode the controls element of an updateControls sequence as an ASN.1 sequence:  {0}"),



  /**
   * Unable to decode the updatesControls element in the end transaction value sequence as an ASN.1 sequence:  {0}
   */
  ERR_END_TXN_RESPONSE_CONTROLS_NOT_SEQUENCE("Unable to decode the updatesControls element in the end transaction value sequence as an ASN.1 sequence:  {0}"),



  /**
   * Invalid number of elements in an updateControls element sequence (expected 2, got {0,number,0}).
   */
  ERR_END_TXN_RESPONSE_CONTROL_INVALID_ELEMENT_COUNT("Invalid number of elements in an updateControls element sequence (expected 2, got {0,number,0})."),



  /**
   * Unable to decode the message ID element of an updateControls sequence as an integer:  {0}
   */
  ERR_END_TXN_RESPONSE_CONTROL_MSGID_NOT_INT("Unable to decode the message ID element of an updateControls sequence as an integer:  {0}"),



  /**
   * Unable to decode an updateControls sequence element in the end transaction value as an ASN.1 sequence:  {0}
   */
  ERR_END_TXN_RESPONSE_CONTROL_NOT_SEQUENCE("Unable to decode an updateControls sequence element in the end transaction value as an ASN.1 sequence:  {0}"),



  /**
   * Too many elements in the end transaction value sequence (expected 1 or 2, got {0,number,0}).
   */
  ERR_END_TXN_RESPONSE_INVALID_ELEMENT_COUNT("Too many elements in the end transaction value sequence (expected 1 or 2, got {0,number,0})."),



  /**
   * Unexpected element type {0} encountered in the end transaction value sequence.
   */
  ERR_END_TXN_RESPONSE_INVALID_TYPE("Unexpected element type {0} encountered in the end transaction value sequence."),



  /**
   * Cannot decode the end transaction value as an ASN.1 sequence:  {0}
   */
  ERR_END_TXN_RESPONSE_VALUE_NOT_SEQUENCE("Cannot decode the end transaction value as an ASN.1 sequence:  {0}"),



  /**
   * The provided extended request cannot be decoded as a password modify request because an error occurred while attempting to parse the value:  {0}
   */
  ERR_PW_MODIFY_REQUEST_CANNOT_DECODE("The provided extended request cannot be decoded as a password modify request because an error occurred while attempting to parse the value:  {0}"),



  /**
   * The provided extended request cannot be decoded as a password modify request because an element in the value sequence had an invalid BER type of {0}.
   */
  ERR_PW_MODIFY_REQUEST_INVALID_TYPE("The provided extended request cannot be decoded as a password modify request because an element in the value sequence had an invalid BER type of {0}."),



  /**
   * The provided extended request cannot be decoded as a password modify request because it does not have a value.
   */
  ERR_PW_MODIFY_REQUEST_NO_VALUE("The provided extended request cannot be decoded as a password modify request because it does not have a value."),



  /**
   * Unable to decode the provided extended result as a password modify extended result because the value sequence contained multiple elements.
   */
  ERR_PW_MODIFY_RESPONSE_MULTIPLE_ELEMENTS("Unable to decode the provided extended result as a password modify extended result because the value sequence contained multiple elements."),



  /**
   * Unable to decode the provided extended result as a password modify extended result because the value element could not be decoded as an ASN.1 sequence:  {0}
   */
  ERR_PW_MODIFY_RESPONSE_VALUE_NOT_SEQUENCE("Unable to decode the provided extended result as a password modify extended result because the value element could not be decoded as an ASN.1 sequence:  {0}"),



  /**
   * An error occurred while attempting to create a default SSL context:  {0}
   */
  ERR_STARTTLS_REQUEST_CANNOT_CREATE_DEFAULT_CONTEXT("An error occurred while attempting to create a default SSL context:  {0}"),



  /**
   * The provided extended cannot request be decoded as a StartTLS request because it has a value.
   */
  ERR_STARTTLS_REQUEST_HAS_VALUE("The provided extended cannot request be decoded as a StartTLS request because it has a value."),



  /**
   * The provided extended cannot request be decoded as a start transaction request because it has a value.
   */
  ERR_START_TXN_REQUEST_HAS_VALUE("The provided extended cannot request be decoded as a start transaction request because it has a value."),



  /**
   * The provided extended request cannot be decoded as a Who Am I? request because it has a value.
   */
  ERR_WHO_AM_I_REQUEST_HAS_VALUE("The provided extended request cannot be decoded as a Who Am I? request because it has a value."),



  /**
   * Cancel Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_CANCEL("Cancel Extended Request"),



  /**
   * End Transaction Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_END_TXN("End Transaction Extended Request"),



  /**
   * Password Modify Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_PASSWORD_MODIFY("Password Modify Extended Request"),



  /**
   * StartTLS Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_START_TLS("StartTLS Extended Request"),



  /**
   * Start Transaction Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_START_TXN("Start Transaction Extended Request"),



  /**
   * Who Am I? Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_WHO_AM_I("Who Am I? Extended Request"),



  /**
   * Aborted Transaction Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_ABORTED_TXN("Aborted Transaction Extended Result"),



  /**
   * End Transaction Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_END_TXN("End Transaction Extended Result"),



  /**
   * Notice Of Disconnection Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_NOTICE_OF_DISCONNECT("Notice Of Disconnection Extended Result"),



  /**
   * Password Modify Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_PASSWORD_MODIFY("Password Modify Extended Result"),



  /**
   * Start Transaction Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_START_TXN("Start Transaction Extended Result"),



  /**
   * Who Am I? Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_WHO_AM_I("Who Am I? Extended Result");



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
      rb = ResourceBundle.getBundle("unboundid-ldapsdk-extop");
    } catch (final Exception e) {}
    RESOURCE_BUNDLE = rb;
  }



  /**
   * The map that will be used to hold the unformatted message strings, indexed by property name.
   */
  private static final ConcurrentHashMap<ExtOpMessages,String> MESSAGE_STRINGS = new ConcurrentHashMap<>(100);



  /**
   * The map that will be used to hold the message format objects, indexed by property name.
   */
  private static final ConcurrentHashMap<ExtOpMessages,MessageFormat> MESSAGES = new ConcurrentHashMap<>(100);



  // The default text for this message
  private final String defaultText;



  /**
   * Creates a new message key.
   */
  private ExtOpMessages(final String defaultText)
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

