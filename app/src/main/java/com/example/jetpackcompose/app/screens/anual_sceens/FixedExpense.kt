package com.example.jetpackcompose.app.screens.anual_sceens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose.R
import com.example.jetpackcompose.components.DatePickerRow
import com.example.jetpackcompose.components.DropdownRow
import com.example.jetpackcompose.components.RowTextField
import java.time.LocalDate

@SuppressLint("NewApi")
@Composable
fun FixedExpense() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth() // Chiều rộng tối đa (có thể tùy chỉnh)
                .padding(16.dp) // Khoảng cách xung quanh Box
                .background(
                    color = Color.White, // Màu nền trắng
                    shape = RoundedCornerShape(8.dp) // Bo góc 8.dp
                )
        ) {
            Column() {
                RowTextField(
                    label = "Tiêu đề",
                    textState = TextFieldValue(""),
                    onValueChange = {}
                )
                Divider(
                    color = Color(0xFFd4d4d4), // Màu của đường chia tách
                    thickness = 0.5.dp // Độ dày của đường chia tách
                )
                RowTextField(
                    label = "Số tiền",
                    textState = TextFieldValue(""),
                    onValueChange = {}
                )
                Divider(
                    color = Color(0xFFd4d4d4), // Màu của đường chia tách
                    thickness = 0.5.dp // Độ dày của đường chia tách
                )
                DropdownRow(
                    label = "Danh mục",
                    options = listOf(
                        Pair(R.drawable.essentials, "Thiết yếu"),
                        Pair(R.drawable.entertainment, "Giải trí"),
                        Pair(R.drawable.invest, "Đầu tư"),
                        Pair(R.drawable.hedgefund, "Dự phòng"),
                    )
                ) { selectedCategory ->
                    // Xử lý khi danh mục được chọn thay đổi
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth() // Chiều rộng tối đa (có thể tùy chỉnh)
                .padding(16.dp) // Khoảng cách xung quanh Box
                .background(
                    color = Color.White, // Màu nền trắng
                    shape = RoundedCornerShape(8.dp) // Bo góc 8.dp
                )
        ) {
            Column() {
                DropdownRow(
                    label = "Lặp lại",
                    options = listOf(
                        Pair(null, "Không lặp lại"),
                        Pair(null, "Hàng ngày"),
                        Pair(null, "Hàng tuần"),
                        Pair(null, "Hàng tháng"),
                    )
                ) { selectedCategory ->
                    // Xử lý khi danh mục được chọn thay đổi
                }
                Divider(
                    color = Color(0xFFd4d4d4), // Màu của đường chia tách
                    thickness = 0.5.dp // Độ dày của đường chia tách
                )
                DatePickerRow(
                    label = "Bắt đầu",
                    initialDate = LocalDate.now()
                ) { date ->
                    // Cập nhật ngày được chọn

                }
                Divider(
                    color = Color(0xFFd4d4d4), // Màu của đường chia tách
                    thickness = 0.5.dp // Độ dày của đường chia tách
                )
                DropdownRow(
                    label = "Kết thúc",
                    options = listOf(
                        Pair(R.drawable.essentials, "Không"),
                        Pair(R.drawable.entertainment, "Ngày chỉ định"),
                        Pair(R.drawable.invest, "Đầu tư"),
                        Pair(R.drawable.hedgefund, "Dự phòng"),
                    )
                ) { selectedCategory ->
                    // Xử lý khi danh mục được chọn thay đổi
                }

            }
        }
    }
}