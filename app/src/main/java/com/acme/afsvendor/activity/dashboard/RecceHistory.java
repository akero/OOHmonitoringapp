package com.acme.afsvendor.activity.dashboard;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.acme.afsvendor.activity.login.OTP;
import com.acme.afsvendor.databinding.ActivityRecceHistoryBinding;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.acme.afsvendor.R;
import com.acme.afsvendor.adapters.CampaignListAdapter;
import com.acme.afsvendor.viewmodel.APIreferenceclass;
import com.acme.afsvendor.viewmodel.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class RecceHistory extends AppCompatActivity implements ApiInterface {

    int id;
    String logintoken;
    String[] idarray;
    private final Context ctxt= this;
    //TODO- populate this token


    ProgressBar progressBar;
    Animation rotateAnimation;
    int btn; // 0 for all, 1 for pending, 2 for approved, 3 for rejected

    String campaignName;
    JSONArray jsonArray1;
    ActivityRecceHistoryBinding binding;
    int whiteColor;
    int blackColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recce_history);
        campaignName="";
        btn= 0;

        whiteColor = ContextCompat.getColor(this, R.color.white);
        blackColor = ContextCompat.getColor(this, R.color.black);

        id= 0;
        logintoken= "";

        try{

            FileHelper fh= new FileHelper();
            id= Integer.parseInt(fh.readUserId(this));
            logintoken = fh.readLoginToken(this);

        }catch(Exception e){
            Log.d("asdsad", e.toString());
        }

        binding.ivNotification.setOnClickListener(new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View view) {

                                                          logout();

                                                      }
                                                  }
        );

        binding.ivPlusicon.setOnClickListener(new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View view) {

                                                          Intent intent= new Intent(RecceHistory.this, RecceDashboardActivity.class);
                                                          startActivity(intent);
                                                      }
                                                  }
        );

        binding.ivSearchicon.setOnClickListener(new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View view) {


                                                      }
                                                  }
        );



        Log.d("id", Integer.toString(id));

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2
        );
        binding.rvCampaignList.setLayoutManager(layoutManager);

        Log.d("whichclass", "RecceHistory");

        binding.ivPlus.setVisibility(View.GONE);




        //animation code
        progressBar= findViewById(R.id.progressBar);
        rotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_animation);
        //animation code
        //animation code
        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(rotateAnimation);
        //view.setVisibility(View.VISIBLE);
        //animation code

        Log.d("tag97", "here");



        //TODO remove after adding to ui
        jsonArray1= new JSONArray();
        CampaignListAdapter adapter = new CampaignListAdapter(this, jsonArray1, false);
        binding.rvCampaignList.setAdapter(adapter);

        APIreferenceclass api= new APIreferenceclass(logintoken, ctxt, id, btn, 0);


    }


    @SuppressLint("ResourceAsColor")
    public void btnAllClick(View view) {
        clearUi();
        btn= 0;

        binding.btnAll.setBackgroundResource(R.drawable.primaryround);
        binding.btnAll.setTextColor(whiteColor);

        binding.btnPending.setBackgroundResource(R.drawable.primarystroke);
        binding.btnPending.setTextColor(blackColor);

        binding.btnApproved.setBackgroundResource(R.drawable.primarystroke);
        binding.btnApproved.setTextColor(blackColor);

        binding.btnRejected.setBackgroundResource(R.drawable.primarystroke);
        binding.btnRejected.setTextColor(blackColor);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(rotateAnimation);

        allClick();
        // ... your logic ...
    }

    @SuppressLint("ResourceAsColor")
    public void btnPendingClick(View view) {
        clearUi();
        btn= 1;

        binding.btnPending.setBackgroundResource(R.drawable.primaryround);
        binding.btnPending.setTextColor(whiteColor);

        binding.btnAll.setBackgroundResource(R.drawable.primarystroke);
        binding.btnAll.setTextColor(blackColor);

        binding.btnApproved.setBackgroundResource(R.drawable.primarystroke);
        binding.btnApproved.setTextColor(blackColor);

        binding.btnRejected.setBackgroundResource(R.drawable.primarystroke);
        binding.btnRejected.setTextColor(blackColor);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(rotateAnimation);

        allClick();
        // ... your logic ...
    }

    @SuppressLint("ResourceAsColor")
    public void btnApprovedClick(View view) {
        clearUi();
        btn= 2;

        binding.btnApproved.setBackgroundResource(R.drawable.primaryround);
        binding.btnApproved.setTextColor(whiteColor);

        binding.btnPending.setBackgroundResource(R.drawable.primarystroke);
        binding.btnPending.setTextColor(blackColor);

        binding.btnAll.setBackgroundResource(R.drawable.primarystroke);
        binding.btnAll.setTextColor(blackColor);

        binding.btnRejected.setBackgroundResource(R.drawable.primarystroke);
        binding.btnRejected.setTextColor(blackColor);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(rotateAnimation);

        allClick();
        // ... your logic ...
    }

    @SuppressLint("ResourceAsColor")
    public void btnRejectedClick(View view) {
        clearUi();
        btn= 3;

        binding.btnRejected.setBackgroundResource(R.drawable.primaryround);
        binding.btnRejected.setTextColor(whiteColor);

        binding.btnPending.setBackgroundResource(R.drawable.primarystroke);
        binding.btnPending.setTextColor(blackColor);

        binding.btnApproved.setBackgroundResource(R.drawable.primarystroke);
        binding.btnApproved.setTextColor(blackColor);

        binding.btnAll.setBackgroundResource(R.drawable.primarystroke);
        binding.btnAll.setTextColor(blackColor);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(rotateAnimation);

        allClick();
        // ... your logic ...
    }

    void allClick(){
        //vendorclientorcampaign=0;
        //TODO pass correct logintoken here
        //logintoken="211|fcsu2C90hfOUduHNXDSZRxu7394NaQhOpiG3zMeM";
        Log.d("tg5","fin");

        APIreferenceclass api= new APIreferenceclass(logintoken, ctxt, id, btn, 0);
    }

    private void clearUi() {
        // Clear the RecyclerView
        if (binding.rvCampaignList.getAdapter() != null) {
            CampaignListAdapter adapter = (CampaignListAdapter) binding.rvCampaignList.getAdapter();
            adapter.clearData(); // You'll need to implement a method 'clearData()' in your adapter class

                jsonArray1 = new JSONArray();

        }
    }

    void logout() {

        try {
            FileHelper fh = new FileHelper();
            fh.writeUserType(this, "");

            Intent intent= new Intent(RecceHistory.this, OTP.class);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onItemClick(int position) {
        try {
            Log.d("tag51", Integer.toString(position));
            // Retrieve JSONObject from your jsonArray at position
            JSONObject jsonObject = jsonArray1.getJSONObject(position);
            Log.d("tag51", jsonArray1.getJSONObject(position).toString());

            //logintoken="Bearer 322|7Dor2CuPXz4orJV5GUleBAUcmgYnbswVMLQ5EUNM";

            // Get site id or site no from the JSONObject
            String id1 = jsonObject.getString("id"); // Or get an id if you have that
            Log.d("tag51", jsonObject.getString("id"));

            // String siteId = jsonObject.getString("siteId"); // If you have a site id.

            //TODO change this

            // Start new activity and pass the retrieved data
            startActivity(new Intent(this, RecceHistoryDetails.class)
                    .putExtra("id", id1)
                    .putExtra("camefrom", "RecceHistory")
                    .putExtra("userid", id)
                    .putExtra("logintoken", logintoken)
                    .putExtra("idarray", idarray));

            // .putExtra("siteId", siteId)); // If you are passing site id
        } catch (JSONException e) {
            Log.d("tag123", e.toString());
            // Handle exception (e.g. show a Toast to the user indicating an error)
        }
    }

    void implementUI(String response){

        try {
        JSONObject jsonObject = new JSONObject();
        jsonArray1= new JSONArray();

        Log.d("tg111", response);
        String ids[];
        JSONObject jsonResponse = new JSONObject(response);

        //campaignName="";
        //campaignName= jsonResponse.getString("campaign_name");

        if(jsonResponse.getString("message").equals("Data fetched successfully!")) {
            JSONArray dataArray = jsonResponse.getJSONArray("data");
            idarray= new String[dataArray.length()];

            if(dataArray != null && dataArray.length() > 0) {


                    for(int i=0; i< dataArray.length();i++){

                        JSONObject dataObject = dataArray.getJSONObject(i);
                        if(dataObject != null) {
                            jsonObject = new JSONObject();
                            Log.d("DataObjectContent", "Data Object: " + dataObject.toString());
                            //AdminCrudDataClass siteDetail = new AdminCrudDataClass();
                            idarray[i]= Integer.toString(dataObject.optInt("id"));

                            jsonObject.putOpt("id", dataObject.optInt("id"));
                            jsonObject.putOpt("uid", "n/a");
                            jsonObject.putOpt("image", dataObject.optString("image1"));
                            jsonObject.putOpt("name", dataObject.optString("retail_name"));

                            //siteDetail.setName(dataObject.optString("name"));

                            try {
                                String imageUrl = dataObject.optString("image1");
                                imageUrl = "https://acme.warburttons.com/" + imageUrl;
                                Log.d("tag41", "imageurl is " + imageUrl);
                                if (imageUrl != "null" && !imageUrl.isEmpty()) {
                                    URL url = new URL(imageUrl);
                                    //Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                    //siteDetail.setImage(bitmap);
                                }
                            } catch (Exception e) {
                                Log.d("tag41", "error in implementui" + e.toString());
                                Log.e("tag41", "sdfdg", e);
                                // Handle error
                            }
                            jsonArray1.put(jsonObject);
//TODO here
                        }
                    }

                Log.d("JSONArrayContent", "JSONArray1: " + jsonArray1.toString());
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Your UI update code goes here

                GridLayoutManager layoutManager = new GridLayoutManager(ctxt, 2);
                binding.rvCampaignList.setLayoutManager(layoutManager);
                CampaignListAdapter adapter = new CampaignListAdapter(ctxt, jsonArray1, false);


                //binding.tvCampaign.setText(campaignName);
                progressBar.clearAnimation();
                progressBar.setVisibility(View.GONE);
                //view.setVisibility(View.GONE);

                binding.rvCampaignList.setAdapter(adapter);
            }});
    }catch (Exception e){}
    }
    @Override
    public void onResponseReceived(String response) {

        try {
            JSONObject jsonobj = new JSONObject(response);

            if (jsonobj.getString("message").equals("Data fetched successfully!")&&btn==0) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        implementUI(response);
                    }
                });
            }else if (jsonobj.getString("message").equals("Data fetched successfully!")&&btn==1) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        implementUI(response);
                    }
                });
            }else if (jsonobj.getString("message").equals("Data fetched successfully!")&&btn==2) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        implementUI(response);
                    }
                });
            }else if (jsonobj.getString("message").equals("Data fetched successfully!")&&btn==3) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        implementUI(response);
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "An error occured.", Toast.LENGTH_SHORT).show();
                    }
            });}


        }catch (Exception e){
            e.printStackTrace();
        }




    }
}