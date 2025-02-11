package com.worldine.quiz

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.worldine.quiz.dataclass.Answer
import com.worldine.quiz.dataclass.Question
import com.worldline.quiz.QuestionScreen
import com.worldline.quiz.ScoreScreen
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
    val questions: List<Question> = listOf(
        Question(
            id = 1,
            label = "Quelle est la capitale de la France ?",
            correctAnswerId = 1,
            answers = listOf(
                Answer(id = 1, label = "Paris"),
                Answer(id = 2, label = "Lyon"),
                Answer(id = 3, label = "Marseille"),
                Answer(id = 4, label = "4")
            ),
            albumImageResId = 1
        ),
        Question(
            id = 2,
            label = "Combien de continents y a-t-il ?",
            correctAnswerId = 2,
            answers = listOf(
                Answer(id = 1, label = "5"),
                Answer(id = 2, label = "7"),
                Answer(id = 3, label = "6"),
                Answer(id = 4, label = "4")
            ),
            albumImageResId = 2
        )
    )

    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = WelcomeRoute,
        ) {
            composable<WelcomeRoute> {
                WelcomeScreen(
                    onStartButtonPushed = {
                        navController.navigate(route = QuizRoute)
                    }
                )
            }
            composable<QuizRoute> {
                QuestionScreen(
                    questions = questions,
                    onFinishButtonPushed = { score: Int, questionSize: Int ->
                        navController.navigate(route = ScoreRoute(score, questionSize))
                    }
                )
            }
            composable<ScoreRoute> { backStackEntry ->
                val scoreRoute: ScoreRoute = backStackEntry.toRoute<ScoreRoute>()
                ScoreScreen(
                    score = scoreRoute.score,
                    total = scoreRoute.questionSize,
                    onResetButtonPushed = {
                        navController.navigate(route = QuizRoute)
                    },
                    onHomeButtonPushed = {
                        navController.navigate(route = WelcomeRoute)
                    }
                )
            }
        }
    }
}

