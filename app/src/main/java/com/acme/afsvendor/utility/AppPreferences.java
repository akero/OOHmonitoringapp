package com.acme.afsvendor.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

    private static final String PLACE_OBJ = "place_obj";
    private static final String USER_DATA = "user_data";
    private static AppPreferences sharePref;
    private static SharedPreferences sharedPreferences;

    private AppPreferences() { }

    public static AppPreferences getInstance(Context context) {
        if (sharePref == null) {
            synchronized (AppPreferences.class) {
                if (sharePref == null) {
                    sharePref = new AppPreferences();
                    sharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
                }
            }
        }
        return sharePref;
    }

    public String getUserData() {
        return sharedPreferences.getString(USER_DATA, "");
    }

    public void saveUserData(String userDataStr) {
        sharedPreferences.edit()
                .putString(USER_DATA, userDataStr)
                .apply();
    }

    public void removeUserData() {
        sharedPreferences.edit().remove(USER_DATA).apply();
    }

    public void clearAll() {
        sharedPreferences.edit().clear().apply();
    }

}
