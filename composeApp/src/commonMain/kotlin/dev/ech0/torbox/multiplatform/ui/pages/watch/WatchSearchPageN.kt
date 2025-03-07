package dev.ech0.torbox.multiplatform.ui.pages.watch

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.russhwolf.settings.Settings
import dev.ech0.torbox.multiplatform.api.tmdbApi
import dev.ech0.torbox.multiplatform.ui.components.WatchSearchListItemN
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


enum class WatchSearchResultType {
    MOVIE, TV, ANIME
}

enum class WatchSearchResultProvider {
    TMDB, KITSU
}

data class WatchSearchResult(
    val provider: WatchSearchResultProvider,
    val id: Long,
    val type: WatchSearchResultType,
    val title: String,
    val summary: String?,
    val poster: String?
)

data class Episode(
    val title: String,
    val number: Int,
    val summary: String? = "",
    val freezeFrame: String? = "",
)

data class Season(
    val number: Int, val episodes: List<Episode>
)

data class WatchSearchResultExpanded(
    val base: WatchSearchResult, val seasons: List<Season>, val cover: String = ""
)

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun WatchSearchPageN() {
    var results by remember { mutableStateOf(mutableListOf<WatchSearchResult>()) }
    val scope = rememberCoroutineScope()
    var textFieldState by remember { mutableStateOf("") }
    var page by remember { mutableStateOf(1) }
    var selectedData by remember {
        mutableStateOf(
            WatchSearchResult(
                WatchSearchResultProvider.TMDB,
                0,
                WatchSearchResultType.MOVIE,
                "",
                "",
                ""
            )
        )
    }
    BackHandler(enabled = page != 1) {
        page = 1
    }
    if (page == 1) {
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun WatchSearchBar(topBar: Boolean) {
            val focusRequester = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current
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
                                    if (textFieldState != "") {
                                        val unsorted = mutableListOf<WatchSearchResult>()
                                        tmdbApi.search(textFieldState).forEach { it ->
                                            val data = it.jsonObject
                                            if (data["media_type"]!!.jsonPrimitive.content.uppercase() == "MOVIE" || data["media_type"]!!.jsonPrimitive.content.uppercase() == "TV") {
                                                unsorted.add(
                                                    WatchSearchResult(
                                                        provider = WatchSearchResultProvider.TMDB,
                                                        id = data["id"]!!.jsonPrimitive.content.toLong(),
                                                        type = WatchSearchResultType.valueOf(data["media_type"]!!.jsonPrimitive.content.uppercase()),
                                                        title = (data["name"]?.jsonPrimitive?.content
                                                            ?: data["title"]?.jsonPrimitive?.content
                                                            ?: ""),
                                                        summary = data["overview"]?.jsonPrimitive?.content,
                                                        poster = tmdbApi.imageHelper(it.jsonObject["poster_path"]!!.jsonPrimitive.content),
                                                    )
                                                )
                                            }

                                        }
                                        results = unsorted.sortedByDescending {
                                            var score = 0.0
                                            if (it.title.lowercase() == textFieldState.toString()
                                                    .lowercase()
                                            ) {
                                                score = Double.MAX_VALUE
                                            }
                                            if (it.title == "") {
                                                score = Double.MIN_VALUE
                                            }
                                            if (it.summary?.isNotBlank() == true) {
                                                score += 2
                                            }
                                            if (it.poster?.isNotBlank() == true) {
                                                score += 1
                                            }
                                            score
                                        }.toMutableList()
                                    }
                                }
                            },
                            expanded = false,
                            onExpandedChange = { },
                            placeholder = { Text("Search away, matey. Courtesy of TMDB") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },

                            )
                    },
                    modifier = if (topBar) {
                        Modifier.focusRequester(focusRequester).fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    } else {
                        Modifier.focusRequester(focusRequester).fillMaxWidth()
                            .padding(top = 0.dp, bottom = 0.dp)
                    },
                    shape = if (topBar) {
                        RoundedCornerShape(25.dp, 25.dp, 25.dp, 25.dp)
                    } else {
                        RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp)
                    },
                    expanded = false,
                    onExpandedChange = { },
                    windowInsets = WindowInsets(top = 0.dp)
                ) {}
            }
        }

        val topBar = Settings().getBoolean("searchTop", false)
        Scaffold(topBar = { if (topBar) WatchSearchBar(topBar) },
            bottomBar = { if (!topBar) WatchSearchBar(topBar) },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                items(results) {
                    WatchSearchListItemN(it, onClick = { page = 2; selectedData = it })
                }
            }
        }
    } else if (page == 2) {
        WatchPageN(selectedData)
    }

}