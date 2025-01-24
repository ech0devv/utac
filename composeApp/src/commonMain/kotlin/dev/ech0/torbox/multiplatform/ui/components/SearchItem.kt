package dev.ech0.torbox.multiplatform.ui.components

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
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.ech0.torbox.multiplatform.api.torboxAPI
import dev.ech0.torbox.multiplatform.formatFileSize
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchItem(
    torrent: JsonObject,
    setLoadingScreen: (Boolean) -> Unit,
    navController: NavController
) {
    val scope = rememberCoroutineScope()
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
            if (torrent["type"]!!.jsonPrimitive.content == "usenet") Icons.Filled.Newspaper else Icons.AutoMirrored.Filled.InsertDriveFile ,
            "File",
            modifier = Modifier.padding(end = 8.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = torrent["raw_title"]!!.jsonPrimitive.content,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${formatFileSize(torrent["size"]!!.jsonPrimitive.long)} ${if(torrent["type"]!!.jsonPrimitive.content != "usenet") ", ${torrent["last_known_seeders"]!!.jsonPrimitive.int} seeding" else ""}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (torrent["owned"]!!.jsonPrimitive.boolean) {
            Icon(
                Icons.Outlined.Check,
                "Owned",
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.outline
            )

        } else if (torrent["cached"]!!.jsonPrimitive.boolean) {
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
                if(torrent["owned"]!!.jsonPrimitive.boolean){
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
                if(torrent["owned"]!!.jsonPrimitive.boolean){
                    navController.navigate("Downloads")
                }else{
                    scope.launch {
                        try {
                            setLoadingScreen(true)
                            if(torrent["type"]!!.jsonPrimitive.content == "usenet"){
                                torboxAPI.createUsenet(torrent["nzb"]!!.jsonPrimitive.content)
                            }else{
                                torboxAPI.createTorrent(torrent["magnet"]!!.jsonPrimitive.content)
                            }
                            setLoadingScreen(false)
                            navController.navigate("Downloads")
                        } catch (e: Exception) {
                            navController.navigate("Error/${e.toString().encodeURLPath()}")
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



