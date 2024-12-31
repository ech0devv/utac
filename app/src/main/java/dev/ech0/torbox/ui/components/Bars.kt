package dev.ech0.torbox.ui.components

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.preference.PreferenceManager


@Composable
fun NavBar(
    navController: NavHostController
) {
    var selectedItem by remember { mutableIntStateOf(3) }
    val context = LocalContext.current
    val preferences = remember { PreferenceManager.getDefaultSharedPreferences(context) }
    val items = listOf("Search", "Watch", "Downloads", "Settings")
    val selectedIcons = listOf(Icons.Filled.Search, Icons.Filled.Tv, Icons.Filled.Download, Icons.Filled.Settings)
    val unselectedIcons = listOf(Icons.Outlined.Search, Icons.Outlined.Tv, Icons.Outlined.Download, Icons.Outlined.Settings)

    navController.addOnDestinationChangedListener{_, destination, _ ->
        selectedItem = items.indexOf(if(destination.route.toString().contains("/")){
            destination.route.toString().split("/")[0]
        }else{
            destination.route.toString()
        })
    }
    Column{
        if(preferences.getBoolean("amoled", false)){
            HorizontalDivider()
        }
        NavigationBar {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        Crossfade(targetState = (selectedItem == index)) { isSelected ->
                            Icon(
                                if (isSelected) selectedIcons[index] else unselectedIcons[index],
                                contentDescription = item
                            )
                        }
                    },
                    label = { Text(item) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index; navController.navigate(route = item) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavHostController
) {
    TopAppBar(
        title = { Text("Torbox") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
        ),
    )
}