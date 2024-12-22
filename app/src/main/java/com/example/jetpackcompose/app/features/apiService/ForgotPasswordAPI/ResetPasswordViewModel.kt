package com.example.jetpackcompose.app.features.apiService.ForgotPasswordAPI

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.app.network.ApiService
import com.example.jetpackcompose.app.network.BaseURL
import com.example.jetpackcompose.app.network.ResetPassword
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResetPasswordViewModel(private val context: Context) : ViewModel() {
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val api = Retrofit.Builder()
        .baseUrl(BaseURL.baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ApiService::class.java)

    var resetPasswordStatus: String = ""
        private set

    fun resetPassword(
        data: ResetPassword,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        // Reset password
        viewModelScope.launch {
            try {
                val response = api.resetPassword(data)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        resetPasswordStatus = "Password reset successfully: ${responseBody.message}"
                        onSuccess(resetPasswordStatus)
                    } else {
                        resetPasswordStatus = "Empty response from server"
                        onError(resetPasswordStatus)
                    }
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    if (errorBodyString != null) {
                        try {
                            val errorResponse = gson.fromJson(errorBodyString, ResetPassword::class.java)
                            val errorMessage = errorResponse.newPassword
                            resetPasswordStatus = "Password reset failed: $errorMessage"
                            onError(resetPasswordStatus)
                        } catch (e: JsonSyntaxException) {
                            resetPasswordStatus = "Error parsing error response: ${e.localizedMessage}"
                            onError(resetPasswordStatus)
                        } catch (e: JsonParseException) {
                            resetPasswordStatus = "Error parsing error response: ${e.localizedMessage}"
                            onError(resetPasswordStatus)
                        }
                    } else {
                        resetPasswordStatus = "Empty error response from server"
                        onError(resetPasswordStatus)
                    }
                }
            } catch (e: Exception) {
                resetPasswordStatus = "Error: ${e.localizedMessage}"
                onError(resetPasswordStatus)
            }
        }
    }

}