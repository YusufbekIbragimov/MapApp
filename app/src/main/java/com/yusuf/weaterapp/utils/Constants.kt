package com.yusuf.weaterapp.utils

import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition

class Constants {
    companion object {
        const val BASE_URL =
            "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
        const val LOCATION = "Tashkent,UZ"

        /**
         *  Usually, we have to save apiKeys in gradle.properties file
         *  and you have to add gradle.properties file to .gitignore
         *  I will do that for github version
         */
        const val API_KEY = "YJDDLDXYRX5Z6RD2KTEKVRCNM"
        val tashkentCameraPosition = CameraPosition(
            Point(41.3110814, 69.2405626),
            12f,
            1.0f,
            0.0f
        )
    }
}