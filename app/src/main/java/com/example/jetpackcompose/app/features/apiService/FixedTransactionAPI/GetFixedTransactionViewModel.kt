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

class GetFixedTransactionViewModel(private val context: Context) : ViewModel() {

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

    // Hàm get fixed expense
    fun getFixedTransactions(
        onSuccess: (List<FixedTransactionResponse>) -> Unit,
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
                Log.d("GetFixedTransactionViewModel", "Token: $token")

                // Gửi yêu cầu API để lấy FixedExpense
                val response = api.getFixedTransactions("Bearer $token")
                Log.d("GetFixedTransactionViewModel", "Response Code: ${response.code()}")

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        transactionStatus = "Fixed Expenses fetched successfully"
                        onSuccess(responseBody.fixedTransactionResponseList)
                    } else {
                        transactionStatus = "Error: Empty response from server"
                        onError(transactionStatus)
                    }
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    transactionStatus = "Error fetching Fixed Expenses: $errorBodyString"
                    onError(transactionStatus)
                }
            } catch (e: Exception) {
                transactionStatus = "Error: ${e.localizedMessage}"
                Log.e("GetFixedTransactionViewModel", "Error fetching Fixed Expenses: ${e.localizedMessage}", e)
                onError(transactionStatus)
            }
        }
    }
}
