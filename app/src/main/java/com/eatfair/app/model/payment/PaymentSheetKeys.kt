package com.eatfair.app.model.payment

data class PaymentSheetKeys(
    val paymentIntent: String,
    val ephemeralKey: String,
    val customer: String,
    val publishableKey: String
)
