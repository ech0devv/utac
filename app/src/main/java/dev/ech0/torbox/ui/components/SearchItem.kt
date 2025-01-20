package dev.ech0.torbox.ui.components

import android.net.Uri
import android.text.format.Formatter
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.outlined.Cached
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.ech0.torbox.api.torboxAPI
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchItem(
    torrent: JSONObject,
    setLoadingScreen: (Boolean) -> Unit,
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val haptics = LocalHapticFeedback.current
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            if (torrent.getString("type") == "usenet") Icons.Filled.Newspaper else Icons.AutoMirrored.Filled.InsertDriveFile ,
            "File",
            modifier = Modifier.padding(end = 8.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = torrent.getString("raw_title"),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${Formatter.formatFileSize(context, torrent.getLong("size"))} ${if(torrent.getString("type") == "usenet") ", ${torrent.getInt("last_known_seeders")} seeding" else ""}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (torrent.getBoolean("owned")) {
            Icon(
                Icons.Outlined.Check,
                "Owned",
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.outline
            )

        } else if (torrent.getBoolean("cached")) {
            Icon(
                Icons.Outlined.Storage,
                "Cached",
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.outline
            )
        }

        IconButton(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(32.dp),
            content = {
                if(torrent.getBoolean("owned")){
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Go to downloads",
                        modifier = Modifier.padding(4.dp)
                    )
                }else{
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add torrent",
                        modifier = Modifier.padding(4.dp)
                    )
                }
            },
            onClick = {
                if(torrent.getBoolean("owned")){
                    navController.navigate("Downloads")
                }else{
                    scope.launch {
                        try {
                            setLoadingScreen(true)
                            if(torrent.getString("type") == "usenet"){
                                torboxAPI.createUsenet(torrent.getString("nzb"))
                            }else{
                                torboxAPI.createTorrent(torrent.getString("magnet"))
                            }
                            setLoadingScreen(false)
                            navController.navigate("Downloads")
                        } catch (e: Exception) {
                            navController.navigate("Error/${Uri.encode(e.toString())}")
                        }
                    }
                }

            },
            colors = IconButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }

    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))
}



