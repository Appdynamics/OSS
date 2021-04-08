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
package com.unboundid.ldap.sdk.unboundidds.monitors;



import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;



/**
 * This enum defines a set of message keys for messages in the
 * com.unboundid.ldap.sdk.unboundidds.monitors package, which correspond to messages in the
 * unboundid-ldapsdk-monitor.properties properties file.
 * <BR><BR>
 * This source file was generated from the properties file.
 * Do not edit it directly.
 */
enum MonitorMessages
{
  /**
   * The number of operations currently being processed within the server.
   */
  INFO_ACTIVE_OPERATIONS_DESC_NUM_OPS_IN_PROGRESS("The number of operations currently being processed within the server."),



  /**
   * The number of persistent searches currently being processed within the server.
   */
  INFO_ACTIVE_OPERATIONS_DESC_NUM_PSEARCHES_IN_PROGRESS("The number of persistent searches currently being processed within the server."),



  /**
   * Information about an operation currently being processed within the server.
   */
  INFO_ACTIVE_OPERATIONS_DESC_OPS_IN_PROGRESS("Information about an operation currently being processed within the server."),



  /**
   * Information about a persistent search currently being processed within the server.
   */
  INFO_ACTIVE_OPERATIONS_DESC_PSEARCHES_IN_PROGRESS("Information about a persistent search currently being processed within the server."),



  /**
   * Number of Active Operations
   */
  INFO_ACTIVE_OPERATIONS_DISPNAME_NUM_OPS_IN_PROGRESS("Number of Active Operations"),



  /**
   * Number of Active Persistent Searches
   */
  INFO_ACTIVE_OPERATIONS_DISPNAME_NUM_PSEARCHES_IN_PROGRESS("Number of Active Persistent Searches"),



  /**
   * Active Operation
   */
  INFO_ACTIVE_OPERATIONS_DISPNAME_OPS_IN_PROGRESS("Active Operation"),



  /**
   * Persistent Search
   */
  INFO_ACTIVE_OPERATIONS_DISPNAME_PSEARCHES_IN_PROGRESS("Persistent Search"),



  /**
   * Provides information about the operations currently being processed within the Directory Server.
   */
  INFO_ACTIVE_OPERATIONS_MONITOR_DESC("Provides information about the operations currently being processed within the Directory Server."),



  /**
   * Active Operations
   */
  INFO_ACTIVE_OPERATIONS_MONITOR_DISPNAME("Active Operations"),



  /**
   * Uniquely identifies the backend in the Directory Server.
   */
  INFO_BACKEND_DESC_BACKEND_ID("Uniquely identifies the backend in the Directory Server."),



  /**
   * The DN of the entry at the highest point of the subtree contained by the backend.  There may be multiple base DNs in a single backend, which represent the top-level entries for different subtrees within that backend.
   */
  INFO_BACKEND_DESC_BASE_DN("The DN of the entry at the highest point of the subtree contained by the backend.  There may be multiple base DNs in a single backend, which represent the top-level entries for different subtrees within that backend."),



  /**
   * The total number of entries contained in the backend in the {0} subtree (including the base entry).
   */
  INFO_BACKEND_DESC_ENTRY_COUNT("The total number of entries contained in the backend in the {0} subtree (including the base entry)."),



  /**
   * Indicates whether the backend is a private backend, which is meant to hold operational and/or administrative data maintained by the server rather than user data.
   */
  INFO_BACKEND_DESC_IS_PRIVATE("Indicates whether the backend is a private backend, which is meant to hold operational and/or administrative data maintained by the server rather than user data."),



  /**
   * The number of soft delete operations that have been processed in the backend since it was started.
   */
  INFO_BACKEND_DESC_SOFT_DELETE_COUNT("The number of soft delete operations that have been processed in the backend since it was started."),



  /**
   * The total number of entries contained in the backend.
   */
  INFO_BACKEND_DESC_TOTAL_ENTRIES("The total number of entries contained in the backend."),



  /**
   * The number of undelete operations that have been processed in the backend since it was started.
   */
  INFO_BACKEND_DESC_UNDELETE_COUNT("The number of undelete operations that have been processed in the backend since it was started."),



  /**
   * Indicates whether the backend accepts write operations.  The writability mode may be ''enabled'' (to indicate that the backend will attempt to process all write operations), ''disabled'' (to indicate that the backend will reject all write operations), or ''internal-only'' (to indicate that the backend will reject write operations from external clients but will attempt to process writes from internal operations and/or synchronization).
   */
  INFO_BACKEND_DESC_WRITABILITY_MODE("Indicates whether the backend accepts write operations.  The writability mode may be ''enabled'' (to indicate that the backend will attempt to process all write operations), ''disabled'' (to indicate that the backend will reject all write operations), or ''internal-only'' (to indicate that the backend will reject write operations from external clients but will attempt to process writes from internal operations and/or synchronization)."),



  /**
   * Backend ID
   */
  INFO_BACKEND_DISPNAME_BACKEND_ID("Backend ID"),



  /**
   * Base DN
   */
  INFO_BACKEND_DISPNAME_BASE_DN("Base DN"),



  /**
   * Entry Count for Base DN {0}
   */
  INFO_BACKEND_DISPNAME_ENTRY_COUNT("Entry Count for Base DN {0}"),



  /**
   * Private Backend
   */
  INFO_BACKEND_DISPNAME_IS_PRIVATE("Private Backend"),



  /**
   * Soft Delete Count
   */
  INFO_BACKEND_DISPNAME_SOFT_DELETE_COUNT("Soft Delete Count"),



  /**
   * Total Entry Count
   */
  INFO_BACKEND_DISPNAME_TOTAL_ENTRIES("Total Entry Count"),



  /**
   * Undelete Count
   */
  INFO_BACKEND_DISPNAME_UNDELETE_COUNT("Undelete Count"),



  /**
   * Writability Mode
   */
  INFO_BACKEND_DISPNAME_WRITABILITY_MODE("Writability Mode"),



  /**
   * Provides general information about the state of a Directory Server backend, including the backend ID, set of base DNs, and number of entries.
   */
  INFO_BACKEND_MONITOR_DESC("Provides general information about the state of a Directory Server backend, including the backend ID, set of base DNs, and number of entries."),



  /**
   * Backend
   */
  INFO_BACKEND_MONITOR_DISPNAME("Backend"),



  /**
   * Provides information about a client connection which is established to the Directory Server.  The value will be comprised of multiple elements which are separated by spaces.  Each element will contain a name followed by an equal sign and a quoted value.
   */
  INFO_CLIENT_CONNECTION_DESC_CONNECTION("Provides information about a client connection which is established to the Directory Server.  The value will be comprised of multiple elements which are separated by spaces.  Each element will contain a name followed by an equal sign and a quoted value."),



  /**
   * Client Connection
   */
  INFO_CLIENT_CONNECTION_DISPNAME_CONNECTION("Client Connection"),



  /**
   * Provides information about all client connections which are established to the Directory Server.
   */
  INFO_CLIENT_CONNECTION_MONITOR_DESC("Provides information about all client connections which are established to the Directory Server."),



  /**
   * Client Connections
   */
  INFO_CLIENT_CONNECTION_MONITOR_DISPNAME("Client Connections"),



  /**
   * Provides information about a client connection which is established to the connection handler.  The value will be comprised of multiple elements which are separated by spaces.  Each element will contain a name followed by an equal sign and a quoted value.
   */
  INFO_CONNECTION_HANDLER_DESC_CONNECTION("Provides information about a client connection which is established to the connection handler.  The value will be comprised of multiple elements which are separated by spaces.  Each element will contain a name followed by an equal sign and a quoted value."),



  /**
   * The address and port on which the connection handler is listening for new connections from clients.
   */
  INFO_CONNECTION_HANDLER_DESC_LISTENER("The address and port on which the connection handler is listening for new connections from clients."),



  /**
   * The number of client connections that are currently established to the connection handler.
   */
  INFO_CONNECTION_HANDLER_DESC_NUM_CONNECTIONS("The number of client connections that are currently established to the connection handler."),



  /**
   * The name of the protocol that the connection handler uses to communicate with clients.
   */
  INFO_CONNECTION_HANDLER_DESC_PROTOCOL("The name of the protocol that the connection handler uses to communicate with clients."),



  /**
   * Client Connection
   */
  INFO_CONNECTION_HANDLER_DISPNAME_CONNECTION("Client Connection"),



  /**
   * Listener
   */
  INFO_CONNECTION_HANDLER_DISPNAME_LISTENER("Listener"),



  /**
   * Number of Connections Established
   */
  INFO_CONNECTION_HANDLER_DISPNAME_NUM_CONNECTIONS("Number of Connections Established"),



  /**
   * Protocol
   */
  INFO_CONNECTION_HANDLER_DISPNAME_PROTOCOL("Protocol"),



  /**
   * Provides information about a Directory Server connection handler, which is used to accept client connections, and to read requests and send responses to those clients.
   */
  INFO_CONNECTION_HANDLER_MONITOR_DESC("Provides information about a Directory Server connection handler, which is used to accept client connections, and to read requests and send responses to those clients."),



  /**
   * Connection Handler
   */
  INFO_CONNECTION_HANDLER_MONITOR_DISPNAME("Connection Handler"),



  /**
   * The recent amount of free system memory, in gigabytes.
   */
  INFO_CPU_MEM_DESC_FREE_MEM_GB("The recent amount of free system memory, in gigabytes."),



  /**
   * The recent percentage of total system memory considered free.
   */
  INFO_CPU_MEM_DESC_FREE_MEM_PCT("The recent percentage of total system memory considered free."),



  /**
   * The recent percentage of the time the CPU spent idle.
   */
  INFO_CPU_MEM_DESC_RECENT_CPU_IDLE("The recent percentage of the time the CPU spent idle."),



  /**
   * The recent percentage of the time the CPU spent in the I/O wait state.
   */
  INFO_CPU_MEM_DESC_RECENT_CPU_IOWAIT("The recent percentage of the time the CPU spent in the I/O wait state."),



  /**
   * The recent percentage of the time the CPU spent in the system state.
   */
  INFO_CPU_MEM_DESC_RECENT_CPU_SYSTEM("The recent percentage of the time the CPU spent in the system state."),



  /**
   * The recent CPU total busy percentage, as a combination of time spent in user, system, and I/O wait states.
   */
  INFO_CPU_MEM_DESC_RECENT_CPU_TOTAL_BUSY("The recent CPU total busy percentage, as a combination of time spent in user, system, and I/O wait states."),



  /**
   * The recent percentage of the time the CPU spent in the user state.
   */
  INFO_CPU_MEM_DESC_RECENT_CPU_USER("The recent percentage of the time the CPU spent in the user state."),



  /**
   * A timestamp indicating when the CPU and memory usage information was last updated.
   */
  INFO_CPU_MEM_DESC_TIMESTAMP("A timestamp indicating when the CPU and memory usage information was last updated."),



  /**
   * The total amount of system memory, in gigabytes.
   */
  INFO_CPU_MEM_DESC_TOTAL_MEM("The total amount of system memory, in gigabytes."),



  /**
   * Recent Free System Memory (Gigabytes)
   */
  INFO_CPU_MEM_DISPNAME_FREE_MEM_GB("Recent Free System Memory (Gigabytes)"),



  /**
   * Recent Free System Memory (Percent)
   */
  INFO_CPU_MEM_DISPNAME_FREE_MEM_PCT("Recent Free System Memory (Percent)"),



  /**
   * Recent CPU Idle Percent
   */
  INFO_CPU_MEM_DISPNAME_RECENT_CPU_IDLE("Recent CPU Idle Percent"),



  /**
   * Recent CPU I/O Wait Percent
   */
  INFO_CPU_MEM_DISPNAME_RECENT_CPU_IOWAIT("Recent CPU I/O Wait Percent"),



  /**
   * Recent CPU System Percent
   */
  INFO_CPU_MEM_DISPNAME_RECENT_CPU_SYSTEM("Recent CPU System Percent"),



  /**
   * Recent CPU Total Busy Percent
   */
  INFO_CPU_MEM_DISPNAME_RECENT_CPU_TOTAL_BUSY("Recent CPU Total Busy Percent"),



  /**
   * Recent CPU User Percent
   */
  INFO_CPU_MEM_DISPNAME_RECENT_CPU_USER("Recent CPU User Percent"),



  /**
   * Timestamp
   */
  INFO_CPU_MEM_DISPNAME_TIMESTAMP("Timestamp"),



  /**
   * Total System Memory (Gigabytes)
   */
  INFO_CPU_MEM_DISPNAME_TOTAL_MEM("Total System Memory (Gigabytes)"),



  /**
   * Provides information about recent system CPU and memory utilization.
   */
  INFO_CPU_MEM_MONITOR_DESC("Provides information about recent system CPU and memory utilization."),



  /**
   * Recent CPU and Memory Usage
   */
  INFO_CPU_MEM_MONITOR_DISPNAME("Recent CPU and Memory Usage"),



  /**
   * The current state of usable disk space available to the Directory Server.  It may be one of ''normal'', ''low space warning'', ''low space error'', or ''out of space error''.
   */
  INFO_DISK_SPACE_USAGE_DESC_CURRENT_STATE("The current state of usable disk space available to the Directory Server.  It may be one of ''normal'', ''low space warning'', ''low space error'', or ''out of space error''."),



  /**
   * The name of the disk space consumer.
   */
  INFO_DISK_SPACE_USAGE_DESC_NAME("The name of the disk space consumer."),



  /**
   * A path representing a filesystem location in which the Directory Server may consume disk space.
   */
  INFO_DISK_SPACE_USAGE_DESC_PATH("A path representing a filesystem location in which the Directory Server may consume disk space."),



  /**
   * The total amount of space in bytes on the volume associated with the disk space consumer path.
   */
  INFO_DISK_SPACE_USAGE_DESC_TOTAL_BYTES("The total amount of space in bytes on the volume associated with the disk space consumer path."),



  /**
   * The amount of free usable space in bytes on the volume associated with the disk space consumer path.
   */
  INFO_DISK_SPACE_USAGE_DESC_USABLE_BYTES("The amount of free usable space in bytes on the volume associated with the disk space consumer path."),



  /**
   * The percentage of the total space on the volume associated with the disk space consumer path which is free and available for use by the disk space consumer.
   */
  INFO_DISK_SPACE_USAGE_DESC_USABLE_PERCENT("The percentage of the total space on the volume associated with the disk space consumer path which is free and available for use by the disk space consumer."),



  /**
   * Current Disk Space State
   */
  INFO_DISK_SPACE_USAGE_DISPNAME_CURRENT_STATE("Current Disk Space State"),



  /**
   * Disk Space Consumer
   */
  INFO_DISK_SPACE_USAGE_DISPNAME_DISK_SPACE_CONSUMER_PREFIX("Disk Space Consumer"),



  /**
   * Name
   */
  INFO_DISK_SPACE_USAGE_DISPNAME_NAME_SUFFIX("Name"),



  /**
   * Path
   */
  INFO_DISK_SPACE_USAGE_DISPNAME_PATH_SUFFIX("Path"),



  /**
   * Total Bytes
   */
  INFO_DISK_SPACE_USAGE_DISPNAME_TOTAL_BYTES_SUFFIX("Total Bytes"),



  /**
   * Usable Bytes
   */
  INFO_DISK_SPACE_USAGE_DISPNAME_USABLE_BYTES_SUFFIX("Usable Bytes"),



  /**
   * Usable Percent
   */
  INFO_DISK_SPACE_USAGE_DISPNAME_USABLE_PERCENT_SUFFIX("Usable Percent"),



  /**
   * Provides information about the disk space available to various components of the Directory Server.
   */
  INFO_DISK_SPACE_USAGE_MONITOR_DESC("Provides information about the disk space available to various components of the Directory Server."),



  /**
   * Disk Space Usage
   */
  INFO_DISK_SPACE_USAGE_MONITOR_DISPNAME("Disk Space Usage"),



  /**
   * The number of entries currently held in the entry cache.
   */
  INFO_ENTRY_CACHE_DESC_CURRENT_COUNT("The number of entries currently held in the entry cache."),



  /**
   * The amount of memory in bytes currently consumed by the entry cache.
   */
  INFO_ENTRY_CACHE_DESC_CURRENT_SIZE("The amount of memory in bytes currently consumed by the entry cache."),



  /**
   * The number of times the server was able to find the requested entry in the entry cache.
   */
  INFO_ENTRY_CACHE_DESC_HITS("The number of times the server was able to find the requested entry in the entry cache."),



  /**
   * The portion of the time the server was able to find the requested entry in the entry cache (the number of cache hits divided by the number of cache tries).
   */
  INFO_ENTRY_CACHE_DESC_HIT_RATIO("The portion of the time the server was able to find the requested entry in the entry cache (the number of cache hits divided by the number of cache tries)."),



  /**
   * The maximum number of entries that may be held in the entry cache at any time.
   */
  INFO_ENTRY_CACHE_DESC_MAX_COUNT("The maximum number of entries that may be held in the entry cache at any time."),



  /**
   * The maximum amount of memory in bytes that the entry cache may consume.
   */
  INFO_ENTRY_CACHE_DESC_MAX_SIZE("The maximum amount of memory in bytes that the entry cache may consume."),



  /**
   * The number of times the server was not able to find the requested entry in the entry cache.
   */
  INFO_ENTRY_CACHE_DESC_MISSES("The number of times the server was not able to find the requested entry in the entry cache."),



  /**
   * The number of times the server has attempted to retrieve an entry from the entry cache.
   */
  INFO_ENTRY_CACHE_DESC_TRIES("The number of times the server has attempted to retrieve an entry from the entry cache."),



  /**
   * Current Entry Count
   */
  INFO_ENTRY_CACHE_DISPNAME_CURRENT_COUNT("Current Entry Count"),



  /**
   * Current Entry Cache Size
   */
  INFO_ENTRY_CACHE_DISPNAME_CURRENT_SIZE("Current Entry Cache Size"),



  /**
   * Cache Hits
   */
  INFO_ENTRY_CACHE_DISPNAME_HITS("Cache Hits"),



  /**
   * Cache Hit Ratio
   */
  INFO_ENTRY_CACHE_DISPNAME_HIT_RATIO("Cache Hit Ratio"),



  /**
   * Maximum Entry Count
   */
  INFO_ENTRY_CACHE_DISPNAME_MAX_COUNT("Maximum Entry Count"),



  /**
   * Maximum Entry Cache Size
   */
  INFO_ENTRY_CACHE_DISPNAME_MAX_SIZE("Maximum Entry Cache Size"),



  /**
   * Cache Misses
   */
  INFO_ENTRY_CACHE_DISPNAME_MISSES("Cache Misses"),



  /**
   * Cache Tries
   */
  INFO_ENTRY_CACHE_DISPNAME_TRIES("Cache Tries"),



  /**
   * Provides information about the Directory Server entry cache, which may be used to hold entries in memory so that they can be accessed more quickly than retrieving them from the database.
   */
  INFO_ENTRY_CACHE_MONITOR_DESC("Provides information about the Directory Server entry cache, which may be used to hold entries in memory so that they can be accessed more quickly than retrieving them from the database."),



  /**
   * Entry Cache
   */
  INFO_ENTRY_CACHE_MONITOR_DISPNAME("Entry Cache"),



  /**
   * The name of the associated FIFO entry cache.
   */
  INFO_FIFO_ENTRY_CACHE_DESC_CACHE_NAME("The name of the associated FIFO entry cache."),



  /**
   * A human-readable message about the capacity and current utilization of the entry cache.
   */
  INFO_FIFO_ENTRY_CACHE_DESC_CAPACITY_DETAILS("A human-readable message about the capacity and current utilization of the entry cache."),



  /**
   * The number of entries currently held in the cache.
   */
  INFO_FIFO_ENTRY_CACHE_DESC_CURRENT_COUNT("The number of entries currently held in the cache."),



  /**
   * The percent of the maximum number of entries that are currently held in the cache.
   */
  INFO_FIFO_ENTRY_CACHE_DESC_ENTRY_COUNT_PERCENT("The percent of the maximum number of entries that are currently held in the cache."),



  /**
   * The total number of times an entry was evicted because the cache already had the maximum allowed number of entries.
   */
  INFO_FIFO_ENTRY_CACHE_DESC_EVICT_COUNT("The total number of times an entry was evicted because the cache already had the maximum allowed number of entries."),



  /**
   * The total number of times an entry was evicted because the JVM was consuming too much memory.
   */
  INFO_FIFO_ENTRY_CACHE_DESC_EVICT_MEM("The total number of times an entry was evicted because the JVM was consuming too much memory."),



  /**
   * The total number of successful attempts to retrieve an entry from the cache.
   */
  INFO_FIFO_ENTRY_CACHE_DESC_HITS("The total number of successful attempts to retrieve an entry from the cache."),



  /**
   * The percentage of the time that an attempt to retrieve an entry from the cache was successful.
   */
  INFO_FIFO_ENTRY_CACHE_DESC_HIT_RATIO("The percentage of the time that an attempt to retrieve an entry from the cache was successful."),



  /**
   * Indicates whether the entry cache is currently full, either due to JVM memory consumption or the number of entries held in the cache.
   */
  INFO_FIFO_ENTRY_CACHE_DESC_IS_FULL("Indicates whether the entry cache is currently full, either due to JVM memory consumption or the number of entries held in the cache."),



  /**
   * The difference between the maximum allowed JVM memory percent and the current JVM memory percent used.  This may be negative if the current JVM memory percent is greater than the maximum allowed percent.
   */
  INFO_FIFO_ENTRY_CACHE_DESC_JVM_MEM_BELOW_MAX_PERCENT("The difference between the maximum allowed JVM memory percent and the current JVM memory percent used.  This may be negative if the current JVM memory percent is greater than the maximum allowed percent."),



  /**
   * The percent of JVM memory that is currently in use.
   */
  INFO_FIFO_ENTRY_CACHE_DESC_JVM_MEM_CURRENT_PERCENT("The percent of JVM memory that is currently in use."),



  /**
   * The maximum percent of JVM memory that may be used in order for new entries to be added to the cache.
   */
  INFO_FIFO_ENTRY_CACHE_DESC_JVM_MEM_MAX_PERCENT("The maximum percent of JVM memory that may be used in order for new entries to be added to the cache."),



  /**
   * The number of times that a mass eviction was performed because the JVM memory became critically low.
   */
  INFO_FIFO_ENTRY_CACHE_DESC_LOW_MEM_COUNT("The number of times that a mass eviction was performed because the JVM memory became critically low."),



  /**
   * The maximum number of entries that may be held in the cache.
   */
  INFO_FIFO_ENTRY_CACHE_DESC_MAX_COUNT("The maximum number of entries that may be held in the cache."),



  /**
   * The maximum amount of memory (in bytes) that may be consumed by the entry cache.
   */
  INFO_FIFO_ENTRY_CACHE_DESC_MAX_MEM("The maximum amount of memory (in bytes) that may be consumed by the entry cache."),



  /**
   * The total number of times an entry was not added to the cache because it was already in the cache.
   */
  INFO_FIFO_ENTRY_CACHE_DESC_NO_PUT_ALREADY_PRESENT("The total number of times an entry was not added to the cache because it was already in the cache."),



  /**
   * The number of times an entry was not added to the cache because it did not meet the filter constraints for inclusion.
   */
  INFO_FIFO_ENTRY_CACHE_DESC_NO_PUT_FILTER("The number of times an entry was not added to the cache because it did not meet the filter constraints for inclusion."),



  /**
   * The number of times an entry was not added to the cache because the JVM was consuming too much memory.
   */
  INFO_FIFO_ENTRY_CACHE_DESC_NO_PUT_MEM("The number of times an entry was not added to the cache because the JVM was consuming too much memory."),



  /**
   * The number of times an entry was not added to the cache because it was too small to be included in the cache.
   */
  INFO_FIFO_ENTRY_CACHE_DESC_NO_PUT_TOO_SMALL("The number of times an entry was not added to the cache because it was too small to be included in the cache."),



  /**
   * The total number of times an entry was added to or replaced in the entry cache.
   */
  INFO_FIFO_ENTRY_CACHE_DESC_PUT_COUNT("The total number of times an entry was added to or replaced in the entry cache."),



  /**
   * The total number of attempts to retrieve an entry from the cache.
   */
  INFO_FIFO_ENTRY_CACHE_DESC_TRIES("The total number of attempts to retrieve an entry from the cache."),



  /**
   * Cache Name
   */
  INFO_FIFO_ENTRY_CACHE_DISPNAME_CACHE_NAME("Cache Name"),



  /**
   * Capacity Details
   */
  INFO_FIFO_ENTRY_CACHE_DISPNAME_CAPACITY_DETAILS("Capacity Details"),



  /**
   * Current Entry Count
   */
  INFO_FIFO_ENTRY_CACHE_DISPNAME_CURRENT_COUNT("Current Entry Count"),



  /**
   * Percent of Maximum Entry Count
   */
  INFO_FIFO_ENTRY_CACHE_DISPNAME_ENTRY_COUNT_PERCENT("Percent of Maximum Entry Count"),



  /**
   * Entries Evicted Due to Entry Count
   */
  INFO_FIFO_ENTRY_CACHE_DISPNAME_EVICT_COUNT("Entries Evicted Due to Entry Count"),



  /**
   * Entries Evicted Due to Memory
   */
  INFO_FIFO_ENTRY_CACHE_DISPNAME_EVICT_MEM("Entries Evicted Due to Memory"),



  /**
   * Cache Hits
   */
  INFO_FIFO_ENTRY_CACHE_DISPNAME_HITS("Cache Hits"),



  /**
   * Cache Hit Ratio
   */
  INFO_FIFO_ENTRY_CACHE_DISPNAME_HIT_RATIO("Cache Hit Ratio"),



  /**
   * Cache Is Full
   */
  INFO_FIFO_ENTRY_CACHE_DISPNAME_IS_FULL("Cache Is Full"),



  /**
   * Remaining JVM Memory Percent
   */
  INFO_FIFO_ENTRY_CACHE_DISPNAME_JVM_MEM_BELOW_MAX_PERCENT("Remaining JVM Memory Percent"),



  /**
   * Current JVM Memory Percent
   */
  INFO_FIFO_ENTRY_CACHE_DISPNAME_JVM_MEM_CURRENT_PERCENT("Current JVM Memory Percent"),



  /**
   * Maximum JVM Memory Percent
   */
  INFO_FIFO_ENTRY_CACHE_DISPNAME_JVM_MEM_MAX_PERCENT("Maximum JVM Memory Percent"),



  /**
   * Critical Memory Evictions
   */
  INFO_FIFO_ENTRY_CACHE_DISPNAME_LOW_MEM_COUNT("Critical Memory Evictions"),



  /**
   * Max Entry Count
   */
  INFO_FIFO_ENTRY_CACHE_DISPNAME_MAX_COUNT("Max Entry Count"),



  /**
   * Maximum Cache Size
   */
  INFO_FIFO_ENTRY_CACHE_DISPNAME_MAX_MEM("Maximum Cache Size"),



  /**
   * Entries Not Added Already Present
   */
  INFO_FIFO_ENTRY_CACHE_DISPNAME_NO_PUT_ALREADY_PRESENT("Entries Not Added Already Present"),



  /**
   * Entries Not Added Due to Filter Constraints
   */
  INFO_FIFO_ENTRY_CACHE_DISPNAME_NO_PUT_FILTER("Entries Not Added Due to Filter Constraints"),



  /**
   * Entries Not Added Due to Memory Constraints
   */
  INFO_FIFO_ENTRY_CACHE_DISPNAME_NO_PUT_MEM("Entries Not Added Due to Memory Constraints"),



  /**
   * Entries Not Added Due to Entry Size
   */
  INFO_FIFO_ENTRY_CACHE_DISPNAME_NO_PUT_TOO_SMALL("Entries Not Added Due to Entry Size"),



  /**
   * Entries Added or Updated
   */
  INFO_FIFO_ENTRY_CACHE_DISPNAME_PUT_COUNT("Entries Added or Updated"),



  /**
   * Cache Tries
   */
  INFO_FIFO_ENTRY_CACHE_DISPNAME_TRIES("Cache Tries"),



  /**
   * Provides information about the state of a Directory Server FIFO entry cache, including its resource consumption and effectiveness.
   */
  INFO_FIFO_ENTRY_CACHE_MONITOR_DESC("Provides information about the state of a Directory Server FIFO entry cache, including its resource consumption and effectiveness."),



  /**
   * FIFO Entry Cache
   */
  INFO_FIFO_ENTRY_CACHE_MONITOR_DISPNAME("FIFO Entry Cache"),



  /**
   * The length of time in milliseconds the gauge has been at the current severity.
   */
  INFO_GAUGE_DESC_CURRENT_DURATION_MILLIS("The length of time in milliseconds the gauge has been at the current severity."),



  /**
   * The length of time the gauge has been at the current severity, as a human-readable string.
   */
  INFO_GAUGE_DESC_CURRENT_DURATION_STRING("The length of time the gauge has been at the current severity, as a human-readable string."),



  /**
   * The current severity for the associated gauge.
   */
  INFO_GAUGE_DESC_CURRENT_SEVERITY("The current severity for the associated gauge."),



  /**
   * The time the gauge entered the current severity.
   */
  INFO_GAUGE_DESC_CURRENT_START_TIME("The time the gauge entered the current severity."),



  /**
   * The error messages for the associated gauge.
   */
  INFO_GAUGE_DESC_ERROR_MESSAGE("The error messages for the associated gauge."),



  /**
   * The name for the associated gauge.
   */
  INFO_GAUGE_DESC_GAUGE_NAME("The name for the associated gauge."),



  /**
   * The time the gauge was initialized.
   */
  INFO_GAUGE_DESC_INIT_TIME("The time the gauge was initialized."),



  /**
   * The duration of the last critical state for the gauge, in milliseconds.
   */
  INFO_GAUGE_DESC_LAST_CRITICAL_DURATION_MILLIS("The duration of the last critical state for the gauge, in milliseconds."),



  /**
   * The duration of the last critical state for the gauge, as a human-readable string.
   */
  INFO_GAUGE_DESC_LAST_CRITICAL_DURATION_STRING("The duration of the last critical state for the gauge, as a human-readable string."),



  /**
   * The time the gauge last exited the critical state.
   */
  INFO_GAUGE_DESC_LAST_CRITICAL_END_TIME("The time the gauge last exited the critical state."),



  /**
   * The time the gauge last entered the critical state.
   */
  INFO_GAUGE_DESC_LAST_CRITICAL_START_TIME("The time the gauge last entered the critical state."),



  /**
   * The duration of the last major state for the gauge, in milliseconds.
   */
  INFO_GAUGE_DESC_LAST_MAJOR_DURATION_MILLIS("The duration of the last major state for the gauge, in milliseconds."),



  /**
   * The duration of the last major state for the gauge, as a human-readable string.
   */
  INFO_GAUGE_DESC_LAST_MAJOR_DURATION_STRING("The duration of the last major state for the gauge, as a human-readable string."),



  /**
   * The time the gauge last exited the major state.
   */
  INFO_GAUGE_DESC_LAST_MAJOR_END_TIME("The time the gauge last exited the major state."),



  /**
   * The time the gauge last entered the major state.
   */
  INFO_GAUGE_DESC_LAST_MAJOR_START_TIME("The time the gauge last entered the major state."),



  /**
   * The duration of the last minor state for the gauge, in milliseconds.
   */
  INFO_GAUGE_DESC_LAST_MINOR_DURATION_MILLIS("The duration of the last minor state for the gauge, in milliseconds."),



  /**
   * The duration of the last minor state for the gauge, as a human-readable string.
   */
  INFO_GAUGE_DESC_LAST_MINOR_DURATION_STRING("The duration of the last minor state for the gauge, as a human-readable string."),



  /**
   * The time the gauge last exited the minor state.
   */
  INFO_GAUGE_DESC_LAST_MINOR_END_TIME("The time the gauge last exited the minor state."),



  /**
   * The time the gauge last entered the minor state.
   */
  INFO_GAUGE_DESC_LAST_MINOR_START_TIME("The time the gauge last entered the minor state."),



  /**
   * The duration of the last normal state for the gauge, in milliseconds.
   */
  INFO_GAUGE_DESC_LAST_NORMAL_DURATION_MILLIS("The duration of the last normal state for the gauge, in milliseconds."),



  /**
   * The duration of the last normal state for the gauge, as a human-readable string.
   */
  INFO_GAUGE_DESC_LAST_NORMAL_DURATION_STRING("The duration of the last normal state for the gauge, as a human-readable string."),



  /**
   * The time the gauge last exited the normal state.
   */
  INFO_GAUGE_DESC_LAST_NORMAL_END_TIME("The time the gauge last exited the normal state."),



  /**
   * The time the gauge last entered the normal state.
   */
  INFO_GAUGE_DESC_LAST_NORMAL_START_TIME("The time the gauge last entered the normal state."),



  /**
   * The duration of the last warning state for the gauge, in milliseconds.
   */
  INFO_GAUGE_DESC_LAST_WARNING_DURATION_MILLIS("The duration of the last warning state for the gauge, in milliseconds."),



  /**
   * The duration of the last warning state for the gauge, as a human-readable string.
   */
  INFO_GAUGE_DESC_LAST_WARNING_DURATION_STRING("The duration of the last warning state for the gauge, as a human-readable string."),



  /**
   * The time the gauge last exited the warning state.
   */
  INFO_GAUGE_DESC_LAST_WARNING_END_TIME("The time the gauge last exited the warning state."),



  /**
   * The time the gauge last entered the warning state.
   */
  INFO_GAUGE_DESC_LAST_WARNING_START_TIME("The time the gauge last entered the warning state."),



  /**
   * The previous severity for the associated gauge.
   */
  INFO_GAUGE_DESC_PREVIOUS_SEVERITY("The previous severity for the associated gauge."),



  /**
   * The resource for the associated gauge.
   */
  INFO_GAUGE_DESC_RESOURCE("The resource for the associated gauge."),



  /**
   * The resource type for the associated gauge.
   */
  INFO_GAUGE_DESC_RESOURCE_TYPE("The resource type for the associated gauge."),



  /**
   * The number of samples taken in obtaining the gauge information in the current interval.
   */
  INFO_GAUGE_DESC_SAMPLES_THIS_INTERVAL("The number of samples taken in obtaining the gauge information in the current interval."),



  /**
   * The summary message for the associated gauge.
   */
  INFO_GAUGE_DESC_SUMMARY("The summary message for the associated gauge."),



  /**
   * The total length of time the gauge has spent in the critical state, in milliseconds.
   */
  INFO_GAUGE_DESC_TOTAL_CRITICAL_DURATION_MILLIS("The total length of time the gauge has spent in the critical state, in milliseconds."),



  /**
   * The total length of time the gauge has spent in the critical state, as a human-readable string.
   */
  INFO_GAUGE_DESC_TOTAL_CRITICAL_DURATION_STRING("The total length of time the gauge has spent in the critical state, as a human-readable string."),



  /**
   * The total length of time the gauge has spent in the major state, in milliseconds.
   */
  INFO_GAUGE_DESC_TOTAL_MAJOR_DURATION_MILLIS("The total length of time the gauge has spent in the major state, in milliseconds."),



  /**
   * The total length of time the gauge has spent in the major state, as a human-readable string.
   */
  INFO_GAUGE_DESC_TOTAL_MAJOR_DURATION_STRING("The total length of time the gauge has spent in the major state, as a human-readable string."),



  /**
   * The total length of time the gauge has spent in the minor state, in milliseconds.
   */
  INFO_GAUGE_DESC_TOTAL_MINOR_DURATION_MILLIS("The total length of time the gauge has spent in the minor state, in milliseconds."),



  /**
   * The total length of time the gauge has spent in the minor state, as a human-readable string.
   */
  INFO_GAUGE_DESC_TOTAL_MINOR_DURATION_STRING("The total length of time the gauge has spent in the minor state, as a human-readable string."),



  /**
   * The total length of time the gauge has spent in the normal state, in milliseconds.
   */
  INFO_GAUGE_DESC_TOTAL_NORMAL_DURATION_MILLIS("The total length of time the gauge has spent in the normal state, in milliseconds."),



  /**
   * The total length of time the gauge has spent in the normal state, as a human-readable string.
   */
  INFO_GAUGE_DESC_TOTAL_NORMAL_DURATION_STRING("The total length of time the gauge has spent in the normal state, as a human-readable string."),



  /**
   * The total length of time the gauge has spent in the warning state, in milliseconds.
   */
  INFO_GAUGE_DESC_TOTAL_WARNING_DURATION_MILLIS("The total length of time the gauge has spent in the warning state, in milliseconds."),



  /**
   * The total length of time the gauge has spent in the warning state, as a human-readable string.
   */
  INFO_GAUGE_DESC_TOTAL_WARNING_DURATION_STRING("The total length of time the gauge has spent in the warning state, as a human-readable string."),



  /**
   * The time the gauge information was last updated.
   */
  INFO_GAUGE_DESC_UPDATE_TIME("The time the gauge information was last updated."),



  /**
   * Current Severity Duration (Milliseconds)
   */
  INFO_GAUGE_DISPNAME_CURRENT_DURATION_MILLIS("Current Severity Duration (Milliseconds)"),



  /**
   * Current Severity Duration String
   */
  INFO_GAUGE_DISPNAME_CURRENT_DURATION_STRING("Current Severity Duration String"),



  /**
   * Current Severity
   */
  INFO_GAUGE_DISPNAME_CURRENT_SEVERITY("Current Severity"),



  /**
   * Current Severity Start Time
   */
  INFO_GAUGE_DISPNAME_CURRENT_START_TIME("Current Severity Start Time"),



  /**
   * Error Messages
   */
  INFO_GAUGE_DISPNAME_ERROR_MESSAGE("Error Messages"),



  /**
   * Gauge Name
   */
  INFO_GAUGE_DISPNAME_GAUGE_NAME("Gauge Name"),



  /**
   * Gauge Initialization Time
   */
  INFO_GAUGE_DISPNAME_INIT_TIME("Gauge Initialization Time"),



  /**
   * Last Critical Duration (Milliseconds)
   */
  INFO_GAUGE_DISPNAME_LAST_CRITICAL_DURATION_MILLIS("Last Critical Duration (Milliseconds)"),



  /**
   * Last Critical Duration String
   */
  INFO_GAUGE_DISPNAME_LAST_CRITICAL_DURATION_STRING("Last Critical Duration String"),



  /**
   * Last Critical State End Time
   */
  INFO_GAUGE_DISPNAME_LAST_CRITICAL_END_TIME("Last Critical State End Time"),



  /**
   * Last Critical State Start Time
   */
  INFO_GAUGE_DISPNAME_LAST_CRITICAL_START_TIME("Last Critical State Start Time"),



  /**
   * Last Major Duration (Milliseconds)
   */
  INFO_GAUGE_DISPNAME_LAST_MAJOR_DURATION_MILLIS("Last Major Duration (Milliseconds)"),



  /**
   * Last Major Duration String
   */
  INFO_GAUGE_DISPNAME_LAST_MAJOR_DURATION_STRING("Last Major Duration String"),



  /**
   * Last Major State End Time
   */
  INFO_GAUGE_DISPNAME_LAST_MAJOR_END_TIME("Last Major State End Time"),



  /**
   * Last Major State Start Time
   */
  INFO_GAUGE_DISPNAME_LAST_MAJOR_START_TIME("Last Major State Start Time"),



  /**
   * Last Minor Duration (Milliseconds)
   */
  INFO_GAUGE_DISPNAME_LAST_MINOR_DURATION_MILLIS("Last Minor Duration (Milliseconds)"),



  /**
   * Last Minor Duration String
   */
  INFO_GAUGE_DISPNAME_LAST_MINOR_DURATION_STRING("Last Minor Duration String"),



  /**
   * Last Minor State End Time
   */
  INFO_GAUGE_DISPNAME_LAST_MINOR_END_TIME("Last Minor State End Time"),



  /**
   * Last Minor State Start Time
   */
  INFO_GAUGE_DISPNAME_LAST_MINOR_START_TIME("Last Minor State Start Time"),



  /**
   * Last Normal Duration (Milliseconds)
   */
  INFO_GAUGE_DISPNAME_LAST_NORMAL_DURATION_MILLIS("Last Normal Duration (Milliseconds)"),



  /**
   * Last Normal Duration String
   */
  INFO_GAUGE_DISPNAME_LAST_NORMAL_DURATION_STRING("Last Normal Duration String"),



  /**
   * Last Normal State End Time
   */
  INFO_GAUGE_DISPNAME_LAST_NORMAL_END_TIME("Last Normal State End Time"),



  /**
   * Last Normal State Start Time
   */
  INFO_GAUGE_DISPNAME_LAST_NORMAL_START_TIME("Last Normal State Start Time"),



  /**
   * Last Warning Duration (Milliseconds)
   */
  INFO_GAUGE_DISPNAME_LAST_WARNING_DURATION_MILLIS("Last Warning Duration (Milliseconds)"),



  /**
   * Last Warning Duration String
   */
  INFO_GAUGE_DISPNAME_LAST_WARNING_DURATION_STRING("Last Warning Duration String"),



  /**
   * Last Warning State End Time
   */
  INFO_GAUGE_DISPNAME_LAST_WARNING_END_TIME("Last Warning State End Time"),



  /**
   * Last Warning State Start Time
   */
  INFO_GAUGE_DISPNAME_LAST_WARNING_START_TIME("Last Warning State Start Time"),



  /**
   * Previous Severity
   */
  INFO_GAUGE_DISPNAME_PREVIOUS_SEVERITY("Previous Severity"),



  /**
   * Resource
   */
  INFO_GAUGE_DISPNAME_RESOURCE("Resource"),



  /**
   * Resource Type
   */
  INFO_GAUGE_DISPNAME_RESOURCE_TYPE("Resource Type"),



  /**
   * Samples This Interval
   */
  INFO_GAUGE_DISPNAME_SAMPLES_THIS_INTERVAL("Samples This Interval"),



  /**
   * Summary
   */
  INFO_GAUGE_DISPNAME_SUMMARY("Summary"),



  /**
   * Total Critical Duration (Milliseconds)
   */
  INFO_GAUGE_DISPNAME_TOTAL_CRITICAL_DURATION_MILLIS("Total Critical Duration (Milliseconds)"),



  /**
   * Total Critical Duration String
   */
  INFO_GAUGE_DISPNAME_TOTAL_CRITICAL_DURATION_STRING("Total Critical Duration String"),



  /**
   * Total Major Duration (Milliseconds)
   */
  INFO_GAUGE_DISPNAME_TOTAL_MAJOR_DURATION_MILLIS("Total Major Duration (Milliseconds)"),



  /**
   * Total Major Duration String
   */
  INFO_GAUGE_DISPNAME_TOTAL_MAJOR_DURATION_STRING("Total Major Duration String"),



  /**
   * Total Minor Duration (Milliseconds)
   */
  INFO_GAUGE_DISPNAME_TOTAL_MINOR_DURATION_MILLIS("Total Minor Duration (Milliseconds)"),



  /**
   * Total Minor Duration String
   */
  INFO_GAUGE_DISPNAME_TOTAL_MINOR_DURATION_STRING("Total Minor Duration String"),



  /**
   * Total Normal Duration (Milliseconds)
   */
  INFO_GAUGE_DISPNAME_TOTAL_NORMAL_DURATION_MILLIS("Total Normal Duration (Milliseconds)"),



  /**
   * Total Normal Duration String
   */
  INFO_GAUGE_DISPNAME_TOTAL_NORMAL_DURATION_STRING("Total Normal Duration String"),



  /**
   * Total Warning Duration (Milliseconds)
   */
  INFO_GAUGE_DISPNAME_TOTAL_WARNING_DURATION_MILLIS("Total Warning Duration (Milliseconds)"),



  /**
   * Total Warning Duration String
   */
  INFO_GAUGE_DISPNAME_TOTAL_WARNING_DURATION_STRING("Total Warning Duration String"),



  /**
   * Update Time
   */
  INFO_GAUGE_DISPNAME_UPDATE_TIME("Update Time"),



  /**
   * Provides information about the state of a gauge defined in the server.
   */
  INFO_GAUGE_MONITOR_DESC("Provides information about the state of a gauge defined in the server."),



  /**
   * Gauge
   */
  INFO_GAUGE_MONITOR_DISPNAME("Gauge"),



  /**
   * The name of the cluster to which the server belongs.
   */
  INFO_GENERAL_DESC_CLUSTER_NAME("The name of the cluster to which the server belongs."),



  /**
   * The number of client connections currently established to the server.
   */
  INFO_GENERAL_DESC_CURRENT_CONNECTIONS("The number of client connections currently established to the server."),



  /**
   * The current time on the server at the time the monitor entry was retrieved.
   */
  INFO_GENERAL_DESC_CURRENT_TIME("The current time on the server at the time the monitor entry was retrieved."),



  /**
   * The names of the alert types for any administrative alerts generated within the server that cause the server to be currently classified as ''degraded''.
   */
  INFO_GENERAL_DESC_DEGRADED_ALERT_TYPE("The names of the alert types for any administrative alerts generated within the server that cause the server to be currently classified as ''degraded''."),



  /**
   * The unique name assigned to the Directory Server instance.
   */
  INFO_GENERAL_DESC_INSTANCE_NAME("The unique name assigned to the Directory Server instance."),



  /**
   * The DN of the configuration entry that defines the location to which the server is assigned.
   */
  INFO_GENERAL_DESC_LOCATION_DN("The DN of the configuration entry that defines the location to which the server is assigned."),



  /**
   * The name of the location to which the server is assigned.
   */
  INFO_GENERAL_DESC_LOCATION_NAME("The name of the location to which the server is assigned."),



  /**
   * The maximum number of client connections that have been established to the server at any time since startup.
   */
  INFO_GENERAL_DESC_MAX_CONNECTIONS("The maximum number of client connections that have been established to the server at any time since startup."),



  /**
   * The name of the Directory Server product.
   */
  INFO_GENERAL_DESC_PRODUCT_NAME("The name of the Directory Server product."),



  /**
   * The UUID value that was generated when the server instance was initially created.
   */
  INFO_GENERAL_DESC_SERVER_UUID("The UUID value that was generated when the server instance was initially created."),



  /**
   * A relatively compact identifier assigned to the server on startup and will be different each time the server has started.
   */
  INFO_GENERAL_DESC_STARTUP_ID("A relatively compact identifier assigned to the server on startup and will be different each time the server has started."),



  /**
   * A unique identifier generated at Directory Server startup.
   */
  INFO_GENERAL_DESC_STARTUP_UUID("A unique identifier generated at Directory Server startup."),



  /**
   * The time the Directory Server completed its startup.
   */
  INFO_GENERAL_DESC_START_TIME("The time the Directory Server completed its startup."),



  /**
   * The DNs of the configuration entries for any third-party extensions loaded in the server.
   */
  INFO_GENERAL_DESC_THIRD_PARTY_EXTENSION_DN("The DNs of the configuration entries for any third-party extensions loaded in the server."),



  /**
   * The total number of client connections that have been established since startup.
   */
  INFO_GENERAL_DESC_TOTAL_CONNECTIONS("The total number of client connections that have been established since startup."),



  /**
   * The names of the alert types for any administrative alerts generated within the server that cause the server to be currently classified as ''unavailable''.
   */
  INFO_GENERAL_DESC_UNAVAILABLE_ALERT_TYPE("The names of the alert types for any administrative alerts generated within the server that cause the server to be currently classified as ''unavailable''."),



  /**
   * A human-readable string containing the length of time the server has been online.
   */
  INFO_GENERAL_DESC_UPTIME("A human-readable string containing the length of time the server has been online."),



  /**
   * The length of time in milliseconds that the server has been online.
   */
  INFO_GENERAL_DESC_UPTIME_MILLIS("The length of time in milliseconds that the server has been online."),



  /**
   * The name of the Directory Server vendor.
   */
  INFO_GENERAL_DESC_VENDOR_NAME("The name of the Directory Server vendor."),



  /**
   * The Directory Server version string.
   */
  INFO_GENERAL_DESC_VERSION("The Directory Server version string."),



  /**
   * Cluster Name
   */
  INFO_GENERAL_DISPNAME_CLUSTER_NAME("Cluster Name"),



  /**
   * Current Connections
   */
  INFO_GENERAL_DISPNAME_CURRENT_CONNECTIONS("Current Connections"),



  /**
   * Current Time
   */
  INFO_GENERAL_DISPNAME_CURRENT_TIME("Current Time"),



  /**
   * Degraded Alert Type
   */
  INFO_GENERAL_DISPNAME_DEGRADED_ALERT_TYPE("Degraded Alert Type"),



  /**
   * Server Instance Name
   */
  INFO_GENERAL_DISPNAME_INSTANCE_NAME("Server Instance Name"),



  /**
   * Location DN
   */
  INFO_GENERAL_DISPNAME_LOCATION_DN("Location DN"),



  /**
   * Location Name
   */
  INFO_GENERAL_DISPNAME_LOCATION_NAME("Location Name"),



  /**
   * Maximum Concurrent Connections
   */
  INFO_GENERAL_DISPNAME_MAX_CONNECTIONS("Maximum Concurrent Connections"),



  /**
   * Product Name
   */
  INFO_GENERAL_DISPNAME_PRODUCT_NAME("Product Name"),



  /**
   * Server UUID
   */
  INFO_GENERAL_DISPNAME_SERVER_UUID("Server UUID"),



  /**
   * Startup ID
   */
  INFO_GENERAL_DISPNAME_STARTUP_ID("Startup ID"),



  /**
   * Startup UUID
   */
  INFO_GENERAL_DISPNAME_STARTUP_UUID("Startup UUID"),



  /**
   * Server Start Time
   */
  INFO_GENERAL_DISPNAME_START_TIME("Server Start Time"),



  /**
   * Third-Party Extension DN
   */
  INFO_GENERAL_DISPNAME_THIRD_PARTY_EXTENSION_DN("Third-Party Extension DN"),



  /**
   * Total Connections
   */
  INFO_GENERAL_DISPNAME_TOTAL_CONNECTIONS("Total Connections"),



  /**
   * Unavailable Alert Type
   */
  INFO_GENERAL_DISPNAME_UNAVAILABLE_ALERT_TYPE("Unavailable Alert Type"),



  /**
   * Up Time
   */
  INFO_GENERAL_DISPNAME_UPTIME("Up Time"),



  /**
   * Up Time (ms)
   */
  INFO_GENERAL_DISPNAME_UPTIME_MILLIS("Up Time (ms)"),



  /**
   * Vendor Name
   */
  INFO_GENERAL_DISPNAME_VENDOR_NAME("Vendor Name"),



  /**
   * Server Version
   */
  INFO_GENERAL_DISPNAME_VERSION("Server Version"),



  /**
   * Provides general information about the state of the Directory Server.
   */
  INFO_GENERAL_MONITOR_DESC("Provides general information about the state of the Directory Server."),



  /**
   * General
   */
  INFO_GENERAL_MONITOR_DISPNAME("General"),



  /**
   * A generic monitor entry which does not have any parsed monitor attributes.
   */
  INFO_GENERIC_MONITOR_DESC("A generic monitor entry which does not have any parsed monitor attributes."),



  /**
   * Generic
   */
  INFO_GENERIC_MONITOR_DISPNAME("Generic"),



  /**
   * The amount of memory in bytes used by the group cache.
   */
  INFO_GROUP_CACHE_DESC_CACHE_SIZE_BYTES("The amount of memory in bytes used by the group cache."),



  /**
   * The percentage of the maximum JVM heap size used by the group cache.
   */
  INFO_GROUP_CACHE_DESC_CACHE_SIZE_PERCENT("The percentage of the maximum JVM heap size used by the group cache."),



  /**
   * The length of time in milliseconds required to compute cache usage information.
   */
  INFO_GROUP_CACHE_DESC_CACHE_SIZE_UPDATE_MILLIS("The length of time in milliseconds required to compute cache usage information."),



  /**
   * The number of dynamic group entries defined in the server.
   */
  INFO_GROUP_CACHE_DESC_DYNAMIC_GROUP_ENTRIES("The number of dynamic group entries defined in the server."),



  /**
   * The number of static group entries defined in the server.
   */
  INFO_GROUP_CACHE_DESC_STATIC_GROUP_ENTRIES("The number of static group entries defined in the server."),



  /**
   * The total number of members of all static groups in the server.
   */
  INFO_GROUP_CACHE_DESC_STATIC_GROUP_MEMBERS("The total number of members of all static groups in the server."),



  /**
   * The number of unique members of static groups in the server.
   */
  INFO_GROUP_CACHE_DESC_STATIC_GROUP_UNIQUE_MEMBERS("The number of unique members of static groups in the server."),



  /**
   * The number of virtual static group entries defined in the server.
   */
  INFO_GROUP_CACHE_DESC_VIRTUAL_STATIC_GROUP_ENTRIES("The number of virtual static group entries defined in the server."),



  /**
   * Current Cache Used (Bytes)
   */
  INFO_GROUP_CACHE_DISPNAME_CACHE_SIZE_BYTES("Current Cache Used (Bytes)"),



  /**
   * Current Cache Used (Percent of Max Heap Size)
   */
  INFO_GROUP_CACHE_DISPNAME_CACHE_SIZE_PERCENT("Current Cache Used (Percent of Max Heap Size)"),



  /**
   * Current Cache Used Update Duration (Milliseconds)
   */
  INFO_GROUP_CACHE_DISPNAME_CACHE_SIZE_UPDATE_MILLIS("Current Cache Used Update Duration (Milliseconds)"),



  /**
   * Dynamic Group Entries
   */
  INFO_GROUP_CACHE_DISPNAME_DYNAMIC_GROUP_ENTRIES("Dynamic Group Entries"),



  /**
   * Static Group Entries
   */
  INFO_GROUP_CACHE_DISPNAME_STATIC_GROUP_ENTRIES("Static Group Entries"),



  /**
   * Total Static Group Members
   */
  INFO_GROUP_CACHE_DISPNAME_STATIC_GROUP_MEMBERS("Total Static Group Members"),



  /**
   * Unique Static Group Members
   */
  INFO_GROUP_CACHE_DISPNAME_STATIC_GROUP_UNIQUE_MEMBERS("Unique Static Group Members"),



  /**
   * Virtual Static Group Entries
   */
  INFO_GROUP_CACHE_DISPNAME_VIRTUAL_STATIC_GROUP_ENTRIES("Virtual Static Group Entries"),



  /**
   * Provides information about the state of the server group cache, including the number of groups of different types defined in the server and the amount of memory consumed by the cache.
   */
  INFO_GROUP_CACHE_MONITOR_DESC("Provides information about the state of the server group cache, including the number of groups of different types defined in the server and the amount of memory consumed by the cache."),



  /**
   * Group Cache
   */
  INFO_GROUP_CACHE_MONITOR_DISPNAME("Group Cache"),



  /**
   * The name of the attribute type with which the index is associated.  This is only applicable for attribute indexes.
   */
  INFO_INDEX_DESC_ATTR_TYPE("The name of the attribute type with which the index is associated.  This is only applicable for attribute indexes."),



  /**
   * The name of the backend in which the index is maintained.
   */
  INFO_INDEX_DESC_BACKEND_ID("The name of the backend in which the index is maintained."),



  /**
   * The base DN for the backend in which the index is maintained.  Note that if a backend has multiple base DNs, it will maintain separate sets of indexes for each base DN.
   */
  INFO_INDEX_DESC_BASE_DN("The base DN for the backend in which the index is maintained.  Note that if a backend has multiple base DNs, it will maintain separate sets of indexes for each base DN."),



  /**
   * The number of cursors created in the index for operating on ranges of keys since the index was brought online.
   */
  INFO_INDEX_DESC_CURSOR_COUNT("The number of cursors created in the index for operating on ranges of keys since the index was brought online."),



  /**
   * The number of keys which have been deleted from the index since it was brought online.
   */
  INFO_INDEX_DESC_DELETE_COUNT("The number of keys which have been deleted from the index since it was brought online."),



  /**
   * The maximum number of entries that may match any single index key.  If the number of entries matching that key exceeds the index entry limit, then that key will no longer be maintained and will not be available for use in processing operations.
   */
  INFO_INDEX_DESC_ENTRY_LIMIT("The maximum number of entries that may match any single index key.  If the number of entries matching that key exceeds the index entry limit, then that key will no longer be maintained and will not be available for use in processing operations."),



  /**
   * The number of index keys which have exceeded the index entry limit since the index was brought online.
   */
  INFO_INDEX_DESC_EXCEEDED_COUNT("The number of index keys which have exceeded the index entry limit since the index was brought online."),



  /**
   * A search filter which may be used to restrict the way that indexing is performed.  If a filter is defined, then the index will only reference entries which match this filter and have one or more values for the associated attribute type, and will only be used for search operations in which the filter contains an AND sequence containing this filter and a component with the associated attribute type.
   */
  INFO_INDEX_DESC_FILTER("A search filter which may be used to restrict the way that indexing is performed.  If a filter is defined, then the index will only reference entries which match this filter and have one or more values for the associated attribute type, and will only be used for search operations in which the filter contains an AND sequence containing this filter and a component with the associated attribute type."),



  /**
   * The number of index reads which have been initiated by the inclusion of the associated attribute type in the filter for a search operation with a non-base scope since the index was brought online.
   */
  INFO_INDEX_DESC_FILTER_INITIATED_READ_COUNT("The number of index reads which have been initiated by the inclusion of the associated attribute type in the filter for a search operation with a non-base scope since the index was brought online."),



  /**
   * Indicates whether the index was fully primed when the backend was brought online.
   */
  INFO_INDEX_DESC_FULLY_PRIMED("Indicates whether the index was fully primed when the backend was brought online."),



  /**
   * The name of the database in which the index is maintained.
   */
  INFO_INDEX_DESC_INDEX_NAME("The name of the database in which the index is maintained."),



  /**
   * The name of the index type for attribute indexes (e.g., presence, equality, ordering, substring, or approximate).  If an attribute has multiple index types, then they will be stored in separate index databases.
   */
  INFO_INDEX_DESC_INDEX_TYPE("The name of the index type for attribute indexes (e.g., presence, equality, ordering, substring, or approximate).  If an attribute has multiple index types, then they will be stored in separate index databases."),



  /**
   * Indicates whether the number of matching entries will continue to be maintained for index keys that have exceeded the index entry limit, even if the list of entries will not be maintained.
   */
  INFO_INDEX_DESC_MAINTAIN_COUNT("Indicates whether the number of matching entries will continue to be maintained for index keys that have exceeded the index entry limit, even if the list of entries will not be maintained."),



  /**
   * The number of index keys that were primed when the backend was brought online.
   */
  INFO_INDEX_DESC_PRIMED_KEYS("The number of index keys that were primed when the backend was brought online."),



  /**
   * Information about an exception caught during prime processing that caused the prime to be incomplete.
   */
  INFO_INDEX_DESC_PRIME_EXCEPTION("Information about an exception caught during prime processing that caused the prime to be incomplete."),



  /**
   * Information about the reason that the prime might not have completed when the index was brought online.
   */
  INFO_INDEX_DESC_PRIME_INCOMPLETE_REASON("Information about the reason that the prime might not have completed when the index was brought online."),



  /**
   * The number of keys which have been read from the index since it was brought online.
   */
  INFO_INDEX_DESC_READ_COUNT("The number of keys which have been read from the index since it was brought online."),



  /**
   * The number of unique index keys near (typically, within 80% of) the index entry limit that have been accessed by search operations since the index was brought online.
   */
  INFO_INDEX_DESC_SEARCH_KEYS_NEAR_LIMIT("The number of unique index keys near (typically, within 80% of) the index entry limit that have been accessed by search operations since the index was brought online."),



  /**
   * The number of unique index keys over the index entry limit that have been accessed by search operations since the index was brought online.
   */
  INFO_INDEX_DESC_SEARCH_KEYS_OVER_LIMIT("The number of unique index keys over the index entry limit that have been accessed by search operations since the index was brought online."),



  /**
   * Indicates whether the index database is considered trusted and it may be used for processing operations.
   */
  INFO_INDEX_DESC_TRUSTED("Indicates whether the index database is considered trusted and it may be used for processing operations."),



  /**
   * The number of keys which have been inserted or replaced in the index since the it was brought online.
   */
  INFO_INDEX_DESC_WRITE_COUNT("The number of keys which have been inserted or replaced in the index since the it was brought online."),



  /**
   * The number of unique index keys near (typically, within 80% of) the index entry limit that have been accessed by add, delete, modify, or modify DN operations since the index was brought online.
   */
  INFO_INDEX_DESC_WRITE_KEYS_NEAR_LIMIT("The number of unique index keys near (typically, within 80% of) the index entry limit that have been accessed by add, delete, modify, or modify DN operations since the index was brought online."),



  /**
   * The number of unique index keys over the index entry limit that have been accessed by add, delete, modify, or modify DN operations since the index was brought online.
   */
  INFO_INDEX_DESC_WRITE_KEYS_OVER_LIMIT("The number of unique index keys over the index entry limit that have been accessed by add, delete, modify, or modify DN operations since the index was brought online."),



  /**
   * Attribute Type
   */
  INFO_INDEX_DISPNAME_ATTR_TYPE("Attribute Type"),



  /**
   * Backend ID
   */
  INFO_INDEX_DISPNAME_BACKEND_ID("Backend ID"),



  /**
   * Base DN
   */
  INFO_INDEX_DISPNAME_BASE_DN("Base DN"),



  /**
   * Number of Cursors Created Since Coming Online
   */
  INFO_INDEX_DISPNAME_CURSOR_COUNT("Number of Cursors Created Since Coming Online"),



  /**
   * Number of Keys Deleted Since Coming Online
   */
  INFO_INDEX_DISPNAME_DELETE_COUNT("Number of Keys Deleted Since Coming Online"),



  /**
   * Index Entry Limit
   */
  INFO_INDEX_DISPNAME_ENTRY_LIMIT("Index Entry Limit"),



  /**
   * Keys Exceeding Entry Limit Since Coming Online
   */
  INFO_INDEX_DISPNAME_EXCEEDED_COUNT("Keys Exceeding Entry Limit Since Coming Online"),



  /**
   * Index Filter
   */
  INFO_INDEX_DISPNAME_FILTER("Index Filter"),



  /**
   * Number of Filter-Initiated Reads Since Coming Online
   */
  INFO_INDEX_DISPNAME_FILTER_INITIATED_READ_COUNT("Number of Filter-Initiated Reads Since Coming Online"),



  /**
   * Index Was Fully Primed
   */
  INFO_INDEX_DISPNAME_FULLY_PRIMED("Index Was Fully Primed"),



  /**
   * Index Name
   */
  INFO_INDEX_DISPNAME_INDEX_NAME("Index Name"),



  /**
   * Attribute Index Type
   */
  INFO_INDEX_DISPNAME_INDEX_TYPE("Attribute Index Type"),



  /**
   * Maintain Entry Count for Keys Exceeding Entry Limit
   */
  INFO_INDEX_DISPNAME_MAINTAIN_COUNT("Maintain Entry Count for Keys Exceeding Entry Limit"),



  /**
   * Number of Keys Primed
   */
  INFO_INDEX_DISPNAME_PRIMED_KEYS("Number of Keys Primed"),



  /**
   * Prime Exception
   */
  INFO_INDEX_DISPNAME_PRIME_EXCEPTION("Prime Exception"),



  /**
   * Prime Incomplete Reason
   */
  INFO_INDEX_DISPNAME_PRIME_INCOMPLETE_REASON("Prime Incomplete Reason"),



  /**
   * Number of Keys Read Since Coming Online
   */
  INFO_INDEX_DISPNAME_READ_COUNT("Number of Keys Read Since Coming Online"),



  /**
   * Keys Near Limit Accessed by Search Since Coming Online
   */
  INFO_INDEX_DISPNAME_SEARCH_KEYS_NEAR_LIMIT("Keys Near Limit Accessed by Search Since Coming Online"),



  /**
   * Keys Over Limit Accessed by Search Since Coming Online
   */
  INFO_INDEX_DISPNAME_SEARCH_KEYS_OVER_LIMIT("Keys Over Limit Accessed by Search Since Coming Online"),



  /**
   * Index Is Trusted
   */
  INFO_INDEX_DISPNAME_TRUSTED("Index Is Trusted"),



  /**
   * Number of Keys Written Since Coming Online
   */
  INFO_INDEX_DISPNAME_WRITE_COUNT("Number of Keys Written Since Coming Online"),



  /**
   * Keys Near Limit Accessed by Write Since Coming Online
   */
  INFO_INDEX_DISPNAME_WRITE_KEYS_NEAR_LIMIT("Keys Near Limit Accessed by Write Since Coming Online"),



  /**
   * Keys Over Limit Accessed by Write Since Coming Online
   */
  INFO_INDEX_DISPNAME_WRITE_KEYS_OVER_LIMIT("Keys Over Limit Accessed by Write Since Coming Online"),



  /**
   * Provides information about processing associated with index databases, including statistics about interactions with those indexes and information about any priming performed for that index.
   */
  INFO_INDEX_MONITOR_DESC("Provides information about processing associated with index databases, including statistics about interactions with those indexes and information about any priming performed for that index."),



  /**
   * Index Database
   */
  INFO_INDEX_MONITOR_DISPNAME("Index Database"),



  /**
   * The current value for the gauge.
   */
  INFO_INDICATOR_GAUGE_DESC_CURRENT_VALUE("The current value for the gauge."),



  /**
   * The values observed for the gauge.
   */
  INFO_INDICATOR_GAUGE_DESC_OBSERVED_VALUES("The values observed for the gauge."),



  /**
   * The previous value for the gauge.
   */
  INFO_INDICATOR_GAUGE_DESC_PREVIOUS_VALUE("The previous value for the gauge."),



  /**
   * Current Value
   */
  INFO_INDICATOR_GAUGE_DISPNAME_CURRENT_VALUE("Current Value"),



  /**
   * Observed Values
   */
  INFO_INDICATOR_GAUGE_DISPNAME_OBSERVED_VALUES("Observed Values"),



  /**
   * Previous Value
   */
  INFO_INDICATOR_GAUGE_DISPNAME_PREVIOUS_VALUE("Previous Value"),



  /**
   * Provides information about the state of an indicator gauge defined in the server.
   */
  INFO_INDICATOR_GAUGE_MONITOR_DESC("Provides information about the state of an indicator gauge defined in the server."),



  /**
   * Indicator Gauge
   */
  INFO_INDICATOR_GAUGE_MONITOR_DISPNAME("Indicator Gauge"),



  /**
   * The number of transactions currently active in the database environment.
   */
  INFO_JE_ENVIRONMENT_DESC_ACTIVE_TXNS("The number of transactions currently active in the database environment."),



  /**
   * The average duration in milliseconds of all checkpoints that have been completed in the associated backend.
   */
  INFO_JE_ENVIRONMENT_DESC_AVG_CP_DURATION("The average duration in milliseconds of all checkpoints that have been completed in the associated backend."),



  /**
   * The backend ID for the backend with which the JE environment statistics are associated.
   */
  INFO_JE_ENVIRONMENT_DESC_BACKEND_ID("The backend ID for the backend with which the JE environment statistics are associated."),



  /**
   * The percentage of the maximum amount of memory available to the database cache that is currently in use.
   */
  INFO_JE_ENVIRONMENT_DESC_CACHE_PCT_FULL("The percentage of the maximum amount of memory available to the database cache that is currently in use."),



  /**
   * The number of database log files that the cleaner needs to examine.
   */
  INFO_JE_ENVIRONMENT_DESC_CLEANER_BACKLOG("The number of database log files that the cleaner needs to examine."),



  /**
   * Indicates whether a checkpoint is currently in progress in the associated backend.
   */
  INFO_JE_ENVIRONMENT_DESC_CP_IN_PROGRESS("Indicates whether a checkpoint is currently in progress in the associated backend."),



  /**
   * The amount of memory in bytes currently consumed by the database cache.
   */
  INFO_JE_ENVIRONMENT_DESC_CURRENT_CACHE_SIZE("The amount of memory in bytes currently consumed by the database cache."),



  /**
   * The path to the directory containing the database environment files.
   */
  INFO_JE_ENVIRONMENT_DESC_DB_DIRECTORY("The path to the directory containing the database environment files."),



  /**
   * The total amount of disk space in bytes consumed by all of the database files.
   */
  INFO_JE_ENVIRONMENT_DESC_DB_ON_DISK_SIZE("The total amount of disk space in bytes consumed by all of the database files."),



  /**
   * A set of general statistics for the database environment, arranged in the form ''name=value''.
   */
  INFO_JE_ENVIRONMENT_DESC_ENV_STATS("A set of general statistics for the database environment, arranged in the form ''name=value''."),



  /**
   * The version string for the Berkeley DB Java Edition database with which the backend is associated.
   */
  INFO_JE_ENVIRONMENT_DESC_JE_VERSION("The version string for the Berkeley DB Java Edition database with which the backend is associated."),



  /**
   * The duration in milliseconds of the last checkpoint completed in the associated backend.
   */
  INFO_JE_ENVIRONMENT_DESC_LAST_CP_DURATION("The duration in milliseconds of the last checkpoint completed in the associated backend."),



  /**
   * The time that the last completed checkpoint began.
   */
  INFO_JE_ENVIRONMENT_DESC_LAST_CP_START_TIME("The time that the last completed checkpoint began."),



  /**
   * The time that the last completed checkpoint ended.
   */
  INFO_JE_ENVIRONMENT_DESC_LAST_CP_STOP_TIME("The time that the last completed checkpoint ended."),



  /**
   * A set of lock statistics for the database environment, arranged in the form ''name=value''.
   */
  INFO_JE_ENVIRONMENT_DESC_LOCK_STATS("A set of lock statistics for the database environment, arranged in the form ''name=value''."),



  /**
   * The maximum amount of memory in bytes that may be consumed by the database cache.
   */
  INFO_JE_ENVIRONMENT_DESC_MAX_CACHE_SIZE("The maximum amount of memory in bytes that may be consumed by the database cache."),



  /**
   * The length of time in milliseconds since the last completed checkpoint in the associated backend.
   */
  INFO_JE_ENVIRONMENT_DESC_MILLIS_SINCE_CP("The length of time in milliseconds since the last completed checkpoint in the associated backend."),



  /**
   * The number of nodes forced out of the database cache to make room for additional data.
   */
  INFO_JE_ENVIRONMENT_DESC_NODES_EVICTED("The number of nodes forced out of the database cache to make room for additional data."),



  /**
   * The number of checkpoints that have been completed in the associated backend.
   */
  INFO_JE_ENVIRONMENT_DESC_NUM_CP("The number of checkpoints that have been completed in the associated backend."),



  /**
   * The number of disk reads in the database environment that likely required a seek to reach the appropriate location in the log.
   */
  INFO_JE_ENVIRONMENT_DESC_RANDOM_READS("The number of disk reads in the database environment that likely required a seek to reach the appropriate location in the log."),



  /**
   * The number of disk writes in the database environment that likely required a seek to the end of the log.
   */
  INFO_JE_ENVIRONMENT_DESC_RANDOM_WRITES("The number of disk writes in the database environment that likely required a seek to the end of the log."),



  /**
   * The number of read locks held by transactions in progress in the database environment.
   */
  INFO_JE_ENVIRONMENT_DESC_READ_LOCKS("The number of read locks held by transactions in progress in the database environment."),



  /**
   * The number of disk reads in the database environment that were close to the location of the last disk read or write.
   */
  INFO_JE_ENVIRONMENT_DESC_SEQUENTIAL_READS("The number of disk reads in the database environment that were close to the location of the last disk read or write."),



  /**
   * The number of disk writes in the database environment that were close to the location of the last disk read or write.
   */
  INFO_JE_ENVIRONMENT_DESC_SEQUENTIAL_WRITES("The number of disk writes in the database environment that were close to the location of the last disk read or write."),



  /**
   * The total duration in milliseconds of all checkpoints that have been completed in the associated backend.
   */
  INFO_JE_ENVIRONMENT_DESC_TOTAL_CP_DURATION("The total duration in milliseconds of all checkpoints that have been completed in the associated backend."),



  /**
   * The number of transactions that are currently blocked while trying to acquire a lock in the database environment.
   */
  INFO_JE_ENVIRONMENT_DESC_TXNS_WAITING_ON_LOCKS("The number of transactions that are currently blocked while trying to acquire a lock in the database environment."),



  /**
   * A set of transaction statistics for the database environment, arranged in the form ''name=value''.
   */
  INFO_JE_ENVIRONMENT_DESC_TXN_STATS("A set of transaction statistics for the database environment, arranged in the form ''name=value''."),



  /**
   * The number of write locks held by transactions in progress in the database environment.
   */
  INFO_JE_ENVIRONMENT_DESC_WRITE_LOCKS("The number of write locks held by transactions in progress in the database environment."),



  /**
   * Active Transactions
   */
  INFO_JE_ENVIRONMENT_DISPNAME_ACTIVE_TXNS("Active Transactions"),



  /**
   * Average Checkpoint Duration (ms)
   */
  INFO_JE_ENVIRONMENT_DISPNAME_AVG_CP_DURATION("Average Checkpoint Duration (ms)"),



  /**
   * Backend ID
   */
  INFO_JE_ENVIRONMENT_DISPNAME_BACKEND_ID("Backend ID"),



  /**
   * DB Cache Percent Full
   */
  INFO_JE_ENVIRONMENT_DISPNAME_CACHE_PCT_FULL("DB Cache Percent Full"),



  /**
   * Cleaner Backlog
   */
  INFO_JE_ENVIRONMENT_DISPNAME_CLEANER_BACKLOG("Cleaner Backlog"),



  /**
   * Checkpoint In Progress
   */
  INFO_JE_ENVIRONMENT_DISPNAME_CP_IN_PROGRESS("Checkpoint In Progress"),



  /**
   * Current DB Cache Size
   */
  INFO_JE_ENVIRONMENT_DISPNAME_CURRENT_CACHE_SIZE("Current DB Cache Size"),



  /**
   * Database Directory
   */
  INFO_JE_ENVIRONMENT_DISPNAME_DB_DIRECTORY("Database Directory"),



  /**
   * DB On-Disk Size
   */
  INFO_JE_ENVIRONMENT_DISPNAME_DB_ON_DISK_SIZE("DB On-Disk Size"),



  /**
   * Environment Statistics
   */
  INFO_JE_ENVIRONMENT_DISPNAME_ENV_STATS("Environment Statistics"),



  /**
   * Berkeley DB JE Version
   */
  INFO_JE_ENVIRONMENT_DISPNAME_JE_VERSION("Berkeley DB JE Version"),



  /**
   * Last Checkpoint Duration (ms)
   */
  INFO_JE_ENVIRONMENT_DISPNAME_LAST_CP_DURATION("Last Checkpoint Duration (ms)"),



  /**
   * Last Checkpoint Start Time
   */
  INFO_JE_ENVIRONMENT_DISPNAME_LAST_CP_START_TIME("Last Checkpoint Start Time"),



  /**
   * Last Checkpoint Stop Time
   */
  INFO_JE_ENVIRONMENT_DISPNAME_LAST_CP_STOP_TIME("Last Checkpoint Stop Time"),



  /**
   * Lock Statistics
   */
  INFO_JE_ENVIRONMENT_DISPNAME_LOCK_STATS("Lock Statistics"),



  /**
   * Maximum DB Cache Size
   */
  INFO_JE_ENVIRONMENT_DISPNAME_MAX_CACHE_SIZE("Maximum DB Cache Size"),



  /**
   * Time Since Last Checkpoint (ms)
   */
  INFO_JE_ENVIRONMENT_DISPNAME_MILLIS_SINCE_CP("Time Since Last Checkpoint (ms)"),



  /**
   * Nodes Evicted from the DB Cache
   */
  INFO_JE_ENVIRONMENT_DISPNAME_NODES_EVICTED("Nodes Evicted from the DB Cache"),



  /**
   * Number of Checkpoints
   */
  INFO_JE_ENVIRONMENT_DISPNAME_NUM_CP("Number of Checkpoints"),



  /**
   * Random-Access Reads Performed
   */
  INFO_JE_ENVIRONMENT_DISPNAME_RANDOM_READS("Random-Access Reads Performed"),



  /**
   * Random-Access Writes Performed
   */
  INFO_JE_ENVIRONMENT_DISPNAME_RANDOM_WRITES("Random-Access Writes Performed"),



  /**
   * Read Locks Held
   */
  INFO_JE_ENVIRONMENT_DISPNAME_READ_LOCKS("Read Locks Held"),



  /**
   * Sequential Reads Performed
   */
  INFO_JE_ENVIRONMENT_DISPNAME_SEQUENTIAL_READS("Sequential Reads Performed"),



  /**
   * Sequential Writes Performed
   */
  INFO_JE_ENVIRONMENT_DISPNAME_SEQUENTIAL_WRITES("Sequential Writes Performed"),



  /**
   * Total Checkpoint Duration (ms)
   */
  INFO_JE_ENVIRONMENT_DISPNAME_TOTAL_CP_DURATION("Total Checkpoint Duration (ms)"),



  /**
   * Transactions Waiting on Locks
   */
  INFO_JE_ENVIRONMENT_DISPNAME_TXNS_WAITING_ON_LOCKS("Transactions Waiting on Locks"),



  /**
   * Transaction Statistics
   */
  INFO_JE_ENVIRONMENT_DISPNAME_TXN_STATS("Transaction Statistics"),



  /**
   * Write Locks Held
   */
  INFO_JE_ENVIRONMENT_DISPNAME_WRITE_LOCKS("Write Locks Held"),



  /**
   * Provides information about the state of the Berkeley DB Java Edition database used by a Directory Server backend.  Note that most of the statistics presented in the monitor entry are obtained from interfaces within the Berkeley DB Java Edition and therefore are not under the control of the Directory Server.  As such, these statistics are available in the underlying entry, but will be omitted from the set of parsed monitor attributes.
   */
  INFO_JE_ENVIRONMENT_MONITOR_DESC("Provides information about the state of the Berkeley DB Java Edition database used by a Directory Server backend.  Note that most of the statistics presented in the monitor entry are obtained from interfaces within the Berkeley DB Java Edition and therefore are not under the control of the Directory Server.  As such, these statistics are available in the underlying entry, but will be omitted from the set of parsed monitor attributes."),



  /**
   * Berkeley DB JE Environment
   */
  INFO_JE_ENVIRONMENT_MONITOR_DISPNAME("Berkeley DB JE Environment"),



  /**
   * The total number of add operations attempted against the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_ADD_ATTEMPTS("The total number of add operations attempted against the LDAP external server."),



  /**
   * The number of failed add operations attempted against the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_ADD_FAILURES("The number of failed add operations attempted against the LDAP external server."),



  /**
   * The number of successful add operations attempted against the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_ADD_SUCCESSES("The number of successful add operations attempted against the LDAP external server."),



  /**
   * The total number of bind operations attempted against the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_BIND_ATTEMPTS("The total number of bind operations attempted against the LDAP external server."),



  /**
   * The number of connections currently available in the connection pool used for processing bind operations in the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_BIND_AVAILABLE_CONNS("The number of connections currently available in the connection pool used for processing bind operations in the LDAP external server."),



  /**
   * The number of failed attempts to check out a connection from the connection pool used for processing bind operations in the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_BIND_CHECKOUT_FAILED("The number of failed attempts to check out a connection from the connection pool used for processing bind operations in the LDAP external server."),



  /**
   * The number of attempts to check out a connection from the connection pool used for processing bind operations in the LDAP external server that were required to create a new connection since none were available.
   */
  INFO_LDAP_EXT_SERVER_DESC_BIND_CHECKOUT_NEW_CONN("The number of attempts to check out a connection from the connection pool used for processing bind operations in the LDAP external server that were required to create a new connection since none were available."),



  /**
   * The number of attempts to check out a connection from the connection pool used for processing bind operations in the LDAP external server that were able to retrieve a connection without needing to wait for one to become available.
   */
  INFO_LDAP_EXT_SERVER_DESC_BIND_CHECKOUT_NO_WAIT("The number of attempts to check out a connection from the connection pool used for processing bind operations in the LDAP external server that were able to retrieve a connection without needing to wait for one to become available."),



  /**
   * The total number of successful attempts to check out a connection from the connection pool used for processing bind operations in the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_BIND_CHECKOUT_SUCCESS("The total number of successful attempts to check out a connection from the connection pool used for processing bind operations in the LDAP external server."),



  /**
   * The number of attempts to check out a connection from the connection pool used for processing bind operations in the LDAP external server that were able to retrieve a connection only after waiting for one to become available.
   */
  INFO_LDAP_EXT_SERVER_DESC_BIND_CHECKOUT_WITH_WAIT("The number of attempts to check out a connection from the connection pool used for processing bind operations in the LDAP external server that were able to retrieve a connection only after waiting for one to become available."),



  /**
   * The number of connections in the connection pool used for processing bind operations in the LDAP external server that were closed as defunct (e.g., because an error had occurred which indicated the connection may no longer be valid).
   */
  INFO_LDAP_EXT_SERVER_DESC_BIND_CLOSED_DEFUNCT("The number of connections in the connection pool used for processing bind operations in the LDAP external server that were closed as defunct (e.g., because an error had occurred which indicated the connection may no longer be valid)."),



  /**
   * The number of connections in the connection pool used for processing bind operations in the LDAP external server that were closed as expired (because they had been established for longer the maximum connection age).
   */
  INFO_LDAP_EXT_SERVER_DESC_BIND_CLOSED_EXPIRED("The number of connections in the connection pool used for processing bind operations in the LDAP external server that were closed as expired (because they had been established for longer the maximum connection age)."),



  /**
   * The number of connections in the connection pool used for processing bind operations in the LDAP external server that were closed as unneeded (because the pool already had the maximum number of available connections when the connection was released).
   */
  INFO_LDAP_EXT_SERVER_DESC_BIND_CLOSED_UNNEEDED("The number of connections in the connection pool used for processing bind operations in the LDAP external server that were closed as unneeded (because the pool already had the maximum number of available connections when the connection was released)."),



  /**
   * The number of failed attempts to create a new connection for use in the connection pool used for processing bind operations in the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_BIND_CONNECT_FAILED("The number of failed attempts to create a new connection for use in the connection pool used for processing bind operations in the LDAP external server."),



  /**
   * The number of successful attempts to create a new connection for use in the connection pool used for processing bind operations in the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_BIND_CONNECT_SUCCESS("The number of successful attempts to create a new connection for use in the connection pool used for processing bind operations in the LDAP external server."),



  /**
   * The number of failed bind operations attempted against the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_BIND_FAILURES("The number of failed bind operations attempted against the LDAP external server."),



  /**
   * The maximum number of connections that may be available in the connection pool used for processing bind operations in the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_BIND_MAX_AVAILABLE_CONNS("The maximum number of connections that may be available in the connection pool used for processing bind operations in the LDAP external server."),



  /**
   * The number of times a valid connection was successfully released back to the connection pool used for processing bind operations in the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_BIND_RELEASED_VALID("The number of times a valid connection was successfully released back to the connection pool used for processing bind operations in the LDAP external server."),



  /**
   * The number of successful bind operations attempted against the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_BIND_SUCCESSES("The number of successful bind operations attempted against the LDAP external server."),



  /**
   * The number of connections currently available in the common connection pool used for both bind and non-bind operations in the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_COMMON_AVAILABLE_CONNS("The number of connections currently available in the common connection pool used for both bind and non-bind operations in the LDAP external server."),



  /**
   * The number of failed attempts to check out a connection from the common connection pool used for both bind and non-bind operations in the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_COMMON_CHECKOUT_FAILED("The number of failed attempts to check out a connection from the common connection pool used for both bind and non-bind operations in the LDAP external server."),



  /**
   * The number of attempts to check out a connection from the common connection pool used for both bind and non-bind operations in the LDAP external server that were required to create a new connection since none were available.
   */
  INFO_LDAP_EXT_SERVER_DESC_COMMON_CHECKOUT_NEW_CONN("The number of attempts to check out a connection from the common connection pool used for both bind and non-bind operations in the LDAP external server that were required to create a new connection since none were available."),



  /**
   * The number of attempts to check out a connection from the common connection pool used for both bind and non-bind operations in the LDAP external server that were able to retrieve a connection without needing to wait for one to become available.
   */
  INFO_LDAP_EXT_SERVER_DESC_COMMON_CHECKOUT_NO_WAIT("The number of attempts to check out a connection from the common connection pool used for both bind and non-bind operations in the LDAP external server that were able to retrieve a connection without needing to wait for one to become available."),



  /**
   * The total number of successful attempts to check out a connection from the common connection pool used for both bind and non-bind operations in the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_COMMON_CHECKOUT_SUCCESS("The total number of successful attempts to check out a connection from the common connection pool used for both bind and non-bind operations in the LDAP external server."),



  /**
   * The number of attempts to check out a connection from the common connection pool used for both bind and non-bind operations in the LDAP external server that were able to retrieve a connection only after waiting for one to become available.
   */
  INFO_LDAP_EXT_SERVER_DESC_COMMON_CHECKOUT_WITH_WAIT("The number of attempts to check out a connection from the common connection pool used for both bind and non-bind operations in the LDAP external server that were able to retrieve a connection only after waiting for one to become available."),



  /**
   * The number of connections in the common connection pool used for both bind and non-bind operations in the LDAP external server that were closed as defunct (e.g., because an error had occurred which indicated the connection may no longer be valid).
   */
  INFO_LDAP_EXT_SERVER_DESC_COMMON_CLOSED_DEFUNCT("The number of connections in the common connection pool used for both bind and non-bind operations in the LDAP external server that were closed as defunct (e.g., because an error had occurred which indicated the connection may no longer be valid)."),



  /**
   * The number of connections in the common connection pool used for both bind and non-bind operations in the LDAP external server that were closed as expired (because they had been established for longer the maximum connection age).
   */
  INFO_LDAP_EXT_SERVER_DESC_COMMON_CLOSED_EXPIRED("The number of connections in the common connection pool used for both bind and non-bind operations in the LDAP external server that were closed as expired (because they had been established for longer the maximum connection age)."),



  /**
   * The number of connections in the common connection pool used for both bind and non-bind operations in the LDAP external server that were closed as unneeded (because the pool already had the maximum number of available connections when the connection was released).
   */
  INFO_LDAP_EXT_SERVER_DESC_COMMON_CLOSED_UNNEEDED("The number of connections in the common connection pool used for both bind and non-bind operations in the LDAP external server that were closed as unneeded (because the pool already had the maximum number of available connections when the connection was released)."),



  /**
   * The number of failed attempts to create a new connection for use in the common connection pool used for both bind and non-bind operations in the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_COMMON_CONNECT_FAILED("The number of failed attempts to create a new connection for use in the common connection pool used for both bind and non-bind operations in the LDAP external server."),



  /**
   * The number of successful attempts to create a new connection for use in the common connection pool used for both bind and non-bind operations in the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_COMMON_CONNECT_SUCCESS("The number of successful attempts to create a new connection for use in the common connection pool used for both bind and non-bind operations in the LDAP external server."),



  /**
   * The maximum number of connections that may be available in the common connection pool used for both bind and non-bind operations in the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_COMMON_MAX_AVAILABLE_CONNS("The maximum number of connections that may be available in the common connection pool used for both bind and non-bind operations in the LDAP external server."),



  /**
   * The number of times a valid connection was successfully released back to the common connection pool used for both bind and non-bind operations in the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_COMMON_RELEASED_VALID("The number of times a valid connection was successfully released back to the common connection pool used for both bind and non-bind operations in the LDAP external server."),



  /**
   * The security mechanism used when communicating with the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_COMMUNICATION_SECURITY("The security mechanism used when communicating with the LDAP external server."),



  /**
   * The total number of compare operations attempted against the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_COMPARE_ATTEMPTS("The total number of compare operations attempted against the LDAP external server."),



  /**
   * The number of failed compare operations attempted against the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_COMPARE_FAILURES("The number of failed compare operations attempted against the LDAP external server."),



  /**
   * The number of successful compare operations attempted against the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_COMPARE_SUCCESSES("The number of successful compare operations attempted against the LDAP external server."),



  /**
   * The total number of delete operations attempted against the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_DELETE_ATTEMPTS("The total number of delete operations attempted against the LDAP external server."),



  /**
   * The number of failed delete operations attempted against the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_DELETE_FAILURES("The number of failed delete operations attempted against the LDAP external server."),



  /**
   * The number of successful delete operations attempted against the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_DELETE_SUCCESSES("The number of successful delete operations attempted against the LDAP external server."),



  /**
   * Messages that may provide more information about the health check state and score assigned to the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_HEALTH_CHECK_MESSAGE("Messages that may provide more information about the health check state and score assigned to the LDAP external server."),



  /**
   * The health check score for the LDAP external server which may be used to rank the availability of the server against other servers with the same state.
   */
  INFO_LDAP_EXT_SERVER_DESC_HEALTH_CHECK_SCORE("The health check score for the LDAP external server which may be used to rank the availability of the server against other servers with the same state."),



  /**
   * The health check state for the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_HEALTH_CHECK_STATE("The health check state for the LDAP external server."),



  /**
   * The time the health check information for the LDAP external server was last updated.
   */
  INFO_LDAP_EXT_SERVER_DESC_HEALTH_CHECK_UPDATE_TIME("The time the health check information for the LDAP external server was last updated."),



  /**
   * The DN of the configuration entry for the load-balancing algorithm that uses this LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_LOAD_BALANCING_ALGORITHM_DN("The DN of the configuration entry for the load-balancing algorithm that uses this LDAP external server."),



  /**
   * The total number of modify operations attempted against the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_MODIFY_ATTEMPTS("The total number of modify operations attempted against the LDAP external server."),



  /**
   * The total number of modify DN operations attempted against the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_MODIFY_DN_ATTEMPTS("The total number of modify DN operations attempted against the LDAP external server."),



  /**
   * The number of failed modify DN operations attempted against the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_MODIFY_DN_FAILURES("The number of failed modify DN operations attempted against the LDAP external server."),



  /**
   * The number of successful modify DN operations attempted against the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_MODIFY_DN_SUCCESSES("The number of successful modify DN operations attempted against the LDAP external server."),



  /**
   * The number of failed modify operations attempted against the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_MODIFY_FAILURES("The number of failed modify operations attempted against the LDAP external server."),



  /**
   * The number of successful modify operations attempted against the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_MODIFY_SUCCESSES("The number of successful modify operations attempted against the LDAP external server."),



  /**
   * The number of connections currently available in the connection pool used for processing non-bind operations in the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_NONBIND_AVAILABLE_CONNS("The number of connections currently available in the connection pool used for processing non-bind operations in the LDAP external server."),



  /**
   * The number of failed attempts to check out a connection from the connection pool used for processing non-bind operations in the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_NONBIND_CHECKOUT_FAILED("The number of failed attempts to check out a connection from the connection pool used for processing non-bind operations in the LDAP external server."),



  /**
   * The number of attempts to check out a connection from the connection pool used for processing non-bind operations in the LDAP external server that were required to create a new connection since none were available.
   */
  INFO_LDAP_EXT_SERVER_DESC_NONBIND_CHECKOUT_NEW_CONN("The number of attempts to check out a connection from the connection pool used for processing non-bind operations in the LDAP external server that were required to create a new connection since none were available."),



  /**
   * The number of attempts to check out a connection from the connection pool used for processing non-bind operations in the LDAP external server that were able to retrieve a connection without needing to wait for one to become available.
   */
  INFO_LDAP_EXT_SERVER_DESC_NONBIND_CHECKOUT_NO_WAIT("The number of attempts to check out a connection from the connection pool used for processing non-bind operations in the LDAP external server that were able to retrieve a connection without needing to wait for one to become available."),



  /**
   * The total number of successful attempts to check out a connection from the connection pool used for processing non-bind operations in the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_NONBIND_CHECKOUT_SUCCESS("The total number of successful attempts to check out a connection from the connection pool used for processing non-bind operations in the LDAP external server."),



  /**
   * The number of attempts to check out a connection from the connection pool used for processing non-bind operations in the LDAP external server that were able to retrieve a connection only after waiting for one to become available.
   */
  INFO_LDAP_EXT_SERVER_DESC_NONBIND_CHECKOUT_WITH_WAIT("The number of attempts to check out a connection from the connection pool used for processing non-bind operations in the LDAP external server that were able to retrieve a connection only after waiting for one to become available."),



  /**
   * The number of connections in the connection pool used for processing non-bind operations in the LDAP external server that were closed as defunct (e.g., because an error had occurred which indicated the connection may no longer be valid).
   */
  INFO_LDAP_EXT_SERVER_DESC_NONBIND_CLOSED_DEFUNCT("The number of connections in the connection pool used for processing non-bind operations in the LDAP external server that were closed as defunct (e.g., because an error had occurred which indicated the connection may no longer be valid)."),



  /**
   * The number of connections in the connection pool used for processing non-bind operations in the LDAP external server that were closed as expired (because they had been established for longer the maximum connection age).
   */
  INFO_LDAP_EXT_SERVER_DESC_NONBIND_CLOSED_EXPIRED("The number of connections in the connection pool used for processing non-bind operations in the LDAP external server that were closed as expired (because they had been established for longer the maximum connection age)."),



  /**
   * The number of connections in the connection pool used for processing non-bind operations in the LDAP external server that were closed as unneeded (because the pool already had the maximum number of available connections when the connection was released).
   */
  INFO_LDAP_EXT_SERVER_DESC_NONBIND_CLOSED_UNNEEDED("The number of connections in the connection pool used for processing non-bind operations in the LDAP external server that were closed as unneeded (because the pool already had the maximum number of available connections when the connection was released)."),



  /**
   * The number of failed attempts to create a new connection for use in the connection pool used for processing non-bind operations in the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_NONBIND_CONNECT_FAILED("The number of failed attempts to create a new connection for use in the connection pool used for processing non-bind operations in the LDAP external server."),



  /**
   * The number of successful attempts to create a new connection for use in the connection pool used for processing non-bind operations in the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_NONBIND_CONNECT_SUCCESS("The number of successful attempts to create a new connection for use in the connection pool used for processing non-bind operations in the LDAP external server."),



  /**
   * The maximum number of connections that may be available in the connection pool used for processing non-bind operations in the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_NONBIND_MAX_AVAILABLE_CONNS("The maximum number of connections that may be available in the connection pool used for processing non-bind operations in the LDAP external server."),



  /**
   * The number of times a valid connection was successfully released back to the connection pool used for processing non-bind operations in the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_NONBIND_RELEASED_VALID("The number of times a valid connection was successfully released back to the connection pool used for processing non-bind operations in the LDAP external server."),



  /**
   * The total number of search operations attempted against the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_SEARCH_ATTEMPTS("The total number of search operations attempted against the LDAP external server."),



  /**
   * The number of failed search operations attempted against the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_SEARCH_FAILURES("The number of failed search operations attempted against the LDAP external server."),



  /**
   * The number of successful search operations attempted against the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_SEARCH_SUCCESSES("The number of successful search operations attempted against the LDAP external server."),



  /**
   * The address of the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_SERVER_ADDRESS("The address of the LDAP external server."),



  /**
   * The port of the LDAP external server.
   */
  INFO_LDAP_EXT_SERVER_DESC_SERVER_PORT("The port of the LDAP external server."),



  /**
   * Add Attempts
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_ADD_ATTEMPTS("Add Attempts"),



  /**
   * Add Failures
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_ADD_FAILURES("Add Failures"),



  /**
   * Add Successes
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_ADD_SUCCESSES("Add Successes"),



  /**
   * Bind Attempts
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_BIND_ATTEMPTS("Bind Attempts"),



  /**
   * Bind Pool Available Connections
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_BIND_AVAILABLE_CONNS("Bind Pool Available Connections"),



  /**
   * Bind Pool Failed Checkouts
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_BIND_CHECKOUT_FAILED("Bind Pool Failed Checkouts"),



  /**
   * Bind Pool Successful Checkouts with a New Connection
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_BIND_CHECKOUT_NEW_CONN("Bind Pool Successful Checkouts with a New Connection"),



  /**
   * Bind Pool Successful Checkouts Without Waiting
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_BIND_CHECKOUT_NO_WAIT("Bind Pool Successful Checkouts Without Waiting"),



  /**
   * Bind Pool Total Successful Checkouts
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_BIND_CHECKOUT_SUCCESS("Bind Pool Total Successful Checkouts"),



  /**
   * Bind Pool Successful Checkouts After Waiting
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_BIND_CHECKOUT_WITH_WAIT("Bind Pool Successful Checkouts After Waiting"),



  /**
   * Bind Pool Connections Closed as Defunct
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_BIND_CLOSED_DEFUNCT("Bind Pool Connections Closed as Defunct"),



  /**
   * Bind Pool Connections Closed as Expired
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_BIND_CLOSED_EXPIRED("Bind Pool Connections Closed as Expired"),



  /**
   * Bind Pool Connections Closed as Unneeded
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_BIND_CLOSED_UNNEEDED("Bind Pool Connections Closed as Unneeded"),



  /**
   * Bind Pool Failed Connection Attempts
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_BIND_CONNECT_FAILED("Bind Pool Failed Connection Attempts"),



  /**
   * Bind Pool Successful Connection Attempts
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_BIND_CONNECT_SUCCESS("Bind Pool Successful Connection Attempts"),



  /**
   * Bind Failures
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_BIND_FAILURES("Bind Failures"),



  /**
   * Bind Pool Maximum Available Connections
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_BIND_MAX_AVAILABLE_CONNS("Bind Pool Maximum Available Connections"),



  /**
   * Bind Pool Connections Released as Valid
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_BIND_RELEASED_VALID("Bind Pool Connections Released as Valid"),



  /**
   * Bind Successes
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_BIND_SUCCESSES("Bind Successes"),



  /**
   * Common Pool Available Connections
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_COMMON_AVAILABLE_CONNS("Common Pool Available Connections"),



  /**
   * Common Pool Failed Checkouts
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_COMMON_CHECKOUT_FAILED("Common Pool Failed Checkouts"),



  /**
   * Common Pool Successful Checkouts with a New Connection
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_COMMON_CHECKOUT_NEW_CONN("Common Pool Successful Checkouts with a New Connection"),



  /**
   * Common Pool Successful Checkouts Without Waiting
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_COMMON_CHECKOUT_NO_WAIT("Common Pool Successful Checkouts Without Waiting"),



  /**
   * Common Pool Total Successful Checkouts
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_COMMON_CHECKOUT_SUCCESS("Common Pool Total Successful Checkouts"),



  /**
   * Common Pool Successful Checkouts After Waiting
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_COMMON_CHECKOUT_WITH_WAIT("Common Pool Successful Checkouts After Waiting"),



  /**
   * Common Pool Connections Closed as Defunct
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_COMMON_CLOSED_DEFUNCT("Common Pool Connections Closed as Defunct"),



  /**
   * Common Pool Connections Closed as Expired
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_COMMON_CLOSED_EXPIRED("Common Pool Connections Closed as Expired"),



  /**
   * Common Pool Connections Closed as Unneeded
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_COMMON_CLOSED_UNNEEDED("Common Pool Connections Closed as Unneeded"),



  /**
   * Common Pool Failed Connection Attempts
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_COMMON_CONNECT_FAILED("Common Pool Failed Connection Attempts"),



  /**
   * Common Pool Successful Connection Attempts
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_COMMON_CONNECT_SUCCESS("Common Pool Successful Connection Attempts"),



  /**
   * Common Pool Maximum Available Connections
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_COMMON_MAX_AVAILABLE_CONNS("Common Pool Maximum Available Connections"),



  /**
   * Common Pool Connections Released as Valid
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_COMMON_RELEASED_VALID("Common Pool Connections Released as Valid"),



  /**
   * Communication Security Mechanism
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_COMMUNICATION_SECURITY("Communication Security Mechanism"),



  /**
   * Compare Attempts
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_COMPARE_ATTEMPTS("Compare Attempts"),



  /**
   * Compare Failures
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_COMPARE_FAILURES("Compare Failures"),



  /**
   * Compare Successes
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_COMPARE_SUCCESSES("Compare Successes"),



  /**
   * Delete Attempts
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_DELETE_ATTEMPTS("Delete Attempts"),



  /**
   * Delete Failures
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_DELETE_FAILURES("Delete Failures"),



  /**
   * Delete Successes
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_DELETE_SUCCESSES("Delete Successes"),



  /**
   * Health Check Message
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_HEALTH_CHECK_MESSAGE("Health Check Message"),



  /**
   * Health Check Score
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_HEALTH_CHECK_SCORE("Health Check Score"),



  /**
   * Health Check State
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_HEALTH_CHECK_STATE("Health Check State"),



  /**
   * Health Check Update Time
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_HEALTH_CHECK_UPDATE_TIME("Health Check Update Time"),



  /**
   * Load-Balancing Algorithm DN
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_LOAD_BALANCING_ALGORITHM_DN("Load-Balancing Algorithm DN"),



  /**
   * Modify Attempts
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_MODIFY_ATTEMPTS("Modify Attempts"),



  /**
   * Modify DN Attempts
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_MODIFY_DN_ATTEMPTS("Modify DN Attempts"),



  /**
   * Modify DN Failures
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_MODIFY_DN_FAILURES("Modify DN Failures"),



  /**
   * Modify DN Successes
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_MODIFY_DN_SUCCESSES("Modify DN Successes"),



  /**
   * Modify Failures
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_MODIFY_FAILURES("Modify Failures"),



  /**
   * Modify Successes
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_MODIFY_SUCCESSES("Modify Successes"),



  /**
   * Non-Bind Pool Available Connections
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_NONBIND_AVAILABLE_CONNS("Non-Bind Pool Available Connections"),



  /**
   * Non-Bind Pool Failed Checkouts
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_NONBIND_CHECKOUT_FAILED("Non-Bind Pool Failed Checkouts"),



  /**
   * Non-Bind Pool Successful Checkouts with a New Connection
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_NONBIND_CHECKOUT_NEW_CONN("Non-Bind Pool Successful Checkouts with a New Connection"),



  /**
   * Non-Bind Pool Successful Checkouts Without Waiting
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_NONBIND_CHECKOUT_NO_WAIT("Non-Bind Pool Successful Checkouts Without Waiting"),



  /**
   * Non-Bind Pool Total Successful Checkouts
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_NONBIND_CHECKOUT_SUCCESS("Non-Bind Pool Total Successful Checkouts"),



  /**
   * Non-Bind Pool Successful Checkouts After Waiting
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_NONBIND_CHECKOUT_WITH_WAIT("Non-Bind Pool Successful Checkouts After Waiting"),



  /**
   * Non-Bind Pool Connections Closed as Defunct
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_NONBIND_CLOSED_DEFUNCT("Non-Bind Pool Connections Closed as Defunct"),



  /**
   * Non-Bind Pool Connections Closed as Expired
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_NONBIND_CLOSED_EXPIRED("Non-Bind Pool Connections Closed as Expired"),



  /**
   * Non-Bind Pool Connections Closed as Unneeded
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_NONBIND_CLOSED_UNNEEDED("Non-Bind Pool Connections Closed as Unneeded"),



  /**
   * Non-Bind Pool Failed Connection Attempts
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_NONBIND_CONNECT_FAILED("Non-Bind Pool Failed Connection Attempts"),



  /**
   * Non-Bind Pool Successful Connection Attempts
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_NONBIND_CONNECT_SUCCESS("Non-Bind Pool Successful Connection Attempts"),



  /**
   * Non-Bind Pool Maximum Available Connections
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_NONBIND_MAX_AVAILABLE_CONNS("Non-Bind Pool Maximum Available Connections"),



  /**
   * Non-Bind Pool Connections Released as Valid
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_NONBIND_RELEASED_VALID("Non-Bind Pool Connections Released as Valid"),



  /**
   * Search Attempts
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_SEARCH_ATTEMPTS("Search Attempts"),



  /**
   * Search Failures
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_SEARCH_FAILURES("Search Failures"),



  /**
   * Search Successes
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_SEARCH_SUCCESSES("Search Successes"),



  /**
   * Server Address
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_SERVER_ADDRESS("Server Address"),



  /**
   * Server Port
   */
  INFO_LDAP_EXT_SERVER_DISPNAME_SERVER_PORT("Server Port"),



  /**
   * Information about an LDAP external server used by a load-balancing algorithm.
   */
  INFO_LDAP_EXT_SERVER_MONITOR_DESC("Information about an LDAP external server used by a load-balancing algorithm."),



  /**
   * LDAP External Server
   */
  INFO_LDAP_EXT_SERVER_MONITOR_DISPNAME("LDAP External Server"),



  /**
   * The total number of abandon requests received from clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_ABANDON_REQUESTS("The total number of abandon requests received from clients of the LDAP connection handler."),



  /**
   * The total number of add requests received from clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_ADD_REQUESTS("The total number of add requests received from clients of the LDAP connection handler."),



  /**
   * The total number of add responses sent to clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_ADD_RESPONSES("The total number of add responses sent to clients of the LDAP connection handler."),



  /**
   * The total number of bind requests received from clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_BIND_REQUESTS("The total number of bind requests received from clients of the LDAP connection handler."),



  /**
   * The total number of bind responses sent to clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_BIND_RESPONSES("The total number of bind responses sent to clients of the LDAP connection handler."),



  /**
   * The total number of request bytes read from clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_BYTES_READ("The total number of request bytes read from clients of the LDAP connection handler."),



  /**
   * The total number of response bytes written to clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_BYTES_WRITTEN("The total number of response bytes written to clients of the LDAP connection handler."),



  /**
   * The total number of compare requests received from clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_COMPARE_REQUESTS("The total number of compare requests received from clients of the LDAP connection handler."),



  /**
   * The total number of compare responses sent to clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_COMPARE_RESPONSES("The total number of compare responses sent to clients of the LDAP connection handler."),



  /**
   * The number of connections to the LDAP connection handler that have been closed.
   */
  INFO_LDAP_STATS_DESC_CONNECTIONS_CLOSED("The number of connections to the LDAP connection handler that have been closed."),



  /**
   * The number of connections that have been established to the LDAP connection since it was started.
   */
  INFO_LDAP_STATS_DESC_CONNECTIONS_ESTABLISHED("The number of connections that have been established to the LDAP connection since it was started."),



  /**
   * The total number of delete requests received from clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_DELETE_REQUESTS("The total number of delete requests received from clients of the LDAP connection handler."),



  /**
   * The total number of delete responses sent to clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_DELETE_RESPONSES("The total number of delete responses sent to clients of the LDAP connection handler."),



  /**
   * The total number of extended requests received from clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_EXTENDED_REQUESTS("The total number of extended requests received from clients of the LDAP connection handler."),



  /**
   * The total number of extended responses sent to clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_EXTENDED_RESPONSES("The total number of extended responses sent to clients of the LDAP connection handler."),



  /**
   * The total number of LDAP messages read from clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_LDAP_MESSAGES_READ("The total number of LDAP messages read from clients of the LDAP connection handler."),



  /**
   * The total number of LDAP messages written to clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_LDAP_MESSAGES_WRITTEN("The total number of LDAP messages written to clients of the LDAP connection handler."),



  /**
   * The total number of modify DN requests received from clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_MODIFY_DN_REQUESTS("The total number of modify DN requests received from clients of the LDAP connection handler."),



  /**
   * The total number of modify DN responses sent to clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_MODIFY_DN_RESPONSES("The total number of modify DN responses sent to clients of the LDAP connection handler."),



  /**
   * The total number of modify requests received from clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_MODIFY_REQUESTS("The total number of modify requests received from clients of the LDAP connection handler."),



  /**
   * The total number of modify responses sent to clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_MODIFY_RESPONSES("The total number of modify responses sent to clients of the LDAP connection handler."),



  /**
   * The total number of operations from the LDAP connection handler for which the server abandoned processing before it was completed.
   */
  INFO_LDAP_STATS_DESC_OPS_ABANDONED("The total number of operations from the LDAP connection handler for which the server abandoned processing before it was completed."),



  /**
   * The total number of requests from the LDAP connection handler for which the server has completed processing.
   */
  INFO_LDAP_STATS_DESC_OPS_COMPLETED("The total number of requests from the LDAP connection handler for which the server has completed processing."),



  /**
   * The total number of requests from the LDAP connection handler that the server has at least started to process.
   */
  INFO_LDAP_STATS_DESC_OPS_INITIATED("The total number of requests from the LDAP connection handler that the server has at least started to process."),



  /**
   * The total number of search result done responses sent to clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_SEARCH_DONE_RESPONSES("The total number of search result done responses sent to clients of the LDAP connection handler."),



  /**
   * The total number of search result entry responses sent to clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_SEARCH_ENTRY_RESPONSES("The total number of search result entry responses sent to clients of the LDAP connection handler."),



  /**
   * The total number of search result reference responses sent to clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_SEARCH_REFERENCE_RESPONSES("The total number of search result reference responses sent to clients of the LDAP connection handler."),



  /**
   * The total number of search requests received from clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_SEARCH_REQUESTS("The total number of search requests received from clients of the LDAP connection handler."),



  /**
   * The total number of unbind requests received from clients of the LDAP connection handler.
   */
  INFO_LDAP_STATS_DESC_UNBIND_REQUESTS("The total number of unbind requests received from clients of the LDAP connection handler."),



  /**
   * Abandon Requests Received
   */
  INFO_LDAP_STATS_DISPNAME_ABANDON_REQUESTS("Abandon Requests Received"),



  /**
   * Add Requests Received
   */
  INFO_LDAP_STATS_DISPNAME_ADD_REQUESTS("Add Requests Received"),



  /**
   * Add Responses Sent
   */
  INFO_LDAP_STATS_DISPNAME_ADD_RESPONSES("Add Responses Sent"),



  /**
   * Bind Requests Received
   */
  INFO_LDAP_STATS_DISPNAME_BIND_REQUESTS("Bind Requests Received"),



  /**
   * Bind Responses Sent
   */
  INFO_LDAP_STATS_DISPNAME_BIND_RESPONSES("Bind Responses Sent"),



  /**
   * Bytes Read
   */
  INFO_LDAP_STATS_DISPNAME_BYTES_READ("Bytes Read"),



  /**
   * Bytes Written
   */
  INFO_LDAP_STATS_DISPNAME_BYTES_WRITTEN("Bytes Written"),



  /**
   * Compare Requests Received
   */
  INFO_LDAP_STATS_DISPNAME_COMPARE_REQUESTS("Compare Requests Received"),



  /**
   * Compare Responses Sent
   */
  INFO_LDAP_STATS_DISPNAME_COMPARE_RESPONSES("Compare Responses Sent"),



  /**
   * Connections Closed
   */
  INFO_LDAP_STATS_DISPNAME_CONNECTIONS_CLOSED("Connections Closed"),



  /**
   * Connections Established
   */
  INFO_LDAP_STATS_DISPNAME_CONNECTIONS_ESTABLISHED("Connections Established"),



  /**
   * Delete Requests Received
   */
  INFO_LDAP_STATS_DISPNAME_DELETE_REQUESTS("Delete Requests Received"),



  /**
   * Delete Responses Sent
   */
  INFO_LDAP_STATS_DISPNAME_DELETE_RESPONSES("Delete Responses Sent"),



  /**
   * Extended Requests Received
   */
  INFO_LDAP_STATS_DISPNAME_EXTENDED_REQUESTS("Extended Requests Received"),



  /**
   * Extended Responses Sent
   */
  INFO_LDAP_STATS_DISPNAME_EXTENDED_RESPONSES("Extended Responses Sent"),



  /**
   * LDAP Messages Read
   */
  INFO_LDAP_STATS_DISPNAME_LDAP_MESSAGES_READ("LDAP Messages Read"),



  /**
   * LDAP Messages Written
   */
  INFO_LDAP_STATS_DISPNAME_LDAP_MESSAGES_WRITTEN("LDAP Messages Written"),



  /**
   * Modify DN Requests Received
   */
  INFO_LDAP_STATS_DISPNAME_MODIFY_DN_REQUESTS("Modify DN Requests Received"),



  /**
   * Modify DN Responses Sent
   */
  INFO_LDAP_STATS_DISPNAME_MODIFY_DN_RESPONSES("Modify DN Responses Sent"),



  /**
   * Modify Requests Received
   */
  INFO_LDAP_STATS_DISPNAME_MODIFY_REQUESTS("Modify Requests Received"),



  /**
   * Modify Responses Sent
   */
  INFO_LDAP_STATS_DISPNAME_MODIFY_RESPONSES("Modify Responses Sent"),



  /**
   * Operations Abandoned
   */
  INFO_LDAP_STATS_DISPNAME_OPS_ABANDONED("Operations Abandoned"),



  /**
   * Operations Completed
   */
  INFO_LDAP_STATS_DISPNAME_OPS_COMPLETED("Operations Completed"),



  /**
   * Operations Initiated
   */
  INFO_LDAP_STATS_DISPNAME_OPS_INITIATED("Operations Initiated"),



  /**
   * Search Done Responses Sent
   */
  INFO_LDAP_STATS_DISPNAME_SEARCH_DONE_RESPONSES("Search Done Responses Sent"),



  /**
   * Search Entry Responses Sent
   */
  INFO_LDAP_STATS_DISPNAME_SEARCH_ENTRY_RESPONSES("Search Entry Responses Sent"),



  /**
   * Search Reference Responses Sent
   */
  INFO_LDAP_STATS_DISPNAME_SEARCH_REFERENCE_RESPONSES("Search Reference Responses Sent"),



  /**
   * Search Requests Received
   */
  INFO_LDAP_STATS_DISPNAME_SEARCH_REQUESTS("Search Requests Received"),



  /**
   * Unbind Requests Received
   */
  INFO_LDAP_STATS_DISPNAME_UNBIND_REQUESTS("Unbind Requests Received"),



  /**
   * Provides statistics about the interaction that the associated LDAP connection handler has had with clients.
   */
  INFO_LDAP_STATS_MONITOR_DESC("Provides statistics about the interaction that the associated LDAP connection handler has had with clients."),



  /**
   * LDAP Connection Handler Statistics
   */
  INFO_LDAP_STATS_MONITOR_DISPNAME("LDAP Connection Handler Statistics"),



  /**
   * The name of the load-balancing algorithm configuration object.
   */
  INFO_LOAD_BALANCING_ALGORITHM_DESC_ALGORITHM_NAME("The name of the load-balancing algorithm configuration object."),



  /**
   * The DN of the configuration entry for the load-balancing algorithm.
   */
  INFO_LOAD_BALANCING_ALGORITHM_DESC_CONFIG_ENTRY_DN("The DN of the configuration entry for the load-balancing algorithm."),



  /**
   * The aggregate health check state for the load-balancing algorithm.
   */
  INFO_LOAD_BALANCING_ALGORITHM_DESC_HEALTH_CHECK_STATE("The aggregate health check state for the load-balancing algorithm."),



  /**
   * The aggregate health check state for local servers for the load-balancing algorithm.
   */
  INFO_LOAD_BALANCING_ALGORITHM_DESC_L_HEALTH_CHECK_STATE("The aggregate health check state for local servers for the load-balancing algorithm."),



  /**
   * The aggregate health check state for non-local servers for the load-balancing algorithm.
   */
  INFO_LOAD_BALANCING_ALGORITHM_DESC_NL_HEALTH_CHECK_STATE("The aggregate health check state for non-local servers for the load-balancing algorithm."),



  /**
   * The number of LDAP external servers associated with the load-balancing algorithm with a health check state of AVAILABLE.
   */
  INFO_LOAD_BALANCING_ALGORITHM_DESC_NUM_AVAILABLE("The number of LDAP external servers associated with the load-balancing algorithm with a health check state of AVAILABLE."),



  /**
   * The number of LDAP external servers associated with the load-balancing algorithm with a health check state of DEGRADED.
   */
  INFO_LOAD_BALANCING_ALGORITHM_DESC_NUM_DEGRADED("The number of LDAP external servers associated with the load-balancing algorithm with a health check state of DEGRADED."),



  /**
   * The number of LDAP external servers associated with the load-balancing algorithm with a health check state of UNAVAILABLE.
   */
  INFO_LOAD_BALANCING_ALGORITHM_DESC_NUM_UNAVAILABLE("The number of LDAP external servers associated with the load-balancing algorithm with a health check state of UNAVAILABLE."),



  /**
   * Information about the health check states of individual LDAP external servers associated with the load-balancing algorithm.
   */
  INFO_LOAD_BALANCING_ALGORITHM_DESC_SERVER_DATA("Information about the health check states of individual LDAP external servers associated with the load-balancing algorithm."),



  /**
   * Algorithm Name
   */
  INFO_LOAD_BALANCING_ALGORITHM_DISPNAME_ALGORITHM_NAME("Algorithm Name"),



  /**
   * Config Entry DN
   */
  INFO_LOAD_BALANCING_ALGORITHM_DISPNAME_CONFIG_ENTRY_DN("Config Entry DN"),



  /**
   * Health Check State
   */
  INFO_LOAD_BALANCING_ALGORITHM_DISPNAME_HEALTH_CHECK_STATE("Health Check State"),



  /**
   * Local Servers Health Check State
   */
  INFO_LOAD_BALANCING_ALGORITHM_DISPNAME_L_HEALTH_CHECK_STATE("Local Servers Health Check State"),



  /**
   * Non-Local Servers Health Check State
   */
  INFO_LOAD_BALANCING_ALGORITHM_DISPNAME_NL_HEALTH_CHECK_STATE("Non-Local Servers Health Check State"),



  /**
   * Number of Available Servers
   */
  INFO_LOAD_BALANCING_ALGORITHM_DISPNAME_NUM_AVAILABLE("Number of Available Servers"),



  /**
   * Number of Degraded Servers
   */
  INFO_LOAD_BALANCING_ALGORITHM_DISPNAME_NUM_DEGRADED("Number of Degraded Servers"),



  /**
   * Number of Unavailable Servers
   */
  INFO_LOAD_BALANCING_ALGORITHM_DISPNAME_NUM_UNAVAILABLE("Number of Unavailable Servers"),



  /**
   * Server Availability Data
   */
  INFO_LOAD_BALANCING_ALGORITHM_DISPNAME_SERVER_DATA("Server Availability Data"),



  /**
   * Provides information about the health of a load-balancing algorithm and the servers associated with it.
   */
  INFO_LOAD_BALANCING_ALGORITHM_MONITOR_DESC("Provides information about the health of a load-balancing algorithm and the servers associated with it."),



  /**
   * Load-Balancing Algorithm
   */
  INFO_LOAD_BALANCING_ALGORITHM_MONITOR_DISPNAME("Load-Balancing Algorithm"),



  /**
   * The average duration (in milliseconds) for collections performed by the {0} collector.
   */
  INFO_MEMORY_USAGE_DESC_AVERAGE_COLLECTION_DURATION("The average duration (in milliseconds) for collections performed by the {0} collector."),



  /**
   * The amount of memory (in bytes) used by the {0} memory pool after the last garbage collection.
   */
  INFO_MEMORY_USAGE_DESC_BYTES_USED_AFTER_COLLECTION("The amount of memory (in bytes) used by the {0} memory pool after the last garbage collection."),



  /**
   * The percentage of the committed amount of tenured memory that is held by memory consumers (assuming that all consumer memory is held in the tenured generation).
   */
  INFO_MEMORY_USAGE_DESC_CONSUMERS_AS_PCT_OF_COMMITTED("The percentage of the committed amount of tenured memory that is held by memory consumers (assuming that all consumer memory is held in the tenured generation)."),



  /**
   * The percentage of the maximum amount of tenured memory that is held by memory consumers (assuming that all consumer memory is held in the tenured generation).
   */
  INFO_MEMORY_USAGE_DESC_CONSUMERS_AS_PCT_OF_MAX("The percentage of the maximum amount of tenured memory that is held by memory consumers (assuming that all consumer memory is held in the tenured generation)."),



  /**
   * The current amount of memory (in bytes) used by the {0} memory pool.
   */
  INFO_MEMORY_USAGE_DESC_CURRENT_BYTES_USED("The current amount of memory (in bytes) used by the {0} memory pool."),



  /**
   * The amount of memory in megabytes that is currently allocated by the JVM.
   */
  INFO_MEMORY_USAGE_DESC_CURRENT_MEM("The amount of memory in megabytes that is currently allocated by the JVM."),



  /**
   * The number of pauses of various durations detected by the Directory Server.  Values will be formatted as a duration in millisecond followed by an equal sign and the number of pauses detected of at least that duration.
   */
  INFO_MEMORY_USAGE_DESC_DETECTED_PAUSES("The number of pauses of various durations detected by the Directory Server.  Values will be formatted as a duration in millisecond followed by an equal sign and the number of pauses detected of at least that duration."),



  /**
   * The amount of memory in megabytes that has been allocated by the JVM which is currently unused.
   */
  INFO_MEMORY_USAGE_DESC_FREE_MEM("The amount of memory in megabytes that has been allocated by the JVM which is currently unused."),



  /**
   * The names of the garbage collectors in use in the Directory Server JVM.
   */
  INFO_MEMORY_USAGE_DESC_GC_NAMES("The names of the garbage collectors in use in the Directory Server JVM."),



  /**
   * The maximum amount of memory in megabytes that can be allocated by the JVM.
   */
  INFO_MEMORY_USAGE_DESC_MAX_MEM("The maximum amount of memory in megabytes that can be allocated by the JVM."),



  /**
   * The duration in milliseconds of the longest pause detected by the Directory Server.
   */
  INFO_MEMORY_USAGE_DESC_MAX_PAUSE_TIME("The duration in milliseconds of the longest pause detected by the Directory Server."),



  /**
   * The names of the memory pools in use within the Directory Server JVM.
   */
  INFO_MEMORY_USAGE_DESC_MEMORY_POOLS("The names of the memory pools in use within the Directory Server JVM."),



  /**
   * The amount of non-heap memory in bytes consumed by the JVM.
   */
  INFO_MEMORY_USAGE_DESC_NON_HEAP_MEMORY("The amount of non-heap memory in bytes consumed by the JVM."),



  /**
   * The duration (in milliseconds) of the most recent collection by the {0} collector.
   */
  INFO_MEMORY_USAGE_DESC_RECENT_COLLECTION_DURATION("The duration (in milliseconds) of the most recent collection by the {0} collector."),



  /**
   * The percent of memory currently allocated by the JVM that is actually in use for holding Java objects.
   */
  INFO_MEMORY_USAGE_DESC_RESERVED_PCT("The percent of memory currently allocated by the JVM that is actually in use for holding Java objects."),



  /**
   * The total number of garbage collections performed by the {0} collector.
   */
  INFO_MEMORY_USAGE_DESC_TOTAL_COLLECTION_COUNT("The total number of garbage collections performed by the {0} collector."),



  /**
   * The total duration (in milliseconds) for all collections performed by the {0} collector.
   */
  INFO_MEMORY_USAGE_DESC_TOTAL_COLLECTION_DURATION("The total duration (in milliseconds) for all collections performed by the {0} collector."),



  /**
   * The total amount of memory in bytes held by all memory consumers.
   */
  INFO_MEMORY_USAGE_DESC_TOTAL_CONSUMER_MEMORY("The total amount of memory in bytes held by all memory consumers."),



  /**
   * The amount of memory in megabytes that has been allocated the JVM which is actually in use for storing Java objects.
   */
  INFO_MEMORY_USAGE_DESC_USED_MEM("The amount of memory in megabytes that has been allocated the JVM which is actually in use for storing Java objects."),



  /**
   * Average Duration for {0} Collections
   */
  INFO_MEMORY_USAGE_DISPNAME_AVERAGE_COLLECTION_DURATION("Average Duration for {0} Collections"),



  /**
   * Size of Memory Pool {0} After Last Collection
   */
  INFO_MEMORY_USAGE_DISPNAME_BYTES_USED_AFTER_COLLECTION("Size of Memory Pool {0} After Last Collection"),



  /**
   * Percentage of Committed Tenured Memory Used by Memory Consumers
   */
  INFO_MEMORY_USAGE_DISPNAME_CONSUMERS_AS_PCT_OF_COMMITTED("Percentage of Committed Tenured Memory Used by Memory Consumers"),



  /**
   * Percentage of Maximum Tenured Memory Used by Memory Consumers
   */
  INFO_MEMORY_USAGE_DISPNAME_CONSUMERS_AS_PCT_OF_MAX("Percentage of Maximum Tenured Memory Used by Memory Consumers"),



  /**
   * Current Size of Memory Pool {0}
   */
  INFO_MEMORY_USAGE_DISPNAME_CURRENT_BYTES_USED("Current Size of Memory Pool {0}"),



  /**
   * Current Reserved Memory (MB)
   */
  INFO_MEMORY_USAGE_DISPNAME_CURRENT_MEM("Current Reserved Memory (MB)"),



  /**
   * Detected Pause Counts by Duration
   */
  INFO_MEMORY_USAGE_DISPNAME_DETECTED_PAUSES("Detected Pause Counts by Duration"),



  /**
   * Free Reserved Memory (MB)
   */
  INFO_MEMORY_USAGE_DISPNAME_FREE_MEM("Free Reserved Memory (MB)"),



  /**
   * Garbage Collector Names
   */
  INFO_MEMORY_USAGE_DISPNAME_GC_NAMES("Garbage Collector Names"),



  /**
   * Maximum Reservable Memory (MB)
   */
  INFO_MEMORY_USAGE_DISPNAME_MAX_MEM("Maximum Reservable Memory (MB)"),



  /**
   * Maximum Detected Pause Duration
   */
  INFO_MEMORY_USAGE_DISPNAME_MAX_PAUSE_TIME("Maximum Detected Pause Duration"),



  /**
   * Memory Pool Names
   */
  INFO_MEMORY_USAGE_DISPNAME_MEMORY_POOLS("Memory Pool Names"),



  /**
   * Non-Heap Memory Bytes Used
   */
  INFO_MEMORY_USAGE_DISPNAME_NON_HEAP_MEMORY("Non-Heap Memory Bytes Used"),



  /**
   * Most Recent {0} Collection Duration
   */
  INFO_MEMORY_USAGE_DISPNAME_RECENT_COLLECTION_DURATION("Most Recent {0} Collection Duration"),



  /**
   * Reserved Memory Percent Full
   */
  INFO_MEMORY_USAGE_DISPNAME_RESERVED_PCT("Reserved Memory Percent Full"),



  /**
   * Total Collections for the {0} Collector
   */
  INFO_MEMORY_USAGE_DISPNAME_TOTAL_COLLECTION_COUNT("Total Collections for the {0} Collector"),



  /**
   * Total Duration for the {0} Collector
   */
  INFO_MEMORY_USAGE_DISPNAME_TOTAL_COLLECTION_DURATION("Total Duration for the {0} Collector"),



  /**
   * Total Bytes Used by Memory Consumers
   */
  INFO_MEMORY_USAGE_DISPNAME_TOTAL_CONSUMER_MEMORY("Total Bytes Used by Memory Consumers"),



  /**
   * Used Reserved Memory (MB)
   */
  INFO_MEMORY_USAGE_DISPNAME_USED_MEM("Used Reserved Memory (MB)"),



  /**
   * Provides information about the memory consumption and garbage collection activity of the Java Virtual Machine in which the Directory Server is running.
   */
  INFO_MEMORY_USAGE_MONITOR_DESC("Provides information about the memory consumption and garbage collection activity of the Java Virtual Machine in which the Directory Server is running."),



  /**
   * JVM Memory Usage
   */
  INFO_MEMORY_USAGE_MONITOR_DISPNAME("JVM Memory Usage"),



  /**
   * The current value for the gauge.
   */
  INFO_NUMERIC_GAUGE_DESC_CURRENT_VALUE("The current value for the gauge."),



  /**
   * The maximum value observed for the gauge.
   */
  INFO_NUMERIC_GAUGE_DESC_MAXIMUM_VALUE("The maximum value observed for the gauge."),



  /**
   * The minimum value observed for the gauge.
   */
  INFO_NUMERIC_GAUGE_DESC_MINIMUM_VALUE("The minimum value observed for the gauge."),



  /**
   * The values observed for the gauge.
   */
  INFO_NUMERIC_GAUGE_DESC_OBSERVED_VALUES("The values observed for the gauge."),



  /**
   * The previous value for the gauge.
   */
  INFO_NUMERIC_GAUGE_DESC_PREVIOUS_VALUE("The previous value for the gauge."),



  /**
   * Current Value
   */
  INFO_NUMERIC_GAUGE_DISPNAME_CURRENT_VALUE("Current Value"),



  /**
   * Maximum Value
   */
  INFO_NUMERIC_GAUGE_DISPNAME_MAXIMUM_VALUE("Maximum Value"),



  /**
   * Minimum Value
   */
  INFO_NUMERIC_GAUGE_DISPNAME_MINIMUM_VALUE("Minimum Value"),



  /**
   * Observed Values
   */
  INFO_NUMERIC_GAUGE_DISPNAME_OBSERVED_VALUES("Observed Values"),



  /**
   * Previous Value
   */
  INFO_NUMERIC_GAUGE_DISPNAME_PREVIOUS_VALUE("Previous Value"),



  /**
   * Provides information about the state of a numeric gauge defined in the server.
   */
  INFO_NUMERIC_GAUGE_MONITOR_DESC("Provides information about the state of a numeric gauge defined in the server."),



  /**
   * Numeric Gauge
   */
  INFO_NUMERIC_GAUGE_MONITOR_DISPNAME("Numeric Gauge"),



  /**
   * The name of the application with which the per-application processing time histogram monitor entry is associated.
   */
  INFO_PER_APP_PROCESSING_TIME_DESC_APP_NAME("The name of the application with which the per-application processing time histogram monitor entry is associated."),



  /**
   * Application Name
   */
  INFO_PER_APP_PROCESSING_TIME_DISPNAME_APP_NAME("Application Name"),



  /**
   * Categorizes operation processing times into a number of user-defined categories for a specified application.
   */
  INFO_PER_APP_PROCESSING_TIME_MONITOR_DESC("Categorizes operation processing times into a number of user-defined categories for a specified application."),



  /**
   * Per-Application Processing Time Histogram
   */
  INFO_PER_APP_PROCESSING_TIME_MONITOR_DISPNAME("Per-Application Processing Time Histogram"),



  /**
   * The percentage of add operations with processing times less than {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_ADD_AGGR_PCT("The percentage of add operations with processing times less than {0,number,0}ms."),



  /**
   * The number of add operations with processing times between {0,number,0}ms and {1,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_ADD_COUNT("The number of add operations with processing times between {0,number,0}ms and {1,number,0}ms."),



  /**
   * The number of add operations with processing times of at least {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_ADD_COUNT_LAST("The number of add operations with processing times of at least {0,number,0}ms."),



  /**
   * The percentage of add operations with processing times between {0,number,0}ms and {1,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_ADD_PCT("The percentage of add operations with processing times between {0,number,0}ms and {1,number,0}ms."),



  /**
   * The percentage of add operations with processing times of at least {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_ADD_PCT_LAST("The percentage of add operations with processing times of at least {0,number,0}ms."),



  /**
   * The total number of add operations processed in the server.
   */
  INFO_PROCESSING_TIME_DESC_ADD_TOTAL_COUNT("The total number of add operations processed in the server."),



  /**
   * The average response time in milliseconds for all add operations processed within the server.
   */
  INFO_PROCESSING_TIME_DESC_ADD_TOTAL_TIME("The average response time in milliseconds for all add operations processed within the server."),



  /**
   * The percentage of operations of all types with processing times less than {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_ALL_AGGR_PCT("The percentage of operations of all types with processing times less than {0,number,0}ms."),



  /**
   * The number of operations of all types with processing times between {0,number,0}ms and {1,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_ALL_COUNT("The number of operations of all types with processing times between {0,number,0}ms and {1,number,0}ms."),



  /**
   * The number of operations of all types with processing times of at least {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_ALL_COUNT_LAST("The number of operations of all types with processing times of at least {0,number,0}ms."),



  /**
   * The percentage of operations of all types with processing times between {0,number,0}ms and {1,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_ALL_PCT("The percentage of operations of all types with processing times between {0,number,0}ms and {1,number,0}ms."),



  /**
   * The percentage of operations of all types with processing times of at least {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_ALL_PCT_LAST("The percentage of operations of all types with processing times of at least {0,number,0}ms."),



  /**
   * The total number of operations of all types processed in the server.
   */
  INFO_PROCESSING_TIME_DESC_ALL_TOTAL_COUNT("The total number of operations of all types processed in the server."),



  /**
   * The overall average response time in milliseconds for all operations processed within the server.
   */
  INFO_PROCESSING_TIME_DESC_ALL_TOTAL_TIME("The overall average response time in milliseconds for all operations processed within the server."),



  /**
   * The percentage of bind operations with processing times less than {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_BIND_AGGR_PCT("The percentage of bind operations with processing times less than {0,number,0}ms."),



  /**
   * The number of bind operations with processing times between {0,number,0}ms and {1,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_BIND_COUNT("The number of bind operations with processing times between {0,number,0}ms and {1,number,0}ms."),



  /**
   * The number of bind operations with processing times of at least {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_BIND_COUNT_LAST("The number of bind operations with processing times of at least {0,number,0}ms."),



  /**
   * The percentage of bind operations with processing times between {0,number,0}ms and {1,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_BIND_PCT("The percentage of bind operations with processing times between {0,number,0}ms and {1,number,0}ms."),



  /**
   * The percentage of bind operations with processing times of at least {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_BIND_PCT_LAST("The percentage of bind operations with processing times of at least {0,number,0}ms."),



  /**
   * The total number of bind operations processed in the server.
   */
  INFO_PROCESSING_TIME_DESC_BIND_TOTAL_COUNT("The total number of bind operations processed in the server."),



  /**
   * The average response time in milliseconds for all bind operations processed within the server.
   */
  INFO_PROCESSING_TIME_DESC_BIND_TOTAL_TIME("The average response time in milliseconds for all bind operations processed within the server."),



  /**
   * The percentage of compare operations with processing times less than {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_COMPARE_AGGR_PCT("The percentage of compare operations with processing times less than {0,number,0}ms."),



  /**
   * The number of compare operations with processing times between {0,number,0}ms and {1,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_COMPARE_COUNT("The number of compare operations with processing times between {0,number,0}ms and {1,number,0}ms."),



  /**
   * The number of compare operations with processing times of at least {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_COMPARE_COUNT_LAST("The number of compare operations with processing times of at least {0,number,0}ms."),



  /**
   * The percentage of compare operations with processing times between {0,number,0}ms and {1,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_COMPARE_PCT("The percentage of compare operations with processing times between {0,number,0}ms and {1,number,0}ms."),



  /**
   * The percentage of compare operations with processing times of at least {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_COMPARE_PCT_LAST("The percentage of compare operations with processing times of at least {0,number,0}ms."),



  /**
   * The total number of compare operations processed in the server.
   */
  INFO_PROCESSING_TIME_DESC_COMPARE_TOTAL_COUNT("The total number of compare operations processed in the server."),



  /**
   * The average response time in milliseconds for all compare operations processed within the server.
   */
  INFO_PROCESSING_TIME_DESC_COMPARE_TOTAL_TIME("The average response time in milliseconds for all compare operations processed within the server."),



  /**
   * The percentage of delete operations with processing times less than {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_DELETE_AGGR_PCT("The percentage of delete operations with processing times less than {0,number,0}ms."),



  /**
   * The number of delete operations with processing times between {0,number,0}ms and {1,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_DELETE_COUNT("The number of delete operations with processing times between {0,number,0}ms and {1,number,0}ms."),



  /**
   * The number of delete operations with processing times of at least {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_DELETE_COUNT_LAST("The number of delete operations with processing times of at least {0,number,0}ms."),



  /**
   * The percentage of delete operations with processing times between {0,number,0}ms and {1,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_DELETE_PCT("The percentage of delete operations with processing times between {0,number,0}ms and {1,number,0}ms."),



  /**
   * The percentage of delete operations with processing times of at least {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_DELETE_PCT_LAST("The percentage of delete operations with processing times of at least {0,number,0}ms."),



  /**
   * The total number of delete operations processed in the server.
   */
  INFO_PROCESSING_TIME_DESC_DELETE_TOTAL_COUNT("The total number of delete operations processed in the server."),



  /**
   * The average response time in milliseconds for all delete operations processed within the server.
   */
  INFO_PROCESSING_TIME_DESC_DELETE_TOTAL_TIME("The average response time in milliseconds for all delete operations processed within the server."),



  /**
   * The percentage of extended operations with processing times less than {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_EXTENDED_AGGR_PCT("The percentage of extended operations with processing times less than {0,number,0}ms."),



  /**
   * The number of extended operations with processing times between {0,number,0}ms and {1,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_EXTENDED_COUNT("The number of extended operations with processing times between {0,number,0}ms and {1,number,0}ms."),



  /**
   * The number of extended operations with processing times of at least {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_EXTENDED_COUNT_LAST("The number of extended operations with processing times of at least {0,number,0}ms."),



  /**
   * The percentage of extended operations with processing times between {0,number,0}ms and {1,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_EXTENDED_PCT("The percentage of extended operations with processing times between {0,number,0}ms and {1,number,0}ms."),



  /**
   * The percentage of extended operations with processing times of at least {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_EXTENDED_PCT_LAST("The percentage of extended operations with processing times of at least {0,number,0}ms."),



  /**
   * The total number of extended operations processed in the server.
   */
  INFO_PROCESSING_TIME_DESC_EXTENDED_TOTAL_COUNT("The total number of extended operations processed in the server."),



  /**
   * The average response time in milliseconds for all extended operations processed within the server.
   */
  INFO_PROCESSING_TIME_DESC_EXTENDED_TOTAL_TIME("The average response time in milliseconds for all extended operations processed within the server."),



  /**
   * The percentage of modify operations with processing times less than {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_MODIFY_AGGR_PCT("The percentage of modify operations with processing times less than {0,number,0}ms."),



  /**
   * The number of modify operations with processing times between {0,number,0}ms and {1,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_MODIFY_COUNT("The number of modify operations with processing times between {0,number,0}ms and {1,number,0}ms."),



  /**
   * The number of modify operations with processing times of at least {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_MODIFY_COUNT_LAST("The number of modify operations with processing times of at least {0,number,0}ms."),



  /**
   * The percentage of modify DN operations with processing times less than {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_MODIFY_DN_AGGR_PCT("The percentage of modify DN operations with processing times less than {0,number,0}ms."),



  /**
   * The number of modify operations with processing times between {0,number,0}ms and {1,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_MODIFY_DN_COUNT("The number of modify operations with processing times between {0,number,0}ms and {1,number,0}ms."),



  /**
   * The number of modify DN operations with processing times of at least {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_MODIFY_DN_COUNT_LAST("The number of modify DN operations with processing times of at least {0,number,0}ms."),



  /**
   * The percentage of modify DN operations with processing times between {0,number,0}ms and {1,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_MODIFY_DN_PCT("The percentage of modify DN operations with processing times between {0,number,0}ms and {1,number,0}ms."),



  /**
   * The percentage of modify DN operations with processing times of at least {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_MODIFY_DN_PCT_LAST("The percentage of modify DN operations with processing times of at least {0,number,0}ms."),



  /**
   * The total number of modify DN operations processed in the server.
   */
  INFO_PROCESSING_TIME_DESC_MODIFY_DN_TOTAL_COUNT("The total number of modify DN operations processed in the server."),



  /**
   * The average response time in milliseconds for all modify DN operations processed within the server.
   */
  INFO_PROCESSING_TIME_DESC_MODIFY_DN_TOTAL_TIME("The average response time in milliseconds for all modify DN operations processed within the server."),



  /**
   * The percentage of modify operations with processing times between {0,number,0}ms and {1,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_MODIFY_PCT("The percentage of modify operations with processing times between {0,number,0}ms and {1,number,0}ms."),



  /**
   * The percentage of modify operations with processing times of at least {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_MODIFY_PCT_LAST("The percentage of modify operations with processing times of at least {0,number,0}ms."),



  /**
   * The total number of modify operations processed in the server.
   */
  INFO_PROCESSING_TIME_DESC_MODIFY_TOTAL_COUNT("The total number of modify operations processed in the server."),



  /**
   * The average response time in milliseconds for all modify operations processed within the server.
   */
  INFO_PROCESSING_TIME_DESC_MODIFY_TOTAL_TIME("The average response time in milliseconds for all modify operations processed within the server."),



  /**
   * The percentage of search operations with processing times less than {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_SEARCH_AGGR_PCT("The percentage of search operations with processing times less than {0,number,0}ms."),



  /**
   * The number of search operations with processing times between {0,number,0}ms and {1,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_SEARCH_COUNT("The number of search operations with processing times between {0,number,0}ms and {1,number,0}ms."),



  /**
   * The number of search operations with processing times of at least {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_SEARCH_COUNT_LAST("The number of search operations with processing times of at least {0,number,0}ms."),



  /**
   * The percentage of search operations with processing times between {0,number,0}ms and {1,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_SEARCH_PCT("The percentage of search operations with processing times between {0,number,0}ms and {1,number,0}ms."),



  /**
   * The percentage of search operations with processing times of at least {0,number,0}ms.
   */
  INFO_PROCESSING_TIME_DESC_SEARCH_PCT_LAST("The percentage of search operations with processing times of at least {0,number,0}ms."),



  /**
   * The total number of search operations processed in the server.
   */
  INFO_PROCESSING_TIME_DESC_SEARCH_TOTAL_COUNT("The total number of search operations processed in the server."),



  /**
   * The average response time in milliseconds for all search operations processed within the server.
   */
  INFO_PROCESSING_TIME_DESC_SEARCH_TOTAL_TIME("The average response time in milliseconds for all search operations processed within the server."),



  /**
   * Percent of Add Processing Times Less Than {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_ADD_AGGR_PCT("Percent of Add Processing Times Less Than {0,number,0}ms"),



  /**
   * Add Processing Times Between {0,number,0}ms and {1,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_ADD_COUNT("Add Processing Times Between {0,number,0}ms and {1,number,0}ms"),



  /**
   * Add Processing Times Over {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_ADD_COUNT_LAST("Add Processing Times Over {0,number,0}ms"),



  /**
   * Percent of Add Processing Times Between {0,number,0}ms and {1,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_ADD_PCT("Percent of Add Processing Times Between {0,number,0}ms and {1,number,0}ms"),



  /**
   * Percent of Add Processing Times Over {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_ADD_PCT_LAST("Percent of Add Processing Times Over {0,number,0}ms"),



  /**
   * Number of Add Operations Processed
   */
  INFO_PROCESSING_TIME_DISPNAME_ADD_TOTAL_COUNT("Number of Add Operations Processed"),



  /**
   * Average Add Response Time (ms)
   */
  INFO_PROCESSING_TIME_DISPNAME_ADD_TOTAL_TIME("Average Add Response Time (ms)"),



  /**
   * Percent of Operation Processing Times Less Than {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_ALL_AGGR_PCT("Percent of Operation Processing Times Less Than {0,number,0}ms"),



  /**
   * Operation Processing Times Between {0,number,0}ms and {1,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_ALL_COUNT("Operation Processing Times Between {0,number,0}ms and {1,number,0}ms"),



  /**
   * Operation Processing Times Over {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_ALL_COUNT_LAST("Operation Processing Times Over {0,number,0}ms"),



  /**
   * Percent of Operation Processing Times Between {0,number,0}ms and {1,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_ALL_PCT("Percent of Operation Processing Times Between {0,number,0}ms and {1,number,0}ms"),



  /**
   * Percent of Operation Processing Times Over {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_ALL_PCT_LAST("Percent of Operation Processing Times Over {0,number,0}ms"),



  /**
   * Total Number of Operations Processed
   */
  INFO_PROCESSING_TIME_DISPNAME_ALL_TOTAL_COUNT("Total Number of Operations Processed"),



  /**
   * Overall Average Response Time (ms)
   */
  INFO_PROCESSING_TIME_DISPNAME_ALL_TOTAL_TIME("Overall Average Response Time (ms)"),



  /**
   * Percent of Bind Processing Times Less Than {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_BIND_AGGR_PCT("Percent of Bind Processing Times Less Than {0,number,0}ms"),



  /**
   * Bind Processing Times Between {0,number,0}ms and {1,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_BIND_COUNT("Bind Processing Times Between {0,number,0}ms and {1,number,0}ms"),



  /**
   * Bind Processing Times Over {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_BIND_COUNT_LAST("Bind Processing Times Over {0,number,0}ms"),



  /**
   * Percent of Bind Processing Times Between {0,number,0}ms and {1,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_BIND_PCT("Percent of Bind Processing Times Between {0,number,0}ms and {1,number,0}ms"),



  /**
   * Percent of Bind Processing Times Over {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_BIND_PCT_LAST("Percent of Bind Processing Times Over {0,number,0}ms"),



  /**
   * Number of Bind Operations Processed
   */
  INFO_PROCESSING_TIME_DISPNAME_BIND_TOTAL_COUNT("Number of Bind Operations Processed"),



  /**
   * Average Bind Response Time (ms)
   */
  INFO_PROCESSING_TIME_DISPNAME_BIND_TOTAL_TIME("Average Bind Response Time (ms)"),



  /**
   * Percent of Compare Processing Times Less Than {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_COMPARE_AGGR_PCT("Percent of Compare Processing Times Less Than {0,number,0}ms"),



  /**
   * Compare Processing Times Between {0,number,0}ms and {1,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_COMPARE_COUNT("Compare Processing Times Between {0,number,0}ms and {1,number,0}ms"),



  /**
   * Compare Processing Times Over {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_COMPARE_COUNT_LAST("Compare Processing Times Over {0,number,0}ms"),



  /**
   * Percent of Compare Processing Times Between {0,number,0}ms and {1,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_COMPARE_PCT("Percent of Compare Processing Times Between {0,number,0}ms and {1,number,0}ms"),



  /**
   * Percent of Compare Processing Times Over {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_COMPARE_PCT_LAST("Percent of Compare Processing Times Over {0,number,0}ms"),



  /**
   * Number of Compare Operations Processed
   */
  INFO_PROCESSING_TIME_DISPNAME_COMPARE_TOTAL_COUNT("Number of Compare Operations Processed"),



  /**
   * Average Compare Response Time (ms)
   */
  INFO_PROCESSING_TIME_DISPNAME_COMPARE_TOTAL_TIME("Average Compare Response Time (ms)"),



  /**
   * Percent of Delete Processing Times Less Than {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_DELETE_AGGR_PCT("Percent of Delete Processing Times Less Than {0,number,0}ms"),



  /**
   * Delete Processing Times Between {0,number,0}ms and {1,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_DELETE_COUNT("Delete Processing Times Between {0,number,0}ms and {1,number,0}ms"),



  /**
   * Delete Processing Times Over {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_DELETE_COUNT_LAST("Delete Processing Times Over {0,number,0}ms"),



  /**
   * Percent of Delete Processing Times Between {0,number,0}ms and {1,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_DELETE_PCT("Percent of Delete Processing Times Between {0,number,0}ms and {1,number,0}ms"),



  /**
   * Percent of Delete Processing Times Over {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_DELETE_PCT_LAST("Percent of Delete Processing Times Over {0,number,0}ms"),



  /**
   * Number of Delete Operations Processed
   */
  INFO_PROCESSING_TIME_DISPNAME_DELETE_TOTAL_COUNT("Number of Delete Operations Processed"),



  /**
   * Average Delete Response Time (ms)
   */
  INFO_PROCESSING_TIME_DISPNAME_DELETE_TOTAL_TIME("Average Delete Response Time (ms)"),



  /**
   * Percent of Extended Operation Processing Times Less Than {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_EXTENDED_AGGR_PCT("Percent of Extended Operation Processing Times Less Than {0,number,0}ms"),



  /**
   * Extended Operation Processing Times Between {0,number,0}ms and {1,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_EXTENDED_COUNT("Extended Operation Processing Times Between {0,number,0}ms and {1,number,0}ms"),



  /**
   * Extended Operation Processing Times Over {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_EXTENDED_COUNT_LAST("Extended Operation Processing Times Over {0,number,0}ms"),



  /**
   * Percent of Extended Operation Processing Times Between {0,number,0}ms and {1,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_EXTENDED_PCT("Percent of Extended Operation Processing Times Between {0,number,0}ms and {1,number,0}ms"),



  /**
   * Percent of Extended Operation Processing Times Over {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_EXTENDED_PCT_LAST("Percent of Extended Operation Processing Times Over {0,number,0}ms"),



  /**
   * Number of Extended Operations Processed
   */
  INFO_PROCESSING_TIME_DISPNAME_EXTENDED_TOTAL_COUNT("Number of Extended Operations Processed"),



  /**
   * Average Extended Operation Response Time (ms)
   */
  INFO_PROCESSING_TIME_DISPNAME_EXTENDED_TOTAL_TIME("Average Extended Operation Response Time (ms)"),



  /**
   * Percent of Modify Processing Times Less Than {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_MODIFY_AGGR_PCT("Percent of Modify Processing Times Less Than {0,number,0}ms"),



  /**
   * Modify Processing Times Between {0,number,0}ms and {1,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_MODIFY_COUNT("Modify Processing Times Between {0,number,0}ms and {1,number,0}ms"),



  /**
   * Modify Processing Times Over {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_MODIFY_COUNT_LAST("Modify Processing Times Over {0,number,0}ms"),



  /**
   * Percent of Modify DN Processing Times Less Than {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_MODIFY_DN_AGGR_PCT("Percent of Modify DN Processing Times Less Than {0,number,0}ms"),



  /**
   * Modify DN Processing Times Between {0,number,0}ms and {1,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_MODIFY_DN_COUNT("Modify DN Processing Times Between {0,number,0}ms and {1,number,0}ms"),



  /**
   * Modify DN Processing Times Over {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_MODIFY_DN_COUNT_LAST("Modify DN Processing Times Over {0,number,0}ms"),



  /**
   * Percent of Modify DN Processing Times Between {0,number,0}ms and {1,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_MODIFY_DN_PCT("Percent of Modify DN Processing Times Between {0,number,0}ms and {1,number,0}ms"),



  /**
   * Percent of Modify DN Processing Times Over {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_MODIFY_DN_PCT_LAST("Percent of Modify DN Processing Times Over {0,number,0}ms"),



  /**
   * Number of Modify DN Operations Processed
   */
  INFO_PROCESSING_TIME_DISPNAME_MODIFY_DN_TOTAL_COUNT("Number of Modify DN Operations Processed"),



  /**
   * Average Modify DN Response Time (ms)
   */
  INFO_PROCESSING_TIME_DISPNAME_MODIFY_DN_TOTAL_TIME("Average Modify DN Response Time (ms)"),



  /**
   * Percent of Modify Processing Times Between {0,number,0}ms and {1,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_MODIFY_PCT("Percent of Modify Processing Times Between {0,number,0}ms and {1,number,0}ms"),



  /**
   * Percent of Modify Processing Times Over {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_MODIFY_PCT_LAST("Percent of Modify Processing Times Over {0,number,0}ms"),



  /**
   * Number of Modify Operations Processed
   */
  INFO_PROCESSING_TIME_DISPNAME_MODIFY_TOTAL_COUNT("Number of Modify Operations Processed"),



  /**
   * Average Modify Response Time (ms)
   */
  INFO_PROCESSING_TIME_DISPNAME_MODIFY_TOTAL_TIME("Average Modify Response Time (ms)"),



  /**
   * Percent of Search Processing Times Less Than {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_SEARCH_AGGR_PCT("Percent of Search Processing Times Less Than {0,number,0}ms"),



  /**
   * Search Processing Times Between {0,number,0}ms and {1,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_SEARCH_COUNT("Search Processing Times Between {0,number,0}ms and {1,number,0}ms"),



  /**
   * Search Processing Times Over {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_SEARCH_COUNT_LAST("Search Processing Times Over {0,number,0}ms"),



  /**
   * Percent of Search Processing Times Between {0,number,0}ms and {1,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_SEARCH_PCT("Percent of Search Processing Times Between {0,number,0}ms and {1,number,0}ms"),



  /**
   * Percent of Search Processing Times Over {0,number,0}ms
   */
  INFO_PROCESSING_TIME_DISPNAME_SEARCH_PCT_LAST("Percent of Search Processing Times Over {0,number,0}ms"),



  /**
   * Number of Search Operations Processed
   */
  INFO_PROCESSING_TIME_DISPNAME_SEARCH_TOTAL_COUNT("Number of Search Operations Processed"),



  /**
   * Average Search Response Time (ms)
   */
  INFO_PROCESSING_TIME_DISPNAME_SEARCH_TOTAL_TIME("Average Search Response Time (ms)"),



  /**
   * Categorizes operation processing times into a number of user-defined categories.
   */
  INFO_PROCESSING_TIME_MONITOR_DESC("Categorizes operation processing times into a number of user-defined categories."),



  /**
   * Processing Time Histogram
   */
  INFO_PROCESSING_TIME_MONITOR_DISPNAME("Processing Time Histogram"),



  /**
   * The base DN for replicated content managed by the replication server.
   */
  INFO_REPLICATION_SERVER_DESC_BASE_DN("The base DN for replicated content managed by the replication server."),



  /**
   * The generation IDs in associated with the replicated base DNs managed by the replication server.
   */
  INFO_REPLICATION_SERVER_DESC_BASE_DN_GENERATION_ID("The generation IDs in associated with the replicated base DNs managed by the replication server."),



  /**
   * The server ID for the replication server.
   */
  INFO_REPLICATION_SERVER_DESC_REPLICATION_SERVER_ID("The server ID for the replication server."),



  /**
   * The port number on which the replication server listens for communication from other servers.
   */
  INFO_REPLICATION_SERVER_DESC_REPLICATION_SERVER_PORT("The port number on which the replication server listens for communication from other servers."),



  /**
   * Indicates whether SSL encryption is available for use with the replication server.
   */
  INFO_REPLICATION_SERVER_DESC_SSL_AVAILABLE("Indicates whether SSL encryption is available for use with the replication server."),



  /**
   * Base DN
   */
  INFO_REPLICATION_SERVER_DISPNAME_BASE_DN("Base DN"),



  /**
   * Generation IDs by Base DN
   */
  INFO_REPLICATION_SERVER_DISPNAME_BASE_DN_GENERATION_ID("Generation IDs by Base DN"),



  /**
   * Replication Server ID
   */
  INFO_REPLICATION_SERVER_DISPNAME_REPLICATION_SERVER_ID("Replication Server ID"),



  /**
   * Replication Server Port
   */
  INFO_REPLICATION_SERVER_DISPNAME_REPLICATION_SERVER_PORT("Replication Server Port"),



  /**
   * SSL Encryption Available
   */
  INFO_REPLICATION_SERVER_DISPNAME_SSL_AVAILABLE("SSL Encryption Available"),



  /**
   * Provides information about the replication server to which the connection is established.
   */
  INFO_REPLICATION_SERVER_MONITOR_DESC("Provides information about the replication server to which the connection is established."),



  /**
   * Replication Server
   */
  INFO_REPLICATION_SERVER_MONITOR_DISPNAME("Replication Server"),



  /**
   * The base DN for the replicated content for which the summary information is provided.
   */
  INFO_REPLICATION_SUMMARY_DESC_BASE_DN("The base DN for the replicated content for which the summary information is provided."),



  /**
   * Information about the Directory Server instances which are involved with replication for data below the associated base DN.
   */
  INFO_REPLICATION_SUMMARY_DESC_REPLICA("Information about the Directory Server instances which are involved with replication for data below the associated base DN."),



  /**
   * Information about the Replication Server instances which are involved with replication for data below the associated base DN.
   */
  INFO_REPLICATION_SUMMARY_DESC_REPLICATION_SERVER("Information about the Replication Server instances which are involved with replication for data below the associated base DN."),



  /**
   * Base DN
   */
  INFO_REPLICATION_SUMMARY_DISPNAME_BASE_DN("Base DN"),



  /**
   * Replica
   */
  INFO_REPLICATION_SUMMARY_DISPNAME_REPLICA("Replica"),



  /**
   * Replication Server
   */
  INFO_REPLICATION_SUMMARY_DISPNAME_REPLICATION_SERVER("Replication Server"),



  /**
   * Provides a summary of replication state information for replicas and replication servers for a portion of the DIT.
   */
  INFO_REPLICATION_SUMMARY_MONITOR_DESC("Provides a summary of replication state information for replicas and replication servers for a portion of the DIT."),



  /**
   * Replication Summary
   */
  INFO_REPLICATION_SUMMARY_MONITOR_DISPNAME("Replication Summary"),



  /**
   * The base DN for the replicated content in the Directory Server.
   */
  INFO_REPLICA_DESC_BASE_DN("The base DN for the replicated content in the Directory Server."),



  /**
   * The address and port number of the replication server to which the replica is connected.
   */
  INFO_REPLICA_DESC_CONNECTED_TO("The address and port number of the replication server to which the replica is connected."),



  /**
   * The current receive window size for this replica.
   */
  INFO_REPLICA_DESC_CURRENT_RECEIVE_WINDOW_SIZE("The current receive window size for this replica."),



  /**
   * The current send window size for this replica.
   */
  INFO_REPLICA_DESC_CURRENT_SEND_WINDOW_SIZE("The current send window size for this replica."),



  /**
   * The generation ID for this replica.
   */
  INFO_REPLICA_DESC_GENERATION_ID("The generation ID for this replica."),



  /**
   * The number of times a connection to a replication server has been lost.
   */
  INFO_REPLICA_DESC_LOST_CONNECTIONS("The number of times a connection to a replication server has been lost."),



  /**
   * The maximum receive window size for this replica.
   */
  INFO_REPLICA_DESC_MAX_RECEIVE_WINDOW_SIZE("The maximum receive window size for this replica."),



  /**
   * The maximum send window size for this replica.
   */
  INFO_REPLICA_DESC_MAX_SEND_WINDOW_SIZE("The maximum send window size for this replica."),



  /**
   * The number of updates in progress in the Directory Server that have not yet been sent to the replication server.
   */
  INFO_REPLICA_DESC_PENDING_UPDATES("The number of updates in progress in the Directory Server that have not yet been sent to the replication server."),



  /**
   * The total number of updates received from the replication server.
   */
  INFO_REPLICA_DESC_RECEIVED_UPDATES("The total number of updates received from the replication server."),



  /**
   * The replica ID for this replica.
   */
  INFO_REPLICA_DESC_REPLICA_ID("The replica ID for this replica."),



  /**
   * The number of updates for this replica that were successfully replayed after automatically resolving a modify conflict.
   */
  INFO_REPLICA_DESC_RESOLVED_MODIFY_CONFLICTS("The number of updates for this replica that were successfully replayed after automatically resolving a modify conflict."),



  /**
   * The number of updates for this replica that were successfully replayed after automatically resolving a naming conflict.
   */
  INFO_REPLICA_DESC_RESOLVED_NAMING_CONFLICTS("The number of updates for this replica that were successfully replayed after automatically resolving a naming conflict."),



  /**
   * The total number of updates sent to the replication server.
   */
  INFO_REPLICA_DESC_SENT_UPDATES("The total number of updates sent to the replication server."),



  /**
   * The number of updates for this replica that have been successfully replayed with no conflicts.
   */
  INFO_REPLICA_DESC_SUCCESSFUL_REPLAYED("The number of updates for this replica that have been successfully replayed with no conflicts."),



  /**
   * The total number of updates from the replication server that have been replayed for this replica.
   */
  INFO_REPLICA_DESC_TOTAL_REPLAYED("The total number of updates from the replication server that have been replayed for this replica."),



  /**
   * The number of updates for this replica that could not be replayed due to a naming conflict that could not be automatically resolved.
   */
  INFO_REPLICA_DESC_UNRESOLVED_NAMING_CONFLICTS("The number of updates for this replica that could not be replayed due to a naming conflict that could not be automatically resolved."),



  /**
   * Indicates whether the replica should use SSL to secure communication with the replication server.
   */
  INFO_REPLICA_DESC_USE_SSL("Indicates whether the replica should use SSL to secure communication with the replication server."),



  /**
   * Base DN
   */
  INFO_REPLICA_DISPNAME_BASE_DN("Base DN"),



  /**
   * Replication Server Address
   */
  INFO_REPLICA_DISPNAME_CONNECTED_TO("Replication Server Address"),



  /**
   * Current Receive Window Size
   */
  INFO_REPLICA_DISPNAME_CURRENT_RECEIVE_WINDOW_SIZE("Current Receive Window Size"),



  /**
   * Current Send Window Size
   */
  INFO_REPLICA_DISPNAME_CURRENT_SEND_WINDOW_SIZE("Current Send Window Size"),



  /**
   * Generation ID
   */
  INFO_REPLICA_DISPNAME_GENERATION_ID("Generation ID"),



  /**
   * Lost Connections
   */
  INFO_REPLICA_DISPNAME_LOST_CONNECTIONS("Lost Connections"),



  /**
   * Maximum Receive Window Size
   */
  INFO_REPLICA_DISPNAME_MAX_RECEIVE_WINDOW_SIZE("Maximum Receive Window Size"),



  /**
   * Maximum Send Window Size
   */
  INFO_REPLICA_DISPNAME_MAX_SEND_WINDOW_SIZE("Maximum Send Window Size"),



  /**
   * Pending Updates
   */
  INFO_REPLICA_DISPNAME_PENDING_UPDATES("Pending Updates"),



  /**
   * Received Updates
   */
  INFO_REPLICA_DISPNAME_RECEIVED_UPDATES("Received Updates"),



  /**
   * Replica ID
   */
  INFO_REPLICA_DISPNAME_REPLICA_ID("Replica ID"),



  /**
   * Updates Replayed after Resolving Modify Conflicts
   */
  INFO_REPLICA_DISPNAME_RESOLVED_MODIFY_CONFLICTS("Updates Replayed after Resolving Modify Conflicts"),



  /**
   * Updates Replayed after Resolving Naming Conflicts
   */
  INFO_REPLICA_DISPNAME_RESOLVED_NAMING_CONFLICTS("Updates Replayed after Resolving Naming Conflicts"),



  /**
   * Sent Updates
   */
  INFO_REPLICA_DISPNAME_SENT_UPDATES("Sent Updates"),



  /**
   * Updates Successfully Replayed
   */
  INFO_REPLICA_DISPNAME_SUCCESSFUL_REPLAYED("Updates Successfully Replayed"),



  /**
   * Total Updates Replayed
   */
  INFO_REPLICA_DISPNAME_TOTAL_REPLAYED("Total Updates Replayed"),



  /**
   * Unresolved Naming Conflicts
   */
  INFO_REPLICA_DISPNAME_UNRESOLVED_NAMING_CONFLICTS("Unresolved Naming Conflicts"),



  /**
   * Use SSL
   */
  INFO_REPLICA_DISPNAME_USE_SSL("Use SSL"),



  /**
   * Information about a replicated portion of the Directory Server content, and the communication between the replica and the replication server.
   */
  INFO_REPLICA_MONITOR_DESC("Information about a replicated portion of the Directory Server content, and the communication between the replica and the replication server."),



  /**
   * Replica
   */
  INFO_REPLICA_MONITOR_DISPNAME("Replica"),



  /**
   * The total number of extended operations that with request OID {0} resulted in failure.
   */
  INFO_RESULT_CODE_DESC_EXTOP_FAILED_COUNT("The total number of extended operations that with request OID {0} resulted in failure."),



  /**
   * The percent of extended operations with request OID {0} that resulted in failure.
   */
  INFO_RESULT_CODE_DESC_EXTOP_FAILED_PERCENT("The percent of extended operations with request OID {0} that resulted in failure."),



  /**
   * The human-readable name of the extended request with OID {0}.
   */
  INFO_RESULT_CODE_DESC_EXTOP_NAME("The human-readable name of the extended request with OID {0}."),



  /**
   * The average response time for extended operations with request OID {0} that returned a result code of {1}.
   */
  INFO_RESULT_CODE_DESC_EXTOP_RC_AVG_RT("The average response time for extended operations with request OID {0} that returned a result code of {1}."),



  /**
   * The number of extended operations with OID {0} that returned a result code of {1}.
   */
  INFO_RESULT_CODE_DESC_EXTOP_RC_COUNT("The number of extended operations with OID {0} that returned a result code of {1}."),



  /**
   * The name of the extended operation {0} result code with integer value {1}.
   */
  INFO_RESULT_CODE_DESC_EXTOP_RC_NAME("The name of the extended operation {0} result code with integer value {1}."),



  /**
   * The percent of extended operations with request OID {0} that returned a result code of {1}.
   */
  INFO_RESULT_CODE_DESC_EXTOP_RC_PERCENT("The percent of extended operations with request OID {0} that returned a result code of {1}."),



  /**
   * The sum of all response times for extended operations with request OID {0} that returned a result code of {1}.
   */
  INFO_RESULT_CODE_DESC_EXTOP_RC_TOTAL_RT("The sum of all response times for extended operations with request OID {0} that returned a result code of {1}."),



  /**
   * The total number of extended operations with request OID {0} that have been processed.
   */
  INFO_RESULT_CODE_DESC_EXTOP_TOTAL_COUNT("The total number of extended operations with request OID {0} that have been processed."),



  /**
   * The total number of {0} operations that resulted in failure.
   */
  INFO_RESULT_CODE_DESC_FAILED_COUNT("The total number of {0} operations that resulted in failure."),



  /**
   * The percent of {0} operations that resulted in failure.
   */
  INFO_RESULT_CODE_DESC_FAILED_PERCENT("The percent of {0} operations that resulted in failure."),



  /**
   * The average response time for {0} operations that returned a result code of {1}.
   */
  INFO_RESULT_CODE_DESC_RC_AVG_RT("The average response time for {0} operations that returned a result code of {1}."),



  /**
   * The number of {0} operations that returned a result code of {1}.
   */
  INFO_RESULT_CODE_DESC_RC_COUNT("The number of {0} operations that returned a result code of {1}."),



  /**
   * The name of the {0} operation result code with integer value {1}.
   */
  INFO_RESULT_CODE_DESC_RC_NAME("The name of the {0} operation result code with integer value {1}."),



  /**
   * The percent of {0} operations that returned a result code of {1}.
   */
  INFO_RESULT_CODE_DESC_RC_PERCENT("The percent of {0} operations that returned a result code of {1}."),



  /**
   * The sum of all response times for {0} operations that returned a result code of {1}.
   */
  INFO_RESULT_CODE_DESC_RC_TOTAL_RT("The sum of all response times for {0} operations that returned a result code of {1}."),



  /**
   * The total number of {0} operations that have been processed.
   */
  INFO_RESULT_CODE_DESC_TOTAL_COUNT("The total number of {0} operations that have been processed."),



  /**
   * Number of Failed Extended Operations with Request OID {0}
   */
  INFO_RESULT_CODE_DISPNAME_EXTOP_FAILED_COUNT("Number of Failed Extended Operations with Request OID {0}"),



  /**
   * Percent of Failed Extended Operations with Request OID {0}
   */
  INFO_RESULT_CODE_DISPNAME_EXTOP_FAILED_PERCENT("Percent of Failed Extended Operations with Request OID {0}"),



  /**
   * Name of the Extended Request with OID {0}
   */
  INFO_RESULT_CODE_DISPNAME_EXTOP_NAME("Name of the Extended Request with OID {0}"),



  /**
   * Extended Operation {0} Result Code {1} Average Response Time
   */
  INFO_RESULT_CODE_DISPNAME_EXTOP_RC_AVG_RT("Extended Operation {0} Result Code {1} Average Response Time"),



  /**
   * Extended Operation {0} Result Code {1} Count
   */
  INFO_RESULT_CODE_DISPNAME_EXTOP_RC_COUNT("Extended Operation {0} Result Code {1} Count"),



  /**
   * Extended Operation {0} Result Code {1} Name
   */
  INFO_RESULT_CODE_DISPNAME_EXTOP_RC_NAME("Extended Operation {0} Result Code {1} Name"),



  /**
   * Extended Operation {0} Result Code {1} Percent
   */
  INFO_RESULT_CODE_DISPNAME_EXTOP_RC_PERCENT("Extended Operation {0} Result Code {1} Percent"),



  /**
   * Extended Operation {0} Result Code {1} Total Response Time
   */
  INFO_RESULT_CODE_DISPNAME_EXTOP_RC_TOTAL_RT("Extended Operation {0} Result Code {1} Total Response Time"),



  /**
   * Total Number of Extended Operations with Request OID {0}
   */
  INFO_RESULT_CODE_DISPNAME_EXTOP_TOTAL_COUNT("Total Number of Extended Operations with Request OID {0}"),



  /**
   * Number of Failed {0} Operations
   */
  INFO_RESULT_CODE_DISPNAME_FAILED_COUNT("Number of Failed {0} Operations"),



  /**
   * Percent of Failed {0} Operations
   */
  INFO_RESULT_CODE_DISPNAME_FAILED_PERCENT("Percent of Failed {0} Operations"),



  /**
   * {0} Operation Result Code {1} Average Response Time
   */
  INFO_RESULT_CODE_DISPNAME_RC_AVG_RT("{0} Operation Result Code {1} Average Response Time"),



  /**
   * {0} Operation Result Code {1} Count
   */
  INFO_RESULT_CODE_DISPNAME_RC_COUNT("{0} Operation Result Code {1} Count"),



  /**
   * {0} Operation Result Code {1} Name
   */
  INFO_RESULT_CODE_DISPNAME_RC_NAME("{0} Operation Result Code {1} Name"),



  /**
   * {0} Operation Result Code {1} Percent
   */
  INFO_RESULT_CODE_DISPNAME_RC_PERCENT("{0} Operation Result Code {1} Percent"),



  /**
   * {0} Operation Result Code {1} Total Response Time
   */
  INFO_RESULT_CODE_DISPNAME_RC_TOTAL_RT("{0} Operation Result Code {1} Total Response Time"),



  /**
   * Total Number of {0} Operations
   */
  INFO_RESULT_CODE_DISPNAME_TOTAL_COUNT("Total Number of {0} Operations"),



  /**
   * Provides information about the result codes returned during operation processing.
   */
  INFO_RESULT_CODE_MONITOR_DESC("Provides information about the result codes returned during operation processing."),



  /**
   * Result Code
   */
  INFO_RESULT_CODE_MONITOR_DISPNAME("Result Code"),



  /**
   * Add
   */
  INFO_RESULT_CODE_OP_NAME_ADD("Add"),



  /**
   * All
   */
  INFO_RESULT_CODE_OP_NAME_ALL("All"),



  /**
   * Bind
   */
  INFO_RESULT_CODE_OP_NAME_BIND("Bind"),



  /**
   * Compare
   */
  INFO_RESULT_CODE_OP_NAME_COMPARE("Compare"),



  /**
   * Delete
   */
  INFO_RESULT_CODE_OP_NAME_DELETE("Delete"),



  /**
   * Extended
   */
  INFO_RESULT_CODE_OP_NAME_EXTENDED("Extended"),



  /**
   * Modify
   */
  INFO_RESULT_CODE_OP_NAME_MODIFY("Modify"),



  /**
   * Modify DN
   */
  INFO_RESULT_CODE_OP_NAME_MODIFY_DN("Modify DN"),



  /**
   * Search
   */
  INFO_RESULT_CODE_OP_NAME_SEARCH("Search"),



  /**
   * Provides the lines that comprise the stack trace frames for all threads within the Directory Server JVM.
   */
  INFO_STACK_TRACE_DESC_TRACE("Provides the lines that comprise the stack trace frames for all threads within the Directory Server JVM."),



  /**
   * Stack Trace
   */
  INFO_STACK_TRACE_DISPNAME_TRACE("Stack Trace"),



  /**
   * Provides a stack trace of all threads processing within the JVM.
   */
  INFO_STACK_TRACE_MONITOR_DESC("Provides a stack trace of all threads processing within the JVM."),



  /**
   * JVM Stack Trace
   */
  INFO_STACK_TRACE_MONITOR_DISPNAME("JVM Stack Trace"),



  /**
   * The number of CPUs available to the Directory Server JVM.
   */
  INFO_SYSTEM_INFO_DESC_AVAILABLE_CPUS("The number of CPUs available to the Directory Server JVM."),



  /**
   * The Java classpath being used to run the Directory Server.
   */
  INFO_SYSTEM_INFO_DESC_CLASSPATH("The Java classpath being used to run the Directory Server."),



  /**
   * The names and values of all environment variables available to process in which the server is running.
   */
  INFO_SYSTEM_INFO_DESC_ENV_VAR("The names and values of all environment variables available to process in which the server is running."),



  /**
   * The amount of memory currently held by the JVM that is marked free within the JVM.
   */
  INFO_SYSTEM_INFO_DESC_FREE_MEMORY("The amount of memory currently held by the JVM that is marked free within the JVM."),



  /**
   * The hostname of the system on which the Directory Server is running.
   */
  INFO_SYSTEM_INFO_DESC_HOSTNAME("The hostname of the system on which the Directory Server is running."),



  /**
   * The path to the directory in which the Directory Server is installed.
   */
  INFO_SYSTEM_INFO_DESC_INSTANCE_ROOT("The path to the directory in which the Directory Server is installed."),



  /**
   * The path to the Java installation used by the Directory Server.
   */
  INFO_SYSTEM_INFO_DESC_JAVA_HOME("The path to the Java installation used by the Directory Server."),



  /**
   * The name of the vendor that provides the Java implementation used by the Directory Server.
   */
  INFO_SYSTEM_INFO_DESC_JAVA_VENDOR("The name of the vendor that provides the Java implementation used by the Directory Server."),



  /**
   * The Java version used by the Directory Server.
   */
  INFO_SYSTEM_INFO_DESC_JAVA_VERSION("The Java version used by the Directory Server."),



  /**
   * The data model (32-bit or 64-bit) for the JVM on which the Directory Server is running.
   */
  INFO_SYSTEM_INFO_DESC_JVM_ARCHITECTURE("The data model (32-bit or 64-bit) for the JVM on which the Directory Server is running."),



  /**
   * The set of JVM arguments being used to run the Directory Server.
   */
  INFO_SYSTEM_INFO_DESC_JVM_ARGUMENTS("The set of JVM arguments being used to run the Directory Server."),



  /**
   * The process ID of the JVM process in which the Directory Server is running.
   */
  INFO_SYSTEM_INFO_DESC_JVM_PID("The process ID of the JVM process in which the Directory Server is running."),



  /**
   * The name of the vendor that provides the JVM used by the Directory Server.
   */
  INFO_SYSTEM_INFO_DESC_JVM_VENDOR("The name of the vendor that provides the JVM used by the Directory Server."),



  /**
   * The version of the Java VM used by the Directory Server.
   */
  INFO_SYSTEM_INFO_DESC_JVM_VERSION("The version of the Java VM used by the Directory Server."),



  /**
   * The maximum amount of memory in bytes that may be used by the JVM used to run the Directory Server.
   */
  INFO_SYSTEM_INFO_DESC_MAX_MEMORY("The maximum amount of memory in bytes that may be used by the JVM used to run the Directory Server."),



  /**
   * The operating system on which the Directory Server is running.
   */
  INFO_SYSTEM_INFO_DESC_OPERATING_SYSTEM("The operating system on which the Directory Server is running."),



  /**
   * The name of the default SSL context protocol that has been selected by the server.
   */
  INFO_SYSTEM_INFO_DESC_SSL_CONTEXT_PROTOCOL("The name of the default SSL context protocol that has been selected by the server."),



  /**
   * The names and values of all system properties defined in the server JVM.
   */
  INFO_SYSTEM_INFO_DESC_SYSTEM_PROP("The names and values of all system properties defined in the server JVM."),



  /**
   * The amount of memory in bytes currently used the JVM used to run the Directory Server.
   */
  INFO_SYSTEM_INFO_DESC_USED_MEMORY("The amount of memory in bytes currently used the JVM used to run the Directory Server."),



  /**
   * The name of the system user as whom the JVM is running.
   */
  INFO_SYSTEM_INFO_DESC_USER_NAME("The name of the system user as whom the JVM is running."),



  /**
   * The path to the directory from which the Directory Server was started.
   */
  INFO_SYSTEM_INFO_DESC_WORKING_DIRECTORY("The path to the directory from which the Directory Server was started."),



  /**
   * Available CPUs
   */
  INFO_SYSTEM_INFO_DISPNAME_AVAILABLE_CPUS("Available CPUs"),



  /**
   * Java Classpath
   */
  INFO_SYSTEM_INFO_DISPNAME_CLASSPATH("Java Classpath"),



  /**
   * Environment Variable
   */
  INFO_SYSTEM_INFO_DISPNAME_ENV_VAR("Environment Variable"),



  /**
   * Free Memory Within the JVM
   */
  INFO_SYSTEM_INFO_DISPNAME_FREE_MEMORY("Free Memory Within the JVM"),



  /**
   * System Hostname
   */
  INFO_SYSTEM_INFO_DISPNAME_HOSTNAME("System Hostname"),



  /**
   * Server Instance Root
   */
  INFO_SYSTEM_INFO_DISPNAME_INSTANCE_ROOT("Server Instance Root"),



  /**
   * Java Home
   */
  INFO_SYSTEM_INFO_DISPNAME_JAVA_HOME("Java Home"),



  /**
   * Java Vendor
   */
  INFO_SYSTEM_INFO_DISPNAME_JAVA_VENDOR("Java Vendor"),



  /**
   * Java Version
   */
  INFO_SYSTEM_INFO_DISPNAME_JAVA_VERSION("Java Version"),



  /**
   * JVM Architecture Data Model
   */
  INFO_SYSTEM_INFO_DISPNAME_JVM_ARCHITECTURE("JVM Architecture Data Model"),



  /**
   * JVM Arguments
   */
  INFO_SYSTEM_INFO_DISPNAME_JVM_ARGUMENTS("JVM Arguments"),



  /**
   * JVM PID
   */
  INFO_SYSTEM_INFO_DISPNAME_JVM_PID("JVM PID"),



  /**
   * Java VM Vendor
   */
  INFO_SYSTEM_INFO_DISPNAME_JVM_VENDOR("Java VM Vendor"),



  /**
   * Java VM Version
   */
  INFO_SYSTEM_INFO_DISPNAME_JVM_VERSION("Java VM Version"),



  /**
   * Maximum JVM Size
   */
  INFO_SYSTEM_INFO_DISPNAME_MAX_MEMORY("Maximum JVM Size"),



  /**
   * Operating System
   */
  INFO_SYSTEM_INFO_DISPNAME_OPERATING_SYSTEM("Operating System"),



  /**
   * SSL Context Protocol
   */
  INFO_SYSTEM_INFO_DISPNAME_SSL_CONTEXT_PROTOCOL("SSL Context Protocol"),



  /**
   * System Property
   */
  INFO_SYSTEM_INFO_DISPNAME_SYSTEM_PROP("System Property"),



  /**
   * Current JVM Size
   */
  INFO_SYSTEM_INFO_DISPNAME_USED_MEMORY("Current JVM Size"),



  /**
   * User Name
   */
  INFO_SYSTEM_INFO_DISPNAME_USER_NAME("User Name"),



  /**
   * Server Working Directory
   */
  INFO_SYSTEM_INFO_DISPNAME_WORKING_DIRECTORY("Server Working Directory"),



  /**
   * Provides general information about the system and JVM on which the Directory Server is running.
   */
  INFO_SYSTEM_INFO_MONITOR_DESC("Provides general information about the system and JVM on which the Directory Server is running."),



  /**
   * System Information
   */
  INFO_SYSTEM_INFO_MONITOR_DISPNAME("System Information"),



  /**
   * The average number of operations observed in the Directory Server work queue since startup.
   */
  INFO_TRADITIONAL_WORK_QUEUE_DESC_AVERAGE_BACKLOG("The average number of operations observed in the Directory Server work queue since startup."),



  /**
   * The number of operations currently in the Directory Server work queue waiting to be processed.
   */
  INFO_TRADITIONAL_WORK_QUEUE_DESC_CURRENT_BACKLOG("The number of operations currently in the Directory Server work queue waiting to be processed."),



  /**
   * The maximum number of operations observed in the Directory Server work queue at any time since startup.
   */
  INFO_TRADITIONAL_WORK_QUEUE_DESC_MAX_BACKLOG("The maximum number of operations observed in the Directory Server work queue at any time since startup."),



  /**
   * The total number of requests that could not be submitted for processing because the work queue was already at its maximum capacity.
   */
  INFO_TRADITIONAL_WORK_QUEUE_DESC_REQUESTS_REJECTED("The total number of requests that could not be submitted for processing because the work queue was already at its maximum capacity."),



  /**
   * The total number of requests submitted for processing to the Directory Server work queue.
   */
  INFO_TRADITIONAL_WORK_QUEUE_DESC_REQUESTS_SUBMITTED("The total number of requests submitted for processing to the Directory Server work queue."),



  /**
   * Average Backlog
   */
  INFO_TRADITIONAL_WORK_QUEUE_DISPNAME_AVERAGE_BACKLOG("Average Backlog"),



  /**
   * Current Backlog
   */
  INFO_TRADITIONAL_WORK_QUEUE_DISPNAME_CURRENT_BACKLOG("Current Backlog"),



  /**
   * Maximum Backlog
   */
  INFO_TRADITIONAL_WORK_QUEUE_DISPNAME_MAX_BACKLOG("Maximum Backlog"),



  /**
   * Requests Rejected
   */
  INFO_TRADITIONAL_WORK_QUEUE_DISPNAME_REQUESTS_REJECTED("Requests Rejected"),



  /**
   * Requests Submitted
   */
  INFO_TRADITIONAL_WORK_QUEUE_DISPNAME_REQUESTS_SUBMITTED("Requests Submitted"),



  /**
   * Provides information about the state of the Directory Server work queue, which is used to hold requests until they can be processed by a worker thread.
   */
  INFO_TRADITIONAL_WORK_QUEUE_MONITOR_DESC("Provides information about the state of the Directory Server work queue, which is used to hold requests until they can be processed by a worker thread."),



  /**
   * Traditional Work Queue
   */
  INFO_TRADITIONAL_WORK_QUEUE_MONITOR_DISPNAME("Traditional Work Queue"),



  /**
   * The average number of operations observed in the Directory Server work queue since startup.
   */
  INFO_UNBOUNDID_WORK_QUEUE_DESC_AVERAGE_SIZE("The average number of operations observed in the Directory Server work queue since startup."),



  /**
   * The average percentage of the time since startup that worker threads have spent processing requests.
   */
  INFO_UNBOUNDID_WORK_QUEUE_DESC_AVG_PCT_BUSY("The average percentage of the time since startup that worker threads have spent processing requests."),



  /**
   * The average length of time in milliseconds that operations have been required to wait in the work queue before being picked up by a worker thread.
   */
  INFO_UNBOUNDID_WORK_QUEUE_DESC_AVG_QUEUE_TIME("The average length of time in milliseconds that operations have been required to wait in the work queue before being picked up by a worker thread."),



  /**
   * The number of operations which are part of an administrative session and are waiting to be processed by the dedicated pool of administrative session worker threads.
   */
  INFO_UNBOUNDID_WORK_QUEUE_DESC_CURRENT_ADMIN_QUEUE_SIZE("The number of operations which are part of an administrative session and are waiting to be processed by the dedicated pool of administrative session worker threads."),



  /**
   * The percentage of worker threads that are currently busy processing operations.
   */
  INFO_UNBOUNDID_WORK_QUEUE_DESC_CURRENT_PCT_BUSY("The percentage of worker threads that are currently busy processing operations."),



  /**
   * The number of operations currently in the Directory Server work queue waiting to be processed.
   */
  INFO_UNBOUNDID_WORK_QUEUE_DESC_CURRENT_SIZE("The number of operations currently in the Directory Server work queue waiting to be processed."),



  /**
   * The maximum number of operations observed in the dedicated queue for operations which are part of an administrative session and are waiting to be processed by a dedicated pool of administrative session worker threads.
   */
  INFO_UNBOUNDID_WORK_QUEUE_DESC_MAX_ADMIN_QUEUE_SIZE("The maximum number of operations observed in the dedicated queue for operations which are part of an administrative session and are waiting to be processed by a dedicated pool of administrative session worker threads."),



  /**
   * The maximum percentage of the time over a any interval that worker threads have spent processing requests.
   */
  INFO_UNBOUNDID_WORK_QUEUE_DESC_MAX_PCT_BUSY("The maximum percentage of the time over a any interval that worker threads have spent processing requests."),



  /**
   * The maximum number of operations observed in the Directory Server work queue at any time since startup.
   */
  INFO_UNBOUNDID_WORK_QUEUE_DESC_MAX_SIZE("The maximum number of operations observed in the Directory Server work queue at any time since startup."),



  /**
   * The number of worker threads which have been created in a separate pool which are reserved for processing operations which are part of an administrative session.
   */
  INFO_UNBOUNDID_WORK_QUEUE_DESC_NUM_ADMIN_THREADS("The number of worker threads which have been created in a separate pool which are reserved for processing operations which are part of an administrative session."),



  /**
   * The number of worker threads which are currently busy processing operations that are part of an administrative session.
   */
  INFO_UNBOUNDID_WORK_QUEUE_DESC_NUM_BUSY_ADMIN_THREADS("The number of worker threads which are currently busy processing operations that are part of an administrative session."),



  /**
   * The number of worker threads that are currently busy processing operations.
   */
  INFO_UNBOUNDID_WORK_QUEUE_DESC_NUM_BUSY_THREADS("The number of worker threads that are currently busy processing operations."),



  /**
   * The number of worker threads configured for the work queue.
   */
  INFO_UNBOUNDID_WORK_QUEUE_DESC_NUM_THREADS("The number of worker threads configured for the work queue."),



  /**
   * The average number of operations observed in the Directory Server work queue over a recent interval.
   */
  INFO_UNBOUNDID_WORK_QUEUE_DESC_RECENT_AVERAGE_SIZE("The average number of operations observed in the Directory Server work queue over a recent interval."),



  /**
   * The percentage of the time over a recent interval that worker threads have spent processing requests.
   */
  INFO_UNBOUNDID_WORK_QUEUE_DESC_RECENT_PCT_BUSY("The percentage of the time over a recent interval that worker threads have spent processing requests."),



  /**
   * The average length of time in milliseconds that recently-processed operations have been required to wait in the work queue before being picked up by a worker thread.
   */
  INFO_UNBOUNDID_WORK_QUEUE_DESC_RECENT_QUEUE_TIME("The average length of time in milliseconds that recently-processed operations have been required to wait in the work queue before being picked up by a worker thread."),



  /**
   * The total number of requests that could not be submitted for processing because the work queue was already at its maximum capacity.
   */
  INFO_UNBOUNDID_WORK_QUEUE_DESC_REQUESTS_REJECTED("The total number of requests that could not be submitted for processing because the work queue was already at its maximum capacity."),



  /**
   * The total number of requests that were stolen from their primary queue by a worker thread associated with a different queue.
   */
  INFO_UNBOUNDID_WORK_QUEUE_DESC_REQUESTS_STOLEN("The total number of requests that were stolen from their primary queue by a worker thread associated with a different queue."),



  /**
   * Average Work Queue Size
   */
  INFO_UNBOUNDID_WORK_QUEUE_DISPNAME_AVERAGE_SIZE("Average Work Queue Size"),



  /**
   * Average Worker Thread Percent Busy
   */
  INFO_UNBOUNDID_WORK_QUEUE_DISPNAME_AVG_PCT_BUSY("Average Worker Thread Percent Busy"),



  /**
   * Average Queue Wait Time (ms)
   */
  INFO_UNBOUNDID_WORK_QUEUE_DISPNAME_AVG_QUEUE_TIME("Average Queue Wait Time (ms)"),



  /**
   * Current Administrative Session Queue Size
   */
  INFO_UNBOUNDID_WORK_QUEUE_DISPNAME_CURRENT_ADMIN_QUEUE_SIZE("Current Administrative Session Queue Size"),



  /**
   * Current Worker Thread Percent Busy
   */
  INFO_UNBOUNDID_WORK_QUEUE_DISPNAME_CURRENT_PCT_BUSY("Current Worker Thread Percent Busy"),



  /**
   * Current Work Queue Size
   */
  INFO_UNBOUNDID_WORK_QUEUE_DISPNAME_CURRENT_SIZE("Current Work Queue Size"),



  /**
   * Maximum Administrative Session Queue Size
   */
  INFO_UNBOUNDID_WORK_QUEUE_DISPNAME_MAX_ADMIN_QUEUE_SIZE("Maximum Administrative Session Queue Size"),



  /**
   * Maximum Worker Thread Percent Busy
   */
  INFO_UNBOUNDID_WORK_QUEUE_DISPNAME_MAX_PCT_BUSY("Maximum Worker Thread Percent Busy"),



  /**
   * Maximum Work Queue Size
   */
  INFO_UNBOUNDID_WORK_QUEUE_DISPNAME_MAX_SIZE("Maximum Work Queue Size"),



  /**
   * Number of Administrative Session Worker Threads
   */
  INFO_UNBOUNDID_WORK_QUEUE_DISPNAME_NUM_ADMIN_THREADS("Number of Administrative Session Worker Threads"),



  /**
   * Number of Busy Administrative Session Worker Threads
   */
  INFO_UNBOUNDID_WORK_QUEUE_DISPNAME_NUM_BUSY_ADMIN_THREADS("Number of Busy Administrative Session Worker Threads"),



  /**
   * Number of Busy Worker Threads
   */
  INFO_UNBOUNDID_WORK_QUEUE_DISPNAME_NUM_BUSY_THREADS("Number of Busy Worker Threads"),



  /**
   * Number of Worker Threads
   */
  INFO_UNBOUNDID_WORK_QUEUE_DISPNAME_NUM_THREADS("Number of Worker Threads"),



  /**
   * Recent Average Work Queue Size
   */
  INFO_UNBOUNDID_WORK_QUEUE_DISPNAME_RECENT_AVERAGE_SIZE("Recent Average Work Queue Size"),



  /**
   * Recent Worker Thread Percent Busy
   */
  INFO_UNBOUNDID_WORK_QUEUE_DISPNAME_RECENT_PCT_BUSY("Recent Worker Thread Percent Busy"),



  /**
   * Recent Queue Wait Time (ms)
   */
  INFO_UNBOUNDID_WORK_QUEUE_DISPNAME_RECENT_QUEUE_TIME("Recent Queue Wait Time (ms)"),



  /**
   * Requests Rejected
   */
  INFO_UNBOUNDID_WORK_QUEUE_DISPNAME_REQUESTS_REJECTED("Requests Rejected"),



  /**
   * Requests Stolen
   */
  INFO_UNBOUNDID_WORK_QUEUE_DISPNAME_REQUESTS_STOLEN("Requests Stolen"),



  /**
   * Provides information about the state of the Directory Server work queue, which is used to hold requests until they can be processed by a worker thread.
   */
  INFO_UNBOUNDID_WORK_QUEUE_MONITOR_DESC("Provides information about the state of the Directory Server work queue, which is used to hold requests until they can be processed by a worker thread."),



  /**
   * UnboundID Work Queue
   */
  INFO_UNBOUNDID_WORK_QUEUE_MONITOR_DISPNAME("UnboundID Work Queue"),



  /**
   * The Directory Server build ID.
   */
  INFO_VERSION_DESC_BUILD_ID("The Directory Server build ID."),



  /**
   * The build number for a promoted Directory Server build.
   */
  INFO_VERSION_DESC_BUILD_NUMBER("The build number for a promoted Directory Server build."),



  /**
   * The compact Directory Server version string.
   */
  INFO_VERSION_DESC_COMPACT_VERSION("The compact Directory Server version string."),



  /**
   * The list of fix IDs for any bug fixes that have been applied to the Directory Server.
   */
  INFO_VERSION_DESC_FIX_IDS("The list of fix IDs for any bug fixes that have been applied to the Directory Server."),



  /**
   * The full Directory Server version string.
   */
  INFO_VERSION_DESC_FULL_VERSION("The full Directory Server version string."),



  /**
   * The version string for the library providing support for the Groovy scripting language
   */
  INFO_VERSION_DESC_GROOVY_VERSION("The version string for the library providing support for the Groovy scripting language"),



  /**
   * The version string for the Berkeley DB Java Edition library
   */
  INFO_VERSION_DESC_JE_VERSION("The version string for the Berkeley DB Java Edition library"),



  /**
   * The version string for the jzlib library
   */
  INFO_VERSION_DESC_JZLIB_VERSION("The version string for the jzlib library"),



  /**
   * The version string for the UnboundID LDAP SDK for Java library
   */
  INFO_VERSION_DESC_LDAP_SDK_VERSION("The version string for the UnboundID LDAP SDK for Java library"),



  /**
   * The Directory Server major version number.
   */
  INFO_VERSION_DESC_MAJOR_VERSION("The Directory Server major version number."),



  /**
   * The Directory Server minor version number.
   */
  INFO_VERSION_DESC_MINOR_VERSION("The Directory Server minor version number."),



  /**
   * The Directory Server point version number.
   */
  INFO_VERSION_DESC_POINT_VERSION("The Directory Server point version number."),



  /**
   * The full name of the Directory Server product.
   */
  INFO_VERSION_DESC_PRODUCT_NAME("The full name of the Directory Server product."),



  /**
   * A string that identifies the source revision from which the server was built.
   */
  INFO_VERSION_DESC_REVISION_ID("A string that identifies the source revision from which the server was built."),



  /**
   * The source revision number from which the Directory Server was built.
   */
  INFO_VERSION_DESC_REVISION_NUMBER("The source revision number from which the Directory Server was built."),



  /**
   * The version string for the UnboundID Server SDK library
   */
  INFO_VERSION_DESC_SERVER_SDK_VERSION("The version string for the UnboundID Server SDK library"),



  /**
   * The short name of the Directory Server product.
   */
  INFO_VERSION_DESC_SHORT_NAME("The short name of the Directory Server product."),



  /**
   * The version string for the SNMP4J AgentX library
   */
  INFO_VERSION_DESC_SNMP4J_AGENTX_VERSION("The version string for the SNMP4J AgentX library"),



  /**
   * The version string for the SNMP4J agent library
   */
  INFO_VERSION_DESC_SNMP4J_AGENT_VERSION("The version string for the SNMP4J agent library"),



  /**
   * The version string for the SNMP4J library
   */
  INFO_VERSION_DESC_SNMP4J_VERSION("The version string for the SNMP4J library"),



  /**
   * The version qualifier string for the Directory Server.
   */
  INFO_VERSION_DESC_VERSION_QUALIFIER("The version qualifier string for the Directory Server."),



  /**
   * Build ID
   */
  INFO_VERSION_DISPNAME_BUILD_ID("Build ID"),



  /**
   * Build Number
   */
  INFO_VERSION_DISPNAME_BUILD_NUMBER("Build Number"),



  /**
   * Compact Version
   */
  INFO_VERSION_DISPNAME_COMPACT_VERSION("Compact Version"),



  /**
   * Fix IDs
   */
  INFO_VERSION_DISPNAME_FIX_IDS("Fix IDs"),



  /**
   * Full Version
   */
  INFO_VERSION_DISPNAME_FULL_VERSION("Full Version"),



  /**
   * Groovy Version
   */
  INFO_VERSION_DISPNAME_GROOVY_VERSION("Groovy Version"),



  /**
   * Berkeley DB JE Version
   */
  INFO_VERSION_DISPNAME_JE_VERSION("Berkeley DB JE Version"),



  /**
   * JZLib Version
   */
  INFO_VERSION_DISPNAME_JZLIB_VERSION("JZLib Version"),



  /**
   * LDAP SDK Version
   */
  INFO_VERSION_DISPNAME_LDAP_SDK_VERSION("LDAP SDK Version"),



  /**
   * Major Version
   */
  INFO_VERSION_DISPNAME_MAJOR_VERSION("Major Version"),



  /**
   * Minor Version
   */
  INFO_VERSION_DISPNAME_MINOR_VERSION("Minor Version"),



  /**
   * Point Version
   */
  INFO_VERSION_DISPNAME_POINT_VERSION("Point Version"),



  /**
   * Product Name
   */
  INFO_VERSION_DISPNAME_PRODUCT_NAME("Product Name"),



  /**
   * Revision ID
   */
  INFO_VERSION_DISPNAME_REVISION_ID("Revision ID"),



  /**
   * Revision Number
   */
  INFO_VERSION_DISPNAME_REVISION_NUMBER("Revision Number"),



  /**
   * Server SDK Version
   */
  INFO_VERSION_DISPNAME_SERVER_SDK_VERSION("Server SDK Version"),



  /**
   * Short Name
   */
  INFO_VERSION_DISPNAME_SHORT_NAME("Short Name"),



  /**
   * SNMP4J AgentX Version
   */
  INFO_VERSION_DISPNAME_SNMP4J_AGENTX_VERSION("SNMP4J AgentX Version"),



  /**
   * SNMP4J Agent Version
   */
  INFO_VERSION_DISPNAME_SNMP4J_AGENT_VERSION("SNMP4J Agent Version"),



  /**
   * SNMP4J Version
   */
  INFO_VERSION_DISPNAME_SNMP4J_VERSION("SNMP4J Version"),



  /**
   * Version Qualifier String
   */
  INFO_VERSION_DISPNAME_VERSION_QUALIFIER("Version Qualifier String"),



  /**
   * Provides information about the Directory Server version.
   */
  INFO_VERSION_MONITOR_DESC("Provides information about the Directory Server version."),



  /**
   * Version
   */
  INFO_VERSION_MONITOR_DISPNAME("Version");



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
      rb = ResourceBundle.getBundle("unboundid-ldapsdk-monitor");
    } catch (final Exception e) {}
    RESOURCE_BUNDLE = rb;
  }



  /**
   * The map that will be used to hold the unformatted message strings, indexed by property name.
   */
  private static final ConcurrentHashMap<MonitorMessages,String> MESSAGE_STRINGS = new ConcurrentHashMap<>(100);



  /**
   * The map that will be used to hold the message format objects, indexed by property name.
   */
  private static final ConcurrentHashMap<MonitorMessages,MessageFormat> MESSAGES = new ConcurrentHashMap<>(100);



  // The default text for this message
  private final String defaultText;



  /**
   * Creates a new message key.
   */
  private MonitorMessages(final String defaultText)
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

