package iti.awad.pomodoro.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import iti.awad.pomodoro.AppConstants
import iti.awad.pomodoro.R
import iti.awad.pomodoro.receivers.TimerNotificationActionReceiver
import iti.awad.pomodoro.ui.TimerActivity
import java.text.SimpleDateFormat
import java.util.*

class NotificationUtil {
    companion object {
        private const val CHANNEL_ID_TIMER = "menu_timer"
        private const val CHANNEL_NAME_TIMER = "timer_app_timer"
        val TIMER_EXPIRED_REQUEST_CODE = 65749
        private const val TIMER_ID = 45687

        @RequiresApi(Build.VERSION_CODES.O)
        fun showTimerExpired(context: Context) {
            val startIntent = Intent(context, TimerNotificationActionReceiver::class.java)
            startIntent.action = AppConstants.ACTION_START
            val startPendingIntent = PendingIntent.getBroadcast(
                context,
                TIMER_EXPIRED_REQUEST_CODE,
                startIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val notificationBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true)
            notificationBuilder?.setContentTitle("Timer Expired!")
                ?.setContentText("Start again?")
                ?.setContentIntent(getPendingIntentWithStack(context, TimerActivity::class.java))
                ?.addAction(R.drawable.ic_play, "Start", startPendingIntent)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(
                CHANNEL_ID_TIMER,
                CHANNEL_NAME_TIMER,
                true
            )
            notificationManager.notify(TIMER_ID, notificationBuilder?.build())
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun showTimerRunning(context: Context, wakeupTime: Long) {
            val stopIntent = Intent(context, TimerNotificationActionReceiver::class.java)
            stopIntent.action = AppConstants.ACTION_STOP
            val stopPendingIntent = PendingIntent.getBroadcast(
                context,
                TIMER_EXPIRED_REQUEST_CODE,
                stopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val pauseIntent = Intent(context, TimerNotificationActionReceiver::class.java)
            pauseIntent.action = AppConstants.ACTION_PAUSE
            val pausePendingIntent = PendingIntent.getBroadcast(
                context,
                TIMER_EXPIRED_REQUEST_CODE,
                pauseIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val dateFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)

            val notificationBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true)
            notificationBuilder?.setContentTitle("Timer is running!")
                ?.setContentText("End: ${dateFormat.format(Date(wakeupTime))}")
                ?.setContentIntent(getPendingIntentWithStack(context, TimerActivity::class.java))
                ?.setOngoing(true)
                ?.addAction(R.drawable.ic_pause, "Pause", pausePendingIntent)
                ?.addAction(R.drawable.ic_stop, "Stop", stopPendingIntent)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(
                CHANNEL_ID_TIMER,
                CHANNEL_NAME_TIMER,
                true
            )
            notificationManager.notify(TIMER_ID, notificationBuilder?.build())
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun showTimerPaused(context: Context) {
            val resumeIntent = Intent(context, TimerNotificationActionReceiver::class.java)
            resumeIntent.action = AppConstants.ACTION_RESUME
            val resumePendingIntent = PendingIntent.getBroadcast(
                context,
                TIMER_EXPIRED_REQUEST_CODE,
                resumeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val notificationBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true)
            notificationBuilder?.setContentTitle("Timer is paused!")
                ?.setContentText("Resume?")
                ?.setOngoing(true)
                ?.setContentIntent(getPendingIntentWithStack(context, TimerActivity::class.java))
                ?.addAction(R.drawable.ic_play, "Resume", resumePendingIntent)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(
                CHANNEL_ID_TIMER,
                CHANNEL_NAME_TIMER,
                true
            )
            notificationManager.notify(TIMER_ID, notificationBuilder?.build())
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun getBasicNotificationBuilder(
            context: Context,
            channelId: String,
            playSound: Boolean
        ): Notification.Builder? {
            val notificationSound: Uri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val notificationBuilder = Notification.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_timer)
                .setAutoCancel(true)
                .setDefaults(0)

            if (playSound) notificationBuilder.setSound(notificationSound)

            return notificationBuilder
        }

        private fun <T> getPendingIntentWithStack(
            context: Context,
            javaClass: Class<T>
        ): PendingIntent {
            val resultIntent = Intent(context, javaClass)
            resultIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addParentStack(javaClass)
            stackBuilder.addNextIntent(resultIntent)
            return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

    fun NotificationManager.createNotificationChannel(
        channelId: String,
        channelName: String,
        playSound: Boolean
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelImportance = if (playSound) NotificationManager.IMPORTANCE_DEFAULT
            else NotificationManager.IMPORTANCE_LOW

            val notificationChannel = NotificationChannel(channelId, channelName, channelImportance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            this.createNotificationChannel(notificationChannel)
        }
    }

        fun hideTimerNotification(context: Context){
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.cancel(TIMER_ID)
        }

    }
}