package com.alizzelol.languictionary.translator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TranslateViewModel : ViewModel() {

    var state by mutableStateOf(TranslateState())

}