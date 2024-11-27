package com.example.jetpackcompose.app.features.apiService.TransactionAPI

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.app.network.ApiService
import com.example.jetpackcompose.app.network.BaseURL
import com.example.jetpackcompose.app.screens.DailyTransaction
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GetTransactionViewModel(private val context: Context) : ViewModel() {

    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val api = Retrofit.Builder()
        .baseUrl(BaseURL.baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ApiService::class.java)

    var transactionList: List<DailyTransaction> = emptyList()
        private set

    var transactionStatus: String = ""
        private set

    // Lấy token từ SharedPreferences
    private fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    // Hàm lấy danh sách giao dịch
    fun getTransactions(
        month: Int,
        year: Int,
        onSuccess: (List<DailyTransaction>) -> Unit,
        onError: (String) -> Unit
    ) {
        val token = getToken()

        if (token.isNullOrEmpty()) {
            transactionStatus = "Error: Token not found. Please log in again."
            onError(transactionStatus)
            return
        }

        viewModelScope.launch {
            try {
                Log.d("TransactionViewModel", "Token: $token")

                val response = api.getTransactions("Bearer $token", month, year)
                Log.d("TransactionViewModel", "Response Code: ${response.code()}")
                Log.d("TransactionViewModel", "Response Error Body: ${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    val transactionsResponse = response.body()
                    if (transactionsResponse != null) {
                        // Lấy dailyTransactions từ response và chuyển đổi thành danh sách
                        val dailyTransactionList = transactionsResponse.dailyTransactions.map { (date, dailyTransaction) ->
                            DailyTransaction(
                                date = date,
                                amountIncome = dailyTransaction.totalDailyIncome,
                                amountExpense = dailyTransaction.totalDailyExpense
                            )
                        }

                        transactionList = dailyTransactionList
                        transactionStatus = "Transactions fetched successfully"
                        onSuccess(transactionList)
                    } else {
                        transactionStatus = "Error: Empty response from server"
                        onError(transactionStatus)
                    }
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    transactionStatus = "Error fetching transactions: $errorBodyString"
                    onError(transactionStatus)
                }
            } catch (e: Exception) {
                transactionStatus = "Error: ${e.localizedMessage}"
                Log.e("TransactionViewModel", "Error fetching transactions: ${e.localizedMessage}", e)
                onError(transactionStatus)
            }
        }
    }
}
