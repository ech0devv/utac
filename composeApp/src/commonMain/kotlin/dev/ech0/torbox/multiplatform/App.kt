package dev.ech0.torbox.multiplatform

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.*
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
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

val LocalNavController = compositionLocalOf<NavController> { error("No NavController found!") }

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val navController = rememberNavController()
        CompositionLocalProvider(LocalNavController provides navController) {
            Scaffold(topBar = {TopBar()}, bottomBar = { NavBar(navController) }, modifier = Modifier.imePadding()){ paddingValues ->
                Navigation(navController, paddingValues){}
                tmdbApi = TMDBApi()
                torboxAPI = TorboxAPI(Settings().getString("apiKey", "__"), navController)
                traktApi = Trakt()
            }
        }
    }
}

@Composable
fun Navigation(
    navController: NavHostController,
    paddingValues: PaddingValues,
    setColorScheme: (ColorScheme?) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "Downloads",
        modifier = Modifier.padding(paddingValues)
    ) {
        composable("Downloads"){
            DownloadsPage()
        }
        composable("Watch"){
            WatchSearchPage()
        }
        composable("Search"){
            SearchPage()
        }
        composable("Settings"){
            SettingsPage(setColorScheme)
        }
        composable("Error/{what}"){ backStackEntry ->
            val what = backStackEntry.arguments?.getString("what") ?: ""
            DisplayError(false, what)
        }
    }

}
