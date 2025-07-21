package com.alizzelol.languictionary.translator

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button // Importar Button
import androidx.compose.material3.ButtonDefaults // Importar ButtonDefaults

@Composable
fun TranslateView(viewModel: TranslateViewModel, paddingValues: PaddingValues){

    val state = viewModel.state
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val itemsSelection = viewModel.itemSelection

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(verticalAlignment = Alignment.CenterVertically){

            DropdownLang(
                itemSelection = itemsSelection,
                selectedIndex = viewModel.indexSource,
                expand = viewModel.expandSource,
                onClickExpanded = { viewModel.onSourceExpanded(true) },
                onClickDismiss = { viewModel.onSourceExpanded(false) },
                onClickItem = { index ->
                    viewModel.onSourceSelected(index)
                }
            )

            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "",
                modifier = Modifier.padding(start = 15.dp, end = 15.dp)
            )

            DropdownLang(
                itemSelection = itemsSelection,
                selectedIndex = viewModel.indexTarget,
                expand = viewModel.expandTarget,
                onClickExpanded = { viewModel.onTargetExpanded(true) },
                onClickDismiss = { viewModel.onTargetExpanded(false) },
                onClickItem = { index ->
                    viewModel.onTargetSelected(index)
                }
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = state.textToTranslate,
            onValueChange = {viewModel.onValue(it)},
            label = {Text(text="Escribe...")},
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.onTranslateForDictionary(state.textToTranslate, context) // Llama a la nueva función
                    keyboardController?.hide()
                }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if(state.isDownloading){
            CircularProgressIndicator()
            Text(text = "Descargando modelos...")
        } else {
            // Mostrar las traducciones en los tres idiomas
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Inglés: ${state.translatedEnglish}", modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Español: ${state.translatedSpanish}", modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Francés: ${state.translatedFrench}", modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para guardar la entrada del diccionario
            Button(
                onClick = {
                    // Solo permite guardar si hay al menos una traducción no vacía
                    if (state.translatedEnglish.isNotBlank() || state.translatedSpanish.isNotBlank() || state.translatedFrench.isNotBlank()) {
                        viewModel.saveDictionaryEntry(
                            english = state.translatedEnglish,
                            spanish = state.translatedSpanish,
                            french = state.translatedFrench
                        )
                        Toast.makeText(context, "Entrada guardada para estudio", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "No hay traducción para guardar", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Color de ejemplo
            ) {
                Icon(Icons.Default.Save, contentDescription = "Guardar para estudio")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar para Estudio")
            }
        }
    }
}