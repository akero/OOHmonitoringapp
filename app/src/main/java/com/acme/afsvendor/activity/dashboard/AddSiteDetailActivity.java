package com.acme.afsvendor.activity.dashboard;

//import androidx.activity.result.PickVisualMediaRequest;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import android.Manifest;
import android.app.DatePickerDialog;
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
        import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.acme.afsvendor.R;
import com.acme.afsvendor.databinding.ActivityAddSiteDetailBinding;
import com.acme.afsvendor.utility.NetworkUtils;
        import com.acme.afsvendor.viewmodel.APIreferenceclass;
import com.acme.afsvendor.viewmodel.ApiInterface;
import com.acme.afsvendor.viewmodel.SiteDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class AddSiteDetailActivity extends AppCompatActivity implements LocationListener, ApiInterface {

    private ActivityAddSiteDetailBinding binding;
    private final Context ctxt= this;

    private Calendar cal = Calendar.getInstance();
    private int yy, mm, dd;
    Uri selectedImage;
    String selectedVendor;

    private String imageUrl = "";
    private final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 102;
    private LocationManager locationManager;

    JSONObject jsonobj;
    String loginToken;
    String campaignId;
    String vendorId;
    String selectedItem, selectedItem1;
    String editingsite;
    int vendorspinnerboolean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_site_detail);
        siteDetail = new SiteDetail();
        campaignId="";
        vendorselected= 0;
        String vendoridtemp= "";
        selectedVendor= "";
        vendorId= "";
        vendorspinnerboolean= 0;
        Log.d("whichclass", "AddSiteDetailActivity");

        try {
            jsonobj = new JSONObject(getIntent().getStringExtra("siteDetail"));

            editingsite= "";
            editingsite= getIntent().getStringExtra("editingsite");
            vendorId= jsonobj.getString("vendor_id");
            Log.d("tag222", vendoridtemp);
            Log.d("tag222", vendorId);



        }catch(Exception e){
            Log.d("tg90", "3"+e.toString());}

        try {
        campaignId= getIntent().getStringExtra("campaignId")!=null?getIntent().getStringExtra("campaignId"):"" ;
        //vendorId= getIntent().getStringExtra("vendorId")!=null?getIntent().getStringExtra("vendorId"):"" ;
        Log.d("tag222", vendorId);
            editingsite= "";
            editingsite= getIntent().getStringExtra("editingsite");
            //if(vendorId.equals("")) {
              //  vendorId = vendoridtemp;
            //}
            }catch(Exception e){
            Log.d("tg90", "3"+e.toString());}

        selectedItem="";
        selectedItem1="";
        selectedImage= null;
        Log.d("tg90", "2"+campaignId);

        FileHelper fh= new FileHelper();
        loginToken= fh.readLoginToken(this);
        Log.d("tag111", "addsitedetailactivity");

        //spinner code

        try {
            vendorspinnerboolean= 1;
            APIreferenceclass api = new APIreferenceclass(ctxt, fh.readLoginToken(this), 1);
        }catch(Exception e){
            Log.d("tg343", e.toString());
        }

        String[] items = new String[]{"Billboard", "Unipole", "Hoarding", "Gantry", "BQS","LED Screen", "MUPI", "Digital Wall Print"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        binding.spinnermediatype.setAdapter(adapter);

        // Inside your onCreate method
        binding.spinnermediatype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                //Toast.makeText(AddCampaignDetails.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });


        String[] items1 = new String[]{"Non Lit/Ambient Lit", "Front Lit", "Back Lit", "Digital"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        binding.spinnerillumination.setAdapter(adapter1);

        // Inside your onCreate method
        binding.spinnerillumination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem1 = parent.getItemAtPosition(position).toString();
                //Toast.makeText(AddCampaignDetails.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        //end of spinner code

        //populating fields
        populateFields(jsonobj);

    }

    String siteno;
    SiteDetail siteDetail;

    void populateFields(JSONObject dataObject){
        if(dataObject != null) {
            siteDetail = new SiteDetail();
            Log.d("tg66", "not null");
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
            Log.d("AddSiteDetailLog",
                    "ID: " + siteDetail.getId() +
                            ", Vendor ID: " + siteDetail.getVendorId() +
                            ", Location: " + siteDetail.getLocation() +
                            ", Created At: " + siteDetail.getCreatedAt() +
                            ", End Date: " + siteDetail.getEndDate() +
                            ", Latitude: " + siteDetail.getLatitude() +
                            ", Longitude: " + siteDetail.getLongitude() +
                            ", Media Type: " + siteDetail.getMediaType() +
                            ", Illumination: " + siteDetail.getIllumination() +
                            ", Start Date: " + siteDetail.getStartDate() +
                            ", Name: " + siteDetail.getName() +
                            ", Site No: " + siteDetail.getSiteNo() +
                            ", Width: " + siteDetail.getWidth() +
                            ", Height: " + siteDetail.getHeight() +
                            ", Total Area: " + siteDetail.getTotalArea() +
                            ", Updated At: " + siteDetail.getUpdatedAt());

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
try {
    TextView tvSiteId = findViewById(R.id.etSiteNo);
    tvSiteId.setText(String.valueOf(siteDetail.getSiteNo()));
}catch (Exception e){
    Log.d("tg55",e.toString());
}
                    TextView tvLocation = findViewById(R.id.etLocation);
                    tvLocation.setText(siteDetail.getLocation());

                    TextView tvSiteName = findViewById(R.id.etSiteName);
                    tvSiteName.setText(siteDetail.getName());

                    TextView tvLastInspection = findViewById(R.id.etStartDate);
                    tvLastInspection.setText(siteDetail.getStartDate());

                    TextView tvLatitude = findViewById(R.id.etLatitude);
                    tvLatitude.setText(siteDetail.getLatitude());

                    TextView tvLongitude = findViewById(R.id.etLongitude);
                    tvLongitude.setText(siteDetail.getLongitude());


                    // Get the existing adapter
                    ArrayAdapter<String> spinnerAdapter = (ArrayAdapter<String>) binding.spinnermediatype.getAdapter();

                    int position = -1;
                    for (int i = 0; i < spinnerAdapter.getCount(); i++) {
                        if (spinnerAdapter.getItem(i).equals(siteDetail.getMediaType())) {
                            position = i;
                            break;
                        }
                    }

// Set the selection if the item is found
                    if (position != -1) {
                        binding.spinnermediatype.setSelection(position);
                    }



                    // Get the existing adapter
                    ArrayAdapter<String> spinnerAdapter1 = (ArrayAdapter<String>) binding.spinnerillumination.getAdapter();

                    int position1 = -1;
                    for (int i = 0; i < spinnerAdapter1.getCount(); i++) {
                        if (spinnerAdapter1.getItem(i).equals(siteDetail.getIllumination())) {
                            position1 = i;
                            break;
                        }
                    }

// Set the selection if the item is found
                    if (position1 != -1) {
                        binding.spinnerillumination.setSelection(position1);
                    }




                    TextView tvStartDate = findViewById(R.id.etEndDate);
                    tvStartDate.setText(siteDetail.getEndDate());

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

            if(binding.etSiteName.getText().toString().equals("")||
               binding.etSiteNo.getText().toString().equals("")||
               binding.etStartDate.getText().toString().equals("")||
               binding.etEndDate.getText().toString().equals("")||
               binding.etLocation.getText().toString().equals("")||
               binding.etLatitude.getText().toString().equals("")||
               binding.etLongitude.getText().toString().equals("")||
               binding.etHeight.getText().toString().equals("")||
               binding.etWidth.getText().toString().equals("")||
               binding.etTotalArea.getText().toString().equals("")
            ) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        } else {
                //Log.d("fck", binding.etTotalArea.getText());
// Create a SiteDetail object
            //siteDetail = new SiteDetail();

// Set the site number
            TextView tvSiteId = findViewById(R.id.etSiteNo);
            siteDetail.setSiteNo(tvSiteId.getText().toString() != null ? tvSiteId.getText().toString() : "");

// Set the location
            TextView tvLocation = findViewById(R.id.etLocation);
            siteDetail.setLocation(tvLocation.getText().toString());

            try {
                if (!vendorId.equals("")) {
                    Log.d("tag222", "works");
                    siteDetail.setVendorId(vendorId);

                }else {
                    Log.d("tag222", "does not work");
                    siteDetail.setVendorId(siteDetail.getVendorId());
                }
                Log.d("vendorid", siteDetail.getVendorId());

            } catch (Exception e) {
                e.printStackTrace();//siteDetail.setCampaignId(campaignId);
                //siteDetail.setVendorId(siteDetail.getVendorId());

            }

// Set the site name
            TextView tvSiteName = findViewById(R.id.etSiteName);
            siteDetail.setName(tvSiteName.getText().toString());

// Set the start date
            TextView tvLastInspection = findViewById(R.id.etStartDate);
            siteDetail.setStartDate(tvLastInspection.getText().toString());

            // Set the end date
            TextView tvEndDate = findViewById(R.id.etEndDate);
            siteDetail.setEndDate(tvEndDate.getText().toString());

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
//Set created at
            siteDetail.setCreatedAt(setCurrentDate());
            siteDetail.setIllumination(selectedItem1);
            siteDetail.setMediaType(selectedItem);
            if(!vendorId.equals("")) {
                siteDetail.setVendorId(vendorId);
             }else{
                siteDetail.setVendorId(selectedVendor);

            }
            Log.d("tg33", selectedVendor);
// Set the total area
            TextView tvTotalArea = findViewById(R.id.etTotalArea);
            siteDetail.setTotalArea(tvTotalArea.getText().toString());

            Log.d("AddSiteDetailLog1",
                    "ID: " + siteDetail.getId() +
                            ", Vendor ID: " + siteDetail.getVendorId() +
                            ", Location: " + siteDetail.getLocation() +
                            ", Created At: " + siteDetail.getCreatedAt() +
                            ", End Date: " + siteDetail.getEndDate() +
                            ", Latitude: " + siteDetail.getLatitude() +
                            ", Longitude: " + siteDetail.getLongitude() +
                            ", Media Type: " + siteDetail.getMediaType() +
                            ", Illumination: " + siteDetail.getIllumination() +
                            ", Start Date: " + siteDetail.getStartDate() +
                            ", Name: " + siteDetail.getName() +
                            ", Site No: " + siteDetail.getSiteNo() +
                            ", Width: " + siteDetail.getWidth() +
                            ", Height: " + siteDetail.getHeight() +
                            ", Total Area: " + siteDetail.getTotalArea() +
                            ", Updated At: " + siteDetail.getUpdatedAt());

// Now you have populated the SiteDetail object with data from the EditText views

// Create a JSON object from the SiteDetail object
            JSONObject siteDetailJson = new JSONObject();
            try {
                Integer id = siteDetail.getId(); // Assuming getId() returns an Integer
                int idValue = id != null ? id : 0; // Replace 0 with your default value
                siteDetailJson.put("id", idValue);
                siteDetailJson.put("campaign_id", campaignId);
                if(vendorselected>1) {
                    siteDetailJson.put("vendor_id", selectedVendor);

                }else{
                    siteDetailJson.put("vendor_id", siteDetail.getVendorId());

                }
                siteDetailJson.put("location", siteDetail.getLocation() != null ? siteDetail.getLocation() : "");

                siteDetailJson.put("created_at", siteDetail.getCreatedAt() != null ? siteDetail.getCreatedAt() : "");
                siteDetailJson.put("end_date", siteDetail.getEndDate() != null ? siteDetail.getEndDate() : "");
                siteDetailJson.put("latitude", siteDetail.getLatitude() != null ? siteDetail.getLatitude() : "");
                siteDetailJson.put("longitute", siteDetail.getLongitude() != null ? siteDetail.getLongitude() : "");
                siteDetailJson.put("media_type", siteDetail.getMediaType() != null ? siteDetail.getMediaType() : "");
                siteDetailJson.put("illumination", siteDetail.getIllumination() != null ? siteDetail.getIllumination() : "");
                siteDetailJson.put("start_date", siteDetail.getStartDate() != null ? siteDetail.getStartDate() : "");
                siteDetailJson.put("name", siteDetail.getName() != null ? siteDetail.getName() : "");
                siteDetailJson.put("site_no", siteDetail.getSiteNo() != null ? siteDetail.getSiteNo() : "");
                siteDetailJson.put("width", siteDetail.getWidth() != null ? siteDetail.getWidth() : "");
                siteDetailJson.put("height", siteDetail.getHeight() != null ? siteDetail.getHeight() : "");
                siteDetailJson.put("total_area", siteDetail.getTotalArea() != null ? siteDetail.getTotalArea() : "");
                siteDetailJson.put("updated_at", siteDetail.getUpdatedAt() != null ? siteDetail.getUpdatedAt() : "");

                Log.d("tg77", siteDetailJson.toString());

                // Add more properties as needed
            } catch (JSONException e) {
                Log.d("tg9", e.toString());
                e.printStackTrace();
            }

            if (editingsite != null && editingsite.equals("yes")) {
                queryType = 2; //PUT
                siteno = Integer.toString(siteDetail.getId());
                //TODO pending from backend. Ask him if siteno is "" then make new site.- check notes for how to implement new site

                Log.d("tag11", siteDetailJson.toString());
                APIreferenceclass api = new APIreferenceclass(queryType, ctxt, loginToken, siteDetailJson.toString(), siteno, selectedImage);


            } else {
                //here make new call to add site
                queryType = 1; //POST
                siteno = siteDetail.getSiteNo();
                //TODO pending from backend. Ask him if siteno is "" then make new site.- check notes for how to implement new site
                Log.d("uri", selectedImage.toString());
                APIreferenceclass api = new APIreferenceclass(queryType, ctxt, loginToken, siteDetailJson.toString(), siteno, selectedImage, 1);
            }
        }
    }


    public String setCurrentDate() {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return currentDate;
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

    private static final int PICK_IMAGE = 1;

    public void dispatchTakePictureIntent() {
        // For Android 10 (API 29) and above, use the system's media picker
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            startActivityForResult(intent, PICK_IMAGE);
        } else {
            // Check if READ_EXTERNAL_STORAGE permission is granted for older versions
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Explain why the permission is needed and request it
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Show an explanation...
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
            } else {
                // Permission has already been granted, proceed with picking the image
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(intent, PICK_IMAGE);
            }
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


        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                selectedImage = data.getData();
                Log.d("tag2321", "image picked");
                // Use the Uri to load the image
                // You might need to use ContentResolver to get the actual image path or perform other operations depending on your use case



            }
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
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS_REQUEST_CODE);
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
        // Check for the READ_EXTERNAL_STORAGE permission
        else if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                showSnackbar("Permission was denied", "Settings", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    public void addMoreSiteClick(View view) {

        editingsite= null;
        //
        siteno= "";
        //TODO here
        TextView tvSiteId = findViewById(R.id.etSiteNo);
        //tvSiteId.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(tvSiteId, InputMethodManager.SHOW_IMPLICIT);


        ScrollView scrollView= findViewById(R.id.scrollview);
        scrollView.smoothScrollTo(0, 0);

        tvSiteId.setText("");


        TextView tvLocation = findViewById(R.id.etLocation);
        tvLocation.setText("");

        TextView tvSitename = findViewById(R.id.etSiteName);
        tvSitename.setText("");
        tvSitename.requestFocus();

        TextView tvSiteName = findViewById(R.id.tvAddSiteDetail);
        tvSiteName.setText("Add Site");

        TextView tvLastInspection = findViewById(R.id.etStartDate);
        tvLastInspection.setText("");

        TextView tvLatitude = findViewById(R.id.etLatitude);
        tvLatitude.setText("");

        TextView tvLongitude = findViewById(R.id.etLongitude);
        tvLongitude.setText("");

        //TODO

        //TextView tvMediaType = findViewById(R.id.tvMediaType);
        //tvMediaType.setText(siteDetail.getMediaType());

        // TextView tvIllumination = findViewById(R.id.tvIllumination);
        // tvIllumination.setText(siteDetail.getIllumination());

        TextView tvStartDate = findViewById(R.id.etEndDate);
        tvStartDate.setText("");

        // Set the site number
        TextView tvSiteNo = findViewById(R.id.etSiteNo);
        tvSiteNo.setText(""); // assuming getter method exists

        // Set the width
        TextView tvWidth = findViewById(R.id.etWidth);
        tvWidth.setText(""); // assuming getter method exists

        // Set the height
        TextView tvHeight = findViewById(R.id.etHeight);
        tvHeight.setText(""); // assuming getter method exists

        // Set the total area
        TextView tvTotalArea = findViewById(R.id.etTotalArea);
        tvTotalArea.setText(""); // assuming getter method exists

        //RoundRectCornerImageView tvImage = findViewById(R.id.ivCampaignImage);
        // if(siteDetail.getImage()!=null) {
        //    tvImage.setImageBitmap(siteDetail.getImage());
        // }
    }

    public void showSuccessMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_emailsent, null);
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        TextView tvResubmit = view.findViewById(R.id.tvResubmit);
        tvResubmit.setVisibility(View.INVISIBLE);
        if (editingsite == null || editingsite.isEmpty()) {
            tvMsg.setText("Site Added Successfully");
        } else {
            tvMsg.setText("Site Updated Successfully");
        }

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

    public void showUpdateSuccessMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_emailsent, null);
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        TextView tvResubmit = view.findViewById(R.id.tvResubmit);
        tvResubmit.setVisibility(View.INVISIBLE);
        if (editingsite == null || editingsite.isEmpty()) {
            tvMsg.setText("Site Updated Successfully");
        } else {
            tvMsg.setText("Site Updated Successfully");
        }

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
                String dateStr = String.format(Locale.getDefault(), "%04d-%02d-%02d", yy, mm + 1, dd);

                Log.d("tag322", Integer.toString(view.getId()));

                //TODO remove
                //dateStr= "2023-07-24";
                    binding.etStartDate.setText(dateStr);
                Log.d("tag322", dateStr);

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
                String dateStr = String.format(Locale.getDefault(), "%04d-%02d-%02d", yy, mm + 1, dd);

                Log.d("tag322", Integer.toString(view.getId()));
                //TODO remove
                //dateStr= "2023-07-24";
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
            selectedImage = null;
            JSONObject jsonobj2 = new JSONObject(response);
            if (jsonobj2.getString("message").equals("Vendors retrieved successfully.")) {
                vendorspinnerboolean = 0;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        vendorlist(response);
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonobj1 = new JSONObject(response);
                            Log.d("tg9", "jsonobj1: " + jsonobj1.toString());

                            Log.d("tg9", "here");

                            if (jsonobj1.getString("message").equals("Site created successfully.")) {
                                Log.d("tg9", "here1");

                                showSuccessMessage();
                                Log.d("tg9", "here2");

                            }else if (jsonobj1.getString("message").equals("Site updated successfully.")) {
                                Log.d("tg9", "here1");

                                showUpdateSuccessMessage();
                                Log.d("tg9", "here2");

                            } else {
                                showFailureMessage();
                            }
                        } catch (Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showFailureMessage();
                                }
                            });
                            e.printStackTrace();
                            Log.d("tagerw1", e.toString());
                        }
                    }
                });
            }}catch(Exception e){
                Log.d("tag123", e.toString());
            }

    }

    JSONArray jsonArray1;
    String vendorName;
int vendorselected;
    void vendorlist(String response){
        //TODO retreive client list
        Log.d("vendorlist", response);
        //TODO put this spinner code after response is received

        String[] items2= null;
        vendorName="";

        try{
            JSONObject json= new JSONObject(response);
            jsonArray1= json.getJSONArray("data");
            items2 = new String[jsonArray1.length()];

            for(int i=0; i<jsonArray1.length(); i++){
                JSONObject json1= jsonArray1.getJSONObject(i);
                items2[i]= json1.optString("name");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        final String[] items3= items2;

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        binding.spinnervendor.setAdapter(adapter2);


        // Inside your onCreate method
        binding.spinnervendor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //TODO put the client id of the client in below and then add it to the jsonobject that is sent to api
                Log.d("clicked", "fk");

                vendorselected+=1;
                Log.d("vendorselected", Integer.toString(vendorselected));
                selectedVendor = parent.getItemAtPosition(position).toString();

                try {
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject json1 = jsonArray1.getJSONObject(i);
                        items3[i] = json1.optString("name");
                        if(items3[i].equals(selectedVendor)){
                            Log.d("selectedvendor", json1.optString("id")+ " "+ selectedVendor);
                            selectedVendor= json1.optString("id");
                            vendorName= json1.optString("name");

                            break;
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


                Log.d("tg92", "selectedVendor"+ selectedVendor);


                //Toast.makeText(AddCampaignDetails.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

// Get the existing adapter
//        ArrayAdapter<String> vendorAdapter = (ArrayAdapter<String>) binding.spinnervendor.getAdapter();

        int position2 = -1;
        try{
        for (int i = 0; i < adapter2.getCount(); i++) {
            //Log.d("position2", Integer.toString(position2));
            //Log.d("position2", siteDetail.getVendorId());
            //Log.d("position2", adapter2.getItem(i));

            if (adapter2.getItem(i).equals(vendorName)) {
                position2 = i;
                break;
            }
        }}catch(Exception e){
            Log.d("tg333", e.toString());
        }

// Set the selection if the item is found
        if (position2 != -1) {
            Log.d("position2", Integer.toString(position2));
            binding.spinnervendor.setSelection(position2);
        }
    }

    public void showFailureMessage() {

        Log.d("tagerw", "in onfailure");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_emailsent, null);
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        TextView tvResubmit = view.findViewById(R.id.tvResubmit);
        tvResubmit.setVisibility(View.INVISIBLE);
        tvMsg.setText("Site Add Failed Please Check All The Fields");
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
