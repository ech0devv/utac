package dev.ech0.torbox.multiplatform.ui.pages.watch

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.russhwolf.settings.Settings
import dev.ech0.torbox.multiplatform.LocalNavController
import dev.ech0.torbox.multiplatform.api.tmdbApi
import dev.ech0.torbox.multiplatform.ui.components.LoadingScreen
import dev.ech0.torbox.multiplatform.ui.components.WatchListItem
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchSearchPage() {
    val navController = LocalNavController.current
    var textFieldState by remember { mutableStateOf("") }
    val topBar = Settings().getBoolean("searchTop", false)
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var shouldLoad by remember { mutableStateOf(false) }
    var results by remember { mutableStateOf<List<JsonObject>>(emptyList()) }
    val scope = rememberCoroutineScope()
    var page by remember { mutableStateOf("") }
    var meta by remember { mutableStateOf(JsonObject(emptyMap())) }
    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }/* TODO: BackHandler(enabled = page != "") {
        page = ""
    }*/
    Crossfade(page) { state ->
        if (state == "") {
            Column(modifier = Modifier.blur(if (shouldLoad) 10.dp else 0.dp).fillMaxSize()) {
                if (!topBar) {
                    LazyColumn(modifier = Modifier.fillMaxSize().weight(1f).graphicsLayer { alpha = 0.99F }
                        .drawWithContent {
                            val colors = listOf(
                                Color.Black, Color.Black, Color.Black, Color.Transparent
                            )
                            drawContent()
                            drawRect(
                                brush = Brush.verticalGradient(colors), blendMode = BlendMode.DstIn
                            )
                        }) {
                        items(results) { result ->
                            WatchListItem(result, { page = it }, { meta = it; })
                        }
                    }
                }
                Column {
                    SearchBar(
                        inputField = {
                            SearchBarDefaults.InputField(
                                query = textFieldState,
                                onQueryChange = {
                                    textFieldState = it
                                },
                                onSearch = {
                                    focusManager.clearFocus()
                                    scope.launch {
                                        val data = tmdbApi.search(textFieldState.toString())
                                        results = List(data.size) { index -> data[index].jsonObject }.filter {
                                            (it.contains("name") || it.contains("title")) && (it["media_type"]!!.jsonPrimitive.content == "tv" || it["media_type"]!!.jsonPrimitive.content == "movie")
                                        }.sortedByDescending {
                                            var score = 0.0
                                            if ((it["name"] ?: it["title"] ?: "").toString()
                                                    .lowercase() == textFieldState.toString().lowercase()
                                            ) {
                                                score = Double.MAX_VALUE
                                            }
                                            if (it.contains("overview") && it["overview"]!!.jsonPrimitive.content
                                                    .isNotBlank()
                                            ) {
                                                score += 2
                                            }
                                            if (it.contains("poster_path") && it["poster_path"]!!.jsonPrimitive.content
                                                    .isNotBlank()
                                            ) {
                                                score += 1
                                            }
                                            if (it.contains("popularity")) {
                                                score += it["popularity"]!!.jsonPrimitive.double
                                            }
                                            score
                                        }
                                        shouldLoad = false
                                    }
                                },
                                expanded = false,
                                onExpandedChange = { },
                                placeholder = { Text("Search away, matey. Courtesy of TMDB") },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },

                                )
                        }, modifier = if (topBar) {
                            Modifier.focusRequester(focusRequester).fillMaxWidth().padding(horizontal = 10.dp)
                        } else {
                            Modifier.focusRequester(focusRequester).fillMaxWidth().padding(top = 0.dp, bottom = 0.dp)
                        }, shape = if (topBar) {
                            RoundedCornerShape(25.dp, 25.dp, 25.dp, 25.dp)
                        } else {
                            RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp)
                        }, expanded = false, onExpandedChange = { }, windowInsets = WindowInsets(top = 0.dp)
                    ) {}
                }
                if (topBar) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().weight(1f)
                    ) {
                        items(results) { result ->
                            if (!result["id"]!!.jsonPrimitive.content.startsWith("/")) {
                                WatchListItem(result, { page = it }, { meta = it; })
                            }
                        }
                    }
                }
            }
            if (shouldLoad) {
                LoadingScreen()
            }
        } else {
           WatchPage(meta, navController)
        }
    }

}