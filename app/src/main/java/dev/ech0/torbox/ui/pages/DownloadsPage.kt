package dev.ech0.torbox.ui.pages

import android.graphics.RenderEffect
import android.graphics.Shader
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.ech0.torbox.LocalNavController
import dev.ech0.torbox.api.torboxAPI
import dev.ech0.torbox.ui.components.DisplayError
import dev.ech0.torbox.ui.components.DownloadItem
import dev.ech0.torbox.ui.components.LoadingScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.Instant
import java.time.format.DateTimeFormatter

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class
)
@Composable
fun DownloadsPage() {
    val navController = LocalNavController.current

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isRefreshing by remember { mutableStateOf(true) }
    var downloads by remember { mutableStateOf<List<JSONObject>>(emptyList()) }
    var shouldLoad by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf(false) }
    var expandedFab by remember { mutableStateOf(false) }
    var magnetPrompt by remember { mutableStateOf(false) }
    var magnetText by remember { mutableStateOf("") }
    var selectedUri by remember { mutableStateOf("") }
    var currentFilter by remember { mutableStateOf(0) }
    val torrentLoader = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { result ->
        try {
            val item = context.contentResolver.openInputStream(result!!)
            val bytes = item!!.readBytes()
            scope.launch {
                shouldLoad = true
                if(context.contentResolver.getType(result) == "application/x-bittorrent"){
                    torboxAPI.createTorrent(bytes)
                }else{
                    torboxAPI.createUsenet(bytes)

                }
                shouldLoad = false
                isRefreshing = true
            }
            item.close()
        } catch (_: NullPointerException) {

        }
    }
    LaunchedEffect(isRefreshing) {
        scope.launch {
            while (true) {
                val startTime = System.currentTimeMillis()
                while (!isRefreshing && System.currentTimeMillis() - startTime < 1000) {
                    delay(50L)
                }
                try {
                    val data = torboxAPI.getListOfTorrents().getJSONArray("data")
                    val data_usenet = torboxAPI.getListOfUsenet().getJSONArray("data")
                    for (i in 0 until data_usenet.length()) {
                        data.put(data_usenet[i])
                    }
                    downloads = List(data.length()) { index -> data.getJSONObject(index) }
                    error = false
                    isRefreshing = false
                } catch (e: Exception) {
                    if (isRefreshing) {
                        isRefreshing = false
                        error = true
                    }
                }
            }
        }

    }


    PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = {
        isRefreshing = true
    }, modifier = Modifier.graphicsLayer {
        if (shouldLoad) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                renderEffect =
                    RenderEffect.createBlurEffect(25f, 25f, Shader.TileMode.MIRROR).asComposeRenderEffect()
            }
        }
    }) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(currentFilter == 0, label = {
                    Text("Alphabetical")
                }, onClick = { currentFilter = 0 }, leadingIcon = {
                    Crossfade(targetState = currentFilter == 0) { state ->
                        if (state) {
                            Icon(Icons.Filled.Check, "Checked")
                            downloads = downloads.sortedBy {  it.getString("name") }
                        } else {
                            Icon(Icons.Filled.Add, "Add")
                        }
                    }
                }, modifier = Modifier.padding(horizontal = 4.dp))
                FilterChip(currentFilter == 1, label = {
                    Text("Time Created")
                }, onClick = { currentFilter = 1 }, leadingIcon = {
                    Crossfade(targetState = currentFilter == 1) { state ->
                        if (state) {
                            Icon(Icons.Filled.Check, "Checked")
                            downloads = downloads.sortedByDescending {
                                Instant.from(
                                    DateTimeFormatter.ISO_INSTANT.parse(it.getString("created_at"))
                                ).toEpochMilli()
                            }
                        } else {
                            Icon(Icons.Filled.Add, "Add")
                        }
                    }
                }, modifier = Modifier.padding(horizontal = 4.dp))
                VerticalDivider(
                    modifier = Modifier
                        .height(FilterChipDefaults.Height)
                        .padding(4.dp)
                )
            }
            if (error) {
                DisplayError(recoverable = true)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 8.dp, top = 8.dp)
                ) {
                    items(downloads) { download ->
                        DownloadItem(
                            download,
                            setLoadingScreen = { shouldLoad = it },
                            setRefresh = { isRefreshing = it },
                            navController
                        )
                    }

                }
            }
        }
        FloatingActionButton(
            onClick = { expandedFab = true }, modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, "Add")
            DropdownMenu(expanded = expandedFab, onDismissRequest = { expandedFab = false }) {
                DropdownMenuItem(onClick = {
                    expandedFab = false
                    torrentLoader.launch("application/x-bittorrent")
                }, text = {
                    Text("Add torrent")
                }, leadingIcon = {
                    Icon(Icons.AutoMirrored.Filled.InsertDriveFile, "Add torrent")
                })
                DropdownMenuItem(onClick = {
                    expandedFab = false
                    magnetPrompt = true
                }, text = {
                    Text("Add magnet")
                }, leadingIcon = {
                    Icon(Icons.Filled.Link, "Add magnet")
                })
                DropdownMenuItem(onClick = {
                    expandedFab = false
                    torrentLoader.launch("application/octet-stream")
                }, text = {
                    Text("Add NZB")
                }, leadingIcon = {
                    Icon(Icons.Filled.Newspaper, "Add NZB")
                })

            }
        }

        if (magnetPrompt) {
            Dialog(onDismissRequest = { magnetPrompt = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),

                    ) {
                    Column(
                        modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Filled.Link, "Input Magnet URI", modifier = Modifier.padding(bottom = 0.dp)
                        )
                        Text(
                            "Input magnet uri",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        OutlinedTextField(
                            modifier = Modifier.padding(top = 24.dp, bottom = 24.dp),
                            value = magnetText,
                            onValueChange = { newVal ->
                                magnetText = newVal

                            },

                            )
                        Button(onClick = {
                            scope.launch {
                                try {
                                    magnetPrompt = false
                                    shouldLoad = true
                                    torboxAPI.createTorrent(magnetText)
                                    magnetText = ""
                                    shouldLoad = false
                                } catch (e: Exception) {
                                    navController.navigate("Error/${Uri.encode(e.toString())}")

                                }
                            }
                        }) {
                            Text("Submit")
                        }
                    }
                }
            }

        }
    }
    if (shouldLoad) {
        LoadingScreen()
    }
}