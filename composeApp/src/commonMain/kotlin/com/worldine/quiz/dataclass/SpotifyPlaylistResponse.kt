package com.worldine.quiz.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class SpotifyPlaylistResponse (
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
    val artists: List<SpotifyArtist>,
    val album: SpotifyAlbum
)

@Serializable
data class SpotifyAlbum(
    val images: List<SpotifyImage>,
    val name: String
)

@Serializable
data class SpotifyArtist(
    val name: String
)

@Serializable
data class SpotifyImage(
    val url: String
)
