package com.example.jetpackcompose.app.features.apiService.TransactionAPI

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

class DeleteTransactionViewModel(private val context: Context) : ViewModel() {

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

    // Hàm DELETE để xóa giao dịch
    fun deleteTransaction(
        transactionId: Int,
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
                Log.d("DeleteTransactionViewModel", "Token: $token")
                Log.d("DeleteTransactionViewModel", "Transaction ID: $transactionId")

                // Gọi API DELETE để xóa giao dịch
                val response = api.deleteTransaction("Bearer $token", transactionId)
                Log.d("DeleteTransactionViewModel", "Response Code: ${response.code()}")

                if (response.isSuccessful) {
                    transactionStatus = "Giao dịch đã được xóa thành công"
                    onSuccess(transactionStatus)
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    transactionStatus = "Lỗi xóa giao dịch: $errorBodyString"
                    onError(transactionStatus)
                }
            } catch (e: Exception) {
                transactionStatus = "Lỗi: ${e.localizedMessage}"
                Log.e("DeleteTransactionViewModel", "Error deleting transaction: ${e.localizedMessage}", e)
                onError(transactionStatus)
            }
        }
    }
}
