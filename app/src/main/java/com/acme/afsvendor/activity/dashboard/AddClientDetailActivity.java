package com.acme.afsvendor.activity.dashboard;

import android.os.Bundle;

import com.acme.afsvendor.utility.NetworkUtils;
import com.acme.afsvendor.viewmodel.APIreferenceclass;
import com.acme.afsvendor.viewmodel.ApiInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.acme.afsvendor.databinding.ActivityAddClientDetailBinding;

import com.acme.afsvendor.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AddClientDetailActivity extends AppCompatActivity implements ApiInterface {
    private ActivityAddClientDetailBinding binding;
    String response1;
    String logintoken;
    String clientid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddClientDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        logintoken= "";

        Log.d("whichclass", "AddClientDetailActivity");
        try {
                FileHelper fh= new FileHelper();
                logintoken= fh.readLoginToken(this);

            //api response from last thread
            response1 = getIntent().getStringExtra("response");
            Log.d("tg74", response1);

            //siteNumber= getIntent().getStringExtra("siteNumber");
        }catch(Exception e){
            e.printStackTrace();
        }
        implementUI(response1);
    }

    void implementUI(String apiresponse){
        JSONObject jsonobj= null;

        try {
        jsonobj= new JSONObject(apiresponse);
        clientid= jsonobj.getString("id");
        }catch(Exception e){
            Log.d("tg23", e.toString());
        }

        if(jsonobj!=null){

            Log.d("tg23", "sadasgdsgds"+apiresponse);
            try {
                //inserting data into UI
                binding.etFullName.setText(jsonobj.getString("name"));
                binding.etEmail.setText(jsonobj.getString("email"));
                binding.etCompanyName.setText(jsonobj.getString("company_name"));
                binding.etCompanyAddress.setText(jsonobj.getString("company_address"));
                binding.etGst.setText(jsonobj.getString("gst_no"));
                binding.etPhone.setText(jsonobj.getString("phone_number"));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

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

            //new setters

            ClientDetail clientDetail= new ClientDetail();

            String name=binding.etFullName.getText().toString();
            String email=binding.etEmail.getText().toString();
            String phone_number=binding.etPhone.getText().toString();
            String company_name=binding.etCompanyName.getText().toString();
            String company_address=binding.etCompanyAddress.getText().toString();
            String gst_no= binding.etGst.getText().toString();

            clientDetail.setName(name);
            clientDetail.setEmail(email);
            clientDetail.setPhoneNumber(phone_number);
            clientDetail.setCompanyName(company_name);
            clientDetail.setCompanyAddress(company_address);
            clientDetail.setGstNo(gst_no);

            try{
                JSONObject jsonobj= new JSONObject();
                jsonobj.put("name", clientDetail.getName()!= null? clientDetail.getName():"");
                jsonobj.put("email", clientDetail.getEmail()!=null? clientDetail.getEmail():"");
                jsonobj.put("phone", clientDetail.getPhoneNumber()!= null? clientDetail.getPhoneNumber():"");
                jsonobj.put("companyname", clientDetail.getComapnyName()!=null? clientDetail.getComapnyName():"");
                jsonobj.put("companyaddress", clientDetail.getCompanyAddress()!= null? clientDetail.getCompanyAddress():"");
                jsonobj.put("gst", clientDetail.getGstNo()!=null? clientDetail.getGstNo():"");



            }catch(JSONException e){
                Log.d("erroraddclientdetailactivity", e.toString());
            }



            //end of new setters


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

            APIreferenceclass api= new APIreferenceclass(jsonPayload, this, logintoken, clientid, 0, jsonPayload.toString());
        }
    }

    @Override
    public void onResponseReceived(String response) {
        Log.d("tg99", response);
        try {
            JSONObject jsonobj = new JSONObject(response);

            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    try{
                        if(jsonobj.getBoolean("success")== true){
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
            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    showFailureMessage();
                }});

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
        tvMsg.setText("Client Add Failed Please Check All The Fields");
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

    public void showSuccessMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_emailsent, null);

        TextView tvMsg = view.findViewById(R.id.tvMsg);
        TextView tvResubmit = view.findViewById(R.id.tvResubmit);
        tvResubmit.setVisibility(View.GONE);
        tvMsg.setText("Client Edited Successfully");

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
