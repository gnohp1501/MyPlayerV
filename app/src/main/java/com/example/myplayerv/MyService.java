package com.example.myplayerv;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.myplayerv.activities.VideoPlayerActivity;
import com.example.myplayerv.entities.MediaFiles;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    ArrayList<MediaFiles> mediaFiles = new ArrayList<>();
    int position;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        position = intent.getIntExtra("position", 1);
        mediaFiles = intent.getExtras().getParcelableArrayList("videoArrayList");
        //
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_notification);
        contentView.setImageViewBitmap(R.id.image, ThumbnailUtils.createVideoThumbnail(mediaFiles.get(position).getPath(),
                MediaStore.Images.Thumbnails.MINI_KIND));
        contentView.setTextViewText(R.id.title, "Video is playing :\n" + mediaFiles.get(position).getDisplayName().replace(".mp4", ""));
        //

        //
        Intent callIntent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
        callIntent.putExtra("position", position);
        callIntent.putExtra("video_title", mediaFiles.get(position).getDisplayName());
        Log.d("hi", mediaFiles.get(position).getDisplayName());
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("videoArrayList", mediaFiles);
        callIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, callIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //

        Notification notification = new NotificationCompat.Builder(this, AppConstant.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContent(contentView)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
