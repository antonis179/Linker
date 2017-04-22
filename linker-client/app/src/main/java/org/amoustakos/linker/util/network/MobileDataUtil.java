package org.amoustakos.linker.util.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import timber.log.Timber;

import static org.amoustakos.linker.util.network.NetworkUtil.getNetworkInfo;

/**
 * Created by Antonis Moustakos on 3/12/2017.
 *
 * TODO: NEED TO INSTALL AS SYSTEM APP FOR THIS TO WORK.
 */

public class MobileDataUtil {


    public static boolean isConnected(Context context){
        final ConnectivityManager conManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = conManager.getActiveNetworkInfo();

        return net != null
                && net.isConnected()
                && ConnectivityManager.TYPE_MOBILE == net.getType();
    }

    public static boolean getState(Context context){
        try{
            TelephonyManager telephonyService = (TelephonyManager)
                    context.getSystemService(Context.TELEPHONY_SERVICE);
            Method getMobileDataEnabledMethod =
                    telephonyService.getClass().getDeclaredMethod("getDataEnabled");

            if (null != getMobileDataEnabledMethod){
                return (Boolean) getMobileDataEnabledMethod.invoke(telephonyService);
            }
        }catch (Exception e){
            Timber.w(e, e.getMessage());
        }
        return false;
    }


    public static boolean setState(Context context, boolean active){
        final ConnectivityManager conman = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Method dataMtd;
        try {
            dataMtd =ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
        } catch (NoSuchMethodException e) {
            Timber.w(e, e.getMessage());
            return false;
        }
        dataMtd.setAccessible(true);
        try {
            dataMtd.invoke(conman, active);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Timber.w(e, e.getMessage());
            return false;
        }
        return true;
    }


    public static boolean toggleState(Context context){
        MobileDataUtil.setState(context, !WifiUtil.getState(context));
        return MobileDataUtil.getState(context);
    }


    /**
     * Checks if there is fast connectivity
     */
    public static boolean isConnectedFast(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return info != null && info.isConnected() && isConnectionFast(info.getType(), info.getSubtype());
    }
    /**
     * Checks if the connection is fast
     */
    public static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI)
            return true;
        else if (type == ConnectivityManager.TYPE_MOBILE)
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
//                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        else
            return false;
    }
}
