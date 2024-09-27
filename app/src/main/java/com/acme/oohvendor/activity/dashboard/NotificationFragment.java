package com.acme.oohvendor.activity.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.acme.oohvendor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private GridLayout codeGrid;
    private List<String> codeList = new ArrayList<>();
    private String response; // This will be passed to the fragment

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        codeGrid = view.findViewById(R.id.code_grid);
        Button cancelButton = view.findViewById(R.id.cancel_button);

        // Assuming response is passed via a Bundle
        if (getArguments() != null) {
            response = getArguments().getString("response");
            parseResponse(response);
        }

        // Add buttons to GridLayout
        for (String code : codeList) {
            Button codeButton = new Button(getContext());
            codeButton.setText(code);
            codeButton.setBackgroundResource(R.drawable.code_button_background); // Custom background if needed
            codeButton.setTextColor(Color.BLUE);

            // Set adjusted fixed width and height
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics()); // Increased width
            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()); // Increased height
            params.setMargins(8, 8, 8, 8); // Set margins between buttons
            codeButton.setLayoutParams(params);

            codeButton.setOnClickListener(v -> {
                // Handle code click event
                Intent intent = new Intent(getActivity(), ViewSiteDetailActivity.class);
                intent.putExtra("sitenumber", code);
                intent.putExtra("camefrom", "ViewVendorSites");
                startActivity(intent);
            });

            // Add button to GridLayout
            codeGrid.addView(codeButton);
        }


        // Handle cancel button click
        cancelButton.setOnClickListener(v -> {
            // Dismiss the fragment or handle as needed
            getActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    // Parse response JSON and extract codes
    private void parseResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray dataArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject item = dataArray.getJSONObject(i);
                String code = item.getString("code");
                codeList.add(code); // Add code to the list
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
