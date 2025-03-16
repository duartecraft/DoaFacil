package br.com.doafacil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import br.com.doafacil.navigation.Navigation
import br.com.doafacil.ui.theme.DoaFacilTheme
import br.com.doafacil.utils.StripePaymentHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DoaFacilTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // 🔹 Criando uma instância de StripePaymentHelper
                    val stripePaymentHelper = StripePaymentHelper(this, this)

                    // 🔹 Passando stripePaymentHelper para Navigation
                    Navigation(navController, stripePaymentHelper)
                }
            }
        }
    }
}