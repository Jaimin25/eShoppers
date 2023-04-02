package com.example.cj_project_ecom.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.cj_project_ecom.AuthActivity;
import com.example.cj_project_ecom.HomeActivity;
import com.example.cj_project_ecom.R;

public class AccountFragment extends Fragment {
    LinearLayout guestView, profileView;
    Button auth_btn;

    String auth_type;
    public AccountFragment() {
        // Required empty public constructor
    }
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
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
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppData", Context.MODE_PRIVATE);
        SharedPreferences.Editor shEdit = sharedPreferences.edit();

        auth_type = sharedPreferences.getString("auth_type", "");
        Log.w("", auth_type);
        guestView = view.findViewById(R.id.guestView);
        profileView = view.findViewById(R.id.profileView);
        auth_btn = view.findViewById(R.id.auth_btn);

        if(auth_type != null && !auth_type.equals("na")){
            guestView.setVisibility(View.GONE);
        }

        auth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shEdit.remove("auth_type");
                shEdit.apply();
                Intent i = new Intent(view.getContext(), AuthActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        return view;
    }
}