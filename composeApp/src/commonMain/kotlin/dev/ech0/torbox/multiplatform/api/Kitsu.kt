package dev.ech0.torbox.multiplatform.api

import com.russhwolf.settings.Settings
import dev.ech0.torbox.multiplatform.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.serialization.json.*

class KitsuAPI() {
    private val base = " https://kitsu.io/api/edge/"
    private val ktor = HttpClient(){
    }
    init {
    }
    suspend fun search(query: String): JsonArray{
        val response = ktor.get(base + "search/multi?query=${query.encodeURLPath()}&include_adult=${Settings().getBoolean("adultContent", false)}"){
            headers {
                append(HttpHeaders.Accept, "application/json")
            }
        }
        val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        return json["results"]!!.jsonArray
    }
}