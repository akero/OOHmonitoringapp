package com.acme.afsvendor.activity.dashboard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.acme.afsvendor.R;
import com.acme.afsvendor.databinding.ActivityAddClientBinding;
import com.acme.afsvendor.databinding.ActivityAddVenderBinding;
import com.acme.afsvendor.utility.NetworkUtils;
import com.acme.afsvendor.viewmodel.APIreferenceclass;
import com.acme.afsvendor.viewmodel.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

public class AddVenderActivity extends AppCompatActivity implements ApiInterface {

    private ActivityAddVenderBinding binding;
    String siteNumber;
    int edit;
    String response1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_vender);
        logintoken= getIntent().getStringExtra("logintoken");
        response1= "";
        edit= 0;

        try{
            if(getIntent().getStringExtra("camefrom").equals("AdminViewVendorDetails")){
                edit= 1;
                response1= getIntent().getStringExtra("response");
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        if(edit== 1){
            fillDetails(response1);
        }

        Log.d("whichclass", "AddVendorActivity");
        //siteNumber= getIntent().getStringExtra("siteNumber");
    }

    void fillDetails(String response){

        try {
            JSONObject jsonobj= new JSONObject(response);
            Log.d("response", response);

            binding.etFullName.setText(jsonobj.optString("name"));
            binding.etLocation.setText(jsonobj.optString("company_address"));
            binding.etEmail.setText(jsonobj.optString("email"));
            binding.etPhone.setText(jsonobj.optString("phone_number"));
            binding.etCompanyName.setText(jsonobj.optString("company_name"));
            binding.etCompanyAddress.setText(jsonobj.optString("company_address"));
            binding.etGst.setText(jsonobj.optString("gst_no"));
            binding.etDescription.setText(jsonobj.optString(""));
            vendorid= Integer.toString(jsonobj.optInt("id"));





        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    String name="";
    String email="";
    String phone_number="";
    String company_name="";
    String company_address="";
    String gst_no="";
    //Byte[] logo;
    String logintoken;

    public void btnSaveClick(View view) {
        if (    binding.etFullName.getText().toString().isEmpty() ||
                binding.etEmail.getText().toString().isEmpty() ||
                binding.etCompanyName.getText().toString().isEmpty() ||
                binding.etCompanyAddress.getText().toString().isEmpty() ||
                binding.etGst.getText().toString().isEmpty() ||
                binding.etPhone.getText().toString().isEmpty()) {

            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_LONG).show();
        } else if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "Check your Internet Connection and Try Again", Toast.LENGTH_LONG).show();
        } else {

            String name=binding.etFullName.getText().toString();
            String email=binding.etEmail.getText().toString();
            String phone_number=binding.etPhone.getText().toString();
            String company_name=binding.etCompanyName.getText().toString();
            String company_address=binding.etCompanyAddress.getText().toString();
            String gst_no= binding.etGst.getText().toString();

            JSONObject jsonPayload= new JSONObject();
            try{
                jsonPayload.put("name", name);
                jsonPayload.put("email", email);
                jsonPayload.put("phone_number", phone_number);
                jsonPayload.put("company_name", company_name);
                jsonPayload.put("company_address", company_address);
                jsonPayload.put("gst_no", gst_no);

            }catch(Exception e){
                Log.d("tg6", e.toString());
            }

            if(edit==0) {
                APIreferenceclass api = new APIreferenceclass(jsonPayload, this, logintoken, 0);
            }else{
                APIreferenceclass api = new APIreferenceclass(jsonPayload, this, logintoken, vendorid);

            }
        }
    }

    String vendorid;

    @Override
    public void onResponseReceived(String response) {
        Log.d("tg9", response);
        try {
            JSONObject jsonobj = new JSONObject(response);

            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    try{
                        if(jsonobj.getBoolean("success")== true){
                            binding.etFullName.setText("");
                            binding.etEmail.setText("");
                            binding.etCompanyName.setText("");
                            binding.etCompanyAddress.setText("");
                            binding.etGst.setText("");
                            binding.etPhone.setText("");
                            binding.etLocation.setText("");
                            showSuccessMessage();}
                        else{
                            showFailureMessage();
                        }
                    }catch (Exception e){
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                showFailureMessage();
                            }});
                        Log.d("tagerw1", e.toString());
                    }
                }

            });}catch(Exception e){


            Log.d("tag123", e.toString());
        }
    }

    public void showFailureMessage() {

        Log.d("tagerw", "in onfailure");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_emailsent, null);
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        TextView tvResubmit = view.findViewById(R.id.tvResubmit);
        tvResubmit.setVisibility(View.INVISIBLE);
        tvMsg.setText("Vendor Add Failed Please Check All The Fields");
        Button btnClose = view.findViewById(R.id.btnClose);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /*@Override
    public void onResponseReceived(String response){

        Log.d("tg6", response);
        try{
            JSONObject jsonobj= new JSONObject(response);
            if(jsonobj.get("success").equals("false")) {
                Toast.makeText(this, "Please check the fields", Toast.LENGTH_SHORT);

            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.etFullName.setText("");
                        binding.etEmail.setText("");
                        binding.etCompanyName.setText("");
                        binding.etCompanyAddress.setText("");
                        binding.etGst.setText("");
                        binding.etPhone.setText("");
                        showSuccessMessage();
                        //Toast.makeText(AddClientActivity.this, "Client successfully created" , Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }catch (Exception e){
            Log.d("tg9", e.toString());
        }
    }*/

    public void showSuccessMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_emailsent, null);

        TextView tvMsg = view.findViewById(R.id.tvMsg);
        TextView tvResubmit = view.findViewById(R.id.tvResubmit);
        tvResubmit.setVisibility(View.GONE);
        if(edit== 0) {
            tvMsg.setText("Vendor Added Successfully");
        }else{
            tvMsg.setText("Vendor updated Successfully");
        }
        Button btnClose = view.findViewById(R.id.btnClose);
        builder.setView(view);
        final AlertDialog dialog = builder.create();

        if (dialog != null) {
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        if (dialog != null) {
            dialog.show();
        }
    }

    public void btnCloseClick(View view) {
        finish();
    }

}
