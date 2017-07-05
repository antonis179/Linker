package org.amoustakos.linker.resources;

import org.amoustakos.linker.exceptions.SettingsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public final class SettingsLoader {
	//LOG4J
	private static final Logger logger = LogManager.getLogger(SettingsLoader.class.getName());

	private static final int LOAD_RETRY_LIMIT = 2;

    private SettingsLoader() {}

    public static void loadSettings() throws IOException, SettingsException {
		logger.info("Server settings loading...");
		logger.info("Loading settings file from: " + new File(Settings.SETTINGS_PATH).getCanonicalPath());
		
		boolean loaded = false;
		Exception exc = null;
		int tries = 0;
		while(!loaded && (tries < LOAD_RETRY_LIMIT)){
			try {
				tries++;
				Settings.init();
				loaded = true;
			} catch (Exception e1) {
				exc = e1;
				if(tries < LOAD_RETRY_LIMIT){
					logger.warn("Loading of settings file failed. Retrying in 5 sec.");
					logger.catching(e1);
					try {Thread.sleep(5000);} catch (InterruptedException ignored) {}
				}
			}
		}
		if(!loaded){
		    String error = String.format(SettingsException.LOADING_FAILED, tries);
            logger.error(error);
            throw new SettingsException(error, exc);
		}
	}
}
