package com.example.github2.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.github2.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val EXTRA_TYPE = "type"
        private const val TIME_FORMAT = "HH:mm"
        private const val EXTRA_MESSAGE = "message"
        private const val CHANNEL_ID = "channel_01"
        private const val ID_REPEATING = 101
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_NAME = "repeating_alarm"
    }

    override fun onReceive(context: Context, intent: Intent) {
        sendAlarmNotification(context)
    }

    fun setRepeatingAlarm(context: Context, type: String, time: String) {
        if (isDateInvalid(time, TIME_FORMAT)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, "GitHub Reminder")
        intent.putExtra(EXTRA_TYPE, type)

        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(context, "Repeating Alarm set up at 09.00 A.M", Toast.LENGTH_SHORT).show()
    }

    private fun sendAlarmNotification(context: Context) {
        val notifIntent = context.packageManager.getLaunchIntentForPackage("com.example.github2")

        val pendingIntent = PendingIntent.getActivity(context, 0, notifIntent, 0)

        val mNotificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.notifisalerting)
            .setContentTitle("GitHub")
            .setContentText("Let's figure out the most amazing person in github !")
            .setAutoCancel(true)
            .setSubText("world class app")
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_NAME
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            mBuilder.setChannelId(CHANNEL_ID)
            mNotificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = mBuilder.build()
        mNotificationManagerCompat.notify(NOTIFICATION_ID, notification)
    }

    fun alarmCancellation(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = ID_REPEATING
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "Repeating Alarm stopped", Toast.LENGTH_SHORT).show()
    }

    private fun isDateInvalid(time: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(time)
            false
        } catch (e: ParseException) {
            true
        }
    }
}