package com.example.jetpackcompose.app.features.apiService.ForgotPasswordAPI

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.app.network.ApiService
import com.example.jetpackcompose.app.network.BaseURL
import com.example.jetpackcompose.app.network.SendOtp
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SendOtpViewModel(private val context: Context) : ViewModel() {
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

    fun sendOtp(
        data: SendOtp,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        // Send OTP to email
        viewModelScope.launch {
            try {
                val response = api.sendOtp(data)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        otpStatus = "OTP sent successfully: ${responseBody.message}"
                        onSuccess(otpStatus)
                    } else {
                        otpStatus = "Empty response from server"
                        onError(otpStatus)
                    }
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    if (errorBodyString != null) {
                        try {
                            val errorResponse = gson.fromJson(errorBodyString, SendOtp::class.java)
                            val errorMessage = errorResponse.email
                            otpStatus = "OTP failed: $errorMessage"
                            onError(otpStatus)
                        } catch (e: JsonSyntaxException) {
                            otpStatus = "Error parsing error response: ${e.localizedMessage}"
                            onError(otpStatus)
                        } catch (e: JsonParseException) {
                            otpStatus = "Error parsing error response: ${e.localizedMessage}"
                            onError(otpStatus)
                        }
                    }
                }
            } catch (e: Exception) {
                otpStatus = "Error sending OTP: ${e.localizedMessage}"
                Log.e("SendOtpViewModel", "Error sending OTP: ${e.localizedMessage}", e)
                onError(otpStatus)
            }
        }
    }
}