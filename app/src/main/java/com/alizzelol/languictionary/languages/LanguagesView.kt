package com.alizzelol.languictionary.languages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun LanguagesView(){

    val item = listOf("English", "French", "Spanish")
    var expanded by remember{ mutableStateOf(false) }

}