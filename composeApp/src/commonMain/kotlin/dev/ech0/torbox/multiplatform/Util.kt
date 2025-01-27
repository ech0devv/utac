package dev.ech0.torbox.multiplatform

import kotlinx.serialization.json.JsonObject
import kotlin.math.round
import kotlin.math.roundToLong

fun roundToHundredth(number: Double): Double{
    return round(number * 100.0) / 100.0
}

fun formatFileSize(bytes: Long): String {
    val kilobyte = 1024.0
    val megabyte = kilobyte * 1024
    val gigabyte = megabyte * 1024

    return when {
        bytes < kilobyte -> "$bytes B"
        bytes < megabyte -> "${roundToHundredth(bytes/kilobyte)} KB"
        bytes < gigabyte -> "${roundToHundredth(bytes/megabyte)} MB"
        else -> "${roundToHundredth(bytes/gigabyte)} GB"
    }
}