package com.sarmaru.mihai.jetpackfundamentals.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificationsHelper {

    private static final String CHANNEL_ID = "Dog Channel";
    public static final int NOTIFICATION_ID = 1;

    private static NotificationsHelper instance;
    private Context context;

    private NotificationsHelper(Context context) {
        this.context = context;
    }

    public static NotificationsHelper getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationsHelper(context);
        }
        return instance;
    }

    private void createNotificationChannel() {
        // Check if Android version is grater than O
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = CHANNEL_ID;
            String channelDescription = "Dogs retrieved notifications channel";
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, channelImportance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
