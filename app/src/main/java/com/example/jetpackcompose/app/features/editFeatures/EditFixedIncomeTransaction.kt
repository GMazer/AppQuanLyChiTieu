package com.example.jetpackcompose.app.features.editFeatures

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI.FixedTransactionResponse
import com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI.FixedTransactionUpdate
import com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI.GetFixedTransactionViewModel
import com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI.PutFixedTransactionViewModel
import com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI.RepeatFrequency
import com.example.jetpackcompose.components.DatePickerRow
import com.example.jetpackcompose.components.DropdownRepeat
import com.example.jetpackcompose.components.DropdownRow
import com.example.jetpackcompose.components.EndDateRow
import com.example.jetpackcompose.components.MessagePopup
import com.example.jetpackcompose.components.MyButtonComponent
import com.example.jetpackcompose.components.RowNumberField
import com.example.jetpackcompose.components.RowTextField
import com.example.jetpackcompose.components.montserrat
import com.example.jetpackcompose.ui.theme.textColor
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale

@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditIncomeExpenseTransaction(
    navController: NavHostController,
    fixedTransactionId: Int,
    startDate: String?,
    endDate: String?
) {
    val getFixedTransactionViewModel: GetFixedTransactionViewModel =
        GetFixedTransactionViewModel(LocalContext.current)
    val putFixedTransactionViewModel: PutFixedTransactionViewModel =
        PutFixedTransactionViewModel(LocalContext.current)

    val vietnamLocale = Locale("vi", "VN")
    val currentDate = remember { SimpleDateFormat("yyyy-MM-dd", vietnamLocale).format(Date()) }

    // State để giữ giá trị các trường nhập liệu
    var titleState by remember { mutableStateOf(TextFieldValue("")) }
    var selectedCategory by remember { mutableStateOf("Chi phí nhà ở") }
    var selectedRepeat by remember { mutableStateOf(RepeatFrequency.daily) }
    var selectedDate by remember { mutableStateOf(currentDate) }
    var selectedEndDate by remember { mutableStateOf("") }
    var amountState by remember { mutableStateOf(TextFieldValue("")) }

    // Trạng thái hiển thị thông báo
    var showPopup by remember { mutableStateOf(false) }
    var successMessage2 by remember { mutableStateOf("Đang tải dữ liệu") }
    var errorMessage2 by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }


    // Dữ liệu giao dịch cố định
    var fixedTransaction: FixedTransactionResponse? by remember { mutableStateOf(null) }

    // Lấy thông tin giao dịch cố định khi màn hình được mở
    LaunchedEffect(fixedTransactionId) {
        successMessage2 = "Đang tải dữ liệu..."
        showPopup = true
        getFixedTransactionViewModel.getFixedTransactions(
            onSuccess = { transactionList ->
                showPopup = false
                fixedTransaction =
                    transactionList.find { it.fixed_transaction_id == fixedTransactionId }

                // Điền dữ liệu vào các trường nhập liệu
                fixedTransaction?.let {
                    val calendar = Calendar.getInstance()
                    calendar.set(it.startDate[0], it.startDate[1] - 1, it.startDate[2]) // Month trừ 1 vì Calendar sử dụng 0-based indexing cho tháng.

                    titleState = TextFieldValue(it.title ?: "")
                    selectedCategory = it.categoryName

                    Log.d("EditFixedExpenseTransaction", "Category repeat: ${it.repeate_frequency}")
                    selectedRepeat = RepeatFrequency.valueOf(it.repeate_frequency)
                    Log.d("EditFixedExpenseTransaction", "Category repeat: $selectedRepeat")

                    amountState = TextFieldValue(it.amount.toString())
                    selectedDate = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
                    Log.d("EditFixedExpenseTransaction", "Start Date: ${it.startDate}")
                    selectedEndDate = it.endDate?.let { endDateList ->
                        SimpleDateFormat("yyyy-MM-dd").format(Date(endDateList[0].toLong()))
                    } ?: ""
                }
            },
            onError = { errorMessage ->
                Log.d("EditFixedExpenseTransaction", "Error: $errorMessage")
                errorMessage2 = "Có lỗi xảy ra khi tải giao dịch!"
                showPopup = true
            }
        )
    }

    Column(
        modifier = Modifier
            .background(Color(0xfff5f5f5))
            .fillMaxSize()
    ) {
        // Thanh tiêu đề với nút Quay lại và Xóa
        Box(
            modifier = Modifier
                .background(Color(0xfff1f1f1))
                .height(50.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_arrow_back_ios_24),
                        contentDescription = "Back",
                        tint = textColor
                    )
                }
                Text(
                    text = "Chỉnh sửa thu chi cố định",
                    fontSize = 16.sp,
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Divider(
            color = Color.LightGray,
            thickness = 1.dp
        )

        // Hiển thị thông báo thành công hay thất bại
        MessagePopup(
            showPopup = showPopup,
            successMessage = successMessage2,
            errorMessage = errorMessage2,
            onDismiss = {
                showPopup = false
                successMessage2 = ""
                errorMessage2 = ""
            }
        )

        Column(
            modifier = Modifier
                .background(Color(0xfff5f5f5))
                .fillMaxSize()
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
                    Log.d("EditFixedIncomeTransaction", "selectedRepeat: $selectedRepeat")
                    DropdownRepeat(
                        initialValue = when (selectedRepeat) {
                            RepeatFrequency.daily -> 0
                            RepeatFrequency.weekly -> 1
                            RepeatFrequency.monthly -> 2
                            RepeatFrequency.yearly -> 3
                            else -> 0  // Default giá trị nếu không có
                        },
                        label = "Lặp lại",
                        options = RepeatFrequency.entries
                            .map { it.displayName to it }
                    ) { repeat ->
                        selectedRepeat = repeat
                        Log.d("EditFixedExpenseTransaction", "Category repeat display: $selectedRepeat")
                    }
                    Divider(color = Color(0xFFd4d4d4), thickness = 0.5.dp)

                    DatePickerRow(
                        label = "Bắt đầu",
                        initialDate = LocalDate.parse(startDate),
                    ) { date ->
                        selectedDate = date
                    }
                    Divider(color = Color(0xFFd4d4d4), thickness = 0.5.dp)

                    EndDateRow(
                        label = "Kết thúc",
                        initialDate = endDate,
                        onDateSelected = { date ->
                            selectedEndDate = date
                        }
                    )
                }
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 50.dp)
                    .fillMaxWidth()
            ) {
                MyButtonComponent(
                    value = "Chỉnh sửa thu nhập",
                    isLoading = isLoading,
                    onClick = {
                        isLoading = true
                        // Tạo đối tượng FixedTransactionUpdate
                        val updatedTransaction = FixedTransactionUpdate(
                            category_id = when (selectedCategory) {
                                "Tiền lương" -> 10
                                "Tiền thưởng" -> 11
                                "Thu nhập phụ" -> 12
                                "Trợ cấp" -> 13
                                else -> 0
                            },  // Sử dụng hashCode để làm ví dụ
                            title = titleState.text,
                            amount = amountState.text.toLong(),
                            type = "income",
                            repeat_frequency = selectedRepeat.name,
                            start_date = selectedDate,
                            end_date = selectedEndDate
                        )

                        Log.d(
                            "EditFixedExpenseTransaction",
                            "Fixed Transaction ID: $fixedTransactionId"
                        )
                        Log.d(
                            "EditFixedExpenseTransaction",
                            "Updated Transaction: $updatedTransaction"
                        )
                        // Gọi PutFixedTransactionViewModel để cập nhật giao dịch
                        putFixedTransactionViewModel.putFixedTransaction(
                            fixed_transaction_id = fixedTransactionId,
                            data = updatedTransaction,
                            onSuccess = { successMessage ->
                                showPopup = true
                                successMessage2 = successMessage
                                navController.popBackStack("anual", inclusive = false)
                            },
                            onError = { errorMessage ->
                                showPopup = true
                                errorMessage2 = errorMessage
                            }
                        )
                    }
                )
            }
        }
    }
}