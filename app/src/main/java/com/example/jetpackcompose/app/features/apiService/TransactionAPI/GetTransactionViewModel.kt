package com.example.jetpackcompose.app.features.apiService.TransactionAPI

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.app.network.ApiService
import com.example.jetpackcompose.app.network.BaseURL
import com.example.jetpackcompose.app.network.TransactionResponse
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

    var dateTransactionList: Map<String, List<TransactionResponse.TransactionDetail>> = emptyMap()

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
        onSuccess1: (List<DailyTransaction>) -> Unit,
        onSuccess2: (Map<String, List<TransactionResponse.TransactionDetail>>) -> Unit,
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

                        // Chuyển danh sách transactions thành Map nhóm theo ngày
                        val dateTransactionDetailList: Map<String, List<TransactionResponse.TransactionDetail>> = transactionsResponse.transactions
                            .groupBy { transaction -> transaction.transactionDate.joinToString("-") }
                            .mapValues { (_, transactions) ->
                                transactions.map {
                                    TransactionResponse.TransactionDetail(
                                        categoryName = it.categoryName,
                                        amount = it.amount,
                                        transactionDate = it.transactionDate,
                                        note = it.note,
                                        type = it.type,
                                        transactionId = it.transactionId
                                    )
                                }
                            }

                        // Log to check the map content
                        Log.d("TransactionViewModel", "TransactionDetails grouped by date: $dateTransactionDetailList")

                        // Set the transactionList and status
                        transactionList = dailyTransactionList
                        dateTransactionList = dateTransactionDetailList
                        transactionStatus = "Transactions fetched successfully"
                        onSuccess1(transactionList)
                        onSuccess2(dateTransactionList)
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


