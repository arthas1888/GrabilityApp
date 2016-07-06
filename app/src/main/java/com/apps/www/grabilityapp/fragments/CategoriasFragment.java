package com.apps.www.grabilityapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.www.grabilityapp.Adapters.CategoriasRecyclerViewAdapter;
import com.apps.www.grabilityapp.MainActivity;
import com.apps.www.grabilityapp.R;
import com.apps.www.grabilityapp.database.ProductosDataBase;
import com.apps.www.grabilityapp.utilidades.ConnectionDetector;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * Created by gustavo morales on 3/07/2016.
 * tavomorales88@gmail.com
 **/
public class CategoriasFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_PARAM1 = "categorias";
    private static final String D = "CategoriasFragment";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private ArrayList<String> arrayListCat;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CategoriasFragment() {
    }

    @SuppressWarnings("unused")
    public static CategoriasFragment newInstance(ArrayList<String> arrayListCat) {
        CategoriasFragment fragment = new CategoriasFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, arrayListCat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!getResources().getBoolean(R.bool.portrait_only)){
            mColumnCount = 2;
        }
        ProductosDataBase productosDataBase = new ProductosDataBase(getActivity());
        arrayListCat = productosDataBase.getCategorias();
        productosDataBase.close();
        if (arrayListCat.size() == 0) ((MainActivity) (getActivity())).alertSinConexion();
        else {
            ConnectionDetector connectionDetector = new ConnectionDetector(getActivity());
            Log.d(D, "conexion a internet: " + connectionDetector.isConnectingToInternet());
            if (!connectionDetector.isConnectingToInternet()) {
                ((MainActivity) (getActivity())).alertOfflineConection();
            }
        }
        /*for(String cat: arrayListCat){
            Log.d(D, "Cat.: " + cat);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new CategoriasRecyclerViewAdapter(arrayListCat, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(String item);
    }
}
