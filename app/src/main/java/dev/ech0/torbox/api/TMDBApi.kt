package dev.ech0.torbox.api

import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.preference.PreferenceManager
import dev.ech0.torbox.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

lateinit var tmdbApi: TMDBApi

class TMDBApi(var preferences: SharedPreferences) {
    private val base = "https://api.themoviedb.org/3/"
    private var token = BuildConfig.TMDB_KEY
    private val ktor = HttpClient(OkHttp){
    }
    init {
        GlobalScope.launch(Dispatchers.IO) {
            Log.d("dev.ech0.torbox", "got token")
        }
    }
    suspend fun search(query: String): JSONArray{
        val response = ktor.get(base + "search/multi?query=${Uri.encode(query)}&include_adult=${preferences.getBoolean("adultContent", false)}"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
                append(HttpHeaders.Accept, "application/json")
            }
        }
        val json = JSONObject(response.bodyAsText())
        return json.getJSONArray("results")
    }
    suspend fun getTvDetails(id: Int): JSONObject{
        val response = ktor.get(base + "tv/$id?append_to_response=external_ids,content_ratings"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
                append(HttpHeaders.Accept, "application/json")
            }
        }
        val json = JSONObject(response.bodyAsText())
        return json
    }
    suspend fun getSeasonDetails(id: Int, season: Int): JSONObject{
        val response = ktor.get(base + "tv/$id/season/$season"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
                append(HttpHeaders.Accept, "application/json")
            }
        }
        val json = JSONObject(response.bodyAsText())
        return json
    }
    fun imageHelper(path: String): String {
        return "https://image.tmdb.org/t/p/original$path"
    }
    suspend fun getMovieDetails(id: Int): JSONObject{
        val response = ktor.get(base + "movie/$id?append_to_response=external_ids"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
                append(HttpHeaders.Accept, "application/json")
            }
        }
        val json = JSONObject(response.bodyAsText())
        return json
    }
}