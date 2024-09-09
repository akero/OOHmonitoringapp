package com.acme.afsvendor.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.acme.afsvendor.R;
import com.acme.afsvendor.activity.dashboard.BaseActivity;
import com.acme.afsvendor.databinding.ActivityWelcomeBinding;
import com.acme.afsvendor.utility.NetworkUtils;
import com.acme.afsvendor.viewmodel.WelcomeActivityViewModel;

public class WelcomeActivity extends BaseActivity {

    private ActivityWelcomeBinding binding;
    private WelcomeActivityViewModel welcomeActivityViewModel;

    int loginType=1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("tag2","start of welcomeactivity");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        Log.d("whichclass", "WelcomeActivity");
        loginType=getIntent().getIntExtra("loginType", 1);

        welcomeActivityViewModel = new ViewModelProvider(this).get(WelcomeActivityViewModel.class);
        welcomeActivityViewModel.successresponse.observe(this, response -> {
            hideProgressDialog();
            Toast.makeText(WelcomeActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("tag2", "response " + response);


            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            intent.putExtra("Email", binding.etEmailId.getText().toString());
            //startActivity(intent);

        });

        welcomeActivityViewModel.errorMessage.observe(this, errorMessage -> {
            hideProgressDialog();
            Toast.makeText(WelcomeActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            //intent.putExtra("Email","rish1994@gmail.com");
            //startActivity(intent);


            Log.d("tag2", "responseerror " + errorMessage);
        });
    }

    public void btnSubmitClick(View view) {
        if (binding.etEmailId.getText().toString().isEmpty()) {
            Toast.makeText(this, "Fill the Email", Toast.LENGTH_LONG).show();
        } else if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "Check your Internet Connection and Try Again", Toast.LENGTH_LONG).show();
        } else {


            Intent intent = new Intent(WelcomeActivity.this, OTP.class);
            intent.putExtra("Email", binding.etEmailId.getText().toString());
            intent.putExtra("loginType", loginType);
            startActivity(intent);
            //Intent intent= new Intent(WelcomeActivity.this, LoginActivity.class);
            //startActivity(intent);

            //showProgressDialog();
            //welcomeActivityViewModel.callOtp("+91", binding.etEmailId.getText().toString(), this);
        }
    }
}
