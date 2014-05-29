package com.sun.enterprise.container.common.spi.util;

import java.io.IOException;
import java.io.Serializable;

import org.jvnet.hk2.annotations.Contract;

@Contract
public interface GlassFishOutputStreamHandler {

	/**
	 * Called from JavaEEIOUtils' replaceObject. The implementation
	 *  must return the object that needs to be written out to the
	 *  stream OR null if it cannot handle the serialization of this
	 *  object
	 *  
	 */
	public Object replaceObject(Object obj) throws IOException;
	
}
