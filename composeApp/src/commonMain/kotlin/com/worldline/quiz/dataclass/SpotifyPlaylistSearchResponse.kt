package com.worldline.quiz.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class SpotifyPlaylist(
    val id: String,
    val images: List<SpotifyImage>,
    val name: String
)

@Serializable
data class SpotifyPlaylistsContainer(
    val items: List<SpotifyPlaylist?>
)

@Serializable
data class SpotifyPlaylistSearchResponse(
    val playlists: SpotifyPlaylistsContainer
)
