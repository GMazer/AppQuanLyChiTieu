package com.example.jetpackcompose.app.features.apiService

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.app.network.ApiService
import com.example.jetpackcompose.app.network.RegistrationData
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.jetpackcompose.app.network.RegistrationResponse
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException

class SignUpViewModel : ViewModel() {
    private val api = Retrofit.Builder()
        .baseUrl("https://039c-1-54-7-77.ngrok-free.app")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build()
        .create(ApiService::class.java)

    var registrationStatus: String = ""
        private set

    fun registerUser(data: RegistrationData, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = api.register(data)  // Gọi API để đăng ký
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    // Kiểm tra xem phản hồi có phải là chuỗi
                    if (responseBody is String) {
                        registrationStatus = "Registration successful: $responseBody"
                        onSuccess(registrationStatus)
                    } else if (responseBody is RegistrationResponse) {
                        registrationStatus = "Registration successful: ${responseBody.message}"
                        onSuccess(registrationStatus)
                    } else {
                        registrationStatus = "Unexpected response format"
                        onError(registrationStatus)
                    }
                } else {
                    registrationStatus = "Registration failed: ${response.message()}"
                    onError(registrationStatus)
                }
            } catch (e: JsonSyntaxException) {
                registrationStatus = "JSON syntax error: ${e.localizedMessage}"
                Log.e("SignUpViewModel", "JSON Syntax Error: ${e.localizedMessage}", e)
                onError(registrationStatus)
            } catch (e: JsonParseException) {
                registrationStatus = "Error parsing JSON response: ${e.localizedMessage}"
                Log.e("SignUpViewModel", "JSON Parsing Error: ${e.localizedMessage}", e)
                onError(registrationStatus)
            } catch (e: Exception) {
                registrationStatus = "General error: ${e.localizedMessage}"
                Log.e("SignUpViewModel", "General Error: ${e.localizedMessage}", e)
                onError(registrationStatus)
            }
        }
    }
}
