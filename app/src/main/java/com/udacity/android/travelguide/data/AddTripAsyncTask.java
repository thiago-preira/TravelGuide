package com.udacity.android.travelguide.data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.android.travelguide.R;
import com.udacity.android.travelguide.model.Trip;

public class AddTripAsyncTask extends AsyncTask<Trip, Void, Void> {

    private Trip mTrip;
    ProgressDialog progressDialog;
    private Context mContext;
    private Activity activity;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();;
    private static final String USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public AddTripAsyncTask(Context context, Activity activity) {
        this.mContext = context;
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Trip... trips) {
        mTrip  = trips[0];
        if (mTrip.getTripId() == null) {
            String key = mDatabase.child("/trips").child(USER_ID).push().getKey();
            mTrip.setTripId(key);
            mDatabase.child("/trips").child(USER_ID).child(key).setValue(mTrip);
        } else {
            mDatabase.child("/trips").child(USER_ID).child(mTrip.getTripId()).updateChildren(mTrip.toMap());
        }

        return null;
    }


    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(mContext,
                mContext.getResources().getString(R.string.adding_trip_message_title),
                mContext.getResources().getString(R.string.adding_trip_message));
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        activity.finish();
    }
}
