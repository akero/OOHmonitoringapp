package com.acme.afsvendor.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.acme.afsvendor.models.SendOtpResponseModel;

public class WelcomeActivityViewModel extends ViewModel implements ApiInterface {
    public MutableLiveData<SendOtpResponseModel> successresponse = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public void callOtp(String countrycode, String mobile, Context context) {

        APIreferenceclass ap= new APIreferenceclass(countrycode, mobile, context);


    }

    @Override
    public void onResponseReceived(String response) {
        //if (response.isSuccessful()) {
           // successresponse.setValue(response.body());
        //} else {
         //   errorMessage.setValue(response.message());
        //}
    }
}
