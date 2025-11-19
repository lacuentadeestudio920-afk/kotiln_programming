package com.example.cupcake

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cupcake.navigation.CupcakeScreen
import com.example.cupcake.screen.*
import com.example.cupcake.viewmodel.OrderViewModel

@Composable
fun CupcakeNavGraph(
    viewModel: OrderViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = CupcakeScreen.Start.route
    ) {

        composable(CupcakeScreen.Start.route) {
            StartScreen(
                onNext = { navController.navigate(CupcakeScreen.Flavor.route) }
            )
        }

        composable(CupcakeScreen.Flavor.route) {
            FlavorScreen(
                viewModel = viewModel,
                onNext = { navController.navigate(CupcakeScreen.Pickup.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(CupcakeScreen.Pickup.route) {
            PickupScreen(
                viewModel = viewModel,
                onNext = { navController.navigate(CupcakeScreen.Summary.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(CupcakeScreen.Summary.route) {
            SummaryScreen(
                viewModel = viewModel,
                onRestart = {
                    viewModel.resetOrder()
                    navController.popBackStack(
                        CupcakeScreen.Start.route,
                        inclusive = false
                    )
                }
            )
        }
    }
}
