package com.piggeh.palmettoscholars.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.piggeh.palmettoscholars.R;

/**
 * Created by peter on 9/7/2016.
 */
public class PSANotifications {
    private static final String TAG = "PSANotifications";

    //school announcement notification
    public static final int NOTIFICATION_ID_ANNOUNCEMENT = 1;
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
        //notification icon
        notif.setSmallIcon(R.drawable.notification_icon);
        //title and text
        notif.setContentTitle(context.getString(R.string.notif_announcement_title));
        notif.setContentText(announcement);
        //priority
        notif.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        //set notification color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            notif.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }
        //notification click action
        notif.setContentIntent(contentIntent);
        notif.setAutoCancel(true);
        //'Options' action
        notif.addAction(R.drawable.ic_notifications_off, context.getString(R.string.notif_action_options), settingsIntent);
        //vibration, sound & lights
        notif.setDefaults(NotificationCompat.DEFAULT_VIBRATE|NotificationCompat.DEFAULT_SOUND);
        notif.setLights(
                ContextCompat.getColor(context, R.color.colorPrimary),
                resources.getInteger(systemResources
                        .getIdentifier("config_defaultNotificationLedOn", "integer", "android")),
                resources.getInteger(systemResources
                        .getIdentifier("config_defaultNotificationLedOff", "integer", "android")));

        return notif.build();
    }

    //newsletter notifications
    public static final int NOTIFICATION_ID_NEWSLETTER = 2;
    public static Notification generateNewsletter(Context context, String title, PendingIntent contentIntent, PendingIntent openExternallyIntent, PendingIntent settingsIntent){
        //resources
        Resources resources = context.getResources();
        Resources systemResources = Resources.getSystem();

        //newsletter notification
        NotificationCompat.Builder notif = new NotificationCompat.Builder(context);
        //notification icon
        notif.setSmallIcon(R.drawable.ic_newsletter);
        //title & text
        notif.setContentTitle(context.getString(R.string.notif_newsletter_title));
        notif.setContentText(title);
        //priority
        notif.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        //set notification color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            notif.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }
        //notification click action
        notif.setContentIntent(contentIntent);
        notif.setAutoCancel(true);
        //'Open in browser' action
        notif.addAction(R.drawable.ic_open_externally, context.getString(R.string.notif_newsletter_action_open_externally), openExternallyIntent);
        //'Options' action
        notif.addAction(R.drawable.ic_notifications_off, context.getString(R.string.notif_action_options), settingsIntent);
        //vibration, sound & lights
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
