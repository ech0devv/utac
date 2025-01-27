package dev.ech0.torbox.multiplatform

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = "iOS"
}

actual fun getPlatform(): Platform = IOSPlatform()