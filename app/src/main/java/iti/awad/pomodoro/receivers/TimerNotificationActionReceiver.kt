package iti.awad.pomodoro.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import iti.awad.pomodoro.AppConstants
import iti.awad.pomodoro.TimerState
import iti.awad.pomodoro.utils.AlarmUtil
import iti.awad.pomodoro.utils.NotificationUtil
import iti.awad.pomodoro.utils.PrefUtils

class TimerNotificationActionReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AppConstants.ACTION_STOP -> {
                AlarmUtil.removeAlarm(context)
                PrefUtils.setTimerStateId(TimerState.Stopped, context)
                NotificationUtil.hideTimerNotification(context)
            }

            AppConstants.ACTION_PAUSE -> {
                var secondsRemaining = PrefUtils.getSecondsRemaining(context)
                val alarmSetTime = PrefUtils.getAlarmSetTime(context)
                val nowSeconds = AlarmUtil.nowSeconds
                secondsRemaining -= nowSeconds - alarmSetTime
                PrefUtils.setSecondsRemaining(secondsRemaining, context)
                AlarmUtil.removeAlarm(context)
                PrefUtils.setTimerStateId(TimerState.Paused, context)
                NotificationUtil.showTimerPaused(context)
            }

            AppConstants.ACTION_RESUME -> {
                val secondsRemaining = PrefUtils.getSecondsRemaining(context)
                val wakeupTime = AlarmUtil.setAlarm(context, AlarmUtil.nowSeconds, secondsRemaining)
                PrefUtils.setTimerStateId(TimerState.Running, context)
                NotificationUtil.showTimerRunning(context, wakeupTime)

            }

            AppConstants.ACTION_START -> {
                val minutesRemaining = PrefUtils.getTimerLength(context)
                val secondsRemaining = minutesRemaining * 60L
                val wakeupTime = AlarmUtil.setAlarm(context, AlarmUtil.nowSeconds, secondsRemaining)
                PrefUtils.setTimerStateId(TimerState.Running, context)
                PrefUtils.setSecondsRemaining(secondsRemaining, context)
                NotificationUtil.showTimerRunning(context, wakeupTime)
            }
        }
    }
}
