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
package com.unboundid.ldap.sdk.unboundidds.tools;



import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;



/**
 * This enum defines a set of message keys for messages in the
 * com.unboundid.ldap.sdk.unboundidds.tools package, which correspond to messages in the
 * unboundid-ldapsdk-tools.properties properties file.
 * <BR><BR>
 * This source file was generated from the properties file.
 * Do not edit it directly.
 */
enum ToolMessages
{
  /**
   * An error occurred while attempting to read the encryption passphrase from file ''{0}'':  {1}
   */
  ERR_CSD_CANNOT_READ_PASSPHRASE("An error occurred while attempting to read the encryption passphrase from file ''{0}'':  {1}"),



  /**
   * An error occurred while attempting to write the generated encryption passphrase to file ''{0}'':  {1}
   */
  ERR_CSD_CANNOT_WRITE_GENERATED_PASSPHRASE("An error occurred while attempting to write the generated encryption passphrase to file ''{0}'':  {1}"),



  /**
   * An error occurred while trying to send the collect support data extended request or to read the response:  {0}
   */
  ERR_CSD_ERROR_SENDING_REQUEST("An error occurred while trying to send the collect support data extended request or to read the response:  {0}"),



  /**
   * An error occurred while attempting to invoke the server-side collect-support-data tool to perform local processing:  {0}
   */
  ERR_CSD_INVOKE_ERROR("An error occurred while attempting to invoke the server-side collect-support-data tool to perform local processing:  {0}"),



  /**
   * An error occurred while attempting to create support data archive output file ''{0}'':  {1}
   */
  ERR_CSD_LISTENER_CANNOT_CREATE_OUTPUT_FILE("An error occurred while attempting to create support data archive output file ''{0}'':  {1}"),



  /**
   * An error occurred while attempting to close the output stream used to write to support data archive file ''{0}'':  {1}
   */
  ERR_CSD_LISTENER_CLOSE_ERROR("An error occurred while attempting to close the output stream used to write to support data archive file ''{0}'':  {1}"),



  /**
   * An error occurred while attempting to write to support data archive file ''{0}'':  {1}
   */
  ERR_CSD_LISTENER_WRITE_ERROR("An error occurred while attempting to write to support data archive file ''{0}'':  {1}"),



  /**
   * If the --noPrompt argument is used in conjunction with an encrypted support data archive, then the --passphraseFile argument must be provided to supply the encryption passphrase.
   */
  ERR_CSD_NO_PASSPHRASE_WITH_NO_PROMPT("If the --noPrompt argument is used in conjunction with an encrypted support data archive, then the --passphraseFile argument must be provided to supply the encryption passphrase."),



  /**
   * Passphrase file ''{0}'' does not exist.
   */
  ERR_CSD_PASSPHRASE_FILE_MISSING("Passphrase file ''{0}'' does not exist."),



  /**
   * ERROR:  The provided passphrase values do not match.
   */
  ERR_CSD_PASSPHRASE_MISMATCH("ERROR:  The provided passphrase values do not match."),



  /**
   * An error occurred while attempting to interactively prompt for the encryption passphrase:  {0}
   */
  ERR_CSD_PASSPHRASE_PROMPT_READ_ERROR("An error occurred while attempting to interactively prompt for the encryption passphrase:  {0}"),



  /**
   * Unable to perform local collect-support-data processing because the necessary server-side support could not be found in the classpath.  When using the collect-support-data tool outside of the server codebase, the --useRemoteServer argument must be provided.
   */
  ERR_CSD_SERVER_CODE_NOT_AVAILABLE("Unable to perform local collect-support-data processing because the necessary server-side support could not be found in the classpath.  When using the collect-support-data tool outside of the server codebase, the --useRemoteServer argument must be provided."),



  /**
   * The value provided for the --logTimeRange argument is not valid because value ''{0}'' cannot be parsed as a timestamp in any of the supported formats.  Supported formats include the generalized time format (e.g., ''YYYYMMDDhhmmss.uuuZ''), a format similar to the generalized time format but without the time zone indicator to indicate that the value should be in the local time zone (e.g., ''YYYYMMDDhhmmss.uuu''), or the server''s default log timestamp format (e.g., ''[DD/MMM/YYYY:hh:mm:ss Z]'' or ''[DD/MMM/YYYY:hh:mm:ss.uuu Z]'').
   */
  ERR_CSD_TIME_RANGE_CANNOT_PARSE_TIMESTAMP("The value provided for the --logTimeRange argument is not valid because value ''{0}'' cannot be parsed as a timestamp in any of the supported formats.  Supported formats include the generalized time format (e.g., ''YYYYMMDDhhmmss.uuuZ''), a format similar to the generalized time format but without the time zone indicator to indicate that the value should be in the local time zone (e.g., ''YYYYMMDDhhmmss.uuu''), or the server''s default log timestamp format (e.g., ''[DD/MMM/YYYY:hh:mm:ss Z]'' or ''[DD/MMM/YYYY:hh:mm:ss.uuu Z]'')."),



  /**
   * The value provided for the ''--logTimeRange'' argument is not valid because the start time is after the end time.
   */
  ERR_CSD_TIME_RANGE_START_GREATER_THAN_END("The value provided for the ''--logTimeRange'' argument is not valid because the start time is after the end time."),



  /**
   * An error occurred while attempting to establish a connection to use to communicate with the server:  {0}
   */
  ERR_GEN_TOTP_SECRET_CANNOT_CONNECT("An error occurred while attempting to establish a connection to use to communicate with the server:  {0}"),



  /**
   * An error occurred while attempting to read the static password from file ''{0}'':  {1}
   */
  ERR_GEN_TOTP_SECRET_CANNOT_READ_PW_FROM_FILE("An error occurred while attempting to read the static password from file ''{0}'':  {1}"),



  /**
   * An error occurred while attempting to read the static password from standard input:  {0}
   */
  ERR_GEN_TOTP_SECRET_CANNOT_READ_PW_FROM_STDIN("An error occurred while attempting to read the static password from standard input:  {0}"),



  /**
   * Diagnostic Message:  {0}
   */
  ERR_GEN_TOTP_SECRET_DIAGNOSTIC_MESSAGE("Diagnostic Message:  {0}"),



  /**
   * Unable to generate a TOTP shared secret.
   */
  ERR_GEN_TOTP_SECRET_GEN_FAILURE("Unable to generate a TOTP shared secret."),



  /**
   * Matched DN:  {0}
   */
  ERR_GEN_TOTP_SECRET_MATCHED_DN("Matched DN:  {0}"),



  /**
   * Referral URL:  {0}
   */
  ERR_GEN_TOTP_SECRET_REFERRAL_URL("Referral URL:  {0}"),



  /**
   * Result code:  {0}
   */
  ERR_GEN_TOTP_SECRET_RESULT_CODE("Result code:  {0}"),



  /**
   * Unable to revoke all TOTP shared secrets.
   */
  ERR_GEN_TOTP_SECRET_REVOKE_ALL_FAILURE("Unable to revoke all TOTP shared secrets."),



  /**
   * Unable to revoke TOTP shared secret {0}.
   */
  ERR_GEN_TOTP_SECRET_REVOKE_FAILURE("Unable to revoke TOTP shared secret {0}."),



  /**
   * Unable to parse line ''{0}'' (line number {1,number,0}) read from file ''{2}'' to create a compare request:  {3}
   */
  ERR_LDAPCOMPARE_ASSERTION_FILE_CANNOT_PARSE_AVA("Unable to parse line ''{0}'' (line number {1,number,0}) read from file ''{2}'' to create a compare request:  {3}"),



  /**
   * Assertion file ''{0}'' does not contain any compare assertions.
   */
  ERR_LDAPCOMPARE_ASSERTION_FILE_EMPTY("Assertion file ''{0}'' does not contain any compare assertions."),



  /**
   * Unable to parse line ''{0}'' (line number {1,number,0}) read from file ''{2}'' to create a compare request because its first component ''{3}'' cannot be parsed as a valid DN:  {4}
   */
  ERR_LDAPCOMPARE_ASSERTION_FILE_LINE_INVALID_DN("Unable to parse line ''{0}'' (line number {1,number,0}) read from file ''{2}'' to create a compare request because its first component ''{3}'' cannot be parsed as a valid DN:  {4}"),



  /**
   * Unable to parse line ''{0}'' (line number {1,number,0}) read from file ''{2}'' to create a compare request because the line does not end with an attribute-value assertion.
   */
  ERR_LDAPCOMPARE_ASSERTION_FILE_LINE_MISSING_AVA("Unable to parse line ''{0}'' (line number {1,number,0}) read from file ''{2}'' to create a compare request because the line does not end with an attribute-value assertion."),



  /**
   * Unable to parse line ''{0}'' (line number {1,number,0}) read from file ''{2}'' to create a compare request because the line does not contain any tab characters to separate the target entry DN from the attribute-value assertion.
   */
  ERR_LDAPCOMPARE_ASSERTION_FILE_LINE_MISSING_TAB("Unable to parse line ''{0}'' (line number {1,number,0}) read from file ''{2}'' to create a compare request because the line does not contain any tab characters to separate the target entry DN from the attribute-value assertion."),



  /**
   * Unable to parse ''{0}'' as an attribute-value assertion because the assertion value cannot be base64-decoded:  {1}
   */
  ERR_LDAPCOMPARE_AVA_CANNOT_BASE64_DECODE_VALUE("Unable to parse ''{0}'' as an attribute-value assertion because the assertion value cannot be base64-decoded:  {1}"),



  /**
   * Unable to parse ''{0}'' as an attribute-value assertion because it indicates that the assertion value should be read from file ''{1}'', but an error occurred while attempting to read the contents of that file:  {2}
   */
  ERR_LDAPCOMPARE_AVA_CANNOT_READ_FILE("Unable to parse ''{0}'' as an attribute-value assertion because it indicates that the assertion value should be read from file ''{1}'', but an error occurred while attempting to read the contents of that file:  {2}"),



  /**
   * Unable to parse ''{0}'' as an attribute-value assertion because it starts with a colon, indicating an empty attribute type name or OID.
   */
  ERR_LDAPCOMPARE_AVA_NO_ATTR("Unable to parse ''{0}'' as an attribute-value assertion because it starts with a colon, indicating an empty attribute type name or OID."),



  /**
   * Unable to parse ''{0}'' as an attribute-value assertion because it does not contain a colon to separate the attribute type name or OID from the assertion value.
   */
  ERR_LDAPCOMPARE_AVA_NO_COLON("Unable to parse ''{0}'' as an attribute-value assertion because it does not contain a colon to separate the attribute type name or OID from the assertion value."),



  /**
   * Unable to parse ''{0}'' as an attribute-value assertion because it indicates that the assertion value should be read from file ''{1}'', but that file does not exist.
   */
  ERR_LDAPCOMPARE_AVA_NO_SUCH_FILE("Unable to parse ''{0}'' as an attribute-value assertion because it indicates that the assertion value should be read from file ''{1}'', but that file does not exist."),



  /**
   * An error occurred while attempting to open output file ''{0}'' for writing:  {1}
   */
  ERR_LDAPCOMPARE_CANNOT_OPEN_OUTPUT_FILE("An error occurred while attempting to open output file ''{0}'' for writing:  {1}"),



  /**
   * An error occurred while trying to read from assertion file ''{0}'':  {1}
   */
  ERR_LDAPCOMPARE_CANNOT_READ_ASSERTION_FILE("An error occurred while trying to read from assertion file ''{0}'':  {1}"),



  /**
   * An error occurred while trying to read from DN file ''{0}'':  {1}
   */
  ERR_LDAPCOMPARE_CANNOT_READ_DN_FILE("An error occurred while trying to read from DN file ''{0}'':  {1}"),



  /**
   * Assertion file ''{0}'' does not contain any entry DNs.
   */
  ERR_LDAPCOMPARE_DN_FILE_EMPTY("Assertion file ''{0}'' does not contain any entry DNs."),



  /**
   * If neither the {0} nor the {0} argument is provided, then there must be at least two unnamed trailing arguments.  The first trailing argument should specify the attribute-value assertion to use for all compare requests, which should be formatted with the name or OID of the target attribute type followed by either a single colon and the string representation of the assertion value, two colons and the base64-encoded representation of the assertion value, or a colon and a less-than sign followed by the path to the file from which the raw assertion value should be read.  All remaining trailing arguments should specify the DNs of the entries in which to process the compare requests.
   */
  ERR_LDAPCOMPARE_INVALID_TRAILING_ARG_COUNT_WITHOUT_FILE("If neither the {0} nor the {0} argument is provided, then there must be at least two unnamed trailing arguments.  The first trailing argument should specify the attribute-value assertion to use for all compare requests, which should be formatted with the name or OID of the target attribute type followed by either a single colon and the string representation of the assertion value, two colons and the base64-encoded representation of the assertion value, or a colon and a less-than sign followed by the path to the file from which the raw assertion value should be read.  All remaining trailing arguments should specify the DNs of the entries in which to process the compare requests."),



  /**
   * If the {0} argument is provided, then there must be exactly one unnamed trailing argument, which should specify the attribute-value assertion to use for all compare requests.  The attribute-value assertion should be formatted with the name or OID of the target attribute type followed by either a single colon and the string representation of the assertion value, two colons and the base64-encoded representation of the assertion value, or a colon and a less-than sign followed by the path to a file from which the raw assertion value will be read.
   */
  ERR_LDAPCOMPARE_INVALID_TRAILING_ARG_COUNT_WITH_DN_FILE("If the {0} argument is provided, then there must be exactly one unnamed trailing argument, which should specify the attribute-value assertion to use for all compare requests.  The attribute-value assertion should be formatted with the name or OID of the target attribute type followed by either a single colon and the string representation of the assertion value, two colons and the base64-encoded representation of the assertion value, or a colon and a less-than sign followed by the path to a file from which the raw assertion value will be read."),



  /**
   * Unable to parse unnamed trailing argument value ''{0}'' as a valid DN:  {1}
   */
  ERR_LDAPCOMPARE_MALFORMED_TRAILING_ARG_DN("Unable to parse unnamed trailing argument value ''{0}'' as a valid DN:  {1}"),



  /**
   * The compare operation failed.
   */
  ERR_LDAPCOMPARE_RESULT_FAILED("The compare operation failed."),



  /**
   * Processing completed with one or more errors.  True results:  {0,number,0}.  False results:  {1,number,0}.  Error results:  {2,number,0}.
   */
  ERR_LDAPCOMPARE_RESULT_WITH_ERRORS("Processing completed with one or more errors.  True results:  {0,number,0}.  False results:  {1,number,0}.  Error results:  {2,number,0}."),



  /**
   * If the {0} argument is provided, then there must not be any unnamed trailing arguments.
   */
  ERR_LDAPCOMPARE_TRAILING_ARGS_WITH_ASSERTION_FILE("If the {0} argument is provided, then there must not be any unnamed trailing arguments."),



  /**
   * The input data contained base64-encoded DN line ''{0}'' with no DN.
   */
  ERR_LDAPDELETE_BASE64_DN_EMPTY("The input data contained base64-encoded DN line ''{0}'' with no DN."),



  /**
   * The input data contained base64-encoded DN line ''{0}'' whose value did not have a valid base64 encoding.
   */
  ERR_LDAPDELETE_BASE64_DN_NOT_BASE64("The input data contained base64-encoded DN line ''{0}'' whose value did not have a valid base64 encoding."),



  /**
   * An error occurred while trying to read the encryption passphrase from file ''{0}'':  {1}
   */
  ERR_LDAPDELETE_CANNOT_READ_ENCRYPTION_PW_FILE("An error occurred while trying to read the encryption passphrase from file ''{0}'':  {1}"),



  /**
   * The following error was encountered while trying to delete entry ''{0}'' as part of the client-side subtree delete for entry ''{1}'':
   */
  ERR_LDAPDELETE_CLIENT_SIDE_SUBTREE_DEL_DEL_ERROR("The following error was encountered while trying to delete entry ''{0}'' as part of the client-side subtree delete for entry ''{1}'':"),



  /**
   * The client-side subtree delete attempt failed with one or more errors.
   */
  ERR_LDAPDELETE_CLIENT_SIDE_SUBTREE_DEL_FAILED("The client-side subtree delete attempt failed with one or more errors."),



  /**
   * The client-side subtree delete attempt for entry ''{0}'' did not remove any entries.  This likely indicates that either the entry doesn''t exist or that it is not accessible to the requester.
   */
  ERR_LDAPDELETE_CLIENT_SIDE_SUBTREE_DEL_NO_BASE_ENTRY("The client-side subtree delete attempt for entry ''{0}'' did not remove any entries.  This likely indicates that either the entry doesn''t exist or that it is not accessible to the requester."),



  /**
   * One or more errors were encountered while searching for entries below ''{0}''.  The first search error was:
   */
  ERR_LDAPDELETE_CLIENT_SIDE_SUBTREE_DEL_SEARCH_ERROR("One or more errors were encountered while searching for entries below ''{0}''.  The first search error was:"),



  /**
   * The input data contained DN line ''{0}'' with no DN.
   */
  ERR_LDAPDELETE_DN_EMPTY("The input data contained DN line ''{0}'' with no DN."),



  /**
   * A malformed DN was encountered on line {0,number,0}.  The line starts with a space, which indicates that it should be a continuation of the previous line, but either there was no previous line, or the previous line was blank or contained a comment.
   */
  ERR_LDAPDELETE_DN_LINE_STARTS_WITH_UNEXPECTED_SPACE("A malformed DN was encountered on line {0,number,0}.  The line starts with a space, which indicates that it should be a continuation of the previous line, but either there was no previous line, or the previous line was blank or contained a comment."),



  /**
   * Unable to decrypt the data using the provided passphrase.
   */
  ERR_LDAPDELETE_ENCRYPTION_PASSPHRASE_ERROR("Unable to decrypt the data using the provided passphrase."),



  /**
   * An error occurred while attempting to close reject file ''{0}'':  {1}.  It is possible that some data at the end of the file may have been lost.
   */
  ERR_LDAPDELETE_ERROR_CLOSING_REJECT_WRITER("An error occurred while attempting to close reject file ''{0}'':  {1}.  It is possible that some data at the end of the file may have been lost."),



  /**
   * An error occurred while attempting to open DN file ''{0}'' for reading:  {1}
   */
  ERR_LDAPDELETE_ERROR_OPENING_DN_FILE("An error occurred while attempting to open DN file ''{0}'' for reading:  {1}"),



  /**
   * An error occurred while trying to read data from filter file ''{0}'':  {1}
   */
  ERR_LDAPDELETE_ERROR_READING_FILTER_FILE("An error occurred while trying to read data from filter file ''{0}'':  {1}"),



  /**
   * An error occurred while attempting to read data from standard input:  {0}
   */
  ERR_LDAPDELETE_ERROR_READING_STDIN("An error occurred while attempting to read data from standard input:  {0}"),



  /**
   * The search returned a result of {0}, but that result did not include the expected simple paged results control.
   */
  ERR_LDAPDELETE_MISSING_PAGED_RESULTS_RESPONSE("The search returned a result of {0}, but that result did not include the expected simple paged results control."),



  /**
   * Value ''{0}'' provided for argument ''{1}'' is invalid.  The value must contain the ID of the entry-balancing request processor followed by a colon and the ID of a desired backend set to use for that entry-balancing request processor.
   */
  ERR_LDAPDELETE_ROUTE_TO_BACKEND_SET_INVALID_FORMAT("Value ''{0}'' provided for argument ''{1}'' is invalid.  The value must contain the ID of the entry-balancing request processor followed by a colon and the ID of a desired backend set to use for that entry-balancing request processor."),



  /**
   * The search with base DN ''{0}'' and filter ''{1}'' failed with result code {2} and message ''{3}''.
   */
  ERR_LDAPDELETE_SEARCH_ERROR("The search with base DN ''{0}'' and filter ''{1}'' failed with result code {2} and message ''{3}''."),



  /**
   * The search with base DN ''{0}'' and filter ''{1}'' returned an entry with DN ''{2}'' that could not be parsed:  {3}
   */
  ERR_LDAPDELETE_SEARCH_LISTENER_CANNOT_PARSE_ENTRY_DN("The search with base DN ''{0}'' and filter ''{1}'' returned an entry with DN ''{2}'' that could not be parsed:  {3}"),



  /**
   * The search with base DN ''{0}'' and filter ''{1}'' returned {0}.  This reference will not be followed.
   */
  ERR_LDAPDELETE_SEARCH_LISTENER_REFERENCE("The search with base DN ''{0}'' and filter ''{1}'' returned {0}.  This reference will not be followed."),



  /**
   * Did not delete any entries matching filter ''{0}'' because the search to identify entries matching that filter did not return any results.
   */
  ERR_LDAPDELETE_SEARCH_RETURNED_NO_ENTRIES("Did not delete any entries matching filter ''{0}'' because the search to identify entries matching that filter did not return any results."),



  /**
   * If the {0} argument is provided, then you cannot also use trailing arguments to specify the DNs entries to delete.
   */
  ERR_LDAPDELETE_TRAILING_ARG_CONFLICT("If the {0} argument is provided, then you cannot also use trailing arguments to specify the DNs entries to delete."),



  /**
   * Unsupported character set:  {0}.
   */
  ERR_LDAPDELETE_UNSUPPORTED_CHARSET("Unsupported character set:  {0}."),



  /**
   * An error occurred while attempting to write to the rejects file:  {0}
   */
  ERR_LDAPDELETE_WRITE_TO_REJECTS_FAILED("An error occurred while attempting to write to the rejects file:  {0}"),



  /**
   * An error occurred while attempting to create a connection pool to communicate with the directory server:  {0}
   */
  ERR_LDAPMODIFY_CANNOT_CREATE_CONNECTION_POOL("An error occurred while attempting to create a connection pool to communicate with the directory server:  {0}"),



  /**
   * An error occurred while attempting to create an LDIF reader to obtain the changes to process:  {0}
   */
  ERR_LDAPMODIFY_CANNOT_CREATE_LDIF_READER("An error occurred while attempting to create an LDIF reader to obtain the changes to process:  {0}"),



  /**
   * An error occurred while attempting to create a reject writer to write rejected changes to file ''{0}'':  {1}
   */
  ERR_LDAPMODIFY_CANNOT_CREATE_REJECT_WRITER("An error occurred while attempting to create a reject writer to write rejected changes to file ''{0}'':  {1}"),



  /**
   * A search operation used to identify entries below ''{0}'' matching filter ''{1}'' succeeded, but an error occurred while attempting to decode the simple paged results response control contained in the search result.  Search processing cannot continue.
   */
  ERR_LDAPMODIFY_CANNOT_DECODE_PAGED_RESULTS_CONTROL("A search operation used to identify entries below ''{0}'' matching filter ''{1}'' succeeded, but an error occurred while attempting to decode the simple paged results response control contained in the search result.  Search processing cannot continue."),



  /**
   * Unable to obtain an LDAP connection to use to search for entries beneath ''{0}'' that match filter ''{1}'':  {2}
   */
  ERR_LDAPMODIFY_CANNOT_GET_SEARCH_CONNECTION("Unable to obtain an LDAP connection to use to search for entries beneath ''{0}'' that match filter ''{1}'':  {2}"),



  /**
   * An error occurred while trying to start an LDAP transaction:  {0}
   */
  ERR_LDAPMODIFY_CANNOT_START_TXN("An error occurred while trying to start an LDAP transaction:  {0}"),



  /**
   * An error occurred while trying to delete entry ''{0}'':
   */
  ERR_LDAPMODIFY_CLIENT_SIDE_SUB_DEL_ERROR("An error occurred while trying to delete entry ''{0}'':"),



  /**
   * {0,number,0} entries were successfully deleted.
   */
  ERR_LDAPMODIFY_CLIENT_SIDE_SUB_DEL_FINAL_DEL_COUNT("{0,number,0} entries were successfully deleted."),



  /**
   * One entry was successfully deleted.
   */
  ERR_LDAPMODIFY_CLIENT_SIDE_SUB_DEL_FINAL_DEL_COUNT_1("One entry was successfully deleted."),



  /**
   * {0,number,0} delete errors were encountered.
   */
  ERR_LDAPMODIFY_CLIENT_SIDE_SUB_DEL_FINAL_DEL_ERR_COUNT("{0,number,0} delete errors were encountered."),



  /**
   * One delete error was encountered.
   */
  ERR_LDAPMODIFY_CLIENT_SIDE_SUB_DEL_FINAL_DEL_ERR_COUNT_1("One delete error was encountered."),



  /**
   * The client-side subtree delete attempt did not complete successfully.
   */
  ERR_LDAPMODIFY_CLIENT_SIDE_SUB_DEL_FINAL_ERR_BASE("The client-side subtree delete attempt did not complete successfully."),



  /**
   * One or more errors occurred while searching for entries to delete.
   */
  ERR_LDAPMODIFY_CLIENT_SIDE_SUB_DEL_FINAL_SEARCH_ERR("One or more errors occurred while searching for entries to delete."),



  /**
   * At least one error occurred while searching for entries to delete.  The first search error encountered was:
   */
  ERR_LDAPMODIFY_CLIENT_SIDE_SUB_DEL_SEARCH_ERROR("At least one error occurred while searching for entries to delete.  The first search error encountered was:"),



  /**
   * The client-side subtree delete completed successfully, but did not delete any entries, presumably because base entry ''{0}'' did not exist.
   */
  ERR_LDAPMODIFY_CLIENT_SIDE_SUB_DEL_SUCCEEDED_WITH_0_ENTRIES("The client-side subtree delete completed successfully, but did not delete any entries, presumably because base entry ''{0}'' did not exist."),



  /**
   * Encryption passphrase file ''{0}'' is empty.
   */
  ERR_LDAPMODIFY_ENCRYPTION_PW_FILE_EMPTY("Encryption passphrase file ''{0}'' is empty."),



  /**
   * Encryption passphrase file ''{0}''has multiple lines.  The file must contain exactly one line, which is comprised entirely of the encryption passphrase.
   */
  ERR_LDAPMODIFY_ENCRYPTION_PW_FILE_MULTIPLE_LINES("Encryption passphrase file ''{0}''has multiple lines.  The file must contain exactly one line, which is comprised entirely of the encryption passphrase."),



  /**
   * An error occurred while trying to read the encryption passphrase from file ''{0}'':  {1}
   */
  ERR_LDAPMODIFY_ENCRYPTION_PW_FILE_READ_ERROR("An error occurred while trying to read the encryption passphrase from file ''{0}'':  {1}"),



  /**
   * An error occurred while attempting to open DN file ''{0}'' for reading:  {1}
   */
  ERR_LDAPMODIFY_ERROR_OPENING_DN_FILE("An error occurred while attempting to open DN file ''{0}'' for reading:  {1}"),



  /**
   * An error occurred while attempting to open filter file ''{0}'' for reading:  {1}
   */
  ERR_LDAPMODIFY_ERROR_OPENING_FILTER_FILE("An error occurred while attempting to open filter file ''{0}'' for reading:  {1}"),



  /**
   * The lines that comprise the invalid change record are as follows:
   */
  ERR_LDAPMODIFY_INVALID_LINES("The lines that comprise the invalid change record are as follows:"),



  /**
   * Invalid value ''{0}'' provided for password update behavior property ''{1}''.  The value must be either ''true'' or ''false''.
   */
  ERR_LDAPMODIFY_INVALID_PW_UPDATE_BOOLEAN_VALUE("Invalid value ''{0}'' provided for password update behavior property ''{1}''.  The value must be either ''true'' or ''false''."),



  /**
   * An I/O error occurred while attempting to read an LDIF change record:  {0}.  It is not possible to continue processing changes.
   */
  ERR_LDAPMODIFY_IO_ERROR_READING_CHANGE("An I/O error occurred while attempting to read an LDIF change record:  {0}.  It is not possible to continue processing changes."),



  /**
   * An I/O error occurred while attempting to read from filter file ''{0}'':  {1}
   */
  ERR_LDAPMODIFY_IO_ERROR_READING_DN_FILE("An I/O error occurred while attempting to read from filter file ''{0}'':  {1}"),



  /**
   * An I/O error occurred while attempting to read from filter file ''{0}'':  {1}
   */
  ERR_LDAPMODIFY_IO_ERROR_READING_FILTER_FILE("An I/O error occurred while attempting to read from filter file ''{0}'':  {1}"),



  /**
   * Value ''{0}'' provided for argument ''{1}'' is invalid.  The value should be in the form ''name=value'', where name is one of ''is-self-change'', ''allow-pre-encoded-password'', ''skip-password-validation'', ''ignore-password-history'', ''ignore-minimum-password-age'', ''password-storage-scheme'', or ''must-change-password''.  For all property names except ''password-storage-scheme'', the value should be either ''true'' or ''false'' (for example, ''is-self-change=true'').  For the ''password-storage-scheme'' property, the value should be the name of the desired password storage scheme (for example, ''password-storage-scheme=PBKDF2'').
   */
  ERR_LDAPMODIFY_MALFORMED_PW_UPDATE_BEHAVIOR("Value ''{0}'' provided for argument ''{1}'' is invalid.  The value should be in the form ''name=value'', where name is one of ''is-self-change'', ''allow-pre-encoded-password'', ''skip-password-validation'', ''ignore-password-history'', ''ignore-minimum-password-age'', ''password-storage-scheme'', or ''must-change-password''.  For all property names except ''password-storage-scheme'', the value should be either ''true'' or ''false'' (for example, ''is-self-change=true'').  For the ''password-storage-scheme'' property, the value should be the name of the desired password storage scheme (for example, ''password-storage-scheme=PBKDF2'')."),



  /**
   * A search operation used to identify entries below ''{0}'' matching filter ''{1}'' succeeded, but the search result did not include the expected simple paged results response control needed to determine whether there are more entries to be processed.  Search processing cannot continue.
   */
  ERR_LDAPMODIFY_MISSING_PAGED_RESULTS_RESPONSE("A search operation used to identify entries below ''{0}'' matching filter ''{1}'' succeeded, but the search result did not include the expected simple paged results response control needed to determine whether there are more entries to be processed.  Search processing cannot continue."),



  /**
   * Refusing to process a non-modify operation when the {0} argument was provided.
   */
  ERR_LDAPMODIFY_NON_MODIFY_WITH_BULK("Refusing to process a non-modify operation when the {0} argument was provided."),



  /**
   * An invalid LDIF change record was encountered while reading changes at or near line {0}:  {1}.  That change will be skipped, but the ldapmodify tool will continue trying to read additional changes to process.
   */
  ERR_LDAPMODIFY_RECOVERABLE_LDIF_ERROR_READING_CHANGE("An invalid LDIF change record was encountered while reading changes at or near line {0}:  {1}.  That change will be skipped, but the ldapmodify tool will continue trying to read additional changes to process."),



  /**
   * Value ''{0}'' provided for argument ''{1}'' is invalid.  The value must contain the ID of the entry-balancing request processor followed by a colon and the ID of a desired backend set to use for that entry-balancing request processor.
   */
  ERR_LDAPMODIFY_ROUTE_TO_BACKEND_SET_INVALID_FORMAT("Value ''{0}'' provided for argument ''{1}'' is invalid.  The value must contain the ID of the entry-balancing request processor followed by a colon and the ID of a desired backend set to use for that entry-balancing request processor."),



  /**
   * A search operation used to identify entries below ''{0}'' matching filter ''{1}'' returned a non-success result.
   */
  ERR_LDAPMODIFY_SEARCH_FAILED("A search operation used to identify entries below ''{0}'' matching filter ''{1}'' returned a non-success result."),



  /**
   * A search operation used to identify entries below ''{0}'' matching filter ''{1}'' returned a non-success result indicating that the connection may no longer be valid, but it was not possible to obtain a replacement connection to re-try the search.
   */
  ERR_LDAPMODIFY_SEARCH_FAILED_CANNOT_RECONNECT("A search operation used to identify entries below ''{0}'' matching filter ''{1}'' returned a non-success result indicating that the connection may no longer be valid, but it was not possible to obtain a replacement connection to re-try the search."),



  /**
   * A search result reference was returned while attempting to modify entries below ''{0}'' matching filter ''{1}''.  The referral will not be followed.  The reference included the following referral URL(s):  {2}.
   */
  ERR_LDAPMODIFY_SEARCH_LISTENER_REFERRAL("A search result reference was returned while attempting to modify entries below ''{0}'' matching filter ''{1}''.  The referral will not be followed.  The reference included the following referral URL(s):  {2}."),



  /**
   * Unable to write information about the rejected change to file ''{0}'':  {1}
   */
  ERR_LDAPMODIFY_UNABLE_TO_WRITE_REJECTED_CHANGE("Unable to write information about the rejected change to file ''{0}'':  {1}"),



  /**
   * Unrecognized password update behavior ''{0}''.  The supported behaviors are:  ''is-self-change'', ''allow-pre-encoded-password'', ''skip-password-validation'', ''ignore-password-history'', ''ignore-minimum-password-age'', ''password-storage-scheme'', and ''must-change-password''.
   */
  ERR_LDAPMODIFY_UNRECOGNIZED_PW_UPDATE_BEHAVIOR("Unrecognized password update behavior ''{0}''.  The supported behaviors are:  ''is-self-change'', ''allow-pre-encoded-password'', ''skip-password-validation'', ''ignore-password-history'', ''ignore-minimum-password-age'', ''password-storage-scheme'', and ''must-change-password''."),



  /**
   * An invalid LDIF change record was encountered while reading changes at or near line {0}:  {1}.  That change will be skipped, and the ldapmodify tool cannot continue processing changes.
   */
  ERR_LDAPMODIFY_UNRECOVERABLE_LDIF_ERROR_READING_CHANGE("An invalid LDIF change record was encountered while reading changes at or near line {0}:  {1}.  That change will be skipped, and the ldapmodify tool cannot continue processing changes."),



  /**
   * Unsupported change record:
   */
  ERR_LDAPMODIFY_UNSUPPORTED_CHANGE_RECORD_HEADER("Unsupported change record:"),



  /**
   * An error occurred while attempting to create a connection pool to communicate with the directory server:  {0}
   */
  ERR_LDAPSEARCH_CANNOT_CREATE_CONNECTION_POOL("An error occurred while attempting to create a connection pool to communicate with the directory server:  {0}"),



  /**
   * An error occurred while attempting to decode the simple paged results response control included in the search result:  {0}.  It is not possible to continue paging through the search results.
   */
  ERR_LDAPSEARCH_CANNOT_DECODE_PAGED_RESULTS_RESPONSE_CONTROL("An error occurred while attempting to decode the simple paged results response control included in the search result:  {0}.  It is not possible to continue paging through the search results."),



  /**
   * Unable to open output file ''{0}'' for writing:  {1}
   */
  ERR_LDAPSEARCH_CANNOT_OPEN_OUTPUT_FILE("Unable to open output file ''{0}'' for writing:  {1}"),



  /**
   * An error occurred while attempting to read from filter URL file ''{0}'':  {1}
   */
  ERR_LDAPSEARCH_CANNOT_READ_FILTER_FILE("An error occurred while attempting to read from filter URL file ''{0}'':  {1}"),



  /**
   * An error occurred while attempting to read from LDAP URL file ''{0}'':  {1}
   */
  ERR_LDAPSEARCH_CANNOT_READ_LDAP_URL_FILE("An error occurred while attempting to read from LDAP URL file ''{0}'':  {1}"),



  /**
   * The first trailing argument ''{0}'' is not a valid LDAP search filter.
   */
  ERR_LDAPSEARCH_FIRST_TRAILING_ARG_NOT_FILTER("The first trailing argument ''{0}'' is not a valid LDAP search filter."),



  /**
   * Filter ''{0}'' is a valid search filter, but is not valid for use with the matched values request control.
   */
  ERR_LDAPSEARCH_INVALID_MATCHED_VALUES_FILTER("Filter ''{0}'' is a valid search filter, but is not valid for use with the matched values request control."),



  /**
   * The value provided for the ''{0}'' argument is invalid.  The value may be ''search-base'' to use the base DN from the search request as the base DN for join searches, ''source-entry-dn'' to use the DN of the source entry as the base DN for join searches, or any valid DN to use that DN as the base DN for join searches.
   */
  ERR_LDAPSEARCH_JOIN_BASE_DN_INVALID_VALUE("The value provided for the ''{0}'' argument is invalid.  The value may be ''search-base'' to use the base DN from the search request as the base DN for join searches, ''source-entry-dn'' to use the DN of the source entry as the base DN for join searches, or any valid DN to use that DN as the base DN for join searches."),



  /**
   * The value provided for the ''{0}'' argument is invalid.  The value must be one of the following:  the string ''dn:'' followed by the name or OID of an attribute in the source entry whose values are the DNs of the entries with which to join; the string ''reverse-dn:'' followed by the name or OID of an attribute in the target entry with a value that matches the DN of the source entry; the string ''equals:'' followed by the name or OID of an attribute in the source entry, a colon, and the name or OID of an attribute in target entries that must have a value that equals at least one value of the source attribute; or the string ''contains:'' followed by the name or OID of an attribute in the source entry, a colon, and the name or OID of an attribute in target entries that must have a value that matches or contains as a substring at least one value of the source attribute.
   */
  ERR_LDAPSEARCH_JOIN_RULE_INVALID_VALUE("The value provided for the ''{0}'' argument is invalid.  The value must be one of the following:  the string ''dn:'' followed by the name or OID of an attribute in the source entry whose values are the DNs of the entries with which to join; the string ''reverse-dn:'' followed by the name or OID of an attribute in the target entry with a value that matches the DN of the source entry; the string ''equals:'' followed by the name or OID of an attribute in the source entry, a colon, and the name or OID of an attribute in target entries that must have a value that equals at least one value of the source attribute; or the string ''contains:'' followed by the name or OID of an attribute in the source entry, a colon, and the name or OID of an attribute in target entries that must have a value that matches or contains as a substring at least one value of the source attribute."),



  /**
   * Filter file ''{0}'' contains a value that cannot be parsed as a valid LDAP search filter:  {1}
   */
  ERR_LDAPSEARCH_MALFORMED_FILTER("Filter file ''{0}'' contains a value that cannot be parsed as a valid LDAP search filter:  {1}"),



  /**
   * LDAP URL file ''{0}'' contains value ''{1}'' that cannot be parsed as a valid LDAP URL.
   */
  ERR_LDAPSEARCH_MALFORMED_LDAP_URL("LDAP URL file ''{0}'' contains value ''{1}'' that cannot be parsed as a valid LDAP URL."),



  /**
   * The value provided for the ''{0}'' argument is invalid.  The value must start with ''examineCount='' followed by an integer greater than or equal to zero.  It may optionally also contain '':alwaysExamine'' (to indicate that it should always attempt to examine candidate entries to determine whether they actually match), '':allowUnindexed'' (to indicate that the server should attempt to determine the matching entry count even if the search criteria is not indexed), '':skipResolvingExplodedIndexes'' (to indicate whether the server should skip the effort of actually retrieving the candidate entry IDs for exploded index keys in which the number of matching entries is known), '':fastShortCircuitThreshold='' followed by an integer value (to specify the short-circuit threshold that the server should use when determining whether to continue with index processing, even if there may be additional filter components that may be very fast to process), '':slowShortCircuitThreshold='' followed by an integer value (to specify the short-circuit threshold that should be used if it is expected that there may be potentially-expensive filter components left to evaluate), and/or '':debug'' (to indicate that the response control should include debug information about the processing performed in the course of making the determination.
   */
  ERR_LDAPSEARCH_MATCHING_ENTRY_COUNT_INVALID_VALUE("The value provided for the ''{0}'' argument is invalid.  The value must start with ''examineCount='' followed by an integer greater than or equal to zero.  It may optionally also contain '':alwaysExamine'' (to indicate that it should always attempt to examine candidate entries to determine whether they actually match), '':allowUnindexed'' (to indicate that the server should attempt to determine the matching entry count even if the search criteria is not indexed), '':skipResolvingExplodedIndexes'' (to indicate whether the server should skip the effort of actually retrieving the candidate entry IDs for exploded index keys in which the number of matching entries is known), '':fastShortCircuitThreshold='' followed by an integer value (to specify the short-circuit threshold that the server should use when determining whether to continue with index processing, even if there may be additional filter components that may be very fast to process), '':slowShortCircuitThreshold='' followed by an integer value (to specify the short-circuit threshold that should be used if it is expected that there may be potentially-expensive filter components left to evaluate), and/or '':debug'' (to indicate that the response control should include debug information about the processing performed in the course of making the determination."),



  /**
   * The search request included the simple paged results request control, but the corresponding search result did not include the expected response control.  It is not possible to continue paging through the search results.
   */
  ERR_LDAPSEARCH_MISSING_PAGED_RESULTS_RESPONSE_CONTROL("The search request included the simple paged results request control, but the corresponding search result did not include the expected response control.  It is not possible to continue paging through the search results."),



  /**
   * The --moveSubtreeFrom argument must be provided the same number of times as the --moveSubtreeTo argument.
   */
  ERR_LDAPSEARCH_MOVE_SUBTREE_MISMATCH("The --moveSubtreeFrom argument must be provided the same number of times as the --moveSubtreeTo argument."),



  /**
   * If neither the ''{0}'' nor the ''{1}'' argument is provided, the filter to use for the search must be provided as a trailing argument.
   */
  ERR_LDAPSEARCH_NO_TRAILING_ARGS("If neither the ''{0}'' nor the ''{1}'' argument is provided, the filter to use for the search must be provided as a trailing argument."),



  /**
   * The ''{0}'' output format cannot be used in conjunction with the ''{1}'' argument.
   */
  ERR_LDAPSEARCH_OUTPUT_FORMAT_NOT_SUPPORTED_WITH_URLS("The ''{0}'' output format cannot be used in conjunction with the ''{1}'' argument."),



  /**
   * The ''{0}'' output format requires that the set of requested arguments be identified with the ''{1}'' argument rather than as unnamed trailing arguments.
   */
  ERR_LDAPSEARCH_OUTPUT_FORMAT_REQUIRES_REQUESTED_ATTR_ARG("The ''{0}'' output format requires that the set of requested arguments be identified with the ''{1}'' argument rather than as unnamed trailing arguments."),



  /**
   * A value provided for the ''{0}'' argument is invalid because there are multiple attempts to set a value for property ''{1}''.  The same property cannot be set multiple times.
   */
  ERR_LDAPSEARCH_OVERRIDE_LIMIT_DUPLICATE_PROPERTY_NAME("A value provided for the ''{0}'' argument is invalid because there are multiple attempts to set a value for property ''{1}''.  The same property cannot be set multiple times."),



  /**
   * A value provided for the ''{0}'' argument is invalid because it starts with an equal sign, indicating an empty property name.  Property names must not be empty.
   */
  ERR_LDAPSEARCH_OVERRIDE_LIMIT_EMPTY_PROPERTY_NAME("A value provided for the ''{0}'' argument is invalid because it starts with an equal sign, indicating an empty property name.  Property names must not be empty."),



  /**
   * A value provided for the ''{0}'' argument is invalid because it contains an empty value for property ''{1}''.  Property values must not be empty.
   */
  ERR_LDAPSEARCH_OVERRIDE_LIMIT_EMPTY_PROPERTY_VALUE("A value provided for the ''{0}'' argument is invalid because it contains an empty value for property ''{1}''.  Property values must not be empty."),



  /**
   * A value provided for the ''{0}'' argument is invalid because it does not contain an equal sign to separate the property name from the value.
   */
  ERR_LDAPSEARCH_OVERRIDE_LIMIT_NO_EQUAL("A value provided for the ''{0}'' argument is invalid because it does not contain an equal sign to separate the property name from the value."),



  /**
   * The value provided for the ''{0}'' argument is invalid.  The value must start with ''ps''.  That may be followed by a colon and a comma-delimited list of change types (''add'', ''delete'', ''modify'', or ''modifydn''), or ''any'' to indicate that all change types should be included.  That may be followed by a colon and a ''1'' to indicate that the search should only return matching entries when they are argeted by an appropriate operation, or ''0'' to indicate that the search should first return all existing entries that match the criteria before transitioning to returning to returning entries as updates occur.  That may be followed by a colon and a ''1'' to request that the server include entry change notification controls with search result entries, or ''0'' to request no such controls.  A value of just ''ps'' is equivalent to ''ps:any:1:1''.
   */
  ERR_LDAPSEARCH_PERSISTENT_SEARCH_INVALID_VALUE("The value provided for the ''{0}'' argument is invalid.  The value must start with ''ps''.  That may be followed by a colon and a comma-delimited list of change types (''add'', ''delete'', ''modify'', or ''modifydn''), or ''any'' to indicate that all change types should be included.  That may be followed by a colon and a ''1'' to indicate that the search should only return matching entries when they are argeted by an appropriate operation, or ''0'' to indicate that the search should first return all existing entries that match the criteria before transitioning to returning to returning entries as updates occur.  That may be followed by a colon and a ''1'' to request that the server include entry change notification controls with search result entries, or ''0'' to request no such controls.  A value of just ''ps'' is equivalent to ''ps:any:1:1''."),



  /**
   * The --renameAttributeFrom argument must be provided the same number of times as the --renameAttributeTo argument.
   */
  ERR_LDAPSEARCH_RENAME_ATTRIBUTE_MISMATCH("The --renameAttributeFrom argument must be provided the same number of times as the --renameAttributeTo argument."),



  /**
   * Value ''{0}'' provided for argument ''{1}'' is invalid.  The value must contain the ID of the entry-balancing request processor followed by a colon and the ID of a desired backend set to use for that entry-balancing request processor.
   */
  ERR_LDAPSEARCH_ROUTE_TO_BACKEND_SET_INVALID_FORMAT("Value ''{0}'' provided for argument ''{1}'' is invalid.  The value must contain the ID of the entry-balancing request processor followed by a colon and the ID of a desired backend set to use for that entry-balancing request processor."),



  /**
   * The value provided for the ''{0}'' argument is invalid.  The value must be a comma-delimited list of one or more attribute names or OIDs, each of which may be prefixed by ''-'' to indicate that sorting for that attribute should be in descending order, or ''+'' to indicate that sorting for that attribute should be in ascending order.  If an attribute name is not prefixed with either ''-'' or ''+'', then sorting for that attribute will be in ascending order.
   */
  ERR_LDAPSEARCH_SORT_ORDER_INVALID_VALUE("The value provided for the ''{0}'' argument is invalid.  The value must be a comma-delimited list of one or more attribute names or OIDs, each of which may be prefixed by ''-'' to indicate that sorting for that attribute should be in descending order, or ''+'' to indicate that sorting for that attribute should be in ascending order.  If an attribute name is not prefixed with either ''-'' or ''+'', then sorting for that attribute will be in ascending order."),



  /**
   * Trailing arguments are not allowed when the ''{0}'' argument is provided.
   */
  ERR_LDAPSEARCH_TRAILING_ARGS_WITH_URL_FILE("Trailing arguments are not allowed when the ''{0}'' argument is provided."),



  /**
   * If a filter file is specified, then a filter cannot be provided as a trailing argument.
   */
  ERR_LDAPSEARCH_TRAILING_FILTER_WITH_FILTER_FILE("If a filter file is specified, then a filter cannot be provided as a trailing argument."),



  /**
   * The value provided for the ''{0}'' argument is invalid.  The value must either be a colon-delimited list of four integers (representing the before count; after count; offset index; and expected result set size, with zero used if the expected result set size is not known), or a colon-delimited list of two integers and one string (representing the before count, after count, and the primary sort attribute value that should be used as the start of the result set).
   */
  ERR_LDAPSEARCH_VLV_INVALID_VALUE("The value provided for the ''{0}'' argument is invalid.  The value must either be a colon-delimited list of four integers (representing the before count; after count; offset index; and expected result set size, with zero used if the expected result set size is not known), or a colon-delimited list of two integers and one string (representing the before count, after count, and the primary sort attribute value that should be used as the start of the result set)."),



  /**
   * No matching result codes were found.
   */
  ERR_LDAP_RC_NO_RESULTS("No matching result codes were found."),



  /**
   * An error occurred while attempting to base64-decode the DN in line ''{0}'' read from file ''{1}'':  {2}
   */
  ERR_MANAGE_ACCT_CANNOT_BASE64_DECODE_DN("An error occurred while attempting to base64-decode the DN in line ''{0}'' read from file ''{1}'':  {2}"),



  /**
   * An error occurred while attempting to create a connection pool to perform {0} processing:  {1}
   */
  ERR_MANAGE_ACCT_CANNOT_CREATE_CONNECTION_POOL("An error occurred while attempting to create a connection pool to perform {0} processing:  {1}"),



  /**
   * An error occurred while attempting to initialize the password policy state operation processor:  {0}
   */
  ERR_MANAGE_ACCT_CANNOT_CREATE_PROCESSOR("An error occurred while attempting to initialize the password policy state operation processor:  {0}"),



  /**
   * An error occurred while attempting to initialize the variable-rate adjustor to alter the maximum load over time based on the definitions contained in file ''{0}'':  {1}
   */
  ERR_MANAGE_ACCT_CANNOT_CREATE_RATE_ADJUSTOR("An error occurred while attempting to initialize the variable-rate adjustor to alter the maximum load over time based on the definitions contained in file ''{0}'':  {1}"),



  /**
   * An error occurred while attempting to create a reject writer to write information about failed operation attempts to file ''{0}'':  {1}
   */
  ERR_MANAGE_ACCT_CANNOT_CREATE_REJECT_WRITER("An error occurred while attempting to create a reject writer to write information about failed operation attempts to file ''{0}'':  {1}"),



  /**
   * An error occurred while attempting to write a sample variable rate definition to file ''{0}'':  {1}
   */
  ERR_MANAGE_ACCT_CANNOT_GENERATE_SAMPLE_RATE_FILE("An error occurred while attempting to write a sample variable rate definition to file ''{0}'':  {1}"),



  /**
   * An error occurred while attempting to read from DN file ''{0}'':  {1}
   */
  ERR_MANAGE_ACCT_ERROR_READING_DN_FILE("An error occurred while attempting to read from DN file ''{0}'':  {1}"),



  /**
   * An error occurred while attempting to read from filter file ''{0}'':  {1}
   */
  ERR_MANAGE_ACCT_ERROR_READING_FILTER_FILE("An error occurred while attempting to read from filter file ''{0}'':  {1}"),



  /**
   * An error occurred while attempting to read from user ID file ''{0}'':  {1}
   */
  ERR_MANAGE_ACCT_ERROR_READING_USER_ID_FILE("An error occurred while attempting to read from user ID file ''{0}'':  {1}"),



  /**
   * Filter file ''{0}'' contains line ''{1}'' that cannot be parsed as a valid search filter:  {2}
   */
  ERR_MANAGE_ACCT_MALFORMED_FILTER_FROM_FILE("Filter file ''{0}'' contains line ''{1}'' that cannot be parsed as a valid search filter:  {2}"),



  /**
   * Unable to perform any {0} processing because no subcommand was selected.
   */
  ERR_MANAGE_ACCT_PROCESSOR_NO_SUBCOMMAND("Unable to perform any {0} processing because no subcommand was selected."),



  /**
   * Support for the ''{0}'' subcommand is not currently implemented in the {1} tool.
   */
  ERR_MANAGE_ACCT_PROCESSOR_UNSUPPORTED_SUBCOMMAND("Support for the ''{0}'' subcommand is not currently implemented in the {1} tool."),



  /**
   * Unable to get an LDAP connection to use to process search request {0}:  {1}
   */
  ERR_MANAGE_ACCT_SEARCH_OP_CANNOT_GET_CONNECTION("Unable to get an LDAP connection to use to process search request {0}:  {1}"),



  /**
   * An error occurred while attempting to retrieve a simple paged results response control from search result {0} for filter {1}:  {2}
   */
  ERR_MANAGE_ACCT_SEARCH_OP_ERROR_READING_PAGE_RESPONSE("An error occurred while attempting to retrieve a simple paged results response control from search result {0} for filter {1}:  {2}"),



  /**
   * An unexpected exception was raised while attempting to process search request {0}:  {1}
   */
  ERR_MANAGE_ACCT_SEARCH_OP_EXCEPTION("An unexpected exception was raised while attempting to process search request {0}:  {1}"),



  /**
   * An error occurred while attempting to search for entries matching filter {0}:  {1} {2}.  No retry attempt will be made for this search.
   */
  ERR_MANAGE_ACCT_SEARCH_OP_FAILED_NO_RETRY("An error occurred while attempting to search for entries matching filter {0}:  {1} {2}.  No retry attempt will be made for this search."),



  /**
   * An error occurred while attempting to search for entries matching filter {0}:  {1} {2}.  This was the second attempt to process the search and no further retry attempts will be made.
   */
  ERR_MANAGE_ACCT_SEARCH_OP_FAILED_SECOND_ATTEMPT("An error occurred while attempting to search for entries matching filter {0}:  {1} {2}.  This was the second attempt to process the search and no further retry attempts will be made."),



  /**
   * Unable to read schema information from file ''{0}'':  {1}
   */
  ERR_OID_LOOKUP_CANNOT_GET_SCHEMA_FROM_FILE("Unable to read schema information from file ''{0}'':  {1}"),



  /**
   * ERROR:  Unable to create the LDIF reader that will be used to read change records from file ''{0}'':  {1}
   */
  ERR_PARALLEL_UPDATE_CANNOT_CREATE_LDIF_READER("ERROR:  Unable to create the LDIF reader that will be used to read change records from file ''{0}'':  {1}"),



  /**
   * ERROR:  Unable to create the connection pool to use for processing requests:  {0}
   */
  ERR_PARALLEL_UPDATE_CANNOT_CREATE_POOL("ERROR:  Unable to create the connection pool to use for processing requests:  {0}"),



  /**
   * ERROR:  Unable to join the progress monitor thread after processing is expected to be complete:  {0}
   */
  ERR_PARALLEL_UPDATE_CANNOT_JOIN_PROGRESS_MONITOR("ERROR:  Unable to join the progress monitor thread after processing is expected to be complete:  {0}"),



  /**
   * ERROR:  Unable to join operation processing thread ''{0}'' after processing is expected to be complete:  {1}
   */
  ERR_PARALLEL_UPDATE_CANNOT_JOIN_THREAD("ERROR:  Unable to join operation processing thread ''{0}'' after processing is expected to be complete:  {1}"),



  /**
   * ERROR:  Unable to write message ''{0}'' to log file ''{1}'':  {2}.  Aborting processing.
   */
  ERR_PARALLEL_UPDATE_CANNOT_WRITE_LOG_MESSAGE("ERROR:  Unable to write message ''{0}'' to log file ''{1}'':  {2}.  Aborting processing."),



  /**
   * ERROR:  Unable to write a reject message for change record {0} with comment {1}:  {2}.  Aborting processing.
   */
  ERR_PARALLEL_UPDATE_CANNOT_WRITE_REJECT("ERROR:  Unable to write a reject message for change record {0} with comment {1}:  {2}.  Aborting processing."),



  /**
   * ERROR:  An attempt to enqueue a change record failed:  {0}
   */
  ERR_PARALLEL_UPDATE_ENQUEUE_FAILED("ERROR:  An attempt to enqueue a change record failed:  {0}"),



  /**
   * ERROR:  An unexpected error occurred while attempting to close the log writer used to write a record of processing to file ''{0}'':  {1}
   */
  ERR_PARALLEL_UPDATE_ERROR_CLOSING_LOG_WRITER("ERROR:  An unexpected error occurred while attempting to close the log writer used to write a record of processing to file ''{0}'':  {1}"),



  /**
   * ERROR:  An unexpected error occurred while attempting to close the reject writer used to write rejected change records to file ''{0}'':  {1}
   */
  ERR_PARALLEL_UPDATE_ERROR_CLOSING_REJECT_WRITER("ERROR:  An unexpected error occurred while attempting to close the reject writer used to write rejected change records to file ''{0}'':  {1}"),



  /**
   * ERROR:  Unable to create the log writer that will write log messages to file ''{0}'':  {1}
   */
  ERR_PARALLEL_UPDATE_ERROR_CREATING_LOG_WRITER("ERROR:  Unable to create the log writer that will write log messages to file ''{0}'':  {1}"),



  /**
   * ERROR:  Unable to create the reject writer that will be used to write rejected change records to file ''{0}'':  {1}
   */
  ERR_PARALLEL_UPDATE_ERROR_CREATING_REJECT_WRITER("ERROR:  Unable to create the reject writer that will be used to write rejected change records to file ''{0}'':  {1}"),



  /**
   * ERROR:  An unexpected error occurred while attempting to read an LDIF change record from file ''{0}'':  {1}  Aborting processing.
   */
  ERR_PARALLEL_UPDATE_ERROR_READING_LDIF_FILE("ERROR:  An unexpected error occurred while attempting to read an LDIF change record from file ''{0}'':  {1}  Aborting processing."),



  /**
   * ERROR:  Value ''{0}'' provided for argument ''{1}'' is invalid because it does not contain an equal sign to separate the property name from its value.
   */
  ERR_PARALLEL_UPDATE_MALFORMED_PW_UPDATE_VALUE("ERROR:  Value ''{0}'' provided for argument ''{1}'' is invalid because it does not contain an equal sign to separate the property name from its value."),



  /**
   * The retry pass was unable to successfully replay any of the {0,number,0} remaining changes.  Adding all of those changes to the rejects file.
   */
  ERR_PARALLEL_UPDATE_NO_PROGRESS_MULTIPLE("The retry pass was unable to successfully replay any of the {0,number,0} remaining changes.  Adding all of those changes to the rejects file."),



  /**
   * The retry pass was unable to successfully replay the remaining 1 change.  Adding that change to the rejects file.
   */
  ERR_PARALLEL_UPDATE_NO_PROGRESS_ONE("The retry pass was unable to successfully replay the remaining 1 change.  Adding that change to the rejects file."),



  /**
   * ERROR:  Value ''{0}'' provided for argument ''{1}'' is invalid because property ''{2}'' is expected to have a Boolean value of either ''true'' or ''false''.
   */
  ERR_PARALLEL_UPDATE_PW_UPDATE_VALUE_NOT_BOOLEAN("ERROR:  Value ''{0}'' provided for argument ''{1}'' is invalid because property ''{2}'' is expected to have a Boolean value of either ''true'' or ''false''."),



  /**
   * ERROR:  A recoverable error occurred while attempting to parse an LDIF change record read from file ''{0}'':  {1}  Skipping that malformed record and continuing processing.
   */
  ERR_PARALLEL_UPDATE_RECOVERABLE_LDIF_EXCEPTION("ERROR:  A recoverable error occurred while attempting to parse an LDIF change record read from file ''{0}'':  {1}  Skipping that malformed record and continuing processing."),



  /**
   * The following change record was rejected with result code {0} and message {1}:
   */
  ERR_PARALLEL_UPDATE_REJECT_COMMENT("The following change record was rejected with result code {0} and message {1}:"),



  /**
   * ERROR:  Value ''{0}'' provided for argument ''{1}'' is invalid because it uses an unsupported property name.  Allowed property names are ''{2}'', ''{3}'', ''{4}'', ''{5}'', ''{6}'', ''{7}'', and ''{8}''.
   */
  ERR_PARALLEL_UPDATE_UNKNOWN_PW_UPDATE_PROP("ERROR:  Value ''{0}'' provided for argument ''{1}'' is invalid because it uses an unsupported property name.  Allowed property names are ''{2}'', ''{3}'', ''{4}'', ''{5}'', ''{6}'', ''{7}'', and ''{8}''."),



  /**
   * ERROR:  An unrecoverable error occurred while attempting to parse an LDIF change record read from file ''{0}'':  {1}  Aborting processing.
   */
  ERR_PARALLEL_UPDATE_UNRECOVERABLE_LDIF_EXCEPTION("ERROR:  An unrecoverable error occurred while attempting to parse an LDIF change record read from file ''{0}'':  {1}  Aborting processing."),



  /**
   * ERROR:  No user identity was provided, and either no authentication credentials were provided or it was not possible to infer determine the identity of the authenticated connection.  Use the ''{0}'' argument to specify the identity of the user whose password should be updated.
   */
  ERR_PWMOD_CANNOT_DETERMINE_USER_IDENTITY("ERROR:  No user identity was provided, and either no authentication credentials were provided or it was not possible to infer determine the identity of the authenticated connection.  Use the ''{0}'' argument to specify the identity of the user whose password should be updated."),



  /**
   * An error occurred while attempting to prompt for the current password:  {0}
   */
  ERR_PWMOD_CANNOT_PROMPT_FOR_CURRENT_PW("An error occurred while attempting to prompt for the current password:  {0}"),



  /**
   * An error occurred while attempting to prompt for the new password:  {0}
   */
  ERR_PWMOD_CANNOT_PROMPT_FOR_NEW_PW("An error occurred while attempting to prompt for the new password:  {0}"),



  /**
   * An error occurred while attempting to read the current password from file ''{0}'':  {1}
   */
  ERR_PWMOD_CANNOT_READ_CURRENT_PW_FILE("An error occurred while attempting to read the current password from file ''{0}'':  {1}"),



  /**
   * An error occurred while attempting to read the new password from file ''{0}'':  {1}
   */
  ERR_PWMOD_CANNOT_READ_NEW_PW_FILE("An error occurred while attempting to read the new password from file ''{0}'':  {1}"),



  /**
   * The search to retrieve the server root DSE did not return an entry, which may be the result of insufficient access rights to view that entry.  Use the {0} argument to specify the appropriate mechanism to use when changing the password.
   */
  ERR_PWMOD_CANNOT_RETRIEVE_ROOT_DSE("The search to retrieve the server root DSE did not return an entry, which may be the result of insufficient access rights to view that entry.  Use the {0} argument to specify the appropriate mechanism to use when changing the password."),



  /**
   * The ''{0}'' argument does not allow empty values.
   */
  ERR_PWMOD_CHAR_SET_EMPTY("The ''{0}'' argument does not allow empty values."),



  /**
   * An error occurred while attempting to retrieve the server root DSE to determine the type of password update to perform (result code {0}, error message {1}).  Use the {2} argument to specify the appropriate mechanism to use when changing the password.
   */
  ERR_PWMOD_ERROR_GETTING_ROOT_DSE("An error occurred while attempting to retrieve the server root DSE to determine the type of password update to perform (result code {0}, error message {1}).  Use the {2} argument to specify the appropriate mechanism to use when changing the password."),



  /**
   * An error occurred while attempting to send the password modify extended request to the server or read its response.  The result code was {0} and the diagnostic message was {1}
   */
  ERR_PWMOD_EXTOP_ERROR("An error occurred while attempting to send the password modify extended request to the server or read its response.  The result code was {0} and the diagnostic message was {1}"),



  /**
   * The password modify extended operation failed with result code {0} and diagnostic message {1}
   */
  ERR_PWMOD_EXTOP_FAILED("The password modify extended operation failed with result code {0} and diagnostic message {1}"),



  /**
   * Unable to process referral result {0} obtained in response to a password modify extended operation because none of the referral URLs could be parsed as valid LDAP URLs.
   */
  ERR_PWMOD_EXTOP_NO_VALID_REFERRAL_URLS("Unable to process referral result {0} obtained in response to a password modify extended operation because none of the referral URLs could be parsed as valid LDAP URLs."),



  /**
   * Unable to follow a referral encountered while attempting to process the change using a password modify extended operation.  The first error encountered during processing had a result code of {0} and a diagnostic message of {1}
   */
  ERR_PWMOD_FOLLOW_REFERRAL_FAILED("Unable to follow a referral encountered while attempting to process the change using a password modify extended operation.  The first error encountered during processing had a result code of {0} and a diagnostic message of {1}"),



  /**
   * The modify operation failed with result code {0} and diagnostic message {1}
   */
  ERR_PWMOD_MODIFY_FAILED("The modify operation failed with result code {0} and diagnostic message {1}"),



  /**
   * ERROR:  The provided passwords do not match.
   */
  ERR_PWMOD_NEW_PW_MISMATCH("ERROR:  The provided passwords do not match."),



  /**
   * No new password was specified for the user.  While this is acceptable when using the password modify extended operation (because the server may generate a password on behalf of the client), it is not allowed when the password will be updated with a regular LDAP modify operation.  Use one of the {0}, {1}, {2}, or {3} arguments to specify the new password to use.
   */
  ERR_PWMOD_NO_NEW_PW_FOR_MODIFY("No new password was specified for the user.  While this is acceptable when using the password modify extended operation (because the server may generate a password on behalf of the client), it is not allowed when the password will be updated with a regular LDAP modify operation.  Use one of the {0}, {1}, {2}, or {3} arguments to specify the new password to use."),



  /**
   * ERROR:  The password must not be empty.
   */
  ERR_PWMOD_PW_EMPTY("ERROR:  The password must not be empty."),



  /**
   * Unable to determine the DN of the entry for the user with username ''{0}'' because the search to find that entry failed with result code {1} and diagnostic message {2}
   */
  ERR_PWMOD_SEARCH_FOR_USER_FAILED("Unable to determine the DN of the entry for the user with username ''{0}'' because the search to find that entry failed with result code {1} and diagnostic message {2}"),



  /**
   * Unable to determine the DN of the entry for the user with username ''{0}'' because multiple matching entries were found.  The username must uniquely identify the target user.
   */
  ERR_PWMOD_SEARCH_FOR_USER_MULTIPLE_MATCHES("Unable to determine the DN of the entry for the user with username ''{0}'' because multiple matching entries were found.  The username must uniquely identify the target user."),



  /**
   * Unable to determine the DN of the entry for the user with username ''{0}'' because no matching entries were found.
   */
  ERR_PWMOD_SEARCH_FOR_USER_NO_MATCHES("Unable to determine the DN of the entry for the user with username ''{0}'' because no matching entries were found."),



  /**
   * Too many referrals were encountered while attempting to process the change using a password modify extended operation.
   */
  ERR_PWMOD_TOO_MANY_REFERRALS("Too many referrals were encountered while attempting to process the change using a password modify extended operation."),



  /**
   * ERROR:  The DN of the target user for the password update was determined to be the null DN (that is, the DN of the root DSE entry).  This suggests that either an empty user identity was explicitly specified, that the empty user identity was inferred because no authentication credentials were provided, or that anonymous authentication was performed.  Use the ''{0}'' argument to specify the user identity, either as a valid, non-empty DN (optionally preceded by ''dn:'') or a non-empty username (which must be preceded by ''u:'').
   */
  ERR_PWMOD_USER_IDENTITY_EMPTY_DN("ERROR:  The DN of the target user for the password update was determined to be the null DN (that is, the DN of the root DSE entry).  This suggests that either an empty user identity was explicitly specified, that the empty user identity was inferred because no authentication credentials were provided, or that anonymous authentication was performed.  Use the ''{0}'' argument to specify the user identity, either as a valid, non-empty DN (optionally preceded by ''dn:'') or a non-empty username (which must be preceded by ''u:'')."),



  /**
   * ERROR:  The user identity for the password update was determined to be ''u:'', which contains an empty username.  Use the ''{0}'' argument to specify the identity of the user whose password should be updated.  The user identity may be provided as a non-empty DN (optionally preceded by ''dn:'') or a non-empty username (which must be preceded by ''u:'').
   */
  ERR_PWMOD_USER_IDENTITY_EMPTY_USERNAME("ERROR:  The user identity for the password update was determined to be ''u:'', which contains an empty username.  Use the ''{0}'' argument to specify the identity of the user whose password should be updated.  The user identity may be provided as a non-empty DN (optionally preceded by ''dn:'') or a non-empty username (which must be preceded by ''u:'')."),



  /**
   * ERROR:  The DN of the target user for the password update was determined to be ''{0}'', but this cannot be parsed as a valid distinguished name.  Use the ''{1}'' argument to specify the user identity, either as a valid, non-empty DN (optionally preceded by ''dn:'') or a non-empty username (which must be preceded by ''u:'').
   */
  ERR_PWMOD_USER_IDENTITY_NOT_VALID_DN("ERROR:  The DN of the target user for the password update was determined to be ''{0}'', but this cannot be parsed as a valid distinguished name.  Use the ''{1}'' argument to specify the user identity, either as a valid, non-empty DN (optionally preceded by ''dn:'') or a non-empty username (which must be preceded by ''u:'')."),



  /**
   * Unable to parse the entry DN:  {0}
   */
  ERR_SPLIT_LDIF_ATTR_HASH_TRANSLATOR_CANNOT_PARSE_DN("Unable to parse the entry DN:  {0}"),



  /**
   * Unable to determine which set should hold an entry more than one level below split base DN ''{0}'' because the tool was configured to assume a flat DIT and therefore is not maintaining the cache needed to ensure that a subordinate entry is placed in the same set as its parent.
   */
  ERR_SPLIT_LDIF_ATTR_HASH_TRANSLATOR_NON_FLAT_DIT("Unable to determine which set should hold an entry more than one level below split base DN ''{0}'' because the tool was configured to assume a flat DIT and therefore is not maintaining the cache needed to ensure that a subordinate entry is placed in the same set as its parent."),



  /**
   * Unable to determine which algorithm should be used to split the data.  One of the following subcommands must be used to specify the algorithm:  {0}
   */
  ERR_SPLIT_LDIF_CANNOT_DETERMINE_SPLIT_ALGORITHM("Unable to determine which algorithm should be used to split the data.  One of the following subcommands must be used to specify the algorithm:  {0}"),



  /**
   * An error occurred while attempting to open output file ''{0}'' for writing:  {1}
   */
  ERR_SPLIT_LDIF_CANNOT_OPEN_OUTPUT_FILE("An error occurred while attempting to open output file ''{0}'' for writing:  {1}"),



  /**
   * Unable to determine which set should hold entry ''{0}'' because it is more than one level below split base DN ''{1}'' but one or more of its superiors does not exist in the LDIF file.
   */
  ERR_SPLIT_LDIF_ENTRY_WITHOUT_PARENT("Unable to determine which set should hold entry ''{0}'' because it is more than one level below split base DN ''{1}'' but one or more of its superiors does not exist in the LDIF file."),



  /**
   * An error was encountered while attempting to close output file ''{0}'':  {1}.  The file may be incomplete.
   */
  ERR_SPLIT_LDIF_ERROR_CLOSING_FILE("An error was encountered while attempting to close output file ''{0}'':  {1}.  The file may be incomplete."),



  /**
   * An error occurred while attempting to create the LDIF reader:  {0}
   */
  ERR_SPLIT_LDIF_ERROR_CREATING_LDIF_READER("An error occurred while attempting to create the LDIF reader:  {0}"),



  /**
   * An error occurred while attempting to read schema information from the specified schema path(s):  {0}
   */
  ERR_SPLIT_LDIF_ERROR_LOADING_SCHEMA("An error occurred while attempting to read schema information from the specified schema path(s):  {0}"),



  /**
   * An error occurred while attempting to write information about error ''{0}'' to output file ''{1}'':  {2}
   */
  ERR_SPLIT_LDIF_ERROR_WRITING_ERROR_TO_FILE("An error occurred while attempting to write information about error ''{0}'' to output file ''{1}'':  {2}"),



  /**
   * An error occurred while attempting to write entry ''{0}'' to output file ''{1}'':  {2}
   */
  ERR_SPLIT_LDIF_ERROR_WRITING_TO_FILE("An error occurred while attempting to write entry ''{0}'' to output file ''{1}'':  {2}"),



  /**
   * Unable to parse the entry DN:  {0}
   */
  ERR_SPLIT_LDIF_FEWEST_ENTRIES_TRANSLATOR_CANNOT_PARSE_DN("Unable to parse the entry DN:  {0}"),



  /**
   * Unable to determine which set should hold an entry more than one level below split base DN ''{0}'' because the tool was configured to assume a flat DIT and therefore is not maintaining the cache needed to ensure that a subordinate entry is placed in the same set as its parent.
   */
  ERR_SPLIT_LDIF_FEWEST_ENTRIES_TRANSLATOR_NON_FLAT_DIT("Unable to determine which set should hold an entry more than one level below split base DN ''{0}'' because the tool was configured to assume a flat DIT and therefore is not maintaining the cache needed to ensure that a subordinate entry is placed in the same set as its parent."),



  /**
   * Unable to parse the entry DN:  {0}
   */
  ERR_SPLIT_LDIF_FILTER_TRANSLATOR_CANNOT_PARSE_DN("Unable to parse the entry DN:  {0}"),



  /**
   * Unable to determine which set should hold an entry more than one level below split base DN ''{0}'' because the tool was configured to assume a flat DIT and therefore is not maintaining the cache needed to ensure that a subordinate entry is placed in the same set as its parent.
   */
  ERR_SPLIT_LDIF_FILTER_TRANSLATOR_NON_FLAT_DIT("Unable to determine which set should hold an entry more than one level below split base DN ''{0}'' because the tool was configured to assume a flat DIT and therefore is not maintaining the cache needed to ensure that a subordinate entry is placed in the same set as its parent."),



  /**
   * A malformed LDIF record was encountered while in the source data:  {0}.  This record will be skipped, but processing will continue.
   */
  ERR_SPLIT_LDIF_INVALID_LDIF_RECORD_RECOVERABLE("A malformed LDIF record was encountered while in the source data:  {0}.  This record will be skipped, but processing will continue."),



  /**
   * A malformed LDIF record was encountered while in the source data:  {0}.  Processing cannot continue.
   */
  ERR_SPLIT_LDIF_INVALID_LDIF_RECORD_UNRECOVERABLE("A malformed LDIF record was encountered while in the source data:  {0}.  Processing cannot continue."),



  /**
   * An I/O error occurred while attempting to read the source data:  {0}.  Processing cannot continue.
   */
  ERR_SPLIT_LDIF_IO_READ_ERROR("An I/O error occurred while attempting to read the source data:  {0}.  Processing cannot continue."),



  /**
   * All values provided for the ''{0}'' argument must be unique.  Filter ''{1}'' was provided multiple times.
   */
  ERR_SPLIT_LDIF_NON_UNIQUE_FILTER("All values provided for the ''{0}'' argument must be unique.  Filter ''{1}'' was provided multiple times."),



  /**
   * If the ''{0}'' subcommand is used, the ''{1}'' argument must be provided multiple times, with each occurrence defining the criteria for a different set.
   */
  ERR_SPLIT_LDIF_NOT_ENOUGH_FILTERS("If the ''{0}'' subcommand is used, the ''{1}'' argument must be provided multiple times, with each occurrence defining the criteria for a different set."),



  /**
   * The {0} argument was provided, but no schema files were found in the specified path(s).
   */
  ERR_SPLIT_LDIF_NO_SCHEMA_FILES("The {0} argument was provided, but no schema files were found in the specified path(s)."),



  /**
   * If the {0} argument is provided multiple times, then the {1} argument must also be provided to specify the base path and name for the file to be written.
   */
  ERR_SPLIT_LDIF_NO_TARGET_BASE_PATH("If the {0} argument is provided multiple times, then the {1} argument must also be provided to specify the base path and name for the file to be written."),



  /**
   * Unable to parse the entry DN:  {0}
   */
  ERR_SPLIT_LDIF_RDN_HASH_TRANSLATOR_CANNOT_PARSE_DN("Unable to parse the entry DN:  {0}"),



  /**
   * Unable to obtain an MD5 message digest generator:  {0}
   */
  ERR_SPLIT_LDIF_TRANSLATOR_CANNOT_GET_MD5("Unable to obtain an MD5 message digest generator:  {0}"),



  /**
   * An unexpected error was encountered while attempting to read the source data:  {0}.  Processing cannot continue.
   */
  ERR_SPLIT_LDIF_UNEXPECTED_READ_ERROR("An unexpected error was encountered while attempting to read the source data:  {0}.  Processing cannot continue."),



  /**
   * Unable to parse value ''{0}'' of property ''{1}'' in tool invocation logging properties file ''{2}'' as a Boolean value.
   */
  ERR_TOOL_LOGGER_CANNOT_PARSE_BOOLEAN_PROPERTY("Unable to parse value ''{0}'' of property ''{1}'' in tool invocation logging properties file ''{2}'' as a Boolean value."),



  /**
   * An error occurred while trying to load the tool invocation logging properties from file ''{0}'':  {1}
   */
  ERR_TOOL_LOGGER_ERROR_LOADING_PROPERTIES_FILE("An error occurred while trying to load the tool invocation logging properties from file ''{0}'':  {1}"),



  /**
   * An error occurred while trying to open log file ''{0}'' for writing an invocation log message:  {1}
   */
  ERR_TOOL_LOGGER_ERROR_OPENING_LOG_FILE("An error occurred while trying to open log file ''{0}'' for writing an invocation log message:  {1}"),



  /**
   * An error occurred while trying to write a tool invocation log message to file ''{0}'':  {1}
   */
  ERR_TOOL_LOGGER_ERROR_WRITING_LOG_MESSAGE("An error occurred while trying to write a tool invocation log message to file ''{0}'':  {1}"),



  /**
   * No invocation logging will be performed for command ''{0}'' because properties file ''{1}'' does not have a valid value for either the ''{2}'' or the ''{3}'' property.
   */
  ERR_TOOL_LOGGER_NO_LOG_FILES("No invocation logging will be performed for command ''{0}'' because properties file ''{1}'' does not have a valid value for either the ''{2}'' or the ''{3}'' property."),



  /**
   * Value ''{0}'' for property ''{1}'' in tool invocation logging properties file ''{2}'' cannot be used as an invocation log file because the path references a directory rather than a file.
   */
  ERR_TOOL_LOGGER_PATH_NOT_FILE("Value ''{0}'' for property ''{1}'' in tool invocation logging properties file ''{2}'' cannot be used as an invocation log file because the path references a directory rather than a file."),



  /**
   * Value ''{0}'' for property ''{1}'' in tool invocation logging properties file ''{2}'' cannot be used as an invocation log file because parent directory ''{3}'' either does not exist or is not a directory.
   */
  ERR_TOOL_LOGGER_PATH_PARENT_MISSING("Value ''{0}'' for property ''{1}'' in tool invocation logging properties file ''{2}'' cannot be used as an invocation log file because parent directory ''{3}'' either does not exist or is not a directory."),



  /**
   * Unable to acquire an exclusive lock on tool invocation log file ''{0}'' after {1,number,0} attempts.
   */
  ERR_TOOL_LOGGER_UNABLE_TO_ACQUIRE_FILE_LOCK("Unable to acquire an exclusive lock on tool invocation log file ''{0}'' after {1,number,0} attempts."),



  /**
   * LDIF file ''{0}'' is encrypted, but an error occurred while attempting to decrypt it:  {1}
   */
  ERR_TOOL_UTILS_ENCRYPTED_LDIF_FILE_CANNOT_DECRYPT("LDIF file ''{0}'' is encrypted, but an error occurred while attempting to decrypt it:  {1}"),



  /**
   * The provided passphrase is incorrect.
   */
  ERR_TOOL_UTILS_ENCRYPTED_LDIF_FILE_WRONG_PW("The provided passphrase is incorrect."),



  /**
   * ERROR:  The encryption passphrase must not be empty.
   */
  ERR_TOOL_UTILS_ENCRYPTION_PW_EMPTY("ERROR:  The encryption passphrase must not be empty."),



  /**
   * Encryption passphrase file ''{0}'' is empty.  The file must consist of exactly one line that is comprised entirely of the encryption passphrase.
   */
  ERR_TOOL_UTILS_ENCRYPTION_PW_FILE_EMPTY("Encryption passphrase file ''{0}'' is empty.  The file must consist of exactly one line that is comprised entirely of the encryption passphrase."),



  /**
   * Encryption passphrase file ''{0}'' does not exist.
   */
  ERR_TOOL_UTILS_ENCRYPTION_PW_FILE_MISSING("Encryption passphrase file ''{0}'' does not exist."),



  /**
   * Encryption passphrase file ''{0}'' contains multiple lines.  The file must consist of exactly one line that is comprised entirely of the encryption passphrase.
   */
  ERR_TOOL_UTILS_ENCRYPTION_PW_FILE_MULTIPLE_LINES("Encryption passphrase file ''{0}'' contains multiple lines.  The file must consist of exactly one line that is comprised entirely of the encryption passphrase."),



  /**
   * Encryption passphrase file ''{0}'' exists but is not a file.
   */
  ERR_TOOL_UTILS_ENCRYPTION_PW_FILE_NOT_FILE("Encryption passphrase file ''{0}'' exists but is not a file."),



  /**
   * An error occurred while attempting to read the encryption passphrase from file ''{0}'':  {1}
   */
  ERR_TOOL_UTILS_ENCRYPTION_PW_FILE_READ_ERROR("An error occurred while attempting to read the encryption passphrase from file ''{0}'':  {1}"),



  /**
   * ERROR:  The passphrases do not match.
   */
  ERR_TOOL_UTILS_ENCRYPTION_PW_MISMATCH("ERROR:  The passphrases do not match."),



  /**
   * An error occurred while attempting to write output using the values-only output format:  {0}
   */
  ERR_VALUES_ONLY_OUTPUT_FORMAT_WRITE_ERROR("An error occurred while attempting to write output using the values-only output format:  {0}"),



  /**
   * Indicates that the collect-support-data request should be sent to a server over LDAP rather than operating against the local instance.
   */
  INFO_CSD_ARG_DEC_USE_REMOTE_SERVER("Indicates that the collect-support-data request should be sent to a server over LDAP rather than operating against the local instance."),



  /**
   * Indicates that the resulting support data archive should include the source code (if available) for any third-party extensions installed in the server.
   */
  INFO_CSD_ARG_DESC_ARCHIVE_EXTENSION_SOURCE("Indicates that the resulting support data archive should include the source code (if available) for any third-party extensions installed in the server."),



  /**
   * Indicates that the tool should attempt to collect information that may be time-consuming or resource-intensive to obtain, or that may affect server performance or responsiveness.
   */
  INFO_CSD_ARG_DESC_COLLECT_EXPENSIVE_DATA("Indicates that the tool should attempt to collect information that may be time-consuming or resource-intensive to obtain, or that may affect server performance or responsiveness."),



  /**
   * Indicates that the support data archive should include a replication state dump, which may be several megabytes in size.  This argument cannot be used in conjunction with the --noLDAP argument.
   */
  INFO_CSD_ARG_DESC_COLLECT_REPL_STATE("Indicates that the support data archive should include a replication state dump, which may be several megabytes in size.  This argument cannot be used in conjunction with the --noLDAP argument."),



  /**
   * Provides a comment that will be included in a README file within the support data archive.  This comment may provide additional context about the issue being investigated.
   */
  INFO_CSD_ARG_DESC_COMMENT("Provides a comment that will be included in a README file within the support data archive.  This comment may provide additional context about the issue being investigated."),



  /**
   * Decrypts the encrypted support data archive contained in the specified file.  If the --passphraseFile argument is provided, then it will be used to obtain the passphrase to use to generate the encryption key.  Otherwise, the passphrase will be interactively requested.
   */
  INFO_CSD_ARG_DESC_DECRYPT("Decrypts the encrypted support data archive contained in the specified file.  If the --passphraseFile argument is provided, then it will be used to obtain the passphrase to use to generate the encryption key.  Otherwise, the passphrase will be interactively requested."),



  /**
   * Indicates that the tool should validate the set of arguments but should not actually generate the support data archive.  If the --useRemoteServer argument is provided, then the extended request will include the LDAP no-operation request control.
   */
  INFO_CSD_ARG_DESC_DRY_RUN("Indicates that the tool should validate the set of arguments but should not actually generate the support data archive.  If the --useRemoteServer argument is provided, then the extended request will include the LDAP no-operation request control."),



  /**
   * Include log messages within the specified duration before the current time.  If specified, the value must consist of an integer followed by a time unit, which may be one of millisecond, second, minute, hour, or day, or their plurals (e.g., ''5 minutes'' or ''1 hour'').
   */
  INFO_CSD_ARG_DESC_DURATION("Include log messages within the specified duration before the current time.  If specified, the value must consist of an integer followed by a time unit, which may be one of millisecond, second, minute, hour, or day, or their plurals (e.g., ''5 minutes'' or ''1 hour'')."),



  /**
   * Indicates that the resulting support data archive should be encrypted.  If the --passphraseFile argument is provided in conjunction with the --generatePassphrase argument, then a passphrase will dynamically generated and written to the specified file.  If the --passphraseFile argument is provided without the --generatePassphrase argument, then that file must exist and it will be read to obtain the passphrase used to generate the encryption key.  If the --passphraseFile argument is not provided, then the encryption passphrase will be interactively requested.  Note that when providing an encrypted collect-support-data archive to support personnel, it is strongly recommended that the passphrase be provided over a separate channel than the encrypted archive itself to help prevent unauthorized third-party access.
   */
  INFO_CSD_ARG_DESC_ENCRYPT("Indicates that the resulting support data archive should be encrypted.  If the --passphraseFile argument is provided in conjunction with the --generatePassphrase argument, then a passphrase will dynamically generated and written to the specified file.  If the --passphraseFile argument is provided without the --generatePassphrase argument, then that file must exist and it will be read to obtain the passphrase used to generate the encryption key.  If the --passphraseFile argument is not provided, then the encryption passphrase will be interactively requested.  Note that when providing an encrypted collect-support-data archive to support personnel, it is strongly recommended that the passphrase be provided over a separate channel than the encrypted archive itself to help prevent unauthorized third-party access."),



  /**
   * Indicates that the tool should automatically generate the passphrase used to generate the encryption key and write it to the file specified by the --passphraseFile argument.  This argument may only be used if both the --encrypt and --passphraseFile arguments are also provided.
   */
  INFO_CSD_ARG_DESC_GENERATE_PASSPHRASE("Indicates that the tool should automatically generate the passphrase used to generate the encryption key and write it to the file specified by the --passphraseFile argument.  This argument may only be used if both the --encrypt and --passphraseFile arguments are also provided."),



  /**
   * Indicates that the support data archive should include any binary files that would have otherwise been omitted, but that may contain information that could be helpful in investigating the underlying issue.  Note that sensitive information in these binary files may not be obscured or redacted in the same way that it would be in plain text files.
   */
  INFO_CSD_ARG_DESC_INCLUDE_BINARY_FILES("Indicates that the support data archive should include any binary files that would have otherwise been omitted, but that may contain information that could be helpful in investigating the underlying issue.  Note that sensitive information in these binary files may not be obscured or redacted in the same way that it would be in plain text files."),



  /**
   * The number of times the jstack tool should be invoked to obtain stack traces of all threads running in the JVM.  A value of zero indicates that the jstack tool should not be invoked.  If this argument is not provided, the tool will be invoked ten times by default.
   */
  INFO_CSD_ARG_DESC_JSTACK_COUNT("The number of times the jstack tool should be invoked to obtain stack traces of all threads running in the JVM.  A value of zero indicates that the jstack tool should not be invoked.  If this argument is not provided, the tool will be invoked ten times by default."),



  /**
   * The amount of data in kilobytes that the tool should capture from the beginning of each log file.  If this is specified, then the value must be greater than or equal to zero.
   */
  INFO_CSD_ARG_DESC_LOG_HEAD_SIZE_KB("The amount of data in kilobytes that the tool should capture from the beginning of each log file.  If this is specified, then the value must be greater than or equal to zero."),



  /**
   * The amount of data in kilobytes that the tool should capture from the end of each log file.  If this is specified, then the value must be greater than or equal to zero.
   */
  INFO_CSD_ARG_DESC_LOG_TAIL_SIZE_KB("The amount of data in kilobytes that the tool should capture from the end of each log file.  If this is specified, then the value must be greater than or equal to zero."),



  /**
   * Indicates that no attempt should be made to collect any information over LDAP.  This option should only be used as a last resort if the server is completely unresponsive or will not start, and it must not be used in conjunction with the --useRemoteServer argument.
   */
  INFO_CSD_ARG_DESC_NO_LDAP("Indicates that no attempt should be made to collect any information over LDAP.  This option should only be used as a last resort if the server is completely unresponsive or will not start, and it must not be used in conjunction with the --useRemoteServer argument."),



  /**
   * Indicates that the tool should not interactively prompt for any information, but should fail if any required information is not provided.
   */
  INFO_CSD_ARG_DESC_NO_PROMPT("Indicates that the tool should not interactively prompt for any information, but should fail if any required information is not provided."),



  /**
   * Specifies the path to which the support data archive should be written.  If this path references a file that already exists, then that file will be overwritten with the new support data archive.  If it references a directory that already exists, then a new support data archive will be created in that directory with a name that is dynamically generated.  If it references a file that does not exist, then the parent directory must exist, and a new support data archive file will be created with that path and name.
   */
  INFO_CSD_ARG_DESC_OUTPUT_PATH("Specifies the path to which the support data archive should be written.  If this path references a file that already exists, then that file will be overwritten with the new support data archive.  If it references a directory that already exists, then a new support data archive will be created in that directory with a name that is dynamically generated.  If it references a file that does not exist, then the parent directory must exist, and a new support data archive file will be created with that path and name."),



  /**
   * The path to a file containing the passphrase used to encrypt or decrypt the support data archive.  If this file exists, then it must contain exactly one line that consists entirely of the encryption passphrase.  If this file does not exist, then the --generatePassphrase argument must have also been provided, and the generated passphrase will be written into the specified file.
   */
  INFO_CSD_ARG_DESC_PASSPHRASE_FILE("The path to a file containing the passphrase used to encrypt or decrypt the support data archive.  If this file exists, then it must contain exactly one line that consists entirely of the encryption passphrase.  If this file does not exist, then the --generatePassphrase argument must have also been provided, and the generated passphrase will be written into the specified file."),



  /**
   * Specifies the process ID of an additional process about which information should be collected.  This option may be useful for troubleshooting external server tools.  It may be provided multiple times to specify multiple process IDs.
   */
  INFO_CSD_ARG_DESC_PID("Specifies the process ID of an additional process about which information should be collected.  This option may be useful for troubleshooting external server tools.  It may be provided multiple times to specify multiple process IDs."),



  /**
   * The address of a server to which the collect-support-data extended request should be forwarded.  This may be useful for retrieving support data information from a backend Directory Server instance that can only be accessed through a Directory Proxy Server.  The server to which the initial request will be sent (the one specified by the --hostname and --port arguments) must either be configured with an LDAP external server that has the specified proxy-to address and port values or there must be a server instance with that address and port in the topology registry.  This argument may only be provided if both the --useRemoteServer and --proxyToServerPort arguments are also given.
   */
  INFO_CSD_ARG_DESC_PROXY_TO_ADDRESS("The address of a server to which the collect-support-data extended request should be forwarded.  This may be useful for retrieving support data information from a backend Directory Server instance that can only be accessed through a Directory Proxy Server.  The server to which the initial request will be sent (the one specified by the --hostname and --port arguments) must either be configured with an LDAP external server that has the specified proxy-to address and port values or there must be a server instance with that address and port in the topology registry.  This argument may only be provided if both the --useRemoteServer and --proxyToServerPort arguments are also given."),



  /**
   * The port of a server to which the collect-support-data extended request should be forwarded.  This argument may only be provided if both the --useRemoteServer and --proxyToServerAddress arguments are also given.
   */
  INFO_CSD_ARG_DESC_PROXY_TO_PORT("The port of a server to which the collect-support-data extended request should be forwarded.  This argument may only be provided if both the --useRemoteServer and --proxyToServerAddress arguments are also given."),



  /**
   * The number of intervals of data to collect from tools that use interval-based sampling (e.g., vmstat, iostat, mpstat, etc.).  A value of zero indicates that no information should be collected from these tools.  If this argument is not provided, then data will be collected from ten intervals by default.
   */
  INFO_CSD_ARG_DESC_REPORT_COUNT("The number of intervals of data to collect from tools that use interval-based sampling (e.g., vmstat, iostat, mpstat, etc.).  A value of zero indicates that no information should be collected from these tools.  If this argument is not provided, then data will be collected from ten intervals by default."),



  /**
   * The number of seconds that should elapse between intervals from tools that use interval-based sampling (e.g., vmstat, iostat, mpstat, etc.).  If this argument is provided, the value must be greater than zero.  If it is not provided, an interval duration of one second will be used by default.
   */
  INFO_CSD_ARG_DESC_REPORT_INTERVAL_SECONDS("The number of seconds that should elapse between intervals from tools that use interval-based sampling (e.g., vmstat, iostat, mpstat, etc.).  If this argument is provided, the value must be greater than zero.  If it is not provided, an interval duration of one second will be used by default."),



  /**
   * Indicates that the tool should generate script-friendly output.
   */
  INFO_CSD_ARG_DESC_SCRIPT_FRIENDLY("Indicates that the tool should generate script-friendly output."),



  /**
   * Specifies the degree to which the tool will attempt to obscure or omit potentially sensitive information.  A value of ''none'' indicates that the tool will not attempt to obscure or redact any information.  A value of ''obscure-secrets'' indicates that the tool will attempt to obscure secret information (like the values of sensitive configuration properties) and omit log files containing user data (like the data recovery log).  A value of ''maximum'' indicates that the tool will take even more drastic measures, like omitting access log files and obscuring attribute values in entry DNs and search filters, but at the risk of omitting information that could be useful in investigating the associated issue.  If this is not provided, a value of ''obscure-secrets'' will be used by default.
   */
  INFO_CSD_ARG_DESC_SECURITY_LEVEL("Specifies the degree to which the tool will attempt to obscure or omit potentially sensitive information.  A value of ''none'' indicates that the tool will not attempt to obscure or redact any information.  A value of ''obscure-secrets'' indicates that the tool will attempt to obscure secret information (like the values of sensitive configuration properties) and omit log files containing user data (like the data recovery log).  A value of ''maximum'' indicates that the tool will take even more drastic measures, like omitting access log files and obscuring attribute values in entry DNs and search filters, but at the risk of omitting information that could be useful in investigating the associated issue.  If this is not provided, a value of ''obscure-secrets'' will be used by default."),



  /**
   * Collect data sequentially rather than in parallel.  This has the effect of reducing the initial memory footprint of this tool, but at the expense of taking longer to complete.  Use this option if running without it results in out-of-memory errors.
   */
  INFO_CSD_ARG_DESC_SEQUENTIAL("Collect data sequentially rather than in parallel.  This has the effect of reducing the initial memory footprint of this tool, but at the expense of taking longer to complete.  Use this option if running without it results in out-of-memory errors."),



  /**
   * Include log messages falling within the indicated time range.  The start and end time values should be formatted in the generalized time format (e.g., ''YYYYMMDDhhmmss.uuuZ''), a format similar to the generalized time format but without the time zone indicator to indicate that the value should be in the local time zone (e.g., ''YYYYMMDDhhmmss.uuu''), or the server''s default log timestamp format (e.g., ''[DD/MMM/YYYY:hh:mm:ss Z]'' or ''[DD/MMM/YYYY:hh:mm:ss.uuu Z]'').  If the end time is omitted, the current time will be used.
   */
  INFO_CSD_ARG_DESC_TIME_RANGE("Include log messages falling within the indicated time range.  The start and end time values should be formatted in the generalized time format (e.g., ''YYYYMMDDhhmmss.uuuZ''), a format similar to the generalized time format but without the time zone indicator to indicate that the value should be in the local time zone (e.g., ''YYYYMMDDhhmmss.uuu''), or the server''s default log timestamp format (e.g., ''[DD/MMM/YYYY:hh:mm:ss Z]'' or ''[DD/MMM/YYYY:hh:mm:ss.uuu Z]'').  If the end time is omitted, the current time will be used."),



  /**
   * Indicates that the tool should attempt to use an administrative session to process all operations using a dedicated pool of worker threads.  This may be useful when trying to diagnose problems in a server that is unresponsive because all normal worker threads are busy processing other requests.  This argument may only be used in conjunction with the --useRemoteServer argument.
   */
  INFO_CSD_ARG_DESC_USE_ADMIN_SESSION("Indicates that the tool should attempt to use an administrative session to process all operations using a dedicated pool of worker threads.  This may be useful when trying to diagnose problems in a server that is unresponsive because all normal worker threads are busy processing other requests.  This argument may only be used in conjunction with the --useRemoteServer argument."),



  /**
   * Data Collection Arguments
   */
  INFO_CSD_ARG_GROUP_COLLECTION("Data Collection Arguments"),



  /**
   * Communication Arguments
   */
  INFO_CSD_ARG_GROUP_COMMUNICATION("Communication Arguments"),



  /**
   * Output Arguments
   */
  INFO_CSD_ARG_GROUP_OUTPUT("Output Arguments"),



  /**
   * '{'address'}'
   */
  INFO_CSD_ARG_PLACEHOLDER_ADDRESS("'{'address'}'"),



  /**
   * '{'count'}'
   */
  INFO_CSD_ARG_PLACEHOLDER_COUNT("'{'count'}'"),



  /**
   * '{'pid'}'
   */
  INFO_CSD_ARG_PLACEHOLDER_PID("'{'pid'}'"),



  /**
   * '{'port'}'
   */
  INFO_CSD_ARG_PLACEHOLDER_PORT("'{'port'}'"),



  /**
   * '{'seconds'}'
   */
  INFO_CSD_ARG_PLACEHOLDER_SECONDS("'{'seconds'}'"),



  /**
   * '{'none|obscure-secrets|maximum'}'
   */
  INFO_CSD_ARG_PLACEHOLDER_SECURITY_LEVEL("'{'none|obscure-secrets|maximum'}'"),



  /**
   * '{'sizeKB'}'
   */
  INFO_CSD_ARG_PLACEHOLDER_SIZE_KB("'{'sizeKB'}'"),



  /**
   * '{'startTime[,endTime]'}'
   */
  INFO_CSD_ARG_PLACEHOLDER_TIME_RANGE("'{'startTime[,endTime]'}'"),



  /**
   * The collect-support-data extended operation completed with result code {0} and no diagnostic message.
   */
  INFO_CSD_COMPLETED_WITH_RESULT_CODE("The collect-support-data extended operation completed with result code {0} and no diagnostic message."),



  /**
   * Collects information about the local server instance using the default settings
   */
  INFO_CSD_EXAMPLE_1("Collects information about the local server instance using the default settings"),



  /**
   * Collects information about the specified remote instance, including log messages from the last 10 minutes and a more extensive set of support data.  The output will be encrypted with a generated passphrase written to a file, and the maximum amount of sanitization will be used.
   */
  INFO_CSD_EXAMPLE_2("Collects information about the specified remote instance, including log messages from the last 10 minutes and a more extensive set of support data.  The output will be encrypted with a generated passphrase written to a file, and the maximum amount of sanitization will be used."),



  /**
   * Decrypts an encrypted support data archive using a passphrase read from the specified file.
   */
  INFO_CSD_EXAMPLE_3("Decrypts an encrypted support data archive using a passphrase read from the specified file."),



  /**
   * Wrote {0} of {1} bytes of the support data archive {2}.
   */
  INFO_CSD_LISTENER_WROTE_FRAGMENT("Wrote {0} of {1} bytes of the support data archive {2}."),



  /**
   * Exiting without actually invoking the collect-support-data processing because the --dryRun argument was provided.
   */
  INFO_CSD_LOCAL_MODE_DRY_RUN("Exiting without actually invoking the collect-support-data processing because the --dryRun argument was provided."),



  /**
   * Confirm the passphrase:
   */
  INFO_CSD_PASSPHRASE_CONFIRM_PROMPT("Confirm the passphrase:"),



  /**
   * Enter the passphrase to use to generate the encryption key:
   */
  INFO_CSD_PASSPHRASE_INITIAL_PROMPT("Enter the passphrase to use to generate the encryption key:"),



  /**
   * Collect and package system information useful in troubleshooting problems.  The information is packaged as a zip archive that can be sent to a technical support representative.
   */
  INFO_CSD_TOOL_DESCRIPTION_1("Collect and package system information useful in troubleshooting problems.  The information is packaged as a zip archive that can be sent to a technical support representative."),



  /**
   * Information collected may include configuration files, server monitor entries, portions of log files, JVM thread stack dumps, system metrics, and other data that may be helpful in diagnosing problems, understanding server performance, or otherwise assisting with support requests.  Although the tool will do its best to obscure or omit sensitive data, and the entire archive may be encrypted if you desire, you may wish to review the resulting support data archive to ensure verify its contents.  Further, the archive will include a summary of any potential problems or concerns that are identified in the course of collecting the support data.
   */
  INFO_CSD_TOOL_DESCRIPTION_2("Information collected may include configuration files, server monitor entries, portions of log files, JVM thread stack dumps, system metrics, and other data that may be helpful in diagnosing problems, understanding server performance, or otherwise assisting with support requests.  Although the tool will do its best to obscure or omit sensitive data, and the entire archive may be encrypted if you desire, you may wish to review the resulting support data archive to ensure verify its contents.  Further, the archive will include a summary of any potential problems or concerns that are identified in the course of collecting the support data."),



  /**
   * An authentication ID that identifies the user for whom the TOTP shared secret should be generated.  This must be provided, and it should either be in the form ''dn:'' followed by the DN of the target user''s entry, or in the form ''u:'' followed by the username for the target user.
   */
  INFO_GEN_TOTP_SECRET_DESCRIPTION_AUTH_ID("An authentication ID that identifies the user for whom the TOTP shared secret should be generated.  This must be provided, and it should either be in the form ''dn:'' followed by the DN of the target user''s entry, or in the form ''u:'' followed by the username for the target user."),



  /**
   * Indicates that the tool should interactively prompt for the static password for the user targeted by the {0} argument.
   */
  INFO_GEN_TOTP_SECRET_DESCRIPTION_PROMPT_FOR_USER_PW("Indicates that the tool should interactively prompt for the static password for the user targeted by the {0} argument."),



  /**
   * Indicates that the server should revoke the provided TOTP shared secret rather than generating a new secret for the specified user.
   */
  INFO_GEN_TOTP_SECRET_DESCRIPTION_REVOKE("Indicates that the server should revoke the provided TOTP shared secret rather than generating a new secret for the specified user."),



  /**
   * Indicates that the server should revoke all existing TOTP shared secrets for the specified user rather than generating a new secret.
   */
  INFO_GEN_TOTP_SECRET_DESCRIPTION_REVOKE_ALL("Indicates that the server should revoke all existing TOTP shared secrets for the specified user rather than generating a new secret."),



  /**
   * The static password for the user targeted by the {0} argument.
   */
  INFO_GEN_TOTP_SECRET_DESCRIPTION_USER_PW("The static password for the user targeted by the {0} argument."),



  /**
   * The path to a file containing the static password for the user targeted by the {0} argument.
   */
  INFO_GEN_TOTP_SECRET_DESCRIPTION_USER_PW_FILE("The path to a file containing the static password for the user targeted by the {0} argument."),



  /**
   * Enter the static password for user ''{0}'':
   */
  INFO_GEN_TOTP_SECRET_ENTER_PW("Enter the static password for user ''{0}'':"),



  /**
   * Generate a TOTP secret for the user with username ''john.doe'' and a password entered at an interactive prompt.
   */
  INFO_GEN_TOTP_SECRET_GEN_EXAMPLE("Generate a TOTP secret for the user with username ''john.doe'' and a password entered at an interactive prompt."),



  /**
   * Successfully generated TOTP shared secret ''{0}''.
   */
  INFO_GEN_TOTP_SECRET_GEN_SUCCESS("Successfully generated TOTP shared secret ''{0}''."),



  /**
   * '{'authID'}'
   */
  INFO_GEN_TOTP_SECRET_PLACEHOLDER_AUTH_ID("'{'authID'}'"),



  /**
   * '{'totpSharedSecret'}'
   */
  INFO_GEN_TOTP_SECRET_PLACEHOLDER_SECRET("'{'totpSharedSecret'}'"),



  /**
   * '{'password'}'
   */
  INFO_GEN_TOTP_SECRET_PLACEHOLDER_USER_PW("'{'password'}'"),



  /**
   * Revoke all TOTP shared secrets for the user with username ''john.doe'' whose password is contained in file ''password.txt''.
   */
  INFO_GEN_TOTP_SECRET_REVOKE_ALL_EXAMPLE("Revoke all TOTP shared secrets for the user with username ''john.doe'' whose password is contained in file ''password.txt''."),



  /**
   * Successfully revoked all TOTP shared secrets.
   */
  INFO_GEN_TOTP_SECRET_REVOKE_ALL_SUCCESS("Successfully revoked all TOTP shared secrets."),



  /**
   * Successfully revoked TOTP shared secret ''{0}''.
   */
  INFO_GEN_TOTP_SECRET_REVOKE_SUCCESS("Successfully revoked TOTP shared secret ''{0}''."),



  /**
   * Generate a shared secret that may be used to generate time-based one-time password (TOTP) authentication codes for use in authenticating with the UNBOUNDID-TOTP SASL mechanism, or in conjunction with the validate TOTP password extended operation.
   */
  INFO_GEN_TOTP_SECRET_TOOL_DESC("Generate a shared secret that may be used to generate time-based one-time password (TOTP) authentication codes for use in authenticating with the UNBOUNDID-TOTP SASL mechanism, or in conjunction with the validate TOTP password extended operation."),



  /**
   * The path to a file with information about the compare assertions to be evaluated.  If this is provided, then each line of the file must contain the DN of the target entry followed by one or more horizontal tab characters and the attribute-value assertion to process against that entry (specified as the attribute name followed by a single colon and the string representation of the assertion value, the attribute name followed by two colons and a base64-encoded representation of the assertion value, or the attribute name followed by a colon, a less-than sign, and the path to a file from which the raw assertion value should be read).
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_ASSERTION_FILE("The path to a file with information about the compare assertions to be evaluated.  If this is provided, then each line of the file must contain the DN of the target entry followed by one or more horizontal tab characters and the attribute-value assertion to process against that entry (specified as the attribute name followed by a single colon and the string representation of the assertion value, the attribute name followed by two colons and a base64-encoded representation of the assertion value, or the attribute name followed by a colon, a less-than sign, and the path to a file from which the raw assertion value should be read)."),



  /**
   * Include the LDAP assertion request control (as described in RFC 4528) in all compare requests to indicate that the compare operation should only be evaluated if the target entry matches the provided filter.
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_ASSERTION_FILTER("Include the LDAP assertion request control (as described in RFC 4528) in all compare requests to indicate that the compare operation should only be evaluated if the target entry matches the provided filter."),



  /**
   * Include the authorization identity request control (as described in RFC 3829) in all bind requests.  If this control is included, a successful bind result should include the authorization identity assigned to the connection.
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_AUTHZ_IDENTITY("Include the authorization identity request control (as described in RFC 3829) in all bind requests.  If this control is included, a successful bind result should include the authorization identity assigned to the connection."),



  /**
   * Include the indicated control in all bind requests used to authenticate to the server.
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_BIND_CONTROL("Include the indicated control in all bind requests used to authenticate to the server."),



  /**
   * Include the indicated control in all compare requests sent to the directory server.
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_COMPARE_CONTROL("Include the indicated control in all compare requests sent to the directory server."),



  /**
   * Continue processing even if there are errors.
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_CONTINUE_ON_ERROR("Continue processing even if there are errors."),



  /**
   * The path to a file containing the DNs of the entries in which to perform the compare assertions.  If this argument is provided, then each DN must be provided on its own line with no other content on that line, and there must be exactly one unnamed trailing argument to specify the attribute-value assertion to evaluate against all entries referenced in the DN file.  If this argument is not provided, then there must be at least two unnamed trailing arguments, with the first specifying the attribute-value assertion and all remaining trailing arguments providing the DNs of the entries to evaluate.
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_DN_FILE("The path to a file containing the DNs of the entries in which to perform the compare assertions.  If this argument is provided, then each DN must be provided on its own line with no other content on that line, and there must be exactly one unnamed trailing argument to specify the attribute-value assertion to evaluate against all entries referenced in the DN file.  If this argument is not provided, then there must be at least two unnamed trailing arguments, with the first specifying the attribute-value assertion and all remaining trailing arguments providing the DNs of the entries to evaluate."),



  /**
   * Indicate which compare assertions would be processed, but do not actually send them to the server.
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_DRY_RUN("Indicate which compare assertions would be processed, but do not actually send them to the server."),



  /**
   * Attempt to follow any referrals encountered during compare processing.
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_FOLLOW_REFERRALS("Attempt to follow any referrals encountered during compare processing."),



  /**
   * Include the UnboundID-proprietary get authorization entry request control in all bind requests to indicate that the bind result should include the specified attributes from the entries for the authentication and authorization identities.  This argument may be provided multiple times to specify multiple attributes to request.
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_GET_AUTHZ_ENTRY_ATTR("Include the UnboundID-proprietary get authorization entry request control in all bind requests to indicate that the bind result should include the specified attributes from the entries for the authentication and authorization identities.  This argument may be provided multiple times to specify multiple attributes to request."),



  /**
   * Include the manageDsaIT request control in all compare requests to indicate that any referral entries should be treated as regular entries rather than causing the server to generate a referral result.
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_MANAGE_DSA_IT("Include the manageDsaIT request control in all compare requests to indicate that any referral entries should be treated as regular entries rather than causing the server to generate a referral result."),



  /**
   * Include the UnboundID-proprietary operation purpose request control in all compare requests to provide information about the purpose for the operation.
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_OPERATION_PURPOSE("Include the UnboundID-proprietary operation purpose request control in all compare requests to provide information about the purpose for the operation."),



  /**
   * The path to a file to which standard output should be written.  Messages written to standard error will not be written to this file.
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_OUTPUT_FILE("The path to a file to which standard output should be written.  Messages written to standard error will not be written to this file."),



  /**
   * Specifies the format in which the output should be written.  Allowed values include ''tab-delimited'' ''csv'', or ''json''.  For each output format, the output will include the DN of the target entry, the name of the attribute type, the assertion value, the numeric value for the result code, and the name for the result code.  The JSON format may also include additional fields.
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_OUTPUT_FORMAT("Specifies the format in which the output should be written.  Allowed values include ''tab-delimited'' ''csv'', or ''json''.  For each output format, the output will include the DN of the target entry, the name of the attribute type, the assertion value, the numeric value for the result code, and the name for the result code.  The JSON format may also include additional fields."),



  /**
   * Include the proxied authorization control (also known as the proxied authorization v2 request control, as described in RFC 4370) in compare requests to indicate that the server should process the operation under the authority of the specified user.  The authorization identity should generally be specified in the form ''dn:'' followed by the DN of the target user, or ''u:'' followed by a username for the target user.
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_PROXY_AS("Include the proxied authorization control (also known as the proxied authorization v2 request control, as described in RFC 4370) in compare requests to indicate that the server should process the operation under the authority of the specified user.  The authorization identity should generally be specified in the form ''dn:'' followed by the DN of the target user, or ''u:'' followed by a username for the target user."),



  /**
   * Include the proxied authorization v1 request control (as described in draft-weltman-ldapv3-proxy-04) in compare requests to indicate that the server should process the operation under the authority of the user with the specified DN.
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_PROXY_V1_AS("Include the proxied authorization v1 request control (as described in draft-weltman-ldapv3-proxy-04) in compare requests to indicate that the server should process the operation under the authority of the user with the specified DN."),



  /**
   * Indicates that the output should be generated in a script-friendly format.  This argument is not used, as the output will always be in a script-friendly format, and the --outputFormat argument may be used to customize the format in which the output is written.
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_SCRIPT_FRIENDLY("Indicates that the output should be generated in a script-friendly format.  This argument is not used, as the output will always be in a script-friendly format, and the --outputFormat argument may be used to customize the format in which the output is written."),



  /**
   * Write standard output messages to both the specified output file and the default standard output stream.
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_TEE_OUTPUT("Write standard output messages to both the specified output file and the default standard output stream."),



  /**
   * Indicates that the tool should generate terse output.  If this is provided, then additional compare output will be suppressed, even for error results and results with response controls.
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_TERSE("Indicates that the tool should generate terse output.  If this is provided, then additional compare output will be suppressed, even for error results and results with response controls."),



  /**
   * Use an administrative session to process all operations using a dedicated pool of worker threads.  This may be useful when trying to diagnose problems in a server that is unresponsive because all normal worker threads are busy processing other requests.
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_USE_ADMIN_SESSION("Use an administrative session to process all operations using a dedicated pool of worker threads.  This may be useful when trying to diagnose problems in a server that is unresponsive because all normal worker threads are busy processing other requests."),



  /**
   * Include the password policy request control (as described in draft-behera-ldap-password-policy) in all bind requests.  If this control is included, the bind result may include additional information about the target user''s password policy state.
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_USE_PW_POLICY_CONTROL("Include the password policy request control (as described in draft-behera-ldap-password-policy) in all bind requests.  If this control is included, the bind result may include additional information about the target user''s password policy state."),



  /**
   * Indicates that the tool should generate verbose output.  If this is provided, then complete details about the result of each compare operation will be written to standard error.  If this is not provided, then that detail will only be written for operations that include an error result or that include one or more response controls.
   */
  INFO_LDAPCOMPARE_ARG_DESCRIPTION_VERBOSE("Indicates that the tool should generate verbose output.  If this is provided, then complete details about the result of each compare operation will be written to standard error.  If this is not provided, then that detail will only be written for operations that include an error result or that include one or more response controls."),



  /**
   * Use the integer value of the compare operation result code as the tool exit code.  This primarily applies to cases in which only a single compare assertion is processed and the response to that compare operation has a result code of either ''compare false'' (integer value 5) or ''compare true'' (integer value 6).  By default, the tool will use an exit code of zero in such cases to indicate that processing completed successfully, but the exit code may be useful in scripts to more easily obtain the result of the compare operation.  Note that if multiple compare operations are all processed successfully, then the tool will always return an exit code of zero, and if an error is encountered during processing, then the exit code will reflect the LDAP result code for that associated error.  This option only affects the tool exit code and does not alter the visible output in any way.
   */
  INFO_LDAPCOMPARE_ARG_DESC_USE_COMPARE_RESULT_CODE_AS_EXIT_CODE("Use the integer value of the compare operation result code as the tool exit code.  This primarily applies to cases in which only a single compare assertion is processed and the response to that compare operation has a result code of either ''compare false'' (integer value 5) or ''compare true'' (integer value 6).  By default, the tool will use an exit code of zero in such cases to indicate that processing completed successfully, but the exit code may be useful in scripts to more easily obtain the result of the compare operation.  Note that if multiple compare operations are all processed successfully, then the tool will always return an exit code of zero, and if an error is encountered during processing, then the exit code will reflect the LDAP result code for that associated error.  This option only affects the tool exit code and does not alter the visible output in any way."),



  /**
   * Bind Control Arguments
   */
  INFO_LDAPCOMPARE_ARG_GROUP_BIND_CONTROLS("Bind Control Arguments"),



  /**
   * Compare Control Arguments
   */
  INFO_LDAPCOMPARE_ARG_GROUP_COMPARE_CONTROLS("Compare Control Arguments"),



  /**
   * Output Arguments
   */
  INFO_LDAPCOMPARE_ARG_GROUP_OUTPUT("Output Arguments"),



  /**
   * Processing Arguments
   */
  INFO_LDAPCOMPARE_ARG_GROUP_PROCESSING("Processing Arguments"),



  /**
   * '{'attribute'}'
   */
  INFO_LDAPCOMPARE_ARG_PLACEHOLDER_ATTRIBUTE("'{'attribute'}'"),



  /**
   * '{'authzID'}'
   */
  INFO_LDAPCOMPARE_ARG_PLACEHOLDER_AUTHZ_ID("'{'authzID'}'"),



  /**
   * '{'tab-delimited|csv|json'}'
   */
  INFO_LDAPCOMPARE_ARG_PLACEHOLDER_FORMAT("'{'tab-delimited|csv|json'}'"),



  /**
   * Include the UnboundID-proprietary get user resource limits request control in all bind requests to indicate that a successful bind result should include information about resource limits (e.g., size limit, time limit, idle time limit, etc.) that the server may impose for the user.
   */
  INFO_LDAPCOMPARE_ARG_PLACEHOLDER_GET_USER_RESOURCE_LIMITS("Include the UnboundID-proprietary get user resource limits request control in all bind requests to indicate that a successful bind result should include information about resource limits (e.g., size limit, time limit, idle time limit, etc.) that the server may impose for the user."),



  /**
   * '{'purpose'}'
   */
  INFO_LDAPCOMPARE_ARG_PLACEHOLDER_PURPOSE("'{'purpose'}'"),



  /**
   * Use an LDAP compare operation to determine whether entry ''uid=jdoe,ou=People,dc=example,dc=com'' has a value of ''Austin'' for the ''l'' attribute.  The result of the compare operation will be displayed in the default tab-delimited text format.
   */
  INFO_LDAPCOMPARE_EXAMPLE_1("Use an LDAP compare operation to determine whether entry ''uid=jdoe,ou=People,dc=example,dc=com'' has a value of ''Austin'' for the ''l'' attribute.  The result of the compare operation will be displayed in the default tab-delimited text format."),



  /**
   * Use a series of LDAP compare operations to determine which of the entries with DNs are read from the file ''entry-dns.txt'' have a ''title'' value of ''manager''.  The result of each compare operation will be displayed in the comma-separated values (CSV) format, with all unnecessary output suppressed.
   */
  INFO_LDAPCOMPARE_EXAMPLE_2("Use a series of LDAP compare operations to determine which of the entries with DNs are read from the file ''entry-dns.txt'' have a ''title'' value of ''manager''.  The result of each compare operation will be displayed in the comma-separated values (CSV) format, with all unnecessary output suppressed."),



  /**
   * Use a series of LDAP compare operations using both entry DNs and attribute-value assertions read from the file ''compare-assertions.txt''.  The results of the compare operations will be written in JSON format to the file ''compare-assertion-results.json'', and verbose output about the processing will be written to standard error.
   */
  INFO_LDAPCOMPARE_EXAMPLE_3("Use a series of LDAP compare operations using both entry DNs and attribute-value assertions read from the file ''compare-assertions.txt''.  The results of the compare operations will be written in JSON format to the file ''compare-assertion-results.json'', and verbose output about the processing will be written to standard error."),



  /**
   * Attribute Name
   */
  INFO_LDAPCOMPARE_FORMAT_HEADER_ATTR("Attribute Name"),



  /**
   * Entry DN
   */
  INFO_LDAPCOMPARE_FORMAT_HEADER_DN("Entry DN"),



  /**
   * Result Code Value
   */
  INFO_LDAPCOMPARE_FORMAT_HEADER_RC_INT("Result Code Value"),



  /**
   * Result Code Name
   */
  INFO_LDAPCOMPARE_FORMAT_HEADER_RC_NAME("Result Code Name"),



  /**
   * Assertion Value
   */
  INFO_LDAPCOMPARE_FORMAT_HEADER_VALUE("Assertion Value"),



  /**
   * All compare operations completed without error.  True results:  {0,number,0}.  False results:  {1,number,0}.
   */
  INFO_LDAPCOMPARE_RESULT_ALL_SUCCEEDED("All compare operations completed without error.  True results:  {0,number,0}.  False results:  {1,number,0}."),



  /**
   * The attribute-value assertion did not match the target entry.
   */
  INFO_LDAPCOMPARE_RESULT_COMPARE_DID_NOT_MATCH("The attribute-value assertion did not match the target entry."),



  /**
   * The attribute-value assertion matched the target entry.
   */
  INFO_LDAPCOMPARE_RESULT_COMPARE_MATCHED("The attribute-value assertion matched the target entry."),



  /**
   * Compare Result:
   */
  INFO_LDAPCOMPARE_RESULT_HEADER("Compare Result:"),



  /**
   * Attribute Name: {0}
   */
  INFO_LDAPCOMPARE_RESULT_HEADER_ATTR("Attribute Name: {0}"),



  /**
   * Entry DN: {0}
   */
  INFO_LDAPCOMPARE_RESULT_HEADER_DN("Entry DN: {0}"),



  /**
   * Assertion Value: {0}
   */
  INFO_LDAPCOMPARE_RESULT_HEADER_VALUE("Assertion Value: {0}"),



  /**
   * Perform compare operations in an LDAP directory server.  Compare operations may be used to efficiently determine whether a specified entry has a given value.
   */
  INFO_LDAPCOMPARE_TOOL_DESCRIPTION_1("Perform compare operations in an LDAP directory server.  Compare operations may be used to efficiently determine whether a specified entry has a given value."),



  /**
   * The exit code for this tool will indicate whether processing was successful or unsuccessful, and to provide a basic indication of the reason for unsuccessful attempts.  By default, it will use an exit code of zero (which corresponds to the LDAP ''success'' result) if all compare operations completed with a result code of either ''compare false'' or ''compare true'' (integer values 5 and 6, respectively), but if the --useCompareResultCodeAsExitCode argument is provided, only one compare assertion is performed, and it yields an exit code of ''compare false'' or ''compare true'', then the numeric value for that result code will be used as the exit code.  If any error occurs during processing, then the exit code will be a nonzero value that reflects the first error result that was encountered.
   */
  INFO_LDAPCOMPARE_TOOL_DESCRIPTION_2("The exit code for this tool will indicate whether processing was successful or unsuccessful, and to provide a basic indication of the reason for unsuccessful attempts.  By default, it will use an exit code of zero (which corresponds to the LDAP ''success'' result) if all compare operations completed with a result code of either ''compare false'' or ''compare true'' (integer values 5 and 6, respectively), but if the --useCompareResultCodeAsExitCode argument is provided, only one compare assertion is performed, and it yields an exit code of ''compare false'' or ''compare true'', then the numeric value for that result code will be used as the exit code.  If any error occurs during processing, then the exit code will be a nonzero value that reflects the first error result that was encountered."),



  /**
   * The attribute type and assertion value to use for the compare operations will typically be provided as the first unnamed trailing argument provided on the command line.  It should be formatted with the name or OID of the target attribute type followed by a single colon and the string representation of the assertion value.  Alternatively, the attribute name or OID may be followed by two colons and the base64-encoded representation of the assertion value, or it may be followed by a colon and a less-than symbol to indicate that the assertion value should be read from a file (in which case the exact bytes of the file, including line breaks, will be used as the assertion value).
   */
  INFO_LDAPCOMPARE_TOOL_DESCRIPTION_3("The attribute type and assertion value to use for the compare operations will typically be provided as the first unnamed trailing argument provided on the command line.  It should be formatted with the name or OID of the target attribute type followed by a single colon and the string representation of the assertion value.  Alternatively, the attribute name or OID may be followed by two colons and the base64-encoded representation of the assertion value, or it may be followed by a colon and a less-than symbol to indicate that the assertion value should be read from a file (in which case the exact bytes of the file, including line breaks, will be used as the assertion value)."),



  /**
   * The DNs of the entries to compare may either be provided on the command line as additional unnamed trailing arguments after the provided attribute-value assertion, or they may be read from a file whose path is provided using the --dnFile argument.
   */
  INFO_LDAPCOMPARE_TOOL_DESCRIPTION_4("The DNs of the entries to compare may either be provided on the command line as additional unnamed trailing arguments after the provided attribute-value assertion, or they may be read from a file whose path is provided using the --dnFile argument."),



  /**
   * If the attribute-value assertion is provided on the command line as an unnamed trailing argument, then the same assertion will be performed for all operations.  If multiple types of assertions should be performed, then you may use the --assertionFile argument to specify the path to a file containing both attribute-value assertions and entry DNs.
   */
  INFO_LDAPCOMPARE_TOOL_DESCRIPTION_5("If the attribute-value assertion is provided on the command line as an unnamed trailing argument, then the same assertion will be performed for all operations.  If multiple types of assertions should be performed, then you may use the --assertionFile argument to specify the path to a file containing both attribute-value assertions and entry DNs."),



  /**
   * [attribute:value|attribute::base64Value|attribute:<valueFilePath [dn1 [dn2 [ dn3 [ .. ]]]]]
   */
  INFO_LDAPCOMPARE_TRAILING_ARGS_PLACEHOLDER("[attribute:value|attribute::base64Value|attribute:<valueFilePath [dn1 [dn2 [ dn3 [ .. ]]]]]"),



  /**
   * Indicates that delete requests should include the assertion request control to indicate that the server should reject any attempt to delete an entry that does not match the provided filter.
   */
  INFO_LDAPDELETE_ARG_DESC_ASSERTION_FILTER("Indicates that delete requests should include the assertion request control to indicate that the server should reject any attempt to delete an entry that does not match the provided filter."),



  /**
   * The replication assurance level that should be used for servers in the same location as the server that originally processed the change.  The value must be one of ''none'', ''received-any-server'', or ''processed-all-servers''.  If this is not provided, the server will determine an appropriate local assurance level.
   */
  INFO_LDAPDELETE_ARG_DESC_ASSURED_REPLICATION_LOCAL_LEVEL("The replication assurance level that should be used for servers in the same location as the server that originally processed the change.  The value must be one of ''none'', ''received-any-server'', or ''processed-all-servers''.  If this is not provided, the server will determine an appropriate local assurance level."),



  /**
   * The replication assurance level that should be used for servers in a different location from the server that originally processed the change.  The value must be one of ''none'', ''received-any-remote-location'', ''received-all-remote-locations'', or ''processed-all-remote-servers''.  If this is not provided, the server will determine an appropriate remote assurance level.
   */
  INFO_LDAPDELETE_ARG_DESC_ASSURED_REPLICATION_REMOTE_LEVEL("The replication assurance level that should be used for servers in a different location from the server that originally processed the change.  The value must be one of ''none'', ''received-any-remote-location'', ''received-all-remote-locations'', or ''processed-all-remote-servers''.  If this is not provided, the server will determine an appropriate remote assurance level."),



  /**
   * The maximum length of time that the server should delay the response to the delete operation while waiting for the desired replication assurance to be achieved.  If this is not provided, the server will determine an appropriate timeout to use.
   */
  INFO_LDAPDELETE_ARG_DESC_ASSURED_REPLICATION_TIMEOUT("The maximum length of time that the server should delay the response to the delete operation while waiting for the desired replication assurance to be achieved.  If this is not provided, the server will determine an appropriate timeout to use."),



  /**
   * Indicates that bind requests should include the authorization identity request control to retrieve the authorization identity for the authenticated connection.
   */
  INFO_LDAPDELETE_ARG_DESC_AUTHZ_ID("Indicates that bind requests should include the authorization identity request control to retrieve the authorization identity for the authenticated connection."),



  /**
   * Provides a control to include in all bind requests.
   */
  INFO_LDAPDELETE_ARG_DESC_BIND_CONTROL("Provides a control to include in all bind requests."),



  /**
   * The character set/data encoding to use when reading data from files or standard input.  If this is not specified, the UTF-8 character set will be used by default.
   */
  INFO_LDAPDELETE_ARG_DESC_CHARSET("The character set/data encoding to use when reading data from files or standard input.  If this is not specified, the UTF-8 character set will be used by default."),



  /**
   * Indicates that all delete requests should be processed as client-side subtree deletes by searching for all entries below the target entry and then deleting them.
   */
  INFO_LDAPDELETE_ARG_DESC_CLIENT_SIDE_SUB_DEL("Indicates that all delete requests should be processed as client-side subtree deletes by searching for all entries below the target entry and then deleting them."),



  /**
   * Indicates that the tool should continue processing even after encountering an error.  This is only applicable if it is run with arguments that would cause it to attempt to delete multiple entries.
   */
  INFO_LDAPDELETE_ARG_DESC_CONTINUE_ON_ERROR("Indicates that the tool should continue processing even after encountering an error.  This is only applicable if it is run with arguments that would cause it to attempt to delete multiple entries."),



  /**
   * Provides a control to include in all delete requests.
   */
  INFO_LDAPDELETE_ARG_DESC_DELETE_CONTROL("Provides a control to include in all delete requests."),



  /**
   * An LDAP search filter that can be used to identify the entries to delete.  The base DN for the searches should be specified using the --searchBaseDN argument, with the null DN being used by default if that argument is not given.  This argument may be provided multiple times to specify multiple filters, and searches will be performed in the order in which the filters are provided.  This argument must not be used in conjunction with any other argument used to indicate which entries should be deleted.
   */
  INFO_LDAPDELETE_ARG_DESC_DELETE_ENTRIES_MATCHING_FILTER("An LDAP search filter that can be used to identify the entries to delete.  The base DN for the searches should be specified using the --searchBaseDN argument, with the null DN being used by default if that argument is not given.  This argument may be provided multiple times to specify multiple filters, and searches will be performed in the order in which the filters are provided.  This argument must not be used in conjunction with any other argument used to indicate which entries should be deleted."),



  /**
   * The path to a file containing LDAP search filters (one filter per line, with blank lines and lines starting with the ''#'' character being ignored) that can be used to identify the entries to delete.  The base DN for the searches should be specified using the --searchBaseDN argument, with the null DN being used by default if that argument is not given.  This argument may be provided multiple times to specify the paths to multiple filter files, and the files will be processed in the order they are provided on the command line.  This argument must not be used in conjunction with any other argument used to indicate which entries should be deleted.
   */
  INFO_LDAPDELETE_ARG_DESC_DELETE_ENTRIES_MATCHING_FILTER_FILE("The path to a file containing LDAP search filters (one filter per line, with blank lines and lines starting with the ''#'' character being ignored) that can be used to identify the entries to delete.  The base DN for the searches should be specified using the --searchBaseDN argument, with the null DN being used by default if that argument is not given.  This argument may be provided multiple times to specify the paths to multiple filter files, and the files will be processed in the order they are provided on the command line.  This argument must not be used in conjunction with any other argument used to indicate which entries should be deleted."),



  /**
   * The DN of an entry to delete.  This argument may be provided multiple times to specify the DNs of multiple entries to delete, and entries will be deleted in the order in which the arguments were given.  This argument must not be used in conjunction with any other argument used to indicate which entries should be deleted.
   */
  INFO_LDAPDELETE_ARG_DESC_DN("The DN of an entry to delete.  This argument may be provided multiple times to specify the DNs of multiple entries to delete, and entries will be deleted in the order in which the arguments were given.  This argument must not be used in conjunction with any other argument used to indicate which entries should be deleted."),



  /**
   * The path to a file containing the DNs of the entries to delete.  Each DN must be on its own line in the file, with blank lines and lines starting with the ''#'' character being ignored.  Each DN line may optionally start with ''dn:'' (or ''dn::'' to indicate that the DN is base64-encoded), and long DNs may be wrapped across multiple lines by starting subsequent lines with at least one space.  This argument may be provided multiple times to specify multiple DN files, and the files will be processed in the order they were provided.  This argument must not be used in conjunction with any other argument used to indicate which entries should be deleted.
   */
  INFO_LDAPDELETE_ARG_DESC_DN_FILE("The path to a file containing the DNs of the entries to delete.  Each DN must be on its own line in the file, with blank lines and lines starting with the ''#'' character being ignored.  Each DN line may optionally start with ''dn:'' (or ''dn::'' to indicate that the DN is base64-encoded), and long DNs may be wrapped across multiple lines by starting subsequent lines with at least one space.  This argument may be provided multiple times to specify multiple DN files, and the files will be processed in the order they were provided.  This argument must not be used in conjunction with any other argument used to indicate which entries should be deleted."),



  /**
   * Indicates that the tool should display what it would do, and may perform searches if appropriate, but will not actually attempt to delete any entries.  Note that if the server supports the no-operation request control, you may wish to use the --noOperation argument instead, which will actually send the delete requests with a control indicating that the server should perform as much validation of the request that it can, but should not actually delete the target entry.
   */
  INFO_LDAPDELETE_ARG_DESC_DRY_RUN("Indicates that the tool should display what it would do, and may perform searches if appropriate, but will not actually attempt to delete any entries.  Note that if the server supports the no-operation request control, you may wish to use the --noOperation argument instead, which will actually send the delete requests with a control indicating that the server should perform as much validation of the request that it can, but should not actually delete the target entry."),



  /**
   * The path to a file containing the passphrase used to encrypt an input file.  If this is not provided and an input file is encrypted (and the encryption key cannot be automatically obtained, for example, from a Ping Identity Directory Server''s encryption settings database), then the user will be interactively prompted for the passphrase.
   */
  INFO_LDAPDELETE_ARG_DESC_ENCRYPTION_PW_FILE("The path to a file containing the passphrase used to encrypt an input file.  If this is not provided and an input file is encrypted (and the encryption key cannot be automatically obtained, for example, from a Ping Identity Directory Server''s encryption settings database), then the user will be interactively prompted for the passphrase."),



  /**
   * Indicates that the tool should attempt to follow any referrals that it encounters.  By default, any referrals that are returned will be treated as failures.
   */
  INFO_LDAPDELETE_ARG_DESC_FOLLOW_REFERRALS("Indicates that the tool should attempt to follow any referrals that it encounters.  By default, any referrals that are returned will be treated as failures."),



  /**
   * Indicates that bind requests should include the Ping Identity-proprietary get authorization entry request control to retrieve the specified attribute from the authenticated user''s entry.  This argument may be provided multiple times to request that multiple attributes be returned.
   */
  INFO_LDAPDELETE_ARG_DESC_GET_AUTHZ_ENTRY_ATTR("Indicates that bind requests should include the Ping Identity-proprietary get authorization entry request control to retrieve the specified attribute from the authenticated user''s entry.  This argument may be provided multiple times to request that multiple attributes be returned."),



  /**
   * Indicates that delete requests sent through a Directory Proxy Server should include the Ping Identity-proprietary get backend set ID request control to indicate that the response should include a control with the ID of the entry-balancing backend set in which the delete was processed.
   */
  INFO_LDAPDELETE_ARG_DESC_GET_BACKEND_SET_ID("Indicates that delete requests sent through a Directory Proxy Server should include the Ping Identity-proprietary get backend set ID request control to indicate that the response should include a control with the ID of the entry-balancing backend set in which the delete was processed."),



  /**
   * Indicates that delete requests should include the Ping Identity-proprietary get server ID request control to indicate that the response should include a control with the server ID of the Directory Server instance in which the delete was processed.
   */
  INFO_LDAPDELETE_ARG_DESC_GET_SERVER_ID("Indicates that delete requests should include the Ping Identity-proprietary get server ID request control to indicate that the response should include a control with the server ID of the Directory Server instance in which the delete was processed."),



  /**
   * Indicates that bind requests should include the Ping Identity-proprietary get user resource limits request control to retrieve information about the resource limits that the server will impose for the authenticated connection.
   */
  INFO_LDAPDELETE_ARG_DESC_GET_USER_RESOURCE_LIMITS("Indicates that bind requests should include the Ping Identity-proprietary get user resource limits request control to retrieve information about the resource limits that the server will impose for the authenticated connection."),



  /**
   * Indicates that delete requests should include the Ping Identity-proprietary hard delete request control to indicate that the target entries should be completely removed, even if they would have otherwise been processed as soft deletes.
   */
  INFO_LDAPDELETE_ARG_DESC_HARD_DELETE("Indicates that delete requests should include the Ping Identity-proprietary hard delete request control to indicate that the target entries should be completely removed, even if they would have otherwise been processed as soft deletes."),



  /**
   * The LDAP protocol version that should be used.
   */
  INFO_LDAPDELETE_ARG_DESC_LDAP_VERSION("The LDAP protocol version that should be used."),



  /**
   * Indicates that search and delete requests should include the Manage DSA IT request control to indicate that the server should treat referral entries as regular entries.
   */
  INFO_LDAPDELETE_ARG_DESC_MANAGE_DSA_IT("Indicates that search and delete requests should include the Manage DSA IT request control to indicate that the server should treat referral entries as regular entries."),



  /**
   * Indicates that the tool should not attempt to retry operations that fail in a way that the connection to the directory server may be invalid.  By default, it will automatically try to establish a new connection and retry the failed operation.
   */
  INFO_LDAPDELETE_ARG_DESC_NEVER_RETRY("Indicates that the tool should not attempt to retry operations that fail in a way that the connection to the directory server may be invalid.  By default, it will automatically try to establish a new connection and retry the failed operation."),



  /**
   * Indicates that delete requests should include the no-operation request control to indicate that the server should perform as much processing as possible for the delete operation without actually removing the entry.
   */
  INFO_LDAPDELETE_ARG_DESC_NO_OP("Indicates that delete requests should include the no-operation request control to indicate that the server should perform as much processing as possible for the delete operation without actually removing the entry."),



  /**
   * Indicates that requests should include the Ping Identity-proprietary operation purpose request control to indicate the intended purpose for the operations.
   */
  INFO_LDAPDELETE_ARG_DESC_OP_PURPOSE("Indicates that requests should include the Ping Identity-proprietary operation purpose request control to indicate the intended purpose for the operations."),



  /**
   * Indicates that delete requests should include the pre-read request control to indicate that delete responses should include a post-read response control with the values of the specified at the time the entry was deleted.  This may be provided multiple times to request multiple pre-read attributes.
   */
  INFO_LDAPDELETE_ARG_DESC_PRE_READ_ATTR("Indicates that delete requests should include the pre-read request control to indicate that delete responses should include a post-read response control with the values of the specified at the time the entry was deleted.  This may be provided multiple times to request multiple pre-read attributes."),



  /**
   * Indicates that search and delete requests should include the proxied authorization v2 request control, to request that they be processed under the authority of the specified user.
   */
  INFO_LDAPDELETE_ARG_DESC_PROXY_AS("Indicates that search and delete requests should include the proxied authorization v2 request control, to request that they be processed under the authority of the specified user."),



  /**
   * Indicates that search and delete requests should include the proxied authorization v1 request control, to request that they be processed under the authority of the specified user.
   */
  INFO_LDAPDELETE_ARG_DESC_PROXY_V1_AS("Indicates that search and delete requests should include the proxied authorization v1 request control, to request that they be processed under the authority of the specified user."),



  /**
   * The maximum number of delete operations that should be attempted per second.  If this is not provided, then no rate limit will be imposed on delete requests.
   */
  INFO_LDAPDELETE_ARG_DESC_RATE_PER_SECOND("The maximum number of delete operations that should be attempted per second.  If this is not provided, then no rate limit will be imposed on delete requests."),



  /**
   * The path to a file that will be updated with the DNs of any entries that could not be deleted, along with information about the failed delete attempt.  If this is not provided, then failure information will only be written to standard error.
   */
  INFO_LDAPDELETE_ARG_DESC_REJECT_FILE("The path to a file that will be updated with the DNs of any entries that could not be deleted, along with information about the failed delete attempt.  If this is not provided, then failure information will only be written to standard error."),



  /**
   * Indicates that delete requests should include the Ping Identity-proprietary replication repair request control to indicate that the delete operation should not be replicated.
   */
  INFO_LDAPDELETE_ARG_DESC_REPLICATION_REPAIR("Indicates that delete requests should include the Ping Identity-proprietary replication repair request control to indicate that the delete operation should not be replicated."),



  /**
   * Indicates that if an operation fails in a way that indicates that the connection to the directory server may be invalid, the tool should automatically retry the failed operation on a newly created connection.
   */
  INFO_LDAPDELETE_ARG_DESC_RETRY_FAILED_OPS("Indicates that if an operation fails in a way that indicates that the connection to the directory server may be invalid, the tool should automatically retry the failed operation on a newly created connection."),



  /**
   * Indicates that search and delete requests should include the Ping Identity-proprietary route to backend set request control to indicate that the Directory Proxy Server should forward those requests to servers in the specified entry-balancing backend set.  This control may be provided multiple times to specify multiple backend sets for the same or different entry-balancing request processors.
   */
  INFO_LDAPDELETE_ARG_DESC_ROUTE_TO_BACKEND_SET("Indicates that search and delete requests should include the Ping Identity-proprietary route to backend set request control to indicate that the Directory Proxy Server should forward those requests to servers in the specified entry-balancing backend set.  This control may be provided multiple times to specify multiple backend sets for the same or different entry-balancing request processors."),



  /**
   * Indicates that search and delete requests should include the Ping Identity-proprietary route to server request control to indicate that the Directory Proxy Server should forward those requests to the specified backend server.
   */
  INFO_LDAPDELETE_ARG_DESC_ROUTE_TO_SERVER("Indicates that search and delete requests should include the Ping Identity-proprietary route to server request control to indicate that the Directory Proxy Server should forward those requests to the specified backend server."),



  /**
   * Generate script-friendly output.
   */
  INFO_LDAPDELETE_ARG_DESC_SCRIPT_FRIENDLY("Generate script-friendly output."),



  /**
   * The base DN to use when searching for entries to delete.  This argument may only be used in conjunction with the --deleteEntriesMatchingFilter or --deleteEntriesMatchingFiltersFromFile arguments.  It may be provided multiple times to specify multiple search base DNs.  If this argument is not given, the null DN will be used as the base DN for the searches.
   */
  INFO_LDAPDELETE_ARG_DESC_SEARCH_BASE_DN("The base DN to use when searching for entries to delete.  This argument may only be used in conjunction with the --deleteEntriesMatchingFilter or --deleteEntriesMatchingFiltersFromFile arguments.  It may be provided multiple times to specify multiple search base DNs.  If this argument is not given, the null DN will be used as the base DN for the searches."),



  /**
   * The page size to use in conjunction with the simple paged results control when retrieving entries.  This argument may be used in conjunction with either the --deleteEntriesMatchingFilter or the --deleteEntriesMatchingFilterFromFile argument to indicate that the search should use the simple paged results control to retrieve the entries in pages rather than all at once.  It may also be used in conjunction with the --clientSideSubtreeDelete argument to indicate the page size for the simple paged results control that it uses.
   */
  INFO_LDAPDELETE_ARG_DESC_SEARCH_PAGE_SIZE("The page size to use in conjunction with the simple paged results control when retrieving entries.  This argument may be used in conjunction with either the --deleteEntriesMatchingFilter or the --deleteEntriesMatchingFilterFromFile argument to indicate that the search should use the simple paged results control to retrieve the entries in pages rather than all at once.  It may also be used in conjunction with the --clientSideSubtreeDelete argument to indicate the page size for the simple paged results control that it uses."),



  /**
   * Indicates that all delete requests should be processed as server-side subtree deletes by using the subtree delete request control.
   */
  INFO_LDAPDELETE_ARG_DESC_SERVER_SIDE_SUB_DEL("Indicates that all delete requests should be processed as server-side subtree deletes by using the subtree delete request control."),



  /**
   * Indicates that delete requests should include the Ping Identity-proprietary soft delete request control to indicate that the server should hide the entries rather than deleting them immediately.  Soft-deleted may or may not be completely removed after a period of time, based on the server configuration.
   */
  INFO_LDAPDELETE_ARG_DESC_SOFT_DELETE("Indicates that delete requests should include the Ping Identity-proprietary soft delete request control to indicate that the server should hide the entries rather than deleting them immediately.  Soft-deleted may or may not be completely removed after a period of time, based on the server configuration."),



  /**
   * Indicates that delete requests should include the Ping Identity-proprietary suppress referential integrity updates request control so that the server will not perform any referential integrity processing for the delete operation.
   */
  INFO_LDAPDELETE_ARG_DESC_SUPPRESS_REFINT_UPDATES("Indicates that delete requests should include the Ping Identity-proprietary suppress referential integrity updates request control so that the server will not perform any referential integrity processing for the delete operation."),



  /**
   * Indicates that the tool should attempt to use the Ping Identity-proprietary start administrative session extended operation to create an administrative session that will cause all requests to be processed in a dedicated pool of worker threads.  This may be useful when trying to diagnose or resolve an issue when all regular worker threads are busy processing other requests
   */
  INFO_LDAPDELETE_ARG_DESC_USE_ADMIN_SESSION("Indicates that the tool should attempt to use the Ping Identity-proprietary start administrative session extended operation to create an administrative session that will cause all requests to be processed in a dedicated pool of worker threads.  This may be useful when trying to diagnose or resolve an issue when all regular worker threads are busy processing other requests"),



  /**
   * Indicates that delete requests should include the Ping Identity-proprietary assured replication request control to delay the response from the server until the change has been replicated to other servers.  The --assuredReplicationLocalLevel, --assuredReplicationRemoteLevel, and --assuredReplicationTimeout arguments may also be used to customize the content of the request control.
   */
  INFO_LDAPDELETE_ARG_DESC_USE_ASSURED_REPLICATION("Indicates that delete requests should include the Ping Identity-proprietary assured replication request control to delay the response from the server until the change has been replicated to other servers.  The --assuredReplicationLocalLevel, --assuredReplicationRemoteLevel, and --assuredReplicationTimeout arguments may also be used to customize the content of the request control."),



  /**
   * Generate verbose output.
   */
  INFO_LDAPDELETE_ARG_DESC_VERBOSE("Generate verbose output."),



  /**
   * Control Arguments
   */
  INFO_LDAPDELETE_ARG_GROUP_CONTROLS("Control Arguments"),



  /**
   * Data Arguments
   */
  INFO_LDAPDELETE_ARG_GROUP_DATA("Data Arguments"),



  /**
   * Delete Operation Arguments
   */
  INFO_LDAPDELETE_ARG_GROUP_OPERATION("Delete Operation Arguments"),



  /**
   * '{'none|received-any-server|processed-all-servers'}'
   */
  INFO_LDAPDELETE_ARG_PLACEHOLDER_ASSURED_REPLICATION_LOCAL_LEVEL("'{'none|received-any-server|processed-all-servers'}'"),



  /**
   * '{'none|received-any-remote-location|received-all-remote-locations|processed-all-remote-servers'}'
   */
  INFO_LDAPDELETE_ARG_PLACEHOLDER_ASSURED_REPLICATION_REMOTE_LEVEL("'{'none|received-any-remote-location|received-all-remote-locations|processed-all-remote-servers'}'"),



  /**
   * '{'attr'}'
   */
  INFO_LDAPDELETE_ARG_PLACEHOLDER_ATTR("'{'attr'}'"),



  /**
   * '{'authzID'}'
   */
  INFO_LDAPDELETE_ARG_PLACEHOLDER_AUTHZ_ID("'{'authzID'}'"),



  /**
   * '{'charset'}'
   */
  INFO_LDAPDELETE_ARG_PLACEHOLDER_CHARSET("'{'charset'}'"),



  /**
   * '{'id'}'
   */
  INFO_LDAPDELETE_ARG_PLACEHOLDER_ID("'{'id'}'"),



  /**
   * '{'deletesPerSecond'}'
   */
  INFO_LDAPDELETE_ARG_PLACEHOLDER_RATE_PER_SECOND("'{'deletesPerSecond'}'"),



  /**
   * '{'entry-balancing-processor-id:backend-set-id'}'
   */
  INFO_LDAPDELETE_ARG_PLACEHOLDER_ROUTE_TO_BACKEND_SET("'{'entry-balancing-processor-id:backend-set-id'}'"),



  /**
   * Attempting a client-side subtree delete with base entry ''{0}''...
   */
  INFO_LDAPDELETE_CLIENT_SIDE_SUBTREE_DELETING("Attempting a client-side subtree delete with base entry ''{0}''..."),



  /**
   * Successfully deleted entry ''{0}'', which did not have any subordinate entries.
   */
  INFO_LDAPDELETE_CLIENT_SIDE_SUBTREE_DEL_ONLY_BASE("Successfully deleted entry ''{0}'', which did not have any subordinate entries."),



  /**
   * Successfully deleted ''{0}'' and {1,number,0} subordinate entries.
   */
  INFO_LDAPDELETE_CLIENT_SIDE_SUBTREE_DEL_WITH_SUBS("Successfully deleted ''{0}'' and {1,number,0} subordinate entries."),



  /**
   * Deleting entry ''{0}''...
   */
  INFO_LDAPDELETE_DELETING_ENTRY("Deleting entry ''{0}''..."),



  /**
   * The data to be read is encrypted.  Please provide the passphrase needed to decrypt the data:
   */
  INFO_LDAPDELETE_ENCRYPTION_PASSPHRASE_PROMPT("The data to be read is encrypted.  Please provide the passphrase needed to decrypt the data:"),



  /**
   * Deletes the entry with DN ''uid=test.user,ou=People,dc=example,dc=com''
   */
  INFO_LDAPDELETE_EXAMPLE_1("Deletes the entry with DN ''uid=test.user,ou=People,dc=example,dc=com''"),



  /**
   * Deletes the entries whose DNs are contained in the file ''dns-to-delete.txt''.
   */
  INFO_LDAPDELETE_EXAMPLE_2("Deletes the entries whose DNs are contained in the file ''dns-to-delete.txt''."),



  /**
   * Deletes all entries matching filter ''(description=delete)'' below base entry ''ou=People,dc=example,dc=com''.
   */
  INFO_LDAPDELETE_EXAMPLE_3("Deletes all entries matching filter ''(description=delete)'' below base entry ''ou=People,dc=example,dc=com''."),



  /**
   * Deletes the entries whose DNs are read from standard input (one DN per line).
   */
  INFO_LDAPDELETE_EXAMPLE_4("Deletes the entries whose DNs are read from standard input (one DN per line)."),



  /**
   * Issuing search request {0}...
   */
  INFO_LDAPDELETE_ISSUING_SEARCH_REQUEST("Issuing search request {0}..."),



  /**
   * Not actually attempting the delete because the {0} argument was provided.
   */
  INFO_LDAPDELETE_NOT_DELETING_BECAUSE_OF_DRY_RUN("Not actually attempting the delete because the {0} argument was provided."),



  /**
   * Opening DN file ''{0}'' for reading
   */
  INFO_LDAPDELETE_READING_DNS_FROM_FILE("Opening DN file ''{0}'' for reading"),



  /**
   * Opening filter file ''{0}'' for reading
   */
  INFO_LDAPDELETE_READING_FILTERS_FROM_FILE("Opening filter file ''{0}'' for reading"),



  /**
   * Enter the DNs of the entries to delete, with one DN per line:
   */
  INFO_LDAPDELETE_READING_FROM_STDIN("Enter the DNs of the entries to delete, with one DN per line:"),



  /**
   * Received search result {0}
   */
  INFO_LDAPDELETE_RECEIVED_SEARCH_RESULT("Received search result {0}"),



  /**
   * Sending delete request {0}
   */
  INFO_LDAPDELETE_SENDING_DELETE_REQUEST("Sending delete request {0}"),



  /**
   * Delete one or more entries from an LDAP directory server.  You can provide the DNs of the entries to delete using named arguments, as trailing arguments, from a file, or from standard input.  Alternatively, you can identify entries to delete using a search base DN and filter.
   */
  INFO_LDAPDELETE_TOOL_DESCRIPTION("Delete one or more entries from an LDAP directory server.  You can provide the DNs of the entries to delete using named arguments, as trailing arguments, from a file, or from standard input.  Alternatively, you can identify entries to delete using a search base DN and filter."),



  /**
   * ['{'dn1'}' ['{'dn2'}' ['{'dn3'}' ...] ] ]
   */
  INFO_LDAPDELETE_TRAILING_ARGS_PLACEHOLDER("['{'dn1'}' ['{'dn2'}' ['{'dn3'}' ...] ] ]"),



  /**
   * Adding entry {0} ...
   */
  INFO_LDAPMODIFY_ADDING_ENTRY("Adding entry {0} ..."),



  /**
   * Add request for entry {0} added to the set of operations to be included in the multi-update request.
   */
  INFO_LDAPMODIFY_ADD_ADDED_TO_MULTI_UPDATE("Add request for entry {0} added to the set of operations to be included in the multi-update request."),



  /**
   * Specifies a control that should be included in all add requests sent to the server.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_ADD_CONTROL("Specifies a control that should be included in all add requests sent to the server."),



  /**
   * Treat any add operation that includes the ''{0}'' attribute as an undelete operation.  Undelete requests may be used to restore a soft-deleted entry, optionally using a different DN than was originally assigned to the entry.  The server must be configured to allow soft delete operations, and the requester must have the soft-delete-read privilege.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_ALLOW_UNDELETE("Treat any add operation that includes the ''{0}'' attribute as an undelete operation.  Undelete requests may be used to restore a soft-deleted entry, optionally using a different DN than was originally assigned to the entry.  The server must be configured to allow soft delete operations, and the requester must have the soft-delete-read privilege."),



  /**
   * A filter that will be used in conjunction with the LDAP assertion request control (as described in RFC 4528) to indicate that the server should only apply changes to entries that match this filter.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_ASSERTION_FILTER("A filter that will be used in conjunction with the LDAP assertion request control (as described in RFC 4528) to indicate that the server should only apply changes to entries that match this filter."),



  /**
   * Indicates that all operation requests should include the UnboundID-proprietary assured replication request control to indicate that the server should delay returning a response to the client until a minimum amount of replication processing has been performed for the operation.  The ''--{0}'', ''--{1}'', and ''--{2}'' arguments may be used to configure the settings to use for the assured replication control, but the server will automatically determine an appropriate value for any argument that is not provided.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_ASSURED_REPLICATION("Indicates that all operation requests should include the UnboundID-proprietary assured replication request control to indicate that the server should delay returning a response to the client until a minimum amount of replication processing has been performed for the operation.  The ''--{0}'', ''--{1}'', and ''--{2}'' arguments may be used to configure the settings to use for the assured replication control, but the server will automatically determine an appropriate value for any argument that is not provided."),



  /**
   * Specifies the local assurance level to use for the assured replication request control.  This should only be used if the ''{0}'' argument is provided.  The value should be one of ''none'', ''received-any-server'', or ''processed-all-servers''.  If assured replication is to be used but this argument is not provided, then the server will automatically determine the local assurance level to use.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_ASSURED_REPL_LOCAL_LEVEL("Specifies the local assurance level to use for the assured replication request control.  This should only be used if the ''{0}'' argument is provided.  The value should be one of ''none'', ''received-any-server'', or ''processed-all-servers''.  If assured replication is to be used but this argument is not provided, then the server will automatically determine the local assurance level to use."),



  /**
   * Specifies the remote assurance level to use for the assured replication request control.  This should only be used if the ''{0}'' argument is provided.  The value should be one of ''none'', ''received-any-remote-location'', ''received-all-remote-locations'', or ''processed-all-remote-servers''.  If assured replication is to be used but this argument is not provided, then the server will automatically determine the remote assurance level to use.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_ASSURED_REPL_REMOTE_LEVEL("Specifies the remote assurance level to use for the assured replication request control.  This should only be used if the ''{0}'' argument is provided.  The value should be one of ''none'', ''received-any-remote-location'', ''received-all-remote-locations'', or ''processed-all-remote-servers''.  If assured replication is to be used but this argument is not provided, then the server will automatically determine the remote assurance level to use."),



  /**
   * Specifies the timeout to use for assured replication processing.  This should only be used if the ''{0}'' argument is also provided.  If assured replication is to be used but this argument is not provided, then the server will automatically determine the timeout to use.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_ASSURED_REPL_TIMEOUT("Specifies the timeout to use for assured replication processing.  This should only be used if the ''{0}'' argument is also provided.  If assured replication is to be used but this argument is not provided, then the server will automatically determine the timeout to use."),



  /**
   * Indicates that all bind requests should include the authorization identity request control as defined in RFC 3829.  With this control, a successful bind result should include the authorization identity assigned to the connection.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_AUTHZ_IDENTITY("Indicates that all bind requests should include the authorization identity request control as defined in RFC 3829.  With this control, a successful bind result should include the authorization identity assigned to the connection."),



  /**
   * Specifies a control that should be included in all bind requests used to authenticate to the server.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_BIND_CONTROL("Specifies a control that should be included in all bind requests used to authenticate to the server."),



  /**
   * The character set for the LDIF data to be read.  If this argument is not provided, a default encoding of UTF-8 will be used.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_CHARACTER_SET("The character set for the LDIF data to be read.  If this argument is not provided, a default encoding of UTF-8 will be used."),



  /**
   * Indicates that all delete requests should be processed as client-side subtree deletes by searching for all entries below the target entry and then deleting them.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_CLIENT_SIDE_SUBTREE_DELETE("Indicates that all delete requests should be processed as client-side subtree deletes by searching for all entries below the target entry and then deleting them."),



  /**
   * Continue processing changes even if an error is encountered.  If this is not provided, then processing will abort after the first failed operation.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_CONTINUE_ON_ERROR("Continue processing changes even if an error is encountered.  If this is not provided, then processing will abort after the first failed operation."),



  /**
   * Assume that any LDIF change record without a changeType represents an add operation.  If this is not provided, then any change record without a changeType will be considered an error.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_DEFAULT_ADD("Assume that any LDIF change record without a changeType represents an add operation.  If this is not provided, then any change record without a changeType will be considered an error."),



  /**
   * Specifies a control that should be included in all delete requests sent to the server.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_DELETE_CONTROL("Specifies a control that should be included in all delete requests sent to the server."),



  /**
   * Parse the LDIF representations of the changes to apply but don''t actually perform any LDAP communication.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_DRY_RUN("Parse the LDIF representations of the changes to apply but don''t actually perform any LDAP communication."),



  /**
   * The path to a file that contains the passphrase used to generate the key used to encrypt the LDIF data.  If the data is encrypted and this argument is not provided, then the tool will interactively prompt for the correct password.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_ENCRYPTION_PW_FILE("The path to a file that contains the passphrase used to generate the key used to encrypt the LDIF data.  If the data is encrypted and this argument is not provided, then the tool will interactively prompt for the correct password."),



  /**
   * Attempt to follow any referrals encountered while processing changes.  If this is not provided, then any referral received will be considered an error.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_FOLLOW_REFERRALS("Attempt to follow any referrals encountered while processing changes.  If this is not provided, then any referral received will be considered an error."),



  /**
   * Indicates that all add requests should include the generate password request control to request that the server generate a password for the new account.  The generated password will be returned in a corresponding response control.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_GENERATE_PASSWORD("Indicates that all add requests should include the generate password request control to request that the server generate a password for the new account.  The generated password will be returned in a corresponding response control."),



  /**
   * Indicates that all bind requests should include the UnboundID-proprietary get authorization entry request control to request that the server return the specified attribute (or collection of attributes, in the case of a special identifier like ''*'' to indicate all user attributes or ''+'' to indicate all operational attributes) from the authenticated user''s entry.  This argument may be provided multiple times to specify multiple attributes to request.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_GET_AUTHZ_ENTRY_ATTR("Indicates that all bind requests should include the UnboundID-proprietary get authorization entry request control to request that the server return the specified attribute (or collection of attributes, in the case of a special identifier like ''*'' to indicate all user attributes or ''+'' to indicate all operational attributes) from the authenticated user''s entry.  This argument may be provided multiple times to specify multiple attributes to request."),



  /**
   * Indicates that all add, delete, modify, and modify DN requests should include the UnboundID-proprietary get backend set ID request control to request that the Directory Proxy Server include a corresponding get backend set ID response control in each operation response, indicating the entry-balancing backend set in which the write was processed.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_GET_BACKEND_SET_ID("Indicates that all add, delete, modify, and modify DN requests should include the UnboundID-proprietary get backend set ID request control to request that the Directory Proxy Server include a corresponding get backend set ID response control in each operation response, indicating the entry-balancing backend set in which the write was processed."),



  /**
   * Indicates that all bind requests should include the get recent login history request control to request that the server include a corresponding response control with information about the user''s recent login history.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_GET_RECENT_LOGIN_HISTORY("Indicates that all bind requests should include the get recent login history request control to request that the server include a corresponding response control with information about the user''s recent login history."),



  /**
   * Indicates that all add, delete modify, and modify DN requests should include the UnboundID-proprietary get server ID request control to request that server include a corresponding get server ID response control in each operation response, indicating the server in which the write was processed.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_GET_SERVER_ID("Indicates that all add, delete modify, and modify DN requests should include the UnboundID-proprietary get server ID request control to request that server include a corresponding get server ID response control in each operation response, indicating the server in which the write was processed."),



  /**
   * Indicates that all bind requests should include the UnboundID-proprietary get user resource limits request control to request that the server return information about resource limits (e.g., size limit, time limit, idle time limit, etc.) imposed for the user.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_GET_USER_RESOURCE_LIMITS("Indicates that all bind requests should include the UnboundID-proprietary get user resource limits request control to request that the server return information about resource limits (e.g., size limit, time limit, idle time limit, etc.) imposed for the user."),



  /**
   * Indicates that all delete requests should include the UnboundID-proprietary hard delete request control, which will permanently delete the target entry even if the server would have otherwise performed a soft delete operation to hide the entry for a period of time before deleting it.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_HARD_DELETE("Indicates that all delete requests should include the UnboundID-proprietary hard delete request control, which will permanently delete the target entry even if the server would have otherwise performed a soft delete operation to hide the entry for a period of time before deleting it."),



  /**
   * Indicates that all add and modify requests should include the UnboundID-proprietary ignore NO-USER-MODIFICATION request control to permit setting values for certain operational attributes not normally permitted to be provided by external clients.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_IGNORE_NO_USER_MOD("Indicates that all add and modify requests should include the UnboundID-proprietary ignore NO-USER-MODIFICATION request control to permit setting values for certain operational attributes not normally permitted to be provided by external clients."),



  /**
   * Specifies the LDAP protocol version that the tool should use when communicating with the directory server.  This argument has no effect and is provided only for the purpose of compatibility with other ldapmodify tools.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_LDAP_VERSION("Specifies the LDAP protocol version that the tool should use when communicating with the directory server.  This argument has no effect and is provided only for the purpose of compatibility with other ldapmodify tools."),



  /**
   * The path to the LDIF file containing the changes to process.  If this is not provided, then the changes will be read from standard input.  If this argument is provided multiple times to supply multiple LDIF files, then they will be processed in the order they were provided on the command line.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_LDIF_FILE("The path to the LDIF file containing the changes to process.  If this is not provided, then the changes will be read from standard input.  If this argument is provided multiple times to supply multiple LDIF files, then they will be processed in the order they were provided on the command line."),



  /**
   * Indicates that all requests should include the manageDsaIT request control to indicate that referral entries should be treated as regular entries.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_MANAGE_DSA_IT("Indicates that all requests should include the manageDsaIT request control to indicate that referral entries should be treated as regular entries."),



  /**
   * Specifies a control that should be included in all modify requests sent to the server.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_MODIFY_CONTROL("Specifies a control that should be included in all modify requests sent to the server."),



  /**
   * Specifies a control that should be included in all modify DN requests sent to the server.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_MODIFY_DN_CONTROL("Specifies a control that should be included in all modify DN requests sent to the server."),



  /**
   * Indicates that the changes read from standard input or the specified LDIF file should be applied to the entries whose DNs are contained in the specified file rather than the DN contained in the change record.  Only modify operations will be processed when this argument is provided, and it may be provided multiple times to specify multiple DN files.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_MODIFY_DN_FILE("Indicates that the changes read from standard input or the specified LDIF file should be applied to the entries whose DNs are contained in the specified file rather than the DN contained in the change record.  Only modify operations will be processed when this argument is provided, and it may be provided multiple times to specify multiple DN files."),



  /**
   * Indicates that the changes read from standard input or the specified LDIF file should be applied to all entries that match the specified filter.  Only modify operations will be processed when this argument is provided, and the DN of the modify change record will be used as the base DN for the search used to identify the entries to modify.  If the filter may match a large number of entries, then it is strongly recommended that the {0} argument be provided to process the search in batches.  This argument may be provided multiple times to specify multiple filters to use to search for entries to modify.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_MODIFY_ENTRIES_MATCHING_FILTER("Indicates that the changes read from standard input or the specified LDIF file should be applied to all entries that match the specified filter.  Only modify operations will be processed when this argument is provided, and the DN of the modify change record will be used as the base DN for the search used to identify the entries to modify.  If the filter may match a large number of entries, then it is strongly recommended that the {0} argument be provided to process the search in batches.  This argument may be provided multiple times to specify multiple filters to use to search for entries to modify."),



  /**
   * Indicates that the changes read from standard input or the specified LDIF file should be applied to the entry with the specified DN rather than the DN contained in the change record.  Only modify operations will be processed when this argument is provided, and it may be provided multiple times to specify the DNs of multiple entries to modify.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_MODIFY_ENTRY_DN("Indicates that the changes read from standard input or the specified LDIF file should be applied to the entry with the specified DN rather than the DN contained in the change record.  Only modify operations will be processed when this argument is provided, and it may be provided multiple times to specify the DNs of multiple entries to modify."),



  /**
   * Indicates that the changes read from standard input or the specified LDIF file should be applied to all entries that match a filter read from the specified file.  Only modify operations will be processed when this argument is provided, and the DN of the modify change record will be used as the base DN for the search used to identify the entries to modify.  If any of the filters may match a large number of entries, then it is strongly recommended that the {0} argument be provided to process the search in batches.  This argument may be provided multiple times to specify multiple filter files to use to search for entries to modify.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_MODIFY_FILTER_FILE("Indicates that the changes read from standard input or the specified LDIF file should be applied to all entries that match a filter read from the specified file.  Only modify operations will be processed when this argument is provided, and the DN of the modify change record will be used as the base DN for the search used to identify the entries to modify.  If any of the filters may match a large number of entries, then it is strongly recommended that the {0} argument be provided to process the search in batches.  This argument may be provided multiple times to specify multiple filter files to use to search for entries to modify."),



  /**
   * Indicates that all add, delete, modify, and modify DN requests should be sent to the server in a single multi-update request with the specified error behavior.  The value for this argument must be one of ''atomic'' (to indicate that all updates should be processed atomically so that they will either all succeed or all fail), ''abort-on-error'' (to indicate that the server should only process changes until the first error is encountered and ignore any remaining changes after that error), or ''continue-on-error'' (to indicate that the server should continue attempting to process changes, even after one of them has resulted in a failure).
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_MULTI_UPDATE_ERROR_BEHAVIOR("Indicates that all add, delete, modify, and modify DN requests should be sent to the server in a single multi-update request with the specified error behavior.  The value for this argument must be one of ''atomic'' (to indicate that all updates should be processed atomically so that they will either all succeed or all fail), ''abort-on-error'' (to indicate that the server should only process changes until the first error is encountered and ignore any remaining changes after that error), or ''continue-on-error'' (to indicate that the server should continue attempting to process changes, even after one of them has resulted in a failure)."),



  /**
   * Include the UnboundID-proprietary name with entryUUID request control in all add requests sent to the server to indicate that the server should use the entryUUID operational attribute as the naming attribute for the resulting entry instead of the provided RDN.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_NAME_WITH_ENTRY_UUID("Include the UnboundID-proprietary name with entryUUID request control in all add requests sent to the server to indicate that the server should use the entryUUID operational attribute as the naming attribute for the resulting entry instead of the provided RDN."),



  /**
   * Include the LDAP no-operation control in all add, delete, modify, and modify DN requests sent to the server to indicate that the operation should be validated but no changes should actually be applied.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_NO_OPERATION("Include the LDAP no-operation control in all add, delete, modify, and modify DN requests sent to the server to indicate that the operation should be validated but no changes should actually be applied."),



  /**
   * Indicates that all operations should include the UnboundID-proprietary operation purpose request control to provide the specified reason for the operation.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_OPERATION_PURPOSE("Indicates that all operations should include the UnboundID-proprietary operation purpose request control to provide the specified reason for the operation."),



  /**
   * Specifies a control that should be included in all add, delete, modify, and modify DN requests sent to the server.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_OP_CONTROL("Specifies a control that should be included in all add, delete, modify, and modify DN requests sent to the server."),



  /**
   * Indicates that all add, bind and modify requests should include the password policy request control (as defined in draft-behera-ldap-password-policy-10) to request that the response include password policy-related information about the target entry.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_PASSWORD_POLICY("Indicates that all add, bind and modify requests should include the password policy request control (as defined in draft-behera-ldap-password-policy-10) to request that the response include password policy-related information about the target entry."),



  /**
   * Indicates that all add and modify requests that target either the ''{0}'' or ''{1}'' attribute should include the UnboundID-proprietary password validation details request control to indicate that the response should include information about the password quality requirements that the server will impose for the target user''s password and whether the provided password satisfies each of those constraints.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_PASSWORD_VALIDATION_DETAILS("Indicates that all add and modify requests that target either the ''{0}'' or ''{1}'' attribute should include the UnboundID-proprietary password validation details request control to indicate that the response should include information about the password quality requirements that the server will impose for the target user''s password and whether the provided password satisfies each of those constraints."),



  /**
   * Indicates that all modify operation requests should include the permissive modify request control, which indicates that the server should be more lenient for certain types of changes (e.g., trying to add an attribute value that already exists, or trying to remove a value that does not exist) that would normally cause the modify operation to be rejected.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_PERMISSIVE_MODIFY("Indicates that all modify operation requests should include the permissive modify request control, which indicates that the server should be more lenient for certain types of changes (e.g., trying to add an attribute value that already exists, or trying to remove a value that does not exist) that would normally cause the modify operation to be rejected."),



  /**
   * Indicates that all add, modify, and modify DN requests should include the post-read control (as described in RFC 4527) to retrieve the value(s) of the specified attribute as they appear immediately after the operation has been processed.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_POST_READ_ATTRIBUTE("Indicates that all add, modify, and modify DN requests should include the post-read control (as described in RFC 4527) to retrieve the value(s) of the specified attribute as they appear immediately after the operation has been processed."),



  /**
   * Indicates that all delete, modify, and modify DN requests should include the pre-read control (as described in RFC 4527) to retrieve the value(s) of the specified attribute as they appear immediately before the operation has been processed.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_PRE_READ_ATTRIBUTE("Indicates that all delete, modify, and modify DN requests should include the pre-read control (as described in RFC 4527) to retrieve the value(s) of the specified attribute as they appear immediately before the operation has been processed."),



  /**
   * Indicates that all requests should include the proxied authorization request control (as described in RFC 4370) to process the operation under an alternate authorization identity.  The authorization ID should generally be specified in the form ''dn:'' followed by the target user''s DN, or ''u:'' followed by the username.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_PROXY_AS("Indicates that all requests should include the proxied authorization request control (as described in RFC 4370) to process the operation under an alternate authorization identity.  The authorization ID should generally be specified in the form ''dn:'' followed by the target user''s DN, or ''u:'' followed by the username."),



  /**
   * Indicates that all requests should include the legacy proxied authorization v1 request control (as described in draft-weltman-ldapv3-proxy-04) to process the operation under an alternate authorization identity, specified as the DN of the desired user.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_PROXY_V1_AS("Indicates that all requests should include the legacy proxied authorization v1 request control (as described in draft-weltman-ldapv3-proxy-04) to process the operation under an alternate authorization identity, specified as the DN of the desired user."),



  /**
   * Indicates that any modify operation that targets either the ''{0}'' or ''{1}'' attribute should include the UnboundID-proprietary purge current password request control.  This will indicate that the server should could purge the current password from the entry (even if it would have otherwise been retired for a brief period of time).
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_PURGE_CURRENT_PASSWORD("Indicates that any modify operation that targets either the ''{0}'' or ''{1}'' attribute should include the UnboundID-proprietary purge current password request control.  This will indicate that the server should could purge the current password from the entry (even if it would have otherwise been retired for a brief period of time)."),



  /**
   * Specifies that all add and modify requests should include the password update behavior request control with the specified behavior.  Values for this argument must be in the form ''name=value'', where the property name can be any of the following:  is-self-change, allow-pre-encoded-password, skip-password-validation, ignore-password-history, ignore-minimum-password-age, password-storage-scheme, and must-change-password.  The value for each property should be either ''true'' or ''false'', with the exception of the password-storage-scheme property, whose value should be the name of the desired password storage scheme.  This argument can be provided multiple times to specify multiple password update behaviors.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_PW_UPDATE_BEHAVIOR("Specifies that all add and modify requests should include the password update behavior request control with the specified behavior.  Values for this argument must be in the form ''name=value'', where the property name can be any of the following:  is-self-change, allow-pre-encoded-password, skip-password-validation, ignore-password-history, ignore-minimum-password-age, password-storage-scheme, and must-change-password.  The value for each property should be either ''true'' or ''false'', with the exception of the password-storage-scheme property, whose value should be the name of the desired password storage scheme.  This argument can be provided multiple times to specify multiple password update behaviors."),



  /**
   * Specifies a maximum operation rate that the tool should be permitted to achieve.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_RATE_PER_SECOND("Specifies a maximum operation rate that the tool should be permitted to achieve."),



  /**
   * The path to a file to which the tool should write information about any rejected changes.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_REJECT_FILE("The path to a file to which the tool should write information about any rejected changes."),



  /**
   * Indicates that all operation requests should include the UnboundID-proprietary replication repair request control.  This will cause the change to be applied only to the target directory server instance but not to any other server in the replication topology.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_REPLICATION_REPAIR("Indicates that all operation requests should include the UnboundID-proprietary replication repair request control.  This will cause the change to be applied only to the target directory server instance but not to any other server in the replication topology."),



  /**
   * Indicates that any modify operation that targets either the ''{0}'' or ''{1}'' attribute should include the UnboundID-proprietary retire current password request control.  This will indicate that the server should continue to allow the user to authenticate with their current password (in addition to the new password) for a brief period of time.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_RETIRE_CURRENT_PASSWORD("Indicates that any modify operation that targets either the ''{0}'' or ''{1}'' attribute should include the UnboundID-proprietary retire current password request control.  This will indicate that the server should continue to allow the user to authenticate with their current password (in addition to the new password) for a brief period of time."),



  /**
   * Indicates that, if an operation fails in a way that indicates the connection to the server may no longer be valid, the tool should automatically create a new connection and re-try the operation before reporting it as a failure.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_RETRY_FAILED_OPERATIONS("Indicates that, if an operation fails in a way that indicates the connection to the server may no longer be valid, the tool should automatically create a new connection and re-try the operation before reporting it as a failure."),



  /**
   * Specifies the ID of an entry-balancing backend set to which the Directory Proxy Server should send all of the add, delete, modify, and modify DN requests.  The value should be formatted as the entry-balancing request processor ID followed by a colon and the desired backend set ID for that entry-balancing request processor.  This argument can be provided multiple times to specify multiple backend set IDs for the same or different entry-balancing request processors.  The request control will be configured to use absolute routing rather than a routing hint.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_ROUTE_TO_BACKEND_SET("Specifies the ID of an entry-balancing backend set to which the Directory Proxy Server should send all of the add, delete, modify, and modify DN requests.  The value should be formatted as the entry-balancing request processor ID followed by a colon and the desired backend set ID for that entry-balancing request processor.  This argument can be provided multiple times to specify multiple backend set IDs for the same or different entry-balancing request processors.  The request control will be configured to use absolute routing rather than a routing hint."),



  /**
   * Specifies the ID of the backend server to which the Directory Proxy Server should send all add, delete, modify, and modify DN requests.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_ROUTE_TO_SERVER("Specifies the ID of the backend server to which the Directory Proxy Server should send all add, delete, modify, and modify DN requests."),



  /**
   * Indicates that the tool should operate in script-friendly mode.  This argument has no effect and is provided only for the purpose of compatibility with other ldapmodify tools.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_SCRIPT_FRIENDLY("Indicates that the tool should operate in script-friendly mode.  This argument has no effect and is provided only for the purpose of compatibility with other ldapmodify tools."),



  /**
   * Specifies the page size to use in conjunction with the simple paged results control when searching for entries to modify based on the filter provided in the {0} or {1} argument.  If this argument is not provided, then the simple paged results control will not be used.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_SEARCH_PAGE_SIZE("Specifies the page size to use in conjunction with the simple paged results control when searching for entries to modify based on the filter provided in the {0} or {1} argument.  If this argument is not provided, then the simple paged results control will not be used."),



  /**
   * Indicates that all delete requests should be processed as server-side subtree deletes using the subtree delete request control.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_SERVER_SIDE_SUBTREE_DELETE("Indicates that all delete requests should be processed as server-side subtree deletes using the subtree delete request control."),



  /**
   * Indicates that all delete requests should include the UnboundID-proprietary soft delete request control, which indicates that the server should hide the entry for a period of time before deleting it so that it may be restored with an undelete operation if the delete should be reverted.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_SOFT_DELETE("Indicates that all delete requests should include the UnboundID-proprietary soft delete request control, which indicates that the server should hide the entry for a period of time before deleting it so that it may be restored with an undelete operation if the delete should be reverted."),



  /**
   * Indicates that the tool should strip any illegal trailing spaces from LDIF records before attempting to process them.  If this is not provided, then any LDIF record with one or more illegal trailing spaces will be considered an error.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_STRIP_TRAILING_SPACES("Indicates that the tool should strip any illegal trailing spaces from LDIF records before attempting to process them.  If this is not provided, then any LDIF record with one or more illegal trailing spaces will be considered an error."),



  /**
   * Indicates that all operations should include the UnboundID-proprietary suppress operational attribute updates request control to indicate that the server should not apply any updates to the specified operational attributes.  The value may be one of ''last-access-time'', ''last-login-time'', ''last-login-ip'', or ''lastmod''.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_SUPPRESS_OP_ATTR_UPDATES("Indicates that all operations should include the UnboundID-proprietary suppress operational attribute updates request control to indicate that the server should not apply any updates to the specified operational attributes.  The value may be one of ''last-access-time'', ''last-login-time'', ''last-login-ip'', or ''lastmod''."),



  /**
   * Indicates that the tool should include the UnboundID-proprietary suppress referential integrity updates request control in all delete and modify DN operations to indicate that the server should not perform any referential integrity processing for those operations.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_SUPPRESS_REFERINT_UPDATES("Indicates that the tool should include the UnboundID-proprietary suppress referential integrity updates request control in all delete and modify DN operations to indicate that the server should not perform any referential integrity processing for those operations."),



  /**
   * Indicates that all add, modify, and modify DN requests should include a uniqueness request control that indicates that the server should attempt to prevent the requested operation from introducing a conflict with the same value in an existing entry.  This may be provided multiple times to specify multiple unique attribute types, and in that case the ''--uniquenessMultipleAttributeBehavior'' argument may be used to specify the behavior to exhibit for conflicts across those attribute types.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_UNIQUE_ATTR("Indicates that all add, modify, and modify DN requests should include a uniqueness request control that indicates that the server should attempt to prevent the requested operation from introducing a conflict with the same value in an existing entry.  This may be provided multiple times to specify multiple unique attribute types, and in that case the ''--uniquenessMultipleAttributeBehavior'' argument may be used to specify the behavior to exhibit for conflicts across those attribute types."),



  /**
   * Specifies the base DN that should be included in the uniqueness request control.  This can only be used if at least one of the ''--uniquenessAttribute'' or ''--uniquenessFilter'' arguments is provided.  If this is not given, the server will look for uniqueness conflicts within all public naming contexts.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_UNIQUE_BASE_DN("Specifies the base DN that should be included in the uniqueness request control.  This can only be used if at least one of the ''--uniquenessAttribute'' or ''--uniquenessFilter'' arguments is provided.  If this is not given, the server will look for uniqueness conflicts within all public naming contexts."),



  /**
   * Indicates that all add, modify and modify DN requests should include a uniqueness request control that indicates that the server should attempt to prevent the requested operation from introducing a conflict with other entries matching the provided filter.  If the ''--uniquenessAttribute'' argument is provided, then this filter will be used to narrow the set of potentially conflicting entries to only those that also match this filter.  If the ''--uniquenessAttribute'' argument is not provided, then the server will consider it a conflict if any other entry matches the provided filter.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_UNIQUE_FILTER("Indicates that all add, modify and modify DN requests should include a uniqueness request control that indicates that the server should attempt to prevent the requested operation from introducing a conflict with other entries matching the provided filter.  If the ''--uniquenessAttribute'' argument is provided, then this filter will be used to narrow the set of potentially conflicting entries to only those that also match this filter.  If the ''--uniquenessAttribute'' argument is not provided, then the server will consider it a conflict if any other entry matches the provided filter."),



  /**
   * Specifies the behavior that the server should exhibit if the multiple unique attribute types are configured.  This can only be used if the ''--uniquenessAttribute'' argument is provided.  Allowed values for this argument are ''unique-within-each-attribute'' (to indicate that each attribute should be considered separately), ''unique-across-all-attributes-including-in-same-entry'' (to indicate that the value of one unique attribute cannot also be present in the value of any other unique attributes, even if the conflicting values are in the same entry), ''unique-across-all-attributes-except-in-same-entry'' (to indicate that the value of one unique attribute cannot also be present in the value of any other unique attribute, although the same entry will be permitted to have the same value in multiple attributes), or ''unique-in-combination'' (to indicate that no other entry will be permitted to have the same combination of unique attribute values).  If this is not specified, then a default of ''unique-within-each-attribute'' will be used.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_UNIQUE_MULTIPLE_ATTRIBUTE_BEHAVIOR("Specifies the behavior that the server should exhibit if the multiple unique attribute types are configured.  This can only be used if the ''--uniquenessAttribute'' argument is provided.  Allowed values for this argument are ''unique-within-each-attribute'' (to indicate that each attribute should be considered separately), ''unique-across-all-attributes-including-in-same-entry'' (to indicate that the value of one unique attribute cannot also be present in the value of any other unique attributes, even if the conflicting values are in the same entry), ''unique-across-all-attributes-except-in-same-entry'' (to indicate that the value of one unique attribute cannot also be present in the value of any other unique attribute, although the same entry will be permitted to have the same value in multiple attributes), or ''unique-in-combination'' (to indicate that no other entry will be permitted to have the same combination of unique attribute values).  If this is not specified, then a default of ''unique-within-each-attribute'' will be used."),



  /**
   * Specifies the level of post-commit validation that should be used for the uniqueness request control.  This can only be used if at least one of the ''--uniquenessAttribute'' or ''--uniquenessFilter'' arguments is provided.  Allowed values for this argument are ''none'' (to indicate that no post-commit validation should be performed), ''all-subtree-views'' (to indicate that a minimum of one post-commit check should be performed in each applicable subtree view), ''all-backend-sets'' (to indicate that a minimum of one post-commit check should be performed in each entry-balancing backend set), and ''all-available-backend-servers'' (to indicate that the post-commit check should be made in all backend servers available through the Directory Proxy Server).  If this is not specified, then a default of ''all-subtree-views'' will be used.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_UNIQUE_POST_COMMIT_LEVEL("Specifies the level of post-commit validation that should be used for the uniqueness request control.  This can only be used if at least one of the ''--uniquenessAttribute'' or ''--uniquenessFilter'' arguments is provided.  Allowed values for this argument are ''none'' (to indicate that no post-commit validation should be performed), ''all-subtree-views'' (to indicate that a minimum of one post-commit check should be performed in each applicable subtree view), ''all-backend-sets'' (to indicate that a minimum of one post-commit check should be performed in each entry-balancing backend set), and ''all-available-backend-servers'' (to indicate that the post-commit check should be made in all backend servers available through the Directory Proxy Server).  If this is not specified, then a default of ''all-subtree-views'' will be used."),



  /**
   * Specifies the level of pre-commit validation that should be used for the uniqueness request control.  This can only be used if at least one of the ''--uniquenessAttribute'' or ''--uniquenessFilter'' arguments is provided.  Allowed values for this argument are ''none'' (to indicate that no pre-commit validation should be performed), ''all-subtree-views'' (to indicate that a minimum of one pre-commit check should be performed in each applicable subtree view), ''all-backend-sets'' (to indicate that a minimum of one pre-commit check should be performed in each entry-balancing backend set), and ''all-available-backend-servers'' (to indicate that the pre-commit check should be made in all backend servers available through the Directory Proxy Server).  If this is not specified, then a default of ''all-subtree-views'' will be used.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_UNIQUE_PRE_COMMIT_LEVEL("Specifies the level of pre-commit validation that should be used for the uniqueness request control.  This can only be used if at least one of the ''--uniquenessAttribute'' or ''--uniquenessFilter'' arguments is provided.  Allowed values for this argument are ''none'' (to indicate that no pre-commit validation should be performed), ''all-subtree-views'' (to indicate that a minimum of one pre-commit check should be performed in each applicable subtree view), ''all-backend-sets'' (to indicate that a minimum of one pre-commit check should be performed in each entry-balancing backend set), and ''all-available-backend-servers'' (to indicate that the pre-commit check should be made in all backend servers available through the Directory Proxy Server).  If this is not specified, then a default of ''all-subtree-views'' will be used."),



  /**
   * Indicates that the tool should attempt to use an administrative session to process all operations using a dedicated pool of worker threads.  This may be useful when trying to diagnose problems in a server that is unresponsive because all normal worker threads are busy processing other requests.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_USE_ADMIN_SESSION("Indicates that the tool should attempt to use an administrative session to process all operations using a dedicated pool of worker threads.  This may be useful when trying to diagnose problems in a server that is unresponsive because all normal worker threads are busy processing other requests."),



  /**
   * Indicates that the server should create a transaction to process all operations as a single atomic unit.  This should generally only be used with a relatively small number of changes, and it is recommended that the changes be supplied in an LDIF file rather than via standard input.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_USE_TRANSACTION("Indicates that the server should create a transaction to process all operations as a single atomic unit.  This should generally only be used with a relatively small number of changes, and it is recommended that the changes be supplied in an LDIF file rather than via standard input."),



  /**
   * Provide verbose output for operations processed.
   */
  INFO_LDAPMODIFY_ARG_DESCRIPTION_VERBOSE("Provide verbose output for operations processed."),



  /**
   * Indicates that the tool should not attempt to retry operations that fail in a way that the connection to the directory server may be invalid.  By default, it will automatically try to establish a new connection and retry the failed operation.
   */
  INFO_LDAPMODIFY_ARG_DESC_NEVER_RETRY("Indicates that the tool should not attempt to retry operations that fail in a way that the connection to the directory server may be invalid.  By default, it will automatically try to establish a new connection and retry the failed operation."),



  /**
   * Control Arguments
   */
  INFO_LDAPMODIFY_ARG_GROUP_CONTROLS("Control Arguments"),



  /**
   * Data Arguments
   */
  INFO_LDAPMODIFY_ARG_GROUP_DATA("Data Arguments"),



  /**
   * Operation Arguments
   */
  INFO_LDAPMODIFY_ARG_GROUP_OPS("Operation Arguments"),



  /**
   * '{'entry-balancing-processor-id:backend-set-id'}'
   */
  INFO_LDAPMODIFY_ARG_PLACEHOLDER_ROUTE_TO_BACKEND_SET("'{'entry-balancing-processor-id:backend-set-id'}'"),



  /**
   * '{'id'}'
   */
  INFO_LDAPMODIFY_ARG_PLACEHOLDER_ROUTE_TO_SERVER("'{'id'}'"),



  /**
   * The server refused to process the operation because entry ''{0}'' did not match assertion filter ''{1}''.
   */
  INFO_LDAPMODIFY_ASSERTION_FAILED("The server refused to process the operation because entry ''{0}'' did not match assertion filter ''{1}''."),



  /**
   * Attempting a client-side delete of the subtree with base DN {0} ...
   */
  INFO_LDAPMODIFY_CLIENT_SIDE_DELETING_SUBTREE("Attempting a client-side delete of the subtree with base DN {0} ..."),



  /**
   * The client-side subtree delete completed successfully, removing base entry ''{0}'', which did not have any subordinates.
   */
  INFO_LDAPMODIFY_CLIENT_SIDE_SUB_DEL_SUCCEEDED_WITH_1_ENTRY("The client-side subtree delete completed successfully, removing base entry ''{0}'', which did not have any subordinates."),



  /**
   * The client-side subtree delete completed successfully, removing a total of {0,number,0} entries at and below ''{1}''.
   */
  INFO_LDAPMODIFY_CLIENT_SIDE_SUB_DEL_SUCCEEDED_WITH_ENTRIES("The client-side subtree delete completed successfully, removing a total of {0,number,0} entries at and below ''{1}''."),



  /**
   * Successfully connected to {0}.
   */
  INFO_LDAPMODIFY_CONNECTION_ESTABLISHED("Successfully connected to {0}."),



  /**
   * Delete request for entry {0} added to the set of operations to be included in the multi-update request.
   */
  INFO_LDAPMODIFY_DELETE_ADDED_TO_MULTI_UPDATE("Delete request for entry {0} added to the set of operations to be included in the multi-update request."),



  /**
   * Deleting entry {0} ...
   */
  INFO_LDAPMODIFY_DELETING_ENTRY("Deleting entry {0} ..."),



  /**
   * Would have attempted to add entry {0} if argument {1} had not been provided.
   */
  INFO_LDAPMODIFY_DRY_RUN_ADD("Would have attempted to add entry {0} if argument {1} had not been provided."),



  /**
   * Would have attempted to delete entry {0} if argument {1} had not been provided.
   */
  INFO_LDAPMODIFY_DRY_RUN_DELETE("Would have attempted to delete entry {0} if argument {1} had not been provided."),



  /**
   * Would have attempted to modify entry {0} if argument {1} had not been provided.
   */
  INFO_LDAPMODIFY_DRY_RUN_MODIFY("Would have attempted to modify entry {0} if argument {1} had not been provided."),



  /**
   * Would have attempted to move entry {0} if argument {1} had not been provided.
   */
  INFO_LDAPMODIFY_DRY_RUN_MOVE("Would have attempted to move entry {0} if argument {1} had not been provided."),



  /**
   * Would have attempted to move entry {0} to {1} if argument {2} had not been provided.
   */
  INFO_LDAPMODIFY_DRY_RUN_MOVE_TO("Would have attempted to move entry {0} to {1} if argument {2} had not been provided."),



  /**
   * Would have attempted to rename entry {0} if argument {1} had not been provided.
   */
  INFO_LDAPMODIFY_DRY_RUN_RENAME("Would have attempted to rename entry {0} if argument {1} had not been provided."),



  /**
   * Would have attempted to rename entry {0} to {1} if argument {2} had not been provided.
   */
  INFO_LDAPMODIFY_DRY_RUN_RENAME_TO("Would have attempted to rename entry {0} to {1} if argument {2} had not been provided."),



  /**
   * Read the changes to apply from standard input and send them to the target directory server over an unencrypted LDAP connection.  Any change records that don''t include a changetype will be treated as entries to be added.
   */
  INFO_LDAPMODIFY_EXAMPLE_1("Read the changes to apply from standard input and send them to the target directory server over an unencrypted LDAP connection.  Any change records that don''t include a changetype will be treated as entries to be added."),



  /**
   * Read the changes to apply from the specified LDIF and apply them to all entries matching filter ''(objectClass=person)'' using the simple paged results control to retrieve no more than 100 entries at a time.  The communication will be encrypted with SSL, and if the first server is unavailable, the second server will be tried.
   */
  INFO_LDAPMODIFY_EXAMPLE_2("Read the changes to apply from the specified LDIF and apply them to all entries matching filter ''(objectClass=person)'' using the simple paged results control to retrieve no more than 100 entries at a time.  The communication will be encrypted with SSL, and if the first server is unavailable, the second server will be tried."),



  /**
   * Modifying entry {0} ...
   */
  INFO_LDAPMODIFY_MODIFYING_ENTRY("Modifying entry {0} ..."),



  /**
   * Modify request for entry {0} added to the set of operations to be included in the multi-update request.
   */
  INFO_LDAPMODIFY_MODIFY_ADDED_TO_MULTI_UPDATE("Modify request for entry {0} added to the set of operations to be included in the multi-update request."),



  /**
   * Modify DN request for entry {0} added to the set of operations to be included in the multi-update request.
   */
  INFO_LDAPMODIFY_MODIFY_DN_ADDED_TO_MULTI_UPDATE("Modify DN request for entry {0} added to the set of operations to be included in the multi-update request."),



  /**
   * Moving entry {0} ...
   */
  INFO_LDAPMODIFY_MOVING_ENTRY("Moving entry {0} ..."),



  /**
   * Moving entry {0} to {1} ...
   */
  INFO_LDAPMODIFY_MOVING_ENTRY_TO("Moving entry {0} to {1} ..."),



  /**
   * The operation was accepted by the server but no change was applied because the request included the LDAP no-op control.
   */
  INFO_LDAPMODIFY_NO_OP("The operation was accepted by the server but no change was applied because the request included the LDAP no-op control."),



  /**
   * '{'behavior'}'
   */
  INFO_LDAPMODIFY_PLACEHOLDER_BEHAVIOR("'{'behavior'}'"),



  /**
   * '{'charset'}'
   */
  INFO_LDAPMODIFY_PLACEHOLDER_CHARSET("'{'charset'}'"),



  /**
   * '{'level'}'
   */
  INFO_LDAPMODIFY_PLACEHOLDER_LEVEL("'{'level'}'"),



  /**
   * '{'name=value'}'
   */
  INFO_LDAPMODIFY_PLACEHOLDER_NAME_EQUALS_VALUE("'{'name=value'}'"),



  /**
   * Renaming entry {0} ...
   */
  INFO_LDAPMODIFY_RENAMING_ENTRY("Renaming entry {0} ..."),



  /**
   * Renaming entry {0} to {1} ...
   */
  INFO_LDAPMODIFY_RENAMING_ENTRY_TO("Renaming entry {0} to {1} ..."),



  /**
   * Completed search processing for {0,number,0} entries below ''{1}'' matching filter ''{2}''.
   */
  INFO_LDAPMODIFY_SEARCH_COMPLETED("Completed search processing for {0,number,0} entries below ''{1}'' matching filter ''{2}''."),



  /**
   * Completed search processing for a page of results for entries below ''{0}'' matching filter ''{1}'', but there are still more entries to process.  {2,number,0} entries processed so far.
   */
  INFO_LDAPMODIFY_SEARCH_COMPLETED_MORE_PAGES("Completed search processing for a page of results for entries below ''{0}'' matching filter ''{1}'', but there are still more entries to process.  {2,number,0} entries processed so far."),



  /**
   * Sending the changes to the server in a multi-update request ...
   */
  INFO_LDAPMODIFY_SENDING_MULTI_UPDATE_REQUEST("Sending the changes to the server in a multi-update request ..."),



  /**
   * Successfully started a transaction with ID {0}
   */
  INFO_LDAPMODIFY_STARTED_TXN("Successfully started a transaction with ID {0}"),



  /**
   * Apply a set of add, delete, modify, and/or modify DN operations to a directory server.  Supply the changes to apply in LDIF format, either from standard input or from a file specified with the ''{0}'' argument.  Change records must be separated by at least one blank line.
   */
  INFO_LDAPMODIFY_TOOL_DESCRIPTION("Apply a set of add, delete, modify, and/or modify DN operations to a directory server.  Supply the changes to apply in LDIF format, either from standard input or from a file specified with the ''{0}'' argument.  Change records must be separated by at least one blank line."),



  /**
   * The criteria for the search request can be specified in a number of different ways, including providing all of the details directly via command-line arguments, providing all of the arguments except the filter via command-line arguments and specifying a file that holds the filters to use, or specifying a file that includes a set of LDAP URLs with the base DN, scope, filter, and attributes to return.
   */
  INFO_LDAPSEARCH_ADDITIONAL_DESCRIPTION_PARAGRAPH_1("The criteria for the search request can be specified in a number of different ways, including providing all of the details directly via command-line arguments, providing all of the arguments except the filter via command-line arguments and specifying a file that holds the filters to use, or specifying a file that includes a set of LDAP URLs with the base DN, scope, filter, and attributes to return."),



  /**
   * See the examples below for a number of sample command lines for this tool.
   */
  INFO_LDAPSEARCH_ADDITIONAL_DESCRIPTION_PARAGRAPH_2("See the examples below for a number of sample command lines for this tool."),



  /**
   * Indicates that all search requests should include the UnboundID-proprietary account usable request control to request that each search result entry returned include a response control with information about the password policy usability state for the entry.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_ACCOUNT_USABLE("Indicates that all search requests should include the UnboundID-proprietary account usable request control to request that each search result entry returned include a response control with information about the password policy usability state for the entry."),



  /**
   * A filter that will be used in conjunction with the LDAP assertion request control (as described in RFC 4528) to indicate that the server should only process searches in which the entry specified as the base DN matches this filter.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_ASSERTION_FILTER("A filter that will be used in conjunction with the LDAP assertion request control (as described in RFC 4528) to indicate that the server should only process searches in which the entry specified as the base DN matches this filter."),



  /**
   * Indicates that all bind requests should include the authorization identity request control as defined in RFC 3829.  With this control, a successful bind result should include the authorization identity assigned to the connection.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_AUTHZ_IDENTITY("Indicates that all bind requests should include the authorization identity request control as defined in RFC 3829.  With this control, a successful bind result should include the authorization identity assigned to the connection."),



  /**
   * Specifies the base DN that should be used for the search.  If a filter file is provided, then this base DN will be used for each search with a filter read from that file.  This argument must not be provided if the --ldapURLFile is given.  If no base DN is specified, then the null base DN will be used by default.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_BASE_DN("Specifies the base DN that should be used for the search.  If a filter file is provided, then this base DN will be used for each search with a filter read from that file.  This argument must not be provided if the --ldapURLFile is given.  If no base DN is specified, then the null base DN will be used by default."),



  /**
   * Specifies a control that should be included in all bind requests used to authenticate to the server.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_BIND_CONTROL("Specifies a control that should be included in all bind requests used to authenticate to the server."),



  /**
   * Indicates that the output should be gzip-compressed.  This can only be used if the --outputFile argument is provided and the --teeResultsToStandardOut argument is not provided.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_COMPRESS_OUTPUT("Indicates that the output should be gzip-compressed.  This can only be used if the --outputFile argument is provided and the --teeResultsToStandardOut argument is not provided."),



  /**
   * Continue processing searches even if an error is encountered.  If this is not provided, then processing will abort after the first failed search.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_CONTINUE_ON_ERROR("Continue processing searches even if an error is encountered.  If this is not provided, then processing will abort after the first failed search."),



  /**
   * Indicates that the tool exit code should represent the number of entries returned during search processing.  If this is not provided, or if an error is encountered while processing the search, then the tool exit code will reflect the LDAP result code received during search processing (or an appropriate client-side result code if a problem was encountered before sending any searches).  Note that because some operating systems do not allow exit code values greater than 255, that will be the maximum exit value for this tool even if more than 255 entries are returned.  Also note that this argument can only be used when processing a single search.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_COUNT_ENTRIES("Indicates that the tool exit code should represent the number of entries returned during search processing.  If this is not provided, or if an error is encountered while processing the search, then the tool exit code will reflect the LDAP result code received during search processing (or an appropriate client-side result code if a problem was encountered before sending any searches).  Note that because some operating systems do not allow exit code values greater than 255, that will be the maximum exit value for this tool even if more than 255 entries are returned.  Also note that this argument can only be used when processing a single search."),



  /**
   * Specifies the alias dereferencing policy to use for search requests.  The value should be one of ''never'', ''always'', ''search'', or ''find''.  If this argument is not provided, a default of ''never'' will be used.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_DEREFERENCE_POLICY("Specifies the alias dereferencing policy to use for search requests.  The value should be one of ''never'', ''always'', ''search'', or ''find''.  If this argument is not provided, a default of ''never'' will be used."),



  /**
   * Indicates that no line wrapping should be performed when displaying the LDIF representations of matching entries.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_DONT_WRAP("Indicates that no line wrapping should be performed when displaying the LDIF representations of matching entries."),



  /**
   * Indicate which searches would be issued but do not actually send them to the server.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_DRY_RUN("Indicate which searches would be issued but do not actually send them to the server."),



  /**
   * The path to a file that specifies the passphrase to use to encrypt the output.  This can only be provided if the --encryptOutput argument is given, but if that argument is given and no passphrase file is specified, then the passphrase will be interactively requested.  If a file is specified, then that file must exist and must contain exactly one line comprised entirely of the passphrase.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_ENCRYPTION_PW_FILE("The path to a file that specifies the passphrase to use to encrypt the output.  This can only be provided if the --encryptOutput argument is given, but if that argument is given and no passphrase file is specified, then the passphrase will be interactively requested.  If a file is specified, then that file must exist and must contain exactly one line comprised entirely of the passphrase."),



  /**
   * Indicates that the output should be encrypted with a key generated from a provided password.  This can only be used if the --outputFile argument is provided and the --teeResultsToStandardOut argument is not provided.  If the --encryptionPassphraseFile argument is provided, then that file will be used to specify the encryption passphrase; otherwise, the passphrase will be interactively requested.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_ENCRYPT_OUTPUT("Indicates that the output should be encrypted with a key generated from a provided password.  This can only be used if the --outputFile argument is provided and the --teeResultsToStandardOut argument is not provided.  If the --encryptionPassphraseFile argument is provided, then that file will be used to specify the encryption passphrase; otherwise, the passphrase will be interactively requested."),



  /**
   * Specifies the name or OID of an attribute that should be excluded from search result entries.  This argument may be provided multiple times to specify multiple attributes to exclude.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_EXCLUDE_ATTRIBUTE("Specifies the name or OID of an attribute that should be excluded from search result entries.  This argument may be provided multiple times to specify multiple attributes to exclude."),



  /**
   * Indicates that all search requests should include the UnboundID-proprietary exclude branch request control to indicate that matching entries below the specified base DN should be excluded from search results.  This argument may be provided multiple times if multiple branches should be excluded.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_EXCLUDE_BRANCH("Indicates that all search requests should include the UnboundID-proprietary exclude branch request control to indicate that matching entries below the specified base DN should be excluded from search results.  This argument may be provided multiple times if multiple branches should be excluded."),



  /**
   * Specifies a filter to use when processing a search.  This may be provided multiple times to issue multiple searches with different filters.  If this argument is provided, then the first trailing argument will not be interpreted as a search filter (all trailing arguments will be interpreted as requested attributes).
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_FILTER("Specifies a filter to use when processing a search.  This may be provided multiple times to issue multiple searches with different filters.  If this argument is provided, then the first trailing argument will not be interpreted as a search filter (all trailing arguments will be interpreted as requested attributes)."),



  /**
   * Specifies the path to a file containing the search filters to issue.  Each filter should be on a separate line.  Blank lines and lines beginning with the ''#'' character will be ignored.  This argument may be provided multiple times to specify multiple filter files.  If a filter file is provided, then the first trailing argument will not be interpreted as a search filter (all trailing arguments will be interpreted as requested attributes).
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_FILTER_FILE("Specifies the path to a file containing the search filters to issue.  Each filter should be on a separate line.  Blank lines and lines beginning with the ''#'' character will be ignored.  This argument may be provided multiple times to specify multiple filter files.  If a filter file is provided, then the first trailing argument will not be interpreted as a search filter (all trailing arguments will be interpreted as requested attributes)."),



  /**
   * Attempt to follow any referrals and search result references encountered during search processing.  If this is not provided, then referrals and search references will be displayed in the output.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_FOLLOW_REFERRALS("Attempt to follow any referrals and search result references encountered during search processing.  If this is not provided, then referrals and search references will be displayed in the output."),



  /**
   * Indicates that all bind requests should include the UnboundID-proprietary get authorization entry request control to request that the server return the specified attribute (or collection of attributes, in the case of a special identifier like ''*'' to indicate all user attributes or ''+'' to indicate all operational attributes) from the authenticated user''s entry.  This argument may be provided multiple times to specify multiple attributes to request.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_GET_AUTHZ_ENTRY_ATTR("Indicates that all bind requests should include the UnboundID-proprietary get authorization entry request control to request that the server return the specified attribute (or collection of attributes, in the case of a special identifier like ''*'' to indicate all user attributes or ''+'' to indicate all operational attributes) from the authenticated user''s entry.  This argument may be provided multiple times to specify multiple attributes to request."),



  /**
   * Indicates that all search requests should include the UnboundID-proprietary get backend set ID request control to request that the Directory Proxy Server include a corresponding get backend set ID response control in each search result entry, indicating the entry-balancing backend set from which that entry was retrieved.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_GET_BACKEND_SET_ID("Indicates that all search requests should include the UnboundID-proprietary get backend set ID request control to request that the Directory Proxy Server include a corresponding get backend set ID response control in each search result entry, indicating the entry-balancing backend set from which that entry was retrieved."),



  /**
   * Indicates that all search requests should include the UnboundID-proprietary get effective rights request control to return information about the access control rights that a user has when interacting with each matching entry.  This argument may be provided multiple times to specify multiple attributes.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_GET_EFFECTIVE_RIGHTS_ATTR("Indicates that all search requests should include the UnboundID-proprietary get effective rights request control to return information about the access control rights that a user has when interacting with each matching entry.  This argument may be provided multiple times to specify multiple attributes."),



  /**
   * Indicates that all search requests should include the UnboundID-proprietary get effective rights request control to return information about the access control rights the specified user has when interacting with each matching entry.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_GET_EFFECTIVE_RIGHTS_AUTHZID("Indicates that all search requests should include the UnboundID-proprietary get effective rights request control to return information about the access control rights the specified user has when interacting with each matching entry."),



  /**
   * Indicates that all bind requests should include the get recent login history request control to request that the server include a corresponding response control with information about the user''s recent login history.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_GET_RECENT_LOGIN_HISTORY("Indicates that all bind requests should include the get recent login history request control to request that the server include a corresponding response control with information about the user''s recent login history."),



  /**
   * Indicates that all search requests should include the UnboundID-proprietary get server ID request control to request that server include a corresponding get server ID response control in each search result entry, indicating the server from which that entry was retrieved.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_GET_SERVER_ID("Indicates that all search requests should include the UnboundID-proprietary get server ID request control to request that server include a corresponding get server ID response control in each search result entry, indicating the server from which that entry was retrieved."),



  /**
   * Indicates that all bind requests should include the UnboundID-proprietary get user resource limits request control to request that the server return information about resource limits (e.g., size limit, time limit, idle time limit, etc.) imposed for the user.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_GET_USER_RESOURCE_LIMITS("Indicates that all bind requests should include the UnboundID-proprietary get user resource limits request control to request that the server return information about resource limits (e.g., size limit, time limit, idle time limit, etc.) imposed for the user."),



  /**
   * Indicates that the output should not reveal the number of values contained in redacted attributes.  If this argument is present, then a redacted attribute will only ever have a single value of ''***REDACTED***''.  If this argument is not present, then a redacted attribute with multiple values will still have the same number of values that it originally had, but those values will be ''***REDACTED1***'', ''***REDACTED2***'', etc.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_HIDE_REDACTED_VALUE_COUNT("Indicates that the output should not reveal the number of values contained in redacted attributes.  If this argument is present, then a redacted attribute will only ever have a single value of ''***REDACTED***''.  If this argument is not present, then a redacted attribute with multiple values will still have the same number of values that it originally had, but those values will be ''***REDACTED1***'', ''***REDACTED2***'', etc."),



  /**
   * Indicates that all search requests should include the subentries request control as described in draft-ietf-ldup-subentry to indicate that the server may return any LDAP subentries that match the search criteria.  LDAP subentries are normally excluded from search results.  This control does not take a value.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_INCLUDE_DRAFT_LDUP_SUBENTRIES("Indicates that all search requests should include the subentries request control as described in draft-ietf-ldup-subentry to indicate that the server may return any LDAP subentries that match the search criteria.  LDAP subentries are normally excluded from search results.  This control does not take a value."),



  /**
   * Indicates that all search requests should include the UnboundID-proprietary return conflict entries request control to indicate that the server may return any replication conflict entries that match the search criteria.  Replication conflict entries are normally excluded from search results.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_INCLUDE_REPL_CONFLICTS("Indicates that all search requests should include the UnboundID-proprietary return conflict entries request control to indicate that the server may return any replication conflict entries that match the search criteria.  Replication conflict entries are normally excluded from search results."),



  /**
   * Indicates that all search requests should include the subentries request control as described in RFC 3672 to indicate that the server may return any LDAP subentries that match the search criteria, optionally including regular entries along with the subentries.  LDAP subentries are normally excluded from search results.  This control requires a Boolean value of either ''true'' or ''false'' to indicate whether the server should return only subentries (if true), or both regular entries and subentries (if false).
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_INCLUDE_RFC_3672_SUBENTRIES("Indicates that all search requests should include the subentries request control as described in RFC 3672 to indicate that the server may return any LDAP subentries that match the search criteria, optionally including regular entries along with the subentries.  LDAP subentries are normally excluded from search results.  This control requires a Boolean value of either ''true'' or ''false'' to indicate whether the server should return only subentries (if true), or both regular entries and subentries (if false)."),



  /**
   * Indicates that all search requests should include the UnboundID-proprietary soft-deleted entry access request control to indicate that the server may return any soft-deleted entries that match the search criteria.  Soft-deleted entries are normally excluded from search results.  The value for this argument must be one of:  ''with-non-deleted-entries'' (indicates that both regular and soft-deleted entries should be returned), ''without-non-deleted-entries'' (indicates that only soft-deleted entries should be returned), or ''deleted-entries-in-undeleted-form'' (returns only soft-deleted entries in the form in the form the entries had before they were deleted).
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_INCLUDE_SOFT_DELETED("Indicates that all search requests should include the UnboundID-proprietary soft-deleted entry access request control to indicate that the server may return any soft-deleted entries that match the search criteria.  Soft-deleted entries are normally excluded from search results.  The value for this argument must be one of:  ''with-non-deleted-entries'' (indicates that both regular and soft-deleted entries should be returned), ''without-non-deleted-entries'' (indicates that only soft-deleted entries should be returned), or ''deleted-entries-in-undeleted-form'' (returns only soft-deleted entries in the form in the form the entries had before they were deleted)."),



  /**
   * Specifies an identifier that indicates which attribute(s) should be included in entries joined with search result entries.  The value may be an attribute name or OID, a special token like ''*'' to indicate all user attributes or ''+'' to indicate all operational attributes, or an object class name prefixed by an ''@'' symbol to indicate all attributes associated with the specified object class.  This may be provided multiple times to specify multiple requested attributes.  If this is not specified, then the server will behave as if all user attributes had been requested.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_JOIN_ATTR("Specifies an identifier that indicates which attribute(s) should be included in entries joined with search result entries.  The value may be an attribute name or OID, a special token like ''*'' to indicate all user attributes or ''+'' to indicate all operational attributes, or an object class name prefixed by an ''@'' symbol to indicate all attributes associated with the specified object class.  This may be provided multiple times to specify multiple requested attributes.  If this is not specified, then the server will behave as if all user attributes had been requested."),



  /**
   * Specifies the base DN to use for searches used to join search result entries with related entries.  The value may be one of ''search-base'' to use the base DN of the search request, ''source-entry-dn'' to use the DN of the source entry as the base DN for join searches, or any valid LDAP DN to use a custom base DN for join searches.  If this is not specified, then the default join base DN will be the search base DN.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_JOIN_BASE_DN("Specifies the base DN to use for searches used to join search result entries with related entries.  The value may be one of ''search-base'' to use the base DN of the search request, ''source-entry-dn'' to use the DN of the source entry as the base DN for join searches, or any valid LDAP DN to use a custom base DN for join searches.  If this is not specified, then the default join base DN will be the search base DN."),



  /**
   * Specifies an additional filter that the server will require target entries to match in order to be joined with the source entry.  If this is not provided, no additional join filter will be used.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_JOIN_FILTER("Specifies an additional filter that the server will require target entries to match in order to be joined with the source entry.  If this is not provided, no additional join filter will be used."),



  /**
   * Indicates that the server should not return an entry that matches the search criteria if it is not joined with at least one additional entry.  If this is not provided, then entries matching the search criteria will be returned even if they are not joined with any other entries.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_JOIN_REQUIRE_MATCH("Indicates that the server should not return an entry that matches the search criteria if it is not joined with at least one additional entry.  If this is not provided, then entries matching the search criteria will be returned even if they are not joined with any other entries."),



  /**
   * Indicates that all search requests should include the join request control to indicate that matching entries should be joined with related entries based on the specified criteria.  Allowed values include ''dn:'' followed by the name of an attribute in the source entry containing the DNs of the entries with which to join; ''reverse-dn:'' followed by the name of an attribute in the target entries whose value is the DN of the source entry; ''equals:'' followed by the name of an attribute in the source entry, a colon, and the name of an attribute in target entries that must exactly match the source attribute; or ''contains:'' followed by the name of an attribute in the source entry, a colon, and the name of an attribute in target entries that must match or contain the value of the source attribute.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_JOIN_RULE("Indicates that all search requests should include the join request control to indicate that matching entries should be joined with related entries based on the specified criteria.  Allowed values include ''dn:'' followed by the name of an attribute in the source entry containing the DNs of the entries with which to join; ''reverse-dn:'' followed by the name of an attribute in the target entries whose value is the DN of the source entry; ''equals:'' followed by the name of an attribute in the source entry, a colon, and the name of an attribute in target entries that must exactly match the source attribute; or ''contains:'' followed by the name of an attribute in the source entry, a colon, and the name of an attribute in target entries that must match or contain the value of the source attribute."),



  /**
   * Specifies the scope to use for searches used to join search result entries with related entries.  The value may be one of ''base'', ''one'', ''sub'', or ''subordinates''.  If this is not specified, then the scope of the search request will be used as the join scope.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_JOIN_SCOPE("Specifies the scope to use for searches used to join search result entries with related entries.  The value may be one of ''base'', ''one'', ''sub'', or ''subordinates''.  If this is not specified, then the scope of the search request will be used as the join scope."),



  /**
   * Specifies the maximum number of entries that the server will permit to be joined with any single search result entry.  If this is not provided, the size limit from the search request will be used.  Note that the server will impose a maximum join size limit of 1000 entries, so any join size limit greater than that will be limited to 1000.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_JOIN_SIZE_LIMIT("Specifies the maximum number of entries that the server will permit to be joined with any single search result entry.  If this is not provided, the size limit from the search request will be used.  Note that the server will impose a maximum join size limit of 1000 entries, so any join size limit greater than that will be limited to 1000."),



  /**
   * Specifies the path to a file containing LDAP URLs that define the search requests to issue.  The LDAP URLs will specify the base DN, scope, filter, and attributes to return for each search (any hostnames and port numbers included in the URLs will be ignored).  Each URL should be on a separate line.  Blank lines and lines beginning with the ''#'' character will be ignored.  This argument may be provided multiple times to specify multiple LDAP URL files.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_LDAP_URL_FILE("Specifies the path to a file containing LDAP URLs that define the search requests to issue.  The LDAP URLs will specify the base DN, scope, filter, and attributes to return for each search (any hostnames and port numbers included in the URLs will be ignored).  Each URL should be on a separate line.  Blank lines and lines beginning with the ''#'' character will be ignored.  This argument may be provided multiple times to specify multiple LDAP URL files."),



  /**
   * Specifies the LDAP protocol version that the tool should use when communicating with the directory server.  This argument has no effect and is provided only for the purpose of compatibility with other ldapsearch tools.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_LDAP_VERSION("Specifies the LDAP protocol version that the tool should use when communicating with the directory server.  This argument has no effect and is provided only for the purpose of compatibility with other ldapsearch tools."),



  /**
   * Indicates that all search requests should include the manageDsaIT request control to indicate that any referral entries in the scope of the search should be treated as regular entries rather than causing the server to send search result references.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_MANAGE_DSA_IT("Indicates that all search requests should include the manageDsaIT request control to indicate that any referral entries in the scope of the search should be treated as regular entries rather than causing the server to send search result references."),



  /**
   * Indicates that all search requests should include the matched values request control (as described in RFC 3876) to indicate that search result entries should only include values for a given attribute that match the provided filter.  This argument may be provided multiple times to specify multiple matched values filters.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_MATCHED_VALUES_FILTER("Indicates that all search requests should include the matched values request control (as described in RFC 3876) to indicate that search result entries should only include values for a given attribute that match the provided filter.  This argument may be provided multiple times to specify multiple matched values filters."),



  /**
   * Indicates that all search requests should include the UnboundID-proprietary matching entry count request control, which indicates that the server should return information about the number of entries that match the search criteria.  The maximum number of entries to examine must be specified, which helps indicate whether an exact count or an estimate will be returned.  If alwaysExamine is specified and the number of candidates is less than the examine count, then each candidate will be examined to verify that it matches the criteria and would actually be returned to the client in a search.  If allowUnindexed is specified, then the count will be allowed to be processed even if the search is unindexed (and may take a very long time to complete).  If debug is specified, then additional debug information may be included in the output.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_MATCHING_ENTRY_COUNT_CONTROL("Indicates that all search requests should include the UnboundID-proprietary matching entry count request control, which indicates that the server should return information about the number of entries that match the search criteria.  The maximum number of entries to examine must be specified, which helps indicate whether an exact count or an estimate will be returned.  If alwaysExamine is specified and the number of candidates is less than the examine count, then each candidate will be examined to verify that it matches the criteria and would actually be returned to the client in a search.  If allowUnindexed is specified, then the count will be allowed to be processed even if the search is unindexed (and may take a very long time to complete).  If debug is specified, then additional debug information may be included in the output."),



  /**
   * Specifies the base DN for a subtree to be moved to another location in the DIT, with this source DN being replaced with the base DN specified using the --moveSubtreeTo argument.  This argument may be provided multiple times as long as the --moveSubtreeTo argument is also provided the same number of times, and the order of --moveSubtreeFrom values must correspond to the order of --moveSubtreeTo values.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_MOVE_SUBTREE_FROM("Specifies the base DN for a subtree to be moved to another location in the DIT, with this source DN being replaced with the base DN specified using the --moveSubtreeTo argument.  This argument may be provided multiple times as long as the --moveSubtreeTo argument is also provided the same number of times, and the order of --moveSubtreeFrom values must correspond to the order of --moveSubtreeTo values."),



  /**
   * Specifies the new base DN for a subtree to be moved.  This argument must be provided the same number of times as the --moveSubtreeFrom argument.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_MOVE_SUBTREE_TO("Specifies the new base DN for a subtree to be moved.  This argument must be provided the same number of times as the --moveSubtreeFrom argument."),



  /**
   * Indicates that all search requests should include the UnboundID-proprietary operation purpose request control to provide the specified reason for the operation.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_OPERATION_PURPOSE("Indicates that all search requests should include the UnboundID-proprietary operation purpose request control to provide the specified reason for the operation."),



  /**
   * Specifies the path to the file to which search results should be written.  If this argument is not provided then results will be written to standard output.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_OUTPUT_FILE("Specifies the path to the file to which search results should be written.  If this argument is not provided then results will be written to standard output."),



  /**
   * Specifies the format that should be used for the output generated by this tool.  Allowed values are ''LDIF'' (LDAP Data Interchange Format, which is the standard string representation for LDAP data), ''JSON'' (JavaScript Object Notation, which is a popular format used by web services), ''CSV'' (comma-separated values, which is a commonly-used format for text processing, with only a single value per attribute), ''multi-valued-csv'' (comma-separated values with a vertical bar between values of multivalued attributes), ''tab-delimited'' (another commonly-used general text format, with only a single value per attribute), ''multi-valued-tab-delimited'' (tab-delimited text with a vertical bar between values of multivalued attributes), ''dsn-only'' (in which only the DN of each matching entry will be written on a line by itself with no information about the entry''s attributes), and ''values-only'' (in which each value returned will be written on a line by itself with no attribute names, entry DNs, or delimiters between entries).  If either the ''CSV'' or ''tab-delimited'' format is selected, the set of requested attributes must be provided with the ''{0}'' argument, the order in which the attributes are provided on the command line specifies the order in which they will be listed in the output, and if any of those attributes has multiple values then only the first value will be used.  Further, the ''CSV'' and ''tab-delimited'' formats cannot be used in conjunction with the ''{1}'' argument.  If no output format is specified, a default of ''LDIF'' will be used.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_OUTPUT_FORMAT("Specifies the format that should be used for the output generated by this tool.  Allowed values are ''LDIF'' (LDAP Data Interchange Format, which is the standard string representation for LDAP data), ''JSON'' (JavaScript Object Notation, which is a popular format used by web services), ''CSV'' (comma-separated values, which is a commonly-used format for text processing, with only a single value per attribute), ''multi-valued-csv'' (comma-separated values with a vertical bar between values of multivalued attributes), ''tab-delimited'' (another commonly-used general text format, with only a single value per attribute), ''multi-valued-tab-delimited'' (tab-delimited text with a vertical bar between values of multivalued attributes), ''dsn-only'' (in which only the DN of each matching entry will be written on a line by itself with no information about the entry''s attributes), and ''values-only'' (in which each value returned will be written on a line by itself with no attribute names, entry DNs, or delimiters between entries).  If either the ''CSV'' or ''tab-delimited'' format is selected, the set of requested attributes must be provided with the ''{0}'' argument, the order in which the attributes are provided on the command line specifies the order in which they will be listed in the output, and if any of those attributes has multiple values then only the first value will be used.  Further, the ''CSV'' and ''tab-delimited'' formats cannot be used in conjunction with the ''{1}'' argument.  If no output format is specified, a default of ''LDIF'' will be used."),



  /**
   * Indicates that search operations should include the override search limits request control with the specified name-value pair.  This may be provided multiple times to specify multiple property name-value pairs to include in the control.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_OVERRIDE_SEARCH_LIMIT("Indicates that search operations should include the override search limits request control with the specified name-value pair.  This may be provided multiple times to specify multiple property name-value pairs to include in the control."),



  /**
   * Indicates that all search requests should include the simple paged results control (as described in RFC 2696) to indicate that the search should return entries in pages of no more than the specified size.  This can be useful for searches that must return a large number of entries but the server restricts the number of entries that may be returned for any search.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_PAGE_SIZE("Indicates that all search requests should include the simple paged results control (as described in RFC 2696) to indicate that the search should return entries in pages of no more than the specified size.  This can be useful for searches that must return a large number of entries but the server restricts the number of entries that may be returned for any search."),



  /**
   * Indicates that bind requests should include the password policy request control (as defined in draft-behera-ldap-password-policy-10) to request that the response include password policy-related information about the target entry.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_PASSWORD_POLICY("Indicates that bind requests should include the password policy request control (as defined in draft-behera-ldap-password-policy-10) to request that the response include password policy-related information about the target entry."),



  /**
   * Indicates that all search requests should include the UnboundID-proprietary permit unindexed search request control to indicate that the server should process the search operation even if it cannot do so efficiently using server indexes.  The requester must have either the unindexed-search or unindexed-search-with-control privilege.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_PERMIT_UNINDEXED_SEARCH("Indicates that all search requests should include the UnboundID-proprietary permit unindexed search request control to indicate that the server should process the search operation even if it cannot do so efficiently using server indexes.  The requester must have either the unindexed-search or unindexed-search-with-control privilege."),



  /**
   * Indicates that the search request should include the persistent search request control (as described in draft-ietf-ldapext-psearch) to indicate that the server should return information about changes to entries that match the search criteria as they are processed.  This argument may only be used when processing a single search operation.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_PERSISTENT_SEARCH("Indicates that the search request should include the persistent search request control (as described in draft-ietf-ldapext-psearch) to indicate that the server should return information about changes to entries that match the search criteria as they are processed.  This argument may only be used when processing a single search operation."),



  /**
   * Indicates that all search requests should include the proxied authorization request control (as described in RFC 4370) to process the operation under an alternate authorization identity.  The authorization ID should generally be specified in the form ''dn:'' followed by the target user''s DN, or ''u:'' followed by the username.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_PROXY_AS("Indicates that all search requests should include the proxied authorization request control (as described in RFC 4370) to process the operation under an alternate authorization identity.  The authorization ID should generally be specified in the form ''dn:'' followed by the target user''s DN, or ''u:'' followed by the username."),



  /**
   * Indicates that all search requests should include the legacy proxied authorization v1 request control (as described in draft-weltman-ldapv3-proxy-04) to process the search under an alternate authorization identity, specified as the DN of the desired user.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_PROXY_V1_AS("Indicates that all search requests should include the legacy proxied authorization v1 request control (as described in draft-weltman-ldapv3-proxy-04) to process the search under an alternate authorization identity, specified as the DN of the desired user."),



  /**
   * Specifies a maximum search rate that the tool should be permitted to achieve.  Note that this limit applies only to the rate at which the client issues search requests and not to the rate at which the server may send matching entries.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_RATE_PER_SECOND("Specifies a maximum search rate that the tool should be permitted to achieve.  Note that this limit applies only to the rate at which the client issues search requests and not to the rate at which the server may send matching entries."),



  /**
   * Indicates that all search requests should include the UnboundID-proprietary real attributes only request control to indicate that the server should not include any virtual attributes in entries that are returned.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_REAL_ATTRS_ONLY("Indicates that all search requests should include the UnboundID-proprietary real attributes only request control to indicate that the server should not include any virtual attributes in entries that are returned."),



  /**
   * Specifies the name or OID of an attribute whose values should be redacted to indicate that the attribute is present in search result entries but to hide the actual values for that attribute.  This argument may be provided multiple times to specify multiple attributes to redact.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_REDACT_ATTRIBUTE("Specifies the name or OID of an attribute whose values should be redacted to indicate that the attribute is present in search result entries but to hide the actual values for that attribute.  This argument may be provided multiple times to specify multiple attributes to redact."),



  /**
   * Indicates that all search requests should include the UnboundID-proprietary reject unindexed search request control to indicate that the server should not process the search operation if it cannot do so efficiently using server indexes, even if the requester has the unindexed-search privilege.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_REJECT_UNINDEXED_SEARCH("Indicates that all search requests should include the UnboundID-proprietary reject unindexed search request control to indicate that the server should not process the search operation if it cannot do so efficiently using server indexes, even if the requester has the unindexed-search privilege."),



  /**
   * Specifies the name or OID of an attribute that should have its name replaced with the value specified in the --renameAttributeTo argument.  This argument may be provided multiple times as long as the --renameAttributeTo argument is also provided the same number of times, and the order of --renameAttributeFrom values must correspond to the order of --renameAttributeTo values.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_RENAME_ATTRIBUTE_FROM("Specifies the name or OID of an attribute that should have its name replaced with the value specified in the --renameAttributeTo argument.  This argument may be provided multiple times as long as the --renameAttributeTo argument is also provided the same number of times, and the order of --renameAttributeFrom values must correspond to the order of --renameAttributeTo values."),



  /**
   * Specifies the new name to use for an attribute to be renamed.  This argument must be provided the same number of times as the --renameAttributeFrom argument.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_RENAME_ATTRIBUTE_TO("Specifies the new name to use for an attribute to be renamed.  This argument must be provided the same number of times as the --renameAttributeFrom argument."),



  /**
   * Specifies an identifier that indicates which attribute(s) should be included in entries that match the search criteria.  The value may be an attribute name or OID, a special token like ''*'' to indicate all user attributes or ''+'' to indicate all operational attributes, or an object class name prefixed by an ''@'' symbol to indicate all attributes associated with the specified object class.  This may be provided multiple times to specify multiple requested attributes, and it may be provided instead of or in addition to the set of requested attributes in the set of trailing arguments.  If this is not specified, then the server will behave as if all user attributes had been requested.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_REQUESTED_ATTR("Specifies an identifier that indicates which attribute(s) should be included in entries that match the search criteria.  The value may be an attribute name or OID, a special token like ''*'' to indicate all user attributes or ''+'' to indicate all operational attributes, or an object class name prefixed by an ''@'' symbol to indicate all attributes associated with the specified object class.  This may be provided multiple times to specify multiple requested attributes, and it may be provided instead of or in addition to the set of requested attributes in the set of trailing arguments.  If this is not specified, then the server will behave as if all user attributes had been requested."),



  /**
   * Indicates that {0} should exit with a nonzero result code, {1}, if the search completes successfully but does not return any matching entries.  This argument only affects the tool exit code; it will not have any visible effect on the output.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_REQUIRE_MATCH("Indicates that {0} should exit with a nonzero result code, {1}, if the search completes successfully but does not return any matching entries.  This argument only affects the tool exit code; it will not have any visible effect on the output."),



  /**
   * Indicates that, if a search fails in a way that indicates the connection to the server may no longer be valid, the tool should automatically create a new connection and re-try the search before reporting it as a failure.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_RETRY_FAILED_OPERATIONS("Indicates that, if a search fails in a way that indicates the connection to the server may no longer be valid, the tool should automatically create a new connection and re-try the search before reporting it as a failure."),



  /**
   * Specifies the ID of an entry-balancing backend set to which the Directory Proxy Server should send all of the search requests.  The value should be formatted as the entry-balancing request processor ID followed by a colon and the desired backend set ID for that entry-balancing request processor.  This argument can be provided multiple times to specify multiple backend set IDs for the same or different entry-balancing request processors.  The request control will be configured to use absolute routing rather than a routing hint.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_ROUTE_TO_BACKEND_SET("Specifies the ID of an entry-balancing backend set to which the Directory Proxy Server should send all of the search requests.  The value should be formatted as the entry-balancing request processor ID followed by a colon and the desired backend set ID for that entry-balancing request processor.  This argument can be provided multiple times to specify multiple backend set IDs for the same or different entry-balancing request processors.  The request control will be configured to use absolute routing rather than a routing hint."),



  /**
   * Specifies the ID of the backend server to which the Directory Proxy Server should send all search requests.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_ROUTE_TO_SERVER("Specifies the ID of the backend server to which the Directory Proxy Server should send all search requests."),



  /**
   * Specifies the scope that to use for search requests.  The value should be one of ''base'', ''one'', ''sub'', or ''subordinates''.  If this argument is not provided, a default of ''sub'' will be used.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_SCOPE("Specifies the scope that to use for search requests.  The value should be one of ''base'', ''one'', ''sub'', or ''subordinates''.  If this argument is not provided, a default of ''sub'' will be used."),



  /**
   * Specifies the name or OID of an attribute whose values should be scrambled.  Scrambling will be performed in a manner that attempts to preserve the associated attribute syntax and that will generally try to ensure that a given input value will consistently yield the same scrambled output.  This argument may be provided multiple times to specify multiple attributes to scramble.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_SCRAMBLE_ATTRIBUTE("Specifies the name or OID of an attribute whose values should be scrambled.  Scrambling will be performed in a manner that attempts to preserve the associated attribute syntax and that will generally try to ensure that a given input value will consistently yield the same scrambled output.  This argument may be provided multiple times to specify multiple attributes to scramble."),



  /**
   * Specifies the name of a JSON field whose values should be scrambled.  If the --scrambleAttribute argument is used to scramble any attributes whose values may be JSON objects, then all JSON field names will be preserved and only the values of the specified fields will be scrambled.  If this argument is given (and it may be provided multiple times to target multiple JSON fields), then only the specified fields will have their values scrambled.  If this argument is not provided, then any of the scramble attribute values that are JSON objects will have all values scrambled.  JSON field names will be treated in a case-insensitive manner.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_SCRAMBLE_JSON_FIELD("Specifies the name of a JSON field whose values should be scrambled.  If the --scrambleAttribute argument is used to scramble any attributes whose values may be JSON objects, then all JSON field names will be preserved and only the values of the specified fields will be scrambled.  If this argument is given (and it may be provided multiple times to target multiple JSON fields), then only the specified fields will have their values scrambled.  If this argument is not provided, then any of the scramble attribute values that are JSON objects will have all values scrambled.  JSON field names will be treated in a case-insensitive manner."),



  /**
   * Specifies the value that will be used to seed the random number generator used in the course of scrambling attribute values.  If a random seed is provided, then scrambling the same entry with the same seed should consistently yield the same scrambled representations.  If no random seed is specified, an appropriate value will be selected automatically.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_SCRAMBLE_RANDOM_SEED("Specifies the value that will be used to seed the random number generator used in the course of scrambling attribute values.  If a random seed is provided, then scrambling the same entry with the same seed should consistently yield the same scrambled representations.  If no random seed is specified, an appropriate value will be selected automatically."),



  /**
   * Indicates that the tool should operate in script-friendly mode.  This argument has no effect and is provided only for the purpose of compatibility with other ldapsearch tools.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_SCRIPT_FRIENDLY("Indicates that the tool should operate in script-friendly mode.  This argument has no effect and is provided only for the purpose of compatibility with other ldapsearch tools."),



  /**
   * Specifies a control that should be included in all search requests sent to the server.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_SEARCH_CONTROL("Specifies a control that should be included in all search requests sent to the server."),



  /**
   * Indicates that the tool should generate a separate output file for each search rather than combining all results into a single file.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_SEPARATE_OUTPUT_FILES("Indicates that the tool should generate a separate output file for each search rather than combining all results into a single file."),



  /**
   * Specifies the maximum number of entries that the server should return for each search.  A value of zero (which is the default if this argument is not used) indicates that no client-side size limit should be imposed.  Note that the server may impose its own limit on the number of entries that may be returned for a search.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_SIZE_LIMIT("Specifies the maximum number of entries that the server should return for each search.  A value of zero (which is the default if this argument is not used) indicates that no client-side size limit should be imposed.  Note that the server may impose its own limit on the number of entries that may be returned for a search."),



  /**
   * Indicates that all search requests should include the server-side sort request control (as described in RFC 2891) to request that the server sort results before returning them to the client.  The sort order should be a comma-separated list of attribute names, each of which may be optionally prefixed by ''+'' (to indicate that sorting should be in ascending order for that attribute) or ''-'' (for descending order), and may be optionally followed by a colon and the name or OID for the ordering matching rule that should be used when sorting.  Ascending order will be used if neither ''+'' or ''-'' is specified, and if no matching rule ID is given then the attribute type''s own ordering rule will be used.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_SORT_ORDER("Indicates that all search requests should include the server-side sort request control (as described in RFC 2891) to request that the server sort results before returning them to the client.  The sort order should be a comma-separated list of attribute names, each of which may be optionally prefixed by ''+'' (to indicate that sorting should be in ascending order for that attribute) or ''-'' (for descending order), and may be optionally followed by a colon and the name or OID for the ordering matching rule that should be used when sorting.  Ascending order will be used if neither ''+'' or ''-'' is specified, and if no matching rule ID is given then the attribute type''s own ordering rule will be used."),



  /**
   * Indicates that the tool should not include any comments that attempt to provide a human-readable representation of any base64-encoded attribute values in the search results.  If this argument is not provided, then any attribute value whose LDIF representation requires base64 encoding will be immediately followed by a comment that attempts to provide a human-readable representation of the raw bytes that comprise that base64-encoded value.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_SUPPRESS_BASE64_COMMENTS("Indicates that the tool should not include any comments that attempt to provide a human-readable representation of any base64-encoded attribute values in the search results.  If this argument is not provided, then any attribute value whose LDIF representation requires base64 encoding will be immediately followed by a comment that attempts to provide a human-readable representation of the raw bytes that comprise that base64-encoded value."),



  /**
   * Indicates that all operations should include the UnboundID-proprietary suppress operational attribute updates request control to indicate that the server should not apply any updates to the specified operational attributes.  The value may be one of ''last-access-time'', ''last-login-time'', ''last-login-ip'', or ''lastmod''.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_SUPPRESS_OP_ATTR_UPDATES("Indicates that all operations should include the UnboundID-proprietary suppress operational attribute updates request control to indicate that the server should not apply any updates to the specified operational attributes.  The value may be one of ''last-access-time'', ''last-login-time'', ''last-login-ip'', or ''lastmod''."),



  /**
   * Indicates that search results should be written to standard output as well as to the output file specified via the ''{0}'' argument.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_TEE("Indicates that search results should be written to standard output as well as to the output file specified via the ''{0}'' argument."),



  /**
   * Indicates that the tool should generate terse output.  The only thing written to standard output will be search result entries and references, without any summary messages.  Standard error will not be affected.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_TERSE("Indicates that the tool should generate terse output.  The only thing written to standard output will be search result entries and references, without any summary messages.  Standard error will not be affected."),



  /**
   * Specifies the maximum length of time in seconds that the server should spend processing each search.  A value of zero (which is the default if this argument is not provided) indicates that no client-side time limit should be imposed.  Note that the server may impose its own time limit for search requests.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_TIME_LIMIT("Specifies the maximum length of time in seconds that the server should spend processing each search.  A value of zero (which is the default if this argument is not provided) indicates that no client-side time limit should be imposed.  Note that the server may impose its own time limit for search requests."),



  /**
   * Indicates that the server should only include the names of the attributes contained in the entry rather than both names and values.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_TYPES_ONLY("Indicates that the server should only include the names of the attributes contained in the entry rather than both names and values."),



  /**
   * Indicates that the tool should attempt to use an administrative session to process all operations using a dedicated pool of worker threads.  This may be useful when trying to diagnose problems in a server that is unresponsive because all normal worker threads are busy processing other requests.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_USE_ADMIN_SESSION("Indicates that the tool should attempt to use an administrative session to process all operations using a dedicated pool of worker threads.  This may be useful when trying to diagnose problems in a server that is unresponsive because all normal worker threads are busy processing other requests."),



  /**
   * Indicates that the tool should generate verbose output.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_VERBOSE("Indicates that the tool should generate verbose output."),



  /**
   * Indicates that all search requests should include the UnboundID-proprietary virtual attributes only request control to indicate that the server should only include virtual attributes in entries that are returned.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_VIRTUAL_ATTRS_ONLY("Indicates that all search requests should include the UnboundID-proprietary virtual attributes only request control to indicate that the server should only include virtual attributes in entries that are returned."),



  /**
   * Indicates that all search requests should include the virtual list view (VLV) request control (as described in draft-ietf-ldapext-ldapv3-vlv) to indicate that the server should return the specified subset of the sorted search results (and the ''{0}'' argument must also be given to specify the sort order).  The value should be a colon-delimited list indicating which page of results to return, and it may take one of two forms.  In either case, the first element specifies the number of elements to return before the entry identified as the start of the results, and the second is the number of entries after the ''start'' entry.  The third element identifies the start of the result set, and it may be either an integer offset (in which the first entry in the result set has an offset of one), or a string that provides a value for which the server should identify the first entry whose value for the primary sort attribute is greater than or equal to the given string.  In the event that an offset is provided, a fourth element must also be given to indicate the expected number of entries in the result set, or zero if that is not known.  For example, a value of ''0:9:1:0'' indicates that the server should return the first ten entries of the result set (starting at offset 1, which is the first entry, return the zero previous entries and the nine following entries, with no indication of how many entries match the search criteria).  Alternately, a value of ''0:99:smith'' indicates that the server should the first 100 entries in the result set for which the primary sort attribute has a value that is greater than or equal to ''smith''.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_VLV("Indicates that all search requests should include the virtual list view (VLV) request control (as described in draft-ietf-ldapext-ldapv3-vlv) to indicate that the server should return the specified subset of the sorted search results (and the ''{0}'' argument must also be given to specify the sort order).  The value should be a colon-delimited list indicating which page of results to return, and it may take one of two forms.  In either case, the first element specifies the number of elements to return before the entry identified as the start of the results, and the second is the number of entries after the ''start'' entry.  The third element identifies the start of the result set, and it may be either an integer offset (in which the first entry in the result set has an offset of one), or a string that provides a value for which the server should identify the first entry whose value for the primary sort attribute is greater than or equal to the given string.  In the event that an offset is provided, a fourth element must also be given to indicate the expected number of entries in the result set, or zero if that is not known.  For example, a value of ''0:9:1:0'' indicates that the server should return the first ten entries of the result set (starting at offset 1, which is the first entry, return the zero previous entries and the nine following entries, with no indication of how many entries match the search criteria).  Alternately, a value of ''0:99:smith'' indicates that the server should the first 100 entries in the result set for which the primary sort attribute has a value that is greater than or equal to ''smith''."),



  /**
   * The column at which long lines in the LDIF representation of an entry should be wrapped.  A value of zero indicates that no wrapping should be performed.  If this is not provided, then the wrap column will be based on the width of the terminal used to run the tool.
   */
  INFO_LDAPSEARCH_ARG_DESCRIPTION_WRAP_COLUMN("The column at which long lines in the LDIF representation of an entry should be wrapped.  A value of zero indicates that no wrapping should be performed.  If this is not provided, then the wrap column will be based on the width of the terminal used to run the tool."),



  /**
   * Control Arguments
   */
  INFO_LDAPSEARCH_ARG_GROUP_CONTROLS("Control Arguments"),



  /**
   * Data Arguments
   */
  INFO_LDAPSEARCH_ARG_GROUP_DATA("Data Arguments"),



  /**
   * Operation Arguments
   */
  INFO_LDAPSEARCH_ARG_GROUP_OPS("Operation Arguments"),



  /**
   * Entry Transformation Arguments
   */
  INFO_LDAPSEARCH_ARG_GROUP_TRANSFORMATIONS("Entry Transformation Arguments"),



  /**
   * '{'returnOnlySubentries'}'
   */
  INFO_LDAPSEARCH_ARG_PLACEHOLDER_INCLUDE_RFC_3672_SUBENTRIES("'{'returnOnlySubentries'}'"),



  /**
   * '{'entry-balancing-processor-id:backend-set-id'}'
   */
  INFO_LDAPSEARCH_ARG_PLACEHOLDER_ROUTE_TO_BACKEND_SET("'{'entry-balancing-processor-id:backend-set-id'}'"),



  /**
   * '{'id'}'
   */
  INFO_LDAPSEARCH_ARG_PLACEHOLDER_ROUTE_TO_SERVER("'{'id'}'"),



  /**
   * No search operation was actually processed because the {0} argument was provided.  The request that would have been sent is:  {1}
   */
  INFO_LDAPSEARCH_DRY_RUN_REQUEST_NOT_SENT("No search operation was actually processed because the {0} argument was provided.  The request that would have been sent is:  {1}"),



  /**
   * Establishes an unencrypted LDAP connection to directory.example.com:389, performs a simple bind to authenticate as user ''uid=jdoe,ou=People,dc=example,dc=com'', and issues a search request to retrieve the givenName, sn, and mail attributes for the user with uid ''jqpublic'' below dc=example,dc=com.  The search results will be written to standard output.
   */
  INFO_LDAPSEARCH_EXAMPLE_1("Establishes an unencrypted LDAP connection to directory.example.com:389, performs a simple bind to authenticate as user ''uid=jdoe,ou=People,dc=example,dc=com'', and issues a search request to retrieve the givenName, sn, and mail attributes for the user with uid ''jqpublic'' below dc=example,dc=com.  The search results will be written to standard output."),



  /**
   * Establishes an SSL-encrypted LDAP connection to directory.example.com:636, interactively prompting the user about whether to trust the certificate presented by the directory server.  The tool will then bind with the SASL PLAIN mechanism using an authentication ID of ''u:jdoe'' and a password read from a file.  It will then issue a search request for each filter in a given file, writing the results for each search into a separate output file.
   */
  INFO_LDAPSEARCH_EXAMPLE_2("Establishes an SSL-encrypted LDAP connection to directory.example.com:636, interactively prompting the user about whether to trust the certificate presented by the directory server.  The tool will then bind with the SASL PLAIN mechanism using an authentication ID of ''u:jdoe'' and a password read from a file.  It will then issue a search request for each filter in a given file, writing the results for each search into a separate output file."),



  /**
   * Establishes an LDAP connection to directory.example.com:389 that is secured with the StartTLS extended operation, using the information in the provided trust store file to determine whether to trust the certificate presented by the directory server.  It will then issue an unauthenticated search to retrieve all user and operational attributes from the server''s root DSE.  The output will be written to a specified output file as well as displayed on standard output.
   */
  INFO_LDAPSEARCH_EXAMPLE_3("Establishes an LDAP connection to directory.example.com:389 that is secured with the StartTLS extended operation, using the information in the provided trust store file to determine whether to trust the certificate presented by the directory server.  It will then issue an unauthenticated search to retrieve all user and operational attributes from the server''s root DSE.  The output will be written to a specified output file as well as displayed on standard output."),



  /**
   * Issues a search request to retrieve all entries at or below ''dc=example,dc=com'', using the simple paged results control to retrieve up to 100 entries at a time.  The search will use an unencrypted LDAP connection, and the tool will interactively prompt the user for the password to use when performing simple authentication.
   */
  INFO_LDAPSEARCH_EXAMPLE_4("Issues a search request to retrieve all entries at or below ''dc=example,dc=com'', using the simple paged results control to retrieve up to 100 entries at a time.  The search will use an unencrypted LDAP connection, and the tool will interactively prompt the user for the password to use when performing simple authentication."),



  /**
   * Issues a search request to retrieve a special entry that provides details about the server''s use of indexes to determine the candidate set of potential matching entries.  This feature is only supported in the UnboundID/Ping Identity Directory Server, and the user must have access control rights to retrieve the ''cn=debugsearch'' entry and the ''debugsearchindex'' operational attribute.
   */
  INFO_LDAPSEARCH_EXAMPLE_5("Issues a search request to retrieve a special entry that provides details about the server''s use of indexes to determine the candidate set of potential matching entries.  This feature is only supported in the UnboundID/Ping Identity Directory Server, and the user must have access control rights to retrieve the ''cn=debugsearch'' entry and the ''debugsearchindex'' operational attribute."),



  /**
   * Intermediate search result with more pages to retrieve:
   */
  INFO_LDAPSEARCH_INTERMEDIATE_PAGED_SEARCH_RESULT("Intermediate search result with more pages to retrieve:"),



  /**
   * '{'name=value'}'
   */
  INFO_LDAPSEARCH_NAME_VALUE_PLACEHOLDER("'{'name=value'}'"),



  /**
   * Sending search request {0}
   */
  INFO_LDAPSEARCH_SENDING_SEARCH_REQUEST("Sending search request {0}"),



  /**
   * Process one or more searches in an LDAP directory server.
   */
  INFO_LDAPSEARCH_TOOL_DESCRIPTION("Process one or more searches in an LDAP directory server."),



  /**
   * Total Entries Returned:  {0,number,0}
   */
  INFO_LDAPSEARCH_TOTAL_SEARCH_ENTRIES("Total Entries Returned:  {0,number,0}"),



  /**
   * Total References Returned:  {0,number,0}
   */
  INFO_LDAPSEARCH_TOTAL_SEARCH_REFERENCES("Total References Returned:  {0,number,0}"),



  /**
   * '{'filter'}' ['{'attr1'}' ['{'attr2'}' ...]]
   */
  INFO_LDAPSEARCH_TRAILING_ARGS_PLACEHOLDER("'{'filter'}' ['{'attr1'}' ['{'attr2'}' ...]]"),



  /**
   * List result codes in alphabetic order rather than in numeric order.
   */
  INFO_LDAP_RC_ARG_DESC_ALPHABETIC("List result codes in alphabetic order rather than in numeric order."),



  /**
   * Retrieve the result code with the specified integer value.
   */
  INFO_LDAP_RC_ARG_DESC_INT_VALUE("Retrieve the result code with the specified integer value."),



  /**
   * List all defined result codes.
   */
  INFO_LDAP_RC_ARG_DESC_LIST("List all defined result codes."),



  /**
   * The format to use for the output.  The value may be one of ''csv'', ''json'', ''tab-delimited'', or ''table''.  If the output format is not specified, then the ''table'' format will be used by default.
   */
  INFO_LDAP_RC_ARG_DESC_OUTPUT_FORMAT("The format to use for the output.  The value may be one of ''csv'', ''json'', ''tab-delimited'', or ''table''.  If the output format is not specified, then the ''table'' format will be used by default."),



  /**
   * Format the output as tab-delimited text rather than as a table.
   */
  INFO_LDAP_RC_ARG_DESC_SCRIPT_FRIENDLY("Format the output as tab-delimited text rather than as a table."),



  /**
   * Search for result codes whose name contains the given substring.
   */
  INFO_LDAP_RC_ARG_DESC_SEARCH("Search for result codes whose name contains the given substring."),



  /**
   * '{'intValue'}'
   */
  INFO_LDAP_RC_ARG_PLACEHOLDER_INT_VALUE("'{'intValue'}'"),



  /**
   * '{'csv|json|tab-delimited|table'}'
   */
  INFO_LDAP_RC_ARG_PLACEHOLDER_OUTPUT_FORMAT("'{'csv|json|tab-delimited|table'}'"),



  /**
   * '{'searchString'}'
   */
  INFO_LDAP_RC_ARG_PLACEHOLDER_SEARCH_STRING("'{'searchString'}'"),



  /**
   * Display a table with all defined LDAP result codes.
   */
  INFO_LDAP_RC_EXAMPLE_1("Display a table with all defined LDAP result codes."),



  /**
   * Display information about the result code with an integer value of ''49''.  The matching result code should be formatted as a JSON object.
   */
  INFO_LDAP_RC_EXAMPLE_2("Display information about the result code with an integer value of ''49''.  The matching result code should be formatted as a JSON object."),



  /**
   * Display information about any defined result codes whose name contains the substring ''attribute''.
   */
  INFO_LDAP_RC_EXAMPLE_3("Display information about any defined result codes whose name contains the substring ''attribute''."),



  /**
   * Integer Value
   */
  INFO_LDAP_RC_INT_VALUE_LABEL("Integer Value"),



  /**
   * Result Code Name
   */
  INFO_LDAP_RC_NAME_LABEL("Result Code Name"),



  /**
   * Display and query LDAP result codes.
   */
  INFO_LDAP_RC_TOOL_DESC_1("Display and query LDAP result codes."),



  /**
   * This tool may be used to list all known defined LDAP result codes, retrieve the name of the result code with a given integer value, or search for all result codes with names containing a given substring.
   */
  INFO_LDAP_RC_TOOL_DESC_2("This tool may be used to list all known defined LDAP result codes, retrieve the name of the result code with a given integer value, or search for all result codes with names containing a given substring."),



  /**
   * At most one of the --list, --int-value, and --search arguments may be provided.  If none of them is provided, then the --list option will be chosen by default.
   */
  INFO_LDAP_RC_TOOL_DESC_3("At most one of the --list, --int-value, and --search arguments may be provided.  If none of them is provided, then the --list option will be chosen by default."),



  /**
   * Indicates that the tool should append to the file specified by the {0} argument if it already exists.  If this argument is not provided and the reject file already exists, it will be overwritten.
   */
  INFO_MANAGE_ACCT_ARG_DESC_APPEND_TO_REJECT_FILE("Indicates that the tool should append to the file specified by the {0} argument if it already exists.  If this argument is not provided and the reject file already exists, it will be overwritten."),



  /**
   * The base DN to use when searching for user entries when using any of the {0}, {1}, {2}, or {3} arguments.  If this argument is not provided, the default base DN will be the null DN.
   */
  INFO_MANAGE_ACCT_ARG_DESC_BASE_DN("The base DN to use when searching for user entries when using any of the {0}, {1}, {2}, or {3} arguments.  If this argument is not provided, the default base DN will be the null DN."),



  /**
   * The path to a file containing the DNs of the user entries on which to operate.  The DN file must contain one DN per line, and blank lines and lines starting with an octothorpe (#) will be ignored.  This argument may be provided multiple times to use multiple DN files.
   */
  INFO_MANAGE_ACCT_ARG_DESC_DN_FILE("The path to a file containing the DNs of the user entries on which to operate.  The DN file must contain one DN per line, and blank lines and lines starting with an octothorpe (#) will be ignored.  This argument may be provided multiple times to use multiple DN files."),



  /**
   * The path to a file containing a set of LDAP filters to use to identify the user entries on which to operate.  The {0} argument may be used to specify the base DN for the searches.  The filter file must contain one filter per line, and blank lines and lines starting with an octothorpe (#) will be ignored.  This argument may be provided multiple times to use multiple filter files.
   */
  INFO_MANAGE_ACCT_ARG_DESC_FILTER_INPUT_FILE("The path to a file containing a set of LDAP filters to use to identify the user entries on which to operate.  The {0} argument may be used to specify the base DN for the searches.  The filter file must contain one filter per line, and blank lines and lines starting with an octothorpe (#) will be ignored.  This argument may be provided multiple times to use multiple filter files."),



  /**
   * The path to a file to create with a sample variable rate definition.  This file will contain comments that describe the expected format for the file to use with the {0} argument.
   */
  INFO_MANAGE_ACCT_ARG_DESC_GENERATE_SAMPLE_RATE_FILE("The path to a file to create with a sample variable rate definition.  This file will contain comments that describe the expected format for the file to use with the {0} argument."),



  /**
   * The number of concurrent threads to use when searching for entries to target with {0} operations.  This will only be used if arguments are provided that require the tool to perform searches to identify which entries to target (and will primarily be useful only in cases in which there will be multiple searches, as when reading filters or user IDs from a file).  If this argument is not provided, a default value of one will be used.
   */
  INFO_MANAGE_ACCT_ARG_DESC_NUM_SEARCH_THREADS("The number of concurrent threads to use when searching for entries to target with {0} operations.  This will only be used if arguments are provided that require the tool to perform searches to identify which entries to target (and will primarily be useful only in cases in which there will be multiple searches, as when reading filters or user IDs from a file).  If this argument is not provided, a default value of one will be used."),



  /**
   * The number of concurrent threads to use when performing {0} operations against user entries.  If this argument is not provided, a default value of one will be used.
   */
  INFO_MANAGE_ACCT_ARG_DESC_NUM_THREADS("The number of concurrent threads to use when performing {0} operations against user entries.  If this argument is not provided, a default value of one will be used."),



  /**
   * The maximum rate, in operations per second, at which to target user entries.  If neither this nor the {0} argument is provided, then the tool will not perform any rate limiting.
   */
  INFO_MANAGE_ACCT_ARG_DESC_RATE_PER_SECOND("The maximum rate, in operations per second, at which to target user entries.  If neither this nor the {0} argument is provided, then the tool will not perform any rate limiting."),



  /**
   * The path to a file to write with a record of any unsuccessful attempts to retrieve or update account information for a user.
   */
  INFO_MANAGE_ACCT_ARG_DESC_REJECT_FILE("The path to a file to write with a record of any unsuccessful attempts to retrieve or update account information for a user."),



  /**
   * Indicates that any searches performed to identify which users to target should make use of the simple paged results control to cause the entries to be returned in sets of no more than the specified number of entries.  When processing searches that may return a large number of entries, setting a page size can help reduce the likelihood that the server will need to block while sending results to the client if matching entries are identified much more quickly than the client can process the desired {0} operations against those entries.  Further, when performing searches that may return a large number of entries, and especially for unindexed searches, setting a page size can help the client more efficiently resume processing in the event that the connection used to process the search becomes unusable.
   */
  INFO_MANAGE_ACCT_ARG_DESC_SIMPLE_PAGE_SIZE("Indicates that any searches performed to identify which users to target should make use of the simple paged results control to cause the entries to be returned in sets of no more than the specified number of entries.  When processing searches that may return a large number of entries, setting a page size can help reduce the likelihood that the server will need to block while sending results to the client if matching entries are identified much more quickly than the client can process the desired {0} operations against those entries.  Further, when performing searches that may return a large number of entries, and especially for unindexed searches, setting a page size can help the client more efficiently resume processing in the event that the connection used to process the search becomes unusable."),



  /**
   * Indicates that the result information for each {0} operation processed should suppress information about password policy state operations included in the result that do not have any values.  Operations presented without values generally indicated that the user''s password policy is not configured for the associated functionality (e.g., if the password expiration time was requested but password expiration is not enabled in the user''s password policy).
   */
  INFO_MANAGE_ACCT_ARG_DESC_SUPPRESS_EMPTY_RESULT_OPERATIONS("Indicates that the result information for each {0} operation processed should suppress information about password policy state operations included in the result that do not have any values.  Operations presented without values generally indicated that the user''s password policy is not configured for the associated functionality (e.g., if the password expiration time was requested but password expiration is not enabled in the user''s password policy)."),



  /**
   * The DN of the user entry on which to operate.  This argument may be provided multiple times to specify multiple user DNs.
   */
  INFO_MANAGE_ACCT_ARG_DESC_TARGET_DN("The DN of the user entry on which to operate.  This argument may be provided multiple times to specify multiple user DNs."),



  /**
   * An LDAP filter that may be used to identify the user entries on which to operate.  The {0} argument may be used to specify the base DN for the search.  This argument may be provided multiple times to specify multiple target filters.
   */
  INFO_MANAGE_ACCT_ARG_DESC_TARGET_FILTER("An LDAP filter that may be used to identify the user entries on which to operate.  The {0} argument may be used to specify the base DN for the search.  This argument may be provided multiple times to specify multiple target filters."),



  /**
   * A string that identifies a user on which to operate.  The tool will search for the user with a base DN specified by the {0} argument and a user ID attribute specified by the {1} argument.  This argument may be provided multiple times to specify multiple target user IDs.
   */
  INFO_MANAGE_ACCT_ARG_DESC_TARGET_USER_ID("A string that identifies a user on which to operate.  The tool will search for the user with a base DN specified by the {0} argument and a user ID attribute specified by the {1} argument.  This argument may be provided multiple times to specify multiple target user IDs."),



  /**
   * The name or OID of the attribute used to identify users with IDs specified using the {0} and {1} arguments.  If this argument is not provided, a default user ID attribute of {2} will be used.
   */
  INFO_MANAGE_ACCT_ARG_DESC_USER_ID_ATTR("The name or OID of the attribute used to identify users with IDs specified using the {0} and {1} arguments.  If this argument is not provided, a default user ID attribute of {2} will be used."),



  /**
   * The path to a file containing a set of user ID values.  The file must contain one user ID per line, and blank lines and lines starting with an octothorpe (#) will be ignored.  The tool will search for the users with a base DN specified by the {0} argument and a user ID attribute specified by the {1} argument.  This argument may be provided multiple times to use multiple user ID files.
   */
  INFO_MANAGE_ACCT_ARG_DESC_USER_ID_INPUT_FILE("The path to a file containing a set of user ID values.  The file must contain one user ID per line, and blank lines and lines starting with an octothorpe (#) will be ignored.  The tool will search for the users with a base DN specified by the {0} argument and a user ID attribute specified by the {1} argument.  This argument may be provided multiple times to use multiple user ID files."),



  /**
   * The path to a file containing a variable rate definition that may be used to cause the tool to vary the amount of load it generates over time.  If neither this nor the {0} argument is provided, then the tool will not perform any rate limiting.
   */
  INFO_MANAGE_ACCT_ARG_DESC_VARIABLE_RATE_DATA("The path to a file containing a variable rate definition that may be used to cause the tool to vary the amount of load it generates over time.  If neither this nor the {0} argument is provided, then the tool will not perform any rate limiting."),



  /**
   * Arguments for Adjusting Performance
   */
  INFO_MANAGE_ACCT_ARG_GROUP_PERFORMANCE("Arguments for Adjusting Performance"),



  /**
   * Arguments for Identifying Target Users
   */
  INFO_MANAGE_ACCT_ARG_GROUP_TARGET_USER_ARGS("Arguments for Identifying Target Users"),



  /**
   * A timestamp for an authentication failure time to add to the user''s entry.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  This argument may be provided multiple times to specify multiple authentication failure times.  If this argument is not provided, the current time will be added to the set of authentication failure times.
   */
  INFO_MANAGE_ACCT_SC_ADD_AUTH_FAILURE_TIME_ARG_VALUE("A timestamp for an authentication failure time to add to the user''s entry.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  This argument may be provided multiple times to specify multiple authentication failure times.  If this argument is not provided, the current time will be added to the set of authentication failure times."),



  /**
   * Adds the current time to the set of authentication failure times for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_ADD_AUTH_FAILURE_TIME_EXAMPLE("Adds the current time to the set of authentication failure times for user ''{0}''."),



  /**
   * A timestamp for a grace login use time to add to the user''s entry.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  This argument may be provided multiple times to specify multiple grace login use times.  If this argument is not provided, the current time will be used.
   */
  INFO_MANAGE_ACCT_SC_ADD_GRACE_LOGIN_TIME_ARG_VALUE("A timestamp for a grace login use time to add to the user''s entry.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  This argument may be provided multiple times to specify multiple grace login use times.  If this argument is not provided, the current time will be used."),



  /**
   * Adds the current time to the set of grace login use times for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_ADD_GRACE_LOGIN_TIME_EXAMPLE("Adds the current time to the set of grace login use times for user ''{0}''."),



  /**
   * The TOTP shared secret to register for a user.  This argument must be provided at least once, but may be provided multiple times to specify multiple TOTP shared secrets to register.
   */
  INFO_MANAGE_ACCT_SC_ADD_TOTP_SHARED_SECRET_ARG_VALUE("The TOTP shared secret to register for a user.  This argument must be provided at least once, but may be provided multiple times to specify multiple TOTP shared secrets to register."),



  /**
   * Adds value ''{0}'' to the set of TOTP shared secrets for user ''{1}''.
   */
  INFO_MANAGE_ACCT_SC_ADD_TOTP_SHARED_SECRET_EXAMPLE("Adds value ''{0}'' to the set of TOTP shared secrets for user ''{1}''."),



  /**
   * The public ID for a YubiKey OTP device to register for a user.  This argument must be provided at least once, but may be provided multiple times to specify multiple public IDs to register.
   */
  INFO_MANAGE_ACCT_SC_ADD_YUBIKEY_ID_ARG_VALUE("The public ID for a YubiKey OTP device to register for a user.  This argument must be provided at least once, but may be provided multiple times to specify multiple public IDs to register."),



  /**
   * Adds value ''{0}'' to the set of registered YubiKey OTP public IDs for user ''{1}''.
   */
  INFO_MANAGE_ACCT_SC_ADD_YUBIKEY_ID_EXAMPLE("Adds value ''{0}'' to the set of registered YubiKey OTP public IDs for user ''{1}''."),



  /**
   * Clear the account activation time for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_CLEAR_ACCT_ACT_TIME_EXAMPLE("Clear the account activation time for user ''{0}''."),



  /**
   * Clear the account expiration time for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_CLEAR_ACCT_EXP_TIME_EXAMPLE("Clear the account expiration time for user ''{0}''."),



  /**
   * Clears the authentication failure times for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_CLEAR_AUTH_FAILURE_TIMES_EXAMPLE("Clears the authentication failure times for user ''{0}''."),



  /**
   * Clears the set of grace login use times for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_CLEAR_GRACE_LOGIN_TIMES_EXAMPLE("Clears the set of grace login use times for user ''{0}''."),



  /**
   * Clear the account disabled state information for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_CLEAR_IS_DISABLED_EXAMPLE("Clear the account disabled state information for user ''{0}''."),



  /**
   * Clears the time the server last invoked password validators during a bind operation for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_CLEAR_LAST_BIND_PW_VALIDATION_TIME_EXAMPLE("Clears the time the server last invoked password validators during a bind operation for user ''{0}''."),



  /**
   * Clears the last login IP address for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_CLEAR_LAST_LOGIN_IP_EXAMPLE("Clears the last login IP address for user ''{0}''."),



  /**
   * Clears the last login time for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_CLEAR_LAST_LOGIN_TIME_EXAMPLE("Clears the last login time for user ''{0}''."),



  /**
   * Clears the password reset state information from the account for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_CLEAR_MUST_CHANGE_PW_EXAMPLE("Clears the password reset state information from the account for user ''{0}''."),



  /**
   * Clears the record of the most recent required change time with which user ''{0}'' complied.
   */
  INFO_MANAGE_ACCT_SC_CLEAR_PW_CHANGED_BY_REQ_TIME_EXAMPLE("Clears the record of the most recent required change time with which user ''{0}'' complied."),



  /**
   * Clear the password changed time for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_CLEAR_PW_CHANGED_TIME_EXAMPLE("Clear the password changed time for user ''{0}''."),



  /**
   * Clear the password expiration warned time for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_CLEAR_PW_EXP_WARNED_TIME_EXAMPLE("Clear the password expiration warned time for user ''{0}''."),



  /**
   * Clears the password history for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_CLEAR_PW_HISTORY_EXAMPLE("Clears the password history for user ''{0}''."),



  /**
   * Clears the recent login history for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_CLEAR_RECENT_LOGIN_HISTORY_EXAMPLE("Clears the recent login history for user ''{0}''."),



  /**
   * Clears the set of TOTP shared secrets registered for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_CLEAR_TOTP_SHARED_SECRETS_EXAMPLE("Clears the set of TOTP shared secrets registered for user ''{0}''."),



  /**
   * Clears the list of the public IDs of the YubiKey OTP devices registered for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_CLEAR_YUBIKEY_IDS_EXAMPLE("Clears the list of the public IDs of the YubiKey OTP devices registered for user ''{0}''."),



  /**
   * Adds one or more new values to the set of authentication failure times for a user.  If the resulting set of authentication failure times has reached the configured lockout failure count, the user''s account will be locked.  This will have no effect if failure lockout is not enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_ADD_AUTH_FAILURE_TIME("Adds one or more new values to the set of authentication failure times for a user.  If the resulting set of authentication failure times has reached the configured lockout failure count, the user''s account will be locked.  This will have no effect if failure lockout is not enabled in the user''s password policy."),



  /**
   * Adds one or more new values to the set of grace login use times for a user.  This will have no effect unless both password expiration and grace login support are configured in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_ADD_GRACE_LOGIN_TIME("Adds one or more new values to the set of grace login use times for a user.  This will have no effect unless both password expiration and grace login support are configured in the user''s password policy."),



  /**
   * Adds a value to the set of TOTP shared secrets for a user.
   */
  INFO_MANAGE_ACCT_SC_DESC_ADD_TOTP_SHARED_SECRET("Adds a value to the set of TOTP shared secrets for a user."),



  /**
   * Adds a value to the set of registered YubiKey OTP public IDs for a user.
   */
  INFO_MANAGE_ACCT_SC_DESC_ADD_YUBIKEY_ID("Adds a value to the set of registered YubiKey OTP public IDs for a user."),



  /**
   * Clear the account activation time for a user.  If the account previously had an activation time in the future, this will make it immediately eligible for use.  This can also be accomplished with a standard LDAP modify operation by removing the ds-pwp-account-activation-time attribute from the user''s entry.
   */
  INFO_MANAGE_ACCT_SC_DESC_CLEAR_ACCT_ACT_TIME("Clear the account activation time for a user.  If the account previously had an activation time in the future, this will make it immediately eligible for use.  This can also be accomplished with a standard LDAP modify operation by removing the ds-pwp-account-activation-time attribute from the user''s entry."),



  /**
   * Clear the account expiration time for a user.  If the account previously had an expiration time in the past, this will make it immediately eligible for use.  This can also be accomplished with a standard LDAP modify operation by removing the ds-pwp-account-expiration-time attribute from the user''s entry.
   */
  INFO_MANAGE_ACCT_SC_DESC_CLEAR_ACCT_EXP_TIME("Clear the account expiration time for a user.  If the account previously had an expiration time in the past, this will make it immediately eligible for use.  This can also be accomplished with a standard LDAP modify operation by removing the ds-pwp-account-expiration-time attribute from the user''s entry."),



  /**
   * Clears the set of authentication failure times for a user.  If the user''s account had been locked be of too many failed authentication attempts, this will also clear that lockout.
   */
  INFO_MANAGE_ACCT_SC_DESC_CLEAR_AUTH_FAILURE_TIMES("Clears the set of authentication failure times for a user.  If the user''s account had been locked be of too many failed authentication attempts, this will also clear that lockout."),



  /**
   * Clears the set of grace login use times for a user.  This will have no effect unless both password expiration and grace login support are configured in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_CLEAR_GRACE_LOGIN_TIMES("Clears the set of grace login use times for a user.  This will have no effect unless both password expiration and grace login support are configured in the user''s password policy."),



  /**
   * Clear the account disabled state information from a user entry, which is logically equivalent to using the {0} subcommand with an {1} value of false.  This can also be accomplished with a standard LDAP modify operation by removing the ds-pwp-account-disabled operational attribute from the user''s entry.
   */
  INFO_MANAGE_ACCT_SC_DESC_CLEAR_IS_DISABLED("Clear the account disabled state information from a user entry, which is logically equivalent to using the {0} subcommand with an {1} value of false.  This can also be accomplished with a standard LDAP modify operation by removing the ds-pwp-account-disabled operational attribute from the user''s entry."),



  /**
   * Clears the time the server last invoked password validators during a bind operation for a user.
   */
  INFO_MANAGE_ACCT_SC_DESC_CLEAR_LAST_BIND_PW_VALIDATION_TIME("Clears the time the server last invoked password validators during a bind operation for a user."),



  /**
   * Clears the last login IP address from a user''s entry.  This will have no effect if last login IP address tracking is not enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_CLEAR_LAST_LOGIN_IP("Clears the last login IP address from a user''s entry.  This will have no effect if last login IP address tracking is not enabled in the user''s password policy."),



  /**
   * Clears the last login time from a user''s entry.  This will have no effect if last login time tracking is not enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_CLEAR_LAST_LOGIN_TIME("Clears the last login time from a user''s entry.  This will have no effect if last login time tracking is not enabled in the user''s password policy."),



  /**
   * Clears the password reset state information from a user''s account.  If the account had previously been locked because the user failed to choose a new password in a timely manner after an administrative reset, that lockout will be lifted.  This will have no effect if neither force change on add nor force change on reset is enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_CLEAR_MUST_CHANGE_PW("Clears the password reset state information from a user''s account.  If the account had previously been locked because the user failed to choose a new password in a timely manner after an administrative reset, that lockout will be lifted.  This will have no effect if neither force change on add nor force change on reset is enabled in the user''s password policy."),



  /**
   * Clears the record of the most recent required change time with which a user has complied.  This will not have any effect unless a required change time is configured in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_CLEAR_PW_CHANGED_BY_REQ_TIME("Clears the record of the most recent required change time with which a user has complied.  This will not have any effect unless a required change time is configured in the user''s password policy."),



  /**
   * Clear the password changed time value for a user.  For password policy evaluations that require knowing when the user''s password was last changed, the server will attempt to fall back to using the create timestamp, if available.
   */
  INFO_MANAGE_ACCT_SC_DESC_CLEAR_PW_CHANGED_TIME("Clear the password changed time value for a user.  For password policy evaluations that require knowing when the user''s password was last changed, the server will attempt to fall back to using the create timestamp, if available."),



  /**
   * Clear a record of any password expiration warning from a user''s entry.
   */
  INFO_MANAGE_ACCT_SC_DESC_CLEAR_PW_EXP_WARNED_TIME("Clear a record of any password expiration warning from a user''s entry."),



  /**
   * Clears the password history for a user.  This will have no effect if the password history is not enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_CLEAR_PW_HISTORY("Clears the password history for a user.  This will have no effect if the password history is not enabled in the user''s password policy."),



  /**
   * Clears the user''s recent login history.
   */
  INFO_MANAGE_ACCT_SC_DESC_CLEAR_RECENT_LOGIN_HISTORY("Clears the user''s recent login history."),



  /**
   * Clears the set of TOTP shared secrets registered for a user.
   */
  INFO_MANAGE_ACCT_SC_DESC_CLEAR_TOTP_SHARED_SECRETS("Clears the set of TOTP shared secrets registered for a user."),



  /**
   * Clears the list of the public IDs of the YubiKey OTP devices registered for a user.
   */
  INFO_MANAGE_ACCT_SC_DESC_CLEAR_YUBIKEY_IDS("Clears the list of the public IDs of the YubiKey OTP devices registered for a user."),



  /**
   * Retrieve the account activation time for a user.  If the activation time is in the future, the user cannot authenticate or be used as an alternate authorization identity.  This can also be accomplished with a standard LDAP search operation by retrieving the ds-pwp-account-activation-time operational attribute from the user''s entry.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_ACCT_ACT_TIME("Retrieve the account activation time for a user.  If the activation time is in the future, the user cannot authenticate or be used as an alternate authorization identity.  This can also be accomplished with a standard LDAP search operation by retrieving the ds-pwp-account-activation-time operational attribute from the user''s entry."),



  /**
   * Retrieve the account expiration time for a user.  If the expiration time is in the past, the user cannot authenticate or be used as an alternate authorization identity.  This can also be accomplished with a standard LDAP search operation by retrieving the ds-pwp-account-expiration-time operational attribute from the user''s entry.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_ACCT_EXP_TIME("Retrieve the account expiration time for a user.  If the expiration time is in the past, the user cannot authenticate or be used as an alternate authorization identity.  This can also be accomplished with a standard LDAP search operation by retrieving the ds-pwp-account-expiration-time operational attribute from the user''s entry."),



  /**
   * Determines whether a user''s account is locked as a result of too many failed authentication attempts.  This will only be available if failure lockout is enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_ACCT_FAILURE_LOCKED("Determines whether a user''s account is locked as a result of too many failed authentication attempts.  This will only be available if failure lockout is enabled in the user''s password policy."),



  /**
   * Determines whether a user''s account is locked because it has been too long since the user last authenticated.  This will only be available if idle lockout is enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_ACCT_IDLE_LOCKED("Determines whether a user''s account is locked because it has been too long since the user last authenticated.  This will only be available if idle lockout is enabled in the user''s password policy."),



  /**
   * Determine whether a user account has an expiration time in the past and therefore cannot authenticate or be used as an alternate authorization identity.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_ACCT_IS_EXPIRED("Determine whether a user account has an expiration time in the past and therefore cannot authenticate or be used as an alternate authorization identity."),



  /**
   * Determines whether the user''s account is locked because the user failed to choose a new password in a timely manner after an administrative reset.  This will only be available if either force change on add or force change on reset is enabled in the user''s password policy, and if a maximum password reset age is defined in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_ACCT_IS_RESET_LOCKED("Determines whether the user''s account is locked because the user failed to choose a new password in a timely manner after an administrative reset.  This will only be available if either force change on add or force change on reset is enabled in the user''s password policy, and if a maximum password reset age is defined in the user''s password policy."),



  /**
   * Determine whether a user account has an activation time in the future and therefore cannot authenticate or be used as an alternate authorization identity.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_ACCT_NOT_YET_ACTIVE("Determine whether a user account has an activation time in the future and therefore cannot authenticate or be used as an alternate authorization identity."),



  /**
   * Determines whether a user''s account is locked because it contains a password that does not satisfy all of the configured password validators.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_ACCT_VALIDATION_LOCKED("Determines whether a user''s account is locked because it contains a password that does not satisfy all of the configured password validators."),



  /**
   * Retrieve all available state information for a user.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_ALL("Retrieve all available state information for a user."),



  /**
   * Retrieves the timestamps for any failed authentication attempts for a user since the user''s last successful authentication.  This will only be available if failure lockout is enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_AUTH_FAILURE_TIMES("Retrieves the timestamps for any failed authentication attempts for a user since the user''s last successful authentication.  This will only be available if failure lockout is enabled in the user''s password policy."),



  /**
   * Retrieves a list of the one-time password delivery mechanisms that are available for a user.  If the user''s entry includes information about which OTP delivery mechanisms are preferred, then the values will be returned in order of most preferred to least preferred.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_AVAILABLE_OTP_MECHS("Retrieves a list of the one-time password delivery mechanisms that are available for a user.  If the user''s entry includes information about which OTP delivery mechanisms are preferred, then the values will be returned in order of most preferred to least preferred."),



  /**
   * Retrieves a list of the SASL mechanisms that are available for a user.  This will take into account the server configuration, the types of credentials the user has, and the authentication constraints defined for the user.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_AVAILABLE_SASL_MECHS("Retrieves a list of the SASL mechanisms that are available for a user.  This will take into account the server configuration, the types of credentials the user has, and the authentication constraints defined for the user."),



  /**
   * Retrieves the time that a user''s account was locked as a result of too many failed authentication attempts.  This will only be available if failure lockout is enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_FAILURE_LOCKED_TIME("Retrieves the time that a user''s account was locked as a result of too many failed authentication attempts.  This will only be available if failure lockout is enabled in the user''s password policy."),



  /**
   * Retrieves the times that a user has authenticated with grace logins after their password had expired.  This will only be available if both password expiration and grace login supports are configured in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_GRACE_LOGIN_TIMES("Retrieves the times that a user has authenticated with grace logins after their password had expired.  This will only be available if both password expiration and grace login supports are configured in the user''s password policy."),



  /**
   * Determines whether a user has an active retired password.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_HAS_RETIRED_PW("Determines whether a user has an active retired password."),



  /**
   * Determines whether a user has one static password.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_HAS_STATIC_PW("Determines whether a user has one static password."),



  /**
   * Determines whether a user has at least one TOTP shared secret that may be used in conjunction with the UNBOUNDID-TOTP SASL mechanism or the Verify TOTP extended operation.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_HAS_TOTP_SHARED_SECRET("Determines whether a user has at least one TOTP shared secret that may be used in conjunction with the UNBOUNDID-TOTP SASL mechanism or the Verify TOTP extended operation."),



  /**
   * Determines whether a user has at least one registered YubiKey OTP device public ID.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_HAS_YUBIKEY_ID("Determines whether a user has at least one registered YubiKey OTP device public ID."),



  /**
   * Retrieves the time at which the user''s account was locked because it had been too long since the user last authenticated, or the time it will be locked unless the user authenticates before that time.  This will only be available if idle lockout is enabled in the user''s password policy
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_IDLE_LOCKOUT_TIME("Retrieves the time at which the user''s account was locked because it had been too long since the user last authenticated, or the time it will be locked unless the user authenticates before that time.  This will only be available if idle lockout is enabled in the user''s password policy"),



  /**
   * Determine whether a user account has been disabled by an administrator and cannot authenticate or be used as an alternate authorization identity.  This can also be accomplished with a standard LDAP search operation by determining whether the user entry has a ds-pwp-account-disabled operational attribute with a value of true.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_IS_DISABLED("Determine whether a user account has been disabled by an administrator and cannot authenticate or be used as an alternate authorization identity.  This can also be accomplished with a standard LDAP search operation by determining whether the user entry has a ds-pwp-account-disabled operational attribute with a value of true."),



  /**
   * Determine whether a user account will be allowed to authenticate or be used as an alternate authorization identity.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_IS_USABLE("Determine whether a user account will be allowed to authenticate or be used as an alternate authorization identity."),



  /**
   * Retrieves the time the server last invoked password validators during a bind operation for a user.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_LAST_BIND_PW_VALIDATION_TIME("Retrieves the time the server last invoked password validators during a bind operation for a user."),



  /**
   * Retrieves the IP address of the client from which a user last authenticated.  This will only be available if last login IP address tracking is enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_LAST_LOGIN_IP("Retrieves the IP address of the client from which a user last authenticated.  This will only be available if last login IP address tracking is enabled in the user''s password policy."),



  /**
   * Retrieves the time that a user last authenticated.  This will only be available if last login time tracking is enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_LAST_LOGIN_TIME("Retrieves the time that a user last authenticated.  This will only be available if last login time tracking is enabled in the user''s password policy."),



  /**
   * Determines whether a user''s password has been reset by an administrator and the user must choose a new password before they will be allowed to perform any other operations.  This will only be available if force change on add or force change on reset is enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_MUST_CHANGE_PW("Determines whether a user''s password has been reset by an administrator and the user must choose a new password before they will be allowed to perform any other operations.  This will only be available if force change on add or force change on reset is enabled in the user''s password policy."),



  /**
   * Retrieve the DN of the password policy that governs a user.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_POLICY_DN("Retrieve the DN of the password policy that governs a user."),



  /**
   * Retrieves the most recent required change time with which a user has complied.  This will only be available if a required change time is configured in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_PW_CHANGED_BY_REQ_TIME("Retrieves the most recent required change time with which a user has complied.  This will only be available if a required change time is configured in the user''s password policy."),



  /**
   * Retrieve the time that a user''s password was last changed, whether via a self change or an administrative reset.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_PW_CHANGED_TIME("Retrieve the time that a user''s password was last changed, whether via a self change or an administrative reset."),



  /**
   * Retrieves the time that a user''s password will expire.  This will only be available if password expiration is enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_PW_EXP_TIME("Retrieves the time that a user''s password will expire.  This will only be available if password expiration is enabled in the user''s password policy."),



  /**
   * Retrieve the time that a user received the first warning about an upcoming password expiration.  This will only be available if password expiration is enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_PW_EXP_WARNED_TIME("Retrieve the time that a user received the first warning about an upcoming password expiration.  This will only be available if password expiration is enabled in the user''s password policy."),



  /**
   * Retrieves the number of passwords in a user''s password history.  This will only be available if the password history is enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_PW_HISTORY_COUNT("Retrieves the number of passwords in a user''s password history.  This will only be available if the password history is enabled in the user''s password policy."),



  /**
   * Determine whether a user account has an expired password and therefore cannot authenticate or be used as an alternate authorization identity.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_PW_IS_EXPIRED("Determine whether a user account has an expired password and therefore cannot authenticate or be used as an alternate authorization identity."),



  /**
   * Retrieves the time that a user''s former password was retired.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_PW_RETIRED_TIME("Retrieves the time that a user''s former password was retired."),



  /**
   * Retrieves the user''s recent login history.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_RECENT_LOGIN_HISTORY("Retrieves the user''s recent login history."),



  /**
   * Retrieves the number of additional failed authentication attempts that will be required to lock a user''s account.  This will only be available if failure lockout is enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_REMAINING_FAILURE_COUNT("Retrieves the number of additional failed authentication attempts that will be required to lock a user''s account.  This will only be available if failure lockout is enabled in the user''s password policy."),



  /**
   * Retrieves the number of additional grace logins that will be available to a user before they will be unable to authenticate with an expired password.  This will only be available if both password expiration and grace login support are enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_REMAINING_GRACE_LOGIN_COUNT("Retrieves the number of additional grace logins that will be available to a user before they will be unable to authenticate with an expired password.  This will only be available if both password expiration and grace login support are enabled in the user''s password policy."),



  /**
   * Retrieves the time that a user''s account was or will be locked for failing to choose a new password in a timely manner after an administrative reset.  This will only be available if either force change on add or force change on reset is enabled in the user''s password policy, and if a maximum password reset age is defined in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_RESET_LOCKOUT_TIME("Retrieves the time that a user''s account was or will be locked for failing to choose a new password in a timely manner after an administrative reset.  This will only be available if either force change on add or force change on reset is enabled in the user''s password policy, and if a maximum password reset age is defined in the user''s password policy."),



  /**
   * Retrieves the time that a user''s retired password will expire.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_RETIRED_PW_EXP_TIME("Retrieves the time that a user''s retired password will expire."),



  /**
   * Retrieves the length of time in seconds since the server last invoked password validators during a bind operation for a user.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_SECONDS_SINCE_LAST_BIND_PW_VALIDATION("Retrieves the length of time in seconds since the server last invoked password validators during a bind operation for a user."),



  /**
   * Retrieve the length of time in seconds until a user''s account is eligible for use.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_SECONDS_UNTIL_ACCT_ACT("Retrieve the length of time in seconds until a user''s account is eligible for use."),



  /**
   * Retrieve the length of time in seconds until a user''s account expires.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_SECONDS_UNTIL_ACCT_EXP("Retrieve the length of time in seconds until a user''s account expires."),



  /**
   * Retrieves the length of time in seconds until a user''s temporary failure lockout expires.  This will only be available if failure lockout is enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_SECONDS_UNTIL_FAILURE_UNLOCK("Retrieves the length of time in seconds until a user''s temporary failure lockout expires.  This will only be available if failure lockout is enabled in the user''s password policy."),



  /**
   * Retrieves the length of time in seconds until the user''s account will be locked because it has been too long since the user last authenticated.  This will only be available if idle lockout is enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_SECONDS_UNTIL_IDLE_LOCKOUT("Retrieves the length of time in seconds until the user''s account will be locked because it has been too long since the user last authenticated.  This will only be available if idle lockout is enabled in the user''s password policy."),



  /**
   * Retrieves the length of time in seconds until a user''s password will expire.  This will only be available if password expiration is available in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_SECONDS_UNTIL_PW_EXP("Retrieves the length of time in seconds until a user''s password will expire.  This will only be available if password expiration is available in the user''s password policy."),



  /**
   * Retrieve the length of time in seconds until a user will be eligible to start receiving warnings about an upcoming password expiration.  This will only be available if password expiration is enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_SECONDS_UNTIL_PW_EXP_WARNING("Retrieve the length of time in seconds until a user will be eligible to start receiving warnings about an upcoming password expiration.  This will only be available if password expiration is enabled in the user''s password policy."),



  /**
   * Determines the length of time in seconds until the user''s account will be locked unless they choose a new password after an administrative password reset.  This will only be available if either force change on add or force change on reset is enabled in the user''s password policy, and if a maximum password reset age is defined in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_SECONDS_UNTIL_RESET_LOCKOUT("Determines the length of time in seconds until the user''s account will be locked unless they choose a new password after an administrative password reset.  This will only be available if either force change on add or force change on reset is enabled in the user''s password policy, and if a maximum password reset age is defined in the user''s password policy."),



  /**
   * Retrieves the length of time in seconds until the user account is locked for failure to comply with a required change time.  This will only be available if a required change time is configured in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_SECS_UNTIL_REQ_CHANGE_TIME("Retrieves the length of time in seconds until the user account is locked for failure to comply with a required change time.  This will only be available if a required change time is configured in the user''s password policy."),



  /**
   * Retrieve any password policy state account usability error messages for a user.  The messages may provide information about conditions that prevent a user account from authenticating, being used as an alternate authorization identity, or otherwise functioning normally.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_USABILITY_ERRORS("Retrieve any password policy state account usability error messages for a user.  The messages may provide information about conditions that prevent a user account from authenticating, being used as an alternate authorization identity, or otherwise functioning normally."),



  /**
   * Retrieve any password policy state account usability notice messages for a user.  These messages may provide useful information about the state of the user account, but do not necessarily represent a current or imminent problem with the account.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_USABILITY_NOTICES("Retrieve any password policy state account usability notice messages for a user.  These messages may provide useful information about the state of the user account, but do not necessarily represent a current or imminent problem with the account."),



  /**
   * Retrieve any password policy state account usability warning messages for a user.  The messages may provide information about conditions that may leave a user account unusable in the near future unless corrective action is taken.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_USABILITY_WARNINGS("Retrieve any password policy state account usability warning messages for a user.  The messages may provide information about conditions that may leave a user account unusable in the near future unless corrective action is taken."),



  /**
   * Retrieves a list of the public IDs of the YubiKey OTP devices registered for a user.
   */
  INFO_MANAGE_ACCT_SC_DESC_GET_YUBIKEY_IDS("Retrieves a list of the public IDs of the YubiKey OTP devices registered for a user."),



  /**
   * Purges a retired password from a user''s entry so that it may no longer be used to authenticate.
   */
  INFO_MANAGE_ACCT_SC_DESC_PURGE_RETIRED_PW("Purges a retired password from a user''s entry so that it may no longer be used to authenticate."),



  /**
   * Removes a value from the set of TOTP shared secrets for a user.
   */
  INFO_MANAGE_ACCT_SC_DESC_REMOVE_TOTP_SHARED_SECRET("Removes a value from the set of TOTP shared secrets for a user."),



  /**
   * Removes a value from the set of registered YubiKey OTP public IDs for a user.
   */
  INFO_MANAGE_ACCT_SC_DESC_REMOVE_YUBIKEY_ID("Removes a value from the set of registered YubiKey OTP public IDs for a user."),



  /**
   * Set the account activation time for a user.  If the activation time is in the future, the user cannot authenticate or be used as an alternate authorization identity.  This can also be accomplished with a standard LDAP modify operation by setting the ds-pwp-account-activation-time attribute to have a generalized time representation of the desired activation time.
   */
  INFO_MANAGE_ACCT_SC_DESC_SET_ACCT_ACT_TIME("Set the account activation time for a user.  If the activation time is in the future, the user cannot authenticate or be used as an alternate authorization identity.  This can also be accomplished with a standard LDAP modify operation by setting the ds-pwp-account-activation-time attribute to have a generalized time representation of the desired activation time."),



  /**
   * Set the account expiration time for a user.  If the expiration time is in the past, the user cannot authenticate or be used as an alternate authorization identity.  This can also be accomplished with a standard LDAP modify operation by setting the ds-pwp-account-expiration-time attribute to have a generalized time representation of the desired expiration time.
   */
  INFO_MANAGE_ACCT_SC_DESC_SET_ACCT_EXP_TIME("Set the account expiration time for a user.  If the expiration time is in the past, the user cannot authenticate or be used as an alternate authorization identity.  This can also be accomplished with a standard LDAP modify operation by setting the ds-pwp-account-expiration-time attribute to have a generalized time representation of the desired expiration time."),



  /**
   * Specifies whether a user''s account should be locked as a result of too many failed authentication attempts.  This will have no effect if failure lockout is not enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_SET_ACCT_FAILURE_LOCKED("Specifies whether a user''s account should be locked as a result of too many failed authentication attempts.  This will have no effect if failure lockout is not enabled in the user''s password policy."),



  /**
   * Specifies whether a user''s account should be locked because it contains a password that does not satisfy all of the configured password validators.
   */
  INFO_MANAGE_ACCT_SC_DESC_SET_ACCT_VALIDATION_LOCKED("Specifies whether a user''s account should be locked because it contains a password that does not satisfy all of the configured password validators."),



  /**
   * Sets the timestamps for failed authentication attempts for a user.  If the number of authentication failure times provided is greater than or equal to the lockout failure count for the user''s password policy, the user''s account will be locked.  This will have no effect if failure lockout is not enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_SET_AUTH_FAILURE_TIMES("Sets the timestamps for failed authentication attempts for a user.  If the number of authentication failure times provided is greater than or equal to the lockout failure count for the user''s password policy, the user''s account will be locked.  This will have no effect if failure lockout is not enabled in the user''s password policy."),



  /**
   * Replaces the set of grace login use times for a user.  This will have no effect unless both password expiration and grace login support are configured in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_SET_GRACE_LOGIN_TIMES("Replaces the set of grace login use times for a user.  This will have no effect unless both password expiration and grace login support are configured in the user''s password policy."),



  /**
   * Specify whether a user account is administratively disabled.  A disabled account cannot authenticate or be used as an alternate authorization identity.  This can also be accomplished with a standard LDAP modify operation by setting the value of the ds-pwp-account-disabled operational attribute in the user''s entry with a value of either true or false (or by removing the attribute from the user''s entry, which is equivalent to giving it a value of false).
   */
  INFO_MANAGE_ACCT_SC_DESC_SET_IS_DISABLED("Specify whether a user account is administratively disabled.  A disabled account cannot authenticate or be used as an alternate authorization identity.  This can also be accomplished with a standard LDAP modify operation by setting the value of the ds-pwp-account-disabled operational attribute in the user''s entry with a value of either true or false (or by removing the attribute from the user''s entry, which is equivalent to giving it a value of false)."),



  /**
   * Specifies the time the server last invoked password validators during a bind operation for a user.
   */
  INFO_MANAGE_ACCT_SC_DESC_SET_LAST_BIND_PW_VALIDATION_TIME("Specifies the time the server last invoked password validators during a bind operation for a user."),



  /**
   * Specifies the IP address of the client from which a user last authenticated.  This will have no effect if last login IP address tracking is not enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_SET_LAST_LOGIN_IP("Specifies the IP address of the client from which a user last authenticated.  This will have no effect if last login IP address tracking is not enabled in the user''s password policy."),



  /**
   * Specifies the time that a user last authenticated.  This will have no effect if last login time tracking is not enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_SET_LAST_LOGIN_TIME("Specifies the time that a user last authenticated.  This will have no effect if last login time tracking is not enabled in the user''s password policy."),



  /**
   * Specifies whether a user''s password has been reset by an administrator and the user must choose a new password before they will be allowed to perform any other operations.  This will have no effect if neither force change on add nor force change on reset is enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_SET_MUST_CHANGE_PW("Specifies whether a user''s password has been reset by an administrator and the user must choose a new password before they will be allowed to perform any other operations.  This will have no effect if neither force change on add nor force change on reset is enabled in the user''s password policy."),



  /**
   * Specifies the most recent required change time with which a user has complied.  This will not have any effect unless a required change time is configured in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_SET_PW_CHANGED_BY_REQ_TIME("Specifies the most recent required change time with which a user has complied.  This will not have any effect unless a required change time is configured in the user''s password policy."),



  /**
   * Set the password changed time value for a user.
   */
  INFO_MANAGE_ACCT_SC_DESC_SET_PW_CHANGED_TIME("Set the password changed time value for a user."),



  /**
   * Specify the time that a user received the first warning about an upcoming password expiration.  Note that this will not in itself trigger a password expiration warning nor cause any account status notification handlers to be invoked.  This will have no effect if password expiration is not enabled in the user''s password policy.
   */
  INFO_MANAGE_ACCT_SC_DESC_SET_PW_EXP_WARNED_TIME("Specify the time that a user received the first warning about an upcoming password expiration.  Note that this will not in itself trigger a password expiration warning nor cause any account status notification handlers to be invoked.  This will have no effect if password expiration is not enabled in the user''s password policy."),



  /**
   * Replaces the set of TOTP shared secrets registered for a user.
   */
  INFO_MANAGE_ACCT_SC_DESC_SET_TOTP_SHARED_SECRETS("Replaces the set of TOTP shared secrets registered for a user."),



  /**
   * Replaces the list of the public IDs of the YubiKey OTP devices registered for a user.
   */
  INFO_MANAGE_ACCT_SC_DESC_SET_YUBIKEY_IDS("Replaces the list of the public IDs of the YubiKey OTP devices registered for a user."),



  /**
   * Retrieve the account activation time for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_GET_ACCT_ACT_TIME_EXAMPLE("Retrieve the account activation time for user ''{0}''."),



  /**
   * Retrieve the account expiration time for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_GET_ACCT_EXP_TIME_EXAMPLE("Retrieve the account expiration time for user ''{0}''."),



  /**
   * Determines whether the account for user ''{0}'' is locked as a result of too many failed authentication attempts.
   */
  INFO_MANAGE_ACCT_SC_GET_ACCT_FAILURE_LOCKED_EXAMPLE("Determines whether the account for user ''{0}'' is locked as a result of too many failed authentication attempts."),



  /**
   * Determines whether the account for user ''{0}'' is locked because it has been too long since the user last authenticated.
   */
  INFO_MANAGE_ACCT_SC_GET_ACCT_IDLE_LOCKED_EXAMPLE("Determines whether the account for user ''{0}'' is locked because it has been too long since the user last authenticated."),



  /**
   * Determine whether user ''{0}'' has an account expiration time in the past.
   */
  INFO_MANAGE_ACCT_SC_GET_ACCT_IS_EXPIRED_EXAMPLE("Determine whether user ''{0}'' has an account expiration time in the past."),



  /**
   * Determines whether the account for user ''{0}'' is locked because the user failed to choose a new password in a timely manner after an administrative reset.
   */
  INFO_MANAGE_ACCT_SC_GET_ACCT_IS_RESET_LOCKED_EXAMPLE("Determines whether the account for user ''{0}'' is locked because the user failed to choose a new password in a timely manner after an administrative reset."),



  /**
   * Determine whether user ''{0}'' has an account activation time in the future.
   */
  INFO_MANAGE_ACCT_SC_GET_ACCT_NOT_YET_ACTIVE_EXAMPLE("Determine whether user ''{0}'' has an account activation time in the future."),



  /**
   * Determines whether the account for user ''{0}'' is locked because it contains a password that does not satisfy all of the configured password validators.
   */
  INFO_MANAGE_ACCT_SC_GET_ACCT_VALIDATION_LOCKED_EXAMPLE("Determines whether the account for user ''{0}'' is locked because it contains a password that does not satisfy all of the configured password validators."),



  /**
   * Retrieve all available state information for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_GET_ALL_EXAMPLE("Retrieve all available state information for user ''{0}''."),



  /**
   * Retrieves the timestamps for any failed authentication attempts for user ''{0}'' since that user''s last successful authentication.
   */
  INFO_MANAGE_ACCT_SC_GET_AUTH_FAILURE_TIMES_EXAMPLE("Retrieves the timestamps for any failed authentication attempts for user ''{0}'' since that user''s last successful authentication."),



  /**
   * Retrieves a list of the one-time password delivery mechanisms that are available for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_GET_AVAILABLE_OTP_MECHS_EXAMPLE("Retrieves a list of the one-time password delivery mechanisms that are available for user ''{0}''."),



  /**
   * Retrieves a list of the SASL mechanisms that are available for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_GET_AVAILABLE_SASL_MECHS_EXAMPLE("Retrieves a list of the SASL mechanisms that are available for user ''{0}''."),



  /**
   * Retrieves the time that the account for user ''{0}'' was locked as a result of too many failed authentication attempts.
   */
  INFO_MANAGE_ACCT_SC_GET_FAILURE_LOCKED_TIME_EXAMPLE("Retrieves the time that the account for user ''{0}'' was locked as a result of too many failed authentication attempts."),



  /**
   * Retrieves the grace login times for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_GET_GRACE_LOGIN_TIMES_EXAMPLE("Retrieves the grace login times for user ''{0}''."),



  /**
   * Determines whether user ''{0}'' has a retired password.
   */
  INFO_MANAGE_ACCT_SC_GET_HAS_RETIRED_PW_EXAMPLE("Determines whether user ''{0}'' has a retired password."),



  /**
   * Determines whether user ''{0}'' has a static password.
   */
  INFO_MANAGE_ACCT_SC_GET_HAS_STATIC_PW_EXAMPLE("Determines whether user ''{0}'' has a static password."),



  /**
   * Determines whether user ''{0}'' has at least one TOTP shared secret.
   */
  INFO_MANAGE_ACCT_SC_GET_HAS_TOTP_SHARED_SECRET_EXAMPLE("Determines whether user ''{0}'' has at least one TOTP shared secret."),



  /**
   * Determines whether user ''{0}'' has at least one registered YubiKey OTP device public ID.
   */
  INFO_MANAGE_ACCT_SC_GET_HAS_YUBIKEY_ID_EXAMPLE("Determines whether user ''{0}'' has at least one registered YubiKey OTP device public ID."),



  /**
   * Retrieves the time at which the account for user ''{0}'' was locked because it had been too long since the user had last authenticated.
   */
  INFO_MANAGE_ACCT_SC_GET_IDLE_LOCKOUT_TIME_EXAMPLE("Retrieves the time at which the account for user ''{0}'' was locked because it had been too long since the user had last authenticated."),



  /**
   * Determine whether the account for user ''{0}'' has been disabled by an administrator.
   */
  INFO_MANAGE_ACCT_SC_GET_IS_DISABLED_EXAMPLE("Determine whether the account for user ''{0}'' has been disabled by an administrator."),



  /**
   * Determine whether user ''{0}'' will be permitted to authenticate or be used as an alternate authorization identity.
   */
  INFO_MANAGE_ACCT_SC_GET_IS_USABLE_EXAMPLE("Determine whether user ''{0}'' will be permitted to authenticate or be used as an alternate authorization identity."),



  /**
   * Retrieves the time the server last invoked password validators during a bind operation for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_GET_LAST_BIND_PW_VALIDATION_TIME_EXAMPLE("Retrieves the time the server last invoked password validators during a bind operation for user ''{0}''."),



  /**
   * Retrieves the IP address of the client from which user ''{0}'' last authenticated.
   */
  INFO_MANAGE_ACCT_SC_GET_LAST_LOGIN_IP_EXAMPLE("Retrieves the IP address of the client from which user ''{0}'' last authenticated."),



  /**
   * Retrieves the time that user ''{0}'' last authenticated.
   */
  INFO_MANAGE_ACCT_SC_GET_LAST_LOGIN_TIME_EXAMPLE("Retrieves the time that user ''{0}'' last authenticated."),



  /**
   * Determines whether the password for user ''{0}'' has been reset and must be changed before any other operations will be allowed.
   */
  INFO_MANAGE_ACCT_SC_GET_MUST_CHANGE_PW_EXAMPLE("Determines whether the password for user ''{0}'' has been reset and must be changed before any other operations will be allowed."),



  /**
   * Retrieve the DN of the password policy that governs user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_GET_POLICY_DN_EXAMPLE("Retrieve the DN of the password policy that governs user ''{0}''."),



  /**
   * Retrieves the most recent required change time with which user ''{0}'' has complied.
   */
  INFO_MANAGE_ACCT_SC_GET_PW_CHANGED_BY_REQ_TIME_EXAMPLE("Retrieves the most recent required change time with which user ''{0}'' has complied."),



  /**
   * Retrieve the time that the password for user ''{0}'' was last changed.
   */
  INFO_MANAGE_ACCT_SC_GET_PW_CHANGED_TIME_EXAMPLE("Retrieve the time that the password for user ''{0}'' was last changed."),



  /**
   * Retrieves the time that the password for user ''{0}'' will expire.
   */
  INFO_MANAGE_ACCT_SC_GET_PW_EXP_TIME_EXAMPLE("Retrieves the time that the password for user ''{0}'' will expire."),



  /**
   * Retrieve the time that user ''{0}'' received the first warning about an upcoming password expiration.
   */
  INFO_MANAGE_ACCT_SC_GET_PW_EXP_WARNED_TIME_EXAMPLE("Retrieve the time that user ''{0}'' received the first warning about an upcoming password expiration."),



  /**
   * Retrieves the number of passwords in the password history for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_GET_PW_HISTORY_COUNT_EXAMPLE("Retrieves the number of passwords in the password history for user ''{0}''."),



  /**
   * Determine whether user ''{0}'' has an expired password.
   */
  INFO_MANAGE_ACCT_SC_GET_PW_IS_EXPIRED_EXAMPLE("Determine whether user ''{0}'' has an expired password."),



  /**
   * Retrieves the time the former password for user ''{0}'' was retired.
   */
  INFO_MANAGE_ACCT_SC_GET_PW_RETIRED_TIME_EXAMPLE("Retrieves the time the former password for user ''{0}'' was retired."),



  /**
   * Retrieves the recent login history for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_GET_RECENT_LOGIN_HISTORY_EXAMPLE("Retrieves the recent login history for user ''{0}''."),



  /**
   * Retrieves the remaining number of failed authentication attempts that will be required to lock the account for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_GET_REMAINING_FAILURE_COUNT_EXAMPLE("Retrieves the remaining number of failed authentication attempts that will be required to lock the account for user ''{0}''."),



  /**
   * Retrieves the remaining number of grace logins that for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_GET_REMAINING_GRACE_LOGIN_COUNT_EXAMPLE("Retrieves the remaining number of grace logins that for user ''{0}''."),



  /**
   * Retrieves the time that the account for user ''{0}'' was or will be locked for failing to choose a new password in a timely manner after an administrative reset.
   */
  INFO_MANAGE_ACCT_SC_GET_RESET_LOCKOUT_TIME_EXAMPLE("Retrieves the time that the account for user ''{0}'' was or will be locked for failing to choose a new password in a timely manner after an administrative reset."),



  /**
   * Retrieves the time that the retired password for user ''{0}'' will expire.
   */
  INFO_MANAGE_ACCT_SC_GET_RETIRED_PW_EXP_TIME_EXAMPLE("Retrieves the time that the retired password for user ''{0}'' will expire."),



  /**
   * Retrieve the length of time in seconds until user account ''{0}'' will be eligible for use.
   */
  INFO_MANAGE_ACCT_SC_GET_SECONDS_UNTIL_ACCT_ACT_EXAMPLE("Retrieve the length of time in seconds until user account ''{0}'' will be eligible for use."),



  /**
   * Retrieve the length of time in seconds until user account ''{0}'' expires.
   */
  INFO_MANAGE_ACCT_SC_GET_SECONDS_UNTIL_ACCT_EXP_EXAMPLE("Retrieve the length of time in seconds until user account ''{0}'' expires."),



  /**
   * Retrieves the length of time in seconds until the temporary failure lockout for user ''{0}'' expires.
   */
  INFO_MANAGE_ACCT_SC_GET_SECONDS_UNTIL_FAILURE_UNLOCK_EXAMPLE("Retrieves the length of time in seconds until the temporary failure lockout for user ''{0}'' expires."),



  /**
   * Retrieves the length of time until the account for user ''{0}'' will be locked because it has been too long since the user last authenticated.
   */
  INFO_MANAGE_ACCT_SC_GET_SECONDS_UNTIL_IDLE_LOCKOUT_EXAMPLE("Retrieves the length of time until the account for user ''{0}'' will be locked because it has been too long since the user last authenticated."),



  /**
   * Retrieves the length of time in seconds until the password for user ''{0}'' expires.
   */
  INFO_MANAGE_ACCT_SC_GET_SECONDS_UNTIL_PW_EXP_EXAMPLE("Retrieves the length of time in seconds until the password for user ''{0}'' expires."),



  /**
   * Retrieve the length of time in seconds until user ''{0}'' will be eligible to start receiving password expiration warnings.
   */
  INFO_MANAGE_ACCT_SC_GET_SECONDS_UNTIL_PW_EXP_WARNING_EXAMPLE("Retrieve the length of time in seconds until user ''{0}'' will be eligible to start receiving password expiration warnings."),



  /**
   * Determines the length of time in seconds until the account for user ''{0}'' will be locked unless they choose a new password.
   */
  INFO_MANAGE_ACCT_SC_GET_SECONDS_UNTIL_RESET_LOCKOUT_EXAMPLE("Determines the length of time in seconds until the account for user ''{0}'' will be locked unless they choose a new password."),



  /**
   * Retrieves the length of time in seconds since the server last invoked password validators during a bind operation for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_GET_SECS_SINCE_LAST_BIND_PW_VALIDATION_EXAMPLE("Retrieves the length of time in seconds since the server last invoked password validators during a bind operation for user ''{0}''."),



  /**
   * Retrieves the length of time in seconds until the account for user ''{0}'' is locked for failure to comply with a required change time.
   */
  INFO_MANAGE_ACCT_SC_GET_SECS_UNTIL_REQ_CHANGE_TIME_EXAMPLE("Retrieves the length of time in seconds until the account for user ''{0}'' is locked for failure to comply with a required change time."),



  /**
   * Retrieve any password policy state account usability error messages for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_GET_USABILITY_ERRORS_EXAMPLE("Retrieve any password policy state account usability error messages for user ''{0}''."),



  /**
   * Retrieve any password policy state account usability notice messages for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_GET_USABILITY_NOTICES_EXAMPLE("Retrieve any password policy state account usability notice messages for user ''{0}''."),



  /**
   * Retrieve any password policy state account usability warning messages for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_GET_USABILITY_WARNINGS_EXAMPLE("Retrieve any password policy state account usability warning messages for user ''{0}''."),



  /**
   * Retrieves a list of the public IDs of the YubiKey OTP devices registered for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_GET_YUBIKEY_IDS_EXAMPLE("Retrieves a list of the public IDs of the YubiKey OTP devices registered for user ''{0}''."),



  /**
   * Purges a retired password for user ''{0}''.
   */
  INFO_MANAGE_ACCT_SC_PURGE_RETIRED_PW_EXAMPLE("Purges a retired password for user ''{0}''."),



  /**
   * The TOTP shared secret to deregister for a user.  This argument must be provided at least once, but may be provided multiple times to specify multiple TOTP shared secrets to deregister.
   */
  INFO_MANAGE_ACCT_SC_REMOVE_TOTP_SHARED_SECRET_ARG_VALUE("The TOTP shared secret to deregister for a user.  This argument must be provided at least once, but may be provided multiple times to specify multiple TOTP shared secrets to deregister."),



  /**
   * Removes value ''{0}'' from the set of TOTP shared secrets for user ''{1}''.
   */
  INFO_MANAGE_ACCT_SC_REMOVE_TOTP_SHARED_SECRET_EXAMPLE("Removes value ''{0}'' from the set of TOTP shared secrets for user ''{1}''."),



  /**
   * The public ID for a YubiKey OTP device to deregister for a user.  This argument must be provided at least once, but may be provided multiple times to specify multiple public IDs to deregister.
   */
  INFO_MANAGE_ACCT_SC_REMOVE_YUBIKEY_ID_ARG_VALUE("The public ID for a YubiKey OTP device to deregister for a user.  This argument must be provided at least once, but may be provided multiple times to specify multiple public IDs to deregister."),



  /**
   * Removes value ''{0}'' from the set of registered YubiKey OTP public IDs for user ''{1}''.
   */
  INFO_MANAGE_ACCT_SC_REMOVE_YUBIKEY_ID_EXAMPLE("Removes value ''{0}'' from the set of registered YubiKey OTP public IDs for user ''{1}''."),



  /**
   * The new value for the account activation time.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  If this argument is not provided, the account activation time will be set to the current time.
   */
  INFO_MANAGE_ACCT_SC_SET_ACCT_ACT_TIME_ARG_VALUE("The new value for the account activation time.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  If this argument is not provided, the account activation time will be set to the current time."),



  /**
   * Set the account activation time for user ''{0}'' to ''{1}''.
   */
  INFO_MANAGE_ACCT_SC_SET_ACCT_ACT_TIME_EXAMPLE("Set the account activation time for user ''{0}'' to ''{1}''."),



  /**
   * The new value for the account expiration time.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  If this argument is not provided, the account expiration time will be set to the current time.
   */
  INFO_MANAGE_ACCT_SC_SET_ACCT_EXP_TIME_ARG_VALUE("The new value for the account expiration time.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  If this argument is not provided, the account expiration time will be set to the current time."),



  /**
   * Set the account expiration time for user ''{0}'' to ''{1}''.
   */
  INFO_MANAGE_ACCT_SC_SET_ACCT_EXP_TIME_EXAMPLE("Set the account expiration time for user ''{0}'' to ''{1}''."),



  /**
   * Indicates whether the user''s account should be locked as a result of too many failed authentication attempts.  The value must be either ''true'' (in which case the server will update the user''s entry to include the necessary number of failed authentication attempts) or ''false'' (in which case the server will clear information about any authentication failures from a user''s entry).
   */
  INFO_MANAGE_ACCT_SC_SET_ACCT_FAILURE_LOCKED_ARG_VALUE("Indicates whether the user''s account should be locked as a result of too many failed authentication attempts.  The value must be either ''true'' (in which case the server will update the user''s entry to include the necessary number of failed authentication attempts) or ''false'' (in which case the server will clear information about any authentication failures from a user''s entry)."),



  /**
   * Forces the account for user ''{0}'' to be locked as a result of too many failed authentication attempts.
   */
  INFO_MANAGE_ACCT_SC_SET_ACCT_FAILURE_LOCKED_EXAMPLE("Forces the account for user ''{0}'' to be locked as a result of too many failed authentication attempts."),



  /**
   * Indicates whether the user''s account should be locked because it contains a password that does not satisfy all of the configured password validators.  The value must be either ''true'' or ''false''.
   */
  INFO_MANAGE_ACCT_SC_SET_ACCT_VALIDATION_LOCKED_ARG_VALUE("Indicates whether the user''s account should be locked because it contains a password that does not satisfy all of the configured password validators.  The value must be either ''true'' or ''false''."),



  /**
   * Forces the account for user ''{0}'' to be locked because it contains a password that does not satisfy all of the configured password validators.
   */
  INFO_MANAGE_ACCT_SC_SET_ACCT_VALIDATION_LOCKED_EXAMPLE("Forces the account for user ''{0}'' to be locked because it contains a password that does not satisfy all of the configured password validators."),



  /**
   * A timestamp for an authentication failure time to set in the user''s entry.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  This argument may be provided multiple times to specify multiple authentication failure times.  If this argument is not provided, the set of authentication failure times will be updated to include only the current time.
   */
  INFO_MANAGE_ACCT_SC_SET_AUTH_FAILURE_TIMES_ARG_VALUE("A timestamp for an authentication failure time to set in the user''s entry.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  This argument may be provided multiple times to specify multiple authentication failure times.  If this argument is not provided, the set of authentication failure times will be updated to include only the current time."),



  /**
   * Sets the authentication failure times for user ''{0}'' to include values of ''{1}'' and ''{2}''.
   */
  INFO_MANAGE_ACCT_SC_SET_AUTH_FAILURE_TIMES_EXAMPLE("Sets the authentication failure times for user ''{0}'' to include values of ''{1}'' and ''{2}''."),



  /**
   * A timestamp for a grace login use time to set in the user''s entry.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  This argument may be provided multiple times to specify multiple grace login use times.  If this argument is not provided, the set of grace login use times will be updated to contain only the current time.
   */
  INFO_MANAGE_ACCT_SC_SET_GRACE_LOGIN_TIMES_ARG_VALUE("A timestamp for a grace login use time to set in the user''s entry.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  This argument may be provided multiple times to specify multiple grace login use times.  If this argument is not provided, the set of grace login use times will be updated to contain only the current time."),



  /**
   * Sets the grace login use times for user ''{0}'' to be ''{1}'' and ''{2}''.
   */
  INFO_MANAGE_ACCT_SC_SET_GRACE_LOGIN_TIMES_EXAMPLE("Sets the grace login use times for user ''{0}'' to be ''{1}'' and ''{2}''."),



  /**
   * The new value for the flag indicating whether the user''s account is disabled.  The value must be either ''true'' or ''false''.
   */
  INFO_MANAGE_ACCT_SC_SET_IS_DISABLED_ARG_VALUE("The new value for the flag indicating whether the user''s account is disabled.  The value must be either ''true'' or ''false''."),



  /**
   * Update user ''{0}'' to make the account administratively disabled.
   */
  INFO_MANAGE_ACCT_SC_SET_IS_DISABLED_EXAMPLE("Update user ''{0}'' to make the account administratively disabled."),



  /**
   * The new value for the last bind password validation time for the user.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  If this argument is not provided, the last bind password validation time will be set to the current time.
   */
  INFO_MANAGE_ACCT_SC_SET_LAST_BIND_PW_VALIDATION_TIME_ARG_VALUE("The new value for the last bind password validation time for the user.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  If this argument is not provided, the last bind password validation time will be set to the current time."),



  /**
   * Updates the password policy state for user ''{0}'' to indicate that the server last invoked password validators during a bind at time ''{1}''.
   */
  INFO_MANAGE_ACCT_SC_SET_LAST_BIND_PW_VALIDATION_TIME_EXAMPLE("Updates the password policy state for user ''{0}'' to indicate that the server last invoked password validators during a bind at time ''{1}''."),



  /**
   * The last login IP address value to use.  It may be an IPv4 or IPv6 address.
   */
  INFO_MANAGE_ACCT_SC_SET_LAST_LOGIN_IP_ARG_VALUE("The last login IP address value to use.  It may be an IPv4 or IPv6 address."),



  /**
   * Sets the last login IP address for user ''{0}'' to ''{1}''.
   */
  INFO_MANAGE_ACCT_SC_SET_LAST_LOGIN_IP_EXAMPLE("Sets the last login IP address for user ''{0}'' to ''{1}''."),



  /**
   * The timestamp to use for the last login time value.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  If this argument is not provided, the last login time will be set to the current time.
   */
  INFO_MANAGE_ACCT_SC_SET_LAST_LOGIN_TIME_ARG_VALUE("The timestamp to use for the last login time value.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  If this argument is not provided, the last login time will be set to the current time."),



  /**
   * Sets the last login time for user ''{0}'' to ''{1}''.
   */
  INFO_MANAGE_ACCT_SC_SET_LAST_LOGIN_TIME_EXAMPLE("Sets the last login time for user ''{0}'' to ''{1}''."),



  /**
   * Indicates whether the user should be forced to choose a new password before being permitted to perform any other operations.  The value must be either ''true'' or ''false''.
   */
  INFO_MANAGE_ACCT_SC_SET_MUST_CHANGE_PW_ARG_VALUE("Indicates whether the user should be forced to choose a new password before being permitted to perform any other operations.  The value must be either ''true'' or ''false''."),



  /**
   * Specifies that the password for user ''{0}'' has been reset by an administrator and must be changed before they will be allowed to perform any other operations.
   */
  INFO_MANAGE_ACCT_SC_SET_MUST_CHANGE_PW_EXAMPLE("Specifies that the password for user ''{0}'' has been reset by an administrator and must be changed before they will be allowed to perform any other operations."),



  /**
   * The most recent required change time with which the user has complied.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  If this argument is not provided, the most recent required change time will be used.
   */
  INFO_MANAGE_ACCT_SC_SET_PW_CHANGED_BY_REQ_TIME_ARG_VALUE("The most recent required change time with which the user has complied.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  If this argument is not provided, the most recent required change time will be used."),



  /**
   * Specifies that user ''{0}'' has most recently complied with the most recent required change time.
   */
  INFO_MANAGE_ACCT_SC_SET_PW_CHANGED_BY_REQ_TIME_EXAMPLE("Specifies that user ''{0}'' has most recently complied with the most recent required change time."),



  /**
   * The new value for the password changed time.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  If this argument is not provided, the password changed time will be set to the current time.
   */
  INFO_MANAGE_ACCT_SC_SET_PW_CHANGED_TIME_ARG_VALUE("The new value for the password changed time.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  If this argument is not provided, the password changed time will be set to the current time."),



  /**
   * Set the password changed time for user ''{0}'' to ''{1}''.
   */
  INFO_MANAGE_ACCT_SC_SET_PW_CHANGED_TIME_EXAMPLE("Set the password changed time for user ''{0}'' to ''{1}''."),



  /**
   * The new value for the password expiration warned time.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  If this argument is not provided, the password expiration warned time will be set to the current time.
   */
  INFO_MANAGE_ACCT_SC_SET_PW_EXP_WARNED_TIME_ARG_VALUE("The new value for the password expiration warned time.  It must be a timestamp formatted either in generalized time form or in the local time zone using one of the following formats:  YYYYMMDDhhmmss.uuu, YYYYMMDDhhmmss, or YYYYMMDDhhmm.  If this argument is not provided, the password expiration warned time will be set to the current time."),



  /**
   * Set the password expiration warned time for user ''{0}'' to ''{1}''.
   */
  INFO_MANAGE_ACCT_SC_SET_PW_EXP_WARNED_TIME_EXAMPLE("Set the password expiration warned time for user ''{0}'' to ''{1}''."),



  /**
   * The TOTP shared secret to register for a user.  This argument must be provided at least once, but may be provided multiple times to specify multiple TOTP shared secrets to register.
   */
  INFO_MANAGE_ACCT_SC_SET_TOTP_SHARED_SECRETS_ARG_VALUE("The TOTP shared secret to register for a user.  This argument must be provided at least once, but may be provided multiple times to specify multiple TOTP shared secrets to register."),



  /**
   * Replaces the set of TOTP shared secrets for user ''{0}'' with a single value of ''{1}''.
   */
  INFO_MANAGE_ACCT_SC_SET_TOTP_SHARED_SECRETS_EXAMPLE("Replaces the set of TOTP shared secrets for user ''{0}'' with a single value of ''{1}''."),



  /**
   * The public ID for a YubiKey OTP device to register for a user.  This argument must be provided at least once, but may be provided multiple times to specify multiple public IDs to register.
   */
  INFO_MANAGE_ACCT_SC_SET_YUBIKEY_IDS_ARG_VALUE("The public ID for a YubiKey OTP device to register for a user.  This argument must be provided at least once, but may be provided multiple times to specify multiple public IDs to register."),



  /**
   * Replaces the list of the public IDs of the YubiKey OTP devices registered for user ''{0}'' with a single public ID of ''{1}''.
   */
  INFO_MANAGE_ACCT_SC_SET_YUBIKEY_IDS_EXAMPLE("Replaces the list of the public IDs of the YubiKey OTP devices registered for user ''{0}'' with a single public ID of ''{1}''."),



  /**
   * Successfully completed the search matching filter {0}.  Entries returned:  {1}.
   */
  INFO_MANAGE_ACCT_SEARCH_OP_SUCCESSFUL_FULL("Successfully completed the search matching filter {0}.  Entries returned:  {1}."),



  /**
   * Successfully retrieved a page of entries matching filter {0}.  Entries returned:  {1}.
   */
  INFO_MANAGE_ACCT_SEARCH_OP_SUCCESSFUL_PAGE("Successfully retrieved a page of entries matching filter {0}.  Entries returned:  {1}."),



  /**
   * Retrieve or update information about the current state of a user account.  Processing will be performed using the password policy state extended operation, and you must have the password-reset privilege to use this extended operation.
   */
  INFO_MANAGE_ACCT_TOOL_DESC("Retrieve or update information about the current state of a user account.  Processing will be performed using the password policy state extended operation, and you must have the password-reset privilege to use this extended operation."),



  /**
   * NOTICE:  Received an unsolicited notification on connection {0}:  {1}
   */
  INFO_MANAGE_ACCT_UNSOLICITED_NOTIFICATION("NOTICE:  Received an unsolicited notification on connection {0}:  {1}"),



  /**
   * Only return OID registry items that contain a string (the OID, name, type, origin, or URL) that exactly matches the provided search string (ignoring differences in capitalization).  By default, the tool will use substring matching rather than exact matching.
   */
  INFO_OID_LOOKUP_ARG_DESC_EXACT_MATCH("Only return OID registry items that contain a string (the OID, name, type, origin, or URL) that exactly matches the provided search string (ignoring differences in capitalization).  By default, the tool will use substring matching rather than exact matching."),



  /**
   * The format to use for the output.  The value may be one of ''csv'', ''json'', ''multi-line'', or ''tab-delimited''.  If the output format is not specified, then the ''multi-line'' format will be used by default.
   */
  INFO_OID_LOOKUP_ARG_DESC_OUTPUT_FORMAT("The format to use for the output.  The value may be one of ''csv'', ''json'', ''multi-line'', or ''tab-delimited''.  If the output format is not specified, then the ''multi-line'' format will be used by default."),



  /**
   * The path to a file or directory containing schema definitions to use to augment the default OID registry.  If provided, the value may be the path to an LDIF file containing a subschema subentry, or it may be the path to a directory containing one or more schema files (in which case the files will be processed in lexicographic order by file name).  This argument may be provided multiple times to provide multiple schema paths.
   */
  INFO_OID_LOOKUP_ARG_DESC_SCHEMA_PATH("The path to a file or directory containing schema definitions to use to augment the default OID registry.  If provided, the value may be the path to an LDIF file containing a subschema subentry, or it may be the path to a directory containing one or more schema files (in which case the files will be processed in lexicographic order by file name).  This argument may be provided multiple times to provide multiple schema paths."),



  /**
   * Generate only the minimal amount of output with no additional comments.
   */
  INFO_OID_LOOKUP_ARG_DESC_TERSE("Generate only the minimal amount of output with no additional comments."),



  /**
   * '{'csv|json|multi-line|tab-delimited'}'
   */
  INFO_OID_LOOKUP_ARG_PLACEHOLDER_OUTPUT_FORMAT("'{'csv|json|multi-line|tab-delimited'}'"),



  /**
   * Display a list of all items in the OID registry.
   */
  INFO_OID_LOOKUP_EXAMPLE_1("Display a list of all items in the OID registry."),



  /**
   * Search the OID registry for items that contain the string ''2.5.4.3'' in the OID, name, type, origin, or URL fields.  Any matching items will be formatted as JSON objects.
   */
  INFO_OID_LOOKUP_EXAMPLE_2("Search the OID registry for items that contain the string ''2.5.4.3'' in the OID, name, type, origin, or URL fields.  Any matching items will be formatted as JSON objects."),



  /**
   * {0,number,0} matching items were found in the OID registry:
   */
  INFO_OID_LOOKUP_MULTIPLE_MATCHES("{0,number,0} matching items were found in the OID registry:"),



  /**
   * One matching item was found in the OID registry:
   */
  INFO_OID_LOOKUP_ONE_MATCH("One matching item was found in the OID registry:"),



  /**
   * Name:  {0}
   */
  INFO_OID_LOOKUP_OUTPUT_LINE_NAME("Name:  {0}"),



  /**
   * OID:  {0}
   */
  INFO_OID_LOOKUP_OUTPUT_LINE_OID("OID:  {0}"),



  /**
   * Origin:  {0}
   */
  INFO_OID_LOOKUP_OUTPUT_LINE_ORIGIN("Origin:  {0}"),



  /**
   * Type:  {0}
   */
  INFO_OID_LOOKUP_OUTPUT_LINE_TYPE("Type:  {0}"),



  /**
   * URL:  {0}
   */
  INFO_OID_LOOKUP_OUTPUT_LINE_URL("URL:  {0}"),



  /**
   * Search the OID registry to retrieve information about items that match a given OID or name.
   */
  INFO_OID_LOOKUP_TOOL_DESC_1("Search the OID registry to retrieve information about items that match a given OID or name."),



  /**
   * The string to use to search the OID registry should be provided as an unnamed trailing argument.  All items in the OID registry will be examined, and any items that contain the provided string in its OID, name, type, origin, or URL will be matched.  If no search string is provided, the entire OID registry will be displayed.
   */
  INFO_OID_LOOKUP_TOOL_DESC_2("The string to use to search the OID registry should be provided as an unnamed trailing argument.  All items in the OID registry will be examined, and any items that contain the provided string in its OID, name, type, origin, or URL will be matched.  If no search string is provided, the entire OID registry will be displayed."),



  /**
   * ['{'lookup-string'}']
   */
  INFO_OID_LOOKUP_TRAILING_ARG_PLACEHOLDER("['{'lookup-string'}']"),



  /**
   * Updates applied successfully on the initial attempt:  {0,number,0}.
   */
  INFO_PARALLEL_UPDATED_OPS_SUCCEEDED_INITIAL("Updates applied successfully on the initial attempt:  {0,number,0}."),



  /**
   * Updates applied successfully after one or more retry attempts:  {0,number,0}.
   */
  INFO_PARALLEL_UPDATED_OPS_SUCCEEDED_RETRY("Updates applied successfully after one or more retry attempts:  {0,number,0}."),



  /**
   * Include the specified control in all add requests.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_ADD_CONTROL("Include the specified control in all add requests."),



  /**
   * Include the proprietary undelete request control in all add requests that include the ''ds-undelete-from-dn'' attribute.  This control indicates that the server should un-delete the target soft-deleted entry rather than creating a new entry.  The server must be configured to allow soft delete operations, and the requester must hav ethe soft-delete-read privilege.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_ALLOW_UNDELETE("Include the proprietary undelete request control in all add requests that include the ''ds-undelete-from-dn'' attribute.  This control indicates that the server should un-delete the target soft-deleted entry rather than creating a new entry.  The server must be configured to allow soft delete operations, and the requester must hav ethe soft-delete-read privilege."),



  /**
   * The assurance level to request when replicating changes to other servers in the same location as the server that originally received the change.  The value should be one of ''none'', ''received-any-server'', or ''processed-all-servers''.  If this is not specified, then the server will determine an appropriate local assurance level.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_ASSURED_REPLICATION_LOCAL_LEVEL("The assurance level to request when replicating changes to other servers in the same location as the server that originally received the change.  The value should be one of ''none'', ''received-any-server'', or ''processed-all-servers''.  If this is not specified, then the server will determine an appropriate local assurance level."),



  /**
   * The assurance level to request when replicating changes to other servers in a different location from the server that originally received the change.  The value should be one of ''none'', ''received-any-remote-location'', ''received-all-remote-locations'', or ''processed-all-remote-servers''.  If this is not specified, then the server will determine an appropriate remote assurance level.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_ASSURED_REPLICATION_REMOTE_LEVEL("The assurance level to request when replicating changes to other servers in a different location from the server that originally received the change.  The value should be one of ''none'', ''received-any-remote-location'', ''received-all-remote-locations'', or ''processed-all-remote-servers''.  If this is not specified, then the server will determine an appropriate remote assurance level."),



  /**
   * The maximum length of time to wait for confirmation that the requested replication assurance criteria has been satisfied.  If this is not specified, then the server will determine an appropriate assurance timeout.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_ASSURED_REPLICATION_TIMEOUT("The maximum length of time to wait for confirmation that the requested replication assurance criteria has been satisfied.  If this is not specified, then the server will determine an appropriate assurance timeout."),



  /**
   * Include the specified control in all bind requests.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_BIND_CONTROL("Include the specified control in all bind requests."),



  /**
   * Indicates that any LDIF records without a changetype should be treated as add requests.  If this is not provided, then LDIF records without a changetype will be rejected.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_DEFAULT_ADD("Indicates that any LDIF records without a changetype should be treated as add requests.  If this is not provided, then LDIF records without a changetype will be rejected."),



  /**
   * Include the specified control in all delete requests.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_DELETE_CONTROL("Include the specified control in all delete requests."),



  /**
   * The path to a file containing the passphrase needed to decrypt the contents of the LDIF file.  If this argument is not provided and the LDIF file is encrypted, then the tool may interactively prompt for the encryption passphrase.  If this argument is provided, then the file must exist and it must contain exactly one line that is comprised only of the encryption passphrase.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_ENCRYPTION_PASSPHRASE_FILE("The path to a file containing the passphrase needed to decrypt the contents of the LDIF file.  If this argument is not provided and the LDIF file is encrypted, then the tool may interactively prompt for the encryption passphrase.  If this argument is provided, then the file must exist and it must contain exactly one line that is comprised only of the encryption passphrase."),



  /**
   * Indicates that the tool should attempt to follow any referrals that the server returns.  If this is not provided, then referral results will be treated as failures.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_FOLLOW_REFERRALS("Indicates that the tool should attempt to follow any referrals that the server returns.  If this is not provided, then referral results will be treated as failures."),



  /**
   * Include the proprietary hard delete request control in all delete requests.  This control indicates that the server should completely remove the target entry, even if it would have otherwise automatically processed the operation as a soft delete.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_HARD_DELETE("Include the proprietary hard delete request control in all delete requests.  This control indicates that the server should completely remove the target entry, even if it would have otherwise automatically processed the operation as a soft delete."),



  /**
   * Include the proprietary ignore NO-USER-MODIFICATION request control in all add and modify requests.  This control indicates that the server should not reject requests in which the client attempts to set values for attribute types declared with the NO-USER-MODIFICATION constraint in the schema.  This control primarily applies to add operations, but the server can be configured to allow it for a limited set of attributes in modify operation.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_IGNORE_NO_USER_MOD("Include the proprietary ignore NO-USER-MODIFICATION request control in all add and modify requests.  This control indicates that the server should not reject requests in which the client attempts to set values for attribute types declared with the NO-USER-MODIFICATION constraint in the schema.  This control primarily applies to add operations, but the server can be configured to allow it for a limited set of attributes in modify operation."),



  /**
   * Indicates that the LDIF file is gzip-compressed.  This argument is no longer required, as the tool will automatically detect whether the LDIF file is compressed or encrypted.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_IS_COMPRESSED("Indicates that the LDIF file is gzip-compressed.  This argument is no longer required, as the tool will automatically detect whether the LDIF file is compressed or encrypted."),



  /**
   * The path to the LDIF file containing the changes to process.  This must be specified, and the file must exist.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_LDIF_FILE("The path to the LDIF file containing the changes to process.  This must be specified, and the file must exist."),



  /**
   * The path to a log file that will be written with a record of all operations attempted during processing.  If this is specified, then the file does not need to exist, but the parent directory must exist.  If this is not specified, then no log file will be kept.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_LOG_FILE("The path to a log file that will be written with a record of all operations attempted during processing.  If this is specified, then the file does not need to exist, but the parent directory must exist.  If this is not specified, then no log file will be kept."),



  /**
   * Include the specified control in all modify requests.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_MODIFY_CONTROL("Include the specified control in all modify requests."),



  /**
   * Include the specified control in all modify DN requests.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_MODIFY_DN_CONTROL("Include the specified control in all modify DN requests."),



  /**
   * Include the proprietary name with entryUUID request control in all add requests.  This control indicates that the server should use entryUUID as the naming attribute for the entry, rather than the RDN included in the add request.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_NAME_WITH_ENTRYUUID("Include the proprietary name with entryUUID request control in all add requests.  This control indicates that the server should use entryUUID as the naming attribute for the entry, rather than the RDN included in the add request."),



  /**
   * Indicates that the tool should never attempt to retry any failed operations.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_NEVER_RETRY("Indicates that the tool should never attempt to retry any failed operations."),



  /**
   * The number of threads to use when processing operations in parallel.  The value must be greater than or equal to one, with a value of one indicating that processing should be performed in a single thread (without any parallelism).  If this is not specified, a default of eight threads will be used.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_NUM_THREADS("The number of threads to use when processing operations in parallel.  The value must be greater than or equal to one, with a value of one indicating that processing should be performed in a single thread (without any parallelism).  If this is not specified, a default of eight threads will be used."),



  /**
   * Include the proprietary operation purpose request control in all requests.  This control provides a human-readable reason for the associated operation.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_OPERATION_PURPOSE("Include the proprietary operation purpose request control in all requests.  This control provides a human-readable reason for the associated operation."),



  /**
   * Include the proxied authorization request control (as described in RFC 4370, also known as the proxied authorization v2 request control) in all add, delete, modify, and modify DN requests.  This control indicates that the operation should be processed under the authority of an alternate authorization identity.  That authorization identity should generally be specified as either the string ''dn:'' followed by the DN of the entry for the target user or ''u:'' followed by the username for the target user.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_PROXY_AS("Include the proxied authorization request control (as described in RFC 4370, also known as the proxied authorization v2 request control) in all add, delete, modify, and modify DN requests.  This control indicates that the operation should be processed under the authority of an alternate authorization identity.  That authorization identity should generally be specified as either the string ''dn:'' followed by the DN of the entry for the target user or ''u:'' followed by the username for the target user."),



  /**
   * Include the proxied authorization v1 request control (as described in draft-weltman-ldapv3-proxy-04) in all add, delete, modify, and modify DN requests.  This control indicates that the operation should be processed under the authority of an alternate authorization identity, specified as the DN of the entry for the target user.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_PROXY_V1_AS("Include the proxied authorization v1 request control (as described in draft-weltman-ldapv3-proxy-04) in all add, delete, modify, and modify DN requests.  This control indicates that the operation should be processed under the authority of an alternate authorization identity, specified as the DN of the entry for the target user."),



  /**
   * Include the proprietary password update behavior request control in all add and modify requests.  This control may be used to customize the server''s treatment of any passwords contained in the request.  Values for this argument must be provided in the form ''name=value'', where the property can be any of the following:  is-self-change, allow-pre-encoded-password, skip-password-validation, ignore-password-history, ignore-minimum-password-age, password-storage-scheme, and must-change-password.  The value for the password-storage-scheme property should be the name of the desired scheme, and the value for all other properties should be either ''true'' or ''false''.  This argument can be provided multiple times to specify multiple password update behaviors.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_PW_UPDATE_BEHAVIOR("Include the proprietary password update behavior request control in all add and modify requests.  This control may be used to customize the server''s treatment of any passwords contained in the request.  Values for this argument must be provided in the form ''name=value'', where the property can be any of the following:  is-self-change, allow-pre-encoded-password, skip-password-validation, ignore-password-history, ignore-minimum-password-age, password-storage-scheme, and must-change-password.  The value for the password-storage-scheme property should be the name of the desired scheme, and the value for all other properties should be either ''true'' or ''false''.  This argument can be provided multiple times to specify multiple password update behaviors."),



  /**
   * The maximum rate (in number of requests per second) at which the tool should attempt to process requests.  If this is specified, the value must be greater than or equal to one.  If this is not specified, then no rate limiting will be performed.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_RATE_PER_SECOND("The maximum rate (in number of requests per second) at which the tool should attempt to process requests.  If this is specified, the value must be greater than or equal to one.  If this is not specified, then no rate limiting will be performed."),



  /**
   * The path to a file that will be written with a record of all operations that could not be completed successfully, even after all retry attempts.  This must be specified, and the file does not need to exist, but the parent directory must exist.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_REJECT_FILE("The path to a file that will be written with a record of all operations that could not be completed successfully, even after all retry attempts.  This must be specified, and the file does not need to exist, but the parent directory must exist."),



  /**
   * Include the proprietary soft delete request control in all delete requests.  This control indicates that the server should hide the target entry for a period of time before actually removing it so that it may be restored with an undelete operation if the delete should be reverted.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_SOFT_DELETE("Include the proprietary soft delete request control in all delete requests.  This control indicates that the server should hide the target entry for a period of time before actually removing it so that it may be restored with an undelete operation if the delete should be reverted."),



  /**
   * Include the proprietary suppress operational attribute update request control in all add, bind, delete, modify, and modify DN requests.  This control indicates that the server should not update the specified operational attribute in the course of processing the operation.  The value may be one of ''last-access-time'', ''last-login-time'', ''last-login-ip'', or ''lastmod''.  This argument may be provided multiple times to suppress updates to multiple operational attributes.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_SUPPRESS_OP_ATTR_UPDATES("Include the proprietary suppress operational attribute update request control in all add, bind, delete, modify, and modify DN requests.  This control indicates that the server should not update the specified operational attribute in the course of processing the operation.  The value may be one of ''last-access-time'', ''last-login-time'', ''last-login-ip'', or ''lastmod''.  This argument may be provided multiple times to suppress updates to multiple operational attributes."),



  /**
   * Include the proprietary suppress referential integrity updates request control in all delete and modify DN requests.  This control indicates that the server should not perform any referential integrity processing for the associated operation.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_SUPPRESS_REFERENTIAL_INTEGRITY_UPDATES("Include the proprietary suppress referential integrity updates request control in all delete and modify DN requests.  This control indicates that the server should not perform any referential integrity processing for the associated operation."),



  /**
   * Include the proprietary assured replication request control in all add, delete, modify, and modify DN requests.  This control indicates that the server should wait for confirmation that the change has been replicated to a minimum degree of assurance before returning the response to the client.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_USE_ASSURED_REPLICATION("Include the proprietary assured replication request control in all add, delete, modify, and modify DN requests.  This control indicates that the server should wait for confirmation that the change has been replicated to a minimum degree of assurance before returning the response to the client."),



  /**
   * Include the manageDsaIT request control in all add, delete, modify, and modify DN request.  This control indicates that the server should treat referral entries as regular entries, and that the server should attempt to process that change directly against the referral entry rather generating a referral to indicate that the change should be sent to another server or to a different location in the same server.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_USE_MANAGE_DSA_IT("Include the manageDsaIT request control in all add, delete, modify, and modify DN request.  This control indicates that the server should treat referral entries as regular entries, and that the server should attempt to process that change directly against the referral entry rather generating a referral to indicate that the change should be sent to another server or to a different location in the same server."),



  /**
   * Include the permissive modify request control in all modify requests.  This control indicates that the server should not reject attempts to add attribute values that already exist in the entry or attempts to remove attribute values that do not exist in the entry.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_USE_PERMISSIVE_MODIFY("Include the permissive modify request control in all modify requests.  This control indicates that the server should not reject attempts to add attribute values that already exist in the entry or attempts to remove attribute values that do not exist in the entry."),



  /**
   * Include the proprietary replication repair request control in all add, delete, modify, and modify DN requests.  This control indicates that the requested operation should only be processed in the server instance that originally received the change, and it should not be replicated to other servers in the topology.  This should generally only be used under the direction of support personnel.
   */
  INFO_PARALLEL_UPDATE_ARG_DESC_USE_REPLICATION_REPAIR("Include the proprietary replication repair request control in all add, delete, modify, and modify DN requests.  This control indicates that the requested operation should only be processed in the server instance that originally received the change, and it should not be replicated to other servers in the topology.  This should generally only be used under the direction of support personnel."),



  /**
   * Request Control Arguments
   */
  INFO_PARALLEL_UPDATE_ARG_GROUP_CONTROLS("Request Control Arguments"),



  /**
   * Change Processing Arguments
   */
  INFO_PARALLEL_UPDATE_ARG_GROUP_PROCESSING("Change Processing Arguments"),



  /**
   * '{'level'}'
   */
  INFO_PARALLEL_UPDATE_ARG_PLACEHOLDER_ASSURED_REPLICATION_LOCAL_LEVEL("'{'level'}'"),



  /**
   * '{'level'}'
   */
  INFO_PARALLEL_UPDATE_ARG_PLACEHOLDER_ASSURED_REPLICATION_REMOTE_LEVEL("'{'level'}'"),



  /**
   * '{'purpose'}'
   */
  INFO_PARALLEL_UPDATE_ARG_PLACEHOLDER_OPERATION_PURPOSE("'{'purpose'}'"),



  /**
   * '{'authzID'}'
   */
  INFO_PARALLEL_UPDATE_ARG_PLACEHOLDER_PROXY_AS("'{'authzID'}'"),



  /**
   * '{'name=value'}'
   */
  INFO_PARALLEL_UPDATE_ARG_PLACEHOLDER_PW_UPDATE_BEHAVIOR("'{'name=value'}'"),



  /**
   * '{'attr'}'
   */
  INFO_PARALLEL_UPDATE_ARG_PLACEHOLDER_SUPPRESS_OP_ATTR_UPDATES("'{'attr'}'"),



  /**
   * Beginning a retry pass for {0,number,0} operations.
   */
  INFO_PARALLEL_UPDATE_BEGINNING_RETRY("Beginning a retry pass for {0,number,0} operations."),



  /**
   * All {0} processing completed successfully.
   */
  INFO_PARALLEL_UPDATE_COMPLETION_MESSAGE_ALL_SUCCEEDED("All {0} processing completed successfully."),



  /**
   * All {0} processing completed, but {1,number,0} changes were rejected.
   */
  INFO_PARALLEL_UPDATE_COMPLETION_MESSAGE_MULTIPLE_REJECTED("All {0} processing completed, but {1,number,0} changes were rejected."),



  /**
   * All {0} processing completed, but one change was rejected.
   */
  INFO_PARALLEL_UPDATE_COMPLETION_MESSAGE_ONE_REJECTED("All {0} processing completed, but one change was rejected."),



  /**
   * All {0} processing has completed.
   */
  INFO_PARALLEL_UPDATE_DONE("All {0} processing has completed."),



  /**
   * Reached the end of the LDIF file.
   */
  INFO_PARALLEL_UPDATE_END_OF_LDIF("Reached the end of the LDIF file."),



  /**
   * Uses ten concurrent threads to apply the changes read from the changes.ldif file against the LDAP server at server.example.com:636 over a TLS-encrypted connection.  Any rejected changes will be written to the rejects.ldif file, and processing will be limited to a rate of 5000 attempts per second.
   */
  INFO_PARALLEL_UPDATE_EXAMPLE_DESC("Uses ten concurrent threads to apply the changes read from the changes.ldif file against the LDAP server at server.example.com:636 over a TLS-encrypted connection.  Any rejected changes will be written to the rejects.ldif file, and processing will be limited to a rate of 5000 attempts per second."),



  /**
   * Total processing time:  {0}.
   */
  INFO_PARALLEL_UPDATE_SUMMARY_DURATION("Total processing time:  {0}."),



  /**
   * Initial attempts:  {0,number,0}.
   */
  INFO_PARALLEL_UPDATE_SUMMARY_INITIAL_ATTEMPTS("Initial attempts:  {0,number,0}."),



  /**
   * Updates attempted:  {0,number,0}.
   */
  INFO_PARALLEL_UPDATE_SUMMARY_OPS_ATTEMPTED("Updates attempted:  {0,number,0}."),



  /**
   * Updates rejected:  {0,number,0}.
   */
  INFO_PARALLEL_UPDATE_SUMMARY_OPS_REJECTED("Updates rejected:  {0,number,0}."),



  /**
   * Updates applied successfully:  {0,number,0}.
   */
  INFO_PARALLEL_UPDATE_SUMMARY_OPS_SUCCEEDED("Updates applied successfully:  {0,number,0}."),



  /**
   * Overall rate:  {0} changes per second.
   */
  INFO_PARALLEL_UPDATE_SUMMARY_RATE("Overall rate:  {0} changes per second."),



  /**
   * Retry attempts:  {0,number,0}.
   */
  INFO_PARALLEL_UPDATE_SUMMARY_RETRY_ATTEMPTS("Retry attempts:  {0,number,0}."),



  /**
   * Use multiple concurrent threads to apply a set of add, delete, modify, and modify DN operations read from an LDIF file.
   */
  INFO_PARALLEL_UPDATE_TOOL_DESCRIPTION_1("Use multiple concurrent threads to apply a set of add, delete, modify, and modify DN operations read from an LDIF file."),



  /**
   * As with other tools like ldapmodify, changes in the LDIF file to be processed should be ordered such that if there are any dependencies between changes (for example, if one add change record creates a parent entry and another add change record creates a child of that parent), prerequisite changes come before the changes that depend on them.  When this tool is preparing to process a change, it will determine whether the new change depends on any other changes that are currently in progress, and if so, will delay processing that change until its dependencies have been satisfied.  If a change does not depend on any other changes that are currently being processed, then it can be processed in parallel with those changes.
   */
  INFO_PARALLEL_UPDATE_TOOL_DESCRIPTION_2("As with other tools like ldapmodify, changes in the LDIF file to be processed should be ordered such that if there are any dependencies between changes (for example, if one add change record creates a parent entry and another add change record creates a child of that parent), prerequisite changes come before the changes that depend on them.  When this tool is preparing to process a change, it will determine whether the new change depends on any other changes that are currently in progress, and if so, will delay processing that change until its dependencies have been satisfied.  If a change does not depend on any other changes that are currently being processed, then it can be processed in parallel with those changes."),



  /**
   * This tool will keep track of any changes that fail in a way that indicates they succeed if re-tried later (for example, an attempt to add an entry that fails because its parent does not exist, but its parent may be created later in the set of LDIF changes), and can optionally re-try those changes after processing is complete.  Any changes that are not retried, as well as changes that still fail after the retry attempts, will be written to a rejects file with information about the reason for the failure so that an administrator can take any necessary further action upon them.
   */
  INFO_PARALLEL_UPDATE_TOOL_DESCRIPTION_3("This tool will keep track of any changes that fail in a way that indicates they succeed if re-tried later (for example, an attempt to add an entry that fails because its parent does not exist, but its parent may be created later in the set of LDIF changes), and can optionally re-try those changes after processing is complete.  Any changes that are not retried, as well as changes that still fail after the retry attempts, will be written to a rejects file with information about the reason for the failure so that an administrator can take any necessary further action upon them."),



  /**
   * The {0} tool received an unsolicited notification with result code {1} and OID {2}.
   */
  INFO_PARALLEL_UPDATE_UNSOLICITED_NOTIFICATION_NO_MESSAGE("The {0} tool received an unsolicited notification with result code {1} and OID {2}."),



  /**
   * The {0} tool received an unsolicited notification with result code {1}, OID {2}, and diagnostic message ''{3}''.
   */
  INFO_PARALLEL_UPDATE_UNSOLICITED_NOTIFICATION_WITH_MESSAGE("The {0} tool received an unsolicited notification with result code {1}, OID {2}, and diagnostic message ''{3}''."),



  /**
   * Indicates that the tool should use the result code for the first rejected operation (if any) as the exit code.  By default, the tool will use an exit code of zero if all changes were attempted, even if some of those changes were rejected.
   */
  INFO_PARALLEL_UPDATE_USE_FIRST_REJECT_RC("Indicates that the tool should use the result code for the first rejected operation (if any) as the exit code.  By default, the tool will use an exit code of zero if all changes were attempted, even if some of those changes were rejected."),



  /**
   * '{'attr'}'
   */
  INFO_PLACEHOLDER_ATTR("'{'attr'}'"),



  /**
   * '{'authzID'}'
   */
  INFO_PLACEHOLDER_AUTHZID("'{'authzID'}'"),



  /**
   * '{'fieldName'}'
   */
  INFO_PLACEHOLDER_FIELD_NAME("'{'fieldName'}'"),



  /**
   * '{'filter'}'
   */
  INFO_PLACEHOLDER_FILTER("'{'filter'}'"),



  /**
   * '{'level'}'
   */
  INFO_PLACEHOLDER_LEVEL("'{'level'}'"),



  /**
   * '{'num'}'
   */
  INFO_PLACEHOLDER_NUM("'{'num'}'"),



  /**
   * '{'path'}'
   */
  INFO_PLACEHOLDER_PATH("'{'path'}'"),



  /**
   * '{'purpose'}'
   */
  INFO_PLACEHOLDER_PURPOSE("'{'purpose'}'"),



  /**
   * '{'timeout'}'
   */
  INFO_PLACEHOLDER_TIMEOUT("'{'timeout'}'"),



  /**
   * Include the UnboundID-proprietary assured replication request control to indicate that the update response should be delayed until the change has been sufficiently replicated to other servers.
   */
  INFO_PWMOD_ARG_DESC_ASSURED_REPLICATION("Include the UnboundID-proprietary assured replication request control to indicate that the update response should be delayed until the change has been sufficiently replicated to other servers."),



  /**
   * The local assurance level to use in the assured replication request control.  If provided, the value should be one of ''none'' (to indicate that the update response should not be delayed for replication to local servers), ''received-any-server'' (to indicate that the update response should be delayed until the change has been received by, but not necessarily applied on, at least one other local server), or ''processed-all-servers'' (to indicate that the update response should be delayed until the change has been applied in all available local servers).  If this is not specified, then the server will select an appropriate local assurance level.
   */
  INFO_PWMOD_ARG_DESC_ASSURED_REPLICATION_LOCAL_LEVEL("The local assurance level to use in the assured replication request control.  If provided, the value should be one of ''none'' (to indicate that the update response should not be delayed for replication to local servers), ''received-any-server'' (to indicate that the update response should be delayed until the change has been received by, but not necessarily applied on, at least one other local server), or ''processed-all-servers'' (to indicate that the update response should be delayed until the change has been applied in all available local servers).  If this is not specified, then the server will select an appropriate local assurance level."),



  /**
   * The remote assurance level to use in the assured replication request control.  The value should be one of ''none'' (to indicate that the update response should not be delayed for replication to remote servers), ''received-any-remote-location'' (to indicate that the update response should be delayed until the change has been received by, but not necessarily applied on, at least one server in at least one remote location), ''received-all-remote-locations'' (to indicate that the update response should be delayed until the change has been received by, but not necessarily applied on, at least one server in every remote location ), or ''processed-all-remote-servers'' (to indicate that the update response should be delayed until the change has been applied by all available remote servers in all locations).  If this is not specified, then the server will select an appropriate remote assurance level.
   */
  INFO_PWMOD_ARG_DESC_ASSURED_REPLICATION_REMOTE_LEVEL("The remote assurance level to use in the assured replication request control.  The value should be one of ''none'' (to indicate that the update response should not be delayed for replication to remote servers), ''received-any-remote-location'' (to indicate that the update response should be delayed until the change has been received by, but not necessarily applied on, at least one server in at least one remote location), ''received-all-remote-locations'' (to indicate that the update response should be delayed until the change has been received by, but not necessarily applied on, at least one server in every remote location ), or ''processed-all-remote-servers'' (to indicate that the update response should be delayed until the change has been applied by all available remote servers in all locations).  If this is not specified, then the server will select an appropriate remote assurance level."),



  /**
   * The timeout to use for assured replication processing.  If provided, the value should be in the form of an integer followed by a time unit that is one of ''nanosecond'', ''microsecond'', ''millisecond'', ''second'', ''minute'', ''hour'', ''day'', or ''week'' (or one of their plurals or abbreviations).  For example, values of ''500 milliseconds'', ''500 ms'', and ''500ms'' all specify a timeout of 500 milliseconds.  If this is not specified, then the server will select an appropriate timeout.
   */
  INFO_PWMOD_ARG_DESC_ASSURED_REPLICATION_TIMEOUT("The timeout to use for assured replication processing.  If provided, the value should be in the form of an integer followed by a time unit that is one of ''nanosecond'', ''microsecond'', ''millisecond'', ''second'', ''minute'', ''hour'', ''day'', or ''week'' (or one of their plurals or abbreviations).  For example, values of ''500 milliseconds'', ''500 ms'', and ''500ms'' all specify a timeout of 500 milliseconds.  If this is not specified, then the server will select an appropriate timeout."),



  /**
   * Include the specified control in the bind request used to authenticate to the server.  This may be provided multiple times to specify multiple bind request controls.
   */
  INFO_PWMOD_ARG_DESC_BIND_CONTROL("Include the specified control in the bind request used to authenticate to the server.  This may be provided multiple times to specify multiple bind request controls."),



  /**
   * The current password to provide when setting the new password.
   */
  INFO_PWMOD_ARG_DESC_CURRENT_PASSWORD("The current password to provide when setting the new password."),



  /**
   * The path to a file containing the current password to provide when setting the new password.  If this is provided, then the file must exist and it must contain exactly one line with only the user''s current password.
   */
  INFO_PWMOD_ARG_DESC_CURRENT_PASSWORD_FILE("The path to a file containing the current password to provide when setting the new password.  If this is provided, then the file must exist and it must contain exactly one line with only the user''s current password."),



  /**
   * Automatically attempt to follow any referrals received when processing requests.
   */
  INFO_PWMOD_ARG_DESC_FOLLOW_REFERRALS("Automatically attempt to follow any referrals received when processing requests."),



  /**
   * A set of characters that may be included in the generated client-side password.  This may be provided multiple times to specify multiple character sets, in which case the generated password will include at least one character from each of the sets.  If this is not provided, then the generated password will be a mix of lowercase ASCII letters, uppercase ASCII letters, ASCII digits, and a selected set of ASCII symbols.
   */
  INFO_PWMOD_ARG_DESC_GENERATED_PASSWORD_CHARACTER_SET("A set of characters that may be included in the generated client-side password.  This may be provided multiple times to specify multiple character sets, in which case the generated password will include at least one character from each of the sets.  If this is not provided, then the generated password will be a mix of lowercase ASCII letters, uppercase ASCII letters, ASCII digits, and a selected set of ASCII symbols."),



  /**
   * The number of characters to include in the generated client-side password.  If this is not provided, then a default length of twelve characters will be used.
   */
  INFO_PWMOD_ARG_DESC_GENERATED_PASSWORD_LENGTH("The number of characters to include in the generated client-side password.  If this is not provided, then a default length of twelve characters will be used."),



  /**
   * Have this tool generate the new password for the target user.  If this argument is provided, then the new password will be displayed before sending the request to the server.
   */
  INFO_PWMOD_ARG_DESC_GENERATE_CLIENT_SIDE_NEW_PASSWORD("Have this tool generate the new password for the target user.  If this argument is provided, then the new password will be displayed before sending the request to the server."),



  /**
   * Include the UnboundID-proprietary get authorization entry request control in the bind request to indicate that the bind response should include the entry for the authenticated user with the specified attribute.  This argument may be provided multiple times to specify that multiple attributes from the user entry should be included.
   */
  INFO_PWMOD_ARG_DESC_GET_AUTHZ_ENTRY_ATTRIBUTE("Include the UnboundID-proprietary get authorization entry request control in the bind request to indicate that the bind response should include the entry for the authenticated user with the specified attribute.  This argument may be provided multiple times to specify that multiple attributes from the user entry should be included."),



  /**
   * Include the UnboundID-proprietary get password validation details request control in the request used to update the password.  The corresponding response control will include information about the requirements that the server will impose for the target user and whether the provided new password satisfies each of those constraints.
   */
  INFO_PWMOD_ARG_DESC_GET_PW_VALIDATION_DETAILS("Include the UnboundID-proprietary get password validation details request control in the request used to update the password.  The corresponding response control will include information about the requirements that the server will impose for the target user and whether the provided new password satisfies each of those constraints."),



  /**
   * Include the UnboundID-proprietary get user resource limits request control in the bind request to indicate that the server should return information about resource limits (e.g., size limit, time limit, idle time limit, etc.) imposed for the user.
   */
  INFO_PWMOD_ARG_DESC_GET_USER_RESOURCE_LIMITS("Include the UnboundID-proprietary get user resource limits request control in the bind request to indicate that the server should return information about resource limits (e.g., size limit, time limit, idle time limit, etc.) imposed for the user."),



  /**
   * The new password to set for the target user.
   */
  INFO_PWMOD_ARG_DESC_NEW_PASSWORD("The new password to set for the target user."),



  /**
   * The path to a file containing the new password to set for the target user.  If this argument is provided, then the file must exist, and it must contain exactly one line with only the desired new password.
   */
  INFO_PWMOD_ARG_DESC_NEW_PASSWORD_FILE("The path to a file containing the new password to set for the target user.  If this argument is provided, then the file must exist, and it must contain exactly one line with only the desired new password."),



  /**
   * Include the LDAP no-operation request control (as described in draft-zeilenga-ldap-noop) to indicate that the server should validate that the password change would likely succeed, but that the user''s password should not actually be changed.
   */
  INFO_PWMOD_ARG_DESC_NO_OPERATION("Include the LDAP no-operation request control (as described in draft-zeilenga-ldap-noop) to indicate that the server should validate that the password change would likely succeed, but that the user''s password should not actually be changed."),



  /**
   * Include the UnboundID-proprietary operation purpose request control in the request sent to update the target user''s password to provide additional information about the purpose for the request.
   */
  INFO_PWMOD_ARG_DESC_OPERATION_PURPOSE("Include the UnboundID-proprietary operation purpose request control in the request sent to update the target user''s password to provide additional information about the purpose for the request."),



  /**
   * The name of the attribute containing the password to update.  This will only be used if the password will be updated with a regular LDAP modify operation (as opposed to the password modify extended operation), and if it is not specified, then a default of userPassword will be used.
   */
  INFO_PWMOD_ARG_DESC_PASSWORD_ATTRIBUTE("The name of the attribute containing the password to update.  This will only be used if the password will be updated with a regular LDAP modify operation (as opposed to the password modify extended operation), and if it is not specified, then a default of userPassword will be used."),



  /**
   * The method to use to set the password.  Allowed values are ''password-modify-extended-operation'' (for the password modify extended operation as described in RFC 3062), ''ldap-modify'' (for a regular LDAP modify operation targeting the specified password attribute), or ''active-directory'' (for an Active Directory-specific password change mechanism).  If this is not specified, then the tool will attempt to automatically determine the appropriate method.
   */
  INFO_PWMOD_ARG_DESC_PASSWORD_CHANGE_METHOD("The method to use to set the password.  Allowed values are ''password-modify-extended-operation'' (for the password modify extended operation as described in RFC 3062), ''ldap-modify'' (for a regular LDAP modify operation targeting the specified password attribute), or ''active-directory'' (for an Active Directory-specific password change mechanism).  If this is not specified, then the tool will attempt to automatically determine the appropriate method."),



  /**
   * Include the UnboundID-proprietary password update behavior request control in the request used to update the password to specify settings that the server should use when updating the password.  Values of this argument must be in the form ''name=value'', where the property name can be any one of the following:  is-self-change, allow-pre-encoded-password, skip-password-validation, ignore-password-history, ignore-minimum-password-age, password-storage-scheme, and must-change-password.  The value for each property should be either ''true'' or ''false'', with the exception of the storage-scheme property, whose value should be the name of the desired password storage scheme to use to encode the new password.  This argument can be provided multiple times to specify multiple password update behaviors.
   */
  INFO_PWMOD_ARG_DESC_PASSWORD_UPDATE_BEHAVIOR("Include the UnboundID-proprietary password update behavior request control in the request used to update the password to specify settings that the server should use when updating the password.  Values of this argument must be in the form ''name=value'', where the property name can be any one of the following:  is-self-change, allow-pre-encoded-password, skip-password-validation, ignore-password-history, ignore-minimum-password-age, password-storage-scheme, and must-change-password.  The value for each property should be either ''true'' or ''false'', with the exception of the storage-scheme property, whose value should be the name of the desired password storage scheme to use to encode the new password.  This argument can be provided multiple times to specify multiple password update behaviors."),



  /**
   * Interactively prompt for the user''s current password.
   */
  INFO_PWMOD_ARG_DESC_PROMPT_FOR_CURRENT_PASSWORD("Interactively prompt for the user''s current password."),



  /**
   * Interactively prompt for the new password to set for the target user.
   */
  INFO_PWMOD_ARG_DESC_PROMPT_FOR_NEW_PASSWORD("Interactively prompt for the new password to set for the target user."),



  /**
   * Explicitly provide the bind DN as the value of the user identity field in the password modify extended request, rather than omitting that field.  This argument only applies when changing passwords using the password modify extended operation, although the bind DN may be used as the target entry DN for modify requests if no alternate user identity is specified.
   */
  INFO_PWMOD_ARG_DESC_PROVIDE_BIND_DN_AS_USER_IDENTITY("Explicitly provide the bind DN as the value of the user identity field in the password modify extended request, rather than omitting that field.  This argument only applies when changing passwords using the password modify extended operation, although the bind DN may be used as the target entry DN for modify requests if no alternate user identity is specified."),



  /**
   * Indicate the UnboundID-proprietary purge password request control in the request used to update the password.  This will indicate that the server should completely remove the former password from the user''s entry, even if the server would have otherwise retired the former password for a brief period of time.
   */
  INFO_PWMOD_ARG_DESC_PURGE_CURRENT_PASSWORD("Indicate the UnboundID-proprietary purge password request control in the request used to update the password.  This will indicate that the server should completely remove the former password from the user''s entry, even if the server would have otherwise retired the former password for a brief period of time."),



  /**
   * Include the UnboundID-proprietary retire password request control in the request used to update the password.  This will indicate that the server should continue to allow the user to authenticate with their former password (in addition to the new password) for a brief period of time.
   */
  INFO_PWMOD_ARG_DESC_RETIRE_CURRENT_PASSWORD("Include the UnboundID-proprietary retire password request control in the request used to update the password.  This will indicate that the server should continue to allow the user to authenticate with their former password (in addition to the new password) for a brief period of time."),



  /**
   * Generate output in a script-friendly format.
   */
  INFO_PWMOD_ARG_DESC_SCRIPT_FRIENDLY("Generate output in a script-friendly format."),



  /**
   * The base DN to use when searching for the user to update.  This will be ignored if the user identity is provided as a DN or if the password will be changed using the password modify extended operation.  If this is not provided, the null DN will be used as the default search base DN.
   */
  INFO_PWMOD_ARG_DESC_SEARCH_BASE_DN("The base DN to use when searching for the user to update.  This will be ignored if the user identity is provided as a DN or if the password will be changed using the password modify extended operation.  If this is not provided, the null DN will be used as the default search base DN."),



  /**
   * Include the specified control in the request used to update the user''s password.  This may be provided multiple times to specify multiple update controls.
   */
  INFO_PWMOD_ARG_DESC_UPDATE_CONTROL("Include the specified control in the request used to update the user''s password.  This may be provided multiple times to specify multiple update controls."),



  /**
   * The name of the attribute that will be used to search for the user to update if the user identity is provided as a username rather than a DN, and if the password is to be updated with an LDAP modify operation rather than the password modify extended operation.  This argument will be ignored if the authorization identity is provided as a DN or if password will be changed using the password modify extended operation.  If this is not provided, then a default value of ''uid'' will be used for non-Active Directory servers, and default values of ''samAccountName'' and ''userPrincipalName'' will be used for Active Directory.  This argument may be provided multiple times to specify multiple user ID attributes, in which case the search will construct an OR filter to search across each of those attributes.  The resulting search must match exactly one entry for the password change attempt to proceed.
   */
  INFO_PWMOD_ARG_DESC_USERNAME_ATTRIBUTE("The name of the attribute that will be used to search for the user to update if the user identity is provided as a username rather than a DN, and if the password is to be updated with an LDAP modify operation rather than the password modify extended operation.  This argument will be ignored if the authorization identity is provided as a DN or if password will be changed using the password modify extended operation.  If this is not provided, then a default value of ''uid'' will be used for non-Active Directory servers, and default values of ''samAccountName'' and ''userPrincipalName'' will be used for Active Directory.  This argument may be provided multiple times to specify multiple user ID attributes, in which case the search will construct an OR filter to search across each of those attributes.  The resulting search must match exactly one entry for the password change attempt to proceed."),



  /**
   * The identity for the user whose password should be changed.  This may be a DN, or it may be an authorization identity in either the form ''dn:'' followed by a user DN or ''u:'' followed by a username.  If neither this argument nor the --provideBindDNAsUserIdentity argument is provided, then the current authorization identity for the underlying connection will be assumed.
   */
  INFO_PWMOD_ARG_DESC_USER_IDENTITY("The identity for the user whose password should be changed.  This may be a DN, or it may be an authorization identity in either the form ''dn:'' followed by a user DN or ''u:'' followed by a username.  If neither this argument nor the --provideBindDNAsUserIdentity argument is provided, then the current authorization identity for the underlying connection will be assumed."),



  /**
   * Use an administrative session to process the bind and update operations using a dedicated pool of worker threads.  This may be useful when trying to update the server when all normal worker threads are busy processing other requests.
   */
  INFO_PWMOD_ARG_DESC_USE_ADMIN_SESSION("Use an administrative session to process the bind and update operations using a dedicated pool of worker threads.  This may be useful when trying to update the server when all normal worker threads are busy processing other requests."),



  /**
   * Include the authorization identity request control in the bind request to indicate that the server should return the authorization identity that resulted from the bind.
   */
  INFO_PWMOD_ARG_DESC_USE_AUTHZ_ID_CONTROL("Include the authorization identity request control in the bind request to indicate that the server should return the authorization identity that resulted from the bind."),



  /**
   * Include the password policy request control (as described in draft-behera-ldap-password-policy) in the bind request used to authenticate to the server.
   */
  INFO_PWMOD_ARG_DESC_USE_PW_POLICY_CONTROL_ON_BIND("Include the password policy request control (as described in draft-behera-ldap-password-policy) in the bind request used to authenticate to the server."),



  /**
   * Include the password policy request control (as described in draft-behera-ldap-password-policy) in the request used to update the user''s password.
   */
  INFO_PWMOD_ARG_DESC_USE_PW_POLICY_CONTROL_ON_UPDATE("Include the password policy request control (as described in draft-behera-ldap-password-policy) in the request used to update the user''s password."),



  /**
   * Provide verbose output about the processing that the tool performs.
   */
  INFO_PWMOD_ARG_DESC_VERBOSE("Provide verbose output about the processing that the tool performs."),



  /**
   * Bind Control Arguments
   */
  INFO_PWMOD_ARG_GROUP_BIND_CONTROL("Bind Control Arguments"),



  /**
   * Current Password Arguments
   */
  INFO_PWMOD_ARG_GROUP_CURRENT_PASSWORD("Current Password Arguments"),



  /**
   * New Password Arguments
   */
  INFO_PWMOD_ARG_GROUP_NEW_PASSWORD("New Password Arguments"),



  /**
   * Other Arguments
   */
  INFO_PWMOD_ARG_GROUP_OTHER("Other Arguments"),



  /**
   * Update Control Arguments
   */
  INFO_PWMOD_ARG_GROUP_UPDATE_CONTROL("Update Control Arguments"),



  /**
   * User Identity Arguments
   */
  INFO_PWMOD_ARG_GROUP_USER_IDENTITY("User Identity Arguments"),



  /**
   * '{'attributeName'}'
   */
  INFO_PWMOD_ARG_PLACEHOLDER_ATTRIBUTE_NAME("'{'attributeName'}'"),



  /**
   * '{'password-modify-extended-operation|ldap-modify|active-directory'}'
   */
  INFO_PWMOD_ARG_PLACEHOLDER_CHANGE_METHOD("'{'password-modify-extended-operation|ldap-modify|active-directory'}'"),



  /**
   * '{'chars'}'
   */
  INFO_PWMOD_ARG_PLACEHOLDER_CHARS("'{'chars'}'"),



  /**
   * '{'dnOrAuthzID'}'
   */
  INFO_PWMOD_ARG_PLACEHOLDER_DN_OR_AUTHZID("'{'dnOrAuthzID'}'"),



  /**
   * '{'length'}'
   */
  INFO_PWMOD_ARG_PLACEHOLDER_LENGTH("'{'length'}'"),



  /**
   * '{'level'}'
   */
  INFO_PWMOD_ARG_PLACEHOLDER_LEVEL("'{'level'}'"),



  /**
   * '{'name=value'}'
   */
  INFO_PWMOD_ARG_PLACEHOLDER_NAME_VALUE("'{'name=value'}'"),



  /**
   * '{'password'}'
   */
  INFO_PWMOD_ARG_PLACEHOLDER_PASSWORD("'{'password'}'"),



  /**
   * '{'purpose'}'
   */
  INFO_PWMOD_ARG_PLACEHOLDER_PURPOSE("'{'purpose'}'"),



  /**
   * '{'timeout'}'
   */
  INFO_PWMOD_ARG_PLACEHOLDER_TIMEOUT("'{'timeout'}'"),



  /**
   * The {0} tool generated a new password of ''{1}''.
   */
  INFO_PWMOD_CLIENT_SIDE_GEN_PW("The {0} tool generated a new password of ''{1}''."),



  /**
   * Confirm the new password:
   */
  INFO_PWMOD_CONFIRM_NEW_PW("Confirm the new password:"),



  /**
   * The server root DSE does not claim to support the password modify extended operation, and it does not appear to be an Active Directory instance.  Selecting a regular LDAP modify operation for the password update.
   */
  INFO_PWMOD_DEFAULTING_TO_LDAP_MOD("The server root DSE does not claim to support the password modify extended operation, and it does not appear to be an Active Directory instance.  Selecting a regular LDAP modify operation for the password update."),



  /**
   * Perform a self password change as the user with a username of ''jdoe'', with both the current and new passwords obtained from interactive prompting.  The tool will automatically determine the best method to use to change the password.
   */
  INFO_PWMOD_EXAMPLE_1("Perform a self password change as the user with a username of ''jdoe'', with both the current and new passwords obtained from interactive prompting.  The tool will automatically determine the best method to use to change the password."),



  /**
   * Use a regular LDAP modify operation to perform an administrative reset of the password for user ''uid=jdoe,ou=People,dc=example,dc=com''.  The tool will generate a new password for the user.
   */
  INFO_PWMOD_EXAMPLE_2("Use a regular LDAP modify operation to perform an administrative reset of the password for user ''uid=jdoe,ou=People,dc=example,dc=com''.  The tool will generate a new password for the user."),



  /**
   * The password was not actually updated because the password modify extended request included the no-operation request control, but the server reported that the operation would have likely succeeded if that control had not been provided.
   */
  INFO_PWMOD_EXTOP_NO_OP("The password was not actually updated because the password modify extended request included the no-operation request control, but the server reported that the operation would have likely succeeded if that control had not been provided."),



  /**
   * # Password Modify Extended Result:
   */
  INFO_PWMOD_EXTOP_RESULT_HEADER("# Password Modify Extended Result:"),



  /**
   * Successfully changed the password using a password modify extended operation.
   */
  INFO_PWMOD_EXTOP_SUCCESSFUL("Successfully changed the password using a password modify extended operation."),



  /**
   * Issuing search request {0} to determine the entry DN that corresponds to username ''{1}''.
   */
  INFO_PWMOD_ISSUING_SEARCH_FOR_USER("Issuing search request {0} to determine the entry DN that corresponds to username ''{1}''."),



  /**
   * The password was not actually updated because the modify request included the no-operation request control, but the server reported that the operation would have likely succeeded if that control had not been provided.
   */
  INFO_PWMOD_MODIFY_NO_OP("The password was not actually updated because the modify request included the no-operation request control, but the server reported that the operation would have likely succeeded if that control had not been provided."),



  /**
   * # Modify Result:
   */
  INFO_PWMOD_MODIFY_RESULT_HEADER("# Modify Result:"),



  /**
   * Successfully changed the password using an LDAP modify operation.
   */
  INFO_PWMOD_MODIFY_SUCCESSFUL("Successfully changed the password using an LDAP modify operation."),



  /**
   * Omitting the user identity from the password modify extended request.
   */
  INFO_PWMOD_OMITTING_USER_IDENTITY_FROM_EXTOP("Omitting the user identity from the password modify extended request."),



  /**
   * Enter the current password for the user:
   */
  INFO_PWMOD_PROMPT_CURRENT_PW("Enter the current password for the user:"),



  /**
   * Enter the new password for the user:
   */
  INFO_PWMOD_PROMPT_NEW_PW("Enter the new password for the user:"),



  /**
   * The server root DSE reports a vendor version of ''{0}''.  Selecting an Active Directory-specific method for the password update.
   */
  INFO_PWMOD_SELECTING_AD_METHOD_AD_VERSION("The server root DSE reports a vendor version of ''{0}''.  Selecting an Active Directory-specific method for the password update."),



  /**
   * The server root DSE reports that it supports {0,number,0} supported controls below Microsoft''s base OID of ''{1}''.  Assuming that it''s an Active Directory instance and selecting an Active Directory-specific method for the password update.
   */
  INFO_PWMOD_SELECTING_AD_METHOD_CONTROL_COUNT("The server root DSE reports that it supports {0,number,0} supported controls below Microsoft''s base OID of ''{1}''.  Assuming that it''s an Active Directory instance and selecting an Active Directory-specific method for the password update."),



  /**
   * The server root DSE advertises support for the password modify extended operation.  Selecting that method for the password update.
   */
  INFO_PWMOD_SELECTING_PW_MOD_EXTOP_METHOD("The server root DSE advertises support for the password modify extended operation.  Selecting that method for the password update."),



  /**
   * The server generated a new password of ''{0}''.
   */
  INFO_PWMOD_SERVER_GENERATED_PW("The server generated a new password of ''{0}''."),



  /**
   * Update the password for a user in an LDAP directory server using the password modify extended operation (as defined in RFC 3062), a standard LDAP modify operation, or an Active Directory-specific modification.
   */
  INFO_PWMOD_TOOL_DESCRIPTION_1("Update the password for a user in an LDAP directory server using the password modify extended operation (as defined in RFC 3062), a standard LDAP modify operation, or an Active Directory-specific modification."),



  /**
   * Unless the password change method is explicitly specified (using the --passwordChangeMethod argument), this tool will attempt to automatically determine which method is the most appropriate for the target server using information provided in the server''s root DSE.  If the server advertises support for the password modify extended operation, then that method will be used.  If it appears to be an Active Directory server, then an Active Directory-specific password change method will be selected, using a regular LDAP modify to update the unicodePwd attribute with a specially encoded value.  Otherwise, a regular LDAP modify operation will be used to update the value of a specified password attribute.
   */
  INFO_PWMOD_TOOL_DESCRIPTION_2("Unless the password change method is explicitly specified (using the --passwordChangeMethod argument), this tool will attempt to automatically determine which method is the most appropriate for the target server using information provided in the server''s root DSE.  If the server advertises support for the password modify extended operation, then that method will be used.  If it appears to be an Active Directory server, then an Active Directory-specific password change method will be selected, using a regular LDAP modify to update the unicodePwd attribute with a specially encoded value.  Otherwise, a regular LDAP modify operation will be used to update the value of a specified password attribute."),



  /**
   * The new password to set for the user may be specified in one of several ways.  It may be directly provided on the command line, read from a specified file, interactively prompted from the user, or automatically generated by this tool.  If the new password is not specified using any of those methods, and if the password is to be updated using the password modify extended operation, then the new password field of the request will be left blank to indicate that the server should generate a new password for the user and include it in the response to the client.  If no new password is specified and some other password change method is selected, then the tool will exit with an error.
   */
  INFO_PWMOD_TOOL_DESCRIPTION_3("The new password to set for the user may be specified in one of several ways.  It may be directly provided on the command line, read from a specified file, interactively prompted from the user, or automatically generated by this tool.  If the new password is not specified using any of those methods, and if the password is to be updated using the password modify extended operation, then the new password field of the request will be left blank to indicate that the server should generate a new password for the user and include it in the response to the client.  If no new password is specified and some other password change method is selected, then the tool will exit with an error."),



  /**
   * The current password for the user may also be specified.  This is optional, although some servers may require a user to provide their current password when setting a new one.  If a current password is provided (whether given as a command-line argument, read from a specified file, or interactively requested from the user), and if a regular LDAP modify operation is used to change the password, then the resulting modify request will include a delete of the current value and an add of the new value.  If no current password is provided, then the modify request will replace any existing password(s) with the new value.
   */
  INFO_PWMOD_TOOL_DESCRIPTION_4("The current password for the user may also be specified.  This is optional, although some servers may require a user to provide their current password when setting a new one.  If a current password is provided (whether given as a command-line argument, read from a specified file, or interactively requested from the user), and if a regular LDAP modify operation is used to change the password, then the resulting modify request will include a delete of the current value and an add of the new value.  If no current password is provided, then the modify request will replace any existing password(s) with the new value."),



  /**
   * Using DN ''{0}'' as the user identity for the modify operation that will be used to update the password.
   */
  INFO_PWMOD_USER_IDENTITY_DN_FOR_MOD("Using DN ''{0}'' as the user identity for the modify operation that will be used to update the password."),



  /**
   * Using bind DN value ''{0}'' as the user identity for the password modify extended request.
   */
  INFO_PWMOD_USING_USER_IDENTITY_FROM_DN_FOR_EXTOP("Using bind DN value ''{0}'' as the user identity for the password modify extended request."),



  /**
   * Bind Result:
   */
  INFO_REPORT_BIND_RESULT_HEADER("Bind Result:"),



  /**
   * Aborted Transaction Unsolicited Notification
   */
  INFO_RESULT_UTILS_ABORTED_TXN_HEADER("Aborted Transaction Unsolicited Notification"),



  /**
   * Account Usable Response Control:
   */
  INFO_RESULT_UTILS_ACCOUNT_USABLE_HEADER("Account Usable Response Control:"),



  /**
   * Account Is Inactive:  {0}
   */
  INFO_RESULT_UTILS_ACCOUNT_USABLE_IS_INACTIVE("Account Is Inactive:  {0}"),



  /**
   * Account Is Usable:  {0}
   */
  INFO_RESULT_UTILS_ACCOUNT_USABLE_IS_USABLE("Account Is Usable:  {0}"),



  /**
   * Must Change Password:  {0}
   */
  INFO_RESULT_UTILS_ACCOUNT_USABLE_MUST_CHANGE_PW("Must Change Password:  {0}"),



  /**
   * Password Is Expired:  {0}
   */
  INFO_RESULT_UTILS_ACCOUNT_USABLE_PW_EXPIRED("Password Is Expired:  {0}"),



  /**
   * Remaining Grace Logins:  {0,number,0}
   */
  INFO_RESULT_UTILS_ACCOUNT_USABLE_REMAINING_GRACE("Remaining Grace Logins:  {0,number,0}"),



  /**
   * Seconds Until Password Expiration:  {0,number,0}
   */
  INFO_RESULT_UTILS_ACCOUNT_USABLE_SECONDS_UNTIL_EXPIRATION("Seconds Until Password Expiration:  {0,number,0}"),



  /**
   * Seconds Until Account Unlock:  {0,number,0}
   */
  INFO_RESULT_UTILS_ACCOUNT_USABLE_SECONDS_UNTIL_UNLOCK("Seconds Until Account Unlock:  {0,number,0}"),



  /**
   * Unusable Reasons:
   */
  INFO_RESULT_UTILS_ACCOUNT_USABLE_UNUSABLE_REASONS_HEADER("Unusable Reasons:"),



  /**
   * Change Sequence Number:  {0}
   */
  INFO_RESULT_UTILS_ASSURED_REPL_CSN("Change Sequence Number:  {0}"),



  /**
   * Assured Replication Response Control:
   */
  INFO_RESULT_UTILS_ASSURED_REPL_HEADER("Assured Replication Response Control:"),



  /**
   * Local Assurance Level:  {0}
   */
  INFO_RESULT_UTILS_ASSURED_REPL_LOCAL_LEVEL("Local Assurance Level:  {0}"),



  /**
   * Local Assurance Message:  {0}
   */
  INFO_RESULT_UTILS_ASSURED_REPL_LOCAL_MESSAGE("Local Assurance Message:  {0}"),



  /**
   * Local Assurance Satisfied:  {0}
   */
  INFO_RESULT_UTILS_ASSURED_REPL_LOCAL_SATISFIED("Local Assurance Satisfied:  {0}"),



  /**
   * Remote Assurance Level:  {0}
   */
  INFO_RESULT_UTILS_ASSURED_REPL_REMOTE_LEVEL("Remote Assurance Level:  {0}"),



  /**
   * Remote Assurance Message:  {0}
   */
  INFO_RESULT_UTILS_ASSURED_REPL_REMOTE_MESSAGE("Remote Assurance Message:  {0}"),



  /**
   * Remote Assurance Satisfied:  {0}
   */
  INFO_RESULT_UTILS_ASSURED_REPL_REMOTE_SATISFIED("Remote Assurance Satisfied:  {0}"),



  /**
   * Server Result Code:  {0}
   */
  INFO_RESULT_UTILS_ASSURED_REPL_SERVER_RESULT_CODE("Server Result Code:  {0}"),



  /**
   * Server Result:
   */
  INFO_RESULT_UTILS_ASSURED_REPL_SERVER_RESULT_HEADER("Server Result:"),



  /**
   * Replica ID:  {0,number,0}
   */
  INFO_RESULT_UTILS_ASSURED_REPL_SERVER_RESULT_REPL_ID("Replica ID:  {0,number,0}"),



  /**
   * Replication Server ID:  {0,number,0}
   */
  INFO_RESULT_UTILS_ASSURED_REPL_SERVER_RESULT_REPL_SERVER_ID("Replication Server ID:  {0,number,0}"),



  /**
   * Authorization Identity Response Control:
   */
  INFO_RESULT_UTILS_AUTHZ_ID_RESPONSE_HEADER("Authorization Identity Response Control:"),



  /**
   * Authorization ID:  {0}
   */
  INFO_RESULT_UTILS_AUTHZ_ID_RESPONSE_ID("Authorization ID:  {0}"),



  /**
   * Cookie Data:
   */
  INFO_RESULT_UTILS_CONTENT_SYNC_DONE_COOKIE_HEADER("Cookie Data:"),



  /**
   * Refresh Deletes:  {0}
   */
  INFO_RESULT_UTILS_CONTENT_SYNC_DONE_REFRESH_DELETES("Refresh Deletes:  {0}"),



  /**
   * Content Synchronization Done Response Control:
   */
  INFO_RESULT_UTILS_CONTENT_SYNC_DONE_RESPONSE_HEADER("Content Synchronization Done Response Control:"),



  /**
   * Cookie Data:
   */
  INFO_RESULT_UTILS_CONTENT_SYNC_STATE_COOKIE_HEADER("Cookie Data:"),



  /**
   * Entry UUID:  {0}
   */
  INFO_RESULT_UTILS_CONTENT_SYNC_STATE_ENTRY_UUID("Entry UUID:  {0}"),



  /**
   * Synchronization State:  {0}
   */
  INFO_RESULT_UTILS_CONTENT_SYNC_STATE_NAME("Synchronization State:  {0}"),



  /**
   * Content Synchronization State Response Control:
   */
  INFO_RESULT_UTILS_CONTENT_SYNC_STATE_RESPONSE_HEADER("Content Synchronization State Response Control:"),



  /**
   * Diagnostic Message:  {0}
   */
  INFO_RESULT_UTILS_DIAGNOSTIC_MESSAGE("Diagnostic Message:  {0}"),



  /**
   * Change Number:  {0,number,0}
   */
  INFO_RESULT_UTILS_ECN_CHANGE_NUMBER("Change Number:  {0,number,0}"),



  /**
   * Change Type:  {0}
   */
  INFO_RESULT_UTILS_ECN_CHANGE_TYPE("Change Type:  {0}"),



  /**
   * Entry Change Notification Control:
   */
  INFO_RESULT_UTILS_ECN_HEADER("Entry Change Notification Control:"),



  /**
   * Previous DN:  {0}
   */
  INFO_RESULT_UTILS_ECN_PREVIOUS_DN("Previous DN:  {0}"),



  /**
   * End Transaction Extended Result Failed Operation Message ID:  {0}
   */
  INFO_RESULT_UTILS_END_TXN_RESULT_FAILED_MSG_ID("End Transaction Extended Result Failed Operation Message ID:  {0}"),



  /**
   * End Transaction Extended Result Response Control for Message ID {0}:
   */
  INFO_RESULT_UTILS_END_TXN_RESULT_OP_CONTROL("End Transaction Extended Result Response Control for Message ID {0}:"),



  /**
   * Generate Password Response Control:
   */
  INFO_RESULT_UTILS_GENERATE_PW_HEADER("Generate Password Response Control:"),



  /**
   * Must Change Password:  {0}
   */
  INFO_RESULT_UTILS_GENERATE_PW_MUST_CHANGE("Must Change Password:  {0}"),



  /**
   * Generated Password:  {0}
   */
  INFO_RESULT_UTILS_GENERATE_PW_PASSWORD("Generated Password:  {0}"),



  /**
   * Seconds Until Expiration:  {0,number,0}
   */
  INFO_RESULT_UTILS_GENERATE_PW_SECONDS_UNTIL_EXPIRATION("Seconds Until Expiration:  {0,number,0}"),



  /**
   * Response Control:
   */
  INFO_RESULT_UTILS_GENERIC_RESPONSE_CONTROL_HEADER("Response Control:"),



  /**
   * Authentication Identity Entry:
   */
  INFO_RESULT_UTILS_GET_AUTHZ_ENTRY_AUTHN_ENTRY_HEADER("Authentication Identity Entry:"),



  /**
   * Authentication Identity ID:  {0}
   */
  INFO_RESULT_UTILS_GET_AUTHZ_ENTRY_AUTHN_ID("Authentication Identity ID:  {0}"),



  /**
   * Authorization Identity Entry:
   */
  INFO_RESULT_UTILS_GET_AUTHZ_ENTRY_AUTHZ_ENTRY_HEADER("Authorization Identity Entry:"),



  /**
   * Authorization Identity ID:  {0}
   */
  INFO_RESULT_UTILS_GET_AUTHZ_ENTRY_AUTHZ_ID("Authorization Identity ID:  {0}"),



  /**
   * Get Authorization Entry Response Control:
   */
  INFO_RESULT_UTILS_GET_AUTHZ_ENTRY_HEADER("Get Authorization Entry Response Control:"),



  /**
   * Authentication and Authorization Identities Match:  {0}
   */
  INFO_RESULT_UTILS_GET_AUTHZ_ENTRY_IDS_MATCH("Authentication and Authorization Identities Match:  {0}"),



  /**
   * Is Authenticated:  {0}
   */
  INFO_RESULT_UTILS_GET_AUTHZ_ENTRY_IS_AUTHENTICATED("Is Authenticated:  {0}"),



  /**
   * Backend Set ID:  {0}
   */
  INFO_RESULT_UTILS_GET_BACKEND_SET_ID("Backend Set ID:  {0}"),



  /**
   * Entry-Balancing Request Processor ID:  {0}
   */
  INFO_RESULT_UTILS_GET_BACKEND_SET_ID_EB_RP_ID("Entry-Balancing Request Processor ID:  {0}"),



  /**
   * Get Backend Set ID Response Control:
   */
  INFO_RESULT_UTILS_GET_BACKEND_SET_ID_HEADER("Get Backend Set ID Response Control:"),



  /**
   * Account Usability Error:
   */
  INFO_RESULT_UTILS_GET_PW_STATE_ISSUES_ERROR_HEADER("Account Usability Error:"),



  /**
   * Error Message:  {0}
   */
  INFO_RESULT_UTILS_GET_PW_STATE_ISSUES_ERROR_MESSAGE("Error Message:  {0}"),



  /**
   * Error Name:  {0}
   */
  INFO_RESULT_UTILS_GET_PW_STATE_ISSUES_ERROR_NAME("Error Name:  {0}"),



  /**
   * Failure Message:  {0}
   */
  INFO_RESULT_UTILS_GET_PW_STATE_ISSUES_FAILURE_MESSAGE("Failure Message:  {0}"),



  /**
   * Authentication Failure Reason:
   */
  INFO_RESULT_UTILS_GET_PW_STATE_ISSUES_FAILURE_REASON_HEADER("Authentication Failure Reason:"),



  /**
   * Failure Type:  {0}
   */
  INFO_RESULT_UTILS_GET_PW_STATE_ISSUES_FAILURE_TYPE("Failure Type:  {0}"),



  /**
   * Get Password Policy State Issues Response Control:
   */
  INFO_RESULT_UTILS_GET_PW_STATE_ISSUES_HEADER("Get Password Policy State Issues Response Control:"),



  /**
   * Account Usability Notice:
   */
  INFO_RESULT_UTILS_GET_PW_STATE_ISSUES_NOTICE_HEADER("Account Usability Notice:"),



  /**
   * Notice Message:  {0}
   */
  INFO_RESULT_UTILS_GET_PW_STATE_ISSUES_NOTICE_MESSAGE("Notice Message:  {0}"),



  /**
   * Notice Name:  {0}
   */
  INFO_RESULT_UTILS_GET_PW_STATE_ISSUES_NOTICE_NAME("Notice Name:  {0}"),



  /**
   * Account Usability Warning:
   */
  INFO_RESULT_UTILS_GET_PW_STATE_ISSUES_WARNING_HEADER("Account Usability Warning:"),



  /**
   * Warning Message:  {0}
   */
  INFO_RESULT_UTILS_GET_PW_STATE_ISSUES_WARNING_MESSAGE("Warning Message:  {0}"),



  /**
   * Warning Name:  {0}
   */
  INFO_RESULT_UTILS_GET_PW_STATE_ISSUES_WARNING_NAME("Warning Name:  {0}"),



  /**
   * Additional Attempt Count:  {0,number,0}
   */
  INFO_RESULT_UTILS_GET_RECENT_LOGIN_HISTORY_ADDITIONAL_COUNT("Additional Attempt Count:  {0,number,0}"),



  /**
   * Authentication Method:  {0}
   */
  INFO_RESULT_UTILS_GET_RECENT_LOGIN_HISTORY_AUTH_METHOD("Authentication Method:  {0}"),



  /**
   * Client IP Address:  {0}
   */
  INFO_RESULT_UTILS_GET_RECENT_LOGIN_HISTORY_CLIENT_IP("Client IP Address:  {0}"),



  /**
   * Failed Attempt:
   */
  INFO_RESULT_UTILS_GET_RECENT_LOGIN_HISTORY_FAILURE_HEADER("Failed Attempt:"),



  /**
   * Failure Reason:  {0}
   */
  INFO_RESULT_UTILS_GET_RECENT_LOGIN_HISTORY_FAILURE_REASON("Failure Reason:  {0}"),



  /**
   * Get Recent Login History Response Control:
   */
  INFO_RESULT_UTILS_GET_RECENT_LOGIN_HISTORY_HEADER("Get Recent Login History Response Control:"),



  /**
   * No Failed Attempts
   */
  INFO_RESULT_UTILS_GET_RECENT_LOGIN_HISTORY_NO_FAILURES("No Failed Attempts"),



  /**
   * No Successful Attempts
   */
  INFO_RESULT_UTILS_GET_RECENT_LOGIN_HISTORY_NO_SUCCESSES("No Successful Attempts"),



  /**
   * Successful Attempt:
   */
  INFO_RESULT_UTILS_GET_RECENT_LOGIN_HISTORY_SUCCESS_HEADER("Successful Attempt:"),



  /**
   * Timestamp:  {0}
   */
  INFO_RESULT_UTILS_GET_RECENT_LOGIN_HISTORY_TIMESTAMP("Timestamp:  {0}"),



  /**
   * Server ID:  {0}
   */
  INFO_RESULT_UTILS_GET_SERVER_ID("Server ID:  {0}"),



  /**
   * Get Server ID Response Control:
   */
  INFO_RESULT_UTILS_GET_SERVER_ID_HEADER("Get Server ID Response Control:"),



  /**
   * Client Connection Policy Name:  {0}
   */
  INFO_RESULT_UTILS_GET_USER_RLIM_CCP_NAME("Client Connection Policy Name:  {0}"),



  /**
   * Equivalent Authorization User DN:  {0}
   */
  INFO_RESULT_UTILS_GET_USER_RLIM_EQUIVALENT_AUTHZ_USER_DN("Equivalent Authorization User DN:  {0}"),



  /**
   * Group DNs:
   */
  INFO_RESULT_UTILS_GET_USER_RLIM_GROUP_DNS_HEADER("Group DNs:"),



  /**
   * Get User Resource Limits Response Control:
   */
  INFO_RESULT_UTILS_GET_USER_RLIM_HEADER("Get User Resource Limits Response Control:"),



  /**
   * Idle Time Limit:  {0}
   */
  INFO_RESULT_UTILS_GET_USER_RLIM_IDLE_TIME_LIMIT("Idle Time Limit:  {0}"),



  /**
   * Lookthrough Limit:  {0}
   */
  INFO_RESULT_UTILS_GET_USER_RLIM_LOOKTHROUGH_LIMIT("Lookthrough Limit:  {0}"),



  /**
   * Other Attributes:
   */
  INFO_RESULT_UTILS_GET_USER_RLIM_OTHER_ATTRIBUTES_HEADER("Other Attributes:"),



  /**
   * Privileges:
   */
  INFO_RESULT_UTILS_GET_USER_RLIM_PRIVILEGES_HEADER("Privileges:"),



  /**
   * Size Limit:  {0}
   */
  INFO_RESULT_UTILS_GET_USER_RLIM_SIZE_LIMIT("Size Limit:  {0}"),



  /**
   * Time Limit:  {0}
   */
  INFO_RESULT_UTILS_GET_USER_RLIM_TIME_LIMIT("Time Limit:  {0}"),



  /**
   * seconds
   */
  INFO_RESULT_UTILS_GET_USER_RLIM_UNIT_SECONDS("seconds"),



  /**
   * Unlimited
   */
  INFO_RESULT_UTILS_GET_USER_RLIM_VALUE_UNLIMITED("Unlimited"),



  /**
   * Intermediate Client Response Control:
   */
  INFO_RESULT_UTILS_INTERMEDIATE_CLIENT_HEADER("Intermediate Client Response Control:"),



  /**
   * Server Response ID:  {0}
   */
  INFO_RESULT_UTILS_INTERMEDIATE_CLIENT_RESPONSE_ID("Server Response ID:  {0}"),



  /**
   * Server Name:  {0}
   */
  INFO_RESULT_UTILS_INTERMEDIATE_CLIENT_SERVER_NAME("Server Name:  {0}"),



  /**
   * Server Session ID:  {0}
   */
  INFO_RESULT_UTILS_INTERMEDIATE_CLIENT_SESSION_ID("Server Session ID:  {0}"),



  /**
   * Upstream Server Address:  {0}
   */
  INFO_RESULT_UTILS_INTERMEDIATE_CLIENT_UPSTREAM_ADDRESS("Upstream Server Address:  {0}"),



  /**
   * Upstream Response:
   */
  INFO_RESULT_UTILS_INTERMEDIATE_CLIENT_UPSTREAM_RESPONSE_HEADER("Upstream Response:"),



  /**
   * Upstream Server Secure:  {0}
   */
  INFO_RESULT_UTILS_INTERMEDIATE_CLIENT_UPSTREAM_SECURE("Upstream Server Secure:  {0}"),



  /**
   * Joined With Entry:
   */
  INFO_RESULT_UTILS_JOINED_WITH_ENTRY_HEADER("Joined With Entry:"),



  /**
   * Join Diagnostic Message:  {0}
   */
  INFO_RESULT_UTILS_JOIN_DIAGNOSTIC_MESSAGE("Join Diagnostic Message:  {0}"),



  /**
   * Join Result Control:
   */
  INFO_RESULT_UTILS_JOIN_HEADER("Join Result Control:"),



  /**
   * Join Matched DN:  {0}
   */
  INFO_RESULT_UTILS_JOIN_MATCHED_DN("Join Matched DN:  {0}"),



  /**
   * Join Referral URL:  {0}
   */
  INFO_RESULT_UTILS_JOIN_REFERRAL_URL("Join Referral URL:  {0}"),



  /**
   * Join Result Code:  {0}
   */
  INFO_RESULT_UTILS_JOIN_RESULT_CODE("Join Result Code:  {0}"),



  /**
   * Matched DN:  {0}
   */
  INFO_RESULT_UTILS_MATCHED_DN("Matched DN:  {0}"),



  /**
   * Debug Info:
   */
  INFO_RESULT_UTILS_MATCHING_ENTRY_COUNT_DEBUG_HEADER("Debug Info:"),



  /**
   * Matching Entry Count Response Control:
   */
  INFO_RESULT_UTILS_MATCHING_ENTRY_COUNT_HEADER("Matching Entry Count Response Control:"),



  /**
   * Search Is Indexed:  {0}
   */
  INFO_RESULT_UTILS_MATCHING_ENTRY_COUNT_INDEXED("Search Is Indexed:  {0}"),



  /**
   * Count Type:  Examined
   */
  INFO_RESULT_UTILS_MATCHING_ENTRY_COUNT_TYPE_EXAMINED("Count Type:  Examined"),



  /**
   * Count Type:  Unexamined
   */
  INFO_RESULT_UTILS_MATCHING_ENTRY_COUNT_TYPE_UNEXAMINED("Count Type:  Unexamined"),



  /**
   * Count Type:  Unknown
   */
  INFO_RESULT_UTILS_MATCHING_ENTRY_COUNT_TYPE_UNKNOWN("Count Type:  Unknown"),



  /**
   * Count Type:  Upper Bound
   */
  INFO_RESULT_UTILS_MATCHING_ENTRY_COUNT_TYPE_UPPER_BOUND("Count Type:  Upper Bound"),



  /**
   * Count Value:  {0,number,0}
   */
  INFO_RESULT_UTILS_MATCHING_ENTRY_COUNT_VALUE("Count Value:  {0,number,0}"),



  /**
   * Multi-Update Changes Applied:  {0}
   */
  INFO_RESULT_UTILS_MULTI_UPDATE_CHANGES_APPLIED("Multi-Update Changes Applied:  {0}"),



  /**
   * Multi-Update {0} Operation Result:
   */
  INFO_RESULT_UTILS_MULTI_UPDATE_RESULT_HEADER("Multi-Update {0} Operation Result:"),



  /**
   * Notice of Disconnection Unsolicited Notification
   */
  INFO_RESULT_UTILS_NOTICE_OF_DISCONNECTION_HEADER("Notice of Disconnection Unsolicited Notification"),



  /**
   * Number of Entries Returned:  {0,number,0}
   */
  INFO_RESULT_UTILS_NUM_SEARCH_ENTRIES("Number of Entries Returned:  {0,number,0}"),



  /**
   * Number of References Returned:  {0,number,0}
   */
  INFO_RESULT_UTILS_NUM_SEARCH_REFERENCES("Number of References Returned:  {0,number,0}"),



  /**
   * Cookie Data:
   */
  INFO_RESULT_UTILS_PAGED_RESULTS_COOKIE_HEADER("Cookie Data:"),



  /**
   * Estimated Total Result Set Size:  {0,number,0}
   */
  INFO_RESULT_UTILS_PAGED_RESULTS_COUNT("Estimated Total Result Set Size:  {0,number,0}"),



  /**
   * Simple Paged Results Response Control:
   */
  INFO_RESULT_UTILS_PAGED_RESULTS_HEADER("Simple Paged Results Response Control:"),



  /**
   * Password Expired Response Control:
   */
  INFO_RESULT_UTILS_PASSWORD_EXPIRED_HEADER("Password Expired Response Control:"),



  /**
   * Password Expiring Response Control:
   */
  INFO_RESULT_UTILS_PASSWORD_EXPIRING_HEADER("Password Expiring Response Control:"),



  /**
   * Seconds Until Expiration:  {0,number,0}
   */
  INFO_RESULT_UTILS_PASSWORD_EXPIRING_SECONDS_UNTIL_EXPIRATION("Seconds Until Expiration:  {0,number,0}"),



  /**
   * Password Modify Extended Result Generated Password:  {0}
   */
  INFO_RESULT_UTILS_PASSWORD_MODIFY_RESULT_GENERATED_PW("Password Modify Extended Result Generated Password:  {0}"),



  /**
   * Post-Read Entry:
   */
  INFO_RESULT_UTILS_POST_READ_ENTRY_HEADER("Post-Read Entry:"),



  /**
   * Post-Read Response Control:
   */
  INFO_RESULT_UTILS_POST_READ_HEADER("Post-Read Response Control:"),



  /**
   * Pre-Read Entry:
   */
  INFO_RESULT_UTILS_PRE_READ_ENTRY_HEADER("Pre-Read Entry:"),



  /**
   * Pre-Read Response Control:
   */
  INFO_RESULT_UTILS_PRE_READ_HEADER("Pre-Read Response Control:"),



  /**
   * Error Type:  {0}
   */
  INFO_RESULT_UTILS_PW_POLICY_ERROR_TYPE("Error Type:  {0}"),



  /**
   * Error Type:  None
   */
  INFO_RESULT_UTILS_PW_POLICY_ERROR_TYPE_NONE("Error Type:  None"),



  /**
   * Password Policy Response Control:
   */
  INFO_RESULT_UTILS_PW_POLICY_HEADER("Password Policy Response Control:"),



  /**
   * Warning Type:  {0}
   */
  INFO_RESULT_UTILS_PW_POLICY_WARNING_TYPE("Warning Type:  {0}"),



  /**
   * Warning Type:  None
   */
  INFO_RESULT_UTILS_PW_POLICY_WARNING_TYPE_NONE("Warning Type:  None"),



  /**
   * Warning Value:  {0,number,0}
   */
  INFO_RESULT_UTILS_PW_POLICY_WARNING_VALUE("Warning Value:  {0,number,0}"),



  /**
   * Password Validation Details Response Control:
   */
  INFO_RESULT_UTILS_PW_VALIDATION_DETAILS_HEADER("Password Validation Details Response Control:"),



  /**
   * Missing Current Password:  {0}
   */
  INFO_RESULT_UTILS_PW_VALIDATION_DETAILS_MISSING_CURRENT("Missing Current Password:  {0}"),



  /**
   * Must Change Password:  {0}
   */
  INFO_RESULT_UTILS_PW_VALIDATION_DETAILS_MUST_CHANGE("Must Change Password:  {0}"),



  /**
   * Password Quality Requirement Description:  {0}
   */
  INFO_RESULT_UTILS_PW_VALIDATION_DETAILS_PQR_DESC("Password Quality Requirement Description:  {0}"),



  /**
   * Password Quality Requirement Validation Result:
   */
  INFO_RESULT_UTILS_PW_VALIDATION_DETAILS_PQR_HEADER("Password Quality Requirement Validation Result:"),



  /**
   * Additional Validation Info:  {0}
   */
  INFO_RESULT_UTILS_PW_VALIDATION_DETAILS_PQR_INFO("Additional Validation Info:  {0}"),



  /**
   * Client-Side Validation Property:  {0}={1}
   */
  INFO_RESULT_UTILS_PW_VALIDATION_DETAILS_PQR_PROP("Client-Side Validation Property:  {0}={1}"),



  /**
   * Requirement Satisfied:  {0}
   */
  INFO_RESULT_UTILS_PW_VALIDATION_DETAILS_PQR_SATISFIED("Requirement Satisfied:  {0}"),



  /**
   * Client-Side Validation Type:  {0}
   */
  INFO_RESULT_UTILS_PW_VALIDATION_DETAILS_PQR_TYPE("Client-Side Validation Type:  {0}"),



  /**
   * Result Type:  {0}
   */
  INFO_RESULT_UTILS_PW_VALIDATION_DETAILS_RESULT_TYPE_DEFAULT("Result Type:  {0}"),



  /**
   * Result Type:  Multiple Passwords Provided
   */
  INFO_RESULT_UTILS_PW_VALIDATION_DETAILS_RESULT_TYPE_MULTIPLE_PW("Result Type:  Multiple Passwords Provided"),



  /**
   * Result Type:  No Password Provided
   */
  INFO_RESULT_UTILS_PW_VALIDATION_DETAILS_RESULT_TYPE_NO_PW("Result Type:  No Password Provided"),



  /**
   * Result Type:  No Validation Attempted
   */
  INFO_RESULT_UTILS_PW_VALIDATION_DETAILS_RESULT_TYPE_NO_VALIDATION("Result Type:  No Validation Attempted"),



  /**
   * Result Type:  Validation Result
   */
  INFO_RESULT_UTILS_PW_VALIDATION_DETAILS_RESULT_TYPE_RESULT("Result Type:  Validation Result"),



  /**
   * Seconds Until Expiration:  {0,number,0}
   */
  INFO_RESULT_UTILS_PW_VALIDATION_DETAILS_SECONDS_TO_EXP("Seconds Until Expiration:  {0,number,0}"),



  /**
   * Referral URL:  {0}
   */
  INFO_RESULT_UTILS_REFERRAL_URL("Referral URL:  {0}"),



  /**
   * Is Critical:  {0}
   */
  INFO_RESULT_UTILS_RESPONSE_CONTROL_IS_CRITICAL("Is Critical:  {0}"),



  /**
   * OID:  {0}
   */
  INFO_RESULT_UTILS_RESPONSE_CONTROL_OID("OID:  {0}"),



  /**
   * Raw Value:
   */
  INFO_RESULT_UTILS_RESPONSE_CONTROL_RAW_VALUE_HEADER("Raw Value:"),



  /**
   * Extended Result OID:  {0}
   */
  INFO_RESULT_UTILS_RESPONSE_EXTOP_OID("Extended Result OID:  {0}"),



  /**
   * Extended Result Raw Value:
   */
  INFO_RESULT_UTILS_RESPONSE_EXTOP_RAW_VALUE_HEADER("Extended Result Raw Value:"),



  /**
   * Result Code:  {0}
   */
  INFO_RESULT_UTILS_RESULT_CODE("Result Code:  {0}"),



  /**
   * Search Result Reference:
   */
  INFO_RESULT_UTILS_SEARCH_REFERENCE_HEADER("Search Result Reference:"),



  /**
   * Soft-Deleted Entry DN:  {0}
   */
  INFO_RESULT_UTILS_SOFT_DELETED_DN("Soft-Deleted Entry DN:  {0}"),



  /**
   * Soft Delete Response Control:
   */
  INFO_RESULT_UTILS_SOFT_DELETE_HEADER("Soft Delete Response Control:"),



  /**
   * Attribute Name:  {0}
   */
  INFO_RESULT_UTILS_SORT_ATTRIBUTE_NAME("Attribute Name:  {0}"),



  /**
   * Server-Side Sort Response Control:
   */
  INFO_RESULT_UTILS_SORT_HEADER("Server-Side Sort Response Control:"),



  /**
   * Result Code:  {0}
   */
  INFO_RESULT_UTILS_SORT_RESULT_CODE("Result Code:  {0}"),



  /**
   * Start Transaction Extended Result Transaction ID:  {0}
   */
  INFO_RESULT_UTILS_START_TXN_RESULT_TXN_ID("Start Transaction Extended Result Transaction ID:  {0}"),



  /**
   * NOTE:  No changes will actually be applied to the server until the transaction is committed.
   */
  INFO_RESULT_UTILS_SUCCESS_WITH_TXN("NOTE:  No changes will actually be applied to the server until the transaction is committed."),



  /**
   * Transaction ID:  {0}
   */
  INFO_RESULT_UTILS_TXN_ID_HEADER("Transaction ID:  {0}"),



  /**
   * Backend Lock Acquired:  {0}
   */
  INFO_RESULT_UTILS_TXN_SETTINGS_BACKEND_LOCK_ACQUIRED("Backend Lock Acquired:  {0}"),



  /**
   * Transaction Settings Response Control:
   */
  INFO_RESULT_UTILS_TXN_SETTINGS_HEADER("Transaction Settings Response Control:"),



  /**
   * Number of Lock Conflicts:  {0,number,0}
   */
  INFO_RESULT_UTILS_TXN_SETTINGS_NUM_CONFLICTS("Number of Lock Conflicts:  {0,number,0}"),



  /**
   * Uniqueness Response Control:
   */
  INFO_RESULT_UTILS_UNIQUENESS_HEADER("Uniqueness Response Control:"),



  /**
   * Uniqueness ID:  {0}
   */
  INFO_RESULT_UTILS_UNIQUENESS_ID("Uniqueness ID:  {0}"),



  /**
   * Message:  {0}
   */
  INFO_RESULT_UTILS_UNIQUENESS_MESSAGE("Message:  {0}"),



  /**
   * Post-Commit Validation Status:  {0}
   */
  INFO_RESULT_UTILS_UNIQUENESS_POST_COMMIT_STATUS("Post-Commit Validation Status:  {0}"),



  /**
   * Pre-Commit Validation Status:  {0}
   */
  INFO_RESULT_UTILS_UNIQUENESS_PRE_COMMIT_STATUS("Pre-Commit Validation Status:  {0}"),



  /**
   * Failed
   */
  INFO_RESULT_UTILS_UNIQUENESS_STATUS_VALUE_FAILED("Failed"),



  /**
   * Not Attempted
   */
  INFO_RESULT_UTILS_UNIQUENESS_STATUS_VALUE_NOT_ATTEMPTED("Not Attempted"),



  /**
   * Passed
   */
  INFO_RESULT_UTILS_UNIQUENESS_STATUS_VALUE_PASSED("Passed"),



  /**
   * Unsolicited Notification
   */
  INFO_RESULT_UTILS_UNSOLICITED_NOTIFICATION_HEADER("Unsolicited Notification"),



  /**
   * Estimated Content Count:  {0,number,0}
   */
  INFO_RESULT_UTILS_VLV_CONTENT_COUNT("Estimated Content Count:  {0,number,0}"),



  /**
   * Context ID:
   */
  INFO_RESULT_UTILS_VLV_CONTEXT_ID_HEADER("Context ID:"),



  /**
   * Virtual List View Response Control:
   */
  INFO_RESULT_UTILS_VLV_HEADER("Virtual List View Response Control:"),



  /**
   * Result Code:  {0}
   */
  INFO_RESULT_UTILS_VLV_RESULT_CODE("Result Code:  {0}"),



  /**
   * Target Position:  {0,number,0}
   */
  INFO_RESULT_UTILS_VLV_TARGET_POSITION("Target Position:  {0,number,0}"),



  /**
   * Wrote {0,number,0} entries to file {1}.
   */
  INFO_SPLIT_LDIF_COUNT_TO_FILE("Wrote {0,number,0} entries to file {1}."),



  /**
   * Excluded {0,number,0} entries that were not within the split base DN.
   */
  INFO_SPLIT_LDIF_EXCLUDED_COUNT("Excluded {0,number,0} entries that were not within the split base DN."),



  /**
   * Indicates that the target LDIF files should be gzip-compressed.
   */
  INFO_SPLIT_LDIF_GLOBAL_ARG_DESC_COMPRESS_TARGET("Indicates that the target LDIF files should be gzip-compressed."),



  /**
   * The path to the file containing the passphrase to use to generate the encryption key.  This passphrase will be used to decrypt the input (if it is encrypted) and to encrypt the output (if it is to be encrypted).  If this is not provided and either the input or output is encrypted, then the passphrase will be interactively requested.  If it is provided, then the specified file must exist and must contain exactly one line that is comprised entirely of the encryption passphrase.
   */
  INFO_SPLIT_LDIF_GLOBAL_ARG_DESC_ENCRYPT_PW_FILE("The path to the file containing the passphrase to use to generate the encryption key.  This passphrase will be used to decrypt the input (if it is encrypted) and to encrypt the output (if it is to be encrypted).  If this is not provided and either the input or output is encrypted, then the passphrase will be interactively requested.  If it is provided, then the specified file must exist and must contain exactly one line that is comprised entirely of the encryption passphrase."),



  /**
   * Indicates that the target LDIF files should be encrypted with a key generated from a provided passphrase.  If the --encryptionPassphraseFile argument is provided, then the passphrase will be read from the specified file.  Otherwise, it will be interactively requested from the user.
   */
  INFO_SPLIT_LDIF_GLOBAL_ARG_DESC_ENCRYPT_TARGET("Indicates that the target LDIF files should be encrypted with a key generated from a provided passphrase.  If the --encryptionPassphraseFile argument is provided, then the passphrase will be read from the specified file.  Otherwise, it will be interactively requested from the user."),



  /**
   * The number of threads to use when processing.  If this is not specified, a single thread will be used.
   */
  INFO_SPLIT_LDIF_GLOBAL_ARG_DESC_NUM_THREADS("The number of threads to use when processing.  If this is not specified, a single thread will be used."),



  /**
   * Indicates that entries outside the split base DN should be added to all sets containing split data.
   */
  INFO_SPLIT_LDIF_GLOBAL_ARG_DESC_OUTSIDE_TO_ALL_SETS("Indicates that entries outside the split base DN should be added to all sets containing split data."),



  /**
   * Indicates that entries outside the split base DN should be added to a dedicated set in its own file.
   */
  INFO_SPLIT_LDIF_GLOBAL_ARG_DESC_OUTSIDE_TO_DEDICATED_SET("Indicates that entries outside the split base DN should be added to a dedicated set in its own file."),



  /**
   * The path to a file or directory from which to read schema definitions to use when processing.  If the provided path is a file, then it must be an LDIF file containing the definitions to read.  If the provided path is a directory, then schema definitions will be read from all files with an extension of ''.ldif'' in that directory.  If this argument is provided multiple times, then the paths will be processed in the order they were provided on the command line.  If this argument is not provided, then a default standard schema will be used.
   */
  INFO_SPLIT_LDIF_GLOBAL_ARG_DESC_SCHEMA_PATH("The path to a file or directory from which to read schema definitions to use when processing.  If the provided path is a file, then it must be an LDIF file containing the definitions to read.  If the provided path is a directory, then schema definitions will be read from all files with an extension of ''.ldif'' in that directory.  If this argument is provided multiple times, then the paths will be processed in the order they were provided on the command line.  If this argument is not provided, then a default standard schema will be used."),



  /**
   * Indicates that the source LDIF files are gzip-compressed.
   */
  INFO_SPLIT_LDIF_GLOBAL_ARG_DESC_SOURCE_COMPRESSED("Indicates that the source LDIF files are gzip-compressed."),



  /**
   * The path to an LDIF file containing the data to be split into multiple sets.  This argument may be provided multiple times to specify multiple LDIF files, and the files will be processed in the order they were provided on the command line.
   */
  INFO_SPLIT_LDIF_GLOBAL_ARG_DESC_SOURCE_LDIF("The path to an LDIF file containing the data to be split into multiple sets.  This argument may be provided multiple times to specify multiple LDIF files, and the files will be processed in the order they were provided on the command line."),



  /**
   * The base DN below which entries should be split.  The entry with this DN will appear in all sets, but each entry below this base DN will appear in exactly one set.  This must be provided.
   */
  INFO_SPLIT_LDIF_GLOBAL_ARG_DESC_SPLIT_BASE_DN("The base DN below which entries should be split.  The entry with this DN will appear in all sets, but each entry below this base DN will appear in exactly one set.  This must be provided."),



  /**
   * The path and base name to use for the LDIF files containing the data that has been split.  The resulting LDIF files will use this path with different extensions (''.set1'' for the first set, ''.set2'' for the second set, and so on, and ''.outside-split-base-dn'' if a file is to be created to hold entries that exist outside of the split base DN).  If this is not provided, then the source LDIF path will be used as the base name.
   */
  INFO_SPLIT_LDIF_GLOBAL_ARG_DESC_TARGET_LDIF_BASE("The path and base name to use for the LDIF files containing the data that has been split.  The resulting LDIF files will use this path with different extensions (''.set1'' for the first set, ''.set2'' for the second set, and so on, and ''.outside-split-base-dn'' if a file is to be created to hold entries that exist outside of the split base DN).  If this is not provided, then the source LDIF path will be used as the base name."),



  /**
   * Processing complete.  Read {0,number,0} entries.
   */
  INFO_SPLIT_LDIF_PROCESSING_COMPLETE("Processing complete.  Read {0,number,0} entries."),



  /**
   * Processed {0,number,0} entries
   */
  INFO_SPLIT_LDIF_PROGRESS("Processed {0,number,0} entries"),



  /**
   * Indicates that the tool should assume that all entries below the split base DN will be exactly one level below that entry, so that it is not necessary to maintain a cache to determine where a subordinate entry''s parent resides.  If this argument is provided but one or more subordinate entries are found, then they will be added to a separate file so the appropriate set can be manually identified.
   */
  INFO_SPLIT_LDIF_SC_FEWEST_ENTRIES_ARG_DESC_ASSUME_FLAT_DIT("Indicates that the tool should assume that all entries below the split base DN will be exactly one level below that entry, so that it is not necessary to maintain a cache to determine where a subordinate entry''s parent resides.  If this argument is provided but one or more subordinate entries are found, then they will be added to a separate file so the appropriate set can be manually identified."),



  /**
   * The number of sets into which the data should be split.  This must be provided, and the value must be greater than or equal to two.
   */
  INFO_SPLIT_LDIF_SC_FEWEST_ENTRIES_ARG_DESC_NUM_SETS("The number of sets into which the data should be split.  This must be provided, and the value must be greater than or equal to two."),



  /**
   * Splits the data by selecting the set with the fewest number of entries.  When processing data in a flat DIT, this will essentially be a round-robin distribution.  When processing data in a DIT with branches of varying sizes, this can help ensure that the resulting LDIF files will have roughly the same number of entries (although running the tool with multiple threads may make this less accurate).  Unless the --assumeFlatDIT argument is provided, a cache will be used to ensure that subordinate entries are added into the same set as their parents.
   */
  INFO_SPLIT_LDIF_SC_FEWEST_ENTRIES_DESC("Splits the data by selecting the set with the fewest number of entries.  When processing data in a flat DIT, this will essentially be a round-robin distribution.  When processing data in a DIT with branches of varying sizes, this can help ensure that the resulting LDIF files will have roughly the same number of entries (although running the tool with multiple threads may make this less accurate).  Unless the --assumeFlatDIT argument is provided, a cache will be used to ensure that subordinate entries are added into the same set as their parents."),



  /**
   * Splits LDIF file ''whole.ldif'' into four sets, with names starting with ''split.ldif'', so that each entry immediately below ''ou=People,dc=example,dc=com'' will be placed in the set that currently has the fewest entries.
   */
  INFO_SPLIT_LDIF_SC_FEWEST_ENTRIES_EXAMPLE("Splits LDIF file ''whole.ldif'' into four sets, with names starting with ''split.ldif'', so that each entry immediately below ''ou=People,dc=example,dc=com'' will be placed in the set that currently has the fewest entries."),



  /**
   * Indicates that the tool should assume that all entries below the split base DN will be exactly one level below that entry, so that it is not necessary to maintain a cache to determine where a subordinate entry''s parent resides.  If this argument is provided but one or more subordinate entries are found, then they will be added to a separate file so the appropriate set can be manually identified.
   */
  INFO_SPLIT_LDIF_SC_FILTER_ARG_DESC_ASSUME_FLAT_DIT("Indicates that the tool should assume that all entries below the split base DN will be exactly one level below that entry, so that it is not necessary to maintain a cache to determine where a subordinate entry''s parent resides.  If this argument is provided but one or more subordinate entries are found, then they will be added to a separate file so the appropriate set can be manually identified."),



  /**
   * A filter to evaluate against entries exactly one level below the split base DN to determine which set should be used to hold the entry.  This argument should be provided multiple times, once for each set into which the data should be split, and the filters will be evaluated in the order they were provided on the command line until one is found that matches the target entry.
   */
  INFO_SPLIT_LDIF_SC_FILTER_ARG_DESC_FILTER("A filter to evaluate against entries exactly one level below the split base DN to determine which set should be used to hold the entry.  This argument should be provided multiple times, once for each set into which the data should be split, and the filters will be evaluated in the order they were provided on the command line until one is found that matches the target entry."),



  /**
   * Splits the data by using search filters to select the appropriate set.  The filters will be evaluated in the order they were provided on the command line, and the entry will be added to the first set for which a matching filter is found.  If the entry doesn''t match any of the provided filters, then the appropriate set will be determined by computing a hash on the RDN.  Unless the --assumeFlatDIT argument is provided, a cache will be used to ensure that subordinate entries are added into the same set as their parents.
   */
  INFO_SPLIT_LDIF_SC_FILTER_DESC("Splits the data by using search filters to select the appropriate set.  The filters will be evaluated in the order they were provided on the command line, and the entry will be added to the first set for which a matching filter is found.  If the entry doesn''t match any of the provided filters, then the appropriate set will be determined by computing a hash on the RDN.  Unless the --assumeFlatDIT argument is provided, a cache will be used to ensure that subordinate entries are added into the same set as their parents."),



  /**
   * Splits LDIF file ''whole.ldif'' into four sets, with names starting with ''split.ldif'', so that each entry immediately below ''ou=People,dc=example,dc=com'' will be selected by matching that entry against a search filter.  Entries matching ''(timeZone=Eastern)'' will go into the first set, entries matching ''(timeZone=Central)'' into the second set, ''(timeZone=Mountain)'' into the third set, and ''(timeZone=Pacific)'' into the fourth.  Any entries not matching any of those filters will be placed by computing a hash on its RDN.
   */
  INFO_SPLIT_LDIF_SC_FILTER_EXAMPLE("Splits LDIF file ''whole.ldif'' into four sets, with names starting with ''split.ldif'', so that each entry immediately below ''ou=People,dc=example,dc=com'' will be selected by matching that entry against a search filter.  Entries matching ''(timeZone=Eastern)'' will go into the first set, entries matching ''(timeZone=Central)'' into the second set, ''(timeZone=Mountain)'' into the third set, and ''(timeZone=Pacific)'' into the fourth.  Any entries not matching any of those filters will be placed by computing a hash on its RDN."),



  /**
   * Indicates that the hashing process should examine all values for the target attribute.  If this argument is not provided, then only the first value for the attribute will be used to determine the set in which an entry should be placed.
   */
  INFO_SPLIT_LDIF_SC_HASH_ON_ATTR_ARG_DESC_ALL_VALUES("Indicates that the hashing process should examine all values for the target attribute.  If this argument is not provided, then only the first value for the attribute will be used to determine the set in which an entry should be placed."),



  /**
   * Indicates that the tool should assume that all entries below the split base DN will be exactly one level below that entry, so that it is not necessary to maintain a cache to determine where a subordinate entry''s parent resides.  If this argument is provided but one or more subordinate entries are found, then they will be added to a separate file so the appropriate set can be manually identified.
   */
  INFO_SPLIT_LDIF_SC_HASH_ON_ATTR_ARG_DESC_ASSUME_FLAT_DIT("Indicates that the tool should assume that all entries below the split base DN will be exactly one level below that entry, so that it is not necessary to maintain a cache to determine where a subordinate entry''s parent resides.  If this argument is provided but one or more subordinate entries are found, then they will be added to a separate file so the appropriate set can be manually identified."),



  /**
   * The name or OID of the attribute for which to compute the hash.  This must be provided.
   */
  INFO_SPLIT_LDIF_SC_HASH_ON_ATTR_ARG_DESC_ATTR_NAME("The name or OID of the attribute for which to compute the hash.  This must be provided."),



  /**
   * The number of sets into which the data should be split.  This must be provided, and the value must be greater than or equal to two.
   */
  INFO_SPLIT_LDIF_SC_HASH_ON_ATTR_ARG_DESC_NUM_SETS("The number of sets into which the data should be split.  This must be provided, and the value must be greater than or equal to two."),



  /**
   * Splits the data by computing a hash on the value(s) of a specified attribute in entries immediately below the split base DN, and using a modulus operation to determine the set in which to place the entry.  If an entry does not contain the target attribute, the set will be chosen based on a hash of the DN component immediately below the split base DN.  Unless the --assumeFlatDIT argument is provided, a cache will be used to ensure that subordinate entries are added into the same set as their parents.
   */
  INFO_SPLIT_LDIF_SC_HASH_ON_ATTR_DESC("Splits the data by computing a hash on the value(s) of a specified attribute in entries immediately below the split base DN, and using a modulus operation to determine the set in which to place the entry.  If an entry does not contain the target attribute, the set will be chosen based on a hash of the DN component immediately below the split base DN.  Unless the --assumeFlatDIT argument is provided, a cache will be used to ensure that subordinate entries are added into the same set as their parents."),



  /**
   * Splits LDIF file ''whole.ldif'' so that entries below ''ou=People,dc=example,dc=com'' will be split into four sets, selected by computing a digest of the first value of the ''uid'' attribute, with names starting with ''split.ldif''.
   */
  INFO_SPLIT_LDIF_SC_HASH_ON_ATTR_EXAMPLE("Splits LDIF file ''whole.ldif'' so that entries below ''ou=People,dc=example,dc=com'' will be split into four sets, selected by computing a digest of the first value of the ''uid'' attribute, with names starting with ''split.ldif''."),



  /**
   * The number of sets into which the data should be split.  This must be provided, and the value must be greater than or equal to two.
   */
  INFO_SPLIT_LDIF_SC_HASH_ON_RDN_ARG_DESC_NUM_SETS("The number of sets into which the data should be split.  This must be provided, and the value must be greater than or equal to two."),



  /**
   * Splits the data by computing a hash on the normalized representation of the DN component immediately below the split base DN, and using a modulus operation to determine the set in which to place the entry.  This split algorithm does not require any caching to ensure that a subordinate entry is placed in the same set as its parent.
   */
  INFO_SPLIT_LDIF_SC_HASH_ON_RDN_DESC("Splits the data by computing a hash on the normalized representation of the DN component immediately below the split base DN, and using a modulus operation to determine the set in which to place the entry.  This split algorithm does not require any caching to ensure that a subordinate entry is placed in the same set as its parent."),



  /**
   * Splits LDIF file ''whole.ldif'' so that entries below ''ou=People,dc=example,dc=com'' will be split into four sets, selected by computing a digest of the RDN component immediately below ''ou=People,dc=example,dc=com'', with names starting with ''split.ldif''.
   */
  INFO_SPLIT_LDIF_SC_HASH_ON_RDN_EXAMPLE("Splits LDIF file ''whole.ldif'' so that entries below ''ou=People,dc=example,dc=com'' will be split into four sets, selected by computing a digest of the RDN component immediately below ''ou=People,dc=example,dc=com'', with names starting with ''split.ldif''."),



  /**
   * Splits a single LDIF file into multiple sets by separating entries below a specified base DN into different mutually-exclusive collections of entries.  A number of algorithms are available to determine how entries should be split, and entries outside the split base DN may be included in all sets or added to a dedicated LDIF file.
   */
  INFO_SPLIT_LDIF_TOOL_DESCRIPTION("Splits a single LDIF file into multiple sets by separating entries below a specified base DN into different mutually-exclusive collections of entries.  A number of algorithms are available to determine how entries should be split, and entries outside the split base DN may be included in all sets or added to a dedicated LDIF file."),



  /**
   * Tool processing was interrupted by an unexpected JVM shutdown.
   */
  INFO_TOOL_INTERRUPTED_BY_JVM_SHUTDOWN("Tool processing was interrupted by an unexpected JVM shutdown."),



  /**
   * LDIF file ''{0}'' is encrypted.  Please enter the passphrase required to decrypt it:
   */
  INFO_TOOL_UTILS_ENCRYPTED_LDIF_FILE_PW_PROMPT("LDIF file ''{0}'' is encrypted.  Please enter the passphrase required to decrypt it:"),



  /**
   * Confirm the encryption passphrase:
   */
  INFO_TOOL_UTILS_ENCRYPTION_PW_CONFIRM("Confirm the encryption passphrase:"),



  /**
   * Enter the encryption passphrase:
   */
  INFO_TOOL_UTILS_ENCRYPTION_PW_PROMPT("Enter the encryption passphrase:"),



  /**
   * WARNING:  The server returned an unexpected intermediate response message with OID ''{0}'' as part of collect-support-data extended operation processing.  This unrecognized intermediate response message will be ignored.
   */
  WARN_CSD_LISTENER_UNEXPECTED_IR("WARNING:  The server returned an unexpected intermediate response message with OID ''{0}'' as part of collect-support-data extended operation processing.  This unrecognized intermediate response message will be ignored."),



  /**
   * WARNING:  Unnamed trailing argument ''{0}'' starts with a dash.  Trailing arguments should only be used for the search filter (unless a different argument is used to specify the filters) and set of requested attributes to include in search result entries.  If this was intended to be a named argument, it should be placed earlier in the argument list.
   */
  WARN_LDAPSEARCH_TRAILING_ARG_STARTS_WITH_DASH("WARNING:  Unnamed trailing argument ''{0}'' starts with a dash.  Trailing arguments should only be used for the search filter (unless a different argument is used to specify the filters) and set of requested attributes to include in search result entries.  If this was intended to be a named argument, it should be placed earlier in the argument list."),



  /**
   * WARNING:  Search result reference returned while searching for entries matching filter ''{0}'':  {1}.  This referral will not be followed, so entries only accessible via this referral will not be processed.
   */
  WARN_MANAGE_ACCT_SEARCH_OP_REFERRAL("WARNING:  Search result reference returned while searching for entries matching filter ''{0}'':  {1}.  This referral will not be followed, so entries only accessible via this referral will not be processed."),



  /**
   * No matching items were found in the OID registry.
   */
  WARN_OID_LOOKUP_NO_MATCHES("No matching items were found in the OID registry."),



  /**
   * WARNING:  An unexpected error occurred while attempting to close the LDIF reader used to read the change records from file ''{0}'':  {1}
   */
  WARN_PARALLEL_UPDATE_ERROR_CLOSING_READER("WARNING:  An unexpected error occurred while attempting to close the LDIF reader used to read the change records from file ''{0}'':  {1}");



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
      rb = ResourceBundle.getBundle("unboundid-ldapsdk-tools");
    } catch (final Exception e) {}
    RESOURCE_BUNDLE = rb;
  }



  /**
   * The map that will be used to hold the unformatted message strings, indexed by property name.
   */
  private static final ConcurrentHashMap<ToolMessages,String> MESSAGE_STRINGS = new ConcurrentHashMap<>(100);



  /**
   * The map that will be used to hold the message format objects, indexed by property name.
   */
  private static final ConcurrentHashMap<ToolMessages,MessageFormat> MESSAGES = new ConcurrentHashMap<>(100);



  // The default text for this message
  private final String defaultText;



  /**
   * Creates a new message key.
   */
  private ToolMessages(final String defaultText)
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

