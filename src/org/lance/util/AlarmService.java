package org.lance.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import java.util.Calendar;

/**
 * ƒ÷÷”π§æﬂ
 * 
 * @author lance
 * 
 */
public class AlarmService {
	private static int period = 300000;

	public static void addAlarm(Context paramContext,
			PendingIntent paramPendingIntent, int paramInt) {
		AlarmManager localAlarmManager = (AlarmManager) paramContext
				.getSystemService(Context.ALARM_SERVICE);
		localAlarmManager.cancel(paramPendingIntent);
		if (paramInt == -1) {
			paramInt = period;
		}
		Calendar localCalendar = Calendar.getInstance();
		localCalendar.setTimeInMillis(System.currentTimeMillis());
		localCalendar.add(14, paramInt);
		localAlarmManager.set(0, localCalendar.getTimeInMillis(),
				paramPendingIntent);
	}

	public static void addAlarm(Context paramContext, int period,
			PendingIntent paramPendingIntent, int paramInt) {
		AlarmManager localAlarmManager = (AlarmManager) paramContext
				.getSystemService(Context.ALARM_SERVICE);
		localAlarmManager.cancel(paramPendingIntent);
		if (paramInt == -1) {
			paramInt = period;
		}
		Calendar localCalendar = Calendar.getInstance();
		localCalendar.setTimeInMillis(System.currentTimeMillis());
		localCalendar.add(14, paramInt);
		localAlarmManager.set(0, localCalendar.getTimeInMillis(),
				paramPendingIntent);
	}

	public static void deleteAlarm(Context paramContext,
			PendingIntent paramPendingIntent) {
		((AlarmManager) paramContext.getSystemService(Context.ALARM_SERVICE))
				.cancel(paramPendingIntent);
	}
}