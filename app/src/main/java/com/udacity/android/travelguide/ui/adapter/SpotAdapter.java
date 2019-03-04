package com.udacity.android.travelguide.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.android.travelguide.R;
import com.udacity.android.travelguide.model.Spot;
import com.udacity.android.travelguide.ui.recyclerview.ChildModel;
import com.udacity.android.travelguide.ui.recyclerview.ListItem;

import java.util.List;

public class SpotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int LAYOUT_HEADER = 0;
    private static final int LAYOUT_CHILD = 1;

    private LayoutInflater inflater;
    private Context context;
    private List<ListItem> listItemArrayList;
    private OnSpotClickListener mOnSpotClickListener;

    public SpotAdapter( Context context, List<ListItem> listItemArrayList, OnSpotClickListener onSpotClickListener) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.listItemArrayList = listItemArrayList;
        this.mOnSpotClickListener = onSpotClickListener;
    }

    @Override
    public int getItemCount() {
        return listItemArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (listItemArrayList.get(position).isHeader()) {
            return LAYOUT_HEADER;
        } else
            return LAYOUT_CHILD;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;
        if (viewType == LAYOUT_HEADER) {
            View view = inflater.inflate(R.layout.spot_header, parent, false);
            holder = new SpotHeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.spot_item, parent, false);
            holder = new SpotViewHolder(view);
        }


        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder.getItemViewType() == LAYOUT_HEADER) {
            SpotHeaderViewHolder viewHolder = (SpotHeaderViewHolder) holder;
            viewHolder.mSpotHeaderTextView.setText(listItemArrayList.get(position).getName());
        } else {
            final ChildModel listItem = (ChildModel) listItemArrayList.get(position);
            SpotViewHolder spotViewHolder = (SpotViewHolder) holder;
            spotViewHolder.mSpotNameTextView.setText(listItem.getName());
            spotViewHolder.mSpotDescriptionTextView.setText(listItem.getDescription());
            if(mOnSpotClickListener!=null)
            spotViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnSpotClickListener.onSpotClick(v,position);
                }
            });
        }

    }

    class SpotHeaderViewHolder extends RecyclerView.ViewHolder {

        TextView mSpotHeaderTextView;

        public SpotHeaderViewHolder(View itemView) {
            super(itemView);
            mSpotHeaderTextView = itemView.findViewById(R.id.tv_spot_header);
        }

    }

    class SpotViewHolder extends RecyclerView.ViewHolder {

        TextView mSpotNameTextView;
        TextView mSpotDescriptionTextView;

        public SpotViewHolder(View itemView) {
            super(itemView);

            mSpotNameTextView = itemView.findViewById(R.id.tv_spot_name);
            mSpotDescriptionTextView = itemView.findViewById(R.id.tv_spot_description);
        }

    }

    public interface OnSpotClickListener{
        void onSpotClick(View view, int position);
    }

    public ListItem getItem(int position){
        return listItemArrayList.get(position);
    }
}
