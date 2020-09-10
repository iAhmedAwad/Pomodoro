package iti.awad.pomodoro.utils

import android.content.Context
import iti.awad.pomodoro.TimerState as TimerState

class PrefUtils {

    companion object {
        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "previous_timer_length_seconds_id"
        private const val SECONDS_REMAINING_ID = "seconds_remaining_id"
        private const val SHARED_PREFERENCES_FILE_KEY = "shared_preferences_file_key"
        private const val TIMER_STATE_ID = "timer_state_id"
        private const val ALARM_SET_ID = "alarm_set_id"
        private const val TIMER_LENGTH_ID = "com.key.time.timerLength"

        fun getTimerLength(context : Context) : Int{
            val preferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)

            return preferences.getInt(TIMER_LENGTH_ID,25)
        }

        fun getPreviousTimerLengthSeconds (context: Context): Long{
            val preferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)

            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID,0)

        }

        fun setPreviousTimerLengthSeconds(seconds: Long, context: Context){

            val preferences =  context.getSharedPreferences(
                SHARED_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)

            val editor = preferences.edit()

            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds).apply()
        }

        fun getTimerState(context : Context): TimerState {

            val preferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)

            val timerStateId = preferences.getInt(TIMER_STATE_ID, 0)

            return TimerState.values()[timerStateId]

        }

        fun setTimerStateId(state: TimerState, context: Context){

            val preferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)

            val editor = preferences.edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal).apply()
        }

        fun getSecondsRemaining (context: Context): Long{
            val preferences = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)

            return preferences.getLong(SECONDS_REMAINING_ID,0)

        }

        fun setSecondsRemaining(seconds: Long, context: Context){

            val preferences =  context.getSharedPreferences(
                SHARED_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)

            val editor = preferences.edit()

            editor.putLong(SECONDS_REMAINING_ID, seconds).apply()
        }

        fun getAlarmSetTime(context :Context): Long{
            val preferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
           return preferences.getLong(ALARM_SET_ID, 0)
        }

        fun setAlarmSetTime(context: Context, time: Long){
            val preferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putLong(ALARM_SET_ID, time).apply()
        }
    }
}