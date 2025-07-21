package com.alizzelol.languictionary.translator

data class TranslateState(
    val textToTranslate: String = "",
    val isDownloading: Boolean = false,
    val translatedEnglish: String = "", // Nuevo campo para la traducción en inglés
    val translatedSpanish: String = "", // Nuevo campo para la traducción en español
    val translatedFrench: String = ""   // Nuevo campo para la traducción en francés
)