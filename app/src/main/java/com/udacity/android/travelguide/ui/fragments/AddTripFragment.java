package com.udacity.android.travelguide.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
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
import com.udacity.android.travelguide.BuildConfig;
import com.udacity.android.travelguide.R;
import com.udacity.android.travelguide.data.AddTripAsyncTask;
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
import static com.udacity.android.travelguide.util.Constants.ADD_SPOT_REQUEST_CODE;
import static com.udacity.android.travelguide.util.Constants.AUTOCOMPLETE_REQUEST_CODE;
import static com.udacity.android.travelguide.util.Constants.PHOTO_URL_TEMPLATE;
import static com.udacity.android.travelguide.util.Constants.SPOT_KEY;
import static com.udacity.android.travelguide.util.Constants.TRIP_KEY;

public class AddTripFragment extends BaseFragment {

    public static final String TAG = AddTripFragment.class.getSimpleName();

    private EditText mPlacePickerEditText;
    private EditText mStartDateEditText;
    private EditText mEndDateEditText;
    private Trip mTrip = new Trip();
    private RecyclerView mSpotRecyclerView;
    private SpotAdapter adapter;

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
            populate(trip);
        }
    }

    private void populate(Trip trip) {
        mTrip = trip;
        mStartDateEditText.setText(mTrip.getStartDate());
        mEndDateEditText.setText(mTrip.getEndDate());
        mPlacePickerEditText.setText(mTrip.getLocation());
        setSpotList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_trip, container, false);
        setPlacePickerEditText(view);
        setStartDateEditText(view);
        setEndDateEditText(view);
        mSpotRecyclerView = view.findViewById(R.id.rv_spot_list);
        return view;
    }

    private void setSpotList() {
        if (mTrip.getSpots() != null) {
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
                itemModel.setSpot(spot);
                items.add(itemModel);
            }

            adapter = new SpotAdapter(getContext(), items,onStopClick());
            DividerItemDecoration divider = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
            divider.setDrawable(getResources().getDrawable(R.drawable.recycler_view_divider));
            mSpotRecyclerView.addItemDecoration(divider);
            mSpotRecyclerView.setAdapter(adapter);
            mSpotRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }
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
                        mTrip.setEndDate(date);
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
                        mTrip.setStartDate(date);
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
                ((TripActivity) getActivity()).buildWithTripData(mTrip);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
                toast(getString(R.string.error_searching_places));
            } else if (resultCode == RESULT_CANCELED) {
            }
        } else if (requestCode == ADD_SPOT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Spot spot = Parcels.unwrap(data.getParcelableExtra(SPOT_KEY));
                mTrip.addSpot(spot);
                setSpotList();
            }
        }
    }

    private void retrieveDataFromPlace(Place place) {
        mTrip.setLocation(place.getName());
        PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0);
        String photoUrl = String.format(PHOTO_URL_TEMPLATE,
                photoMetadata.a(), BuildConfig.GooglePlacesApiKey
        );
        mTrip.setPhotoUrl(photoUrl);
        mPlacePickerEditText.setText(mTrip.getLocation());
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveTrip() {
        AddTripAsyncTask asyncTask = new AddTripAsyncTask(getContext(),getActivity());
        asyncTask.execute(mTrip);
    }

    private SpotAdapter.OnSpotClickListener onStopClick(){
        return (view, position) -> {
            ListItem item = adapter.getItem(position);
            Spot spot = (Spot) item.getData();
            if(spot!= null) {
                String stringUri = String.format("geo:0,0?q=%f,%f(%s)", spot.getLatitude(), spot.getLongitude(),spot.getName());
                Uri gmmIntentUri = Uri.parse(stringUri);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        };
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getArguments().putParcelable(TRIP_KEY, Parcels.wrap(mTrip));
    }
}
