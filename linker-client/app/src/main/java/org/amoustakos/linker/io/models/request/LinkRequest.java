package org.amoustakos.linker.io.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.amoustakos.linker.io.models.base.BaseRequest;

/**
 * Created by antonis on 4/15/2017.
 */
public class LinkRequest extends BaseRequest {

    @SerializedName("link") @Expose
    public String link;

}
