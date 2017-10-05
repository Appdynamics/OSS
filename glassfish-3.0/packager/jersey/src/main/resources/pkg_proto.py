#!/usr/bin/env python
# -*- coding: utf-8 -*-
#
# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
#
# Copyright 2008 Sun Microsystems, Inc. All rights reserved.
#
# The contents of this file are subject to the terms of either the GNU
# General Public License Version 2 only ("GPL") or the Common Development
# and Distribution License("CDDL") (collectively, the "License").  You
# may not use this file except in compliance with the License. You can obtain
# a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
# or updatetool/LICENSE.txt.  See the License for the specific
# language governing permissions and limitations under the License.
#
# When distributing the software, include this License Header Notice in each
# file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
# Sun designates this particular file as subject to the "Classpath" exception
# as provided by Sun in the GPL Version 2 section of the License file that
# accompanied this code.  If applicable, add the following below the License
# Header, with the fields enclosed by brackets [] replaced by your own
# identifying information: "Portions Copyrighted [year]
# [name of copyright owner]"
#
# Contributor(s):
#
# If you wish your version of this file to be governed by only the CDDL or
# only the GPL Version 2, indicate your decision by adding "[Contributor]
# elects to include this software in this distribution under the [CDDL or GPL
# Version 2] license."  If you don't indicate a single choice of license, a
# recipient has the option to distribute your version of this file under
# either the CDDL, the GPL Version 2 or to extend the choice of license to
# its licensees as provided above.  However, if you add GPL Version 2 code
# and therefore, elected the GPL Version 2 license, then the option applies
# only if the new code is made subject to such option by the copyright
# holder.
#

import imp

conf = imp.load_source("pkg_conf", "../pkg_conf.py")

pkg = {
    "name"          : "jersey",
    "version"       : "1.1.4.1,0-1.0",
    "attributes"    : { "pkg.summary" : "Jersey Core, RESTful Web services for GlassFish",
                        "pkg.description" : 
"Jersey core runtime libraries including some 3rd party dependencies. \
 Documentation and examples of Jersey were moved to a separate package, Jersey Examples And Documentation. \
 If you are about to upgrade Jersey, you might want to install that new package as well, otherwise Jersey examples and javadocs will get deleted. \
 Also please note, that from 1.0.3 version on, Jersey docs and examples get installed into <as_home>/glassfish/jersey directory instead of just <as_home>/jersey. \
 Jersey is the open source (under dual CDDL+GPL license)\
 JAX-RS (JSR 311) Reference Implementation for building RESTful Web services. \
 But, it is also more than the Reference Implementation. \
 Jersey provides additional APIs and extension points (SPIs) \
 so that developers may extend Jersey to suite their needs.",
                     "info.classification" : "Web Services"  },
    "dirtrees" : [ "glassfish"],
    "depends" : { 
                  "pkg:/glassfish-common@3.0" : {"type" : "require" }
                },
    "licenses" : { "../../../../CDDL+GPL.txt" : {"license" : "CDDL and GPL v2 with classpath exception"}}
}
