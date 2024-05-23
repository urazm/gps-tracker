package com.grnl.gpstracker.location

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class LocationService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onCreate() {
        Log.d("LOGLOG", "OnCreate")
        super.onCreate()
    }

    override fun onDestroy() {
        Log.d("LOGLOG", "OnDestroy")
        super.onDestroy()
    }
}