package org.amoustakos.linker.io.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.amoustakos.linker.io.models.base.BaseRequest;

public class LinkRequest extends BaseRequest {

    @SerializedName("link") @Expose
    public String link;


    /*
     * Constructors
     */
    public LinkRequest(String link) {
        this.link = link;
    }

    public LinkRequest() {}
}
