package com.udacity.android.travelguide.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.squareup.picasso.Picasso;
import com.udacity.android.travelguide.R;
import com.udacity.android.travelguide.model.Trip;
import com.udacity.android.travelguide.ui.fragments.AddTripFragment;

import org.parceler.Parcels;

import static com.udacity.android.travelguide.util.Constants.TRIP_KEY;

public class TripActivity extends BaseActivity {

    private CollapsingToolbarLayout mTripToolbar;
    private Toolbar mAddTripToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        mTripToolbar = findViewById(R.id.toolbarLayout);
        mAddTripToolbar = findViewById(R.id.tb_add_trip);

        configurePlacesSDK();

        Trip trip = Parcels.unwrap(getIntent().getParcelableExtra(TRIP_KEY));


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();

        if (trip != null) {
            buildToolbarWithTripData(trip);
        } else {
            buildToolbarWithoutTripData();
        }
        AddTripFragment addTripFragment = AddTripFragment.newInstance(trip);
        tx.replace(R.id.trip_fragment, addTripFragment);
        tx.commit();
    }

    private void buildToolbarWithoutTripData() {
        showCreateTripToolbar();
        setUpToolbar(R.id.tb_add_trip);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void buildToolbarWithTripData(Trip trip) {
        showImageToolbar();
        setUpToolbar();
        mTripToolbar.setTitle(trip.getLocation());
        ImageView appBar = findViewById(R.id.iv_app_bar);
        Picasso.with(getContext()).load(trip.getPhotoUrl()).into(appBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void configurePlacesSDK() {
        String apiKey = getString(R.string.google_places_api_key);

        if (apiKey.equals("")) {
            Toast.makeText(this, getString(R.string.error_api_key), Toast.LENGTH_LONG).show();
            return;
        }

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.trip_fragment);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    private void showCreateTripToolbar() {
        mTripToolbar.setVisibility(View.GONE);
        mAddTripToolbar.setVisibility(View.VISIBLE);
    }

    private void showImageToolbar() {
        mTripToolbar.setVisibility(View.VISIBLE);
        mAddTripToolbar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_trip_menu, menu);
        return true;
    }

}
