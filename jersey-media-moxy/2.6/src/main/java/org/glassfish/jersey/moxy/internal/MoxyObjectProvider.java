/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
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

package org.glassfish.jersey.moxy.internal;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.glassfish.jersey.message.filtering.spi.AbstractObjectProvider;
import org.glassfish.jersey.message.filtering.spi.ObjectGraph;

import org.eclipse.persistence.jaxb.JAXBContext;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.JAXBHelper;
import org.eclipse.persistence.jaxb.Subgraph;
import org.eclipse.persistence.jaxb.TypeMappingInfo;

import jersey.repackaged.com.google.common.collect.Sets;

/**
 * @author Michal Gajdos (michal.gajdos at oracle.com)
 */
final class MoxyObjectProvider extends AbstractObjectProvider<org.eclipse.persistence.jaxb.ObjectGraph> {

    private final static JAXBContext JAXB_CONTEXT;

    static {
        try {
            JAXB_CONTEXT = JAXBHelper.getJAXBContext(
                    JAXBContextFactory.createContext(new TypeMappingInfo[]{}, Collections.emptyMap(), null));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public org.eclipse.persistence.jaxb.ObjectGraph transform(final ObjectGraph graph) {
        return createObjectGraph(graph.getEntityClass(), graph);
    }

    private org.eclipse.persistence.jaxb.ObjectGraph createObjectGraph(final Class<?> entityClass, final ObjectGraph objectGraph) {
        final org.eclipse.persistence.jaxb.ObjectGraph graph = JAXB_CONTEXT.createObjectGraph(entityClass);
        final Set<String> fields = objectGraph.getFields();

        if (!fields.isEmpty()) {
            graph.addAttributeNodes(fields.toArray(new String[fields.size()]));
        }

        final Map<String, ObjectGraph> subgraphs = objectGraph.getSubgraphs();
        if (!subgraphs.isEmpty()) {
            createSubgraphs(graph, objectGraph.getEntityClass(), subgraphs);
        }

        return graph;
    }

    private void createSubgraphs(final org.eclipse.persistence.jaxb.ObjectGraph graph,
                                 final Class<?> entityClass, final Map<String, ObjectGraph> entitySubgraphs) {
        final Set<String> processed = Sets.newHashSet();

        for (final Map.Entry<String, ObjectGraph> entry : entitySubgraphs.entrySet()) {
            final Subgraph subgraph = graph.addSubgraph(entry.getKey());
            final ObjectGraph entityGraph = entry.getValue();

            final Set<String> fields = entityGraph.getFields(entry.getKey());
            if (!fields.isEmpty()) {
                subgraph.addAttributeNodes(fields.toArray(new String[fields.size()]));
            }

            final Map<String, ObjectGraph> subgraphs = entityGraph.getSubgraphs(entry.getKey());
            if (!subgraphs.isEmpty()) {
                final Class<?> subEntityClass = entityGraph.getEntityClass();

                processed.add(getProcessedSubgraph(entityClass, entry.getKey(), subEntityClass));
                createSubgraphs(entry.getKey(), subgraph, subEntityClass, subgraphs, processed);
            }
        }
    }

    private void createSubgraphs(final String parent, final Subgraph graph, final Class<?> entityClass,
                                 final Map<String, ObjectGraph> entitySubgraphs, final Set<String> processed) {
        for (final Map.Entry<String, ObjectGraph> entry : entitySubgraphs.entrySet()) {
            final Subgraph subgraph = graph.addSubgraph(entry.getKey());
            final ObjectGraph entityGraph = entry.getValue();
            
            String path = parent + "." + entry.getKey();

            final Set<String> fields = entityGraph.getFields(path);
            if (!fields.isEmpty()) {
                subgraph.addAttributeNodes(fields.toArray(new String[fields.size()]));
            }

            final Map<String, ObjectGraph> subgraphs = entityGraph.getSubgraphs();
            final Class<?> subEntityClass = entityGraph.getEntityClass();
            final String processedSubgraph = getProcessedSubgraph(entityClass, entry.getKey(), subEntityClass);

            if (!subgraphs.isEmpty() && !processed.contains(processedSubgraph)) {
                processed.add(processedSubgraph);
                createSubgraphs(path, subgraph, subEntityClass, subgraphs, processed);
            }
        }
    }

    private String getProcessedSubgraph(final Class<?> parent, final String field, final Class<?> fieldClass) {
        return parent.getName() + "_" + field + "_" + fieldClass.getName();
    }
}
