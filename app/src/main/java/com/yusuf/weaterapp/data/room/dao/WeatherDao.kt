package com.yusuf.weaterapp.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
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

    @Query("DELETE FROM GeoEntity WHERE id = :id")
    suspend fun deleteGeo(id: Int)
}