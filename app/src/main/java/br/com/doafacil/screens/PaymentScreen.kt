package br.com.doafacil.screens

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.doafacil.network.PaymentIntentRequest
import br.com.doafacil.network.RetrofitInstance
import br.com.doafacil.ui.theme.DoaFacilTheme
import br.com.doafacil.utils.PaymentHelperInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    navController: NavController,
    ngoId: String,
    ngoName: String,
    stripePaymentHelper: PaymentHelperInterface
) {
    val context = LocalContext.current
    val isInPreview = LocalInspectionMode.current
    val coroutineScope = rememberCoroutineScope()

    var donationAmount by remember { mutableStateOf("") }
    var clientSecret by remember { mutableStateOf<String?>(null) }

    val availableCurrencies = listOf("BRL", "USD", "EUR", "GBP", "JPY")
    var selectedCurrency by remember { mutableStateOf("BRL") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val activity = if (!isInPreview) context as? ComponentActivity else null
    if (!isInPreview && activity == null) {
        Toast.makeText(context, "Erro ao acessar a Activity!", Toast.LENGTH_LONG).show()
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Realizar Doação") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
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
            Text("Escolha o valor da doação:", style = MaterialTheme.typography.titleMedium, fontSize = 16.sp)

            OutlinedTextField(
                value = donationAmount,
                onValueChange = { donationAmount = it },
                label = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Valor")
                        Text(
                            selectedCurrency,
                            modifier = Modifier.clickable { isDropdownExpanded = true },
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp,
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                availableCurrencies.forEach { currency ->
                    DropdownMenuItem(
                        text = { Text(currency) },
                        onClick = {
                            selectedCurrency = currency
                            isDropdownExpanded = false
                        }
                    )
                }
            }

            Text(
                text = "◆ Cada doação realizada adiciona *10 pontos* ao seu perfil!",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 6.dp)
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        Log.d("PaymentScreen", "Criando PaymentIntent para valor: $donationAmount $selectedCurrency")
                        val secret = createPaymentIntent(donationAmount.toIntOrNull() ?: 0, selectedCurrency)
                        clientSecret = secret
                        if (!isInPreview) {
                            secret?.let {
                                Log.d("PaymentScreen", "🚀 Iniciando Stripe Payment")
                                stripePaymentHelper.startPayment(it)
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = donationAmount.isNotEmpty()
            ) {
                Text(
                    text = "Doar agora",
                    fontSize = 16.sp)
            }
        }
    }
}

private suspend fun createPaymentIntent(amount: Int, currency: String): String? {
    return withContext(Dispatchers.IO) {
        try {
            val response = RetrofitInstance.api.createPaymentIntent(
                PaymentIntentRequest(amount, currency)
            ).execute()
            val clientSecret = response.body()?.clientSecret
            Log.d("PaymentScreen", "ClientSecret recebido: $clientSecret")
            clientSecret
        } catch (e: Exception) {
            Log.e("PaymentScreen", "❌ Erro ao criar PaymentIntent: ${e.localizedMessage}")
            null
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentScreenPreview() {
    val fakeStripeHelper = object : PaymentHelperInterface {
        override fun startPayment(clientSecret: String) {
            Log.d("PaymentScreenPreview", "Simulação de pagamento iniciada")
        }
    }

    DoaFacilTheme {
        PaymentScreen(
            navController = rememberNavController(),
            ngoId = "1",
            ngoName = "Amigos do Bem",
            stripePaymentHelper = fakeStripeHelper
        )
    }
}