package com.worldline.quiz.api

import com.worldline.quiz.dataclass.SpotifyPlaylist
import com.worldline.quiz.dataclass.SpotifyPlaylistResponse
import com.worldline.quiz.dataclass.SpotifyPlaylistSearchResponse
import com.worldline.quiz.dataclass.SpotifyTrack
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class SpotifyApi {
    private val client = HttpClient()
    private var token: String? = null

    suspend fun getAccessToken(clientId: String, clientSecret: String): String? {
        return try {
            val client = HttpClient()

            val response: HttpResponse = client.post("https://accounts.spotify.com/api/token") {
                header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded)
                setBody("grant_type=client_credentials&client_id=$clientId&client_secret=$clientSecret")
            }

            val jsonResponse = response.bodyAsText()
            val jsonElement = Json.parseToJsonElement(jsonResponse)

            return jsonElement.jsonObject["access_token"]?.jsonPrimitive?.content
        } catch (e: Exception) {
            println("Erreur lors de l'obtention du token Spotify: ${e.message}")
            null
        }
    }

    suspend fun initialize(clientId: String, clientSecret: String) {
        if (clientId.isEmpty() || clientSecret.isEmpty()) {
            throw Exception("Le client-id ou client-secret n'est pas renseigné.")
        }
        token = getAccessToken(clientId, clientSecret)
    }

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

    suspend fun searchPlaylists(query: String): List<SpotifyPlaylist> {
        return try {
            val response =
                client.get("https://api.spotify.com/v1/search?q=$query&type=playlist") {
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            val jsonResponse = response.bodyAsText()
            val jsonParser = Json { ignoreUnknownKeys = true }
            val parsed = jsonParser.decodeFromString(
                SpotifyPlaylistSearchResponse.serializer(),
                jsonResponse
            )
            parsed.playlists.items.filterNotNull()
        } catch (e: Exception) {
            println("Erreur lors de la recherche de playlists: ${e.message}")
            emptyList()
        }

    }
}