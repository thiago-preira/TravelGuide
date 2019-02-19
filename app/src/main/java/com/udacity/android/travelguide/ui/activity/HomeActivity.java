package com.udacity.android.travelguide.ui.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.udacity.android.travelguide.R;
import com.udacity.android.travelguide.model.Trip;
import com.udacity.android.travelguide.ui.adapter.TripAdapter;
import com.udacity.android.travelguide.ui.viewmodel.TripViewModel;
import com.udacity.android.travelguide.util.DeviceUtils;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.udacity.android.travelguide.util.Constants.TRIP_KEY;

public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private RecyclerView mTripsRecyclerView;
    private TripAdapter mTripAdapter;
    private List<Trip> mTrips = new ArrayList<>();
    private ProgressBar mTripsProgressBar;
    private FloatingActionButton mAddTripFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setUpToolbar();
        mTripsRecyclerView = findViewById(R.id.rv_trips);
        mTripsProgressBar = findViewById(R.id.pb_trips);
        mAddTripFAB = findViewById(R.id.fab_add_trip);
        showProgress();

        mAddTripFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TripActivity.class));
            }
        });

        TripViewModel viewModel = ViewModelProviders.of(this).get(TripViewModel.class);
        LiveData<List<Trip>> liveData = viewModel.getDataSnapshotLiveData();
        liveData.observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(@Nullable List<Trip> trips) {
                if (trips != null) {
                    setAdapter(trips);
                    setLayoutManager();
                    hideProgress();
                }
            }
        });
    }

    private void hideProgress() {
        mTripsProgressBar.setVisibility(View.INVISIBLE);
        mTripsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showProgress() {
        mTripsProgressBar.setVisibility(View.VISIBLE);
        mTripsRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void setAdapter(List<Trip> trips) {
        mTrips = trips;
        mTripAdapter = new TripAdapter(this, onTripClick(), mTrips);
        mTripsRecyclerView.setAdapter(mTripAdapter);
    }

    private void setLayoutManager() {
        mTripsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private TripAdapter.TripOnClickListener onTripClick() {
        return new TripAdapter.TripOnClickListener() {
            @Override
            public void onTripClick(View view, int position) {
                Trip trip = mTrips.get(position);
                Intent tripIntent = new Intent(getContext(), TripActivity.class);
                tripIntent.putExtra(TRIP_KEY, Parcels.wrap(trip));
                startActivity(tripIntent);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.filter:
                signOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(createIntentFor(SplashActivity.class));
    }

}
