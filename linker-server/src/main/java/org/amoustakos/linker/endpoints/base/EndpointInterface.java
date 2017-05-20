package org.amoustakos.linker.endpoints.base;

import org.eclipse.jetty.http.HttpMethod;
import org.json.JSONException;

public interface EndpointInterface {

	String handle(HttpMethod method, String json) throws JSONException;

	String get(String json) throws JSONException;
	String post(String json) throws JSONException;
	String put(String json) throws JSONException;
	String delete(String json) throws JSONException;
	String head(String json) throws JSONException;
	String options(String json) throws JSONException;


	String unhandled();
	String error(int code, String msg);
}
