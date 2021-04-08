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
package com.unboundid.util;



import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;



/**
 * This enum defines a set of message keys for messages in the
 * com.unboundid.util package, which correspond to messages in the
 * unboundid-ldapsdk-util.properties properties file.
 * <BR><BR>
 * This source file was generated from the properties file.
 * Do not edit it directly.
 */
enum UtilityMessages
{
  /**
   * The aggregate input stream does not support the use of mark and reset functionality.
   */
  ERR_AGGREGATE_INPUT_STREAM_MARK_NOT_SUPPORTED("The aggregate input stream does not support the use of mark and reset functionality."),



  /**
   * The provided string cannot be parsed as an argument list because it ends with a backslash that was not immediately preceded by another backslash.  The backslash character will be considered an escape to indicate that the next character should be included as-is with no interpretation.
   */
  ERR_ARG_STRING_DANGLING_BACKSLASH("The provided string cannot be parsed as an argument list because it ends with a backslash that was not immediately preceded by another backslash.  The backslash character will be considered an escape to indicate that the next character should be included as-is with no interpretation."),



  /**
   * The provided string cannot be parsed as an argument list because it has an unmatched quote starting at or near position {0}.
   */
  ERR_ARG_STRING_UNMATCHED_QUOTE("The provided string cannot be parsed as an argument list because it has an unmatched quote starting at or near position {0}."),



  /**
   * A base32-encoded string must have a length that is a multiple of 8 bytes.
   */
  ERR_BASE32_DECODE_INVALID_LENGTH("A base32-encoded string must have a length that is a multiple of 8 bytes."),



  /**
   * Invalid character ''{0}'' encountered.
   */
  ERR_BASE32_DECODE_UNEXPECTED_CHAR("Invalid character ''{0}'' encountered."),



  /**
   * Unexpected equal sign found at position {0,number,0}.
   */
  ERR_BASE32_DECODE_UNEXPECTED_EQUAL("Unexpected equal sign found at position {0,number,0}."),



  /**
   * A base64-encoded string must have a length that is a multiple of 4 bytes.
   */
  ERR_BASE64_DECODE_INVALID_LENGTH("A base64-encoded string must have a length that is a multiple of 4 bytes."),



  /**
   * Invalid character ''{0}'' encountered.
   */
  ERR_BASE64_DECODE_UNEXPECTED_CHAR("Invalid character ''{0}'' encountered."),



  /**
   * Unexpected equal sign found at position {0,number,0}.
   */
  ERR_BASE64_DECODE_UNEXPECTED_EQUAL("Unexpected equal sign found at position {0,number,0}."),



  /**
   * The provided string did not have a valid length for base64url-encoded data.
   */
  ERR_BASE64_URLDECODE_INVALID_LENGTH("The provided string did not have a valid length for base64url-encoded data."),



  /**
   * Unable to load the Bouncy Castle FIPS provider class ''{0}'' into the JVM:  {1}.  This suggests that the Bouncy Castle FIPS provider jar file (typically named something like ''bc-fips-'{'version'}'.jar'' is not included in the classpath.
   */
  ERR_BC_FIPS_HELPER_CANNOT_LOAD_FIPS_PROVIDER_CLASS("Unable to load the Bouncy Castle FIPS provider class ''{0}'' into the JVM:  {1}.  This suggests that the Bouncy Castle FIPS provider jar file (typically named something like ''bc-fips-'{'version'}'.jar'' is not included in the classpath."),



  /**
   * Unable to load the Bouncy Castle JSSE provider class ''{0}'' into the JVM:  {1}.  This suggests that the Bouncy Castle FIPS TLS provider jar file (typically named something like ''bctls-fips-'{'version'}'.jar'' is not included in the classpath.
   */
  ERR_BC_FIPS_HELPER_CANNOT_LOAD_JSSE_PROVIDER_CLASS("Unable to load the Bouncy Castle JSSE provider class ''{0}'' into the JVM:  {1}.  This suggests that the Bouncy Castle FIPS TLS provider jar file (typically named something like ''bctls-fips-'{'version'}'.jar'' is not included in the classpath."),



  /**
   * An unexpected error occurred while attempting to create an instance of the Bouncy Castle FIPS provider class ''{0}'':  {1}
   */
  ERR_BC_FIPS_PROVIDER_CANNOT_INSTANTIATE_FIPS_PROVIDER("An unexpected error occurred while attempting to create an instance of the Bouncy Castle FIPS provider class ''{0}'':  {1}"),



  /**
   * An unexpected error occurred while attempting to create an instance of the Bouncy Castle JSSE provider class ''{0}'':  {1}
   */
  ERR_BC_FIPS_PROVIDER_CANNOT_INSTANTIATE_JSSE_PROVIDER("An unexpected error occurred while attempting to create an instance of the Bouncy Castle JSSE provider class ''{0}'':  {1}"),



  /**
   * The provided array is null.
   */
  ERR_BS_BUFFER_ARRAY_NULL("The provided array is null."),



  /**
   * The provided buffer is null.
   */
  ERR_BS_BUFFER_BUFFER_NULL("The provided buffer is null."),



  /**
   * The provided byte string is null.
   */
  ERR_BS_BUFFER_BYTE_STRING_NULL("The provided byte string is null."),



  /**
   * The provided capacity {0,number,0} is negative.
   */
  ERR_BS_BUFFER_CAPACITY_NEGATIVE("The provided capacity {0,number,0} is negative."),



  /**
   * The provided character sequence is null.
   */
  ERR_BS_BUFFER_CHAR_SEQUENCE_NULL("The provided character sequence is null."),



  /**
   * The provided end position {0,number,0} is beyond the length {1,number,0} of the provided value.
   */
  ERR_BS_BUFFER_END_BEYOND_LENGTH("The provided end position {0,number,0} is beyond the length {1,number,0} of the provided value."),



  /**
   * The provided length {0,number,0} is negative.
   */
  ERR_BS_BUFFER_LENGTH_NEGATIVE("The provided length {0,number,0} is negative."),



  /**
   * The provided offset {0,number,0} is negative.
   */
  ERR_BS_BUFFER_OFFSET_NEGATIVE("The provided offset {0,number,0} is negative."),



  /**
   * The provided offset {0,number,0} plus the provided length {1,number,0} is greater than the size of the provided array ({2,number,0}).
   */
  ERR_BS_BUFFER_OFFSET_PLUS_LENGTH_TOO_LARGE("The provided offset {0,number,0} plus the provided length {1,number,0} is greater than the size of the provided array ({2,number,0})."),



  /**
   * The provided offset {0,number,0} is too large for the buffer with length {1,number,0}.
   */
  ERR_BS_BUFFER_OFFSET_TOO_LARGE("The provided offset {0,number,0} is too large for the buffer with length {1,number,0}."),



  /**
   * The provided position {0,number,0} is negative.
   */
  ERR_BS_BUFFER_POS_NEGATIVE("The provided position {0,number,0} is negative."),



  /**
   * The provided position {0,number,0} is greater than the length of the buffer ({1,number,0}).
   */
  ERR_BS_BUFFER_POS_TOO_LARGE("The provided position {0,number,0} is greater than the length of the buffer ({1,number,0})."),



  /**
   * The provided start position {0,number,0} is greater than the provided end position {1,number,0}.
   */
  ERR_BS_BUFFER_START_BEYOND_END("The provided start position {0,number,0} is greater than the provided end position {1,number,0}."),



  /**
   * The provided start position {0,number,0} is beyond the length {1,number,0} of the provided value.
   */
  ERR_BS_BUFFER_START_BEYOND_LENGTH("The provided start position {0,number,0} is beyond the length {1,number,0} of the provided value."),



  /**
   * The provided start position {0,number,0} is negative.
   */
  ERR_BS_BUFFER_START_NEGATIVE("The provided start position {0,number,0} is negative."),



  /**
   * Unable to interactively read the encryption passphrase from the user:  {0}
   */
  ERR_CANNOT_GET_ENCRYPTION_PASSPHRASE("Unable to interactively read the encryption passphrase from the user:  {0}"),



  /**
   * Unable to acquire the closeable lock with a timeout of {0}.
   */
  ERR_CLOSEABLE_LOCK_TRY_LOCK_TIMEOUT("Unable to acquire the closeable lock with a timeout of {0}."),



  /**
   * Unable to acquire the closeable read lock with a timeout of {0}.
   */
  ERR_CLOSEABLE_RW_LOCK_TRY_LOCK_READ_TIMEOUT("Unable to acquire the closeable read lock with a timeout of {0}."),



  /**
   * Unable to acquire the closeable write lock with a timeout of {0}.
   */
  ERR_CLOSEABLE_RW_LOCK_TRY_LOCK_WRITE_TIMEOUT("Unable to acquire the closeable write lock with a timeout of {0}."),



  /**
   * Unable to open output file ''{0}'' for writing:  {0}
   */
  ERR_CL_TOOL_ERROR_CREATING_OUTPUT_FILE("Unable to open output file ''{0}'' for writing:  {0}"),



  /**
   * A shutdown hook was invoked for command-line tool ''{0}'' but the doShutdownHookProcessing method has not been implemented for that tool.  This method must be overridden and implemented for all tools that override the registerShutdownHook method to return true.
   */
  ERR_COMMAND_LINE_TOOL_SHUTDOWN_HOOK_NOT_IMPLEMENTED("A shutdown hook was invoked for command-line tool ''{0}'' but the doShutdownHookProcessing method has not been implemented for that tool.  This method must be overridden and implemented for all tools that override the registerShutdownHook method to return true."),



  /**
   * Unable to instantiate a certificate factory of type ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode.
   */
  ERR_CRYPTO_HELPER_GET_CERT_FACTORY_WRONG_PROVIDER_FOR_FIPS_MODE("Unable to instantiate a certificate factory of type ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode."),



  /**
   * Unable to instantiate cipher transformation ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode.
   */
  ERR_CRYPTO_HELPER_GET_CIPHER_WRONG_PROVIDER_FOR_FIPS_MODE("Unable to instantiate cipher transformation ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode."),



  /**
   * Unable to instantiate message digest algorithm ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode.
   */
  ERR_CRYPTO_HELPER_GET_DIGEST_WRONG_PROVIDER_FOR_FIPS_MODE("Unable to instantiate message digest algorithm ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode."),



  /**
   * Unable to instantiate a key factory for algorithm ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode.
   */
  ERR_CRYPTO_HELPER_GET_KEY_FACTORY_WRONG_PROVIDER_FOR_FIPS_MODE("Unable to instantiate a key factory for algorithm ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode."),



  /**
   * Unable to instantiate key store ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode.
   */
  ERR_CRYPTO_HELPER_GET_KEY_STORE_WRONG_PROVIDER_FOR_FIPS_MODE("Unable to instantiate key store ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode."),



  /**
   * Unable to instantiate key store ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used for the ''{3}'' key store type.
   */
  ERR_CRYPTO_HELPER_GET_KEY_STORE_WRONG_PROVIDER_FOR_STORE_TYPE("Unable to instantiate key store ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used for the ''{3}'' key store type."),



  /**
   * Unable to instantiate key store ''{0}'' because only the ''{1}'' and ''{2}'' store types may be used when operating in FIPS 140-2-compliant mode.
   */
  ERR_CRYPTO_HELPER_GET_KEY_STORE_WRONG_STORE_TYPE_FOR_FIPS_MODE("Unable to instantiate key store ''{0}'' because only the ''{1}'' and ''{2}'' store types may be used when operating in FIPS 140-2-compliant mode."),



  /**
   * Unable to instantiate a key manager factory for algorithm ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode.
   */
  ERR_CRYPTO_HELPER_GET_KM_FACTORY_WRONG_PROVIDER_FOR_FIPS_MODE("Unable to instantiate a key manager factory for algorithm ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode."),



  /**
   * Unable to instantiate a key pair generator for algorithm ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode.
   */
  ERR_CRYPTO_HELPER_GET_KP_GEN_WRONG_PROVIDER_FOR_FIPS_MODE("Unable to instantiate a key pair generator for algorithm ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode."),



  /**
   * Unable to instantiate MAC algorithm ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode.
   */
  ERR_CRYPTO_HELPER_GET_MAC_WRONG_PROVIDER_FOR_FIPS_MODE("Unable to instantiate MAC algorithm ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode."),



  /**
   * Unable to instantiate a secure random number generator for provider ''{0}'' because that provider does not support any secure random algorithms.
   */
  ERR_CRYPTO_HELPER_GET_SEC_RAND_NO_ALG_FOR_PROVIDER("Unable to instantiate a secure random number generator for provider ''{0}'' because that provider does not support any secure random algorithms."),



  /**
   * Unable to instantiate a secure random number generator for algorithm ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode.
   */
  ERR_CRYPTO_HELPER_GET_SEC_RAND_WRONG_PROVIDER_FOR_FIPS_MODE("Unable to instantiate a secure random number generator for algorithm ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode."),



  /**
   * Unable to instantiate a secure random number generator using provider ''{0}'' because only the ''{1}'' provider may be used when operating in FIPS 140-2-compliant mode.
   */
  ERR_CRYPTO_HELPER_GET_SEC_RAND_WRONG_PROVIDER_FOR_FIPS_MODE_NO_ALG("Unable to instantiate a secure random number generator using provider ''{0}'' because only the ''{1}'' provider may be used when operating in FIPS 140-2-compliant mode."),



  /**
   * Unable to instantiate signature algorithm ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode.
   */
  ERR_CRYPTO_HELPER_GET_SIGNATURE_WRONG_PROVIDER_FOR_FIPS_MODE("Unable to instantiate signature algorithm ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode."),



  /**
   * Unable to instantiate a secret key factory for algorithm ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode.
   */
  ERR_CRYPTO_HELPER_GET_SK_FACTORY_WRONG_PROVIDER_FOR_FIPS_MODE("Unable to instantiate a secret key factory for algorithm ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode."),



  /**
   * Unable to instantiate an SSL context for protocol ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode.
   */
  ERR_CRYPTO_HELPER_GET_SSL_CONTEXT_WRONG_PROVIDER_FOR_FIPS_MODE("Unable to instantiate an SSL context for protocol ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode."),



  /**
   * Unable to instantiate a trust manager factory for algorithm ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode.
   */
  ERR_CRYPTO_HELPER_GET_TM_FACTORY_WRONG_PROVIDER_FOR_FIPS_MODE("Unable to instantiate a trust manager factory for algorithm ''{0}'' using provider ''{1}'' because only the ''{2}'' provider may be used when operating in FIPS 140-2-compliant mode."),



  /**
   * System property ''{0}'' is set to ''true'', but an error occurred while attempting to load the FIPS provider:  {1}
   */
  ERR_CRYPTO_HELPER_INSTANTIATION_ERROR_FROM_FIPS_MODE_PROPERTY("System property ''{0}'' is set to ''true'', but an error occurred while attempting to load the FIPS provider:  {1}"),



  /**
   * System property ''{0}'' had an unexpected value of ''{1}''.  If that property is defined, then it must only have a value of ''true'' or ''false''.
   */
  ERR_CRYPTO_HELPER_INVALID_FIPS_MODE_PROPERTY_VALUE("System property ''{0}'' had an unexpected value of ''{1}''.  If that property is defined, then it must only have a value of ''true'' or ''false''."),



  /**
   * The ''{0}'' provider is not available in the JVM.
   */
  ERR_CRYPTO_HELPER_NO_SUCH_PROVIDER("The ''{0}'' provider is not available in the JVM."),



  /**
   * Provider ''{0}'' cannot be used when operating in FIPS 140-2-compliant mode.  Only the ''1}'' provider is allowed.
   */
  ERR_CRYPTO_HELPER_PROVIDER_NOT_AVAILABLE_IN_FIPS_MODE("Provider ''{0}'' cannot be used when operating in FIPS 140-2-compliant mode.  Only the ''1}'' provider is allowed."),



  /**
   * FIPS provider ''{0}'' is not currently supported.  At present, only the ''{1}'' provider is supported.
   */
  ERR_CRYPTO_HELPER_UNSUPPORTED_FIPS_PROVIDER("FIPS provider ''{0}'' is not currently supported.  At present, only the ''{1}'' provider is supported."),



  /**
   * Unable to decode bytes ''{0}'' as a valid UUID because the length of the provided content was not exactly 128 bits.
   */
  ERR_DECODE_UUID_INVALID_LENGTH("Unable to decode bytes ''{0}'' as a valid UUID because the length of the provided content was not exactly 128 bits."),



  /**
   * Unable to base64-decode value ''{0}'' contained on line {1,number,0} of file ''{2}'':  {3}
   */
  ERR_DN_FILE_READER_CANNOT_BASE64_DECODE("Unable to base64-decode value ''{0}'' contained on line {1,number,0} of file ''{2}'':  {3}"),



  /**
   * Unable to parse value ''{0}'' contained on line {1,number,0} of file ''{2}'' as a DN:  {3}
   */
  ERR_DN_FILE_READER_CANNOT_PARSE_DN("Unable to parse value ''{0}'' contained on line {1,number,0} of file ''{2}'' as a DN:  {3}"),



  /**
   * Unable to access data in file ''{0}'' for use in the value pattern:  {1}
   */
  ERR_FILE_VALUE_PATTERN_NOT_USABLE("Unable to access data in file ''{0}'' for use in the value pattern:  {1}"),



  /**
   * Unable to parse value ''{0}'' contained on line {1,number,0} of file ''{2}'' as an LDAP search filter:  {3}
   */
  ERR_FILTER_FILE_READER_CANNOT_PARSE_FILTER("Unable to parse value ''{0}'' contained on line {1,number,0} of file ''{2}'' as an LDAP search filter:  {3}"),



  /**
   * Attempted to write beyond the end of the array backing the output stream
   */
  ERR_FIXED_ARRAY_OS_WRITE_BEYOND_END("Attempted to write beyond the end of the array backing the output stream"),



  /**
   * Unable to decode the provided hexadecimal string to a byte array because the provided string had a non-hex character at index {0,number,0}.
   */
  ERR_FROM_HEX_NON_HEX_CHARACTER("Unable to decode the provided hexadecimal string to a byte array because the provided string had a non-hex character at index {0,number,0}."),



  /**
   * Unable to decode the provided hexadecimal string to a byte array because the provided string had a length of {0,number,0} characters, but it is only possible to process strings with an even number of characters.
   */
  ERR_FROM_HEX_ODD_NUMBER_OF_CHARACTERS("Unable to decode the provided hexadecimal string to a byte array because the provided string had a length of {0,number,0} characters, but it is only possible to process strings with an even number of characters."),



  /**
   * Unable to parse the provided timestamp ''{0}'' because it had an invalid number of characters before the sub-second and/or time zone portion.
   */
  ERR_GENTIME_CANNOT_PARSE_INVALID_LENGTH("Unable to parse the provided timestamp ''{0}'' because it had an invalid number of characters before the sub-second and/or time zone portion."),



  /**
   * Unable to parse time zone information from the provided timestamp ''{0}''.
   */
  ERR_GENTIME_DECODE_CANNOT_PARSE_TZ("Unable to parse time zone information from the provided timestamp ''{0}''."),



  /**
   * Unable to access data from ''{0}'' for use in the value pattern:  {1}
   */
  ERR_HTTP_VALUE_PATTERN_NOT_USABLE("Unable to access data from ''{0}'' for use in the value pattern:  {1}"),



  /**
   * Invalid choice.  Enter the desired option number or letter.
   */
  ERR_INTERACTIVE_ARG_MENU_INVALID_CHOICE("Invalid choice.  Enter the desired option number or letter."),



  /**
   * Invalid value:  {0}
   */
  ERR_INTERACTIVE_ARG_PROMPT_INVALID_VALUE("Invalid value:  {0}"),



  /**
   * If the ''{0}'' argument is provided, then one of the following arguments must also be provided:  {1}.
   */
  ERR_INTERACTIVE_DEPENDENT_ARG_SET_CONFLICT("If the ''{0}'' argument is provided, then one of the following arguments must also be provided:  {1}."),



  /**
   * At most one of the following arguments must be provided:  {0}.
   */
  ERR_INTERACTIVE_EXCLUSIVE_ARG_SET_CONFLICT("At most one of the following arguments must be provided:  {0}."),



  /**
   * Unable to authenticate to the directory server using the provided settings:  {0}
   */
  ERR_INTERACTIVE_LDAP_CANNOT_AUTHENTICATE("Unable to authenticate to the directory server using the provided settings:  {0}"),



  /**
   * Unable to establish a connection to server ''{0}'' on port {1,number,0}:  {2}
   */
  ERR_INTERACTIVE_LDAP_CANNOT_CONNECT("Unable to establish a connection to server ''{0}'' on port {1,number,0}:  {2}"),



  /**
   * An error occurred while attempting to initialize the key manager for SSL/TLS encryption:  {0}.  Unable to communicate securely with the directory server.
   */
  ERR_INTERACTIVE_LDAP_CANNOT_CREATE_KEY_MANAGER("An error occurred while attempting to initialize the key manager for SSL/TLS encryption:  {0}.  Unable to communicate securely with the directory server."),



  /**
   * An error occurred while attempting to initialize the socket factory for SSL/TLS encryption:  {0}.  Unable to communicate securely with the directory server.
   */
  ERR_INTERACTIVE_LDAP_CANNOT_CREATE_SOCKET_FACTORY("An error occurred while attempting to initialize the socket factory for SSL/TLS encryption:  {0}.  Unable to communicate securely with the directory server."),



  /**
   * An error occurred while attempting to initialize the trust manager for SSL/TLS encryption:  {0}.  Unable to communicate securely with the directory server.
   */
  ERR_INTERACTIVE_LDAP_CANNOT_CREATE_TRUST_MANAGER("An error occurred while attempting to initialize the trust manager for SSL/TLS encryption:  {0}.  Unable to communicate securely with the directory server."),



  /**
   * Unable to secure the connection using StartTLS:  {0}
   */
  ERR_INTERACTIVE_LDAP_CANNOT_PERFORM_STARTTLS("Unable to secure the connection using StartTLS:  {0}"),



  /**
   * An error occurred while attempting to determine the selected menu item:  {0}
   */
  ERR_INTERACTIVE_MENU_CANNOT_READ_CHOICE("An error occurred while attempting to determine the selected menu item:  {0}"),



  /**
   * These errors must be corrected before the tool can be run.
   */
  ERR_INTERACTIVE_MENU_CORRECT_VALIDATION_ERRORS("These errors must be corrected before the tool can be run."),



  /**
   * One or more errors were detected while performing extended argument validation:  {0}
   */
  ERR_INTERACTIVE_MENU_EXTENDED_VALIDATION_ERRORS("One or more errors were detected while performing extended argument validation:  {0}"),



  /**
   * Invalid choice.  Enter the desired option number, or ''q'' to quit.
   */
  ERR_INTERACTIVE_MENU_INVALID_CHOICE("Invalid choice.  Enter the desired option number, or ''q'' to quit."),



  /**
   * Interactive mode is currently not supported for multi-server LDAP command-line tools.
   */
  ERR_INTERACTIVE_MULTI_SERVER_LDAP_NOT_SUPPORTED("Interactive mode is currently not supported for multi-server LDAP command-line tools."),



  /**
   * ERROR:  The provided values do not match.
   */
  ERR_INTERACTIVE_PROMPT_CONFIRM_MISMATCH("ERROR:  The provided values do not match."),



  /**
   * File ''{0}'' does not exist.  You must provide a path to a directory that exists.
   */
  ERR_INTERACTIVE_PROMPT_DIR_DOES_NOT_EXIST("File ''{0}'' does not exist.  You must provide a path to a directory that exists."),



  /**
   * An error occurred while attempting to read a response from the user:  {0}
   */
  ERR_INTERACTIVE_PROMPT_ERROR_READING_RESPONSE("An error occurred while attempting to read a response from the user:  {0}"),



  /**
   * File ''{0}'' does not exist.  You must provide a path to a file that exists.
   */
  ERR_INTERACTIVE_PROMPT_FILE_DOES_NOT_EXIST("File ''{0}'' does not exist.  You must provide a path to a file that exists."),



  /**
   * The value must be between {0} and {1}.
   */
  ERR_INTERACTIVE_PROMPT_INTEGER_OUT_OF_RANGE("The value must be between {0} and {1}."),



  /**
   * Unable to parse the provided value as a valid Boolean response.  Please enter either ''true'' or ''false''.
   */
  ERR_INTERACTIVE_PROMPT_INVALID_BOOLEAN("Unable to parse the provided value as a valid Boolean response.  Please enter either ''true'' or ''false''."),



  /**
   * Unable to parse the provided value as a valid LDAP distinguished name.  Please enter a valid DN.
   */
  ERR_INTERACTIVE_PROMPT_INVALID_DN("Unable to parse the provided value as a valid LDAP distinguished name.  Please enter a valid DN."),



  /**
   * Unable to parse the provided value as a valid LDAP search filter.  Please enter a valid filter.
   */
  ERR_INTERACTIVE_PROMPT_INVALID_FILTER("Unable to parse the provided value as a valid LDAP search filter.  Please enter a valid filter."),



  /**
   * Unable to parse the provided value as an integer.  Please enter a valid integer value.
   */
  ERR_INTERACTIVE_PROMPT_INVALID_INTEGER_WITHOUT_RANGE("Unable to parse the provided value as an integer.  Please enter a valid integer value."),



  /**
   * Unable to parse the provided value as an integer.  Please enter a valid integer value between {0} and {1}.
   */
  ERR_INTERACTIVE_PROMPT_INVALID_INTEGER_WITH_RANGE("Unable to parse the provided value as an integer.  Please enter a valid integer value between {0} and {1}."),



  /**
   * Unable to parse the provided value as a timestamp.  Supported formats include any generalized time format, as well as local timestamps in any of the following formats:  ''YYYYMMDDhhmmss.uuu'', ''YYYYMMDDhhmmss'', or ''YYYYMMDDhhmm''.  Please enter a valid timestamp.
   */
  ERR_INTERACTIVE_PROMPT_INVALID_TIMESTAMP("Unable to parse the provided value as a timestamp.  Supported formats include any generalized time format, as well as local timestamps in any of the following formats:  ''YYYYMMDDhhmmss.uuu'', ''YYYYMMDDhhmmss'', or ''YYYYMMDDhhmm''.  Please enter a valid timestamp."),



  /**
   * The null DN is not allowed for this value.  Please enter a non-empty DN.
   */
  ERR_INTERACTIVE_PROMPT_NULL_DN_NOT_ALLOWED("The null DN is not allowed for this value.  Please enter a non-empty DN."),



  /**
   * File ''{0}'' does not exist, and neither does its parent.  You must provide a path in which at least the parent directory exists.
   */
  ERR_INTERACTIVE_PROMPT_PARENT_DOES_NOT_EXIST("File ''{0}'' does not exist, and neither does its parent.  You must provide a path in which at least the parent directory exists."),



  /**
   * Path ''{0}'' exists but references a file rather than a directory.  The specified path must reference a directory.
   */
  ERR_INTERACTIVE_PROMPT_PATH_MUST_BE_DIR("Path ''{0}'' exists but references a file rather than a directory.  The specified path must reference a directory."),



  /**
   * Path ''{0}'' exists but references a directory rather than a file.  The specified path must reference a file.
   */
  ERR_INTERACTIVE_PROMPT_PATH_MUST_BE_FILE("Path ''{0}'' exists but references a directory rather than a file.  The specified path must reference a file."),



  /**
   * You are require to supply a value.
   */
  ERR_INTERACTIVE_PROMPT_VALUE_REQUIRED("You are require to supply a value."),



  /**
   * At least one of the following arguments must be provided:  {0}.
   */
  ERR_INTERACTIVE_REQUIRED_ARG_SET_CONFLICT("At least one of the following arguments must be provided:  {0}."),



  /**
   * The specified value was invalid:  {0}
   */
  ERR_INTERACTIVE_TRAILING_VALUE_INVALID("The specified value was invalid:  {0}"),



  /**
   * Unable to create the key manager for secure communication:  {0}
   */
  ERR_LDAP_TOOL_CANNOT_CREATE_KEY_MANAGER("Unable to create the key manager for secure communication:  {0}"),



  /**
   * Unable to create the SSL socket factory to use for secure communication with the server:  {0}
   */
  ERR_LDAP_TOOL_CANNOT_CREATE_SSL_SOCKET_FACTORY("Unable to create the SSL socket factory to use for secure communication with the server:  {0}"),



  /**
   * Unable to read the bind password:  {0}
   */
  ERR_LDAP_TOOL_CANNOT_READ_BIND_PASSWORD("Unable to read the bind password:  {0}"),



  /**
   * Unable to read the key store password:  {0}
   */
  ERR_LDAP_TOOL_CANNOT_READ_KEY_STORE_PASSWORD("Unable to read the key store password:  {0}"),



  /**
   * Unable to read the trust store password:  {0}
   */
  ERR_LDAP_TOOL_CANNOT_READ_TRUST_STORE_PASSWORD("Unable to read the trust store password:  {0}"),



  /**
   * If either the ''--{0}'' or ''--{1}'' arguments are provided multiple times, then both arguments must be provided the same number of times.
   */
  ERR_LDAP_TOOL_HOST_PORT_COUNT_MISMATCH("If either the ''--{0}'' or ''--{1}'' arguments are provided multiple times, then both arguments must be provided the same number of times."),



  /**
   * SASL option ''{0}'' cannot be used with the {1} mechanism.
   */
  ERR_LDAP_TOOL_INVALID_SASL_OPTION("SASL option ''{0}'' cannot be used with the {1} mechanism."),



  /**
   * SASL option ''{0}'' is invalid.  SASL options must be in the form ''name=value''.
   */
  ERR_LDAP_TOOL_MALFORMED_SASL_OPTION("SASL option ''{0}'' is invalid.  SASL options must be in the form ''name=value''."),



  /**
   * SASL option ''{0}'' is required for use with the {1} mechanism.
   */
  ERR_LDAP_TOOL_MISSING_REQUIRED_SASL_OPTION("SASL option ''{0}'' is required for use with the {1} mechanism."),



  /**
   * One or more SASL options were provided, but the ''mech'' option was not given to indicate which SASL mechanism to use.
   */
  ERR_LDAP_TOOL_NO_SASL_MECH("One or more SASL options were provided, but the ''mech'' option was not given to indicate which SASL mechanism to use."),



  /**
   * StartTLS negotiation failed:  {0}
   */
  ERR_LDAP_TOOL_START_TLS_FAILED("StartTLS negotiation failed:  {0}"),



  /**
   * SASL mechanism ''{0}'' is not supported.
   */
  ERR_LDAP_TOOL_UNSUPPORTED_SASL_MECH("SASL mechanism ''{0}'' is not supported."),



  /**
   * If non-null, the sets of server name prefixes and server name suffixes must not be empty.
   */
  ERR_MULTI_LDAP_TOOL_PREFIXES_AND_SUFFIXES_EMPTY("If non-null, the sets of server name prefixes and server name suffixes must not be empty."),



  /**
   * If both the sets of server name prefixes and server name suffixes are non-null, they must have the same number of elements.
   */
  ERR_MULTI_LDAP_TOOL_PREFIXES_AND_SUFFIXES_MISMATCH("If both the sets of server name prefixes and server name suffixes are non-null, they must have the same number of elements."),



  /**
   * The sets of server name prefixes and server name suffixes must not both be null.
   */
  ERR_MULTI_LDAP_TOOL_PREFIXES_AND_SUFFIXES_NULL("The sets of server name prefixes and server name suffixes must not both be null."),



  /**
   * No Exception
   */
  ERR_NO_EXCEPTION("No Exception"),



  /**
   * Unable to parse ''{0}'' as an object identifier because component ''{1}'' is out of range for a 32-bit integer.
   */
  ERR_OID_CANNOT_PARSE_AS_INT("Unable to parse ''{0}'' as an object identifier because component ''{1}'' is out of range for a 32-bit integer."),



  /**
   * Unable to parse ''{0}'' as an object identifier because it contains consecutive periods at position {1,number,0}.
   */
  ERR_OID_CONSECUTIVE_PERIODS("Unable to parse ''{0}'' as an object identifier because it contains consecutive periods at position {1,number,0}."),



  /**
   * Unable to parse the provided string as an object identifier because it is empty.
   */
  ERR_OID_EMPTY("Unable to parse the provided string as an object identifier because it is empty."),



  /**
   * Unable to parse ''{0}'' as an object identifier because it ends with a period.
   */
  ERR_OID_ENDS_WITH_PERIOD("Unable to parse ''{0}'' as an object identifier because it ends with a period."),



  /**
   * Unable to retrieve the parent for OID ''{0}'' because it is not a valid numeric OID.
   */
  ERR_OID_GET_PARENT_NOT_VALID("Unable to retrieve the parent for OID ''{0}'' because it is not a valid numeric OID."),



  /**
   * Unable to parse ''{0}'' as an object identifier because it contains illegal character ''{1}'' at position {2,number,0}.
   */
  ERR_OID_ILLEGAL_CHARACTER("Unable to parse ''{0}'' as an object identifier because it contains illegal character ''{1}'' at position {2,number,0}."),



  /**
   * Unable to parse ''{0}'' as a valid object identifier because the first component value of {1,number,0} is not valid.  The first component of a numeric OID can only be zero, one, or two.
   */
  ERR_OID_ILLEGAL_FIRST_COMPONENT("Unable to parse ''{0}'' as a valid object identifier because the first component value of {1,number,0} is not valid.  The first component of a numeric OID can only be zero, one, or two."),



  /**
   * Unable to parse ''{0}'' as a valid object identifier because the second component value of {1,number,0} is out of range.  If the first component is {2,number,0}, then the second component must not be greater than 39.
   */
  ERR_OID_ILLEGAL_SECOND_COMPONENT("Unable to parse ''{0}'' as a valid object identifier because the second component value of {1,number,0} is out of range.  If the first component is {2,number,0}, then the second component must not be greater than 39."),



  /**
   * Unable to create an OID that is a child of OID ''{0}'' because the provided is not a valid numeric OID.
   */
  ERR_OID_INIT_PARENT_NOT_VALID("Unable to create an OID that is a child of OID ''{0}'' because the provided is not a valid numeric OID."),



  /**
   * Unable to determine whether OID ''{0}'' is an ancestor of OID ''{1}'' because the latter is not a valid numeric OID.
   */
  ERR_OID_IS_ANCESTOR_OF_PROVIDED_NOT_VALID("Unable to determine whether OID ''{0}'' is an ancestor of OID ''{1}'' because the latter is not a valid numeric OID."),



  /**
   * Unable to determine whether OID ''{0}'' is an ancestor of OID ''{1}'' because the former is not a valid numeric OID.
   */
  ERR_OID_IS_ANCESTOR_OF_THIS_NOT_VALID("Unable to determine whether OID ''{0}'' is an ancestor of OID ''{1}'' because the former is not a valid numeric OID."),



  /**
   * Unable to determine whether OID ''{0}'' is a descendant of OID ''{1}'' because the latter is not a valid numeric OID.
   */
  ERR_OID_IS_DESCENDANT_OF_PROVIDED_NOT_VALID("Unable to determine whether OID ''{0}'' is a descendant of OID ''{1}'' because the latter is not a valid numeric OID."),



  /**
   * Unable to determine whether OID ''{0}'' is a descendant of OID ''{1}'' because the former is not a valid numeric OID.
   */
  ERR_OID_IS_DESCENDANT_OF_THIS_NOT_VALID("Unable to determine whether OID ''{0}'' is a descendant of OID ''{1}'' because the former is not a valid numeric OID."),



  /**
   * Unable to parse ''{0}'' as an object identifier because it contains component ''{1}'' that starts with a leading zero.
   */
  ERR_OID_LEADING_ZERO("Unable to parse ''{0}'' as an object identifier because it contains component ''{1}'' that starts with a leading zero."),



  /**
   * Unable to parse ''{0}'' as a valid object identifier because it only contains a single component.  Valid OIDs must have at least two components.
   */
  ERR_OID_NOT_ENOUGH_COMPONENTS("Unable to parse ''{0}'' as a valid object identifier because it only contains a single component.  Valid OIDs must have at least two components."),



  /**
   * Unable to parse JSON object {0} as an OID registry item because it is missing the required ''{1}'' field.
   */
  ERR_OID_REGISTRY_ITEM_OBJECT_MISSING_FIELD("Unable to parse JSON object {0} as an OID registry item because it is missing the required ''{1}'' field."),



  /**
   * Unable to parse ''{0}'' as an object identifier because it starts with a period.
   */
  ERR_OID_STARTS_WITH_PERIOD("Unable to parse ''{0}'' as an object identifier because it starts with a period."),



  /**
   * Unable to create a cipher from the passphrase-encrypted stream header because no passphrase was provided when decoding the header.
   */
  ERR_PW_ENCRYPTED_HEADER_NO_KEY_AVAILABLE("Unable to create a cipher from the passphrase-encrypted stream header because no passphrase was provided when decoding the header."),



  /**
   * The provided passphrase is invalid.
   */
  ERR_PW_ENCRYPTED_HEADER_SEQUENCE_BAD_PW("The provided passphrase is invalid."),



  /**
   * An unexpected error occurred while trying to decode the header sequence:  {0}
   */
  ERR_PW_ENCRYPTED_HEADER_SEQUENCE_DECODE_ERROR("An unexpected error occurred while trying to decode the header sequence:  {0}"),



  /**
   * Unrecognized header sequence element type:  {0}.
   */
  ERR_PW_ENCRYPTED_HEADER_SEQUENCE_UNRECOGNIZED_ELEMENT_TYPE("Unrecognized header sequence element type:  {0}."),



  /**
   * Unsupported encoding version {0,number,0}.
   */
  ERR_PW_ENCRYPTED_HEADER_SEQUENCE_UNSUPPORTED_VERSION("Unsupported encoding version {0,number,0}."),



  /**
   * Even after multiple attempts, an encryption key could not be generated consistently. This likely indicates some serious issue with the JVM implementation.
   */
  ERR_PW_ENCRYPTED_STREAM_HEADER_CANNOT_GENERATE_KEY("Even after multiple attempts, an encryption key could not be generated consistently. This likely indicates some serious issue with the JVM implementation."),



  /**
   * Unable to decode the contents of the provided byte array as a passphrase-encrypted stream header because the non-magic portion of the header could not be properly decoded:  {0}
   */
  ERR_PW_ENCRYPTED_STREAM_HEADER_DECODE_ASN1_DECODE_ERROR("Unable to decode the contents of the provided byte array as a passphrase-encrypted stream header because the non-magic portion of the header could not be properly decoded:  {0}"),



  /**
   * Unable to decode the contents of the provided byte array as a passphrase-encrypted stream header because it does not start with the expected magic value.
   */
  ERR_PW_ENCRYPTED_STREAM_HEADER_DECODE_MAGIC_MISMATCH("Unable to decode the contents of the provided byte array as a passphrase-encrypted stream header because it does not start with the expected magic value."),



  /**
   * Unable to decode the contents of the provided byte array as a passphrase-encrypted stream header because the array is too short to contain a valid header.
   */
  ERR_PW_ENCRYPTED_STREAM_HEADER_DECODE_TOO_SHORT("Unable to decode the contents of the provided byte array as a passphrase-encrypted stream header because the array is too short to contain a valid header."),



  /**
   * Unable to read a passphrase-encrypted stream header from the provided input stream because the non-magic portion of the header could not be properly decoded:  {0}
   */
  ERR_PW_ENCRYPTED_STREAM_HEADER_READ_ASN1_DECODE_ERROR("Unable to read a passphrase-encrypted stream header from the provided input stream because the non-magic portion of the header could not be properly decoded:  {0}"),



  /**
   * Unable to read a passphrase-encrypted stream header from the provided input stream because the end of the stream was reached after the magic bytes.
   */
  ERR_PW_ENCRYPTED_STREAM_HEADER_READ_END_OF_STREAM_AFTER_MAGIC("Unable to read a passphrase-encrypted stream header from the provided input stream because the end of the stream was reached after the magic bytes."),



  /**
   * Unable to read a passphrase-encrypted stream header from the provided input stream because the end of the stream was reached while trying to read the magic bytes at the beginning of the header.
   */
  ERR_PW_ENCRYPTED_STREAM_HEADER_READ_END_OF_STREAM_IN_MAGIC("Unable to read a passphrase-encrypted stream header from the provided input stream because the end of the stream was reached while trying to read the magic bytes at the beginning of the header."),



  /**
   * Unable to read a passphrase-encrypted stream header from the provided input stream because the data read at the beginning of the stream did not start with the expected magic value.
   */
  ERR_PW_ENCRYPTED_STREAM_HEADER_READ_MAGIC_MISMATCH("Unable to read a passphrase-encrypted stream header from the provided input stream because the data read at the beginning of the stream did not start with the expected magic value."),



  /**
   * Unable to read a password from file ''{0}'' because that file is empty.  A password file must contain exactly one line, that line must be non-empty, and the entire contents of that line will be used as the password.
   */
  ERR_PW_FILE_READER_FILE_EMPTY("Unable to read a password from file ''{0}'' because that file is empty.  A password file must contain exactly one line, that line must be non-empty, and the entire contents of that line will be used as the password."),



  /**
   * Unable to read a password from file ''{0}'' because file contains just an empty line.  A password file must contain exactly one line, that line must be non-empty, and the entire contents of that line will be used as the password.
   */
  ERR_PW_FILE_READER_FILE_HAS_EMPTY_LINE("Unable to read a password from file ''{0}'' because file contains just an empty line.  A password file must contain exactly one line, that line must be non-empty, and the entire contents of that line will be used as the password."),



  /**
   * Unable to read a password from file ''{0}'' because that file has multiple lines.  A password file must contain exactly one line, that line must be non-empty, and the entire contents of that line will be used as the password.
   */
  ERR_PW_FILE_READER_FILE_HAS_MULTIPLE_LINES("Unable to read a password from file ''{0}'' because that file has multiple lines.  A password file must contain exactly one line, that line must be non-empty, and the entire contents of that line will be used as the password."),



  /**
   * Unable to read a password from file ''{0}'' because that file does not exist.
   */
  ERR_PW_FILE_READER_FILE_MISSING("Unable to read a password from file ''{0}'' because that file does not exist."),



  /**
   * Unable to read a password from file ''{0}'' because that path exists but does not represent a file.
   */
  ERR_PW_FILE_READER_FILE_NOT_FILE("Unable to read a password from file ''{0}'' because that path exists but does not represent a file."),



  /**
   * The provided password was incorrect.  Please provide the correct password used as the encryption key for file ''{0}'':
   */
  ERR_PW_FILE_READER_WRONG_PW("The provided password was incorrect.  Please provide the correct password used as the encryption key for file ''{0}'':"),



  /**
   * ERROR:  Unable to interactively read a password when no console is available (for example, if the output has been redirected, or if the tool is not running in an interactive shell).
   */
  ERR_PW_READER_CANNOT_READ_PW_WITH_NO_CONSOLE("ERROR:  Unable to interactively read a password when no console is available (for example, if the output has been redirected, or if the tool is not running in an interactive shell)."),



  /**
   * An error occurred while attempting to read a password from the terminal:  {0}
   */
  ERR_PW_READER_FAILURE("An error occurred while attempting to read a password from the terminal:  {0}"),



  /**
   * Unable to create a random characters value pattern component from string ''{0}'' because the string ''{1}'' cannot be parsed as an integer to represent the number of characters in the string to generate.
   */
  ERR_RANDOM_CHARS_VALUE_PATTERN_CANNOT_PARSE_LENGTH("Unable to create a random characters value pattern component from string ''{0}'' because the string ''{1}'' cannot be parsed as an integer to represent the number of characters in the string to generate."),



  /**
   * Unable to create a random characters value pattern component from string ''{0}'' because the set of characters to include in the string is empty.
   */
  ERR_RANDOM_CHARS_VALUE_PATTERN_EMPTY_CHAR_SET("Unable to create a random characters value pattern component from string ''{0}'' because the set of characters to include in the string is empty."),



  /**
   * Unable to create a random characters value pattern component from string ''{0}'' because the parsed length value of {1,number,0} is not greater than zero.
   */
  ERR_RANDOM_CHARS_VALUE_PATTERN_INVALID_LENGTH("Unable to create a random characters value pattern component from string ''{0}'' because the parsed length value of {1,number,0} is not greater than zero."),



  /**
   * Header key ''{0}'' already has a value of ''{1}''.  Cannot also set it to ''{2}''.
   */
  ERR_RATE_ADJUSTOR_DUPLICATE_HEADER_KEY("Header key ''{0}'' already has a value of ''{1}''.  Cannot also set it to ''{2}''."),



  /**
   * Header line ''{0}'' is malformed because the key is an empty string.
   */
  ERR_RATE_ADJUSTOR_HEADER_EMPTY_KEY("Header line ''{0}'' is malformed because the key is an empty string."),



  /**
   * Header line ''{0}'' is malformed because it does not include an equal sign to separate the header key from its value.
   */
  ERR_RATE_ADJUSTOR_HEADER_NO_EQUAL("Header line ''{0}'' is malformed because it does not include an equal sign to separate the header key from its value."),



  /**
   * Default duration ''{0}'' could not be parsed as a duration value:  {1}
   */
  ERR_RATE_ADJUSTOR_INVALID_DEFAULT_DURATION("Default duration ''{0}'' could not be parsed as a duration value:  {1}"),



  /**
   * Duration ''{0}'' from input line ''{1}'' could not be parsed as a duration value:  {2}
   */
  ERR_RATE_ADJUSTOR_INVALID_DURATION("Duration ''{0}'' from input line ''{1}'' could not be parsed as a duration value:  {2}"),



  /**
   * The rate input included an invalid value, ''{0}'', for the ''{1}'' property in the header.  This key must have one of these values:  {2}.
   */
  ERR_RATE_ADJUSTOR_INVALID_FORMAT("The rate input included an invalid value, ''{0}'', for the ''{1}'' property in the header.  This key must have one of these values:  {2}."),



  /**
   * The header included the following invalid keys:  {0}. The supported keys are {1}.
   */
  ERR_RATE_ADJUSTOR_INVALID_KEYS("The header included the following invalid keys:  {0}. The supported keys are {1}."),



  /**
   * Input line ''{0}'' has an invalid format.  All non-empty, non-comment lines must take the form ''absolute-rate-per-second, duration'' (e.g., ''1000, 1m'') or ''relative-rate, duration'' (e.g., ''2.5X, 1d''), where the rate is a multiplier on the baseline rate per second.  The duration can be omitted if a {1} value is provided in the header
   */
  ERR_RATE_ADJUSTOR_INVALID_LINE("Input line ''{0}'' has an invalid format.  All non-empty, non-comment lines must take the form ''absolute-rate-per-second, duration'' (e.g., ''1000, 1m'') or ''relative-rate, duration'' (e.g., ''2.5X, 1d''), where the rate is a multiplier on the baseline rate per second.  The duration can be omitted if a {1} value is provided in the header"),



  /**
   * Rate ''{0}'' from input line ''{1}'' could not be parsed as a floating point value
   */
  ERR_RATE_ADJUSTOR_INVALID_RATE("Rate ''{0}'' from input line ''{1}'' could not be parsed as a floating point value"),



  /**
   * The rate input did not include a value for the ''{0}'' property in the header.  This key is required, and must have one of these values:  {1}.  All header lines must appear at the beginning of the file and must take the form ''{2} key=value''.
   */
  ERR_RATE_ADJUSTOR_MISSING_FORMAT("The rate input did not include a value for the ''{0}'' property in the header.  This key is required, and must have one of these values:  {1}.  All header lines must appear at the beginning of the file and must take the form ''{2} key=value''."),



  /**
   * Unable to find a line comprised of the text ''{0}'' to denote the end of the header.
   */
  ERR_RATE_ADJUSTOR_NO_END_HEADER_FOUND("Unable to find a line comprised of the text ''{0}'' to denote the end of the header."),



  /**
   * Rate ''{0}'' from input line ''{1}'' specified a relative rate.  This is not supported because the target rate per second was not provided (e.g., using the ''--ratePerSecond'' command-line argument).
   */
  ERR_RATE_ADJUSTOR_RELATIVE_RATE_WITHOUT_BASELINE("Rate ''{0}'' from input line ''{1}'' specified a relative rate.  This is not supported because the target rate per second was not provided (e.g., using the ''--ratePerSecond'' command-line argument)."),



  /**
   * The provided value pattern string contained a back-reference component with an index of ''{0}'', but there are not that many components to reference at that point in the pattern.
   */
  ERR_REF_VALUE_PATTERN_INVALID_INDEX("The provided value pattern string contained a back-reference component with an index of ''{0}'', but there are not that many components to reference at that point in the pattern."),



  /**
   * The provided value pattern string contained a back-reference component with an invalid value of ''{0}''.
   */
  ERR_REF_VALUE_PATTERN_NOT_VALID("The provided value pattern string contained a back-reference component with an invalid value of ''{0}''."),



  /**
   * The provided value pattern string contained a back-reference component with an index of zero.  Back-reference indexes should start with one rather than zero.
   */
  ERR_REF_VALUE_PATTERN_ZERO_INDEX("The provided value pattern string contained a back-reference component with an index of zero.  Back-reference indexes should start with one rather than zero."),



  /**
   * Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because character ''{1}'' at position ''{2,number,0}'' was not a numeric digit.
   */
  ERR_RFC_3339_INVALID_DIGIT("Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because character ''{1}'' at position ''{2,number,0}'' was not a numeric digit."),



  /**
   * Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because character ''{1}'' at position ''{2,number,0}'' was not the expected ''{3}'' separator.
   */
  ERR_RFC_3339_INVALID_SEPARATOR("Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because character ''{1}'' at position ''{2,number,0}'' was not the expected ''{3}'' separator."),



  /**
   * Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because day of month value {1,number,0} is not valid for month value {2,number,0}.
   */
  ERR_RFC_3339_TIME_INVALID_DAY_FOR_MONTH("Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because day of month value {1,number,0} is not valid for month value {2,number,0}."),



  /**
   * Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because hour value {1,number,0} is not valid.
   */
  ERR_RFC_3339_TIME_INVALID_HOUR("Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because hour value {1,number,0} is not valid."),



  /**
   * Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because minute value {1,number,0} is not valid.
   */
  ERR_RFC_3339_TIME_INVALID_MINUTE("Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because minute value {1,number,0} is not valid."),



  /**
   * Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because month value {1,number,0} is not valid.
   */
  ERR_RFC_3339_TIME_INVALID_MONTH("Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because month value {1,number,0} is not valid."),



  /**
   * Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because second value {1,number,0} is not valid.
   */
  ERR_RFC_3339_TIME_INVALID_SECOND("Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because second value {1,number,0} is not valid."),



  /**
   * Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because it contained invalid sub-second character ''{1}'' at position {2,number,0}.
   */
  ERR_RFC_3339_TIME_INVALID_SUB_SECOND_CHAR("Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because it contained invalid sub-second character ''{1}'' at position {2,number,0}."),



  /**
   * Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because the time zone component was neither ''Z'' (to indicate the UTC time zone) nor a UTC offset in the form ''+HH:MM'' or ''-HH:MM''.
   */
  ERR_RFC_3339_TIME_INVALID_TZ("Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because the time zone component was neither ''Z'' (to indicate the UTC time zone) nor a UTC offset in the form ''+HH:MM'' or ''-HH:MM''."),



  /**
   * Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because it does not specify a time zone after the sub-second component.
   */
  ERR_RFC_3339_TIME_MISSING_TIME_ZONE_AFTER_SUB_SECOND("Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because it does not specify a time zone after the sub-second component."),



  /**
   * Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because the sub-second component does not contain any digits after the period.
   */
  ERR_RFC_3339_TIME_NO_SUB_SECOND_DIGITS("Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because the sub-second component does not contain any digits after the period."),



  /**
   * Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because the sub-second component has more than three digits.  This implementation does not support greater than millisecond-level precision.
   */
  ERR_RFC_3339_TIME_TOO_MANY_SUB_SECOND_DIGITS("Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because the sub-second component has more than three digits.  This implementation does not support greater than millisecond-level precision."),



  /**
   * Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because the provided string is too short.
   */
  ERR_RFC_3339_TIME_TOO_SHORT("Unable to parse ''{0}'' as a timestamp in the ISO 8601 format described in RFC 3339 because the provided string is too short."),



  /**
   * SASL option ''{0}'' is required with the {1} mechanism but was not provided.
   */
  ERR_SASL_MISSING_REQUIRED_OPTION("SASL option ''{0}'' is required with the {1} mechanism but was not provided."),



  /**
   * A password must be provided for a GSSAPI bind unless useTicketCache=true and requireCache=true options are provided.
   */
  ERR_SASL_OPTION_GSSAPI_PASSWORD_REQUIRED("A password must be provided for a GSSAPI bind unless useTicketCache=true and requireCache=true options are provided."),



  /**
   * SASL option ''{0}'' must have a value of either ''true'' or ''false''.
   */
  ERR_SASL_OPTION_MALFORMED_BOOLEAN_VALUE("SASL option ''{0}'' must have a value of either ''true'' or ''false''."),



  /**
   * Conflicting SASL mechanisms ''{0}'' and ''{1}'' were specified.
   */
  ERR_SASL_OPTION_MECH_CONFLICT("Conflicting SASL mechanisms ''{0}'' and ''{1}'' were specified."),



  /**
   * A password was provided, but SASL mechanism {0} does not accept a password.
   */
  ERR_SASL_OPTION_MECH_DOESNT_ACCEPT_PASSWORD("A password was provided, but SASL mechanism {0} does not accept a password."),



  /**
   * SASL mechanism {0} requires a password but none was provided.
   */
  ERR_SASL_OPTION_MECH_REQUIRES_PASSWORD("SASL mechanism {0} requires a password but none was provided."),



  /**
   * Unable to parse string ''{0}'' as a SASL option because it is missing an equal sign to separate the name from the value.
   */
  ERR_SASL_OPTION_MISSING_EQUAL("Unable to parse string ''{0}'' as a SASL option because it is missing an equal sign to separate the name from the value."),



  /**
   * SASL option ''{0}'' was provided multiple times.
   */
  ERR_SASL_OPTION_NOT_MULTI_VALUED("SASL option ''{0}'' was provided multiple times."),



  /**
   * No SASL mechanism was specified.
   */
  ERR_SASL_OPTION_NO_MECH("No SASL mechanism was specified."),



  /**
   * Unable to parse string ''{0}'' as a SASL option because it does not have an option name before the equal sign.
   */
  ERR_SASL_OPTION_STARTS_WITH_EQUAL("Unable to parse string ''{0}'' as a SASL option because it does not have an option name before the equal sign."),



  /**
   * SASL option ''{0}'' is not supported with the {1} mechanism.
   */
  ERR_SASL_OPTION_UNSUPPORTED_FOR_MECH("SASL option ''{0}'' is not supported with the {1} mechanism."),



  /**
   * Unsupported SASL mechanism ''{0}''.
   */
  ERR_SASL_OPTION_UNSUPPORTED_MECH("Unsupported SASL mechanism ''{0}''."),



  /**
   * SASL option ''{0}'' can only be used when the static password has not already been provided through some other means.
   */
  ERR_SASL_PROMPT_FOR_PROVIDED_PW("SASL option ''{0}'' can only be used when the static password has not already been provided through some other means."),



  /**
   * SASL option ''{0}'' can only have a value of ''true'' or ''false''.
   */
  ERR_SASL_PROMPT_FOR_STATIC_PW_BAD_VALUE("SASL option ''{0}'' can only have a value of ''true'' or ''false''."),



  /**
   * An error occurred while trying to retrieve the next line from file ''{0}'':  {1}
   */
  ERR_STREAM_FILE_VALUE_PATTERN_ERROR_GETTING_NEXT_VALUE("An error occurred while trying to retrieve the next line from file ''{0}'':  {1}"),



  /**
   * Unable to create a stream file value pattern component for file ''{0}'' because that file is empty.
   */
  ERR_STREAM_FILE_VALUE_PATTERN_FILE_EMPTY("Unable to create a stream file value pattern component for file ''{0}'' because that file is empty."),



  /**
   * Unable to access data in file ''{0}'' for use in the value pattern:  {1}
   */
  ERR_STREAM_FILE_VALUE_PATTERN_NOT_USABLE("Unable to access data in file ''{0}'' for use in the value pattern:  {1}"),



  /**
   * Unable to create a stream file value pattern component for file ''{0}'' because that file does not exist.
   */
  ERR_STREAM_FILE_VALUE_PATTERN_PATH_MISSING("Unable to create a stream file value pattern component for file ''{0}'' because that file does not exist."),



  /**
   * Unable to create a stream file value pattern component for file ''{0}'' because that path exists but does not reference a file.
   */
  ERR_STREAM_FILE_VALUE_PATTERN_PATH_NOT_FILE("Unable to create a stream file value pattern component for file ''{0}'' because that path exists but does not reference a file."),



  /**
   * Extended operations are not supported for LDAPInterface instances of type {0}.
   */
  ERR_SUBTREE_DELETER_INTERFACE_EXTOP_NOT_SUPPORTED("Extended operations are not supported for LDAPInterface instances of type {0}."),



  /**
   * The ''Who Am I?'' extended operation returned an authorization ID of ''{0}'', which does not start with ''dn:'' and therefore cannot be used to determine the DN of the authenticated user.  As such, it is not possible to determine the DN of the user that should be permitted to bypass the subtree accessibility restriction.
   */
  ERR_SUBTREE_DELETER_INTERFACE_WHO_AM_I_AUTHZ_ID_NOT_DN("The ''Who Am I?'' extended operation returned an authorization ID of ''{0}'', which does not start with ''dn:'' and therefore cannot be used to determine the DN of the authenticated user.  As such, it is not possible to determine the DN of the user that should be permitted to bypass the subtree accessibility restriction."),



  /**
   * The response to a search request with base DN ''{0}'' filter ''{1}'' did not include the expected simple paged results response control.
   */
  ERR_SUBTREE_DELETER_MISSING_PAGED_RESULTS_RESPONSE("The response to a search request with base DN ''{0}'' filter ''{1}'' did not include the expected simple paged results response control."),



  /**
   * Received a search result reference with URL(s) {0} in response to a search request with base DN ''{1}'' and filter ''{2}''.
   */
  ERR_SUBTREE_DELETER_SEARCH_LISTENER_REFERENCE_RETURNED("Received a search result reference with URL(s) {0} in response to a search request with base DN ''{1}'' and filter ''{2}''."),



  /**
   * Not attempting to delete DN ''{0}'' because an error occurred while attempting to delete descendant entry ''{1}''.
   */
  ERR_SUBTREE_DELETER_SKIPPING_UNDELETABLE_ANCESTOR("Not attempting to delete DN ''{0}'' because an error occurred while attempting to delete descendant entry ''{1}''."),



  /**
   * Entry ''{0}'' was found to contain attribute ''{1}'' when that attribute was expected to be missing.
   */
  ERR_TEST_ATTR_EXISTS("Entry ''{0}'' was found to contain attribute ''{1}'' when that attribute was expected to be missing."),



  /**
   * Entry ''{0}'' was found to contain attribute ''{1}'' with value(s) {2} when that attribute was expected to be missing.
   */
  ERR_TEST_ATTR_EXISTS_WITH_VALUES("Entry ''{0}'' was found to contain attribute ''{1}'' with value(s) {2} when that attribute was expected to be missing."),



  /**
   * Entry ''{0}'' exists but does not have any values for attribute ''{1}''.
   */
  ERR_TEST_ATTR_MISSING("Entry ''{0}'' exists but does not have any values for attribute ''{1}''."),



  /**
   * Entry ''{0}'' exists and contains attribute ''{1}'' with value(s) {2}, but does not include expected value(s) {3}.
   */
  ERR_TEST_ATTR_MISSING_VALUE("Entry ''{0}'' exists and contains attribute ''{1}'' with value(s) {2}, but does not include expected value(s) {3}."),



  /**
   * Result {0} was expected to contain a diagnostic message value of ''{1}'' but ''{0}'' was found instead.
   */
  ERR_TEST_DIAGNOSTIC_MESSAGE_MISMATCH("Result {0} was expected to contain a diagnostic message value of ''{1}'' but ''{0}'' was found instead."),



  /**
   * The provided DN values ''{0}'' and ''{1}'' are not equal.
   */
  ERR_TEST_DNS_NOT_EQUAL("The provided DN values ''{0}'' and ''{1}'' are not equal."),



  /**
   * Entry ''{0}'' was found to exist in the server but does not match expected filter ''{1}''.
   */
  ERR_TEST_ENTRY_DOES_NOT_MATCH_FILTER("Entry ''{0}'' was found to exist in the server but does not match expected filter ''{1}''."),



  /**
   * Entry ''{0}'' was found in the server but was expected to be missing.
   */
  ERR_TEST_ENTRY_EXISTS("Entry ''{0}'' was found in the server but was expected to be missing."),



  /**
   * Search result entry {0} was not expected to have any control with OID ''{1}'' but one was found.
   */
  ERR_TEST_ENTRY_HAS_CONTROL("Search result entry {0} was not expected to have any control with OID ''{1}'' but one was found."),



  /**
   * Entry ''{0}'' does not exist in the server.
   */
  ERR_TEST_ENTRY_MISSING("Entry ''{0}'' does not exist in the server."),



  /**
   * Search result entry {0} was expected to have at least one control with OID ''{1}'' but none was found.
   */
  ERR_TEST_ENTRY_MISSING_CONTROL("Search result entry {0} was expected to have at least one control with OID ''{1}'' but none was found."),



  /**
   * Result {0} was expected to contain a matched DN value of ''{1}'' but ''{0}'' was found instead.
   */
  ERR_TEST_MATCHED_DN_MISMATCH("Result {0} was expected to contain a matched DN value of ''{1}'' but ''{0}'' was found instead."),



  /**
   * Result {0} had one of the unacceptable result codes {1}.
   */
  ERR_TEST_MULTI_RESULT_CODE_FOUND("Result {0} had one of the unacceptable result codes {1}."),



  /**
   * Result {0} did not have any of the acceptable result codes {1}.
   */
  ERR_TEST_MULTI_RESULT_CODE_MISSING("Result {0} did not have any of the acceptable result codes {1}."),



  /**
   * Result {0} received from processing request {1} had one of the unacceptable result codes {2}.
   */
  ERR_TEST_PROCESSING_MULTI_RESULT_CODE_FOUND("Result {0} received from processing request {1} had one of the unacceptable result codes {2}."),



  /**
   * Result {0} received from processing request {1} did not have any of the acceptable result codes {2}.
   */
  ERR_TEST_PROCESSING_MULTI_RESULT_CODE_MISSING("Result {0} received from processing request {1} did not have any of the acceptable result codes {2}."),



  /**
   * Result {0} received from processing request {1} had an unacceptable result code of ''{2}''.
   */
  ERR_TEST_PROCESSING_SINGLE_RESULT_CODE_FOUND("Result {0} received from processing request {1} had an unacceptable result code of ''{2}''."),



  /**
   * Result {0} received from processing request {1} did not have the expected result code of {2}.
   */
  ERR_TEST_PROCESSING_SINGLE_RESULT_CODE_MISSING("Result {0} received from processing request {1} did not have the expected result code of {2}."),



  /**
   * Search result reference {0} was not expected to have any control with OID ''{1}'' but one was found.
   */
  ERR_TEST_REF_HAS_CONTROL("Search result reference {0} was not expected to have any control with OID ''{1}'' but one was found."),



  /**
   * Search result reference {0} was expected to have at least one control with OID ''{1}'' but none was found.
   */
  ERR_TEST_REF_MISSING_CONTROL("Search result reference {0} was expected to have at least one control with OID ''{1}'' but none was found."),



  /**
   * Result {0} was not expected to contain a diagnostic message but a value of ''{1}'' was found.
   */
  ERR_TEST_RESULT_CONTAINS_DIAGNOSTIC_MESSAGE("Result {0} was not expected to contain a diagnostic message but a value of ''{1}'' was found."),



  /**
   * Result {0} was not expected to contain a matched DN but a value of ''{1}'' was found.
   */
  ERR_TEST_RESULT_CONTAINS_MATCHED_DN("Result {0} was not expected to contain a matched DN but a value of ''{1}'' was found."),



  /**
   * Result {0} was not expected to have any control with OID ''{1}'' but one was found.
   */
  ERR_TEST_RESULT_HAS_CONTROL("Result {0} was not expected to have any control with OID ''{1}'' but one was found."),



  /**
   * Result {0} was not expected to have any referral URLs but one or more were found.
   */
  ERR_TEST_RESULT_HAS_REFERRAL("Result {0} was not expected to have any referral URLs but one or more were found."),



  /**
   * Result {0} was expected to have at least one control with OID ''{1}'' but none was found.
   */
  ERR_TEST_RESULT_MISSING_CONTROL("Result {0} was expected to have at least one control with OID ''{1}'' but none was found."),



  /**
   * Result {0} was expected to contain a diagnostic message but none was found.
   */
  ERR_TEST_RESULT_MISSING_DIAGNOSTIC_MESSAGE("Result {0} was expected to contain a diagnostic message but none was found."),



  /**
   * Result {0} was expected to contain a diagnostic message of ''{1}'' but none was found.
   */
  ERR_TEST_RESULT_MISSING_EXPECTED_DIAGNOSTIC_MESSAGE("Result {0} was expected to contain a diagnostic message of ''{1}'' but none was found."),



  /**
   * Result {0} was expected to contain a matched DN of ''{1}'' but none was found.
   */
  ERR_TEST_RESULT_MISSING_EXPECTED_MATCHED_DN("Result {0} was expected to contain a matched DN of ''{1}'' but none was found."),



  /**
   * Result {0} was expected to contain a matched DN but none was found.
   */
  ERR_TEST_RESULT_MISSING_MATCHED_DN("Result {0} was expected to contain a matched DN but none was found."),



  /**
   * Result {0} was expected to have at least one referral URL but did not contain any.
   */
  ERR_TEST_RESULT_MISSING_REFERRAL("Result {0} was expected to have at least one referral URL but did not contain any."),



  /**
   * The search was not expected to have returned any entries, but result {0} indicates that the number of entries returned was {1}.
   */
  ERR_TEST_SEARCH_ENTRIES_RETURNED("The search was not expected to have returned any entries, but result {0} indicates that the number of entries returned was {1}."),



  /**
   * The search was expected to have returned {0} entries, but result {1} indicates that the number of entries returned was {2}.
   */
  ERR_TEST_SEARCH_ENTRY_COUNT_MISMATCH_MULTI_EXPECTED("The search was expected to have returned {0} entries, but result {1} indicates that the number of entries returned was {2}."),



  /**
   * The search was expected to have returned one entry, but result {0} indicates that the number of entries returned was {1}.
   */
  ERR_TEST_SEARCH_ENTRY_COUNT_MISMATCH_ONE_EXPECTED("The search was expected to have returned one entry, but result {0} indicates that the number of entries returned was {1}."),



  /**
   * The search with result {0} was expected to have returned entry ''{1}'', but either was not included in the set of entries that were returned, or a search result listener was used for the search that makes it impossible to determine what entries were returned.
   */
  ERR_TEST_SEARCH_ENTRY_NOT_RETURNED("The search with result {0} was expected to have returned entry ''{1}'', but either was not included in the set of entries that were returned, or a search result listener was used for the search that makes it impossible to determine what entries were returned."),



  /**
   * The search was expected to have returned one or more entries, but result {0} indicates that none were returned.
   */
  ERR_TEST_SEARCH_NO_ENTRIES_RETURNED("The search was expected to have returned one or more entries, but result {0} indicates that none were returned."),



  /**
   * The search was expected to have returned one or more references, but result {0} indicates that none were returned.
   */
  ERR_TEST_SEARCH_NO_REFS_RETURNED("The search was expected to have returned one or more references, but result {0} indicates that none were returned."),



  /**
   * The search was not expected to have returned any references, but result {0} indicates that the number of references returned was {1}.
   */
  ERR_TEST_SEARCH_REFS_RETURNED("The search was not expected to have returned any references, but result {0} indicates that the number of references returned was {1}."),



  /**
   * The search was expected to have returned {0} references, but result {1} indicates that the number of references returned was {2}.
   */
  ERR_TEST_SEARCH_REF_COUNT_MISMATCH_MULTI_EXPECTED("The search was expected to have returned {0} references, but result {1} indicates that the number of references returned was {2}."),



  /**
   * The search was expected to have returned one reference, but result {0} indicates that the number of references returned was {1}.
   */
  ERR_TEST_SEARCH_REF_COUNT_MISMATCH_ONE_EXPECTED("The search was expected to have returned one reference, but result {0} indicates that the number of references returned was {1}."),



  /**
   * Result {0} had an unacceptable result code of ''{1}''.
   */
  ERR_TEST_SINGLE_RESULT_CODE_FOUND("Result {0} had an unacceptable result code of ''{1}''."),



  /**
   * Result {0} did not have the expected result code of ''{1}''.
   */
  ERR_TEST_SINGLE_RESULT_CODE_MISSING("Result {0} did not have the expected result code of ''{1}''."),



  /**
   * Entry ''{0}'' was found to contain attribute ''{1}'' with value ''{2}'' when that value was expected to be missing.
   */
  ERR_TEST_VALUE_EXISTS("Entry ''{0}'' was found to contain attribute ''{1}'' with value ''{2}'' when that value was expected to be missing."),



  /**
   * Entry ''{0}'' exists and contains attribute ''{1}'', but that attribute does not include value ''{2}''.
   */
  ERR_TEST_VALUE_MISSING("Entry ''{0}'' exists and contains attribute ''{1}'', but that attribute does not include value ''{2}''."),



  /**
   * The provided string value ''{0}'' does not represent a valid LDAP distinguished name:  {1}
   */
  ERR_TEST_VALUE_NOT_VALID_DN("The provided string value ''{0}'' does not represent a valid LDAP distinguished name:  {1}"),



  /**
   * Unable to create a timestamp value pattern component from string ''{0}'' because timestamp format string ''{1}'' is not valid for use with the Java SimpleDateFormat class.
   */
  ERR_TIMESTAMP_VALUE_PATTERN_CANNOT_PARSE_FORMAT_STRING("Unable to create a timestamp value pattern component from string ''{0}'' because timestamp format string ''{1}'' is not valid for use with the Java SimpleDateFormat class."),



  /**
   * Unable to create a timestamp value pattern component from string ''{0}'' because maximum bound value ''{1}'' could not be parsed as a generalized time value:  {2}
   */
  ERR_TIMESTAMP_VALUE_PATTERN_CANNOT_PARSE_MAX("Unable to create a timestamp value pattern component from string ''{0}'' because maximum bound value ''{1}'' could not be parsed as a generalized time value:  {2}"),



  /**
   * Unable to create a timestamp value pattern component from string ''{0}'' because minimum bound value ''{1}'' could not be parsed as a generalized time value:  {2}
   */
  ERR_TIMESTAMP_VALUE_PATTERN_CANNOT_PARSE_MIN("Unable to create a timestamp value pattern component from string ''{0}'' because minimum bound value ''{1}'' could not be parsed as a generalized time value:  {2}"),



  /**
   * Unable to create a timestamp value pattern component from string ''{0}'' because the format element is not the last element in the string.
   */
  ERR_TIMESTAMP_VALUE_PATTERN_FORMAT_NOT_AT_END("Unable to create a timestamp value pattern component from string ''{0}'' because the format element is not the last element in the string."),



  /**
   * Unable to create a timestamp value pattern component from string ''{0}''.  If the string is not just ''timestamp'', then it must start with ''timestamp:min='' or ''timestamp:format=''.
   */
  ERR_TIMESTAMP_VALUE_PATTERN_MALFORMED("Unable to create a timestamp value pattern component from string ''{0}''.  If the string is not just ''timestamp'', then it must start with ''timestamp:min='' or ''timestamp:format=''."),



  /**
   * Unable to create a timestamp value pattern component from string ''{0}'' because minimum bound value ''{1}'' is greater than or equal to maximum bound value ''{2}''.  The minimum bound value must be less than the maximum bound value.
   */
  ERR_TIMESTAMP_VALUE_PATTERN_MIN_NOT_LT_MAX("Unable to create a timestamp value pattern component from string ''{0}'' because minimum bound value ''{1}'' is greater than or equal to maximum bound value ''{2}''.  The minimum bound value must be less than the maximum bound value."),



  /**
   * Unable to create a timestamp value pattern component from string ''{0}'' because it specifies a minimum timestamp bound without a maximum timestamp bound.
   */
  ERR_TIMESTAMP_VALUE_PATTERN_MIN_WITHOUT_MAX("Unable to create a timestamp value pattern component from string ''{0}'' because it specifies a minimum timestamp bound without a maximum timestamp bound."),



  /**
   * An empty array was provided where a non-null and non-empty array is required.  Thread stack trace:  {0}
   */
  ERR_VALIDATOR_ARRAY_EMPTY("An empty array was provided where a non-null and non-empty array is required.  Thread stack trace:  {0}"),



  /**
   * An empty array was provided where a non-null and non-empty array is required.  Message:  {0}.  Thread stack trace:  {1}
   */
  ERR_VALIDATOR_ARRAY_EMPTY_CUSTOM_MESSAGE("An empty array was provided where a non-null and non-empty array is required.  Message:  {0}.  Thread stack trace:  {1}"),



  /**
   * A null array was provided where a non-null and non-empty array is required.  Thread stack trace:  {0}
   */
  ERR_VALIDATOR_ARRAY_NULL("A null array was provided where a non-null and non-empty array is required.  Thread stack trace:  {0}"),



  /**
   * A null array was provided where a non-null and non-empty array is required.  Message:  {0}.  Thread stack trace:  {1}
   */
  ERR_VALIDATOR_ARRAY_NULL_CUSTOM_MESSAGE("A null array was provided where a non-null and non-empty array is required.  Message:  {0}.  Thread stack trace:  {1}"),



  /**
   * An empty character sequence was provided where a non-null and non-empty character sequence is required.  Thread stack trace:  {0}
   */
  ERR_VALIDATOR_CHAR_SEQUENCE_EMPTY("An empty character sequence was provided where a non-null and non-empty character sequence is required.  Thread stack trace:  {0}"),



  /**
   * An empty character sequence was provided where a non-null and non-empty character sequence is required.  Message:  {0}.  Thread stack trace:  {1}
   */
  ERR_VALIDATOR_CHAR_SEQUENCE_EMPTY_CUSTOM_MESSAGE("An empty character sequence was provided where a non-null and non-empty character sequence is required.  Message:  {0}.  Thread stack trace:  {1}"),



  /**
   * A null character sequence was provided where a non-null and non-empty character sequence is required.  Thread stack trace:  {0}
   */
  ERR_VALIDATOR_CHAR_SEQUENCE_NULL("A null character sequence was provided where a non-null and non-empty character sequence is required.  Thread stack trace:  {0}"),



  /**
   * A null character sequence was provided where a non-null and non-empty character sequence is required.  Message:  {0}.  Thread stack trace:  {1}
   */
  ERR_VALIDATOR_CHAR_SEQUENCE_NULL_CUSTOM_MESSAGE("A null character sequence was provided where a non-null and non-empty character sequence is required.  Message:  {0}.  Thread stack trace:  {1}"),



  /**
   * An empty collection was provided where a non-null and non-empty collection is required.  Thread stack trace:  {0}
   */
  ERR_VALIDATOR_COLLECTION_EMPTY("An empty collection was provided where a non-null and non-empty collection is required.  Thread stack trace:  {0}"),



  /**
   * An empty collection was provided where a non-null and non-empty collection is required.  Message:  {0}.  Thread stack trace:  {1}
   */
  ERR_VALIDATOR_COLLECTION_EMPTY_CUSTOM_MESSAGE("An empty collection was provided where a non-null and non-empty collection is required.  Message:  {0}.  Thread stack trace:  {1}"),



  /**
   * A null collection was provided where a non-null and non-empty collection is required.  Thread stack trace:  {0}
   */
  ERR_VALIDATOR_COLLECTION_NULL("A null collection was provided where a non-null and non-empty collection is required.  Thread stack trace:  {0}"),



  /**
   * A null collection was provided where a non-null and non-empty collection is required.  Message:  {0}.  Thread stack trace:  {1}
   */
  ERR_VALIDATOR_COLLECTION_NULL_CUSTOM_MESSAGE("A null collection was provided where a non-null and non-empty collection is required.  Message:  {0}.  Thread stack trace:  {1}"),



  /**
   * {0}.  Thread stack trace:  {1}
   */
  ERR_VALIDATOR_FAILURE_CUSTOM_MESSAGE("{0}.  Thread stack trace:  {1}"),



  /**
   * A result of true was found for a condition which the LDAP SDK requires to be false.  Thread stack trace {0}
   */
  ERR_VALIDATOR_FALSE_CHECK_FAILURE("A result of true was found for a condition which the LDAP SDK requires to be false.  Thread stack trace {0}"),



  /**
   * An empty map was provided where a non-null and non-empty map is required.  Thread stack trace:  {0}
   */
  ERR_VALIDATOR_MAP_EMPTY("An empty map was provided where a non-null and non-empty map is required.  Thread stack trace:  {0}"),



  /**
   * An empty map was provided where a non-null and non-empty map is required.  Message:  {0}.  Thread stack trace:  {1}
   */
  ERR_VALIDATOR_MAP_EMPTY_CUSTOM_MESSAGE("An empty map was provided where a non-null and non-empty map is required.  Message:  {0}.  Thread stack trace:  {1}"),



  /**
   * A null map was provided where a non-null and non-empty map is required.  Thread stack trace:  {0}
   */
  ERR_VALIDATOR_MAP_NULL("A null map was provided where a non-null and non-empty map is required.  Thread stack trace:  {0}"),



  /**
   * A null map was provided where a non-null and non-empty map is required.  Message:  {0}.  Thread stack trace:  {1}
   */
  ERR_VALIDATOR_MAP_NULL_CUSTOM_MESSAGE("A null map was provided where a non-null and non-empty map is required.  Message:  {0}.  Thread stack trace:  {1}"),



  /**
   * A null object was provided where a non-null object is required (non-null index {0,number,0}).  Thread stack trace:  {1}
   */
  ERR_VALIDATOR_NULL_CHECK_FAILURE("A null object was provided where a non-null object is required (non-null index {0,number,0}).  Thread stack trace:  {1}"),



  /**
   * A result of false was found for a condition which the LDAP SDK requires to be true.  Thread stack trace {0}
   */
  ERR_VALIDATOR_TRUE_CHECK_FAILURE("A result of false was found for a condition which the LDAP SDK requires to be true.  Thread stack trace {0}"),



  /**
   * The specified file does not contain any data.
   */
  ERR_VALUE_PATTERN_COMPONENT_EMPTY_FILE("The specified file does not contain any data."),



  /**
   * The provided value pattern string contained a numeric range starting at position {0,number,0} which contained a zero-length format string.
   */
  ERR_VALUE_PATTERN_EMPTY_FORMAT("The provided value pattern string contained a numeric range starting at position {0,number,0} which contained a zero-length format string."),



  /**
   * The provided value pattern string contained a numeric range starting at position {0,number,0} which contained a zero-length increment.
   */
  ERR_VALUE_PATTERN_EMPTY_INCREMENT("The provided value pattern string contained a numeric range starting at position {0,number,0} which contained a zero-length increment."),



  /**
   * The provided value pattern string contained a numeric range starting at position {0,number,0} which contained a zero-length lower bound.
   */
  ERR_VALUE_PATTERN_EMPTY_LOWER_BOUND("The provided value pattern string contained a numeric range starting at position {0,number,0} which contained a zero-length lower bound."),



  /**
   * The provided value pattern string contained a numeric range starting at position {0,number,0} which contained a zero-length upper bound.
   */
  ERR_VALUE_PATTERN_EMPTY_UPPER_BOUND("The provided value pattern string contained a numeric range starting at position {0,number,0} which contained a zero-length upper bound."),



  /**
   * The provided value pattern string contained an invalid character ''{0}'' at position {1,number,0}.
   */
  ERR_VALUE_PATTERN_INVALID_CHARACTER("The provided value pattern string contained an invalid character ''{0}'' at position {1,number,0}."),



  /**
   * The provided value pattern string contained a numeric range starting at position {0,number,0} which did not contain either a dash or colon to separate the lower bound from the upper bound.
   */
  ERR_VALUE_PATTERN_NO_DELIMITER("The provided value pattern string contained a numeric range starting at position {0,number,0} which did not contain either a dash or colon to separate the lower bound from the upper bound."),



  /**
   * The provided value pattern string contained an unmatched closing brace at position {0,number,0}.
   */
  ERR_VALUE_PATTERN_UNMATCHED_CLOSE("The provided value pattern string contained an unmatched closing brace at position {0,number,0}."),



  /**
   * The provided value pattern string contained an unmatched opening brace at position {0,number,0}.
   */
  ERR_VALUE_PATTERN_UNMATCHED_OPEN("The provided value pattern string contained an unmatched opening brace at position {0,number,0}."),



  /**
   * The provided value pattern string contained a numeric range starting at position {0,number,0} with a value that is outside the acceptable range.  Values must be between {1,number,0} and {2,number,0}.
   */
  ERR_VALUE_PATTERN_VALUE_NOT_LONG("The provided value pattern string contained a numeric range starting at position {0,number,0} with a value that is outside the acceptable range.  Values must be between {1,number,0} and {2,number,0}."),



  /**
   * Arguments obtained from properties file ''{0}'':
   */
  INFO_CL_TOOL_ARGS_FROM_PROPERTIES_FILE("Arguments obtained from properties file ''{0}'':"),



  /**
   * Indicates that the tool should append to the file specified by the {0} argument if it already exists.  If this argument is not provided and the output file already exists, it will be overwritten.
   */
  INFO_CL_TOOL_DESCRIPTION_APPEND_TO_OUTPUT_FILE("Indicates that the tool should append to the file specified by the {0} argument if it already exists.  If this argument is not provided and the output file already exists, it will be overwritten."),



  /**
   * Display usage information for this program.
   */
  INFO_CL_TOOL_DESCRIPTION_HELP("Display usage information for this program."),



  /**
   * Display the names and descriptions of the supported subcommands.
   */
  INFO_CL_TOOL_DESCRIPTION_HELP_SUBCOMMANDS("Display the names and descriptions of the supported subcommands."),



  /**
   * Launch the tool in interactive mode.
   */
  INFO_CL_TOOL_DESCRIPTION_INTERACTIVE("Launch the tool in interactive mode."),



  /**
   * Write all standard output and standard error messages to the specified file instead of to the console.
   */
  INFO_CL_TOOL_DESCRIPTION_OUTPUT_FILE("Write all standard output and standard error messages to the specified file instead of to the console."),



  /**
   * Write all standard output and standard error messages to the console as well as to the specified output file.  The {0} argument must also be provided.
   */
  INFO_CL_TOOL_DESCRIPTION_TEE_OUTPUT("Write all standard output and standard error messages to the console as well as to the specified output file.  The {0} argument must also be provided."),



  /**
   * Display version information for this program.
   */
  INFO_CL_TOOL_DESCRIPTION_VERSION("Display version information for this program."),



  /**
   * Examples
   */
  INFO_CL_TOOL_LABEL_EXAMPLES("Examples"),



  /**
   * Use ''{0} '{'subcommand'}' --help'' to get more detailed help for a specific subcommand.
   */
  INFO_CL_TOOL_USE_SUBCOMMAND_HELP("Use ''{0} '{'subcommand'}' --help'' to get more detailed help for a specific subcommand."),



  /**
   * Successfully wrote properties file {0}.
   */
  INFO_CL_TOOL_WROTE_PROPERTIES_FILE("Successfully wrote properties file {0}."),



  /**
   * Timestamp
   */
  INFO_COLUMN_LABEL_TIMESTAMP("Timestamp"),



  /**
   * The current value for this argument is:
   */
  INFO_INTERACTIVE_ARG_DESC_CURRENT_VALUE("The current value for this argument is:"),



  /**
   * The current values for this argument are:
   */
  INFO_INTERACTIVE_ARG_DESC_CURRENT_VALUES("The current values for this argument are:"),



  /**
   * Would you like to alter the values of any command-line arguments for the tool?
   */
  INFO_INTERACTIVE_ARG_MENU_PROMPT("Would you like to alter the values of any command-line arguments for the tool?"),



  /**
   * This argument requires at least one value.
   */
  INFO_INTERACTIVE_ARG_PROMPT_AT_LEAST_ONE_REQUIRED("This argument requires at least one value."),



  /**
   * Value Constraints:
   */
  INFO_INTERACTIVE_ARG_PROMPT_CONSTRAINTS("Value Constraints:"),



  /**
   * Description:
   */
  INFO_INTERACTIVE_ARG_PROMPT_DESCRIPTION("Description:"),



  /**
   * Enter a new value
   */
  INFO_INTERACTIVE_ARG_PROMPT_NEW_VALUE("Enter a new value"),



  /**
   * Enter the desired new value(s), pressing ENTER on an empty line to indicate no more values are needed.
   */
  INFO_INTERACTIVE_ARG_PROMPT_NEW_VALUES("Enter the desired new value(s), pressing ENTER on an empty line to indicate no more values are needed."),



  /**
   * This argument requires a value.
   */
  INFO_INTERACTIVE_ARG_PROMPT_SINGLE_REQUIRED("This argument requires a value."),



  /**
   * Specify one or more new values for argument ''{0}''
   */
  INFO_INTERACTIVE_ARG_PROMPT_SPECIFY_MULTIPLE_VALUES("Specify one or more new values for argument ''{0}''"),



  /**
   * Specify a new value for argument ''{0}''
   */
  INFO_INTERACTIVE_ARG_PROMPT_SPECIFY_SINGLE_VALUE("Specify a new value for argument ''{0}''"),



  /**
   * Confirm the value
   */
  INFO_INTERACTIVE_ARG_PROMPT_VALUE_CONFIRM("Confirm the value"),



  /**
   * No
   */
  INFO_INTERACTIVE_CHOICE_NO("No"),



  /**
   * Yes
   */
  INFO_INTERACTIVE_CHOICE_YES("Yes"),



  /**
   * Launching {0} in interactive mode.
   */
  INFO_INTERACTIVE_LAUNCHING("Launching {0} in interactive mode."),



  /**
   * Enter the authentication ID to use to authenticate (typically ''dn:'' followed by the user DN or ''u:'' followed by the username)
   */
  INFO_INTERACTIVE_LDAP_AUTH_AUTHID_PROMPT("Enter the authentication ID to use to authenticate (typically ''dn:'' followed by the user DN or ''u:'' followed by the username)"),



  /**
   * Enter the authorization ID to use (typically in the same format as the authentication ID), or simply press ENTER if you do not wish to use an alternate authorization identity
   */
  INFO_INTERACTIVE_LDAP_AUTH_AUTHZID_PROMPT("Enter the authorization ID to use (typically in the same format as the authentication ID), or simply press ENTER if you do not wish to use an alternate authorization identity"),



  /**
   * Enter the DN of the user as whom you wish to bind, or simply press ENTER for anonymous simple authentication
   */
  INFO_INTERACTIVE_LDAP_AUTH_BIND_DN_PROMPT("Enter the DN of the user as whom you wish to bind, or simply press ENTER for anonymous simple authentication"),



  /**
   * Do not authenticate
   */
  INFO_INTERACTIVE_LDAP_AUTH_OPTION_NONE("Do not authenticate"),



  /**
   * Use SASL authentication
   */
  INFO_INTERACTIVE_LDAP_AUTH_OPTION_SASL("Use SASL authentication"),



  /**
   * Use simple authentication
   */
  INFO_INTERACTIVE_LDAP_AUTH_OPTION_SIMPLE("Use simple authentication"),



  /**
   * How do you wish to authenticate to the directory server?
   */
  INFO_INTERACTIVE_LDAP_AUTH_PROMPT("How do you wish to authenticate to the directory server?"),



  /**
   * Enter the password for the user
   */
  INFO_INTERACTIVE_LDAP_AUTH_PW_PROMPT("Enter the password for the user"),



  /**
   * Enter the authentication realm, or simply press ENTER for no realm
   */
  INFO_INTERACTIVE_LDAP_AUTH_REALM_PROMPT("Enter the authentication realm, or simply press ENTER for no realm"),



  /**
   * Which SASL mechanism would you like to use?
   */
  INFO_INTERACTIVE_LDAP_AUTH_SASL_PROMPT("Which SASL mechanism would you like to use?"),



  /**
   * Do you wish to use the client certificate to authenticate using SASL EXTERNAL?
   */
  INFO_INTERACTIVE_LDAP_CERT_AUTH_PROMPT("Do you wish to use the client certificate to authenticate using SASL EXTERNAL?"),



  /**
   * Enter the nickname for the desired certificate in the certificate key store (or just press ENTER if there is only one client certificate in the key store)
   */
  INFO_INTERACTIVE_LDAP_CERT_NICKNAME_PROMPT("Enter the nickname for the desired certificate in the certificate key store (or just press ENTER if there is only one client certificate in the key store)"),



  /**
   * Yes, from a Java JKS key store
   */
  INFO_INTERACTIVE_LDAP_CLIENT_CERT_OPTION_JKS("Yes, from a Java JKS key store"),



  /**
   * No
   */
  INFO_INTERACTIVE_LDAP_CLIENT_CERT_OPTION_NO("No"),



  /**
   * Yes, from a PKCS #12 key store
   */
  INFO_INTERACTIVE_LDAP_CLIENT_CERT_OPTION_PKCS12("Yes, from a PKCS #12 key store"),



  /**
   * Do you want to present a client certificate to the directory server?
   */
  INFO_INTERACTIVE_LDAP_CLIENT_CERT_PROMPT("Do you want to present a client certificate to the directory server?"),



  /**
   * Enter the path to the key store file
   */
  INFO_INTERACTIVE_LDAP_KEYSTORE_PATH_PROMPT("Enter the path to the key store file"),



  /**
   * Enter the PIN needed to access the contents of the key store (or just press ENTER if no PIN is required)
   */
  INFO_INTERACTIVE_LDAP_KEYSTORE_PIN_PROMPT("Enter the PIN needed to access the contents of the key store (or just press ENTER if no PIN is required)"),



  /**
   * Enter the address of the directory server
   */
  INFO_INTERACTIVE_LDAP_PROMPT_HOST("Enter the address of the directory server"),



  /**
   * Enter the port on which to communicate with the directory server
   */
  INFO_INTERACTIVE_LDAP_PROMPT_PORT("Enter the port on which to communicate with the directory server"),



  /**
   * Would you like to re-enter LDAP connection settings?
   */
  INFO_INTERACTIVE_LDAP_RETRY_PROMPT("Would you like to re-enter LDAP connection settings?"),



  /**
   * CRAM-MD5
   */
  INFO_INTERACTIVE_LDAP_SASL_OPTION_CRAM_MD5("CRAM-MD5"),



  /**
   * DIGEST-MD5
   */
  INFO_INTERACTIVE_LDAP_SASL_OPTION_DIGEST_MD5("DIGEST-MD5"),



  /**
   * PLAIN
   */
  INFO_INTERACTIVE_LDAP_SASL_OPTION_PLAIN("PLAIN"),



  /**
   * No.  Use unencrypted LDAP.
   */
  INFO_INTERACTIVE_LDAP_SECURITY_OPTION_NONE("No.  Use unencrypted LDAP."),



  /**
   * Yes.  Use SSL with default trust settings.
   */
  INFO_INTERACTIVE_LDAP_SECURITY_OPTION_SSL_DEFAULT("Yes.  Use SSL with default trust settings."),



  /**
   * Yes.  Use SSL with a manually specified configuration.
   */
  INFO_INTERACTIVE_LDAP_SECURITY_OPTION_SSL_MANUAL("Yes.  Use SSL with a manually specified configuration."),



  /**
   * Yes.  Use StartTLS with default trust settings.
   */
  INFO_INTERACTIVE_LDAP_SECURITY_OPTION_START_TLS_DEFAULT("Yes.  Use StartTLS with default trust settings."),



  /**
   * Yes.  Use StartTLS with a manually specified configuration.
   */
  INFO_INTERACTIVE_LDAP_SECURITY_OPTION_START_TLS_MANUAL("Yes.  Use StartTLS with a manually specified configuration."),



  /**
   * Should the LDAP communication be encrypted?
   */
  INFO_INTERACTIVE_LDAP_SECURITY_PROMPT("Should the LDAP communication be encrypted?"),



  /**
   * Enter the path to the trust store file
   */
  INFO_INTERACTIVE_LDAP_TRUSTSTORE_PATH_PROMPT("Enter the path to the trust store file"),



  /**
   * Enter the PIN needed to access the contents of the trust store (or just press ENTER if no PIN is required)
   */
  INFO_INTERACTIVE_LDAP_TRUSTSTORE_PIN_PROMPT("Enter the PIN needed to access the contents of the trust store (or just press ENTER if no PIN is required)"),



  /**
   * Blindly trust any server certificate
   */
  INFO_INTERACTIVE_LDAP_TRUST_OPTION_BLIND("Blindly trust any server certificate"),



  /**
   * Use a JKS trust store
   */
  INFO_INTERACTIVE_LDAP_TRUST_OPTION_JKS("Use a JKS trust store"),



  /**
   * Use a PKCS #12 trust store
   */
  INFO_INTERACTIVE_LDAP_TRUST_OPTION_PKCS12("Use a PKCS #12 trust store"),



  /**
   * Interactively prompt about whether to trust the server certificate
   */
  INFO_INTERACTIVE_LDAP_TRUST_OPTION_PROMPT("Interactively prompt about whether to trust the server certificate"),



  /**
   * How do you wish to determine whether the server certificate should be trusted?
   */
  INFO_INTERACTIVE_LDAP_TRUST_PROMPT("How do you wish to determine whether the server certificate should be trusted?"),



  /**
   * The command used to run {0} with the current settings would be:
   */
  INFO_INTERACTIVE_MENU_CURRENT_ARGS_HEADER("The command used to run {0} with the current settings would be:"),



  /**
   * Enter choice:
   */
  INFO_INTERACTIVE_MENU_ENTER_CHOICE_WITHOUT_DEFAULT("Enter choice:"),



  /**
   * Enter choice [{0}]:
   */
  INFO_INTERACTIVE_MENU_ENTER_CHOICE_WITH_DEFAULT("Enter choice [{0}]:"),



  /**
   * The {0} tool does not require any arguments to run with the current settings.
   */
  INFO_INTERACTIVE_MENU_NO_CURRENT_ARGS("The {0} tool does not require any arguments to run with the current settings."),



  /**
   * Display the command to run {0} with these settings
   */
  INFO_INTERACTIVE_MENU_OPTION_DISPLAY_ARGS("Display the command to run {0} with these settings"),



  /**
   * Quit this program
   */
  INFO_INTERACTIVE_MENU_OPTION_QUIT("Quit this program"),



  /**
   * Re-prompt for the LDAP connection arguments
   */
  INFO_INTERACTIVE_MENU_OPTION_REPROMPT_FOR_CONN_ARGS("Re-prompt for the LDAP connection arguments"),



  /**
   * Re-prompt for the LDAP connection and authentication arguments
   */
  INFO_INTERACTIVE_MENU_OPTION_REPROMPT_FOR_CONN_AUTH_ARGS("Re-prompt for the LDAP connection and authentication arguments"),



  /**
   * Run {0} with these settings
   */
  INFO_INTERACTIVE_MENU_OPTION_RUN("Run {0} with these settings"),



  /**
   * Leave undefined
   */
  INFO_INTERACTIVE_MENU_OPTION_UNDEFINED("Leave undefined"),



  /**
   * Press ENTER to continue
   */
  INFO_INTERACTIVE_MENU_PROMPT_PRESS_ENTER_TO_CONTINUE("Press ENTER to continue"),



  /**
   * '{'trailing arguments'}'
   */
  INFO_INTERACTIVE_MENU_TRAILING_ARGS_IDENTIFIER("'{'trailing arguments'}'"),



  /**
   * Confirm the PIN
   */
  INFO_INTERACTIVE_PIN_CONFIRM_PROMPT("Confirm the PIN"),



  /**
   * Confirm the password
   */
  INFO_INTERACTIVE_PW_CONFIRM_PROMPT("Confirm the password"),



  /**
   * Running the following command:
   */
  INFO_INTERACTIVE_RUNNING_WITH_ARGS("Running the following command:"),



  /**
   * Running {0} without any arguments.
   */
  INFO_INTERACTIVE_RUNNING_WITH_NO_ARGS("Running {0} without any arguments."),



  /**
   * Which subcommand do you want to use?
   */
  INFO_INTERACTIVE_SUBCOMMAND_PROMPT("Which subcommand do you want to use?"),



  /**
   * Trailing argument value
   */
  INFO_INTERACTIVE_TRAILING_ARG_PROMPT("Trailing argument value"),



  /**
   * The {0} tool optionally allows one or more trailing arguments that are not immediately preceded by any identifiers.  The expected usage for the trailing arguments is:
   */
  INFO_INTERACTIVE_TRAILING_DESC_MULTIPLE_OPTIONAL("The {0} tool optionally allows one or more trailing arguments that are not immediately preceded by any identifiers.  The expected usage for the trailing arguments is:"),



  /**
   * The {0} tool requires at least {1,number,0} trailing argument(s) (which are not immediately preceded by any identifiers) to be provided.  The expected usage for the trailing arguments is:
   */
  INFO_INTERACTIVE_TRAILING_DESC_MULTIPLE_REQUIRED("The {0} tool requires at least {1,number,0} trailing argument(s) (which are not immediately preceded by any identifiers) to be provided.  The expected usage for the trailing arguments is:"),



  /**
   * The {0} tool optionally allows a single trailing argument which is not immediately preceded by any identifier.  The expected usage for this argument is:
   */
  INFO_INTERACTIVE_TRAILING_DESC_SINGLE_OPTIONAL("The {0} tool optionally allows a single trailing argument which is not immediately preceded by any identifier.  The expected usage for this argument is:"),



  /**
   * The {0} tool requires exactly one trailing argument which is not immediately preceded by any identifier.  The expected usage for this argument is:
   */
  INFO_INTERACTIVE_TRAILING_DESC_SINGLE_REQUIRED("The {0} tool requires exactly one trailing argument which is not immediately preceded by any identifier.  The expected usage for this argument is:"),



  /**
   * Enter the desired trailing argument values, pressing ENTER on a blank line to indicate that no more trailing arguments are needed.
   */
  INFO_INTERACTIVE_TRAILING_PROMPT_MULTIPLE("Enter the desired trailing argument values, pressing ENTER on a blank line to indicate that no more trailing arguments are needed."),



  /**
   * Enter the desired trailing argument value, or simply press ENTER to indicate no trailing argument should be provided
   */
  INFO_INTERACTIVE_TRAILING_PROMPT_SINGLE("Enter the desired trailing argument value, or simply press ENTER to indicate no trailing argument should be provided"),



  /**
   * LDAP Connection Arguments
   */
  INFO_LDAP_TOOL_ARG_GROUP_CONNECT("LDAP Connection Arguments"),



  /**
   * LDAP Connection and Authentication Arguments
   */
  INFO_LDAP_TOOL_ARG_GROUP_CONNECT_AND_AUTH("LDAP Connection and Authentication Arguments"),



  /**
   * The DN to use to bind to the directory server when performing simple authentication.
   */
  INFO_LDAP_TOOL_DESCRIPTION_BIND_DN("The DN to use to bind to the directory server when performing simple authentication."),



  /**
   * The password to use to bind to the directory server when performing simple authentication or a password-based SASL mechanism.
   */
  INFO_LDAP_TOOL_DESCRIPTION_BIND_PW("The password to use to bind to the directory server when performing simple authentication or a password-based SASL mechanism."),



  /**
   * The path to the file containing the password to use to bind to the directory server when performing simple authentication or a password-based SASL mechanism.
   */
  INFO_LDAP_TOOL_DESCRIPTION_BIND_PW_FILE("The path to the file containing the password to use to bind to the directory server when performing simple authentication or a password-based SASL mechanism."),



  /**
   * Indicates that the tool should interactively prompt the user for the bind password.
   */
  INFO_LDAP_TOOL_DESCRIPTION_BIND_PW_PROMPT("Indicates that the tool should interactively prompt the user for the bind password."),



  /**
   * The nickname (alias) of the client certificate in the key store to present to the directory server for SSL client authentication.
   */
  INFO_LDAP_TOOL_DESCRIPTION_CERT_NICKNAME("The nickname (alias) of the client certificate in the key store to present to the directory server for SSL client authentication."),



  /**
   * Enable Java''s low-level support for debugging SSL/TLS communication.  This is equivalent to setting the ''javax.net.debug'' property to ''all''.
   */
  INFO_LDAP_TOOL_DESCRIPTION_ENABLE_SSL_DEBUGGING("Enable Java''s low-level support for debugging SSL/TLS communication.  This is equivalent to setting the ''javax.net.debug'' property to ''all''."),



  /**
   * Provide information about the supported SASL mechanisms, including the properties available for use with each.
   */
  INFO_LDAP_TOOL_DESCRIPTION_HELP_SASL("Provide information about the supported SASL mechanisms, including the properties available for use with each."),



  /**
   * The IP address or resolvable name to use to connect to the directory server.  If this is not provided, then a default value of ''localhost'' will be used.
   */
  INFO_LDAP_TOOL_DESCRIPTION_HOST("The IP address or resolvable name to use to connect to the directory server.  If this is not provided, then a default value of ''localhost'' will be used."),



  /**
   * The format (e.g., jks, jceks, pkcs12, etc.) for the key store file.
   */
  INFO_LDAP_TOOL_DESCRIPTION_KEY_STORE_FORMAT("The format (e.g., jks, jceks, pkcs12, etc.) for the key store file."),



  /**
   * The password to use to access the key store contents.
   */
  INFO_LDAP_TOOL_DESCRIPTION_KEY_STORE_PASSWORD("The password to use to access the key store contents."),



  /**
   * The path to the file containing the password to use to access the key store contents.
   */
  INFO_LDAP_TOOL_DESCRIPTION_KEY_STORE_PASSWORD_FILE("The path to the file containing the password to use to access the key store contents."),



  /**
   * Indicates that the tool should interactively prompt the user for the password to use to access the key store contents.
   */
  INFO_LDAP_TOOL_DESCRIPTION_KEY_STORE_PASSWORD_PROMPT("Indicates that the tool should interactively prompt the user for the password to use to access the key store contents."),



  /**
   * The path to the file to use as the key store for obtaining client certificates when communicating securely with the directory server.
   */
  INFO_LDAP_TOOL_DESCRIPTION_KEY_STORE_PATH("The path to the file to use as the key store for obtaining client certificates when communicating securely with the directory server."),



  /**
   * The port to use to connect to the directory server.  If this is not provided, then a default value of 389 will be used.
   */
  INFO_LDAP_TOOL_DESCRIPTION_PORT("The port to use to connect to the directory server.  If this is not provided, then a default value of 389 will be used."),



  /**
   * A name-value pair providing information to use when performing SASL authentication.
   */
  INFO_LDAP_TOOL_DESCRIPTION_SASL_OPTION("A name-value pair providing information to use when performing SASL authentication."),



  /**
   * Trust any certificate presented by the directory server.
   */
  INFO_LDAP_TOOL_DESCRIPTION_TRUST_ALL("Trust any certificate presented by the directory server."),



  /**
   * The format (e.g., jks, jceks, pkcs12, etc.) for the trust store file.
   */
  INFO_LDAP_TOOL_DESCRIPTION_TRUST_STORE_FORMAT("The format (e.g., jks, jceks, pkcs12, etc.) for the trust store file."),



  /**
   * The password to use to access the trust store contents.
   */
  INFO_LDAP_TOOL_DESCRIPTION_TRUST_STORE_PASSWORD("The password to use to access the trust store contents."),



  /**
   * The path to the file containing the password to use to access the trust store contents.
   */
  INFO_LDAP_TOOL_DESCRIPTION_TRUST_STORE_PASSWORD_FILE("The path to the file containing the password to use to access the trust store contents."),



  /**
   * Indicates that the tool should interactively prompt the user for the password to use to access the trust store contents.
   */
  INFO_LDAP_TOOL_DESCRIPTION_TRUST_STORE_PASSWORD_PROMPT("Indicates that the tool should interactively prompt the user for the password to use to access the trust store contents."),



  /**
   * The path to the file to use as trust store when determining whether to trust a certificate presented by the directory server.
   */
  INFO_LDAP_TOOL_DESCRIPTION_TRUST_STORE_PATH("The path to the file to use as trust store when determining whether to trust a certificate presented by the directory server."),



  /**
   * Use the SASL EXTERNAL mechanism to authenticate.
   */
  INFO_LDAP_TOOL_DESCRIPTION_USE_SASL_EXTERNAL("Use the SASL EXTERNAL mechanism to authenticate."),



  /**
   * Use SSL when communicating with the directory server.
   */
  INFO_LDAP_TOOL_DESCRIPTION_USE_SSL("Use SSL when communicating with the directory server."),



  /**
   * Use StartTLS when communicating with the directory server.
   */
  INFO_LDAP_TOOL_DESCRIPTION_USE_START_TLS("Use StartTLS when communicating with the directory server."),



  /**
   * Enter the bind password:
   */
  INFO_LDAP_TOOL_ENTER_BIND_PASSWORD("Enter the bind password:"),



  /**
   * Enter the key store password:
   */
  INFO_LDAP_TOOL_ENTER_KEY_STORE_PASSWORD("Enter the key store password:"),



  /**
   * Enter the trust store password:
   */
  INFO_LDAP_TOOL_ENTER_TRUST_STORE_PASSWORD("Enter the trust store password:"),



  /**
   * '{'nickname'}'
   */
  INFO_LDAP_TOOL_PLACEHOLDER_CERT_NICKNAME("'{'nickname'}'"),



  /**
   * '{'dn'}'
   */
  INFO_LDAP_TOOL_PLACEHOLDER_DN("'{'dn'}'"),



  /**
   * '{'format'}'
   */
  INFO_LDAP_TOOL_PLACEHOLDER_FORMAT("'{'format'}'"),



  /**
   * '{'host'}'
   */
  INFO_LDAP_TOOL_PLACEHOLDER_HOST("'{'host'}'"),



  /**
   * '{'password'}'
   */
  INFO_LDAP_TOOL_PLACEHOLDER_PASSWORD("'{'password'}'"),



  /**
   * '{'path'}'
   */
  INFO_LDAP_TOOL_PLACEHOLDER_PATH("'{'path'}'"),



  /**
   * '{'port'}'
   */
  INFO_LDAP_TOOL_PLACEHOLDER_PORT("'{'port'}'"),



  /**
   * '{'name=value'}'
   */
  INFO_LDAP_TOOL_PLACEHOLDER_SASL_OPTION("'{'name=value'}'"),



  /**
   * Connection and Authentication Arguments
   */
  INFO_MULTI_LDAP_TOOL_GROUP_CONN_AND_AUTH("Connection and Authentication Arguments"),



  /**
   * {0,number,0} days
   */
  INFO_NUM_DAYS_PLURAL("{0,number,0} days"),



  /**
   * {0,number,0} day
   */
  INFO_NUM_DAYS_SINGULAR("{0,number,0} day"),



  /**
   * {0,number,0} hours
   */
  INFO_NUM_HOURS_PLURAL("{0,number,0} hours"),



  /**
   * {0,number,0} hour
   */
  INFO_NUM_HOURS_SINGULAR("{0,number,0} hour"),



  /**
   * {0,number,0} minutes
   */
  INFO_NUM_MINUTES_PLURAL("{0,number,0} minutes"),



  /**
   * {0,number,0} minute
   */
  INFO_NUM_MINUTES_SINGULAR("{0,number,0} minute"),



  /**
   * {0,number,0} seconds
   */
  INFO_NUM_SECONDS_PLURAL("{0,number,0} seconds"),



  /**
   * {0,number,0} second
   */
  INFO_NUM_SECONDS_SINGULAR("{0,number,0} second"),



  /**
   * {0} seconds
   */
  INFO_NUM_SECONDS_WITH_DECIMAL("{0} seconds"),



  /**
   * Password file ''{0}'' is encrypted.  Please enter the password used as the encryption key:
   */
  INFO_PW_FILE_READER_ENTER_PW_PROMPT("Password file ''{0}'' is encrypted.  Please enter the password used as the encryption key:"),



  /**
   * The path to a sample variable data rate file that should be generated.  This file will contain comments that describe the expected format for the file to use with the {0} argument.
   */
  INFO_RATE_ADJUSTOR_GENERATE_SAMPLE_RATE_FILE_ARG_DESCRIPTION("The path to a sample variable data rate file that should be generated.  This file will contain comments that describe the expected format for the file to use with the {0} argument."),



  /**
   * The path to a file containing information that can be used to cause the tool to vary the target rate over time.  Use the {0} argument to generate a sample rate definition file with comments that describe the required format for this file.
   */
  INFO_RATE_ADJUSTOR_VARIABLE_RATE_DATA_ARG_DESCRIPTION("The path to a file containing information that can be used to cause the tool to vary the target rate over time.  Use the {0} argument to generate a sample rate definition file with comments that describe the required format for this file."),



  /**
   * A mechanism that can be used to destroy an existing authentication session, or to perform a bind without actually authenticating but optionally including a trace string that may help provide information about the client.
   */
  INFO_SASL_ANONYMOUS_DESCRIPTION("A mechanism that can be used to destroy an existing authentication session, or to perform a bind without actually authenticating but optionally including a trace string that may help provide information about the client."),



  /**
   * A trace string that may be used to provide additional information about the client performing the bind.  Note, however, that because the client is not providing any proof of its identity, it is not possible to determine the validity of any trace information given.
   */
  INFO_SASL_ANONYMOUS_OPTION_TRACE("A trace string that may be used to provide additional information about the client performing the bind.  Note, however, that because the client is not providing any proof of its identity, it is not possible to determine the validity of any trace information given."),



  /**
   * A mechanism that can be used to perform password-based authentication in a manner that prevents an observer from discovering the password by generating an MD5 digest based on the provided password and additional information, including random data provided by the server.
   */
  INFO_SASL_CRAM_MD5_DESCRIPTION("A mechanism that can be used to perform password-based authentication in a manner that prevents an observer from discovering the password by generating an MD5 digest based on the provided password and additional information, including random data provided by the server."),



  /**
   * A string that identifies the user that is trying to authenticate.  The value is typically in the form ''dn:'' followed by the DN of the target user''s entry, or ''u:'' followed by the username for the target user.
   */
  INFO_SASL_CRAM_MD5_OPTION_AUTH_ID("A string that identifies the user that is trying to authenticate.  The value is typically in the form ''dn:'' followed by the DN of the target user''s entry, or ''u:'' followed by the username for the target user."),



  /**
   * A mechanism that can be used to perform password-based authentication in a manner that prevents an observer from discovering the password by generating an MD5 digest based on the provided password and additional information, including random data provided by both the client and the server.
   */
  INFO_SASL_DIGEST_MD5_DESCRIPTION("A mechanism that can be used to perform password-based authentication in a manner that prevents an observer from discovering the password by generating an MD5 digest based on the provided password and additional information, including random data provided by both the client and the server."),



  /**
   * A string that identifies the user under whose authority subsequent operations should be processed.  If this is not provided, then no alternate authorization identity will be used.
   */
  INFO_SASL_DIGEST_MD5_OPTION_AUTHZ_ID("A string that identifies the user under whose authority subsequent operations should be processed.  If this is not provided, then no alternate authorization identity will be used."),



  /**
   * A string that identifies the user that is trying to authenticate.  The value is typically in the form ''dn:'' followed by the DN of the target user''s entry, or ''u:'' followed by the username for the target user.
   */
  INFO_SASL_DIGEST_MD5_OPTION_AUTH_ID("A string that identifies the user that is trying to authenticate.  The value is typically in the form ''dn:'' followed by the DN of the target user''s entry, or ''u:'' followed by the username for the target user."),



  /**
   * The quality of protection that should be used for any communication that occurs after the authentication has completed.  Allowed values are ''auth'' (for just authentication with no communication protection), ''auth-int'' (for integrity protection for communication, which does not encrypt but ensures that the communication cannot be imperceptibly altered by a man in the middle attack), and ''auth-conf'' (for confidentiality protection for communication, which encrypts the communication so that it cannot be deciphered by a third-party observer).  If no value is specified, then a default of ''auth'' will be assumed.  If any of multiple qualities of protection will be considered acceptable then the permissible QoP values may be separated by commas and listed in order from most desirable to least desirable.
   */
  INFO_SASL_DIGEST_MD5_OPTION_QOP("The quality of protection that should be used for any communication that occurs after the authentication has completed.  Allowed values are ''auth'' (for just authentication with no communication protection), ''auth-int'' (for integrity protection for communication, which does not encrypt but ensures that the communication cannot be imperceptibly altered by a man in the middle attack), and ''auth-conf'' (for confidentiality protection for communication, which encrypts the communication so that it cannot be deciphered by a third-party observer).  If no value is specified, then a default of ''auth'' will be assumed.  If any of multiple qualities of protection will be considered acceptable then the permissible QoP values may be separated by commas and listed in order from most desirable to least desirable."),



  /**
   * The realm for the user trying to authenticate.  If this is not provided, then no realm will be specified in the bind request.
   */
  INFO_SASL_DIGEST_MD5_OPTION_REALM("The realm for the user trying to authenticate.  If this is not provided, then no realm will be specified in the bind request."),



  /**
   * Enter the static password:
   */
  INFO_SASL_ENTER_STATIC_PW("Enter the static password:"),



  /**
   * A mechanism that can allow the client to authenticate to the server using information that the server may have about the client which was not provided in the form of an LDAP message (e.g., a client certificate that was presented during an SSL or StartTLS handshake).
   */
  INFO_SASL_EXTERNAL_DESCRIPTION("A mechanism that can allow the client to authenticate to the server using information that the server may have about the client which was not provided in the form of an LDAP message (e.g., a client certificate that was presented during an SSL or StartTLS handshake)."),



  /**
   * A mechanism that can allow the client to authenticate to the server using Kerberos V.  It may be possible to leverage an existing Kerberos session or to authenticate using a newly-created session.
   */
  INFO_SASL_GSSAPI_DESCRIPTION("A mechanism that can allow the client to authenticate to the server using Kerberos V.  It may be possible to leverage an existing Kerberos session or to authenticate using a newly-created session."),



  /**
   * A string that identifies the user under whose authority subsequent operations should be processed.  If this is not provided, then no alternate authorization identity will be used.
   */
  INFO_SASL_GSSAPI_OPTION_AUTHZ_ID("A string that identifies the user under whose authority subsequent operations should be processed.  If this is not provided, then no alternate authorization identity will be used."),



  /**
   * A string that identifies the user that is trying to authenticate.  This should generally be the user''s Kerberos principal.
   */
  INFO_SASL_GSSAPI_OPTION_AUTH_ID("A string that identifies the user that is trying to authenticate.  This should generally be the user''s Kerberos principal."),



  /**
   * The path to a file containing the JAAS configuration that will be used for Kerberos processing.  if this is not specified then an automatically-generated JAAS configuration will be used.
   */
  INFO_SASL_GSSAPI_OPTION_CONFIG_FILE("The path to a file containing the JAAS configuration that will be used for Kerberos processing.  if this is not specified then an automatically-generated JAAS configuration will be used."),



  /**
   * Indicates whether JAAS and Kerberos processing debug information may be written to standard error.
   */
  INFO_SASL_GSSAPI_OPTION_DEBUG("Indicates whether JAAS and Kerberos processing debug information may be written to standard error."),



  /**
   * The address of the Kerberos KDC that should be used during authentication processing.  If this is not provided, then an attempt will be made to determine the appropriate KDC address from the underlying system configuration.
   */
  INFO_SASL_GSSAPI_OPTION_KDC_ADDRESS("The address of the Kerberos KDC that should be used during authentication processing.  If this is not provided, then an attempt will be made to determine the appropriate KDC address from the underlying system configuration."),



  /**
   * The name of the protocol used in the directory server''s service principal.  If this is not provided, then a default protocol of ''ldap'' will be used.
   */
  INFO_SASL_GSSAPI_OPTION_PROTOCOL("The name of the protocol used in the directory server''s service principal.  If this is not provided, then a default protocol of ''ldap'' will be used."),



  /**
   * The quality of protection that should be used for any communication that occurs after the authentication has completed.  Allowed values are ''auth'' (for just authentication with no communication protection), ''auth-int'' (for integrity protection for communication, which does not encrypt but ensures that the communication cannot be imperceptibly altered by a man in the middle attack), and ''auth-conf'' (for confidentiality protection for communication, which encrypts the communication so that it cannot be deciphered by a third-party observer).  If no value is specified, then a default of ''auth'' will be assumed.  If any of multiple qualities of protection will be considered acceptable then the permissible QoP values may be separated by commas and listed in order from most desirable to least desirable.
   */
  INFO_SASL_GSSAPI_OPTION_QOP("The quality of protection that should be used for any communication that occurs after the authentication has completed.  Allowed values are ''auth'' (for just authentication with no communication protection), ''auth-int'' (for integrity protection for communication, which does not encrypt but ensures that the communication cannot be imperceptibly altered by a man in the middle attack), and ''auth-conf'' (for confidentiality protection for communication, which encrypts the communication so that it cannot be deciphered by a third-party observer).  If no value is specified, then a default of ''auth'' will be assumed.  If any of multiple qualities of protection will be considered acceptable then the permissible QoP values may be separated by commas and listed in order from most desirable to least desirable."),



  /**
   * The name of the Kerberos realm in which the authentication will be processed.  if this is not provided, then an attempt will be made to determine the appropriate realm from the underlying system configuration.
   */
  INFO_SASL_GSSAPI_OPTION_REALM("The name of the Kerberos realm in which the authentication will be processed.  if this is not provided, then an attempt will be made to determine the appropriate realm from the underlying system configuration."),



  /**
   * Indicates whether to attempt to renew the client''s Kerberos ticket-granting ticket if authentication succeeds using an existing Kerberos session.  If this is not provided, then no attempt will be made to renew the TGT.
   */
  INFO_SASL_GSSAPI_OPTION_RENEW_TGT("Indicates whether to attempt to renew the client''s Kerberos ticket-granting ticket if authentication succeeds using an existing Kerberos session.  If this is not provided, then no attempt will be made to renew the TGT."),



  /**
   * Indicates whether the client will be required to have an existing Kerberos session that will be used for the authentication rather than using a newly-created session.  This option will only be examined if a ticket cache should be used during authentication processing.  If this is not provided, then an existing Kerberos session will not be required.
   */
  INFO_SASL_GSSAPI_OPTION_REQUIRE_TICKET_CACHE("Indicates whether the client will be required to have an existing Kerberos session that will be used for the authentication rather than using a newly-created session.  This option will only be examined if a ticket cache should be used during authentication processing.  If this is not provided, then an existing Kerberos session will not be required."),



  /**
   * Specifies the path to a ticket cache file that should be used to look for an existing Kerberos session.  This option will only be examined if a ticket cache should be used during authentication processing.  If this is not provided, then the default ticket cache file path will be used.
   */
  INFO_SASL_GSSAPI_OPTION_TICKET_CACHE("Specifies the path to a ticket cache file that should be used to look for an existing Kerberos session.  This option will only be examined if a ticket cache should be used during authentication processing.  If this is not provided, then the default ticket cache file path will be used."),



  /**
   * Indicates whether to attempt to use a ticket cache in order to determine whether the user has an existing Kerberos session that may be used instead of using a newly-created session.
   */
  INFO_SASL_GSSAPI_OPTION_USE_TICKET_CACHE("Indicates whether to attempt to use a ticket cache in order to determine whether the user has an existing Kerberos session that may be used instead of using a newly-created session."),



  /**
   * The {0} SASL Mechanism
   */
  INFO_SASL_HELP_MECHANISM("The {0} SASL Mechanism"),



  /**
   * The SASL options available for use with the {0} mechanism include:
   */
  INFO_SASL_HELP_MECHANISM_OPTIONS("The SASL options available for use with the {0} mechanism include:"),



  /**
   * A password may optionally be provided when using the {0} SASL mechanism.
   */
  INFO_SASL_HELP_PASSWORD_OPTIONAL("A password may optionally be provided when using the {0} SASL mechanism."),



  /**
   * A password must be provided when using the {0} SASL mechanism.
   */
  INFO_SASL_HELP_PASSWORD_REQUIRED("A password must be provided when using the {0} SASL mechanism."),



  /**
   * A mechanism that can allow the client to authenticate with an OAuth 2.0 access token.
   */
  INFO_SASL_OAUTHBEARER_DESCRIPTION("A mechanism that can allow the client to authenticate with an OAuth 2.0 access token."),



  /**
   * The OAuth 2.0 access token to use to authenticate.
   */
  INFO_SASL_OAUTHBEARER_OPTION_ACCESS_TOKEN("The OAuth 2.0 access token to use to authenticate."),



  /**
   * A mechanism that can allow the client to perform password-based authentication, optionally using an alternate authorization identity.
   */
  INFO_SASL_PLAIN_DESCRIPTION("A mechanism that can allow the client to perform password-based authentication, optionally using an alternate authorization identity."),



  /**
   * A string that identifies the user under whose authority subsequent operations should be processed.  If this is not provided, then no alternate authorization identity will be used.
   */
  INFO_SASL_PLAIN_OPTION_AUTHZ_ID("A string that identifies the user under whose authority subsequent operations should be processed.  If this is not provided, then no alternate authorization identity will be used."),



  /**
   * A string that identifies the user that is trying to authenticate.  The value is typically in the from ''dn:'' followed by the DN of the target user''s entry, or ''u:'' followed by the username for the target user.
   */
  INFO_SASL_PLAIN_OPTION_AUTH_ID("A string that identifies the user that is trying to authenticate.  The value is typically in the from ''dn:'' followed by the DN of the target user''s entry, or ''u:'' followed by the username for the target user."),



  /**
   * A string that identifies the user that is trying to authenticate.
   */
  INFO_SASL_SCRAM_OPTION_USERNAME("A string that identifies the user that is trying to authenticate."),



  /**
   * A salted challenge-response authentication mechanism that can allow the client to perform password-based authentication in a manner that proves that the client knows the password without transferring the password in the clear, while allowing the server to store the password in a non-reversible form, and also requiring the client to perform multiple rounds of processing to make guessing attacks more expensive.  This variant uses the SHA-1 digest algorithm in the course of computing the password.
   */
  INFO_SASL_SCRAM_SHA_1_DESCRIPTION("A salted challenge-response authentication mechanism that can allow the client to perform password-based authentication in a manner that proves that the client knows the password without transferring the password in the clear, while allowing the server to store the password in a non-reversible form, and also requiring the client to perform multiple rounds of processing to make guessing attacks more expensive.  This variant uses the SHA-1 digest algorithm in the course of computing the password."),



  /**
   * A salted challenge-response authentication mechanism that can allow the client to perform password-based authentication in a manner that proves that the client knows the password without transferring the password in the clear, while allowing the server to store the password in a non-reversible form, and also requiring the client to perform multiple rounds of processing to make guessing attacks more expensive.  This variant uses the 256-bit SHA-2 digest algorithm in the course of computing the password.
   */
  INFO_SASL_SCRAM_SHA_256_DESCRIPTION("A salted challenge-response authentication mechanism that can allow the client to perform password-based authentication in a manner that proves that the client knows the password without transferring the password in the clear, while allowing the server to store the password in a non-reversible form, and also requiring the client to perform multiple rounds of processing to make guessing attacks more expensive.  This variant uses the 256-bit SHA-2 digest algorithm in the course of computing the password."),



  /**
   * A salted challenge-response authentication mechanism that can allow the client to perform password-based authentication in a manner that proves that the client knows the password without transferring the password in the clear, while allowing the server to store the password in a non-reversible form, and also requiring the client to perform multiple rounds of processing to make guessing attacks more expensive.  This variant uses the 256-bit SHA-2 digest algorithm in the course of computing the password.
   */
  INFO_SASL_SCRAM_SHA_512_DESCRIPTION("A salted challenge-response authentication mechanism that can allow the client to perform password-based authentication in a manner that proves that the client knows the password without transferring the password in the clear, while allowing the server to store the password in a non-reversible form, and also requiring the client to perform multiple rounds of processing to make guessing attacks more expensive.  This variant uses the 256-bit SHA-2 digest algorithm in the course of computing the password."),



  /**
   * Enter the OAuth 2.0 access token:
   */
  INFO_SASL_TOOL_ENTER_OAUTHBEARER_ACCESS_TOKEN("Enter the OAuth 2.0 access token:"),



  /**
   * A mechanism that can be used to perform multifactor authentication with both a certificate and a password.
   */
  INFO_SASL_UNBOUNDID_CERT_PLUS_PASSWORD_DESCRIPTION("A mechanism that can be used to perform multifactor authentication with both a certificate and a password."),



  /**
   * A mechanism that can be used to perform multifactor authentication with a one-time password that has been delivered to the use through some out-of-band mechanism (triggered by the deliver one-time password extended request).
   */
  INFO_SASL_UNBOUNDID_DELIVERED_OTP_DESCRIPTION("A mechanism that can be used to perform multifactor authentication with a one-time password that has been delivered to the use through some out-of-band mechanism (triggered by the deliver one-time password extended request)."),



  /**
   * A string that identifies the user under whose authority subsequent operations should be processed.  If this is not provided, then no alternate authorization identity will be used.
   */
  INFO_SASL_UNBOUNDID_DELIVERED_OTP_OPTION_AUTHZ_ID("A string that identifies the user under whose authority subsequent operations should be processed.  If this is not provided, then no alternate authorization identity will be used."),



  /**
   * A string that identifies the user that is trying to authenticate.  The value is typically in the form ''dn:'' followed by the DN of the target user''s entry, or ''u:'' followed by the username for the target user.
   */
  INFO_SASL_UNBOUNDID_DELIVERED_OTP_OPTION_AUTH_ID("A string that identifies the user that is trying to authenticate.  The value is typically in the form ''dn:'' followed by the DN of the target user''s entry, or ''u:'' followed by the username for the target user."),



  /**
   * The one-time password to use to authenticate.
   */
  INFO_SASL_UNBOUNDID_DELIVERED_OTP_OPTION_OTP("The one-time password to use to authenticate."),



  /**
   * A mechanism that can be used to perform multifactor authentication with a static password and a time-based one-time password (TOTP) code.
   */
  INFO_SASL_UNBOUNDID_TOTP_DESCRIPTION("A mechanism that can be used to perform multifactor authentication with a static password and a time-based one-time password (TOTP) code."),



  /**
   * A string that identifies the user under whose authority subsequent operations should be processed.  If this is not provided, then no alternate authorization identity will be used.
   */
  INFO_SASL_UNBOUNDID_TOTP_OPTION_AUTHZ_ID("A string that identifies the user under whose authority subsequent operations should be processed.  If this is not provided, then no alternate authorization identity will be used."),



  /**
   * A string that identifies the user that is trying to authenticate.  The value is typically in the form ''dn:'' followed by the DN of the target user''s entry, or ''u:'' followed by the username for the target user.
   */
  INFO_SASL_UNBOUNDID_TOTP_OPTION_AUTH_ID("A string that identifies the user that is trying to authenticate.  The value is typically in the form ''dn:'' followed by the DN of the target user''s entry, or ''u:'' followed by the username for the target user."),



  /**
   * Indicates whether to interactively prompt for the user''s static password.  The value may be either ''true'' or ''false''.
   */
  INFO_SASL_UNBOUNDID_TOTP_OPTION_PROMPT_FOR_PW("Indicates whether to interactively prompt for the user''s static password.  The value may be either ''true'' or ''false''."),



  /**
   * The time-based one-time password authentication code.
   */
  INFO_SASL_UNBOUNDID_TOTP_OPTION_TOTP_PASSWORD("The time-based one-time password authentication code."),



  /**
   * A mechanism that can be used to perform multifactor authentication with a one-time password that has been generated by a YubiKey device.
   */
  INFO_SASL_UNBOUNDID_YUBIKEY_OTP_DESCRIPTION("A mechanism that can be used to perform multifactor authentication with a one-time password that has been generated by a YubiKey device."),



  /**
   * A string that identifies the user under whose authority subsequent operations should be processed.  If this is not provided, then no alternate authorization identity will be used.
   */
  INFO_SASL_UNBOUNDID_YUBIKEY_OTP_OPTION_AUTHZ_ID("A string that identifies the user under whose authority subsequent operations should be processed.  If this is not provided, then no alternate authorization identity will be used."),



  /**
   * A string that identifies the user that is trying to authenticate.  The value is typically in the form ''dn:'' followed by the DN of the target user''s entry, or ''u:'' followed by the username for the target user.
   */
  INFO_SASL_UNBOUNDID_YUBIKEY_OTP_OPTION_AUTH_ID("A string that identifies the user that is trying to authenticate.  The value is typically in the form ''dn:'' followed by the DN of the target user''s entry, or ''u:'' followed by the username for the target user."),



  /**
   * The one-time password to use to authenticate.
   */
  INFO_SASL_UNBOUNDID_YUBIKEY_OTP_OPTION_OTP("The one-time password to use to authenticate."),



  /**
   * Indicates whether to interactively prompt for the user''s static password.  The value may be either ''true'' or ''false''.
   */
  INFO_SASL_UNBOUNDID_YUBIKEY_OTP_OPTION_PROMPT_FOR_PW("Indicates whether to interactively prompt for the user''s static password.  The value may be either ''true'' or ''false''.");



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
      rb = ResourceBundle.getBundle("unboundid-ldapsdk-util");
    } catch (final Exception e) {}
    RESOURCE_BUNDLE = rb;
  }



  /**
   * The map that will be used to hold the unformatted message strings, indexed by property name.
   */
  private static final ConcurrentHashMap<UtilityMessages,String> MESSAGE_STRINGS = new ConcurrentHashMap<>(100);



  /**
   * The map that will be used to hold the message format objects, indexed by property name.
   */
  private static final ConcurrentHashMap<UtilityMessages,MessageFormat> MESSAGES = new ConcurrentHashMap<>(100);



  // The default text for this message
  private final String defaultText;



  /**
   * Creates a new message key.
   */
  private UtilityMessages(final String defaultText)
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

