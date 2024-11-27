package com.yusuf.weaterapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yusuf.weaterapp.data.room.entitiy.GeoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM GeoEntity")
    fun getAllGeos(): Flow<List<GeoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGeo(geo: GeoEntity)
}