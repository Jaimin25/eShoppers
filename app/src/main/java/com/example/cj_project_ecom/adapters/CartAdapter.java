package com.example.cj_project_ecom.adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.cj_project_ecom.HomeActivity;
import com.example.cj_project_ecom.Utils;
import com.example.cj_project_ecom.fragments.HomeFragment;
import com.example.cj_project_ecom.models.CartModel;
import com.example.cj_project_ecom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.RecyclerViewHolder> {

    private ArrayList<CartModel> courseDataArrayList;
    private Context mcontext;

    private JSONArray jsonArr;
    private JSONObject cjsonString;
    private String jsonString;

    private String amount, price;
    private ItemTouchListener onItemTouchListener;

    public CartAdapter(ArrayList<CartModel> recyclerDataArrayList, Context mcontext, ItemTouchListener onItemTouchListener) {
        this.courseDataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
        this.onItemTouchListener = onItemTouchListener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_view, parent, false);

        return new RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        CartModel recyclerData = courseDataArrayList.get(position);

        holder.amtCart.setText(recyclerData.getAmount());

        if(Utils.isLoggedin(mcontext.getApplicationContext()).equalsIgnoreCase("user")) {

                for (int i = 0; i < HomeFragment.ProductArrayList.size(); i++) {

                    if (recyclerData.getPuid().equals(HomeFragment.ProductArrayList.get(i).getPuid())) {
                        price = HomeFragment.ProductArrayList.get(i).getPprice();
                        amount = recyclerData.getAmount();
                        recyclerData.setPrice(price);
                        holder.tvProducts.setText(HomeFragment.ProductArrayList.get(i).getProduct_name());
                        holder.tvProducts.setText(HomeFragment.ProductArrayList.get(i).getPinfo());
                        holder.priceProduct.setText(Html.fromHtml("<b>Total: </b>₹"+String.format("%,d", Integer.parseInt(amount)*Integer.parseInt(price)), 0));
                        Glide.with(holder.itemView).load(HomeFragment.ProductArrayList.get(i).getImgid()).into(holder.ivProducts);
                    }
                }

        }

        if(Integer.parseInt(recyclerData.getAmount()) == 5){
            holder.addBtn.setEnabled(false);
        }

        holder.rmvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int amt = Integer.parseInt(holder.amtCart.getText().toString())-1;
                int prc = Integer.parseInt(recyclerData.getPprice());

                courseDataArrayList.get(holder.getAdapterPosition()).setAmount(String.valueOf(amt));
                onItemTouchListener.onBtnClick(v, holder.getAdapterPosition());

                int total_amt = (amt+1)*prc;

                holder.amtCart.setText(String.valueOf(amt));

                holder.priceProduct.setText(Html.fromHtml("<b>Total: </b>₹"+String.format("%,d", (total_amt)-(prc) ),0));

                if(amt < 5){
                    holder.addBtn.setEnabled(true);
                }
                if(amt <= 0){
                    courseDataArrayList.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                }

            }
        });

        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amt = Integer.parseInt(holder.amtCart.getText().toString())+1;
                int prc = Integer.parseInt(recyclerData.getPprice());

                courseDataArrayList.get(holder.getAdapterPosition()).setAmount(String.valueOf(amt));
                onItemTouchListener.onBtnClick(v, holder.getAdapterPosition());

                int total_amt = (amt-1)*prc;

                holder.amtCart.setText(String.valueOf(amt));
                holder.priceProduct.setText(Html.fromHtml("<b>Total: </b>₹"+String.format("%,d", (total_amt)+(prc) ),0));

                if(amt == 5){
                    holder.addBtn.setEnabled(false);
                }


            }
        });


    }

    public interface ItemTouchListener {

        void onBtnClick(View view, int position);

    }


    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return courseDataArrayList.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private final TextView amtCart;
        private TextView tvProducts, infoProduct, priceProduct;
        private ImageView ivProducts;

        private Button addBtn, rmvBtn;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProducts = itemView.findViewById(R.id.idIVProducts);
            tvProducts = itemView.findViewById(R.id.idTVProducts);
            infoProduct = itemView.findViewById(R.id.idTVProductsInfo);
            priceProduct = itemView.findViewById(R.id.idTVPrice);
            amtCart = itemView.findViewById(R.id.amountTv);

            addBtn = itemView.findViewById(R.id.addBtn);
            rmvBtn = itemView.findViewById(R.id.removeBtn);


        }

    }
}