package com.acme.afsvendor.activity.vender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import com.acme.afsvendor.R;
import com.acme.afsvendor.activity.dashboard.AddCampaignDetails;
import com.acme.afsvendor.activity.dashboard.FileHelper;
import com.acme.afsvendor.activity.dashboard.ViewVendorSites;
import com.acme.afsvendor.adapters.CampaignListAdapter;
import com.acme.afsvendor.databinding.ActivityVenderDashBoardBinding;
import com.acme.afsvendor.viewmodel.APIreferenceclass;
import com.acme.afsvendor.viewmodel.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VenderDashBoardActivity extends AppCompatActivity implements ApiInterface{

    private ActivityVenderDashBoardBinding binding;
    Executor cronetExecutor= Executors.newSingleThreadExecutor();

    String loginToken="";

    JSONArray jsonArray1;

    //intent contents
    JSONArray jsonArray2;
    ProgressBar progressBar;
    Animation rotateAnimation;
    int vendorid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vender_dash_board);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.rvCampaignList.setLayoutManager(layoutManager);

        Log.d("whichclass", "VendorDashBoardActivity");

        binding.ivPlus.setVisibility(View.GONE);

        //animation code
        progressBar= findViewById(R.id.progressBar);
        rotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_animation);
        //animation code

        try{
            //vendorid= getIntent().getIntExtra("vendorid", 0);
        }catch (Exception e){
            e.printStackTrace();
        }

        loginToken= FileHelper.readLoginToken(this);
        Log.d("tg4", loginToken);

        jsonArray1= new JSONArray();
        jsonArray2= new JSONArray();
        //Log.d("vdbatest", "logintoken "+loginToken);
        CampaignListAdapter adapter = new CampaignListAdapter(this, jsonArray1, false);
        binding.rvCampaignList.setAdapter(adapter);
        campaignList();
    }

    public void onResponseReceived(String response){

        Log.d("addbatest", "response is "+ response);
        Log.d("tag21","5");

        implementUi(response);
    }

    private void implementUi(String response){
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObject1= new JSONObject();
            JSONObject jsonObject2= new JSONObject();
            jsonArray1= new JSONArray();
            jsonArray2= new JSONArray();
            //jsonArray3= new JSONArray();
            String ids[];
            JSONObject jsonResponse = new JSONObject(response);
            if(jsonResponse.getString("status").equals("success")) {
                JSONArray dataArray = jsonResponse.getJSONArray("live_campaigns");
                if(dataArray != null && dataArray.length() > 0) {

                        for (int i = 0; i < dataArray.length(); i++) {

                            JSONObject dataObject = dataArray.getJSONObject(i);
                            if (dataObject != null) {
                                jsonObject = new JSONObject();
                                Log.d("DataObjectContent", "Data Object: " + dataObject.toString());
                                //AdminCrudDataClass siteDetail = new AdminCrudDataClass();
                                jsonObject.putOpt("id", dataObject.optInt("id"));
                                jsonObject.putOpt("uid", dataObject.optString("uid"));
                                jsonObject.putOpt("image", dataObject.optString("image"));
                                jsonObject.putOpt("name", dataObject.optString("name"));

                                //siteDetail.setName(dataObject.optString("name"));

                                try {
                                    String imageUrl = dataObject.optString("image");
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
                                jsonArray1.put(jsonObject);

                            }
                        }

                    Log.d("JSONArrayContent", "JSONArray1: " + jsonArray1.toString());
                }

                JSONArray dataArray1 = jsonResponse.getJSONArray("old_campaigns");
                if(dataArray1 != null && dataArray1.length() > 0) {

                    for (int i = 0; i < dataArray1.length(); i++) {

                        JSONObject dataObject = dataArray1.getJSONObject(i);
                        if (dataObject != null) {
                            jsonObject = new JSONObject();
                            Log.d("DataObjectContent", "Data Object: " + dataObject.toString());
                            //AdminCrudDataClass siteDetail = new AdminCrudDataClass();
                            jsonObject.putOpt("id", dataObject.optInt("id"));
                            jsonObject.putOpt("uid", dataObject.optString("uid"));
                            jsonObject.putOpt("image", dataObject.optString("image"));
                            jsonObject.putOpt("name", dataObject.optString("name"));

                            //siteDetail.setName(dataObject.optString("name"));

                            try {
                                String imageUrl = dataObject.optString("image");
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
                            jsonArray1.put(jsonObject);

                        }
                    }

                    Log.d("JSONArrayContent", "JSONArray1: " + jsonArray1.toString());
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Your UI update code goes here

                    GridLayoutManager layoutManager = new GridLayoutManager(ctxt, 1);
                    binding.rvCampaignList.setLayoutManager(layoutManager);
                    CampaignListAdapter adapter = new CampaignListAdapter(ctxt, jsonArray1, false);

                    //animation code

                            progressBar.clearAnimation();
                            progressBar.setVisibility(View.GONE);

                            //animation code

                    binding.rvCampaignList.setAdapter(adapter);

                }});
        }catch (Exception e){
            Log.d("tag21", e.toString());

        }
    }

    private void campaignList() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.rvCampaignList.setLayoutManager(layoutManager);

        int vendorclientorcampaign= 2;

        //String logintoken= "322|7Dor2CuPXz4orJV5GUleBAUcmgYnbswVMLQ5EUNM";

        /*
        FileHelper fh= new FileHelper();
        logintoken= fh.readLoginToken(this);
*/
        int a=0;

        //animation code
        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(rotateAnimation);
        //animation code

//here
        APIreferenceclass apiref= new APIreferenceclass(loginToken, this, vendorid);
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

    //String logintoken;

    public void onItemClick(int position) {
        try {
            Log.d("tag51", Integer.toString(position));
            // Retrieve JSONObject from your jsonArray at position
            JSONObject jsonObject = jsonArray1.getJSONObject(position);
            Log.d("tag51", jsonArray1.getJSONObject(position).toString());

            // Get site id or site no from the JSONObject
            String id = jsonObject.getString("id"); // Or get an id if you have that
            Log.d("tag51", jsonObject.getString("id"));
            Log.d("tag60", jsonObject.toString());

            // String siteId = jsonObject.getString("siteId"); // If you have a site id.
            int vendorclientorcampaign= 2;



                // Start new activity and pass the retrieved data
                startActivity(new Intent(this, ViewVendorSites.class)
                        .putExtra("campaignType", "old")
                        .putExtra("id", id)
                        .putExtra("logintoken", loginToken)
                        .putExtra("vendorclientorcampaign", vendorclientorcampaign)
                        .putExtra("apiresponse", jsonObject.toString()));

            // .putExtra("siteId", siteId)); // If you are passing site id
        } catch (JSONException e) {
            Log.d("tag123", e.toString());
            // Handle exception (e.g. show a Toast to the user indicating an error)
        }
    }

    Context ctxt= this;

    public void onPlusClick(View view) {

        Log.d("tag20", "onplusclick");

        Intent intent = new Intent(VenderDashBoardActivity.this, AddCampaignDetails.class);
        intent.putExtra("logintoken", loginToken);
        startActivity(intent);
    }
}