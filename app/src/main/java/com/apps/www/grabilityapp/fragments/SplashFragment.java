package com.apps.www.grabilityapp.fragments;


import android.animation.Animator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import com.apps.www.grabilityapp.R;
import com.apps.www.grabilityapp.SplashActivity;
import com.apps.www.grabilityapp.utilidades.Paths;
import com.github.jorgecastillo.FillableLoader;
import com.github.jorgecastillo.listener.OnStateChangeListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gustavo morales on 4/07/2016.
 * tavomorales88@gmail.com
 **/
public class SplashFragment extends Fragment implements OnStateChangeListener {

    @Bind(R.id.fillableLoader) @Nullable FillableLoader fillableLoader;
    private static final String D = "SplashFragment";
    private View rootView;

    public SplashFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(D, "entra aca");
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_splash, container, false);

        //fillableLoader.start();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, rootView);
        fillableLoader.setSvgPath(Paths.LOGO_RAPPI_SVG);
        fillableLoader.setOnStateChangeListener(this);
        reset();
    }


    @Override
    public void onStateChange(int state) {
        Log.d(D, "state: " + state);
        ((SplashActivity) getActivity()).onStateChange(state);
    }

    public void reset() {
        assert fillableLoader != null;
        fillableLoader.reset();

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                fillableLoader.start();
                int cx = (rootView.getLeft() + rootView.getRight()) / 2;
                int cy = rootView.getBottom();
                int finalRadius = Math.max(rootView.getWidth(), rootView.getHeight());

                Animator anim = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    anim = ViewAnimationUtils.createCircularReveal(rootView, cx, cy, 0, finalRadius);
                    anim.start();
                }
                int color = Color.WHITE;
                rootView.setBackgroundColor(color);

            }
        }, 250);
    }


}
