package far1db.clockwidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class ClockWidget extends AppWidgetProvider {

    private String ACTION_DATE_ALARM = "WidgetDateAlarm";
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Update all instances of the widget
        for (int appWidgetId : appWidgetIds) {
            updateWidgetDate(context, appWidgetManager, appWidgetId);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Set an Alarm to change the date everyday at approximately 00:00
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ClockWidget.class);
        intent.setAction(ACTION_DATE_ALARM);

        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        // Fire the alarm everyday at 00:00
        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Cancel the alarm when the last instance of the widget is removed
        alarmManager.cancel(alarmIntent);

        super.onDisabled(context);
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

