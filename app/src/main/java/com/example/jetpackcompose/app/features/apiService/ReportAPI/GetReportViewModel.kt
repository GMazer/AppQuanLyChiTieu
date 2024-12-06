package com.example.jetpackcompose.app.features.apiService.ReportAPI

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.app.network.ApiService
import com.example.jetpackcompose.app.network.BaseURL
import com.example.jetpackcompose.app.network.ReportResponse
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetReportViewModel(private val context: Context) : ViewModel() {

    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val api = Retrofit.Builder()
        .baseUrl(BaseURL.baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ApiService::class.java)

    var reportData: ReportResponse? = null
        private set

    var reportStatus: String = ""
        private set

    // Lấy token từ SharedPreferences
    private fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    // Hàm lấy báo cáo
    fun getReport(
        month: Int,
        year: Int,
        onSuccess: (ReportResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val token = getToken()

        if (token.isNullOrEmpty()) {
            reportStatus = "Error: Token not found. Please log in again."
            onError(reportStatus)
            return
        }

        viewModelScope.launch {
            try {
                Log.d("GetReportViewModel", "Token: $token")

                val response = api.getReport("Bearer $token", month, year)
                Log.d("GetReportViewModel", "Response Code: ${response.code()}")
                Log.d("GetReportViewModel", "Response Error Body: ${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    val reportResponse = response.body()
                    if (reportResponse != null) {
                        // Gán dữ liệu và gọi callback thành công
                        reportData = reportResponse
                        reportStatus = "Report fetched successfully"
                        onSuccess(reportResponse)
                    } else {
                        reportStatus = "Error: Empty report response from server"
                        onError(reportStatus)
                    }
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    reportStatus = "Error fetching report: $errorBodyString"
                    onError(reportStatus)
                }
            } catch (e: Exception) {
                reportStatus = "Error: ${e.localizedMessage}"
                Log.e("GetReportViewModel", "Error fetching report: ${e.localizedMessage}", e)
                onError(reportStatus)
            }
        }
    }
}
