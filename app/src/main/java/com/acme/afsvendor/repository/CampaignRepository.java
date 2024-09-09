package com.acme.afsvendor.repository;

import androidx.lifecycle.MutableLiveData;
import com.acme.afsvendor.api.CampaignService;
import com.acme.afsvendor.models.SendOtpResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampaignRepository {

    private CampaignService campaignService;
    private MutableLiveData<SendOtpResponseModel> sendotpLiveData;

    public CampaignRepository(CampaignService campaignService) {
        this.campaignService = campaignService;
        this.sendotpLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<SendOtpResponseModel> getSendotpLiveData() {
        return sendotpLiveData;
    }

    public void sendOtp(String countrycode, String mobile) {
        campaignService.sendOtp(countrycode, mobile).enqueue(new Callback<SendOtpResponseModel>() {
            @Override
            public void onResponse(Call<SendOtpResponseModel> call, Response<SendOtpResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sendotpLiveData.postValue(response.body());
                } else {
                    // Handle the error case. Maybe post a different kind of data to the LiveData.
                }
            }

            @Override
            public void onFailure(Call<SendOtpResponseModel> call, Throwable t) {
                // Handle the failure case. Maybe post a different kind of data to the LiveData.
            }
        });
    }
}
