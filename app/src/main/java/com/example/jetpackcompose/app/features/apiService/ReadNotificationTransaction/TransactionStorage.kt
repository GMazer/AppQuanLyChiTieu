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

    // Xóa giao dịch theo chỉ số
    fun deleteTransactionByIndex(index: Int) {
        // Tải danh sách giao dịch hiện tại
        val transactions = loadTransactions().toMutableList()

        // Kiểm tra xem chỉ số có hợp lệ không
        if (index >= 0 && index < transactions.size) {
            // Xóa giao dịch tại chỉ số
            transactions.removeAt(index)

            // Lưu lại danh sách giao dịch đã được cập nhật
            saveTransactions(transactions)
        }
    }
}
