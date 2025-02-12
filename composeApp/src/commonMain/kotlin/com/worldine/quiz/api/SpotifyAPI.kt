package com.worldine.quiz.api

import com.worldine.quiz.dataclass.SpotifyPlaylistResponse
import com.worldine.quiz.dataclass.SpotifyTrack
import com.worldine.quiz.dataclass.SpotifyTrackItem
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class SpotifyApi(private val token: String) {
    private val client = HttpClient()

    suspend fun getPlaylistTracks(playlistId: String): List<SpotifyTrack> {
        return try {
            val response = client.get("https://api.spotify.com/v1/playlists/$playlistId/tracks") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            val jsonResponse = response.bodyAsText()

            val jsonParser = Json { ignoreUnknownKeys = true }
            val parsed = jsonParser.decodeFromString(SpotifyPlaylistResponse.serializer(), jsonResponse)
            return parsed.items.map { it.track }
        } catch (e: Exception) {
            println("Erreur API Spotify: ${e.message}")
            emptyList()
        }
    }

}