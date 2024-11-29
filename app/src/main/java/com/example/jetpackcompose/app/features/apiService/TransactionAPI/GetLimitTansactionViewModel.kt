package com.example.jetpackcompose.app.features.apiService.TransactionAPI

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.app.features.inputFeatures.LimitTransaction
import com.example.jetpackcompose.app.features.inputFeatures.RemainLimit
import com.example.jetpackcompose.app.network.ApiService
import com.example.jetpackcompose.app.network.BaseURL
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetLimitTransactionViewModel(private val context: Context) : ViewModel() {

    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val api = Retrofit.Builder()
        .baseUrl(BaseURL.baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ApiService::class.java)

    var limitTransactionStatus: String = ""
        private set

    // Lấy token từ SharedPreferences
    private fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    // Hàm thêm giới hạn danh mục
    fun getLimitTransaction(
        onSuccess: (List<RemainLimit.CategoryLimit>) -> Unit,
        onError: (String) -> Unit
    ) {
        val token = getToken()

        if (token.isNullOrEmpty()) {
            limitTransactionStatus = "Error: Token not found. Please log in again."
            onError(limitTransactionStatus)
            return
        }

        viewModelScope.launch {
            try {
                Log.d("LimitTransactionViewModel", "Token: $token")

                val response = api.getLimitTransaction("Bearer $token")
                Log.d("LimitTransactionViewModel", "Response Code: ${response.code()}")

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        onSuccess(responseBody)
                    } else {
                        limitTransactionStatus = "Error: Empty response from server"
                        onError(limitTransactionStatus)
                    }
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    limitTransactionStatus = "Error adding limit transaction: $errorBodyString"
                    onError(limitTransactionStatus)
                }
            } catch (e: Exception) {
                limitTransactionStatus = "Error: ${e.localizedMessage}"
                Log.e("LimitTransactionViewModel", "Error adding limit transaction: ${e.localizedMessage}", e)
                onError(limitTransactionStatus)
            }
        }
    }
}
