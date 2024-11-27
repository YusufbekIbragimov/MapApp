package com.yusuf.weaterapp.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Date.toFormattedStringDay(format: String = "yyyy-MM-dd"): String {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(this)
}

fun Date.roundToHours(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this

    // Set minutes and seconds to 0
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.time.toFormattedStringHour()
}

fun Date.toFormattedStringHour(format: String = "HH:mm:ss"): String {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(this)
}

fun Double.toCelsiusString(decimalPlaces: Int = 2): String {
    val roundedCelsius = (this - 32) * 5 / 9 // Fahrenheit to Celsius conversion
    val decimalFormat = DecimalFormat("0.${"#".repeat(decimalPlaces)}") // Customizable decimal formatting
    return "${decimalFormat.format(roundedCelsius)}°C" // Temperature formatting with degree symbol
}