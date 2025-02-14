package com.worldline.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.worldline.quiz.api.SpotifyApi
import com.worldline.quiz.dataclass.SpotifyTrackItem
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlinx.serialization.Serializable

@Serializable
object WelcomeRoute

@Serializable
object PlaylistSearchRoute

@Serializable
object QuizRoute

@Serializable
data class ScoreRoute(val score: Int, val questionSize: Int)

@Composable
@Preview
fun App(navController: NavHostController = rememberNavController()) {
    val spotifyApi = remember { SpotifyApi() }
    var quizTracks by remember { mutableStateOf(emptyList<SpotifyTrackItem>()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                spotifyApi.initialize("", "")
            } catch (e: Exception) {
                println("Erreur lors de l'initialisation de l'API : ${e.message}")
            }
        }
    }

    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = WelcomeRoute
        ) {
            composable<WelcomeRoute> {
                WelcomeScreen(
                    onStartButtonPushed = { navController.navigate(PlaylistSearchRoute) }
                )
            }
            composable<PlaylistSearchRoute> {
                PlaylistSearchScreen(
                    spotifyApi = spotifyApi,
                    onPlaylistSelected = { playlistId ->
                        coroutineScope.launch {
                            try {
                                val tracks = spotifyApi.getPlaylistTracks(playlistId)
                                val selectedTracks = if (tracks.size > 10) {
                                    tracks.shuffled().take(10)
                                } else {
                                    tracks
                                }
                                quizTracks = selectedTracks.map { track ->
                                    SpotifyTrackItem(track = track)
                                }
                                navController.navigate(QuizRoute)
                            } catch (e: Exception) {
                                println("Erreur lors de la récupération des titres : ${e.message}")
                            }
                        }
                    }
                )
            }
            composable<QuizRoute> {
                if (quizTracks.isNotEmpty()) {
                    QuizScreen(
                        questions = quizTracks,
                        onFinishButtonPushed = { score, questionSize ->
                            navController.navigate(ScoreRoute(score, questionSize))
                        }
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
            composable<ScoreRoute> { backStackEntry ->
                val scoreRoute: ScoreRoute = backStackEntry.toRoute<ScoreRoute>()
                ScoreScreen(
                    score = scoreRoute.score,
                    total = scoreRoute.questionSize,
                    onPlaylistSearchButtonPushed = { navController.navigate(PlaylistSearchRoute) },
                    onHomeButtonPushed = { navController.navigate(WelcomeRoute) }
                )
            }
        }
    }
}
