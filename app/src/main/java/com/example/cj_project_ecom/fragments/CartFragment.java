package com.example.cj_project_ecom.fragments;

import static com.example.cj_project_ecom.HomeActivity.jsonString;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.example.cj_project_ecom.HomeActivity;
import com.example.cj_project_ecom.LoadingDialog;
import com.example.cj_project_ecom.ProductDetails;
import com.example.cj_project_ecom.R;
import com.example.cj_project_ecom.RequestHandler;
import com.example.cj_project_ecom.Utils;
import com.example.cj_project_ecom.adapters.CartAdapter;
import com.example.cj_project_ecom.models.CartModel;
import com.example.cj_project_ecom.models.ProductModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CartFragment extends Fragment implements CartAdapter.ItemTouchListener {
    private String logged_in;
    private LinearLayout emptyCtv;
    private RecyclerView cartGv;
    private String cartString;
    private JSONArray crtjsonarr;
    private JSONObject crtjsonstr;
    private CartAdapter adapter;
    private TextView sumTotal;
    private ArrayList<CartModel> cartArrayList;
    private LoadingDialog loadingDialog;
    private LinearLayout cartV;

    public CartFragment() {
        // Required empty public constructor
    }
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        emptyCtv = view.findViewById(R.id.emptyCtv);
        cartV = view.findViewById(R.id.cartV);
        sumTotal = view.findViewById(R.id.sumTotalTv);
        if(Utils.isLoggedin(getContext()).equalsIgnoreCase("user")){
           emptyCtv.setVisibility(View.GONE);
           cartV.setVisibility(View.VISIBLE);
        }

        cartGv = view.findViewById(R.id.idRvCart);
        cartArrayList = new ArrayList<CartModel>();

        cartString = Utils.getCartData();

        if(cartString.isEmpty() || cartString.equalsIgnoreCase("[]") || cartString == null){
            emptyCtv.setVisibility(View.VISIBLE);
            cartV.setVisibility(View.GONE);
        }

        int sum = 0;
        try {
            crtjsonarr = new JSONArray(cartString);

            for(int i=0; i < crtjsonarr.length(); i++) {
                crtjsonstr = new JSONObject(crtjsonarr.get(i).toString());
                cartArrayList.add(new CartModel(crtjsonstr.getString("puid"), crtjsonstr.getString("amount")));
                for (int j = 0; j < HomeFragment.ProductArrayList.size(); j++) {

                    if (crtjsonstr.getString("puid").equals(HomeFragment.ProductArrayList.get(j).getPuid())) {
                        sum = sum+Integer.parseInt(crtjsonstr.getString("amount"))*Integer.parseInt(HomeFragment.ProductArrayList.get(j).getPprice());
                    }
                }
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }

        sumTotal.setText("Total: ₹"+String.format("%,d", sum));
        
        adapter = new CartAdapter(cartArrayList, getActivity(), this);
        loadingDialog = new LoadingDialog(getContext());
        GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),1);

        cartGv.setLayoutManager(layoutManager);
        cartGv.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBtnClick(View view, int position) {

        int total_sum = 0;
        for (int j = 0; j < cartArrayList.size(); j++) {
            int price = Integer.parseInt(cartArrayList.get(j).getPprice());
            int amt = Integer.parseInt(cartArrayList.get(j).getAmount());
            total_sum = total_sum + (amt*price);
        }

        sumTotal.setText("Total: ₹"+String.format("%,d", total_sum));

        loadingDialog.show();
        if (view.getId() == R.id.addBtn){
            updateCart(Utils.getUserID(getContext()), cartArrayList.get(position).getPuid(), "add", cartArrayList.get(position).getAmount());
        }else if (view.getId() == R.id.removeBtn){
            updateCart(Utils.getUserID(getContext()), cartArrayList.get(position).getPuid(), "remove", cartArrayList.get(position).getAmount());

        }
    }

    private String cart_url = "https://sapotaceous-shame.000webhostapp.com/update_cart.php";

    private void updateCart(String uid, String pid, String type, String amount){
        class UpdateCart extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();

                if(type.equalsIgnoreCase("add")) {
                    params.put("puid", pid);
                    params.put("uid", uid);
                    params.put("type", "add");
                } else if(type.equalsIgnoreCase("remove")){
                    params.put("puid", pid);
                    params.put("uid", uid);
                    params.put("amt", amount);
                    params.put("type", "remove");
                }
                //returing the response
                return requestHandler.sendPostRequest(cart_url, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(loadingDialog != null){
                    loadingDialog.cancel();
                }

                try {
                    JSONObject obj = new JSONObject(s);
                    if(obj.getString("success").equalsIgnoreCase("1")){

                        JSONArray latestCart = new JSONArray(obj.getString("cart"));
                        Utils.setCartData(latestCart.toString());
                    } else {
                        AlertDialog ad =Utils.initAlertDialog(getContext(), "Server", obj.getString("message")).create();
                        ad.show();
                    }
                } catch (JSONException e) {

                }
            }
        }

        UpdateCart uc = new UpdateCart();
        uc.execute();
    }


}