/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2004-2014 Oracle and/or its affiliates. All rights reserved.
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

package javax.xml.bind.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * <p>
 * Maps a JavaBean property to XML IDREF.
 * 
 * <p>
 * To preserve referential integrity of an object graph across XML
 * serialization followed by a XML deserialization, requires an object
 * reference to be marshalled by reference or containment
 * appropriately. Annotations <tt>&#64;XmlID</tt> and <tt>&#64;XmlIDREF</tt>
 * together allow a customized mapping of a JavaBean property's
 * type by containment or reference. 
 *
 * <p><b>Usage</b> </p>
 * The <tt>&#64;XmlIDREF</tt> annotation can be used with the following
 * program elements: 
 * <ul> 
 *   <li> a JavaBean property </li>
 *   <li> non static, non transient field </li>
 * </ul>
 * 
 * <p>See "Package Specification" in javax.xml.bind.package javadoc for
 * additional common information.</p>
 *
 * <p> The usage is subject to the following constraints:
 * <ul>
 *
 *   <li> If the type of the field or property is a collection type,
 *        then the collection item type must contain a property or
 *        field annotated with <tt>&#64;XmlID</tt>.  </li> 
 *   <li> If the field or property is single valued, then the type of
 *        the property or field must contain a property or field
 *        annotated with <tt>&#64;XmlID</tt>.
 *        <p>Note: If the collection item type or the type of the
 *        property (for non collection type) is java.lang.Object, then
 *        the instance must contain a property/field annotated with
 *        <tt>&#64;XmlID</tt> attribute.  
 *        </li>
 *   <li> This annotation can be used with the following annotations:
 *        {@link XmlElement}, {@link XmlAttribute}, {@link XmlList}, 
 *        and {@link XmlElements}.</li>  
 *
 * </ul>
 * <p><b>Example:</b> Map a JavaBean property to <tt>xs:IDREF</tt>
 *   (i.e. by reference rather than by containment)</p>
 * <pre>
 *
 *   //EXAMPLE: Code fragment
 *   public class Shipping {
 *       &#64;XmlIDREF public Customer getCustomer();
 *       public void setCustomer(Customer customer);
 *       ....
 *    }
 *
 *   &lt;!-- Example: XML Schema fragment --&gt;
 *   &lt;xs:complexType name="Shipping"&gt;
 *     &lt;xs:complexContent&gt;
 *       &lt;xs:sequence&gt;
 *         &lt;xs:element name="customer" type="xs:IDREF"/&gt;
 *         ....
 *       &lt;/xs:sequence&gt;
 *     &lt;/xs:complexContent&gt;
 *   &lt;/xs:complexType&gt;
 *
 * </pre>
 *
 *
 * <p><b>Example 2: </b> The following is a complete example of
 * containment versus reference.
 * 
 * <pre>
 *    // By default, Customer maps to complex type <tt>xs:Customer</tt>
 *    public class Customer {
 *        
 *        // map JavaBean property type to <tt>xs:ID</tt>
 *        &#64;XmlID public String getCustomerID();
 *        public void setCustomerID(String id);
 *
 *        // .... other properties not shown 
 *    }
 *
 *
 *   // By default, Invoice maps to a complex type <tt>xs:Invoice</tt> 
 *   public class Invoice {
 *    
 *       // map by reference
 *       &#64;XmlIDREF public Customer getCustomer();       
 *       public void setCustomer(Customer customer);
 *
 *      // .... other properties not shown here
 *   }
 *
 *   // By default, Shipping maps to complex type <tt>xs:Shipping</tt>
 *   public class Shipping {
 *
 *       // map by reference
 *       &#64;XmlIDREF public Customer getCustomer();       
 *       public void setCustomer(Customer customer);
 *   }
 *
 *   // at least one class must reference Customer by containment;
 *   // Customer instances won't be marshalled.
 *   &#64;XmlElement(name="CustomerData")
 *   public class CustomerData {
 *       // map reference to Customer by containment by default.
 *       public Customer getCustomer();
 *
 *       // maps reference to Shipping by containment by default. 
 *       public Shipping getShipping();     
 *
 *       // maps reference to Invoice by containment by default. 
 *       public Invoice getInvoice();     
 *   }
 *
 *   &lt;!-- XML Schema mapping for above code frament --&gt;
 *
 *   &lt;xs:complexType name="Invoice"&gt;
 *     &lt;xs:complexContent&gt;
 *       &lt;xs:sequence&gt;
 *         &lt;xs:element name="customer" type="xs:IDREF"/&gt;
 *         ....
 *       &lt;/xs:sequence&gt;
 *     &lt;/xs:complexContent&gt;
 *   &lt;/xs:complexType&gt;
 *
 *   &lt;xs:complexType name="Shipping"&gt;
 *     &lt;xs:complexContent&gt;
 *       &lt;xs:sequence&gt;
 *         &lt;xs:element name="customer" type="xs:IDREF"/&gt;
 *         ....
 *       &lt;/xs:sequence&gt;
 *     &lt;/xs:complexContent&gt;
 *   &lt;/xs:complexType&gt;
 *
 *   &lt;xs:complexType name="Customer"&gt;
 *     &lt;xs:complexContent&gt;
 *       &lt;xs:sequence&gt;
 *         ....
 *       &lt;/xs:sequence&gt;
 *       &lt;xs:attribute name="CustomerID" type="xs:ID"/&gt;
 *     &lt;/xs:complexContent&gt;
 *   &lt;/xs:complexType&gt;
 *
 *   &lt;xs:complexType name="CustomerData"&gt;
 *     &lt;xs:complexContent&gt;
 *       &lt;xs:sequence&gt;
 *         &lt;xs:element name="customer" type="xs:Customer"/&gt;
 *         &lt;xs:element name="shipping" type="xs:Shipping"/&gt;
 *         &lt;xs:element name="invoice"  type="xs:Invoice"/&gt;
 *       &lt;/xs:sequence&gt;
 *     &lt;/xs:complexContent&gt;
 *   &lt;/xs:complexType&gt;
 *
 *   &lt;xs:element name"customerData" type="xs:CustomerData"/&gt;
 *
 *   &lt;!-- Instance document conforming to the above XML Schema --&gt;
 *    &lt;customerData&gt;
 *       &lt;customer customerID="Alice"&gt;
 *           ....
 *       &lt;/customer&gt;
 *
 *       &lt;shipping customer="Alice"&gt;
 *           ....
 *       &lt;/shipping&gt;
 *         
 *       &lt;invoice customer="Alice"&gt;
 *           ....
 *       &lt;/invoice&gt;
 *   &lt;/customerData&gt;
 *
 * </pre>
 *
 * <p><b>Example 3: </b> Mapping List to repeating element of type IDREF
 * <pre>
 *     // Code fragment
 *     public class Shipping {
 *         &#64;XmlIDREF
 *         &#64;XmlElement(name="Alice")
 *             public List customers;
 *     }
 *
 *     &lt;!-- XML schema fragment --&gt;
 *     &lt;xs:complexType name="Shipping"&gt;
 *       &lt;xs:sequence&gt;
 *         &lt;xs:choice minOccurs="0" maxOccurs="unbounded"&gt;
 *           &lt;xs:element name="Alice" type="xs:IDREF"/&gt;
 *         &lt;/xs:choice&gt;
 *       &lt;/xs:sequence&gt;
 *     &lt;/xs:complexType&gt;
 * </pre>
 *
 * <p><b>Example 4: </b> Mapping a List to a list of elements of type IDREF.
 * <pre>
 *     //Code fragment
 *     public class Shipping {
 *         &#64;XmlIDREF
 *         &#64;XmlElements(
 *             &#64;XmlElement(name="Alice", type="Customer.class")
 *              &#64;XmlElement(name="John", type="InternationalCustomer.class")
 *         public List customers;
 *     }
 *
 *     &lt;!-- XML Schema fragment --&gt;
 *     &lt;xs:complexType name="Shipping"&gt;
 *       &lt;xs:sequence&gt;
 *         &lt;xs:choice minOccurs="0" maxOccurs="unbounded"&gt;
 *           &lt;xs:element name="Alice" type="xs:IDREF"/&gt;
 *           &lt;xs:element name="John" type="xs:IDREF"/&gt;
 *         &lt;/xs:choice&gt;
 *       &lt;/xs:sequence&gt;
 *     &lt;/xs:complexType&gt;
 * </pre>
 * @author Sekhar Vajjhala, Sun Microsystems, Inc. 
 * @see XmlID
 * @since 1.6, JAXB 2.0
 */

@Retention(RUNTIME) @Target({FIELD, METHOD})
public @interface XmlIDREF {}
