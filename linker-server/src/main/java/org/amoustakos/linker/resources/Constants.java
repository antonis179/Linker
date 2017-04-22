package org.amoustakos.linker.resources;

import com.google.gson.Gson;

import java.io.File;

/**
 * Class used for any value that is needed before logging initialization
 */
public final class Constants {
    private Constants(){}


	/*
	 * Settings file default location
	 */
	public static final String SETTINGS_LOCATION = '.' + File.separator + "linker.properties";

	/*
	 * Single GSON instrance
	 */
	public static volatile Gson gson = new Gson();

}
