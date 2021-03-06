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

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Future;


public final class JettyServer {
    private static final String SERVER_STARTED = "Server started.";
    private static final String SETTINGS_LOAD_LIMIT_REACHED = "Retry limit reached. Settings could not be loaded.";

    //Log4j
	private static final Logger logger = LogManager.getLogger(JettyServer.class.getName());

	private Server server;
    private int port = -1;
	private volatile boolean acceptConnections = false;

    private final Thread mainThread = Thread.currentThread();


	/*
	 * Constructors
	 */
    public JettyServer() {
        init();
    }


    /*
     * Methods
     */

    /**
     * Init server configuration
     */
    private synchronized void init(){
        if(getServer() != null){
            logger.warn(ServerException.ALREADY_INITIALIZED);
            return;
        }

        setServer(new Server());

		/*
		 * HTTP
		 */
        try(ServerConnector baseConn = new ServerConnector(server)) {
            //Base
            port = Settings.getInstance().getServerPort();
            baseConn.setPort(port);
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

        logger.info("Server version: " + Version.getFormattedVersion());
    }



    public synchronized void start() throws ServerException{
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

        Runtime.getRuntime().removeShutdownHook(mainThread);
		Runtime.getRuntime().addShutdownHook(
                                                new Thread(this::shutdown)
                                            );
		setAcceptConnections(true);


        if(Settings.getInstance().getServerIp() == null) {
            try {
                //TODO: add popup window
                String hostName = InetAddress.getLocalHost().getHostName();
                InetAddress[] addresses = InetAddress.getAllByName(hostName);
                for(InetAddress addr : addresses)
                    if(addr instanceof Inet4Address)
                        logger.info("Bound to address: " + addr.getHostAddress() + ":" + String.valueOf(port));
            } catch (UnknownHostException ignored) {}
        }
        else{
            logger.info("Bound to address: " + Settings.getInstance().getServerIp() + ":" + String.valueOf(port));
        }
    }

    public synchronized boolean stop(){
        setAcceptConnections(false);
        try {
            logger.info("Terminating connections and stopping server.");
            for (Connector con : getServer().getConnectors()){
                Future<Void> future = con.shutdown();
                while(!future.isDone())
                    Thread.sleep(500L);
            }
            getServer().stop();
            logger.info("Server stopped.");
            return true;
        } catch (Exception e) {
            logger.error("Failed to stop server.", e);
            return false;
        }
    }

	public synchronized void shutdown(){
        int exitNum = stop() ? 0 : 1;
        try {
            System.out.flush();
            System.err.flush();
            mainThread.join();
        } catch (Exception e) {
            Runtime.getRuntime().halt(exitNum);
        }
    }


	/*
	 * Getters - Setters
	 */
    private synchronized Server getServer() {return server;}
    private void setServer(Server server) {this.server = server;}
	public synchronized boolean isAcceptingConnections() {return acceptConnections;}
	public synchronized void setAcceptConnections(boolean acceptConnections) {this.acceptConnections = acceptConnections;}
}





