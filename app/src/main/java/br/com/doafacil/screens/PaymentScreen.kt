package br.com.doafacil.screens

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
import br.com.doafacil.navigation.Routes
import br.com.doafacil.network.PaymentIntentRequest
import br.com.doafacil.network.RetrofitInstance
import br.com.doafacil.ui.theme.DoaFacilTheme
import br.com.doafacil.utils.GamificationManager
import br.com.doafacil.utils.GamificationAction
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(navController: NavController, ngoId: String, ngoName: String) {
    val context = LocalContext.current
    val isInPreview = LocalInspectionMode.current
    val coroutineScope = rememberCoroutineScope()

    var donationAmount by remember { mutableStateOf("") }
    var clientSecret by remember { mutableStateOf<String?>(null) }

    // Moedas disponíveis
    val availableCurrencies = listOf("BRL", "USD", "EUR", "GBP", "JPY")
    var selectedCurrency by remember { mutableStateOf("BRL") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val activity = if (!isInPreview) context as? ComponentActivity else null
    if (!isInPreview && activity == null) {
        Toast.makeText(context, "Erro ao acessar a Activity!", Toast.LENGTH_LONG).show()
        return
    }

    val paymentSheet = remember {
        if (!isInPreview) {
            PaymentSheet(activity!!) { result ->
                when (result) {
                    is PaymentSheetResult.Completed -> {
                        GamificationManager.addPointsForAction(GamificationAction.DONATION)
                        val newPoints = GamificationManager.getPoints()
                        Toast.makeText(
                            context,
                            "Pagamento bem-sucedido! \nVocê ganhou ${GamificationAction.DONATION.points} pontos!\nTotal: $newPoints pontos.",
                            Toast.LENGTH_LONG
                        ).show()
                        navController.navigate(Routes.donationConfirmation(ngoId, ngoName))
                    }
                    is PaymentSheetResult.Failed -> {
                        Toast.makeText(context, "Erro no pagamento: ${result.error.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
                    is PaymentSheetResult.Canceled -> {
                        Toast.makeText(context, "Pagamento cancelado.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else null
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
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary // Define a cor da seta como branca
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
                label = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Valor")
                        Text(
                            selectedCurrency,
                            modifier = Modifier.clickable { isDropdownExpanded = true },
                            color = MaterialTheme.colorScheme.primary
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
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        val secret = createPaymentIntent(donationAmount.toIntOrNull() ?: 0, selectedCurrency)
                        clientSecret = secret
                        if (!isInPreview) {
                            secret?.let {
                                paymentSheet?.presentWithPaymentIntent(
                                    it,
                                    PaymentSheet.Configuration(
                                        merchantDisplayName = "DoaFácil",
                                        allowsDelayedPaymentMethods = true
                                    )
                                )
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = donationAmount.isNotEmpty()
            ) {
                Text("Doar agora")
            }
        }
    }
}

/**
 * Função para criar um PaymentIntent com a Stripe.
 */
private suspend fun createPaymentIntent(amount: Int, currency: String): String? {
    return withContext(Dispatchers.IO) {
        try {
            val response = RetrofitInstance.api.createPaymentIntent(
                PaymentIntentRequest(amount, currency)
            ).execute()
            response.body()?.clientSecret
        } catch (e: Exception) {
            null
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
            ngoName = "Amigos do Bem"
        )
    }
}
