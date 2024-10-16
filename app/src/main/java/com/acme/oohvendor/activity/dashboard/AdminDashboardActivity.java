package com.acme.oohvendor.activity.dashboard;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acme.oohvendor.R;
import com.acme.oohvendor.activity.login.OTP;
import com.acme.oohvendor.adapters.CampaignListAdapter;
import com.acme.oohvendor.databinding.ActivityMainBinding;
import com.acme.oohvendor.viewmodel.APIreferenceclass;
import com.acme.oohvendor.viewmodel.ApiInterface;
import com.google.android.gms.common.api.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminDashboardActivity extends AppCompatActivity implements ApiInterface {
    ActivityMainBinding binding;
    boolean showMenus = false;
    private final Context ctxt= this;
    int vendorclientorcampaign=0; //campaign is 0, client is 1, vendor is 2
    String logintoken="";
    int delete= 0;
    JSONArray jsonArray1;
    JSONArray jsonArray2;//client array
    JSONArray jsonArray3;//vendor array
    ProgressBar progressBar;
    Animation rotateAnimation;
    String userid;
    boolean statespinnerselected;

    //todo access token save to memory add to api call
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.rvCampaignList.setLayoutManager(layoutManager);
        Log.d("whichclass", "AdminDashboardActivity");
        Log.d("tg5","2");
        userid="";
        statespinnerselected= false;
        populatevendorspinner= false;
        fetchsitesaccordingtovendor= false;

        logintoken = FileHelper.readLoginToken(this);
        userid= FileHelper.readUserId(this);
        Log.d("tg4", logintoken);

        try{

            String name= getIntent().getStringExtra("name");
            binding.title.setText(binding.title.getText()+ " -\n"+ name);

        }catch (Exception e){
            e.printStackTrace();
        }

        //animation code
        progressBar= findViewById(R.id.progressBar);
        rotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_animation);
        //animation code

        Log.d("tg5","3");
        //TODO remove after adding to ui
        jsonArray1= new JSONArray();
        jsonArray2=new JSONArray();
        jsonArray3= new JSONArray();
        CampaignListAdapter adapter = new CampaignListAdapter(this, jsonArray1, false);
        binding.rvCampaignList.setAdapter(adapter);
        Log.d("tg5","4");

        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(rotateAnimation);

        View view= null;
        btnCompaignClick(view);
    }

    //onresponsereceived from api
    public void onResponseReceived(String response){
        Log.d("addbatest", "response is "+ response);
        Log.d("tag21","5");
        if(delete== 0&& !statespinnerselected) {
            implementUi(response);
        }else if(delete== 1 && vendorclientorcampaign== 0){
            delete= 0;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Campaign deleted successfully", Toast.LENGTH_SHORT).show();
                    campaignList();

                }
            });

        }else if(delete== 1 && vendorclientorcampaign== 1){
            delete= 0;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Client deleted successfully", Toast.LENGTH_SHORT).show();
                    clientList();

                }
            });

        }else if(delete== 1 && vendorclientorcampaign== 2){
            delete= 0;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Vendor deleted successfully", Toast.LENGTH_SHORT).show();
                    venderList();

                }
            });

        }else if(delete== 0&& statespinnerselected) {
            implementUi(response);
            statespinnerselected= false;
        }else if(populatevendorspinner){
            populatevendorspinner= false;
            Log.d("vendr",response);
            populatevendorspinner1(response);
        }else if(fetchsitesaccordingtovendor){
            fetchsitesaccordingtovendor= false;
            implementUi(response);
        }
    }

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
            if(jsonResponse.getInt("status")== 200) {
                JSONArray dataArray = jsonResponse.getJSONArray("data");
                if(dataArray != null && dataArray.length() > 0) {
                    if(vendorclientorcampaign==0){//cam

                    for(int i=0; i< dataArray.length();i++){

                    JSONObject dataObject = dataArray.getJSONObject(i);
                    if(dataObject != null) {
                        jsonObject = new JSONObject();
                        Log.d("DataObjectContent", "Data Object: " + dataObject.toString());
                        //AdminCrudDataClass siteDetail = new AdminCrudDataClass();
                        jsonObject.putOpt("id", dataObject.optInt("id"));
                        jsonObject.putOpt("uid", dataObject.optString("uid"));
                        jsonObject.putOpt("image", dataObject.optString("image"));
                        jsonObject.putOpt("name", dataObject.optString("project"));
                        String imageUrl = dataObject.optString("image"); // or "logo"
                        //imageUrl = "https://acme.warburttons.com/" + imageUrl;
                        jsonObject.putOpt("image", imageUrl); // Store the full image URL
                        Log.d("tag1234", imageUrl);

                        //siteDetail.setName(dataObject.optString("name"));

                        jsonArray1.put(jsonObject);
//TODO here
                        }
                    }
                    }else if(vendorclientorcampaign==1){//client
                        for(int i=0; i< dataArray.length();i++){
                            Log.d("tg12", Integer.toString(i));
                            JSONObject dataObject = dataArray.getJSONObject(i);
                            Log.d("tg12", dataArray.getJSONObject(i).toString());

                            if(dataObject != null) {
                                jsonObject1 = new JSONObject();
                                jsonObject= new JSONObject();
                                Log.d("DataObjectContent", "Data Object: " + dataObject.toString());
                                //AdminCrudDataClass siteDetail = new AdminCrudDataClass();
                                jsonObject.putOpt("id", dataObject.optInt("id"));
                                jsonObject.putOpt("company_name", dataObject.optString("company_name"));
                                jsonObject.putOpt("image", dataObject.optString("logo"));
                                jsonObject.putOpt("name", dataObject.optString("name"));
                                Log.d("tg12", dataArray.getJSONObject(i).toString());
                                String imageUrl = dataObject.optString("image"); // or "logo"
                                //imageUrl = "https://acme.warburttons.com/" + imageUrl;
                                //jsonObject.putOpt("image", imageUrl); // Store the full image URL

                                //to pass to client class
                                jsonObject1.putOpt("id", dataObject.optInt("id"));
                                jsonObject1.putOpt("name", dataObject.optString("name"));
                                jsonObject1.putOpt("email", dataObject.optString("email"));
                                jsonObject1.putOpt("phone_number", dataObject.optString("phone_number"));
                                jsonObject1.putOpt("company_name", dataObject.optString("company_name"));
                                jsonObject1.putOpt("company_address", dataObject.optString("company_address"));
                                jsonObject1.putOpt("gst_no", dataObject.optString("gst_no"));
                                jsonObject1.putOpt("logo", dataObject.optString("logo"));
                                jsonObject1.putOpt("created_at", dataObject.optString("created_at"));
                                jsonObject1.putOpt("updated_at", dataObject.optString("updated_at"));

                                //siteDetail.setName(dataObject.optString("name"));

                                jsonArray1.put(jsonObject);
                                jsonArray2.put(jsonObject1);
                            //TODO here
                            }
                        }

                    }else if(vendorclientorcampaign==2){//ven

                        for(int i=0; i< dataArray.length();i++){
                            JSONObject dataObject = dataArray.getJSONObject(i);
                            if(dataObject != null) {
                                jsonObject2 = new JSONObject();
                                jsonObject= new JSONObject();

                                Log.d("DataObjectContent", "Data Object: " + dataObject.toString());
                                //AdminCrudDataClass siteDetail = new AdminCrudDataClass();
                                jsonObject.putOpt("id", dataObject.optInt("id"));
                                jsonObject.putOpt("company_name", dataObject.optString("company_name"));
                                jsonObject.putOpt("image", dataObject.optString("logo"));
                                jsonObject.putOpt("name", dataObject.optString("name"));
                                String imageUrl = dataObject.optString("image"); // or "logo"
                                //imageUrl = "https://acme.warburttons.com/" + imageUrl;
                                //jsonObject.putOpt("image", imageUrl); // Store the full image URL


                                //to pass to client class
                                jsonObject2.putOpt("id", dataObject.optInt("id"));
                                jsonObject2.putOpt("name", dataObject.optString("name"));
                                jsonObject2.putOpt("email", dataObject.optString("email"));
                                jsonObject2.putOpt("phone_number", dataObject.optString("phone_number"));
                                jsonObject2.putOpt("company_name", dataObject.optString("company_name"));
                                jsonObject2.putOpt("company_address", dataObject.optString("company_address"));
                                jsonObject2.putOpt("gst_no", dataObject.optString("gst_no"));
                                jsonObject2.putOpt("logo", dataObject.optString("logo"));
                                jsonObject2.putOpt("created_at", dataObject.optString("created_at"));
                                jsonObject2.putOpt("updated_at", dataObject.optString("updated_at"));
                                jsonObject2.putOpt("area", dataObject.optString("area"));
                                //siteDetail.setName(dataObject.optString("name"));

                                jsonArray1.put(jsonObject);
                                jsonArray3.put(jsonObject2);
//TODO here
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
                                              progressBar.clearAnimation();
                                              progressBar.setVisibility(View.GONE);
                                              //view.setVisibility(View.GONE);

                                  GridLayoutManager layoutManager = new GridLayoutManager(ctxt, 2);
                                  binding.rvCampaignList.setLayoutManager(layoutManager);

                                  if(jsonArray1.toString().equals("[]")){

                                      binding.noData.setVisibility(View.VISIBLE);
                                  }else{

                                      binding.noData.setVisibility(View.GONE);
                                  }
                                    CampaignListAdapter adapter = new CampaignListAdapter(ctxt, jsonArray1, true);
                                    binding.rvCampaignList.setAdapter(adapter);
                              }});
        }catch (Exception e){
            Log.d("tag40",e.toString());
            try{

                JSONObject jsonobject= new JSONObject(response);
                if(jsonobject.getBoolean("success")){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            populatevendorspinner= false;
                            Log.d("vendr",response);
                            populatevendorspinner1(response);
                        }
                    });


                }

            }catch (Exception f){
                f.printStackTrace();
            }

        }
    }

    private void campaignList() {
        vendorclientorcampaign=0;
        //TODO pass correct logintoken here
        //logintoken="211|fcsu2C90hfOUduHNXDSZRxu7394NaQhOpiG3zMeM";
        Log.d("tg5","fin");

        APIreferenceclass api= new APIreferenceclass(vendorclientorcampaign, logintoken, this);
    }

    private void clientList() {
        vendorclientorcampaign=1;
        //TODO pass correct logintoken here
        //logintoken="Bearer 211|fcsu2C90hfOUduHNXDSZRxu7394NaQhOpiG3zMeM";
        //TODO- here

        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(rotateAnimation);
        APIreferenceclass api= new APIreferenceclass(vendorclientorcampaign, logintoken, this);
    }
    //

    private void venderList() {
        vendorclientorcampaign=2;
        //TODO pass correct logintoken here
        //logintoken="Bearer 211|fcsu2C90hfOUduHNXDSZRxu7394NaQhOpiG3zMeM";

        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(rotateAnimation);
        APIreferenceclass api= new APIreferenceclass(vendorclientorcampaign, logintoken, this);
    }

    @SuppressLint("ResourceAsColor")
    public void btnCompaignClick(View view) {
        clearUi();

        binding.tvVender.setBackgroundResource(0);
        binding.tvVender.setTextColor(R.color.colorPrimaryDark);

        binding.tvClient.setBackgroundResource(0);
        binding.tvClient.setTextColor(R.color.colorPrimaryDark);

        binding.tvCompaign.setBackgroundResource(R.drawable.primaryround);
        binding.tvCompaign.setTextColor(Color.WHITE);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(rotateAnimation);

        binding.llSpinner.setVisibility(View.VISIBLE);

        // Sample data for the spinners
        String[] states = {"Select a State", "Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli and Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Ladakh", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"};


        // Finding the spinners
        Spinner spinnerState = findViewById(R.id.spinnerstate);
        populatevendorspinner();



        // Creating adapters for the spinners
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, states);

        // Set the layout for dropdown options
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attaching the adapters to the spinners
        spinnerState.setAdapter(stateAdapter);

        // Setting up an OnItemSelectedListener for the first spinner
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // This code runs when an item is selected
                String selectedState = parent.getItemAtPosition(position).toString();
                statespinnerselected= true;
                if (!selectedState.equals("Select a State")) {
                    // Code to execute based on the selected state
                    //Toast.makeText(AdminDashboardActivity.this, "Selected: " + selectedState, Toast.LENGTH_SHORT).show();

                    // Add your custom logic here based on the selected state
                    executeYourLogicBasedOnState(selectedState);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // This code runs when no item is selected (usually when the spinner starts)
            }
        });

        campaignList();
        // ... your logic ...
    }

    boolean populatevendorspinner;

    void populatevendorspinner(){

        populatevendorspinner= true;
        APIreferenceclass api= new APIreferenceclass(AdminDashboardActivity.this, logintoken, 1);

    }

    String vendorsid[]; //name

    void populatevendorspinner1(String response){

        String[] vendors;

        try{

            JSONObject jsonResponse = new JSONObject(response);
            JSONArray dataArray = jsonResponse.getJSONArray("data");
            vendors= new String[dataArray.length()+1];
            vendorsid= new String[dataArray.length()];

            vendors[0]= "Select a Vendor";

            for(int i=0; i< dataArray.length();i++){
                JSONObject dataObject = dataArray.getJSONObject(i);
                vendors[i+1]= dataObject.optString("name");
                vendorsid[i]= dataObject.optString("name");
            }
            Spinner spinnerVendor = findViewById(R.id.spinnervendor);
            ArrayAdapter<String> vendorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vendors);
            vendorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerVendor.setAdapter(vendorAdapter);

            // Setting up an OnItemSelectedListener for the first spinner
            spinnerVendor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // This code runs when an item is selected
                    String selectedState = parent.getItemAtPosition(position).toString();
                    statespinnerselected= true;
                    if (!selectedState.equals("Select a Vendor")) {
                        // Code to execute based on the selected state
                        //Toast.makeText(AdminDashboardActivity.this, "Selected: " + selectedState, Toast.LENGTH_SHORT).show();

                        String idofselectedvendor= parent.getItemAtPosition(position).toString();//name

                        // Add your custom logic here based on the selected state
                        executeYourLogicBasedOnStateVendor(idofselectedvendor);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // This code runs when no item is selected (usually when the spinner starts)
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @SuppressLint("ResourceAsColor")
    public void btnVenderClick(View view) {
        Log.d("tag20", "btnvenderclick");
        clearUi();

        binding.tvVender.setBackgroundResource(R.drawable.primaryround);
        binding.tvVender.setTextColor(Color.WHITE);

        binding.tvClient.setBackgroundResource(0);
        binding.tvClient.setTextColor(R.color.colorPrimaryDark);

        binding.tvCompaign.setBackgroundResource(0);
        binding.tvCompaign.setTextColor(R.color.colorPrimaryDark);

        binding.llSpinner.setVisibility(View.GONE);


        venderList();
    }

    // Custom logic that executes based on the selected state
    private void executeYourLogicBasedOnState(String state) {
        APIreferenceclass api= new APIreferenceclass(AdminDashboardActivity.this, logintoken, state, "a", 1);

    }

    // Custom logic that executes based on the selected state
    private void executeYourLogicBasedOnStateVendor(String idofvendor) {
        fetchsitesaccordingtovendor= true;
        APIreferenceclass api= new APIreferenceclass(AdminDashboardActivity.this, logintoken, idofvendor, "a", 1, 2, 2);

    }

    boolean fetchsitesaccordingtovendor;

    @SuppressLint("ResourceAsColor")
    public void btnClientClick(View view) {
        Log.d("tag20", "btnclientclick");
        clearUi();

        binding.tvVender.setBackgroundResource(0);
        binding.tvVender.setTextColor(R.color.colorPrimaryDark);

        binding.tvClient.setBackgroundResource(R.drawable.primaryround);
        Log.d("tag20", "1");
        binding.tvClient.setTextColor(Color.WHITE);
        Log.d("tag20", "2");


        binding.llSpinner.setVisibility(View.GONE);

        binding.tvCompaign.setBackgroundResource(0);
        binding.tvCompaign.setTextColor(R.color.colorPrimaryDark);

        clientList();
        // ... your logic ...
    }

    public void onEditClick(int position, View view){
        //add code to dropdown a list which says delete
        //delete code
        // Context should be your activity or fragment context
        Log.d("tg98", Integer.toString(position));
        PopupMenu popup = new PopupMenu(this, view); // 'view' is the anchor view for popup menu
        if(vendorclientorcampaign== 0|| vendorclientorcampaign==1|| vendorclientorcampaign==2) {
            popup.getMenuInflater().inflate(R.menu.popup_menu_campaign, popup.getMenu()); // Inflate your menu resource

        }
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.delete) {
                    // Handle delete action
                    JSONObject campaignItem= null;
                    if(vendorclientorcampaign== 0){
                    try {
                        RecyclerView recyclerView = findViewById(R.id.rvCampaignList);
                        CampaignListAdapter campaignAdapter = (CampaignListAdapter) recyclerView.getAdapter();
                        campaignItem = campaignAdapter.jsonArray.getJSONObject(position);
                        Log.d("tg93", campaignItem.toString());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    APIreferenceclass api= new APIreferenceclass(campaignItem, vendorclientorcampaign, logintoken, ctxt);
                    delete= 1;
                    return true;
                }else if(vendorclientorcampaign== 1){
                    try {
                        RecyclerView recyclerView = findViewById(R.id.rvCampaignList);
                        CampaignListAdapter campaignAdapter = (CampaignListAdapter) recyclerView.getAdapter();
                        campaignItem = campaignAdapter.jsonArray.getJSONObject(position);
                        Log.d("tg93", campaignItem.toString());

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    APIreferenceclass api= new APIreferenceclass(campaignItem, vendorclientorcampaign, logintoken, ctxt);
                    delete= 1;

                    return true;
                }else if(vendorclientorcampaign== 2){
                    try {
                        RecyclerView recyclerView = findViewById(R.id.rvCampaignList);
                        CampaignListAdapter campaignAdapter = (CampaignListAdapter) recyclerView.getAdapter();
                        campaignItem = campaignAdapter.jsonArray.getJSONObject(position);
                        Log.d("tg93", campaignItem.toString());

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    APIreferenceclass api= new APIreferenceclass(campaignItem, vendorclientorcampaign, logintoken, ctxt);
                    delete= 1;
                    return true;

                    }
                }

                //to edit campaign info
                else if(item.getItemId()== R.id.edit) {

                    if(vendorclientorcampaign==0){
                    JSONObject campaignItem = null;

                    try {
                        RecyclerView recyclerView = findViewById(R.id.rvCampaignList);
                        CampaignListAdapter campaignAdapter = (CampaignListAdapter) recyclerView.getAdapter();
                        campaignItem = campaignAdapter.jsonArray.getJSONObject(position);

                        //Starting edit campaign
                        Intent intent = new Intent(AdminDashboardActivity.this, EditCampaign.class);
                        intent.putExtra("logintoken", logintoken);
                        intent.putExtra("campaignItem", campaignItem.toString());
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //APIreferenceclass api= new APIreferenceclass(campaignItem, vendorclientorcampaign, logintoken, ctxt);
                    return true;
                }else if(vendorclientorcampaign==1){
                        JSONObject campaignItem = null;

                        try {
                            RecyclerView recyclerView = findViewById(R.id.rvCampaignList);
                            CampaignListAdapter campaignAdapter = (CampaignListAdapter) recyclerView.getAdapter();
                            campaignItem = campaignAdapter.jsonArray.getJSONObject(position);

                            //Starting edit campaign
                            Intent intent = new Intent(AdminDashboardActivity.this, AddClientDetailDashActivity.class);
                            intent.putExtra("logintoken", logintoken);
                            intent.putExtra("response", campaignItem.toString());
                            startActivity(intent);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //APIreferenceclass api= new APIreferenceclass(campaignItem, vendorclientorcampaign, logintoken, ctxt);
                        return true;
                    }
                else if(vendorclientorcampaign==2){
                        JSONObject campaignItem = null;
Log.d("tag41", "click detected");
                        try {
                            RecyclerView recyclerView = findViewById(R.id.rvCampaignList);
                            CampaignListAdapter campaignAdapter = (CampaignListAdapter) recyclerView.getAdapter();
                            campaignItem = campaignAdapter.jsonArray.getJSONObject(position);

                            startActivity(new Intent(AdminDashboardActivity.this, AdminViewVendorDetails.class)
                                    //.putExtra("id", id)
                                    .putExtra("campaignItem", campaignItem.toString())
                                    .putExtra("logintoken", logintoken)
                                    .putExtra("vendorclientorcampaign", vendorclientorcampaign));
                                    //.putExtra("jsonArray", jsonObject1.toString()));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //APIreferenceclass api= new APIreferenceclass(campaignItem, vendorclientorcampaign, logintoken, ctxt);
                        return true;
                    }
                }
            return false;
        }});
        popup.show(); // Show the popup menu

    }

    public void onItemClick(int position) {
        try {
            Log.d("tag51", Integer.toString(position));
            // Retrieve JSONObject from your jsonArray at position
            JSONObject jsonObject = jsonArray1.getJSONObject(position);
            Log.d("tag51", jsonArray1.getJSONObject(position).toString());

            //TODO add correct login token here
            //logintoken="Bearer 322|7Dor2CuPXz4orJV5GUleBAUcmgYnbswVMLQ5EUNM";

            // Get site id or site no from the JSONObject
            String id = jsonObject.getString("id"); // Or get an id if you have that
            Log.d("tag51", jsonObject.getString("id"));
            Log.d("tag60", jsonObject.toString());

            // String siteId = jsonObject.getString("siteId"); // If you have a site id.

            if(vendorclientorcampaign==0){//campaign

                // Start new activity and pass the retrieved data

                Log.d("sitenumber", id);
                startActivity(new Intent(this, ViewSiteDetailActivity.class)
                        .putExtra("campaignType", "old")
                        .putExtra("sitenumber", id)
                        .putExtra("logintoken", logintoken)
                        .putExtra("vendorclientorcampaign", vendorclientorcampaign)
                        .putExtra("apiresponse", jsonObject.toString()));

            }else if(vendorclientorcampaign==1){//client

                JSONObject jsonObject1 = jsonArray2.getJSONObject(position);
                Log.d("tag91", jsonArray2.getJSONObject(position).toString());
                Log.d("tag91",jsonArray2.toString());
                Log.d("tag91",jsonObject1.toString());
                Log.d("tag91",Integer.toString(position));
                Log.d("tg11", id);

                startActivity(new Intent(this, ClientDashFirstPage.class)
                        .putExtra("id", id)
                        .putExtra("logintoken", logintoken)
                        .putExtra("vendorclientorcampaign", vendorclientorcampaign)
                        .putExtra("jsonArray", jsonObject1.toString()));
                //jsonObject1= new JSONObject();
                //jsonArray2= new JSONArray();

            }else if(vendorclientorcampaign==2){//vendor

                JSONObject jsonObject1 = jsonArray3.getJSONObject(position);
                Log.d("jsn", jsonObject1.toString());
                startActivity(new Intent(this, ProfileActivityAdminDash.class)
                        .putExtra("userid", id)
                        .putExtra("logintoken", logintoken)
                        .putExtra("vendorclientorcampaign", vendorclientorcampaign)
                        .putExtra("jsonArray", jsonObject1.toString())
                        .putExtra("area", jsonObject1.getString("name"))
                );

               /* startActivity(new Intent(this, AdminViewVendorDetails.class)
                        .putExtra("id", id)
                        .putExtra("logintoken", logintoken)
                        .putExtra("vendorclientorcampaign", vendorclientorcampaign)
                        .putExtra("jsonArray", jsonObject1.toString()));
                */}

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
            if (vendorclientorcampaign == 0) {//campaign
                jsonArray1 = new JSONArray();
            } else if (vendorclientorcampaign == 1) {//client
                jsonArray2 = new JSONArray();
            } else if(vendorclientorcampaign == 2){//vendor
                jsonArray3= new JSONArray();
            }
        }
        // Reset any other UI elements here as needed
    }

    public void onPlusClick(View view) {

        Log.d("tag20", "onplusclick");
    if(vendorclientorcampaign==1) {//client
        Intent intent = new Intent(AdminDashboardActivity.this, AddClientActivity.class);
        intent.putExtra("logintoken", logintoken);
        startActivity(intent);
    }else if(vendorclientorcampaign==2){//vendor
        Intent intent = new Intent(AdminDashboardActivity.this, AddVenderActivity.class);
        intent.putExtra("logintoken", logintoken);
        startActivity(intent);
    }else{
        Intent intent = new Intent(AdminDashboardActivity.this, AddCampaignDetails.class);
        intent.putExtra("logintoken", logintoken);
        startActivity(intent);
        }
    }

    public void onNotificationClick(View view){
        //TODO ask dev what this does
    }

    public void onProfileClick(View view){
        userid= FileHelper.readUserId(this);
        Intent intent= new Intent(AdminDashboardActivity.this, ProfileActivity.class);;
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

            Intent intent= new Intent(AdminDashboardActivity.this, OTP.class);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onAddSiteClick(View view){

        Intent intent= new Intent(AdminDashboardActivity.this, AddSiteActivity.class);
        //intent.putExtra("area", area);
        //Log.d("xyz", String.valueOf(vendorid));
        //intent.putExtra("vendorid", String.valueOf(vendorid));
        startActivity(intent);
    }

    public void onAddClientClick(View view) {
        startActivity(new Intent(this, AddSiteDetailActivity.class));
    }

    public void onAddVenderClick(View view) {
        startActivity(new Intent(this, AddVenderActivity.class));
    }
}