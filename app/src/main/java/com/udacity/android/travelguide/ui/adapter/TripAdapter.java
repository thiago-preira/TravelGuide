package com.udacity.android.travelguide.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.android.travelguide.R;
import com.udacity.android.travelguide.model.Trip;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripViewHolder> {

    private final Context mContext;
    private final TripOnClickListener tripOnClickListener;
    private final List<Trip> mTripList;

    public TripAdapter(Context context, TripOnClickListener tripOnClickListener, List<Trip> tripList) {
        this.mContext = context;
        this.tripOnClickListener = tripOnClickListener;
        this.mTripList = tripList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trip_item,parent,false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder tripViewHolder, final int position) {
        Trip trip = mTripList.get(position);
        if(tripOnClickListener!=null){
            tripViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tripOnClickListener.onTripClick(v,position);
                }
            });
        }
        tripViewHolder.loadTrip(trip, mContext);
    }

    @Override
    public int getItemCount() {
        if (mTripList == null)
            return 0;
        return mTripList.size();
    }



    public interface TripOnClickListener {
        void onTripClick(View view, int position);
    }
}
