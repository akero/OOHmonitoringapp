package com.acme.afsvendor.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.acme.afsvendor.activity.dashboard.FileHelper;
import com.acme.afsvendor.activity.dashboard.RecceHistory;
import com.acme.afsvendor.activity.dashboard.RecceInstallation;
import com.acme.afsvendor.viewmodel.APIreferenceclass;
import com.acme.afsvendor.viewmodel.ApiInterface;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.navigation.ui.AppBarConfiguration;

import com.acme.afsvendor.databinding.ActivityOtpBinding;

import com.acme.afsvendor.R;

public class OTP extends AppCompatActivity implements ApiInterface {

    private AppBarConfiguration appBarConfiguration;
    private ActivityOtpBinding binding;
    String emailInput;

    ProgressBar progressBar;
    Animation rotateAnimation;
    String userTypeCheck;
    String logintoken, userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d("whichclass", "OTP");

        int loginType= 1;
        userTypeCheck= "";
        logintoken= "";
        userid= "";

        //animation code
        progressBar= findViewById(R.id.progressBar);
        rotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_animation);
        //animation code

        Log.d("tag51", Integer.toString(loginType));

        EditText email= findViewById(R.id.etEmailId);
        Button btn= findViewById(R.id.btnNext);

        Context context= this;

        try {
            userTypeCheck = FileHelper.readUserType(OTP.this);
            logintoken= FileHelper.readLoginToken(OTP.this);
            userid= FileHelper.readUserId(OTP.this);

        }catch(Exception e){
            Log.d("tag23232", e.toString());
        }

        if(userTypeCheck != null && userTypeCheck.equals("supervisor")){

            //TODO change
            Intent intent= new Intent(OTP.this, RecceInstallation.class);
            intent.putExtra("logintoken", logintoken);
            intent.putExtra("recceid", userid);


            startActivity(intent);
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailInput = email.getText().toString().trim();

                // Check if the input is empty
                if(emailInput.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Email cannot be empty", Toast.LENGTH_SHORT).show();
                }
                // Check if the email format is correct using a regex pattern.
                else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    Toast.makeText(getApplicationContext(), "Invalid email format", Toast.LENGTH_SHORT).show();
                }
                // If the email input is valid, start the new activity.
                else {


                    if (loginType == 0) {//client

                        Log.d("tag23", "0");

                        //animation code
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.startAnimation(rotateAnimation);
                        //view.setVisibility(View.VISIBLE);
                        //animation code

                        APIreferenceclass api= new APIreferenceclass(loginType, context, emailInput, "");

                    } else if (loginType == 1) {//admin

                        Log.d("tag23", "1");

                        //animation code
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.startAnimation(rotateAnimation);
                        //view.setVisibility(View.VISIBLE);
                        //animation code


                            APIreferenceclass api = new APIreferenceclass(loginType, context, emailInput, "");

                        //startActivity(new Intent(OTP.this, AdminDashboardActivity.class));
                    } else {//vendor
                        Log.d("tag23", "2");

                        //animation code
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.startAnimation(rotateAnimation);
                        //view.setVisibility(View.VISIBLE);
                        //animation code

                        APIreferenceclass api= new APIreferenceclass(loginType, context, emailInput, "");
                    }

                }
            }

        });



    }

    @Override
    public void onResponseReceived(String response){

        Log.d("tag23", response);

        Intent intent = new Intent(OTP.this, ContentOtp.class);

        intent.putExtra("email", emailInput);
        //intent.putExtra("loginType", loginType);

        //animation code
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.clearAnimation();
                progressBar.setVisibility(View.GONE);
                //view.setVisibility(View.GONE);
            }
        });
        //animation code

        startActivity(intent);
    }
}