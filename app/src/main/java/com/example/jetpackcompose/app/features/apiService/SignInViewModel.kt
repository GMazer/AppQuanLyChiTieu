package com.example.jetpackcompose.app.features.apiService

import android.util.Log
import android.content.Context
import android.content.SharedPreferences
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
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST



class SignInViewModel(private val context: Context) : ViewModel() {

    val gson = GsonBuilder()
        .setLenient() // Cho phép đọc dữ liệu JSON không hoàn chỉnh
        .create()

    private val api = Retrofit.Builder()
        .baseUrl("https://039c-1-54-7-77.ngrok-free.app")
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

    // Hàm đăng nhập người dùng
    fun signInUser(data: LoginData, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = api.login(data) // Gọi API đăng nhập
                if (response.isSuccessful) {
                    Log.i("Response: ", response.body().toString())

                    // Kiểm tra nếu phản hồi trả về một chuỗi token đơn giản
                    val responseBody = response.body()
                    if (responseBody is String) {
                        // Nếu responseBody là một chuỗi (token trực tiếp)
                        val token = responseBody
                        saveToken(token) // Lưu token vào SharedPreferences
                        loginStatus = "Login successful"
                        onSuccess(loginStatus)
                    } else {
                        // Nếu phản hồi trả về là đối tượng JSON như mong đợi
                        val loginResponse = responseBody as? LoginResponse
                        val token = loginResponse?.token
                        if (token != null) {
                            saveToken(token) // Lưu token vào SharedPreferences
                            loginStatus = "Login successful"
                            onSuccess(loginStatus)
                        } else {
                            loginStatus = "Token missing"
                            onError(loginStatus)
                        }
                    }
                } else {
                    loginStatus = "Login failed: ${response.message()}"
                    onError(loginStatus)
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
