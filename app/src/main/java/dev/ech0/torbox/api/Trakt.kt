package dev.ech0.torbox.api

import android.content.SharedPreferences
import android.util.Log
import dev.ech0.torbox.BuildConfig
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import kotlin.time.Duration.Companion.seconds

lateinit var traktApi: Trakt

class Trakt(var preferences: SharedPreferences) {
    private val base = "https://api.trakt.tv/"

    private val ktor = HttpClient(OkHttp) {
    }

    private lateinit var token: String
    private var lastConnect = 0L
    private suspend fun throttle(){
        val now = System.currentTimeMillis()
        if (now - lastConnect < 1000) {
            delay(1000 - (now - lastConnect))
        }
        lastConnect = System.currentTimeMillis()
    }
    init {
        if (preferences.getString("traktToken", "") != "") {
            GlobalScope.launch(Dispatchers.IO) {
                getAccToken().let {
                    if (it.has("error")) {
                        preferences.edit().remove("traktToken").apply()
                        traktApi = Trakt(preferences)
                    } else {
                        token = it.getString("access_token")
                        preferences.edit().putString("traktToken", it.getString("refresh_token")).apply()
                    }
                }
            }
        }
    }

    suspend fun getAuthCode(): JSONObject {
        throttle()
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
        throttle()
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
        throttle()
        val refreshToken = preferences.getString("traktToken", "")
        if (refreshToken != "") {
            val result: JSONObject = flow {
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
                emit(json)
            }.retry(5) {
                delay(1000)
                true
            }.single()
            return result
        } else {
            return JSONObject()
        }
    }

    suspend fun addShow(id: Long, season: Int, episode: Int) {
        throttle()
        val response = ktor.post(base + "sync/history") {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, "Bearer $token")
                append("trakt-api-key", BuildConfig.TRAKT_KEY)
                append("trakt-api-version", "2")
            }
            setBody(
                JSONObject(
                    """
                    {
                        "shows": [
                            {
                                "ids": {
                                    "tmdb": $id
                                },
                                "seasons": [
                                    {
                                        "number": $season,
                                        "episodes": [
                                            {
                                                "number": $episode
                                            }
                                        ]
                                    }
                                ]
                            }
                        ]
                    }
                """.trimIndent()
                ).toString()
            )
        }
        val json = JSONObject(response.bodyAsText())
    }

    suspend fun addMovie(id: Long) {
        throttle()
        val response = ktor.post(base + "sync/history") {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, "Bearer $token")
                append("trakt-api-key", BuildConfig.TRAKT_KEY)
                append("trakt-api-version", "2")
            }
            setBody(
                JSONObject(
                    """
                    {
                        "movies": [
                            {
                                "ids": {
                                    "tmdb": $id
                                }
                            }
                        ]
                    }
                """.trimIndent()
                ).toString()
            )
        }
        val json = JSONObject(response.bodyAsText())
    }
    suspend fun removeShow(id: Long, season: Int, episode: Int) {
        throttle()
        val response = ktor.post(base + "sync/history/remove") {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, "Bearer $token")
                append("trakt-api-key", BuildConfig.TRAKT_KEY)
                append("trakt-api-version", "2")
            }
            setBody(
                JSONObject(
                    """
                    {
                        "shows": [
                            {
                                "ids": {
                                    "tmdb": $id
                                },
                                "seasons": [
                                    {
                                        "number": $season,
                                        "episodes": [
                                            {
                                                "number": $episode
                                            }
                                        ]
                                    }
                                ]
                            }
                        ]
                    }
                """.trimIndent()
                ).toString()
            )
        }
        val json = JSONObject(response.bodyAsText())
    }

    suspend fun removeMovie(id: Long) {
        throttle()
        val response = ktor.post(base + "sync/history/remove") {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, "Bearer $token")
                append("trakt-api-key", BuildConfig.TRAKT_KEY)
                append("trakt-api-version", "2")
            }
            setBody(
                JSONObject(
                    """
                    {
                        "movies": [
                            {
                                "ids": {
                                    "tmdb": $id
                                }
                            }
                        ]
                    }
                """.trimIndent()
                ).toString()
            )
        }
        val json = JSONObject(response.bodyAsText())
    }

    suspend fun getWatchedShow(traktId: Long): JSONObject? {
        throttle()
        val response = ktor.get(base + "sync/history/shows/$traktId") {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, "Bearer $token")
                append("trakt-api-key", BuildConfig.TRAKT_KEY)
                append("trakt-api-version", "2")
            }
        }
        try{
            val json = JSONArray(response.bodyAsText())
            return JSONObject().put("data", json)
        }catch(e: Exception) {
            return null
        }
    }

    suspend fun getWatchedMovie(traktId: Long): JSONObject? {
        throttle()
        val response = ktor.get(base + "sync/history/movies/$traktId") {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, "Bearer $token")
                append("trakt-api-key", BuildConfig.TRAKT_KEY)
                append("trakt-api-version", "2")
            }
        }

        try{
            val json = JSONArray(response.bodyAsText())
            return json.optJSONObject(0) ?: null
        }catch(e: Exception) {
            return null
        }
    }

    suspend fun getTraktIdFromTMDB(id: Long): Int {
        throttle()
        val response = ktor.get(base + "search/tmdb/$id") {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, "Bearer $token")
                append("trakt-api-key", BuildConfig.TRAKT_KEY)
                append("trakt-api-version", "2")
            }
        }
        val json = JSONArray(response.bodyAsText()).getJSONObject(0)
        return json.getJSONObject(json.getString("type")).getJSONObject("ids").getInt("trakt")
    }
}