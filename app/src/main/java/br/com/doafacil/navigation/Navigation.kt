package br.com.doafacil.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.doafacil.screens.*
import br.com.doafacil.utils.PaymentHelperInterface

@Composable
fun Navigation(navController: NavHostController, stripePaymentHelper: PaymentHelperInterface) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(navController)
        }

        composable(Routes.HOME) {
            HomeScreen(navController)
        }

        composable(Routes.NGO_LIST) {
            NGOListScreen(navController)
        }

        composable(
            route = Routes.NGO_DETAILS,
            arguments = listOf(navArgument("ngoId") { type = NavType.StringType })
        ) { backStackEntry ->
            NGODetailsScreen(
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

            PaymentScreen(navController, ngoId, ngoName, stripePaymentHelper)
        }

        composable(Routes.DONATION_HISTORY) {
            DonationHistoryScreen(navController, userPoints = 100, userLevel = "Ouro")
        }

        // 🚀 ADICIONADO DE VOLTA: AgendamentoScreen
        composable(
            route = Routes.AGENDAMENTO,
            arguments = listOf(navArgument("ngoId") { type = NavType.StringType })
        ) { backStackEntry ->
            AgendamentoScreen(
                ngoId = backStackEntry.arguments?.getString("ngoId") ?: "",
                navController = navController
            )
        }

        // 🚀 ADICIONADO DE VOLTA: RateScreen
        composable(Routes.RATE) {
            RateScreen(navController)
        }
    }
}