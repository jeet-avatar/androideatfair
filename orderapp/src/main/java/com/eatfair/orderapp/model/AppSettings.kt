package com.eatfair.orderapp.model

data class AppSettings(
    val language: String = "English",
    val redCardEnabled: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val soundsEnabled: Boolean = true,
    val repeatAlertsEnabled: Boolean = false
)

data class FAQItem(
    val question: String,
    val answer: String,
    val isExpanded: Boolean = false
)