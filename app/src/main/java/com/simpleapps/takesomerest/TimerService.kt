package com.simpleapps.takesomerest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.simpleapps.takesomerest.startWorking.Companion.minute
import com.simpleapps.takesomerest.startWorking.Companion.seconds


class TimerService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // If we get killed, after returning from here, restart
        showNotif(
            this,
            "Timer Running",
            "Timer Left " + String.format("%02d:%02d", minute, seconds)
        )
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("texts", "onDestroy: ")
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        Log.d("texts", "onStart: ")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
        Log.d("texts", "onUnbind: ")
    }

    override fun stopService(name: Intent?): Boolean {
        Log.d("texts", "stopService: ")
        return super.stopService(name)
    }

    fun showNotif(context: Context, textTitle: String, textContent: String) {
        val contentIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                this,
                0,
                Intent(this, startWorking::class.java),
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this,
                0, Intent(this, startWorking::class.java), PendingIntent.FLAG_ONE_SHOT
            )
        }
        val builder = NotificationCompat.Builder(context.applicationContext, TIMER_CHANNEL)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(textTitle)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentText(textContent)
            .setContentIntent(contentIntent)
            .setSound(null)
            .setPriority(NotificationCompat.PRIORITY_LOW)
        createNotificationChannel(TIMER_CHANNEL, "Channel For Timer", context)
/*
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(TIMER_CHANNEL_ID, builder.build())
        }
*/
        startForeground(TIMER_CHANNEL_ID, builder.build())
    }

    companion object {
        const val TIMER_CHANNEL = "timer channel"
        const val TIMER_CHANNEL_ID = 1

        fun showNotif(context: Context, textTitle: String, textContent: String) {
            val contentIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, startWorking::class.java),
                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                PendingIntent.getActivity(
                    context,
                    0, Intent(context, startWorking::class.java), PendingIntent.FLAG_ONE_SHOT
                )
            }
            val builder = NotificationCompat.Builder(context.applicationContext, TIMER_CHANNEL)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(textTitle)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentText(textContent)
                .setContentIntent(contentIntent)
                .setSound(null)
                .setPriority(NotificationCompat.PRIORITY_LOW)
            createNotificationChannel(TIMER_CHANNEL, "Channel For Timer", context)
            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(TIMER_CHANNEL_ID, builder.build())
            }
        }

        fun removeNotification(context: Context) {
            NotificationManagerCompat.from(context).cancel(TIMER_CHANNEL_ID)
        }

        fun createNotificationChannel(
            channelName: String,
            channeldDescription: String,
            context: Context
        ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = channelName
                val descriptionText = channeldDescription
                val importance = NotificationManager.IMPORTANCE_LOW
                val channel = NotificationChannel(TIMER_CHANNEL, name, importance).apply {
                    description = descriptionText
                    setSound(null, null)
                }

                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }


}
