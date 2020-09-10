package iti.awad.pomodoro.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import iti.awad.pomodoro.TimerState
import iti.awad.pomodoro.utils.NotificationUtil
import iti.awad.pomodoro.utils.PrefUtils

class TimerExpiredReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtil.showTimerExpired(context)
        PrefUtils.setTimerStateId(TimerState.Stopped, context)
        PrefUtils.setAlarmSetTime(context, 0)
        //Log.d("Hello", "I am received!")
    }
}
