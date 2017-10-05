package ndr;

import java.io.IOException;

public class NdrException extends IOException {

	public static final String NO_NULL_REF = "ref pointer cannot be null";
	public static final String INVALID_CONFORMANCE = "invalid array conformance";

	public NdrException( String msg ) {
		super( msg );
	}
}
