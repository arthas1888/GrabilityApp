package com.apps.www.grabilityapp;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.apps.www.grabilityapp.modelos.Productos;

public class DetailsProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deatils_product);
        setupWindowAnimations();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        imageView.setImageBitmap(bitmap);

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(200);
        getWindow().setEnterTransition(slide);

        Fade fade = (Fade) TransitionInflater.from(this).inflateTransition(R.transition.activity_fade);
        getWindow().setReturnTransition(fade);
    }
}
