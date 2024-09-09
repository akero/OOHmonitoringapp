package com.acme.afsvendor.activity.dashboard;

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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import com.acme.afsvendor.R;
import com.acme.afsvendor.databinding.ActivityAddCampaignDetailsBinding;
import com.acme.afsvendor.utility.NetworkUtils;
import com.acme.afsvendor.viewmodel.APIreferenceclass;
import com.acme.afsvendor.viewmodel.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class AddCampaignDetails extends AppCompatActivity implements ApiInterface {

    private ActivityAddCampaignDetailsBinding binding;
    String siteNumber;
    String selectedItem;
    String selectedItem1;
    String selectedClient;
    int selectedVendor;
    private UploadHelper uploadHelper;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 102;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private final int REQUEST_IMAGE_CAPTURE = 101;
    Uri selectedImage;
    int clientspinnerboolean;
    int apiboolean;
    int vendorspinnerboolean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_campaign_details);
        logintoken= getIntent().getStringExtra("logintoken");
        selectedItem="";
        selectedItem1="";
        selectedClient="";
        selectedVendor=0;
        imageStream= null;
        selectedImage= null;
        clientspinnerboolean= 0;
        apiboolean= 0;
        vendorspinnerboolean= 0;

        Log.d("whichclass", "AddCampaignDetails");

        binding.etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        AddCampaignDetails.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                Log.d("date", year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                                binding.etStartDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                Log.d("date", String.valueOf(binding.etStartDate.getText()));


                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });

        binding.etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        AddCampaignDetails.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                binding.etEndDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });

        //spinner code
        String[] items = new String[]{"Billboard", "Unipole", "Hoarding", "Gantry", "BQS","LED Screen", "MUPI", "Digital Wall Print"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        binding.spinnermediatype.setAdapter(adapter);

        binding.spinnermediatype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                Log.d("tg92", selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        String[] items1 = new String[]{"Non Lit/Ambient Lit", "Front Lit", "Back Lit", "Digital"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        binding.spinnerillumination.setAdapter(adapter1);

        binding.spinnerillumination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem1 = parent.getItemAtPosition(position).toString();
                Log.d("tg92", selectedItem1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //end of spinner code

        //code to select client
        //  fill with client names- emailed

        FileHelper fh= new FileHelper();

        Context ctxt= this;
        try {
            clientspinnerboolean= 1;
            APIreferenceclass api = new APIreferenceclass(ctxt, fh.readLoginToken(this));
        }catch(Exception e){
            Log.d("tg343", e.toString());
        }

        try {
            vendorspinnerboolean= 1;
            APIreferenceclass api = new APIreferenceclass(ctxt, fh.readLoginToken(this), 1);
        }catch(Exception e){
            Log.d("tg343", e.toString());
        }
    }

    String logintoken;

    public void btnSaveClick(View view) {
        if (    binding.etName.getText().toString().isEmpty() ||
                //binding.etVendor.getText().toString().isEmpty() ||
                binding.etStartDate.getText().toString().isEmpty() ||
                binding.etEndDate.getText().toString().isEmpty() ||
                //TODO uncomment
                //binding.etclientid.getText().toString().isEmpty() ||
                binding.etnumsites.getText().toString().isEmpty() ||
                selectedItem.equals("")||
                selectedItem1.equals("")){

            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_LONG).show();
        } else if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "Check your Internet Connection and Try Again", Toast.LENGTH_LONG).show();
        } else {

            String name=binding.etName.getText().toString();
            //String vendor=binding.etVendor.getText().toString();
            String startdate=binding.etStartDate.getText().toString();
            String enddate=binding.etEndDate.getText().toString();

            //TODO uncomment
            //String clientid=binding.etclientid.getText().toString();
            String numsites=binding.etnumsites.getText().toString();
            String mediatype=selectedItem;
            String illumination=selectedItem1;

            JSONObject jsonPayload= new JSONObject();
            try{
                //String base64Image = Base64.encodeToString(imageStream.toByteArray(), Base64.DEFAULT);
                //jsonPayload.put("image", base64Image);
                jsonPayload.put("name", name);
                jsonPayload.put("vendor", selectedVendor);
                jsonPayload.put("start_date", startdate);
                jsonPayload.put("end_date", enddate);
                jsonPayload.put("client_id", selectedClient);
                jsonPayload.put("num_of_site", numsites);
                //jsonPayload.put("image", imageStream);


                //TODO fill below 2 with correct values

                jsonPayload.put("media_type", mediatype);
                jsonPayload.put("illumination", illumination);
                jsonPayload.put("uid", 1);

                FileHelper fh= new FileHelper();
                jsonPayload.put("user_id", fh.readUserId(this));

            }catch(Exception e){
                Log.d("tg6", e.toString());
            }Log.d("tg6", selectedImage.toString());
            apiboolean= 1;

            Log.d("tg66", jsonPayload.toString());

            APIreferenceclass api= new APIreferenceclass(jsonPayload, this, logintoken, selectedImage);
            selectedImage= null;
        }
    }

    JSONArray jsonArray;

    void clientlist(String response){
        //TODO retreive client list

        Log.d("clientlist", response);
        //TODO put this spinner code after response is received

        String[] items2= null;

        try{
            JSONObject json= new JSONObject(response);
            jsonArray= json.getJSONArray("data");
            items2 = new String[jsonArray.length()];

            for(int i=0; i<jsonArray.length(); i++){
                JSONObject json1= jsonArray.getJSONObject(i);
                items2[i]= json1.optString("name");
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        final String[] items3= items2;

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        binding.spinnermediatype1.setAdapter(adapter2);

        // Inside your onCreate method
        binding.spinnermediatype1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //TODO put the client id of the client in below and then add it to the jsonobject that is sent to api



                selectedClient = parent.getItemAtPosition(position).toString();

                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json1 = jsonArray.getJSONObject(i);
                        items3[i] = json1.optString("name");
                        if(items3[i].equals(selectedClient)){
                            Log.d("selectedclient", json1.optString("id")+ " "+ selectedClient);
                            selectedClient= json1.optString("id");

                            break;
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


                Log.d("tg92", "selectedClient"+ selectedClient);


                //Toast.makeText(AddCampaignDetails.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });


    }

    //for vendor fetch
    JSONArray jsonArray1;

    void vendorlist(String response){
        //TODO retreive client list

        Log.d("vendorlist", response);
        //TODO put this spinner code after response is received

        String[] items2= null;

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

                String selectedvendorint="";

                selectedvendorint = parent.getItemAtPosition(position).toString();

                try {
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject json1 = jsonArray1.getJSONObject(i);
                        items3[i] = json1.optString("name");
                        if(items3[i].equals(selectedvendorint)){
                            Log.d("selectedvendor", json1.toString());
                            Log.d("selectedvendor", json1.optString("id")+ " "+ selectedVendor);
                            selectedVendor= json1.optInt("id");
                            Log.d("selectedvendor", Integer.toString(selectedVendor));

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


    }

    @Override
    public void onResponseReceived(String response) {

        Log.d("spinnerresponse", response);

        JSONObject jsono= null;
        try {

            jsono = new JSONObject(response);



        if(jsono.getString("message").equals("Vendors retrieved successfully.")){
            vendorspinnerboolean=0;
            runOnUiThread(new Runnable() {
                @Override
                public void run() { vendorlist(response);
                }
            });
        }else if(jsono.getString("message").equals("Clients retrieved successfully.")){
            clientspinnerboolean=0;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    clientlist(response);
                }
            });
        }else{
            apiboolean= 0;
            Log.d("tg9", "response "+response);
        try {
            JSONObject jsonobj = new JSONObject(response);

            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    try{
                        if(jsonobj.getBoolean("success")== true){
                            binding.etName.setText("");
                            //binding.etVendor.setText("");
                            binding.etStartDate.setText("");
                            binding.etEndDate.setText("");
                            binding.etnumsites.setText("");
                            //TODO uncomment
                            //binding.etclientid.setText("");
                            showSuccessMessage();
                        }
                        else{
                            showFailureMessage();
                        }
                    }catch (Exception e){
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                showFailureMessage();
                            }});
                        Log.d("tagerw1", e.toString());
                    }
                }

            });}catch(Exception e){
            Log.d("tag123", e.toString());
        }}
        }catch (Exception e){
            e.printStackTrace();
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
        tvMsg.setText("Campaign Add Failed Please Check All The Fields");
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

    public void showSuccessMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_emailsent, null);

        TextView tvMsg = view.findViewById(R.id.tvMsg);
        TextView tvResubmit = view.findViewById(R.id.tvResubmit);
        tvResubmit.setVisibility(View.GONE);
        tvMsg.setText("Campaign Added Successfully");

        Button btnClose = view.findViewById(R.id.btnClose);
        builder.setView(view);
        final AlertDialog dialog = builder.create();

        if (dialog != null) {
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        if (dialog != null) {
            dialog.show();
        }
    }

    ByteArrayOutputStream imageStream;
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
        uploadHelper.handleActivityResult(requestCode, resultCode, data);
        imageStream= null;
        imageStream= uploadHelper.getImageStream();}
        catch(Exception e){
            Log.d("tag998", e.toString());
        }
        // Use the image stream as needed
    }
*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            binding.ivCampaignImage.setImageBitmap(imageBitmap);
            //getlatlong();
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
    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 101;

    private void requestReadStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

  /*  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open gallery
                if (uploadHelper == null) {
                    uploadHelper = new UploadHelper(this);
                }
                uploadHelper.openGallery();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission to access storage is required to select an image", Toast.LENGTH_LONG).show();
            }
        }
    }
*/
    public void btnCloseClick(View view) {
        finish();
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

    private void showSnackbar(String mainTextStringId, String actionStringId, View.OnClickListener listener) {
        Toast.makeText(this, mainTextStringId, Toast.LENGTH_LONG).show();
    }
}
