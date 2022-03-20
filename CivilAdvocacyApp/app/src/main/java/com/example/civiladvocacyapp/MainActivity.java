package com.example.civiladvocacyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = "Main Activity";
    private RecyclerView recyclerView;
    private Adapter adapter;
    private final ArrayList<Official> officialList = new ArrayList<>();
    private SwipeRefreshLayout swiper;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;
    private static String locationString = "Undefined Location";
    private TextView location;
    private String value;
    private TextView no_internet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        no_internet = findViewById(R.id.message);
        officialList.clear();
        location = findViewById(R.id.location);
        location.setText(locationString);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkInternet();
        determineLocation();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new Adapter(officialList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swiper = findViewById(R.id.swiper);
        swiper.setOnRefreshListener(this::doRefresh);
        Log.d(TAG, "ON create");
    }

    public void find_zip(){
        if(checkInternet()) {
            LayoutInflater inflater = LayoutInflater.from(this);
            @SuppressLint("InflateParams") final View view = inflater.inflate(R.layout.find_zip, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter a City, State or a Zip Code:");
            builder.setView(view);
            no_internet.setVisibility(View.INVISIBLE);
            builder.setPositiveButton("OK", (dialog, id) -> {
                EditText enteredValue = view.findViewById(R.id.find_location);
                if (enteredValue.getText().length() != 0) {
                    officialList.clear();
                    adapter.notifyDataSetChanged();
                    value = enteredValue.getText().toString();
                    OfficialLoaderRunnable officialLoaderRunnable = new OfficialLoaderRunnable(value,this);
                    new Thread(officialLoaderRunnable).start();
                }
            });
            builder.setNegativeButton("CANCEL", (dialog, id) -> Log.d(TAG, "No zipcode was entered"));

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Cannot find zip without Internet");
            builder.setMessage("Please connect to the internet to find data for city, state or zip");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mini_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about) {
            Intent intent = new Intent(this,About.class);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId() == R.id.search){
            find_zip();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Official temp = officialList.get(pos);
        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra("info", temp);
        intent.putExtra("location", locationString);
        startActivity(intent);
    }

    public void updateList(Official s){
        officialList.add(s);
        adapter.notifyItemInserted(officialList.indexOf(s));
    }

    public void setTextLocation(String s){
        locationString = s;
        location.setText(locationString);
    }

    private void determineLocation() {
        if (checkPermission() ) {
            if(checkInternet())
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location1 -> {
                        if (location1 != null) {
                            no_internet.setVisibility(View.INVISIBLE);
                            getPlace(location1);
                        }
                    })
                    .addOnFailureListener(this, e -> Toast.makeText(MainActivity.this,
                            e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return false;
        }
        return true;
    }


    private void getPlace(Location loc) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            String a ="";
            for (Address ad:addresses) {
                a = String.format("%s %s %s %s %s %s",
                        (ad.getSubThoroughfare() == null ? "" : ad.getSubThoroughfare()),
                        (ad.getThoroughfare() == null ? "" : ad.getThoroughfare()),
                        (ad.getLocality() == null ? "" : ad.getLocality()),
                        (ad.getAdminArea() == null ? "" : ad.getAdminArea()),
                        (ad.getPostalCode() == null ? "" : ad.getPostalCode()),
                        (ad.getCountryName() == null ? "" : ad.getCountryName()));
            }
            OfficialLoaderRunnable officialLoaderRunnable = new OfficialLoaderRunnable(a,this);
            new Thread(officialLoaderRunnable).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void NoData(String s){
        location.setText(R.string.no_internet_location);
        no_internet.setVisibility(View.VISIBLE);
        officialList.clear();
        if(adapter!=null)
            adapter.notifyDataSetChanged();
        if(s.equals("no internet"))
            no_internet.setText(R.string.no_internet_message);
        else
            no_internet.setText(R.string.no_location_found);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                } else {
                    Toast.makeText(this,"Location request denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void doRefresh() {
        if(checkInternet()) {
            officialList.clear();
            adapter.notifyDataSetChanged();
            OfficialLoaderRunnable officialLoaderRunnable = new OfficialLoaderRunnable(locationString, this);
            new Thread(officialLoaderRunnable).start();
        }
        swiper.setRefreshing(false);
    }

    public boolean checkInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            no_internet.setVisibility(View.INVISIBLE);
            return true;
        } else {
            NoData("no internet");
        }
        return false;
    }
}