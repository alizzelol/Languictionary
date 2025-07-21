package com.alizzelol.languictionary.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Clase abstracta que representa la base de datos Room de la aplicación.
 * Es el punto de acceso principal para interactuar con los DAOs y la base de datos subyacente.
 */
@Database(entities = [SavedTranslation::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Retorna una instancia del DAO para las operaciones con SavedTranslation.
     */
    abstract fun savedTranslationDao(): SavedTranslationDao

    companion object {
        @Volatile // Asegura que la instancia de la base de datos sea siempre la misma y no se cachee incorrectamente
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la única instancia de la base de datos.
         * Si la instancia no existe, la crea de forma segura en un bloque sincronizado.
         * @param context El contexto de la aplicación para construir la base de datos.
         * @return La instancia de AppDatabase.
         */
        fun getDatabase(context: Context): AppDatabase {
            // Si INSTANCE no es nula, entonces se ha inicializado y se devuelve.
            // Si es nula, entonces se inicializa en un bloque sincronizado.
            return INSTANCE ?: synchronized(this) { // Bloquea este código para que solo un hilo pueda ejecutarlo a la vez
                val instance = Room.databaseBuilder(
                    context.applicationContext, // Usa el contexto de la aplicación para evitar fugas de memoria
                    AppDatabase::class.java,
                    "languictionary_database" // Nombre del archivo de la base de datos en el dispositivo
                )
                    // .fallbackToDestructiveMigration() // Opcional: si cambias la versión y no tienes migraciones, esto borra y recrea la DB
                    .build()
                INSTANCE = instance
                instance // Retorna la instancia
            }
        }
    }
}