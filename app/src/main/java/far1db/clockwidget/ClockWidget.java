package far1db.clockwidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.AlarmClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ClockWidget extends AppWidgetProvider {

    private String ACTION_DATE_ALARM = "WidgetDateAlarm";
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Update all instances of the widget
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
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

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Get date
        Date date = new Date();
        date.getTime();

        // Format the date according to current Locale
        String dateStr = DateFormat.getDateInstance(DateFormat.FULL).format(date);

        // Get the remote views of the widget's layout
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_widget);
        // Display the date in the appropriate text view
        views.setTextViewText(R.id.tv_date, dateStr);

        // Open the default alarm app when the widget is clicked
        Intent clockIntent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);

        PackageManager packageManager = context.getPackageManager();
        List activities = packageManager.queryIntentActivities(clockIntent, PackageManager.MATCH_DEFAULT_ONLY);

        // Verify if there is an application to receive the intent
        if ( activities.size() > 0 ) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clockIntent, 0);
            views.setOnClickPendingIntent(R.id.layout_widget, pendingIntent);
        }

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName widget = new ComponentName(context.getApplicationContext(), ClockWidget.class);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(widget);

        // Update the widget instances if ACTION_DATE_ALARM is received
        if(intent.getAction().equals(ACTION_DATE_ALARM)) {
            if(appWidgetIds.length > 0) {
                onUpdate(context, appWidgetManager, appWidgetIds);
            }
        }

        super.onReceive(context, intent);
    }
}

