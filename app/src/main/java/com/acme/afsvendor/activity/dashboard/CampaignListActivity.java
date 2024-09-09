package com.acme.afsvendor.activity.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import com.acme.afsvendor.R;
import com.acme.afsvendor.adapters.CampaignListAdapter;
import com.acme.afsvendor.databinding.ActivityCampaignListBinding;
import org.json.JSONArray;
import org.json.JSONObject;

public class CampaignListActivity extends AppCompatActivity {

    private ActivityCampaignListBinding binding;
    private boolean oldcampaign = true;
    private boolean showMenus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_campaign_list);
        Log.d("whichclass", "CampaignListActivity");
        campaignList();
    }

    private void campaignList() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.rvCampaignList.setLayoutManager(layoutManager);

        JSONArray jsonArray = new JSONArray();
        try {

            JSONObject jsonObjectairbnb = new JSONObject();
            jsonObjectairbnb.put("sitenumber", "001");
            jsonObjectairbnb.put("unitnumber", "#887001");
            jsonArray.put(jsonObjectairbnb);

            JSONObject jsonObjecthyundai = new JSONObject();
            jsonObjecthyundai.put("sitenumber", "002");
            jsonObjecthyundai.put("unitnumber", "#878002");
            jsonArray.put(jsonObjecthyundai);

            JSONObject jsonObjectford = new JSONObject();
            jsonObjectford.put("sitenumber", "003");
            jsonObjectford.put("unitnumber", "#765003");
            jsonArray.put(jsonObjectford);

            JSONObject jsonObjectpatanjli = new JSONObject();
            jsonObjectpatanjli.put("sitenumber", "004");
            jsonObjectpatanjli.put("unitnumber", "#432004");
            jsonArray.put(jsonObjectpatanjli);

        } catch (Exception e) {
            e.printStackTrace();
        }

        CampaignListAdapter adapter = new CampaignListAdapter(this, jsonArray, false);
        binding.rvCampaignList.setAdapter(adapter);
    }

    public void oldCampaignClick(View view) {
        oldcampaign = true;
        binding.tvOldCampaign.setBackgroundResource(R.drawable.primaryround);
        binding.tvLiveCampaign.setBackgroundResource(R.color.coloryellow);
        binding.tvOldCampaign.setTextColor(Color.parseColor("#FFFFFF"));
        binding.tvLiveCampaign.setTextColor(Color.parseColor("#0089BE"));
    }

    public void liveCampaignClick(View view) {
        oldcampaign = false;
        binding.tvLiveCampaign.setBackgroundResource(R.drawable.primaryround);
        binding.tvOldCampaign.setBackgroundResource(R.color.coloryellow);
        binding.tvLiveCampaign.setTextColor(Color.parseColor("#FFFFFF"));
        binding.tvOldCampaign.setTextColor(Color.parseColor("#0089BE"));
    }

    public void onPlusClick(View view) {
        if (showMenus) {
            binding.ivPlus.setImageResource(R.drawable.ic_add);
            showMenus = false;
            binding.ivAddSite.setVisibility(View.INVISIBLE);
            binding.ivRedo.setVisibility(View.INVISIBLE);
            binding.ivDeleteSite.setVisibility(View.INVISIBLE);
            TranslateAnimation animate = new TranslateAnimation(0F, 0F, 0F, view.getHeight());
            animate.setDuration(0);
            binding.ivAddSite.startAnimation(animate);
            binding.ivRedo.startAnimation(animate);
            binding.ivDeleteSite.startAnimation(animate);
        } else {
            showMenus = true;
            binding.ivAddSite.setVisibility(View.VISIBLE);
            binding.ivRedo.setVisibility(View.VISIBLE);
            binding.ivDeleteSite.setVisibility(View.VISIBLE);
            TranslateAnimation animate = new TranslateAnimation(0F, 0F, view.getHeight(), 0F);
            animate.setDuration(500);
            animate.setFillAfter(true);
            binding.ivAddSite.startAnimation(animate);
            binding.ivRedo.startAnimation(animate);
            binding.ivDeleteSite.startAnimation(animate);
            binding.ivPlus.setImageResource(R.drawable.ic_cross);
        }
    }

    public void onAddSiteClick(View view) {
        startActivity(new Intent(this, AddSiteDetailActivity.class));
    }

    public void onRedoClientClick(View view) {
        // Your logic for onRedoClientClick
    }

    public void onDeleteSiteClick(View view) {
        // Your logic for onDeleteSiteClick
    }

    public void onItemClick(int position) {
        startActivity(new Intent(this, ViewSiteDetailActivity.class)
                .putExtra("position", position)
                .putExtra("campaignType", "old"));
    }
}
