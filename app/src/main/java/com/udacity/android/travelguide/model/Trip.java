package com.udacity.android.travelguide.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Trip {

    private String tripId;
    private String location;
    private String startDate;
    private String endDate;
    private String photoUrl;

    public Trip() {
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getDuration() {
        return "12 JAN - 20 FEV 2019";
    }
}
