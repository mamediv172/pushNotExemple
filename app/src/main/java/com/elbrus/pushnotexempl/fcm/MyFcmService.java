package com.elbrus.pushnotexempl.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.elbrus.pushnotexempl.MainActivity;
import com.elbrus.pushnotexempl.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFcmService extends FirebaseMessagingService {

    private String title;
    private String message;
    private String imageUrl;
    private String type;
    private int notificationId = 0;

    private Map<String, String> data;


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        OnMessageExecute(remoteMessage);
    }


    private void OnMessageExecute(RemoteMessage remoteMessage){
        data = remoteMessage.getData();

        title = data.get("title");

        if (remoteMessage.getData().containsKey("content")
                && remoteMessage.getData().get("content") != null){
            message = data.get("content");
        }

        if (remoteMessage.getData().containsKey("id")
        && remoteMessage.getData().get("id") != null){
            notificationId = Integer.valueOf(data.get("id"));
        }



        Intent intent = new Intent(this, MainActivity.class);
        showNotification(intent);
    }



    private void showNotification(Intent intent){
        String channelId = "Default";
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(
                this,
                channelId)
                .setSmallIcon(R.drawable.ic_notifications_paused)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);


        Notification notification = notBuilder.build();

        notification.tickerText = title;

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId, notBuilder.build());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

    }
}
