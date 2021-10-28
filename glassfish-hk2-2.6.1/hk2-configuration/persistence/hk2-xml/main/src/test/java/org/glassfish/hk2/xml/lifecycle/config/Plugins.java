/*
 * Copyright (c) 2014, 2018 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.hk2.xml.lifecycle.config;

import java.beans.PropertyVetoException;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public interface Plugins {

  @XmlElement(name="*")
  List<Plugin> getPlugins();
  void setPlugins(List<Plugin> plugins);

  /*
  @DuckTyped
  Plugin createPlugin(String name, String type, String path);

  @DuckTyped
  Plugin getPluginByName(String name);

  @DuckTyped
  Plugin deletePlugin(Plugin plugin);

  class Duck  {
    public static Plugin createPlugin(final Plugins plugins, final String name, final String type, final String path) throws TransactionFailure {
      ConfigSupport.apply(new SingleConfigCode<Plugins>() {
        @Override
        public Object run(Plugins writeablePlugins) throws TransactionFailure, PropertyVetoException {
          Plugin plugin = writeablePlugins.createChild(Plugin.class);
          plugin.setName(name);
          plugin.setType(type);
          plugin.setPath(path);
          writeablePlugins.getPlugins().add(plugin);
          return plugin;
        }
      }, plugins);

      // read-only view
      return getPluginByName(plugins, name);
    }

    public static Plugin getPluginByName(final Plugins plugins, final String name) {
      List<Plugin> pluginConfigsList = plugins.getPlugins();
      for (Plugin plugin : pluginConfigsList) {
        if (name.equals(plugin.getName())) {
          return plugin;
        }
      }
      return null;
    }

    public static Plugin deletePlugin(final Plugins plugins,
        final Plugin plugin) throws TransactionFailure {
      return (Plugin) ConfigSupport.apply(new SingleConfigCode<Plugins>() {

        @Override
        public Object run(Plugins writeablePlugins)
            throws TransactionFailure {
          writeablePlugins.getPlugins().remove(plugin);
          return plugin; 
        }

      }, plugins);
    
    }

  }
  */
}
