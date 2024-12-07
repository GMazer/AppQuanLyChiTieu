package com.example.jetpackcompose.app.screens.anual_sceens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI.FixedTransactionResponse
import com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI.GetFixedTransactionViewModel
import com.example.jetpackcompose.app.features.apiService.TransactionAPI.GetTransactionViewModel
import com.example.jetpackcompose.components.montserrat
import com.example.jetpackcompose.ui.theme.colorPrimary
import com.example.jetpackcompose.ui.theme.textColor

@Composable
fun AnualScreen(navController: NavHostController) {

    // Khởi tạo ViewModel
    val viewModel: GetFixedTransactionViewModel = GetFixedTransactionViewModel(LocalContext.current)

    // Lấy danh sách FixedTransaction từ ViewModel
    var fixedTransactions by remember { mutableStateOf<List<FixedTransactionResponse>>(emptyList()) }

    // Gọi hàm getFixedTransactions để lấy dữ liệu
    LaunchedEffect(Unit) {
        viewModel.getFixedTransactions(
            onSuccess = { transactions ->
                fixedTransactions = transactions
            },
            onError = { errorMessage ->
                // Xử lý lỗi (nếu có)
                Log.e("AnualScreen", errorMessage)
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Khoảng cách từ viền
    ) {
        // Hàng chứa nút trở về và nút chuyển hướng
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), // Khoảng cách dọc
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Căn 2 nút ra 2 bên
        ) {
            // Nút trở về (mũi tên trái)
            IconButton(onClick = {
                navController.popBackStack("mainscreen", inclusive = false)  // Quay lại màn hình trước
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack, // Mũi tên trái
                    contentDescription = "Back",
                    tint = colorPrimary // Màu mũi tên
                )
            }

            // Nút chuyển hướng (dấu +)
            IconButton(onClick = {
                navController.navigate("inputfixedtab") // Điều hướng tới màn hình InputFixedTab
            }) {
                Icon(
                    imageVector = Icons.Default.Add, // Biểu tượng dấu +
                    contentDescription = "Add",
                    tint = colorPrimary
                )
            }
        }

        // Nội dung chính của màn hình
        Spacer(modifier = Modifier.height(16.dp))

        // Hiển thị danh sách FixedTransaction
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(fixedTransactions) { transaction ->
                Log.d("Hypear PRIME", "Transaction: $transaction")
                FixedTransactionRow(transaction = transaction)
            }
        }
    }
}



@Composable
fun FixedTransactionRow(transaction: FixedTransactionResponse) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                // Bạn có thể thêm hành động tại đây khi click vào một hàng
                // VD: Điều hướng đến màn hình chi tiết của giao dịch
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Cột bên trái: Note và Category Name
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = transaction.title ?: "No Title",
                fontWeight = FontWeight.Bold,
                fontFamily = montserrat,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = transaction.categoryName,
                fontSize = 12.sp,
                fontFamily = montserrat,
            )
        }

        // Cột bên phải: Amount và Mũi tên
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hiển thị Amount
            Text(
                text = transaction.amount.toString(), // Hiển thị số tiền
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            // Mũi tên sang phải
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Navigate",
                tint = colorPrimary,
                modifier = Modifier.padding(start = 8.dp) // Khoảng cách giữa số tiền và mũi tên
            )
        }
    }
}



@Preview
@Composable
fun PreviewAnualScreen() {
    AnualScreen(navController = NavHostController(context = TODO()))
}
