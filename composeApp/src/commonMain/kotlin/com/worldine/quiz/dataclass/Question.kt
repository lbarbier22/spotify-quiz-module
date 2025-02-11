package com.worldine.quiz.dataclass

data class Question(
    val id: Int,
    val label: String,
    val correctAnswerId: Int,
    val answers: List<Answer>,
    val albumImageResId: Int // URL de l’image de l’album
)