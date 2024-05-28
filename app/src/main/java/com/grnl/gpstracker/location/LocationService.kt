package com.grnl.gpstracker.location

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.grnl.gpstracker.MainActivity
import com.grnl.gpstracker.R
import com.grnl.gpstracker.db.AppDatabase
import com.grnl.gpstracker.db.DistanceEntry
import com.grnl.gpstracker.db.LocationEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LocationService : Service() {
    private var distance = 0.0f
    private lateinit var locProvider: FusedLocationProviderClient
    private lateinit var locRequest: LocationRequest
    private var lastLocation: Location? = null
    private lateinit var database: AppDatabase

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("LocationService", "onStartCommand called")
        startNotification()
        startLocationUpdates()
        isRunning = true
        return START_STICKY
    }

    override fun onCreate() {
        Log.d("LocationService", "onCreate called")
        super.onCreate()
        initLocation()
        database = AppDatabase.getDatabase(this)
    }

    override fun onDestroy() {
        Log.d("LocationService", "onDestroy called")
        super.onDestroy()
        isRunning = false
        locProvider.removeLocationUpdates(locCallBack)
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

    private val locCallBack = object : LocationCallback() {
        override fun onLocationResult(lResult: LocationResult) {
            super.onLocationResult(lResult)
            val currLocation = lResult.lastLocation
            if (currLocation != null && lastLocation != null) {
                val distanceTo = lastLocation!!.distanceTo(currLocation)
                distance += distanceTo
                saveLocation(currLocation)
            }
            lastLocation = currLocation
            saveDistance(distance)
            Log.d("distance", "distance: $distance")
        }
    }

    private fun initLocation() {
        locRequest = LocationRequest.Builder(5000).setPriority(PRIORITY_HIGH_ACCURACY).build()
        locProvider = LocationServices.getFusedLocationProviderClient(baseContext)
    }


    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locProvider.requestLocationUpdates(
            locRequest,
            locCallBack,
            Looper.myLooper()
        )
    }

    private fun saveLocation(location: Location) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault()).format(Date())
        val locationEntry = LocationEntry(latitude = location.latitude, longitude = location.longitude, timestamp = timestamp)
        CoroutineScope(Dispatchers.IO).launch {
            database.locationDao().insertLocation(locationEntry)
            Log.d("LocationService", "Location saved: $locationEntry")
        }
    }

    private fun saveDistance(distance: Float) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault()).format(Date())
        val distanceEntry = DistanceEntry(distance = distance, timestamp = timestamp)
        CoroutineScope(Dispatchers.IO).launch {
            database.distanceDao().insertDistance(distanceEntry)
            Log.d("LocationService", "Distance saved: $distanceEntry")

        }
    }
    companion object{
        const val CHANNEL_ID = "channel_1"
        var isRunning = false
        var startTime = 0L
    }
}

