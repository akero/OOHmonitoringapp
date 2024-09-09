package com.acme.afsvendor.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.acme.afsvendor.R;
import com.acme.afsvendor.activity.dashboard.AdminDashboardActivity;
import com.acme.afsvendor.activity.dashboard.ClientDashBoardActivity;
import com.acme.afsvendor.activity.dashboard.FileHelper;
import com.acme.afsvendor.activity.dashboard.RecceAsmDashboard;
import com.acme.afsvendor.activity.dashboard.RecceDashboardActivity;
import com.acme.afsvendor.activity.dashboard.RecceHistory;
import com.acme.afsvendor.activity.dashboard.RecceInstallation;
import com.acme.afsvendor.activity.vender.VenderDashBoardActivity;
import com.acme.afsvendor.viewmodel.APIreferenceclass;
import com.acme.afsvendor.viewmodel.ApiInterface;

import org.json.JSONObject;

public class ContentOtp extends AppCompatActivity implements ApiInterface {

    private EditText otp1, otp2, otp3, otp4, otp5;
    String email="";
    ProgressBar progressBar;
    Animation rotateAnimation;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_otp); // make sure to use your actual layout name

        Log.d("whichclass", "ContentOtp");

        context= this;

        // Initialize UI Elements
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        //otp5 = findViewById(R.id.otp5);
        //Button btnNext = findViewById(R.id.btnNext);

        //animation code
        progressBar= findViewById(R.id.progressBar);
        rotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_animation);
        //animation code

        // Setup auto-advance
        setupAutoAdvance(otp1, otp2);
        setupAutoAdvance(otp2, otp3);
        setupAutoAdvance(otp3, otp4);
        //setupAutoAdvance(otp4, otp5);
        setupAutoAdvance(otp4, null);

        email= getIntent().getStringExtra("email");

       // btnNext.setOnClickListener(new View.OnClickListener() {
           // @Override
         //   public void onClick(View v) {
              //  validateAndContinue();
          //  }
       // });
    }

    private void setupAutoAdvance(final EditText currentEditText, final EditText nextEditText) {
        currentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this functionality
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Check if a single digit is entered
                if (s.length() == 1 && nextEditText != null) {
                    nextEditText.requestFocus();
                }else if (s.length() == 1 && nextEditText == null) {
                    // When the last EditText is filled, validate and continue
                    validateAndContinue();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed for this functionality
            }
        });
    }

    private void validateAndContinue() {
        String otp = otp1.getText().toString() + otp2.getText().toString() +
                otp3.getText().toString() + otp4.getText().toString();

        // Add OTP validation logic as per your requirement.
        // If OTP is valid, proceed to the next Activity
        if (isValidOtp(otp)) {

            //animation code
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(rotateAnimation);
            //view.setVisibility(View.VISIBLE);
            //animation code

            Context context= this;
            APIreferenceclass api= new APIreferenceclass(otp, context, email, 1);
            Log.d("tg4","otp works");

            //Intent intent = new Intent(ContentOtp.this, ContentOtp.class);
            // Add any other data to send to the next activity
            // intent.putExtra("email", emailInput);
            // intent.putExtra("loginType", loginType);
            //startActivity(intent);
        } else {
            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();

            // Handle invalid OTP entry
            // This could be showing an error message, shaking animation, etc.
        }
    }

    String name;
    String token;
    String userid;
    String loginType;
    int clientid, vendorid, recceid, recceasmid;

    @Override
    public void onResponseReceived(String response){
        Log.d("tg4","otp response works" +response);
        clientid= 0;
        vendorid= 0;
        userid="";
        recceid= 0;
        recceasmid=0;

        try {
        JSONObject jsonObject = new JSONObject(response);
        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("data"));
        if(jsonObject.getBoolean("success")== true){
            name= jsonObject1.getString("name");
            token= jsonObject1.getString("token");
            userid= jsonObject1.getString("id");

            loginType= jsonObject1.getString("type");
            if(loginType.equals("client")){
                clientid= jsonObject1.getInt("id");
            }
            if(loginType.equals("vendor")){
                vendorid= jsonObject1.getInt("id");
            }

            if(loginType.equals("supervisor")){
                recceid= jsonObject1.getInt("id");
                boolean success2= FileHelper.writeUserType(this, "supervisor");
            }

            if(loginType.equals("asm")){
                recceasmid= jsonObject1.getInt("id");
            }

            Log.d("tg4", loginType);

            boolean success = FileHelper.writeLoginToken(this, token);
            boolean success1 = FileHelper.writeUserId(this, userid);



            if(loginType.equals("admin")){

                Log.d("tg5","1");

                loadingSpinner();

                Intent intent= new Intent(ContentOtp.this, AdminDashboardActivity.class);
                intent.putExtra("logintoken", token);



                startActivity(intent);

            }else if(loginType.equals("vendor")){

                Intent intent= new Intent(ContentOtp.this, VenderDashBoardActivity.class);
                intent.putExtra("logintoken", token);
                intent.putExtra("vendorid", vendorid);

                loadingSpinner();

                startActivity(intent);
            }else if(loginType.equals("client")){

                Intent intent= new Intent(ContentOtp.this, ClientDashBoardActivity.class);
                intent.putExtra("logintoken", token);
                intent.putExtra("clientid", clientid);

                loadingSpinner();
                startActivity(intent);
            }else if(loginType.equals("supervisor")){

                Intent intent= new Intent(ContentOtp.this, RecceDashboardActivity.class);
                Log.d("tag232", "reccedash");

                intent.putExtra("logintoken", token);
                intent.putExtra("recceid", recceid);

                loadingSpinner();
                startActivity(intent);
            }
            //todo add installation login
            else if(loginType.equals("asm")){

                Intent intent= new Intent(ContentOtp.this, RecceAsmDashboard.class);
                intent.putExtra("logintoken", token);
                intent.putExtra("recceasmid", recceasmid);

                loadingSpinner();
                startActivity(intent);
            }
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //stop loading spinner
                    loadingSpinner();

                    otp1.setText("");
                    otp2.setText("");
                    otp3.setText("");
                    otp4.setText("");
                    otp1.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(otp1, InputMethodManager.SHOW_IMPLICIT);


                    Animation anim= AnimationUtils.loadAnimation(context, R.anim.shake);
                    otp1.startAnimation(anim);
                    otp2.startAnimation(anim);
                    otp3.startAnimation(anim);
                    otp4.startAnimation(anim);



                }
            });

        }
    }catch(Exception e){
        Log.d("tg4", e.toString());
    }
}

    void loadingSpinner(){
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
    }

    private boolean isValidOtp(String otp) {
        return otp.length() == 4;
    }
}
