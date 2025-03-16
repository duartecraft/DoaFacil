package br.com.doafacil.network

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call

interface PaymentApi {
    @POST("create-payment-intent")
    fun createPaymentIntent(@Body request: PaymentIntentRequest): Call<PaymentResponse>
}