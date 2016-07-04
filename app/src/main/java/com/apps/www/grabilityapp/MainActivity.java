package com.apps.www.grabilityapp;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.apps.www.grabilityapp.database.ProductosDataBase;
import com.apps.www.grabilityapp.fragments.ProductosFragment;
import com.apps.www.grabilityapp.services.UpdateProductIntentService;
import com.apps.www.grabilityapp.utilidades.Constantes;
import com.apps.www.grabilityapp.utilidades.MetodosPublicos;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gustavo morales on 3/07/2016.
 * tavomorales88@gmail.com
 **/
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProductosFragment.OnListFragmentInteractionListener {

    private static final String D = MainActivity.class.getSimpleName();
    private static final int RC_HANDLE_PERM = 2;

    private MainActivity instance;
    private GetJSONBroadcastReceiver getJSONBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if (findViewById(R.id.container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ProductosFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        instance = this;
        getJSONBroadcastReceiver = new GetJSONBroadcastReceiver();

        // Checkea permisos
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
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

    private void requestPermission() {
        Log.d(D,"Permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.ACCESS_NETWORK_STATE};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NETWORK_STATE)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_PERM);
            return;
        }

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(instance, permissions,
                        RC_HANDLE_PERM);
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(instance)
                .setTitle("Alerta")
                .setMessage("Permisos")
                .setPositiveButton("Si", listener)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update) {
            Intent i = new Intent(this, UpdateProductIntentService.class);
            startService(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(String item) {

    }

    /**
     * Clase especializada en recibir la respuesta de las peticiones enviadas al servidor
     */
    public class GetJSONBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            int option = intent.getIntExtra(Constantes.OPTION_JSON_BROADCAST, 0);
            final String action = intent.getAction();
            //progressdialog.dismiss();
            if (Constantes.BROADCAST_GET_JSON.equals(action)) {
                switch (option) {
                    case Constantes.UPDATE_PRODUCTS:
                        MetodosPublicos.alertDialog(instance, "Listado de productos actualizados exitosamente");
                        ProductosDataBase productosDataBase = new ProductosDataBase(instance);
                        ArrayList<String> arrayListCat = productosDataBase.getCategorias();
                        productosDataBase.close();
                        for(String cat: arrayListCat){
                            Log.d(D, "Cat.: " + cat);
                        }
                        break;
                    case Constantes.SEND_REQUEST:
                    case Constantes.BAD_REQUEST:
                        MetodosPublicos.alertDialog(instance, "Fallo al actualizar la base de datos");
                        break;
                    case Constantes.TIME_OUT_REQUEST:
                        MetodosPublicos.alertDialog(instance, "Equipo sin conexion al Servidor, Intentelo mas tarde.");
                        break;
                }
            }
        }
    }
}
