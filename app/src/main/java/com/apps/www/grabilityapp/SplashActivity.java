package com.apps.www.grabilityapp;

import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.apps.www.grabilityapp.fragments.CategoriasFragment;
import com.apps.www.grabilityapp.fragments.SplashFragment;
import com.apps.www.grabilityapp.services.UpdateProductIntentService;
import com.apps.www.grabilityapp.utilidades.Constantes;
import com.apps.www.grabilityapp.utilidades.MetodosPublicos;
import com.apps.www.grabilityapp.utilidades.Paths;
import com.apps.www.grabilityapp.utilidades.ResettableView;
import com.github.jorgecastillo.FillableLoader;
import com.github.jorgecastillo.FillableLoaderBuilder;
import com.github.jorgecastillo.State;
import com.github.jorgecastillo.clippingtransforms.PlainClippingTransform;
import com.github.jorgecastillo.clippingtransforms.WavesClippingTransform;
import com.github.jorgecastillo.listener.OnStateChangeListener;

/**
 * Created by gustavo morales on 4/07/2016.
 * tavomorales88@gmail.com
 **/
public class SplashActivity extends AppCompatActivity  {

    private static final String D = "SplashActivity";
    private SplashActivity instance;
    private GetJSONBroadcastReceiver getJSONBroadcastReceiver;
    private boolean ready;
    private boolean badRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Fragment fragment = new SplashFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment).commit();
        instance = this;
        getJSONBroadcastReceiver = new GetJSONBroadcastReceiver();
        Intent i = new Intent(this, UpdateProductIntentService.class);
        startService(i);
        badRequest = false;
    }

    public void onStateChange(int state) {
        switch(state) {
            case State.FILL_STARTED:
                break;
            case State.FINISHED:
                if (ready || badRequest) startActivity( new Intent(instance, MainActivity.class));
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
                        break;
                    case Constantes.SEND_REQUEST:
                    case Constantes.BAD_REQUEST:
                        badRequest = true;
                        Log.e(D, "Fallo al actualizar la base de datos");
                        break;
                    case Constantes.TIME_OUT_REQUEST:
                        badRequest = true;
                        Log.e(D, "Equipo sin conexion al Servidor, Intentelo mas tarde.");
                        break;
                }
            }
        }
    }
}
