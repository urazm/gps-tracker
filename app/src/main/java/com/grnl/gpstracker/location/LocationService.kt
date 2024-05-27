package com.grnl.gpstracker.location

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.grnl.gpstracker.MainActivity
import com.grnl.gpstracker.R

class LocationService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("LocationService", "onStartCommand called")
        startNotification()
        isRunning = true
        return START_STICKY
    }

    override fun onCreate() {
        Log.d("LocationService", "onCreate called")
        super.onCreate()
    }

    override fun onDestroy() {
        Log.d("LocationService", "onDestroy called")
        super.onDestroy()
        isRunning = false
    }

    private fun startNotification() {
        val channelName = "Location Service"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Channel for Location Service"
            }
            val manager = getSystemService(NotificationManager::class.java) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Location Service Enabled")
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
    }


    companion object{
        const val CHANNEL_ID = "channel_1"
        var isRunning = false
        var startTime = 0L
    }
}

