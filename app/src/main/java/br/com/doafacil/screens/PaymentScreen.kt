package br.com.doafacil.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.doafacil.navigation.Routes
import br.com.doafacil.ui.theme.DoaFacilTheme
import br.com.doafacil.utils.GamificationManager
import br.com.doafacil.utils.GamificationAction
import br.com.doafacil.utils.StripePaymentHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    navController: NavController,
    ngoId: String,
    ngoName: String,
    stripePaymentHelper: StripePaymentHelper
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var donationAmount by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Realizar Doação") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Escolha o valor da doação:", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = donationAmount,
                onValueChange = { donationAmount = it },
                label = { Text("Valor (R$)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val amount = donationAmount.toIntOrNull()
                    if (amount == null || amount <= 0) {
                        Toast.makeText(context, "Insira um valor válido!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    coroutineScope.launch {
                        stripePaymentHelper.createPaymentIntent(
                            amount,
                            "brl",
                            onSuccess = {
                                stripePaymentHelper.startPaymentFlow()
                            },
                            onError = { error ->
                                Toast.makeText(context, "Erro ao iniciar pagamento: $error", Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = donationAmount.isNotEmpty()
            ) {
                Text("Doar Agora")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentScreenPreview() {
    DoaFacilTheme {
        PaymentScreen(
            navController = rememberNavController(),
            ngoId = "1",
            ngoName = "Amigos do Bem",
            stripePaymentHelper = StripePaymentHelper(LocalContext.current, androidx.activity.ComponentActivity())
        )
    }
}
