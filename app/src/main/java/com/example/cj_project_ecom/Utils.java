package com.example.cj_project_ecom;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class Utils {
    private static String logged_in;

    public static void initToast(Context ctx, String msg){
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }

    public static AlertDialog.Builder initAlertDialog(Context ctx, String title, String msg){
        AlertDialog.Builder adb = new AlertDialog.Builder(ctx);
        adb.setTitle(title);
        adb.setMessage(msg);
        return adb;
    }

    public static boolean isLoggedin(Context ctx){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("AppData_user", ctx.MODE_PRIVATE);
        logged_in = sharedPreferences.getString("logged_in", "");

        if(logged_in != null && logged_in.equalsIgnoreCase("true")){
            return true;
        } else if (logged_in.equalsIgnoreCase("guest")) {
            return true;
        } else {
            return false;
        }
    }
}
