package com.acme.oohvendor.activity.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.acme.oohvendor.R;
import com.acme.oohvendor.adapters.CampaignListAdapter;
import com.acme.oohvendor.databinding.ActivityMainBinding;
import com.acme.oohvendor.databinding.ActivityViewCampaignSitesBinding;
import com.acme.oohvendor.databinding.ActivityViewCampaignSitesClientDashBinding;
import com.acme.oohvendor.viewmodel.APIreferenceclass;
import com.acme.oohvendor.viewmodel.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewCampaignSitesClientDash extends AppCompatActivity implements ApiInterface {

    int id=0;
    ActivityViewCampaignSitesClientDashBinding binding;
    boolean showMenus = false;
    int delete= 0;
    private final Context ctxt= this;
    int vendorclientorcampaign=0; //campaign is 0, client is 1, vendor is 2
    String logintoken="";
    String idofcampaign;
    String[] idarray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_campaign_sites_client_dash);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.rvCampaignList.setLayoutManager(layoutManager);
        Log.d("whichclass", "ViewCampaignSitesClientDash");

        //animation code
        progressBar= findViewById(R.id.progressBar);
        rotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_animation);
        //animation code

        FileHelper fh= new FileHelper();
        logintoken= fh.readLoginToken(this);

        idofcampaign=getIntent().getStringExtra("id");
        Log.d("tag58", idofcampaign);

        //TODO remove after adding to ui
        jsonArray1= new JSONArray();
        CampaignListAdapter adapter = new CampaignListAdapter(this, jsonArray1, true);
        binding.rvCampaignList.setAdapter(adapter);
        campaignList(idofcampaign);
    }

    //onresponsereceived from api
    public void onResponseReceived(String response){

        Log.d("addbatest", "response is "+ response);
        Log.d("tag58","got response");

        if(delete==0) {
            implementUi(response);
        }else if(delete==1){
            delete= 0;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Site deleted successfully", Toast.LENGTH_SHORT).show();
                    campaignList(idofcampaign);
                }
            });
        }
    }

    String vendorid;
    JSONArray jsonArray1;
    private void implementUi(String response){
        try {
            JSONObject jsonObject = new JSONObject();
            //jsonArray1= new JSONArray();

            String ids[];
            JSONObject jsonResponse = new JSONObject(response);
            if(jsonResponse.getString("status").equals("success")) {
                JSONArray dataArray = jsonResponse.getJSONArray("sites");

                idarray= new String[dataArray.length()];

                if(dataArray != null && dataArray.length() > 0) {
                    if(vendorclientorcampaign==0){
                        //TODO here. have to filter based on whether this class is called from admin dash or client dash or vendor dash. Right now only 0 being called

                        Log.d("tag998", "1");
                        for(int i=0; i< dataArray.length();i++){

                            JSONObject dataObject = dataArray.getJSONObject(i);
                            if(dataObject != null) {
                                jsonObject = new JSONObject();
                                Log.d("DataObjectContent", "Data Object: " + dataObject.toString());
                                idarray[i]= Integer.toString(dataObject.optInt("id"));
                                vendorid= dataObject.optString("vendor_id");
                                //AdminCrudDataClass siteDetail = new AdminCrudDataClass();
                                jsonObject.putOpt("id", dataObject.optInt("id"));
                                jsonObject.putOpt("uid", dataObject.optString("uid"));
                                jsonObject.putOpt("image", dataObject.optString("image"));
                                jsonObject.putOpt("name", dataObject.optString("name"));
                                //siteDetail.setName(dataObject.optString("name"));

                                jsonArray1.put(jsonObject);
//TODO here
                            }
                        }
                    }else if(vendorclientorcampaign==1){

                        Log.d("tag998", "2");
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
                                jsonArray1.put(jsonObject);
//TODO here
                            }
                        }

                    }else if(vendorclientorcampaign==2){

                        Log.d("tag998", "3");
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

//TODO here
                                jsonArray1.put(jsonObject);
                            }
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
                    CampaignListAdapter adapter = new CampaignListAdapter(ctxt, jsonArray1, true);

                    //animation code
                    progressBar.clearAnimation();
                    progressBar.setVisibility(View.GONE);
                    //animation code
                try{
                    binding.clientid.setText(jsonResponse.getString("client_id"));
                    binding.clientname.setText(jsonResponse.getString("client_name"));
                    binding.campaign.setText(jsonResponse.getString("campaign_name"));
                    binding.title.setText(jsonResponse.getString("campaign_name"));
                    binding.totalsites.setText(jsonResponse.getString("site_count"));

                }catch (Exception e){
                    Log.d("tag222", e.toString());
                }
                    binding.rvCampaignList.setAdapter(adapter);
                }});
        }catch (Exception e){}
    }

    ProgressBar progressBar;
    Animation rotateAnimation;

    private void campaignList(String id) {
        vendorclientorcampaign=0;
        //TODO pass correct logintoken here

        //animation code
        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(rotateAnimation);
        //view.setVisibility(View.VISIBLE);
        //animation code
        APIreferenceclass api= new APIreferenceclass(logintoken, this, id);
    }

    public void onItemClick(int position) {
        try {
            Log.d("tag51", Integer.toString(position));
            // Retrieve JSONObject from your jsonArray at position
            JSONObject jsonObject = jsonArray1.getJSONObject(position);
            Log.d("tag51", jsonArray1.getJSONObject(position).toString());

            //TODO add correct login token here
            //="Bearer 322|7Dor2CuPXz4orJV5GUleBAUcmgYnbswVMLQ5EUNM";

            // Get site id or site no from the JSONObject
            String id = jsonObject.getString("id"); // Or get an id if you have that
            Log.d("tag51", jsonObject.getString("id"));

            // String siteId = jsonObject.getString("siteId"); // If you have a site id.

            // Start new activity and pass the retrieved data
            startActivity(new Intent(this, ViewSiteDetailActivityClientDash.class)
                    .putExtra("campaignType", "old")
                    .putExtra("campaignId", idofcampaign)
                    .putExtra("siteNumber", id)
                    .putExtra("logintoken", logintoken)
                    .putExtra("idarray", idarray)
                    .putExtra("vendorclientorcampaign", vendorclientorcampaign));

            // .putExtra("siteId", siteId)); // If you are passing site id
        } catch (JSONException e) {
            Log.d("tag123", e.toString());
            // Handle exception (e.g. show a Toast to the user indicating an error)
        }
    }

    public void onEditClick(int position, View view){
        //add code to dropdown a list which says delete
        //delete code
        // Context should be your activity or fragment context
        Log.d("tg98", Integer.toString(position));
        PopupMenu popup = new PopupMenu(this, view); // 'view' is the anchor view for popup menu
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu()); // Inflate your menu resource

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.delete) {
                    // Handle delete action
                    JSONObject campaignItem= null;
                    int siteid= 0;
                    if(vendorclientorcampaign== 0) {
                        try {
                            RecyclerView recyclerView = findViewById(R.id.rvCampaignList);
                            CampaignListAdapter campaignAdapter = (CampaignListAdapter) recyclerView.getAdapter();
                            campaignItem = campaignAdapter.jsonArray.getJSONObject(position);
                            siteid= campaignItem.getInt("id");

                            Log.d("tg93", Integer.toString(siteid));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        APIreferenceclass api = new APIreferenceclass(siteid, logintoken, ctxt, 1);
                        delete = 1;
                        return true;
                    }
                }

                return false;
            }});
        popup.show(); // Show the popup menu

    }

    public void onPlusClick(View view) {
        Log.d("tag20", "onplugtreehththtsclick");
        Intent intent= new Intent(ViewCampaignSitesClientDash.this, AddSiteDetailActivity.class);
        intent.putExtra("campaignId",idofcampaign);
        intent.putExtra("vendorId",vendorid);
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

    public void onDeleteClientClick(View view) {
        // ... your logic ...
    }
}