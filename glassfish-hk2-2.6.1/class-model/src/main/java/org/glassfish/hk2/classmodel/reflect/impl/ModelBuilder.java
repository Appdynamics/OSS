/*
 * Copyright (c) 2010, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.hk2.classmodel.reflect.impl;

import org.glassfish.hk2.classmodel.reflect.*;
import org.objectweb.asm.Opcodes;
import java.net.URI;
import java.util.Collection;

/**
 * convenient class to build model implementations
 */
class ModelBuilder {

    public final String name;
    public final TypeProxy sink;
    public final TypeProxy parent;
    public final URI definingURI;

    public ModelBuilder(String name, TypeProxy sink, URI definingURI, TypeProxy parent) {
        this.name = name;
        this.sink = sink;
        this.definingURI = definingURI;
        this.parent = parent;
    }
}
