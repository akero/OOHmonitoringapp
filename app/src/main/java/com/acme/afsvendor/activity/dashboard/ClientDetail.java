package com.acme.afsvendor.activity.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;

import com.acme.afsvendor.R;

public class ClientDetail extends AppCompatActivity {

    private int id;
    private String name;
    private String email;
    private String phonenumber;
    private String companyname;
    private String companyaddress;
    private String gstno;
    private Bitmap logo;
    private String created_at;
    private String updated_at;

    // Getters and Setters for each field

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phonenumber;
    }

    public void setPhoneNumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getComapnyName() {
        return companyname;
    }

    public void setCompanyName(String companyname) {
        this.companyname = companyname;
    }

    public String getCompanyAddress() {
        return companyaddress;
    }

    public void setCompanyAddress(String companyaddress) {
        this.companyaddress = companyaddress;
    }

    public String getGstNo() {
        return gstno;
    }

    public void setGstNo(String gstno) {
        this.gstno = gstno;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
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
