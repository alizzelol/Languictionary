package com.alizzelol.languictionary

import android.content.Context
import com.alizzelol.languictionary.data.AppDatabase
import com.alizzelol.languictionary.repositories.DictionaryRepository

/**
 * Interfaz que define las dependencias que la aplicación proporcionará.
 * Esto hace que sea más fácil de probar y gestionar las dependencias.
 */
interface AppContainer {
    // Proporciona una instancia de DictionaryRepository
    val dictionaryRepository: DictionaryRepository
}

/**
 * Implementación concreta de AppContainer que proporciona las dependencias reales.
 * Utiliza un patrón Singleton para la base de datos y el repositorio
 * para asegurar que solo haya una instancia en toda la aplicación.
 */
class AppDataContainer(private val context: Context) : AppContainer {
    // Inicializa el DictionaryRepository de forma "lazy" (solo cuando se necesita).
    // Pasa el DictionaryDao obtenido de la AppDatabase.
    override val dictionaryRepository: DictionaryRepository by lazy {
        DictionaryRepository(AppDatabase.getDatabase(context).dictionaryDao())
    }
}