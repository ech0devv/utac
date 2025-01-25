package dev.ech0.torbox.multiplatform

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}
@Composable
actual fun PlayVideo(videoUrl: String) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val localSnackbarHostState = LocalSnackbarHostState.current
    try {
        val playVideo = Intent(Intent.ACTION_VIEW)
        playVideo.setDataAndType(
            Uri.parse(videoUrl), "video/x-unknown"
        )
        context.startActivity(playVideo)
    } catch (e: ActivityNotFoundException) {
        scope.launch {
            localSnackbarHostState.showSnackbar("No video player found :(")
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}