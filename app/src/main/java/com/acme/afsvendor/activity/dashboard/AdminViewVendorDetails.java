package com.acme.afsvendor.activity.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.acme.afsvendor.R;
import com.acme.afsvendor.databinding.ActivityAdminViewVendorDetailsBinding;
import com.acme.afsvendor.databinding.ActivityViewSiteDetailBinding;
import com.acme.afsvendor.utility.RoundRectCornerImageView;
import com.acme.afsvendor.viewmodel.APIreferenceclass;
import com.acme.afsvendor.viewmodel.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
//using clientlistactivity for getters

    public class AdminViewVendorDetails extends AppCompatActivity implements ApiInterface{


    private String id = "";
        private int position = 0;
        //String siteNumber= "";
        String logintoken="";
        String apiresponse;
        private ActivityAdminViewVendorDetailsBinding binding;

        //TODO populate all fields. pass api call data from prev activity
        //have to pass logintoken and siteid
        String jsonArray;

        String campaignitem;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_view_vendor_details);
            Log.d("tag41", "1");
            Log.d("whichclass", "AdminViewVendorDetails");
            campaignitem="";
            if (getIntent().getExtras() != null) {
                Log.d("tag41", "2");
                logintoken = getIntent().getExtras().getString("logintoken", "");
                Log.d("tag41", "3");

                id= getIntent().getExtras().getString("id", "");
                Log.d("tag41", "4");


                apiresponse= getIntent().getExtras().getString("jsonArray", "");
                Log.d("tag41", "5");

                campaignitem= getIntent().getExtras().getString("campaignItem");
                Log.d("tag41campaignitem", campaignitem);

                try {
                    jsonArray = getIntent().getExtras().getString("jsonArray");
                }catch(Exception e){
                    Log.d("tag60", e.toString());
                }
                Log.d("tag41", "3");

                //Log.d("tag90", apiresponse);
                Log.d("tag41", "3");
            }

            try{
                JSONObject jsonobj= new JSONObject(campaignitem);
                id= Integer.toString(jsonobj.optInt("id"));
            }catch(Exception e){
                Log.d("tag333", e.toString());
            }
            Log.d("tag41", "4");
            //implementUI(apiresponse);
            apicall(logintoken, id);
            Log.d("tag41", "5");
        }

        String response2;

        private void implementUI(String response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
            Log.d("tag610", response);
            response2= "";
            response2= response;

            //here
            try {

                JSONObject jsonobj12= new JSONObject(response);

                JSONObject jsonResponse = new JSONObject(jsonobj12.getString("data"));
                Log.d("tag915", jsonResponse.toString());

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
                    //Log.d("tag95", "1");

                    try {
                        String imageUrl = jsonResponse.optString("logo");
                        imageUrl= "https://acme.warburttons.com/"+ imageUrl;
                        //Log.d("tag41", "imageurl is "+ imageUrl);
                        if(imageUrl != "null" && !imageUrl.isEmpty()) {
                            URL url = new URL(imageUrl);
                            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            siteDetail.setLogo(bitmap);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Handle error
                    }
                    //Log.d("tag95", "1");

                    // Update UI



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





            } catch (Exception e) {
e.printStackTrace();
                        Log.d("tag3232", e.toString());

            }
                }});
            // Assigning values and listeners to Buttons
            Button btnNext = findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle next button click
                    Intent intent= new Intent(AdminViewVendorDetails.this, AddVenderActivity.class);
                    intent.putExtra("logintoken", logintoken);
                    intent.putExtra("camefrom", "AdminViewVendorDetails");
                    intent.putExtra("response", response2);
                    startActivity(intent);
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
        }

        void apicall(String logintoken, String id){

            Log.d("tag41", "6");
            Context context= this;
            String padding="";
            APIreferenceclass api= new APIreferenceclass(logintoken, context, id, 0, true);
            Log.d("tag41", "7");
        }
        public static String[] extractDataStrings(String apiResponse) {
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(apiResponse, JsonObject.class);
            JsonArray dataArray = jsonResponse.getAsJsonArray("data");

            String[] dataStrings = new String[dataArray.size()];
            for (int i = 0; i < dataArray.size(); i++) {
                dataStrings[i] = dataArray.get(i).toString();
            }

            return dataStrings;
        }

        String response1="";

        @Override
        public void onResponseReceived(String response){

            response1= response;
            implementUI(response);
            Log.d("tag41", response);
        }

        public void btnCloseClick(View view) {
            finish();
        }

        public void btnNextClick(View view) {
            //edit


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

        void writeToFile(String response, Context context){
            String name="logintoken";
            String content= response;
            FileOutputStream fostream= null;

            try{
                fostream= context.openFileOutput(name,Context.MODE_PRIVATE);
                fostream.write(response.getBytes());
                fostream.close();

            }catch(Exception e){
                Log.d("tag24", "error-" +e.toString());
            }
            finally{
                try{

                    if(fostream!=null){
                        fostream.close();
                    }
                }catch(Exception e){
                    Log.d("tag25","Closing outputstream failed");
                }
            }
        }

        public String formatJSONString(String unformattedJson) {
            try {
                JSONObject jsonObject = new JSONObject(unformattedJson);
                return jsonObject.toString(4); // `4` is the number of spaces to use for indentation
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void writeToFile(String data, String fileName) {
            File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "YourAppName");
            // Create the folder if it doesn't exist
            if (!directory.exists()) {
                directory.mkdirs();
            }
            // Create the file
            File file = new File(directory, fileName);
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(data);
                Toast.makeText(this, "Data saved at " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error saving data to file", Toast.LENGTH_SHORT).show();
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

        /*   private boolean checkPermission() {
               int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
               int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
               return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
           }

           private void requestPermission() {

               Log.d("tag45","5");
               View v= null;
               ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
           }
       */
   /* private long downloadReference;
    private BroadcastReceiver onDownloadComplete;

    private void startDownload(String fileURL, String fileName) {
        try {
            if (checkPermission()) {
                Uri downloadUri = Uri.parse(fileURL);
                String destination = Environment.DIRECTORY_DOWNLOADS;

                // Set up the request
                DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                request.setTitle(fileName);
                request.setDescription("Downloading...");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(destination, fileName);

                // Enqueue the download
                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                downloadReference = downloadManager.enqueue(request);

                Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show();
            } else {
                requestPermission();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Download failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
*/
        @Override
        protected void onDestroy() {
            super.onDestroy();
        }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permissions granted, continue with download
                Log.d("tag45","7");
                View v = null;
                onDownloadClick(v);
            } else {
                Log.d("tag45","8");

                // permissions denied, show a message to the user
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
*/
    }
