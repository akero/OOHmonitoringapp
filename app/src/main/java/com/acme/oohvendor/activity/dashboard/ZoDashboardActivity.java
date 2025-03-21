package com.acme.oohvendor.activity.dashboard;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.acme.oohvendor.R;
import com.acme.oohvendor.activity.login.OTP;
import com.acme.oohvendor.adapters.CampaignListAdapter;
//import com.acme.afsvendor.databinding.ActivityMainBinding;
//import com.acme.afsvendor.databinding.ActivityViewCampaignSitesBinding;
import com.acme.oohvendor.databinding.ActivityAsmDashboardBinding;
import com.acme.oohvendor.databinding.ActivityViewVendorSitesBinding;
import com.acme.oohvendor.databinding.ActivityZoDashboardBinding;
import com.acme.oohvendor.viewmodel.APIreferenceclass;
import com.acme.oohvendor.viewmodel.ApiInterface;
import com.acme.oohvendor.viewmodel.MainViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class ZoDashboardActivity extends AppCompatActivity implements ApiInterface {

    int id=0;
    String image="";
    String vendor_id="";
    int campaign_id=0;
    String start_date=null;
    String end_date=null;
    String location="";
    String longitude="";
    String latitude="";
    String width="";
    String height="";
    String total_area="";
    String media_type="";
    String illumination="";
    String created_at="";
    String updated_at="";
    MainViewModel mainViewModel;
    ActivityZoDashboardBinding binding;
    //JSONArray jsonArray;
    boolean showMenus = false;
    private final Context ctxt= this;
    int vendorclientorcampaign=2; //campaign is 0, client is 1, vendor is 2
    //TODO- populate this token
    String logintoken="";
    String idofcampaign;

    ProgressBar progressBar;
    Animation rotateAnimation;
    int vendorid;
    String area, realarea;

    //todo access token save to memory add to api call

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_zo_dashboard);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.rvCampaignList.setLayoutManager(layoutManager);
        vendorid= 0;
        area= "";
        realarea= "";

        Log.d("whichclass", "ActivityzoDashbo");

        binding.ivPlus.setVisibility(View.GONE);

        //animation code
        progressBar= findViewById(R.id.progressBar);
        rotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_animation);
        //animation code

        Log.d("tag97", "here");

        //idofcampaign=getIntent().getStringExtra("id");
        //Log.d("tag58", idofcampaign);
        vendorid= getIntent().getIntExtra("recceasmid", 0);
        Log.d("tag58", Integer.toString(vendorid));

        area= getIntent().getStringExtra("area");
        realarea= getIntent().getStringExtra("realarea");
        Log.d("tag58area", area);

        try{

            String name= getIntent().getStringExtra("name");
            binding.title.setText(binding.title.getText()+ " -\n "+ name);

        }catch (Exception e){
            e.printStackTrace();
        }

        FileHelper fh= new FileHelper();
        logintoken= fh.readLoginToken(this);

        //TODO remove after adding to ui
        jsonArray1= new JSONArray();
        CampaignListAdapter adapter = new CampaignListAdapter(this, jsonArray1, false);
        binding.rvCampaignList.setAdapter(adapter);
        campaignList(realarea);
    }


    //onresponsereceived from api
    public void onResponseReceived(String response){

        Log.d("addbatest", "response is "+ response);
        Log.d("tag58","got response");
        Log.d("tag111", "viewvendorsites");

        implementUi(response);

        /*
        //TODO: handle population
        String[] dataStrings = extractDataStrings(response);
        // Usage example
        for(String dataStr : dataStrings) {
            Log.d("tag2222",dataStr);
        }
        //id array. send to ui
        String[] idArray= extractIds(dataStrings);

        //TODO extract the unit id and pass that too
        implementUi(response);
        Log.d("MyApp", "Extracted IDs: " + Arrays.toString(idArray));

 */

    }
    String[] idarray;

    String campaignName;
    JSONArray jsonArray1;
    private void implementUi(String response){
        try {
            JSONObject jsonObject = new JSONObject();
            //jsonArray1= new JSONArray();

            Log.d("tg111", response);
            String ids[];
            JSONObject jsonResponse = new JSONObject(response);

            campaignName="";
            //campaignName= jsonResponse.getString("campaign_name");

            if(jsonResponse.getInt("status")== 200) {
                JSONArray dataArray = jsonResponse.getJSONArray("data");
                idarray= new String[dataArray.length()];

                if(dataArray != null && dataArray.length() > 0) {
                    if(vendorclientorcampaign==0){

                        for(int i=0; i< dataArray.length();i++){

                            JSONObject dataObject = dataArray.getJSONObject(i);
                            if(dataObject != null) {
                                jsonObject = new JSONObject();
                                Log.d("DataObjectContent", "Data Object: " + dataObject.toString());
                                //AdminCrudDataClass siteDetail = new AdminCrudDataClass();
                                idarray[i]= Integer.toString(dataObject.optInt("id"));

                                jsonObject.putOpt("id", dataObject.optInt("id"));
                                jsonObject.putOpt("uid", dataObject.optString("uid"));
                                jsonObject.putOpt("image", dataObject.optString("image"));
                                jsonObject.putOpt("name", dataObject.optString("location"));
                                jsonObject.putOpt("uid", dataObject.optString("code"));

                                //siteDetail.setName(dataObject.optString("name"));

                                try {
                                    String imageUrl = dataObject.optString("image");
                                    if (imageUrl == null || imageUrl.isEmpty()) {
                                        imageUrl = dataObject.optString("new_image");
                                    }
                                    imageUrl = "https://ooh.warburttons.com/" + imageUrl;

                                    Log.d("tag41", "imageurl is " + imageUrl);
                                    if (imageUrl != "null" && !imageUrl.isEmpty()) {
                                        URL url = new URL(imageUrl);
                                        Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                        //siteDetail.setImage(bitmap);
                                    }
                                } catch (Exception e) {
                                    Log.d("tag41", "error in implementui" + e.toString());
                                    Log.e("tag41", "sdfdg", e);
                                    // Handle error
                                }
                                if(i==0) {
                                    jsonArray1 = new JSONArray();
                                }
                                jsonArray1.put(jsonObject);
//TODO here
                            }
                        }
                    }else if(vendorclientorcampaign==1){
                        for(int i=0; i< dataArray.length();i++){
                            JSONObject dataObject = dataArray.getJSONObject(i);
                            if(dataObject != null) {
                                jsonObject = new JSONObject();
                                Log.d("DataObjectContent", "Data Object: " + dataObject.toString());
                                //AdminCrudDataClass siteDetail = new AdminCrudDataClass();
                                idarray[i]= Integer.toString(dataObject.optInt("id"));

                                jsonObject.putOpt("id", dataObject.optInt("id"));
                                jsonObject.putOpt("company_name", dataObject.optString("company_name"));
                                jsonObject.putOpt("image", dataObject.optString("logo"));
                                jsonObject.putOpt("name", dataObject.optString("name"));

                                //siteDetail.setName(dataObject.optString("name"));

                                try {
                                    String imageUrl = dataObject.optString("logo");
                                    if (imageUrl == null || imageUrl.isEmpty()) {
                                        imageUrl = dataObject.optString("new_image");
                                    }
                                    imageUrl = "https://acme.warburttons.com/" + imageUrl;

                                    Log.d("tag41", "imageurl is " + imageUrl);
                                    if (imageUrl != "null" && !imageUrl.isEmpty()) {
                                        URL url = new URL(imageUrl);
                                        Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                        //siteDetail.setImage(bitmap);
                                    }
                                } catch (Exception e) {
                                    Log.d("tag41", "error in implementui" + e.toString());
                                    Log.e("tag41", "sdfdg", e);
                                    // Handle error
                                }
                                if(i==0) {
                                    jsonArray1 = new JSONArray();
                                }
                                jsonArray1.put(jsonObject);
//TODO here
                            }
                        }

                    }else if(vendorclientorcampaign==2){
                        for(int i=0; i< dataArray.length();i++){
                            JSONObject dataObject = dataArray.getJSONObject(i);
                            if(dataObject != null) {
                                jsonObject = new JSONObject();
                                Log.d("DataObjectContent", "Data Object: " + dataObject.toString());
                                //AdminCrudDataClass siteDetail = new AdminCrudDataClass();
                                idarray[i]= Integer.toString(dataObject.optInt("id"));

                                jsonObject.putOpt("id", dataObject.optInt("id"));
                                jsonObject.putOpt("company_name", dataObject.optString("retail_name"));
                                jsonObject.putOpt("image", dataObject.optString("image"));
                                jsonObject.putOpt("name", dataObject.optString("location"));

                                //siteDetail.setName(dataObject.optString("name"));

                                try {
                                    String imageUrl = dataObject.optString("image");
                                    if (imageUrl == null || imageUrl.isEmpty()) {
                                        imageUrl = dataObject.optString("new_image");
                                    }
                                    imageUrl = "https://ooh.warburttons.com/" + imageUrl;
                                    Log.d("tag41", "imageurl is " + imageUrl);
                                    if (imageUrl != "null" && !imageUrl.isEmpty()) {
                                        URL url = new URL(imageUrl);
                                        Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                        //siteDetail.setImage(bitmap);
                                    }
                                } catch (Exception e) {
                                    Log.d("tag41", "error in implementui" + e.toString());
                                    Log.e("tag41", "sdfdg", e);
                                    // Handle error
                                }
                                if(i==0) {
                                    jsonArray1 = new JSONArray();
                                }
                                jsonArray1.put(jsonObject);
//TODO here
                            }
                        }
                    }
                    Log.d("JSONArrayContent", "JSONArray1: " + jsonArray1.toString());
                }else if(dataArray == null || dataArray.length() <= 0){
                    jsonArray1= new JSONArray();
                    //jsonArray1.put(jsonObject);
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Your UI update code goes here

                    GridLayoutManager layoutManager = new GridLayoutManager(ctxt, 1);
                    binding.rvCampaignList.setLayoutManager(layoutManager);
                    if(jsonArray1.toString().equals("[]")){

                        binding.noData.setVisibility(View.VISIBLE);
                    }else{

                        binding.noData.setVisibility(View.GONE);
                    }

                    CampaignListAdapter adapter = new CampaignListAdapter(ctxt, jsonArray1, false);


                    //binding.tvCampaign.setText(campaignName);
                    progressBar.clearAnimation();
                    progressBar.setVisibility(View.GONE);
                    //view.setVisibility(View.GONE);

                    binding.rvCampaignList.setAdapter(adapter);
                }});
        }catch (Exception e){}
    }

    private void campaignList(String id) {
        vendorclientorcampaign=0;
        //TODO pass correct logintoken here
        //logintoken="211|fcsu2C90hfOUduHNXDSZRxu7394NaQhOpiG3zMeM";

        //animation code
        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(rotateAnimation);
        //view.setVisibility(View.VISIBLE);
        //animation code

        double a= 1;
        int b= 1;
        APIreferenceclass api= new APIreferenceclass(logintoken, this, id, a, b);
    }

    @Override
    public void onBackPressed() {
        // Do nothing when the back button is pressed
    }


    public void onItemClick(int position) {
        try {
            Log.d("tag51", Integer.toString(position));
            // Retrieve JSONObject from your jsonArray at position
            JSONObject jsonObject = jsonArray1.getJSONObject(position);
            Log.d("tag51", jsonArray1.getJSONObject(position).toString());

            //logintoken="Bearer 322|7Dor2CuPXz4orJV5GUleBAUcmgYnbswVMLQ5EUNM";

            // Get site id or site no from the JSONObject
            String id = jsonObject.getString("id"); // Or get an id if you have that
            Log.d("tag51", jsonObject.getString("id"));

            // String siteId = jsonObject.getString("siteId"); // If you have a site id.

            //TODO change this

            // Start new activity and pass the retrieved data
            startActivity(new Intent(this, ViewSiteDetailActivity.class)
                    .putExtra("campaignType", "old")
                    .putExtra("sitenumber", id)
                    .putExtra("camefrom", "ZoDashboardActivity")
                    .putExtra("campaignId", idofcampaign)
                    .putExtra("logintoken", logintoken)
                    .putExtra("vendorclientorcampaign", vendorclientorcampaign)
                    .putExtra("idarray", idarray));

            // .putExtra("siteId", siteId)); // If you are passing site id
        } catch (JSONException e) {
            Log.d("tag123", e.toString());
            // Handle exception (e.g. show a Toast to the user indicating an error)
        }
    }

    private void clearUi() {
        // Clear the RecyclerView
        if (binding.rvCampaignList.getAdapter() != null) {
            CampaignListAdapter adapter = (CampaignListAdapter) binding.rvCampaignList.getAdapter();
            adapter.clearData(); // You'll need to implement a method 'clearData()' in your adapter class
        }
        // Reset any other UI elements here as needed
    }

    private String[] extractIds(String[] dataStrings) {
        // Create an array to store extracted ids.
        String[] ids = new String[dataStrings.length];

        // Loop through each string in the input array.
        for (int i = 0; i < dataStrings.length; i++) {
            try {
                // Parse the string into a JSONObject.
                JSONObject jsonObject = new JSONObject(dataStrings[i]);

                // Extract the "id" field and store it in the ids array.
                ids[i] = String.valueOf(jsonObject.getInt("id"));
            } catch (JSONException e) {
                // Handle JSON parsing error. Here setting the id to a default error value ("error").
                ids[i] = "error";
                e.printStackTrace();
            }
        }

        // Return the extracted ids.
        return ids;
    }

    //extracting data into an array. the api response
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

    public void onPlusClick(View view) {
        //TODO here
        Log.d("tag20", "onplusclick1");

        Intent intent= new Intent(ZoDashboardActivity.this, AddSiteDetailActivity.class);
        startActivity(intent);
    }

    public void onNotificationClick(View view){
        //TODO ask lodu what this does
    }

    public void onAddClientClick(View view) {
        startActivity(new Intent(this, AddClientActivity.class));
    }

    public void onAddVenderClick(View view) {
        startActivity(new Intent(this, AddVenderActivity.class));
    }

    public void onAddSiteClick(View view){

        Intent intent= new Intent(ZoDashboardActivity.this, AddSiteActivity.class);
        //intent.putExtra("area", area);
        //Log.d("xyz", String.valueOf(vendorid));
        //intent.putExtra("vendorid", String.valueOf(vendorid));
        startActivity(intent);
    }

    public void onDeleteClientClick(View view) {
        // ... your logic ...
    }

    public void onProfileClick(View view){
        String userid= FileHelper.readUserId(this);
        Intent intent= new Intent(ZoDashboardActivity.this, ProfileActivity.class);;
        intent.putExtra("userid", userid);
        startActivity(intent);

    }

    public void back(View v){
        onBackPressed();
    }

    public void logout(View v) {

        try {
            FileHelper fh = new FileHelper();
            fh.writeUserType(this, "");

            Intent intent= new Intent(ZoDashboardActivity.this, OTP.class);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}