package dev.ech0.torbox.multiplatform.ui.pages.watch

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import dev.ech0.torbox.multiplatform.api.tmdbApi
import dev.ech0.torbox.multiplatform.api.traktApi
import dev.ech0.torbox.multiplatform.ui.components.LoadingScreen
import dev.ech0.torbox.multiplatform.ui.components.TorrentSelectDialogArguments
import dev.ech0.torbox.multiplatform.ui.components.TorrentSelectDialog
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WatchPage(meta: JsonObject, navController: NavController) {
    var overviewDialog by remember { mutableStateOf(false) }
    var overviewDialogContent by remember { mutableStateOf("") }
    val type = meta["media_type"]!!.jsonPrimitive.content
    var details by remember { mutableStateOf(JsonObject(emptyMap())) }
    var seasons by remember { mutableStateOf(arrayOfNulls<JsonObject>(0)) }
    var seasonCarouselState by remember { mutableStateOf(PagerState(pageCount = { 1 })) }
    var traktResponse by remember { mutableStateOf(JsonObject(emptyMap())) }
    var shouldLoad by remember { mutableStateOf(false) }
    val haptics = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    var traktId by remember { mutableStateOf(0) }
    var torrentSelectDialogArgs by remember {
        mutableStateOf(
            TorrentSelectDialogArguments(
                0, 0, "", "", {}, navController
            )
        )
    }
    var displayTorrentSelectDialog by remember { mutableStateOf(false) }
    LaunchedEffect(true) {
        traktId = traktApi.getTraktIdFromTMDB(meta["id"]!!.jsonPrimitive.long)
        if (type == "tv") {
            traktResponse = traktApi.getWatchedShow(traktId.toLong()) ?: JsonObject(emptyMap())
            details = tmdbApi.getTvDetails(meta["id"]!!.jsonPrimitive.int)
            seasons = arrayOfNulls<JsonObject>(details["number_of_seasons"]!!.jsonPrimitive.int)
            seasons[0] = tmdbApi.getSeasonDetails(meta["id"]!!.jsonPrimitive.int, 1)
            seasonCarouselState = PagerState(pageCount = { seasons.size })
        } else if (type == "movie") {
            traktResponse = traktApi.getWatchedMovie(traktId.toLong()) ?: JsonObject(emptyMap())
            details = tmdbApi.getMovieDetails(meta["id"]!!.jsonPrimitive.int)
            seasons = arrayOfNulls<JsonObject>(1)
            seasons[0] = Json.parseToJsonElement(
                """
                {
                    "air_date": "${details["release_date"]!!.jsonPrimitive.content}",
                    "episodes": [
                        {
                            "episode_number": 1,
                            "name": "Movie",
                            "runtime": ${details["runtime"]!!.jsonPrimitive.int},
                            "watched": ${traktApi.getWatchedMovie(traktId.toLong()) != null}
                        }
                    ]
                    
                }
            """.trimIndent()
            ).jsonObject
            // recompose
            seasonCarouselState = PagerState(pageCount = { 1 })
        }
    }
    LaunchedEffect(seasonCarouselState) {
        snapshotFlow { seasonCarouselState.currentPage }.collect { page ->
            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            if (seasons.size != 0) {
                if (seasons[page] == null) {
                    val updatedSeasons = seasons.copyOf()
                    updatedSeasons[page] = tmdbApi.getSeasonDetails(meta["id"]!!.jsonPrimitive.int, page + 1)
                    seasons = updatedSeasons
                }
            }
        }
    }
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Box(
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
        ) {
            AsyncImage(
                model = tmdbApi.imageHelper(
                meta["backdrop_path"]?.jsonPrimitive?.contentOrNull ?: meta["poster_path"]?.jsonPrimitive?.contentOrNull
                ?: ""
            ),
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
            SelectionContainer {
                Text(
                    meta["name"]?.jsonPrimitive?.contentOrNull ?: meta["title"]?.jsonPrimitive?.contentOrNull ?: "",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        shadow = Shadow(
                            color = Color.Gray, offset = Offset(0f, 0f), blurRadius = 25f
                        )
                    ),
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.fillMaxWidth().padding(top = 178.dp).padding(horizontal = 24.dp),
                    textAlign = TextAlign.Center
                )
            }

        }
        if (details.contains("tagline")) {
            if (details["tagline"]!!.jsonPrimitive.content.isNotBlank()) {
                Text(
                    details["tagline"]!!.jsonPrimitive.content,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 12.dp).align(Alignment.CenterHorizontally)
                )
            }
        }
        if (details.contains("content_ratings")) {
            val ratings = details["content_ratings"]!!.jsonObject["results"]!!.jsonArray
            if (ratings.size > 0) {
                var rating = ratings[0].jsonObject
                for (i in 0 until ratings.size) {
                    if (ratings[i].jsonObject["iso_3166_1"]!!.jsonPrimitive.content == "US") {
                        rating = ratings[i].jsonObject
                        break
                    }
                }
                Text(
                    rating["rating"]!!.jsonPrimitive.content,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(
                        top = if (details.contains("tagline")) {
                            4.dp
                        } else {
                            12.dp
                        }
                    ).align(Alignment.CenterHorizontally)
                )
            }
        }

        if (meta.contains("overview")) {
            Text(
                meta["overview"]!!.jsonPrimitive.content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp).clickable {
                    overviewDialogContent = meta["overview"]!!.jsonPrimitive.content; overviewDialog = true
                },
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }
        Crossfade(targetState = seasonCarouselState.currentPage) { state ->
            Column(modifier = Modifier.fillMaxSize()) {
                if (seasons.size > state && seasons[state] != null) {
                    if (type == "tv") {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                        ) {
                            IconButton(
                                modifier = Modifier.size(24.dp).padding(start = 8.dp), onClick = {
                                    scope.launch {
                                        seasonCarouselState.animateScrollToPage(seasonCarouselState.currentPage - 1)
                                    }
                                }) {
                                Icon(
                                    Icons.Filled.KeyboardDoubleArrowLeft,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            HorizontalPager(state = seasonCarouselState, modifier = Modifier.weight(1f)) { i ->
                                Text(
                                    text = "Season ${i + 1}",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            IconButton(
                                modifier = Modifier.size(24.dp).padding(end = 8.dp), onClick = {
                                    scope.launch {
                                        seasonCarouselState.animateScrollToPage(seasonCarouselState.currentPage + 1)
                                    }
                                }) {
                                Icon(
                                    Icons.Filled.KeyboardDoubleArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    for (i in 0 until seasons[state]!!["episodes"]!!.jsonArray.size) {
                        val episode = seasons[state]!!["episodes"]!!.jsonArray[i].jsonObject
                        var watched = false
                        if (type == "tv") {
                            if (traktResponse.contains("data")) {
                                for (k in 0 until traktResponse["data"]!!.jsonArray.size) {
                                    val episodeObject =
                                        traktResponse["data"]!!.jsonArray[k].jsonObject["episode"]!!.jsonObject
                                    if (episodeObject["season"]!!.jsonPrimitive.int == state + 1 && episodeObject["number"]!!.jsonPrimitive.int == i + 1) {
                                        watched = true
                                    }
                                }
                            }
                        } else {
                            if (traktResponse.contains("type")) {
                                watched = true
                            }
                        }
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp)
                                .clip(RoundedCornerShape(12.dp)).wrapContentHeight()
                                .background(if (episode.contains("still_path")) Color.Transparent else MaterialTheme.colorScheme.surfaceVariant)
                                .combinedClickable(onClick = {
                                    torrentSelectDialogArgs = TorrentSelectDialogArguments(
                                        state + 1, i + 1, "imdb:${
                                            details["external_ids"]!!.jsonObject["imdb_id"]!!.jsonPrimitive.content
                                        }", type, {
                                            displayTorrentSelectDialog = false
                                        }, navController = navController
                                    )
                                    displayTorrentSelectDialog = true
                                    scope.launch {
                                        if (type == "movie") {
                                            traktApi.addMovie(meta["id"]!!.jsonPrimitive.long)
                                            traktResponse = traktApi.getWatchedMovie(traktId.toLong()) ?: JsonObject(
                                                emptyMap()
                                            )
                                        } else {
                                            traktApi.addShow(meta["id"]!!.jsonPrimitive.long, state + 1, i + 1)
                                            traktResponse = traktApi.getWatchedShow(traktId.toLong()) ?: JsonObject(
                                                emptyMap()
                                            )
                                        }
                                    }
                                }, onLongClick = {
                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                    scope.launch {
                                        if (watched) {
                                            if (type == "movie") {
                                                traktApi.removeMovie(meta["id"]!!.jsonPrimitive.long)
                                                traktResponse =
                                                    traktApi.getWatchedMovie(traktId.toLong()) ?: JsonObject(emptyMap())
                                            } else {
                                                traktApi.removeShow(meta["id"]!!.jsonPrimitive.long, state + 1, i + 1)
                                                traktResponse =
                                                    traktApi.getWatchedShow(traktId.toLong()) ?: JsonObject(emptyMap())
                                            }
                                        } else {
                                            if (type == "movie") {
                                                traktApi.addMovie(meta["id"]!!.jsonPrimitive.long)
                                                traktResponse =
                                                    traktApi.getWatchedMovie(traktId.toLong()) ?: JsonObject(emptyMap())
                                            } else {
                                                traktApi.addShow(meta["id"]!!.jsonPrimitive.long, state + 1, i + 1)
                                                traktResponse =
                                                    traktApi.getWatchedShow(traktId.toLong()) ?: JsonObject(emptyMap())
                                            }
                                        }
                                    }
                                })
                        ) {
                            if (episode.contains("still_path")) {
                                AsyncImage(
                                    model = tmdbApi.imageHelper(
                                    episode["still_path"]!!.jsonPrimitive.content
                                ),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.blur(25.dp).graphicsLayer { alpha = 0.5f }.matchParentSize()
                                        .drawWithContent {
                                            drawContent()
                                            if (watched) {
                                                drawRect(Color.Black.copy(alpha = 0.9f))
                                            }
                                        })

                            }
                            Row(
                                modifier = Modifier.align(Alignment.Center).padding(8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.align(Alignment.CenterVertically).padding(8.dp).weight(1f)
                                ) {
                                    Text(
                                        "${i + 1}. ${episode["name"]!!.jsonPrimitive.content}",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(2.dp),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Spacer(modifier = Modifier.weight(1f))

                                    if (episode.contains("overview")) {
                                        Text(
                                            episode["overview"]!!.jsonPrimitive.content,
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.padding(2.dp),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                                Column(
                                    modifier = Modifier.align(Alignment.CenterVertically).padding(end = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.align(Alignment.CenterHorizontally),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Filled.Timer,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(12.dp).padding(end = 2.dp)
                                        )
                                        Text(
                                            "${episode["runtime"]!!.jsonPrimitive.intOrNull ?: "Unknown "}m",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    if (watched) {
                                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                            Icon(
                                                Icons.Filled.CheckCircle,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                    }
                } else {
                    Column(Modifier.fillMaxSize().padding(top = 16.dp)) {
                        LoadingScreen()
                    }
                }
            }
        }
    }
    if (overviewDialog) {
        Dialog(
            onDismissRequest = { overviewDialog = false }) {
            Box(
                modifier = Modifier.fillMaxSize().padding(16.dp).clip(RoundedCornerShape(12.dp))
                    .clickable(interactionSource = null, indication = null) { overviewDialog = false }) {
                Card(
                    modifier = Modifier.align(Alignment.Center)
                        .clickable(interactionSource = null, indication = null) { }) {
                    SelectionContainer {
                        Text(
                            overviewDialogContent,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

            }
        }
    }
    if (displayTorrentSelectDialog) {
        TorrentSelectDialog(torrentSelectDialogArgs)
    }

}