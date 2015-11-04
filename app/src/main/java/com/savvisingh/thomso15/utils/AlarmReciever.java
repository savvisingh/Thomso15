package com.savvisingh.thomso15.utils;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import com.savvisingh.thomso15.R;


public class AlarmReciever extends BroadcastReceiver {
    DatabaseHelper myDbHelper;
	NotificationManager nm;
	private long[] mVibratePattern = { 0, 200, 200, 300 };
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		
		 myDbHelper = new DatabaseHelper(context);
		 
		String eventname = intent.getExtras().getString("event");
		
		 String eventvenue= myDbHelper.getVenueDisplay(eventname);
		 Log.i("event_name", eventname);
		int id =myDbHelper.getID(eventname);
		Log.i("id", String.valueOf(id));
		Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.thomsologosmallicon);
		
		
		Notification.Builder notificationBuilder = new Notification.Builder(
				context).setTicker(eventname)
				.setSmallIcon(R.drawable.ic_stat_toggle_star)
				.setLargeIcon(largeIcon)
				.setAutoCancel(true).setContentTitle(eventname)
				
   				.setContentText(eventvenue)
				.setVibrate(mVibratePattern);
			

		// Pass the Notification to the NotificationManager:
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(id,
				notificationBuilder.build());
		
		
		
		
				
		
		
		
	}
};