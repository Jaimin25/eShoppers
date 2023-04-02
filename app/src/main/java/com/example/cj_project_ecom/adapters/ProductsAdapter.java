package com.example.cj_project_ecom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cj_project_ecom.models.ProductModel;
import com.example.cj_project_ecom.R;

import java.util.ArrayList;

public class ProductsAdapter extends ArrayAdapter<ProductModel> {

    public ProductsAdapter(@NonNull Context context, ArrayList<ProductModel> ProductsModelArrayList) {
        super(context, 0, ProductsModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.product_view, parent, false);
        }

        ProductModel ProductsModel = getItem(position);
        TextView ProductsTV = listitemView.findViewById(R.id.idTVProducts);
        ImageView ProductsIV = listitemView.findViewById(R.id.idIVProducts);

        ProductsTV.setText(ProductsModel.getProduct_name());
        ProductsIV.setImageResource(ProductsModel.getImgid());
        return listitemView;
    }
}
