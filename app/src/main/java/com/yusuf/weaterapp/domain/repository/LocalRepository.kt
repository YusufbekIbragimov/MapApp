package com.yusuf.weaterapp.domain.repository

import com.yusuf.weaterapp.data.room.entitiy.GeoEntity
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun saveGeo(geo: GeoEntity)
    suspend fun getAllGeos(): Flow<List<GeoEntity>>
}