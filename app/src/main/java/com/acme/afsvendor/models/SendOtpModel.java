package com.acme.afsvendor.models;

public class SendOtpModel {
    private String country_code;
    private String mobile;

    public SendOtpModel(String county_code, String mobile) {
        this.country_code = country_code;
        this.mobile = mobile;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    // Optional: Override toString for a better representation if required.
    @Override
    public String toString() {
        return "SendOtpModel{" +
                "country_code='" + country_code + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}