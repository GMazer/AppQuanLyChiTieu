package com.example.jetpackcompose.app.screens.anual_sceens.ViewModel

import com.example.jetpackcompose.app.features.inputFeatures.TransactionType
//import kotlinx.serialization.Serializable

//@Serializable


data class FixedTransaction(
    val amount: Long,
    val category_id: Int,
    val title: String,
    val type: String, // "income" or "expense"
    val repeat_frequency: RepeatFrequency, // "weekly", "monthly", etc.
    val start_date: String,
    val end_date: String
)

enum class RepeatFrequency(val displayName: String) {
    daily("daily"),
    weekly("weekly"),
    monthly("monthly"),
    yearly("yearly");

    // Hàm để lấy tên hiển thị của enum
    override fun toString(): String {
        return displayName
    }
}



