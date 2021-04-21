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
package com.unboundid.util.json;



import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;



/**
 * This enum defines a set of message keys for messages in the
 * com.unboundid.util.json package, which correspond to messages in the
 * unboundid-ldapsdk-json.properties properties file.
 * <BR><BR>
 * This source file was generated from the properties file.
 * Do not edit it directly.
 */
enum JSONMessages
{
  /**
   * The ''{0}'' field is not permitted for use with the ''{1}'' authentication type.
   */
  ERR_AUTH_DETAILS_FIELD_NOT_PERMITTED_FOR_AUTH_TYPE("The ''{0}'' field is not permitted for use with the ''{1}'' authentication type."),



  /**
   * The ''{0}'' field has an invalid value.  The value must be a single string or an array of strings, and only the strings ''auth'', ''auth-int'', and ''auth-conf'' are allowed.
   */
  ERR_AUTH_DETAILS_INVALID_QOP("The ''{0}'' field has an invalid value.  The value must be a single string or an array of strings, and only the strings ''auth'', ''auth-int'', and ''auth-conf'' are allowed."),



  /**
   * Either the ''{0}'' or ''{1}'' field must be used to specify the password for the ''{2}'' authentication type unless the ''{3}'' property is set with a value of true.
   */
  ERR_AUTH_DETAILS_MISSING_GSSAPI_PASSWORD("Either the ''{0}'' or ''{1}'' field must be used to specify the password for the ''{2}'' authentication type unless the ''{3}'' property is set with a value of true."),



  /**
   * A JSON object contained in the ''{0}'' field is missing the required ''{1}'' field.
   */
  ERR_AUTH_DETAILS_MISSING_REQUIRED_FIELD("A JSON object contained in the ''{0}'' field is missing the required ''{1}'' field."),



  /**
   * The ''{0}'' field must be provided in conjunction with the ''{1}'' authentication type.
   */
  ERR_AUTH_DETAILS_MISSING_REQUIRED_FIELD_FOR_AUTH_TYPE("The ''{0}'' field must be provided in conjunction with the ''{1}'' authentication type."),



  /**
   * Neither the ''{0}'' nor the ''{1}'' field was present to specify the password to use in conjunction with the ''{2}'' authentication type.
   */
  ERR_AUTH_DETAILS_NO_PASSWORD("Neither the ''{0}'' nor the ''{1}'' field was present to specify the password to use in conjunction with the ''{2}'' authentication type."),



  /**
   * Unrecognized authentication type ''{0}''.
   */
  ERR_AUTH_DETAILS_UNRECOGNIZED_TYPE("Unrecognized authentication type ''{0}''."),



  /**
   * Field ''{0}'' cannot be used in conjunction with field ''{1}''.
   */
  ERR_LDAP_SPEC_CONFLICTING_FIELD("Field ''{0}'' cannot be used in conjunction with field ''{1}''."),



  /**
   * Unable to parse the JSON object as an LDAP connection details specification because an error was encountered while attempting to process the value of field ''{0}'':  {1}
   */
  ERR_LDAP_SPEC_ERROR_PROCESSING_FIELD("Unable to parse the JSON object as an LDAP connection details specification because an error was encountered while attempting to process the value of field ''{0}'':  {1}"),



  /**
   * Field ''{0}'' cannot be used unless the ''{1}'' field is also present.
   */
  ERR_LDAP_SPEC_MISSING_DEPENDENT_FIELD("Field ''{0}'' cannot be used unless the ''{1}'' field is also present."),



  /**
   * Unable to parse the JSON object as an LDAP connection details specification because it is missing the required ''{0}'' top-level field.
   */
  ERR_LDAP_SPEC_MISSING_SERVER_DETAILS("Unable to parse the JSON object as an LDAP connection details specification because it is missing the required ''{0}'' top-level field."),



  /**
   * File ''{0}'' referenced in field ''{1}'' is empty.
   */
  ERR_LDAP_SPEC_READ_FILE_EMPTY("File ''{0}'' referenced in field ''{1}'' is empty."),



  /**
   * File ''{0}'' referenced in field ''{1}'' contains only an empty line.
   */
  ERR_LDAP_SPEC_READ_FILE_EMPTY_LINE("File ''{0}'' referenced in field ''{1}'' contains only an empty line."),



  /**
   * An error occurred while attempting to read from file ''{0}'' referenced in field ''{1}'':  {2}
   */
  ERR_LDAP_SPEC_READ_FILE_ERROR("An error occurred while attempting to read from file ''{0}'' referenced in field ''{1}'':  {2}"),



  /**
   * File ''{0}'' referenced in field ''{1}'' contains multiple lines.
   */
  ERR_LDAP_SPEC_READ_FILE_MULTIPLE_LINES("File ''{0}'' referenced in field ''{1}'' contains multiple lines."),



  /**
   * Unable to parse the JSON object as an LDAP connection details specification because it contains an unrecognized field of ''{0}'' in the object value of the ''{1}'' field.
   */
  ERR_LDAP_SPEC_UNRECOGNIZED_FIELD("Unable to parse the JSON object as an LDAP connection details specification because it contains an unrecognized field of ''{0}'' in the object value of the ''{1}'' field."),



  /**
   * Unable to parse the JSON object as an LDAP connection details specification because it contains an unrecognized top-level field of ''{0}''.
   */
  ERR_LDAP_SPEC_UNRECOGNIZED_TOP_LEVEL_FIELD("Unable to parse the JSON object as an LDAP connection details specification because it contains an unrecognized top-level field of ''{0}''."),



  /**
   * The integer value of field ''{0}'' is greater than the maximum allowed value of {1,number,0}.
   */
  ERR_LDAP_SPEC_VALUE_ABOVE_MAX("The integer value of field ''{0}'' is greater than the maximum allowed value of {1,number,0}."),



  /**
   * The integer value of field ''{0}'' is less than the minimum allowed value of {1,number,0}.
   */
  ERR_LDAP_SPEC_VALUE_BELOW_MIN("The integer value of field ''{0}'' is less than the minimum allowed value of {1,number,0}."),



  /**
   * The value of field ''{0}'' is not a boolean.
   */
  ERR_LDAP_SPEC_VALUE_NOT_BOOLEAN("The value of field ''{0}'' is not a boolean."),



  /**
   * The value of field ''{0}'' is not an integer.
   */
  ERR_LDAP_SPEC_VALUE_NOT_INTEGER("The value of field ''{0}'' is not an integer."),



  /**
   * The value of field ''{0}'' is not a JSON object.
   */
  ERR_LDAP_SPEC_VALUE_NOT_OBJECT("The value of field ''{0}'' is not a JSON object."),



  /**
   * The value of field ''{0}'' is not a string.
   */
  ERR_LDAP_SPEC_VALUE_NOT_STRING("The value of field ''{0}'' is not a string."),



  /**
   * An unexpected problem was encountered while attempting to parse string ''{0}'' as a JSON number:  {1}
   */
  ERR_NUMBER_CANNOT_PARSE("An unexpected problem was encountered while attempting to parse string ''{0}'' as a JSON number:  {1}"),



  /**
   * The string ''{0}'' cannot be parsed as a valid JSON number because the exponent is not an integer.  A JSON number cannot have a floating-point value as the exponent.
   */
  ERR_NUMBER_DECIMAL_IN_EXPONENT("The string ''{0}'' cannot be parsed as a valid JSON number because the exponent is not an integer.  A JSON number cannot have a floating-point value as the exponent."),



  /**
   * The string ''{0}'' cannot be parsed as a valid JSON number because the decimal point is not immediately followed by a digit.
   */
  ERR_NUMBER_DECIMAL_NOT_FOLLOWED_BY_DIGIT("The string ''{0}'' cannot be parsed as a valid JSON number because the decimal point is not immediately followed by a digit."),



  /**
   * An empty string cannot be parsed as a JSON number.
   */
  ERR_NUMBER_EMPTY_STRING("An empty string cannot be parsed as a JSON number."),



  /**
   * The string ''{0}'' cannot be parsed as a valid JSON number because the exponent indicator is not immediately followed by an optionally-signed digit.
   */
  ERR_NUMBER_EXPONENT_NOT_FOLLOWED_BY_DIGIT("The string ''{0}'' cannot be parsed as a valid JSON number because the exponent indicator is not immediately followed by an optionally-signed digit."),



  /**
   * The string ''{0}'' cannot be parsed as a valid JSON number because it has an illegal character at position {1,number,0}.
   */
  ERR_NUMBER_ILLEGAL_CHAR("The string ''{0}'' cannot be parsed as a valid JSON number because it has an illegal character at position {1,number,0}."),



  /**
   * The string ''{0}'' cannot be parsed as a valid JSON number because it has an illegal leading zero.  The first digit may be zero only if it is the only digit or if it is immediately followed by a decimal point.
   */
  ERR_NUMBER_ILLEGAL_LEADING_ZERO("The string ''{0}'' cannot be parsed as a valid JSON number because it has an illegal leading zero.  The first digit may be zero only if it is the only digit or if it is immediately followed by a decimal point."),



  /**
   * The string ''{0}'' cannot be parsed as a valid JSON number because it does not end with a digit.  All valid JSON number string representations must end with a digit.
   */
  ERR_NUMBER_LAST_CHAR_NOT_DIGIT("The string ''{0}'' cannot be parsed as a valid JSON number because it does not end with a digit.  All valid JSON number string representations must end with a digit."),



  /**
   * The string ''{0}'' cannot be parsed as a valid JSON number because it includes multiple decimal points.
   */
  ERR_NUMBER_MULTIPLE_DECIMAL_POINTS("The string ''{0}'' cannot be parsed as a valid JSON number because it includes multiple decimal points."),



  /**
   * The string ''{0}'' cannot be parsed as a valid JSON number because it includes multiple exponents.
   */
  ERR_NUMBER_MULTIPLE_EXPONENTS("The string ''{0}'' cannot be parsed as a valid JSON number because it includes multiple exponents."),



  /**
   * The string ''{0}'' cannot be parsed as a JSON object because it has data at position {1,number,0} beyond the closing curly brace that indicates the end of the object.
   */
  ERR_OBJECT_DATA_BEYOND_END("The string ''{0}'' cannot be parsed as a JSON object because it has data at position {1,number,0} beyond the closing curly brace that indicates the end of the object."),



  /**
   * The string ''{0}'' cannot be parsed as a JSON object because it does not start with an opening curly brace.
   */
  ERR_OBJECT_DOESNT_START_WITH_BRACE("The string ''{0}'' cannot be parsed as a JSON object because it does not start with an opening curly brace."),



  /**
   * The string ''{0}'' cannot be parsed as a JSON object because it has multiple fields named ''{1}''.
   */
  ERR_OBJECT_DUPLICATE_FIELD("The string ''{0}'' cannot be parsed as a JSON object because it has multiple fields named ''{1}''."),



  /**
   * The string ''{0}'' cannot be parsed as a JSON object because unexpected token ''{1}'' was encountered at position {2,number,0} when a colon (to separate a field name from its value) was expected.
   */
  ERR_OBJECT_EXPECTED_COLON("The string ''{0}'' cannot be parsed as a JSON object because unexpected token ''{1}'' was encountered at position {2,number,0} when a colon (to separate a field name from its value) was expected."),



  /**
   * The string ''{0}'' cannot be parsed as a JSON object because unexpected token ''{1}'' was encountered at position {2,number,0} when a comma (as a field separator) or a closing curly brace (to indicate the end of the object) was expected.
   */
  ERR_OBJECT_EXPECTED_COMMA_OR_CLOSE_BRACE("The string ''{0}'' cannot be parsed as a JSON object because unexpected token ''{1}'' was encountered at position {2,number,0} when a comma (as a field separator) or a closing curly brace (to indicate the end of the object) was expected."),



  /**
   * The string ''{0}'' cannot be parsed as a JSON object because unexpected token ''{1}'' was encountered at position {2,number,0} when a string (representing a field name) was expected.
   */
  ERR_OBJECT_EXPECTED_STRING("The string ''{0}'' cannot be parsed as a JSON object because unexpected token ''{1}'' was encountered at position {2,number,0} when a string (representing a field name) was expected."),



  /**
   * The string ''{0}'' cannot be parsed as a JSON object because unexpected token ''{1}'' was encountered at position {2,number,0} when a value was expected for field ''{3}''.
   */
  ERR_OBJECT_EXPECTED_VALUE("The string ''{0}'' cannot be parsed as a JSON object because unexpected token ''{1}'' was encountered at position {2,number,0} when a value was expected for field ''{3}''."),



  /**
   * The string ''{0}'' cannot be parsed as a JSON object because a JSON string contained in the object contains invalid escaped character ''{1}'' at position {2,number,0}.
   */
  ERR_OBJECT_INVALID_ESCAPED_CHAR("The string ''{0}'' cannot be parsed as a JSON object because a JSON string contained in the object contains invalid escaped character ''{1}'' at position {2,number,0}."),



  /**
   * The string ''{0}'' cannot be parsed as a JSON object because it contains character ''{1}'' at position {2,number,0}.
   */
  ERR_OBJECT_INVALID_FIRST_TOKEN_CHAR("The string ''{0}'' cannot be parsed as a JSON object because it contains character ''{1}'' at position {2,number,0}."),



  /**
   * The string ''{0}'' cannot be parsed as a JSON object because it contains an array with unexpected token ''{1}'' at position {2,number,0} when a comma or closing square bracket was expected.
   */
  ERR_OBJECT_INVALID_TOKEN_WHEN_ARRAY_COMMA_OR_BRACKET_EXPECTED("The string ''{0}'' cannot be parsed as a JSON object because it contains an array with unexpected token ''{1}'' at position {2,number,0} when a comma or closing square bracket was expected."),



  /**
   * The string ''{0}'' cannot be parsed as a JSON object because it contains an array with unexpected token ''{1}'' at position {2,number,0} when a value was expected.
   */
  ERR_OBJECT_INVALID_TOKEN_WHEN_ARRAY_VALUE_EXPECTED("The string ''{0}'' cannot be parsed as a JSON object because it contains an array with unexpected token ''{1}'' at position {2,number,0} when a value was expected."),



  /**
   * The string ''{0}'' cannot be parsed as a JSON object because an invalid Unicode escape sequence was found at position {1,number,0} while attempting to parse a JSON string.
   */
  ERR_OBJECT_INVALID_UNICODE_ESCAPE("The string ''{0}'' cannot be parsed as a JSON object because an invalid Unicode escape sequence was found at position {1,number,0} while attempting to parse a JSON string."),



  /**
   * Invalid data read from the input stream {0,number,0} bytes into the JSON object:  multiple ''{1}'' fields were found at the same level in the JSON object.
   */
  ERR_OBJECT_READER_DUPLICATE_FIELD("Invalid data read from the input stream {0,number,0} bytes into the JSON object:  multiple ''{1}'' fields were found at the same level in the JSON object."),



  /**
   * Invalid data read from the input stream {0,number,0} bytes into the JSON object.  No valid JSON token can start with character ''{1}''.
   */
  ERR_OBJECT_READER_ILLEGAL_FIRST_CHAR_FOR_JSON_TOKEN("Invalid data read from the input stream {0,number,0} bytes into the JSON object.  No valid JSON token can start with character ''{1}''."),



  /**
   * Invalid data read from the input stream {0,number,0} bytes into the JSON object.  A forward slash was found while skipping whitespace, which will be interpreted as the start of a comment, but that slash was not immediately followed by a second slash (to indicate that the comment extends until a line break) or an asterisk (to indicate that the comment extends until encountering an asterisk immediately followed by a slash).
   */
  ERR_OBJECT_READER_ILLEGAL_SLASH_SKIPPING_WHITESPACE("Invalid data read from the input stream {0,number,0} bytes into the JSON object.  A forward slash was found while skipping whitespace, which will be interpreted as the start of a comment, but that slash was not immediately followed by a second slash (to indicate that the comment extends until a line break) or an asterisk (to indicate that the comment extends until encountering an asterisk immediately followed by a slash)."),



  /**
   * Data read from the input stream cannot be parsed as a valid JSON object because the first token read was ''{0}'' rather than the expected open curly brace to indicate the start of the object.
   */
  ERR_OBJECT_READER_ILLEGAL_START_OF_OBJECT("Data read from the input stream cannot be parsed as a valid JSON object because the first token read was ''{0}'' rather than the expected open curly brace to indicate the start of the object."),



  /**
   * Invalid data read from the input stream {0,number,0} bytes into the JSON object:  an unquoted JSON value starting with ''f'' is expected to represent the Boolean value ''false'', but something else was found instead.
   */
  ERR_OBJECT_READER_INVALID_BOOLEAN_FALSE("Invalid data read from the input stream {0,number,0} bytes into the JSON object:  an unquoted JSON value starting with ''f'' is expected to represent the Boolean value ''false'', but something else was found instead."),



  /**
   * Invalid data read from the input stream {0,number,0} bytes into the JSON object:  an unquoted JSON value starting with ''t'' is expected to represent the Boolean value ''true'', but something else was found instead.
   */
  ERR_OBJECT_READER_INVALID_BOOLEAN_TRUE("Invalid data read from the input stream {0,number,0} bytes into the JSON object:  an unquoted JSON value starting with ''t'' is expected to represent the Boolean value ''true'', but something else was found instead."),



  /**
   * Invalid data read from the input stream {0,number,0} bytes into the JSON object:  a backslash cannot immediately be followed by character ''{1}''.
   */
  ERR_OBJECT_READER_INVALID_ESCAPED_CHAR("Invalid data read from the input stream {0,number,0} bytes into the JSON object:  a backslash cannot immediately be followed by character ''{1}''."),



  /**
   * Invalid data read from the input stream {0,number,0} bytes into the JSON object:  an unquoted JSON value starting with ''n'' is expected to represent the null value, but something else was found instead.
   */
  ERR_OBJECT_READER_INVALID_NULL("Invalid data read from the input stream {0,number,0} bytes into the JSON object:  an unquoted JSON value starting with ''n'' is expected to represent the null value, but something else was found instead."),



  /**
   * Invalid data read from the input stream {0,number,0} bytes into the JSON object:  invalid token ''{1}'' was found immediately after an array value.  Only a comma (to indicate that the array has at least one more element) or a closing square bracket (to indicate the end of the array) may immediately follow a JSON value in an array.
   */
  ERR_OBJECT_READER_INVALID_TOKEN_AFTER_ARRAY_VALUE("Invalid data read from the input stream {0,number,0} bytes into the JSON object:  invalid token ''{1}'' was found immediately after an array value.  Only a comma (to indicate that the array has at least one more element) or a closing square bracket (to indicate the end of the array) may immediately follow a JSON value in an array."),



  /**
   * Invalid data read from the input stream {0,number,0} bytes into the JSON object:  invalid token ''{1}'' was found immediately after the value for field ''{2}''.  Only a comma (to indicate that the object has at least one more field) or a closing curly brace (to indicate the end of the object) may immediately follow a JSON field value.
   */
  ERR_OBJECT_READER_INVALID_TOKEN_AFTER_OBJECT_VALUE("Invalid data read from the input stream {0,number,0} bytes into the JSON object:  invalid token ''{1}'' was found immediately after the value for field ''{2}''.  Only a comma (to indicate that the object has at least one more field) or a closing curly brace (to indicate the end of the object) may immediately follow a JSON field value."),



  /**
   * Invalid data read from the input stream {0,number,0} bytes into the JSON object:  invalid token ''{1}'' was found in an array where a JSON value (or possibly a closing square bracket to indicate the end of the array) was expected.
   */
  ERR_OBJECT_READER_INVALID_TOKEN_IN_ARRAY("Invalid data read from the input stream {0,number,0} bytes into the JSON object:  invalid token ''{1}'' was found in an array where a JSON value (or possibly a closing square bracket to indicate the end of the array) was expected."),



  /**
   * Invalid data read from the input stream {0,number,0} bytes into the JSON object:  invalid token ''{1}'' was found in an object where a field name (or possibly a closing curly brace to indicate the end of the object) was expected.
   */
  ERR_OBJECT_READER_INVALID_TOKEN_IN_OBJECT("Invalid data read from the input stream {0,number,0} bytes into the JSON object:  invalid token ''{1}'' was found in an object where a field name (or possibly a closing curly brace to indicate the end of the object) was expected."),



  /**
   * Invalid data read from the input stream {0,number,0} bytes into the JSON object:  a Unicode escape sequence was found, but was not followed by four hexadecimal digits.
   */
  ERR_OBJECT_READER_INVALID_UNICODE_ESCAPE("Invalid data read from the input stream {0,number,0} bytes into the JSON object:  a Unicode escape sequence was found, but was not followed by four hexadecimal digits."),



  /**
   * Invalid data read from the input stream {0,number,0} bytes into the JSON object:  non-ASCII byte ''{1}'' encountered in the middle of a JSON string is not a valid first byte for a multi-byte UTF-8 character.
   */
  ERR_OBJECT_READER_INVALID_UTF_8_BYTE_IN_STREAM("Invalid data read from the input stream {0,number,0} bytes into the JSON object:  non-ASCII byte ''{1}'' encountered in the middle of a JSON string is not a valid first byte for a multi-byte UTF-8 character."),



  /**
   * Invalid data read from the input stream {0,number,0} bytes into the JSON object:  invalid token ''{1}'' was found in an object immediately following field name ''{2}''.  Only a colon is allowed to follow a JSON field name.
   */
  ERR_OBJECT_READER_TOKEN_NOT_COLON("Invalid data read from the input stream {0,number,0} bytes into the JSON object:  invalid token ''{1}'' was found in an object immediately following field name ''{2}''.  Only a colon is allowed to follow a JSON field name."),



  /**
   * Invalid data read from the input stream {0,number,0} bytes into the JSON object:  invalid token ''{1}'' was found in an object immediately following the colon after field name ''{2}''.  The field name and colon must be followed by the field value.
   */
  ERR_OBJECT_READER_TOKEN_NOT_VALUE("Invalid data read from the input stream {0,number,0} bytes into the JSON object:  invalid token ''{1}'' was found in an object immediately following the colon after field name ''{2}''.  The field name and colon must be followed by the field value."),



  /**
   * Invalid data read from the input stream {0,number,0} bytes into the JSON object:  ASCII control character ''{1}'' must be escaped with a backslash.
   */
  ERR_OBJECT_READER_UNESCAPED_CONTROL_CHAR("Invalid data read from the input stream {0,number,0} bytes into the JSON object:  ASCII control character ''{1}'' must be escaped with a backslash."),



  /**
   * Unexpected end of the input stream reached {0,number,0} bytes into an incomplete JSON object.
   */
  ERR_OBJECT_READER_UNEXPECTED_END_OF_STREAM("Unexpected end of the input stream reached {0,number,0} bytes into an incomplete JSON object."),



  /**
   * The string ''{0}'' cannot be parsed as a JSON object because the assumed Boolean value starting at position {1,number,0} is neither ''true'' nor ''false''.
   */
  ERR_OBJECT_UNABLE_TO_PARSE_BOOLEAN("The string ''{0}'' cannot be parsed as a JSON object because the assumed Boolean value starting at position {1,number,0} is neither ''true'' nor ''false''."),



  /**
   * The string ''{0}'' cannot be parsed as a JSON object because the assumed null value starting at position {1,number,0} was not comprised of the string ''null''.
   */
  ERR_OBJECT_UNABLE_TO_PARSE_NULL("The string ''{0}'' cannot be parsed as a JSON object because the assumed null value starting at position {1,number,0} was not comprised of the string ''null''."),



  /**
   * The string ''{0}'' cannot be parsed as a JSON object because it contains an unclosed comment starting at position {1,number,0}.
   */
  ERR_OBJECT_UNCLOSED_COMMENT("The string ''{0}'' cannot be parsed as a JSON object because it contains an unclosed comment starting at position {1,number,0}."),



  /**
   * The string ''{0}'' cannot be parsed as a JSON object because a JSON string contained in the object contains an unescaped control character {1} at position {2,number,0}.
   */
  ERR_OBJECT_UNESCAPED_CONTROL_CHAR("The string ''{0}'' cannot be parsed as a JSON object because a JSON string contained in the object contains an unescaped control character {1} at position {2,number,0}."),



  /**
   * The string ''{0}'' cannot be parsed as a JSON object because the end of the string was encountered when more data was needed to complete the object.
   */
  ERR_OBJECT_UNEXPECTED_END_OF_STRING("The string ''{0}'' cannot be parsed as a JSON object because the end of the string was encountered when more data was needed to complete the object."),



  /**
   * The value of field ''{0}'' either must be a boolean value of true or false, or it must be an array containing one or more of the following string values:  ''add'', ''bind'', ''compare'', ''delete'', ''extended'', ''modify'', ''modify-dn'', or ''search''.
   */
  ERR_POOL_OPTIONS_INVALID_RETRY_TYPES("The value of field ''{0}'' either must be a boolean value of true or false, or it must be an array containing one or more of the following string values:  ''add'', ''bind'', ''compare'', ''delete'', ''extended'', ''modify'', ''modify-dn'', or ''search''."),



  /**
   * An error was encountered while attempting to create a key manager:  {0}
   */
  ERR_SECURITY_OPTIONS_CANNOT_CREATE_KEY_MANAGER("An error was encountered while attempting to create a key manager:  {0}"),



  /**
   * An error was encountered while attempting to create a StartTLS post-connect processor:  {0}
   */
  ERR_SECURITY_OPTIONS_CANNOT_CREATE_POST_CONNECT_PROCESSOR("An error was encountered while attempting to create a StartTLS post-connect processor:  {0}"),



  /**
   * An error was encountered while attempting to create an SSL socket factory:  {0}
   */
  ERR_SECURITY_OPTIONS_CANNOT_CREATE_SOCKET_FACTORY("An error was encountered while attempting to create an SSL socket factory:  {0}"),



  /**
   * An error was encountered while attempting to create a trust manager:  {0}
   */
  ERR_SECURITY_OPTIONS_CANNOT_CREATE_TRUST_MANAGER("An error was encountered while attempting to create a trust manager:  {0}"),



  /**
   * Field ''{0}'' cannot be provided when no key store is configured.
   */
  ERR_SECURITY_OPTIONS_INVALID_FIELD_WITHOUT_KS("Field ''{0}'' cannot be provided when no key store is configured."),



  /**
   * No other fields can be used in conjunction with an explicit or default ''{0}'' value of ''none''.
   */
  ERR_SECURITY_OPTIONS_INVALID_FIELD_WITH_NONE("No other fields can be used in conjunction with an explicit or default ''{0}'' value of ''none''."),



  /**
   * Field ''{0}'' cannot be used in conjunction with a ''{1}'' value of ''true''.
   */
  ERR_SECURITY_OPTIONS_INVALID_FIELD_WITH_TRUST_ALL("Field ''{0}'' cannot be used in conjunction with a ''{1}'' value of ''true''."),



  /**
   * The ''{0}'' field has an invalid value of ''{1}''.  When the ''{2}'' field is not present, the only supported key store type is ''PKCS11''.
   */
  ERR_SECURITY_OPTIONS_INVALID_KS_TYPE_WITHOUT_FILE("The ''{0}'' field has an invalid value of ''{1}''.  When the ''{2}'' field is not present, the only supported key store type is ''PKCS11''."),



  /**
   * The ''{0}'' field has an invalid value of ''{1}''.  The only supported key store types for use in conjunction with a key store file are ''JKS'' and ''PKCS12''.
   */
  ERR_SECURITY_OPTIONS_INVALID_KS_TYPE_WITH_FILE("The ''{0}'' field has an invalid value of ''{1}''.  The only supported key store types for use in conjunction with a key store file are ''JKS'' and ''PKCS12''."),



  /**
   * The ''{0}'' field has an invalid value of ''{1}''.  The only supported trust store types are ''JKS'' and ''PKCS12''.
   */
  ERR_SECURITY_OPTIONS_INVALID_TS_TYPE("The ''{0}'' field has an invalid value of ''{1}''.  The only supported trust store types are ''JKS'' and ''PKCS12''."),



  /**
   * Field ''{0}'' was provided with an invalid value.  Allowed values are ''none'' (to indicate no communication security), ''SSL'' or ''TLS'' (to indicate that communication should be entirely encrypted with transport-layer security), or ''StartTLS'' (to indicate that an initially-insecure connection should be encrypted with the StartTLS extended operation).
   */
  ERR_SECURITY_OPTIONS_INVALID_TYPE("Field ''{0}'' was provided with an invalid value.  Allowed values are ''none'' (to indicate no communication security), ''SSL'' or ''TLS'' (to indicate that communication should be entirely encrypted with transport-layer security), or ''StartTLS'' (to indicate that an initially-insecure connection should be encrypted with the StartTLS extended operation)."),



  /**
   * The required ''{0}'' key was not provided.
   */
  ERR_SECURITY_OPTIONS_MISSING_SECURITY_TYPE("The required ''{0}'' key was not provided."),



  /**
   * A JSON object contained in the ''{0}'' field has a ''{1}'' field whose value is an array without any elements.
   */
  ERR_SERVER_DETAILS_EMPTY_ARRAY("A JSON object contained in the ''{0}'' field has a ''{1}'' field whose value is an array without any elements."),



  /**
   * A JSON object contained in the ''{0}'' field has a ''{1}'' field whose value is not an array.
   */
  ERR_SERVER_DETAILS_FIELD_NOT_ARRAY("A JSON object contained in the ''{0}'' field has a ''{1}'' field whose value is not an array."),



  /**
   * The ''{0}'' JSON object must have exactly one field.
   */
  ERR_SERVER_DETAILS_INVALID_FIELD_SET("The ''{0}'' JSON object must have exactly one field."),



  /**
   * A JSON object contained in the ''{0}'' field is missing the required ''{1}'' field.
   */
  ERR_SERVER_DETAILS_MISSING_FIELD("A JSON object contained in the ''{0}'' field is missing the required ''{1}'' field."),



  /**
   * The ''{0}'' array in a JSON object contained in the ''{1}'' field includes an element that is not a JSON object.
   */
  ERR_SERVER_DETAILS_SERVERS_VALUE_NOT_OBJECT("The ''{0}'' array in a JSON object contained in the ''{1}'' field includes an element that is not a JSON object.");



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
      rb = ResourceBundle.getBundle("unboundid-ldapsdk-json");
    } catch (final Exception e) {}
    RESOURCE_BUNDLE = rb;
  }



  /**
   * The map that will be used to hold the unformatted message strings, indexed by property name.
   */
  private static final ConcurrentHashMap<JSONMessages,String> MESSAGE_STRINGS = new ConcurrentHashMap<>(100);



  /**
   * The map that will be used to hold the message format objects, indexed by property name.
   */
  private static final ConcurrentHashMap<JSONMessages,MessageFormat> MESSAGES = new ConcurrentHashMap<>(100);



  // The default text for this message
  private final String defaultText;



  /**
   * Creates a new message key.
   */
  private JSONMessages(final String defaultText)
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

