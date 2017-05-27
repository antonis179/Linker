package org.amoustakos.linker.endpoints;

import org.amoustakos.linker.endpoints.base.BaseEndpoint;
import org.amoustakos.linker.io.base.BaseResponse;
import org.amoustakos.linker.io.request.LinkRequest;
import org.amoustakos.linker.resources.Constants;
import org.amoustakos.linker.utils.DesktopUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

public class Link extends BaseEndpoint{
    //Log4j
    private static final Logger logger = LogManager.getLogger(Link.class.getName());

    @Override
    public String get(String json) throws JSONException {
        return post(json);
    }

    @Override
    public String post(String json) throws JSONException {
        LinkRequest req = Constants.gson.fromJson(json, LinkRequest.class);
        if(req == null)
            return error(HttpServletResponse.SC_BAD_REQUEST, "Empty request.");
        if(req.getLink() == null)
            return error(HttpServletResponse.SC_BAD_REQUEST, "No link provided.");

        try {
            DesktopUtil.browse(URI.create(req.getLink()));
        } catch (IOException e) {
            logger.catching(e);
            return error(HttpServletResponse.SC_BAD_REQUEST, "Something went wrong! Please check your JRE AWT support.");
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
