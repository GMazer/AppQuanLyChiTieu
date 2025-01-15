package com.example.jetpackcompose.app.screens.anual_sceens

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI.FixedTransaction
import com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI.PostFixedTransactionViewModel
import com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI.RepeatFrequency
import com.example.jetpackcompose.components.DatePickerRow
import com.example.jetpackcompose.components.DropdownRepeat
import com.example.jetpackcompose.components.DropdownRow
import com.example.jetpackcompose.components.EndDateRow
import com.example.jetpackcompose.components.MessagePopup
import com.example.jetpackcompose.components.MyButtonComponent
import com.example.jetpackcompose.components.RowNumberField
import com.example.jetpackcompose.components.RowTextField
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@SuppressLint("NewApi")
@Composable
fun FixedIncome(
    viewModel: PostFixedTransactionViewModel = PostFixedTransactionViewModel(LocalContext.current),
    navController: NavController
) {

    // Dữ liệu cần thiết cho form
    val vietnamLocale = Locale("vi", "VN")
    val currentDate = remember { SimpleDateFormat("yyyy-MM-dd", vietnamLocale).format(Date()) }

    var titleState by remember { mutableStateOf(TextFieldValue("")) }
    var selectedCategory by remember { mutableStateOf("Tiền lương") }
    var selectedRepeat by remember { mutableStateOf(RepeatFrequency.daily) } // Thay đổi thành enum
    var selectedDate by remember { mutableStateOf(currentDate) }
    var selectedEndDate by remember { mutableStateOf("") }
    var amountState by remember { mutableStateOf(TextFieldValue("")) }

    // State for handling success/error message
    var statusMessage by remember { mutableStateOf("") }
    var statusColor by remember { mutableStateOf(Color.Red) }

    // State for MessagePopup
    var showPopup by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
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
                            Pair(R.drawable.salary, "Tiền lương"),
                            Pair(R.drawable.baseline_card_giftcard_24, "Tiền thưởng"),
                            Pair(R.drawable.secondary, "Thu nhập phụ"),
                            Pair(R.drawable.subsidy, "Trợ cấp")
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
                    DropdownRepeat(
                        initialValue = 0,
                        label = "Lặp lại",
                        options = RepeatFrequency.values().map {
                            it.displayName to it
                        } // Lấy tất cả giá trị enum
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

                    EndDateRow(
                        label = "Kết thúc",
                        initialDate = selectedEndDate,
                        onDateSelected = { date ->
                            selectedEndDate = date
                        }
                    )
                }
            }

            // Nút thêm giao dịch
            MyButtonComponent(
                value = "Thêm",
                isLoading = isLoading,
                onClick = {
                    isLoading = true
                    // Chuyển giá trị sang FixedTransaction và gọi ViewModel để thêm
                    val amount = amountState.text.toLongOrNull() ?: 0L

                    val fixedTransaction = FixedTransaction(
                        category_id = when (selectedCategory) {
                            "Tiền lương" -> 10
                            "Tiền thưởng" -> 11
                            "Thu nhập phụ" -> 12
                            "Trợ cấp" -> 13
                            else -> 0
                        },
                        title = titleState.text,
                        amount = amount,
                        repeat_frequency = selectedRepeat, // Sử dụng enum RepeatFrequency
                        start_date = selectedDate,
                        end_date = selectedEndDate
                    )

                    Log.i("FixedIncome", "FixedTransaction: $fixedTransaction")

                    // Gọi ViewModel để thêm giao dịch và xử lý kết quả
                    viewModel.addFixedTransaction(fixedTransaction,
                        onSuccess = { message ->
                            navController.popBackStack("anual", inclusive = false)
                            // Cập nhật thông báo thành công và hiển thị popup
                            successMessage = "Gửi dữ liệu thành công"
                            errorMessage = ""
                            statusMessage = message
                            statusColor = Color.Green
                            showPopup = true // Hiển thị popup thành công
                        },
                        onError = { message ->
                            // Cập nhật thông báo lỗi và hiển thị popup
                            successMessage = ""
                            errorMessage = selectedDate
                            statusMessage = message
                            statusColor = Color.Red
                            showPopup = true // Hiển thị popup lỗi
                        }
                    )
                }
            )
        }

        // Hiển thị thông báo popup
        MessagePopup(
            showPopup = showPopup,
            successMessage = successMessage,
            errorMessage = errorMessage,
            onDismiss = { showPopup = false } // Đóng popup khi nhấn ngoài
        )
    }
}

