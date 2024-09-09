package com.acme.afsvendor.activity.dashboard;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.acme.afsvendor.R;

public abstract class BaseActivity extends AppCompatActivity {

    private AlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected AlertDialog addProgress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_progess, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        if (dialog != null) {
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }
        return dialog;
    }

    protected void showProgressDialog() {
        if (pDialog == null) {
            pDialog = addProgress();
            pDialog.show();
        } else {
            if (!pDialog.isShowing()) {
                pDialog.show();
            } else {
                pDialog.dismiss();
            }
            logMessage("dialogg", "not null");
        }
    }

    protected void hideProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    protected void logMessage(String _TAG, String _message) {
        Log.e(_TAG, _message);
    }

    public interface OnDialogOkListener {
        void onOk();
    }

    protected void showAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_popup, null);
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        tvMsg.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/pregular.otf"));
        tvMsg.setText(msg);
        Button btnOk = view.findViewById(R.id.btnOk);
        btnOk.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/pbold.otf"));
        builder.setView(view);
        AlertDialog dialog = builder.create();
        if (dialog != null) {
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }
        btnOk.setOnClickListener(v -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });
        if (dialog != null) {
            dialog.show();
        }
    }

    protected void showAlert(String msg, OnDialogOkListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_popup, null);
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        tvMsg.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/pregular.otf"));
        tvMsg.setText(msg);
        Button btnOk = view.findViewById(R.id.btnOk);
        btnOk.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/pbold.otf"));
        builder.setView(view);
        AlertDialog dialog = builder.create();
        if (dialog != null) {
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }
        btnOk.setOnClickListener(v -> {
            if (dialog != null) {
                dialog.dismiss();
                listener.onOk();
            }
        });
        if (dialog != null) {
            dialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        hideProgressDialog();
        super.onDestroy();
    }
}
