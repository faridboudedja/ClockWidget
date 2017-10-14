package far1db.clockwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.util.Date;

public class ClockWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Update all instances of the widget
        for (int appWidgetId : appWidgetIds) {
            updateWidgetDate(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private void updateWidgetDate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Get date
        Date date = new Date();
        date.getTime();

        // Format the date according to current Locale
        String dateStr = DateFormat.getDateInstance(DateFormat.FULL).format(date);

        // Get the remote views of the widget's layout
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_widget);
        // Display the date in the appropriate text view
        views.setTextViewText(R.id.tv_date, dateStr);

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

