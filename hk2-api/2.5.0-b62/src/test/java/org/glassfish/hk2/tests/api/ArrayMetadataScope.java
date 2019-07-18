/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2015 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.hk2.tests.api;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Scope;

import org.glassfish.hk2.api.Metadata;

/**
 * @author jwells
 *
 */
@Scope
@Retention(RUNTIME)
@Target( { TYPE, METHOD, FIELD, PARAMETER })
public @interface ArrayMetadataScope {
    public static final String STRING_KEY = "ScopeStringArray";
    public static final String BYTE_KEY = "ScopeByteArray";
    public static final String SHORT_KEY = "ScopeShortArray";
    public static final String INT_KEY = "ScopeIntArray";
    public static final String CHAR_KEY = "ScopeCharArray";
    public static final String LONG_KEY = "ScopeLongArray";
    public static final String CLASS_KEY = "ScopeClassArray";
    public static final String FLOAT_KEY = "ScopeFloatArray";
    public static final String DOUBLE_KEY = "ScopeDoubleArray";
    
    @Metadata(STRING_KEY)
    public String[] getString();
    
    @Metadata(BYTE_KEY)
    public byte[] getByte();
    
    @Metadata(SHORT_KEY)
    public short[] getShort();
    
    @Metadata(INT_KEY)
    public int[] getInt();
    
    @Metadata(CHAR_KEY)
    public char[] getChar();
    
    @Metadata(LONG_KEY)
    public long[] getLong();
    
    @Metadata(CLASS_KEY)
    public Class<?>[] getClasses();
    
    @Metadata(FLOAT_KEY)
    public float[] getFloat();
    
    @Metadata(DOUBLE_KEY)
    public double[] getDouble();

}
