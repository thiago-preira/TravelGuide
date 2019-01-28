package com.udacity.android.travelguide.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private RecyclerView mTripsRecyclerView;
    private TripAdapter mTripAdapter;
    private DatabaseReference mDatabase;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private String mUserId;
    private List<Trip> mTrips = new ArrayList<>();
    private ValueEventListener mTripEventListener;
    private ProgressBar mTripsProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setUpToolbar();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mTripsRecyclerView = findViewById(R.id.rv_trips);
        mTripsProgressBar = findViewById(R.id.pb_trips);

        mUserId = getUid();

        showProgress();

        mTripEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (databaseError.getCode() != DatabaseError.DISCONNECTED) {
                    Log.e(TAG, String.format("%s:%s", databaseError.getMessage(), databaseError.getDetails()));
                }
            }
        };

        mDatabase.child("trips").child(mUserId).addValueEventListener(mTripEventListener);
    }

    private void hideProgress() {
        mTripsProgressBar.setVisibility(View.INVISIBLE);
        mTripsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showProgress() {
        mTripsProgressBar.setVisibility(View.VISIBLE);
        mTripsRecyclerView.setVisibility(View.INVISIBLE);
    }


    private void showData(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<List<Trip>> typeIndicator = new GenericTypeIndicator<List<Trip>>() {
        };
        mTrips = dataSnapshot.getValue(typeIndicator);
        mTripsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTripAdapter = new TripAdapter(this, onTripClick(), mTrips);
        mTripsRecyclerView.setAdapter(mTripAdapter);
        hideProgress();
    }

    private TripAdapter.TripOnClickListener onTripClick() {
        return new TripAdapter.TripOnClickListener() {
            @Override
            public void onTripClick(View view, int position) {

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
        removeValueListener();
        FirebaseAuth.getInstance().signOut();
        startActivity(createIntentFor(SplashActivity.class));
    }

    @Override
    protected void onStop() {
        removeValueListener();
        super.onStop();
    }

    private void removeValueListener() {
        if (mTripEventListener != null) {
            mDatabase.removeEventListener(mTripEventListener);
        }
    }

    @Override
    protected void onDestroy() {
        removeValueListener();
        super.onDestroy();
    }
}
