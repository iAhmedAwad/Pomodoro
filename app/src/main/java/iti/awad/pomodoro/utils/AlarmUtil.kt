package iti.awad.pomodoro.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import iti.awad.pomodoro.receivers.TimerExpiredReceiver
import java.util.*

class AlarmUtil {

    companion object {
        val nowSeconds
            get() = Calendar.getInstance().timeInMillis / 1000
        private const val ALARM_INTENT_RQUEST_CODE = 2758
        fun setAlarm(context: Context, nowSeconds: Long, remainingSeconds: Long): Long {
            val wakeupTime = (nowSeconds + remainingSeconds) * 1000
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                ALARM_INTENT_RQUEST_CODE, intent, 0
            )
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeupTime, pendingIntent)
            PrefUtils.setAlarmSetTime(context, nowSeconds)
            return wakeupTime
        }

        fun removeAlarm(context: Context) {
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                ALARM_INTENT_RQUEST_CODE, intent, 0
            )
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtils.setAlarmSetTime(context, 0)
        }
    }

}