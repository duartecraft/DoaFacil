package br.com.doafacil.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.doafacil.screens.*

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(navController)
        }
        
        composable(Routes.NGO_LIST) {
            NGOListScreen(navController)
        }
        
        composable(
            route = Routes.NGO_DETAILS,
            arguments = listOf(
                navArgument("ngoId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            NGODetailsScreen(
                ngoId = backStackEntry.arguments?.getString("ngoId") ?: "",
                navController = navController
            )
        }
        
        composable(Routes.DONATION_HISTORY) {
            DonationHistoryScreen(navController)
        }

        composable(
            route = Routes.AGENDAMENTO,
            arguments = listOf(
                navArgument("ngoId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            AgendamentoScreen(
                ngoId = backStackEntry.arguments?.getString("ngoId") ?: "",
                navController = navController
            )
        }
    }
} 