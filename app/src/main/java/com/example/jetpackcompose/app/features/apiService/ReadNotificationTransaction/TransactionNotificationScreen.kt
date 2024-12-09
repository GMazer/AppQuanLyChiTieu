package com.example.jetpackcompose.app.features.apiService.ReadNotificationTransaction

// TransactionNotificationScreen.kt
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun TransactionNotificationScreen(navController: NavController) {
    // Nhận ViewModel và quan sát LiveData
    val viewModel: TransactionNotificationViewModel = TransactionNotificationViewModel()
    val transactionList by viewModel.transactionList.observeAsState(emptyList())
    Log.d("Composable", "Transaction List: $transactionList")

    // Kiểm tra xem danh sách giao dịch không rỗng
    if (transactionList.isNotEmpty()) {
        LazyColumn {
            items(transactionList) { transaction ->
                Text(
                    text = "Type: ${transaction.type}, Amount: ${transaction.amount}, Date: ${transaction.date}"
                )
            }
        }
    } else {
        Text(text = "Không có giao dịch nào.")
    }
}


