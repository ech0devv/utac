package dev.ech0.torbox.multiplatform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.launch
import java.awt.Desktop

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "UTAC",
    ) {
        App()
    }
}
@Composable
actual fun PlayVideo(videoUrl: String) {
    val desktop = Desktop.getDesktop()
    val snackbarHostState = LocalSnackbarHostState.current
    val scope = rememberCoroutineScope()
    if(desktop.isSupported(Desktop.Action.BROWSE)) {
        desktop.browse(java.net.URI.create(videoUrl))
    }else{
        scope.launch{
            snackbarHostState.showSnackbar("No video player found :(")
        }
    }
}