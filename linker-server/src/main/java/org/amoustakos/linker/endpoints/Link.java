package org.amoustakos.linker.endpoints;

import org.amoustakos.linker.endpoints.base.BaseEndpoint;
import org.amoustakos.linker.io.base.BaseResponse;
import org.amoustakos.linker.io.request.LinkRequest;
import org.amoustakos.linker.resources.Constants;
import org.json.JSONException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

/**
 * Created by antonis on 4/15/2017.
 */
public class Link extends BaseEndpoint{

    @Override
    public String get(String json) throws JSONException {
        LinkRequest req = Constants.gson.fromJson(json, LinkRequest.class);
        if(req == null)
            return error(HttpServletResponse.SC_BAD_REQUEST, "Empty request.");
        if(req.getLink() == null)
            return error(HttpServletResponse.SC_BAD_REQUEST, "No link provided.");

        try {
            java.awt.Desktop.getDesktop().browse(URI.create(req.getLink()));
        } catch (IOException ignored) {
            return error(HttpServletResponse.SC_BAD_REQUEST, "Bad format.");
        }

        BaseResponse baseResp = new BaseResponse();
        baseResp.setStatusCode(HttpServletResponse.SC_OK);
        baseResp.setStatusMessage("OK");
        return Constants.gson.toJson(baseResp);
    }

    @Override
    public String post(String json) throws JSONException {
        LinkRequest req = Constants.gson.fromJson(json, LinkRequest.class);
        if(req == null)
            return error(HttpServletResponse.SC_BAD_REQUEST, "Empty request.");
        if(req.getLink() == null)
            return error(HttpServletResponse.SC_BAD_REQUEST, "No link provided.");

        try {
            java.awt.Desktop.getDesktop().browse(URI.create(req.getLink()));
        } catch (IOException ignored) {
            return error(HttpServletResponse.SC_BAD_REQUEST, "Bad format.");
        }

        BaseResponse baseResp = new BaseResponse();
        baseResp.setStatusCode(HttpServletResponse.SC_OK);
        baseResp.setStatusMessage("OK");
        return Constants.gson.toJson(baseResp);
    }

    @Override
    public String unhandled() throws JSONException {
        return error(HttpServletResponse.SC_BAD_REQUEST, "Wrong operation requested.");
    }
}
