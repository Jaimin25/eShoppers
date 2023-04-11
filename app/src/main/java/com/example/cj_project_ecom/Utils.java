package com.example.cj_project_ecom;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Utils {
    private static String logged_in;

    private static String cart_data;
    public static String getCartData() {
        return cart_data;
    }
    public static void setCartData(String data) {
        cart_data = data;
    }

    public static void initToast(Context ctx, String msg){
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }

    public static String getUserName(Context ctx){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("AppData_user", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        return username;
    }
    public static String getUserEmail(Context ctx){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("AppData_user", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");

        return email;
    }public static String getUserID(Context ctx){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("AppData_user", Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "");

        return uid;
    }

    public static AlertDialog.Builder initAlertDialog(Context ctx, String title, String msg){
        AlertDialog.Builder adb = new AlertDialog.Builder(ctx);
        adb.setTitle(title);
        adb.setMessage(msg);
        return adb;
    }

    public static String isLoggedin(Context ctx){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("AppData_user", ctx.MODE_PRIVATE);
        logged_in = sharedPreferences.getString("logged_in", "");

        if(logged_in != null && logged_in.equalsIgnoreCase("true")){
            return "user";
        } else if (logged_in.equalsIgnoreCase("guest")) {
            return "guest";
        }
        return "nl";
    }

    public static String getJsonFromServer(String url) throws IOException {

        BufferedReader inputStream = null;

        URL jsonUrl = new URL(url);
        URLConnection dc = jsonUrl.openConnection();

        dc.setConnectTimeout(5000);
        dc.setReadTimeout(5000);

        inputStream = new BufferedReader(new InputStreamReader(
                dc.getInputStream()));

        // read the JSON results into a string
        String jsonResult = inputStream.readLine();
        return jsonResult;
    }
}
