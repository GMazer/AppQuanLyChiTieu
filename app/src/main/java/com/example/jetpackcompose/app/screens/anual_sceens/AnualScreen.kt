package com.example.jetpackcompose.app.screens.anual_sceens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.components.OtherTab
import com.example.jetpackcompose.ui.theme.colorPrimary

@Composable
fun AnualScreen(navController: NavHostController) {
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
                navController.popBackStack("mainscreen", inclusive = false) // Quay lại màn hình chính") // Quay lại màn hình trước
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
                    tint = colorPrimary // Màu biểu tượng
                )
            }
        }

        // Nội dung chính của màn hình
        Spacer(modifier = Modifier.height(16.dp)) // Khoảng cách dưới hàng nút
        Text(
            text = "Anual Screen Content", // Nội dung mẫu
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Preview
@Composable
fun PreviewAnualScreen() {
    AnualScreen(navController = NavHostController(context = TODO()))
}
