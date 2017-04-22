package org.amoustakos.linker.resources;

import com.google.common.collect.ImmutableMap;
import com.mashape.unirest.http.Unirest;
import org.amoustakos.linker.exceptions.SettingsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public final class SettingsLoader {
	/*
	 * LOG4J
	 */
	private static final Logger logger = LogManager.getLogger(SettingsLoader.class.getName());

    private SettingsLoader() {}

    public static void loadSettings() throws IOException, SettingsException {
		logger.info("Setting Unirest options...");

		Unirest.setConcurrency(Settings.UNIREST_CONCURRENT_CON_TOTAL, Settings.UNIREST_CONCURRENT_CON_PER_ROUTE);
		Unirest.clearDefaultHeaders();
		Map<String, String> headers = ImmutableMap.<String, String>builder()
											.put("Accept", Settings.HEADER_ACCEPT)
											.put("Content-Type", Settings.HEADER_CONTENT)
											.build();
		for(Map.Entry<String, String> e : headers.entrySet())
			Unirest.setDefaultHeader(e.getKey(), e.getValue());

		logger.info("Server settings loading...");
		logger.info("Loading settings file from: " + new File(Settings.SETTINGS_PATH).getCanonicalPath());
		
		boolean loaded = false;
		Exception exc = null;
		int tries = 0;
		while(!loaded && (tries < 10)){
			try {
				tries++;
				Settings.init();
				loaded = true;
			} catch (Exception e1) {
				exc = e1;
				if(tries < 10){
					logger.error("Loading of settings file failed. Retrying in 5 sec.", e1);
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
