package com.acme.afsvendor.activity.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.acme.afsvendor.R;
import com.acme.afsvendor.databinding.ActivityAdminViewClientDetailsBinding;
import com.acme.afsvendor.databinding.ActivityViewSiteDetailBinding;
import com.acme.afsvendor.utility.RoundRectCornerImageView;
import com.acme.afsvendor.viewmodel.ApiInterface;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class AdminViewClientDetails extends AppCompatActivity implements ApiInterface {

    private String id = "";
    private int position = 0;
    //String siteNumber= "";
    String logintoken="";
    String apiresponse;
    private ActivityAdminViewClientDetailsBinding binding;

    //TODO populate all fields. pass api call data from prev activity
    //have to pass logintoken and siteid
    String jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_view_client_details);
        Log.d("tag999", "3");
        Log.d("tag41", "1");
        Log.d("whichclass", "AdminViewClientDetails");
        if (getIntent().getExtras() != null) {
            Log.d("tag41", "2");
            logintoken = getIntent().getExtras().getString("logintoken", "");
            Log.d("tag41", "3");

            id= getIntent().getExtras().getString("id", "");
            Log.d("tag41", "4");

            apiresponse= getIntent().getExtras().getString("jsonArray");
            Log.d("tag41", "5");

            try {
                jsonArray = getIntent().getExtras().getString("jsonArray");
            }catch(Exception e){
                Log.d("tag60", e.toString());
            }
            Log.d("tag41", "3");

            Log.d("tag90", apiresponse);
            Log.d("tag41", "3");
        }

        // Assigning values and listeners to Buttons
        Button btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle next button click
                //try {
                    Log.d("tag90", response1);

                    Intent intent = new Intent(AdminViewClientDetails.this, AddClientDetailActivity.class);
                    intent.putExtra("loginToken", logintoken);
                    intent.putExtra("response", response1);
                    //intent.putExtra("campaignId", campaignId);
                    //Log.d("tag000", logintoken+ "| "+ campaignId+"| "+  siteNumber+ "| "+ jsonobj.toString());
                    //Log.d("tag000", siteNumber);

                    //intent.putExtra("siteNumber", siteNumber);
                    // intent.putExtra("siteDetail", jsonobj.toString());
                    startActivity(intent);

               // }catch(Exception e){
                 //   Log.d("tag222", e.toString());
                   // e.printStackTrace();

            //    }
            }
        });

        Button btnDownload = findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDownloadClick(v);
                // Handle download button click
            }
        });

        Button btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle close button click
                finish();
            }
        });


        Log.d("tag41", "4");
        response1= apiresponse;

        Log.d("tag90", response1);


        implementUI(apiresponse);
        //apicall(logintoken, id);
        Log.d("tag41", "5");
    }

    private void implementUI(String response) {
        Log.d("tag60", response);
        //here
        try {
            JSONObject jsonResponse = new JSONObject(response);
            Log.d("tag95", "1");

                    if(jsonResponse != null) {
                        Log.d("tag95", "1");

                        ClientDetail siteDetail = new ClientDetail();
                        siteDetail.setId(jsonResponse.optInt("id"));
                        siteDetail.setName(jsonResponse.optString("name"));
                        siteDetail.setEmail(jsonResponse.optString("email"));
                        siteDetail.setPhoneNumber(jsonResponse.optString("phone_number"));
                        siteDetail.setCompanyName(jsonResponse.optString("company_name"));
                        siteDetail.setCompanyAddress(jsonResponse.optString("company_address"));
                        siteDetail.setGstNo(jsonResponse.optString("gst_no"));
                        siteDetail.setCreatedAt(jsonResponse.optString("created_at"));
                        siteDetail.setUpdatedAt(jsonResponse.optString("updated_at"));
                        Log.d("tag95", "1");

                        try {
                            String imageUrl = jsonResponse.optString("logo");
                            imageUrl= "https://acme.warburttons.com/"+ imageUrl;
                            Log.d("tag41", "imageurl is "+ imageUrl);
                            if(imageUrl != "null" && !imageUrl.isEmpty()) {
                                URL url = new URL(imageUrl);
                                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                siteDetail.setLogo(bitmap);
                            }
                        } catch (Exception e) {
                            Log.d("tag41", "error in implementui" +e.toString());
                            // Handle error
                        }
                        Log.d("tag95", "1");

                        // Update UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                TextView tvSiteId = findViewById(R.id.etTotalSites);
                                //TODO after person implements site name then change this
                                tvSiteId.setText(String.valueOf(siteDetail.getId()));

                                TextView tvSiteId1 = findViewById(R.id.etSiteNo);
                                //TODO after person implements site name then change this
                                tvSiteId1.setText(String.valueOf(siteDetail.getPhoneNumber()));

                                TextView tvLocation = findViewById(R.id.tvLocation);
                                tvLocation.setText(siteDetail.getComapnyName());

                                TextView tvSiteName = findViewById(R.id.tvAddSiteDetail);
                                tvSiteName.setText(siteDetail.getName());

                                TextView tvLastInspection = findViewById(R.id.tvStartDate);
                                tvLastInspection.setText(siteDetail.getCreatedAt());

                                TextView tvLatitude = findViewById(R.id.tvLatitude);
                                tvLatitude.setText(siteDetail.getCreatedAt());

                                TextView tvLongitude = findViewById(R.id.tvLongitude);
                                tvLongitude.setText(siteDetail.getUpdatedAt());

                                TextView tvMediaType = findViewById(R.id.tvMediaType);
                                tvMediaType.setText(siteDetail.getGstNo());

                                TextView tvIllumination = findViewById(R.id.tvIllumination);
                               // tvIllumination.setText(siteDetail.getIllumination());

                                TextView tvStartDate = findViewById(R.id.tvStartDate);
                                tvStartDate.setText(siteDetail.getEmail());

                                // Set the site number
                                TextView tvSiteNo = findViewById(R.id.etSiteNo);
                                //tvSiteNo.setText(String.valueOf(siteDetail.getSiteNo())); // assuming getter method exists

                                // Set the width
                                TextView tvWidth = findViewById(R.id.tvWidth);
                               // tvWidth.setText(siteDetail.getWidth()); // assuming getter method exists

                                // Set the height
                                TextView tvHeight = findViewById(R.id.tvHeight);
                                //tvHeight.setText(siteDetail.getCompanyAddress()); // assuming getter method exists

                                // Set the total area
                                TextView tvTotalArea = findViewById(R.id.tvTotalArea);
                                tvTotalArea.setText(siteDetail.getCompanyAddress()); // assuming getter method exists

                                RoundRectCornerImageView tvImage = findViewById(R.id.ivCampaignImage);
                                if(siteDetail.getLogo()!=null) {
                                    tvImage.setImageBitmap(siteDetail.getLogo());
                                }
                            }
                        });
                    }
        } catch (Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(ViewSiteDetailActivity.this, "Error retrieving or parsing data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    String response1="";

    @Override
    public void onResponseReceived(String response){

        response1= response;
        Log.d("tag90", response1);


        implementUI(response);
    }

    public void btnCloseClick(View view) {
        finish();
    }

    public void btnNextClick(View view) {
        //TODO ask what this does
        // finish();
    }

    public void onNotificationClick(View view){
        //TODO ask dev what this does
    }

    public void onDownloadClick(View view) {
        //handle click
        Log.d("tag46", response1);

        File txtFile;
        String a = response1;
        FileWriter writer = null; // Initialize writer outside try-catch so it can be accessed in the finally block
        try {
            txtFile = new File(getExternalFilesDir(null), "Site Details.txt");
            Log.d("tag46", "File path: " + txtFile.getAbsolutePath());
            writer = new FileWriter(txtFile);
            writer.write(a); // 'a' is your JSON string
            String fullPath = txtFile.getAbsolutePath();
            String shortenedPath = fullPath.replace("/storage/emulated/0/", "Internal Storage > ... > ");

            Toast.makeText(this, "File downloaded to Internal Storage> Android> data> com.acme.afsvendor", Toast.LENGTH_LONG).show();
            writer.flush(); // Good practice to flush before closing, though close() does this too
        } catch (Exception e) {
            Log.d("tag46", e.toString());
        } finally {
            try {
                if (writer != null) {
                    writer.close(); // Ensure the writer is closed, this will also flush the buffer
                }
            } catch (IOException e) {
                Toast.makeText(this, "Download failed check your internet", Toast.LENGTH_LONG).show();
                Log.d("tag46", e.toString());
            }
        }
    }

    public void oldCampaignClick(View view) {
        binding.tvOldCampaign.setBackgroundResource(R.drawable.primaryround);
        binding.tvLiveCampaign.setBackgroundResource(R.color.coloryellow);
    }

    public void liveCampaignClick(View view) {
        binding.tvLiveCampaign.setBackgroundResource(R.drawable.primaryround);
        binding.tvOldCampaign.setBackgroundResource(R.color.coloryellow);
    }

    //TODO delete. download code

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
