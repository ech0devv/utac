package dev.ech0.torbox.multiplatform.api

import com.russhwolf.settings.Settings
import dev.ech0.torbox.multiplatform.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.serialization.json.*

lateinit var tmdbApi: TMDBApi

class TMDBApi() {
    private val base = "https://api.themoviedb.org/3/"
    private var token = BuildConfig.TMDB_KEY
    private val ktor = HttpClient(){
    }
    init {
    }
    suspend fun search(query: String): JsonArray{
        val response = ktor.get(base + "search/multi?query=${query.encodeURLPath()}&include_adult=${Settings().getBoolean("adultContent", false)}"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
                append(HttpHeaders.Accept, "application/json")
            }
        }
        val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        return json["results"]!!.jsonArray
    }
    suspend fun getTvDetails(id: Int): JsonObject{
        val response = ktor.get(base + "tv/$id?append_to_response=external_ids,content_ratings"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
                append(HttpHeaders.Accept, "application/json")
            }
        }
        val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        return json
    }
    suspend fun getSeasonDetails(id: Int, season: Int): JsonObject{
        val response = ktor.get(base + "tv/$id/season/$season"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
                append(HttpHeaders.Accept, "application/json")
            }
        }
        val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        return json
    }
    fun imageHelper(path: String): String {
        return "https://image.tmdb.org/t/p/original$path"
    }
    suspend fun getMovieDetails(id: Int): JsonObject{
        val response = ktor.get(base + "movie/$id?append_to_response=external_ids"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
                append(HttpHeaders.Accept, "application/json")
            }
        }
        val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        return json
    }
}