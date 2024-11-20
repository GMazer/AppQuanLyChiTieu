package com.example.jetpackcompose.app.features.apiService

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.app.network.ApiService
import com.example.jetpackcompose.app.network.LoginData
import com.example.jetpackcompose.app.network.LoginResponse
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignInViewModel(private val context: Context) : ViewModel() {

    private val gson = GsonBuilder()
        .setLenient() // Cho phép đọc dữ liệu JSON không chính xác hoàn toàn
        .create()

    private val api = Retrofit.Builder()
        .baseUrl("https://3263-1-54-7-77.ngrok-free.app") // Thay thế bằng base URL API của bạn
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ApiService::class.java)

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    var loginStatus: String = ""
        private set

    // Lưu token vào SharedPreferences
    private fun saveToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("auth_token", token)
        editor.apply()
    }

    // Lấy token từ SharedPreferences
    fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    // Xóa token khi người dùng đăng xuất
    fun clearToken() {
        val editor = sharedPreferences.edit()
        editor.remove("auth_token")
        editor.apply()
    }

    fun isTokenCleared(): Boolean {
        return getToken() == null
    }

    // Hàm đăng nhập người dùng
    fun signInUser(
        data: LoginData,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = api.login(data) // Gọi API đăng nhập
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        // Lấy token từ phản hồi
                        val status = responseBody.status
                        if (status == "success") {
                            val token = responseBody.message
                            if (token.isNotEmpty()) {
                                saveToken(token) // Lưu token vào SharedPreferences
                                loginStatus = "Login successful"
                                onSuccess(loginStatus)
                            } else {
                                loginStatus = "Login failed: Empty token"
                                onError(loginStatus)
                            }
                        } else {
                            loginStatus = "Login failed: ${responseBody.message}"
                            onError(loginStatus)
                        }
                    } else {
                        loginStatus = "Login failed: Empty response from server"
                        onError(loginStatus)
                    }
                } else {
                    // Xử lý errorBody
                    val errorBodyString = response.errorBody()?.string() // Lấy nội dung của errorBody
                    if (errorBodyString != null) {
                        // Parse JSON từ errorBody
                        val errorResponse = gson.fromJson(errorBodyString, LoginResponse::class.java)
                        val errorMessage = errorResponse.message
                        loginStatus = "Login failed: $errorMessage"
                        onError(loginStatus)
                    } else {
                        loginStatus = "Login failed: Unknown error"
                        onError(loginStatus)
                    }
                }
            } catch (e: JsonSyntaxException) {
                loginStatus = "JSON syntax error: ${e.localizedMessage}"
                Log.e("SignInViewModel", "JSON Syntax Error: ${e.localizedMessage}", e)
                onError(loginStatus)
            } catch (e: JsonParseException) {
                loginStatus = "Error parsing JSON response: ${e.localizedMessage}"
                Log.e("SignInViewModel", "JSON Parsing Error: ${e.localizedMessage}", e)
                onError(loginStatus)
            } catch (e: Exception) {
                loginStatus = "General error: ${e.localizedMessage}"
                Log.e("SignInViewModel", "General Error: ${e.localizedMessage}", e)
                onError(loginStatus)
            }
        }
    }
}
