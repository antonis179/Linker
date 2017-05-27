package org.amoustakos.linker.io.models.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by antonis on 4/14/2017.
 */
public class BaseResponse {

    @SerializedName("statusCode") @Expose
    public int statusCode;
    @SerializedName("statusMessage") @Expose
    public String statusMessage;


    public BaseResponse() {}

    public BaseResponse(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}
