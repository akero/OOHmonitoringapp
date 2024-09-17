package com.acme.oohvendor.activity.dashboard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.acme.oohvendor.R;
import com.acme.oohvendor.utility.RoundRectCornerImageView;
import com.acme.oohvendor.viewmodel.APIreferenceclass;
import com.acme.oohvendor.viewmodel.ApiInterface;
import com.acme.oohvendor.databinding.ActivityRecceHistoryDetailsBinding;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class RecceHistoryDetails extends AppCompatActivity implements ApiInterface {
    int id;
    String logintoken;
    String projectId;

    ActivityRecceHistoryDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_recce_history_details);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_recce_history_details);


        id= 0;
        logintoken= "";
        projectId= "";

        Log.d("whichclass", "reccehistorydetails");

        try{

            id= getIntent().getIntExtra("userid", 0);
            logintoken= getIntent().getStringExtra("logintoken");
            projectId= getIntent().getStringExtra("id");;

            Log.d("id", id+" projectid "+ projectId);
        }catch(Exception e){
            Log.d("asdsad", e.toString());
        }



        APIreferenceclass api= new APIreferenceclass(logintoken, this, id, projectId, 0);

    }

    void implementUI(String response){

        try {
            JSONObject jsonobj1 = new JSONObject(response);
            JSONObject jsonobj = new JSONObject(jsonobj1.getString("data"));

            binding.tvAddSiteDetail.setText(jsonobj.getString("retail_name"));
            binding.etSiteNo.setText(jsonobj.getString("asm_name"));
            binding.tvStartDate.setText(jsonobj.getString("state"));
            binding.tvEndDate.setText(jsonobj.getString("district"));
            binding.tvLocation.setText(jsonobj.getString("city"));
            binding.tvLongitude.setText(jsonobj.getString("length"));
            binding.tvWidth.setText(jsonobj.getString("width"));
            binding.tvHeight.setText(jsonobj.getString("date"));
            binding.tvTotalArea.setText(jsonobj.getString("owner_name"));
            binding.tvMediaType.setText(jsonobj.getString("email"));
            binding.tvIllumination.setText(jsonobj.getString("mobile"));
            binding.tvLatitude1.setText(jsonobj.getString("lat"));
            binding.tvLongitude1.setText(jsonobj.getString("long"));
            binding.tvLatitude.setText(jsonobj.getString("location"));

            RoundRectCornerImageView tvImage = findViewById(R.id.ivCampaignImage);
            if (jsonobj.getString("image1") != null) {
                Log.d("tg2", "image code executing");
                new LoadImageAsyncTask(tvImage).execute("https://acme.warburttons.com/" + jsonobj.getString("image1"));
            }

            RoundRectCornerImageView tvImage1 = findViewById(R.id.ivCampaignImage1);
            if (jsonobj.getString("owner_signature") != null) {
                Log.d("tg2", "image code executing");
                new LoadImageAsyncTask(tvImage1).execute("https://acme.warburttons.com/" + jsonobj.getString("owner_signature"));
            }



        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private class LoadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private RoundRectCornerImageView imageView;

        LoadImageAsyncTask(RoundRectCornerImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String imageUrl = params[0];
            try {
                URL url = new URL(imageUrl);
                return BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                // Handle error case
            }
        }
    }
    @Override
    public void onResponseReceived(String response){

        Log.d("response", response);
        try{

            JSONObject jsonobj1= new JSONObject(response);
            if(jsonobj1.getString("message").equals("Data fetched successfully!")){


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        implementUI(response);
                    }
                });
            }


        }catch (Exception e){
            e.printStackTrace();
        }



    }


}