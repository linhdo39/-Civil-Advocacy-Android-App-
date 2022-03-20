package com.example.civiladvocacyapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {
    private final String url = "https://developers.google.com/civic-information/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void redirectGoogleApi(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("No Application found that handles ACTION_VIEW (https) intents");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
