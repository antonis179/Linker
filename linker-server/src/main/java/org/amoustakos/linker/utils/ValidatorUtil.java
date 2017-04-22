package org.amoustakos.linker.utils;

import com.google.gson.Gson;
import org.amoustakos.linker.io.base.BaseRequest;
import org.json.JSONException;

/**
 * Created by antonis on 3/31/2017.
 */
public final class ValidatorUtil {
    private static final Gson gson = new Gson();
    private ValidatorUtil(){}


    public static boolean isValidRequest(String json){
        try {
            BaseRequest req = gson.fromJson(json, BaseRequest.class);
            return req != null;
        } catch (JSONException ignored) {
            return false;
        }
    }


}
