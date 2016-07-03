package com.apps.www.grabilityapp;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by gustavo morales on 3/07/2016.
 * tavomorales88@gmail.com
 **/
public class ApplicationContext extends Application {

    public static Context applicationContext = null;
    public static Handler applicationHandler = null;
    private RequestQueue mRequestQueue;
    private static final String TAG = "ApplicationContext";
    private static ApplicationContext mInstance;

    public static synchronized ApplicationContext getInstance()
    {
        return mInstance;
    }

    /**
     * Metodo que se lanza en cuanto la aplicacion se crea
     */
    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        applicationHandler = new Handler(applicationContext.getMainLooper());
        mInstance = this;
    }

    public RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

}