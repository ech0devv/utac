package dev.ech0.torbox.ui.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.preference.PreferenceManager
import dev.ech0.torbox.R
import dev.ech0.torbox.api.Trakt
import dev.ech0.torbox.api.traktApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun TraktPrompt(dismiss: () -> Unit, navController: NavController) {
    var context = LocalContext.current
    val preferences = remember { PreferenceManager.getDefaultSharedPreferences(context) }
    var shouldLoad by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var traktResponseJSON by remember { mutableStateOf(JSONObject()) }
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
                val resp = traktApi.getRefreshToken(traktResponseJSON.getString("device_code"))
                if (resp != "") {
                    preferences.edit().putString("traktToken", resp).apply()
                    traktApi = Trakt(preferences)
                    dismiss()
                    traktResponded = false
                }
                delay(traktResponseJSON.getInt("interval") * 1000L)
            }
        }
    }
    Dialog(onDismissRequest = dismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .wrapContentSize(),
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painterResource(R.drawable.trakt), "Input API Key", modifier = Modifier.padding(bottom = 0.dp)
                )
                Text(
                    "Log in to Trakt",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
                if (traktResponded) {
                    SelectionContainer {
                        Text(
                            traktResponseJSON.getString("user_code"),
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(top = 16.dp)
                        )

                    }
                    SelectionContainer {
                        Text(
                            "Go to ${traktResponseJSON.getString("verification_url")} and enter the above code, or click this button",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                    Button(onClick = {
                        uriHandler.openUri(
                            traktResponseJSON.getString("verification_url") + "/${
                                traktResponseJSON.getString(
                                    "user_code"
                                )
                            }"
                        )
                    }, modifier = Modifier.padding(top = 16.dp)) {
                        Text("Copy & Open Trakt")
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .zIndex(2f)
                    ) {
                        LoadingScreen()
                    }
                }
            }

        }
    }
}
