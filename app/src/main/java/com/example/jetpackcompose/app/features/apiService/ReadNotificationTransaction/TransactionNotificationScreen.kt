package com.example.jetpackcompose.app.features.apiService.ReadNotificationTransaction

// TransactionNotificationScreen.kt
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.screens.anual_sceens.FixedTransactionRow
import com.example.jetpackcompose.components.montserrat
import com.example.jetpackcompose.ui.theme.colorPrimary
import com.example.jetpackcompose.ui.theme.textColor

@Composable
fun TransactionNotificationScreen(navController: NavController) {
    // Nhận ViewModel và quan sát LiveData
    Column(
        modifier = Modifier
            .background(Color(0xfff5f5f5))
            .fillMaxSize()
    ) {
        // Hàng chứa nút trở về và nút chuyển hướng
        Row(
            modifier = Modifier
                .height(50.dp)
                .background(Color(0xfff1f1f1))
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Nút quay lại
            Box(modifier = Modifier.weight(1.5f)) {
                IconButton(onClick = {
                    navController.popBackStack("other", inclusive = false)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_arrow_back_ios_24),
                        contentDescription = "Back",
                        tint = colorPrimary,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }

            // Chữ "Thu chi cố định" luôn nằm giữa và ẩn nếu diện tích không đủ
            Box(modifier = Modifier.weight(2f)) {
                androidx.compose.material.Text(
                    "Giao dịch từ thông báo",
                    fontSize = 14.sp,
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier
                        .requiredWidthIn(min = 0.dp) // Ẩn chữ khi không đủ diện tích
                        .fillMaxWidth(), // Đảm bảo chiếm toàn bộ không gian của Row
                    textAlign = TextAlign.Center
                )
            }

            // Chữ "Chỉnh sửa" hoặc "Hoàn thành"
            Box(modifier = Modifier.weight(1f)) {

            }

            // Nút thêm mới
            Box(modifier = Modifier.weight(0.5f)) {

            }

            Divider(color = Color.LightGray, thickness = 1.dp)

        }
    }
}


