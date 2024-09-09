package com.acme.afsvendor.viewmodel;

import android.graphics.Bitmap;
import android.util.Log;

public class SiteDetail {
    private boolean success;
    private String message;
    private int id;
    private Bitmap image;
    private String vendor_id;
    private int campaign_id;
    private String start_date;
    private String end_date;
    private String location;
    private String longitute;
    private String latitude;
    private String width;
    private String height;
    private String total_area;
    private String media_type;
    private String name; // assuming it's a String, change the type if necessary
    private String site_no; // assuming it's a String, change the type if necessary

    private String illumination;
    private String created_at;
    private String updated_at;

    // Getters and Setters for each field

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSiteNo() {
        return site_no;
    }

    public void setSiteNo(String site_no) {
        this.site_no = site_no;
    }


    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getVendorId() {
        return vendor_id;
    }

    public void setVendorId(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public int getCampaignId() {
        return campaign_id;
    }

    public void setCampaignId(int campaign_id) {
        this.campaign_id = campaign_id;
    }

    public String getStartDate() {
        return start_date;
    }

    public void setStartDate(String start_date) {
        this.start_date = start_date;
    }

    public String getEndDate() {
        return end_date;
    }

    public void setEndDate(String end_date) {
        this.end_date = end_date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLongitude() {
        return longitute;
    }

    public void setLongitude(String longitute) {
        this.longitute = longitute;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getTotalArea() {

        try {
            total_area = Integer.toString(Integer.parseInt(height) * Integer.parseInt(width));
        }catch(Exception e){
            Log.d("tag333", e.toString());
            total_area= null;
        }
        return total_area;
    }

    public void setTotalArea(String total_area) {
        this.total_area = total_area;
    }

    public String getMediaType() {
        return media_type;
    }

    public void setMediaType(String media_type) {
        this.media_type = media_type;
    }

    public String getIllumination() {
        return illumination;
    }

    public void setIllumination(String illumination) {
        this.illumination = illumination;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(String updated_at) {
        this.updated_at = updated_at;
    }
}
