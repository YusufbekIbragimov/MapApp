package com.yusuf.weaterapp.data.room.entitiy

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yusuf.weaterapp.domain.model.GeoModel

@Entity
data class GeoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val address: String,
    val lat: Double,
    val lon: Double
)

fun GeoEntity.toDomain(): GeoModel {
    return GeoModel(id, name, address, lat, lon)
}