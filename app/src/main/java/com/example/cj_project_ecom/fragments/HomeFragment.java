package com.example.cj_project_ecom.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.util.Util;
import com.example.cj_project_ecom.AuthActivity;
import com.example.cj_project_ecom.HomeActivity;
import com.example.cj_project_ecom.ProductDetails;
import com.example.cj_project_ecom.Utils;
import com.example.cj_project_ecom.models.ProductModel;
import com.example.cj_project_ecom.adapters.ProductsAdapter;
import com.example.cj_project_ecom.R;

import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView coursesGV;
    private AsyncTask<Void, Void, Void> mTask;

    String jsonString, favString;
    private JSONArray pjsonarr, fjsonarr;
    private JSONObject pjsonstr, fjsonstr;

    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        coursesGV = view.findViewById(R.id.idRvProducts);

        ArrayList<ProductModel> ProductArrayList = new ArrayList<ProductModel>();
        jsonString = HomeActivity.jsonString;

        try {
            pjsonarr = new JSONArray(jsonString);

            for(int i=0; i < pjsonarr.length(); i++){
                pjsonstr = new JSONObject(pjsonarr.get(i).toString());
                ProductArrayList.add(new ProductModel(pjsonstr.getString("pname"), 4.3, pjsonstr.getString("puid"), "https://sapotaceous-shame.000webhostapp.com/product_img_resources/" + pjsonstr.getString("pimg"), pjsonstr.getString("pinfo"), pjsonstr.getString("pprice"), pjsonstr.getString("stock")));

            }
            ProductsAdapter adapter = new ProductsAdapter(ProductArrayList, getActivity(), new ProductsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ProductModel item) {
                    Intent intnt = new Intent(getActivity(), ProductDetails.class);
                    intnt.putExtra("puid", item.getPuid());
                    intnt.putExtra("pname", item.getProduct_name());
                    intnt.putExtra("pinfo", item.getPinfo());
                    intnt.putExtra("pprice", item.getPprice());
                    intnt.putExtra("pimg", item.getImgid());
                    intnt.putExtra("in_stock", item.getIn_stock());
                    startActivity(intnt);
                }
            });

            GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),2);
            coursesGV.setLayoutManager(layoutManager);
            coursesGV.setAdapter(adapter);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        return view;


    }
}