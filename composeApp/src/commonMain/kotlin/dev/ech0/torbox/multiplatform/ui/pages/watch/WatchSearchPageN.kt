package dev.ech0.torbox.multiplatform.ui.pages.watch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.russhwolf.settings.Settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchSearchBar(topBar: Boolean) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var textFieldState by remember { mutableStateOf("") }
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
                    },
                    expanded = false,
                    onExpandedChange = { },
                    placeholder = { Text("Search away, matey. Courtesy of TMDB") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },

                    )
            }, modifier = if (topBar) {
                Modifier.focusRequester(focusRequester).fillMaxWidth().padding(horizontal = 10.dp)
            } else {
                Modifier.focusRequester(focusRequester).fillMaxWidth()
                    .padding(top = 0.dp, bottom = 0.dp)
            }, shape = if (topBar) {
                RoundedCornerShape(25.dp, 25.dp, 25.dp, 25.dp)
            } else {
                RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp)
            }, expanded = false, onExpandedChange = { }, windowInsets = WindowInsets(top = 0.dp)
        ) {}
    }
}

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
    val summary: String,
    val poster: String
)
data class Episode(
    val title: String,
    val number: Int,
    val summary: String = "",
    val freezeFrame: String = "",
)
data class Season(
    val number: Int,
    val episodes: List<Episode>
)
class WatchSearchResultExpanded(
    val base: WatchSearchResult,
    val seasons: List<Season>,
    val cover: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchSearchPageN() {
    val topBar = Settings().getBoolean("searchTop", false)
    Scaffold(topBar = { if (topBar) WatchSearchBar(topBar) },
        bottomBar = { if (!topBar) WatchSearchBar(topBar) }) {

    }

}