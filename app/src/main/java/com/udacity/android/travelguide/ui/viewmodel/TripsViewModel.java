package com.udacity.android.travelguide.ui.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.udacity.android.travelguide.data.FirebaseQueryLiveData;
import com.udacity.android.travelguide.model.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TripsViewModel extends ViewModel {
    private static final String USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static final DatabaseReference TRIP_REFERENCE = FirebaseDatabase.getInstance()
            .getReference("/trips").child(USER_ID);
    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(TRIP_REFERENCE);

    GenericTypeIndicator<Map<String, Trip>> typeIndicator = new GenericTypeIndicator<Map<String, Trip>>() {
    };

    private final LiveData<List<Trip>> tripLiveData = Transformations.map(liveData, new Deserializer());

    private class Deserializer implements Function<DataSnapshot, List<Trip>> {
        @Override
        public List<Trip> apply(DataSnapshot dataSnapshot) {
            if(dataSnapshot.getValue(typeIndicator)!=null) {
                return new ArrayList<>(dataSnapshot.getValue(typeIndicator).values());
            }
            return new ArrayList<>();
        }
    }

    @NonNull
    public LiveData<List<Trip>> getDataSnapshotLiveData() {
        return tripLiveData;
    }
}
