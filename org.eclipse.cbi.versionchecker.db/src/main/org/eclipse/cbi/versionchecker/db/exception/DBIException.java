package org.eclipse.cbi.versionchecker.db.exception;

public class DBIException extends Exception {

	Exception source; // Exception used to create this, or just null

	public DBIException() {
		super();
	}

	public DBIException(String message) {
		super(message);
	}

	public DBIException(Throwable cause) {
		super(cause);
	}

	public DBIException(String message, Throwable cause) {
		super(message, cause);
	}

	public DBIException(Exception e){
		super(e.getMessage(), e.getCause());
		source = e;
	}


}
