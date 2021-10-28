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

import org.jvnet.hk2.annotations.Contract;

@Contract
public interface Tenants {
  @XmlElement(name="tenant")
  List<Tenant> getTenants();
  void setTenants(List<Tenant> tenants);
  
  /*
  @DuckTyped
  Tenant createTenant(String name, String id, String topLevelDir);
  
  @DuckTyped
  Tenant createTenant(String name, String id);
   
  @DuckTyped
  Tenant deleteTenant(Tenant tenant);
  
  @DuckTyped
  Tenant getTenant(String name, String id);
  
  @DuckTyped
  Tenant getTenant(String id);
  
  @DuckTyped
  Tenant getTenantById(String id);

  @DuckTyped
  Tenant getTenantByName(String name);

  @DuckTyped
  Tenant getTenantForPartition(String partitionName, String partitionId);
  
  @DuckTyped
  Tenant getTenantForPDB(String pdbName, String pdbId);

  @DuckTyped
  Service getServiceById(String serviceId);

  @NotNull
  @Element
  Resources getResources();
  void setResources(Resources resources);

  class Duck  {
    public static Tenant createTenant(final Tenants tenantMapping, 
            final String name, final String id) 
            throws TransactionFailure {
        return createTenant(tenantMapping, name, id, null);
    }  
      
    public static Tenant createTenant(final Tenants tenantMapping, 
            final String name, final String id, final String topLevelDir) 
            throws TransactionFailure {
      return (Tenant) ConfigSupport.apply(new SingleConfigCode<Tenants>() {
        @Override
        public Object run(Tenants writeableTenantMapping) throws TransactionFailure, PropertyVetoException {
          Tenant tenant = writeableTenantMapping.createChild(Tenant.class);
          tenant.setName(name);
          tenant.setId(id);
          if (topLevelDir != null) tenant.setTopLevelDir(topLevelDir);
          writeableTenantMapping.getTenants().add(tenant);
          return tenant;
        }
      }, tenantMapping);
    }
    
    public static Tenant deleteTenant(final Tenants tenantMapping,
        final Tenant tenant) throws TransactionFailure {
      return (Tenant) ConfigSupport.apply(new SingleConfigCode<Tenants>() {

        @Override
        public Object run(Tenants writeableTenantMapping)
            throws TransactionFailure {
          writeableTenantMapping.getTenants().remove(tenant);
          return tenant; 
        }

      }, tenantMapping);
    }
    
    public static Tenant getTenant(final Tenants tenantMapping,
              final String name, final String id) {
      Tenant tenant = null;
      for (Tenant t : tenantMapping.getTenants()) {
        if (t.getName().equals(name) && t.getId().equals(id)) {
          tenant = t;
          break;
        }
      }
      return tenant;
    }
    
    public static Tenant getTenant(final Tenants tenantMapping,
              final String id) {
      Tenant tenant = null;
      for (Tenant t : tenantMapping.getTenants()) {
        if (t.getId().equals(id)) {
          tenant = t;
          break;
        }
      }
      return tenant;
    }
    
    public static Tenant getTenantByName(final Tenants tenantMapping,
              final String name) {
      Tenant tenant = null;
      for (Tenant t : tenantMapping.getTenants()) {
        if (t.getName().equals(name)) {
          tenant = t;
          break;
        }
      }
      return tenant;
    }
    
    public static Tenant getTenantForPartition(final Tenants tenantMapping,
        final String partitionId) {
      long inputPartitionId = Long.parseLong(partitionId);
      
      for (Tenant tenant : tenantMapping.getTenants()) {
        LifecycleConfig lifecycleConfig = tenantMapping.getParent(LifecycleConfig.class);
        List<Runtime> runtimeConfigs = lifecycleConfig.getRuntimes().getRuntimes();
        for (Runtime runtimeConfig : runtimeConfigs) {
          for (Partition partition : runtimeConfig.getPartitions()) {
            long candidateId = Long.parseLong(partition.getId());
            if (candidateId == inputPartitionId ) {
              return tenant;
            }
          }
        }
      }
      return null;
    }
    
    public static Tenant getTenantForPDB(final Tenants tenantMapping, 
        final String pdbId) {
      for (Tenant tenant : tenantMapping.getTenants()) {
        Service service = tenant.getServiceByPDBId(pdbId);
        if (service != null) {
          return tenant;
        }
      }
      return null;
    }

    public static Service getServiceById(final Tenants tenants, final String id) {
      for (Tenant tenant : tenants.getTenants()) {
        Service service = tenant.getServiceById(id);
        if (service != null) {
            return service;
        }
      }
      return null;
    }
  }
  */
}
