package dev.ech0.torbox.api

import org.json.JSONArray
import org.json.JSONObject

class Torrent constructor(
    var raw: JSONObject,
    var title: String,
    var size: Long,
    var magnet: String,
    var torrent: String,
    var seeders: Int,
    var leechers: Int,
    var hash: String,
    var cached: Boolean = false,
    var files: JSONArray = JSONArray()
)