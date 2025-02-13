package com.worldine.quiz

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.worldine.quiz.api.SpotifyApi
import com.worldine.quiz.dataclass.SpotifyTrackItem
import com.worldline.quiz.QuizScreen
import com.worldline.quiz.ScoreScreen
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlinx.serialization.Serializable

@Serializable
object WelcomeRoute

@Serializable
object QuizRoute

@Serializable
data class ScoreRoute(val score: Int, val questionSize: Int)

@Composable
@Preview
fun App(navController: NavHostController = rememberNavController()) {
    val spotifyApi = remember { SpotifyApi() }
    var spotifyTrackItem by remember { mutableStateOf(emptyList<SpotifyTrackItem>()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {

                spotifyApi.initialize("", "")

                val tracks = spotifyApi.getPlaylistTracks("59WglXRaPqT4rLsuboFGVJ")
                val selectedTracks = if (tracks.size > 10) {
                    tracks.shuffled().take(10)
                } else {
                    tracks
                }
                spotifyTrackItem = selectedTracks.map { track ->
                    SpotifyTrackItem(track = track)
                }
            } catch (e: Exception) {
                println("Erreur lors de la récupération des titres : ${e.message}")
            }
        }
    }


    MaterialTheme {
        if (spotifyTrackItem.isNotEmpty()) {
            NavHost(
                navController = navController,
                startDestination = WelcomeRoute
            ) {
                composable<WelcomeRoute> {
                    WelcomeScreen(
                        onStartButtonPushed = { navController.navigate(route = QuizRoute) }
                    )
                }
                composable<QuizRoute> {
                    QuizScreen(
                        questions = spotifyTrackItem,
                        onFinishButtonPushed = { score, questionSize ->
                            navController.navigate(route = ScoreRoute(score, questionSize))
                        }
                    )
                }
                composable<ScoreRoute> { backStackEntry ->
                    val scoreRoute: ScoreRoute = backStackEntry.toRoute<ScoreRoute>()
                    ScoreScreen(
                        score = scoreRoute.score,
                        total = scoreRoute.questionSize,
                        onResetButtonPushed = { navController.navigate(route = QuizRoute) },
                        onHomeButtonPushed = { navController.navigate(route = WelcomeRoute) }
                    )
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

