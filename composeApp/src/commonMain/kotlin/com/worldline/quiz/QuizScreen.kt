package com.worldline.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.worldline.quiz.dataclass.SpotifyTrackItem

@Composable
fun QuizScreen(questions: List<SpotifyTrackItem>, onFinishButtonPushed: (Int, Int) -> Unit) {
    val spotifyGreen = Color(0xFF1DB954)
    val cardColor = Color(0xFF1E1E1E)
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF512afe),
            Color(0xFF57dd5e),
            Color(0xFF7a2c9e)
        )
    )

    var questionProgress by remember { mutableStateOf(0) }
    var userAnswer by remember { mutableStateOf(TextFieldValue("")) }
    var score by remember { mutableStateOf(0) }
    var remainingLives by remember { mutableStateOf(4) }
    var questionFinished by remember { mutableStateOf(false) }
    val initialBlur = 60f
    val blurRadius = remember { mutableStateOf(initialBlur) }
    var headerText by remember { mutableStateOf("Bonne réponse!") }
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
            headerText = "Bonne réponse!"
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
                blurRadius.value = (blurRadius.value - (initialBlur / 4)).coerceAtLeast(0f)
            } else {
                headerText = "La bonne réponse était : $correctAnswer"
                hintText = ""
                blurRadius.value = 0f
                questionFinished = true
            }
        }
    }

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
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                if (!questionFinished) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        repeat(remainingLives) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = "Coeur",
                                tint = spotifyGreen,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                } else {
                    Text(
                        text = headerText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = spotifyGreen,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                if (hintText.isNotEmpty()) {
                    Text(
                        text = hintText,
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }

                Card(
                    backgroundColor = Color.DarkGray.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        AsyncImage(
                            model = currentQuestion.track.album.images.firstOrNull()?.url ?: "",
                            contentDescription = "Image de l'album",
                            modifier = Modifier
                                .padding(8.dp)
                                .graphicsLayer {
                                    renderEffect = BlurEffect(radiusX = blurRadius.value, radiusY = blurRadius.value)
                                    clip = true
                                }
                        )
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "Quel est l'artiste de cet album ?",
                            fontSize = 20.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                OutlinedTextField(
                    value = userAnswer,
                    onValueChange = { userAnswer = it },
                    label = { Text("Tape ta réponse ici", color = Color.White) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.LightGray,
                        focusedBorderColor = spotifyGreen,
                        unfocusedBorderColor = Color.LightGray,
                        cursorColor = spotifyGreen,
                        focusedLabelColor = spotifyGreen,
                        unfocusedLabelColor = Color.LightGray
                    )
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (questionFinished) {
                            resetQuestion()
                        } else {
                            processAnswer()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = spotifyGreen,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Suivant",
                        Modifier.padding(end = 15.dp)
                    )
                    Text("Suivant")
                }

                Spacer(modifier = Modifier.height(16.dp))

                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = spotifyGreen,
                    backgroundColor = Color.Gray,
                    progress = (questionProgress + 1).toFloat() / questions.size
                )
            }
        }
    }
}
