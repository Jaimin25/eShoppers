package com.example.cj_project_ecom;


import android.content.Intent;
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
import androidx.appcompat.widget.Toolbar;

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

    private boolean in_stock, is_fav;
    private String fav_url = "https://sapotaceous-shame.000webhostapp.com/add_to_fav.php";
    private JSONArray fjsonarr;
    private String favString;
    private JSONObject fjsonstr;
    private String img_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);

        Toolbar tl = findViewById(R.id.toolbar);
        setSupportActionBar(tl);

        RelativeLayout compBtn = findViewById(R.id.image_btn);
        ImageView cart_img = findViewById(R.id.image);
        ImageView favIv = findViewById(R.id.favIv);
        TextView cart_tv = findViewById(R.id.cartTv);

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


        pnameTv.setText(Html.fromHtml("<b>Product Name: </b>"+pname));
        pinfoTv.setText(Html.fromHtml("<b>Product Details: </b>"+pinfo));

        Glide.with(this).load(img_url).into(pimgIv);

        if(!in_stock){
            compBtn.setBackgroundColor(Color.parseColor("#ff0000"));
            compBtn.setEnabled(false);
            cart_img.setVisibility(View.INVISIBLE);
            cart_tv.setText("Out Of Stock!");
        }

        favString = HomeActivity.favString;

        try {
            fjsonarr = new JSONArray(favString);

            for(int i=0; i < fjsonarr.length(); i++){
                fjsonstr = new JSONObject(fjsonarr.get(i).toString());
                if(fjsonstr.getString("puid").equalsIgnoreCase(puid)){
                    is_fav = true;
                    Utils.initToast(this, "Favourite");
                    favIv.setBackgroundResource(R.drawable.ic_fav_filled_foreground);
                } else {
                    is_fav = false;
                    favIv.setBackgroundResource(R.drawable.ic_fav_border_foreground);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        compBtn.setOnClickListener(view -> {

        });

        favLv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToFav(Utils.getUserID(ProductDetails.this), puid);
            }
        });

    }

    private void addToFav(String uid, String pid){
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
                AlertDialog ad =Utils.initAlertDialog(ProductDetails.this, "Server", s).create();
                ad.show();
            }
        }

        AddFav af = new AddFav();
        af.execute();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}