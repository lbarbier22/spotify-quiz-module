package com.worldine.quiz

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform