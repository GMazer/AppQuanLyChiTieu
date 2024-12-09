package com.example.jetpackcompose.app.features.apiService.ReadNotificationTransaction

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class TransactionStorage(private val context: Context) {

    private val fileName = "transactions.json"
    private val gson = Gson()

    // Lưu danh sách giao dịch
    fun saveTransactions(transactionList: List<TransactionReadNoti>) {
        val jsonString = gson.toJson(transactionList)
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
            output.write(jsonString.toByteArray())
        }
    }

    // Đọc danh sách giao dịch
    fun loadTransactions(): List<TransactionReadNoti> {
        val file = File(context.filesDir, fileName)
        if (!file.exists()) return emptyList()

        val jsonString = file.readText()
        val type = object : TypeToken<List<TransactionReadNoti>>() {}.type
        return gson.fromJson(jsonString, type)
    }
}