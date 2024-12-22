package dev.ech0.torbox.ui.pages.watch

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.text.format.Formatter
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import dev.ech0.torbox.api.Torrent
import dev.ech0.torbox.api.torboxAPI
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class Season constructor(var episodes: List<Episode>)
class Episode constructor(var episode: Int, var name: String, var overview: String?) {}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WatchShowPage(meta: JSONObject, navController: NavController) {

}
/*
val haptics = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Modifier.padding(start = 100.dp)
    var seriesDetails = JSONObject()
    var seasonCarouselState by remember { mutableStateOf(PagerState(pageCount = { 0 })) }
    var episodeReady by remember { mutableStateOf(false) }
    var seasons by remember { mutableStateOf(HashMap<Int, Season>()) }
    var pickTorrentDialog by remember { mutableStateOf(false) }
    var pickFileDialog by remember { mutableStateOf(false) }
    var pickFileOptions by remember { mutableStateOf(mutableListOf<String>()) }
    var pickFileSelected by remember { mutableIntStateOf(0) }
    var chosenSeasonNumber by remember { mutableIntStateOf(0) }
    var chosenEpisode by remember { mutableStateOf(Episode(0, "", "")) }
    var episodeSearchResult by remember { mutableStateOf(mutableListOf<Torrent>()) }
    var torrentList by remember { mutableStateOf(JSONObject()) }
    LaunchedEffect(true) {
        val seasonsTemp = HashMap<Int, Season>()
        if (meta.getString("type") == "series") {
            //seriesDetails = tvdbApi.seriesDetails(meta.getString("id").split("-")[1].toInt())
        } else if (meta.getString("type") == "movie") {
            navController.navigate("Error/${Uri.encode("movies not supported yet, sorry :(")}")
            return@LaunchedEffect
        }
        for (i in 0 until seriesDetails.getJSONArray("episodes").length()) {
            val episode = seriesDetails.getJSONArray("episodes").getJSONObject(i)
            val seasonNumber = episode.getInt("seasonNumber")
            if (seasonNumber == 0) continue;
            if (seasonsTemp.contains(seasonNumber)) {
                seasonsTemp[seasonNumber] = Season(
                    (seasonsTemp[seasonNumber]!!.episodes + (Episode(
                        episode.getInt("number"), when (!episode.isNull("name")) {
                            true -> when (!episode.isNull("nameTranslations")) {
                                true -> episode.getJSONArray("nameTranslations").getString(0)
                                else -> episode.getString("name")
                            }

                            else -> "Episode ${episode.getInt("number")}"
                        }, episode.getString("overview")
                    )))
                )
            } else {
                seasonsTemp[seasonNumber] = Season(
                    listOf(
                        Episode(
                            episode.getInt("number"), when (episode.has("name")) {
                                true -> episode.getString("name")
                                else -> "Episode ${episode.getInt("number")}"
                            }, episode.getString("overview")
                        )
                    )
                )
            }
        }
        seasonCarouselState = PagerState(pageCount = { seasonsTemp.size })
        seasons = seasonsTemp
        episodeReady = true
    }
    LaunchedEffect(seasonCarouselState) {
        snapshotFlow { seasonCarouselState.currentPage }.collect { page ->
            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
        ) {
            AsyncImage(
                model = meta.getString("image_url"),
                contentDescription = null,
                modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)).blur(25.dp)
                    .height(200.dp).graphicsLayer { alpha = 0.99F }.drawWithContent {
                        val colors = listOf(
                            Color.Black, Color.Transparent
                        )
                        drawContent()
                        drawRect(
                            brush = Brush.verticalGradient(colors, startY = 0f, endY = size.height),
                            blendMode = BlendMode.DstIn
                        )
                    },
                contentScale = ContentScale.Crop
            )
            Text(
                meta.getString("name"),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.fillMaxWidth().padding(top = 178.dp),
                textAlign = TextAlign.Center
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
        ) {
            Icon(
                Icons.Filled.KeyboardDoubleArrowLeft,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp).padding(start = 8.dp)
            )
            HorizontalPager(state = seasonCarouselState, modifier = Modifier.weight(1f)) { i ->
                Text(
                    text = "Season ${i + 1}",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Icon(
                Icons.Filled.KeyboardDoubleArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp).padding(end = 8.dp)
            )
        }
        if (episodeReady && seasonCarouselState.currentPage != -1) {
            Crossfade(targetState = seasonCarouselState.currentPage + 1) { state ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    items(seasons[state]!!.episodes.size) { episode ->
                        Row(modifier = Modifier.combinedClickable(onClick = {
                            scope.launch {
                                chosenSeasonNumber = state
                                chosenEpisode = seasons[state]!!.episodes[episode]
                                val torrentSearchResults =
                                    torboxAPI.searchTorrents(imdbId, chosenSeasonNumber, chosenEpisode.episode)
                                        .getJSONObject("data").getJSONArray("torrents")
                                for (i in 0 until torrentSearchResults.length()) {
                                    val torrent = torrentSearchResults.getJSONObject(i)
                                    episodeSearchResult.add(
                                        Torrent(
                                            torrent,
                                            torrent.getString("raw_title"),
                                            torrent.getLong("size"),
                                            torrent.getString("magnet"),
                                            "",
                                            torrent.getInt("last_known_seeders"),
                                            0,
                                            Regex("xt=urn:btih:([a-zA-Z0-9]+)").find(
                                                torrent.getString("magnet")
                                            )!!.groupValues[1],

                                            torrent.getBoolean("cached"),
                                            JSONArray()
                                        )
                                    )
                                }
                                pickTorrentDialog = true
                            }
                        })) {
                            Column {
                                Text(
                                    "${seasons[state]!!.episodes[episode].episode}. ${seasons[state]!!.episodes[episode].name}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    "${seasons[state]!!.episodes[episode].overview}",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }

    }
    if (pickTorrentDialog && !pickFileDialog) {
        Dialog(
            onDismissRequest = { pickTorrentDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
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
                            ).fillMaxWidth()
                        ) {
                            for (item in episodeSearchResult) {
                                Row(
                                    Modifier.padding(12.dp).combinedClickable(onClick = {
                                        scope.launch {
                                            val newTorrent = torboxAPI.createTorrent(item.magnet)
                                            if (!item.cached) {
                                                navController.navigate("Downloads")
                                            } else {
                                                val cachedRaw = torboxAPI.checkCache(item.hash)

                                                val cached = cachedRaw.getJSONObject("data")
                                                    .getJSONObject(cachedRaw.getJSONObject("data").keys().next())
                                                    .getJSONArray("files")
                                                var fileIds = mutableListOf<Int>()
                                                var fileId = 0
                                                for (i in 0 until cached.length()) {
                                                    if (cached.getJSONObject(i).getString("name").contains(
                                                            "S${chosenSeasonNumber.toString().padStart(2, '0')}E${chosenEpisode.episode.toString().padStart(2, '0')}"
                                                        ) ||
                                                        cached.getJSONObject(i).getString("name").contains(
                                                            "- ${chosenEpisode.episode.toString().padStart(2, '0')}"
                                                        )
                                                    ) {
                                                        fileIds.add(i)

                                                    }
                                                }
                                                Log.d("dev.ech0.torbox", fileIds.toString())
                                                if (fileIds.size == 1) {
                                                    fileId = fileIds[0]
                                                } else if(fileIds.size == 0){
                                                    Log.d("dev.ech0.torbox", "YES")
                                                    pickFileDialog = true
                                                    pickFileOptions = mutableListOf()
                                                    for (i in 0 until cached.length()) {
                                                        pickFileOptions.add(
                                                            cached.getJSONObject(i).getString("name")
                                                        )
                                                    }
                                                }else {
                                                    Log.d("dev.ech0.torbox", "YES")
                                                    pickFileDialog = true
                                                    pickFileOptions = mutableListOf()
                                                    for (i in 0 until fileIds.size) {
                                                        pickFileOptions.add(
                                                            cached.getJSONObject(fileIds.get(i)).getString("name")
                                                        )
                                                    }
                                                }
                                                val linkJSON = torboxAPI.getTorrentLink(
                                                    newTorrent.getJSONObject("data").getInt("torrent_id"), fileId, false
                                                )
                                                try {
                                                    val playVideo = Intent(Intent.ACTION_VIEW)
                                                    playVideo.setDataAndType(
                                                        Uri.parse(linkJSON.getString("data")), "video/x-unknown"
                                                    )
                                                    context.startActivity(playVideo)
                                                } catch (e: ActivityNotFoundException) {
                                                    Toast.makeText(
                                                        context, "No video player found", Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }

                                        }
                                    })
                                ) {
                                    Column {
                                        Text(item.title)
                                        Text(
                                            "${
                                                Formatter.formatFileSize(
                                                    context, item.size
                                                )
                                            }, ${item.seeders} seeds, ${if (item.cached) "cached" else "not cached"}",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
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
    if (pickFileDialog) {
        Dialog(
            onDismissRequest = { pickFileDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
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
                            ).fillMaxWidth()
                        ) {
                            for (item in pickFileOptions) {
                                Row(
                                    Modifier.padding(12.dp).combinedClickable(onClick = {
                                        pickFileSelected = pickFileOptions.indexOf(item)
                                        pickFileDialog = false
                                    })
                                ) {
                                    Column {
                                        Text(item)
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
 */