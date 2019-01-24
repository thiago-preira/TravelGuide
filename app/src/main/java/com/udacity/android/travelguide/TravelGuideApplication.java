package com.udacity.android.travelguide;

import android.app.Application;

public class TravelGuideApplication extends Application {

    private static TravelGuideApplication instance;

    public static TravelGuideApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
