package com.acme.afsvendor.viewmodel;

public class ApiResponse {
    private String status;
    private SiteDetail data;

    // Getters and Setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SiteDetail getData() {
        return data;
    }

    public void setData(SiteDetail data) {
        this.data = data;
    }
}