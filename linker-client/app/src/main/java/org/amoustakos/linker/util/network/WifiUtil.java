package org.amoustakos.linker.util.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Antonis Moustakos on 3/12/2017.
 */
public class WifiUtil {


    public static boolean isConnected(Context context){
        final ConnectivityManager conManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = conManager.getActiveNetworkInfo();

        if(net != null
                && net.isConnected()
                && ConnectivityManager.TYPE_WIFI == net.getType())
            return true;
        else
            return false;
    }

    public static boolean setState(Context context, boolean active){
        final WifiManager wifiManager = (WifiManager)
                context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.setWifiEnabled(active);
    }

    public static boolean getState(Context context){
        final WifiManager wifiManager = (WifiManager)
                context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    public static boolean toggleState(Context context){
        setState(context, !getState(context));
        return getState(context);
    }

}
