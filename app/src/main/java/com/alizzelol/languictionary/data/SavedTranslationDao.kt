package com.alizzelol.languictionary.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow // Importa Flow para observar cambios en tiempo real

/**
 * Interfaz DAO (Data Access Object) para la entidad SavedTranslation.
 * Define los métodos para interactuar con la tabla 'saved_translations' en la base de datos.
 */
@Dao // Anotación que marca esta interfaz como un DAO de Room
interface SavedTranslationDao {

    /**
     * Inserta una nueva traducción guardada en la base de datos.
     * Si ya existe una traducción con el mismo ID, la reemplaza.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(translation: SavedTranslation)

    /**
     * Obtiene todas las traducciones guardadas de la base de datos.
     * Los resultados se ordenan por la marca de tiempo en orden descendente (las más recientes primero).
     * Retorna un Flow, lo que permite observar los cambios en la base de datos en tiempo real.
     */
    @Query("SELECT * FROM saved_translations ORDER BY timestamp DESC")
    fun getAllSavedTranslations(): Flow<List<SavedTranslation>>

    /**
     * Elimina una traducción guardada de la base de datos por su ID.
     */
    @Query("DELETE FROM saved_translations WHERE id = :translationId")
    suspend fun deleteById(translationId: Int)
}