package com.alizzelol.languictionary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
                    // Obtiene el ViewModel aquí. Usamos viewModel() en lugar de by viewModels()
                    // cuando el ViewModel se utiliza dentro de un Composable en el NavGraph.
                    val viewModel: TranslateViewModel = viewModel()
                    TranslateView(viewModel)
                }
            }
        }
    }
}

