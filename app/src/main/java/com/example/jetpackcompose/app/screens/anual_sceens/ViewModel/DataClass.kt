package com.example.jetpackcompose.app.screens.anual_sceens.ViewModel

import com.example.jetpackcompose.app.features.inputFeatures.TransactionType
//import kotlinx.serialization.Serializable

//@Serializable
data class PeriodicTransaction(
    val title: String,
    val startDate: String,
    val endDate: String,
    val note: String,
    val amount: Long,
    val category: String,
    val type: TransactionType // Thêm thuộc tính type
)

