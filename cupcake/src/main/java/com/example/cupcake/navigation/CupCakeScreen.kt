package com.example.cupcake.navigation

sealed class CupcakeScreen(val route: String) {
    object Start : CupcakeScreen("start")
    object Flavor : CupcakeScreen("flavor")
    object Pickup : CupcakeScreen("pickup")
    object Summary : CupcakeScreen("summary")
}
