package com.example.cupcake.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

class OrderViewModel : ViewModel() {

    var quantity by mutableStateOf(1)
    var flavor by mutableStateOf("")
    var date by mutableStateOf("")

    fun resetOrder() {
        quantity = 1
        flavor = ""
        date = ""
    }
}
