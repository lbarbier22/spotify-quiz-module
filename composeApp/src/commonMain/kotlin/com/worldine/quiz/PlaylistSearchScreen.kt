package com.worldine.quiz

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.worldine.quiz.api.SpotifyApi
import com.worldine.quiz.dataclass.SpotifyPlaylist
import kotlinx.coroutines.launch

@Composable
fun PlaylistSearchScreen(
    spotifyApi: SpotifyApi,
    onPlaylistSelected: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var playlists by remember { mutableStateOf(emptyList<SpotifyPlaylist>()) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Recherche ta playlist") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                isLoading = true
                coroutineScope.launch {
                    try {
                        playlists = spotifyApi.searchPlaylists(query)
                    } catch (e: Exception) {

                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Rechercher")
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            playlists.take(6).forEach { playlist ->
                Card(
                    shape = RoundedCornerShape(8.dp),
                    elevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onPlaylistSelected(playlist.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = playlist.images.firstOrNull()?.url ?: "",
                            contentDescription = playlist.name,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = playlist.name, style = MaterialTheme.typography.h6)
                    }
                }
            }
        }
    }
}
