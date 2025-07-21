// Ruta del archivo: com.alizzelol.languictionary.data.TranslationRepository.kt
package com.alizzelol.languictionary.repositories

import com.alizzelol.languictionary.data.DictionaryDao
import com.alizzelol.languictionary.data.DictionaryEntry
import kotlinx.coroutines.flow.Flow

class DictionaryRepository(private val dictionaryDao: DictionaryDao) { // Ahora usa DictionaryDao

    val allDictionaryEntries: Flow<List<DictionaryEntry>> = dictionaryDao.getAllDictionaryEntries() // Obtiene DictionaryEntry

    suspend fun insert(entry: DictionaryEntry) { // Inserta DictionaryEntry
        dictionaryDao.insert(entry)
    }

    suspend fun delete(entryId: Int) { // Elimina DictionaryEntry
        dictionaryDao.deleteById(entryId)
    }
}