package iti.awad.pomodoro.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import iti.awad.pomodoro.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Settings"
    }
}