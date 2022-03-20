package com.example.civiladvocacyapp;

import java.io.Serializable;

public class Official implements Serializable {
    private final String official_name;
    private final String official_position;
    private final String addressLine;
    private final String party;
    private final String phone;
    private final String website;
    private final String email;
    private final String photoURL;
    private final String facebook;
    private final String twitter;
    private final String youtube;

    public Official(String official_name, String official_position, String addressLine, String party,
                    String phone, String website, String email, String photoURL, String facebook, String twitter, String youtube) {
        this.official_name = official_name;
        this.official_position = official_position;
        this.addressLine = addressLine;
        this.party = party;
        this.phone = phone;
        this.website = website;
        this.email = email;
        this.photoURL = photoURL;
        this.facebook = facebook;
        this.twitter = twitter;
        this.youtube = youtube;
    }

    public String getName(){ return official_name;}
    public String getPosition(){return official_position;}
    public String getAddress(){return addressLine;}
    public String getParty(){return party;}
    public String getPhone(){return phone;}
    public String getWebsite(){return website;}
    public String getEmail(){return email;}
    public String getPhotoURL(){return photoURL;}
    public String getFacebook(){return facebook;}
    public String getTwitter(){return twitter;}
    public String getYoutube(){return youtube;}

}

