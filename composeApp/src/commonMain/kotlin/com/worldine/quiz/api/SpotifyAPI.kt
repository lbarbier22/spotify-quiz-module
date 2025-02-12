package com.worldine.quiz.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class SpotifyPlaylistResponse(
    val href: String,
    val items: List<SpotifyTrackItem>
)

@Serializable
data class SpotifyTrackItem(
    val track: SpotifyTrack
)

@Serializable
data class SpotifyTrack(
    val name: String,
    val album: SpotifyAlbum
)

@Serializable
data class SpotifyAlbum(
    val images: List<SpotifyImage>,
    val name: String
)

@Serializable
data class SpotifyImage(
    val url: String
)

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