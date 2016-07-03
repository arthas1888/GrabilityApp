package com.apps.www.grabilityapp.services;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.apps.www.grabilityapp.ApplicationContext;
import com.apps.www.grabilityapp.database.ProductosDataBase;
import com.apps.www.grabilityapp.modelos.Productos;
import com.apps.www.grabilityapp.utilidades.AsyncJSONResponse;
import com.apps.www.grabilityapp.utilidades.ConnectionDetector;
import com.apps.www.grabilityapp.utilidades.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by gustavo morales on 3/07/2016.
 * tavomorales88@gmail.com
 **/
public class UpdateProductIntentService extends IntentService implements AsyncJSONResponse {

    private static final String D = UpdateProductIntentService.class.getSimpleName();
    private int option;
    private UpdateProductIntentService instance;

    @SuppressWarnings("unused")
    public UpdateProductIntentService() {
        super(D);
    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    @SuppressWarnings("unused")
    public UpdateProductIntentService(String name) {
        super(name);
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onHandleIntent(Intent intent) {

        option = intent.getIntExtra("option", 0);
        instance = this;


        String url = Constantes.JSON_URL;
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        Log.d(D, "conexion a internet: " + connectionDetector.isConnectingToInternet());

        if (connectionDetector.isConnectingToInternet()){
            try {
                getJson(url);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Intent localIntent = new Intent(Constantes.BROADCAST_GET_JSON);
            localIntent.putExtra(Constantes.OPTION_JSON_BROADCAST, 10);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void getJson(String url) throws JSONException {

        JsonArrayRequest request = new JsonArrayRequest(com.android.volley.Request.Method.GET, url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response)
                    {
                        String jsonArray = response.toString();
                        Log.d(D, "respuesta server: " + jsonArray);
                        UpdateProductosAsyncTask updateProductosAsyncTask = new UpdateProductosAsyncTask(instance);
                        updateProductosAsyncTask.delegate = instance;
                        updateProductosAsyncTask.execute(jsonArray);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(D, "Error: " + error.getMessage());
                Intent localIntent = new Intent(Constantes.BROADCAST_GET_JSON);
                localIntent.putExtra("option", 10);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
            }
        }
        );
        int socketTimeout = 20000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        ApplicationContext.getInstance().addToRequestQueue(request);
    }

    @Override
    public void processFinish() {
        Intent localIntent = new Intent(Constantes.BROADCAST_GET_JSON);
        localIntent.putExtra(Constantes.OPTION_JSON_BROADCAST, option);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
    }

    class UpdateProductosAsyncTask extends AsyncTask<String, Void, String> {

        private Context context;

        public UpdateProductosAsyncTask(Context context) {
            this.context = context;
        }

        public AsyncJSONResponse delegate = null;

        @Override
        protected String doInBackground(String... strings) {
            updateDataBaseProductos(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String jsonArray) {
            delegate.processFinish();
        }

        private void updateDataBaseProductos(String responseJSON) {
            ProductosDataBase productosDataBase = new ProductosDataBase(context);
            JSONArray jsonArray;
            try {
                ArrayList<Productos> productosArrayList = new ArrayList<>();
                jsonArray = new JSONArray(responseJSON);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Productos productos = new Productos();
                    productos.setNombre(jsonObject.getString("Nombre"));
                    productos.setTipo(jsonObject.getString("Tipo"));
                    productos.setDescripcion(jsonObject.getString("Descripcion"));
                    productosArrayList.add(productos);
                }
                productosDataBase.delete();
                productosDataBase.add(productosArrayList);

            } catch (Exception e) {
                Log.e(D, "updateDataBaseProductos: " + e.getMessage());
            } finally {
                productosDataBase.close();
            }
        }
    }
}