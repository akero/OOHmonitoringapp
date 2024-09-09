package com.acme.afsvendor.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.acme.afsvendor.R;
import com.acme.afsvendor.databinding.ActivityForgotPasswordBinding;
import com.acme.afsvendor.utility.NetworkUtils;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        Log.d("whichclass", "ForgotPasswordActivity");
    }

    public void btnSubmitClick(View view) {
        if (binding.etEmailId.getText().toString().isEmpty()) {
            Toast.makeText(this, "Fill the Email", Toast.LENGTH_LONG).show();
        } else if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "Check your Internet Connection and Try Again", Toast.LENGTH_LONG).show();
        } else {
            showSuccessMessage();
        }
    }

    public void btnSigninClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void showSuccessMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_emailsent, null);
        TextView tvResubmit = view.findViewById(R.id.tvResubmit);
        String text = "<font color=#0089BE>Didn't receive email? </font> <font color=#DA2829>Resubmit</font>";
        tvResubmit.setText(Html.fromHtml(text));
        Button btnClose = view.findViewById(R.id.btnClose);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        if (dialog != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                    finish();
                }
            }
        });
        if (dialog != null) {
            dialog.show();
        }
    }
}
