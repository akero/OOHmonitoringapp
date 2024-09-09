package com.acme.afsvendor.activity.dashboard;

import android.graphics.Bitmap;

public class AdminCrudDataClass {

    int id =0;
    String name= "";
    Bitmap image;
    String uid= "";

    public void setId(int id)
    {
        this.id=id;
    }

    public int getId(){
        return id;
    }

    public void setName(String name)
    {
        this.name=name;
    }

    public String getName(){
        return name;
    }

    public void setImage(Bitmap image)
    {
        this.image=image;
    }

    public Bitmap getImage(){
        return image;
    }

    public void setUid(String uid)
    {
        this.uid=uid;
    }

    public String getUid(){
        return uid;
    }

}
