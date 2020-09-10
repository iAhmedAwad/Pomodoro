package iti.awad.pomodoro.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Pair
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import iti.awad.pomodoro.R

class SplashScreenActivity : AppCompatActivity() {

    val SPLASH_TIME_OUT = 5000L;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )

       Handler().postDelayed( {
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)
    }
}