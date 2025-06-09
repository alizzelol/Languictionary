package com.alizzelol.languictionary.languages

import androidx.compose.runtime.Composable

@Composable
fun getStrings() : List<Map<String, String>>{
    val en = mapOf(
        "title" to "Hello World",
        "subtitle" to "The World is yours"
    )
    val es = mapOf(
        "title" to "Hola Mundo",
        "subtitle" to "El mundo es tuyo"
    )
    val fr = mapOf(
        "title" to "Bonjour le Monde",
        "subtitle" to "Le monde est à toi"
    )
    return listOf(
        en, es, fr
    )
}