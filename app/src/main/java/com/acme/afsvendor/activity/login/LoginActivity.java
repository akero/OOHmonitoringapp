package com.acme.afsvendor.activity.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.acme.afsvendor.R;
import com.acme.afsvendor.activity.dashboard.AdminDashboardActivity;
import com.acme.afsvendor.activity.dashboard.BaseActivity;
import com.acme.afsvendor.activity.dashboard.ClientDashBoardActivity;
import com.acme.afsvendor.activity.vender.VenderDashBoardActivity;
import com.acme.afsvendor.databinding.ActivityLoginBinding;
import com.acme.afsvendor.utility.AppPreferences;
import com.acme.afsvendor.utility.NetworkUtils;
import com.acme.afsvendor.viewmodel.ApiInterface;
import com.acme.afsvendor.viewmodel.LoginActivityViewModel;

import java.io.FileOutputStream;

public class LoginActivity extends BaseActivity implements ApiInterface {

    private ActivityLoginBinding binding;
    private boolean hidePassword = true;
    private int loginType = 1;
    private LoginActivityViewModel loginActivityViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("tg3", "0");

        Log.d("whichclass", "LoginActivity");

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.etLoginid.setText(getIntent().getStringExtra("Email"));

        loginActivityViewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);
        loginActivityViewModel.getSuccessresponse().observe(this, response -> {
            hideProgressDialog();
            Toast.makeText(LoginActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("result1", "response " + response);

            AppPreferences.getInstance(LoginActivity.this).saveUserData(response.toString());


            if (loginType == 0) {//client


                Log.d("tg7", "client");
                startActivity(new Intent(LoginActivity.this, ClientDashBoardActivity.class));

            } else if (loginType == 1) {//admin
                Log.d("tg7", "admin");
                startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));

            } else {//vendor
                Log.d("tg7", "vendor");
                startActivity(new Intent(LoginActivity.this, VenderDashBoardActivity.class));
            }
        });

        loginActivityViewModel.getErrorMessage().observe(this, errorMessage -> {
            hideProgressDialog();
            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            Log.d("result1", "response " + errorMessage);
        });
    }

    public void btnloginClick(View view) {
        Context context= this;

        if (binding.etLoginid.getText().toString().isEmpty() || binding.etPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_LONG).show();
        } else if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "Check your Internet Connection and Try Again", Toast.LENGTH_LONG).show();
        } else {
try {
    loginActivityViewModel.callLogin(
            binding.etLoginid.getText().toString(),
            binding.etPassword.getText().toString(),
            context, loginType);
    Log.d("tag4", "success");
}catch(Exception e){
    Log.d("tag4", e.toString());
}
            }
        }


    @SuppressLint("ResourceAsColor")
    public void btnAdminClick(View view) {

        loginType = 1;

        binding.tvAdminLogin.setTextColor(Color.WHITE);
        binding.tvClientLogin.setTextColor(R.color.colorPrimaryDark);
        binding.tvVenderLogin.setTextColor(R.color.colorPrimaryDark);

        binding.tvAdminLogin.setBackgroundResource(R.drawable.primaryround);
        binding.tvClientLogin.setBackgroundResource(0);
        binding.tvVenderLogin.setBackgroundResource(0);

        // Set backgrounds and colors similarly for the views as mentioned in the Kotlin code
        // Repeat this for other methods too
    }

    @SuppressLint("ResourceAsColor")
    public void btnVenderClick(View view) {

        loginType = 2;

        binding.tvAdminLogin.setTextColor(R.color.colorPrimaryDark);
        binding.tvClientLogin.setTextColor(R.color.colorPrimaryDark);
        binding.tvVenderLogin.setTextColor(Color.WHITE);

        binding.tvAdminLogin.setBackgroundResource(0);
        binding.tvClientLogin.setBackgroundResource(0);
        binding.tvVenderLogin.setBackgroundResource(R.drawable.primaryround);

        // Set backgrounds and colors similarly for the views as mentioned in the Kotlin code
    }

    @SuppressLint("ResourceAsColor")
    public void btnClientClick(View view) {

        loginType = 0;

        binding.tvAdminLogin.setTextColor(R.color.colorPrimaryDark);
        binding.tvClientLogin.setTextColor(Color.WHITE);
        binding.tvVenderLogin.setTextColor(R.color.colorPrimaryDark);

        binding.tvAdminLogin.setBackgroundResource(0);
        binding.tvClientLogin.setBackgroundResource(R.drawable.primaryround);
        binding.tvVenderLogin.setBackgroundResource(0);

        // Set backgrounds and colors similarly for the views as mentioned in the Kotlin code
    }

    public void onForgotPasswordClick(View view) {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }

    public void onHidePassword(View view) {
        if (hidePassword) {
            hidePassword = false;
            binding.ivHidepassword.setImageResource(R.drawable.ic_eye);
            binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            hidePassword = true;
            binding.ivHidepassword.setImageResource(R.drawable.ic_eye_hide);
            binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    @Override
    public void onResponseReceived(String response){

        //returning different login tokens for every query

        if (loginType == 0) {

            String res="";
            res=response.replace("{\"success\":true,\"data\":{\"token\":\"","");
            int a= res.indexOf(",");
            res=res.substring(0,a-1);
            writeToFile(res, this);

            Log.d("tag23", "0" +res);

            startActivity(new Intent(LoginActivity.this, ClientDashBoardActivity.class));

        } else if (loginType == 1) {

            String res="";
            res=response.replace("{\"success\":true,\"data\":{\"token\":\"","");
            int a= res.indexOf(",");
            res=res.substring(0,a-1);
            writeToFile(res, this);

            Log.d("tag23", "1"+ res);

            startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));

        } else {

            String res="";
            res=response.replace("{\"success\":true,\"data\":{\"token\":\"","");
            int a= res.indexOf(",");
            res=res.substring(0,a-1);
            writeToFile(res, this);
            Log.d("tag23", "2"+res);


            startActivity(new Intent(LoginActivity.this, VenderDashBoardActivity.class));
        }

    }


    //saving login token to file
    void writeToFile(String response, Context context){
        String name="logintoken";
        String content= response;
        FileOutputStream fostream= null;

        try{
            fostream= context.openFileOutput(name,Context.MODE_PRIVATE);
            fostream.write(response.getBytes());
            fostream.close();

        }catch(Exception e){
            Log.d("tag24", "error-" +e.toString());
        }
        finally{
            try{

                if(fostream!=null){
                    fostream.close();
                }
            }catch(Exception e){
                Log.d("tag25","Closing outputstream failed");
            }
        }
    }
}