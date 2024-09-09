package com.acme.afsvendor.activity.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.acme.afsvendor.adapters.CampaignListAdapter;
import com.acme.afsvendor.databinding.ActivityViewCampaignSitesBinding;
import com.acme.afsvendor.viewmodel.APIreferenceclass;
import com.acme.afsvendor.viewmodel.ApiInterface;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acme.afsvendor.databinding.ActivityAdminDashVendorSitesBinding;

import com.acme.afsvendor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class AdminDashVendorSites extends AppCompatActivity implements ApiInterface{
        private AppBarConfiguration appBarConfiguration;
    private ActivityAdminDashVendorSitesBinding binding;

    boolean lastsitecall;
        int id=0;
        //JSONArray jsonArray;
        JSONArray sitearray;

    boolean showMenus = false;
        int delete= 0;

        private final Context ctxt= this;
        int vendorclientorcampaign=0; //campaign is 0, client is 1, vendor is 2
        String logintoken="";

        String idofvendor;
        boolean showedit;
        boolean gettingcampaignids;

        String[] idarray;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_dash_vendor_sites);
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            binding.rvCampaignList.setLayoutManager(layoutManager);
            Log.d("whichclass", "AdminDashVendorSites");
            gettingcampaignids= false;
            lastsitecall= false;
            vendorname= "";
            campaignid= "";
            sitearray= new JSONArray();
            showedit= true;
            sitesretrieved= false;


            //animation code
            progressBar= findViewById(R.id.progressBar);
            rotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_animation);
            //animation code

            FileHelper fh= new FileHelper();
            logintoken= fh.readLoginToken(this);

            idofvendor=getIntent().getStringExtra("id");
            Log.d("tag58", idofvendor);

            try{
                if(getIntent().getStringExtra("camefrom").equals("ClientDashBoardActivity")){
                    binding.ivPlus.setVisibility(View.GONE);
                    showedit= false;
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            jsonArray1= new JSONArray();
            CampaignListAdapter adapter = new CampaignListAdapter(this, jsonArray1, showedit);
            binding.rvCampaignList.setAdapter(adapter);
            //campaignList(idofcampaign);

            //animation code
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(rotateAnimation);
            //view.setVisibility(View.VISIBLE);
            //animation code

            getvendorcampaigns(idofvendor);
        }

        void getvendorcampaigns(String id){
            gettingcampaignids= true;

            Log.d("tag222", "1");
            APIreferenceclass api= new APIreferenceclass(logintoken, this, id, true);

        }

        String vendorname;

        boolean sitesretrieved;
        //onresponsereceived from api


        public void onResponseReceived(String response){

            Log.d("addbatest", "response is "+ response);
            Log.d("tag58","got response");

            if(delete==0&&!gettingcampaignids&sitesretrieved) {
                try{
                    JSONObject jsonobj= new JSONObject(response);
                    vendorname= jsonobj.getString("vendor_name");
                }catch(Exception e){
                    e.printStackTrace();
                }

                implementUInew(sitearray);
            }else if(delete==1){
                delete= 0;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Site deleted successfully", Toast.LENGTH_SHORT).show();
                        //campaignList(idofcampaign);
                    }
                });
            }
            else if(gettingcampaignids){
                gettingcampaignids= false;
                extractcampaignids(response);
            }else if(delete==0&&!gettingcampaignids&!sitesretrieved){
                Log.d("sites", response);

                try {
                    JSONObject jsonobj = new JSONObject(response);
                    JSONArray jsonar= jsonobj.getJSONArray("sites");
                    for(int i=0; i<jsonar.length(); i++){

                        sitearray.put(jsonar.getJSONObject(i));
                    }


                }catch(Exception e){
                    Log.d("siteserror", e.toString()
                    );
                }
                if (lastsitecall)
                {
                    sitesretrieved= true;
                    Log.d("sites", "lastsitecall");

                    for(int i=0; i<sitearray.length();i++) {
                        try {
                            Log.d("sitesarray", sitearray.getJSONObject(i).toString()+ Integer.toString(sitearray.length()));
                        }catch(Exception e){
                            Log.d("tag22", e.toString());                        }
                        }

                    implementUInew(sitearray);
                }
            }
        }

    private void implementUInew(JSONArray sitearray){

            JSONObject jsonObject = new JSONObject();
            jsonArray1 = new JSONArray();
            String ids[];
            //JSONObject jsonResponse = new JSONObject(response);
            //if(jsonResponse.getString("status").equals("success")) {
            JSONArray dataArray = sitearray;
            idarray= new String[dataArray.length()];

            if (dataArray != null && dataArray.length() > 0) {
                try {
                Log.d("tag998", "1");
                for (int i = 0; i < dataArray.length(); i++) {

                        JSONObject dataObject = dataArray.getJSONObject(i);
                        if (dataObject != null) {
                            jsonObject = new JSONObject();
                            Log.d("DataObjectContent", "Data Object: " + dataObject.toString());

                            vendorid = dataObject.optString("vendor_id");
                            //AdminCrudDataClass siteDetail = new AdminCrudDataClass();

                            idarray[i]= Integer.toString(dataObject.optInt("id"));

                            jsonObject.putOpt("id", dataObject.optInt("id"));
                            jsonObject.putOpt("uid", dataObject.optString("uid"));
                            jsonObject.putOpt("image", dataObject.optString("image"));
                            jsonObject.putOpt("name", dataObject.optString("name"));
                            //siteDetail.setName(dataObject.optString("name"));


                            jsonArray1.put(jsonObject);
//TODO here
                        }
                    }}catch(Exception e) {
                    e.printStackTrace();
                }
                }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Your UI update code goes here

                    GridLayoutManager layoutManager = new GridLayoutManager(ctxt, 2);
                    binding.rvCampaignList.setLayoutManager(layoutManager);
                    CampaignListAdapter adapter = new CampaignListAdapter(ctxt, jsonArray1, showedit);
                    binding.title.setText("Sites for "+ vendorname);

                    //animation code
                    progressBar.clearAnimation();
                    progressBar.setVisibility(View.GONE);
                    //animation code

                    binding.rvCampaignList.setAdapter(adapter);

              /*      try {
                        JSONObject jsonobject = new JSONObject(response);
                        binding.title.setText(jsonobject.getString("campaign_name"));
                        binding.clientid.setText(Integer.toString(jsonobject.getInt("client_id")));
                        binding.clientname.setText(jsonobject.getString("client_name"));
                        binding.campaign.setText(jsonobject.getString("campaign_name"));
                        binding.totalsites.setText(jsonobject.getString("site_count"));
                    }catch(Exception e){
                        Log.d("tag322121", e.toString());
                    }*/
                }});

           }


    void extractcampaignids(String response){
            Log.d("extractcampaignidsresponse", response);

            JSONArray jsonArray;
            JSONObject jsonobj1;

            String campaignids= "";
            try {
                JSONObject jsonobj = new JSONObject(response);
                if(jsonobj.getString("status").equals("success")){
                    if(!jsonobj.getString("live_campaigns_count").equals("0")){

                        jsonArray= jsonobj.getJSONArray("live_campaigns");
                        for(int i=0; i< jsonobj.getInt("live_campaigns_count"); i++){
                            jsonobj1= jsonArray.getJSONObject(i);

                            campaignids= campaignids+ jsonobj1.getInt("id")+",";
                        }
                    }
                    if(!jsonobj.getString("old_campaigns__count").equals("0")){

                        jsonArray= jsonobj.getJSONArray("old_campaigns");
                        for(int i=0; i< jsonobj.getInt("old_campaigns__count"); i++){
                            jsonobj1= jsonArray.getJSONObject(i);

                            campaignids= campaignids+ jsonobj1.getInt("id")+",";
                        }
                    }

                    Log.d("campaignids", campaignids);
                    getsitesforcampaigns(campaignids);
                }

            }catch(Exception e){
                Log.d("error", e.toString());
            }

            }


    void getsitesforcampaigns(String campaignids1){

        String campaignIdsString = campaignids1;
        String[] campaignIds = campaignIdsString.split(",");
        String url1="";

        for(int i=0; i< campaignIds.length; i++){
            Log.d("sites", campaignIds[i]);
        }

        for(int i=0; i<=campaignIds.length; i++){

            url1= "https://acme.warburttons.com/api/get_vendor_sites/"+campaignIds[i]+"/"+idofvendor;
            Log.d("sitesloop", Integer.toString(i)+ campaignIds.length);
            if(i== campaignIds.length-1){
                lastsitecall= true;
            }
            Log.d("tag222", "2");
            APIreferenceclass api= new APIreferenceclass(logintoken, this, url1, true, true);
            Log.d("sites", "one loop"+ i+campaignIds.length);

            try {
                TimeUnit.MILLISECONDS.sleep(100); // Delay of 0.1 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

        String vendorid;
        JSONArray jsonArray1;
        private void implementUi(String response){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonArray1= new JSONArray();

                String ids[];
                JSONObject jsonResponse = new JSONObject(response);
                if(jsonResponse.getString("status").equals("success")) {
                    JSONArray dataArray = jsonResponse.getJSONArray("sites");

                    if(dataArray != null && dataArray.length() > 0) {
                        if(vendorclientorcampaign==0){

                            //TODO here. have to filter based on whether this class is called from admin dash or client dash or vendor dash. Right now only 0 being called

                            Log.d("tag998", "1");
                            for(int i=0; i< dataArray.length();i++){

                                JSONObject dataObject = dataArray.getJSONObject(i);
                                if(dataObject != null) {
                                    jsonObject = new JSONObject();
                                    Log.d("DataObjectContent", "Data Object: " + dataObject.toString());

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
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Your UI update code goes here
                            //animation code
                            progressBar.clearAnimation();
                            progressBar.setVisibility(View.GONE);
                            //animation code

                            Toast.makeText(getApplicationContext(), "No sites.", Toast.LENGTH_SHORT).show();
                        }});}

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Your UI update code goes here

                        GridLayoutManager layoutManager = new GridLayoutManager(ctxt, 2);
                        binding.rvCampaignList.setLayoutManager(layoutManager);
                        CampaignListAdapter adapter = new CampaignListAdapter(ctxt, jsonArray1, showedit);

                        //animation code
                        progressBar.clearAnimation();
                        progressBar.setVisibility(View.GONE);
                        //animation code

                        binding.rvCampaignList.setAdapter(adapter);

                       /* try {
                            JSONObject jsonobject = new JSONObject(response);
                            binding.title.setText(jsonobject.getString("campaign_name"));
                            binding.clientid.setText(Integer.toString(jsonobject.getInt("client_id")));
                            binding.clientname.setText(jsonobject.getString("client_name"));
                            binding.campaign.setText(jsonobject.getString("campaign_name"));
                            binding.totalsites.setText(jsonobject.getString("site_count"));
                        }catch(Exception e){
                            Log.d("tag322121", e.toString());
                        }*/
                    }});
            }catch (Exception e){
                Log.d("tg222", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Your UI update code goes here
                        //animation code
                        progressBar.clearAnimation();
                        progressBar.setVisibility(View.GONE);
                        //animation code

                        Toast.makeText(getApplicationContext(), "No sites.", Toast.LENGTH_SHORT).show();
                    }});}}


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
            Log.d("tag222", "3");
            APIreferenceclass api= new APIreferenceclass(logintoken, this, id);
        }

        String campaignid;

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

                String camefrom= "";
                try{
                    camefrom= getIntent().getStringExtra("camefrom");
                    camefrom= "admindashvendorsites";
                }catch (Exception e){
                    e.printStackTrace();
                }

                // Start new activity and pass the retrieved data
                startActivity(new Intent(this, ViewSiteDetailActivity.class)
                        .putExtra("campaignType", "old")
                        //.putExtra("campaignId", idofcampaign)
                        .putExtra("camefrom", camefrom)
                        .putExtra("siteNumber", id)
                        .putExtra("logintoken", logintoken)
                        .putExtra("vendorclientorcampaign", vendorclientorcampaign)
                        .putExtra("idarray", idarray));

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


                            Log.d("tag222", "4");
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
            Intent intent= new Intent(com.acme.afsvendor.activity.dashboard.AdminDashVendorSites.this, AddSiteDetailActivity.class);
            //intent.putExtra("campaignId",idofcampaign);
            Log.d("vendorid", vendorid);
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


