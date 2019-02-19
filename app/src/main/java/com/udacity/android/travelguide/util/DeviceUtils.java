package com.udacity.android.travelguide.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

public class DeviceUtils {
    private static final String TAG = DeviceUtils.class.getSimpleName();

    /*Code to check density of screen
     * https://stackoverflow.com/questions/3166501/getting-the-screen-density-programmatically-in-android
     * */
    public static int getDeviceDensity(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    public static String getLanguage(Context context) {
        return getLocale(context).toString().replace("_", "-");
    }

    /*Code to get Locale from phone
     *https://stackoverflow.com/questions/14389349/android-get-current-locale-not-default
     */
    public static Locale getLocale(Context context) {
        return context.getResources().getConfiguration().locale;
    }

    /*Code to check internet access
     * https://www.programcreek.com/java-api-examples/android.net.NetworkInfo
     * */
    public static boolean hasInternet(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        } catch (SecurityException e) {
            Log.e(TAG, "hasInternet: ", e);
        }
        return false;
    }

    public static int numberOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }
}
