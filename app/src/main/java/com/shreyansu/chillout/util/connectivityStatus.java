package com.shreyansu.chillout.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class connectivityStatus
{
    public static boolean isConnected(Context context)
    {
        ConnectivityManager cm=(ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork =cm.getActiveNetworkInfo();
        boolean isconnected = activeNetwork !=null &&activeNetwork.isConnectedOrConnecting();
        return isconnected;
    }

}
