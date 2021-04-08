/*
 * Copyright 2021 Ping Identity Corporation
 * All Rights Reserved.
 */
/*
 * Copyright 2021 Ping Identity Corporation
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
package com.unboundid.ldap.sdk;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;



/**
 * This class provides information about the current version of the UnboundID
 * LDAP SDK for Java.
 * <BR><BR>
 * Note that if you reference the constant values directly in your source code,
 * those constant values may be compiled into your source code directly rather
 * than dynamically replacing them at runtime or when the class is loaded.  This
 * means that if you swap out the LDAP SDK library after the source code has
 * been compiled, the already-compiled code may not accurately reflect the
 * values from the version of the LDAP SDK that is actually being used.  To
 * avoid that, you may wish to use the provided methods to obtain the values
 * rather than referencing the constants directly (e.g., use
 * {@link #getProductName} instead of {@link #PRODUCT_NAME}).
 */
@ThreadSafety(level=ThreadSafetyLevel.COMPLETELY_THREADSAFE)
public final class Version
{
  //
  // NOTE -- This file is dynamically generated.  Do not edit it.  If you need
  //         to add something to it, then add it to the
  //         resource/Version.java.stub file below the LDAP SDK build root.
  //



  /**
   * The official full product name for the LDAP SDK.  For this build, the
   * value is "UnboundID LDAP SDK for Java".
   */
  public static final String PRODUCT_NAME =
       "UnboundID LDAP SDK for Java";



  /**
   * The short product name for the LDAP SDK.  This will not have any spaces.
   * For this build, the value is "unboundid-ldapsdk".
   */
  public static final String SHORT_NAME =
       "unboundid-ldapsdk";



  /**
   * The major version number for the LDAP SDK.  For this build, the value is
   * 6.
   */
  public static final int MAJOR_VERSION = 6;



  /**
   * The minor version number for the LDAP SDK.  For this build, the value is
   * 0.
   */
  public static final int MINOR_VERSION = 0;



  /**
   * The point version number for the LDAP SDK.  For this build, the value is
   * 0.
   */
  public static final int POINT_VERSION = 0;



  /**
   * The version qualifier string for the LDAP SDK.  It will often be a
   * zero-length string, but may be non-empty for special builds that should be
   * tagged in some way (e.g., "-beta1" or "-rc2").  For this build, the value
   * is "".
   */
  public static final String VERSION_QUALIFIER =
       "";



  /**
   * A timestamp that indicates when this build of the LDAP SDK was generated.
   * For this build, the value is "20210408162531Z".
   */
  public static final String BUILD_TIMESTAMP = "20210408162531Z";



  /**
   * The type of repository from which the source code used to build the LDAP
   * SDK was retrieved.  It will be one of "subversion", "git", or "{unknown}".
   * For this build, the value is "git".
   */
  public static final String REPOSITORY_TYPE = "git";



  /**
   * The URL for the repository from which the source code used to build the
   * LDAP SDK was retrieved.  If repository information could not be determined
   * at build time, then this will be a file URL that references the path to the
   * source code on the system used to build the LDAP SDK library.  For this
   * build, the value is
   * "https://github.com/pingidentity/ldapsdk.git".
   */
  public static final String REPOSITORY_URL =
       "https://github.com/pingidentity/ldapsdk.git";



  /**
   * The path to the LDAP SDK source code in the repository.  If repository
   * information could not be determined at build time, then this will be "/".
   * For this build, the value is
   * "/".
   */
  public static final String REPOSITORY_PATH =
       "/";



  /**
   * The string representation of the source revision from which this build of
   * the LDAP SDK was generated.  For a subversion repository, this will be the
   * string representation of the revision number.  For a git repository, this
   * will be the hexadecimal representation of the digest for the most recent
   * commit.  If repository information could not be determined at build time,
   * the value will be "{unknown}".  For this build, the value is
   * "7df578770ca7f0408f2788930f743add535f9680".
   */
  public static final String REVISION_ID =
       "7df578770ca7f0408f2788930f743add535f9680";



  /**
   * The revision number for the source revision from which this build of the
   * LDAP SDK was generated.  For a subversion repository, this will be the
   * revision number.  For a git repository (which uses a hexadecimal digest to
   * indicate revisions), or if repository information could not be determined
   * at build time, the value will be -1.  For this build, the value is
   * -1.
   *
   * @deprecated  Use the {@link #REVISION_ID} property instead, since it can
   *              handle non-numeric revision identifiers.
   */
  @Deprecated()
  public static final long REVISION_NUMBER = -1;



  /**
   * The full version string for the LDAP SDK.  For this build, the value is
   * "UnboundID LDAP SDK for Java 6.0.0".
   */
  public static final String FULL_VERSION_STRING =
       PRODUCT_NAME + ' ' + MAJOR_VERSION + '.' + MINOR_VERSION + '.' +
       POINT_VERSION + VERSION_QUALIFIER;



  /**
   * The short version string for the LDAP SDK.  This will not have any spaces.
   * For this build, the value is
   * "unboundid-ldapsdk-6.0.0".
   */
  public static final String SHORT_VERSION_STRING =
       SHORT_NAME + '-' + MAJOR_VERSION + '.' + MINOR_VERSION + '.' +
       POINT_VERSION + VERSION_QUALIFIER;



  /**
   * The version number string for the LDAP SDK, which contains just the major,
   * minor, and point version, and optional version qualifier.  For this build,
   * the version string is
   * "6.0.0".
   */
  public static final String NUMERIC_VERSION_STRING =
       MAJOR_VERSION + "." + MINOR_VERSION + '.' +
       POINT_VERSION + VERSION_QUALIFIER;



  /**
   * Prevent this class from being instantiated.
   */
  private Version()
  {
    // No implementation is required.
  }



  /**
   * Prints version information from this class to standard output.
   *
   * @param  args  The command-line arguments provided to this program.
   */
  public static void main(final String... args)
  {
    for (final String line : getVersionLines())
    {
      System.out.println(line);
    }
  }



  /**
   * Retrieves the official full product name for the LDAP SDK.  For this build,
   * the value is "UnboundID LDAP SDK for Java".
   *
   * @return  The official full product name for the LDAP SDK.
   */
  public static String getProductName()
  {
    return PRODUCT_NAME;
  }



  /**
   * Retrieves the short product name for the LDAP SDK.  This will not have any
   * spaces.  For this build, the value is "unboundid-ldapsdk".
   *
   * @return  The short product name for the LDAP SDK.
   */
  public static String getShortName()
  {
    return SHORT_NAME;
  }



  /**
   * Retrieves the major version number for the LDAP SDK.  For this build, the
   * value is 6.
   *
   * @return  The major version number for the LDAP SDK.
   */
  public static int getMajorVersion()
  {
    return MAJOR_VERSION;
  }



  /**
   * Retrieves the minor version number for the LDAP SDK.  For this build, the
   * value is 0.
   *
   * @return  The minor version number for the LDAP SDK.
   */
  public static int getMinorVersion()
  {
    return MINOR_VERSION;
  }



  /**
   * Retrieves the point version number for the LDAP SDK.  For this build, the
   * value is 0.
   *
   * @return  The point version number for the LDAP SDK.
   */
  public static int getPointVersion()
  {
    return POINT_VERSION;
  }



  /**
   * Retrieves the version qualifier string for the LDAP SDK.  It will often be
   * a zero-length string, but may be non-empty for special builds that should
   * be tagged in some way (e.g., "-beta1" or "-rc2"). For this build, the value
   * is "".
   *
   * @return  The version qualifier string for the LDAP SDK.
   */
  public static String getVersionQualifier()
  {
    return VERSION_QUALIFIER;
  }



  /**
   * Retrieves a timestamp that indicates when this build of the LDAP SDK was
   * generated.  For this build, the value is "20210408162531Z".
   *
   * @return  A timestamp that indicates when this build of the LDAP SDK was
   *          generated.
   */
  public static String getBuildTimestamp()
  {
    return BUILD_TIMESTAMP;
  }



  /**
   * Retrieves the type of repository from which the source code used to build
   * the LDAP SDK was retrieved.  It will be one of "subversion", "git", or
   * "{unknown}".  For this build, the value is "git".
   *
   * @return  The type of repository from which the source code used to build
   *          the LDAP SDK was retrieved.
   */
  public static String getRepositoryType()
  {
    return REPOSITORY_TYPE;
  }



  /**
   * Retrieves the URL for the repository from which the source code used to
   * build the LDAP SDK was retrieved.  If repository information could not be
   * determined at build time, then this will be a file URL that references the
   * path to the source code on the system used to build the LDAP SDK library.
   * For this build, the value is
   * "https://github.com/pingidentity/ldapsdk.git".
   *
   * @return  The URL for the repository from which the source code used to
   *          build the LDAP SDK was retrieved.
   */
  public static String getRepositoryURL()
  {
    return REPOSITORY_URL;
  }



  /**
   * Retrieves the path to the LDAP SDK source code in the repository.  If
   * repository information could not be determined at build time, then this
   * will be "/".  For this build, the value is
   * "/".
   *
   * @return  The path to the LDAP SDK source code in the repository.
   */
  public static String getRepositoryPath()
  {
    return REPOSITORY_PATH;
  }



  /**
   * Retrieves the string representation of the source revision from which this
   * build of the LDAP SDK was generated.  For a subversion repository, this
   * will be the string representation of the revision number.  For a git
   * repository, this will be the hexadecimal representation of the digest for
   * the most recent commit.  If repository information could not be determined
   * at build time, the value will be "{unknown}".  For this build, the value is
   * "7df578770ca7f0408f2788930f743add535f9680".
   *
   * @return  The string representation of the source revision from which this
   *          build of the LDAP SDK was generated.
   */
  public static String getRevisionID()
  {
    return REVISION_ID;
  }



  /**
   * Retrieves the full version string for the LDAP SDK.  For this build, the
   * value is
   * "UnboundID LDAP SDK for Java 6.0.0".
   *
   * @return  The full version string for the LDAP SDK.
   */
  public static String getFullVersionString()
  {
    return FULL_VERSION_STRING;
  }



  /**
   * Retrieves the short version string for the LDAP SDK.  This will not have
   * any spaces.  For this build, the value is
   * "unboundid-ldapsdk-6.0.0".
   *
   * @return  The short version string for the LDAP SDK.
   */
  public static String getShortVersionString()
  {
    return SHORT_VERSION_STRING;
  }



  /**
   * Retrieves the version number string for the LDAP SDK, which contains just
   * the major, minor, and point version, and optional version qualifier.  For
   * this build, the version string is
   * "6.0.0".
   *
   * @return  The version number string for the LDAP SDK.
   */
  public static String getNumericVersionString()
  {
    return NUMERIC_VERSION_STRING;
  }



  /**
   * Retrieves a list of lines containing information about the LDAP SDK
   * version.
   *
   * @return  A list of lines containing information about the LDAP SDK
   *          version.
   */
  public static List<String> getVersionLines()
  {
    final ArrayList<String> versionLines = new ArrayList<>(11);

    versionLines.add("Full Version String:   " + FULL_VERSION_STRING);
    versionLines.add("Short Version String:  " + SHORT_VERSION_STRING);
    versionLines.add("Product Name:          " + PRODUCT_NAME);
    versionLines.add("Short Name:            " + SHORT_NAME);
    versionLines.add("Major Version:         " + MAJOR_VERSION);
    versionLines.add("Minor Version:         " + MINOR_VERSION);
    versionLines.add("Point Version:         " + POINT_VERSION);
    versionLines.add("Version Qualifier:     " + VERSION_QUALIFIER);
    versionLines.add("Build Timestamp:       " + BUILD_TIMESTAMP);
    versionLines.add("Repository Type:       " + REPOSITORY_TYPE);
    versionLines.add("Repository URL:        " + REPOSITORY_URL);
    versionLines.add("Repository Path:       " + REPOSITORY_PATH);
    versionLines.add("Revision:              " + REVISION_ID);

    return Collections.unmodifiableList(versionLines);
  }
}
