package dev.ech0.torbox.multiplatform

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import dev.ech0.torbox.multiplatform.theme.AppTheme
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
    var colorScheme by remember {
        mutableStateOf(Settings().getString("theme", "Torbox"))
    }
    var darkMode by remember {
        mutableStateOf(Settings().getBoolean("dark", true))
    }
    AppTheme(themeName = colorScheme, darkTheme = darkMode) {
        var showContent by remember { mutableStateOf(false) }
        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)){
            CompositionLocalProvider(
                LocalNavController provides navController, LocalSnackbarHostState provides snackbarHostState
            ) {
                Scaffold(
                    topBar = { TopBar() },
                    bottomBar = { NavBar(navController) },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { paddingValues ->
                    Navigation(navController, paddingValues, { colorScheme = it }, { darkMode = it })
                    tmdbApi = TMDBApi()
                    torboxAPI = TorboxAPI(Settings().getString("apiKey", "__"), navController)
                    traktApi = Trakt()
                }

            }
        }
    }
}

@Composable
fun Navigation(
    navController: NavHostController, paddingValues: PaddingValues, setColorScheme: (String) -> Unit, setDarkTheme: (Boolean) -> Unit
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
            SettingsPage(setColorScheme, setDarkTheme)
        }
        composable("Error/{what}") { backStackEntry ->
            val what = backStackEntry.arguments?.getString("what") ?: ""
            DisplayError(false, what)
        }
    }

}
