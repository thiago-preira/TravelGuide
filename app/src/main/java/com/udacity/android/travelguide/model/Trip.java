package com.udacity.android.travelguide.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.udacity.android.travelguide.util.DateUtils;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Parcel
@IgnoreExtraProperties
public class Trip {

    private String tripId;
    private String location;
    private String startDate;
    private String endDate;
    private String photoUrl;
    private List<Spot> spots;

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

    public List<Spot> getSpots() {
        return spots;
    }

    public void setSpots(List<Spot> spots) {
        this.spots = spots;
    }

    @Exclude
    public String getDuration() {
        return DateUtils.getDuration(startDate, endDate);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("tripId", tripId);
        result.put("location", location);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("photoUrl", photoUrl);
        result.put("spots", spots);
        return result;
    }

    public void addSpot(Spot spot) {
        if (spots == null) {
            spots = new ArrayList<>();
        }
        spots.add(spot);
    }

    public Spot getSpot(int position) {
        if(spots == null){
            return null;
        }

        return spots.get(position);
    }
}
