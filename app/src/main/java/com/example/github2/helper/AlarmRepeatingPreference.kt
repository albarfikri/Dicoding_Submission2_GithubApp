package com.example.github2.helper

import android.content.Context
import com.example.github2.model.User

internal class AlarmRepeatingPreference(context: Context) {
    companion object {
        const val pref_name = "alarm_pref"
        private const val isRepeatingAlarmPref = "repeatingAlarm"
    }

    private val preferences = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE)

    fun getValue(): User {
        val model = User()
        model.reminded = preferences.getBoolean(isRepeatingAlarmPref, false)
        return model
    }

    fun setValue(value: User) {
        val editor = preferences.edit()
        editor.putBoolean(isRepeatingAlarmPref, value.reminded)
        editor.apply()
    }
}