package com.acme.afsvendor.activity.dashboard;

import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.acme.afsvendor.R;
import com.acme.afsvendor.databinding.ActivityProfileBinding;
import com.acme.afsvendor.viewmodel.APIreferenceclass;
import com.acme.afsvendor.viewmodel.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;


public class ProfileActivity extends AppCompatActivity implements ApiInterface {

    String userid, logintoken;
    ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        userid= "";
        logintoken= "";
        try{
            userid= FileHelper.readUserId(this);
            logintoken= FileHelper.readLoginToken(this);
        }catch (Exception e){
            Log.d("tag322", e.toString());
        }

        APIreferenceclass api= new APIreferenceclass(logintoken, userid, this, 1);


    }

    @Override
    public void onResponseReceived(String response){

        Log.d("response", response);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject dataobj = new JSONObject(response);
                    if(dataobj.getInt("status")== 200) {
                        JSONObject jsonobj = dataobj.getJSONObject("data");

                        binding.tvHeigh.setText(jsonobj.optString("name"));
                        binding.tvWidt.setText(jsonobj.optString("email"));
                        binding.tvHeigh1.setText(jsonobj.optString("phone_number"));
                        binding.tvWidt1.setText(jsonobj.optString("gst_no"));
                        binding.tvHeigh2.setText(jsonobj.optString("area"));
                        binding.tvWidt2.setText(jsonobj.optString("zone"));
                    }

                }catch (Exception e){
                    Log.d("tag23321", e.toString());
                }

            }
        });
    }

    public void onSaveClick(View v){
        JSONObject jsonobj= new JSONObject();
        try {
            jsonobj.putOpt("name", binding.tvHeigh.getText());
            jsonobj.putOpt("email", binding.tvWidt.getText());
            jsonobj.putOpt("phone_number", binding.tvHeigh1.getText());
            jsonobj.putOpt("gst_no", binding.tvWidt1.getText());
            jsonobj.putOpt("area", binding.tvHeigh2.getText());
            jsonobj.putOpt("zone", binding.tvWidt2.getText());



        }catch (Exception e){
            Log.d("tag22", e.toString());
        }


        }
}