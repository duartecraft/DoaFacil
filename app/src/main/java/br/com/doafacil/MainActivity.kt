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
import br.com.doafacil.utils.GamificationManager  // Import necessário
import br.com.doafacil.utils.StripePaymentHelper  // Import do StripePaymentHelper

class MainActivity : ComponentActivity() {
    private lateinit var stripePaymentHelper: StripePaymentHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o StripePaymentHelper antes de qualquer uso
        stripePaymentHelper = StripePaymentHelper(this, this)

        // Inicializa o GamificationManager antes de qualquer uso
        GamificationManager.init(this)

        setContent {
            DoaFacilTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    Navigation(navController, stripePaymentHelper)
                }
            }
        }
    }
}
