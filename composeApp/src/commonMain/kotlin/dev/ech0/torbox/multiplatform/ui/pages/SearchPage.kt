package dev.ech0.torbox.multiplatform.ui.pages

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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.russhwolf.settings.Settings
import dev.ech0.torbox.multiplatform.LocalNavController
import dev.ech0.torbox.multiplatform.LocalSnackbarHostState
import dev.ech0.torbox.multiplatform.api.torboxAPI
import dev.ech0.torbox.multiplatform.ui.components.LoadingScreen
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import dev.ech0.torbox.multiplatform.ui.components.SearchItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage() {
    val navController = LocalNavController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var textFieldState by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<JsonObject>>(emptyList()) }
    var shouldLoad by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val topBar = Settings().getBoolean("searchTop", false)
    val snackbarHostState = LocalSnackbarHostState.current
    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }
    Column(
        modifier = Modifier.fillMaxSize().blur(if(shouldLoad){10.dp}else{0.dp})
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
                    query = textFieldState,
                    onQueryChange = {
                        textFieldState = it
                    },
                    onSearch = {
                        focusManager.clearFocus()
                        scope.launch {
                            try{
                                if(textFieldState != ""){
                                    shouldLoad = true
                                    torboxAPI
                                    var data =
                                        torboxAPI.searchTorrents(textFieldState)["data"]!!.jsonObject["torrents"]!!.jsonArray
                                    results = List(data.size) { index -> data[index].jsonObject}
                                    if(Settings().getInt("plan", 4) == 2 && Settings().getBoolean("usenet", true)){
                                        data = torboxAPI.searchUsenet(textFieldState)["data"]!!.jsonObject["nzbs"]!!.jsonArray
                                        results = results.plus(List(data.size) { index -> data[index].jsonObject })
                                        results = results.shuffled()
                                    }
                                    shouldLoad = false
                                }else{
                                    snackbarHostState.showSnackbar("Search for something, you goober.")
                                }
                            }catch(e: Exception){
                                navController.navigate("Error/${e.toString().encodeURLPath()}")
                            }
                        }
                    },
                    expanded = false,
                    onExpandedChange = { },
                    placeholder = { Text("Search away, matey.") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
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