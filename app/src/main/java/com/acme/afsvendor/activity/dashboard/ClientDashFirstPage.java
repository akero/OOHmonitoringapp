package com.acme.afsvendor.activity.dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import com.acme.afsvendor.R;
import com.acme.afsvendor.adapters.CampaignListAdapter;
import com.acme.afsvendor.databinding.ActivityClientDashFirstPageBinding;
import com.acme.afsvendor.viewmodel.APIreferenceclass;
import com.acme.afsvendor.viewmodel.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClientDashFirstPage extends AppCompatActivity implements ApiInterface {

    private ActivityClientDashFirstPageBinding binding;
    private boolean oldcampaign = true;

    String loginToken="";
    int liveold; //0 live

    Context ctxt;

    ProgressBar progressBar;
    Animation rotateAnimation;
    int clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_client_dash_first_page);

        Log.d("tag199", "1");
        Log.d("whichclass", "ClientDashFirstPage");
        clientId= 0;
        liveold= 0;

        //animation code
        progressBar= findViewById(R.id.progressBar);
        rotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_animation);
        //animation code

        String jsonArray= "";
        try {
            jsonArray = getIntent().getStringExtra("jsonArray");
            Log.d("tag234", jsonArray);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            JSONObject jsonobj = new JSONObject(jsonArray);
            clientId= jsonobj.getInt("id");
            Log.d("clientid", Integer.toString(clientId));
        }catch(Exception e){
            e.printStackTrace();
        }

        ctxt= this;

        loginToken= FileHelper.readLoginToken(this);
        Log.d("tg4", loginToken);

        CampaignListAdapter adapter = new CampaignListAdapter(this, jsonArray1, false);
        binding.rvCampaignList.setAdapter(adapter);

        campaignList();
    }

    public void onNotificationClick(View v){
    }

    private void campaignList() {
        int vendorclientorcampaign= 1;

        Log.d("tg5","fin");

        //animation code
        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(rotateAnimation);
        //animation code

        Log.d("tg99", Integer.toString(clientId)+ vendorclientorcampaign);

        APIreferenceclass api= new APIreferenceclass(clientId, vendorclientorcampaign, loginToken, this);
    }

    public void onPlusClick(View view) {

        Log.d("tag20", "here");

        Intent intent = new Intent(ClientDashFirstPage.this, AddCampaignDetails.class);
        intent.putExtra("logintoken", loginToken);
        startActivity(intent);
    }

    @Override
    public void onResponseReceived(String response){
        Log.d("cldbatest","response is "+ response);
        implementUi(response);
    }

    JSONArray jsonArray1;
    JSONArray jsonArray2;//client array
    JSONArray jsonArray3;//vendor array

    private void implementUi(String response){
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObject1= new JSONObject();
            JSONObject jsonObject2= new JSONObject();
            jsonArray1= new JSONArray();
            jsonArray2= new JSONArray();
            jsonArray3= new JSONArray();

            JSONObject jsonResponse = new JSONObject(response);
            if(jsonResponse.getString("status").equals("success")) {

                if(liveold== 0){
                JSONArray dataArray = jsonResponse.getJSONArray("live_campaigns");
                if(dataArray != null && dataArray.length() > 0) {
                    // if(vendorclientorcampaign==0){

                    for(int i=0; i< dataArray.length();i++){

                        JSONObject dataObject = dataArray.getJSONObject(i);
                        if(dataObject != null) {
                            jsonObject = new JSONObject();
                            Log.d("DataObjectContent", "Data Object: " + dataObject.toString());

                            jsonObject.putOpt("id", dataObject.optInt("id"));
                            jsonObject.putOpt("uid", dataObject.optString("uid"));
                            jsonObject.putOpt("image", dataObject.optString("logo"));
                            jsonObject.putOpt("name", dataObject.optString("name"));

                            jsonArray1.put(jsonObject);
                        }
                    }
                    Log.d("JSONArrayContent", "JSONArray1: " + jsonArray1.toString());
                }
                }else{

                    JSONArray dataArray = jsonResponse.getJSONArray("old_campaigns");
                    if(dataArray != null && dataArray.length() > 0) {

                        for(int i=0; i< dataArray.length();i++){

                            JSONObject dataObject = dataArray.getJSONObject(i);
                            if(dataObject != null) {
                                jsonObject = new JSONObject();
                                Log.d("DataObjectContent", "Data Object: " + dataObject.toString());

                                jsonObject.putOpt("id", dataObject.optInt("id"));
                                jsonObject.putOpt("uid", dataObject.optString("uid"));
                                jsonObject.putOpt("image", dataObject.optString("logo"));
                                jsonObject.putOpt("name", dataObject.optString("name"));

                                jsonArray1.put(jsonObject);
                            }
                        }
                        Log.d("JSONArrayContent", "JSONArray1: " + jsonArray1.toString());
                    }
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    GridLayoutManager layoutManager = new GridLayoutManager(ctxt, 2);
                    binding.rvCampaignList.setLayoutManager(layoutManager);
                    CampaignListAdapter adapter = new CampaignListAdapter(ctxt, jsonArray1, false);

                    //animation code
                    progressBar.clearAnimation();
                    progressBar.setVisibility(View.GONE);
                    //animation code

                    binding.rvCampaignList.setAdapter(adapter);

                    //updating header

                    try {
                        String live_campaigns_count= jsonResponse.getString("live_campaigns_count");
                        String old_campaigns_count= jsonResponse.getString("old_campaigns__count");
                        String client_name= jsonResponse.getString("client_name");

                        binding.title.setText(client_name);
                        binding.clientid.setText(":- "+Integer.toString(clientId));
                        binding.clientname.setText(":- "+client_name);
                        String formattedText = ":- Live :- " + live_campaigns_count + " - Old :- " + old_campaigns_count;
                        binding.campaign.setText(getColoredText(formattedText, Integer.parseInt(old_campaigns_count)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }});
        }catch (Exception e){
            Log.d("tag40",e.toString());
        }
    }

    private SpannableString getColoredText(String text, int oldCount) {
        SpannableString spannableString = new SpannableString(text);

        // Set the color for the "Old :-" part
        ForegroundColorSpan oldColorSpan = new ForegroundColorSpan(Color.RED);
        spannableString.setSpan(oldColorSpan, text.indexOf("Old :-"), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }


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

            int vendorclientorcampaign= 1;

            Log.d("tag122", id);

            // Start new activity and pass the retrieved data
            startActivity(new Intent(this, ViewCampaignSitesClientDash.class)
                    .putExtra("campaignType", "old")
                    .putExtra("id", id)
                    .putExtra("vendorclientorcampaign", vendorclientorcampaign)
                    .putExtra("logintoken", loginToken)
                    .putExtra("apiresponse", jsonObject.toString()));

        } catch (JSONException e) {
            Log.d("tag123", e.toString());
            }
    }

    public void liveCampaignClick(View v){
        clearUi();
        liveold=0;

        binding.tvOldCampaign.setBackgroundResource(0);
        binding.tvOldCampaign.setTextColor(R.color.colorPrimaryDark);

        binding.tvLiveCampaign.setBackgroundResource(R.drawable.primaryround);
        binding.tvLiveCampaign.setTextColor(Color.WHITE);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(rotateAnimation);

        ImageView iv= findViewById(R.id.ivPlus);
        iv.setVisibility(View.VISIBLE);


        campaignList();
    }

    public void oldCampaignClick(View v){
        clearUi();
        liveold=1;

        binding.tvOldCampaign.setBackgroundResource(R.drawable.primaryround);
        binding.tvOldCampaign.setTextColor(Color.WHITE);

        binding.tvLiveCampaign.setBackgroundResource(0);
        binding.tvLiveCampaign.setTextColor(R.color.colorPrimaryDark);

        ImageView iv= findViewById(R.id.ivPlus);
        iv.setVisibility(View.GONE);

        campaignList();
    }

    private void clearUi() {
        // Clear the RecyclerView
        if (binding.rvCampaignList.getAdapter() != null) {
            CampaignListAdapter adapter = (CampaignListAdapter) binding.rvCampaignList.getAdapter();
            adapter.clearData();
        }
    }
}