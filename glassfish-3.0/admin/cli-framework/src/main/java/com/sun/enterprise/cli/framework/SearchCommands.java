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

package com.sun.enterprise.cli.framework;

import java.util.Iterator;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

/**
 *    SearchCommands.java
 *    This class cannot be instantiated.
 *    Given a pattern, this class will search for
 *    the commands that match the pattern.
 *    @version  $Revision: 1.2 $
 */
public class SearchCommands
{
    private Map<String, String> allCommandsMap = null;

    
        //private constructor cannot be instantiated
    private SearchCommands() throws CommandValidationException
    {
        final CLIDescriptorsReader cdr = CLIDescriptorsReader.getInstance();
        final ValidCommandsList vcl = cdr.getCommandsList();
        final Iterator<ValidCommand> commands = vcl.getCommands();
        allCommandsMap = new Hashtable<String, String>();
        while (commands.hasNext())
        {
            final ValidCommand command = (ValidCommand)commands.next();
            //System.out.println("command = " + command.getName());
            final String  usageText = command.getUsageText();

            //Do not want to include the hiddden commands
            //the hidden commands have empty usageText
            if (usageText != null && usageText.length()>0)
                allCommandsMap.put(command.getName(), command.getUsageText());
        }
    }

        /**
         *  returns all the valid commands
         **/
    public static String[] getAllCommands()
        throws CommandValidationException
    {
        return new SearchCommands().allCommands();
    }

        /**
         *  returns commands that matches the pattern
         **/
    public static String[] getMatchedCommands(String pattern)
        throws CommandException, CommandValidationException
    {
        return new SearchCommands().findCommands(pattern);
    }

    public static String[] getMatchedCommands(String pattern, Map<String, String> moreCommands)
            throws CommandException, CommandValidationException {
        return new SearchCommands().findCommands(pattern, moreCommands);
    }
    

    private String[] allCommands()
    {
        final Set<String> set = allCommandsMap.keySet();
        return (String[])set.toArray(new String[set.size()]);
    }

    
    private String[] findCommands(final String pattern, Map<String, String> moreCommands)
        throws CommandException {
        if (moreCommands != null) {
            allCommandsMap.putAll(moreCommands);
        }
        return findCommands(pattern);
    }
    
    
    private String[] findCommands(final String pattern)
        throws CommandException
    {
        try 
        {
            List<String> allCommandsList = new Vector<String>(allCommandsMap.keySet());
                //sort commands in alphabetical order
            Collections.sort(allCommandsList);

            List<String> matchedCommands = new Vector();
            for (int ii=0; ii+1<allCommandsList.size();ii++)
            {
//                System.out.println("Matching pattern : " + allCommandsList.get(ii) + "  " + pattern);
                if (allCommandsList.get(ii).matches(pattern)) {
                    matchedCommands.add(allCommandsList.get(ii));
                }
            }
            return (String[])matchedCommands.toArray(new String[matchedCommands.size()]);
        }
        catch (PatternSyntaxException pse)
        {
            throw new CommandException("InvalidPattern", pse);
        }
        catch (Exception e){
            throw new CommandException(e);
        }
    }
}
    
    

