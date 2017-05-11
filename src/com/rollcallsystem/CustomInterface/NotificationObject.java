package com.rollcallsystem.CustomInterface;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

public class NotificationObject {

	private	NotificationManager Manager;
	private	Notification notification;
	
	public void Notification_0(NotificationManager manager,Context context,String Title,String Text)
	{
		this.Manager = manager;
		NotificationCompat.Builder B = new Builder(context);
		B.setSmallIcon(R.drawable.stat_notify_call_mute).setContentTitle(Title).setContentText(Text);
		notification = B.build();
	}
	
	/**
	 * 
	 * @param manager
	 * @param context
	 * @param icon
	 * @param Time
	 * @param Title
	 * @param Text
	 * @param Info
	 */
	public void Notification_1(NotificationManager manager, Context context, int icon, long Time, String Title, String Text, String Info)
	{
		this.Manager = manager;
		NotificationCompat.Builder B = new Builder(context);
		B.setSmallIcon(icon).setWhen(Time).setContentTitle(Title).setContentText(Text).setContentInfo(Info);
		notification = B.build();
	}

	/**
	 * 送出通知
	 * 
	 * @param ID
	 */
	public void send(int ID)
	{
		Manager.notify(ID, notification);
	}

	/**
	 * 關閉通知
	 * 
	 * @param ID
	 */
	public void cancel(int ID)
	{
		Manager.cancel(ID);
	}
}
