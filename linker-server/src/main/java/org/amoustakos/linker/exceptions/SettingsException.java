package org.amoustakos.linker.exceptions;

public class SettingsException extends Exception{
	private static final long serialVersionUID = 1L;

	public static final String LOADING_FAILED = "Loading of settings file failed. tried %d times";
	public static final String INVALID_SERVER_MODE = "Invalid server mode setting.";
	
	public SettingsException(){}
	public SettingsException(String message){super(message);}
	public SettingsException(String message, Throwable cause){super(message, cause);}
	public SettingsException(Throwable cause){super(cause);}
}
