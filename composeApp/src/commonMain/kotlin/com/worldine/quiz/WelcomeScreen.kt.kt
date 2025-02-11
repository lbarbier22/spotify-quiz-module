package com.worldine.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeScreen(onStartButtonPushed : () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        Card(
            backgroundColor = Color.LightGray,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Quiz",
                    style = MaterialTheme.typography.h4,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "A simple quiz to discovers KMP and compose.",
                    style = MaterialTheme.typography.h6,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(8.dp)
                )
                Button(
                    modifier = Modifier.padding(all = 10.dp),
                    onClick = { onStartButtonPushed() }
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = "Localized description")
                    Text("Start the Quiz")
                }
            }
        }
    }
}