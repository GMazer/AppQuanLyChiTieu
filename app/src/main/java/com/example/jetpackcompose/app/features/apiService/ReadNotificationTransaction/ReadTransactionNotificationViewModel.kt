package com.example.jetpackcompose.app.features.apiService.ReadNotificationTransaction

// TransactionNotificationViewModel.kt
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TransactionNotificationViewModel() : ViewModel() {
    private val _transactionList = MutableLiveData<List<TransactionReadNoti>>()
    val transactionList: LiveData<List<TransactionReadNoti>> get() = _transactionList

    // Cập nhật danh sách giao dịch
    fun updateTransactionList(transactionList: List<TransactionReadNoti>) {
        // Log dữ liệu để kiểm tra
        Log.d("TransactionNotificationViewModel", "Danh sách giao dịch đã được cập nhật trong ViewModel: $transactionList")
        _transactionList.value = transactionList
    }
}


