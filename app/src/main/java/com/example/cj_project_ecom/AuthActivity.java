package com.example.cj_project_ecom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import android.widget.TextView;

import java.util.regex.Pattern;

public class AuthActivity extends AppCompatActivity {
    TextInputEditText username_et;
    TextInputEditText email_et;
    TextInputEditText password_et;
    MaterialButton submit_btn;
    TextView reg_toggle, guest_login;
    boolean is_login=true;

    String auth_type;

    TextInputLayout username_lay, email_lay, pass_lay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        username_et = findViewById(R.id.username_et);
        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);
        submit_btn = findViewById(R.id.submit_btn);
        reg_toggle = findViewById(R.id.reg_toggle);
        guest_login = findViewById(R.id.guest_login);

        username_lay = findViewById(R.id.username_et_lay);
        email_lay = findViewById(R.id.email_et_lay);
        pass_lay = findViewById(R.id.password_et_lay);

        SharedPreferences sharedPreferences = getSharedPreferences("AppData", MODE_PRIVATE);

        auth_type = sharedPreferences.getString("auth_type", "");
        Log.w("", auth_type);
        if(auth_type != null && auth_type.equals("na")){

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
                    reg_toggle.setText("Already a member? Login here.");
                } else {
                    is_login=true;
                    submit_btn.setText("Login");
                    username_lay.setVisibility(View.GONE);
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
                SharedPreferences sharedPreferences = getSharedPreferences("AppData", MODE_PRIVATE);
                SharedPreferences.Editor shEdit = sharedPreferences.edit();

                shEdit.putString("auth_type", "na");
                shEdit.apply();
                Intent i = new Intent(AuthActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

}