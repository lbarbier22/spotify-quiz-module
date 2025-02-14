package com.worldline.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeScreen(onStartButtonPushed: () -> Unit) {
    val cardColor = Color(0xFF1E1E1E)
    val spotifyGreen = Color(0xFF1DB954)

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
            .background(brush = backgroundGradient),
        contentAlignment = Alignment.Center
    ) {
        Card(
            backgroundColor = cardColor,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.9f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Spotify Quiz",
                    style = MaterialTheme.typography.h3,
                    fontWeight = FontWeight.ExtraBold,
                    color = spotifyGreen,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Règles du jeu :",
                    style = MaterialTheme.typography.h6,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = """
                        • Dix albums floutés apparaissent.
                        • Tu disposes de 4 vies pour deviner chacun des artistes.
                        • À chaque mauvaise réponse, l'image se défloute progressivement.
                        • Après 4 erreurs, l'album est révélé.
                        • A chaque erreur, un indice est donné.
                        
                        Amuse-toi bien !
                    """.trimIndent(),
                    style = MaterialTheme.typography.body1,
                    color = Color.LightGray,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Button(
                    onClick = onStartButtonPushed,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = spotifyGreen,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Démarrer le quiz"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Commencer le Quiz")
                }
            }
        }
    }
}
