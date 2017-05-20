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
package org.amoustakos.linker;

import org.amoustakos.linker.exceptions.ServerException;
import org.amoustakos.linker.handler.RequestHandler;
import org.amoustakos.linker.resources.Settings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

import java.util.concurrent.Future;


public final class JettyServer {
    private static final String SERVER_STARTED = "Server started.";
    private static final String SETTINGS_LOAD_LIMIT_REACHED = "Retry limit reached. Settings could not be loaded.";

    //Log4j
	private static final Logger logger = LogManager.getLogger(JettyServer.class.getName());

	private static Server server;
	private static volatile boolean acceptConnections = false;


	/*
	 * Constructors
	 */
    private JettyServer() {}


    /*
     * Methods
     */
    public synchronized static void start() throws ServerException{
		if(getServer() != null){
			logger.error(ServerException.ALREADY_INITIALIZED);
			return;
		}

		setServer(new Server());

		/*
		 * HTTP
		 */
        try(ServerConnector baseConn = new ServerConnector(server)) {
            //Base
            baseConn.setPort(Settings.getInstance().getServerPort());
            if(Settings.getInstance().getServerIp() != null)
                baseConn.setHost(Settings.getInstance().getServerIp());
            if(Settings.getInstance().getServerQueue() >= 0)
                baseConn.setAcceptQueueSize(Settings.getInstance().getServerQueue());

            getServer().setConnectors(new Connector[]{baseConn});
        }
        catch(Exception e){
            logger.error("Failed to instantiate connector(s)", e);
            System.exit(2);
        }
        getServer().setHandler(new RequestHandler());


		logger.info("Starting server...");

		int tries = 0;
		Exception exc = null;
		boolean started = false;
		while(!started && (tries < 10)){
			try{
				tries++;
				getServer().start();
				logger.info(SERVER_STARTED);
				started = true;
			} 
			catch (Exception e){
				exc = e;
				if(tries < 10){
					logger.error(ServerException.INIT_FAILED, e);
					try {Thread.sleep(5000);} catch (InterruptedException ignored) {}
				}
			}
		}
		
		if(!getServer().isStarted()){
			if(exc == null)
				throw new ServerException(SETTINGS_LOAD_LIMIT_REACHED);
			else
				throw new ServerException(SETTINGS_LOAD_LIMIT_REACHED, exc);
		}

		Runtime.getRuntime().addShutdownHook(
                                                new Thread(JettyServer::shutdown)
                                            );
		logger.info("Server version: " + Version.getFormattedVersion());
		setAcceptConnections(true);
	}

	private static synchronized void shutdown(){
        int exitNum;
		setAcceptConnections(false);
        try {
            logger.info("Terminating connections and stopping server.");
            for (Connector con : getServer().getConnectors()){
                Future<Void> future = con.shutdown();
                while(!future.isDone())
                    Thread.sleep(250L);
            }
            getServer().stop();
            logger.info("Server stopped.");
            exitNum = 0;
        } catch (Exception e) {
            logger.error("Failed to stop server. Killing main thread.", e);
            exitNum = 1;
        }
        System.exit(exitNum);
    }


	/*
	 * Getters - Setters
	 */
    private synchronized static Server getServer() {return server;}
    private static void setServer(Server server) {JettyServer.server = server;}
	public static synchronized boolean isAcceptingConnections() {return acceptConnections;}
	public static synchronized void setAcceptConnections(boolean acceptConnections) {JettyServer.acceptConnections = acceptConnections;}
}





