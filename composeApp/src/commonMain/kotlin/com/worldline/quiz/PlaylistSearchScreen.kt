package com.worldline.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.worldline.quiz.api.SpotifyApi
import com.worldline.quiz.dataclass.SpotifyPlaylist
import kotlinx.coroutines.launch

@Composable
fun PlaylistSearchScreen(
    spotifyApi: SpotifyApi,
    onPlaylistSelected: (String) -> Unit
) {
    val spotifyGreen = Color(0xFF1DB954)
    val cardColor = Color(0xFF1E1E1E)
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF512afe),
            Color(0xFF57dd5e),
            Color(0xFF7a2c9e)
        )
    )

    var query by remember { mutableStateOf("") }
    var playlists by remember { mutableStateOf(emptyList<SpotifyPlaylist>()) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            backgroundColor = cardColor,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Recherche de Playlist",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.ExtraBold,
                    color = spotifyGreen,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Recherche ta playlist", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.LightGray,
                        focusedBorderColor = spotifyGreen,
                        unfocusedBorderColor = Color.LightGray,
                        cursorColor = spotifyGreen,
                        focusedLabelColor = spotifyGreen,
                        unfocusedLabelColor = Color.LightGray
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        isLoading = true
                        coroutineScope.launch {
                            try {
                                playlists = spotifyApi.searchPlaylists(query)
                            } catch (e: Exception) {
                                // Gérer l'erreur si besoin
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = spotifyGreen,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Rechercher")
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (isLoading) {
                    CircularProgressIndicator(color = spotifyGreen)
                } else {
                    playlists.take(6).forEach { playlist ->
                        playlist?.let {
                        Card(
                            backgroundColor = Color.DarkGray,
                            shape = RoundedCornerShape(8.dp),
                            elevation = 4.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { onPlaylistSelected(playlist.id) }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                AsyncImage(
                                    model = playlist.images.firstOrNull()?.url ?: "",
                                    contentDescription = playlist.name,
                                    modifier = Modifier.size(64.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = playlist.name,
                                    style = MaterialTheme.typography.h6,
                                    color = Color.White,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                    }
                }
            }
        }
    }
}
