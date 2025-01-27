package dev.ech0.torbox.multiplatform.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.russhwolf.settings.Settings
import dev.ech0.torbox.multiplatform.LocalSnackbarHostState
import dev.ech0.torbox.multiplatform.api.torboxAPI
import dev.ech0.torbox.multiplatform.formatFileSize
import dev.ech0.torbox.multiplatform.getPlatform
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DownloadItem(
    download: Download, setLoadingScreen: (Boolean) -> Unit, setRefresh: (Boolean) -> Unit, navController: NavController
) {
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current
    val haptics = LocalHapticFeedback.current
    var expanded by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = LocalSnackbarHostState.current
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 20.dp)
            .combinedClickable(onClick = {}, onLongClick = {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                expanded = true
            }), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            if (download.seeds != null) Icons.AutoMirrored.Filled.InsertDriveFile else Icons.Filled.Newspaper,
            "File",
            modifier = Modifier.padding(end = 8.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = download.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = if (Settings().getBoolean("blurDL", false)) {
                    Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .blur(10.dp)
                } else {
                    Modifier
                }
            )
                Text(
                    text = "${download.downloadState.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }}, ↓${
                        formatFileSize(download.downloadSpeed.toLong())
                    }/s${
                        if (download.seeds != null) ", ↑${
                            formatFileSize(download.uploadSpeed.toLong())
                        }/s" else ""
                    }", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant
                )

        }
        fun handleClick(action: String) {
            expanded = false
            scope.launch {
                try {
                    if (download.seeds != null) {
                        torboxAPI.controlTorrent(download.id, action)
                    } else {
                        torboxAPI.controlUsenet(download.id, action)
                    }
                } catch (e: Exception) {
                    navController.navigate("Error/${e.toString().encodeURLPath()}")
                }
                setLoadingScreen(false)
                setRefresh(true)
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            val dropdownItems = if (download.seeds != null) {
                listOf(
                    Triple("Reannounce", Icons.Filled.Campaign, "reannounce"),
                    Triple("Resume", Icons.Filled.PlayArrow, "resume"),
                    Triple("Delete", Icons.Filled.Delete, "delete")
                )
            } else {
                listOf(
                    Triple("Pause", Icons.Filled.Pause, "pause"),
                    Triple("Resume", Icons.Filled.PlayArrow, "resume"),
                    Triple("Delete", Icons.Filled.Delete, "delete")
                )
            }
            dropdownItems.forEach { (text, icon, action) ->
                DropdownMenuItem(
                    onClick = { handleClick(action) },
                    text = { Text(text) },
                    leadingIcon = { Icon(icon, text) })
            }
        }
        if (download.cached) {
            IconButtonLongClickable(
                onClick = {
                    scope.launch {
                        if(!getPlatform().name.contains("Java")){
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                        setLoadingScreen(true)
                        try {
                            if (download.seeds != null) {
                                uriHandler.openUri(
                                    torboxAPI.getTorrentLink(
                                        download.id, download.files.size > 1
                                    )["data"]!!.jsonPrimitive.content
                                )
                            } else {
                                uriHandler.openUri(
                                    torboxAPI.getUsenetLink(
                                        download.id, false
                                    )["data"]!!.jsonPrimitive.content
                                )
                            }
                        } catch (e: Exception) {
                            navController.navigate("Error/${e.toString().encodeURLPath()}")
                        }
                        setLoadingScreen(false)
                    }
                }, onLongClick = {
                    scope.launch {
                        setLoadingScreen(true)
                        try {
                            if (download.seeds != null) {
                                clipboardManager.setText(
                                    AnnotatedString(
                                        torboxAPI.getTorrentLink(
                                            download.id, download.files.size > 1
                                        )["data"]!!.jsonPrimitive.content
                                    )
                                )
                            } else {
                                clipboardManager.setText(
                                    AnnotatedString(
                                        torboxAPI.getUsenetLink(
                                            download.id, false
                                        )["data"]!!.jsonPrimitive.content
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            navController.navigate("Error/${e.toString().encodeURLPath()}")
                        }
                        setLoadingScreen(false)
                        snackbarHostState.showSnackbar("Copied!")
                    }

                }, modifier = Modifier.padding(start = 16.dp).size(32.dp), content = {
                    Icon(
                        Icons.Filled.Download, contentDescription = "download", modifier = Modifier.padding(4.dp)
                    )
                }, colors = IconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        } else {
            if (download.downloadState.startsWith("error")) {
                Icon(
                    Icons.Filled.ErrorOutline,
                    "Error",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.tertiary
                )
            } else if (download.progress == 0.0 || download.downloadState.startsWith("stalled") || download.downloadState.startsWith(
                    "checkingDL"
                )
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(start = 16.dp).size(24.dp)
                )
            } else {
                CircularProgressIndicator(
                    progress = {
                        download.progress.toFloat()
                    },
                    modifier = Modifier.padding(start = 16.dp).size(24.dp),
                    trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                )
            }

        }
    }

    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))
}

@Serializable
data class Download(
    val id: Int,
    val name: String,
    val downloadState: String,
    val downloadSpeed: Double,
    val uploadSpeed: Double = 0.0,
    val progress: Double,
    val cached: Boolean,
    val seeds: Int? = null,
    val files: List<String>
)
