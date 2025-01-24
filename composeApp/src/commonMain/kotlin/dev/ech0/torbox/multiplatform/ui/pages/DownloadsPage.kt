package dev.ech0.torbox.multiplatform.ui.pages

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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.ech0.torbox.multiplatform.LocalNavController
import dev.ech0.torbox.multiplatform.api.torboxAPI
import dev.ech0.torbox.multiplatform.ui.components.Download
import dev.ech0.torbox.multiplatform.ui.components.DownloadItem
import dev.ech0.torbox.multiplatform.ui.components.LoadingScreen
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.*


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalSerializationApi::class
)
@Composable
fun DownloadsPage(magnet: String = "") {
    val navController = LocalNavController.current
    val scope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(true) }
    var downloads by remember { mutableStateOf<List<JsonObject>>(emptyList()) }
    var shouldLoad by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf(false) }
    var expandedFab by remember { mutableStateOf(false) }
    var magnetPrompt by remember { mutableStateOf(false) }
    var magnetText by remember { mutableStateOf("") }
    var selectedUri by remember { mutableStateOf("") }
    var currentFilter by remember { mutableStateOf(0) }
    var openInPrompted by remember { mutableStateOf(false) }
    /*TODO: val torrentLoader = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { result ->
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
    }*/
    LaunchedEffect(isRefreshing) {
        scope.launch {
            while (true) {
                val startTime = Clock.System.now().toEpochMilliseconds()
                while (!isRefreshing && Clock.System.now().toEpochMilliseconds() - startTime < 1000) {
                    delay(50L)
                }
                try {
                    val data_torrents = torboxAPI.getListOfTorrents()["data"]!!.jsonArray
                    val data_usenet = torboxAPI.getListOfUsenet()["data"]!!.jsonArray
                    val data = buildJsonArray {
                        addAll(data_torrents)
                        addAll(data_usenet)
                    }
                    downloads = data.map { it.jsonObject }
                    error = false
                    isRefreshing = false
                } catch (e: Exception) {
                    if (isRefreshing) {
                        isRefreshing = false
                        error = true
                    }
                }
            }
        }/*if(magnet != "" && !openInPrompted){
            magnetPrompt = true
            magnetText = Uri.parse(magnet).toString()
            openInPrompted = true
        }*/
    }


    PullToRefreshBox(
        isRefreshing = isRefreshing, onRefresh = {
            isRefreshing = true
        }, modifier = if(shouldLoad){
            Modifier.blur(25.dp)
        }else{
            Modifier
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp)
                    .horizontalScroll(rememberScrollState()), verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(currentFilter == 0, label = {
                    Text("Alphabetical")
                }, onClick = { currentFilter = 0 }, leadingIcon = {
                    Crossfade(targetState = currentFilter == 0) { state ->
                        if (state) {
                            Icon(Icons.Filled.Check, "Checked")
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
                        } else {
                            Icon(Icons.Filled.Add, "Add")
                        }
                    }
                }, modifier = Modifier.padding(horizontal = 4.dp))
                VerticalDivider(
                    modifier = Modifier.height(FilterChipDefaults.Height).padding(4.dp)
                )
            }
            if (error) {
                //DisplayError(recoverable = true)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(bottom = 8.dp, top = 8.dp)
                ) {
                    items(
                        when (currentFilter) {
                        0 -> {
                            downloads.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it["name"]!!.jsonPrimitive.content })
                        }
                        1 -> {
                            downloads.sortedByDescending {
                                Instant.parse(it["created_at"]!!.jsonPrimitive.content).toEpochMilliseconds()
                            }
                        }
                        else -> {
                            downloads
                        }
                    }) { download ->
                        DownloadItem(
                            download = Download(
                            id = download["id"]?.jsonPrimitive?.intOrNull ?: 0,
                            name = download["name"]?.jsonPrimitive?.content ?: "Unknown",
                            cached = download["cached"]?.jsonPrimitive?.boolean ?: false,
                            downloadState = download["download_state"]?.jsonPrimitive?.content ?: "",
                            downloadSpeed = download["download_speed"]?.jsonPrimitive?.double ?: 0.0,
                            uploadSpeed = download["upload_speed"]?.jsonPrimitive?.double ?: 0.0,
                            progress = download["progress"]?.jsonPrimitive?.double ?: 0.0,
                            seeds = download["seeds"]?.jsonPrimitive?.intOrNull ?: 0,
                            files = listOf()
                        ), setLoadingScreen = { shouldLoad = it }, setRefresh = { isRefreshing = it }, navController
                        )
                    }

                }
            }
        }
        FloatingActionButton(
            onClick = { expandedFab = true }, modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, "Add")
            DropdownMenu(expanded = expandedFab, onDismissRequest = { expandedFab = false }) {
                DropdownMenuItem(onClick = {
                    expandedFab = false
                    //torrentLoader.launch("application/x-bittorrent")
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
                    //torrentLoader.launch("application/octet-stream")
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
                    modifier = Modifier.fillMaxWidth().padding(16.dp),

                    ) {
                    Column(
                        modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
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
                                    navController.navigate("Error/${e.toString().encodeURLPath()}")

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