package dev.ech0.torbox

import android.net.Uri
import androidx.navigation.NavController
import org.json.JSONObject


fun<T> execute(navController: NavController, action: () -> T): T? {
    return try {
        action()
    } catch (e: Exception) {
        navController.navigate("Error/${Uri.encode(e.toString())}")
        null
    }
}
// https://stackoverflow.com/a/23377941
fun optString(json: JSONObject, key: String?): String? {
    // http://code.google.com/p/android/issues/detail?id=13830
    return if (json.isNull(key)) null
    else json.optString(key, null)
}