/*
 *	Copyright (c) 2015 Antonis Moustakos
 *	
 *	Permission is hereby granted, free of charge, to any person obtaining a copy
 *	of this software and associated documentation files (the "Software"), to deal
 *	in the Software without restriction, including without limitation the rights
 *	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *	copies of the Software, and to permit persons to whom the Software is
 *	furnished to do so, subject to the following conditions:
 *	
 *	The above copyright notice and this permission notice shall be included in
 *	all copies or substantial portions of the Software.
 *	
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *	THE SOFTWARE.
 */
package org.amoustakos.linker.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * TODO: Add update function every "X" minutes (X stored in constants or loaded from file)
 * @author antonis
 *
 */
public final class Settings {
	/*
	 * LOG4J
	 */	
	private static final Logger logger = LogManager.getLogger(Settings.class.getName());

	/*
	 * Unirest
	 */
	public static final String HEADER_ACCEPT = "application/json;charset=utf-8";
	public static final String HEADER_CONTENT = "application/json;charset=utf-8";
	public static final int UNIREST_CONCURRENT_CON_TOTAL = 20000;
	public static final int UNIREST_CONCURRENT_CON_PER_ROUTE = 5000;
	
	
	public static String SETTINGS_PATH;


	/*
	 * SERVER
	 */
	private int serverPort;
	private String serverIp; //optional
	private String logPath;
	
	/*
	 * Singleton
	 */
	private static Settings instance;
	public static Settings getInstance(){return instance;}
	public static void init() throws IOException {
        if(instance == null)
            instance = new Settings();
    }

	private Settings() throws IOException{
		updateSettings();
	}
	
	private void updateSettings() throws IOException, NullPointerException{
		Properties config = new Properties();
		config.load(new FileInputStream(new File(SETTINGS_PATH)));
		
		/*
		 * NULL checks for settings that can cause fatal errors
		 */		
		
        //Server
        if(config.getProperty("server.port") == null)
            throw new NullPointerException();
        if(config.getProperty("log.path") == null)
            throw new NullPointerException();

		/*
		 * SERVER
		 */
		serverIp = config.getProperty("server.ip");
		serverPort = Integer.parseInt(config.getProperty("server.port"));
		logPath = config.getProperty("log.path");
        logger.info("Connector settings loaded successfully");


		logger.info("Server settings loaded successfully");
	}

	/*
	 * Getters - Setters
	 */
    public int getServerPort() {
        return serverPort;
    }
    public String getServerIp() {
        return serverIp;
    }
    public String getLogPath() {
        return logPath;
    }
}
