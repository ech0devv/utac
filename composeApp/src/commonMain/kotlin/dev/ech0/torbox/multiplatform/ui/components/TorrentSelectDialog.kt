package dev.ech0.torbox.multiplatform.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.russhwolf.settings.Settings
import dev.ech0.torbox.multiplatform.LocalSnackbarHostState
import dev.ech0.torbox.multiplatform.PlayVideo
import dev.ech0.torbox.multiplatform.api.torboxAPI
import dev.ech0.torbox.multiplatform.formatFileSize
import io.ktor.http.*
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*

class Result(
    var raw_title: String,
    var title_parsed_data: JsonObject,
    var link: String,
    var usenet: Boolean,
    var cached: Boolean,
    var owned: Boolean,
    var size: Long = 0,
)

class TorrentSelectDialogArguments(
    var season: Int,
    var episode: Int,
    var remoteId: String,
    var type: String,
    var onDismiss: () -> Unit,
    var navController: NavController
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TorrentSelectDialog(arguments: TorrentSelectDialogArguments) {
    var torrentResults by remember { mutableStateOf(JsonArray(emptyList())) }
    var usenetResults by remember { mutableStateOf(JsonArray(emptyList())) }
    var results by remember { mutableStateOf<List<Result>>(emptyList()) }
    var loadingText by remember { mutableStateOf("Just a sec...") }
    var shouldLoad by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = LocalSnackbarHostState.current

    var playVideo by remember { mutableStateOf(false) }
    var videoUrl by remember { mutableStateOf("") }
    LaunchedEffect(true) {
        loadingText = "Just a sec..."
        shouldLoad = true
        joinAll(launch {
            try {
                if (arguments.type == "tv") {
                    torrentResults = torboxAPI.searchTorrents(
                        arguments.remoteId, arguments.season, arguments.episode
                    )["data"]!!.jsonObject["torrents"]!!.jsonArray
                } else if (arguments.type == "movie") {/*torrentResults =
                        torboxAPI.searchTorrentsId(arguments.remoteId).getJSONObject("data").getJSONArray("torrents")*/
                    torrentResults =
                        torboxAPI.searchTorrents(arguments.remoteId)["data"]!!.jsonObject["torrents"]!!.jsonArray
                }
            } catch (e: Exception) {
                snackbarHostState.showSnackbar("Failed to get torrent search results.")
            }
            for (i in 0 until torrentResults.size) {
                val torrent = torrentResults[i].jsonObject
                val titleParsedData = torrent["title_parsed_data"]!!.jsonObject
                val rawTitle = torrent["raw_title"]!!.jsonPrimitive.content
                val link = torrent["magnet"]!!.jsonPrimitive.content
                val cached = torrent["cached"]!!.jsonPrimitive.boolean
                val owned = torrent["owned"]!!.jsonPrimitive.boolean
                val size = torrent["size"]!!.jsonPrimitive.long
                results += Result(rawTitle, titleParsedData, link, false, cached, owned, size)
            }
        }, launch {
            if (Settings().getInt("plan", 4) == 2 && Settings().getBoolean("usenet", true)) {
                try {
                    if (arguments.type == "tv") {
                        usenetResults = torboxAPI.searchUsenet(
                            arguments.remoteId, arguments.season, arguments.episode
                        )["data"]!!.jsonObject["nzbs"]!!.jsonArray
                    } else if (arguments.type == "movie") {
                        usenetResults =
                            torboxAPI.searchUsenet(arguments.remoteId)["data"]!!.jsonObject["nzbs"]!!.jsonArray
                    }
                } catch (e: Exception) {
                    snackbarHostState.showSnackbar("Failed to get usenet search results.")
                }
                for (i in 0 until usenetResults.size) {
                    val usenet = usenetResults[i].jsonObject
                    val titleParsedData = usenet["title_parsed_data"]!!.jsonObject
                    val rawTitle = usenet["raw_title"]!!.jsonPrimitive.content
                    val link = usenet["nzb"]!!.jsonPrimitive.content
                    val cached = usenet["cached"]!!.jsonPrimitive.boolean
                    val owned = usenet["owned"]!!.jsonPrimitive.boolean
                    val size = usenet["size"]!!.jsonPrimitive.long
                    results += Result(rawTitle, titleParsedData, link, true, cached, owned, size)
                }
            }
        })
        results = results.shuffled().sortedByDescending {
            when {
                it.owned -> Int.MAX_VALUE
                it.cached -> Int.MAX_VALUE - 1

                else -> -1
            }
            var score = 0
            if (it.owned) {
                score += 1000000
            } else if (it.cached) {
                score += 100000
            }
            if (it.title_parsed_data.contains("resolution")) {
                score += it.title_parsed_data["resolution"]!!.jsonPrimitive.content.replace("p", "").toIntOrNull() ?: 0
            }
            score
        }

        shouldLoad = false
    }
    Dialog(
        onDismissRequest = arguments.onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier.clip(
                RoundedCornerShape(12.dp)
            )
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxWidth().verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.VideoFile, "Select Video File", modifier = Modifier.padding(bottom = 0.dp)
                    )
                    Text(
                        "Select Video File",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
                    )
                    Column(
                        Modifier.border(
                            2.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(12.dp)
                        ).fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 8.dp).wrapContentSize()
                        ) {
                            if (shouldLoad) {
                                Box(modifier = Modifier.padding(32.dp)) {
                                    LoadingScreen(loadingText)
                                }
                            } else {
                                if (results.size == 0) {
                                    Row(
                                        modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
                                    ) {
                                        Text("No results found")
                                    }
                                }

                                for (i in 0 until results.size) {
                                    val torrent = results.get(i)
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(vertical = 16.dp).clickable {
                                            scope.launch {
                                                loadingText = "Just a sec... (1/4)"
                                                shouldLoad = true
                                                var newTorrent: JsonObject
                                                if (torrent.usenet) {
                                                    newTorrent = torboxAPI.createUsenet(torrent.link)
                                                } else {
                                                    newTorrent = torboxAPI.createTorrent(torrent.link)
                                                }
                                                if (!torrent.cached) {
                                                    arguments.navController.navigate("Downloads")
                                                } else {
                                                    var fileIds = mutableListOf<Int>()
                                                    var fileId = 0
                                                    loadingText = "Just a sec... (2/4)"
                                                    if (!torrent.usenet) {
                                                        val cachedRaw = torboxAPI.checkCache(
                                                            Regex("xt=urn:btih:([a-zA-Z0-9]+)").find(
                                                                torrent.link
                                                            )!!.groupValues[1]
                                                        )
                                                        loadingText = "Just a sec... (3/4)"

                                                        val cached =
                                                            cachedRaw["data"]!!.jsonObject[cachedRaw["data"]!!.jsonObject.keys.first()]!!.jsonObject["files"]!!.jsonArray

                                                        if (arguments.type == "tv") {
                                                            for (i in 0 until cached.size) {
                                                                if (cached[i].jsonObject["name"]!!.jsonPrimitive.content.contains(
                                                                        "S${
                                                                            arguments.season.toString().padStart(2, '0')
                                                                        }E${
                                                                            arguments.episode.toString()
                                                                                .padStart(2, '0')
                                                                        }", ignoreCase = true
                                                                    ) || cached[i].jsonObject["name"]!!.jsonPrimitive.content.contains(
                                                                        "- ${
                                                                            arguments.episode.toString()
                                                                                .padStart(2, '0')
                                                                        }"
                                                                    )
                                                                ) {
                                                                    fileIds.add(i)

                                                                }
                                                            }
                                                            if (fileIds.size == 1) {
                                                                fileId = fileIds[0]
                                                            } else {
                                                                arguments.navController.navigate(
                                                                    "Error/${
                                                                        "Too many files that match found. Fix is coming in next release. Pick another torrent for now.".encodeURLPath()
                                                                    }"
                                                                )
                                                            }
                                                        } else {
                                                            for (i in 0 until cached.size) {
                                                                if (cached[i].jsonObject["name"]!!.jsonPrimitive.content.any {
                                                                        it.toString().endsWith("mkv") || it.toString()
                                                                            .endsWith("mp4") || it.toString()
                                                                            .endsWith("mov") || it.toString()
                                                                            .endsWith("avi")
                                                                    }) {
                                                                    fileId = i
                                                                }
                                                            }
                                                        }
                                                    }
                                                    var linkJSON: JsonObject
                                                    if (torrent.usenet) {
                                                        linkJSON = torboxAPI.getUsenetLink(
                                                            newTorrent["data"]!!.jsonObject["usenetdownload_id"]!!.jsonPrimitive.int,
                                                            false
                                                        )
                                                    } else {
                                                        linkJSON = torboxAPI.getTorrentLink(
                                                            newTorrent["data"]!!.jsonObject["torrent_id"]!!.jsonPrimitive.int,
                                                            fileId,
                                                            false
                                                        )
                                                    }
                                                    loadingText = "Just a sec... (4/4)"/* TODO: try {
                                                        val playVideo = Intent(Intent.ACTION_VIEW)
                                                        playVideo.setDataAndType(
                                                            Uri.parse(linkJSON.getString("data")), "video/x-unknown"
                                                        )
                                                        context.startActivity(playVideo)
                                                    } catch (e: ActivityNotFoundException) {
                                                        Toast.makeText(
                                                            context, "No video player found", Toast.LENGTH_SHORT
                                                        ).show()
                                                    }*/
                                                    videoUrl = linkJSON["data"]!!.jsonPrimitive.content
                                                    playVideo = true
                                                    shouldLoad = false
                                                }
                                            }
                                        }) {
                                        Column {
                                            Text(torrent.raw_title)

                                            FlowRow {
                                                for (key in torrent.title_parsed_data.keys) {
                                                    var titleData = torrent.title_parsed_data[key]
                                                    var titleString = ""
                                                    if (titleData is JsonPrimitive) {
                                                        if (key == "bitDepth") {
                                                            titleString = "${titleData.jsonPrimitive.content} bit"
                                                        } else if (key == "season") {
                                                            titleString = "S${titleData.jsonPrimitive.content}"
                                                        } else if (key == "title") {
                                                            continue
                                                        } else if (titleData.jsonPrimitive.booleanOrNull != null) {
                                                            titleString =
                                                                if (titleData.jsonPrimitive.boolean) key else continue
                                                        } else {
                                                            titleString = titleData.jsonPrimitive.content
                                                        }
                                                    } else if (titleData is JsonArray && key == "season") {
                                                        titleString = "S$titleData"
                                                    } else {
                                                        continue
                                                    }
                                                    if (titleString.isEmpty()) {
                                                        continue
                                                    }
                                                    Box(
                                                        modifier = Modifier.padding(2.dp).clip(RoundedCornerShape(4.dp))
                                                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                                    ) {
                                                        Text(
                                                            titleString.toString(),
                                                            style = MaterialTheme.typography.bodySmall,
                                                            modifier = Modifier.padding(4.dp)
                                                        )
                                                    }
                                                }
                                                if (torrent.cached) {
                                                    Box(
                                                        modifier = Modifier.padding(2.dp).clip(RoundedCornerShape(4.dp))
                                                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                                    ) {
                                                        Text(
                                                            "cached",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            modifier = Modifier.padding(4.dp)
                                                        )
                                                    }
                                                }
                                                if (torrent.owned) {
                                                    Box(
                                                        modifier = Modifier.padding(2.dp).clip(RoundedCornerShape(4.dp))
                                                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                                    ) {
                                                        Text(
                                                            "owned",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            modifier = Modifier.padding(4.dp)
                                                        )
                                                    }
                                                }
                                                if (torrent.usenet) {
                                                    Box(
                                                        modifier = Modifier.padding(2.dp).clip(RoundedCornerShape(4.dp))
                                                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                                    ) {
                                                        Text(
                                                            "usenet",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            modifier = Modifier.padding(4.dp)
                                                        )
                                                    }
                                                }
                                                if (torrent.size > 0) {
                                                    Box(
                                                        modifier = Modifier.padding(2.dp).clip(RoundedCornerShape(4.dp))
                                                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                                    ) {
                                                        Text(
                                                            formatFileSize(torrent.size),
                                                            style = MaterialTheme.typography.bodySmall,
                                                            modifier = Modifier.padding(4.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    HorizontalDivider()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (playVideo) {
        PlayVideo(videoUrl)
        playVideo = false
    }
}

