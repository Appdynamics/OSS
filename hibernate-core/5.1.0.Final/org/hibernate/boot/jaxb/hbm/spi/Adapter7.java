//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.10 at 11:20:59 AM CST 
//


package org.hibernate.boot.jaxb.hbm.spi;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.hibernate.tuple.GenerationTiming;

public class Adapter7
    extends XmlAdapter<String, GenerationTiming>
{


    public GenerationTiming unmarshal(String value) {
        return (org.hibernate.boot.jaxb.hbm.internal.GenerationTimingConverter.fromXml(value));
    }

    public String marshal(GenerationTiming value) {
        return (org.hibernate.boot.jaxb.hbm.internal.GenerationTimingConverter.toXml(value));
    }

}
