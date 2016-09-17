/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.piggeh.palmettoscholars.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.piggeh.palmettoscholars.R;
import com.piggeh.palmettoscholars.activities.MainActivity;
import com.piggeh.palmettoscholars.utils.PSANotifications;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (remoteMessage.getData().get("title").equals("New PSA announcement")){
                Log.d(TAG, "Message is for an Announcement");

                notifyAnnouncement(remoteMessage.getData().get("message"));

                /*Intent settingsIntent = new Intent(this, MainActivity.class);
                settingsIntent.putExtra("navigation_page", MainActivity.PAGE_SETTINGS);
                PendingIntent settingsPendingIntent =
                        PendingIntent.getActivity(
                                this,
                                0,
                                settingsIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                //notification content intent
                Intent contentIntent = new Intent(this, MainActivity.class);
                //settingsIntent.putExtra("navigation_page", PAGE_SETTINGS);
                PendingIntent contentPendingIntent =
                        PendingIntent.getActivity(
                                this,
                                1,
                                contentIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                //notification
                Notification announcementNotif = PSANotifications.generateAnnouncement(this,
                        remoteMessage.getData().get("message"),
                        contentPendingIntent,
                        settingsPendingIntent);
                //notification manager
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(PSANotifications.NOTIFICATION_ID_ANNOUNCEMENT,
                        announcementNotif);*/
            }
        } else{
            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

                if (remoteMessage.getNotification().getTitle().equals("New PSA announcement")){
                    Log.d(TAG, "Message Notification is for an Announcement");

                    notifyAnnouncement(remoteMessage.getNotification().getBody());
                /*Intent settingsIntent = new Intent(this, MainActivity.class);
                settingsIntent.putExtra("navigation_page", MainActivity.PAGE_SETTINGS);
                PendingIntent settingsPendingIntent =
                        PendingIntent.getActivity(
                                this,
                                0,
                                settingsIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                //notification content intent
                Intent contentIntent = new Intent(this, MainActivity.class);
                //settingsIntent.putExtra("navigation_page", PAGE_SETTINGS);
                PendingIntent contentPendingIntent =
                        PendingIntent.getActivity(
                                this,
                                1,
                                contentIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                //notification
                Notification announcementNotif = PSANotifications.generateAnnouncement(this,
                        remoteMessage.getNotification().getBody(),
                        contentPendingIntent,
                        settingsPendingIntent);
                //notification manager
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(PSANotifications.NOTIFICATION_ID_ANNOUNCEMENT,
                        announcementNotif);*/
                }
            }
        }



        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    //school announcement notification
    public static final int NOTIFICATION_ID_ANNOUNCEMENT = 1;
    public void notifyAnnouncement(String announcement) {
        Intent contentIntent = new Intent(this, MainActivity.class);
        contentIntent.putExtra("navigation_page", MainActivity.PAGE_ANNOUNCEMENTS);
        contentIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, contentIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Intent settingsIntent = new Intent(this, MainActivity.class);
        settingsIntent.putExtra("navigation_page", MainActivity.PAGE_SETTINGS);
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent settingsPendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, settingsIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(getString(R.string.notif_announcement_title))
                .setContentText(announcement)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setContentIntent(contentPendingIntent)
                .addAction(R.drawable.ic_notifications_off,
                        getString(R.string.notif_action_options), settingsPendingIntent);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        // Build the notification and issues it with notification manager.
        notificationManager.notify(NOTIFICATION_ID_ANNOUNCEMENT, notificationBuilder.build());
    }

    /*private Notification generateAnnouncement(String announcement, PendingIntent contentIntent, PendingIntent settingsIntent){
        //resources
        Resources resources = getResources();
        Resources systemResources = Resources.getSystem();

        //announcement notification
        NotificationCompat.Builder notif = new NotificationCompat.Builder(this);
        //notification icon
        notif.setSmallIcon(R.drawable.notification_icon_nodpi);
        //title and text
        notif.setContentTitle(getString(R.string.notif_announcement_title));
        notif.setContentText(announcement);
        //priority
        notif.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        //set notification color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            notif.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        //notification click action
        notif.setContentIntent(contentIntent);
        notif.setAutoCancel(true);
        //'Options' action
        notif.addAction(R.drawable.ic_notifications_off, getString(R.string.notif_action_options), settingsIntent);
        //vibration, sound & lights
        notif.setDefaults(NotificationCompat.DEFAULT_VIBRATE|NotificationCompat.DEFAULT_SOUND);
        notif.setLights(
                ContextCompat.getColor(this, R.color.colorPrimary),
                resources.getInteger(systemResources
                        .getIdentifier("config_defaultNotificationLedOn", "integer", "android")),
                resources.getInteger(systemResources
                        .getIdentifier("config_defaultNotificationLedOff", "integer", "android")));

        return notif.build();
    }*/

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notifications_on)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}