package com.acme.oohvendor.activity.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.acme.oohvendor.R;
import com.acme.oohvendor.activity.login.OTP;
import com.acme.oohvendor.adapters.CampaignListAdapter;
import com.acme.oohvendor.databinding.ActivityMainBinding;
import com.acme.oohvendor.databinding.ActivityViewCampaignSitesBinding;
import com.acme.oohvendor.viewmodel.APIreferenceclass;
import com.acme.oohvendor.viewmodel.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewCampaignSites extends AppCompatActivity implements ApiInterface {

    int id=0;
    ActivityViewCampaignSitesBinding binding;
    //JSONArray jsonArray;
    boolean showMenus = false;
    int delete= 0;

    private final Context ctxt= this;
    int vendorclientorcampaign=0; //campaign is 0, client is 1, vendor is 2
    String logintoken="";
    String clientid;
    boolean showedit;
    boolean populatevendorspinner;
    //todo access token save to memory add to api call

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_campaign_sites);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.rvCampaignList.setLayoutManager(layoutManager);
        Log.d("whichclass", "ViewCampaignSites");
        approvesites= false;
        showedit= true;
        statespinnerselected= false;
        populatevendorspinner= false;
        fetchsitesaccordingtovendor= false;
        area= "";
        //animation code
        progressBar= findViewById(R.id.progressBar);
        rotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_animation);
        //animation code

        try{

            String name= getIntent().getStringExtra("name");
            Log.d("abc", "1");
            binding.title6.setText("Client"+ " - "+ name);
            Log.d("abc", "2");

        }catch (Exception e){
            Log.d("abc", e.toString());
        }

        FileHelper fh= new FileHelper();
        logintoken= fh.readLoginToken(this);

        clientid=getIntent().getStringExtra("clientid");
        //Log.d("tag58", clientid);

        try{
            if(getIntent().getStringExtra("camefrom").equals("ClientDashBoardActivity")){
                binding.ivPlus.setVisibility(View.GONE);
                showedit= false;
            }else if(getIntent().getStringExtra("camefrom").equals("contentotp")){
                binding.ivPlus.setVisibility(View.GONE);
                showedit= false;

        }}catch(Exception e){
            e.printStackTrace();
        }

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

        //TODO remove after adding to ui
        jsonArray1= new JSONArray();
        CampaignListAdapter adapter = new CampaignListAdapter(this, jsonArray1, showedit);
        binding.rvCampaignList.setAdapter(adapter);
        campaignList(clientid);
    }

    @SuppressLint("ResourceAsColor")
    public void btnVenderClick(View view) {
        Log.d("tag20", "btnvenderclick");
        //clearUi();

        binding.tvVender.setBackgroundResource(R.drawable.primaryround);
        binding.tvVender.setTextColor(Color.WHITE);

        binding.tvClient.setBackgroundResource(0);
        binding.tvClient.setTextColor(R.color.colorPrimaryDark);

        binding.tvCompaign.setBackgroundResource(0);
        binding.tvCompaign.setTextColor(R.color.colorPrimaryDark);

        //binding.llSpinner.setVisibility(View.GONE);


        approvesites= true;
        venderList();
    }


    //TODO here send api call for sites to be approved
    private void venderList() {
        vendorclientorcampaign=2;
        //TODO pass correct logintoken here
        //logintoken="Bearer 211|fcsu2C90hfOUduHNXDSZRxu7394NaQhOpiG3zMeM";

        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(rotateAnimation);
        double a= 1.0;
        double b= 1.0;
        double c= 1.0;
        APIreferenceclass api= new APIreferenceclass(logintoken, ViewCampaignSites.this ,area, a, b , c);
    }

    @SuppressLint("ResourceAsColor")
    public void btnCompaignClick(View view) {
        //clearUi();

        binding.tvVender.setBackgroundResource(0);
        binding.tvVender.setTextColor(R.color.colorPrimaryDark);

        binding.tvClient.setBackgroundResource(0);
        binding.tvClient.setTextColor(R.color.colorPrimaryDark);

        binding.tvCompaign.setBackgroundResource(R.drawable.primaryround);
        binding.tvCompaign.setTextColor(Color.WHITE);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(rotateAnimation);

        /*binding.llSpinner.setVisibility(View.VISIBLE);

        // Sample data for the spinners
        String[] states = {"Select a State", "Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli and Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Ladakh", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "TN1","Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"};

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
*/
        approvesites= false;
        campaignList(area);
        // ... your logic ...
    }
    Boolean approvesites;


    boolean statespinnerselected;

    void populatevendorspinner(){

        populatevendorspinner= true;
        APIreferenceclass api= new APIreferenceclass(ViewCampaignSites.this, logintoken, 1);

    }

    // Custom logic that executes based on the selected state
    private void executeYourLogicBasedOnState(String state) {
        APIreferenceclass api= new APIreferenceclass(ViewCampaignSites.this, logintoken, state, "a", 1);

    }

    // Custom logic that executes based on the selected state
    private void executeYourLogicBasedOnStateVendor(String idofvendor) {
        fetchsitesaccordingtovendor= true;
        APIreferenceclass api= new APIreferenceclass(ViewCampaignSites.this, logintoken, idofvendor, "a", 1, 2, 2);

    }

    boolean fetchsitesaccordingtovendor;


    //onresponsereceived from api
    public void onResponseReceived(String response){

        Log.d("addbatest", "response is "+ response);
        Log.d("tag58","got response");

        if(delete==0&&!statespinnerselected) {
            implementUi(response);
        }else if(delete==1){
            delete= 0;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Site deleted successfully", Toast.LENGTH_SHORT).show();
                    campaignList(clientid);
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

    String[] idarray;
    String vendorid;
    JSONArray jsonArray1;
    private void implementUi(String response){
        try {
            JSONObject jsonObject = new JSONObject();
            //jsonArray1= new JSONArray();



            JSONObject jsonResponse = new JSONObject(response);
            if(jsonResponse.getInt("status")== 200) {
                JSONArray dataArray = jsonResponse.getJSONArray("data");

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
                                jsonObject.putOpt("name", dataObject.optString("location"));
                                jsonObject.putOpt("uid", dataObject.optString("code"));
                                //siteDetail.setName(dataObject.optString("name"));

                                jsonArray1= new JSONArray();
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
                                jsonObject.putOpt("company_name", dataObject.optString("project"));
                                jsonObject.putOpt("image", dataObject.optString("image"));
                                jsonObject.putOpt("name", dataObject.optString("retail_name"));


                                jsonArray1= new JSONArray();
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
                            jsonArray1= new JSONArray();
                            jsonArray1.put(jsonObject);
                        }
                        }
                    }
                    Log.d("JSONArrayContent", "JSONArray1: " + jsonArray1.toString());
                }else if(dataArray == null || dataArray.length() <= 0){
                    jsonArray1= new JSONArray();
                    //jsonArray1.put(jsonObject);
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

                    if(jsonArray1.toString().equals("[]")){

                        binding.noData.setVisibility(View.VISIBLE);
                    }else{

                        binding.noData.setVisibility(View.GONE);
                    }

                    CampaignListAdapter adapter = new CampaignListAdapter(ctxt, jsonArray1, showedit);

                    //animation code
                            progressBar.clearAnimation();
                            progressBar.setVisibility(View.GONE);
                    //animation code

                    binding.rvCampaignList.setAdapter(adapter);

                    try {
                        JSONObject jsonobject = new JSONObject(response);
                        //binding.title.setText(jsonobject.getString("campaign_name"));
                        binding.clientid.setText(Integer.toString(jsonobject.getInt("client_id")));
                        binding.clientname.setText(jsonobject.getString("client_name"));
                        binding.campaign.setText(jsonobject.getString("campaign_name"));
                        binding.totalsites.setText(jsonobject.getString("site_count"));
                    }catch(Exception e){
                        Log.d("tag322121", e.toString());
                    }
                }});
        }catch (Exception e) {
            Log.d("tg222", e.toString());
            try{

                JSONObject jsonobject= new JSONObject(response);
                if(jsonobject.getBoolean("success")&&populatevendorspinner){

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
            /*runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    // Your UI update code goes here
                    //animation code
                    progressBar.clearAnimation();
                    progressBar.setVisibility(View.GONE);
                    //animation code

                    Toast.makeText(getApplicationContext(), "No sites.", Toast.LENGTH_SHORT).show();
                }});
*/
        }}
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

            String camefrom= "";
            try{
                camefrom= getIntent().getStringExtra("camefrom");
            }catch (Exception e){
                e.printStackTrace();
            }

            // Start new activity and pass the retrieved data
            startActivity(new Intent(this, ViewSiteDetailActivity.class)
                    .putExtra("campaignType", "old")
                    .putExtra("clientId", clientid)
                    .putExtra("camefrom", camefrom)
                    .putExtra("sitenumber", id)
                    .putExtra("logintoken", logintoken)
                    .putExtra("vendorclientorcampaign", vendorclientorcampaign)
                    .putExtra("approvesites", approvesites)

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



                        APIreferenceclass api = new APIreferenceclass(siteid, logintoken, ctxt, 1);
                        delete = 1;
                        return true;
                    }
                }

                return false;
            }});
        popup.show(); // Show the popup menu

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

    //copy paste below

    public void onPlusClick(View view) {

        Log.d("tag20", "onplugtreehththtsclick");
        Intent intent= new Intent(ViewCampaignSites.this, AddSiteDetailActivity.class);
        //intent.putExtra("campaignId",idofcampaign);
        intent.putExtra("vendorId",vendorid);
        startActivity(intent);
    }

    String area;

    public void onAddSiteClick(View view){

        Intent intent= new Intent(ViewCampaignSites.this, AddSiteActivity.class);

        intent.putExtra("area", area);
        Log.d("xyz", String.valueOf(vendorid));
        intent.putExtra("vendorid", String.valueOf(vendorid));
        startActivity(intent);
    }

    public void onProfileClick(View view){
        String userid= FileHelper.readUserId(this);
        Intent intent= new Intent(ViewCampaignSites.this, ProfileActivity.class);;
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

            Intent intent= new Intent(ViewCampaignSites.this, OTP.class);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}