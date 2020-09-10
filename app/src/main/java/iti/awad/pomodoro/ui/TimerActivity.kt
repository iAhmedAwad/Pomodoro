package iti.awad.pomodoro.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import iti.awad.pomodoro.R
import iti.awad.pomodoro.TimerState
import iti.awad.pomodoro.utils.AlarmUtil
import iti.awad.pomodoro.utils.NotificationUtil
import iti.awad.pomodoro.utils.PrefUtils
import kotlinx.android.synthetic.main.activity_timer.*

class TimerActivity : AppCompatActivity() {
    private var secondsRemaining = 0L
    private lateinit var timer: CountDownTimer
    private var timerLengthInSeconds = 0L
    private var timerState = TimerState.Stopped

    //TODO: (NEW CLASS) remove these functions into a separate class
/*
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

 */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        setSupportActionBar(toolbar)
        supportActionBar?.setIcon(R.drawable.ic_timer)
        supportActionBar?.setTitle("     Timer")

        initButtons()
    }

    private fun initButtons() {

        fabPlay.setOnClickListener { v ->
            startTimer()
            timerState = TimerState.Running
            updateButtons()
        }
        fabPause.setOnClickListener { v ->

            timer.cancel()
            timerState = TimerState.Paused
            updateButtons()


        }

        fabStop.setOnClickListener { v ->
            if (timer != null) {
                timer.cancel()
                timerState = TimerState.Stopped
                onTimerFinished()
            } else {
                Toast.makeText(this, "Timer is not active!", Toast.LENGTH_SHORT)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initTimer()
        AlarmUtil.removeAlarm(this)
        NotificationUtil.hideTimerNotification(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        super.onPause()
        if (timerState == TimerState.Running) {
            timer.cancel()
            val wakeupTime = AlarmUtil.setAlarm(this, AlarmUtil.nowSeconds, secondsRemaining)
            NotificationUtil.showTimerRunning(this, wakeupTime)

        } else if (timerState == TimerState.Paused) {
            NotificationUtil.showTimerPaused(this)
        }

        PrefUtils.setPreviousTimerLengthSeconds(timerLengthInSeconds, this)
        PrefUtils.setSecondsRemaining(secondsRemaining, this)
        PrefUtils.setTimerStateId(timerState, this)
    }

    private fun initTimer() {
        timerState = PrefUtils.getTimerState(this)
        if (timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if (timerState == TimerState.Paused || timerState == TimerState.Running)
            PrefUtils.getSecondsRemaining(this)
        else timerLengthInSeconds

        val alarmSetTime = PrefUtils.getAlarmSetTime(this)
        if (alarmSetTime > 0)
            secondsRemaining -= AlarmUtil.nowSeconds - alarmSetTime

        if (secondsRemaining <= 0)
            onTimerFinished()
        else if (timerState == TimerState.Running)
            startTimer()
        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished() {
        timerState = TimerState.Stopped
        setNewTimerLength()
        progressCountdown.progress = 0
        PrefUtils.setSecondsRemaining(timerLengthInSeconds, this)
        secondsRemaining = timerLengthInSeconds
        updateButtons()
        updateCountdownUI()
    }

    private fun startTimer() {
        timerState = TimerState.Running
        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }

            override fun onFinish() = onTimerFinished()

        }.start()
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = PrefUtils.getTimerLength(this)
        timerLengthInSeconds = lengthInMinutes * 60L
        progressCountdown.max = timerLengthInSeconds.toInt()
    }

    private fun setPreviousTimerLength() {
        timerLengthInSeconds = PrefUtils.getPreviousTimerLengthSeconds(this)
        progressCountdown.max = timerLengthInSeconds.toInt()

    }

    private fun updateCountdownUI() {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsString = secondsInMinuteUntilFinished.toString()
        textViewCountdown.text = "$minutesUntilFinished:${
            if (secondsString.length == 2) secondsString else "0" + secondsString
        }"

        progressCountdown.progress = (timerLengthInSeconds - secondsRemaining).toInt()
    }

    private fun updateButtons() {

        when (timerState) {
            TimerState.Running -> {
                fabPlay.isEnabled = false
                fabStop.isEnabled = true
                fabPause.isEnabled = true
            }

            TimerState.Stopped -> {
                fabPlay.isEnabled = true
                fabStop.isEnabled = false
                fabPause.isEnabled = false
            }

            TimerState.Paused -> {
                fabPlay.isEnabled = true
                fabStop.isEnabled = true
                fabPause.isEnabled = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_timer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionSettings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}