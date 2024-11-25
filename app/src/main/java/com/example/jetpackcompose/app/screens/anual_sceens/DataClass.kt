package com.example.jetpackcompose.app.screens.anual_sceens

import com.example.jetpackcompose.app.features.inputFeatures.TransactionType
import kotlinx.serialization.Serializable
import com.google.gson.Gson

@Serializable
data class PeriodicTransaction(
    val title: String,
    val startDate: String,
    val endDate: String,
    val note: String,
    val amount: Long,
    val category: String,
    val type: TransactionType // Thêm thuộc tính type
)

fun convertToJson(transaction: PeriodicTransaction): String {
    val gson = Gson()
    return gson.toJson(transaction)
}

fun sendToApi(json: String) {
    val url = "https://api.example.com/transactions"
}

