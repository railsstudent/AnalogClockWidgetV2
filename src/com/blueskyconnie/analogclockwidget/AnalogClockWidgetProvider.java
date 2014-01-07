package com.blueskyconnie.analogclockwidget;

import java.util.Calendar;
import java.util.Random;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class AnalogClockWidgetProvider extends AppWidgetProvider {

	private static Random rnd = new Random(System.currentTimeMillis());
	
	private static int ONE_SEC = 1000;
	private static int ONE_MIN = 60 * ONE_SEC;
	
	private static String MY_OWN_WIDGET_UPDATE = "MY_OWN_WIDGET_UPDATE";
	private static String MY_OWN_WIDGET_BACKGROUND = "MY_OWN_WIDGET_BACKGROUND";
	private static int[] ImageIds = { R.drawable.kero_small, R.drawable.bob, 
		R.drawable.kitty1, R.drawable.kitty2, R.drawable.kitty3, R.drawable.kitty4, R.drawable.kitty5, 
		R.drawable.kitty6, R.drawable.kitty7, R.drawable.kitty8, R.drawable.kitty9, R.drawable.kitty10, 
		R.drawable.kitty11, R.drawable.kitty12, R.drawable.kitty13, R.drawable.kitty14, R.drawable.kitty15		
	};

	public PendingIntent[] createPendingIntents(Context context) {
		PendingIntent[] pendingIntents = new PendingIntent[2];
		
		Intent intent = new Intent(MY_OWN_WIDGET_UPDATE);
		pendingIntents[0] = PendingIntent.getBroadcast(context, 0, intent, 0);

		Intent intent1 = new Intent(MY_OWN_WIDGET_BACKGROUND);
		pendingIntents[1] = PendingIntent.getBroadcast(context, 1, intent1, 0);
		return pendingIntents;
	}
	
	public void onDisabled(Context context) {
		super.onDisabled(context);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent[] pendingIntents = createPendingIntents(context);
		for (int i = 0; i < pendingIntents.length; i++) {
			alarmManager.cancel(pendingIntents[i]);
		}
	}

	public void onEnabled(Context context) {
		super.onEnabled(context);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent[] pendingIntents = createPendingIntents(context);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.add(Calendar.MINUTE, 1);
		alarmManager.setRepeating(AlarmManager.RTC, cal.getTimeInMillis(), ONE_MIN, pendingIntents[0]);

		Calendar calBgrd = Calendar.getInstance();
		calBgrd.setTimeInMillis(System.currentTimeMillis());
		calBgrd.add(Calendar.MINUTE, 30);
		alarmManager.setRepeating(AlarmManager.RTC, calBgrd.getTimeInMillis(), 30 * ONE_MIN, pendingIntents[1]);
	}
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		for (int i = 0; i < appWidgetIds.length; i++) {
			updateTime(context, appWidgetManager, appWidgetIds[i]);
		}
	}

	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if (intent != null) {
			if (intent.getAction().equals(MY_OWN_WIDGET_UPDATE)) {
				AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
				ComponentName appWidget = new ComponentName(context.getPackageName(), 
						AnalogClockWidgetProvider.class.getName());
				int[] appWidgetIds = appWidgetManager.getAppWidgetIds(appWidget);
				onUpdate(context, appWidgetManager, appWidgetIds);
			} else if (intent.getAction().equals(MY_OWN_WIDGET_BACKGROUND)) {
				// update background of the analog clock
				AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
				ComponentName appWidget = new ComponentName(context.getPackageName(), 
						AnalogClockWidgetProvider.class.getName());
				int[] appWidgetIds = appWidgetManager.getAppWidgetIds(appWidget);
				updateImages(context, appWidgetManager, appWidgetIds);
			}
		} 
	}

	private static void updateImages(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int i = 0; i < appWidgetIds.length; i++) {
			updateImage(context, appWidgetManager, appWidgetIds[i]);
		}
	}
	
	private static void updateImage(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_main);
		int imgId = ImageIds[rnd.nextInt(ImageIds.length)];
		views.setImageViewResource(R.id.imgView, imgId);
		appWidgetManager.updateAppWidget(appWidgetId, views);
	}
	
	private static void updateTime(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_main);
		appWidgetManager.updateAppWidget(appWidgetId, views);
	}
}
