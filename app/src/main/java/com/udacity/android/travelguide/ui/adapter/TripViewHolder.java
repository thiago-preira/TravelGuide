package com.udacity.android.travelguide.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.android.travelguide.R;
import com.udacity.android.travelguide.model.Trip;

public class TripViewHolder extends RecyclerView.ViewHolder {

    ImageView mPhotoImageView;
    TextView mLocationTextView;
    TextView mDurationTextView;


    public TripViewHolder(@NonNull View itemView) {
        super(itemView);
        mPhotoImageView = itemView.findViewById(R.id.iv_trip_photo);
        mLocationTextView = itemView.findViewById(R.id.tv_trip_destiny);
        mDurationTextView = itemView.findViewById(R.id.tv_trip_duration);
    }

    public void loadTrip(Trip trip, Context context) {
        mLocationTextView.setText(trip.getLocation());
        mDurationTextView.setText(trip.getDuration());
        Picasso.with(context).load(trip.getPhotoUrl()).into(mPhotoImageView);
    }

}
