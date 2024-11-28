package com.yusuf.weaterapp.data.network.repository

import com.yusuf.weaterapp.data.room.dao.WeatherDao
import com.yusuf.weaterapp.data.room.entitiy.GeoEntity
import com.yusuf.weaterapp.domain.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val dao: WeatherDao
) : LocalRepository {
    override suspend fun saveGeo(geo: GeoEntity) {
        dao.saveGeo(geo)
    }

    override suspend fun deleteGeo(geo: GeoEntity) {
        dao.deleteGeo(geo.id)
    }

    override suspend fun getAllGeos(): Flow<List<GeoEntity>> {
        return dao.getAllGeos()
    }
}