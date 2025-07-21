package com.alizzelol.languictionary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alizzelol.languictionary.translator.TranslateView
import com.alizzelol.languictionary.translator.TranslateViewModel

//Implementando MLKit
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // El NavController gestiona la pila de back y la navegación
            val navController = rememberNavController()

            // El NavHost es el contenedor de nuestras pantallas navegables
            NavHost(
                navController = navController,
                startDestination = "welcome_screen" // Define la pantalla de inicio
            ) {
                // Define la ruta para la pantalla de bienvenida
                composable("welcome_screen") {
                    WelcomeScreen(
                        // Cuando se presiona el botón, navega a la pantalla del traductor
                        onNavigateToTranslator = {
                            navController.navigate("translate_screen")
                        }
                    )
                }

                // Define la ruta para la pantalla del traductor
                composable("translate_screen") {
                    // Asegúrate de que appContainer esté disponible aquí.
                    // Necesitarás la línea 'val appContainer = (application as LanguictionaryApplication).container'
                    // en tu onCreate, antes de setContent.
                    val appContainer = (application as LanguictionaryApplication).container // Repite o asegura que esté accesible

                    // 2. Inicializar el ViewModel usando el factory que le proporciona el repositorio
                    val translateViewModel: TranslateViewModel = viewModel(
                        factory = TranslateViewModel.provideFactory(appContainer.dictionaryRepository)
                    )
                    Scaffold( // Envuelve TranslateView en un Scaffold para el FloatingActionButton
                        floatingActionButton = {
                            FloatingActionButton(onClick = { navController.navigate("dictionary_entries_screen") }) {
                                Icon(Icons.Default.History, contentDescription = "Ver tarjetas de estudio")
                            }
                        }
                    ) { paddingValues ->
                        TranslateView(translateViewModel, paddingValues)
                    }
                }
            }
        }
    }
}

