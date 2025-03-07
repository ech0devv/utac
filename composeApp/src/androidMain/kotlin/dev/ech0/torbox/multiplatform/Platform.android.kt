package dev.ech0.torbox.multiplatform

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android"
}

actual fun getPlatform(): Platform = AndroidPlatform()