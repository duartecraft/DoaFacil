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

    private lateinit var stripePaymentHelper: StripePaymentHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        stripePaymentHelper = StripePaymentHelper(this)

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