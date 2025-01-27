package dev.ech0.torbox.multiplatform.api

import com.russhwolf.settings.Settings
import dev.ech0.torbox.multiplatform.BuildConfig
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.single
import kotlinx.datetime.Clock
import kotlinx.io.IOException
import kotlinx.serialization.json.*

lateinit var traktApi: Trakt

class Trakt {
    private val base = "https://api.trakt.tv/"

    private val ktor = HttpClient {}

    private lateinit var token: String
    private var lastConnect = 0L
    private suspend fun throttle() {
        val now = Clock.System.now().toEpochMilliseconds()
        if (now - lastConnect < 1000) {
            delay(1000 - (now - lastConnect))
        }
        lastConnect = Clock.System.now().toEpochMilliseconds()
    }

    private var loggedIn = false

    init {
        if (Settings().getString("traktToken", "") != "") {
            GlobalScope.launch(Dispatchers.IO) {
                getAccToken().let {
                    if (it.contains("error")) {
                        Settings().remove("traktToken")
                        traktApi = Trakt()
                    } else {
                        token = it["access_token"]!!.jsonPrimitive.content
                        Settings().putString("traktToken", it["refresh_token"]!!.jsonPrimitive.content)
                        loggedIn = true
                    }
                }
            }
        } else {
            loggedIn = false
        }

    }

    suspend fun getAuthCode(): JsonObject {
        throttle()
        val response = ktor.post(base + "oauth/device/code") {
            setBody(JsonObject(mapOf(Pair("client_id", JsonPrimitive(BuildConfig.TRAKT_KEY)))))
            headers {
                append(HttpHeaders.ContentType, "application/json")
            }
        }
        val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        Json.parseToJsonElement(response.bodyAsText()).jsonObject
        return json
    }

    suspend fun getRefreshToken(code: String): String {
        throttle()
        val response = ktor.post(base + "oauth/device/token") {
            setBody(
                JsonObject(
                    mapOf(
                        Pair("client_id", JsonPrimitive(BuildConfig.TRAKT_KEY)),
                        Pair("client_secret", JsonPrimitive(BuildConfig.TRAKT_SECRET)),
                        Pair("code", JsonPrimitive(code))
                    )
                ).toString()
            )
            headers {
                append(HttpHeaders.ContentType, "application/json")
            }
        }
        if (response.status.value == 200) {
            val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
            return json["refresh_token"]!!.jsonPrimitive.content
        } else if (response.status.value == 400) {
            return ""
        } else {
            throw IOException("Trakt API Error")
        }
    }

    private suspend fun getAccToken(): JsonObject {
        throttle()
        val refreshToken = Settings().getString("traktToken", "")
        if (refreshToken != "") {
            val result: JsonObject = flow {
                val response = ktor.post(base + "oauth/token") {
                    setBody(
                        JsonObject(
                            mapOf(
                                Pair("client_id", JsonPrimitive(BuildConfig.TRAKT_KEY)),
                                Pair("client_secret", JsonPrimitive(BuildConfig.TRAKT_SECRET)),
                                Pair("refresh_token", JsonPrimitive(refreshToken)),
                                Pair("redirect_uri", JsonPrimitive("urn:ietf:wg:oauth:2.0:oob")),
                                Pair("grant_type", JsonPrimitive("refresh_token"))
                            )
                        ).toString()
                    )
                    headers {
                        append(HttpHeaders.ContentType, "application/json")
                    }
                }
                val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
                emit(json)
            }.retry(5) {
                delay(1000)
                true
            }.single()
            return result
        } else {
            return Json.parseToJsonElement("{}").jsonObject
        }
    }

    suspend fun addShow(id: Long, season: Int, episode: Int) {
        if (loggedIn) {
            throttle()
            val response = ktor.post(base + "sync/history") {
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                    append(HttpHeaders.Authorization, "Bearer $token")
                    append("trakt-api-key", BuildConfig.TRAKT_KEY)
                    append("trakt-api-version", "2")
                }
                setBody(
                    Json.parseToJsonElement(
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
                    ).jsonObject.toString()
                )
            }
            val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        }
    }

    suspend fun addMovie(id: Long) {
        if (loggedIn) {
            throttle()
            val response = ktor.post(base + "sync/history") {
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                    append(HttpHeaders.Authorization, "Bearer $token")
                    append("trakt-api-key", BuildConfig.TRAKT_KEY)
                    append("trakt-api-version", "2")
                }
                setBody(
                    Json.parseToJsonElement(
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
                    ).jsonObject.toString()
                )
            }
            val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        }
    }

    suspend fun removeShow(id: Long, season: Int, episode: Int) {
        if (loggedIn) {

            throttle()
            val response = ktor.post(base + "sync/history/remove") {
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                    append(HttpHeaders.Authorization, "Bearer $token")
                    append("trakt-api-key", BuildConfig.TRAKT_KEY)
                    append("trakt-api-version", "2")
                }
                setBody(
                    Json.parseToJsonElement(
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
                    ).jsonObject.toString()
                )
            }
            val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        }
    }

    suspend fun removeMovie(id: Long) {
        if (loggedIn) {

            throttle()
            val response = ktor.post(base + "sync/history/remove") {
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                    append(HttpHeaders.Authorization, "Bearer $token")
                    append("trakt-api-key", BuildConfig.TRAKT_KEY)
                    append("trakt-api-version", "2")
                }
                setBody(
                    Json.parseToJsonElement(
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
                    ).jsonObject.toString()
                )
            }
            val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        }
    }

    suspend fun getWatchedShow(traktId: Long): JsonObject? {
        if (loggedIn) {
            throttle()
            val response = ktor.get(base + "sync/history/shows/$traktId") {
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                    append(HttpHeaders.Authorization, "Bearer $token")
                    append("trakt-api-key", BuildConfig.TRAKT_KEY)
                    append("trakt-api-version", "2")
                }
            }
            try {
                val json = Json.parseToJsonElement(response.bodyAsText()).jsonArray
                return JsonObject(mapOf(Pair("data", json)))
            } catch (e: Exception) {
                return null
            }
        } else {
            return null
        }
    }

    suspend fun getWatchedMovie(traktId: Long): JsonObject? {
        if (loggedIn) {
            throttle()
            val response = ktor.get(base + "sync/history/movies/$traktId") {
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                    append(HttpHeaders.Authorization, "Bearer $token")
                    append("trakt-api-key", BuildConfig.TRAKT_KEY)
                    append("trakt-api-version", "2")
                }
            }

            try {
                val json = Json.parseToJsonElement(response.bodyAsText()).jsonArray
                return if (json.size == 1) {
                    json[0].jsonObject
                } else {
                    null
                }
            } catch (e: Exception) {
                return null
            }
        } else {
            return null
        }
    }

    suspend fun getTraktIdFromTMDB(id: Long): Int {
        if (loggedIn) {
            throttle()
            val response = ktor.get(base + "search/tmdb/$id") {
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                    append(HttpHeaders.Authorization, "Bearer $token")
                    append("trakt-api-key", BuildConfig.TRAKT_KEY)
                    append("trakt-api-version", "2")
                }
            }
            val json = Json.parseToJsonElement(response.bodyAsText()).jsonArray[0].jsonObject
            return json[json["type"]!!.jsonPrimitive.content]!!.jsonObject["ids"]!!.jsonObject["trakt"]!!.jsonPrimitive.int
        } else {
            return 0
        }
    }
}