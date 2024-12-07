package com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.app.network.ApiService
import com.example.jetpackcompose.app.network.BaseURL
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostFixedTransactionViewModel(private val context: Context) : ViewModel() {

    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val api = Retrofit.Builder()
        .baseUrl(BaseURL.baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ApiService::class.java)

    var transactionStatus: String = ""
        private set

    // Lấy token từ SharedPreferences
    private fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    // Hàm post fixed expense
    fun addFixedTransaction (
        fixedExpense: FixedTransaction,
        onSuccess: (String) -> Unit,
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
                Log.d("FixedExpenseViewModel", "Token: $token")
                Log.d("FixedExpenseViewModel", "FixedExpense Data: $fixedExpense")

                // Gửi yêu cầu API để thêm FixedExpense
                val response = api.addFixedTransaction("Bearer $token", fixedExpense)
                Log.d("FixedExpenseViewModel", "Response Code: ${response.code()}")

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        transactionStatus = "Fixed Expense added successfully"
                        onSuccess(transactionStatus)
                    } else {
                        transactionStatus = "Error: Empty response from server"
                        onError(transactionStatus)
                    }
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    transactionStatus = "Error adding Fixed Expense: $errorBodyString"
                    onError(transactionStatus)
                }
            } catch (e: Exception) {
                transactionStatus = "Error: ${e.localizedMessage}"
                Log.e("FixedExpenseViewModel", "Error adding Fixed Expense: ${e.localizedMessage}", e)
                onError(transactionStatus)
            }
        }
    }
}




