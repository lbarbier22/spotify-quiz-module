package com.worldline.quiz

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScoreScreen(score: Int, total:Int, onResetButtonPushed: () -> Unit, onHomeButtonPushed: () -> Unit){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(10.dp),
            backgroundColor = Color.LightGray
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        fontSize = 45.sp,
                        text = "Score",
                    )
                    Text(
                        fontSize = 30.sp,
                        text = "$score/$total",
                    )
                    Button(
                        modifier = Modifier.padding(all = 20.dp),
                        onClick = { onResetButtonPushed() }
                    ) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Localized description")
                        Text(text = " Retake the Quiz")
                    }
                    Button(
                        modifier = Modifier.padding(all = 20.dp),
                        onClick = { onHomeButtonPushed() }
                    ) {
                        Icon(Icons.Filled.Home, contentDescription = "Go back to menu")
                    }
                }
            }
        }
    }
}
