package dev.ech0.torbox.multiplatform

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.russhwolf.settings.Settings
import dev.ech0.torbox.multiplatform.api.*
import dev.ech0.torbox.multiplatform.ui.components.DisplayError
import dev.ech0.torbox.multiplatform.ui.components.NavBar
import dev.ech0.torbox.multiplatform.ui.components.TopBar
import dev.ech0.torbox.multiplatform.ui.pages.DownloadsPage
import dev.ech0.torbox.multiplatform.ui.pages.SearchPage
import dev.ech0.torbox.multiplatform.ui.pages.SettingsPage
import dev.ech0.torbox.multiplatform.ui.pages.watch.WatchSearchPage
import org.jetbrains.compose.ui.tooling.preview.Preview

val LocalNavController = compositionLocalOf<NavController> { error("No NavController found!") }
val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> { error("No SnackbarHostState found!") }

@Composable
expect fun PlayVideo(videoUrl: String)

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }
        CompositionLocalProvider(
            LocalNavController provides navController, LocalSnackbarHostState provides snackbarHostState
        ) {
            Scaffold(
                topBar = { TopBar() },
                bottomBar = { NavBar(navController) },
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                modifier = Modifier.imePadding()
            ) { paddingValues ->
                Navigation(navController, paddingValues) {}
                tmdbApi = TMDBApi()
                torboxAPI = TorboxAPI(Settings().getString("apiKey", "__"), navController)
                traktApi = Trakt()
            }

        }
    }
}

@Composable
fun Navigation(
    navController: NavHostController, paddingValues: PaddingValues, setColorScheme: (ColorScheme?) -> Unit
) {
    NavHost(
        navController = navController, startDestination = "Downloads", modifier = Modifier.padding(paddingValues)
    ) {
        composable("Downloads") {
            DownloadsPage()
        }
        composable("Watch") {
            WatchSearchPage()
        }
        composable("Search") {
            SearchPage()
        }
        composable("Settings") {
            SettingsPage(setColorScheme)
        }
        composable("Error/{what}") { backStackEntry ->
            val what = backStackEntry.arguments?.getString("what") ?: ""
            DisplayError(false, what)
        }
    }

}
