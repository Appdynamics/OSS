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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;

import org.jvnet.hk2.annotations.Contract;

/**
 * Mix-in interface that provides fields to track created and updated information.
 * There is no need to write these fields, or if written, they will be overwritten
 * upon transaction commit
 * See {@link AuditInterceptor} for hooking it up into HK2 config system, i.e.
 */
@Contract
public interface Auditable {
  @XmlAttribute
  void setCreatedOn(String date);
  String getCreatedOn();
  

  @XmlAttribute
  void setUpdatedOn(String date);
  String getUpdatedOn();
  
  /*
  @DuckTyped
  Date getCreatedOnDate();

  @DuckTyped
  Date getUpdatedOnDate();
  */

  /*
  class Duck {
    public static Date getCreatedOnDate(final Auditable auditable) {
      return date(auditable.getCreatedOn());
    }

    public static Date getUpdatedOnDate(final Auditable auditable) {
      return date(auditable.getUpdatedOn());
    }

    private static Date date(String dateString) {
      if (dateString != null) {
        try {
          // OWLS-13546: SimpleDateFormat is not thread safe - use a new instance each time
          DateFormat dateFormat = new SimpleDateFormat(AuditInterceptor.ISO_DATE_FORMAT);
          return dateFormat.parse(dateString);
        } catch (ParseException e) {
          throw new RuntimeException(e);
        }
      }
      return null;
    }

  }
  */
}
