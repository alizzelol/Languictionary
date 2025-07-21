package com.alizzelol.languictionary.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DictionaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: DictionaryEntry) // Ahora inserta DictionaryEntry

    @Query("SELECT * FROM dictionary_entries ORDER BY timestamp DESC")
    fun getAllDictionaryEntries(): Flow<List<DictionaryEntry>> // Ahora obtiene DictionaryEntry

    @Query("DELETE FROM dictionary_entries WHERE id = :entryId")
    suspend fun deleteById(entryId: Int) // Ahora elimina DictionaryEntry por ID
}