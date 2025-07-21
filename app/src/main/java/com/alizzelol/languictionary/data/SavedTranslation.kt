package com.alizzelol.languictionary.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa una traducción guardada en la base de datos local.
 * Cada instancia de esta clase corresponde a una fila en la tabla 'saved_translations'.
 */
@Entity(tableName = "saved_translations") // Define el nombre de la tabla en la base de datos
data class SavedTranslation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Clave primaria autogenerada para cada entrada
    val originalText: String,                         // El texto que el usuario introdujo originalmente
    val translatedText: String,                       // El texto resultante de la traducción
    val sourceLanguage: String,                       // El código del idioma de origen (ej. "es", "en")
    val targetLanguage: String,                       // El código del idioma de destino (ej. "en", "fr")
    val timestamp: Long = System.currentTimeMillis()  // Marca de tiempo de cuándo se guardó la traducción, útil para ordenar
)
