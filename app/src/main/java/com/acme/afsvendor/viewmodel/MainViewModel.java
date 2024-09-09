package com.acme.afsvendor.viewmodel;

import androidx.lifecycle.ViewModel;

import com.acme.afsvendor.repository.CampaignRepository;

public class MainViewModel extends ViewModel {

    private final CampaignRepository repository;

    public MainViewModel(CampaignRepository repository) {
        this.repository = repository;

        // Uncomment below if you want to use them:
        // callOtp(new SendOtpModel());
    }

    // Uncomment this if you want to use coroutines.
    // Java doesn't have native support for coroutines, you'd typically use
    // something like Executors or RxJava for asynchronous operations.
    /*
    public void callOtp(SendOtpModel sendOtpModel) {
        // Here we would launch a background task
        try {
            SendOtpResponseModel otpResponse = repository.sendOtp(sendOtpModel);
            // Handle the response here
        } catch (Exception e) {
            // Handle exceptions here
        }
    }
    */
}
