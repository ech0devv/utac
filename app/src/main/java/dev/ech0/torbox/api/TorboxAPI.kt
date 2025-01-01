package dev.ech0.torbox.api

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.navigation.NavHostController
import androidx.preference.PreferenceManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.*
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.contentType
import kotlinx.io.IOException
import org.json.JSONObject

const val base = "https://api.torbox.app/v1/api/"
const val base_search = "https://search-api.torbox.app/"
lateinit var torboxAPI: TorboxAPI
fun isTorboxInitialized() = ::torboxAPI.isInitialized
class TorboxAPI(private var key: String, navController: NavHostController?) {
    init {
        if(key == "__"){
            navController?.navigate("Error/${Uri.encode("go set your api key, you goofball.")}")
        }
    }
    private val ktor = HttpClient(OkHttp){
        install(HttpTimeout){
            requestTimeoutMillis = 120000
            connectTimeoutMillis = 120000
            socketTimeoutMillis = 120000
        }

    }
    fun getApiKey(): String{
        return key
    }
    fun setApiKey(newKey: String){
        key = newKey
    }
    suspend fun getListOfTorrents (): JSONObject{
        val response = ktor.get(base + "torrents/mylist?bypass_cache=true"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
        }
        val json = JSONObject(response.bodyAsText())
        if(!json.getBoolean("success")){
            throw IOException("Failed to get list of torrents with ${json.toString(2)}")
        }
        return json
    }
    suspend fun getListOfUsenet (): JSONObject{
        val response = ktor.get(base + "usenet/mylist?bypass_cache=true"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
        }
        val json = JSONObject(response.bodyAsText())
        if(!json.getBoolean("success")){
            throw IOException("Failed to get list of usenet downloads with ${json.toString(2)}")
        }
        return json
    }
    suspend fun getTorrentLink (id: Number, shouldGetZip: Boolean): JSONObject{
        val response = ktor.get(base + "torrents/requestdl?token=${key}&torrent_id=${id}&file_id=0&zip_link=${shouldGetZip.toString()}")
        val json = JSONObject(response.bodyAsText())
        if(!json.getBoolean("success")){
            throw IOException("Failed to get torrent download link with ${json.toString(2)}")
        }
        return json
    }
    suspend fun getTorrentLink (id: Number, fileId: Number, shouldGetZip: Boolean): JSONObject{
        val response = ktor.get(base + "torrents/requestdl?token=${key}&torrent_id=${id}&file_id=$fileId&zip_link=${shouldGetZip.toString()}")
        val json = JSONObject(response.bodyAsText())
        if(!json.getBoolean("success")){
            throw IOException("Failed to get torrent download link with ${json.toString(2)}")
        }
        return json
    }
    suspend fun getUsenetLink (id: Number, shouldGetZip: Boolean): JSONObject{
        val response = ktor.get(base + "usenet/requestdl?token=${key}&usenet_id=${id}&file_id=0&zip_link=${shouldGetZip.toString()}")
        val json = JSONObject(response.bodyAsText())
        if(!json.getBoolean("success")){
            throw IOException("Failed to get usenet download link with ${json.toString(2)}")
        }
        return json
    }
    suspend fun controlTorrent(id: Number, operation: String): JSONObject{
        val response = ktor.post(base + "torrents/controltorrent"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
            contentType(ContentType.Application.Json)
            var jsonObject = JSONObject()
            jsonObject.put("torrent_id", id).put("operation", operation).put("all", false)
            setBody(jsonObject.toString())
        }
        val json = JSONObject(response.bodyAsText())
        if(!json.getBoolean("success")){
            throw IOException("Failed to get modify torrent download with ${json.toString(2)}")
        }
        return json
    }
    suspend fun controlUsenet(id: Number, operation: String): JSONObject{
        val response = ktor.post(base + "usenet/controlusenetdownload"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
            contentType(ContentType.Application.Json)
            var jsonObject = JSONObject()
            jsonObject.put("usenet_id", id).put("operation", operation).put("all", false)
            setBody(jsonObject.toString())
        }
        val json = JSONObject(response.bodyAsText())
        if(!json.getBoolean("success")){
            throw IOException("Failed to get modify torrent download with ${json.toString(2)}")
        }
        return json
    }
    suspend fun createTorrent(magnet: String): JSONObject{
        val response = ktor.post(base + "torrents/createtorrent"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
            setBody(MultiPartFormDataContent(
                formData {
                    append("magnet", magnet)
                },
                boundary = "WebAppBoundary"
            ))
        }
        val json = JSONObject(response.bodyAsText())
        if(!json.getBoolean("success")){
            throw IOException("Failed to get create torrent download with ${json.toString(2)}")
        }
        return json
    }
    suspend fun createUsenet(link: String): JSONObject{
        val response = ktor.post(base + "usenet/createusenetdownload"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
            setBody(MultiPartFormDataContent(
                formData {
                    append("link", link)
                },
                boundary = "WebAppBoundary"
            ))
        }
        val json = JSONObject(response.bodyAsText())
        if(!json.getBoolean("success")){
            throw IOException("Failed to get create usenet download with ${json.toString(2)}")
        }
        return json
    }
    suspend fun createUsenet(torrent: ByteArray): JSONObject{
        val response = ktor.post(base + "usenet/createusenetdownload"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
            setBody(MultiPartFormDataContent(
                formData {
                    append("file", torrent, Headers.build{
                        append(HttpHeaders.ContentType, "application/octet-stream")
                        append(HttpHeaders.ContentDisposition, "form-data; name=\"file\"; filename=\"file.nzb\"")
                    })
                },
                boundary = "WebAppBoundary"
            ))
        }
        val json = JSONObject(response.bodyAsText())
        if(!json.getBoolean("success")){
            throw IOException("Failed to get create usenet download with ${json.toString(2)}")
        }
        return json
    }
    suspend fun createTorrent(torrent: ByteArray): JSONObject{
        val response = ktor.post(base + "torrents/createtorrent"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
            setBody(MultiPartFormDataContent(
                formData {
                    append("torrent", torrent, Headers.build{
                        append(HttpHeaders.ContentType, "application/x-bittorrent")
                        append(HttpHeaders.ContentDisposition, "form-data; name=\"file\"; filename=\"file.torrent\"")
                    })
                },
                boundary = "WebAppBoundary"
            ))
        }
        val json = JSONObject(response.bodyAsText())
        if(!json.getBoolean("success")){
            throw IOException("Failed to get create torrent download with ${json.toString(2)}")
        }
        return json
    }
    suspend fun searchTorrents(query: String, season: Int, episode: Int): JSONObject{
        val response = ktor.get(base_search + "torrents/${Uri.encode(query)}?check_cache=true&check_owned=true&season=$season&episode=$episode&metadata=false"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
        }
        val json = JSONObject(response.bodyAsText())
        if(!json.getBoolean("success")){
            return JSONObject("""
                {
                    "data": {"torrents": []}
                }
            """.trimIndent())
        }
        return json
    }
    suspend fun searchTorrentsId(query: String): JSONObject{
        val response = ktor.get(base_search + "torrents/${Uri.encode(query)}?check_cache=true&check_owned=true&metadata=false"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
        }
        val json = JSONObject(response.bodyAsText())
        if(!json.getBoolean("success")){
            return JSONObject("""
                {
                    "data": {"torrents": []}
                }
            """.trimIndent())
        }
        return json
    }
    suspend fun searchUsenet(query: String, season: Int, episode: Int): JSONObject{

        val response = ktor.get(base_search + "usenet/${Uri.encode(query)}?check_cache=true&check_owned=true&season=$season&episode=$episode&metadata=false"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
        }
        val json = JSONObject(response.bodyAsText())
        if(!json.getBoolean("success")){
            throw IOException("Failed to search torrents with ${json.toString(2)}")
        }
        return json
    }
    suspend fun searchUsenetId(query: String): JSONObject{
        val response = ktor.get(base_search + "usenet/${Uri.encode(query)}?check_cache=true&check_owned=true&metadata=false"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
        }
        val json = JSONObject(response.bodyAsText())
        if(!json.getBoolean("success")){
            throw IOException("Failed to search torrents with ${json.toString(2)}")
        }
        return json
    }
    suspend fun searchTorrents(query: String): JSONObject{
        val response = ktor.get(base_search + "torrents/search/${Uri.encode(query)}?check_cache=true&check_owned=true"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
        }
        val json = JSONObject(response.bodyAsText())
        if(!json.getBoolean("success")){
            throw IOException("Failed to search torrents with ${json.toString(2)}")
        }
        return json
    }
    suspend fun getTorrentInfo(query: String): JSONObject{
        val response = ktor.get(base + "torrents/torrentinfo?hash=${Uri.encode(query)}"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
        }
        val json = JSONObject(response.bodyAsText())
        if(!json.getBoolean("success")){
            throw IOException("Failed to search torrents with ${json.toString(2)}")
        }
        return json
    }
    suspend fun checkApiKey(toCheck: String, context: Context): Boolean{
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val response = ktor.get(base + "user/me?settings=false"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
        }
        val json = JSONObject(response.bodyAsText())
        if(!json.getBoolean("success")){
            return false
        }
        preferences.edit().putInt("plan", json.getJSONObject("data").getInt("plan")).apply()
        preferences.edit().putString("userdata", json.getJSONObject("data").toString()).apply()
        return true
    }
    suspend fun getMetaFromId(id: String): JSONObject{
        val response = ktor.get("${base_search}meta/${Uri.encode(id)}"){}
        val json = JSONObject(response.bodyAsText())
        return json
    }
    suspend fun getTorrentsIMDB(query: String, season: Int, episode: Int): JSONObject{
        val response = ktor.get(base_search + "torrents/$query?metadata=false&season=$season&episode=$episode&check_cache=true&check_owned=true"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
        }
        val json: JSONObject = JSONObject(response.bodyAsText())
        if(!json.getBoolean("success")){
            throw IOException("Failed to search torrents with ${json.toString(2)}")
        }
        return json
    }
    suspend fun checkCache(hash: String): JSONObject{
        val response = ktor.get(base + "torrents/checkcached?hash=\"${Uri.encode(hash)}\"&format=object&list_files=true"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $key")
            }
        }

        val json: JSONObject = JSONObject(response.bodyAsText())
        if(!json.getBoolean("success")){
            throw IOException("Failed to search torrents with ${json.toString(2)}")
        }
        return json
    }
}