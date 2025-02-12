package com.worldline.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.worldine.quiz.dataclass.SpotifyTrackItem
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun QuizScreen(questions: List<SpotifyTrackItem>, onFinishButtonPushed: (Int, Int) -> Unit) {
    var questionProgress by remember { mutableStateOf(0) }
    var userAnswer by remember { mutableStateOf(TextFieldValue("")) }
    var score by remember { mutableStateOf(0) }
    var remainingLives by remember { mutableStateOf(3) }
    var questionFinished by remember { mutableStateOf(false) }
    val initialBlur = 60f
    val blurRadius = remember { mutableStateOf(initialBlur) }
    var headerText by remember { mutableStateOf("Vies restantes : $remainingLives") }
    val currentQuestion = questions[questionProgress]

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = headerText,
            fontSize = 20.sp,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Card(
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.padding(60.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                AsyncImage(
                    model = currentQuestion.track.album.images.firstOrNull()?.url ?: "",
                    contentDescription = "Image de l'album",
                    modifier = Modifier
                        .padding(20.dp)
                        .graphicsLayer {
                            renderEffect = BlurEffect(
                                radiusX = blurRadius.value,
                                radiusY = blurRadius.value
                            )
                            clip = true
                        }
                )
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "Quel est cet album ?",
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        OutlinedTextField(
            value = userAnswer,
            onValueChange = { userAnswer = it },
            label = { Text("Tape ta réponse ici") },
            placeholder = { Text(currentQuestion.track.album.name, color = Color.Gray) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp)
        )

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                if (questionFinished) {
                    if (questionProgress >= questions.size - 1) {
                        onFinishButtonPushed(score, questions.size)
                    } else {
                        questionProgress++
                        userAnswer = TextFieldValue("")
                        remainingLives = 3
                        blurRadius.value = initialBlur
                        headerText = "Vies restantes : $remainingLives"
                        questionFinished = false
                    }
                } else {
                    if (userAnswer.text.equals(currentQuestion.track.album.name, ignoreCase = true)) {
                        score++
                        headerText = "Bonne réponse !"
                        questionFinished = true
                    } else {
                        remainingLives--
                        if (remainingLives > 0) {
                            headerText = "Vies restantes : $remainingLives"
                            blurRadius.value = (blurRadius.value - (initialBlur / 3)).coerceAtLeast(0f)
                        } else {
                            headerText = "La bonne réponse était : ${currentQuestion.track.album.name}"
                            blurRadius.value = 0f
                            questionFinished = true
                        }
                    }
                }
            }
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Suivant",
                Modifier.padding(end = 15.dp)
            )
            Text("Suivant")
        }

        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            progress = (questionProgress + 1).toFloat() / questions.size
        )
    }
}
