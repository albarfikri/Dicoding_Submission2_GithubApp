package com.example.github2.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.github2.databinding.ActivityReminderBinding
import com.example.github2.helper.AlarmRepeatingPreference
import com.example.github2.model.RepeatingAlarm
import com.example.github2.receiver.AlarmReceiver

class ReminderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReminderBinding
    private lateinit var alarmReminder: RepeatingAlarm
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val alarmPreference = AlarmRepeatingPreference(this)
        binding.switch1.isChecked = alarmPreference.getValue().reminded

        alarmReceiver = AlarmReceiver()

        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                saveReminder(true)
                alarmReceiver.setRepeatingAlarm(this, "Repeating Alarm", "09:00", "Github Reminder")
            } else {
                saveReminder(false)
                alarmReceiver.alarmCancellation(this)
            }
        }

        supportActionBar?.title = "Settings"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun saveReminder(state: Boolean) {
        val alarmPreference = AlarmRepeatingPreference(this)

        alarmReminder = RepeatingAlarm()
        alarmReminder.reminded = state

        alarmPreference.setValue(alarmReminder)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}