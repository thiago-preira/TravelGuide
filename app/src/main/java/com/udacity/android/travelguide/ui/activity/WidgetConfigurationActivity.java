package com.udacity.android.travelguide.ui.activity;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.udacity.android.travelguide.R;
import com.udacity.android.travelguide.model.Trip;
import com.udacity.android.travelguide.ui.viewmodel.TripsViewModel;
import com.udacity.android.travelguide.ui.widget.TripAppWidgetProvider;

import java.util.List;

public class WidgetConfigurationActivity extends BaseActivity {

    private ListView mListView;
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private ArrayAdapter<Trip> tripArrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wigdet_configuration);
        setResult(RESULT_CANCELED);
        mListView = findViewById(R.id.lv_widget_recipes);
        TripsViewModel viewModel = ViewModelProviders.of(this).get(TripsViewModel.class);
        LiveData<List<Trip>> liveData = viewModel.getDataSnapshotLiveData();
        liveData.observe(this, trips -> {
            tripArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
            tripArrayAdapter.addAll(trips);
            mListView.setAdapter(tripArrayAdapter);
        });

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish();
            }
        }

        mListView.setOnItemClickListener(onTripClickListener());


    }

    private AdapterView.OnItemClickListener onTripClickListener() {
        return (parent, view, position, id) -> {
            Context context = getApplicationContext();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            Trip trip = tripArrayAdapter.getItem(position);
            TripAppWidgetProvider.updateAppWidget(getContext(),
                    appWidgetManager,
                    appWidgetId,
                    trip.getSpots()
            );
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        };
    }
}
