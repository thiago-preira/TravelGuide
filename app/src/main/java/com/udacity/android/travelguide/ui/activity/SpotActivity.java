package com.udacity.android.travelguide.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.udacity.android.travelguide.R;
import com.udacity.android.travelguide.model.Spot;
import com.udacity.android.travelguide.model.Trip;
import com.udacity.android.travelguide.util.DateUtils;

import org.parceler.Parcels;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.udacity.android.travelguide.util.Constants.AUTOCOMPLETE_REQUEST_CODE;
import static com.udacity.android.travelguide.util.Constants.SPOT_KEY;
import static com.udacity.android.travelguide.util.Constants.TRIP_KEY;

public class SpotActivity extends BaseActivity {

    private EditText mSpotPickerEditText;
    private EditText mSpotDateEditText;
    private EditText mSpotDescriptionEditText;
    private Spot mSpot;
    private Trip mTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot);
        setUpToolbar();
        getSupportActionBar().setTitle(getString(R.string.add_spot));
        mTrip = Parcels.unwrap(getIntent().getParcelableExtra(TRIP_KEY));
        if(savedInstanceState !=null){
            mSpot = Parcels.unwrap(savedInstanceState.getParcelable(SPOT_KEY));
        }else{
            mSpot = new Spot();
        }
        setSpotPicker();
        setSpotDate();

        mSpotDescriptionEditText = findViewById(R.id.et_spot_description);
    }

    private void setSpotDate() {
        mSpotDateEditText = findViewById(R.id.et_spot_date);

        mSpotDateEditText.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    (view1, year, month, dayOfMonth) -> {
                        String date = DateUtils.dateAsString(year, month, dayOfMonth);
                        mSpotDateEditText.setText(date);
                        mSpot.setDate(date);
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            if(mTrip!=null){
                if(mTrip.getEndDate()!=null){
                    Date maxDate = DateUtils.fromString(mTrip.getEndDate());
                    datePickerDialog.getDatePicker().setMaxDate(maxDate.getTime());
                }
                if(mTrip.getStartDate()!=null){
                    Date minDate = DateUtils.fromString(mTrip.getStartDate());
                    datePickerDialog.getDatePicker().setMinDate(minDate.getTime());
                }
            }
            datePickerDialog.show();
        });
    }

    private void setSpotPicker() {
        mSpotPickerEditText = findViewById(R.id.et_spot_picker);
        mSpotPickerEditText.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(getActivity());
            getActivity().startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_spot_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.save_trip:
                addSpot();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addSpot() {
        if(mSpot.getDate()==null || mSpot.getName() == null){
            toast(getString(R.string.spot_required_message));
            return;
        }
        mSpot.setDescription(mSpotDescriptionEditText.getText().toString());
        Intent returnIntent = new Intent();
        returnIntent.putExtra(SPOT_KEY,Parcels.wrap(mSpot));
        setResult(Activity.RESULT_OK,returnIntent);
        toast(getString(R.string.spot_saved));
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                retrieveDataFromPlace(place);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
                toast(getString(R.string.error_searching_places));
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    private void retrieveDataFromPlace(Place place) {
        mSpot.setName(place.getName());
        mSpotPickerEditText.setText(place.getName());
        mSpot.setLatitude(place.getLatLng().latitude);
        mSpot.setLongitude(place.getLatLng().longitude);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SPOT_KEY, Parcels.wrap(mSpot));
    }
}
