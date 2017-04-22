package org.amoustakos.linker.io.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.amoustakos.linker.io.base.BaseRequest;

/**
 * Created by antonis on 4/15/2017.
 */
public class LinkRequest extends BaseRequest {

    @SerializedName("link") @Expose
    private String link;


    public String getLink() {return link;}
    public void setLink(String link) {this.link = link;}
}
