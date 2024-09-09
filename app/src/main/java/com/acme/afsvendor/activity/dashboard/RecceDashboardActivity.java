package com.acme.afsvendor.activity.dashboard;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.acme.afsvendor.activity.login.OTP;
import com.acme.afsvendor.utility.RoundRectCornerImageView;
import com.acme.afsvendor.viewmodel.APIreferenceclass;
import com.acme.afsvendor.viewmodel.ApiInterface;
import com.acme.afsvendor.viewmodel.SiteDetail;
import com.github.gcacace.signaturepad.views.SignaturePad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.acme.afsvendor.databinding.ActivityRecceDashboardBinding;

import com.acme.afsvendor.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.StringTokenizer;

//TODO rearrange UI accoding to data required for store photos.
//TODO implement all api calls.



public class RecceDashboardActivity extends AppCompatActivity implements ApiInterface, LocationCallback {

    private AppBarConfiguration appBarConfiguration;
    private ActivityRecceDashboardBinding binding;
    boolean wassendbuttonpressed;
    String currentDate;
    private static final int REQUEST_CODE_PERMISSIONS = 123;
    static final int REQUEST_TAKE_PHOTO = 1;
    private final Context ctxt = this;
    String selectedClient;

    boolean pictureandlatlongready; //in imageUri and latlong

    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION
            //Manifest.permission.MANAGE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE
    };

    Boolean locationtaken, picturetaken;
    String latlong, logintoken;
    String ownerpermission, owneraltno, brand;
    int piccounter; //0 init, 1, 2, 3, 4, 5, 6
    boolean pic1taken, pic2taken, pic3taken, pic4taken, picsigntaken, picsitetaken;
    Uri pic1takenURI, pic2takenURI, pic3takenURI, pic4takenURI, picsigntakenURI, picsitetakenURI;
    int storephoto;//0 for init, 1 for store photo, 2 for sign, 3 for main upload with other stuff
    int recceid, clientspinnerboolean;
    ProgressBar progressBar;
    Animation rotateAnimation;
    public static final int REQUEST_SIGN= 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding = ActivityRecceDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (savedInstanceState != null) {
            retailer_code = savedInstanceState.getString("retailer_code");
            division = savedInstanceState.getString("division");
            asm_name = savedInstanceState.getString("asm_name");
            asm_contact = savedInstanceState.getString("asm_contact");
            retailer_code= savedInstanceState.getString("retailer_code");
        }

        binding.etBrand.setText("");
        retailer_code= "";
        division= "";
        asm_name= "";
        asm_contact= "";
        currentDate= "";
        owneraltno= "";
        ownerpermission= "Accepted";
        brand= "";
        wassendbuttonpressed= false;
        clientspinnerboolean= 0;
        piccounter= 0;
        recceid= 0;
        pic1takenURI= null; pic2takenURI= null; pic3takenURI= null; pic4takenURI= null; picsigntakenURI= null; picsitetakenURI= null;
        allpicturestaken= false; pic1taken= false; pic2taken= false; pic3taken= false; pic4taken= false; picsigntaken= false; picsitetaken= false;
        selectedClient= "";
        //binding.etWidth33.setText("");

        picsigntaken= true;
        //animation code
        progressBar= findViewById(R.id.progressBar);
        rotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_animation);
        //animation code

        binding.etTotalArea.setText("");



        try{
            FileHelper fh= new FileHelper();


            recceid= Integer.parseInt(fh.readUserId(this));
        }catch (Exception e){
            e.printStackTrace();
        }

        //init locationhelper
        locationHelper = new LocationHelper();
        picturetaken = false;
        latlong = "";
        locationtaken = false;
        pictureandlatlongready = false;
        storephoto= 0;

        try {
            FileHelper fh = new FileHelper();
            logintoken = fh.readLoginToken(this);

        } catch (Exception e) {
            Log.d("tg223", e.toString());
        }


        //initializing photo buttons
        Button storePhotoButton, storePhotoButton1, storePhotoButton2, storePhotoButton3, ownerSignButton, uploadSiteDetailsButton;
        storePhotoButton = findViewById(R.id.btnUpdatePhoto);
        storePhotoButton1 = findViewById(R.id.btnUpdatePhoto1);
        storePhotoButton2 = findViewById(R.id.btnUpdatePhoto2);
        storePhotoButton3 = findViewById(R.id.btnUpdatePhoto3);
        ownerSignButton = findViewById(R.id.btnUpdatePhoto4);

        RadioGroup radioGroup = findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioYes:
                        // Handle the case when radioYes is checked
                        ownerpermission= "Accepted";

                        break;
                    case R.id.radioNo:
                        // Handle the case when radioNo is checked
                        ownerpermission= "Rejected";
                        break;
                }
            }
        });

        binding.ivReplace.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View view) {
                                                    Intent intent= new Intent(RecceDashboardActivity.this, ReplaceCodeActivity.class);
                                                    startActivity(intent);

                                                 }
                                             });


        storePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("camera", "click registered");
                if (ContextCompat.checkSelfPermission(RecceDashboardActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(RecceDashboardActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {//||ContextCompat.checkSelfPermission(ViewSiteDetailActivity.this, WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(RecceDashboardActivity.this, "Please give camera permissions", Toast.LENGTH_SHORT).show();

                    ActivityCompat.requestPermissions(RecceDashboardActivity.this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                    Log.d("camera", "dont have permission");
                } else {

                    // temporaryuploadchecker();
                    //latlong();
                    Log.d("latlong", latlong);

                    storephoto= 1;
                    piccounter= 1;
                    openCamera();
                }
            }
        });

        storePhotoButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("camera", "click registered");
                if (ContextCompat.checkSelfPermission(RecceDashboardActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(RecceDashboardActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {//||ContextCompat.checkSelfPermission(ViewSiteDetailActivity.this, WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(RecceDashboardActivity.this, "Please give camera permissions", Toast.LENGTH_SHORT).show();

                    ActivityCompat.requestPermissions(RecceDashboardActivity.this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                    Log.d("camera", "dont have permission");
                } else {

                    // temporaryuploadchecker();
                    //latlong();
                    Log.d("latlong", latlong);

                    storephoto= 1;
                    piccounter= 2;
                    openCamera();
                }
            }
        });

        storePhotoButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("camera", "click registered");
                if (ContextCompat.checkSelfPermission(RecceDashboardActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(RecceDashboardActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {//||ContextCompat.checkSelfPermission(ViewSiteDetailActivity.this, WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(RecceDashboardActivity.this, "Please give camera permissions", Toast.LENGTH_SHORT).show();

                    ActivityCompat.requestPermissions(RecceDashboardActivity.this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                    Log.d("camera", "dont have permission");
                } else {

                    // temporaryuploadchecker();
                    //latlong();
                    Log.d("latlong", latlong);

                    storephoto= 1;
                    piccounter= 3;
                    openCamera();
                }
            }
        });

        storePhotoButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("camera", "click registered");
                if (ContextCompat.checkSelfPermission(RecceDashboardActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(RecceDashboardActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {//||ContextCompat.checkSelfPermission(ViewSiteDetailActivity.this, WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(RecceDashboardActivity.this, "Please give camera permissions", Toast.LENGTH_SHORT).show();

                    ActivityCompat.requestPermissions(RecceDashboardActivity.this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                    Log.d("camera", "dont have permission");
                } else {

                    // temporaryuploadchecker();
                    //latlong();
                    Log.d("latlong", latlong);

                    storephoto= 1;
                    piccounter= 4;
                    openCamera();
                }
            }
        });


        ownerSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("signature", "click registered");
                //if (ContextCompat.checkSelfPermission(RecceDashboardActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(RecceDashboardActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {//||ContextCompat.checkSelfPermission(ViewSiteDetailActivity.this, WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

                    //Toast.makeText(RecceDashboardActivity.this, "Please give camera permissions", Toast.LENGTH_SHORT).show();

                    //ActivityCompat.requestPermissions(RecceDashboardActivity.this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                    Log.d("camera", "dont have permission");
                //} else {

                    // temporaryuploadchecker();
                    //latlong();
                    Log.d("latlong", latlong);

                    storephoto= 2;
                    piccounter= 5;
                    //openCamera();

                Intent intent = new Intent(RecceDashboardActivity.this, SignatureActivity.class);
                startActivityForResult(intent, REQUEST_SIGN);



                }


        });

        //fetch retailer details and populate automatically
        binding.btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                APIreferenceclass api= new APIreferenceclass( ctxt, logintoken, binding.etFetch.getText().toString().toUpperCase());
            }
        });

        binding.etWidth9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                latlong();}
        });

        binding.etHeight9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                latlong();
            }
        });

//date

        // Get the current date and time
        Calendar calendar = Calendar.getInstance();

// Get the year, month, and day components of the date
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Month is 0-based, so we add 1
        int day = calendar.get(Calendar.DAY_OF_MONTH);

// Get the hour and minute components of the time
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // 24-hour format
        int minute = calendar.get(Calendar.MINUTE);

// Create a string representation of the date and time
        String currentDateTime = String.format("%04d-%02d-%02d, %02d:%02d",
                year, month, day, hour, minute);

// You can also use SimpleDateFormat to get a formatted date and time string
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd, HH:mm");
        String formattedDateTime = dateTimeFormat.format(calendar.getTime());

// Print the current date and time
        System.out.println("Current date and time: " + currentDateTime);
        System.out.println("Formatted date and time: " + formattedDateTime);

// Set the formatted date and time to the EditText
        binding.etWidth3.setText(formattedDateTime);

        binding.ivNotification.setOnClickListener(new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View view) {

                                                          logout();

                                                      }
                                                  }
        );

        binding.ivHistory.setOnClickListener(new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View view) {

                                                          Intent intent= new Intent(RecceDashboardActivity.this, RecceHistory.class);
                                                          intent.putExtra("logintoken", logintoken);
                                                          intent.putExtra("id", recceid);
                                                          Log.d("id", Integer.toString(recceid));
                                                          startActivity(intent);
                                                      }
                                                  }
        );

        binding.etHeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    try {
                        double area= Double.parseDouble( binding.etHeight.getText().toString())*Double.parseDouble( binding.etWidth.getText().toString());
                        Log.d("area", String.valueOf(area));
                        //jsonPayload.put("area", String.valueOf(area));
                        binding.etarea.setText(String.valueOf(area));

                    } catch (NumberFormatException e) {
                        // Handle the case where the input is not a valid double
                        e.printStackTrace();
                        //width = 0.0; // or any other default value you prefer
                    }

                }
            }
        });

        //photo of sign
        RoundRectCornerImageView imageButton = findViewById(R.id.ivCampaignImage);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("camera", "click registered");
                if (ContextCompat.checkSelfPermission(RecceDashboardActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(RecceDashboardActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {//||ContextCompat.checkSelfPermission(ViewSiteDetailActivity.this, WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(RecceDashboardActivity.this, "Please give camera and location permissions", Toast.LENGTH_SHORT).show();

                    ActivityCompat.requestPermissions(RecceDashboardActivity.this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                    Log.d("camera", "dont have permission");
                } else {

                    // temporaryuploadchecker();
                    latlong();
                    Log.d("latlong", latlong);
                    //TODO uncomment

                    storephoto= 3;
                    piccounter= 6;
                    openCamera();
                }
            }

        });

        /*Context ctxt= this;
        try {
            clientspinnerboolean= 1;
            APIreferenceclass api = new APIreferenceclass(ctxt, logintoken);
        }catch(Exception e){
            Log.d("tg343", e.toString());
        }
*/
        Button uploadSignageDetails = findViewById(R.id.btnUpdatePhoto5);

        uploadSignageDetails.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        if (binding.etHeight.getText().toString().isEmpty() || binding.etWidth.getText().toString().isEmpty() ||  binding.etWidth1.getText().toString().isEmpty() || binding.etHeight2.getText().toString().isEmpty() || binding.etWidth2.getText().toString().isEmpty() || binding.etHeight3.getText().toString().isEmpty() || binding.etWidth3.getText().toString().isEmpty() || binding.etHeight4.getText().toString().isEmpty() ||  binding.etHeight5.getText().toString().isEmpty() || binding.etWidth5.getText().toString().isEmpty() ||  !pictureandlatlongready) {
                                                            Toast.makeText(ctxt, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                                                            Log.d("tag222", "1"+ binding.etHeight.getText().toString()+ "2"+ binding.etWidth.getText().toString()+"3"+ "4"+ binding.etWidth1.getText().toString()+"5"+ binding.etHeight2.getText().toString()+"6"+ binding.etWidth2.getText().toString()+"7"+ binding.etHeight3.getText().toString()+"8"+ binding.etWidth3.getText().toString()+"9"+ binding.etHeight4.getText().toString()+"10"+ binding.etWidth4.getText().toString()+"11"+ binding.etHeight5.getText().toString()+"12"+ binding.etWidth5.getText().toString()+"13"+ "14"+"15"+ Boolean.toString(pictureandlatlongready));
                                                            wassendbuttonpressed= true;
                                                            //progressBar.setVisibility(View.VISIBLE);
                                                            //progressBar.startAnimation(rotateAnimation);
                                                            latlong();
                                                        } else {
                                                            //api call

                                                            //animation code
                                                            progressBar.setVisibility(View.VISIBLE);
                                                            progressBar.startAnimation(rotateAnimation);
                                                            //animation code
                                                            apicall();

                                                        }

                                                        //TODO add check for image
                                                    }
                                                }
        );

        binding.btnUpdatePhoto6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetAndReinitialize();
            }
        });
    }



    public void resetAndReinitialize() {
        // Perform any necessary cleanup or data saving operations here

        // Finish the current activity instance
        finish();

        // Start a new instance of the same activity
        startActivity(getIntent());
        //overridePendingTransition(0, 0); // Optionally, you can remove the transition animation
    }

    void apicall() {
        String lat = "";
        String longitude = "";
        try {
            StringTokenizer str = new StringTokenizer(latlong, ",");
            lat = str.nextToken();
            longitude = str.nextToken();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject jsonPayload = new JSONObject();
        try {

            //jsonPayload.put("project", binding.etHeight1.getText().toString());
            jsonPayload.put("state", binding.etWidth1.getText().toString());
            jsonPayload.put("district", binding.etHeight2.getText().toString());
            jsonPayload.put("city", binding.etWidth2.getText().toString());
            jsonPayload.put("retail_name", binding.etHeight3.getText().toString());
            jsonPayload.put("length", binding.etHeight.getText().toString());
            jsonPayload.put("width", binding.etWidth.getText().toString());
            jsonPayload.put("code", binding.etFetch.getText().toString());
            jsonPayload.put("shop_owner_status", ownerpermission);
            jsonPayload.put("brand", binding.etBrand.getText().toString());
            jsonPayload.put("owner_alt_num", binding.etOwnerAlternateNumber.getText().toString());
            jsonPayload.put("alternate_shop_name", binding.etBrand.getText().toString());

            try {
                double area= Double.parseDouble( binding.etHeight.getText().toString())*Double.parseDouble( binding.etWidth.getText().toString());
                Log.d("area", String.valueOf(area));
                jsonPayload.put("area", String.valueOf(area));

            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid double
                e.printStackTrace();
                //width = 0.0; // or any other default value you prefer
            }

            //jsonPayload.put("area", binding.etWidth.getText().toString());
            jsonPayload.put("date", binding.etWidth3.getText().toString());
            jsonPayload.put("owner_name", binding.etHeight4.getText().toString());
            Log.d("owner name", binding.etHeight4.getText().toString());
            jsonPayload.put("email", binding.etWidth4.getText().toString());

            Log.d("owner email", binding.etWidth4.getText().toString());
            jsonPayload.put("mobile", binding.etHeight5.getText().toString());
            Log.d("owner mobile", binding.etHeight5.getText().toString());
            jsonPayload.put("remarks", binding.etTotalArea.getText().toString());
            jsonPayload.put("location", binding.etTotalArea1.getText().toString());
            //jsonPayload.put("area", binding.area.getText().toString());
            jsonPayload.put("lat", lat);
            //jsonPayload.put("client_id", selectedClient);
            jsonPayload.put("long", longitude);
            jsonPayload.put("retailer_code", retailer_code);
            jsonPayload.put("asm_name", asm_name);
            jsonPayload.put("asm_mobile", asm_contact);
            jsonPayload.put("division", division);


            FileHelper fh = new FileHelper();
            jsonPayload.put("created_by", fh.readUserId(this));

        } catch (Exception e) {
            Log.d("tg6", e.toString());
        }
        //Log.d("tg6", imageUri.toString());
        //apiboolean= 1;

        //Log.d("tg66", jsonPayload.toString());



        //TODO here

        APIreferenceclass api = new APIreferenceclass(jsonPayload, this, logintoken, recceid, pic1takenURI, pic2takenURI,pic3takenURI, pic4takenURI, picsigntakenURI, picsitetakenURI);
        imageUri = null;
    }


    private LocationHelper locationHelper;

    void latlong() {

        locationHelper.requestLocationPermission(this, this);
    }

    public void resetFields(View view) {
        // Reset all EditText fields except "recce" and "signage board"
        resetEditTextFields(R.id.etFetch, R.id.etHeight, R.id.etWidth, R.id.etWidth1,
                R.id.etHeight2, R.id.etWidth2, R.id.etHeight3, R.id.etWidth3, R.id.etHeight4,
                R.id.etWidth4, R.id.etHeight5, R.id.etWidth5, R.id.etTotalArea, R.id.etTotalArea1,
                R.id.area);

        // Reset all Button backgrounds
        //resetButtonBackgrounds(R.id.btnFetch, R.id.btnUpdatePhoto, R.id.btnUpdatePhoto1, R.id.btnUpdatePhoto2,
          //      R.id.btnUpdatePhoto3, R.id.btnUpdatePhoto4, R.id.btnUpdatePhoto5);

        // Change background resource of all buttons and "rlAddImage" to "drawable/primarystroke"
        int[] viewIds = {R.id.btnFetch, R.id.btnUpdatePhoto, R.id.btnUpdatePhoto1, R.id.btnUpdatePhoto2,
                R.id.btnUpdatePhoto3, R.id.btnUpdatePhoto4, R.id.btnUpdatePhoto5, R.id.rlAddImage};
        setBackgroundResource(viewIds, R.drawable.primarystroke);
    }

    private void resetEditTextFields(int... editTextIds) {
        for (int id : editTextIds) {
            EditText editText = findViewById(id);
            if (editText != null) {
                editText.setText("");
            }
        }
    }

    private void resetButtonBackgrounds(int... buttonIds) {
        for (int id : buttonIds) {
            View view = findViewById(id);
            if (view != null) {
                view.setBackgroundResource(android.R.drawable.btn_default);
            }
        }
    }

    private void setBackgroundResource(int[] viewIds, int backgroundResource) {
        for (int id : viewIds) {
            View view = findViewById(id);
            if (view != null) {
                view.setBackgroundResource(backgroundResource);
            }
        }
    }


    Uri photoURI;

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoURI = null;
       // if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Handle error
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(RecceDashboardActivity.this,
                        "com.acme.afsvendor.fileprovider",
                        photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                Log.d("opencamerauri", photoURI.toString());
                startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
            }
     //   } else {
   //         Log.d("camera", "no permission1");
    //        Toast.makeText(RecceDashboardActivity.this, "Don't have camera permissions", Toast.LENGTH_SHORT).show();

     //   }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getFilesDir();
        Log.d("tag222", "created image");
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

    //auto opens when addimage is clicked
    public void addImage() {

    }

    Uri imageUri;
boolean allpicturestaken;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // Image captured successfully
            // Access the image file using the Uri you provided earlier

           // Log.d("tag22", data.toString());
            Uri imageUri1;

            try {
                imageUri = FileProvider.getUriForFile(this,
                        "com.acme.afsvendor.fileprovider",
                        createImageFile());
                imageUri1= imageUri;
                Log.d("tag222", imageUri.toString());

                if(piccounter== 1){
                    Log.d("pic", "1");
                    pic1taken= true;
                    binding.btnUpdatePhoto.setText("Retake Store Photo 1");
                    binding.btnUpdatePhoto.setBackgroundResource(R.drawable.primarystrokegreen);
                    pic1takenURI= photoURI;
                }else if(piccounter== 2){
                    pic2taken= true;
                    binding.btnUpdatePhoto1.setText("Retake Store Photo 2");
                    binding.btnUpdatePhoto1.setBackgroundResource(R.drawable.primarystrokegreen);

                    Log.d("pic", "2");
                    pic2takenURI= photoURI;
                }else if(piccounter== 3){
                    pic3taken= true;
                    binding.btnUpdatePhoto2.setText("Retake Store Photo 3");
                    binding.btnUpdatePhoto2.setBackgroundResource(R.drawable.primarystrokegreen);

                    Log.d("pic", "3");
                    pic3takenURI= photoURI;
                }else if(piccounter== 4){
                    pic4taken= true;
                    binding.btnUpdatePhoto3.setText("Retake Store Photo 4");
                    binding.btnUpdatePhoto3.setBackgroundResource(R.drawable.primarystrokegreen);

                    Log.d("pic", "4");
                    pic4takenURI= photoURI;
                }else if(piccounter== 5){
                    picsigntaken= true;
                    binding.btnUpdatePhoto4.setText("Retake Signature");
                    binding.btnUpdatePhoto4.setBackgroundResource(R.drawable.primarystrokegreen);

                    Log.d("pic", "5");
                    //picsigntakenURI= photoURI;
                }else if(piccounter== 6){
                    picsitetaken= true;
                    binding.tvAddSite.setText("Retake Site Photograph");
                    binding.rlAddImage.setBackgroundResource(R.drawable.primarystrokegreen);

                    Log.d("pic", "6");
                    picsitetakenURI= photoURI;
                }
                if(pic1taken&&pic2taken&&pic3taken&&pic4taken&&picsigntaken&&picsitetaken){
                    allpicturestaken= true;
                    Log.d("pic", "allpicturetakentrue");
                    if(locationtaken){
                        Log.d("pic", "pictureandlatlongreadytrue");
                        pictureandlatlongready= true;
                    }else{
                        latlong();
                    }
                }


                picturetaken = true;
               // if (locationtaken && allpicturestaken) {//main upload
                 //   pictureandlatlongready = true;
                   // apicall();

                    //Log.d("tag22", "activity result works.");
                //}
             //   else if(storephoto== 1){//store photo upload

               //     apicallforstorephoto();
               // }else if(storephoto== 2){

                 //   apicallforsign();
                //}

                Log.d("tag22", "activity result works");

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Image update failed", Toast.LENGTH_SHORT).show();
            }
            // Do something with the imageUri, e.g., display the image or upload it
        }else if (resultCode == SignatureActivity.RESULT_SIGN) {  // Check for your unique result code
            Uri sign = data.getParcelableExtra("sign");
            // Use the value
            if(sign!= null){
                Log.d("sign", "not null");
            }else{
                Log.d("sign", "null");
            }
            picsigntakenURI= sign;
            picsigntaken= true;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.btnUpdatePhoto4.setText("Retake Signature");
                    binding.btnUpdatePhoto4.setBackgroundResource(R.drawable.primarystrokegreen);
                    Toast.makeText(RecceDashboardActivity.this, "Signature saved", Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            Log.d("tag22", "something went wrong");
        }
    }

    void logout() {

        try {
            FileHelper fh = new FileHelper();
            fh.writeUserType(this, "");

            Intent intent= new Intent(RecceDashboardActivity.this, OTP.class);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            boolean cameraGranted = false;
            boolean locationGranted = false;
            boolean storageGranted = false;
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.CAMERA) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    cameraGranted = true;
                    Log.d("permissions", "camera");
                } else if (permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    locationGranted = true;

                    Log.d("permissions", "location");
                } else if (permissions[i].equals(WRITE_EXTERNAL_STORAGE) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    storageGranted = true;

                    Log.d("permissions", "storage");
                }
            }

            if (cameraGranted && locationGranted) {
                // Both permissions granted, proceed with your app logic
                // temporaryuploadchecker();
                latlong();
                Log.d("latlong", latlong);
                //TODO uncomment

                openCamera();
            } else {
                // At least one permission was denied, handle accordingly
                Toast.makeText(RecceDashboardActivity.this, "Location/camera permission denied", Toast.LENGTH_SHORT).show();

            }
        }

    }


    @Override
    public void callback(String a) {
        if (!locationtaken) {
            latlong = a;
            locationtaken = true;
            Log.d("pic", "locationtakentrue");
            if (allpicturestaken) {
                Log.d("pic", "pictureandlatlongreadytrue");
                pictureandlatlongready = true;
                if(wassendbuttonpressed){
                    Log.d("wasendbuttonpressed", "true");
                    apicall();
                }
               // apicallmain(latlong);
            }
        }else{
            latlong= a;
            locationtaken= true;
            if (allpicturestaken) {
                Log.d("pic", "pictureandlatlongreadytrue");
                pictureandlatlongready = true;
                if(wassendbuttonpressed){
                    Log.d("wasendbuttonpressed", "true");
                    apicall();
                }

        }
        Log.d("tag22", "inside callback, latlong " + latlong + "locationtaken" + locationtaken);
    }

        try {
            StringTokenizer str = new StringTokenizer(latlong, ",");
            binding.etHeight9.setText(str.nextToken());
            binding.etWidth9.setText(str.nextToken());
        } catch (Exception e) {
            Log.d("foo", e.toString());
        }


    }

    void apicallmain(){

    }

    String response1;


    @Override
    public void onResponseReceived(String response) {

        Log.d("response", response);
        JSONObject jsono= null;
        try {

            jsono = new JSONObject(response);

    if(jsono.getString("message").equals("Clients retrieved successfully.")){
            clientspinnerboolean=0;
        response1 = response;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    clientlist(response);
                }
            });
        }else if(jsono.getString("message").equals("Data fetched successfully!")){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Log.d("onresponse", "5");
                Toast.makeText(RecceDashboardActivity.this, "Data fetched successfully", Toast.LENGTH_SHORT).show();
                hideKeyboard();
                fillretailerdata(response);
            }
        });

    }else if(response.equals("Error- no internet")){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //animation code
                progressBar.clearAnimation();
                progressBar.setVisibility(View.GONE);
                //animation code
                Log.d("onresponse", "4");

                Toast.makeText(RecceDashboardActivity.this, response, Toast.LENGTH_LONG).show();
            }
        });

            }else if(jsono.getString("message").equals("Data saved successfully!")){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //animation code
                progressBar.clearAnimation();
                progressBar.setVisibility(View.GONE);
                //animation code

                Log.d("onresponse", "3");

                Toast.makeText(RecceDashboardActivity.this, "Data saved successfully", Toast.LENGTH_LONG).show();
                resetAndReinitialize();
            }
        });

    }else if(Objects.requireNonNull(response.contains("The asm_name field is mandatory."))){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //animation code
                progressBar.clearAnimation();
                progressBar.setVisibility(View.GONE);
                //animation code
                Log.d("onresponse", "2");

                Toast.makeText(RecceDashboardActivity.this, "The ASM name field is mandatory", Toast.LENGTH_LONG).show();
            }
        });

    }
    else{
        response1 = response;
        Log.d("tag222 response is", response);
        response1 = response;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //animation code
                progressBar.clearAnimation();
                progressBar.setVisibility(View.GONE);
                //animation code

                Log.d("onresponse", "1");

                Toast.makeText(RecceDashboardActivity.this, response, Toast.LENGTH_LONG).show();
            }
        });


    }
        //implementUI(response);
        }catch (Exception e){
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //animation code
                    progressBar.clearAnimation();
                    progressBar.setVisibility(View.GONE);
                    //animation code
                    if(response.equals("Error- no internet")){



                                //animation code
                                progressBar.clearAnimation();
                                progressBar.setVisibility(View.GONE);
                                //animation code
                                Log.d("onresponse", "4");

                                Toast.makeText(RecceDashboardActivity.this, response, Toast.LENGTH_LONG).show();
                            }


                    else if(Objects.requireNonNull(response.contains("The asm_name field is mandatory."))){


                                //animation code
                                progressBar.clearAnimation();
                                progressBar.setVisibility(View.GONE);
                                //animation code
                                Log.d("onresponse", "2");

                                Toast.makeText(RecceDashboardActivity.this, "The ASM name field is mandatory", Toast.LENGTH_LONG).show();


                    }else if(response.equals("Error- owner email must be unique")){

                                //animation code
                                progressBar.clearAnimation();
                                progressBar.setVisibility(View.GONE);
                                //animation code
                                Log.d("onresponse", "3");
                                Toast.makeText(RecceDashboardActivity.this, "Error- owner email must be unique", Toast.LENGTH_LONG).show();

                           }else if(Objects.requireNonNull(response.contains("Duplicate entry"))){


                        //animation code
                        progressBar.clearAnimation();
                        progressBar.setVisibility(View.GONE);
                        //animation code
                        Log.d("onresponse", "2");

                        Toast.makeText(RecceDashboardActivity.this, "Error- Duplicate email entry", Toast.LENGTH_LONG).show();


                    }else{
                        Toast.makeText(RecceDashboardActivity.this, response, Toast.LENGTH_LONG).show();

                    }

                }
            });
        }
    }

    String retailer_code, division, asm_name, asm_contact;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("retailer_code", retailer_code);
        outState.putString("division", division);
        outState.putString("asm_name", asm_name);
        outState.putString("asm_contact", asm_contact);
        outState.putString("retailer_code", retailer_code);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        retailer_code = savedInstanceState.getString("retailer_code");
        division = savedInstanceState.getString("division");
        asm_name = savedInstanceState.getString("asm_name");
        asm_contact = savedInstanceState.getString("asm_contact");
        retailer_code= savedInstanceState.getString("retailer_code");
    }


    void fillretailerdata(String response){

        try {

            JSONObject jsonobj = new JSONObject(response);
            JSONObject jsonobj1= jsonobj.getJSONObject("data");

            retailer_code= jsonobj1.optString("retailer_code", "");
            division= jsonobj1.optString("division", "");
            asm_name= jsonobj1.optString("asm_name", "");
            asm_contact= jsonobj1.optString("asm_contact", "");

            binding.etHeight3.setText(jsonobj1.getString("retailer_name"));
            if(!TextUtils.isEmpty(binding.etHeight3.getText())){
                binding.etHeight3.setFocusable( View.NOT_FOCUSABLE);
            }

            binding.etHeight5.setText(jsonobj1.getString("contact"));
            if(!TextUtils.isEmpty(binding.etHeight5.getText())){
                binding.etHeight5.setFocusable( View.NOT_FOCUSABLE);
            }

            binding.etTotalArea1.setText(jsonobj1.getString("address"));
            if(!TextUtils.isEmpty(binding.etTotalArea1.getText())){
                binding.etTotalArea1.setFocusable( View.NOT_FOCUSABLE);
            }

            binding.etWidth2.setText(jsonobj1.getString("city"));
            if(!TextUtils.isEmpty(binding.etWidth2.getText())){
                binding.etWidth2.setFocusable( View.NOT_FOCUSABLE);
            }

            binding.etHeight2.setText(jsonobj1.getString("district"));
            if(!TextUtils.isEmpty(binding.etHeight2.getText())){
                binding.etHeight2.setFocusable( View.NOT_FOCUSABLE);
            }

            binding.etWidth1.setText(jsonobj1.getString("state"));
            if(!TextUtils.isEmpty(binding.etWidth1.getText())){
                binding.etWidth1.setFocusable( View.NOT_FOCUSABLE);
            }

            binding.etHeight8.setText(asm_name);
            if(!TextUtils.isEmpty(binding.etHeight8.getText())){
                binding.etHeight8.setFocusable( View.NOT_FOCUSABLE);
            }

            binding.etWidth8.setText(asm_contact);
            if(!TextUtils.isEmpty(binding.etWidth8.getText())){
                binding.etWidth8.setFocusable( View.NOT_FOCUSABLE);
            }

            binding.etBrand.setText(jsonobj1.getString("brand"));
            if(!TextUtils.isEmpty(binding.etBrand.getText())){
                binding.etBrand.setFocusable( View.NOT_FOCUSABLE);
            }

            binding.etOwnerAlternateNumber.setText(jsonobj1.getString("owner_alt_num"));
            if(!TextUtils.isEmpty(binding.etOwnerAlternateNumber.getText())){
                binding.etOwnerAlternateNumber.setFocusable( View.NOT_FOCUSABLE);
            }







        }catch (Exception e){
            Log.d("tag333", e.toString());
        }

    }


    // Method to hide the keyboard
    private void hideKeyboard() {
        // Get the current focus view
        View view = this.getCurrentFocus();

        if (view != null) {
            // Get the InputMethodManager service
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            // Hide the keyboard
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("onstop", "");

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("onpause", "");

    }


    SiteDetail siteDetail;
    JSONObject jsonobj;


}