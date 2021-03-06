/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

/*
 * Copyright 2004-2005 Sun Microsystems, Inc.  All rights reserved.
 * Use is subject to license terms.
 */

/* MutableTimeStatistic.java
 * $Id: MutableTimeStatistic.java,v 1.2 2005/12/25 03:52:20 tcfujii Exp $
 * $Revision: 1.2 $
 * $Date: 2005/12/25 03:52:20 $
 */


package com.sun.enterprise.admin.monitor.stats;

/**
 * Provides mutable nature to the {@link TimeStatistic}. Like other MutableStatistic
 * interfaces, it does have the state to calculate the specific statistical data
 * in {@link TimeStatistic}. Unlike other mutable statistics though, in this interface
 * there is some consideration of a sequence. Once an instance of this interface
 * is created, subsequent call to {@link #incrementCount} has a twofold effect:
 * <ul>
 *   <li> Increments the count for number of times the operation is executed by 1 </li>
 *   <li> Keeps a count of maximum/minimum/total execution time </li>
 * <ul>
 * @author  <a href="mailto:Kedar.Mhaswade@sun.com">Kedar Mhaswade</a>
 * @since S1AS8.0
 * @version $Revision: 1.2 $
 */
public interface MutableTimeStatistic extends MutableStatistic {
    
    /**
     * Increments the count for number of times an operation is called by 1 and 
     * processes the given parameter in a certain manner. 
     */
    public void incrementCount(long currentExecutionTimeMillis);
}
