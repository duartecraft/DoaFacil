package br.com.doafacil.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.doafacil.screens.*
import br.com.doafacil.utils.StripePaymentHelper
import br.com.doafacil.screens.AgendamentoScreen
import br.com.doafacil.screens.PaymentScreen
import br.com.doafacil.screens.DonationConfirmationScreen
import br.com.doafacil.utils.GamificationManager

@Composable
fun Navigation(navController: NavHostController, stripePaymentHelper: StripePaymentHelper) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(navController)
        }
        composable(Routes.RATE) {
            RateScreen(navController)
        }

        composable(Routes.HOME) {
            HomeScreen(navController)
        }

        composable(Routes.NGO_LIST) {
            NGOListScreen(navController)
        }

        composable(Routes.NGO_LIST) {
            NGOListScreen(navController)
        }

        composable(
            route = Routes.NGO_DETAILS,
            arguments = listOf(
                navArgument("ngoId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            NGODetailsScreen(
                ngoId = backStackEntry.arguments?.getString("ngoId") ?: "",
                navController = navController
            )
        }

        composable(Routes.DONATION_HISTORY) {
            DonationHistoryScreen(
                navController,
                userPoints = GamificationManager.getPoints(),
                userLevel = GamificationManager.getLevel()
            )
        }

        composable(
            route = Routes.AGENDAMENTO,
            arguments = listOf(
                navArgument("ngoId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            AgendamentoScreen(
                ngoId = backStackEntry.arguments?.getString("ngoId") ?: "",
                navController = navController
            )
        }

        composable(
            route = Routes.PAYMENT,
            arguments = listOf(
                navArgument("ngoId") { type = NavType.StringType },
                navArgument("ngoName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val ngoId = backStackEntry.arguments?.getString("ngoId") ?: ""
            val ngoName = backStackEntry.arguments?.getString("ngoName") ?: ""
            PaymentScreen(navController, ngoId, ngoName)
        }

        composable(
            route = Routes.DONATION_CONFIRMATION,
            arguments = listOf(
                navArgument("ngoId") { type = NavType.StringType },
                navArgument("ngoName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val ngoId = backStackEntry.arguments?.getString("ngoId") ?: ""
            val ngoName = backStackEntry.arguments?.getString("ngoName") ?: ""
            DonationConfirmationScreen(navController, ngoName, ngoId)
        }
    }
}