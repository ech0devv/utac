package dev.ech0.torbox.ui.components

import android.net.Uri
import android.text.format.Formatter
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.preference.PreferenceManager
import dev.ech0.torbox.IconButtonLongClickable
import dev.ech0.torbox.api.torboxAPI
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DownloadItem(
    download: JSONObject,
    setLoadingScreen: (Boolean) -> Unit,
    setRefresh: (Boolean) -> Unit,
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current
    val haptics = LocalHapticFeedback.current
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val preferences = remember { PreferenceManager.getDefaultSharedPreferences(context) }
    val clipboardManager = LocalClipboardManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 20.dp)
            .combinedClickable(onClick = {}, onLongClick = {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                expanded = true
            }), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            if (download.has("seeds")) Icons.AutoMirrored.Filled.InsertDriveFile else Icons.Filled.Newspaper,
            "File",
            modifier = Modifier.padding(end = 8.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = download.getString("name"),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = if (preferences.getBoolean("blurDL", false)) {
                    Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .blur(10.dp)
                } else {
                    Modifier
                }
            )
            Text(
                text = "${download.getString("download_state")}, ↓${
                    Formatter.formatFileSize(
                        context,
                        download.getDouble("download_speed").toLong()
                    )
                }/s${
                    if (download.has(
                            "seeds"
                        )
                    ) ", ↑${Formatter.formatFileSize(context, download.getDouble("upload_speed").toLong())}/s" else ""
                }", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            fun handleClick(action: String) {
                expanded = false
                scope.launch {
                    try {
                        setLoadingScreen(true)
                        if (download.has("seeds")) {
                            torboxAPI.controlTorrent(download.getInt("id"), action)
                        } else {
                            torboxAPI.controlUsenet(download.getInt("id"), action)
                        }
                        setLoadingScreen(false)
                        setRefresh(true)
                    } catch (e: Exception) {
                        navController.navigate("Error/${Uri.encode(e.toString())}")
                    }
                }
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                val dropdownItems = if (download.has("seeds")) {
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
        }
        if (download.getBoolean("cached")) {
            IconButtonLongClickable(

                onClick = {
                    scope.launch {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        setLoadingScreen(true)
                        try {
                            if (download.has("seeds")) {
                                uriHandler.openUri(
                                    torboxAPI.getTorrentLink(
                                        download.getInt(
                                            "id"
                                        ), download.getJSONArray("files").length() > 1

                                    ).getString("data")
                                )
                            } else {
                                uriHandler.openUri(
                                    torboxAPI.getUsenetLink(
                                        download.getInt(
                                            "id"
                                        ), false

                                    ).getString("data")
                                )
                            }
                        } catch (e: Exception) {
                            navController.navigate("Error/${Uri.encode(e.toString())}")
                        }
                        setLoadingScreen(false)
                    }
                }, onLongClick = {
                    scope.launch {
                        setLoadingScreen(true)
                        try {
                            if (download.has("seeds")) {
                                clipboardManager.setText(
                                    AnnotatedString(
                                        torboxAPI.getTorrentLink(
                                            download.getInt(
                                                "id"
                                            ), download.getJSONArray("files").length() > 1

                                        ).getString("data")
                                    )
                                )
                            } else {
                                clipboardManager.setText(
                                    AnnotatedString(
                                        torboxAPI.getUsenetLink(
                                            download.getInt(
                                                "id"
                                            ), false

                                        ).getString("data")
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            navController.navigate("Error/${Uri.encode(e.toString())}")
                        }
                        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                        setLoadingScreen(false)
                    }
                }, modifier = Modifier
                    .padding(start = 16.dp)
                    .size(32.dp), content = {
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
            if (download.getString("download_state").startsWith("error")) {
                Icon(
                    Icons.Filled.ErrorOutline,
                    "Error",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.tertiary
                )
            } else if (download.getDouble("progress") == 0.0 || download.getString("download_state")
                    .startsWith("stalled") || download.getString("download_state").startsWith("checkingDL")
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(24.dp)
                )
            } else {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(24.dp),
                    progress = { download.getDouble("progress").toFloat() })
            }

        }

    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))


}