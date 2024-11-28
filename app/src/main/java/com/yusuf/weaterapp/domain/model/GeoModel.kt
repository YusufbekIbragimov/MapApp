package com.yusuf.weaterapp.domain.model

import com.yusuf.weaterapp.data.room.entitiy.GeoEntity

data class GeoModel(
    val id: Int = 0,
    val name: String,
    val address: String,
    val lat: Double,
    val lon: Double
)

fun GeoModel.toEntity() = GeoEntity(
    id = id,
    name = name,
    address = address,
    lat = lat,
    lon = lon
)