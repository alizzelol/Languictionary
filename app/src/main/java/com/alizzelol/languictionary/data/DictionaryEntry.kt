package com.alizzelol.languictionary.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dictionary_entries")
data class DictionaryEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val englishWord: String = "",       // Palabra en inglés
    val spanishWord: String = "",       // Palabra en español
    val frenchWord: String = "",        // Palabra en francés
    val timestamp: Long = System.currentTimeMillis() // Marca de tiempo de cuándo se guardó
)
