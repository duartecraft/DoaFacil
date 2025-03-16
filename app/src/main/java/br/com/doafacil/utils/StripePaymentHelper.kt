package br.com.doafacil.utils

import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class StripePaymentHelper(private val context: Context, activity: ComponentActivity) {
    private var stripe: Stripe? = null
    private var paymentSheet: PaymentSheet
    private var paymentIntentClientSecret: String? = null

    init {
        // Inicializa Stripe com uma chave vazia (pois ela será obtida pelo backend)
        PaymentConfiguration.init(context, "pk_test_dummy_key")
        stripe = Stripe(context, PaymentConfiguration.getInstance(context).publishableKey)

        // Inicializa a PaymentSheet corretamente
        paymentSheet = PaymentSheet(activity) { paymentSheetResult ->
            onPaymentSheetResult(paymentSheetResult)
        }
    }

    fun createPaymentIntent(amount: Int, currency: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val backendUrl = "https://doafacil-stripe-bend-doafacil.up.railway.app/create-payment-intent"

                val url = URL(backendUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonInputString = """
                {
                    "amount": $amount,
                    "currency": "$currency"
                }
                """.trimIndent()

                connection.outputStream.use { os ->
                    os.write(jsonInputString.toByteArray())
                    os.flush()
                }

                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonResponse = JSONObject(response)

                paymentIntentClientSecret = jsonResponse.getString("clientSecret")

                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e.localizedMessage ?: "Erro desconhecido ao criar PaymentIntent")
                }
            }
        }
    }

    fun startPaymentFlow() {
        paymentIntentClientSecret?.let { secret ->
            paymentSheet.presentWithPaymentIntent(secret)
        } ?: Toast.makeText(context, "Erro: ClientSecret não encontrado!", Toast.LENGTH_SHORT).show()
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> {
                Toast.makeText(context, "Pagamento concluído com sucesso!", Toast.LENGTH_LONG).show()
            }
            is PaymentSheetResult.Canceled -> {
                Toast.makeText(context, "Pagamento cancelado.", Toast.LENGTH_SHORT).show()
            }
            is PaymentSheetResult.Failed -> {
                Toast.makeText(context, "Erro no pagamento: ${paymentSheetResult.error.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }
}