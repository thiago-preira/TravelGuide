package com.udacity.android.travelguide.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.android.travelguide.R;
import com.udacity.android.travelguide.model.Spot;
import com.udacity.android.travelguide.model.Trip;
import com.udacity.android.travelguide.ui.activity.TripActivity;
import com.udacity.android.travelguide.ui.adapter.SpotAdapter;
import com.udacity.android.travelguide.ui.recyclerview.ChildModel;
import com.udacity.android.travelguide.ui.recyclerview.HeaderModel;
import com.udacity.android.travelguide.ui.recyclerview.ListItem;
import com.udacity.android.travelguide.util.DateUtils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.udacity.android.travelguide.util.Constants.AUTOCOMPLETE_REQUEST_CODE;
import static com.udacity.android.travelguide.util.Constants.PHOTO_URL_TEMPLATE;
import static com.udacity.android.travelguide.util.Constants.TRIP_KEY;

public class AddTripFragment extends BaseFragment {

    public static final String TAG = AddTripFragment.class.getSimpleName();

    private EditText mPlacePickerEditText;
    private EditText mStartDateEditText;
    private EditText mEndDateEditText;
    private Trip mTrip = new Trip();
    private DatabaseReference mDatabase;
    private static final String USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private RecyclerView mSpotRecyclerView;

    public static AddTripFragment newInstance(Trip trip) {
        AddTripFragment fragment = new AddTripFragment();
        Bundle args = new Bundle();
        args.putParcelable(TRIP_KEY, Parcels.wrap(trip));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Trip trip = Parcels.unwrap(getArguments().getParcelable(TRIP_KEY));
        if (trip != null) {
            mTrip = trip;
            mStartDateEditText.setText(mTrip.getStartDate());
            mEndDateEditText.setText(mTrip.getEndDate());
            setSpotList();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_trip, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setPlacePickerEditText(view);
        setStartDateEditText(view);
        setEndDateEditText(view);
        mSpotRecyclerView = view.findViewById(R.id.rv_spot_list);
        return view;
    }

    private void setSpotList() {


        List<ListItem> items = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        List<Spot> spotList = mTrip.getSpots();
        Collections.sort(spotList);


        for (Spot spot : spotList) {
            if (!headers.contains(spot.getDate())) {
                HeaderModel header = new HeaderModel();
                header.setHeader(DateUtils.dateAsString(spot.getDate()));
                items.add(header);
                headers.add(spot.getDate());
            }
            ChildModel itemModel = new ChildModel();
            itemModel.setChild(spot.getName());
            itemModel.setDescription(spot.getDescription());
            items.add(itemModel);
        }

        SpotAdapter adapter = new SpotAdapter(getContext(), items);
        mSpotRecyclerView.setAdapter(adapter);
        mSpotRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    private void setEndDateEditText(View view) {
        mEndDateEditText = view.findViewById(R.id.et_trip_end_date);
        mEndDateEditText.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            new android.app.DatePickerDialog(
                    getActivity(),
                    (view1, year, month, dayOfMonth) -> {
                        String date = DateUtils.dateAsString(year, month, dayOfMonth);
                        mEndDateEditText.setText(date);
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            ).show();
        });
    }

    private void setStartDateEditText(View view) {
        mStartDateEditText = view.findViewById(R.id.et_trip_start_date);
        mStartDateEditText.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            new android.app.DatePickerDialog(getActivity(),
                    (view1, year, month, dayOfMonth) -> {
                        String date = DateUtils.dateAsString(year, month, dayOfMonth);
                        mStartDateEditText.setText(date);
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            ).show();
        });
    }

    private void setPlacePickerEditText(View view) {
        mPlacePickerEditText = view.findViewById(R.id.et_place_picker);
        mPlacePickerEditText.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS);
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(getActivity());
            getActivity().startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                retrieveDataFromPlace(place);
                ((TripActivity) getActivity()).buildToolbarWithTripData(mTrip);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
                toast(getString(R.string.error_searching_places));
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    private void retrieveDataFromPlace(Place place) {
        mTrip.setLocation(place.getName());
        PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0);
        String photoUrl = String.format(PHOTO_URL_TEMPLATE,
                photoMetadata.a(),
                getString(R.string.google_places_api_key));
        mTrip.setPhotoUrl(photoUrl);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.save_trip:
                saveTrip();
                toast(getString(R.string.trip_saved));
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveTrip() {
        mTrip.setStartDate(mStartDateEditText.getText().toString());
        mTrip.setEndDate(mEndDateEditText.getText().toString());

        List<Spot> spots = new ArrayList<>();
                Spot spot2 = new Spot();
        spot2.setName("Lugar 2");
        spot2.setDescription("Descricao lugar 2");
        spot2.setDate(mEndDateEditText.getText().toString());

        Spot spot1 = new Spot();
        spot1.setName("Lugar 1");
        spot1.setDescription("Descricao lugar 1");
        spot1.setDate(mStartDateEditText.getText().toString());

        spots.add(spot1);
        spots.add(spot2);

        for (int i = 0; i < 10; i++) {
            Spot spot3 = new Spot();
            spot3.setName("Lugar 3");
            spot3.setDescription("Descricao lugar 3");
            spot3.setDate(mStartDateEditText.getText().toString());
            spots.add(spot3);
        }


        mTrip.setSpots(spots);

        if (mTrip.getTripId() == null) {
            String key = mDatabase.child("/trips").child(USER_ID).push().getKey();
            mTrip.setTripId(key);
            mDatabase.child("/trips").child(USER_ID).child(key).setValue(mTrip);
        } else {
            mDatabase.child("/trips").child(USER_ID).child(mTrip.getTripId()).updateChildren(mTrip.toMap());
        }

    }
}
