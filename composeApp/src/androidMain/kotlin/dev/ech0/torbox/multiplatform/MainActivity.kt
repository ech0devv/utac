package dev.ech0.torbox.multiplatform

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.russhwolf.settings.Settings
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
@Composable
actual fun GetDynamicScheme(): ColorScheme? {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
        return when {
            Settings().getBoolean("dark", true) -> dynamicDarkColorScheme(LocalContext.current)
            !Settings().getBoolean("dark", true) -> dynamicLightColorScheme(LocalContext.current)
            else -> null
        }
    }
    return null
}