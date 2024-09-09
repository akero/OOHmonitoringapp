package com.acme.afsvendor.activity.dashboard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
//import androidx.databinding.DataBindingUtil;
//import androidx.databinding.ViewDataBinding;
import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import com.acme.afsvendor.R;
import com.acme.afsvendor.databinding.ActivityAddSiteDetailBinding;
import com.acme.afsvendor.databinding.ActivityAddSiteDetailsVendorBinding;
import com.acme.afsvendor.utility.NetworkUtils;
import com.acme.afsvendor.viewmodel.ApiInterface;
import com.acme.afsvendor.viewmodel.SiteDetail;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Calendar;
import java.util.Locale;

public class AddSiteDetailsActivityVendor extends AppCompatActivity implements LocationListener, ApiInterface {

    private ActivityAddSiteDetailsVendorBinding binding;
    private final Context ctxt= this;

    private Calendar cal = Calendar.getInstance();
    private int yy, mm, dd;
    private String imageUrl = "";
    private final int REQUEST_IMAGE_CAPTURE = 101;

    private LocationManager locationManager;

    JSONObject jsonobj;
    String loginToken;
    String campaignId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO- here. Have to start this activity from vendor view sites plus icon. remove the populatefields code.

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_site_details_vendor);
        try {
            jsonobj = new JSONObject(getIntent().getStringExtra("siteDetail"));
            campaignId= getIntent().getStringExtra("campaignId");
        }catch(Exception e){
            Log.d("tg90", e.toString());}

        Log.d("whichclass", "AddCSiteDetailsActivityVendor");

        FileHelper fh= new FileHelper();
        loginToken= fh.readLoginToken(this);
        Log.d("tag111", "addsitedetailactivity");

        //populating fields
        populateFields(jsonobj);

    }

    String siteno;
    SiteDetail siteDetail;


    void populateFields(JSONObject dataObject){
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
            try {
                String imageUrl = dataObject.optString("image");
                imageUrl= "https://acme.warburttons.com/"+ imageUrl;
                Log.d("tag41", "imageurl is "+ imageUrl);
                if(imageUrl != "null" && !imageUrl.isEmpty()) {
                    URL url = new URL(imageUrl);
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    siteDetail.setImage(bitmap);
                }
            } catch (Exception e) {
                Log.d("tag41", "error in implementui" +e.toString());
                Log.e("tag41", "sdfdg", e);
                // Handle error
            }

            siteno= siteDetail.getSiteNo();

            // Update UI
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    TextView tvSiteId = findViewById(R.id.etSiteNo);
                    tvSiteId.setText(String.valueOf(siteDetail.getSiteNo()));

                    TextView tvLocation = findViewById(R.id.etLocation);
                    tvLocation.setText(siteDetail.getLocation());

                    TextView tvSiteName = findViewById(R.id.tvAddSiteDetail);
                    tvSiteName.setText(siteDetail.getName());

                    TextView tvLastInspection = findViewById(R.id.etStartDate);
                    tvLastInspection.setText(siteDetail.getCreatedAt());

                    TextView tvLatitude = findViewById(R.id.etLatitude);
                    tvLatitude.setText(siteDetail.getLatitude());

                    TextView tvLongitude = findViewById(R.id.etLongitude);
                    tvLongitude.setText(siteDetail.getLongitude());

                    //TODO

                    //TextView tvMediaType = findViewById(R.id.tvMediaType);
                    //tvMediaType.setText(siteDetail.getMediaType());

                    // TextView tvIllumination = findViewById(R.id.tvIllumination);
                    // tvIllumination.setText(siteDetail.getIllumination());

                    TextView tvStartDate = findViewById(R.id.etStartDate);
                    tvStartDate.setText(siteDetail.getStartDate());

                    // Set the site number
                    TextView tvSiteNo = findViewById(R.id.etSiteNo);
                    tvSiteNo.setText(String.valueOf(siteDetail.getSiteNo())); // assuming getter method exists

                    // Set the width
                    TextView tvWidth = findViewById(R.id.etWidth);
                    tvWidth.setText(siteDetail.getWidth()); // assuming getter method exists

                    // Set the height
                    TextView tvHeight = findViewById(R.id.etHeight);
                    tvHeight.setText(siteDetail.getHeight()); // assuming getter method exists

                    // Set the total area
                    TextView tvTotalArea = findViewById(R.id.etTotalArea);
                    tvTotalArea.setText(siteDetail.getTotalArea()); // assuming getter method exists

                    //RoundRectCornerImageView tvImage = findViewById(R.id.ivCampaignImage);
                    // if(siteDetail.getImage()!=null) {
                    //    tvImage.setImageBitmap(siteDetail.getImage());
                    // }
                }
            });
        }
    }

    public void btnCloseClick(View view) {
        finish();
    }

    int queryType;
    public void btnSaveClick(View view) {

// Create a SiteDetail object
        SiteDetail siteDetail = new SiteDetail();

// Set the site number
        TextView tvSiteId = findViewById(R.id.etSiteNo);
        siteDetail.setSiteNo(tvSiteId.getText().toString());

// Set the location
        TextView tvLocation = findViewById(R.id.etLocation);
        siteDetail.setLocation(tvLocation.getText().toString());

// Set the site name
        TextView tvSiteName = findViewById(R.id.tvAddSiteDetail);
        siteDetail.setName(tvSiteName.getText().toString());

// Set the last inspection date
        TextView tvLastInspection = findViewById(R.id.etStartDate);
        siteDetail.setCreatedAt(tvLastInspection.getText().toString());

// Set the latitude
        TextView tvLatitude = findViewById(R.id.etLatitude);
        siteDetail.setLatitude(tvLatitude.getText().toString());

// Set the longitude (correct the typo from "longitute" to "longitude" in your JSON or code)
        TextView tvLongitude = findViewById(R.id.etLongitude);
        siteDetail.setLongitude(tvLongitude.getText().toString());

// Set the start date
        TextView tvStartDate = findViewById(R.id.etStartDate);
        siteDetail.setStartDate(tvStartDate.getText().toString());

// Set the width
        TextView tvWidth = findViewById(R.id.etWidth);
        siteDetail.setWidth(tvWidth.getText().toString());

// Set the height
        TextView tvHeight = findViewById(R.id.etHeight);
        siteDetail.setHeight(tvHeight.getText().toString());

// Set the total area
        TextView tvTotalArea = findViewById(R.id.etTotalArea);
        siteDetail.setTotalArea(tvTotalArea.getText().toString());

// Now you have populated the SiteDetail object with data from the EditText views

// Create a JSON object from the SiteDetail object
        JSONObject siteDetailJson = new JSONObject();
        try {
            Integer id = siteDetail.getId(); // Assuming getId() returns an Integer
            int idValue = id != null ? id : 0; // Replace 0 with your default value
            siteDetailJson.put("id", idValue);
            siteDetailJson.put("campaign_id", campaignId);
            siteDetailJson.put("id", id);siteDetailJson.put("vendor_id", siteDetail.getVendorId() != null ? siteDetail.getVendorId() : "");
            siteDetailJson.put("location", siteDetail.getLocation() != null ? siteDetail.getLocation() : "");
            siteDetailJson.put("created_at", siteDetail.getCreatedAt() != null ? siteDetail.getCreatedAt() : "");
            siteDetailJson.put("end_date", siteDetail.getEndDate() != null ? siteDetail.getEndDate() : "");
            siteDetailJson.put("latitude", siteDetail.getLatitude() != null ? siteDetail.getLatitude() : "");
            siteDetailJson.put("longitude", siteDetail.getLongitude() != null ? siteDetail.getLongitude() : "");
            siteDetailJson.put("media_type", siteDetail.getMediaType() != null ? siteDetail.getMediaType() : "");
            siteDetailJson.put("illumination", siteDetail.getIllumination() != null ? siteDetail.getIllumination() : "");
            siteDetailJson.put("start_date", siteDetail.getStartDate() != null ? siteDetail.getStartDate() : "");
            siteDetailJson.put("name", siteDetail.getName() != null ? siteDetail.getName() : "");
            siteDetailJson.put("site_no", siteDetail.getSiteNo() != null ? siteDetail.getSiteNo() : "");
            siteDetailJson.put("width", siteDetail.getWidth() != null ? siteDetail.getWidth() : "");
            siteDetailJson.put("height", siteDetail.getHeight() != null ? siteDetail.getHeight() : "");
            siteDetailJson.put("total_area", siteDetail.getTotalArea() != null ? siteDetail.getTotalArea() : "");
            siteDetailJson.put("updated_at", siteDetail.getUpdatedAt() != null ? siteDetail.getUpdatedAt() : "");

            // Add more properties as needed
        } catch (JSONException e) {
            Log.d("tg9", e.toString());
            e.printStackTrace();
        }

        queryType= 2; //PUT
//        APIreferenceclass api= new APIreferenceclass(queryType, ctxt, loginToken, siteDetailJson.toString(),siteno);
    }

    public void addImage(View view) {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "Check your Internet Connection and Try Again", Toast.LENGTH_LONG).show();
        } else {
            if (!checkPermissions()) {
                requestPermissions();
            } else {
                dispatchTakePictureIntent();
            }
        }
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            binding.ivCampaignImage.setImageBitmap(imageBitmap);
            getlatlong();
        }
    }

    private void getlatlong() {
        if (!checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions();
            }
        } else {
            getLastLocation();
        }
    }

    private void getLastLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        binding.etLatitude.setText(String.valueOf(location.getLatitude()));
        binding.etLongitude.setText(String.valueOf(location.getLongitude()));
    }

    private void showSnackbar(String mainTextStringId, String actionStringId, View.OnClickListener listener) {
        Toast.makeText(this, mainTextStringId, Toast.LENGTH_LONG).show();
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //super.onRequestPermissionsResult();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                showSnackbar("Permission was denied", "Settings", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", Build.DISPLAY, null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    public void addMoreSiteClick(View view) {
    }

    public void showSuccessMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_emailsent, null);
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        TextView tvResubmit = view.findViewById(R.id.tvResubmit);
        tvResubmit.setVisibility(View.INVISIBLE);
        tvMsg.setText("Site Added Successfully");
        Button btnClose = view.findViewById(R.id.btnClose);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showCalendar(View view) {
        yy = cal.get(Calendar.YEAR);
        mm = cal.get(Calendar.MONTH);
        dd = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yy = year;
                mm = month;
                dd = dayOfMonth;
                String dateStr = String.format(Locale.getDefault(), "%02d-%02d-%02d", dd, mm + 1, yy);
                binding.etStartDate.setText(dateStr);
            }
        }, yy, mm, dd);
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.show();
    }

    public void showCalendarEnd(View view) {
        yy = cal.get(Calendar.YEAR);
        mm = cal.get(Calendar.MONTH);
        dd = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yy = year;
                mm = month;
                dd = dayOfMonth;
                String dateStr = String.format(Locale.getDefault(), "%02d-%02d-%02d", dd, mm + 1, yy);
                binding.etEndDate.setText(dateStr);
            }
        }, yy, mm, dd);
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.show();
    }

    private static final String TAG = "LocationProvider";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    @Override
    public void onResponseReceived(String response) {
        Log.d("tg9", response);
        try {
            JSONObject jsonobj = new JSONObject(response);

        if(jsonobj.getBoolean("success")== true){
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                showSuccessMessage();
            }
        });}else{
            showFailureMessage();
        }

        }catch(Exception e){
            Log.d("tag123", e.toString());
        }
    }

    public void showFailureMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_emailsent, null);
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        TextView tvResubmit = view.findViewById(R.id.tvResubmit);
        tvResubmit.setVisibility(View.INVISIBLE);
        tvMsg.setText("Site add failed please recheck all the fields");
        Button btnClose = view.findViewById(R.id.btnClose);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}