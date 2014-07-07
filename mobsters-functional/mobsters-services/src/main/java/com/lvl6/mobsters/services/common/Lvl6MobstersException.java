package com.lvl6.mobsters.services.common;

public class Lvl6MobstersException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5303511250040873314L;

	private Lvl6MobstersStatusCode statusCode;
	
	public Lvl6MobstersException() {
		super();
	}
	
	public Lvl6MobstersException( String message ) {
		super(message);
	}
	
	public Lvl6MobstersException( Lvl6MobstersStatusCode statusCode ) {
		super();
		this.statusCode = statusCode;
	}
	
	public Lvl6MobstersStatusCode getStatusCode() {
		return statusCode;
	}
}
