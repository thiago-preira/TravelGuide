package com.udacity.android.travelguide.ui.recyclerview;

import java.util.Objects;

public class HeaderModel implements ListItem<String> {

    String header;


    @Override
    public boolean isHeader() {
        return true;
    }

    @Override
    public String getName() {
        return header;
    }

    @Override
    public String getData() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeaderModel that = (HeaderModel) o;
        return Objects.equals(header, that.header);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header);
    }
}
