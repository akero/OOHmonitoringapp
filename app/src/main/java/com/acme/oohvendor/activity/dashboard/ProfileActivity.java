package com.acme.oohvendor.activity.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.acme.oohvendor.R;
import com.acme.oohvendor.activity.login.OTP;
import com.acme.oohvendor.databinding.ActivityProfileBinding;
import com.acme.oohvendor.viewmodel.APIreferenceclass;
import com.acme.oohvendor.viewmodel.ApiInterface;

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

    JSONObject jsonobj;

    @Override
    public void onResponseReceived(String response){

        Log.d("response", response);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject dataobj = new JSONObject(response);
                    if(dataobj.getInt("status")== 200) {
                        jsonobj = dataobj.getJSONObject("data");

                        binding.tvHeigh.setText(jsonobj.optString("name"));
                        binding.tvWidt.setText(jsonobj.optString("email"));
                        binding.tvHeigh1.setText(jsonobj.optString("phone_number"));
                        binding.tvWidt1.setText(jsonobj.optString("gst_no"));
                        binding.tvHeigh2.setText(jsonobj.optString("area"));
                        binding.tvWidt2.setText(jsonobj.optString("zone"));

                        if(jsonobj.has("employee_id")){
                            Log.d("xyz", "yes");

                            binding.tvEmployeeid.setVisibility(View.VISIBLE);
                            binding.tvemployeeid1.setVisibility(View.VISIBLE);
                            binding.tvemployeeid1.setText(jsonobj.optString("employeeid"));
                        }

                    }

                }catch (Exception e){
                    Log.d("tag23321", e.toString());
                }

            }
        });
    }

    public void onSaveClick(View v){
        //JSONObject jsonobj= new JSONObject();
        try {

            String type= jsonobj.getString("type");
            int typeno= 0;

            Log.d("before", jsonobj.toString());
            jsonobj.putOpt("name", binding.tvHeigh.getText());
            jsonobj.putOpt("email", binding.tvWidt.getText());
            jsonobj.putOpt("phone_number", binding.tvHeigh1.getText());
            jsonobj.putOpt("gst_no", binding.tvWidt1.getText());
            jsonobj.putOpt("area", binding.tvHeigh2.getText());
            jsonobj.putOpt("zone", binding.tvWidt2.getText());
            jsonobj.remove("created_at");
            jsonobj.remove("updated_at");

            switch(type){
                case "admin":
                    typeno= 7;
                    break;
                case "client":
                    typeno= 0;
                    break;
                case "vendor":
                    typeno= 2;
                    break;
                case "asm":
                    typeno= 4;
                    break;
                case "zo":
                    typeno= 6;
                    break;



            }

            jsonobj.putOpt("type", typeno);

            Log.d("before1", jsonobj.toString());

            APIreferenceclass api= new APIreferenceclass(logintoken, userid, this, 2, jsonobj);


        }catch (Exception e){
            Log.d("tag22", e.toString());
        }


        }

    public void back(View v){
        onBackPressed();
    }

    public void logout(View v) {

        try {
            FileHelper fh = new FileHelper();
            fh.writeUserType(this, "");

            Intent intent= new Intent(ProfileActivity.this, OTP.class);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}