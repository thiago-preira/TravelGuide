package com.udacity.android.travelguide.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.squareup.picasso.Picasso;
import com.udacity.android.travelguide.BuildConfig;
import com.udacity.android.travelguide.R;
import com.udacity.android.travelguide.model.Trip;
import com.udacity.android.travelguide.ui.fragments.AddTripFragment;

import org.parceler.Parcels;

import static com.udacity.android.travelguide.util.Constants.ADD_SPOT_REQUEST_CODE;
import static com.udacity.android.travelguide.util.Constants.TRIP_KEY;

public class TripActivity extends BaseActivity {

    private CollapsingToolbarLayout mTripToolbar;
    private Toolbar mAddTripToolbar;
    private FloatingActionButton mAddSpotFloatAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        mTripToolbar = findViewById(R.id.toolbarLayout);
        mAddTripToolbar = findViewById(R.id.tb_add_trip);
        mAddSpotFloatAction = findViewById(R.id.fab_add_spot);
        mAddSpotFloatAction.setEnabled(false);
        configurePlacesSDK();

        Trip trip = Parcels.unwrap(getIntent().getParcelableExtra(TRIP_KEY));


        if (trip != null) {
            buildWithTripData(trip);
        } else {
            buildToolbarWithoutTripData();
        }
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction tx = fragmentManager.beginTransaction();
            AddTripFragment addTripFragment = AddTripFragment.newInstance(trip);
            tx.replace(R.id.trip_fragment, addTripFragment);
            tx.commit();
        }
    }

    private void buildToolbarWithoutTripData() {
        showCreateTripToolbar();
        setUpToolbar(R.id.tb_add_trip);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void buildWithTripData(Trip trip) {
        showImageToolbar();
        setUpToolbar();
        mTripToolbar.setTitle(trip.getLocation());
        ImageView appBar = findViewById(R.id.iv_app_bar);
        Picasso.with(getContext()).load(trip.getPhotoUrl()).into(appBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAddSpotFloatAction.setEnabled(true);
        mAddSpotFloatAction.setOnClickListener(v -> {
            Intent spotActivity = new Intent(getContext(), SpotActivity.class);
            spotActivity.putExtra(TRIP_KEY, Parcels.wrap(trip));
            startActivityForResult(spotActivity, ADD_SPOT_REQUEST_CODE);
        });
    }

    private void configurePlacesSDK() {
        String apiKey = BuildConfig.GooglePlacesApiKey;

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
