/*
 * Distributed as part of mchange-commons-java 0.2.7
 *
 * Copyright (C) 2014 Machinery For Change, Inc.
 *
 * Author: Steve Waldman <swaldman@mchange.com>
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of EITHER:
 *
 *     1) The GNU Lesser General Public License (LGPL), version 2.1, as 
 *        published by the Free Software Foundation
 *
 * OR
 *
 *     2) The Eclipse Public License (EPL), version 1.0
 *
 * You may choose which license to accept if you wish to redistribute
 * or modify this work. You may offer derivatives of this work
 * under the license you have chosen, or you may provide the same
 * choice of license which you have been offered here.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received copies of both LGPL v2.1 and EPL v1.0
 * along with this software; see the files LICENSE-EPL and LICENSE-LGPL.
 * If not, the text of these licenses are currently available at
 *
 * LGPL v2.1: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 *  EPL v1.0: http://www.eclipse.org/org/documents/epl-v10.php 
 * 
 */

package com.mchange.v1.identicator.test;

import java.util.*;
import com.mchange.v1.identicator.*;


public class TestIdWeakHashMap
{
    final static Identicator id = new Identicator()
    {
	public boolean identical(Object a, Object b)
	{ return ((String) a).charAt(0) == ((String) b).charAt(0); }
	
	public int hash(Object o)
	{ return ((String) o).charAt(0); }
    };

    final static Map weak = new IdWeakHashMap( id );

    public static void main(String[] argv)
    {
	doAdds();
	System.gc();
	show();
	setRemoveHi();
	System.gc();
	show();
    }

    static void setRemoveHi()
    {
	String bye = new String("bye");
	weak.put(bye, "");
	Set ks = weak.keySet();
	ks.remove("hi");
	show();
    }

    static void doAdds()
    {
	String s0 = "hi"; //remember, this one is in the internal string pool!
	String s1 = new String("hello");
	String s2 = new String("yoohoo");
	String s3 = new String("poop");

	weak.put(s0, "");
	weak.put(s1, "");
	weak.put(s2, "");
	weak.put(s3, "");

	show();
    }

    static void show()
    {
	System.out.println("elements:");
	for (Iterator ii = weak.keySet().iterator(); ii.hasNext(); )
	    System.out.println( "\t" + ii.next() );

	System.out.println("size: " + weak.size() );
    }
}

