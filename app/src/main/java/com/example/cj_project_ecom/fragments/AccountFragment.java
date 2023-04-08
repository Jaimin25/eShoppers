package com.example.cj_project_ecom.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cj_project_ecom.AuthActivity;
import com.example.cj_project_ecom.R;
import com.example.cj_project_ecom.Utils;

public class AccountFragment extends Fragment {
    private LinearLayout guestView, profileView;
    private Button auth_btn;

    private TextView usernameTv, emailTv, uidTv;
    private Button logoutBtn;

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

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppData_user", Context.MODE_PRIVATE);
        SharedPreferences.Editor shEdit = sharedPreferences.edit();

        guestView = view.findViewById(R.id.guestView);
        profileView = view.findViewById(R.id.profileView);
        auth_btn = view.findViewById(R.id.auth_btn);

        usernameTv = view.findViewById(R.id.usernameTv);
        emailTv = view.findViewById(R.id.emailTv);
        uidTv = view.findViewById(R.id.uidTv);

        logoutBtn = view.findViewById(R.id.logoutBtn);

        if (Utils.isLoggedin(view.getContext()).equalsIgnoreCase("guest")){
            guestView.setVisibility(View.VISIBLE);
        }else if(Utils.isLoggedin(view.getContext()).equalsIgnoreCase("user")){
            guestView.setVisibility(View.GONE);
            usernameTv.setText(Html.fromHtml("<b>Username: </b>"+ Utils.getUserName(getContext())));
            emailTv.setText(Html.fromHtml("<b>Email: </b>"+ Utils.getUserEmail(getContext())));
            uidTv.setText(Html.fromHtml("<b>Uid: </b>" + Utils.getUserID(getContext())));
        }

        auth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shEdit.clear();
                shEdit.apply();
                Intent i = new Intent(view.getContext(), AuthActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setCancelable(false);
                adb.setTitle("Logout");
                adb.setMessage("Are you sure, you want to logout?");

                adb.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        shEdit.clear();
                        shEdit.putString("logged_in", "guest");
                        shEdit.apply();
                        guestView.setVisibility(View.VISIBLE);
                        Utils.initToast(getContext(), "Successfully logged out");
                    }
                });
                adb.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog adc = adb.create();
                adc.show();
            }
        });

        return view;
    }
}