package org.amoustakos.linker.exceptions;

public class ServerException extends Exception{
	private static final long serialVersionUID = 1L;

	public static final String ALREADY_INITIALIZED = "Server already initialized.";
    public static final String INIT_FAILED = "Server failed to start.";
	
	public ServerException(){}
	public ServerException(String message){super(message);}
	public ServerException(String message, Throwable cause){super(message, cause);}
	public ServerException(Throwable cause){super(cause);}
}
