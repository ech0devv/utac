package dev.ech0.torbox.multiplatform

import kotlinx.serialization.json.JsonObject

fun formatFileSize(bytes: Long): String {
    val kilobyte = 1024.0
    val megabyte = kilobyte * 1024
    val gigabyte = megabyte * 1024

    return when {
        bytes < kilobyte -> "$bytes B"
        bytes < megabyte -> "${bytes/kilobyte} KB"
        bytes < gigabyte -> "${bytes/megabyte} MB"
        else -> "${bytes/gigabyte} GB"
    }
}