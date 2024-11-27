package com.yusuf.weaterapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yusuf.weaterapp.data.room.dao.WeatherDao
import com.yusuf.weaterapp.data.room.entitiy.GeoEntity

@Database(entities = [GeoEntity::class], version = 6, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): WeatherDao
}