package com.example.jetpackcompose.app.features.ReadNotificationTransaction

// TransactionNotificationViewModel.kt
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.content.Context

data class TransactionReadNoti(
    val type: String,
    val amount: Long,
    val date: String
)

class TransactionNotificationViewModel(private val context: Context) : ViewModel() {
    private val transactionStorage = TransactionStorage(context)

    private val _transactionList = MutableLiveData<List<TransactionReadNoti>>()
    val transactionList: LiveData<List<TransactionReadNoti>> get() = _transactionList

    init {
        // Tải danh sách giao dịch từ Internal Storage khi khởi tạo ViewModel
        _transactionList.value = transactionStorage.loadTransactions()
    }

    // Cập nhật danh sách giao dịch và lưu vào Internal Storage
    fun updateTransactionList(transactionList: List<TransactionReadNoti>) {
        _transactionList.value = transactionList
        transactionStorage.saveTransactions(transactionList)
        Log.d("TransactionNotificationViewModel", "Danh sách giao dịch đã được cập nhật và lưu: $transactionList")
    }
}



