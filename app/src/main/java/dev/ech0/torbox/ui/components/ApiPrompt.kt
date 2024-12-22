package dev.ech0.torbox.ui.components

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.preference.PreferenceManager
import dev.ech0.torbox.api.torboxAPI
import kotlinx.coroutines.launch

@Composable
fun ApiPrompt(dismiss: () -> Unit, navController: NavController) {
    var apiKeySet by remember { mutableStateOf("") }
    var showApikey by remember { mutableStateOf(false) }
    var context = LocalContext.current
    val preferences = remember { PreferenceManager.getDefaultSharedPreferences(context) }
    var shouldLoad by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Dialog(onDismissRequest = dismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .wrapContentSize(),

            ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Filled.Password, "Input API Key", modifier = Modifier.padding(bottom = 0.dp)
                )
                Text(
                    "Input API Key",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )

                OutlinedTextField(
                    modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
                    value = apiKeySet,
                    singleLine = true,
                    visualTransformation = if (showApikey) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    onValueChange = { newVal ->
                        apiKeySet = newVal

                    },
                    trailingIcon = {
                        IconButton(content = {
                            if (showApikey) {
                                Icon(Icons.Filled.VisibilityOff, null)
                            } else {
                                Icon(Icons.Filled.Visibility, null)
                            }
                        }, onClick = { showApikey = !showApikey })
                    })
                Text(
                    "Your API Key will be stored locally and sent only to Torbox servers, nowhere else.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                if (!shouldLoad) {
                    Button(onClick = {
                        scope.launch {
                            try {
                                shouldLoad = true
                                torboxAPI.setApiKey(apiKeySet)
                                if (torboxAPI.checkApiKey(apiKeySet, context)) {
                                    preferences.edit().putString("apiKey", apiKeySet).apply()
                                } else {
                                    torboxAPI.setApiKey("__")
                                    navController.navigate("Error/${Uri.encode("Invalid API Key")}")
                                }
                                shouldLoad = false
                                dismiss()
                            } catch (e: Exception) {
                                navController.navigate("Error/${Uri.encode(e.toString())}")
                            }
                        }
                    }) {
                        Text("Submit")
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .zIndex(2f) // Ensure this box is above other content
                    ) {
                        LoadingScreen()
                    }
                }

            }

        }
    }
}
