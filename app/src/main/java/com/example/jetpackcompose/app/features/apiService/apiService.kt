package com.example.jetpackcompose.app.network

import com.example.jetpackcompose.app.features.inputFeatures.LimitTransaction
import com.example.jetpackcompose.app.features.inputFeatures.RemainLimit
import com.example.jetpackcompose.app.features.inputFeatures.Transaction
import com.example.jetpackcompose.app.screens.anual_sceens.ViewModel.FixedTransaction
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

object BaseURL {
    val baseUrl = "https://11e4-1-54-8-129.ngrok-free.app"
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
        val transactionId: Int
    )
}

interface ApiService {

    // API cho đăng ký
    @POST("/api/users/register")
    suspend fun register(@Body registrationData: RegistrationData): Response<RegistrationResponse>

    /// API cho đăng nhập
    @POST("/api/users/login")
    suspend fun login(@Body LoginData: LoginData): Response<LoginResponse>


    // API cho giao dịch
    @GET("api/finance")
    suspend fun getTransactions(
        @Header("Authorization") token: String,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Response<TransactionResponse>

    //API cho nhập transaction
    @POST("api/transactions")
    suspend fun postTransaction(
        @Header("Authorization") token: String,
        @Body transaction: Transaction
    ): Response<PostTransactionResponse>


    // API cho Fixed
    @POST("/api/fixed-transactions")
    suspend fun addFixedTransaction(
        @Header("Authorization") token: String,
        @Body fixedTransaction: FixedTransaction // Dùng FixedTransaction mà không phân biệt loại giao dịch
    ): Response<ApiResponse>

    //API cho PutLimit
    @PUT("/api/category-limits/save")
    suspend fun addLimitTransaction(
        @Header("Authorization") token: String,
        @Body limitTransaction: List<LimitTransaction.CategoryLimit>
    ): Response<ApiResponse>

    @GET("/api/category-limits/remaining")
    suspend fun getLimitTransaction(
        @Header("Authorization") token: String
    ): Response<List<RemainLimit.CategoryLimit>>
}
