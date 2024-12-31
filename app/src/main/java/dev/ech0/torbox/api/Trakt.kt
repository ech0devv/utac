package dev.ech0.torbox.api

import android.content.SharedPreferences
import android.util.Log
import dev.ech0.torbox.BuildConfig
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

lateinit var traktApi: Trakt

class Trakt(var preferences: SharedPreferences) {
    private val base = "https://api.trakt.tv/"

    private val ktor = HttpClient(OkHttp) {}

    private lateinit var token: String

    init {
        if (preferences.contains("traktToken")) {
            GlobalScope.launch(Dispatchers.IO){
                getAccToken().let {
                    token = it.getString("access_token")
                }
            }
        }
    }

    suspend fun getAuthCode(): JSONObject {
        val response = ktor.post(base + "oauth/device/code") {
            setBody(JSONObject().put("client_id", BuildConfig.TRAKT_KEY).toString())
            headers {
                append(HttpHeaders.ContentType, "application/json")
            }
        }
        val json = JSONObject(response.bodyAsText())
        return json
    }

    suspend fun getRefreshToken(code: String): String {
        val response = ktor.post(base + "oauth/device/token") {
            setBody(
                JSONObject().put("client_id", BuildConfig.TRAKT_KEY).put("client_secret", BuildConfig.TRAKT_SECRET)
                    .put("code", code).toString()
            )
            headers {
                append(HttpHeaders.ContentType, "application/json")
            }
        }
        if (response.status.value == 200) {
            val json = JSONObject(response.bodyAsText())
            return json.getString("refresh_token")
        } else if (response.status.value == 400) {
            return ""
        } else {
            throw IOException("Trakt API Error")
        }
    }

    private suspend fun getAccToken(): JSONObject {
        val refreshToken = preferences.getString("traktToken", "")
        if (refreshToken != "") {
            val response = ktor.post(base + "oauth/token") {
                setBody(
                    JSONObject().put("client_id", BuildConfig.TRAKT_KEY)
                        .put("client_secret", BuildConfig.TRAKT_SECRET)
                        .put("refresh_token", refreshToken)
                        .put("redirect_uri", "urn:ietf:wg:oauth:2.0:oob")
                        .put("grant_type", "refresh_token").toString()
                )
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                }
            }
            val json = JSONObject(response.bodyAsText())
            return json
        } else {
            return JSONObject()
        }
    }
}