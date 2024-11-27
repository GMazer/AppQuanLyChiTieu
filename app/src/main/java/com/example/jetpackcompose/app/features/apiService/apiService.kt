package com.example.jetpackcompose.app.network

import com.example.jetpackcompose.app.features.inputFeatures.Transaction
import com.example.jetpackcompose.app.screens.anual_sceens.ViewModel.PeriodicTransaction
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

object BaseURL {
    val baseUrl = "https://0502-2401-d800-9fff-cb7b-1cb2-9270-9d95-a561.ngrok-free.app"
}

data class RegistrationData(
    val phone_number: String,
    val email: String,
    val password: String,
    val retype_password: String
)

data class LoginData(
    val phone_number: String,
    val password: String,
)

data class LoginResponse(
    val status: String,
    val message: String  // Giả sử API trả về một trường 'token'
)

data class RegistrationResponse(
    val status: String,  // Trạng thái đăng ký (thành công hoặc thất bại)
    val message: String  // Hoặc bất kỳ thông tin nào bạn muốn từ API sau khi đăng ký
)

data class FixedIncome(
    val title: String,
    val amount: Long,
    val category: String,
    val date: String
)

data class FixedExpense(
    val title: String,
    val amount: Long,
    val category: String,
    val date: String
)

data class PostTransactionResponse(
    val categoryName: String,
    val amount: Double,
    val transactionDate: List<Int>,
    val note: String,
    val type: String,
    val transactionId: Int
)

data class ApiResponse(
    val status: String,
    val message: String
)

data class TransactionResponse(
    val dailyTransactions: Map<String, DailyTransaction>,
    val totalIncome: Long,
    val totalExpense: Long,
    val balance: Long,
    val transactions: List<TransactionDetail>
) {

    data class DailyTransaction(
        val totalDailyIncome: Long,
        val totalDailyExpense: Long
    )


    data class TransactionDetail(
        val categoryName: String,
        val amount: Long,
        val transactionDate: List<Int>,
        val note: String?,
        val type: String?,
        val transaction_id: Int
    )
}

interface ApiService {

    // API cho đăng ký
    @POST("/api/users/register")
    suspend fun register(@Body registrationData: RegistrationData): Response<RegistrationResponse>

    /// API cho đăng nhập
    @POST("/api/users/login")
    suspend fun login (@Body LoginData: LoginData): Response<LoginResponse>


    // API cho giao dịch
    @GET("api/finance")
    suspend fun getTransactions(@Header("Authorization") token: String, @Query("month") month: Int, @Query("year") year: Int): Response<TransactionResponse>

    //API cho nhập transaction
    @POST("api/transactions")
    suspend fun postTransaction(@Header("Authorization") token: String, @Body transaction: Transaction): Response<PostTransactionResponse>

    // API cho Fixed Income
    @POST("/api/fixed-income")
    suspend fun addFixedIncome(
        @Header("Authorization") token: String,
        @Body fixedIncome: PeriodicTransaction
    ): Response<ApiResponse>

    // API cho Fixed Expense
    @POST("/api/fixed-expense")
    suspend fun addFixedExpense(
        @Header("Authorization") token: String,
        @Body fixedExpense: PeriodicTransaction
    ): Response<ApiResponse>

}
