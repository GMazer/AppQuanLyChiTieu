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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.inputFeatures.TransactionType
import com.example.jetpackcompose.app.screens.anual_sceens.ViewModel.PeriodicTransaction
import com.example.jetpackcompose.components.DatePickerRow
import com.example.jetpackcompose.components.DropdownRow
import com.example.jetpackcompose.components.EndDateRow
import com.example.jetpackcompose.components.RowNumberField
import com.example.jetpackcompose.components.RowTextField
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@SuppressLint("NewApi")
@Composable
fun FixedIncome(onDataChanged: (PeriodicTransaction) -> Unit) {

    val vietnamLocale = Locale("vi", "VN") // Đặt Locale Việt Nam
    val currentDate = remember {
        SimpleDateFormat("dd/MM/yyyy", vietnamLocale).format(Date()) // Định dạng ngày hiện tại
    }

    var titleState by remember { mutableStateOf(TextFieldValue("")) }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(currentDate) }
    var selectedRepeat by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("Không") }
    var numberState by remember { mutableStateOf(TextFieldValue("")) }

    fun createTransaction(): PeriodicTransaction {
        return PeriodicTransaction(
            title = titleState.text,
            startDate = selectedDate,
            endDate = endDate,
            note = selectedRepeat,
            amount = numberState.text.toLongOrNull() ?: 0L,
            category = selectedCategory,
            type = TransactionType.INCOME
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
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
                    textState = titleState,
                    onValueChange = { newValue ->
                        titleState = newValue
                        onDataChanged(createTransaction())
                    }
                )
                Divider(
                    color = Color(0xFFd4d4d4), // Màu của đường chia tách
                    thickness = 0.5.dp // Độ dày của đường chia tách
                )
                RowNumberField(
                    textState = numberState,
                    onValueChange = { newValue ->
                        numberState = newValue // Cập nhật giá trị khi người dùng nhập
                        onDataChanged(createTransaction())
                    }
                )
                Divider(
                    color = Color(0xFFd4d4d4), // Màu của đường chia tách
                    thickness = 0.5.dp // Độ dày của đường chia tách
                )
                DropdownRow(
                    label = "Danh mục",
                    options = listOf(
                        Pair(R.drawable.essentials, "Lương"),
                        Pair(R.drawable.entertainment, "Phụ cấp"),
                        Pair(R.drawable.invest, "Đầu tư"),
                        Pair(R.drawable.hedgefund, "Thu nhập phụ"),
                    )
                ) { category ->
                    // Xử lý khi danh mục được chọn thay đổi
                    selectedCategory = category
                    onDataChanged(createTransaction())
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
                ) { repeat ->
                    // Xử lý khi danh mục được chọn thay đổi
                    selectedRepeat = repeat
                    onDataChanged(createTransaction())
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
                    selectedDate = date.toString()
                    onDataChanged(createTransaction())
                }
                Divider(
                    color = Color(0xFFd4d4d4), // Màu của đường chia tách
                    thickness = 0.5.dp // Độ dày của đường chia tách
                )
                EndDateRow (
                    label = "Kết thúc"
                ) { date ->
                    // Xử lý khi danh mục được chọn thay đổi
                    endDate = date
                    onDataChanged(createTransaction())
                }
            }
        }
    }
}