package com.acme.afsvendor.activity.dashboard;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.acme.afsvendor.R;
import com.acme.afsvendor.activity.login.OTP;
import com.acme.afsvendor.databinding.ActivityRecceInstallationLastPageBinding;
import com.acme.afsvendor.utility.RoundRectCornerImageView;
import com.acme.afsvendor.viewmodel.APIreferenceclass;
import com.acme.afsvendor.viewmodel.ApiInterface;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class RecceInstallationLastPage extends AppCompatActivity implements ApiInterface {
    private ActivityRecceInstallationLastPageBinding binding;

    int userid;
    String projectid;
    boolean wassendbuttonpressed;
    String logintoken;
    ProgressBar progressBar;
    Animation rotateAnimation;
    int storephoto;//0 for init, 1 for store photo, 2 for sign, 3 for main upload with other stuff
    int piccounter;
    Boolean picturetaken;
    Uri pic1takenURI, pic2takenURI, pic3takenURI;
    static final int REQUEST_TAKE_PHOTO = 1;
    boolean pic1taken, pic2taken, pic3taken;

    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.CAMERA
            ,Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static final int REQUEST_CODE_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(this, R.layout.activity_recce_installation_last_page);

        //initializing
        userid= 0;
        projectid= "";
        wassendbuttonpressed= false;
        logintoken= "";
        Button initialPicture;
        Button intermediatePicture;
        Button finalPicture;
        picturetaken = false;
        storephoto= 0;
        piccounter= 0;
        pic1taken= false; pic2taken= false; pic3taken= false;
        pic1takenURI= null; pic2takenURI= null; pic3takenURI= null;
        //animation code
        progressBar= findViewById(R.id.progressBar);
        rotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_animation);
        //animation code

        try{
            userid= getIntent().getIntExtra("userid", 0);
            projectid= getIntent().getStringExtra("projectid");
            logintoken= getIntent().getStringExtra("logintoken");

        }catch (Exception e){
            Log.d("tag232", e.toString());
        }

        initialPicture = findViewById(R.id.btnInitialPhoto);
        intermediatePicture = findViewById(R.id.btnInstallingPhoto);
        finalPicture = findViewById(R.id.btnEndPhoto);

        initialPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("camera", "click registered");
                if (ContextCompat.checkSelfPermission(RecceInstallationLastPage.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {//||ContextCompat.checkSelfPermission(ViewSiteDetailActivity.this, WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(RecceInstallationLastPage.this, "Please give camera permissions", Toast.LENGTH_SHORT).show();

                    ActivityCompat.requestPermissions(RecceInstallationLastPage.this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                    Log.d("camera", "dont have permission");
                } else {



                    storephoto= 0;
                    piccounter= 1;
                    openCamera();
                }
            }
        });

        intermediatePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("camera", "click registered");
                if (ContextCompat.checkSelfPermission(RecceInstallationLastPage.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {//||ContextCompat.checkSelfPermission(ViewSiteDetailActivity.this, WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(RecceInstallationLastPage.this, "Please give camera permissions", Toast.LENGTH_SHORT).show();

                    ActivityCompat.requestPermissions(RecceInstallationLastPage.this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                    Log.d("camera", "dont have permission");
                } else {



                    storephoto= 1;
                    piccounter= 2;
                    openCamera();
                }
            }
        });

        finalPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("camera", "click registered");
                if (ContextCompat.checkSelfPermission(RecceInstallationLastPage.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {//||ContextCompat.checkSelfPermission(ViewSiteDetailActivity.this, WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(RecceInstallationLastPage.this, "Please give camera permissions", Toast.LENGTH_SHORT).show();

                    ActivityCompat.requestPermissions(RecceInstallationLastPage.this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                    Log.d("camera", "dont have permission");
                } else {



                    storephoto= 2;
                    piccounter= 3;
                    openCamera();
                }
            }
        });




        binding.btnTaskCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (!allpicturestaken) {
                        Toast.makeText(RecceInstallationLastPage.this, "Please take all three pictures.", Toast.LENGTH_SHORT).show();
                        //Log.d("tag222", "1"+ binding.etHeight.getText().toString()+ "2"+ binding.etWidth.getText().toString()+"3"+ "4"+ binding.etWidth1.getText().toString()+"5"+ binding.etHeight2.getText().toString()+"6"+ binding.etWidth2.getText().toString()+"7"+ binding.etHeight3.getText().toString()+"8"+ binding.etWidth3.getText().toString()+"9"+ binding.etHeight4.getText().toString()+"10"+ binding.etWidth4.getText().toString()+"11"+ binding.etHeight5.getText().toString()+"12"+ binding.etWidth5.getText().toString()+"13"+ "14"+"15"+ Boolean.toString(pictureandlatlongready));
                        wassendbuttonpressed= true;
                        //progressBar.setVisibility(View.VISIBLE);
                        //progressBar.startAnimation(rotateAnimation);
                        //latlong();
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

        });

//same as above
        binding.tvTaskCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!allpicturestaken) {
                    Toast.makeText(RecceInstallationLastPage.this, "Please take all three pictures.", Toast.LENGTH_SHORT).show();
                    //Log.d("tag222", "1"+ binding.etHeight.getText().toString()+ "2"+ binding.etWidth.getText().toString()+"3"+ "4"+ binding.etWidth1.getText().toString()+"5"+ binding.etHeight2.getText().toString()+"6"+ binding.etWidth2.getText().toString()+"7"+ binding.etHeight3.getText().toString()+"8"+ binding.etWidth3.getText().toString()+"9"+ binding.etHeight4.getText().toString()+"10"+ binding.etWidth4.getText().toString()+"11"+ binding.etHeight5.getText().toString()+"12"+ binding.etWidth5.getText().toString()+"13"+ "14"+"15"+ Boolean.toString(pictureandlatlongready));
                    wassendbuttonpressed= true;
                    //progressBar.setVisibility(View.VISIBLE);
                    //progressBar.startAnimation(rotateAnimation);
                    //latlong();
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


        });



        Log.d("whichclass", "RecceInstallationDashboard");
        APIreferenceclass api = new APIreferenceclass(logintoken, this, userid, projectid, 0);

    }

    void apicall() {

        JSONObject jsonPayload = new JSONObject();
        try {

            //jsonPayload.put("project", binding.etHeight1.getText().toString());


            //jsonPayload.put("area", binding.etWidth.getText().toString());



            FileHelper fh = new FileHelper();
            jsonPayload.put("created_by", fh.readUserId(this));

        } catch (Exception e) {
            Log.d("tg6", e.toString());
        }
        //Log.d("tg6", imageUri.toString());
        //apiboolean= 1;

        //Log.d("tg66", jsonPayload.toString());



        //TODO change url

        APIreferenceclass api = new APIreferenceclass(jsonPayload, this, logintoken, pic1takenURI, pic2takenURI,pic3takenURI);
        imageUri = null;
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
            photoURI = FileProvider.getUriForFile(RecceInstallationLastPage.this,
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
                    binding.btnInitialPhoto.setText("Retake Initial Picture");
                    binding.btnInitialPhoto.setBackgroundResource(R.drawable.primarystrokegreen);
                    pic1takenURI= photoURI;
                }else if(piccounter== 2){
                    pic2taken= true;
                    binding.btnInstallingPhoto.setText("Retake Picture While Installing");
                    binding.btnInstallingPhoto.setBackgroundResource(R.drawable.primarystrokegreen);

                    Log.d("pic", "2");
                    pic2takenURI= photoURI;
                }else if(piccounter== 3){
                    pic3taken= true;
                    binding.btnEndPhoto.setText("Retake Final Picture");
                    binding.btnEndPhoto.setBackgroundResource(R.drawable.primarystrokegreen);

                    Log.d("pic", "3");
                    pic3takenURI= photoURI;
                }
                if(pic1taken&&pic2taken&&pic3taken){
                    allpicturestaken= true;
                    Log.d("pic", "allpicturetakentrue");

                }


                picturetaken = true;

                Log.d("tag22", "activity result works");

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Image update failed", Toast.LENGTH_SHORT).show();
            }
            // Do something with the imageUri, e.g., display the image or upload it
        }
        else {
            Log.d("tag22", "something went wrong");
        }
    }

    void logout() {

        try {
            FileHelper fh = new FileHelper();
            fh.writeUserType(this, "");

            Intent intent= new Intent(RecceInstallationLastPage.this, OTP.class);
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

            if (cameraGranted) {
                openCamera();
            } else {
                // At least one permission was denied, handle accordingly
                Toast.makeText(RecceInstallationLastPage.this, "Camera permission denied", Toast.LENGTH_SHORT).show();

            }
        }

    }


    void implementUI(String response) {

        try {
            JSONObject jsonobj1 = new JSONObject(response);
            JSONObject jsonobj = new JSONObject(jsonobj1.getString("data"));

            binding.tvAddSiteDetail.setText(jsonobj.getString("retail_name"));
            binding.etSiteNo.setText(jsonobj.getString("asm_name"));
            binding.tvStartDate.setText(jsonobj.getString("state"));
            binding.tvEndDate.setText(jsonobj.getString("district"));
            binding.tvLocation.setText(jsonobj.getString("city"));
            binding.tvLongitude.setText(jsonobj.getString("length"));
            binding.tvWidth.setText(jsonobj.getString("width"));
            binding.tvHeight.setText(jsonobj.getString("date"));
            binding.tvTotalArea.setText(jsonobj.getString("owner_name"));
            binding.tvMediaType.setText(jsonobj.getString("email"));
            binding.tvIllumination.setText(jsonobj.getString("mobile"));
            binding.tvLatitude1.setText(jsonobj.getString("lat").substring(0, 8));
            binding.tvLongitude1.setText(jsonobj.getString("long").substring(0, 8));
            binding.tvLatitude.setText(jsonobj.getString("location"));

            RoundRectCornerImageView tvImage = findViewById(R.id.ivCampaignImage);
            if (jsonobj.getString("image1") != null) {
                Log.d("tg2", "image code executing");
                new RecceInstallationLastPage.LoadImageAsyncTask(tvImage).execute("https://acme.warburttons.com/" + jsonobj.getString("image1"));
            }

            RoundRectCornerImageView tvImage1 = findViewById(R.id.ivCampaignImage1);
            if (jsonobj.getString("owner_signature") != null) {
                Log.d("tg2", "image code executing");
                new RecceInstallationLastPage.LoadImageAsyncTask(tvImage1).execute("https://acme.warburttons.com/" + jsonobj.getString("owner_signature"));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class LoadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private RoundRectCornerImageView imageView;

        LoadImageAsyncTask(RoundRectCornerImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String imageUrl = params[0];
            try {
                URL url = new URL(imageUrl);
                return BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                // Handle error case
            }
        }
    }

    @Override
    public void onResponseReceived(String response) {

        Log.d("response", response);
        try {

            JSONObject jsonobj1 = new JSONObject(response);
            if (jsonobj1.getString("message").equals("Data fetched successfully!")) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        implementUI(response);
                    }
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    }
