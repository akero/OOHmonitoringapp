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
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import com.acme.afsvendor.R;
import com.acme.afsvendor.adapters.CampaignListAdapter;
import com.acme.afsvendor.databinding.ActivityClientDashBoardBinding;
import com.acme.afsvendor.viewmodel.APIreferenceclass;
import com.acme.afsvendor.viewmodel.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClientDashBoardActivity extends AppCompatActivity implements ApiInterface {

    private ActivityClientDashBoardBinding binding;
    private boolean oldcampaign = true;

    String loginToken="";

    Context ctxt;

    ProgressBar progressBar;
    Animation rotateAnimation;
    int clientid;
    int live;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_client_dash_board);

        Log.d("tag199", "1");
        Log.d("whichclass", "ClientDashBoardActivity");

        clientid= 0;
        live= 1;

        //animation code
        progressBar= findViewById(R.id.progressBar);
        rotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_animation);
        //animation code

        ctxt= this;

        loginToken= FileHelper.readLoginToken(this);
        Log.d("tg4", loginToken);

        try{
            clientid= getIntent().getIntExtra("clientid" , 0);
        }catch (Exception e){
            e.printStackTrace();
        }

        CampaignListAdapter adapter = new CampaignListAdapter(this, jsonArray1, false);
        binding.rvCampaignList.setAdapter(adapter);
        //TODO remove after adding to ui
       // jsonArray1= new JSONArray();
       // jsonArray2=new JSONArray();
       // jsonArray3= new JSONArray();


      /*
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.rvCampaignList.setLayoutManager(layoutManager);

        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jsonObjectairbnb = new JSONObject();
            jsonObjectairbnb.put("sitenumber", "001");
            jsonObjectairbnb.put("unitnumber", "#887001");
            jsonArray.put(jsonObjectairbnb);

            JSONObject jsonObjecthyundai = new JSONObject();
            jsonObjecthyundai.put("sitenumber", "002");
            jsonObjecthyundai.put("unitnumber", "#878002");
            jsonArray.put(jsonObjecthyundai);

            JSONObject jsonObjectford = new JSONObject();
            jsonObjectford.put("sitenumber", "003");
            jsonObjectford.put("unitnumber", "#765003");
            jsonArray.put(jsonObjectford);

            JSONObject jsonObjectpatanjli = new JSONObject();
            jsonObjectpatanjli.put("sitenumber", "004");
            jsonObjectpatanjli.put("unitnumber", "#432004");
            jsonArray.put(jsonObjectpatanjli);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CampaignListAdapter adapter = new CampaignListAdapter(this, jsonArray);
        binding.rvCampaignList.setAdapter(adapter);
*/
        campaignList();
    }

    public void onNotificationClick(View v){
        //TODO ask lodu what this does
    }
    //String logintoken="534|ehyJudmoAsTjmkbTLBcIjzUOCFIui40OSBL01JJJ";
    private void campaignList() {
        int vendorclientorcampaign= 1;

        Log.d("tg5","fin");

        //animation code
        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(rotateAnimation);
        //view.setVisibility(View.VISIBLE);
        //animation code

        APIreferenceclass api= new APIreferenceclass(vendorclientorcampaign, loginToken, this, clientid, 2);
    }

    public void onPlusClick(View view) {

        Log.d("tag20", "here");

            Intent intent = new Intent(ClientDashBoardActivity.this, AddCampaignDetails.class);
            intent.putExtra("logintoken", loginToken);
            startActivity(intent);
       }

    //TODO add token to future activity
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
                String ids[];
                JSONObject jsonResponse = new JSONObject(response);
                if(jsonResponse.getString("status").equals("success")) {

                    if(live== 1){
                    JSONArray dataArray = jsonResponse.getJSONArray("live_campaigns");
                    if(dataArray != null && dataArray.length() > 0) {
                       // if(vendorclientorcampaign==0){

                            for(int i=0; i< dataArray.length();i++){

                                JSONObject dataObject = dataArray.getJSONObject(i);
                                if(dataObject != null) {
                                    jsonObject = new JSONObject();
                                    Log.d("DataObjectContent", "Data Object: " + dataObject.toString());
                                    //AdminCrudDataClass siteDetail = new AdminCrudDataClass();
                                    jsonObject.putOpt("id", dataObject.optInt("id"));
                                    jsonObject.putOpt("uid", dataObject.optString("uid"));
                                    jsonObject.putOpt("image", dataObject.optString("logo"));
                                    jsonObject.putOpt("name", dataObject.optString("name"));

                                    //siteDetail.setName(dataObject.optString("name"));

                                    // Inside the for-loop where you process each `dataObject`
                                    //String imageUrl = dataObject.optString("image");
                                    //jsonObject.putOpt("image", imageUrl);

                                    jsonArray1.put(jsonObject);
//TODO here
                                }
                            }
                       // }
                        Log.d("JSONArrayContent", "JSONArray1: " + jsonArray1.toString());
                    }
                }else{
                        JSONArray dataArray = jsonResponse.getJSONArray("old_campaigns");
                        if(dataArray != null && dataArray.length() > 0) {
                            // if(vendorclientorcampaign==0){

                            for(int i=0; i< dataArray.length();i++){

                                JSONObject dataObject = dataArray.getJSONObject(i);
                                if(dataObject != null) {
                                    jsonObject = new JSONObject();
                                    Log.d("DataObjectContent", "Data Object: " + dataObject.toString());
                                    //AdminCrudDataClass siteDetail = new AdminCrudDataClass();
                                    jsonObject.putOpt("id", dataObject.optInt("id"));
                                    jsonObject.putOpt("uid", dataObject.optString("uid"));
                                    jsonObject.putOpt("image", dataObject.optString("logo"));
                                    jsonObject.putOpt("name", dataObject.optString("name"));

                                    //siteDetail.setName(dataObject.optString("name"));

                                    // Inside the for-loop where you process each `dataObject`
                                    //String imageUrl = dataObject.optString("image");
                                    //jsonObject.putOpt("image", imageUrl);

                                    jsonArray1.put(jsonObject);
//TODO here
                                }
                            }
                            // }
                            Log.d("JSONArrayContent", "JSONArray1: " + jsonArray1.toString());
                        }


                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Your UI update code goes here

                        GridLayoutManager layoutManager = new GridLayoutManager(ctxt, 2);
                        binding.rvCampaignList.setLayoutManager(layoutManager);
                        CampaignListAdapter adapter = new CampaignListAdapter(ctxt, jsonArray1, false);

                        //animation code
                        progressBar.clearAnimation();
                        progressBar.setVisibility(View.GONE);
                        //animation code

                        binding.clientid.setText(Integer.toString(clientid));
                        try {

                            String live_campaigns_count= jsonResponse.getString("live_campaigns_count");
                            String old_campaigns_count= jsonResponse.getString("old_campaigns__count");
                            binding.clientname.setText(jsonResponse.getString("client_name"));
                            binding.title.setText(jsonResponse.getString("client_name"));
                            String formattedText = "Live :- " + live_campaigns_count + " - Old :- " + old_campaigns_count;
                            binding.campaign.setText(getColoredText(formattedText, Integer.parseInt(old_campaigns_count)));                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        binding.rvCampaignList.setAdapter(adapter);
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

        //TODO replace. this is for response for the current page's data.
        //TODO implement response into UI
        String campaignType="";
        int position= 0;

        /*Intent intent= new Intent(VenderDashBoardActivity.this, UpdateSiteDetailActivity.class);
        intent.putExtra("campaigntype", campaignType);
        intent.putExtra("position", position);
        intent.putExtra("logintoken", loginToken);
        startActivity( intent);
    */

    public void oldCampaignClick(View v){
        live= 0;
        clearUi();

        binding.tvLiveCampaign.setBackgroundResource(0);
        binding.tvLiveCampaign.setTextColor(R.color.colorPrimaryDark);

        binding.tvOldCampaign.setBackgroundResource(R.drawable.primaryround);
        binding.tvOldCampaign.setTextColor(Color.WHITE);


        //animation code
        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(rotateAnimation);
        //view.setVisibility(View.VISIBLE);
        //animation code

        APIreferenceclass api= new APIreferenceclass(1, loginToken, this, clientid, 2);
    }

    public void liveCampaignClick(View v){
        live= 1;
        clearUi();

        binding.tvOldCampaign.setBackgroundResource(0);
        binding.tvOldCampaign.setTextColor(R.color.colorPrimaryDark);

        binding.tvLiveCampaign.setBackgroundResource(R.drawable.primaryround);
        binding.tvLiveCampaign.setTextColor(Color.WHITE);

        //animation code
        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(rotateAnimation);
        //view.setVisibility(View.VISIBLE);
        //animation code

        APIreferenceclass api= new APIreferenceclass(1, loginToken, this, clientid, 2);
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
                Log.d("tag60", jsonObject.toString());

                // String siteId = jsonObject.getString("siteId"); // If you have a site id.
                int vendorclientorcampaign= 1;

                Log.d("tag122", id);

                    // Start new activity and pass the retrieved data
                    startActivity(new Intent(this, ViewCampaignSites.class)
                            .putExtra("campaignType", "old")
                            .putExtra("camefrom", "ClientDashBoardActivity")
                            .putExtra("id", id)
                            .putExtra("vendorclientorcampaign", vendorclientorcampaign)
                            .putExtra("logintoken", loginToken)
                            .putExtra("apiresponse", jsonObject.toString()));

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



            /*if (vendorclientorcampaign == 0) {
                jsonArray1 = new JSONArray();
            } else if (vendorclientorcampaign == 1) {
                jsonArray2 = new JSONArray();
            } else if(vendorclientorcampaign == 2){
                jsonArray3= new JSONArray();
            }*/
        }
        // Reset any other UI elements here as needed
    }

    }
