package com.example.jetpackcompose.app.features.apiService.ForgotPasswordAPI

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.app.network.ApiService
import com.example.jetpackcompose.app.network.BaseURL
import com.example.jetpackcompose.app.network.VerifyOtp
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VerifyOtpViewModel(private val context: Context) : ViewModel() {
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val api = Retrofit.Builder()
        .baseUrl(BaseURL.baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ApiService::class.java)

    var otpStatus: String = ""
        private set

    fun verifyOtp(
        data: VerifyOtp,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        // Confirm OTP
        viewModelScope.launch {
            try {
                val response = api.verifyOtp(data)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        otpStatus = "OTP verified successfully: ${responseBody.message}"
                        onSuccess(otpStatus)
                    } else {
                        otpStatus = "Empty response from server"
                        onError(otpStatus)
                    }
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    if (errorBodyString != null) {
                        try {
                            val errorResponse = gson.fromJson(errorBodyString, VerifyOtp::class.java)
                            val errorMessage = errorResponse.otp
                            otpStatus = "OTP verification failed: $errorMessage"
                            onError(otpStatus)
                        } catch (e: JsonSyntaxException) {
                            otpStatus = "Error parsing error response: ${e.localizedMessage}"
                            onError(otpStatus)
                        } catch (e: JsonParseException) {
                            otpStatus = "Error parsing error response: ${e.localizedMessage}"
                            onError(otpStatus)
                        }
                    } else {
                        otpStatus = "Error parsing error response: ${response.message()}"
                        onError(otpStatus)
                    }
                }
            } catch (e: Exception) {
                otpStatus = "Error: ${e.localizedMessage}"
                onError(otpStatus)
            }
        }
    }
}