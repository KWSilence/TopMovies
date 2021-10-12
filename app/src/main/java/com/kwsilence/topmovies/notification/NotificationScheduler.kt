package com.kwsilence.topmovies.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object NotificationScheduler {
  fun schedule(context: Context, time: Long, id: Int, title: String, text: String) {
    val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, NotificationReceiver::class.java).apply {
      putExtra("title", title)
      putExtra("text", text)
      putExtra("id", id)
    }
    val pending = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pending)
  }

  fun cancel(context: Context, id: Int) {
    val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, NotificationReceiver::class.java)
    val pending = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    manager.cancel(pending)
  }
}
