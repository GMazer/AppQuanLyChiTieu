package com.example.jetpackcompose.app.features.apiService

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.app.network.ApiService
import com.example.jetpackcompose.app.network.BaseURL
import com.example.jetpackcompose.app.network.RegistrationData
import com.example.jetpackcompose.app.network.RegistrationResponse
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUpViewModel : ViewModel() {
    private val gson = GsonBuilder()
        .setLenient()  // Cho phép đọc dữ liệu JSON không hoàn chỉnh
        .create()

    private val api = Retrofit.Builder()
        .baseUrl(BaseURL.baseURl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    var registrationStatus: String = ""
        private set

    fun registerUser(
        data: RegistrationData,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = api.register(data)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        registrationStatus = "Registration successful: ${responseBody.message}"
                        onSuccess(registrationStatus)
                    } else {
                        registrationStatus = "Empty response from server"
                        onError(registrationStatus)
                    }
                } else {
                    // Nếu server trả về lỗi, xử lý errorBody
                    val errorBodyString = response.errorBody()?.string() // Lấy chuỗi JSON từ errorBody
                    if (errorBodyString != null) {
                        try {
                            // Parse errorBody thành RegistrationResponse để lấy message lỗi
                            val errorResponse = gson.fromJson(errorBodyString, RegistrationResponse::class.java)
                            val errorMessage = errorResponse.message
                            registrationStatus = "Registration failed: $errorMessage"
                            onError(registrationStatus)
                        } catch (e: JsonSyntaxException) {
                            registrationStatus = "Error parsing error response: ${e.localizedMessage}"
                            onError(registrationStatus)
                        } catch (e: JsonParseException) {
                            registrationStatus = "Error parsing error response: ${e.localizedMessage}"
                            onError(registrationStatus)
                        }
                    } else {
                        registrationStatus = "Registration failed: Unknown error"
                        onError(registrationStatus)
                    }
                }
            } catch (e: Exception) {
                registrationStatus = "Error: ${e.localizedMessage}"
                Log.e("SignUpViewModel", "Error: ${e.localizedMessage}", e)
                onError(registrationStatus)
            }
        }
    }
}
