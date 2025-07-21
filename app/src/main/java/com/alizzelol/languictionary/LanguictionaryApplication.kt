package com.alizzelol.languictionary

import android.app.Application

/**
 * Clase Application personalizada para inicializar y mantener el contenedor de dependencias.
 * Esto asegura que el AppContainer esté disponible en toda la aplicación.
 */
class LanguictionaryApplication : Application() {

    // 'lateinit' porque se inicializará en onCreate, antes de que se use.
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        // Inicializa el contenedor de dependencias pasándole el contexto de la aplicación.
        container = AppDataContainer(this)
    }
}