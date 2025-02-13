package com.worldline.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.graphicsLayer
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
    var remainingLives by remember { mutableStateOf(4) }
    var questionFinished by remember { mutableStateOf(false) }
    val initialBlur = 60f
    val blurRadius = remember { mutableStateOf(initialBlur) }
    var headerText by remember { mutableStateOf("Vies restantes : $remainingLives") }
    var hintText by remember { mutableStateOf("") }
    val currentQuestion = questions[questionProgress]

    fun normalize(input: String): String = input.replace("\\s+".toRegex(), "")

    fun resetQuestion() {
        if (questionProgress >= questions.size - 1) {
            onFinishButtonPushed(score, questions.size)
        } else {
            questionProgress++
            userAnswer = TextFieldValue("")
            remainingLives = 4
            blurRadius.value = initialBlur
            headerText = "Vies restantes : $remainingLives"
            questionFinished = false
            hintText = ""
        }
    }

    fun processAnswer() {
        val userNormalized = normalize(userAnswer.text).lowercase()
        val correctAnswer = (currentQuestion.track.artists.firstOrNull()?.name ?: "Inconnu")
        val correctNormalized = normalize(correctAnswer).lowercase()

        if (userNormalized == correctNormalized) {
            score++
            headerText = "Bonne réponse !"
            questionFinished = true
            hintText = ""
            blurRadius.value = 0f
        } else {
            remainingLives--
            when (remainingLives) {
                3 -> {
                    val firstLetter = currentQuestion.track.artists.firstOrNull()?.name
                        ?.firstOrNull()?.toString() ?: ""
                    hintText = "Première lettre de l'artiste : $firstLetter"
                }
                2 -> {
                    val albumName = currentQuestion.track.album.name
                    hintText = "Le nom de l'album : $albumName"
                }
                1 -> {
                    val artist = currentQuestion.track.artists.firstOrNull()?.name ?: "Inconnu"
                    val shuffledName = artist.toList().shuffled().joinToString("")
                    hintText = "Réponse mélangée : $shuffledName"
                }
            }
            if (remainingLives > 0) {
                headerText = "Vies restantes : $remainingLives"
                blurRadius.value = (blurRadius.value - (initialBlur / 4)).coerceAtLeast(0f)
            } else {
                headerText = "La bonne réponse était : $correctAnswer"
                hintText = ""
                blurRadius.value = 0f
                questionFinished = true
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = headerText,
            fontSize = 20.sp,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        if (hintText.isNotEmpty()) {
            Text(
                text = hintText,
                fontSize = 16.sp,
                color = MaterialTheme.colors.secondary,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }

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
                    text = "Quel est l'artiste de cet album ?",
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        OutlinedTextField(
            value = userAnswer,
            onValueChange = { userAnswer = it },
            label = { Text("Tape ta réponse ici") },
//            placeholder = { Text(currentQuestion.track.album.name, color = Color.Gray) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp)
        )

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                if (questionFinished) {
                    resetQuestion()
                } else {
                    processAnswer()
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
