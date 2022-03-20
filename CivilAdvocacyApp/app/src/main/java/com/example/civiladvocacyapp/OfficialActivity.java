package com.example.civiladvocacyapp;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

public class OfficialActivity extends AppCompatActivity {
    private final String TAG = "Official Activity";
    private boolean check1 = false, check2 = false,check3= false;
    private TextView field1;
    private TextView field2;
    private TextView field3;
    private TextView field4;
    private TextView location;
    private ImageView imageView;
    private Picasso picasso;
    private TextView office;
    private TextView official_name;
    private TextView party_name;
    private TextView detail1;
    private TextView detail2;
    private TextView detail3;
    private TextView detail4;
    private ImageView DemLogo;
    private ImageView RepLogo;
    private ScrollView scrollView;
    private ConstraintLayout layout;
    private String youtubeID;
    private String facebookID;
    private String twitterID;
    private ImageView Facebook;
    private ImageView Youtube;
    private ImageView Twitter;
    private Official official;
    private String locationStr;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.official_activity);
        official = (Official) getIntent().getSerializableExtra("info");
        locationStr = (String) getIntent().getSerializableExtra("location");
        location = findViewById(R.id.location1);
        location.setText(locationStr);
        scrollView = findViewById(R.id.scrollView2);
        layout = findViewById(R.id.layout);
        field1 = findViewById(R.id.field1);
        field2 = findViewById(R.id.field2);
        field3 = findViewById(R.id.field3);
        field4 = findViewById(R.id.field4);
        imageView = findViewById(R.id.imageView);
        picasso = Picasso.get();
        picasso.setLoggingEnabled(true);
        office = findViewById(R.id.office);
        official_name = findViewById(R.id.official_name);
        party_name = findViewById(R.id.party_name);
        detail1 = findViewById(R.id.detail1);
        detail2 = findViewById(R.id.detail2);
        detail3 = findViewById(R.id.detail3);
        detail4 = findViewById(R.id.detail4);
        DemLogo = findViewById(R.id.Demlogo);
        RepLogo = findViewById(R.id.Replogo);
        facebookID = official.getFacebook();
        youtubeID = official.getYoutube();
        twitterID = official.getTwitter();
        Facebook = findViewById(R.id.facebook);
        Youtube = findViewById(R.id.youtube);
        Twitter = findViewById(R.id.twitter);
        if(!facebookID.equals("")) {
            Facebook.setVisibility(View.VISIBLE);
        }
        else {
            Facebook.setVisibility(View.INVISIBLE);
            Facebook.setOnClickListener(null);
        }

        if(!youtubeID.equals("")) {
            Youtube.setVisibility(View.VISIBLE);
        }
        else {
            Youtube.setVisibility(View.INVISIBLE);
            Youtube.setOnClickListener(null);
        }

        if(!twitterID.equals("")) {
            Twitter.setVisibility(View.VISIBLE);
        }
        else {
            Twitter.setVisibility(View.INVISIBLE);
            Twitter.setOnClickListener(null);
        }
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
        String party = "(" + official.getParty() +")";
        party_name.setText(party);

        if(official.getParty().contains("Democrat")) {
            DemLogo.setVisibility(View.VISIBLE);
            RepLogo.setVisibility(View.INVISIBLE);
            scrollView.setBackgroundColor(Color.BLUE);
            layout.setBackgroundColor(Color.BLUE);
            DemLogo.setOnClickListener(this::DemClick);

        }
        else if(official.getParty().contains("Republican")){
            DemLogo.setVisibility(View.INVISIBLE);
            RepLogo.setVisibility(View.VISIBLE);
            scrollView.setBackgroundColor(Color.RED);
            layout.setBackgroundColor(Color.RED);
            RepLogo.setOnClickListener(this::RepClick);
        }
        else{
            DemLogo.setVisibility(View.INVISIBLE);
            RepLogo.setVisibility(View.INVISIBLE);
            scrollView.setBackgroundColor(Color.BLACK);
            layout.setBackgroundColor(Color.BLACK);
            party_name.setVisibility(View.INVISIBLE);
        }

        if(!official.getAddress().equals("")){
            field1.setText(R.string.official_address);
            String str = "<u>"+official.getAddress() + "<u>";
            check1=true;
            detail1.setText(Html.fromHtml(str));
            detail1.setOnClickListener(this::clickMap);
            detail1.setTextColor(Color.WHITE);
        }

        if(!official.getPhone().equals("")) {
            if(check1) {
                field2.setText(R.string.official_phone);
                check2=true;
                detail2.setText(official.getPhone());
                Linkify.addLinks(detail2,Linkify.PHONE_NUMBERS);
                detail2.setLinkTextColor(Color.WHITE);
            }
            else {
                field1.setText(R.string.official_phone);
                check1=true;
                detail1.setText(official.getPhone());
                detail1.setLinkTextColor(Color.WHITE);
                Linkify.addLinks(detail1,Linkify.PHONE_NUMBERS);
            }
        }

        if(!official.getEmail().equals("")) {
            if(check1 && check2) {
                field3.setText(R.string.official_email);
                check3=true;
                detail3.setText(official.getEmail());
                Linkify.addLinks(detail3,Linkify.EMAIL_ADDRESSES);
                detail3.setLinkTextColor(Color.WHITE);
            }
            else if(check1){
                field2.setText(R.string.official_email);
                check2=true;
                detail2.setText(official.getEmail());
                Linkify.addLinks(detail2,Linkify.EMAIL_ADDRESSES);
                detail2.setLinkTextColor(Color.WHITE);
            }
            else{
                field1.setText(R.string.official_email);
                check1=true;
                detail1.setText(official.getEmail());
                Linkify.addLinks(detail1,Linkify.EMAIL_ADDRESSES);
                detail1.setLinkTextColor(Color.WHITE);
            }
        }

        if(!official.getWebsite().equals("")) {
            if(check1 && check2 && check3) {
                field4.setText(R.string.official_website);
                detail4.setText(official.getWebsite());
                Linkify.addLinks(detail4,Linkify.WEB_URLS);
                detail4.setLinkTextColor(Color.WHITE);
            }
            else {
                detail4.setVisibility(View.INVISIBLE);
                field4.setVisibility(View.INVISIBLE);
                if(check1 && check2){
                    field3.setText(R.string.official_website);
                    check3=true;
                    detail3.setText(official.getWebsite());
                    Linkify.addLinks(detail3,Linkify.WEB_URLS);
                    detail3.setLinkTextColor(Color.WHITE);
                }
                else if (check1){
                    field2.setText(R.string.official_website);
                    check2=true;
                    detail2.setText(official.getWebsite());
                    Linkify.addLinks(detail2,Linkify.WEB_URLS);
                    field3.setVisibility(View.INVISIBLE);
                    detail3.setVisibility(View.INVISIBLE);
                    detail2.setLinkTextColor(Color.WHITE);
                }
                else{
                    field1.setText(R.string.official_website);
                    check1=true;
                    detail1.setText(official.getWebsite());
                    Linkify.addLinks(detail1,Linkify.WEB_URLS);
                    field2.setVisibility(View.INVISIBLE);
                    detail2.setVisibility(View.INVISIBLE);
                    field3.setVisibility(View.INVISIBLE);
                    detail3.setVisibility(View.INVISIBLE);
                    detail1.setLinkTextColor(Color.WHITE);
                }
            }
        }
        if(!check1 &&!check2 && !check3){
            field1.setVisibility(View.INVISIBLE);
            detail1.setVisibility(View.INVISIBLE);
            field2.setVisibility(View.INVISIBLE);
            detail2.setVisibility(View.INVISIBLE);
            field3.setVisibility(View.INVISIBLE);
            detail3.setVisibility(View.INVISIBLE);
            field4.setVisibility(View.INVISIBLE);
            detail4.setVisibility(View.INVISIBLE);
        }
    }

    public void clickMap(View v) {
        String address = official.getAddress();

        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));

        Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Cannot open address");
            builder.setTitle("No App Found");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    private void loadRemoteImage(Official official) {
        if(!official.getPhotoURL().equals("")) {
            String url = official.getPhotoURL();
            Log.d(TAG,url);
            imageView.setOnClickListener(this::imageClick);
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

    public void imageClick(View v){
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("info", official);
        intent.putExtra("location", locationStr);
        startActivity(intent);
    }

    public void youTubeClicked(View v) {
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + youtubeID));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + youtubeID)));

        }
    }

    public boolean checkInstalled(String packageName) {
        try {
            return getPackageManager().getApplicationInfo(packageName, 0).enabled;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void facebookClicked(View v) {
        String FACEBOOK_URL = "https://www.facebook.com/"+ facebookID;

        Intent intent;

        if (checkInstalled("com.facebook.katana")) {
            String urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToUse));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_URL));
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("No Application found that handles ACTION_VIEW (FB) intents");
            builder.setTitle("No App Found");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void twitterClicked(View v) {
        String twitterAppUrl = "twitter://user?screen_name=" + twitterID;
        String twitterWebUrl = "https://twitter.com/" +  twitterID;

        Intent intent;
        // Check if Twitter is installed, if not we'll use the browser
        if (checkInstalled("com.twitter.android")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterAppUrl));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterWebUrl));
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("No Application found that handles ACTION_VIEW (twitter) intents");
            builder.setTitle("No App Found");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
