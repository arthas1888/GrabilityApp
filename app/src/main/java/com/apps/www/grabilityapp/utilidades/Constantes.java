package com.apps.www.grabilityapp.utilidades;

import android.content.Intent;

/**
 * Created by gustavo morales on 3/07/2016.
 * tavomorales88@gmail.com
 **/
public class Constantes {


    public static final String JSON_URL = "https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json";
    public static final String BROADCAST_GET_JSON = "BROADCAST_GET_JSON";
    public static final String OPTION_JSON_BROADCAST = "OPTION_JSON_BROADCAST";

    public static final int UPDATE_PRODUCTS = 0;
    public static final int SUCCESS_REQUEST = 1;
    public static final int SEND_REQUEST = 3;
    public static final int REQUEST_NOT_FOUND = 4;
    public static final int PRODUCTS_NOT_FOUND = 5;
    public static final int PRODUCTS_FOUND = 6;
    public static final int BAD_REQUEST = 9;
    public static final int TIME_OUT_REQUEST = 10;
}
