package com.lvl6.mobsters.services.common;

/**
 */
public class Lvl6MobstersException extends RuntimeException
{
	/**
	 */
	private static final long serialVersionUID = 4084024649764965198L;

	private final Lvl6MobstersStatusCode statusCode;
	
	public Lvl6MobstersException() {
		super();
		this.statusCode = Lvl6MobstersStatusCode.FAIL_OTHER;
	}
	
	public Lvl6MobstersException( String message ) {
		super(message);
		this.statusCode = Lvl6MobstersStatusCode.FAIL_OTHER;
	}
	
	public Lvl6MobstersException(Throwable cause) {
		super(cause);
		this.statusCode = Lvl6MobstersStatusCode.FAIL_OTHER;
	}
	
	public Lvl6MobstersException( String message, Throwable cause ) {
		super(message, cause);
		this.statusCode = Lvl6MobstersStatusCode.FAIL_OTHER;
	}
	
	public Lvl6MobstersException( Lvl6MobstersStatusCode statusCode ) {
		super();
		this.statusCode = statusCode;
	}
	
	public Lvl6MobstersException( Lvl6MobstersStatusCode statusCode, String message ) {
		super(message);
		this.statusCode = statusCode;
	}
	
	public Lvl6MobstersException( Lvl6MobstersStatusCode statusCode, Throwable cause ) {
		super(cause);
		this.statusCode = statusCode;
	}
	
	public Lvl6MobstersException( Lvl6MobstersStatusCode statusCode, String message, Throwable cause ) {
		super(message, cause);
		this.statusCode = statusCode;
	}
	public Lvl6MobstersStatusCode getStatusCode() {
		return statusCode;
	}
}
