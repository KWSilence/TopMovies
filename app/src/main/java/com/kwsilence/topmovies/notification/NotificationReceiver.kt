package com.kwsilence.topmovies.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.kwsilence.topmovies.R

class NotificationReceiver : BroadcastReceiver() {
  private val channel = "TopMovieChannel"

  override fun onReceive(context: Context?, intent: Intent?) {
    context ?: return
    intent ?: return
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      val importance = NotificationManager.IMPORTANCE_HIGH
      val notificationChannel =
        NotificationChannel(channel, channel, importance)
      manager.createNotificationChannel(notificationChannel)
    }
    val notification = NotificationCompat.Builder(context, channel).apply {
      setAutoCancel(true)
      setSmallIcon(R.drawable.ic_notification)
      setContentTitle(intent.getStringExtra("title"))
      setContentText(intent.getStringExtra("text"))
      setChannelId(channel)
    }.build()
    manager.notify(intent.getIntExtra("id", 0), notification)
  }
}
