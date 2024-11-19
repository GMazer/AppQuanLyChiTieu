package com.example.jetpackcompose.app.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class RegistrationData(
    val phone_number: String,
    val email: String,
    val password: String,
    val retype_password: String
)

data class LoginData(
    val phone_number: String,
    val password: String,
)

data class LoginResponse(
    val token: String  // Giả sử API trả về một trường 'token'
)

data class RegistrationResponse(
    val message: String  // Hoặc bất kỳ thông tin nào bạn muốn từ API sau khi đăng ký
)


interface ApiService {
    @POST("/api/users/register")
    suspend fun register(@Body registrationData: RegistrationData): Response<Any>

    @POST("/api/users/login")
    suspend fun login (@Body LoginData: LoginData): Response<Any>
}
