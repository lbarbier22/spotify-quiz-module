package com.worldine.quiz.dataclass

import com.worldine.quiz.api.SpotifyTrack

data class Question(
    val id: Int,
    val track: SpotifyTrack
)