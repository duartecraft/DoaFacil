package br.com.doafacil.network

data class PaymentIntentRequest(
    val amount: Int,
    val currency: String
)
