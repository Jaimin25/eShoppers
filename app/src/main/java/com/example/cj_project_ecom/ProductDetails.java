package com.example.cj_project_ecom;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProductDetails extends AppCompatActivity {

    private TextView pnameTv, pinfoTv;
    private ImageView pimgIv;

    private String pname, pinfo, puid, pprice;

    private boolean in_stock, is_fav = false;
    private String fav_url = "https://sapotaceous-shame.000webhostapp.com/update_favs.php";
    private String cart_url = "https://sapotaceous-shame.000webhostapp.com/update_cart.php";
    private JSONArray fjsonarr;
    private String favString;
    private JSONObject fjsonstr;
    private String img_url;
    private LoadingDialog loadingDialog;
    private AppCompatButton cartBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);

        Toolbar tl = findViewById(R.id.toolbar);
        setSupportActionBar(tl);

        cartBtn = findViewById(R.id.cartBtn);
        ImageView favIv = findViewById(R.id.favIv);

        LinearLayout favLv = findViewById(R.id.fav);

        pnameTv = findViewById(R.id.pnameTv);
        pinfoTv = findViewById(R.id.pinfoTv);
        pimgIv = findViewById(R.id.pimgIv);

        Intent p_intent = getIntent();

        img_url = p_intent.getStringExtra("pimg");
        puid = p_intent.getStringExtra("puid");
        pname = p_intent.getStringExtra("pname");
        pinfo = p_intent.getStringExtra("pinfo");
        pprice = p_intent.getStringExtra("pprice");
        in_stock = p_intent.getStringExtra("in_stock").equalsIgnoreCase("1") ? true : false;

        pnameTv.setText(Html.fromHtml("<b>Product Name: </b>"+pname, 0));
        pinfoTv.setText(Html.fromHtml("<b>Product Details: </b>"+pinfo, 0));

        loadingDialog = new LoadingDialog(ProductDetails.this);

        Glide.with(this).load(img_url).into(pimgIv);

        if(!in_stock){
            cartBtn.setEnabled(false);
            cartBtn.setText("Out Of Stock!");
            cartBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0f0f")));;
        }

        favString = HomeActivity.favString;

        favIv.setImageResource(R.drawable.ic_fav_border_foreground);
        is_fav = false;

        try {
            fjsonarr = new JSONArray(favString);

            for(int i=0; i < fjsonarr.length(); i++){
                fjsonstr = new JSONObject(fjsonarr.get(i).toString());
                if(fjsonstr.getString("puid").equalsIgnoreCase(puid)){
                    is_fav = true;
                    favIv.setImageResource(R.drawable.ic_fav_filled_foreground);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        favLv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Utils.isLoggedin(ProductDetails.this).equalsIgnoreCase("user")){
                    favLv.setEnabled(false);
                    if(is_fav){
                        favIv.setImageResource(R.drawable.ic_fav_border_foreground);
                        is_fav = false;
                    } else {
                        favIv.setImageResource(R.drawable.ic_fav_filled_foreground);
                        is_fav = true;
                    }
                    loadingDialog.setCancelable(false);
                    loadingDialog.show();
                    addToFav(Utils.getUserID(ProductDetails.this), puid, favLv);

                } else {
                    Utils.initToast(ProductDetails.this, "Please authenticate");

                    SharedPreferences sharedPreferences = getSharedPreferences("AppData_user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor shEdit = sharedPreferences.edit();
                    shEdit.clear();
                    shEdit.apply();
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), AuthActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.isLoggedin(ProductDetails.this).equalsIgnoreCase("user")){

                    loadingDialog.show();
                    addToCart(Utils.getUserID(ProductDetails.this), puid);
                } else {
                    Utils.initToast(ProductDetails.this, "Please authenticate");

                    SharedPreferences sharedPreferences = getSharedPreferences("AppData_user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor shEdit = sharedPreferences.edit();
                    shEdit.clear();
                    shEdit.apply();
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), AuthActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void addToFav(String uid, String pid, LinearLayout ll){
        class AddFav extends AsyncTask<Void, Void, String>{

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("puid", pid);
                params.put("uid", uid);

                //returing the response
                return requestHandler.sendPostRequest(fav_url, params);
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
                        ll.setEnabled(true);
                        if(obj.getString("message").equalsIgnoreCase("added")){
                            Utils.initToast(ProductDetails.this, "Added to favourites");
                        } else if (obj.getString("message").equalsIgnoreCase("removed")){
                            Utils.initToast(ProductDetails.this, "Removed from favourites");
                        }
                        JSONArray newFavs = new JSONArray(obj.getString("favs"));
                        HomeActivity.favString = newFavs.toString();
                    } else {
                        AlertDialog ad =Utils.initAlertDialog(ProductDetails.this, "Server", obj.getString("message")).create();
                        ad.show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        AddFav af = new AddFav();
        af.execute();
    }

    private void addToCart(String uid, String pid){
        class UpdateCart extends AsyncTask<Void, Void, String>{

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("puid", pid);
                params.put("uid", uid);
                params.put("type", "add");

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
                        if(obj.getString("message").equalsIgnoreCase("added")){
                            Utils.initToast(ProductDetails.this, "Added to cart");
                        } else if (obj.getString("message").equalsIgnoreCase("removed")){
                            Utils.initToast(ProductDetails.this, "Cart Updated");
                        }
                        JSONArray latestCart = new JSONArray(obj.getString("cart"));
                        Utils.setCartData(latestCart.toString());
                } else {
                        AlertDialog ad =Utils.initAlertDialog(ProductDetails.this, "Server", obj.getString("message")).create();
                        ad.show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        UpdateCart uc = new UpdateCart();
        uc.execute();
    }
    @Override
    public void onBackPressed() {
                finish();
                super.onBackPressed();


    }
}