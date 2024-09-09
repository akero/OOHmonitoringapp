package com.acme.afsvendor.activity.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import com.acme.afsvendor.R;
import com.acme.afsvendor.databinding.ActivityViewSiteDetailBinding;
//import com.acme.afsvendor.databinding.ActivityViewSiteDetailClientDashBinding;
import com.acme.afsvendor.databinding.ActivityViewSiteDetailClientDashBinding;
import com.acme.afsvendor.utility.RoundRectCornerImageView;
import com.acme.afsvendor.viewmodel.APIreferenceclass;
import com.acme.afsvendor.viewmodel.ApiInterface;
import com.acme.afsvendor.viewmodel.SiteDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class ViewSiteDetailActivityClientDash extends AppCompatActivity implements ApiInterface {

    private String campaignType = "";
    private int position = 0;
    String siteNumber= "";
    String logintoken="";
    String campaignId="";
    private GestureDetector gestureDetector;

    private ActivityViewSiteDetailClientDashBinding binding;
    String idarray[];

    //TODO populate all fields. pass api call data from prev activity
    //have to pass logintoken and siteid

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_site_detail_client_dash);

        Log.d("tag41", "1");
        Log.d("whichclass", "ViewSiteDetailActivityClientDash");

        if (getIntent().getExtras() != null) {
            Log.d("tag41", "2");
            idarray= null;
            campaignId = getIntent().getExtras().getString("campaignId", "");
            campaignType = getIntent().getExtras().getString("campaignType", "");
            siteNumber= getIntent().getExtras().getString("siteNumber", "");
            logintoken= getIntent().getExtras().getString("logintoken","");

            try {
                idarray = getIntent().getExtras().getStringArray("idarray");
                for(int i= 0; i<idarray.length; i++){
                    Log.d("idarray", idarray[i]);
                }

            }catch (Exception e){
                Log.d("tag22", e.toString());
            }

            Log.d("tg2", siteNumber);
        }

        gestureDetector = new GestureDetector(this, new MyGestureListener());
        //View rootView = findViewById(R.id.scrollview);
        //rootView.setOnTouchListener((v, event) -> {
          //  gestureDetector.onTouchEvent(event);
            //return true;
        //});

        //buttons to move to next or prev site
        ImageView left= findViewById(R.id.lefticon);
        left.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                String prevSite= "";
                for(int i=0; i<idarray.length;i++){
                    if(idarray[i].equals(siteNumber)){
                        if(!prevSite.equals("")){
                            siteNumber= prevSite;
                            apicall(logintoken, prevSite);
                            break;
                        }
                    }else{
                        prevSite= idarray[i];
                    }
                }
            }

        });

        ImageView right= findViewById(R.id.righticon);
        right.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                String nextSite= "";
                for(int i=0; i<idarray.length;i++){
                    if(idarray[i].equals(siteNumber)){
                        if(i!=idarray.length-1){
                            nextSite= idarray[i+1];
                            siteNumber= nextSite;
                            apicall(logintoken, nextSite);
                            break;
                        }
                    }
                }
            }

        });

        Log.d("tag41", "4");
        apicall(logintoken, siteNumber);
        Log.d("tag41", "5");

        //apicallgetcampaigns(logintoken, campaignId);
    }

    private void handleSwipeLeft() {
        //TODO Update your content here (e.g., fetch new data, update views)

        for(int i= 0; i<siteIdArray.length; i++){
            if(siteIdArray[i]== Integer.parseInt(siteNumber)){

                if((i+1)<= siteIdArray.length) {
                    siteNumber = String.valueOf(siteIdArray[i + 1]);
                    apicall(logintoken, siteNumber);
                }
            }
        }
    }

    private void handleSwipeRight() {

        //TODO Update your content here (e.g., fetch new data, update views)
        for(int i= 0; i<siteIdArray.length; i++){
            if(siteIdArray[i]== Integer.parseInt(siteNumber)){

                if((i-1)>=0) {
                    siteNumber = String.valueOf(siteIdArray[i - 1]);
                    apicall(logintoken, siteNumber);
                }
            }
        }
    }

    SiteDetail siteDetail;
    JSONObject jsonobj;

    private void implementUI(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            if(jsonResponse.getString("status").equals("success")) {
                JSONObject dataArray = new JSONObject(jsonResponse.getString("site"));
                if(dataArray != null && dataArray.length() > 0) {
                    JSONObject dataObject = dataArray;
                    jsonobj= dataObject;
                    if(dataObject != null) {
                        siteDetail = new SiteDetail();
                        siteDetail.setId(dataObject.optInt("id"));
                        siteDetail.setVendorId(dataObject.optString("vendor_id"));
                        siteDetail.setLocation(dataObject.optString("location"));
                        siteDetail.setCreatedAt(dataObject.optString("created_at"));
                        siteDetail.setEndDate(dataObject.optString("end_date"));
                        siteDetail.setLatitude(dataObject.optString("latitude"));
                        siteDetail.setLongitude(dataObject.optString("longitute")); // Consider renaming "longitute" to "longitude" in your JSON or code for consistency
                        siteDetail.setMediaType(dataObject.optString("media_type"));
                        siteDetail.setIllumination(dataObject.optString("illumination"));
                        siteDetail.setStartDate(dataObject.optString("start_date"));
                        siteDetail.setName(dataObject.optString("name"));
                        siteDetail.setSiteNo(dataObject.optString("site_no"));
                        siteDetail.setWidth(dataObject.optString("width"));
                        siteDetail.setHeight(dataObject.optString("height"));
                        siteDetail.setTotalArea(dataObject.optString("total_area"));
                        siteDetail.setUpdatedAt(dataObject.optString("updated_at"));

                        Log.d("SiteDetailLog",
                                "ID: " + siteDetail.getId() +
                                        ", Vendor ID: " + siteDetail.getVendorId() +
                                        ", Location: " + siteDetail.getLocation() +
                                        ", Created At: " + siteDetail.getCreatedAt() +
                                        ", End Date: " + siteDetail.getEndDate() +
                                        ", Latitude: " + siteDetail.getLatitude() +
                                        ", Longitude: " + siteDetail.getLongitude() +
                                        ", Media Type: " + siteDetail.getMediaType() +
                                        ", Illumination: " + siteDetail.getIllumination() +
                                        ", Start Date: " + siteDetail.getStartDate() +
                                        ", Name: " + siteDetail.getName() +
                                        ", Site No: " + siteDetail.getSiteNo() +
                                        ", Width: " + siteDetail.getWidth() +
                                        ", Height: " + siteDetail.getHeight() +
                                        ", Total Area: " + siteDetail.getTotalArea() +
                                        ", Updated At: " + siteDetail.getUpdatedAt());


                        try {
                            String imageUrl = dataObject.optString("image");
                            imageUrl= "https://acme.warburttons.com/"+ imageUrl;
                            Log.d("tag41", "imageurl is "+ imageUrl);
                            Log.d("tg2", "image code not executing 1");

                            if(imageUrl != "null" && !imageUrl.isEmpty()) {
                                URL url = new URL(imageUrl);
                                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                Log.d("tg2", "image code executing");
                                siteDetail.setImage(bitmap);
                            }
                        } catch (Exception e) {
                            Log.d("tg2", "image code not executing 2");
                            Log.d("tag41", "error in implementui" +e.toString());
                            Log.e("tag41", "sdfdg", e);
                            // Handle error
                        }

                        // Update UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                TextView tvSiteId = findViewById(R.id.etSiteNo);
                                //TODO after person implements site name then change this
                                tvSiteId.setText(String.valueOf(siteDetail.getSiteNo()));

                                TextView tvLocation = findViewById(R.id.tvLocation);
                                tvLocation.setText(siteDetail.getLocation());

                                TextView tvSiteName = findViewById(R.id.title);
                                tvSiteName.setText(siteDetail.getName());

                                //TextView tvLastInspection = findViewById(R.id.tvStartDate);
                                //tvLastInspection.setText(siteDetail.getCreatedAt());

                                TextView tvLatitude = findViewById(R.id.tvLatitude);
                                tvLatitude.setText(siteDetail.getLatitude());

                                TextView tvLongitude = findViewById(R.id.tvLongitude);
                                tvLongitude.setText(siteDetail.getLongitude());

                                TextView tvMediaType = findViewById(R.id.tvMediaType);
                                tvMediaType.setText(siteDetail.getMediaType());

                                TextView tvIllumination = findViewById(R.id.tvIllumination);
                                tvIllumination.setText(siteDetail.getIllumination());

                                TextView tvStartDate = findViewById(R.id.tvStartDate);
                                tvStartDate.setText(siteDetail.getStartDate());

                                TextView tvEndDate = findViewById(R.id.tvEndDate);
                                tvEndDate.setText(siteDetail.getEndDate());

                                // Set the site number
                                TextView tvSiteNo = findViewById(R.id.etSiteNo);
                                tvSiteNo.setText(String.valueOf(siteDetail.getSiteNo())); // assuming getter method exists

                                // Set the width
                                TextView tvWidth = findViewById(R.id.tvWidth);
                                tvWidth.setText(siteDetail.getWidth()); // assuming getter method exists

                                // Set the height
                                TextView tvHeight = findViewById(R.id.tvHeight);
                                tvHeight.setText(siteDetail.getHeight()); // assuming getter method exists

                                // Set the total area
                                TextView tvTotalArea = findViewById(R.id.tvTotalArea);
                                tvTotalArea.setText(siteDetail.getTotalArea()); // assuming getter method exists
                                Log.d("tg2", "image code not executing");

                                binding.siteno.setText(siteDetail.getSiteNo());
                                binding.clientid.setText(Integer.toString(siteDetail.getId()));
                                try {
                                    binding.campaign.setText(jsonResponse.getString("campaign_name"));
                                    binding.vendorname.setText(jsonResponse.getString("vendor_name"));

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }


                                RoundRectCornerImageView tvImage = findViewById(R.id.ivCampaignImage);
                                if(siteDetail.getImage()!=null) {
                                    Log.d("tg2", "image code executing");
                                    tvImage.setImageBitmap(siteDetail.getImage());
                                }
                            }
                        });
                    }
                } else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ViewSiteDetailActivityClientDash.this, "Error retrieving or parsing data.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            } else {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ViewSiteDetailActivityClientDash.this, "Error retrieving or parsing data..", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        } catch (Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("tg41", e.toString());
                    Toast.makeText(ViewSiteDetailActivityClientDash.this, "Error retrieving or parsing data...", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Assigning values and listeners to Buttons
        Button btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(ViewSiteDetailActivityClientDash.this, AddSiteDetailActivity.class);
                intent.putExtra("loginToken", logintoken);
                intent.putExtra("campaignId", campaignId);
                intent.putExtra("editingsite", "yes");
                Log.d("tag000", logintoken+ "| "+ campaignId+"| "+  siteNumber+ "| "+ jsonobj.toString());
                Log.d("tag000", siteNumber);

                intent.putExtra("siteNumber", siteNumber);
                intent.putExtra("siteDetail", jsonobj.toString());
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

    void apicall(String logintoken, String siteNumber){

        Log.d("tag41", "6apicall");
        Context context= this;
        APIreferenceclass api= new APIreferenceclass(logintoken, siteNumber, context);
        Log.d("tag41", "7apicall");
    }

    void apicallgetcampaigns(String logintoken, String campaignId){
        Log.d("tag41", "6apicallcampaigns");
        Context context= this;
        APIreferenceclass api= new APIreferenceclass(logintoken, context, campaignId);
        Log.d("tag41", "7apicallcampaigns");
    }

    int[] siteIdArray;

    void extractsiteids(String response){

        try{

            Log.d("tg1234", response);
            JSONObject jsonobj= new JSONObject(response);
            if(jsonobj.getString("status").equals("success")){

                JSONArray jsonArray= jsonobj.getJSONArray("site");
                siteIdArray= new int[jsonArray.length()];

                for(int i=0; i< jsonArray.length(); i++){
                    JSONObject jsonobj1= jsonArray.getJSONObject(i);
                    siteIdArray[i]= jsonobj1.getInt("id");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    String response1="";

    @Override
    public void onResponseReceived(String response){

        JSONObject jsonno= null;

        try{
            jsonno= new JSONObject(response);


        response1= response;

        if(jsonno.getString("message").equals("Sites retrieved successfully.")) {

            //todo here
            //extractsiteids(response);
            implementUI(response);

        }else{
            implementUI(response);
            Log.d("tag41", response);
        }
        }catch(Exception e){
            e.printStackTrace();
        }
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
        //binding.tvOldCampaign.setBackgroundResource(R.drawable.primaryround);
        //binding.tvLiveCampaign.setBackgroundResource(R.color.coloryellow);
    }

    public void liveCampaignClick(View view) {
        //binding.tvLiveCampaign.setBackgroundResource(R.drawable.primaryround);
        //binding.tvOldCampaign.setBackgroundResource(R.color.coloryellow);
    }

    //TODO delete. download code

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();

            if (Math.abs(diffX) > Math.abs(diffY)) {
                // Horizontal swipe detected
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        // Swipe left
                        handleSwipeLeft();
                    } else {
                        // Swipe right
                        handleSwipeRight();
                    }
                }
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}
