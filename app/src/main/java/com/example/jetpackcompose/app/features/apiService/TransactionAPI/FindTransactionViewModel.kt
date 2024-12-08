package com.example.jetpackcompose.app.features.apiService.TransactionAPI

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.app.network.ApiService
import com.example.jetpackcompose.app.network.BaseURL
import com.example.jetpackcompose.app.network.FindTransactionResponse
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FindTransactionViewModel(private val context: Context) : ViewModel() {

    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val api = Retrofit.Builder()
        .baseUrl(BaseURL.baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ApiService::class.java)

    var searchResults: List<FindTransactionResponse> = emptyList()
        private set

    var searchStatus: String = ""
        private set

    // Lấy token từ SharedPreferences
    private fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    // Hàm tìm kiếm giao dịch
    fun findTransactions(
        note: String,
        onSuccess: (List<FindTransactionResponse>) -> Unit,
        onError: (String) -> Unit
    ) {
        val token = getToken()

        if (token.isNullOrEmpty()) {
            searchStatus = "Error: Token not found. Please log in again."
            onError(searchStatus)
            return
        }

        viewModelScope.launch {
            try {
                Log.d("FindTransactionViewModel", "Token: $token")
                Log.d("FindTransactionViewModel", "Keyword: $note")

                val response = api.findTransactions("Bearer $token", note)
                Log.d("FindTransactionViewModel", "Response Code: ${response.code()}")
                Log.d("FindTransactionViewModel", "Response Error Body: ${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    val transactions = response.body()
                    if (transactions != null) {
                        searchResults = transactions
                        searchStatus = "Search completed successfully"
                        onSuccess(searchResults)
                    } else {
                        searchStatus = "No transactions found for the keyword: $note"
                        onError(searchStatus)
                    }
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    searchStatus = "Error fetching transactions: $errorBodyString"
                    onError(searchStatus)
                }
            } catch (e: Exception) {
                searchStatus = "Error: ${e.localizedMessage}"
                Log.e("FindTransactionViewModel", "Error searching transactions: ${e.localizedMessage}", e)
                onError(searchStatus)
            }
        }
    }
}
