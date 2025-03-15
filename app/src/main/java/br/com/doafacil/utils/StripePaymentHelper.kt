package br.com.doafacil.utils

import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import br.com.doafacil.BuildConfig
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
    private var stripe: Stripe
    private var paymentSheet: PaymentSheet
    private var paymentIntentClientSecret: String? = null

    init {
        val stripePublicKey = BuildConfig.STRIPE_PUBLIC_KEY.takeIf { it.isNotEmpty() }
            ?: throw IllegalStateException("⚠ STRIPE_PUBLIC_KEY não configurado corretamente!")

        // Inicializa Stripe com a chave pública
        PaymentConfiguration.init(context, stripePublicKey)
        stripe = Stripe(context, PaymentConfiguration.getInstance(context).publishableKey)

        // 🔹 **Correção:** Agora `activity` é um `ComponentActivity`
        paymentSheet = PaymentSheet(activity, ::onPaymentSheetResult)
    }

    fun createPaymentIntent(amount: Int, currency: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 🔹 Certifique-se de que a URL do backend está correta
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


