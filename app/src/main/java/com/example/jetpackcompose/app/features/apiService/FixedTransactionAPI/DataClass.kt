package com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI

//import kotlinx.serialization.Serializable

//@Serializable


data class FixedTransaction(
    val amount: Long,
    val category_id: Int,
    val title: String,
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

data class GetFixedTransactionResponse(
    val fixedTransactionResponseList: List<FixedTransactionResponse>
)

data class FixedTransactionResponse(
    val title: String?,
    val categoryName: String,
    val amount: Long,
    val startDate: List<Int>,
    val endDate: List<Int>?,
    val fixed_transaction_id: Int,
    val category_id: Int
)



