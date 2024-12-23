package dev.ech0.torbox.ui.components

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.preference.PreferenceManager
import dev.ech0.torbox.api.torboxAPI
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class Result(
    var raw_title: String,
    var title_parsed_data: JSONObject,
    var link: String,
    var usenet: Boolean,
    var cached: Boolean,
    var owned: Boolean
) {}

class TorrentSelectDialogArguments(
    var season: Int,
    var episode: Int,
    var remoteId: String,
    var type: String,
    var onDismiss: () -> Unit,
    var navController: NavController
) {}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TorrentSelectDialog(arguments: TorrentSelectDialogArguments) {
    var torrentResults by remember { mutableStateOf(JSONArray()) }
    var usenetResults by remember { mutableStateOf(JSONArray()) }
    var results by remember { mutableStateOf<List<Result>>(emptyList()) }
    var loadingText by remember { mutableStateOf("Just a sec...") }
    var shouldLoad by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val preferences = remember { PreferenceManager.getDefaultSharedPreferences(context) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(true) {
        loadingText = "Just a sec..."
        shouldLoad = true
        joinAll(launch {
           try{
               if (arguments.type == "tv") {
                   torrentResults = torboxAPI.searchTorrents(arguments.remoteId, arguments.season, arguments.episode)
                       .getJSONObject("data").getJSONArray("torrents")
               } else if (arguments.type == "movie") {
                   torrentResults =
                       torboxAPI.searchTorrentsId(arguments.remoteId).getJSONObject("data").getJSONArray("torrents")
               }
           }catch(e: Exception){
               Toast.makeText(context, "Failed to get torrent search results.", Toast.LENGTH_LONG).show()
               Log.d("dev.ech0.torbox",e.toString())
           }
            for (i in 0 until torrentResults.length()) {
                val torrent = torrentResults.getJSONObject(i)
                val titleParsedData = torrent.getJSONObject("title_parsed_data")
                val rawTitle = torrent.getString("raw_title")
                val link = torrent.getString("magnet")
                val cached = torrent.getBoolean("cached")
                val owned = torrent.getBoolean("owned")
                results += Result(rawTitle, titleParsedData, link, false, cached, owned)
            }
        }, launch {
            if (preferences.getInt("plan", 4) == 2 && preferences.getBoolean("usenet", true)) {
                try {
                    if (arguments.type == "tv") {
                        usenetResults = torboxAPI.searchUsenet(arguments.remoteId, arguments.season, arguments.episode)
                            .getJSONObject("data").getJSONArray("nzbs")
                    } else if (arguments.type == "movie") {
                        usenetResults =
                            torboxAPI.searchUsenetId(arguments.remoteId).getJSONObject("data").getJSONArray("nzbs")
                    }
                }catch (e: Exception){
                    Toast.makeText(context, "Failed to get usenet search results.", Toast.LENGTH_LONG).show()
                    Log.d("dev.ech0.torbox",e.toString())
                }
                for (i in 0 until usenetResults.length()) {
                    val usenet = usenetResults.getJSONObject(i)
                    val titleParsedData = usenet.getJSONObject("title_parsed_data")
                    val rawTitle = usenet.getString("raw_title")
                    val link = usenet.getString("nzb")
                    val cached = usenet.getBoolean("cached")
                    val owned = usenet.getBoolean("owned")
                    results += Result(rawTitle, titleParsedData, link, true, cached, owned)
                }
            }
        })
        results = results.shuffled().sortedByDescending {
            when{
                it.owned -> Int.MAX_VALUE
                it.cached -> Int.MAX_VALUE-1
                it.title_parsed_data.has("resolution") -> it.title_parsed_data.getString("resolution").replace("p", "").toIntOrNull() ?: 0
                else -> -1
            }
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
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
                        Modifier
                            .border(
                                2.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(12.dp)
                            )
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .wrapContentSize()
                        ) {
                            if (shouldLoad) {
                                Box(modifier = Modifier.padding(32.dp)) {
                                    LoadingScreen(loadingText)
                                }
                            } else {
                                if (results.size == 0) {
                                    Row(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .align(Alignment.CenterHorizontally)
                                    ) {
                                        Text("No results found")
                                    }
                                }

                                for (i in 0 until results.size) {
                                    val torrent = results.get(i)
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .padding(vertical = 16.dp)
                                            .clickable {
                                                scope.launch {
                                                    loadingText = "Just a sec... (1/4)"
                                                    shouldLoad = true
                                                    var newTorrent: JSONObject
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

                                                            val cached = cachedRaw
                                                                .getJSONObject("data")
                                                                .getJSONObject(
                                                                    cachedRaw
                                                                        .getJSONObject("data")
                                                                        .keys()
                                                                        .next()
                                                                )
                                                                .getJSONArray("files")

                                                            if (arguments.type == "tv") {
                                                                for (i in 0 until cached.length()) {
                                                                    if (cached
                                                                            .getJSONObject(i)
                                                                            .getString("name")
                                                                            .contains(
                                                                                "S${
                                                                                    arguments.season
                                                                                        .toString()
                                                                                        .padStart(2, '0')
                                                                                }E${
                                                                                    arguments.episode
                                                                                        .toString()
                                                                                        .padStart(2, '0')
                                                                                }", ignoreCase = true
                                                                            ) || cached
                                                                            .getJSONObject(i)
                                                                            .getString("name")
                                                                            .contains(
                                                                                "- ${
                                                                                    arguments.episode
                                                                                        .toString()
                                                                                        .padStart(2, '0')
                                                                                }"
                                                                            )
                                                                    ) {
                                                                        fileIds.add(i)

                                                                    }
                                                                }
                                                                if (fileIds.size == 1) {
                                                                    fileId = fileIds[0]
                                                                    Log.d("dev.ech0.torbox", fileId.toString())
                                                                } else {
                                                                    arguments.navController.navigate(
                                                                        "Error/${
                                                                            Uri.encode(
                                                                                "Too many files that match found. Fix is coming in next release. Pick another torrent for now."
                                                                            )
                                                                        }"
                                                                    )
                                                                }
                                                            } else {
                                                                for (i in 0 until cached.length()) {
                                                                    if (cached
                                                                            .getJSONObject(i)
                                                                            .getString("name")
                                                                            .any {
                                                                                it
                                                                                    .toString()
                                                                                    .endsWith("mkv") || it
                                                                                    .toString()
                                                                                    .endsWith("mp4") || it
                                                                                    .toString()
                                                                                    .endsWith("mov") || it
                                                                                    .toString()
                                                                                    .endsWith("avi")
                                                                            }
                                                                    ) {
                                                                        fileId = i
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        var linkJSON: JSONObject
                                                        if (torrent.usenet) {
                                                            Log.d("dev.ech0.torbox", newTorrent.toString())
                                                            linkJSON = torboxAPI.getUsenetLink(
                                                                newTorrent
                                                                    .getJSONObject("data")
                                                                    .getInt("usenetdownload_id"), false
                                                            )
                                                        } else {
                                                            linkJSON = torboxAPI.getTorrentLink(
                                                                newTorrent
                                                                    .getJSONObject("data")
                                                                    .getInt("torrent_id"), fileId, false
                                                            )
                                                        }
                                                        loadingText = "Just a sec... (4/4)"
                                                        try {
                                                            val playVideo = Intent(Intent.ACTION_VIEW)
                                                            playVideo.setDataAndType(
                                                                Uri.parse(linkJSON.getString("data")), "video/x-unknown"
                                                            )
                                                            context.startActivity(playVideo)
                                                        } catch (e: ActivityNotFoundException) {
                                                            Toast
                                                                .makeText(
                                                                    context, "No video player found", Toast.LENGTH_SHORT
                                                                )
                                                                .show()
                                                        }
                                                        shouldLoad = false
                                                    }
                                                }
                                            }) {
                                        Column {
                                            Text(torrent.raw_title)
                                            FlowRow() {
                                                for (key in torrent.title_parsed_data.keys()) {
                                                    var titleData = torrent.title_parsed_data.get(key)
                                                    if (key == "bitDepth") {
                                                        titleData = "$titleData bit"
                                                    }
                                                    if (key == "season") {
                                                        titleData = "S$titleData"
                                                    }
                                                    if (key == "title") {
                                                        continue
                                                    }
                                                    Box(
                                                        modifier = Modifier
                                                            .padding(2.dp)
                                                            .clip(RoundedCornerShape(4.dp))
                                                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                                    ) {
                                                        Text(
                                                            titleData.toString(),
                                                            style = MaterialTheme.typography.bodySmall,
                                                            modifier = Modifier.padding(4.dp)
                                                        )
                                                    }
                                                }
                                                if (torrent.cached) {
                                                    Box(
                                                        modifier = Modifier
                                                            .padding(2.dp)
                                                            .clip(RoundedCornerShape(4.dp))
                                                            .background(MaterialTheme.colorScheme.surfaceContainer)
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
                                                        modifier = Modifier
                                                            .padding(2.dp)
                                                            .clip(RoundedCornerShape(4.dp))
                                                            .background(MaterialTheme.colorScheme.surfaceContainer)
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
                                                        modifier = Modifier
                                                            .padding(2.dp)
                                                            .clip(RoundedCornerShape(4.dp))
                                                            .background(MaterialTheme.colorScheme.surfaceContainer)
                                                    ) {
                                                        Text(
                                                            "usenet",
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
}

