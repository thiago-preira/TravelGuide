package com.udacity.android.travelguide.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.udacity.android.travelguide.R;
import com.udacity.android.travelguide.model.Spot;

import java.util.List;

public class TripAppWidgetProvider extends AppWidgetProvider {

    private static List<Spot> mSpots;

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId, List<Spot> spots) {
        mSpots = spots;
        String widgetText = "Falhou";
        if (mSpots != null) {
            widgetText = getSpotsAsText();
        }
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.travel_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static String getSpotsAsText() {
        StringBuffer buffer = new StringBuffer();
        for (Spot spot : mSpots) {
            String format = String.format("%s - %s \n %s \n",
                    spot.getDate(),
                    spot.getName(),
                    spot.getDescription());
            buffer.append(format);
        }
        return buffer.toString();
    }
}
