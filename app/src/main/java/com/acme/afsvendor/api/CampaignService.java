package com.acme.afsvendor.api;

import com.acme.afsvendor.models.SendOtpResponseModel;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CampaignService {

    @FormUrlEncoded
    @POST("send-otp")
    Call<SendOtpResponseModel> sendOtp(
            @Field("county_code") String countryCode,
            @Field("mobile") String mobile
    );

    @FormUrlEncoded
    @POST("login-user")
    Call<SendOtpResponseModel> loginUser(
            @Field("loginid") String loginId,
            @Field("password") String password
    );

    class Creator {
        private static CampaignService retrofitService;

        public static CampaignService getInstance() {
            if (retrofitService == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://leavecasa.com/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                retrofitService = retrofit.create(CampaignService.class);
            }
            return retrofitService;
        }
    }
}
