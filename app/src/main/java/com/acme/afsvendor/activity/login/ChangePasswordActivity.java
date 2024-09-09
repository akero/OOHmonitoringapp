package com.acme.afsvendor.activity.login;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.databinding.DataBindingUtil;
import com.acme.afsvendor.R;
import com.acme.afsvendor.databinding.ActivityChangePasswordBinding;
import com.acme.afsvendor.utility.NetworkUtils;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        Log.d("whichclass", "ChangePasswordActivity");
    }

    public void btnResetClick(View view) {
        if (binding.etOldPassword.getText().toString().isEmpty()
                || binding.etNewPassword.getText().toString().isEmpty()
                || binding.etConfirmPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_LONG).show();
        } else if (!binding.etNewPassword.getText().toString().isEmpty()
                && !binding.etNewPassword.getText().toString().equals(binding.etConfirmPassword.getText().toString())) {
            Toast.makeText(this, "New and Confirm Passwords must be same", Toast.LENGTH_LONG).show();
        } else {
            if (!NetworkUtils.isNetworkAvailable(this)) {
                Toast.makeText(this, "Check your Internet Connection and Try Again", Toast.LENGTH_LONG).show();
            } else {
                // Additional code to be executed if connected to network
            }
        }
    }
}
