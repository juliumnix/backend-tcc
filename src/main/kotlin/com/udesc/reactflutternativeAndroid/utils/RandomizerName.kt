package com.udesc.reactflutternativeAndroid.utils

import kotlin.random.Random

object RandomizerName {
    fun generateRandomName(size: Int): String {
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..size)
                .map { characters[Random.nextInt(0, characters.length)] }
                .joinToString("")
    }
}