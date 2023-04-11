package com.example.cj_project_ecom.adapters;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.cj_project_ecom.HomeActivity;
import com.example.cj_project_ecom.Utils;
import com.example.cj_project_ecom.models.ProductModel;
import com.example.cj_project_ecom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.RecyclerViewHolder> {

    private ArrayList<ProductModel> courseDataArrayList;
    private Context mcontext;

    private JSONObject fjsonstr;
    private JSONArray fjsonarr;
    private String favString;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ProductModel item);
    }

    public ProductsAdapter(ArrayList<ProductModel> recyclerDataArrayList, Context mcontext, OnItemClickListener listener) {
        this.courseDataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_view, parent, false);

        return new RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        ProductModel recyclerData = courseDataArrayList.get(position);
        holder.tvProducts.setText(recyclerData.getProduct_name());
        holder.favIv.setBackgroundResource(R.drawable.ic_fav_border_foreground);

        holder.priceTv.setText("₹"+String.format("%,d", Integer.parseInt(recyclerData.getPprice().toString())));

        Glide.with(holder.itemView).load(recyclerData.getImgid()).into(holder.ivProducts);

        if(Utils.isLoggedin(mcontext.getApplicationContext()).equalsIgnoreCase("user")) {
            favString = HomeActivity.favString;
            try {
                fjsonarr = new JSONArray(favString);

                for (int i = 0; i < fjsonarr.length(); i++) {
                    fjsonstr = new JSONObject(fjsonarr.get(i).toString());
                    if (recyclerData.getPuid().equals(fjsonstr.getString("puid"))) {
                        holder.favIv.setBackgroundResource(R.drawable.ic_fav_filled_foreground);
                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        holder.bind(courseDataArrayList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return courseDataArrayList.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView tvProducts, priceTv;
        private ImageView favIv, ivProducts;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            favIv = itemView.findViewById(R.id.favIv);
            ivProducts = itemView.findViewById(R.id.idIVProducts);
            tvProducts = itemView.findViewById(R.id.idTVProducts);
            priceTv = itemView.findViewById(R.id.idTVPrice);

        }

        public void bind(final ProductModel item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}