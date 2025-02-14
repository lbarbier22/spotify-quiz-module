package com.worldline.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.worldline.quiz.api.SpotifyApi
import com.worldline.quiz.dataclass.SpotifyTrackItem
import kotlinx.coroutines.launch

@Composable
fun ScoreScreen(
    score: Int,
    total: Int,
    onPlaylistSearchButtonPushed: () -> Unit,
    onHomeButtonPushed: () -> Unit
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


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            backgroundColor = cardColor,
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Score",
                    fontSize = 45.sp,
                    color = spotifyGreen
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$score / $total",
                    fontSize = 30.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onPlaylistSearchButtonPushed,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = spotifyGreen,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Recherche Playlist")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onHomeButtonPushed,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = spotifyGreen,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Filled.Home, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Retour Menu")
                }
            }
        }
    }
}
