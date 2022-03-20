package com.example.civiladvocacyapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {
    private final String TAG = "Photo Activity";
    private TextView office;
    private TextView official_name;
    private TextView location;
    private Official official;
    private String locationStr;
    private ImageView DemLogo;
    private ImageView RepLogo;
    private ConstraintLayout layout;
    private ImageView imageView;
    private Picasso picasso;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);
        official = (Official) getIntent().getSerializableExtra("info");
        locationStr = (String) getIntent().getSerializableExtra("location");
        location = findViewById(R.id.photo_location);
        location.setText(locationStr);
        layout = findViewById(R.id.photo_layout);
        imageView = findViewById(R.id.imageView2);
        picasso = Picasso.get();
        picasso.setLoggingEnabled(true);
        office = findViewById(R.id.photo_title);
        official_name = findViewById(R.id.photo_name);
        DemLogo = findViewById(R.id.imageView3);
        RepLogo = findViewById(R.id.imageView4);
        loadRemoteImage(official);
        loadInfo(official);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void loadInfo(Official official){
        office.setText(official.getPosition());
        official_name.setText(official.getName());

        if(official.getParty().contains("Democratic")) {
            DemLogo.setVisibility(View.VISIBLE);
            RepLogo.setVisibility(View.INVISIBLE);
            layout.setBackgroundColor(Color.BLUE);
            DemLogo.setOnClickListener(this::DemClick);
        }
        else if(official.getParty().contains("Republican")){
            DemLogo.setVisibility(View.INVISIBLE);
            RepLogo.setVisibility(View.VISIBLE);
            layout.setBackgroundColor(Color.RED);
            RepLogo.setOnClickListener(this::RepClick);
        }
        else{
            DemLogo.setVisibility(View.INVISIBLE);
            RepLogo.setVisibility(View.INVISIBLE);
            layout.setBackgroundColor(Color.BLACK);
        }
    }

    public void DemClick(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://democrats.org/"));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Cannot open Democrat's website");
            builder.setTitle("No App Found");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void RepClick(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.gop.com/"));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Cannot open Republican's website");
            builder.setTitle("No App Found");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void loadRemoteImage(Official official) {
        if(!official.getPhotoURL().equals("")) {
            String url = official.getPhotoURL();
            Log.d(TAG,url);
            picasso.load(url).centerInside().fit().centerInside()
                    .error(R.drawable.brokenimage).fit().centerInside()
                    .placeholder(R.drawable.placeholder)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "load Image Success");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d(TAG, "load Image Error");
                        }
                    });
        }
    }
}
