package br.com.doafacil.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

object RetrofitInstance {
    private const val BASE_URL = "https://doafacil-stripe-bend-doafacil.up.railway.app"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: StripeApiService by lazy {
        retrofit.create(StripeApiService::class.java)
    }
}

interface StripeApiService {
    @POST("/create-payment-intent")
    suspend fun createPaymentIntent(@Body request: PaymentIntentRequest): PaymentIntentResponse
}