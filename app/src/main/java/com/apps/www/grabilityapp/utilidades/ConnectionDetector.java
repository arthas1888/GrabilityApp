package com.apps.www.grabilityapp.utilidades;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by gustavo morales on 3/07/2016.
 * tavomorales88@gmail.com
 **/
public class ConnectionDetector {

    Context context;

    public ConnectionDetector(Context context) {
        this.context = context;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        //boolean isInternetAccessible = isInternetAccessible(context.getResources().getString(R.string.pref_value_ip_server));
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
