package org.amoustakos.linker.endpoints.base;

import org.amoustakos.linker.io.base.BaseResponse;
import org.amoustakos.linker.resources.Constants;
import org.eclipse.jetty.http.HttpMethod;
import org.json.JSONException;


public class BaseEndpoint implements EndpointInterface{

	@Override
	public String handle(HttpMethod method, String json) throws JSONException {
		if(method == null)
			return null;
		String execResult = null;
		switch(method){
			case GET:
                execResult = get(json);
                break;
			case POST:
                execResult = post(json);
                break;
			case PUT:
                execResult = put(json);
                break;
			case DELETE:
                execResult = delete(json);
                break;
			case HEAD:
                execResult = head(json);
                break;
			case OPTIONS:
                execResult = options(json);
                break;
			default:
                execResult = unhandled();
                break;
		}

		if(execResult != null)
		    return execResult;
		else
		    return unhandled();
	}

	public String get(String json) throws JSONException {
		return null;
	}
	public String post(String json) throws JSONException {
		return null;
	}
	public String put(String json) throws JSONException {
		return null;
	}
	public String delete(String json) throws JSONException {
		return null;
	}
	public String head(String json) throws JSONException {
		return null;
	}
	public String options(String json) throws JSONException {
		return null;
	}
	public String unhandled() throws JSONException {
		return null;
	}
    @Override
    public String error(int code, String msg) {
        BaseResponse baseResp = new BaseResponse();
        baseResp.setStatusCode(code);
        baseResp.setStatusMessage(msg);
        return Constants.gson.toJson(baseResp);
    }
}
