package com.udacity.android.travelguide.util;

public interface Constants {
    Boolean ENABLE_SMART_LOCK = false;
    String TRIP_KEY = "trip";
    String SPOT_KEY = "spot";
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    int ADD_SPOT_REQUEST_CODE = 2;
    String PHOTO_URL_TEMPLATE = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=%s&key=%s";
}
