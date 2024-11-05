package com.acme.oohvendor.activity.login;

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

import com.acme.oohvendor.R;
import com.acme.oohvendor.activity.dashboard.AdminDashboardActivity;
import com.acme.oohvendor.activity.dashboard.AsmDashboardActivity;
import com.acme.oohvendor.activity.dashboard.FileHelper;
import com.acme.oohvendor.activity.dashboard.RecceDashboardActivity;
import com.acme.oohvendor.activity.dashboard.ViewCampaignSites;
import com.acme.oohvendor.activity.dashboard.ViewRhmSites;
import com.acme.oohvendor.activity.dashboard.ViewVendorSites;
import com.acme.oohvendor.activity.dashboard.ZoDashboardActivity;
import com.acme.oohvendor.viewmodel.APIreferenceclass;
import com.acme.oohvendor.viewmodel.ApiInterface;

import org.json.JSONObject;

public class ContentOtp extends AppCompatActivity implements ApiInterface {

    private EditText otp1, otp2, otp3, otp4, otp5;
    String email="";
    ProgressBar progressBar;
    Animation rotateAnimation;
    Context context;
    String area; //this is changed to name


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

        area= "";

        // Setup auto-advance
        setupAutoAdvance(otp1, otp2);
        setupAutoAdvance(otp2, otp3);
        setupAutoAdvance(otp3, otp4);
        //setupAutoAdvance(otp4, otp5);
        setupAutoAdvance(otp4, null);

        email= getIntent().getStringExtra("email");
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
    String realarea;


    @Override
    public void onResponseReceived(String response){
        Log.d("tg4","otp response works" +response);
        clientid= 0;
        vendorid= 0;
        userid="";
        recceid= 0;
        recceasmid=0;
        name= "";
        realarea= "";

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
                area= jsonObject1.getString("name");
                Log.d("tag58area",area);
            }
            if(loginType.equals("rhm")){
                vendorid= jsonObject1.getInt("id");
                area= jsonObject1.getString("zone");
                Log.d("tag58area",area);
            }




            if(loginType.equals("supervisor")){
                recceid= jsonObject1.getInt("id");
                boolean success2= FileHelper.writeUserType(this, "supervisor");
            }

            if(loginType.equals("state_lead")){
                recceasmid= jsonObject1.getInt("id");
                area= jsonObject1.getString("name");
                realarea= jsonObject1.getString("state");
            }

            if(loginType.equals("zo")){
                recceasmid= jsonObject1.getInt("id");
                area= jsonObject1.getString("name");
                realarea= jsonObject1.getString("zone");
            }

            Log.d("tg4", loginType);

            boolean success = FileHelper.writeLoginToken(this, token);
            boolean success1 = FileHelper.writeUserId(this, userid);



            if(loginType.equals("admin")){

                Log.d("tg5","1");

                loadingSpinner();

                Intent intent= new Intent(ContentOtp.this, AdminDashboardActivity.class);
                intent.putExtra("logintoken", token);
                intent.putExtra("name", name);



                startActivity(intent);

            }else if(loginType.equals("vendor")){

                Intent intent= new Intent(ContentOtp.this, ViewVendorSites.class);
                intent.putExtra("logintoken", token);
                Log.d("logintag", "correctvendorlogin");
                intent.putExtra("vendorid", vendorid);
                intent.putExtra("vendorclientorcampaign", 2);
                intent.putExtra("area", area);
                intent.putExtra("name", name);
                loadingSpinner();

                startActivity(intent);
            }else if(loginType.equals("rhm")){

                Intent intent= new Intent(ContentOtp.this, ViewRhmSites.class);
                intent.putExtra("logintoken", token);
                Log.d("logintag", "correctvendorlogin");
                intent.putExtra("vendorid", vendorid);
                intent.putExtra("vendorclientorcampaign", 2);
                intent.putExtra("area", area);
                intent.putExtra("name", name);
                loadingSpinner();

                startActivity(intent);
            }else if(loginType.equals("client")){

                Intent intent= new Intent(ContentOtp.this, ViewCampaignSites.class);
                intent.putExtra("logintoken", token);
                intent.putExtra("clientid", Integer.toString(clientid));
                intent.putExtra("vendorclientorcampaign", 1);
                intent.putExtra("camefrom", "contentotp");
                intent.putExtra("name", name);

                loadingSpinner();
                startActivity(intent);
            }else if(loginType.equals("supervisor")){

                Intent intent= new Intent(ContentOtp.this, RecceDashboardActivity.class);
                Log.d("tag232", "reccedash");

                intent.putExtra("logintoken", token);
                intent.putExtra("recceid", recceid);
                intent.putExtra("name", name);

                loadingSpinner();
                startActivity(intent);
            }
            //todo add installation login
            else if(loginType.equals("state_lead")){

                Intent intent= new Intent(ContentOtp.this, AsmDashboardActivity.class);
                intent.putExtra("logintoken", token);
                intent.putExtra("recceasmid", recceasmid);
                intent.putExtra("area", area);
                intent.putExtra("name", name);
                intent.putExtra("realarea", realarea);

                loadingSpinner();
                startActivity(intent);
            }else if(loginType.equals("zo")){

                Intent intent= new Intent(ContentOtp.this, ZoDashboardActivity.class);
                intent.putExtra("logintoken", token);
                intent.putExtra("recceasmid", recceasmid);
                intent.putExtra("area", area);
                intent.putExtra("realarea", realarea);
                intent.putExtra("name", name);

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

                    try {
                        if (jsonObject.getString("message").equals("Unauthorised.")) {
                           // Toast.makeText(context, "Email is unauthorised.", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Log.d("dsad", e.toString());
                    }
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
            }
        });
        //animation code
    }

    private boolean isValidOtp(String otp) {
        return otp.length() == 4;
    }
}
