/*
 * Copyright (c) 2015, 2018 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.hk2.xml.test.customizer;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jvnet.hk2.annotations.Contract;

/**
 * @author jwells
 *
 */
@Contract @Singleton
public class CustomizerOne implements CustomOne, CustomTwo {
    @Inject
    private MuseumBean customized;
    
    private boolean customizer2Called = false;
    private boolean fauxAddCalled = false;
    
    public String customizer1(String prefix, String postfix) {
        return prefix + customized.getName() + postfix;
    }
    
    public void customizer2() {
        customizer2Called = true;
    }
    
    public boolean getCustomizer2Called() {
        return customizer2Called;
    }
    
    public long[] customizer3(String[][] anArray) {
        return new long[0];
    }
    
    public boolean customizer4() {
        return CustomizerTest.C4;
    }
    public int customizer5(){
        return CustomizerTest.C5;
    }
    public long customizer6(){
        return CustomizerTest.C6;
    }
    public float customizer7(){
        return CustomizerTest.C7;
    }
    public double customizer8(){
        return CustomizerTest.C8;
    }
    
    public byte customizer9(){
        return CustomizerTest.C9;
    }
    public short customizer10(){
        return CustomizerTest.C10;
    }
    public char customizer11(){
        return CustomizerTest.C11;
    }
    
    public int customizer12(boolean z, int i, long j, float f, double d, byte b, short s, char c, int[]... var) {
        return var.length;
    }
    
    public String[] toUpper(String lowers[]) {
        if (lowers == null) return null;
        
        String[] retVal = new String[lowers.length];
        
        int lcv = 0;
        for (String lower : lowers) {
            retVal[lcv++] = lower.toUpperCase();
        }
        
        return retVal;
    }

    public void addListener(BeanListener listener) {
        fauxAddCalled = true;
    }
    
    public boolean getFauxAddCalled() {
        return fauxAddCalled;
    }
}
