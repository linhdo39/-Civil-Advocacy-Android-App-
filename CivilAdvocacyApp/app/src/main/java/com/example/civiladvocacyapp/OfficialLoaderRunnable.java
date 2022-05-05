package com.example.civiladvocacyapp;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OfficialLoaderRunnable implements Runnable{
    private static final String TAG = "OfficialLoaderRunnable";
    private final String value;
    private final MainActivity mainActivity;
    private final String API_KEY; // insert your api key here
    private final String url = "https://www.googleapis.com/civicinfo/v2/representatives?key=" + API_KEY+"&address=";

    OfficialLoaderRunnable(String value, MainActivity mainActivity) {
        this.value = value;
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        Uri dataUri = Uri.parse(url+ value);
        String urlToUse = dataUri.toString();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "run: HTTP ResponseCode NOT OK: " + conn.getResponseCode());
                mainActivity.runOnUiThread(() ->  mainActivity.NoData("no data found"));
            }
            else {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
                String line = "";
                String tempLine;
                while( (tempLine = reader.readLine()) != null) {
                    line += tempLine;
                }
                if (!line.equals("")){
                    try {
                        JSONObject Data = new JSONObject(line);
                        String normalizedInput = Data.getString("normalizedInput");
                        JSONObject locationInfo = new JSONObject(normalizedInput);
                        String city = locationInfo.getString("city");
                        String state = locationInfo.getString("state");
                        String zip = locationInfo.getString("zip");
                        String location_display = "";
                        if(city.equals(""))
                            location_display = city + state +zip;
                        else
                            location_display = city + ", " +state + " " +zip;
                        mainActivity.setTextLocation(location_display);
                        JSONArray offices = (JSONArray) Data.get("offices");
                        List<String> officeInfo = new ArrayList<>();
                        for (int i = 0 ; i < offices.length(); i++) {
                            JSONObject info = offices.getJSONObject(i);
                            JSONArray indices = (JSONArray) info.get("officialIndices");
                            for(int j = 0; j < indices.length();j++){
                                officeInfo.add(info.getString("name"));
                            }
                        }
                        JSONArray officials = (JSONArray) Data.get("officials");
                        for (int i = 0 ; i < officials.length(); i++) {
                            JSONObject info = officials.getJSONObject(i);
                            String official_name = info.getString("name");
                            official_name = official_name.trim();
                            String official_position = officeInfo.get(i);
                            JSONArray address=null;
                            if(info.has("address"))
                                address = (JSONArray) info.get("address");
                            String addressLine = "";
                            if(address!=null)
                                for (int j = 0; j <address.length(); j++) {
                                    JSONObject addressDetail = address.getJSONObject(0);
                                    addressLine = addressDetail.getString("line1");
                                    if(addressDetail.has("line2"))
                                        addressLine+= ",+" + addressDetail.getString("line2");
                                    if (addressDetail.has("line3"))
                                        addressLine+=", " + addressDetail.getString("line3");
                                    addressLine+= ", " + addressDetail.getString("city");
                                    addressLine += " " + addressDetail.getString("state");
                                    addressLine += " " + addressDetail.getString("zip");
                                }
                            
                            String party = "", phone="", website="", email="";
                            if(info.has("party"))
                                party = info.getString("party");

                            if(info.has("phones")) {
                                JSONArray phoneArray = info.getJSONArray("phones");
                                String [] phoneList = phoneArray.toString().split(",");
                                phone = phoneList[0].replaceAll("[\\[\\]\"]", "");
                            }
                            if(info.has("urls")) {
                                JSONArray websites = info.getJSONArray("urls");
                                String [] websiteList = websites.toString().split(",");
                                website = websiteList[0].replaceAll("[\\[\\]\"\\\\]", "");
                            }
                            
                            if(info.has("emails")) {
                                JSONArray emails = info.getJSONArray("emails");
                                String [] emailList = emails.toString().split(",");
                                email = emailList[0].replaceAll("[\\[\\]\"]", "");
                            }
                            
                            String photoURL="";
                            if(info.has("photoUrl")) {
                                photoURL = info.getString("photoUrl").replaceAll("[\\[\\]\"]", "");
                                if(!photoURL.contains("https"))
                                    photoURL = photoURL.replaceAll("http","https").trim();
                            }

                            JSONArray channels;
                            String facebook ="", twitter="", youtube="";
                            if(info.has("channels")) {
                                channels = (JSONArray) info.get("channels");
                                for (int j = 0; j < channels.length(); j++) {
                                    JSONObject info2 = channels.getJSONObject(j);
                                    if(info2.getString("type").equals("Facebook"))
                                        facebook = info2.getString("id");
                                    if(info2.getString("type").equals("Twitter"))
                                        twitter = info2.getString("id");
                                    if(info2.getString("type").equals("YouTube"))
                                        youtube = info2.getString("id");
                                }
                            }
                            final Official official = new Official(official_name,official_position,addressLine,party,phone,website,email,photoURL,facebook, twitter, youtube);
                            mainActivity.runOnUiThread(() ->  mainActivity.updateList(official));
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "parseJSON: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "run: ", e);
        }

    }
}
