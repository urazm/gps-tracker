package com.grnl.gpstracker.db
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_entries")
data class LocationEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val timestamp: String
)

@Entity(tableName = "distance_entries")
data class DistanceEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val distance: Float,
    val timestamp: String
)

