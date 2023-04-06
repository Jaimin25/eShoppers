package com.example.cj_project_ecom;

import static android.provider.SyncStateContract.Columns.DATA;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.AsyncTask;

import com.google.android.material.button.MaterialButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.regex.Pattern;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import java.util.UUID;

public class AuthActivity extends AppCompatActivity {
    TextInputEditText username_et;
    TextInputEditText email_et;
    TextInputEditText password_et, c_password_et;
    MaterialButton submit_btn;
    TextView reg_toggle, guest_login;

    boolean is_login = true;
    ProgressBar pb1;

    TextInputLayout username_lay, email_lay, pass_lay, c_pass_lay;

    private static final String ROOT_URL = "https://sapotaceous-shame.000webhostapp.com/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        username_et = findViewById(R.id.username_et);
        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);
        c_password_et = findViewById(R.id.c_password_et);

        submit_btn = findViewById(R.id.submit_btn);
        reg_toggle = findViewById(R.id.reg_toggle);
        guest_login = findViewById(R.id.guest_login);

        username_lay = findViewById(R.id.username_et_lay);
        email_lay = findViewById(R.id.email_et_lay);
        pass_lay = findViewById(R.id.password_et_lay);
        c_pass_lay = findViewById(R.id.c_password_et_lay);

        pb1 = findViewById(R.id.progressBar);
        pb1.setVisibility(View.GONE);

        if(Utils.isLoggedin(AuthActivity.this)){
            Intent i = new Intent(AuthActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }

        reg_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(is_login) {
                    is_login=false;
                    submit_btn.setText("Register");
                    username_lay.setVisibility(View.VISIBLE);
                    c_pass_lay.setVisibility(View.VISIBLE);
                    reg_toggle.setText("Already a member? Login here.");
                } else {
                    is_login=true;
                    submit_btn.setText("Login");
                    username_lay.setVisibility(View.GONE);
                    c_pass_lay.setVisibility(View.GONE);

                    reg_toggle.setText("Do not have an account? Register here.");
                }
            }
        });

        email_et.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pattern EMAIL_ADDRESS_PATTERN = Pattern
                        .compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
                                + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
                                + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");
                if(!EMAIL_ADDRESS_PATTERN.matcher(s).matches())
                {
                    email_lay.setError("valid email id is required");
                    email_lay.setErrorEnabled(true);
                } else {
                    email_lay.setErrorEnabled(false);
                }
            }
        });

        guest_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("AppData_user", MODE_PRIVATE);
                SharedPreferences.Editor shEdit = sharedPreferences.edit();

                shEdit.putString("logged_in", "guest");
                shEdit.apply();
                Intent i = new Intent(AuthActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(submit_btn.getText().toString().toLowerCase().trim().equals("register")){
                    if(!validateEmpty(username_et) && !validateEmpty(email_et) && !validateEmpty(password_et) && !validateEmpty(c_password_et)){
                        if(password_et.getText().toString().trim().equalsIgnoreCase(c_password_et.getText().toString().trim().toLowerCase())) {
                            registerUser();
                        } else {
                            c_password_et.setError("Password not matching");
                        }
                    }
                } else {
                    if(!validateEmpty(email_et) && !validateEmpty(password_et)){
                            userLogin();

                    }
                }
            }
        });

    }

    private void registerUser(){
        final String username = username_et.getText().toString().trim();
        final String email = email_et.getText().toString().trim();
        final String password = password_et.getText().toString().trim();

        class RegisterUser extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("type", "register");
                params.put("uid", getSaltString());
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);

                //returing the response
                return requestHandler.sendPostRequest(ROOT_URL, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (obj.getString("success").equals("1")) {
                        Utils.initToast(AuthActivity.this, obj.getString("message"));
                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");
                        String sh_username = userJson.getString("username").toString();
                        String sh_email = userJson.getString("email").toString();
                        String sh_uid = userJson.get("uid").toString();

                        SharedPreferences sharedPreferences = getSharedPreferences("AppData_user", MODE_PRIVATE);
                        SharedPreferences.Editor shEdit = sharedPreferences.edit();

                        shEdit.putString("logged_in", "true");
                        shEdit.putString("uid", sh_uid);
                        shEdit.putString("username", sh_username);
                        shEdit.putString("email", sh_email);
                        shEdit.apply();

                        Intent i = new Intent(AuthActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Utils.initAlertDialog(AuthActivity.this, "Server Response", obj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }

    private void userLogin() {
        final String email = email_et.getText().toString();
        final String password = password_et.getText().toString();

        class UserLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);
                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    //if no error in response
                    if (obj.getString("success").equalsIgnoreCase("1")) {
                        Utils.initToast(AuthActivity.this, obj.getString("message"));
                        //getting the user from the response
                        JSONArray user = obj.getJSONArray("user");
                        String sh_username = user.get(2).toString();
                        String sh_email = user.get(4).toString();
                        String sh_uid = user.get(1).toString();

                        SharedPreferences sharedPreferences = getSharedPreferences("AppData_user", MODE_PRIVATE);
                        SharedPreferences.Editor shEdit = sharedPreferences.edit();

                        shEdit.putString("logged_in", "true");
                        shEdit.putString("uid", sh_uid);
                        shEdit.putString("username", sh_username);
                        shEdit.putString("email", sh_email);
                        shEdit.apply();

                        Intent i = new Intent(AuthActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        initAlertDialog(AuthActivity.this, "Server Response", obj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("type", "login");
                params.put("email", email);
                params.put("password", password);

                //returing the response
                return requestHandler.sendPostRequest(ROOT_URL, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }

    private boolean validateEmpty(TextInputEditText tiet){
        if(tiet.getText().toString().equalsIgnoreCase("")){
            tiet.setError("Field Required");
            return true;
        } else {
            return false;
        }
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    private void initAlertDialog(Context ctx, String title, String msg){
        AlertDialog.Builder adb = new AlertDialog.Builder(ctx);
        adb.setTitle(title);
        adb.setMessage(msg);

        AlertDialog adc = adb.create();
        adc.show();
    }

}