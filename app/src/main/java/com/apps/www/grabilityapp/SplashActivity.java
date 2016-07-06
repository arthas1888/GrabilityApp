package com.apps.www.grabilityapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.apps.www.grabilityapp.fragments.SplashFragment;
import com.apps.www.grabilityapp.services.UpdateProductIntentService;
import com.apps.www.grabilityapp.utilidades.Constantes;
import com.github.jorgecastillo.State;

/**
 * Created by gustavo morales on 4/07/2016.
 * tavomorales88@gmail.com
 **/
public class SplashActivity extends AppCompatActivity  {

    private static final String D = "SplashActivity";
    private SplashActivity instance;
    private GetJSONBroadcastReceiver getJSONBroadcastReceiver;
    private boolean ready;
    private int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_splash);
        Fragment fragment = new SplashFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment).commit();
        instance = this;
        getJSONBroadcastReceiver = new GetJSONBroadcastReceiver();
        Intent i = new Intent(this, UpdateProductIntentService.class);
        startService(i);
        ready = false;
    }

    public void onStateChange(int state) {
        this.state = state;
        switch(state) {
            case State.FILL_STARTED:
                break;
            case State.FINISHED:
                if (ready) startActivity( new Intent(instance, MainActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        finish();
    }

    public void onResume(){
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constantes.BROADCAST_GET_JSON);
        LocalBroadcastManager.getInstance(instance).registerReceiver(getJSONBroadcastReceiver,
                intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(getJSONBroadcastReceiver);
    }

    /**
     * Clase especializada en recibir la respuesta de las peticiones enviadas al servidor
     */
    public class GetJSONBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            int option = intent.getIntExtra(Constantes.OPTION_JSON_BROADCAST, 0);
            final String action = intent.getAction();
            if (Constantes.BROADCAST_GET_JSON.equals(action)) {
                switch (option) {
                    case Constantes.UPDATE_PRODUCTS:
                        Log.d(D, "Listado de productos actualizados exitosamente");
                        ready = true;
                        if (state == State.FINISHED){
                            startActivity( new Intent(instance, MainActivity.class));
                        }
                        break;
                    case Constantes.SEND_REQUEST:
                    case Constantes.BAD_REQUEST:
                    case Constantes.TIME_OUT_REQUEST:
                        ready = true;
                        if (state == State.FINISHED){
                            startActivity( new Intent(instance, MainActivity.class));
                        }
                        Log.e(D, "Equipo sin conexion al Servidor, Intentelo mas tarde.");
                        break;
                }
            }
        }
    }
}
