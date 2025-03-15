package br.com.doafacil.network

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class PaymentIntentRequest(
    val amount: Int,
    val currency: String = "brl"
)

data class PaymentIntentResponse(
    val clientSecret: String
)

interface StripeApi {
    @Headers("Content-Type: application/json")
    @POST("create-payment-intent")
    fun createPaymentIntent(@Body request: PaymentIntentRequest): Call<PaymentIntentResponse>

    companion object {
        fun create(): StripeApi {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://doafacil-stripe-bend-doafacil.up.railway.app/") // URL do backend
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(StripeApi::class.java)
        }
    }
}