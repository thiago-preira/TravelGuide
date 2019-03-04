package com.udacity.android.travelguide.ui.recyclerview;

import com.udacity.android.travelguide.model.Spot;

import java.util.Objects;

public class ChildModel implements ListItem<Spot> {

    String child;
    String description;
    Spot spot;

    public void setChild(String child) {
        this.child = child;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    @Override
    public boolean isHeader() {
        return false;
    }

    @Override
    public String getName() {
        return child;
    }

    @Override
    public Spot getData() {
        return spot;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChildModel that = (ChildModel) o;
        return Objects.equals(child, that.child);
    }

    @Override
    public int hashCode() {
        return Objects.hash(child);
    }
}
