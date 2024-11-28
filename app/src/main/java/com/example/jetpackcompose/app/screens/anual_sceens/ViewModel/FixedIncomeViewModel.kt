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

class FixedIncomeViewModel(private val context: Context) : ViewModel() {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private val gson = GsonBuilder().setLenient().create()

    private val api = Retrofit.Builder()
        .baseUrl(BaseURL.baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ApiService::class.java)

    var fixedIncomeStatus: String = ""
        private set

    private fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    fun addFixedIncome(
        fixedIncome: FixedTransaction,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val token = getToken()

        if (token.isNullOrEmpty()) {
            fixedIncomeStatus = "Error: Token not found. Please log in again."
            onError(fixedIncomeStatus)
            return
        }

        viewModelScope.launch {
            try {
                val response = api.addFixedTransaction("Bearer $token", fixedIncome)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        fixedIncomeStatus = "Fixed Income added successfully: ${responseBody.message}"
                        onSuccess(fixedIncomeStatus)
                    } else {
                        fixedIncomeStatus = "Error: Empty response from server"
                        onError(fixedIncomeStatus)
                    }
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    fixedIncomeStatus = "Error adding Fixed Income: $errorBodyString"
                    onError(fixedIncomeStatus)
                }
            } catch (e: Exception) {
                fixedIncomeStatus = "Error: ${e.localizedMessage}"
                Log.e("FixedIncomeViewModel", "Error: ${e.localizedMessage}", e)
                onError(fixedIncomeStatus)
            }
        }
    }
}


