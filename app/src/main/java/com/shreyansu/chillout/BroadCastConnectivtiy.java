package com.shreyansu.chillout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.shreyansu.chillout.util.connectivityStatus;

public class BroadCastConnectivtiy extends BroadcastReceiver
{
    private connectivityRecieverListener kConnectivityRecieverListener;

    public BroadCastConnectivtiy(connectivityRecieverListener kConnectivityRecieverListener) {
        this.kConnectivityRecieverListener = kConnectivityRecieverListener;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(kConnectivityRecieverListener !=null && connectivityStatus.isConnected(context))
            kConnectivityRecieverListener.OnNetworkConnectionConnected();


    }
    public interface connectivityRecieverListener
    {
        void OnNetworkConnectionConnected();
    }
}
