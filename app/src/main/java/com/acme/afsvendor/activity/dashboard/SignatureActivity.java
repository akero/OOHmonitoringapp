package com.acme.afsvendor.activity.dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.acme.afsvendor.databinding.ActivityRecceDashboardBinding;
import com.acme.afsvendor.databinding.ActivitySignatureBinding;

import com.acme.afsvendor.R;
import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SignatureActivity extends AppCompatActivity {

    ActivitySignatureBinding binding;
    Bitmap signatureBitmap;
    Uri signatureUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        binding = ActivitySignatureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
                Log.d("sign", "sign3");
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
                Log.d("sign", "sign");
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
                Log.d("sign", "sign2");
            }
        });

        binding.btnUpdatePhoto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("camera", "click registered");

                sign= ownersign();
                if(sign!= null){
                    Log.d("signaf", "not null");
                }else{
                    Log.d("signaf", "null");
                }

                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("sign", sign);
        setResult(RESULT_SIGN, intent);
        super.onBackPressed();
    }
    public static final int RESULT_SIGN = 1;  // Define your unique result code


    Uri sign;

    Uri ownersign(){
        signatureBitmap = binding.signaturePad.getSignatureBitmap();
        signatureUri = null;
        try {
            File externalFilesDir = getExternalFilesDir(null);
            if (externalFilesDir != null) {
                File signatureFile = new File(externalFilesDir, "signature.png");
                FileOutputStream fos = new FileOutputStream(signatureFile);
                signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();

                signatureUri = FileProvider.getUriForFile(SignatureActivity.this,
                        "com.acme.afsvendor.fileprovider",
                        signatureFile);

                Log.d("SignaturePad", "Signature file path: " + signatureFile.getAbsolutePath());
                Log.d("SignaturePad", "Signature URI: " + signatureUri);
            } else {
                Log.e("SignaturePad", "External files directory is null");
            }
        } catch (IOException e) {
            Log.e("SignaturePad", "Error getting signature URI", e);
        }



        // Log bitmap details
        String tag = "SignaturePad";  // Tag for filtering logs

        if (signatureBitmap != null) {
            Log.d(tag, "Bitmap captured successfully");
            Log.d(tag, "Bitmap dimensions: " + signatureBitmap.getWidth() + "x" + signatureBitmap.getHeight());
            Log.d(tag, "Bitmap config: " + signatureBitmap.getConfig());
            Log.d(tag, "Bitmap size: " + signatureBitmap.getByteCount() + " bytes");

            // Check if the bitmap is empty (all pixels are the background color)
            boolean isEmpty = true;
            int backgroundColor = Color.WHITE;  // Assuming white background
            for (int x = 0; x < signatureBitmap.getWidth(); x++) {
                for (int y = 0; y < signatureBitmap.getHeight(); y++) {
                    if (signatureBitmap.getPixel(x, y) != backgroundColor) {
                        isEmpty = false;
                        break;
                    }
                }
                if (!isEmpty) break;
            }
            Log.d(tag, "Is bitmap empty? " + (isEmpty ? "Yes" : "No"));

            if(signatureUri!= null){
                Log.d("signinactivity", "not null");
            }else{
                Log.d("signinactivity", "null");
            }
            return signatureUri;
        } else {
                        Toast.makeText(SignatureActivity.this, "Failed to capture bitmap", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}