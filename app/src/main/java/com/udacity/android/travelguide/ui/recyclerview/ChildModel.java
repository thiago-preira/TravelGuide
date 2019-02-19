package com.udacity.android.travelguide.ui.recyclerview;

import java.util.Objects;

public class ChildModel implements ListItem {

    String child;
    String description;

    public void setChild(String child) {
        this.child = child;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean isHeader() {
        return false;
    }

    @Override
    public String getName() {
        return child;
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
