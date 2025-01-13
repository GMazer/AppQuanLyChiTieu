package com.example.jetpackcompose.app.network

import com.example.jetpackcompose.app.screens.LimitTransaction
import com.example.jetpackcompose.app.screens.RemainLimit
import com.example.jetpackcompose.app.screens.Transaction
import com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI.FixedTransaction
import com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI.FixedTransactionUpdate
import com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI.GetFixedTransactionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

object BaseURL {
    val baseUrl = "https://b878-118-71-204-229.ngrok-free.app"
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
    val status: String,
    val message: String
)

data class  SendOtp(
    val email: String
)

data class VerifyOtp(
    val email: String,
    val otp: String
)

data class ResetPassword(
    val email: String,
    val newPassword: String,
    val confirmPassword: String
)


data class FindTransactionResponse(
    val categoryName: String,
    val amount: Long,
    val transactionDate: List<Int>,
    val note: String,
    val type: String,
    val transaction_id: Long
)

data class PostTransactionResponse(
    val categoryName: String,
    val amount: Double,
    val transactionDate: List<Int>,
    val note: String,
    val type: String,
    val transaction_id: Long
)

data class ApiResponse(
    val status: String,
    val message: String
)

data class ReportExpenseResponse(
    val totalIncome: Long,
    val totalExpense: Long,
    val netAmount: Long,
    val categoryExpenseReports: List<CategoryExpenseReport>
){
    data class CategoryExpenseReport(
        val categoryId: Int,
        val categoryName: String,
        val spentAmount: Long,
        val percentSpent: Double,
        val percentLimit: Double
    )
}

data class ReportIncomeResponse(
    val totalIncome: Long,
    val totalExpense: Long,
    val netAmount: Long,
    val categoryIncomeReports: List<CategoryIncomeReport>
){
    data class CategoryIncomeReport(
        val categoryId: Int,
        val categoryName: String,
        val categoryIncome: Long,
        val percentIncome: Double
    )
}

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
    suspend fun login(@Body LoginData: LoginData): Response<LoginResponse>

    @POST("/api/users/verify-otp")
    suspend fun verifyOtp(@Body verifyOtp: VerifyOtp): Response<ApiResponse>

    @POST("/api/users/send-otp")
    suspend fun sendOtp(@Body sendOtp: SendOtp): Response<ApiResponse>

    @POST("/api/users/reset-password")
    suspend fun resetPassword(@Body resetPassword: ResetPassword): Response<ApiResponse>


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

    @GET("/api/fixed-transactions")
    suspend fun getFixedTransactions(
        @Header("Authorization") token: String
    ): Response<GetFixedTransactionResponse>

    @PUT("/api/fixed-transactions/{fixedTransactionId}")
    suspend fun putFixedTransaction(
        @Header("Authorization") token: String,
        @Path("fixedTransactionId") fixedTransactionId: Int,
        @Body fixedTransaction: FixedTransactionUpdate
    ): Response<ApiResponse>

    @DELETE("/api/fixed-transactions/{fixedTransactionId}")
    suspend fun deleteFixedTransaction(
        @Header("Authorization") token: String,
        @Path("fixedTransactionId") fixedTransactionId: Int
    ): Response<GetFixedTransactionResponse>

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

    @GET("/api/category-limits/current")
    suspend fun getBudgetCategory(
        @Header("Authorization") token: String
    ): Response<List<RemainLimit.CategoryLimit>>

    @PUT("/api/transactions/{transactionId}")
    suspend fun putTransaction(
        @Header("Authorization") token: String,
        @Path("transactionId") transactionId: Int,  // Tham số này sẽ thay thế {transactionId} trong URL
        @Body transaction: Transaction
    ): Response<TransactionResponse>

    @DELETE("/api/transactions/{transactionId}")
    suspend fun deleteTransaction(
        @Header("Authorization") token: String,
        @Path("transactionId") transactionId: Int,  // Tham số này sẽ thay thế {transactionId} trong URL
    ): Response<TransactionResponse>

    @GET("/api/report/monthly_expense")
    suspend fun getReportExpense(
        @Header("Authorization") token: String,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Response<ReportExpenseResponse>

    @GET("/api/report/monthly_income")
    suspend fun getReportIncome(
        @Header("Authorization") token: String,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Response<ReportIncomeResponse>

    @GET("/api/transactions/search")
    suspend fun findTransactions(
        @Header("Authorization") token: String,
        @Query("note") note: String
    ): Response<List<FindTransactionResponse>>

}
