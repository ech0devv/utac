package dev.ech0.torbox.multiplatform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.coroutines.launch
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@Composable
actual fun PlayVideo(videoUrl: String) {
    val nsUrl = NSURL.URLWithString(videoUrl)
    val snackbarHostState = LocalSnackbarHostState.current
    val scope = rememberCoroutineScope()
    if(nsUrl != null) {
        UIApplication.sharedApplication.openURL(nsUrl)
    }else{
        scope.launch {
            snackbarHostState.showSnackbar("Something went wrong")
        }
    }
}

fun MainViewController() = ComposeUIViewController { App() }