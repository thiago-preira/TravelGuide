package com.udacity.android.travelguide.ui.recyclerview;

public interface ListItem<T> {
    boolean isHeader();
    String getName();
    T getData();
}
