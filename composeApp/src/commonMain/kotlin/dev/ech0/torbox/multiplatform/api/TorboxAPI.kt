package dev.ech0.torbox.multiplatform.api


import androidx.navigation.NavHostController
import com.russhwolf.settings.Settings
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.io.IOException
import kotlinx.serialization.json.*

const val base = "https://api.torbox.app/v1/api/"
const val base_search = "https://search-api.torbox.app/"
lateinit var torboxAPI: TorboxAPI
class TorboxAPI(private var key: String, navController: NavHostController?) {
    init {
        if (key == "__") {
            navController?.navigate("Error/${"go set your api key, you goofball".encodeURLPath()}")
        }
    }

    private val ktor = HttpClient {
        install(HttpTimeout) {
            requestTimeoutMillis = 120000
            connectTimeoutMillis = 120000
            socketTimeoutMillis = 120000
        }

    }

    fun getApiKey(): String {
        return key
    }

    fun setApiKey(newKey: String) {
        key = newKey
    }

    suspend fun getListOfTorrents(): JsonObject {
        val response = ktor.get(base + "torrents/mylist?bypass_cache=true") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
        }
        val json = Json.decodeFromString<JsonObject>(response.bodyAsText())
        if (!json["success"]!!.jsonPrimitive.boolean) {
            throw IOException("Failed to get list of torrents with ${json}")
        }
        return json
    }

    suspend fun getListOfUsenet(): JsonObject {
        val response = ktor.get(base + "usenet/mylist?bypass_cache=true") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
        }
        val json = Json.decodeFromString<JsonObject>(response.bodyAsText())
        if (!json["success"]!!.jsonPrimitive.boolean) {
            throw IOException("Failed to get list of usenet downloads with ${json}")
        }
        return json
    }

    suspend fun getTorrentLink(id: Number, shouldGetZip: Boolean): JsonObject {
        val response =
            ktor.get(base + "torrents/requestdl?token=${key}&torrent_id=${id}&file_id=0&zip_link=$shouldGetZip")
        val json = Json.decodeFromString<JsonObject>(response.bodyAsText())
        if (!json["success"]!!.jsonPrimitive.boolean) {
            throw IOException("Failed to get torrent download link with ${json}")
        }
        return json
    }

    suspend fun getTorrentLink(id: Number, fileId: Number, shouldGetZip: Boolean): JsonObject {
        val response =
            ktor.get(base + "torrents/requestdl?token=${key}&torrent_id=${id}&file_id=$fileId&zip_link=$shouldGetZip")
        val json = Json.decodeFromString<JsonObject>(response.bodyAsText())
        if (!json["success"]!!.jsonPrimitive.boolean) {
            throw IOException("Failed to get torrent download link with ${json}")
        }
        return json
    }

    suspend fun getUsenetLink(id: Number, shouldGetZip: Boolean): JsonObject {
        val response =
            ktor.get(base + "usenet/requestdl?token=${key}&usenet_id=${id}&file_id=0&zip_link=$shouldGetZip")
        val json = Json.decodeFromString<JsonObject>(response.bodyAsText())
        if (!json["success"]!!.jsonPrimitive.boolean) {
            throw IOException("Failed to get usenet download link with ${json}")
        }
        return json
    }

    suspend fun controlTorrent(id: Number, operation: String): JsonObject {
        val response = ktor.post(base + "torrents/controltorrent") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
            contentType(ContentType.Application.Json)
            var jsonObject = JsonObject(
                mapOf(
                    Pair("torrent_id", JsonPrimitive(id)),
                    Pair("operation", JsonPrimitive(operation)),
                    Pair("all", JsonPrimitive(false))
                )
            )
            setBody(jsonObject.toString())
        }
        val json = Json.decodeFromString<JsonObject>(response.bodyAsText())
        if (!json["success"]!!.jsonPrimitive.boolean) {
            throw IOException("Failed to get modify torrent download with ${json}")
        }
        return json
    }

    suspend fun controlUsenet(id: Number, operation: String): JsonObject {
        val response = ktor.post(base + "usenet/controlusenetdownload") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
            contentType(ContentType.Application.Json)
            var jsonObject = JsonObject(
                mapOf(
                    Pair("usenet_id", JsonPrimitive(id)),
                    Pair("operation", JsonPrimitive(operation.encodeURLPath())),
                    Pair("all", JsonPrimitive(false))
                )
            )
            setBody(jsonObject.toString())
        }
        val json = Json.decodeFromString<JsonObject>(response.bodyAsText())
        if (!json["success"]!!.jsonPrimitive.boolean) {
            throw IOException("Failed to get modify torrent download with ${json}")
        }
        return json
    }

    suspend fun createTorrent(magnet: String): JsonObject {
        val response = ktor.post(base + "torrents/createtorrent") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("magnet", magnet)
                    }, boundary = "WebAppBoundary"
                )
            )
        }
        val json = Json.decodeFromString<JsonObject>(response.bodyAsText())

        if (!json["success"]!!.jsonPrimitive.boolean) {
            throw IOException("Failed to get create torrent download with ${json}")
        }
        return json
    }

    suspend fun createUsenet(link: String): JsonObject {
        val response = ktor.post(base + "usenet/createusenetdownload") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("link", link)
                    }, boundary = "WebAppBoundary"
                )
            )
        }
        val json = Json.decodeFromString<JsonObject>(response.bodyAsText())
        if (!json["success"]!!.jsonPrimitive.boolean) {
            throw IOException("Failed to get create usenet download with ${json}")
        }
        return json
    }

    suspend fun createUsenet(torrent: ByteArray): JsonObject {
        val response = ktor.post(base + "usenet/createusenetdownload") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("file", torrent, Headers.build {
                            append(HttpHeaders.ContentType, "application/octet-stream")
                            append(HttpHeaders.ContentDisposition, "form-data; name=\"file\"; filename=\"file.nzb\"")
                        })
                    }, boundary = "WebAppBoundary"
                )
            )
        }
        val json = Json.decodeFromString<JsonObject>(response.bodyAsText())
        if (!json["success"]!!.jsonPrimitive.boolean) {
            throw IOException("Failed to get create usenet download with ${json}")
        }
        return json
    }

    suspend fun createTorrent(torrent: ByteArray): JsonObject {
        val response = ktor.post(base + "torrents/createtorrent") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("torrent", torrent, Headers.build {
                            append(HttpHeaders.ContentType, "application/x-bittorrent")
                            append(
                                HttpHeaders.ContentDisposition,
                                "form-data; name=\"file\"; filename=\"file.torrent\""
                            )
                        })
                    }, boundary = "WebAppBoundary"
                )
            )
        }
        val json = Json.decodeFromString<JsonObject>(response.bodyAsText())
        if (!json["success"]!!.jsonPrimitive.boolean) {
            throw IOException("Failed to get create torrent download with ${json}")
        }
        return json
    }

    suspend fun searchTorrents(query: String, season: Int, episode: Int): JsonObject {
        val response =
            ktor.get(base_search + "torrents/${query.encodeURLPath()}?check_cache=true&check_owned=true&season=$season&episode=$episode&metadata=false") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $key")
                }
            }
        val json = Json.decodeFromString<JsonObject>(response.bodyAsText())
        if (!json["success"]!!.jsonPrimitive.boolean) {
            return JsonObject(mapOf(Pair("data", JsonObject(mapOf(Pair("torrents", JsonArray(listOf())))))))
        }
        return json
    }

    suspend fun searchTorrentsId(query: String): JsonObject {
        val response =
            ktor.get(base_search + "torrents/${query.encodeURLPath()}?check_cache=true&check_owned=true&metadata=false") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $key")
                }
            }
        val json = Json.decodeFromString<JsonObject>(response.bodyAsText())
        if (!json["success"]!!.jsonPrimitive.boolean) {
            return JsonObject(mapOf(Pair("data", JsonObject(mapOf(Pair("torrents", JsonArray(listOf())))))))
        }
        return json
    }

    suspend fun searchUsenet(query: String, season: Int, episode: Int): JsonObject {

        val response =
            ktor.get(base_search + "usenet/${query.encodeURLPath()}?check_cache=true&check_owned=true&season=$season&episode=$episode&metadata=false") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $key")
                }
            }
        val json = Json.decodeFromString<JsonObject>(response.bodyAsText())
        if (!json["success"]!!.jsonPrimitive.boolean) {
            throw IOException("Failed to search torrents with ${json}")
        }
        return json
    }

    suspend fun searchUsenetId(query: String): JsonObject {
        val response =
            ktor.get(base_search + "usenet/${query.encodeURLPath()}?check_cache=true&check_owned=true&metadata=false") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $key")
                }
            }
        val json = Json.decodeFromString<JsonObject>(response.bodyAsText())
        if (!json["success"]!!.jsonPrimitive.boolean) {
            throw IOException("Failed to search torrents with ${json}")
        }
        return json
    }

    suspend fun searchTorrents(query: String): JsonObject {
        println(query.encodeURLPath())
        val response =
            ktor.get(base_search + "torrents/search/${query.encodeURLPath()}?check_cache=true&check_owned=true") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $key")
                }
            }
        val json = Json.decodeFromString<JsonObject>(response.bodyAsText())
        if (!json["success"]!!.jsonPrimitive.boolean) {
            throw IOException("Failed to search torrents with ${json}")
        }
        return json
    }

    suspend fun searchUsenet(query: String): JsonObject {
        val response =
            ktor.get(base_search + "usenet/search/${query.encodeURLPath()}?check_cache=true&check_owned=true") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $key")
                }
            }
        val json = Json.decodeFromString<JsonObject>(response.bodyAsText())
        if (!json["success"]!!.jsonPrimitive.boolean) {
            throw IOException("Failed to search torrents with ${json}")
        }
        return json
    }

    suspend fun getTorrentInfo(query: String): JsonObject {
        val response = ktor.get(base + "torrents/torrentinfo?hash=${query.encodeURLPath()}") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
        }
        val json = Json.decodeFromString<JsonObject>(response.bodyAsText())
        if (!json["success"]!!.jsonPrimitive.boolean) {
            throw IOException("Failed to search torrents with ${json}")
        }
        return json
    }

    suspend fun checkApiKey(toCheck: String): Boolean {
        val response = ktor.get(base + "user/me?settings=false") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
        }
        val json = Json.decodeFromString<JsonObject>(response.bodyAsText())
        if (!json["success"]!!.jsonPrimitive.boolean) {
            return false
        }
        Settings().putInt("plan", json["data"]!!.jsonObject["plan"]!!.jsonPrimitive.int)
        Settings().putString("userdata", json["data"].toString())
        return true
    }

    suspend fun getMetaFromId(id: String): JsonObject {
        val response = ktor.get("${base_search}meta/${id.encodeURLPath()}") {}
        val json = Json.decodeFromString<JsonObject>(response.bodyAsText())
        return json
    }

    suspend fun getTorrentsIMDB(query: String, season: Int, episode: Int): JsonObject {
        val response =
            ktor.get(base_search + "torrents/$query?metadata=false&season=$season&episode=$episode&check_cache=true&check_owned=true") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $key")
                }
            }
        val json: JsonObject = Json.decodeFromString<JsonObject>(response.bodyAsText())
        if (!json["success"]!!.jsonPrimitive.boolean) {
            throw IOException("Failed to search torrents with ${json}")
        }
        return json
    }

    suspend fun checkCache(hash: String): JsonObject {
        val response =
            ktor.get(base + "torrents/checkcached?hash=${hash.encodeURLPath()}&format=object&list_files=true") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $key")
                }
            }

        val json: JsonObject = Json.decodeFromString<JsonObject>(response.bodyAsText())
        if (!json["success"]!!.jsonPrimitive.boolean) {
            throw IOException("Failed to search torrents with ${json}")
        }
        return json
    }
}