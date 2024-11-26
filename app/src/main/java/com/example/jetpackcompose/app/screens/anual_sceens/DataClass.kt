package com.example.jetpackcompose.app.screens.anual_sceens

import android.util.Log
import com.example.jetpackcompose.app.features.inputFeatures.TransactionType
import com.google.gson.Gson
import kotlinx.serialization.Serializable
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

@Serializable
data class PeriodicTransaction(
    val title: String,
    val startDate: String,
    val endDate: String,
    val note: String,
    val amount: Long,
    val category: String,
    val type: TransactionType // Thêm thuộc tính type
)

fun convertToJson(transaction: PeriodicTransaction): String {
    val gson = Gson()
    return gson.toJson(transaction)
}

fun sendToApi(json: String) {
    val client = OkHttpClient()
    val requestBody = RequestBody.create(
        MediaType.parse("application/json; charset=utf-8"), json
    )
    val request = Request.Builder()
        .url("api o day")
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
            Log.e("API", "Gửi dữ liệu thất bại: ${e.message}")
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
           if (response.isSuccessful) {
               Log.i("API", "Gửi dữ liệu thành công: ${response.body()?.string()}")
           } else {
               Log.e("API", "Gửi dữ liệu thất bại: ${response.message()}")
           }
        }
    })
}

