package com.example.jetpackcompose.app.screens.anual_sceens.ViewModel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.app.network.ApiService
import com.example.jetpackcompose.app.network.BaseURL
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FixedExpenseViewModel(private val context: Context) : ViewModel() {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val gson = GsonBuilder().setLenient().create()
    private val api = Retrofit.Builder()
        .baseUrl(BaseURL.baseUrl)  // Base URL của API của bạn
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ApiService::class.java)

    var fixedExpenseStatus: String = ""
        private set

    private fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    fun addFixedExpense(fixedExpense: PeriodicTransaction, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val token = getToken()

        if (token.isNullOrEmpty()) {
            fixedExpenseStatus = "Error: Token not found. Please log in again."
            onError(fixedExpenseStatus)
            return
        }

        viewModelScope.launch {
            try {
                val response = api.addFixedExpense("Bearer $token", fixedExpense)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        fixedExpenseStatus = "Fixed Expense added successfully: ${responseBody.message}"
                        onSuccess(fixedExpenseStatus)
                    } else {
                        fixedExpenseStatus = "Error: Empty response from server"
                        onError(fixedExpenseStatus)
                    }
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    fixedExpenseStatus = "Error adding Fixed Expense: $errorBodyString"
                    onError(fixedExpenseStatus)
                }
            } catch (e: Exception) {
                fixedExpenseStatus = "Error: ${e.localizedMessage}"
                Log.e("FixedExpenseViewModel", "Error: ${e.localizedMessage}", e)
                onError(fixedExpenseStatus)
            }
        }
    }
}

class FixedExpenseViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FixedExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FixedExpenseViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
