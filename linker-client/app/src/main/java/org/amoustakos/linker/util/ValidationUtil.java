package org.amoustakos.linker.util;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Antonis Moustakos on 5/16/2017.
 */

public final class ValidationUtil {

    public static boolean validateURL(String url){
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

}
