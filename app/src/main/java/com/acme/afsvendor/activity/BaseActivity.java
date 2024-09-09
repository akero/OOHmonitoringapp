package com.acme.afsvendor.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import androidx.appcompat.app.AlertDialog;
import com.acme.afsvendor.R;

public abstract class BaseActivity extends AppCompatActivity {
    private AlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private AlertDialog addProgress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_progess, null));
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        return dialog;
    }

    protected void showProgressDialog() {
        if (pDialog == null) {
            pDialog = addProgress();
            pDialog.show();
        } else {
            if (pDialog != null) {
                if (!pDialog.isShowing()) {
                    pDialog.show();
                } else {
                    pDialog.dismiss();
                }
            }
        }
    }

    protected void hideProgressDialog() {
        if (pDialog != null) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }

    @Override
    protected void onDestroy() {
        hideProgressDialog();
        super.onDestroy();
    }
}
