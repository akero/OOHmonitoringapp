package com.acme.afsvendor.activity.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.acme.afsvendor.R;
import com.acme.afsvendor.databinding.ActivityRecceDashboardBinding;
import com.acme.afsvendor.databinding.ActivityReplaceCodeBinding;
import com.acme.afsvendor.viewmodel.APIreferenceclass;
import com.acme.afsvendor.viewmodel.ApiInterface;

import org.json.JSONObject;

import java.util.Objects;
import java.util.StringTokenizer;

public class ReplaceCodeActivity extends AppCompatActivity implements ApiInterface {
    private ActivityReplaceCodeBinding binding;
    String latlong, logintoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReplaceCodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            FileHelper fh = new FileHelper();
            logintoken = fh.readLoginToken(this);

        } catch (Exception e) {
            Log.d("tg223", e.toString());
        }

        binding.btnReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apicall();
            }
        });


    }

    void apicall() {


        JSONObject jsonPayload = new JSONObject();
        try {

            //jsonPayload.put("project", binding.etHeight1.getText().toString());
            jsonPayload.put("state", binding.etWidth1.getText().toString());
            jsonPayload.put("district", binding.etHeight3.getText().toString());
            jsonPayload.put("old_code", binding.etHeight2.getText().toString());
            jsonPayload.put("retailer_code", binding.etWidth2.getText().toString());
            jsonPayload.put("city", binding.etWidth3.getText().toString());
            jsonPayload.put("retailer_name", binding.etWidth4.getText().toString());
            jsonPayload.put("address", binding.etWidth5.getText().toString());
            jsonPayload.put("contact", binding.etWidth6.getText().toString());
            jsonPayload.put("asm_name", binding.etHeight8.getText().toString());
            jsonPayload.put("asm_contact", binding.etWidth8.getText().toString());
            jsonPayload.put("division", binding.etWidth7.getText().toString());

            FileHelper fh = new FileHelper();
            jsonPayload.put("created_by", fh.readUserId(this));

        } catch (Exception e) {
            Log.d("tg6", e.toString());
        }


        APIreferenceclass api = new APIreferenceclass(jsonPayload, this, logintoken, 1, 1);

    }

    @Override
    public void onResponseReceived(String response) {

        Log.d("response", response);
        JSONObject jsono = null;
        try {

            jsono = new JSONObject(response);

            if (jsono.getString("message").equals("Data saved successfully!")) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ReplaceCodeActivity.this, "Retailer saved successfully", Toast.LENGTH_SHORT).show();
                        resetAndReinitialize();
                    }
                });
            } else {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("onresponse", "5");
                        Toast.makeText(ReplaceCodeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        } catch (Exception e) {
            Log.d("tag2323", e.toString());
        }
    }

    public void resetAndReinitialize() {
        // Perform any necessary cleanup or data saving operations here

        // Finish the current activity instance
        finish();

        // Start a new instance of the same activity
        startActivity(getIntent());
        overridePendingTransition(0, 0); // Optionally, you can remove the transition animation
    }
}