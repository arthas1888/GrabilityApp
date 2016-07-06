package com.apps.www.grabilityapp;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.www.grabilityapp.modelos.Productos;

public class DetailsProductActivity extends AppCompatActivity {

    private static final String D = "DetailsProductActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_deatils_product);
        setupWindowAnimations();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Producto agregado a favoritos", Snackbar.LENGTH_LONG).show();
            }
        });

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        assert collapsingToolbarLayout != null;
        collapsingToolbarLayout.setTitle("Detalles");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        Bundle extras = getIntent().getExtras();
        if( extras == null) return;
        Productos productos = (Productos) getIntent().getSerializableExtra("productos");
        Bitmap bitmap = getIntent().getParcelableExtra("Image");
        ImageView imageView = (ImageView) findViewById(R.id.header_img);
        assert imageView != null;
        if (bitmap != null)
            imageView.setImageBitmap(bitmap);
        else
            imageView.setImageResource(R.drawable.not_available);

        ((TextView) findViewById(R.id.textViewTitle)).setText(productos.getNombre());
        ((TextView) findViewById(R.id.textViewPrice)).setText("US $" + productos.getPrecio());
        ((TextView) findViewById(R.id.textViewDesc)).setText(productos.getDescripcion());
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

    private void setupWindowAnimations() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide();
            slide.setDuration(200);
            getWindow().setEnterTransition(slide);

            Fade fade = (Fade) TransitionInflater.from(this).inflateTransition(R.transition.activity_fade);
            getWindow().setReturnTransition(fade);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
