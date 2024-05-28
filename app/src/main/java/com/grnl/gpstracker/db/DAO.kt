package com.grnl.gpstracker.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {
    @Insert
    suspend fun insertLocation(location: LocationEntry)

    @Query("SELECT * FROM location_entries")
    suspend fun getAllLocations(): List<LocationEntry>
}

@Dao
interface DistanceDao {
    @Insert
    suspend fun insertDistance(distance: DistanceEntry)

    @Query("SELECT * FROM distance_entries")
    suspend fun getAllDistances(): List<DistanceEntry>
}
