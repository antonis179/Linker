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
package org.amoustakos.linker.handler;

import org.amoustakos.linker.JettyServer;
import org.amoustakos.linker.endpoints.base.EndpointInterface;
import org.amoustakos.linker.io.base.BaseResponse;
import org.amoustakos.linker.resources.Constants;
import org.amoustakos.linker.resources.Settings;
import org.amoustakos.linker.utils.ValidatorUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.json.JSONException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RequestHandler extends GzipHandler {
	private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	private static final String ENCODING_GZIP = "gzip";
	private static final String PAYLOAD = "payload";
	/*
	 * LOG4J
	 */
	private static final Logger logger = LogManager.getLogger(RequestHandler.class.getName());

	/*
	 * Endpoint package
	 */
	private static final String PACKAGE_BASE = "org.amoustakos.linker.endpoints";

  	//Inner variables
	private Pattern slashRemover = Pattern.compile("/");
	private Map<String,EndpointInterface> map = new HashMap<>();


	@Override
	public void handle(String target,Request baseRequest,HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		/*
		 * Set request as handled so it is not passed to another method.
		 */
		baseRequest.setHandled(true);

		/*
		 * Die if server is shutting down
		 */
        if(!JettyServer.isAcceptingConnections()){
            print(response, "The server is not accepting connections. This probably means a shutdown operation is underway.");
            response.flushBuffer();
            return;
        }
		
		String json;
		
		//DEBUG
		long start = System.currentTimeMillis();
		
		if(!response.containsHeader(HEADER_ACCEPT_ENCODING))
			response.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
		
		//CORS filter
		//response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType(Settings.HEADER_CONTENT);

        //Endpoint
        String operation = slashRemover.matcher(target).replaceAll("");

        //Method
        HttpMethod method = HttpMethod.valueOf(baseRequest.getMethod());
        
        //Status check
        if(operation.equals("Status")){
            response.setStatus(HttpServletResponse.SC_OK);
			print(response, makeResponse(HttpServletResponse.SC_OK, null));
        	response.flushBuffer();
        	return;
        }
        //Browser favicon request
		if(operation.equals("favicon.ico")){
            response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
			return;
		}
        
        /*
         * Read user request
         */
        String jsonString = baseRequest.getParameter(PAYLOAD);
        if(ValidatorUtil.isValidRequest(jsonString)) { //Valid GET
            json = jsonString;
        }
        //TODO: check if this works for all request methods
        else{
            StringBuilder jb;
            try(BufferedReader reader = request.getReader()) {
                jb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                    jb.append(line);
                json = jb.toString();
                if(!ValidatorUtil.isValidRequest(json))
                    throw new JSONException("Invalid JSON: " + json);
            }catch(Exception ignored){
                logger.error("User request error - json operative missing.");
                print(response, makeResponse(HttpServletResponse.SC_BAD_REQUEST, "Required Field(s) missing"));
                response.flushBuffer();
                return;
            }
        }

        /*
         * Check class map for the class requested
         */
        try{
        	 if(!map.containsKey(operation)){
        		 Class<?> obj = Class.forName(PACKAGE_BASE + '.' + operation);
        		 Constructor<?> constructor = obj.getConstructor();
        		 EndpointInterface instance = (EndpointInterface) constructor.newInstance();     		 
        		 map.put(operation, instance);
        	 }
        }
        catch(Exception | Error e){
        	logger.error("Operation requested error - invalid operation requested");
            print(response, makeResponse(HttpServletResponse.SC_NOT_FOUND, "Invalid operation requested. Cannot find matching service."));
        	response.flushBuffer();
        	return;
        }
        
        /*
         * Execute the request
         */
		try{
		    String toSend = map.get(operation).handle(method, json);
		    response.setStatus(HttpServletResponse.SC_OK);
		    print(response, toSend);
        }catch(Exception e){
        	logger.error("Execution error", e);
        	response.flushBuffer();
        	return;
        }
        
        logger.debug("Request handled in: " + (System.currentTimeMillis() - start) + " ms");
        response.flushBuffer();
	}

	private static void print(HttpServletResponse response, String message) throws IOException {
        response.getWriter().write(message);
	}

	
	private static String makeResponse(int code, String message) throws JSONException {
        BaseResponse baseResp = new BaseResponse();
        baseResp.setStatusCode(code);
        baseResp.setStatusMessage(message);
		return Constants.gson.toJson(baseResp);
	}
}
