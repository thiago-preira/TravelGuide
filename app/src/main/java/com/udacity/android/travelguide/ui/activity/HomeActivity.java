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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private RecyclerView tripsRecyclerView;
    private TripAdapter mTripAdapter;
    private DatabaseReference mDatabase;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private String mUserId;
    private List<Trip> mTrips = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setUpToolbar();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        tripsRecyclerView = findViewById(R.id.rv_trips);

        mUserId = getUid();

        mDatabase.child("trips").child(mUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG,databaseError.getDetails());
                toast("Error displaying trips");
            }
        });


    }

    private void showData(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<List<Trip>> typeIndicator = new GenericTypeIndicator<List<Trip>>() {};
        mTrips = dataSnapshot.getValue(typeIndicator);
        tripsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTripAdapter = new TripAdapter(this,onTripClick(), mTrips);
        tripsRecyclerView.setAdapter(mTripAdapter);
        //mTripAdapter.notifyDataSetChanged();
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
        inflater.inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
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

    private Trip createTrip(){
        Trip trip = new Trip();
        trip.setTripId(UUID.randomUUID().toString());
        trip.setLocation("Barcelona");
        trip.setStartDate(formatter.format(new Date()));
        trip.setEndDate(formatter.format(new Date()));
        return trip;
    }

}
