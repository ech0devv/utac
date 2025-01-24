package dev.ech0.torbox.multiplatform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform