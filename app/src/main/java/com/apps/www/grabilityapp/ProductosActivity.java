package com.apps.www.grabilityapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.apps.www.grabilityapp.Adapters.ProductosRecyclerViewAdapter;
import com.apps.www.grabilityapp.database.ProductosDataBase;
import com.apps.www.grabilityapp.modelos.Productos;
import com.apps.www.grabilityapp.utilidades.ConnectionDetector;
import com.apps.www.grabilityapp.utilidades.Constantes;
import com.apps.www.grabilityapp.utilidades.OnListInteractionListener;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by gustavo morales on 4/07/2016.
 * tavomorales88@gmail.com
 **/
public class ProductosActivity extends AppCompatActivity implements OnListInteractionListener{

    private static final String D = ProductosActivity.class.getSimpleName();
    private ProductosActivity instance;
    private int mColumnCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mColumnCount = 2;
        }
        setContentView(R.layout.activity_productos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        instance = this;

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_products);
        assert recyclerView != null;

        Bundle extras = getIntent().getExtras();
        if( extras == null) return;
        String categoria = getIntent().getStringExtra("categoria");

        ProductosDataBase productosDataBase = new ProductosDataBase(instance);
        ArrayList<Productos> arrayListProductos = productosDataBase.getProductosByCat(categoria);
        productosDataBase.close();

        for (Productos productos: arrayListProductos){
            Log.d(D, productos.getNombre());
        }

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(instance));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(instance, mColumnCount));
        }
        OnListInteractionListener mListener = this;
        recyclerView.setAdapter(new ProductosRecyclerViewAdapter(instance, arrayListProductos, mListener));
        setupWindowAnimations();
    }

    private void setupWindowAnimations() {
        //Fade fade = (Fade) TransitionInflater.from(this).inflateTransition(R.transition.activity_fade);
        //getWindow().setEnterTransition(fade);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.END);
            slide.setDuration(500);
            getWindow().setEnterTransition(slide);

            Slide slide2 = new Slide(Gravity.START);
            slide2.setDuration(500);
            getWindow().setReturnTransition(slide2);
        }

    }

    @Override
    public void onListInteraction(Productos productos, ImageView imageView) {
        Log.d(D, "entra aca: " + productos.getNombre());
        Bitmap bitmap;
        try {
            bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        }catch (Exception e){
            bitmap = null;
        }
        Intent i = new Intent(instance, DetailsProductActivity.class);
        i.putExtra("productos", productos);
        i.putExtra("Image", bitmap);
        String transitionName = getString(R.string.img_transition);

        ActivityOptions transitionActivityOptions;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            transitionActivityOptions = ActivityOptions
                    .makeSceneTransitionAnimation(instance, imageView, transitionName);
            startActivity(i, transitionActivityOptions.toBundle());
        }else{
            startActivity(i);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
