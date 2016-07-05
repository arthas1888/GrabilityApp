package com.apps.www.grabilityapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.apps.www.grabilityapp.ProductosActivity;
import com.apps.www.grabilityapp.R;
import com.apps.www.grabilityapp.modelos.Productos;
import com.apps.www.grabilityapp.utilidades.MySingleton;
import com.apps.www.grabilityapp.utilidades.OnListInteractionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class ProductosRecyclerViewAdapter extends RecyclerView.Adapter<ProductosRecyclerViewAdapter.ViewHolder> {

    private final List<Productos> mValues;
    private final OnListInteractionListener mListener;
    private Context context;

    public ProductosRecyclerViewAdapter(Context context,
            ArrayList<Productos> items, OnListInteractionListener listener) {
        this.context = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getNombre());
        holder.mPriceView.setText(mValues.get(position).getPrecio());
        String url = mValues.get(position).getUrlImage100();
        holder.mNetworkImageView.setImageUrl(url, holder.mImageLoader);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListInteraction(holder.mItem, holder.mNetworkImageView);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mPriceView;
        public final ImageLoader mImageLoader;
        public final NetworkImageView mNetworkImageView;
        public Productos mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name_product);
            mPriceView = (TextView) view.findViewById(R.id.price_product);
            mNetworkImageView = (NetworkImageView) view.findViewById(R.id.networkImageView);
            mImageLoader = MySingleton.getInstance(context).getImageLoader();
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
