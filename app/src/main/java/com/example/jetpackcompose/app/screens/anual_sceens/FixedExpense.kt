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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.screens.anual_sceens.ViewModel.FixedExpenseViewModel
import com.example.jetpackcompose.app.screens.anual_sceens.ViewModel.FixedTransaction
import com.example.jetpackcompose.app.screens.anual_sceens.ViewModel.RepeatFrequency
import com.example.jetpackcompose.components.DatePickerRow
import com.example.jetpackcompose.components.DropdownRepeat
import com.example.jetpackcompose.components.DropdownRow
import com.example.jetpackcompose.components.EndDateRow
import com.example.jetpackcompose.components.MyButtonComponent
import com.example.jetpackcompose.components.RowNumberField
import com.example.jetpackcompose.components.RowTextField
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@SuppressLint("NewApi")
@Composable
fun FixedExpense(viewModel: FixedExpenseViewModel = FixedExpenseViewModel(LocalContext.current)) {

    // Dữ liệu cần thiết cho form
    val vietnamLocale = Locale("vi", "VN")
    val currentDate = remember { SimpleDateFormat("dd/MM/yyyy", vietnamLocale).format(Date()) }

    var titleState by remember { mutableStateOf(TextFieldValue("")) }
    var selectedCategory by remember { mutableStateOf("Thiết yếu") }
    var selectedRepeat by remember { mutableStateOf(RepeatFrequency.DAILY) } // Thay đổi thành enum
    var selectedDate by remember { mutableStateOf(currentDate) }
    var selectedEndDate by remember { mutableStateOf("") }
    var amountState by remember { mutableStateOf(TextFieldValue("")) }

    // State for handling success/error message
    var statusMessage by remember { mutableStateOf("") }
    var statusColor by remember { mutableStateOf(Color.Red) }

    // Hiển thị kết quả từ ViewModel
    if (statusMessage.isNotEmpty()) {
        Text(text = statusMessage, color = statusColor)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        // Tiêu đề và số tiền
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            Column {
                RowTextField(
                    label = "Tiêu đề",
                    textState = titleState,
                    onValueChange = { newValue -> titleState = newValue }
                )
                Divider(color = Color(0xFFd4d4d4), thickness = 0.5.dp)

                RowNumberField(
                    textState = amountState,
                    onValueChange = { newValue -> amountState = newValue }
                )
                Divider(color = Color(0xFFd4d4d4), thickness = 0.5.dp)

                DropdownRow(
                    label = "Danh mục",
                    options = listOf(
                        Pair(R.drawable.essentials, "Thiết yếu"),
                        Pair(R.drawable.entertainment, "Giải trí"),
                        Pair(R.drawable.invest, "Đầu tư"),
                        Pair(R.drawable.hedgefund, "Dự phòng")
                    )
                ) { category ->
                    selectedCategory = category
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lặp lại và ngày bắt đầu
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            Column {
                DropdownRepeat (
                    label = "Lặp lại",
                    options = RepeatFrequency.values().map { it.displayName to it } // Lấy tất cả giá trị enum
                ) { repeat ->
                    selectedRepeat = repeat // Lưu enum thay vì chuỗi
                }
                Divider(color = Color(0xFFd4d4d4), thickness = 0.5.dp)

                DatePickerRow(
                    label = "Bắt đầu",
                    initialDate = LocalDate.now()
                ) { date ->
                    selectedDate = date.toString()
                }
                Divider(color = Color(0xFFd4d4d4), thickness = 0.5.dp)

                EndDateRow { endDate ->
                    // Xử lý ngày kết thúc
                    selectedEndDate = endDate
                }
            }
        }

        // Nút thêm giao dịch
        MyButtonComponent(
            value = "Thêm",
            onClick = {
                // Chuyển giá trị sang FixedTransaction và gọi ViewModel để thêm
                val amount = amountState.text.toLongOrNull() ?: 0L

                val fixedTransaction = FixedTransaction(
                    category_id = when (selectedCategory) {
                        "Thiết yếu" -> 1
                        "Giải trí" -> 2
                        "Đầu tư" -> 3
                        "Dự phòng" -> 4
                        else -> 0
                    },
                    title = titleState.text,
                    amount = amount,
                    type = "expense", // Chắc chắn đây là chi tiêu
                    repeat_frequency = selectedRepeat, // Sử dụng enum RepeatFrequency
                    start_date = selectedDate,
                    end_date = selectedEndDate // Chưa xử lý ngày kết thúc
                )

                // Gọi ViewModel để thêm giao dịch và xử lý kết quả
                viewModel.addFixedTransaction(fixedTransaction,
                    onSuccess = { message ->
                        // Cập nhật thông báo thành công
                        statusMessage = message
                        statusColor = Color.Green
                    },
                    onError = { message ->
                        // Cập nhật thông báo lỗi
                        statusMessage = message
                        statusColor = Color.Red
                    }
                )
            }
        )
    }
}







