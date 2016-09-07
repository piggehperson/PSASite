package com.piggeh.palmettoscholars.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.piggeh.palmettoscholars.R;

/**
 * Created by peter on 9/7/2016.
 */
public class PSANotifications {
    private static final String TAG = "PSANotifications";

    public static Notification generateAnnouncement(Context context, String announcement, PendingIntent contentIntent, PendingIntent settingsIntent){
        //resources
        Resources resources = context.getResources();
        Resources systemResources = Resources.getSystem();

        //notification settings intent
        /*Intent settingsIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent settingsPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        settingsIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );*/
        //announcement notification
        NotificationCompat.Builder notif = new NotificationCompat.Builder(context);
        notif.setSmallIcon(R.drawable.notification_icon);
        notif.setContentTitle(announcement);
        notif.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notif.setContentText(context.getString(R.string.notif_announcement_text));
        notif.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        notif.addAction(R.drawable.ic_notifications_off, context.getString(R.string.notif_action_options), settingsIntent);
        notif.setAutoCancel(true);
        notif.setContentIntent(contentIntent);
        notif.setDefaults(NotificationCompat.DEFAULT_VIBRATE|NotificationCompat.DEFAULT_SOUND);
        notif.setLights(
                ContextCompat.getColor(context, R.color.colorPrimary),
                resources.getInteger(systemResources
                        .getIdentifier("config_defaultNotificationLedOn", "integer", "android")),
                resources.getInteger(systemResources
                        .getIdentifier("config_defaultNotificationLedOff", "integer", "android")));

        return notif.build();
    }

    public static Notification generateNewsletter(Context context, String title, PendingIntent contentIntent, PendingIntent settingsIntent){
        //resources
        Resources resources = context.getResources();
        Resources systemResources = Resources.getSystem();

        NotificationCompat.Builder notif = new NotificationCompat.Builder(context);
        notif.setSmallIcon(R.drawable.ic_newsletter);
        notif.setContentTitle(context.getString(R.string.notif_newsletter_title));
        notif.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notif.setContentText("Title of newsletter");
        notif.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        notif.addAction(R.drawable.ic_notifications_off, context.getString(R.string.notif_action_options), settingsIntent);
        notif.setAutoCancel(true);
        notif.setContentIntent(contentIntent);
        notif.setDefaults(NotificationCompat.DEFAULT_VIBRATE|NotificationCompat.DEFAULT_SOUND);
        notif.setLights(
                ContextCompat.getColor(context, R.color.colorPrimary),
                resources.getInteger(systemResources
                        .getIdentifier("config_defaultNotificationLedOn", "integer", "android")),
                resources.getInteger(systemResources
                        .getIdentifier("config_defaultNotificationLedOff", "integer", "android")));

        return notif.build();
    }
}
