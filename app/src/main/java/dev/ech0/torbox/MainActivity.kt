package dev.ech0.torbox

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.preference.PreferenceManager
import dev.ech0.torbox.api.*
import dev.ech0.torbox.ui.components.DisplayError
import dev.ech0.torbox.ui.components.NavBar
import dev.ech0.torbox.ui.components.TopBar
import dev.ech0.torbox.ui.pages.DownloadsPage
import dev.ech0.torbox.ui.pages.SearchPage
import dev.ech0.torbox.ui.pages.SettingsPage
import dev.ech0.torbox.ui.pages.watch.WatchSearchPage
import dev.ech0.torbox.ui.theme.AppTheme
import dev.ech0.torbox.ui.theme.amoledScheme
import kotlinx.coroutines.launch

val LocalNavController = compositionLocalOf<NavController> { error("No NavController found!") }

class MainActivity : ComponentActivity() {
    // meow
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        val magnet = intent?.data
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val preferences = remember { PreferenceManager.getDefaultSharedPreferences(context) }
            var colorScheme by remember {
                mutableStateOf(
                    if (preferences.getBoolean("amoled", false)) {
                        amoledScheme
                    } else {
                        null
                    }
                )
            }
            val navController = rememberNavController()
            AppTheme(colorSchemeIn = colorScheme) {
                CompositionLocalProvider(LocalNavController provides navController) {
                    Scaffold(
                        bottomBar = { NavBar(navController) },
                        topBar = { TopBar(navController) },
                        modifier = Modifier.imePadding()
                    ) { paddingValues ->
                        tmdbApi = TMDBApi(preferences)
                        Navigation(navController, paddingValues, setColorScheme = { colorScheme = it })
                        torboxAPI =
                            preferences.getString("apiKey", "__")
                                ?.let { it1 -> TorboxAPI(it1, navController) }!!
                        if(magnet != null){
                            navController.navigate("Downloads/${Uri.encode(magnet.toString())}")
                        }
                        traktApi = Trakt(preferences)
                    }
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
            composable("Downloads") {
                DownloadsPage("")
            }
            composable("Downloads/{magnet}", arguments = listOf(navArgument("magnet"){type = NavType.StringType; defaultValue = "" })) { backStackEntry ->
                DownloadsPage(backStackEntry.arguments?.getString("magnet")!!)
            }
            composable("Watch") {
                WatchSearchPage()
                //DisplayError(recoverable = false, what = "soon, but not yet")
            }
            composable("Settings") {
                SettingsPage(setColorScheme = setColorScheme)
            }
            composable("Search") {
                SearchPage()
            }
            composable(
                "Error/{message}",
                arguments = listOf(navArgument("message") { type = NavType.StringType })
            ) { backStackEntry ->
                DisplayError(
                    recoverable = false,
                    what = backStackEntry.arguments?.getString("message")
                        ?: "Achievement get: How did we get here?"
                )
            }
        }

    }
}


