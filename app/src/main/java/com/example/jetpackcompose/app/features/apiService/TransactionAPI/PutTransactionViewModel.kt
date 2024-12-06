package com.example.jetpackcompose.app.features.apiService.TransactionAPI

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.app.features.inputFeatures.Transaction
import com.example.jetpackcompose.app.network.ApiService
import com.example.jetpackcompose.app.network.BaseURL
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PutTransactionViewModel(private val context: Context) : ViewModel() {

    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val gson = GsonBuilder().setLenient().create()
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

    // Hàm PUT để cập nhật giao dịch
    fun putTransaction(
        transactionId: Int,
        data: Transaction,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val token = getToken()

        if (token.isNullOrEmpty()) {
            transactionStatus = "Lỗi: Không tìm thấy token. Vui lòng đăng nhập lại."
            onError(transactionStatus)
            return
        }

        viewModelScope.launch {
            try {
                Log.d("PutTransactionViewModel", "Token: $token")
                Log.d("PutTransactionViewModel", "Transaction Data: $data")

                // Gọi API PUT để cập nhật giao dịch
                val response = api.putTransaction("Bearer $token", transactionId, data)
                Log.d("PutTransactionViewModel", "Response Code: ${response.code()}")

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        transactionStatus = "Giao dịch đã được cập nhật thành công"
                        onSuccess(transactionStatus)
                    } else {
                        transactionStatus = "Lỗi: Phản hồi từ server trống"
                        onError(transactionStatus)
                    }
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    transactionStatus = "Lỗi cập nhật giao dịch: $errorBodyString"
                    onError(transactionStatus)
                }
            } catch (e: Exception) {
                transactionStatus = "Lỗi: ${e.localizedMessage}"
                Log.e("PutTransactionViewModel", "Error updating transaction: ${e.localizedMessage}", e)
                onError(transactionStatus)
            }
        }
    }
}

