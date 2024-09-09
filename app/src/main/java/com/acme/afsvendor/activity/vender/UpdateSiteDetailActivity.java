package com.acme.afsvendor.activity.vender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.Executor;

import androidx.databinding.DataBindingUtil;
import com.acme.afsvendor.R;
import com.acme.afsvendor.api.MyUrlRequestCallback;
import com.acme.afsvendor.databinding.ActivityUpdateSiteDetailBinding;
import com.acme.afsvendor.viewmodel.ApiInterface;

import org.chromium.net.CronetEngine;
import org.chromium.net.UploadDataProvider;
import org.chromium.net.UploadDataSink;
import org.chromium.net.UrlRequest;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

public class UpdateSiteDetailActivity extends AppCompatActivity implements ApiInterface{

    private ActivityUpdateSiteDetailBinding binding;

    //intent contents
    String campaignType="";
    int position= 0;
    String logintoken="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_site_detail);

        Log.d("whichclass", "UpdateSiteDetailActivity");

        Intent intent= getIntent();

        if(intent!=null){
            campaignType= intent.getStringExtra("campaigntype");
            logintoken= intent.getStringExtra("logintoken");
            position= intent.getIntExtra("position", 0);

        }
    }

    public void btnCloseClick(View view) {
        finish();
    }

    public void btnUpdateClick(View view) {

        finish();
    }

    public void btnSaveClick(View view) {

        try {
            CronetEngine.Builder builder = new CronetEngine.Builder(this);
            CronetEngine cronetEngine = builder.build();

            String token = "474|Z94CA7l1NBZS1deYcnEhp8ZRyxrlWwPA0pp7jZ04";
            // Create a JSON payload with the email and password


            String jsonPayload = "{\"token\":\"" + token + "\"}";

            // Convert the JSON payload to bytes for uploading
            final byte[] postData = jsonPayload.getBytes(StandardCharsets.UTF_8);

            // Create an upload data provider to send the POST data
            UploadDataProvider uploadDataProvider = new UploadDataProvider() {
                @Override
                public long getLength() throws IOException {
                    return postData.length;
                }

                @Override
                public void read(UploadDataSink uploadDataSink, ByteBuffer byteBuffer) throws IOException {
                    byteBuffer.put(postData);
                    uploadDataSink.onReadSucceeded(false);
                }

                @Override
                public void rewind(UploadDataSink uploadDataSink) throws IOException {
                    uploadDataSink.onRewindSucceeded();
                }

                @Override
                public void close() throws IOException {
                    // No-op
                }
            };

            UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                            "https://acme.warburttons.com/api/clients", new MyUrlRequestCallback((ApiInterface) this), cronetExecutor)
                    .setHttpMethod("POST")  // Set the method to GET
                    .addHeader("Content-Type", "application/json")  // Indicate we're sending JSON data
                    .setUploadDataProvider(uploadDataProvider, cronetExecutor);  // Attach the payload

            UrlRequest request = requestBuilder.build();
            request.start();


        } catch (Exception e) {
            Log.d("tag12", e.toString());
        }
        finish();

    }
    Executor cronetExecutor= Executors.newSingleThreadExecutor();
    @Override
    public void onResponseReceived(String response){
        Log.d("tag12", "response is "+ response);

        Toast.makeText(this, "Details saved successfully", Toast.LENGTH_SHORT);

        finish();
    }
}

