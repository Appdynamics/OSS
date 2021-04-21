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
package com.unboundid.ldap.sdk.unboundidds.jsonfilter;



import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;



/**
 * This enum defines a set of message keys for messages in the
 * com.unboundid.ldap.sdk.unboundidds.jsonfilter package, which correspond to messages in the
 * unboundid-ldapsdk-jsonfilter.properties properties file.
 * <BR><BR>
 * This source file was generated from the properties file.
 * Do not edit it directly.
 */
enum JFMessages
{
  /**
   * Unable to parse JSON object {0} as a filter of type ''{1}'' because value ''{2}'' of field ''{3}'' is not a valid expected type value.
   */
  ERR_CONTAINS_FIELD_FILTER_UNRECOGNIZED_EXPECTED_TYPE("Unable to parse JSON object {0} as a filter of type ''{1}'' because value ''{2}'' of field ''{3}'' is not a valid expected type value."),



  /**
   * The JSON matching rule does not support ordering matching.
   */
  ERR_JSON_MATCHING_RULE_ORDERING_NOT_SUPPORTED("The JSON matching rule does not support ordering matching."),



  /**
   * The JSON matching rule does not support substring matching.
   */
  ERR_JSON_MATCHING_RULE_SUBSTRING_NOT_SUPPORTED("The JSON matching rule does not support substring matching."),



  /**
   * Unable to parse JSON object {0} as a filter of type ''{1}'' because object {2} in the array of values for field ''{3}'' cannot be parsed as a valid JSON object filter:  {4}
   */
  ERR_OBJECT_FILTER_ARRAY_ELEMENT_NOT_FILTER("Unable to parse JSON object {0} as a filter of type ''{1}'' because object {2} in the array of values for field ''{3}'' cannot be parsed as a valid JSON object filter:  {4}"),



  /**
   * Unable to parse JSON object {0} as a filter of type ''{1}'' because the array value of field ''{2}'' contains at least one element that is not a JSON object.
   */
  ERR_OBJECT_FILTER_ARRAY_ELEMENT_NOT_OBJECT("Unable to parse JSON object {0} as a filter of type ''{1}'' because the array value of field ''{2}'' contains at least one element that is not a JSON object."),



  /**
   * Unable to parse JSON object {0} as a filter because it has an unrecognized value for the ''{1}'' field.
   */
  ERR_OBJECT_FILTER_INVALID_FILTER_TYPE("Unable to parse JSON object {0} as a filter because it has an unrecognized value for the ''{1}'' field."),



  /**
   * Unable to parse JSON object {0} as a filter because it is missing the required ''{1}'' field.
   */
  ERR_OBJECT_FILTER_MISSING_FILTER_TYPE("Unable to parse JSON object {0} as a filter because it is missing the required ''{1}'' field."),



  /**
   * Unable to parse JSON object {0} as a filter of type ''{1}'' because it is missing required field ''{2}''.
   */
  ERR_OBJECT_FILTER_MISSING_REQUIRED_FIELD("Unable to parse JSON object {0} as a filter of type ''{1}'' because it is missing required field ''{2}''."),



  /**
   * Unable to parse JSON object {0} as a filter of type ''{1}'' because it includes unrecognized field ''{2}''.
   */
  ERR_OBJECT_FILTER_UNRECOGNIZED_FIELD("Unable to parse JSON object {0} as a filter of type ''{1}'' because it includes unrecognized field ''{2}''."),



  /**
   * Unable to parse JSON object {0} as a filter of type ''{1}'' because the value of field ''{2}'' was not expected to be an empty array.
   */
  ERR_OBJECT_FILTER_VALUE_EMPTY_ARRAY("Unable to parse JSON object {0} as a filter of type ''{1}'' because the value of field ''{2}'' was not expected to be an empty array."),



  /**
   * Unable to parse JSON object {0} as a filter of type ''{1}'' because the value of field ''{2}'' is not an array of JSON objects.
   */
  ERR_OBJECT_FILTER_VALUE_NOT_ARRAY("Unable to parse JSON object {0} as a filter of type ''{1}'' because the value of field ''{2}'' is not an array of JSON objects."),



  /**
   * Unable to parse JSON object {0} as a filter of type ''{1}'' because the value of field ''{2}'' is not a Boolean.
   */
  ERR_OBJECT_FILTER_VALUE_NOT_BOOLEAN("Unable to parse JSON object {0} as a filter of type ''{1}'' because the value of field ''{2}'' is not a Boolean."),



  /**
   * Unable to parse JSON object {0} as a filter of type ''{1}'' because the value of field ''{2}'' cannot be decoded as a JSON object filter:  {3}
   */
  ERR_OBJECT_FILTER_VALUE_NOT_FILTER("Unable to parse JSON object {0} as a filter of type ''{1}'' because the value of field ''{2}'' cannot be decoded as a JSON object filter:  {3}"),



  /**
   * Unable to parse JSON object {0} as a filter of type ''{1}'' because the value of field ''{2}'' is not an object.
   */
  ERR_OBJECT_FILTER_VALUE_NOT_OBJECT("Unable to parse JSON object {0} as a filter of type ''{1}'' because the value of field ''{2}'' is not an object."),



  /**
   * Unable to parse JSON object {0} as a filter of type ''{1}'' because the value of field ''{2}'' is not a string.
   */
  ERR_OBJECT_FILTER_VALUE_NOT_STRING("Unable to parse JSON object {0} as a filter of type ''{1}'' because the value of field ''{2}'' is not a string."),



  /**
   * Unable to parse JSON object {0} as a filter of type ''{1}'' because the value of field ''{2}'' is not a string or an array of strings.
   */
  ERR_OBJECT_FILTER_VALUE_NOT_STRINGS("Unable to parse JSON object {0} as a filter of type ''{1}'' because the value of field ''{2}'' is not a string or an array of strings."),



  /**
   * Unable to parse JSON object {0} as a filter of type ''{1}'' because the value of field ''{2}'' cannot be parsed as a valid regular expression:  {3}
   */
  ERR_REGEX_FILTER_DECODE_INVALID_REGEX("Unable to parse JSON object {0} as a filter of type ''{1}'' because the value of field ''{2}'' cannot be parsed as a valid regular expression:  {3}"),



  /**
   * String ''{0}'' cannot be parsed as a valid regular expression:  {1}
   */
  ERR_REGEX_FILTER_INVALID_REGEX("String ''{0}'' cannot be parsed as a valid regular expression:  {1}"),



  /**
   * Unable to parse JSON object {0} as a filter of type ''{1}'' because it does not have a value for at least one of the {2}, {3}, or {4} fields.
   */
  ERR_SUBSTRING_FILTER_NO_COMPONENTS("Unable to parse JSON object {0} as a filter of type ''{1}'' because it does not have a value for at least one of the {2}, {3}, or {4} fields.");



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
      rb = ResourceBundle.getBundle("unboundid-ldapsdk-jsonfilter");
    } catch (final Exception e) {}
    RESOURCE_BUNDLE = rb;
  }



  /**
   * The map that will be used to hold the unformatted message strings, indexed by property name.
   */
  private static final ConcurrentHashMap<JFMessages,String> MESSAGE_STRINGS = new ConcurrentHashMap<>(100);



  /**
   * The map that will be used to hold the message format objects, indexed by property name.
   */
  private static final ConcurrentHashMap<JFMessages,MessageFormat> MESSAGES = new ConcurrentHashMap<>(100);



  // The default text for this message
  private final String defaultText;



  /**
   * Creates a new message key.
   */
  private JFMessages(final String defaultText)
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

