package com.acme.afsvendor.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.acme.afsvendor.api.MyUrlRequestCallback;
import com.acme.afsvendor.models.SendOtpResponseModel;

import org.chromium.net.CronetEngine;
import org.chromium.net.UploadDataProvider;
import org.chromium.net.UploadDataSink;
import org.chromium.net.UrlRequest;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivityViewModel extends ViewModel {

    private MutableLiveData<SendOtpResponseModel> successresponse = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public MutableLiveData<SendOtpResponseModel> getSuccessresponse() {
        return successresponse;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    Executor cronetExecutor= Executors.newSingleThreadExecutor();

    public void callLogin(String email, String pass, Context context, int logintype) {

        //default logintype is 1, admin, 2 is vendor, 0 is client

    //adding api here

        CronetEngine.Builder builder = new CronetEngine.Builder(context);
        CronetEngine cronetEngine = builder.build();

        // Create a JSON payload with the email and password
        String jsonPayload = "{\"email\":\"" + email + "\",\"password\":\"" + pass + "\"}";

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

        String apiURL="";

        //client
        if(logintype==0){
            apiURL="https://acme.warburttons.com/api/login";
        }
        //admin
        else if(logintype==1) {
            apiURL="https://acme.warburttons.com/api/login";
        }
        //vendor
        else{
            apiURL="https://acme.warburttons.com/api/login";
        }

        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                        apiURL, new MyUrlRequestCallback((ApiInterface) context), cronetExecutor)
                .setHttpMethod("POST")  // Set the method to POST
                .addHeader("Content-Type", "application/json")  // Indicate we're sending JSON data
                .setUploadDataProvider(uploadDataProvider, cronetExecutor);  // Attach the payload

        UrlRequest request = requestBuilder.build();
        request.start();
        }
}