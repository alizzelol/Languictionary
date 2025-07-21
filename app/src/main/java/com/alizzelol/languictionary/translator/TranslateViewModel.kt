// Ruta del archivo: com.alizzelol.languictionary.translator.TranslateViewModel.kt
package com.alizzelol.languictionary.translator

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.alizzelol.languictionary.data.DictionaryEntry
import com.alizzelol.languictionary.repositories.DictionaryRepository
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.nl.translate.TranslateLanguage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await // Importar await para las operaciones de ML Kit

class TranslateViewModel(private val repository: DictionaryRepository) : ViewModel() {

    var state by mutableStateOf(TranslateState())
        private set

    var indexSource by mutableIntStateOf(0)
        private set
    var indexTarget by mutableIntStateOf(1)
        private set
    var expandSource by mutableStateOf(false)
        private set
    var expandTarget by mutableStateOf(false)
        private set

    val languageOptions = listOf(
        TranslateLanguage.SPANISH,
        TranslateLanguage.ENGLISH,
        TranslateLanguage.FRENCH,
    )

    val itemSelection = listOf(
        "SPANISH",
        "ENGLISH",
        "FRENCH",
    )

    fun onSourceExpanded(expanded: Boolean) { expandSource = expanded }
    fun onTargetExpanded(expanded: Boolean) { expandTarget = expanded }

    fun onSourceSelected(index: Int) {
        indexSource = index
        expandSource = false
    }

    fun onTargetSelected(index: Int) {
        indexTarget = index
        expandTarget = false
    }

    fun getCurrentSourceLang(): String {
        return languageOptions[indexSource]
    }

    fun getCurrentTargetLang(): String {
        return languageOptions[indexTarget]
    }

    fun onValue(text: String) {
        state = state.copy(textToTranslate = text)
    }

    fun onTranslateForDictionary(text: String, context: Context) {
        if (text.isBlank()) {
            state = state.copy(
                translatedEnglish = "",
                translatedSpanish = "",
                translatedFrench = ""
            )
            return
        }

        val sourceLangCode = getCurrentSourceLang()
        val targetLangCode = getCurrentTargetLang()

        // Los otros dos idiomas para traducir, excluyendo el origen y el destino
        val otherLangCodes = languageOptions.filter { it != sourceLangCode && it != targetLangCode }
        val targetLangCode1 = otherLangCodes.getOrNull(0)
        val targetLangCode2 = otherLangCodes.getOrNull(1)

        state = state.copy(isDownloading = true)

        viewModelScope.launch {
            val translations = mutableMapOf<String, String>()
            var allModelsDownloaded = true

            // Si el texto original es en inglés, español o francés, se considera la "traducción" a sí mismo.
            // Esto es para que el diccionario muestre la palabra original en su campo correspondiente.
            when (sourceLangCode) {
                TranslateLanguage.ENGLISH -> translations[TranslateLanguage.ENGLISH] = text
                TranslateLanguage.SPANISH -> translations[TranslateLanguage.SPANISH] = text
                TranslateLanguage.FRENCH -> translations[TranslateLanguage.FRENCH] = text
            }

            // Traducir al idioma de destino seleccionado
            if (targetLangCode != sourceLangCode) { // Solo si el destino no es el mismo que el origen
                val translatorTarget = getTranslator(sourceLangCode, targetLangCode)
                val resultTarget = translateText(translatorTarget, text, targetLangCode, context) // Pasa targetLangCode
                if (resultTarget != null) {
                    translations[targetLangCode] = resultTarget
                } else {
                    allModelsDownloaded = false
                }
            }


            // Traducir al primer idioma objetivo restante
            if (targetLangCode1 != null && targetLangCode1 != sourceLangCode) {
                val translator1 = getTranslator(sourceLangCode, targetLangCode1)
                val result1 = translateText(translator1, text, targetLangCode1, context) // Pasa targetLangCode1
                if (result1 != null) {
                    translations[targetLangCode1] = result1
                } else {
                    allModelsDownloaded = false
                }
            }

            // Traducir al segundo idioma objetivo restante
            if (targetLangCode2 != null && targetLangCode2 != sourceLangCode) {
                val translator2 = getTranslator(sourceLangCode, targetLangCode2)
                val result2 = translateText(translator2, text, targetLangCode2, context) // Pasa targetLangCode2
                if (result2 != null) {
                    translations[targetLangCode2] = result2
                } else {
                    allModelsDownloaded = false
                }
            }


            state = state.copy(
                isDownloading = false,
                translatedEnglish = translations[TranslateLanguage.ENGLISH] ?: "",
                translatedSpanish = translations[TranslateLanguage.SPANISH] ?: "",
                translatedFrench = translations[TranslateLanguage.FRENCH] ?: ""
            )

            if (!allModelsDownloaded) {
                Toast.makeText(context, "Algunos modelos no se pudieron descargar o traducir. Intenta de nuevo.", Toast.LENGTH_LONG).show()
            }
        }
    }

    // El método translateText ahora recibe el 'displayLanguageCode' para usar en el Toast
    private suspend fun translateText(
        translator: Translator,
        text: String,
        displayLanguageCode: String, // Nuevo parámetro para el nombre del idioma en el Toast
        context: Context
    ): String? {
        return try {
            val conditions = DownloadConditions.Builder().requireWifi().build()
            translator.downloadModelIfNeeded(conditions).addOnSuccessListener {
                Toast.makeText(context, "Modelo para $displayLanguageCode descargado correctamente", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Error en la descarga del modelo de $displayLanguageCode: ${it.message}", Toast.LENGTH_SHORT).show()
            }.await()

            translator.translate(text).await()
        } catch (e: Exception) {
            Toast.makeText(context, "Error al traducir a $displayLanguageCode: ${e.message}", Toast.LENGTH_SHORT).show()
            null
        } finally {
            translator.close()
        }
    }

    private fun getTranslator(sourceLang: String, targetLang: String): Translator {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLang)
            .setTargetLanguage(targetLang)
            .build()
        return Translation.getClient(options)
    }

    fun saveDictionaryEntry(english: String, spanish: String, french: String) {
        viewModelScope.launch {
            if (english.isNotBlank() || spanish.isNotBlank() || french.isNotBlank()) {
                val entry = DictionaryEntry(
                    englishWord = english,
                    spanishWord = spanish,
                    frenchWord = french
                )
                repository.insert(entry)
            }
        }
    }

    companion object {
        fun provideFactory(repository: DictionaryRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(TranslateViewModel::class.java)) {
                        return TranslateViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}