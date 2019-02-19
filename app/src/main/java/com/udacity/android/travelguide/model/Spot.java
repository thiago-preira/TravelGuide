package com.udacity.android.travelguide.model;

import android.support.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

@Parcel
@IgnoreExtraProperties
public class Spot  implements Comparable{

    private String name;
    private String description;
    private String date;
    private Double latitude;
    private Double longitude;

    public Spot() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if( o == null)
            return 0;
        if(!(o instanceof Spot))
            return 0;
        Spot that = (Spot) o;
        return this.getDate().compareTo(that.getDate());
    }
}
