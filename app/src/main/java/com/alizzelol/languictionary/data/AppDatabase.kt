package com.alizzelol.languictionary.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DictionaryEntry::class], version = 2, exportSchema = false) // ¡Versión 2 y nueva entidad!
abstract class AppDatabase : RoomDatabase() {

    abstract fun dictionaryDao(): DictionaryDao // Nuevo método para el nuevo DAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "languictionary_database"
                )
                    .fallbackToDestructiveMigration() // ¡Añade esto! Borrará y recreará la DB en cambios de esquema
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}