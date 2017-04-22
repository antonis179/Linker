package org.amoustakos.linker.exceptions;

public class SettingsException extends Exception{
	private static final long serialVersionUID = 1L;

	public static final String LOADING_FAILED = "Loading of settings file failed. tried %d times";
	
	public SettingsException(){}
	public SettingsException(String message){super(message);}
	public SettingsException(String message, Throwable cause){super(message, cause);}
	public SettingsException(Throwable cause){super(cause);}
}
