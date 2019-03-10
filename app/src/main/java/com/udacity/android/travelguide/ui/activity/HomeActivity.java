package com.udacity.android.travelguide.ui.activity;

import android.app.ActivityOptions;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.udacity.android.travelguide.R;
import com.udacity.android.travelguide.model.Trip;
import com.udacity.android.travelguide.ui.adapter.TripAdapter;
import com.udacity.android.travelguide.ui.viewmodel.TripsViewModel;

import org.parceler.Parcels;

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
    private TextView mAddTripTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setUpToolbar();
        mTripsRecyclerView = findViewById(R.id.rv_trips);
        mTripsProgressBar = findViewById(R.id.pb_trips);
        mAddTripFAB = findViewById(R.id.fab_add_trip);
        mAddTripTip = findViewById(R.id.tv_add_trip_tip);
        showProgress();

        mAddTripFAB.setOnClickListener(v -> startActivity(new Intent(getContext(), TripActivity.class)));

        TripsViewModel viewModel = ViewModelProviders.of(this).get(TripsViewModel.class);
        LiveData<List<Trip>> liveData = viewModel.getDataSnapshotLiveData();
        liveData.observe(this, trips -> {
            if (trips != null && trips.size() >0) {
                setAdapter(trips);
                setLayoutManager();
                hideProgress();
                mAddTripTip.setVisibility(View.GONE);
            }else{
                mTripsRecyclerView.setVisibility(View.GONE);
                hideProgress();
                mAddTripTip.setVisibility(View.VISIBLE);
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
        return (view, position) -> {
            Trip trip = mTrips.get(position);
            Intent tripIntent = new Intent(getContext(), TripActivity.class);
            tripIntent.putExtra(TRIP_KEY, Parcels.wrap(trip));
            // Check if we're running on Android 5.0 or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(tripIntent,
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            } else {
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
