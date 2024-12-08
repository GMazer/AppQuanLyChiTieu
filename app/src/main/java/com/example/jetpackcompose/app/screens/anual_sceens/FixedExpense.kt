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
import com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI.PostFixedTransactionViewModel
import com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI.FixedTransaction
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
fun FixedExpense(
    viewModel: PostFixedTransactionViewModel = PostFixedTransactionViewModel(LocalContext.current),
    navController: NavController
) {

    // Dữ liệu cần thiết cho form
    val vietnamLocale = Locale("vi", "VN")
    val currentDate = remember { SimpleDateFormat("yyyy-MM-dd", vietnamLocale).format(Date()) }

    var titleState by remember { mutableStateOf(TextFieldValue("")) }
    var selectedCategory by remember { mutableStateOf("Chi phí nhà ở") }
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
                            Pair(R.drawable.outline_home_work_24, "Chi phí nhà ở"),
                            Pair(R.drawable.outline_ramen_dining_24, "Ăn uống"),
                            Pair(R.drawable.clothes, "Mua sắm quần áo"),
                            Pair(R.drawable.outline_train_24, "Đi lại"),
                            Pair(R.drawable.outline_cosmetic, "Chăm sóc sắc đẹp"),
                            Pair(R.drawable.entertainment, "Giao lưu"),
                            Pair(R.drawable.outline_health_and_safety_24, "Y tế"),
                            Pair(R.drawable.outline_education, "Học tập"),
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
                isLoading = isLoading,
                onClick = {
                    isLoading = true
                    // Chuyển giá trị sang FixedTransaction và gọi ViewModel để thêm
                    val amount = amountState.text.toLongOrNull() ?: 0L

                    val fixedTransaction = FixedTransaction(
                        category_id = when (selectedCategory) {
                            "Chi phí nhà ở" -> 1
                            "Ăn uống" -> 2
                            "Mua sắm quần áo" -> 3
                            "Đi lại" -> 4
                            "Chăm sóc sắc đẹp" -> 5
                            "Giao lưu" -> 6
                            "Y tế" -> 7
                            "Học tập" -> 8
                            else -> 0
                        },
                        title = titleState.text,
                        amount = amount,
                        repeat_frequency = selectedRepeat, // Sử dụng enum RepeatFrequency
                        start_date = selectedDate,
                        end_date = selectedEndDate
                    )

                    Log.i("FixedExpense", "FixedTransaction: $fixedTransaction")

                    // Gọi ViewModel để thêm giao dịch và xử lý kết quả
                    viewModel.addFixedTransaction(fixedTransaction,
                        onSuccess = { message ->
                            // Cập nhật thông báo thành công và hiển thị popup
                            successMessage = "Gửi dữ liệu thành công!"
                            errorMessage = ""
                            statusMessage = message
                            statusColor = Color.Green
                            showPopup = true // Hiển thị popup thành công
                            navController.popBackStack("anual", inclusive = false)
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









