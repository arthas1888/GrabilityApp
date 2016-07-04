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
import com.android.volley.toolbox.JsonObjectRequest;
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
public class UpdateProductIntentService extends IntentService {

    private static final String D = UpdateProductIntentService.class.getSimpleName();
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

        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String jsonArray = response.toString();
                        Log.d(D, "respuesta server: " + jsonArray);
                        UpdateProductosAsyncTask updateProductosAsyncTask = new UpdateProductosAsyncTask(instance);
                        updateProductosAsyncTask.execute(jsonArray);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(D, "Error: " + error.getMessage());
                processFinish(Constantes.BAD_REQUEST);
            }
        }
        );
        int socketTimeout = 20000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        ApplicationContext.getInstance().addToRequestQueue(request);
    }


    public void processFinish(int option) {
        Intent localIntent = new Intent(Constantes.BROADCAST_GET_JSON);
        localIntent.putExtra(Constantes.OPTION_JSON_BROADCAST, option);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
    }

    class UpdateProductosAsyncTask extends AsyncTask<String, Void, Boolean> {

        private Context context;

        public UpdateProductosAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            return updateDataBaseProductos(strings[0]);
        }

        @Override
        protected void onPostExecute(Boolean response) {
            if (response) processFinish(Constantes.UPDATE_PRODUCTS);
        }

        private boolean updateDataBaseProductos(String responseJSON) {

            boolean response = false;
            ProductosDataBase productosDataBase = new ProductosDataBase(context);

            try {
                ArrayList<Productos> productosArrayList = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(responseJSON);

                JSONArray jsonArray = jsonObject.getJSONObject("feed").getJSONArray("entry");
                Log.d(D, "jsonArray: " + jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObjectProduct = jsonArray.getJSONObject(i);
                    String nombre = jsonObjectProduct.getJSONObject("im:name").getString("label");
                    String desc = jsonObjectProduct.getJSONObject("summary").getString("label");
                    String precio = jsonObjectProduct.getJSONObject("im:price").getJSONObject("attributes").getString("amount");
                    String tipo = jsonObjectProduct.getJSONObject("im:contentType").getJSONObject("attributes").getString("label");
                    String cat = jsonObjectProduct.getJSONObject("category").getJSONObject("attributes").getString("label");
                    String catId = jsonObjectProduct.getJSONObject("category").getJSONObject("attributes").getString("im:id");
                    JSONArray jsonArrayUrl = jsonObjectProduct.getJSONArray("im:image");

                    ArrayList<String> arrayListUrl = new ArrayList<>();
                    for (int j = 0; j < jsonArrayUrl.length(); j++) {
                        JSONObject jsonObjectUrl = jsonArrayUrl.getJSONObject(j);
                        arrayListUrl.add(jsonObjectUrl.getString("label"));
                    }
                    Productos productos = new Productos(nombre, desc, precio, tipo, cat, catId,
                            arrayListUrl.get(0), arrayListUrl.get(1), arrayListUrl.get(2)
                            );
                    //Log.d(D, productos.toString());
                    productosArrayList.add(productos);
                }
                productosDataBase.delete();
                productosDataBase.add(productosArrayList);
                response = true;

            } catch (Exception e) {
                Log.e(D, "updateDataBaseProductos: " + e.getMessage());
            } finally {
                productosDataBase.close();
            }
            return response;
        }
    }
}