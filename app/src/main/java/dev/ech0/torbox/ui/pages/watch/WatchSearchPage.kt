package dev.ech0.torbox.ui.pages.watch

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager
import dev.ech0.torbox.LocalNavController
import dev.ech0.torbox.api.tmdbApi
import dev.ech0.torbox.optString
import dev.ech0.torbox.ui.components.LoadingScreen
import dev.ech0.torbox.ui.components.WatchListItem
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchSearchPage() {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val preferences = remember { PreferenceManager.getDefaultSharedPreferences(context) }
    val textFieldState = rememberTextFieldState()
    val topBar = preferences.getBoolean("searchTop", false)
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var shouldLoad by remember { mutableStateOf(false) }
    var results by remember { mutableStateOf<List<JSONObject>>(emptyList()) }
    val scope = rememberCoroutineScope()
    var page by remember { mutableStateOf("") }
    var meta by remember { mutableStateOf(JSONObject()) }
    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }
    BackHandler(enabled = page != "") {
        page = ""
    }
    Crossfade(page) { state ->
        if (state == "") {
            Column(
                modifier = Modifier
                    .graphicsLayer {
                        if (shouldLoad) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                renderEffect = RenderEffect
                                    .createBlurEffect(25f, 25f, Shader.TileMode.MIRROR)
                                    .asComposeRenderEffect()
                            }
                        }
                    }
                    .fillMaxSize()) {
                if (!topBar) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .graphicsLayer { alpha = 0.99F }
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
                                state = textFieldState,
                                onSearch = {
                                    focusManager.clearFocus()
                                    scope.launch {

                                        shouldLoad = true
                                        val data = tmdbApi.search(textFieldState.text.toString())
                                        results = List(data.length()) { index -> data.getJSONObject(index) }.filter {
                                            (it.has("name") || it.has("title")) && (it.getString("media_type") == "tv" || it.getString("media_type") == "movie")
                                        }.sortedByDescending {
                                            var score = 0.0
                                            if ((optString(it, "name") ?: optString(it, "title") ?: "").equals(
                                                    textFieldState.text.toString(), ignoreCase = true
                                                )
                                            ) {
                                                score = Double.MAX_VALUE
                                            }
                                            if (it.has("overview") && it.getString("overview").isNotBlank()) {
                                                score += 2
                                            }
                                            if (it.has("poster_path") && it.optString("poster_Path").isNotBlank()) {
                                                score += 1
                                            }
                                            if (it.has("popularity")) {
                                                score += it.getDouble("popularity")
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
                            Modifier
                                .focusRequester(focusRequester)
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        } else {
                            Modifier
                                .focusRequester(focusRequester)
                                .fillMaxWidth()
                                .imePadding()
                                .padding(top = 0.dp)
                        }, shape = if (topBar) {
                            RoundedCornerShape(25.dp, 25.dp, 25.dp, 25.dp)
                        } else {
                            RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp)
                        }, expanded = false, onExpandedChange = { }, windowInsets = WindowInsets(top = 0.dp)
                    ) {}
                }
                if (topBar) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        items(results) { result ->
                            if (!result.getString("id").startsWith("/")) {
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