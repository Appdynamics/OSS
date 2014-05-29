/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.admin.rest.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jvnet.hk2.component.Habitat;

/**
 * @author Mitesh Meswani
 */
public class TextResourcesGenerator extends ResourcesGeneratorBase {

    /* The absolute path to dir where resources are generated */
    private File generationDir;

    public TextResourcesGenerator(String outputDir, Habitat habitat) {
        super(habitat);
        generationDir= new File(outputDir);
        generationDir.mkdirs();
    }
    
    @Override
    public ClassWriter getClassWriter(String className, String baseClassName, String resourcePath) {
        ClassWriter writer = null;
        try {
            writer = new TextClassWriter( habitat ,generationDir, className, baseClassName, resourcePath);
        } catch (IOException e) {
            // Log the root cause. The generation is going to fail with NPE.
            Logger.getLogger(TextResourcesGenerator.class.getName()).log(Level.SEVERE, e.getMessage());
            throw new GeneratorException(e);
        }
        return writer;
    }

    @Override
    public String endGeneration() {
        //generate date info in 1 single file
        File file = new File(generationDir+ "/codegeneration.properties");
        try {
            file.createNewFile();
            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("generation_date=" + new Date() + "\n");
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return  "Code Generation done at : " + generationDir;

    }
}
