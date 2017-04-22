package org.amoustakos.linker.io.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by antonis on 4/14/2017.
 */
public class BaseResponse {


    @SerializedName("statusCode") @Expose
    private int statusCode;
    @SerializedName("statusMessage") @Expose
    private String statusMessage;



    public int getStatusCode() {return statusCode;}
    public void setStatusCode(int statusCode) {this.statusCode = statusCode;}
    public String getStatusMessage() {return statusMessage;}
    public void setStatusMessage(String statusMessage) {this.statusMessage = statusMessage;}
}
