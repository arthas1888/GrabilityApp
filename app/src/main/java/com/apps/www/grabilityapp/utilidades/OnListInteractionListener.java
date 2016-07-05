package com.apps.www.grabilityapp.utilidades;

import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;
import com.apps.www.grabilityapp.modelos.Productos;
/**
 * Created by gustavo morales on 4/07/2016.
 * tavomorales88@gmail.com
 **/
public interface OnListInteractionListener {
    void onListInteraction(Productos item, ImageView mNetworkImageView);
}
