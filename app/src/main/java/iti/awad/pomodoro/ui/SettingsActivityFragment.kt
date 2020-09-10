package iti.awad.pomodoro.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import iti.awad.pomodoro.R

class SettingsActivityFragment : PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref)
    }
}