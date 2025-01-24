package dev.ech0.torbox.multiplatform.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.russhwolf.settings.Settings
import dev.ech0.torbox.multiplatform.api.Trakt
import dev.ech0.torbox.multiplatform.api.traktApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun TraktPrompt(dismiss: () -> Unit, navController: NavController) {
    var shouldLoad by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var traktResponseJSON by remember { mutableStateOf(JsonObject(emptyMap())) }
    var traktResponded by remember { mutableStateOf(false) }
    var complete by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(true) {
        scope.launch {
            traktResponseJSON = traktApi.getAuthCode()
            traktResponded = true
        }
    }
    LaunchedEffect(traktResponded) {
        if (traktResponded) {
            while (!complete) {
                val resp = traktApi.getRefreshToken(traktResponseJSON["device_code"]!!.jsonPrimitive.content)
                if (resp != "") {
                    Settings().putString("traktToken", resp)
                    traktApi = Trakt()
                    dismiss()
                    traktResponded = false
                }
                delay(traktResponseJSON["interval"]!!.jsonPrimitive.int * 1000L)
            }
        }
    }
    Dialog(onDismissRequest = dismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp).wrapContentSize(),
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                /* TODO: Icon(
                    painterResource(R.drawable.trakt), "Input API Key", modifier = Modifier.padding(bottom = 0.dp)
                )*/
                Text(
                    "Log in to Trakt",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
                if (traktResponded) {
                    SelectionContainer {
                        Text(
                            traktResponseJSON["user_code"]!!.jsonPrimitive.content,
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(top = 16.dp)
                        )

                    }
                    SelectionContainer {
                        Text(
                            "Go to ${traktResponseJSON["verification_url"]!!.jsonPrimitive.content} and enter the above code, or click this button",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                    Button(onClick = {
                        uriHandler.openUri(
                            traktResponseJSON["verification_url"]!!.jsonPrimitive.content + "/${
                                traktResponseJSON["user_code"]!!.jsonPrimitive.content
                            }"
                        )
                    }, modifier = Modifier.padding(top = 16.dp)) {
                        Text("Copy & Open Trakt")
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min).zIndex(2f)
                    ) {
                        LoadingScreen()
                    }
                }
            }

        }
    }
}
