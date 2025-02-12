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

@Composable
fun QuizScreen(questions: List<SpotifyTrackItem>, onFinishButtonPushed: (Int, Int) -> Unit) {
    var questionProgress by remember { mutableStateOf(0) }
    var userAnswer by remember { mutableStateOf(TextFieldValue("")) }
    var score by remember { mutableStateOf(0) }

    val currentQuestion = questions[questionProgress]

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                    contentDescription = "Image test",
                    modifier = Modifier.size(200.dp),
                    onState = { state ->
                        when (state) {
                            is coil3.compose.AsyncImagePainter.State.Loading -> println("⏳ Image en cours de chargement...")
                            is coil3.compose.AsyncImagePainter.State.Success -> println("✅ Image chargée avec succès !")
                            is coil3.compose.AsyncImagePainter.State.Error -> println("❌ Erreur Coil : ${state.result.throwable.message}")
                            else -> {}
                        }
                    }
                )
                Text(
                    modifier = Modifier.padding(all = 10.dp),
                    text = "Quel est cette album ?",
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        OutlinedTextField(
            value = userAnswer,
            onValueChange = { userAnswer = it },
            label = { Text("Tape ta réponse ici") },
            placeholder = { Text(currentQuestion.track.name, color = Color.Gray) }, // Pour tester l'API
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp)
        )

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                if (userAnswer.text.equals(currentQuestion.track.name, ignoreCase = true)) {
                    score++
                }
                if (questionProgress < questions.size - 1) {
                    questionProgress++
                    userAnswer = TextFieldValue("")
                } else {
                    onFinishButtonPushed(score, questions.size)
                }
            }
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Next",
                Modifier.padding(end = 15.dp)
            )
            Text("Suivant")
        }

        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .padding(top = 20.dp),
            progress = (questionProgress + 1).toFloat() / questions.size
        )
    }
}
