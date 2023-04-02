package com.example.cj_project_ecom.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cj_project_ecom.ProductModel;
import com.example.cj_project_ecom.ProductsAdapter;
import com.example.cj_project_ecom.R;

import android.os.Bundle;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    GridView coursesGV;
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

        coursesGV = view.findViewById(R.id.idGVProducts);

        ArrayList<ProductModel> ProductArrayList = new ArrayList<ProductModel>();

        for(int i=0; i < 13; i++){
            ProductArrayList.add(new ProductModel("Product "+String.valueOf(i+1), 4.3, R.drawable.ic_car_foreground));
        }
        ProductsAdapter adapter = new ProductsAdapter(getActivity(), ProductArrayList);
        coursesGV.setAdapter(adapter);

        return view;


    }
}