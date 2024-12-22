package dev.ech0.torbox.ui.pages

import android.graphics.RenderEffect
import android.graphics.Shader
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.preference.PreferenceManager
import dev.ech0.torbox.LocalNavController
import dev.ech0.torbox.api.torboxAPI
import dev.ech0.torbox.ui.components.LoadingScreen
import dev.ech0.torbox.ui.components.SearchItem
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage() {
    val navController = LocalNavController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val textFieldState = rememberTextFieldState()
    var results by remember { mutableStateOf<List<JSONObject>>(emptyList()) }
    var shouldLoad by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val preferences = remember { PreferenceManager.getDefaultSharedPreferences(context) }
    val topBar = preferences.getBoolean("searchTop", false)
    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }
    Column(
        modifier = Modifier.graphicsLayer {
            if (shouldLoad) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                    renderEffect = RenderEffect
                        .createBlurEffect(25f, 25f, Shader.TileMode.MIRROR)
                        .asComposeRenderEffect()
                }
            }
        }.fillMaxSize()
    ){
        if(!topBar){
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize().weight(1f)
            ) {
                items(results) { result ->
                    SearchItem(result, {shouldLoad = it}, navController)
                }
            }
        }
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    state = textFieldState,
                    onSearch = {
                        focusManager.clearFocus()
                        scope.launch {
                            try{
                                shouldLoad = true
                                val data =
                                    torboxAPI.searchTorrents(textFieldState.text.toString()).getJSONObject("data")
                                        .getJSONArray("torrents")
                                results = List(data.length()) { index -> data.getJSONObject(index) }
                                shouldLoad = false
                            }catch(e: Exception){
                                navController.navigate("Error/${Uri.encode(e.toString())}")
                            }
                        }
                    },
                    expanded = false,
                    onExpandedChange = { },
                    placeholder = { Text("Search away, matey.") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },

                    )
            },
            modifier = if(topBar){
                Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            }else{
                Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
                    .imePadding()
            },
            shape = if (topBar) {
                RoundedCornerShape(25.dp, 25.dp, 25.dp, 25.dp)
            } else {
                RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp)
            },
            expanded = false,
            onExpandedChange = { },
        ) {}
        if(topBar){
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize().weight(1f).padding(top = 12.dp)
            ) {
                items(results) { result ->
                    SearchItem(result, {shouldLoad = it}, navController)
                }
            }
        }
    }

    if (shouldLoad) {
        LoadingScreen()
    }
}