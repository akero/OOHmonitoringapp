package com.acme.afsvendor.activity.dashboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.acme.afsvendor.R;
import com.acme.afsvendor.databinding.ActivityRecceInstallationDashboardBinding;
import com.acme.afsvendor.utility.RoundRectCornerImageView;
import com.acme.afsvendor.viewmodel.APIreferenceclass;
import com.acme.afsvendor.viewmodel.ApiInterface;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class RecceInstallationDashboard extends AppCompatActivity implements ApiInterface {
    int id;
    String logintoken;
    String projectId;
    private ActivityRecceInstallationDashboardBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_recce_history_details);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_recce_installation_dashboard);


        id = 0;
        logintoken = "";
        projectId = "";



        try {

            id = getIntent().getIntExtra("userid", 0);
            logintoken = getIntent().getStringExtra("logintoken");
            projectId = getIntent().getStringExtra("id");
            ;

            Log.d("id", id + " projectid " + projectId);
        } catch (Exception e) {
            Log.d("asdsad", e.toString());
        }

        //Start install button
        binding.btnStartInstallation.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {

                                                                Intent intent = new Intent(RecceInstallationDashboard.this, RecceInstallationLastPage.class);

                                                                intent.putExtra("userid", id);
                                                                intent.putExtra("logintoken", logintoken);
                                                                intent.putExtra("projectid", projectId);
                                                                startActivity(intent);

                                                            }
                                                        });


        //Report issue button
        binding.btnReportIssue.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {

                                                            }
                                                        });

        //Start install button
        binding.tvTaskCompleted1.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {

                                                                Intent intent = new Intent(RecceInstallationDashboard.this, RecceInstallationLastPage.class);

                                                                intent.putExtra("userid", id);
                                                                intent.putExtra("logintoken", logintoken);
                                                                intent.putExtra("projectid", projectId);
                                                                startActivity(intent);

                                                            }
                                                        });


        //Report issue button
        binding.tvTaskCompleted.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {

                                                            }
                                                        });


                Log.d("whichclass", "RecceInstallationDashboard");
        APIreferenceclass api = new APIreferenceclass(logintoken, this, id, projectId, 0);

    }

    void implementUI(String response) {

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
            binding.tvLatitude1.setText(jsonobj.getString("lat").substring(0,8));
            binding.tvLongitude1.setText(jsonobj.getString("long").substring(0,8));
            binding.tvLatitude.setText(jsonobj.getString("location"));

            RoundRectCornerImageView tvImage = findViewById(R.id.ivCampaignImage);
            if (jsonobj.getString("image1") != null) {
                Log.d("tg2", "image code executing");
                new RecceInstallationDashboard.LoadImageAsyncTask(tvImage).execute("https://acme.warburttons.com/" + jsonobj.getString("image1"));
            }

            RoundRectCornerImageView tvImage1 = findViewById(R.id.ivCampaignImage1);
            if (jsonobj.getString("owner_signature") != null) {
                Log.d("tg2", "image code executing");
                new RecceInstallationDashboard.LoadImageAsyncTask(tvImage1).execute("https://acme.warburttons.com/" + jsonobj.getString("owner_signature"));
            }


        } catch (Exception e) {
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
    public void onResponseReceived(String response) {

        Log.d("response", response);
        try {

            JSONObject jsonobj1 = new JSONObject(response);
            if (jsonobj1.getString("message").equals("Data fetched successfully!")) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        implementUI(response);
                    }
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}


