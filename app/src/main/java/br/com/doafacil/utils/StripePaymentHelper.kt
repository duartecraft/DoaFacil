package br.com.doafacil.utils

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import br.com.doafacil.BuildConfig
import br.com.doafacil.utils.PaymentHelperInterface


interface PaymentHelperInterface {
    fun startPayment(clientSecret: String)
}

class StripePaymentHelper(private val activity: ComponentActivity) : PaymentHelperInterface {

    private var stripe: Stripe? = null
    private var paymentSheet: PaymentSheet

    init {
        val stripePublicKey = BuildConfig.STRIPE_PUBLIC_KEY

        if (stripePublicKey.isNotEmpty()) {
            Log.d("StripePaymentHelper", "🔑 Chave Stripe carregada: $stripePublicKey")
            PaymentConfiguration.init(activity, stripePublicKey)
            stripe = Stripe(activity, stripePublicKey)
        } else {
            Log.e("StripePaymentHelper", "❌ Chave Stripe não encontrada! Verifique o BuildConfig.")
        }

        paymentSheet = PaymentSheet(activity) { paymentSheetResult ->
            onPaymentSheetResult(paymentSheetResult)
        }
    }

    override fun startPayment(clientSecret: String) {
        Log.d("StripePaymentHelper", "🚀 Iniciando pagamento com ClientSecret: $clientSecret")
        paymentSheet.presentWithPaymentIntent(
            clientSecret,
            PaymentSheet.Configuration(merchantDisplayName = "DoaFácil")
        )
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> {
                Toast.makeText(activity, "Pagamento concluído!", Toast.LENGTH_LONG).show()
                Log.d("StripePaymentHelper", "✅ Pagamento concluído com sucesso!")
            }
            is PaymentSheetResult.Canceled -> {
                Toast.makeText(activity, "Pagamento cancelado.", Toast.LENGTH_SHORT).show()
                Log.d("StripePaymentHelper", "⚠️ Pagamento cancelado pelo usuário.")
            }
            is PaymentSheetResult.Failed -> {
                Toast.makeText(activity, "Erro: ${paymentSheetResult.error.localizedMessage}", Toast.LENGTH_LONG).show()
                Log.e("StripePaymentHelper", "❌ Erro no pagamento: ${paymentSheetResult.error.localizedMessage}")
            }
        }
    }
}