package dev.ech0.torbox.multiplatform.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import dev.ech0.torbox.multiplatform.*
import io.ktor.http.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = { Text("TorBox") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
        ),
    )
}

@Composable
fun NavBar(
    navController: NavHostController
) {
    var selectedItem by remember { mutableIntStateOf(3) }
    val items = listOf("Search", "Watch", "Downloads", "Settings")
    val selectedIcons = listOf(Icons.Filled.Search, Icons.Filled.Tv, Icons.Filled.Download, Icons.Filled.Settings)
    val unselectedIcons =
        listOf(Icons.Outlined.Search, Icons.Outlined.Tv, Icons.Outlined.Download, Icons.Outlined.Settings)

    navController.addOnDestinationChangedListener { _, dest, _ ->
        //selectedItem =
        selectedItem = when (dest.route) {
            "Search" -> 0
            "Watch" -> 1
            "Downloads" -> 2
            "Settings" -> 3
            "Error/{what}" -> -1
            else -> {
                navController.navigate("Error/${"Navigation error.".encodeURLPath()}")
                -1
            }
        }
    }
    Column {
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