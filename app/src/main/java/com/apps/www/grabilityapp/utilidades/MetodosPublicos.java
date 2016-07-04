package com.apps.www.grabilityapp.utilidades;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by gustavo morales on 3/07/2016.
 * tavomorales88@gmail.com
 **/
public class MetodosPublicos {

    public static void alertDialog(Context context, String msg) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Mensaje");
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("Aceptar", null);
        alertDialog.create().show();

    }

}
