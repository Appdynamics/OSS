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
package com.unboundid.ldap.sdk.unboundidds.tasks;



import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;



/**
 * This enum defines a set of message keys for messages in the
 * com.unboundid.ldap.sdk.unboundidds.tasks package, which correspond to messages in the
 * unboundid-ldapsdk-task.properties properties file.
 * <BR><BR>
 * This source file was generated from the properties file.
 * Do not edit it directly.
 */
enum TaskMessages
{
  /**
   * Entry ''{0}'' cannot be parsed as an add schema file task because it does not have any values for the ds-task-schema-file-name attribute.
   */
  ERR_ADD_SCHEMA_FILE_TASK_NO_FILES("Entry ''{0}'' cannot be parsed as an add schema file task because it does not have any values for the ds-task-schema-file-name attribute."),



  /**
   * The provided entry cannot be parsed as an alert task definition because it does not contain either an alert type and alert message or set of alert types to add or remove from the set of degraded or unavailable alert types.
   */
  ERR_ALERT_ENTRY_NO_ELEMENTS("The provided entry cannot be parsed as an alert task definition because it does not contain either an alert type and alert message or set of alert types to add or remove from the set of degraded or unavailable alert types."),



  /**
   * The provided property set cannot be used to construct an alert task because it does not contain either an alert type and alert message or set of alert types to add or remove from the set of degraded or unavailable alert types.
   */
  ERR_ALERT_PROPERTIES_NO_ELEMENTS("The provided property set cannot be used to construct an alert task because it does not contain either an alert type and alert message or set of alert types to add or remove from the set of degraded or unavailable alert types."),



  /**
   * If either an alert type or alert message is present, then the other must also be present.
   */
  ERR_ALERT_TYPE_AND_MESSAGE_INTERDEPENDENT("If either an alert type or alert message is present, then the other must also be present."),



  /**
   * You cannot specify both include and exclude auditors when creating an audit data security task.
   */
  ERR_AUDIT_DATA_SECURITY_BOTH_INCLUDE_AND_EXCLUDE_AUDITORS("You cannot specify both include and exclude auditors when creating an audit data security task."),



  /**
   * Entry ''{0}'' cannot be parsed as a backup task because it does not specify the path to the backup directory in which to write the backup files.
   */
  ERR_BACKUP_NO_BACKUP_DIRECTORY("Entry ''{0}'' cannot be parsed as a backup task because it does not specify the path to the backup directory in which to write the backup files."),



  /**
   * Task entry ''{0}'' cannot be parsed as a collect support data task entry because value ''{1}'' for ''{2}'' attribute cannot be parsed as a valid duration.  Valid durations must consist of an integer followed by a time unit (millisecond, second, minute, hour, day, or week, or any of their plurals), like ''5 minutes'' or ''1 hour''.
   */
  ERR_CSD_ENTRY_INVALID_DURATION("Task entry ''{0}'' cannot be parsed as a collect support data task entry because value ''{1}'' for ''{2}'' attribute cannot be parsed as a valid duration.  Valid durations must consist of an integer followed by a time unit (millisecond, second, minute, hour, day, or week, or any of their plurals), like ''5 minutes'' or ''1 hour''."),



  /**
   * Task entry ''{0}'' cannot be parsed as a collect support data task entry because it has unrecognized value ''{1}'' for the ''{2}'' attribute.  Allowed security level values include ''{3}'', ''{4}'', and ''{5}''.
   */
  ERR_CSD_ENTRY_INVALID_SECURITY_LEVEL("Task entry ''{0}'' cannot be parsed as a collect support data task entry because it has unrecognized value ''{1}'' for the ''{2}'' attribute.  Allowed security level values include ''{3}'', ''{4}'', and ''{5}''."),



  /**
   * Value ''{0}'' cannot be parsed as a valid value for the ''{1}'' property.  Valid durations must consist of an integer followed by a time unit (millisecond, second, minute, hour, day, or week, or any of their plurals), like ''5 minutes'' or ''1 hour''.
   */
  ERR_CSD_PROPERTY_INVALID_DURATION("Value ''{0}'' cannot be parsed as a valid value for the ''{1}'' property.  Valid durations must consist of an integer followed by a time unit (millisecond, second, minute, hour, day, or week, or any of their plurals), like ''5 minutes'' or ''1 hour''."),



  /**
   * Unable to parse the value of the ''{0}'' attribute as a duration:  {1}
   */
  ERR_DELAY_CANNOT_PARSE_ATTR_VALUE_AS_DURATION("Unable to parse the value of the ''{0}'' attribute as a duration:  {1}"),



  /**
   * Unable to parse {0} value ''{1}'' as an LDAP URL:  {2}
   */
  ERR_DELAY_ENTRY_MALFORMED_URL("Unable to parse {0} value ''{1}'' as an LDAP URL:  {2}"),



  /**
   * If a total search duration per LDAP URL is provided, the value must be greater than the time between searches and the search time limit.
   */
  ERR_DELAY_INVALID_SEARCH_DURATION("If a total search duration per LDAP URL is provided, the value must be greater than the time between searches and the search time limit."),



  /**
   * If a time between searches is provided, the value must be greater than zero and less than the total duration for each search URL.
   */
  ERR_DELAY_INVALID_SEARCH_INTERVAL("If a time between searches is provided, the value must be greater than zero and less than the total duration for each search URL."),



  /**
   * If a search time limit is provided, the value must be greater than zero and less than the total duration for each search URL.
   */
  ERR_DELAY_INVALID_SEARCH_TIME_LIMIT("If a search time limit is provided, the value must be greater than zero and less than the total duration for each search URL."),



  /**
   * If a sleep duration is provided, the value must be greater than zero.
   */
  ERR_DELAY_INVALID_SLEEP_DURATION("If a sleep duration is provided, the value must be greater than zero."),



  /**
   * The provided timeout return state was invalid.  The value must be one of ''{0}'', ''{1}'', or ''{2}''.
   */
  ERR_DELAY_INVALID_TIMEOUT_STATE("The provided timeout return state was invalid.  The value must be one of ''{0}'', ''{1}'', or ''{2}''."),



  /**
   * If the task should wait for the work queue to become idle, the duration must be greater than zero.
   */
  ERR_DELAY_INVALID_WAIT_FOR_QUEUE_IDLE("If the task should wait for the work queue to become idle, the duration must be greater than zero."),



  /**
   * If the task is to perform searches using criteria from an LDAP URL, then a time between searches, search time limit, and total search duration for each LDAP URL must be provided.
   */
  ERR_DELAY_URL_WITHOUT_REQUIRED_PARAM("If the task is to perform searches using criteria from an LDAP URL, then a time between searches, search time limit, and total search duration for each LDAP URL must be provided."),



  /**
   * Entry ''{0}'' cannot be parsed as a disconnect client task because the connection ID value ''{1}'' could not be parsed as an integer.
   */
  ERR_DISCONNECT_TASK_CONN_ID_NOT_LONG("Entry ''{0}'' cannot be parsed as a disconnect client task because the connection ID value ''{1}'' could not be parsed as an integer."),



  /**
   * Entry ''{0}'' cannot be parsed as a disconnect client task because it does not specify the connection ID for the client to disconnect.
   */
  ERR_DISCONNECT_TASK_NO_CONN_ID("Entry ''{0}'' cannot be parsed as a disconnect client task because it does not specify the connection ID for the client to disconnect."),



  /**
   * Entry {0} cannot be parsed as a dump DB details task entry because it is missing attribute {1} to specify the backend ID for the backend to examine.
   */
  ERR_DUMP_DB_ENTRY_MISSING_BACKEND_ID("Entry {0} cannot be parsed as a dump DB details task entry because it is missing attribute {1} to specify the backend ID for the backend to examine."),



  /**
   * Unable to decode an exec task from entry ''{0}'' because it is missing the required {1} attribute.
   */
  ERR_EXEC_ENTRY_MISSING_COMMAND_PATH("Unable to decode an exec task from entry ''{0}'' because it is missing the required {1} attribute."),



  /**
   * If a task state is provided for use in the case of a nonzero exit code, the value must be one of ''{0}'', ''{1}'', or ''{2}''.
   */
  ERR_EXEC_INVALID_STATE_FOR_NONZERO_EXIT_CODE("If a task state is provided for use in the case of a nonzero exit code, the value must be one of ''{0}'', ''{1}'', or ''{2}''."),



  /**
   * The command path must not be null or empty.
   */
  ERR_EXEC_MISSING_PATH("The command path must not be null or empty."),



  /**
   * Unable to decode an exec task from the provided set of properties because the command path was not specified.
   */
  ERR_EXEC_PROPERTIES_MISSING_COMMAND_PATH("Unable to decode an exec task from the provided set of properties because the command path was not specified."),



  /**
   * Entry ''{0}'' cannot be parsed as an export task because value ''{1}'' of attribute ds-task-export-wrap-column cannot be parsed as an integer.
   */
  ERR_EXPORT_TASK_CANNOT_PARSE_WRAP_COLUMN("Entry ''{0}'' cannot be parsed as an export task because value ''{1}'' of attribute ds-task-export-wrap-column cannot be parsed as an integer."),



  /**
   * Entry ''{0}'' cannot be parsed as an export task because it does not specify the backend ID of the backend to export.
   */
  ERR_EXPORT_TASK_NO_BACKEND_ID("Entry ''{0}'' cannot be parsed as an export task because it does not specify the backend ID of the backend to export."),



  /**
   * Entry ''{0}'' cannot be parsed as an export task because it does not specify the path to the LDIF file to be written.
   */
  ERR_EXPORT_TASK_NO_LDIF_FILE("Entry ''{0}'' cannot be parsed as an export task because it does not specify the path to the LDIF file to be written."),



  /**
   * Unable to decode entry ''{0}'' as a file retention task because ''{1}'' is not a valid retain file age value:  {2}
   */
  ERR_FILE_RETENTION_ENTRY_INVALID_RETAIN_AGE("Unable to decode entry ''{0}'' as a file retention task because ''{1}'' is not a valid retain file age value:  {2}"),



  /**
   * Unable to decode entry ''{0}'' as a file retention task because ''{1}'' is not a valid retain file count value.  The value must be a non-negative integer indicating the minimum number of files to retain.
   */
  ERR_FILE_RETENTION_ENTRY_INVALID_RETAIN_COUNT("Unable to decode entry ''{0}'' as a file retention task because ''{1}'' is not a valid retain file count value.  The value must be a non-negative integer indicating the minimum number of files to retain."),



  /**
   * Unable to decode entry ''{0}'' as a file retention task because ''{1}'' is not a valid retain aggregate file size value.  It must be a positive integer representing the minimum number of bytes to retain.
   */
  ERR_FILE_RETENTION_ENTRY_INVALID_RETAIN_SIZE("Unable to decode entry ''{0}'' as a file retention task because ''{1}'' is not a valid retain aggregate file size value.  It must be a positive integer representing the minimum number of bytes to retain."),



  /**
   * Unable to decode entry ''{0}'' as a file retention task because ''{1}'' is not a valid timestamp format.  The allowed timestamp format values are:  {2}.
   */
  ERR_FILE_RETENTION_ENTRY_INVALID_TIMESTAMP_FORMAT("Unable to decode entry ''{0}'' as a file retention task because ''{1}'' is not a valid timestamp format.  The allowed timestamp format values are:  {2}."),



  /**
   * Unable to decode entry ''{0}'' as a file retention task because it is missing required attribute ''{1}''.
   */
  ERR_FILE_RETENTION_ENTRY_MISSING_REQUIRED_ATTR("Unable to decode entry ''{0}'' as a file retention task because it is missing required attribute ''{1}''."),



  /**
   * Unable to decode entry ''{0}'' as a file retention task because it does not contain any retention criteria.  At least one of the ''{1}'', ''{2}'', or ''{3}'' attributes must be provided.
   */
  ERR_FILE_RETENTION_ENTRY_MISSING_RETENTION_CRITERIA("Unable to decode entry ''{0}'' as a file retention task because it does not contain any retention criteria.  At least one of the ''{1}'', ''{2}'', or ''{3}'' attributes must be provided."),



  /**
   * Unable to create a file retention task from the provided set of properties because required property ''{0}'' was not provided.
   */
  ERR_FILE_RETENTION_MISSING_REQUIRED_PROPERTY("Unable to create a file retention task from the provided set of properties because required property ''{0}'' was not provided."),



  /**
   * Unable to create a file retention task from the provided set of properties because none of the ''{0}'', ''{1}'', or ''{2}'' properties was provided.  At least one of these properties must be given.
   */
  ERR_FILE_RETENTION_MISSING_RETENTION_PROPERTY("Unable to create a file retention task from the provided set of properties because none of the ''{0}'', ''{1}'', or ''{2}'' properties was provided.  At least one of these properties must be given."),



  /**
   * Unable to parse retain age value ''{0}'' as a duration:  {1}
   */
  ERR_GENERATE_SERVER_PROFILE_CANNOT_PARSE_RETAIN_AGE("Unable to parse retain age value ''{0}'' as a duration:  {1}"),



  /**
   * Unable to create a generate server profile task from entry ''{0}'' because it has an invalid retain age value of ''{1}'':  {2}
   */
  ERR_GENERATE_SERVER_PROFILE_ENTRY_INVALID_RETAIN_AGE("Unable to create a generate server profile task from entry ''{0}'' because it has an invalid retain age value of ''{1}'':  {2}"),



  /**
   * Unable to create a generate server profile task from the provided property set because it has an invalid retain age value of ''{0}'':  {1}
   */
  ERR_GENERATE_SERVER_PROFILE_PROPS_INVALID_RETAIN_AGE("Unable to create a generate server profile task from the provided property set because it has an invalid retain age value of ''{0}'':  {1}"),



  /**
   * No fully-qualified Groovy class name was provided in task entry {0}.
   */
  ERR_GROOVY_SCRIPTED_TASK_NO_CLASS("No fully-qualified Groovy class name was provided in task entry {0}."),



  /**
   * Neither a backend ID nor set of include branches was specified.
   */
  ERR_IMPORT_TASK_NO_BACKEND_ID_OR_INCLUDE_BRANCHES("Neither a backend ID nor set of include branches was specified."),



  /**
   * Entry ''{0}'' cannot be parsed as an import task because it does not specify the path to the LDIF file(s) containing the data to import.
   */
  ERR_IMPORT_TASK_NO_LDIF("Entry ''{0}'' cannot be parsed as an import task because it does not specify the path to the LDIF file(s) containing the data to import."),



  /**
   * Entry ''{0}'' cannot be parsed as a rebuild task because value ''{1}'' for attribute ds-task-rebuild-max-threads cannot be parsed as an integer.
   */
  ERR_REBUILD_TASK_INVALID_MAX_THREADS("Entry ''{0}'' cannot be parsed as a rebuild task because value ''{1}'' for attribute ds-task-rebuild-max-threads cannot be parsed as an integer."),



  /**
   * Entry ''{0}'' cannot be parsed as a rebuild task because it does not specify the base DN for which to rebuild the indexes.
   */
  ERR_REBUILD_TASK_NO_BASE_DN("Entry ''{0}'' cannot be parsed as a rebuild task because it does not specify the base DN for which to rebuild the indexes."),



  /**
   * Entry ''{0}'' cannot be parsed as a rebuild task because it does not specify any indexes to rebuild.
   */
  ERR_REBUILD_TASK_NO_INDEXES("Entry ''{0}'' cannot be parsed as a rebuild task because it does not specify any indexes to rebuild."),



  /**
   * Entry ''{0}'' cannot be parsed as a re-encode entries task because it does not have a value for the required ''{1}'' attribute.
   */
  ERR_REENCODE_TASK_MISSING_REQUIRED_ATTR("Entry ''{0}'' cannot be parsed as a re-encode entries task because it does not have a value for the required ''{1}'' attribute."),



  /**
   * Property ''{0}'' is required for a re-encode entries task but was not provided.
   */
  ERR_REENCODE_TASK_MISSING_REQUIRED_PROPERTY("Property ''{0}'' is required for a re-encode entries task but was not provided."),



  /**
   * Unable to create a reload global index task from the provided entry because the required ''{0}'' attribute was not included.
   */
  ERR_RELOAD_GLOBAL_INDEX_MISSING_REQUIRED_ATTR("Unable to create a reload global index task from the provided entry because the required ''{0}'' attribute was not included."),



  /**
   * Unable to create a reload global index task from the provided set of properties because the required ''{0}'' property was not included.
   */
  ERR_RELOAD_GLOBAL_INDEX_MISSING_REQUIRED_PROPERTY("Unable to create a reload global index task from the provided set of properties because the required ''{0}'' property was not included."),



  /**
   * Unable to create a remove attribute type task from the information in the provided entry ''{0}'' because the entry is missing the required attribute ''{1}'' used to specify the name or OID of the attribute type to remove.
   */
  ERR_REMOVE_ATTR_TYPE_ENTRY_MISSING_ATTR_TYPE("Unable to create a remove attribute type task from the information in the provided entry ''{0}'' because the entry is missing the required attribute ''{1}'' used to specify the name or OID of the attribute type to remove."),



  /**
   * Unable to create a remove attribute type task from the provided set of properties because the required ''{0}'' property (used to specify the name or OID of the attribute type to remove) was not included.
   */
  ERR_REMOVE_ATTR_TYPE_PROPS_MISSING_ATTR_TYPE("Unable to create a remove attribute type task from the provided set of properties because the required ''{0}'' property (used to specify the name or OID of the attribute type to remove) was not included."),



  /**
   * Entry ''{0}'' cannot be parsed as a restore task because it does not specify the path to the backup directory in which the backup resides.
   */
  ERR_RESTORE_NO_BACKUP_DIRECTORY("Entry ''{0}'' cannot be parsed as a restore task because it does not specify the path to the backup directory in which the backup resides."),



  /**
   * Entry ''{0}'' cannot be parsed as a search task because it has an invalid value of ''{1}'' for the ds-task-search-filter attribute.
   */
  ERR_SEARCH_TASK_ENTRY_INVALID_FILTER("Entry ''{0}'' cannot be parsed as a search task because it has an invalid value of ''{1}'' for the ds-task-search-filter attribute."),



  /**
   * Entry ''{0}'' cannot be parsed as a search task because it has an invalid value of ''{1}'' for the ds-task-search-scope attribute.
   */
  ERR_SEARCH_TASK_ENTRY_INVALID_SCOPE("Entry ''{0}'' cannot be parsed as a search task because it has an invalid value of ''{1}'' for the ds-task-search-scope attribute."),



  /**
   * Entry ''{0}'' cannot be parsed as a search task because it does not have any value for the ds-task-search-base-dn attribute.
   */
  ERR_SEARCH_TASK_ENTRY_NO_BASE_DN("Entry ''{0}'' cannot be parsed as a search task because it does not have any value for the ds-task-search-base-dn attribute."),



  /**
   * Entry ''{0}'' cannot be parsed as a search task because it does not have any value for the ds-task-search-filter attribute.
   */
  ERR_SEARCH_TASK_ENTRY_NO_FILTER("Entry ''{0}'' cannot be parsed as a search task because it does not have any value for the ds-task-search-filter attribute."),



  /**
   * Entry ''{0}'' cannot be parsed as a search task because it does not have any value for the ds-task-search-output-file attribute.
   */
  ERR_SEARCH_TASK_ENTRY_NO_OUTPUT_FILE("Entry ''{0}'' cannot be parsed as a search task because it does not have any value for the ds-task-search-output-file attribute."),



  /**
   * Entry ''{0}'' cannot be parsed as a search task because it does not have any value for the ds-task-search-scope attribute.
   */
  ERR_SEARCH_TASK_ENTRY_NO_SCOPE("Entry ''{0}'' cannot be parsed as a search task because it does not have any value for the ds-task-search-scope attribute."),



  /**
   * The provided value ''{0}'' is not a valid search filter.
   */
  ERR_SEARCH_TASK_INVALID_FILTER_PROPERTY("The provided value ''{0}'' is not a valid search filter."),



  /**
   * The provided value ''{0}'' is not a valid search scope.
   */
  ERR_SEARCH_TASK_INVALID_SCOPE_PROPERTY("The provided value ''{0}'' is not a valid search scope."),



  /**
   * No property was provided to specify the search base DN.
   */
  ERR_SEARCH_TASK_NO_BASE_PROPERTY("No property was provided to specify the search base DN."),



  /**
   * No property was provided to specify the search filter.
   */
  ERR_SEARCH_TASK_NO_FILTER_PROPERTY("No property was provided to specify the search filter."),



  /**
   * No property was provided to specify the output file.
   */
  ERR_SEARCH_TASK_NO_OUTPUT_FILE_PROPERTY("No property was provided to specify the output file."),



  /**
   * No property was provided to specify the search scope.
   */
  ERR_SEARCH_TASK_NO_SCOPE_PROPERTY("No property was provided to specify the search scope."),



  /**
   * Entry ''{0}'' cannot be parsed as a scheduled task because the actual start time value ''{1}'' could not be interpreted as a generalized time:  {2}
   */
  ERR_TASK_CANNOT_PARSE_ACTUAL_START_TIME("Entry ''{0}'' cannot be parsed as a scheduled task because the actual start time value ''{1}'' could not be interpreted as a generalized time:  {2}"),



  /**
   * Entry ''{0}'' contains an invalid value ''{1}'' for attribute {2}.  The value must be either ''true'' or ''false''.
   */
  ERR_TASK_CANNOT_PARSE_BOOLEAN("Entry ''{0}'' contains an invalid value ''{1}'' for attribute {2}.  The value must be either ''true'' or ''false''."),



  /**
   * Entry ''{0}'' cannot be parsed as a scheduled task because the completion time value ''{1}'' could not be interpreted as a generalized time:  {2}
   */
  ERR_TASK_CANNOT_PARSE_COMPLETION_TIME("Entry ''{0}'' cannot be parsed as a scheduled task because the completion time value ''{1}'' could not be interpreted as a generalized time:  {2}"),



  /**
   * Entry ''{0}'' cannot be parsed as a scheduled task because the scheduled start time value ''{1}'' could not be interpreted as a generalized time:  {2}
   */
  ERR_TASK_CANNOT_PARSE_SCHEDULED_START_TIME("Entry ''{0}'' cannot be parsed as a scheduled task because the scheduled start time value ''{1}'' could not be interpreted as a generalized time:  {2}"),



  /**
   * Entry ''{0}'' cannot be parsed as a scheduled task because it has an unrecognized task state ''{1}''.
   */
  ERR_TASK_INVALID_STATE("Entry ''{0}'' cannot be parsed as a scheduled task because it has an unrecognized task state ''{1}''."),



  /**
   * The thread waiting for task ''{0}'' to complete was interrupted.
   */
  ERR_TASK_MANAGER_WAIT_INTERRUPTED("The thread waiting for task ''{0}'' to complete was interrupted."),



  /**
   * No entry for task ''{0}'' exists in the Directory Server.
   */
  ERR_TASK_MANAGER_WAIT_NO_SUCH_TASK("No entry for task ''{0}'' exists in the Directory Server."),



  /**
   * Entry ''{0}'' cannot be parsed as a scheduled task entry because it is missing the ds-task object class.
   */
  ERR_TASK_MISSING_OC("Entry ''{0}'' cannot be parsed as a scheduled task entry because it is missing the ds-task object class."),



  /**
   * Entry ''{0}'' cannot be parsed as a scheduled task because it does not contain a task class name.
   */
  ERR_TASK_NO_CLASS("Entry ''{0}'' cannot be parsed as a scheduled task because it does not contain a task class name."),



  /**
   * Entry ''{0}'' cannot be parsed as a scheduled task because it does not contain a task ID.
   */
  ERR_TASK_NO_ID("Entry ''{0}'' cannot be parsed as a scheduled task because it does not contain a task ID."),



  /**
   * Entry ''{0}'' cannot be parsed as a scheduled task because it does not contain a task state.
   */
  ERR_TASK_NO_STATE("Entry ''{0}'' cannot be parsed as a scheduled task because it does not contain a task state."),



  /**
   * Multiple values were provided for task property ''{0}'', but only a single value is allowed.
   */
  ERR_TASK_PROPERTY_NOT_MULTIVALUED("Multiple values were provided for task property ''{0}'', but only a single value is allowed."),



  /**
   * Task property ''{0}'' contains value ''{1}'' which is not contained in the set of allowed values for that property.
   */
  ERR_TASK_PROPERTY_VALUE_NOT_ALLOWED("Task property ''{0}'' contains value ''{1}'' which is not contained in the set of allowed values for that property."),



  /**
   * Task property ''{0}'' contains a value which cannot be parsed as a Boolean.
   */
  ERR_TASK_PROPERTY_VALUE_NOT_BOOLEAN("Task property ''{0}'' contains a value which cannot be parsed as a Boolean."),



  /**
   * Task property ''{0}'' contains a value which cannot be parsed as a date.
   */
  ERR_TASK_PROPERTY_VALUE_NOT_DATE("Task property ''{0}'' contains a value which cannot be parsed as a date."),



  /**
   * Task property ''{0}'' contains a value which cannot be parsed as a long.
   */
  ERR_TASK_PROPERTY_VALUE_NOT_LONG("Task property ''{0}'' contains a value which cannot be parsed as a long."),



  /**
   * Task property ''{0}'' contains a value which cannot be parsed as a string.
   */
  ERR_TASK_PROPERTY_VALUE_NOT_STRING("Task property ''{0}'' contains a value which cannot be parsed as a string."),



  /**
   * Task property ''{0}'' is required, but no values were provided.
   */
  ERR_TASK_REQUIRED_PROPERTY_WITHOUT_VALUES("Task property ''{0}'' is required, but no values were provided."),



  /**
   * No fully-qualified task class name was provided in task entry {0}.
   */
  ERR_THIRD_PARTY_TASK_NO_CLASS("No fully-qualified task class name was provided in task entry {0}."),



  /**
   * The name of an alert type to add to the set of degraded alert types.
   */
  INFO_ALERT_DESCRIPTION_ADD_DEGRADED("The name of an alert type to add to the set of degraded alert types."),



  /**
   * The name of an alert type to add to the set of unavailable alert types.
   */
  INFO_ALERT_DESCRIPTION_ADD_UNAVAILABLE("The name of an alert type to add to the set of unavailable alert types."),



  /**
   * The message for the administrative alert to generate.  If this is provided, then an alert type must also be provided.
   */
  INFO_ALERT_DESCRIPTION_MESSAGE("The message for the administrative alert to generate.  If this is provided, then an alert type must also be provided."),



  /**
   * The name of an alert type to remove from the set of degraded alert types.
   */
  INFO_ALERT_DESCRIPTION_REMOVE_DEGRADED("The name of an alert type to remove from the set of degraded alert types."),



  /**
   * The name of an alert type to remove from the set of unavailable alert types.
   */
  INFO_ALERT_DESCRIPTION_REMOVE_UNAVAILABLE("The name of an alert type to remove from the set of unavailable alert types."),



  /**
   * The alert type for the administrative alert to generate.  If this is provided, then an alert message must also be provided.
   */
  INFO_ALERT_DESCRIPTION_TYPE("The alert type for the administrative alert to generate.  If this is provided, then an alert message must also be provided."),



  /**
   * Add Degraded Alert Type
   */
  INFO_ALERT_DISPLAY_NAME_ADD_DEGRADED("Add Degraded Alert Type"),



  /**
   * Add Unavailable Alert Type
   */
  INFO_ALERT_DISPLAY_NAME_ADD_UNAVAILABLE("Add Unavailable Alert Type"),



  /**
   * Alert Message
   */
  INFO_ALERT_DISPLAY_NAME_MESSAGE("Alert Message"),



  /**
   * Remove Degraded Alert Type
   */
  INFO_ALERT_DISPLAY_NAME_REMOVE_DEGRADED("Remove Degraded Alert Type"),



  /**
   * Remove Unavailable Alert Type
   */
  INFO_ALERT_DISPLAY_NAME_REMOVE_UNAVAILABLE("Remove Unavailable Alert Type"),



  /**
   * Alert Type
   */
  INFO_ALERT_DISPLAY_NAME_TYPE("Alert Type"),



  /**
   * The backend IDs of the backends in which data should be audited.  If no backend IDs are specified, then the audit will examine data in all backends that support this capability.
   */
  INFO_AUDIT_DATA_SECURITY_DESCRIPTION_BACKEND_ID("The backend IDs of the backends in which data should be audited.  If no backend IDs are specified, then the audit will examine data in all backends that support this capability."),



  /**
   * The names of the auditors to be excluded when examining the security of the data.  If excluded auditors are specified, then you must not also specify included auditors.  If neither included nor excluded auditors are provided, then the audit will use all enabled auditors configured in the server.
   */
  INFO_AUDIT_DATA_SECURITY_DESCRIPTION_EXCLUDE_AUDITOR("The names of the auditors to be excluded when examining the security of the data.  If excluded auditors are specified, then you must not also specify included auditors.  If neither included nor excluded auditors are provided, then the audit will use all enabled auditors configured in the server."),



  /**
   * The names of the auditors to be used in the course of examining the security of the data.  If included auditors are specified, then you must not also specify excluded auditors.  If neither included nor excluded auditors are provided, then the audit will use all enabled auditors configured in the server.
   */
  INFO_AUDIT_DATA_SECURITY_DESCRIPTION_INCLUDE_AUDITOR("The names of the auditors to be used in the course of examining the security of the data.  If included auditors are specified, then you must not also specify excluded auditors.  If neither included nor excluded auditors are provided, then the audit will use all enabled auditors configured in the server."),



  /**
   * The path to the directory on the server filesystem in which report output files should be written.  If this is not specified, then a default location will be used.
   */
  INFO_AUDIT_DATA_SECURITY_DESCRIPTION_OUTPUT_DIR("The path to the directory on the server filesystem in which report output files should be written.  If this is not specified, then a default location will be used."),



  /**
   * A set of filters that may be used to indicate which entries should be examined during the audit.  If no report filters are specified, then all entries in the selected backends will be included.
   */
  INFO_AUDIT_DATA_SECURITY_DESCRIPTION_REPORT_FILTER("A set of filters that may be used to indicate which entries should be examined during the audit.  If no report filters are specified, then all entries in the selected backends will be included."),



  /**
   * Audit Backend IDs
   */
  INFO_AUDIT_DATA_SECURITY_DISPLAY_NAME_BACKEND_ID("Audit Backend IDs"),



  /**
   * Excluded Auditors
   */
  INFO_AUDIT_DATA_SECURITY_DISPLAY_NAME_EXCLUDE_AUDITOR("Excluded Auditors"),



  /**
   * Included Auditors
   */
  INFO_AUDIT_DATA_SECURITY_DISPLAY_NAME_INCLUDE_AUDITOR("Included Auditors"),



  /**
   * Output Directory
   */
  INFO_AUDIT_DATA_SECURITY_DISPLAY_NAME_OUTPUT_DIR("Output Directory"),



  /**
   * Report Filters
   */
  INFO_AUDIT_DATA_SECURITY_DISPLAY_NAME_REPORT_FILTER("Report Filters"),



  /**
   * A comment that may be placed in the support data archive to provided additional information.  If this is omitted, then no additional comment will be included in the archive.
   */
  INFO_CSD_DESCRIPTION_COMMENT("A comment that may be placed in the support data archive to provided additional information.  If this is omitted, then no additional comment will be included in the archive."),



  /**
   * The absolute path on the server filesystem to a file that contains the passphrase to use to encrypt the contents of the support data archive.  If this is provided, then it must reference a file that exists and contains exactly one line that consists entirely of the encryption passphrase.  If this is not provided, then the support data archive will not be encrypted.
   */
  INFO_CSD_DESCRIPTION_ENC_PW_FILE("The absolute path on the server filesystem to a file that contains the passphrase to use to encrypt the contents of the support data archive.  If this is provided, then it must reference a file that exists and contains exactly one line that consists entirely of the encryption passphrase.  If this is not provided, then the support data archive will not be encrypted."),



  /**
   * Indicates whether the support data archive may include binary files that would have otherwise been omitted.  Note that binary files will not have any sensitive information obscured or redacted.
   */
  INFO_CSD_DESCRIPTION_INCLUDE_BINARY_FILES("Indicates whether the support data archive may include binary files that would have otherwise been omitted.  Note that binary files will not have any sensitive information obscured or redacted."),



  /**
   * Indicates whether the support data archive may include information that could be expensive to collect, potentially affecting server performance or responsiveness.
   */
  INFO_CSD_DESCRIPTION_INCLUDE_EXPENSIVE_DATA("Indicates whether the support data archive may include information that could be expensive to collect, potentially affecting server performance or responsiveness."),



  /**
   * Indicates whether the support data archive should include the source code (if available) for any third-party extensions installed in the server.
   */
  INFO_CSD_DESCRIPTION_INCLUDE_EXTENSION_SOURCE("Indicates whether the support data archive should include the source code (if available) for any third-party extensions installed in the server."),



  /**
   * Indicates whether the support data archive should include a replication state dump, which may be several megabytes in size.
   */
  INFO_CSD_DESCRIPTION_INCLUDE_REPLICATION_STATE_DUMP("Indicates whether the support data archive should include a replication state dump, which may be several megabytes in size."),



  /**
   * The number of times to invoke the jstack utility to get a stack trace of all threads in the server.  If this is provided, the value must be greater than or equal to zero.  If it is not provided, the server will use a default jstack count.
   */
  INFO_CSD_DESCRIPTION_JSTACK_COUNT("The number of times to invoke the jstack utility to get a stack trace of all threads in the server.  If this is provided, the value must be greater than or equal to zero.  If it is not provided, the server will use a default jstack count."),



  /**
   * The duration (the length of time leading up to the time the collect support data task is invoked) of log content that should be included in the support data archive.  Duration values should be specified as an integer followed by a time unit (millisecond, second, minute, hour, day, or week, or any of their plurals), like ''5 minutes'' or ''1 hour''.  If this is not provided, the server will determine an appropriate amount of log content to include.
   */
  INFO_CSD_DESCRIPTION_LOG_DURATION("The duration (the length of time leading up to the time the collect support data task is invoked) of log content that should be included in the support data archive.  Duration values should be specified as an integer followed by a time unit (millisecond, second, minute, hour, day, or week, or any of their plurals), like ''5 minutes'' or ''1 hour''.  If this is not provided, the server will determine an appropriate amount of log content to include."),



  /**
   * The amount of data in kilobytes to capture from the beginning of each log file that should be included in the support data archive.  If this is not provided, the server will determine an appropriate amount of log content to include.
   */
  INFO_CSD_DESCRIPTION_LOG_HEAD_SIZE_KB("The amount of data in kilobytes to capture from the beginning of each log file that should be included in the support data archive.  If this is not provided, the server will determine an appropriate amount of log content to include."),



  /**
   * The amount of data in kilobytes to capture from the end of each log file that should be included in the support data archive.  If this is not provided, the server will determine an appropriate amount of log content to include.
   */
  INFO_CSD_DESCRIPTION_LOG_TAIL_SIZE_KB("The amount of data in kilobytes to capture from the end of each log file that should be included in the support data archive.  If this is not provided, the server will determine an appropriate amount of log content to include."),



  /**
   * The absolute path on the server filesystem to which the support data archive should be written.  If this is provided and refers to a file that exists, then that file will be overwritten with the new support data archive.  If this is provided and refers to a directory that exists, then a new support data file will be written into that directory with a name generated by the server.  If this is provided and refers to a file that does not exist, then the parent directory must exist and the support data archive will be created in that directory with the specified name.  If this is not provided, then the server will select an appropriate path and name for the support data archive.
   */
  INFO_CSD_DESCRIPTION_OUTPUT_PATH("The absolute path on the server filesystem to which the support data archive should be written.  If this is provided and refers to a file that exists, then that file will be overwritten with the new support data archive.  If this is provided and refers to a directory that exists, then a new support data file will be written into that directory with a name generated by the server.  If this is provided and refers to a file that does not exist, then the parent directory must exist and the support data archive will be created in that directory with the specified name.  If this is not provided, then the server will select an appropriate path and name for the support data archive."),



  /**
   * The number of intervals of data to collect from tools that use sample-based reporting (e.g., vmstat, iostat, mpstat, etc.).  If this is provided, then it must be an integer value that is greater than or equal to zero, with zero indicating that no data should be captured.  If this is not provided, then the server will choose an appropriate report count value.
   */
  INFO_CSD_DESCRIPTION_REPORT_COUNT("The number of intervals of data to collect from tools that use sample-based reporting (e.g., vmstat, iostat, mpstat, etc.).  If this is provided, then it must be an integer value that is greater than or equal to zero, with zero indicating that no data should be captured.  If this is not provided, then the server will choose an appropriate report count value."),



  /**
   * The duration in seconds between each interval of data collected from tools that use sample-based reporting (e.g., vmstat, iostat, mpstat, etc.).  If this is provided, then it must be an integer value that is greater than or equal to one.  If this is not provided, then the server will choose an appropriate report interval.
   */
  INFO_CSD_DESCRIPTION_REPORT_INTERVAL("The duration in seconds between each interval of data collected from tools that use sample-based reporting (e.g., vmstat, iostat, mpstat, etc.).  If this is provided, then it must be an integer value that is greater than or equal to one.  If this is not provided, then the server will choose an appropriate report interval."),



  /**
   * The minimum age of previous support data archives that should be retained.  If this is provided, then any preexisting support data archives older than this may be deleted.  Duration values should be specified as an integer followed by a time unit (millisecond, second, minute, hour, day, or week, or any of their plurals), like ''5 minutes'' or ''1 hour''.  If this is not provided, the server will either use only a count to determine the number of log files to retain (if a count was provided) or will not remove any previous archives (if no count was provided).  If an output path is specified, then this property may only be used if the output path references a directory rather than a file.
   */
  INFO_CSD_DESCRIPTION_RETAIN_PREVIOUS_ARCHIVE_AGE("The minimum age of previous support data archives that should be retained.  If this is provided, then any preexisting support data archives older than this may be deleted.  Duration values should be specified as an integer followed by a time unit (millisecond, second, minute, hour, day, or week, or any of their plurals), like ''5 minutes'' or ''1 hour''.  If this is not provided, the server will either use only a count to determine the number of log files to retain (if a count was provided) or will not remove any previous archives (if no count was provided).  If an output path is specified, then this property may only be used if the output path references a directory rather than a file."),



  /**
   * The minimum number of previous support data archives that should be retained.  If this is provided, then at least the specified number of the most recent preexisting support data archives will be retained, but older archives may be removed.  If this is not provided, the server will either use only a duration to determine the number of log files to retain (if a retain age was provided) or will not remove any previous archives (if no retain age was provided).  If an output path is specified, then this property may only be used if the output path references a directory rather than a file.
   */
  INFO_CSD_DESCRIPTION_RETAIN_PREVIOUS_ARCHIVE_COUNT("The minimum number of previous support data archives that should be retained.  If this is provided, then at least the specified number of the most recent preexisting support data archives will be retained, but older archives may be removed.  If this is not provided, the server will either use only a duration to determine the number of log files to retain (if a retain age was provided) or will not remove any previous archives (if no retain age was provided).  If an output path is specified, then this property may only be used if the output path references a directory rather than a file."),



  /**
   * The security level that should be used when determining which content to obscure, redact, or exclude from the support data archive.  Allowed values include ''{0}'' (indicating that no data should be obscured or excluded), ''{1}'' (indicating that secret information, like the values of sensitive configuration properties, will be obscured and that logs containing user data, like the data recovery log, will be excluded), and ''{2}'' (indicating that the server will attempt to prevent any personally identifiable information from appearing in the archive, including excluding logs and obscuring DN and filter values).
   */
  INFO_CSD_DESCRIPTION_SECURITY_LEVEL("The security level that should be used when determining which content to obscure, redact, or exclude from the support data archive.  Allowed values include ''{0}'' (indicating that no data should be obscured or excluded), ''{1}'' (indicating that secret information, like the values of sensitive configuration properties, will be obscured and that logs containing user data, like the data recovery log, will be excluded), and ''{2}'' (indicating that the server will attempt to prevent any personally identifiable information from appearing in the archive, including excluding logs and obscuring DN and filter values)."),



  /**
   * Indicates whether the tool should operate in sequential mode, in which it collects items one at a time rather than in parallel.  Operating in sequential mode may reduce the amount of memory consumed by the tool, but will increase the length of time required for it to complete.
   */
  INFO_CSD_DESCRIPTION_USE_SEQUENTIAL_MODE("Indicates whether the tool should operate in sequential mode, in which it collects items one at a time rather than in parallel.  Operating in sequential mode may reduce the amount of memory consumed by the tool, but will increase the length of time required for it to complete."),



  /**
   * Comment
   */
  INFO_CSD_DISPLAY_NAME_COMMENT("Comment"),



  /**
   * Encryption Passphrase File Path
   */
  INFO_CSD_DISPLAY_NAME_ENC_PW_FILE("Encryption Passphrase File Path"),



  /**
   * Include Binary Files
   */
  INFO_CSD_DISPLAY_NAME_INCLUDE_BINARY_FILES("Include Binary Files"),



  /**
   * Include Expensive Data
   */
  INFO_CSD_DISPLAY_NAME_INCLUDE_EXPENSIVE_DATA("Include Expensive Data"),



  /**
   * Include Extension Source Code
   */
  INFO_CSD_DISPLAY_NAME_INCLUDE_EXTENSION_SOURCE("Include Extension Source Code"),



  /**
   * Include Replication State Dump
   */
  INFO_CSD_DISPLAY_NAME_INCLUDE_REPLICATION_STATE_DUMP("Include Replication State Dump"),



  /**
   * JStack Count
   */
  INFO_CSD_DISPLAY_NAME_JSTACK_COUNT("JStack Count"),



  /**
   * Log Duration
   */
  INFO_CSD_DISPLAY_NAME_LOG_DURATION("Log Duration"),



  /**
   * Log File Head Collection Size (KB)
   */
  INFO_CSD_DISPLAY_NAME_LOG_HEAD_SIZE_KB("Log File Head Collection Size (KB)"),



  /**
   * Log File Tail Collection Size (KB)
   */
  INFO_CSD_DISPLAY_NAME_LOG_TAIL_SIZE_KB("Log File Tail Collection Size (KB)"),



  /**
   * Output Path
   */
  INFO_CSD_DISPLAY_NAME_OUTPUT_PATH("Output Path"),



  /**
   * Report Count
   */
  INFO_CSD_DISPLAY_NAME_REPORT_COUNT("Report Count"),



  /**
   * Report Interval (Seconds)
   */
  INFO_CSD_DISPLAY_NAME_REPORT_INTERVAL("Report Interval (Seconds)"),



  /**
   * Retain Previous Support Data Archive Age
   */
  INFO_CSD_DISPLAY_NAME_RETAIN_PREVIOUS_ARCHIVE_AGE("Retain Previous Support Data Archive Age"),



  /**
   * Retain Previous Support Data Archive Count
   */
  INFO_CSD_DISPLAY_NAME_RETAIN_PREVIOUS_ARCHIVE_COUNT("Retain Previous Support Data Archive Count"),



  /**
   * Security Level
   */
  INFO_CSD_DISPLAY_NAME_SECURITY_LEVEL("Security Level"),



  /**
   * Use Sequential Mode
   */
  INFO_CSD_DISPLAY_NAME_USE_SEQUENTIAL_MODE("Use Sequential Mode"),



  /**
   * Invokes the collect-support-data utility to capture a range of information that may be useful in troubleshooting problems or monitoring the performance or behavior of the server.
   */
  INFO_CSD_TASK_DESCRIPTION("Invokes the collect-support-data utility to capture a range of information that may be useful in troubleshooting problems or monitoring the performance or behavior of the server."),



  /**
   * Collect Support Data
   */
  INFO_CSD_TASK_NAME("Collect Support Data"),



  /**
   * The total length of time to continue repeating searches using criteria obtained from each provided LDAP URL.
   */
  INFO_DELAY_DESCRIPTION_SEARCH_DURATION("The total length of time to continue repeating searches using criteria obtained from each provided LDAP URL."),



  /**
   * The length of time between searches using the criteria provided by an LDAP URL.  If a search using that criteria does not match any entries and the total duration for that URL has not yet been reached, the task will sleep for this length of time before re-issuing the search.
   */
  INFO_DELAY_DESCRIPTION_SEARCH_INTERVAL("The length of time between searches using the criteria provided by an LDAP URL.  If a search using that criteria does not match any entries and the total duration for that URL has not yet been reached, the task will sleep for this length of time before re-issuing the search."),



  /**
   * The maximum length of time to wait for the response to any single search request created from the criteria associated with a provided LDAP URL.
   */
  INFO_DELAY_DESCRIPTION_SEARCH_TIME_LIMIT("The maximum length of time to wait for the response to any single search request created from the criteria associated with a provided LDAP URL."),



  /**
   * A set of LDAP URLs that provide criteria for search operations that are eventually expected to return one or more entries.
   */
  INFO_DELAY_DESCRIPTION_SEARCH_URL("A set of LDAP URLs that provide criteria for search operations that are eventually expected to return one or more entries."),



  /**
   * The length of time that the task should sleep.  If the task will also wait until the work queue is idle or perform additional searches, then this sleep will come after those conditions have been satisfied.
   */
  INFO_DELAY_DESCRIPTION_SLEEP_DURATION("The length of time that the task should sleep.  If the task will also wait until the work queue is idle or perform additional searches, then this sleep will come after those conditions have been satisfied."),



  /**
   * The final task state that should be used if a timeout is encountered while waiting for the work queue to become idle or for a search to match one or more entries.
   */
  INFO_DELAY_DESCRIPTION_TIMEOUT_RETURN_STATE("The final task state that should be used if a timeout is encountered while waiting for the work queue to become idle or for a search to match one or more entries."),



  /**
   * The length of time to wait for the work queue to report that all worker threads are idle and there are no outstanding operations to process.
   */
  INFO_DELAY_DESCRIPTION_WAIT_FOR_WORK_QUEUE_IDLE("The length of time to wait for the work queue to report that all worker threads are idle and there are no outstanding operations to process."),



  /**
   * Total Search Duration for Each LDAP URL
   */
  INFO_DELAY_DISPLAY_NAME_SEARCH_DURATION("Total Search Duration for Each LDAP URL"),



  /**
   * Time Between Searches
   */
  INFO_DELAY_DISPLAY_NAME_SEARCH_INTERVAL("Time Between Searches"),



  /**
   * Search Time Limit
   */
  INFO_DELAY_DISPLAY_NAME_SEARCH_TIME_LIMIT("Search Time Limit"),



  /**
   * LDAP URLs for Searches Expected to Return Entries
   */
  INFO_DELAY_DISPLAY_NAME_SEARCH_URL("LDAP URLs for Searches Expected to Return Entries"),



  /**
   * Sleep Duration
   */
  INFO_DELAY_DISPLAY_NAME_SLEEP_DURATION("Sleep Duration"),



  /**
   * Task Return State in Case of a Timeout
   */
  INFO_DELAY_DISPLAY_NAME_TIMEOUT_RETURN_STATE("Task Return State in Case of a Timeout"),



  /**
   * Length of Time to Wait for the Work Queue to Be Idle
   */
  INFO_DELAY_DISPLAY_NAME_WAIT_FOR_WORK_QUEUE_IDLE("Length of Time to Wait for the Work Queue to Be Idle"),



  /**
   * Indicates whether the server should generate an alert notification if this task fails to complete successfully.
   */
  INFO_DESCRIPTION_ALERT_ON_ERROR("Indicates whether the server should generate an alert notification if this task fails to complete successfully."),



  /**
   * Indicates whether the server should generate an alert notification when this task starts running.
   */
  INFO_DESCRIPTION_ALERT_ON_START("Indicates whether the server should generate an alert notification when this task starts running."),



  /**
   * Indicates whether the server should generate an alert notification if this task completes successfully.
   */
  INFO_DESCRIPTION_ALERT_ON_SUCCESS("Indicates whether the server should generate an alert notification if this task completes successfully."),



  /**
   * Indicates whether the data in the specified LDIF files should be appended to the existing data rather than replacing it.
   */
  INFO_DESCRIPTION_APPEND_TO_DB("Indicates whether the data in the specified LDIF files should be appended to the existing data rather than replacing it."),



  /**
   * Indicates whether to append to the specified LDIF file if it already exists.  Otherwise, it will be overwritten.
   */
  INFO_DESCRIPTION_APPEND_TO_LDIF("Indicates whether to append to the specified LDIF file if it already exists.  Otherwise, it will be overwritten."),



  /**
   * The backend ID of the backend to be archived.  Multiple backend IDs may be specified as separate values.  If no backend IDs are provided, then all supported backends will be archived.
   */
  INFO_DESCRIPTION_BACKEND_ID_BACKUP("The backend ID of the backend to be archived.  Multiple backend IDs may be specified as separate values.  If no backend IDs are provided, then all supported backends will be archived."),



  /**
   * The backend ID of the backend from which the data is to be exported.
   */
  INFO_DESCRIPTION_BACKEND_ID_EXPORT("The backend ID of the backend from which the data is to be exported."),



  /**
   * The backend ID of the backend into which the data is to be imported.  If this is not specified, then one or more include branches must be defined.
   */
  INFO_DESCRIPTION_BACKEND_ID_IMPORT("The backend ID of the backend into which the data is to be imported.  If this is not specified, then one or more include branches must be defined."),



  /**
   * The path to the directory in which the backup files should be placed.  If multiple backends are to be archived, then this should be the parent directory of the backup directories for each backend.
   */
  INFO_DESCRIPTION_BACKUP_DIRECTORY_BACKUP("The path to the directory in which the backup files should be placed.  If multiple backends are to be archived, then this should be the parent directory of the backup directories for each backend."),



  /**
   * The path to the directory containing the backup to be restored.
   */
  INFO_DESCRIPTION_BACKUP_DIRECTORY_RESTORE("The path to the directory containing the backup to be restored."),



  /**
   * The backup ID to use for the backup.  If no backup ID is specified, then the server will generate one.
   */
  INFO_DESCRIPTION_BACKUP_ID_BACKUP("The backup ID to use for the backup.  If no backup ID is specified, then the server will generate one."),



  /**
   * The backup ID of the backup that should be restored.  If this is not provided, then the most recent backup contained in the specified backup directory will be used.
   */
  INFO_DESCRIPTION_BACKUP_ID_RESTORE("The backup ID of the backup that should be restored.  If this is not provided, then the most recent backup contained in the specified backup directory will be used."),



  /**
   * The maximum backup rate in megabytes per second at which the backup should be written.
   */
  INFO_DESCRIPTION_BACKUP_MAX_MEGABYTES_PER_SECOND("The maximum backup rate in megabytes per second at which the backup should be written."),



  /**
   * The minimum age of previous backups that should be retained.
   */
  INFO_DESCRIPTION_BACKUP_RETAIN_AGE("The minimum age of previous backups that should be retained."),



  /**
   * The minimum number of previous backups that should be retained.
   */
  INFO_DESCRIPTION_BACKUP_RETAIN_COUNT("The minimum number of previous backups that should be retained."),



  /**
   * The base DN of the backend below which the index data should be rebuilt.
   */
  INFO_DESCRIPTION_BASE_DN_REBUILD("The base DN of the backend below which the index data should be rebuilt."),



  /**
   * Indicates whether to clear all data in the backend when performing an import and one or more include branches is specified.  If this is ''false'', then only data below the specified include branch(es) will be cleared, and data below other base DNs will be preserved.
   */
  INFO_DESCRIPTION_CLEAR_BACKEND("Indicates whether to clear all data in the backend when performing an import and one or more include branches is specified.  If this is ''false'', then only data below the specified include branch(es) will be cleared, and data below other base DNs will be preserved."),



  /**
   * Indicates whether the contents of the backup should be compressed.
   */
  INFO_DESCRIPTION_COMPRESS_BACKUP("Indicates whether the contents of the backup should be compressed."),



  /**
   * Indicates whether to compress the exported LDIF data.
   */
  INFO_DESCRIPTION_COMPRESS_EXPORT("Indicates whether to compress the exported LDIF data."),



  /**
   * The task ID of another task that must complete before this task will be eligible to start running.  If this is not provided, then the task will be eligible to start at any time.
   */
  INFO_DESCRIPTION_DEPENDENCY_ID("The task ID of another task that must complete before this task will be eligible to start running.  If this is not provided, then the task will be eligible to start at any time."),



  /**
   * The connection ID for the client connection to be disconnected.
   */
  INFO_DESCRIPTION_DISCONNECT_CONN_ID("The connection ID for the client connection to be disconnected."),



  /**
   * A message that provides additional information about the disconnection.  If notification is to be sent to the client, then this message will be included in that notification.
   */
  INFO_DESCRIPTION_DISCONNECT_MESSAGE("A message that provides additional information about the disconnection.  If notification is to be sent to the client, then this message will be included in that notification."),



  /**
   * Indicates whether to send the client a notice of disconnection message before terminating the connection.
   */
  INFO_DESCRIPTION_DISCONNECT_NOTIFY("Indicates whether to send the client a notice of disconnection message before terminating the connection."),



  /**
   * The path to a file containing the passphrase to use to generate the encryption key.
   */
  INFO_DESCRIPTION_ENCRYPTION_PASSPHRASE_FILE("The path to a file containing the passphrase to use to generate the encryption key."),



  /**
   * The identifier for the encryption settings definition that to use to generate the encryption key.
   */
  INFO_DESCRIPTION_ENCRYPTION_SETTINGS_DEFINITION_ID("The identifier for the encryption settings definition that to use to generate the encryption key."),



  /**
   * Indicates whether the contents of the backup should be encrypted.
   */
  INFO_DESCRIPTION_ENCRYPT_BACKUP("Indicates whether the contents of the backup should be encrypted."),



  /**
   * Indicates whether to encrypt the exported LDIF data.
   */
  INFO_DESCRIPTION_ENCRYPT_EXPORT("Indicates whether to encrypt the exported LDIF data."),



  /**
   * An optional reason for putting the server into lockdown mode.
   */
  INFO_DESCRIPTION_ENTER_LOCKDOWN_REASON("An optional reason for putting the server into lockdown mode."),



  /**
   * The name of an attribute that should be excluded from exported entries.  If this is not provided, then all attributes will be included in the export.  Otherwise, none of only the specified set of attributes will be included.
   */
  INFO_DESCRIPTION_EXCLUDE_ATTRIBUTE_EXPORT("The name of an attribute that should be excluded from exported entries.  If this is not provided, then all attributes will be included in the export.  Otherwise, none of only the specified set of attributes will be included."),



  /**
   * The name of an attribute that should be excluded from imported entries.  If this is not provided, then all attributes will be included.  Otherwise, all attributes other than the excluded attributes will be included.
   */
  INFO_DESCRIPTION_EXCLUDE_ATTRIBUTE_IMPORT("The name of an attribute that should be excluded from imported entries.  If this is not provided, then all attributes will be included.  Otherwise, all attributes other than the excluded attributes will be included."),



  /**
   * The base DN of a branch that is to be excluded from the export.  If this is not provided, then all data in the backend will be eligible for inclusion in the export.  Otherwise, only data that does not exist below any of the exclude branches will be included.
   */
  INFO_DESCRIPTION_EXCLUDE_BRANCH_EXPORT("The base DN of a branch that is to be excluded from the export.  If this is not provided, then all data in the backend will be eligible for inclusion in the export.  Otherwise, only data that does not exist below any of the exclude branches will be included."),



  /**
   * The base DN of a branch to exclude from the import.  If no exclude branches are provided, then all data in the provided LDIF files will be imported.  Otherwise, only data that does not exist below one of the specified exclude branches will be imported.
   */
  INFO_DESCRIPTION_EXCLUDE_BRANCH_IMPORT("The base DN of a branch to exclude from the import.  If no exclude branches are provided, then all data in the provided LDIF files will be imported.  Otherwise, only data that does not exist below one of the specified exclude branches will be imported."),



  /**
   * A filter that will be applied to an entry to determine whether it should be excluded from the export.  If this is not provided, then all entries will be eligible for inclusion in the export.  Otherwise, no entries that match one of the provided exclude filters will be included.
   */
  INFO_DESCRIPTION_EXCLUDE_FILTER_EXPORT("A filter that will be applied to an entry to determine whether it should be excluded from the export.  If this is not provided, then all entries will be eligible for inclusion in the export.  Otherwise, no entries that match one of the provided exclude filters will be included."),



  /**
   * A filter that will be applied to an entry to determine whether it should be excluded from the import.  If this is not provided, then no entries will be excluded from the import.
   */
  INFO_DESCRIPTION_EXCLUDE_FILTER_IMPORT("A filter that will be applied to an entry to determine whether it should be excluded from the import.  If this is not provided, then no entries will be excluded from the import."),



  /**
   * The maximum backup rate in megabytes per second at which the backup should be written.
   */
  INFO_DESCRIPTION_EXPORT_MAX_MEGABYTES_PER_SECOND("The maximum backup rate in megabytes per second at which the backup should be written."),



  /**
   * Specifies the action to take with this task if any task on which it depends does not complete successfully.  Allowed values include ''cancel'', ''disable'', or ''process''.  If this is not provided, then the task will be disabled if any of the tasks on which it depends does not complete successfully.
   */
  INFO_DESCRIPTION_FAILED_DEPENDENCY_ACTION("Specifies the action to take with this task if any task on which it depends does not complete successfully.  Allowed values include ''cancel'', ''disable'', or ''process''.  If this is not provided, then the task will be disabled if any of the tasks on which it depends does not complete successfully."),



  /**
   * An argument to provide to the scripted task.  It should be in the form name=value.
   */
  INFO_DESCRIPTION_GROOVY_SCRIPTED_TASK_ARG("An argument to provide to the scripted task.  It should be in the form name=value."),



  /**
   * The fully-qualified name of the Groovy class providing the logic for the scripted task.
   */
  INFO_DESCRIPTION_GROOVY_SCRIPTED_TASK_CLASS("The fully-qualified name of the Groovy class providing the logic for the scripted task."),



  /**
   * Indicates whether to calculate a hash of the backup contents, which can be used to verify the integrity of the backup.
   */
  INFO_DESCRIPTION_HASH_BACKUP("Indicates whether to calculate a hash of the backup contents, which can be used to verify the integrity of the backup."),



  /**
   * The name of an attribute that should be included in exported entries.  If this is not provided, then all attributes will be included in the export.  Otherwise, only the specified set of attributes will be included.
   */
  INFO_DESCRIPTION_INCLUDE_ATTRIBUTE_EXPORT("The name of an attribute that should be included in exported entries.  If this is not provided, then all attributes will be included in the export.  Otherwise, only the specified set of attributes will be included."),



  /**
   * The name of an attribute that should be included in imported entries.  If this is not provided, then all attributes will be included.  Otherwise, only the specified attributes will be included.
   */
  INFO_DESCRIPTION_INCLUDE_ATTRIBUTE_IMPORT("The name of an attribute that should be included in imported entries.  If this is not provided, then all attributes will be included.  Otherwise, only the specified attributes will be included."),



  /**
   * The base DN of a branch that is to be included in the export.  If this is not provided, then all data in the backend will be eligible for inclusion in the export.  Otherwise, only data that exists below one of the include branches will be included.
   */
  INFO_DESCRIPTION_INCLUDE_BRANCH_EXPORT("The base DN of a branch that is to be included in the export.  If this is not provided, then all data in the backend will be eligible for inclusion in the export.  Otherwise, only data that exists below one of the include branches will be included."),



  /**
   * The base DN of a branch to include in the import.  If no include branches are provided, then a backend ID must be given, and all data in the provided LDIF files will be imported.  Otherwise, only data that exists below one of the specified include branches will be imported.
   */
  INFO_DESCRIPTION_INCLUDE_BRANCH_IMPORT("The base DN of a branch to include in the import.  If no include branches are provided, then a backend ID must be given, and all data in the provided LDIF files will be imported.  Otherwise, only data that exists below one of the specified include branches will be imported."),



  /**
   * A filter that will be applied to an entry to determine whether it should be included in the export.  If this is not provided, then all entries will be eligible for inclusion in the export.  Otherwise, only entries that match one of the provided include filters will be included.
   */
  INFO_DESCRIPTION_INCLUDE_FILTER_EXPORT("A filter that will be applied to an entry to determine whether it should be included in the export.  If this is not provided, then all entries will be eligible for inclusion in the export.  Otherwise, only entries that match one of the provided include filters will be included."),



  /**
   * A filter that will be applied to an entry to determine whether it should be included in the import.  If this is not provided, then all entries will be included in the import.
   */
  INFO_DESCRIPTION_INCLUDE_FILTER_IMPORT("A filter that will be applied to an entry to determine whether it should be included in the import.  If this is not provided, then all entries will be included in the import."),



  /**
   * Indicates whether to attempt an incremental backup rather than a full backup.  An incremental backup includes only that information which has changed since the previous backup.  Note that if the associated backend does not support incremental backups, then a full backup will be performed.
   */
  INFO_DESCRIPTION_INCREMENTAL("Indicates whether to attempt an incremental backup rather than a full backup.  An incremental backup includes only that information which has changed since the previous backup.  Note that if the associated backend does not support incremental backups, then a full backup will be performed."),



  /**
   * The backup ID for the previous backup on which to base an incremental backup.  If this is not provided, then the server will base the incremental backup on the last available backup in the backup directory.  This will be ignored for non-incremental backups.
   */
  INFO_DESCRIPTION_INCREMENTAL_BASE_ID("The backup ID for the previous backup on which to base an incremental backup.  If this is not provided, then the server will base the incremental backup on the last available backup in the backup directory.  This will be ignored for non-incremental backups."),



  /**
   * The name of the index to be rebuilt.  For attribute indexes, this is the name of the associated attribute.  For VLV indexes, this is the name of the VLV index.  Multiple indexes can be rebuilt by providing multiple index names as separate values.
   */
  INFO_DESCRIPTION_INDEX_REBUILD("The name of the index to be rebuilt.  For attribute indexes, this is the name of the associated attribute.  For VLV indexes, this is the name of the VLV index.  Multiple indexes can be rebuilt by providing multiple index names as separate values."),



  /**
   * Indicates whether the LDIF data to be imported is compressed.
   */
  INFO_DESCRIPTION_IS_COMPRESSED_IMPORT("Indicates whether the LDIF data to be imported is compressed."),



  /**
   * Indicates whether the LDIF data to be imported is encrypted.
   */
  INFO_DESCRIPTION_IS_ENCRYPTED_IMPORT("Indicates whether the LDIF data to be imported is encrypted."),



  /**
   * The path to the LDIF file to which the exported data is to be written.
   */
  INFO_DESCRIPTION_LDIF_FILE_EXPORT("The path to the LDIF file to which the exported data is to be written."),



  /**
   * Specifies the path to the LDIF file containing the data to be imported.  Multiple LDIF files can be specified as separate values, and the data contained in them will be processed in the order they were specified.
   */
  INFO_DESCRIPTION_LDIF_FILE_IMPORT("Specifies the path to the LDIF file containing the data to be imported.  Multiple LDIF files can be specified as separate values, and the data contained in them will be processed in the order they were specified."),



  /**
   * An optional reason for taking the server out of lockdown mode.
   */
  INFO_DESCRIPTION_LEAVE_LOCKDOWN_REASON("An optional reason for taking the server out of lockdown mode."),



  /**
   * The maximum number of concurrent threads to use when rebuilding index data.  A value less than or equal to zero indicates that there should be no limit to the number of threads that may be used.
   */
  INFO_DESCRIPTION_MAX_THREADS_REBUILD("The maximum number of concurrent threads to use when rebuilding index data.  A value less than or equal to zero indicates that there should be no limit to the number of threads that may be used."),



  /**
   * The e-mail address of an individual that should be sent a notification message whenever this task completes.  The notification message will be sent regardless of whether the task completes successfully.
   */
  INFO_DESCRIPTION_NOTIFY_ON_COMPLETION("The e-mail address of an individual that should be sent a notification message whenever this task completes.  The notification message will be sent regardless of whether the task completes successfully."),



  /**
   * The e-mail address of an individual that should be sent a notification message if this task is unable to complete successfully.  No message will be sent to this address if the task does complete successfully.
   */
  INFO_DESCRIPTION_NOTIFY_ON_ERROR("The e-mail address of an individual that should be sent a notification message if this task is unable to complete successfully.  No message will be sent to this address if the task does complete successfully."),



  /**
   * The e-mail address of an individual that should be sent a notification message when this task starts running.
   */
  INFO_DESCRIPTION_NOTIFY_ON_START("The e-mail address of an individual that should be sent a notification message when this task starts running."),



  /**
   * The e-mail address of an individual that should be sent a notification message if this task completes successfully.  No message will be sent to this address if the task does not complete successfully.
   */
  INFO_DESCRIPTION_NOTIFY_ON_SUCCESS("The e-mail address of an individual that should be sent a notification message if this task completes successfully.  No message will be sent to this address if the task does not complete successfully."),



  /**
   * Indicates whether to overwrite an existing reject file if it exists.  Otherwise, the server will append to the file.
   */
  INFO_DESCRIPTION_OVERWRITE_REJECTS("Indicates whether to overwrite an existing reject file if it exists.  Otherwise, the server will append to the file."),



  /**
   * The backend ID for the backend in which the re-encode processing is to be performed.
   */
  INFO_DESCRIPTION_REENCODE_BACKEND_ID("The backend ID for the backend in which the re-encode processing is to be performed."),



  /**
   * The base DN of a branch to exclude from re-encode processing.
   */
  INFO_DESCRIPTION_REENCODE_EXCLUDE_BRANCH("The base DN of a branch to exclude from re-encode processing."),



  /**
   * A filter to use to identify entries to exclude from re-encode processing.
   */
  INFO_DESCRIPTION_REENCODE_EXCLUDE_FILTER("A filter to use to identify entries to exclude from re-encode processing."),



  /**
   * The base DN of a branch to include in re-encode processing.
   */
  INFO_DESCRIPTION_REENCODE_INCLUDE_BRANCH("The base DN of a branch to include in re-encode processing."),



  /**
   * A filter to use to identify entries to include in re-encode processing.
   */
  INFO_DESCRIPTION_REENCODE_INCLUDE_FILTER("A filter to use to identify entries to include in re-encode processing."),



  /**
   * The maximum number of entries to be re-encoded per second.
   */
  INFO_DESCRIPTION_REENCODE_MAX_ENTRIES_PER_SECOND("The maximum number of entries to be re-encoded per second."),



  /**
   * Indicates whether to skip re-encode processing for entries that are stored completely uncached.
   */
  INFO_DESCRIPTION_REENCODE_SKIP_FULLY_UNCACHED("Indicates whether to skip re-encode processing for entries that are stored completely uncached."),



  /**
   * Indicates whether to skip re-encode processing for entries that are stored with a mix of cached and uncached attributes.
   */
  INFO_DESCRIPTION_REENCODE_SKIP_PARTIALLY_UNCACHED("Indicates whether to skip re-encode processing for entries that are stored with a mix of cached and uncached attributes."),



  /**
   * The path to a file to which information about rejected entries should be written.  If this is not specified, then no reject file will be written.
   */
  INFO_DESCRIPTION_REJECT_FILE("The path to a file to which information about rejected entries should be written.  If this is not specified, then no reject file will be written."),



  /**
   * The name(s) of the attributes for which to reload index information.  If this is not provided, then all indexes will be reloaded.
   */
  INFO_DESCRIPTION_RELOAD_GLOBAL_INDEX_ATTR_NAME("The name(s) of the attributes for which to reload index information.  If this is not provided, then all indexes will be reloaded."),



  /**
   * Indicates whether to perform the reload in a background thread so that the task completes immediately.
   */
  INFO_DESCRIPTION_RELOAD_GLOBAL_INDEX_BACKGROUND("Indicates whether to perform the reload in a background thread so that the task completes immediately."),



  /**
   * The base DN of the entry-balancing request processor for which to reload global index information.  This must be specified.
   */
  INFO_DESCRIPTION_RELOAD_GLOBAL_INDEX_BASE_DN("The base DN of the entry-balancing request processor for which to reload global index information.  This must be specified."),



  /**
   * An optional target maximum rate at which entries should be reloaded.  A value of zero indicates no limit.  If this is not specified, then the rate limit will be determined from the Directory Proxy Server configuration.
   */
  INFO_DESCRIPTION_RELOAD_GLOBAL_INDEX_MAX_ENTRIES_PER_SECOND("An optional target maximum rate at which entries should be reloaded.  A value of zero indicates no limit.  If this is not specified, then the rate limit will be determined from the Directory Proxy Server configuration."),



  /**
   * Indicates whether to retrieve the index information from backend Directory Server instances rather than a peer Directory Proxy Server instance.
   */
  INFO_DESCRIPTION_RELOAD_GLOBAL_INDEX_RELOAD_FROM_DS("Indicates whether to retrieve the index information from backend Directory Server instances rather than a peer Directory Proxy Server instance."),



  /**
   * Indicates whether to replace an existing entry if it is contained in the LDIF file when appending to the database.  This is ignored when not operating in append mode.
   */
  INFO_DESCRIPTION_REPLACE_EXISTING("Indicates whether to replace an existing entry if it is contained in the LDIF file when appending to the database.  This is ignored when not operating in append mode."),



  /**
   * Indicates that the server should be restarted instead of shut down.
   */
  INFO_DESCRIPTION_RESTART_SERVER("Indicates that the server should be restarted instead of shut down."),



  /**
   * The earliest time that this task should be allowed to start running.  If it is specified, then it must be in generalized time format (e.g., ''YYYYMMDDhhmmssZ'' for UTC or ''YYYYMMDDhhmmss+0500'' to specify a UTC offset).  If this is not provided, then the task will be eligible to start at any time.
   */
  INFO_DESCRIPTION_SCHEDULED_START_TIME("The earliest time that this task should be allowed to start running.  If it is specified, then it must be in generalized time format (e.g., ''YYYYMMDDhhmmssZ'' for UTC or ''YYYYMMDDhhmmss+0500'' to specify a UTC offset).  If this is not provided, then the task will be eligible to start at any time."),



  /**
   * The name (without path information) of the file whose contents should be added to the server schema.  The file must exist in the server''s schema directory.
   */
  INFO_DESCRIPTION_SCHEMA_FILE("The name (without path information) of the file whose contents should be added to the server schema.  The file must exist in the server''s schema directory."),



  /**
   * A human-readable message that may be used to provide information about the reason for the shutdown.
   */
  INFO_DESCRIPTION_SHUTDOWN_MESSAGE("A human-readable message that may be used to provide information about the reason for the shutdown."),



  /**
   * Indicates whether to include a digital signature at the end of the export data which can be used to ensure that data has not been altered.
   */
  INFO_DESCRIPTION_SIGN_EXPORT("Indicates whether to include a digital signature at the end of the export data which can be used to ensure that data has not been altered."),



  /**
   * Indicates whether the backup hash should be digitally signed to ensure that it cannot be altered.
   */
  INFO_DESCRIPTION_SIGN_HASH_BACKUP("Indicates whether the backup hash should be digitally signed to ensure that it cannot be altered."),



  /**
   * Indicates whether the server should skip schema validation for the entries being imported.
   */
  INFO_DESCRIPTION_SKIP_SCHEMA_VALIDATION("Indicates whether the server should skip schema validation for the entries being imported."),



  /**
   * Indicates whether the server should strip illegal trailing spaces from LDIF records rather than rejecting those records.
   */
  INFO_DESCRIPTION_STRIP_TRAILING_SPACES("Indicates whether the server should strip illegal trailing spaces from LDIF records rather than rejecting those records."),



  /**
   * The unique identifier for the task.  It must not already be in use by any task available in the Directory Server.  If this is not provided, then a UUID will be generated for use as the task ID.
   */
  INFO_DESCRIPTION_TASK_ID("The unique identifier for the task.  It must not already be in use by any task available in the Directory Server.  If this is not provided, then a UUID will be generated for use as the task ID."),



  /**
   * An argument to provide to the third-party task.  It should be in the form name=value.
   */
  INFO_DESCRIPTION_THIRD_PARTY_TASK_ARG("An argument to provide to the third-party task.  It should be in the form name=value."),



  /**
   * The fully-qualified name of the Java class providing the logic for the third-party task.
   */
  INFO_DESCRIPTION_THIRD_PARTY_TASK_CLASS("The fully-qualified name of the Java class providing the logic for the third-party task."),



  /**
   * Indicates whether the server should attempt to verify whether it should be possible to restore the specified backup without actually performing the restore.
   */
  INFO_DESCRIPTION_VERIFY_ONLY("Indicates whether the server should attempt to verify whether it should be possible to restore the specified backup without actually performing the restore."),



  /**
   * The column at which long lines should be wrapped when writing the LDIF data.  If this is not provided, or if the provided value is less than or equal to zero, then no wrapping will be performed.
   */
  INFO_DESCRIPTION_WRAP_COLUMN("The column at which long lines should be wrapped when writing the LDIF data.  If this is not provided, or if the provided value is less than or equal to zero, then no wrapping will be performed."),



  /**
   * Alert on Error
   */
  INFO_DISPLAY_NAME_ALERT_ON_ERROR("Alert on Error"),



  /**
   * Alert on Start
   */
  INFO_DISPLAY_NAME_ALERT_ON_START("Alert on Start"),



  /**
   * Alert on Success
   */
  INFO_DISPLAY_NAME_ALERT_ON_SUCCESS("Alert on Success"),



  /**
   * Append to Existing Data
   */
  INFO_DISPLAY_NAME_APPEND_TO_DB("Append to Existing Data"),



  /**
   * Append to LDIF File
   */
  INFO_DISPLAY_NAME_APPEND_TO_LDIF("Append to LDIF File"),



  /**
   * Backend ID
   */
  INFO_DISPLAY_NAME_BACKEND_ID("Backend ID"),



  /**
   * Backup Directory Path
   */
  INFO_DISPLAY_NAME_BACKUP_DIRECTORY("Backup Directory Path"),



  /**
   * Backup ID
   */
  INFO_DISPLAY_NAME_BACKUP_ID("Backup ID"),



  /**
   * Maximum Megabytes Per Second
   */
  INFO_DISPLAY_NAME_BACKUP_MAX_MEGABYTES_PER_SECOND("Maximum Megabytes Per Second"),



  /**
   * Retain Previous Backup Age
   */
  INFO_DISPLAY_NAME_BACKUP_RETAIN_AGE("Retain Previous Backup Age"),



  /**
   * Retain Previous Backup Count
   */
  INFO_DISPLAY_NAME_BACKUP_RETAIN_COUNT("Retain Previous Backup Count"),



  /**
   * Backend Base DN
   */
  INFO_DISPLAY_NAME_BASE_DN_REBUILD("Backend Base DN"),



  /**
   * Clear the Entire Backend
   */
  INFO_DISPLAY_NAME_CLEAR_BACKEND("Clear the Entire Backend"),



  /**
   * Compress
   */
  INFO_DISPLAY_NAME_COMPRESS("Compress"),



  /**
   * Depends on Task
   */
  INFO_DISPLAY_NAME_DEPENDENCY_ID("Depends on Task"),



  /**
   * Connection ID
   */
  INFO_DISPLAY_NAME_DISCONNECT_CONN_ID("Connection ID"),



  /**
   * Disconnect Message
   */
  INFO_DISPLAY_NAME_DISCONNECT_MESSAGE("Disconnect Message"),



  /**
   * Notify Client Before Disconnecting
   */
  INFO_DISPLAY_NAME_DISCONNECT_NOTIFY("Notify Client Before Disconnecting"),



  /**
   * Encrypt
   */
  INFO_DISPLAY_NAME_ENCRYPT("Encrypt"),



  /**
   * Encryption Passphrase File
   */
  INFO_DISPLAY_NAME_ENCRYPTION_PASSPHRASE_FILE("Encryption Passphrase File"),



  /**
   * Encryption Settings Definition ID
   */
  INFO_DISPLAY_NAME_ENCRYPTION_SETTINGS_DEFINITION_ID("Encryption Settings Definition ID"),



  /**
   * Reason
   */
  INFO_DISPLAY_NAME_ENTER_LOCKDOWN_REASON("Reason"),



  /**
   * Exclude Attribute
   */
  INFO_DISPLAY_NAME_EXCLUDE_ATTRIBUTE("Exclude Attribute"),



  /**
   * Include Branch DN
   */
  INFO_DISPLAY_NAME_EXCLUDE_BRANCH("Include Branch DN"),



  /**
   * Exclude Filter
   */
  INFO_DISPLAY_NAME_EXCLUDE_FILTER("Exclude Filter"),



  /**
   * Maximum Megabytes Per Second
   */
  INFO_DISPLAY_NAME_EXPORT_MAX_MEGABYTES_PER_SECOND("Maximum Megabytes Per Second"),



  /**
   * Failed Dependency Action
   */
  INFO_DISPLAY_NAME_FAILED_DEPENDENCY_ACTION("Failed Dependency Action"),



  /**
   * Groovy-Scripted Task Argument
   */
  INFO_DISPLAY_NAME_GROOVY_SCRIPTED_TASK_ARG("Groovy-Scripted Task Argument"),



  /**
   * Groovy-Scripted Task Class Name
   */
  INFO_DISPLAY_NAME_GROOVY_SCRIPTED_TASK_CLASS("Groovy-Scripted Task Class Name"),



  /**
   * Calculate Hash
   */
  INFO_DISPLAY_NAME_HASH("Calculate Hash"),



  /**
   * Include Attribute
   */
  INFO_DISPLAY_NAME_INCLUDE_ATTRIBUTE("Include Attribute"),



  /**
   * Include Branch DN
   */
  INFO_DISPLAY_NAME_INCLUDE_BRANCH("Include Branch DN"),



  /**
   * Include Filter
   */
  INFO_DISPLAY_NAME_INCLUDE_FILTER("Include Filter"),



  /**
   * Incremental Backup
   */
  INFO_DISPLAY_NAME_INCREMENTAL("Incremental Backup"),



  /**
   * Incremental Base ID
   */
  INFO_DISPLAY_NAME_INCREMENTAL_BASE_ID("Incremental Base ID"),



  /**
   * Index Name
   */
  INFO_DISPLAY_NAME_INDEX_REBUILD("Index Name"),



  /**
   * LDIF Data Is Compressed
   */
  INFO_DISPLAY_NAME_IS_COMPRESSED_IMPORT("LDIF Data Is Compressed"),



  /**
   * LDIF Data Is Encrypted
   */
  INFO_DISPLAY_NAME_IS_ENCRYPTED_IMPORT("LDIF Data Is Encrypted"),



  /**
   * LDIF File Path
   */
  INFO_DISPLAY_NAME_LDIF_FILE("LDIF File Path"),



  /**
   * Reason
   */
  INFO_DISPLAY_NAME_LEAVE_LOCKDOWN_REASON("Reason"),



  /**
   * Maximum Concurrent Rebuild Threads
   */
  INFO_DISPLAY_NAME_MAX_THREADS_REBUILD("Maximum Concurrent Rebuild Threads"),



  /**
   * Notify on Completion
   */
  INFO_DISPLAY_NAME_NOTIFY_ON_COMPLETION("Notify on Completion"),



  /**
   * Notify on Error
   */
  INFO_DISPLAY_NAME_NOTIFY_ON_ERROR("Notify on Error"),



  /**
   * Notify on Start
   */
  INFO_DISPLAY_NAME_NOTIFY_ON_START("Notify on Start"),



  /**
   * Notify on Success
   */
  INFO_DISPLAY_NAME_NOTIFY_ON_SUCCESS("Notify on Success"),



  /**
   * Overwrite Existing Reject File
   */
  INFO_DISPLAY_NAME_OVERWRITE_REJECTS("Overwrite Existing Reject File"),



  /**
   * Backend ID
   */
  INFO_DISPLAY_NAME_REENCODE_BACKEND_ID("Backend ID"),



  /**
   * Exclude Branch
   */
  INFO_DISPLAY_NAME_REENCODE_EXCLUDE_BRANCH("Exclude Branch"),



  /**
   * Exclude Filter
   */
  INFO_DISPLAY_NAME_REENCODE_EXCLUDE_FILTER("Exclude Filter"),



  /**
   * Include Branch
   */
  INFO_DISPLAY_NAME_REENCODE_INCLUDE_BRANCH("Include Branch"),



  /**
   * Include Filter
   */
  INFO_DISPLAY_NAME_REENCODE_INCLUDE_FILTER("Include Filter"),



  /**
   * Maximum Entries to Re-Encode Per Second
   */
  INFO_DISPLAY_NAME_REENCODE_MAX_ENTRIES_PER_SECOND("Maximum Entries to Re-Encode Per Second"),



  /**
   * Skip Fully Uncached Entries
   */
  INFO_DISPLAY_NAME_REENCODE_SKIP_FULLY_UNCACHED("Skip Fully Uncached Entries"),



  /**
   * Skip Partially Uncached Entries
   */
  INFO_DISPLAY_NAME_REENCODE_SKIP_PARTIALLY_UNCACHED("Skip Partially Uncached Entries"),



  /**
   * Reject File Path
   */
  INFO_DISPLAY_NAME_REJECT_FILE("Reject File Path"),



  /**
   * Attribute Name
   */
  INFO_DISPLAY_NAME_RELOAD_GLOBAL_INDEX_ATTR_NAME("Attribute Name"),



  /**
   * Reload in a Background Thread
   */
  INFO_DISPLAY_NAME_RELOAD_GLOBAL_INDEX_BACKGROUND("Reload in a Background Thread"),



  /**
   * Entry-Balancing Request Processor Base DN
   */
  INFO_DISPLAY_NAME_RELOAD_GLOBAL_INDEX_BASE_DN("Entry-Balancing Request Processor Base DN"),



  /**
   * Maximum Reload Rate (Entries/Second)
   */
  INFO_DISPLAY_NAME_RELOAD_GLOBAL_INDEX_MAX_ENTRIES_PER_SECOND("Maximum Reload Rate (Entries/Second)"),



  /**
   * Reload from Backend Directory Servers
   */
  INFO_DISPLAY_NAME_RELOAD_GLOBAL_INDEX_RELOAD_FROM_DS("Reload from Backend Directory Servers"),



  /**
   * Replace Existing Entries when Appending
   */
  INFO_DISPLAY_NAME_REPLACE_EXISTING("Replace Existing Entries when Appending"),



  /**
   * Restart Instead of Shut Down
   */
  INFO_DISPLAY_NAME_RESTART_SERVER("Restart Instead of Shut Down"),



  /**
   * Scheduled Start Time
   */
  INFO_DISPLAY_NAME_SCHEDULED_START_TIME("Scheduled Start Time"),



  /**
   * Schema File Name
   */
  INFO_DISPLAY_NAME_SCHEMA_FILE("Schema File Name"),



  /**
   * Shutdown Message
   */
  INFO_DISPLAY_NAME_SHUTDOWN_MESSAGE("Shutdown Message"),



  /**
   * Sign Export
   */
  INFO_DISPLAY_NAME_SIGN("Sign Export"),



  /**
   * Sign Hash
   */
  INFO_DISPLAY_NAME_SIGN_HASH("Sign Hash"),



  /**
   * Skip Schema Validation
   */
  INFO_DISPLAY_NAME_SKIP_SCHEMA_VALIDATION("Skip Schema Validation"),



  /**
   * Strip Trailing Spaces
   */
  INFO_DISPLAY_NAME_STRIP_TRAILING_SPACES("Strip Trailing Spaces"),



  /**
   * Task ID
   */
  INFO_DISPLAY_NAME_TASK_ID("Task ID"),



  /**
   * Third-Party Task Argument
   */
  INFO_DISPLAY_NAME_THIRD_PARTY_TASK_ARG("Third-Party Task Argument"),



  /**
   * Third-Party Task Class Name
   */
  INFO_DISPLAY_NAME_THIRD_PARTY_TASK_CLASS("Third-Party Task Class Name"),



  /**
   * Verify Without Restoring
   */
  INFO_DISPLAY_NAME_VERIFY_ONLY("Verify Without Restoring"),



  /**
   * Wrap Column
   */
  INFO_DISPLAY_NAME_WRAP_COLUMN("Wrap Column"),



  /**
   * The backend ID for the backend whose contents should be examined.
   */
  INFO_DUMP_DB_DESCRIPTION_BACKEND_ID("The backend ID for the backend whose contents should be examined."),



  /**
   * Backend ID
   */
  INFO_DUMP_DB_DISPLAY_NAME_BACKEND_ID("Backend ID"),



  /**
   * A string containing the arguments to use when running the command.
   */
  INFO_EXEC_DESCRIPTION_COMMAND_ARGUMENTS("A string containing the arguments to use when running the command."),



  /**
   * A string containing the path (on the server filesystem) to the output file to which the command output should be written.  The path may be absolute or relative, and if it is relative, then it will be interpreted as relative to the server root.
   */
  INFO_EXEC_DESCRIPTION_COMMAND_OUTPUT_FILE("A string containing the path (on the server filesystem) to the output file to which the command output should be written.  The path may be absolute or relative, and if it is relative, then it will be interpreted as relative to the server root."),



  /**
   * The absolute path (on the server filesystem) for the command to execute.
   */
  INFO_EXEC_DESCRIPTION_COMMAND_PATH("The absolute path (on the server filesystem) for the command to execute."),



  /**
   * Indicates whether the command''s output should be recorded in the server''s error log.
   */
  INFO_EXEC_DESCRIPTION_LOG_COMMAND_OUTPUT("Indicates whether the command''s output should be recorded in the server''s error log."),



  /**
   * The task state that should be used if the command completes with a nonzero exit code.  Valid values include ''STOPPED_BY_ERROR'', ''COMPLETED_WITH_ERRORS'', and ''COMPLETED_SUCCESSFULLY''.
   */
  INFO_EXEC_DESCRIPTION_TASK_STATE_FOR_NONZERO_EXIT_CODE("The task state that should be used if the command completes with a nonzero exit code.  Valid values include ''STOPPED_BY_ERROR'', ''COMPLETED_WITH_ERRORS'', and ''COMPLETED_SUCCESSFULLY''."),



  /**
   * The path on the server to use as the working directory when executing the command.  If this is not specified, the server root will be used as the default working directory.
   */
  INFO_EXEC_DESCRIPTION_WORKING_DIRECTORY("The path on the server to use as the working directory when executing the command.  If this is not specified, the server root will be used as the default working directory."),



  /**
   * Command Arguments
   */
  INFO_EXEC_DISPLAY_NAME_COMMAND_ARGUMENTS("Command Arguments"),



  /**
   * Command Output File
   */
  INFO_EXEC_DISPLAY_NAME_COMMAND_OUTPUT_FILE("Command Output File"),



  /**
   * Command Path
   */
  INFO_EXEC_DISPLAY_NAME_COMMAND_PATH("Command Path"),



  /**
   * Log Command Output to Server Error Log
   */
  INFO_EXEC_DISPLAY_NAME_LOG_COMMAND_OUTPUT("Log Command Output to Server Error Log"),



  /**
   * Task Completion State for Nonzero Exit Code
   */
  INFO_EXEC_DISPLAY_NAME_TASK_STATE_FOR_NONZERO_EXIT_CODE("Task Completion State for Nonzero Exit Code"),



  /**
   * Working Directory
   */
  INFO_EXEC_DISPLAY_NAME_WORKING_DIRECTORY("Working Directory"),



  /**
   * The pattern to use to identify the matching files.  It may contain zero or more asterisks to use as wildcards, and at most one occurrence of the token ''$'{'timestamp'}''' to specify the position of a timestamp in the filename.
   */
  INFO_FILE_RETENTION_DESCRIPTION_FILENAME_PATTERN("The pattern to use to identify the matching files.  It may contain zero or more asterisks to use as wildcards, and at most one occurrence of the token ''$'{'timestamp'}''' to specify the position of a timestamp in the filename."),



  /**
   * The minimum age (in milliseconds) of files to retain.
   */
  INFO_FILE_RETENTION_DESCRIPTION_RETAIN_AGE("The minimum age (in milliseconds) of files to retain."),



  /**
   * The minimum number of files to retain.
   */
  INFO_FILE_RETENTION_DESCRIPTION_RETAIN_COUNT("The minimum number of files to retain."),



  /**
   * The minimum aggregate size (in bytes) of files to retain.
   */
  INFO_FILE_RETENTION_DESCRIPTION_RETAIN_SIZE("The minimum aggregate size (in bytes) of files to retain."),



  /**
   * The path to the directory (on the server filesystem) that contains the files to examine.
   */
  INFO_FILE_RETENTION_DESCRIPTION_TARGET_DIRECTORY("The path to the directory (on the server filesystem) that contains the files to examine."),



  /**
   * The format to use for timestamp values in the filename pattern.
   */
  INFO_FILE_RETENTION_DESCRIPTION_TIMESTAMP_FORMAT("The format to use for timestamp values in the filename pattern."),



  /**
   * Filename Pattern
   */
  INFO_FILE_RETENTION_DISPLAY_NAME_FILENAME_PATTERN("Filename Pattern"),



  /**
   * Retain File Age in Milliseconds
   */
  INFO_FILE_RETENTION_DISPLAY_NAME_RETAIN_AGE("Retain File Age in Milliseconds"),



  /**
   * Retain File Count
   */
  INFO_FILE_RETENTION_DISPLAY_NAME_RETAIN_COUNT("Retain File Count"),



  /**
   * Retain Aggregate File Size in Bytes
   */
  INFO_FILE_RETENTION_DISPLAY_NAME_RETAIN_SIZE("Retain Aggregate File Size in Bytes"),



  /**
   * Target Directory
   */
  INFO_FILE_RETENTION_DISPLAY_NAME_TARGET_DIRECTORY("Target Directory"),



  /**
   * Timestamp Format
   */
  INFO_FILE_RETENTION_DISPLAY_NAME_TIMESTAMP_FORMAT("Timestamp Format"),



  /**
   * A set of paths to additional files or directories within the instance root that should be included in the generated server profile.  Relative paths will be relative to the instance root.
   */
  INFO_GENERATE_SERVER_PROFILE_ATTR_DESCRIPTION_INCLUDE_PATH("A set of paths to additional files or directories within the instance root that should be included in the generated server profile.  Relative paths will be relative to the instance root."),



  /**
   * The path to the zip file or directory on the server filesystem to which the generated server profile should be written.  This may be an absolute path or a relative path that is relative to the instance root.  If the server profile is to be packaged into a zip file, then this may either be the path to the zip file to be created (which must not yet exist, although its parent directory must exist), or the path to the directory in which the zip file will be created with a server-generated name.  If the profile will not be packaged into a zip file, then this must be the path to the directory in which the profile will be written, and either that directory must exist and be empty, or it must not exist but its parent directory must exist.  If this is not provided, then a zip file or directory will be created in the instance root directory.
   */
  INFO_GENERATE_SERVER_PROFILE_ATTR_DESCRIPTION_PROFILE_ROOT("The path to the zip file or directory on the server filesystem to which the generated server profile should be written.  This may be an absolute path or a relative path that is relative to the instance root.  If the server profile is to be packaged into a zip file, then this may either be the path to the zip file to be created (which must not yet exist, although its parent directory must exist), or the path to the directory in which the zip file will be created with a server-generated name.  If the profile will not be packaged into a zip file, then this must be the path to the directory in which the profile will be written, and either that directory must exist and be empty, or it must not exist but its parent directory must exist.  If this is not provided, then a zip file or directory will be created in the instance root directory."),



  /**
   * The minimum age of previous profile zip files that should be retained.  This may only be provided if the server profile will be packaged into a zip file, and if the name of the zip file will be generated by the server (that is, the profile root specifies a directory rather than a file).  If this is provided, then any preexisting profile zip files older than this age may be deleted.  Duration values should be specified as an integer followed by a time unit (millisecond, second, minute, hour, day, or week, or any of their plurals), like ''5 minutes'' or ''1 hour''.  If this is not provided, the server will either use only a count to determine the number of files to retain (if a count was provided) or will not remove any previous profiles (if no count was provided).
   */
  INFO_GENERATE_SERVER_PROFILE_ATTR_DESCRIPTION_RETAIN_AGE("The minimum age of previous profile zip files that should be retained.  This may only be provided if the server profile will be packaged into a zip file, and if the name of the zip file will be generated by the server (that is, the profile root specifies a directory rather than a file).  If this is provided, then any preexisting profile zip files older than this age may be deleted.  Duration values should be specified as an integer followed by a time unit (millisecond, second, minute, hour, day, or week, or any of their plurals), like ''5 minutes'' or ''1 hour''.  If this is not provided, the server will either use only a count to determine the number of files to retain (if a count was provided) or will not remove any previous profiles (if no count was provided)."),



  /**
   * The minimum number of previous profile zip files that should be retained.  This may only be provided if the server profile will be packaged into a zip file, and if the name of the zip file will be generated by the server (that is, the profile root specifies a directory rather than a file).  If this is provided, then at least the specified number of the most recent preexisting profile zip files will be retained, but older archives may be removed.  If this is not provided, the server will either use only a duration to determine which profile zip files to retain (if a retain age was provided) or will not remove any previous files (if no retain age was provided).
   */
  INFO_GENERATE_SERVER_PROFILE_ATTR_DESCRIPTION_RETAIN_COUNT("The minimum number of previous profile zip files that should be retained.  This may only be provided if the server profile will be packaged into a zip file, and if the name of the zip file will be generated by the server (that is, the profile root specifies a directory rather than a file).  If this is provided, then at least the specified number of the most recent preexisting profile zip files will be retained, but older archives may be removed.  If this is not provided, the server will either use only a duration to determine which profile zip files to retain (if a retain age was provided) or will not remove any previous files (if no retain age was provided)."),



  /**
   * Indicates whether the generated server profile should be packaged into a zip file.
   */
  INFO_GENERATE_SERVER_PROFILE_ATTR_DESCRIPTION_ZIP("Indicates whether the generated server profile should be packaged into a zip file."),



  /**
   * Include Paths
   */
  INFO_GENERATE_SERVER_PROFILE_ATTR_DISPLAY_NAME_INCLUDE_PATH("Include Paths"),



  /**
   * Profile Root
   */
  INFO_GENERATE_SERVER_PROFILE_ATTR_DISPLAY_NAME_PROFILE_ROOT("Profile Root"),



  /**
   * Retain Previous Profile Age
   */
  INFO_GENERATE_SERVER_PROFILE_ATTR_DISPLAY_NAME_RETAIN_AGE("Retain Previous Profile Age"),



  /**
   * Retain Previous Profile Count
   */
  INFO_GENERATE_SERVER_PROFILE_ATTR_DISPLAY_NAME_RETAIN_COUNT("Retain Previous Profile Count"),



  /**
   * Zip Profile
   */
  INFO_GENERATE_SERVER_PROFILE_ATTR_DISPLAY_NAME_ZIP("Zip Profile"),



  /**
   * The backend IDs of the backends in which composed values should be generated.  If this is not specified, then an appropriate set of backends will automatically be determined from the base DN criteria defined in the configurations of the selected plugin instances.
   */
  INFO_POPULATE_COMPOSED_ATTR_DESCRIPTION_BACKEND_ID("The backend IDs of the backends in which composed values should be generated.  If this is not specified, then an appropriate set of backends will automatically be determined from the base DN criteria defined in the configurations of the selected plugin instances."),



  /**
   * The maximum number of entries to update per second with composed values.  If this is not specified, then no rate limit will be imposed.
   */
  INFO_POPULATE_COMPOSED_ATTR_DESCRIPTION_MAX_RATE("The maximum number of entries to update per second with composed values.  If this is not specified, then no rate limit will be imposed."),



  /**
   * The names or DNs of the configuration entries for the composed attribute plugins for which to generate values.  If this is not specified, then values will be generated enabled composed attribute plugin instances defined in the configuration.
   */
  INFO_POPULATE_COMPOSED_ATTR_DESCRIPTION_PLUGIN_CONFIG("The names or DNs of the configuration entries for the composed attribute plugins for which to generate values.  If this is not specified, then values will be generated enabled composed attribute plugin instances defined in the configuration."),



  /**
   * Backend IDs
   */
  INFO_POPULATE_COMPOSED_ATTR_DISPLAY_NAME_BACKEND_ID("Backend IDs"),



  /**
   * Max Rate Per Second
   */
  INFO_POPULATE_COMPOSED_ATTR_DISPLAY_NAME_MAX_RATE("Max Rate Per Second"),



  /**
   * Plugin Configurations
   */
  INFO_POPULATE_COMPOSED_ATTR_DISPLAY_NAME_PLUGIN_CONFIG("Plugin Configurations"),



  /**
   * The name or OID of the attribute type to remove from the server schema.
   */
  INFO_REMOVE_ATTR_TYPE_DESCRIPTION_ATTRIBUTE_TYPE("The name or OID of the attribute type to remove from the server schema."),



  /**
   * Attribute Type
   */
  INFO_REMOVE_ATTR_TYPE_DISPLAY_NAME_ATTRIBUTE_TYPE("Attribute Type"),



  /**
   * Safely removes an attribute type from the server schema after ensuring that it is not in use.
   */
  INFO_REMOVE_ATTR_TYPE_TASK_DESCRIPTION("Safely removes an attribute type from the server schema after ensuring that it is not in use."),



  /**
   * Remove Attribute Type
   */
  INFO_REMOVE_ATTR_TYPE_TASK_NAME("Remove Attribute Type"),



  /**
   * The path to a log file to be rotated.  It may be absolute, or it may be relative to the server root.  Multiple paths may be specified if rotation should be performed for multiple log files.  If no paths are given, the server will rotate all applicable log files.
   */
  INFO_ROTATE_LOG_DESCRIPTION_PATH("The path to a log file to be rotated.  It may be absolute, or it may be relative to the server root.  Multiple paths may be specified if rotation should be performed for multiple log files.  If no paths are given, the server will rotate all applicable log files."),



  /**
   * Path
   */
  INFO_ROTATE_LOG_DISPLAY_NAME_PATH("Path"),



  /**
   * The DN of the user as whom the search should be processed.  If this is not specified, then the search will be processed as an internal root user.
   */
  INFO_SEARCH_TASK_DESCRIPTION_AUTHZ_DN("The DN of the user as whom the search should be processed.  If this is not specified, then the search will be processed as an internal root user."),



  /**
   * The base DN to use for the search.
   */
  INFO_SEARCH_TASK_DESCRIPTION_BASE_DN("The base DN to use for the search."),



  /**
   * The filter to use for the search.
   */
  INFO_SEARCH_TASK_DESCRIPTION_FILTER("The filter to use for the search."),



  /**
   * The path (on the server filesystem) to the LDIF file to be written with entries matching the search criteria.
   */
  INFO_SEARCH_TASK_DESCRIPTION_NAME_OUTPUT_FILE("The path (on the server filesystem) to the LDIF file to be written with entries matching the search criteria."),



  /**
   * The name of an attribute to include in matching entries, or a grouping token like ''*'' (for all user attributes), ''+'' (for all operational attributes), or ''@inetOrgPerson'' (for all attributes in the inetOrgPerson object class).  If this is not provided then all user attributes will be selected.
   */
  INFO_SEARCH_TASK_DESCRIPTION_RETURN_ATTR("The name of an attribute to include in matching entries, or a grouping token like ''*'' (for all user attributes), ''+'' (for all operational attributes), or ''@inetOrgPerson'' (for all attributes in the inetOrgPerson object class).  If this is not provided then all user attributes will be selected."),



  /**
   * The scope to use for the search.  The value must be one of ''base'', ''one'', ''sub'', or ''subordinate''.
   */
  INFO_SEARCH_TASK_DESCRIPTION_SCOPE("The scope to use for the search.  The value must be one of ''base'', ''one'', ''sub'', or ''subordinate''."),



  /**
   * Authorization DN
   */
  INFO_SEARCH_TASK_DISPLAY_NAME_AUTHZ_DN("Authorization DN"),



  /**
   * Search Base DN
   */
  INFO_SEARCH_TASK_DISPLAY_NAME_BASE_DN("Search Base DN"),



  /**
   * Search Filter
   */
  INFO_SEARCH_TASK_DISPLAY_NAME_FILTER("Search Filter"),



  /**
   * Output File
   */
  INFO_SEARCH_TASK_DISPLAY_NAME_OUTPUT_FILE("Output File"),



  /**
   * Attribute to Return
   */
  INFO_SEARCH_TASK_DISPLAY_NAME_RETURN_ATTR("Attribute to Return"),



  /**
   * Search Scope
   */
  INFO_SEARCH_TASK_DISPLAY_NAME_SCOPE("Search Scope"),



  /**
   * May be used to add the contents of one or more files to the server schema.
   */
  INFO_TASK_DESCRIPTION_ADD_SCHEMA_FILE("May be used to add the contents of one or more files to the server schema."),



  /**
   * Cause the Directory Server to generate administrative alerts and/or manage the set of degraded and unavailable alert types reported in the general monitor entry.
   */
  INFO_TASK_DESCRIPTION_ALERT("Cause the Directory Server to generate administrative alerts and/or manage the set of degraded and unavailable alert types reported in the general monitor entry."),



  /**
   * This task may be used to initiate a data security audit in the Directory Server to look for potential issues in the data which may pose a security risk to the directory environment.
   */
  INFO_TASK_DESCRIPTION_AUDIT_DATA_SECURITY("This task may be used to initiate a data security audit in the Directory Server to look for potential issues in the data which may pose a security risk to the directory environment."),



  /**
   * May be used to back up the contents of one or more Directory Server backends.
   */
  INFO_TASK_DESCRIPTION_BACKUP("May be used to back up the contents of one or more Directory Server backends."),



  /**
   * Sleeps for a specified period of time or waits for a given condition to be satisfied.
   */
  INFO_TASK_DESCRIPTION_DELAY("Sleeps for a specified period of time or waits for a given condition to be satisfied."),



  /**
   * May be used to terminate a client connection.
   */
  INFO_TASK_DESCRIPTION_DISCONNECT_CLIENT("May be used to terminate a client connection."),



  /**
   * Dump statistics about the contents of a Directory Server backend which stores its content in a Berkeley DB Java Edition database environment.  It reports information about the total and average size of keys and values in each database, along with the relative sizes of the databases.
   */
  INFO_TASK_DESCRIPTION_DUMP_DB("Dump statistics about the contents of a Directory Server backend which stores its content in a Berkeley DB Java Edition database environment.  It reports information about the total and average size of keys and values in each database, along with the relative sizes of the databases."),



  /**
   * May be used to cause the server to enter lockdown mode.  While in lockdown mode, the server will reject any connection attempts from remote clients, and will reject any request from non-root users.
   */
  INFO_TASK_DESCRIPTION_ENTER_LOCKDOWN_MODE("May be used to cause the server to enter lockdown mode.  While in lockdown mode, the server will reject any connection attempts from remote clients, and will reject any request from non-root users."),



  /**
   * Executes a specified command on the server system with a given set of arguments.  The command to execute must be given as an absolute path, and must be listed in a file containing whitelisted commands in the server''s config directory.  The requester must also have the exec-task privilege, and the exec task must be included in the set of allowed tasks defined in the global configuration.
   */
  INFO_TASK_DESCRIPTION_EXEC("Executes a specified command on the server system with a given set of arguments.  The command to execute must be given as an absolute path, and must be listed in a file containing whitelisted commands in the server''s config directory.  The requester must also have the exec-task privilege, and the exec task must be included in the set of allowed tasks defined in the global configuration."),



  /**
   * May be used to export the contents of a Directory Server backend to LDIF.
   */
  INFO_TASK_DESCRIPTION_EXPORT("May be used to export the contents of a Directory Server backend to LDIF."),



  /**
   * Examines files in a specified directory that match a given pattern, retaining the most recent files in accordance with a provided set of count, age, or size criteria, and removing any older files that are outside of that criteria.
   */
  INFO_TASK_DESCRIPTION_FILE_RETENTION("Examines files in a specified directory that match a given pattern, retaining the most recent files in accordance with a provided set of count, age, or size criteria, and removing any older files that are outside of that criteria."),



  /**
   * Generates a server profile that may be used to create a new instance of the server with the same configuration and setup as the instance from which the profile is obtained.  The resulting profile may be written as a directory structure or packaged into a zip file.
   */
  INFO_TASK_DESCRIPTION_GENERATE_SERVER_PROFILE("Generates a server profile that may be used to create a new instance of the server with the same configuration and setup as the instance from which the profile is obtained.  The resulting profile may be written as a directory structure or packaged into a zip file."),



  /**
   * A generic task which does not require any custom properties.
   */
  INFO_TASK_DESCRIPTION_GENERIC("A generic task which does not require any custom properties."),



  /**
   * Invoke a task encapsulated in a Groovy script using the UnboundID Server SDK.
   */
  INFO_TASK_DESCRIPTION_GROOVY_SCRIPTED_TASK("Invoke a task encapsulated in a Groovy script using the UnboundID Server SDK."),



  /**
   * May be used to import LDIF data into a Directory Server backend.
   */
  INFO_TASK_DESCRIPTION_IMPORT("May be used to import LDIF data into a Directory Server backend."),



  /**
   * May be used to cause the server to leave lockdown mode and resume normal operation.
   */
  INFO_TASK_DESCRIPTION_LEAVE_LOCKDOWN_MODE("May be used to cause the server to leave lockdown mode and resume normal operation."),



  /**
   * Generates composed values for existing entries in the Directory Server.
   */
  INFO_TASK_DESCRIPTION_POPULATE_COMPOSED_ATTR_VALUES("Generates composed values for existing entries in the Directory Server."),



  /**
   * May be used to generate or rebuild Directory Server indexes.
   */
  INFO_TASK_DESCRIPTION_REBUILD("May be used to generate or rebuild Directory Server indexes."),



  /**
   * This task may be used to cause the server to re-encode all or a specified set of entries in a local DB backend.  This may be used to apply encoding changes around the use of features like compression, encryption, cryptographic digests, and/or uncached attributes or entries.
   */
  INFO_TASK_DESCRIPTION_REENCODE_ENTRIES("This task may be used to cause the server to re-encode all or a specified set of entries in a local DB backend.  This may be used to apply encoding changes around the use of features like compression, encryption, cryptographic digests, and/or uncached attributes or entries."),



  /**
   * Cause the Directory Server to immediately reload the encryption settings database from disk in order to make any changes available to the server
   */
  INFO_TASK_DESCRIPTION_REFRESH_ENCRYPTION_SETTINGS("Cause the Directory Server to immediately reload the encryption settings database from disk in order to make any changes available to the server"),



  /**
   * Reload the data for one or more entry-balancing global indexes from backend Directory Servers or a peer Directory Proxy Server.
   */
  INFO_TASK_DESCRIPTION_RELOAD_GLOBAL_INDEX("Reload the data for one or more entry-balancing global indexes from backend Directory Servers or a peer Directory Proxy Server."),



  /**
   * Dynamically reload the certificate key and trust stores for all HTTP connection handler instances configured with support for HTTPS.
   */
  INFO_TASK_DESCRIPTION_RELOAD_HTTP_CONNECTION_HANDLER_CERTIFICATES("Dynamically reload the certificate key and trust stores for all HTTP connection handler instances configured with support for HTTPS."),



  /**
   * May be used to restore the contents of a Directory Server backend from a backup.
   */
  INFO_TASK_DESCRIPTION_RESTORE("May be used to restore the contents of a Directory Server backend from a backup."),



  /**
   * Triggers the rotation for one or more specified log files, or for all applicable log files if no paths are given.
   */
  INFO_TASK_DESCRIPTION_ROTATE_LOG("Triggers the rotation for one or more specified log files, or for all applicable log files if no paths are given."),



  /**
   * May be used to process an internal search within the Directory Server.
   */
  INFO_TASK_DESCRIPTION_SEARCH("May be used to process an internal search within the Directory Server."),



  /**
   * May be used to shut down or restart the Directory Server.
   */
  INFO_TASK_DESCRIPTION_SHUTDOWN("May be used to shut down or restart the Directory Server."),



  /**
   * Synchronize the encryption settings definitions across all of the servers in the topology.
   */
  INFO_TASK_DESCRIPTION_SYNCHRONIZE_ENCRYPTION_SETTINGS("Synchronize the encryption settings definitions across all of the servers in the topology."),



  /**
   * Invoke a third-party task developed using the UnboundID Server SDK.
   */
  INFO_TASK_DESCRIPTION_THIRD_PARTY_TASK("Invoke a third-party task developed using the UnboundID Server SDK."),



  /**
   * Add Schema File
   */
  INFO_TASK_NAME_ADD_SCHEMA_FILE("Add Schema File"),



  /**
   * Administrative Alert
   */
  INFO_TASK_NAME_ALERT("Administrative Alert"),



  /**
   * Audit Data Security
   */
  INFO_TASK_NAME_AUDIT_DATA_SECURITY("Audit Data Security"),



  /**
   * Backup
   */
  INFO_TASK_NAME_BACKUP("Backup"),



  /**
   * Delay
   */
  INFO_TASK_NAME_DELAY("Delay"),



  /**
   * Disconnect Client
   */
  INFO_TASK_NAME_DISCONNECT_CLIENT("Disconnect Client"),



  /**
   * Dump DB Details
   */
  INFO_TASK_NAME_DUMP_DB("Dump DB Details"),



  /**
   * Enter Lockdown Mode
   */
  INFO_TASK_NAME_ENTER_LOCKDOWN_MODE("Enter Lockdown Mode"),



  /**
   * Execute Command
   */
  INFO_TASK_NAME_EXEC("Execute Command"),



  /**
   * LDIF Export
   */
  INFO_TASK_NAME_EXPORT("LDIF Export"),



  /**
   * File Retention
   */
  INFO_TASK_NAME_FILE_RETENTION("File Retention"),



  /**
   * Generate Server Profile
   */
  INFO_TASK_NAME_GENERATE_SERVER_PROFILE("Generate Server Profile"),



  /**
   * Generic
   */
  INFO_TASK_NAME_GENERIC("Generic"),



  /**
   * Groovy-Scripted Task
   */
  INFO_TASK_NAME_GROOVY_SCRIPTED_TASK("Groovy-Scripted Task"),



  /**
   * LDIF Import
   */
  INFO_TASK_NAME_IMPORT("LDIF Import"),



  /**
   * Leave Lockdown Mode
   */
  INFO_TASK_NAME_LEAVE_LOCKDOWN_MODE("Leave Lockdown Mode"),



  /**
   * Populate Composed Attribute Values
   */
  INFO_TASK_NAME_POPULATE_COMPOSED_ATTR_VALUES("Populate Composed Attribute Values"),



  /**
   * Rebuild Index
   */
  INFO_TASK_NAME_REBUILD("Rebuild Index"),



  /**
   * Re-Encode Entries Task
   */
  INFO_TASK_NAME_REENCODE_ENTRIES("Re-Encode Entries Task"),



  /**
   * Refresh Encryption Settings Task
   */
  INFO_TASK_NAME_REFRESH_ENCRYPTION_SETTINGS("Refresh Encryption Settings Task"),



  /**
   * Reload Global Index
   */
  INFO_TASK_NAME_RELOAD_GLOBAL_INDEX("Reload Global Index"),



  /**
   * Reload HTTP Connection Handler Certificates
   */
  INFO_TASK_NAME_RELOAD_HTTP_CONNECTION_HANDLER_CERTIFICATES("Reload HTTP Connection Handler Certificates"),



  /**
   * Restore
   */
  INFO_TASK_NAME_RESTORE("Restore"),



  /**
   * Rotate Log
   */
  INFO_TASK_NAME_ROTATE_LOG("Rotate Log"),



  /**
   * Search
   */
  INFO_TASK_NAME_SEARCH("Search"),



  /**
   * Shutdown
   */
  INFO_TASK_NAME_SHUTDOWN("Shutdown"),



  /**
   * Synchronize Encryption Settings
   */
  INFO_TASK_NAME_SYNCHRONIZE_ENCRYPTION_SETTINGS("Synchronize Encryption Settings"),



  /**
   * Third-Party Task
   */
  INFO_TASK_NAME_THIRD_PARTY_TASK("Third-Party Task");



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
      rb = ResourceBundle.getBundle("unboundid-ldapsdk-task");
    } catch (final Exception e) {}
    RESOURCE_BUNDLE = rb;
  }



  /**
   * The map that will be used to hold the unformatted message strings, indexed by property name.
   */
  private static final ConcurrentHashMap<TaskMessages,String> MESSAGE_STRINGS = new ConcurrentHashMap<>(100);



  /**
   * The map that will be used to hold the message format objects, indexed by property name.
   */
  private static final ConcurrentHashMap<TaskMessages,MessageFormat> MESSAGES = new ConcurrentHashMap<>(100);



  // The default text for this message
  private final String defaultText;



  /**
   * Creates a new message key.
   */
  private TaskMessages(final String defaultText)
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

