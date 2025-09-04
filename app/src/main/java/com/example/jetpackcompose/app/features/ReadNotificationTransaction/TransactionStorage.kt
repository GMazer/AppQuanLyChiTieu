package com.example.jetpackcompose.app.features.ReadNotificationTransaction

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import androidx.compose.runtime.*


class TransactionStorage(private val context: Context?) {

    private val fileName = "transactions.json"
    private val gson = Gson()

    // Biến theo dõi trạng thái danh sách giao dịch
    private val _transactionsState = mutableStateOf<List<TransactionReadNoti>>(emptyList())
    val transactionsState: State<List<TransactionReadNoti>> get() = _transactionsState

    init {
        // Load danh sách giao dịch ban đầu
        _transactionsState.value = loadTransactions()
    }

    companion object {
        fun empty(): TransactionStorage {
            return TransactionStorage(null) // Không sử dụng context để lưu dữ liệu
        }
    }

    // Lưu danh sách giao dịch
    fun saveTransactions(transactionList: List<TransactionReadNoti>) {
        val jsonString = gson.toJson(transactionList)
        context?.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
            output?.write(jsonString.toByteArray())
        }
        _transactionsState.value = transactionList // Cập nhật trạng thái
    }

    // Đọc danh sách giao dịch
    fun loadTransactions(): List<TransactionReadNoti> {
        val file = File(context?.filesDir, fileName)
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

    fun isEmpty(): Boolean {
        return transactionsState.value.isEmpty()
    }
}
