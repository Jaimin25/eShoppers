package com.example.cj_project_ecom;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.util.Util;
import com.example.cj_project_ecom.fragments.HomeFragment;
import com.example.cj_project_ecom.fragments.CartFragment;
import com.example.cj_project_ecom.fragments.AccountFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.View;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    private AsyncTask<Void, Void, String> mTask;
    public static String jsonString, favString;
    String url = "https://sapotaceous-shame.000webhostapp.com/get_products.php";
    String getFavUrl = "https://sapotaceous-shame.000webhostapp.com/get_fav.php";
    String getCartUrl = "https://sapotaceous-shame.000webhostapp.com/get_cart.php";
    private LinearLayout progress;


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Toolbar tl = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tl);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        progress = findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        bottomNavigation.getMenu().getItem(1).setEnabled(false);
        bottomNavigation.getMenu().getItem(2).setEnabled(false);
        mTask = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                try {
                    jsonString = Utils.getJsonFromServer(url);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> prms = new HashMap<>();
                prms.put("uid", Utils.getUserID(HomeActivity.this));

                //returing the response
                return requestHandler.sendPostRequest(getFavUrl, prms);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                try {
                    JSONObject jo = new JSONObject(result);
                    favString = jo.getString("favs");

                    Utils.setCartData(jo.getString("cart"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                progress.setVisibility(View.GONE);
                openFragment(HomeFragment.newInstance("", ""));

                bottomNavigation.getMenu().getItem(1).setEnabled(true);
                bottomNavigation.getMenu().getItem(2).setEnabled(true);
            }

        };
        mTask.execute();
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home:
                            openFragment(HomeFragment.newInstance("", ""));
                            return true;
                        case R.id.cart:
                            openFragment(CartFragment.newInstance("", ""));
                            return true;
                        case R.id.account:
                            openFragment(AccountFragment.newInstance("", ""));
                            return true;
                    }
                    return false;
                }
            };

}