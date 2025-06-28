package com.example.jetpackcompose.app.features.inputFeatures

import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.apiService.TransactionAPI.GetLimitTransactionViewModel
import com.example.jetpackcompose.app.features.apiService.TransactionAPI.PostTransactionViewModel
import com.example.jetpackcompose.app.screens.Category
import com.example.jetpackcompose.app.screens.RemainLimit
import com.example.jetpackcompose.app.screens.Transaction
import com.example.jetpackcompose.components.CategoriesGrid
import com.example.jetpackcompose.components.DatePickerButton
import com.example.jetpackcompose.components.DrawBottomLine
import com.example.jetpackcompose.components.MessagePopup
import com.example.jetpackcompose.components.NoteTextField
import com.example.jetpackcompose.components.NumberTextField
import com.example.jetpackcompose.components.myFont
import com.example.jetpackcompose.ui.theme.primaryColor
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ExpenseContent(
    postViewModel: PostTransactionViewModel = PostTransactionViewModel(LocalContext.current),
    getRemainLimit: GetLimitTransactionViewModel = GetLimitTransactionViewModel(LocalContext.current)
) {
    // State cho các dữ liệu nhập vào và thông báo
    var categoryLimits by remember {
        mutableStateOf(
            listOf(
                RemainLimit.CategoryLimit(
                    categoryId = 1,
                    limitExpense = 0,
                    remainingPercent = 1.00
                ),
                RemainLimit.CategoryLimit(
                    categoryId = 2,
                    limitExpense = 0,
                    remainingPercent = 1.00
                ),
                RemainLimit.CategoryLimit(
                    categoryId = 3,
                    limitExpense = 0,
                    remainingPercent = 1.00
                ),
                RemainLimit.CategoryLimit(
                    categoryId = 4,
                    limitExpense = 0,
                    remainingPercent = 1.00
                ),
                RemainLimit.CategoryLimit(categoryId = 5, limitExpense = 0, remainingPercent = 1.00)
            )
        )
    }

    val currentDate = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd (E)", Locale("vi", "VN"))
    val formattedDate = dateFormat.format(currentDate)
    var textNote by remember { mutableStateOf(TextFieldValue()) }
    var amountValue by remember { mutableStateOf(TextFieldValue()) }
    var rawAmount by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(formattedDate.toString()) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    var showPopup by remember { mutableStateOf(false) }  // Trạng thái popup

    // Cập nhật lại danh sách categories
    var categories by remember {
        mutableStateOf(
            listOf(
                Category(
                    1,
                    "Chi phí nhà ở",
                    { painterResource(R.drawable.outline_home_work_24) },
                    Color(0xFFB40300),
                    1.00f
                ),
                Category(
                    2,
                    "Ăn uống",
                    { painterResource(R.drawable.outline_ramen_dining_24) },
                    Color(0xFF911294),
                    1.00f
                ),
                Category(
                    3,
                    "Mua sắm quần áo",
                    { painterResource(R.drawable.clothes) },
                    Color(0xFF0C326E),
                    1.00f
                ),
                Category(
                    4,
                    "Đi lại",
                    { painterResource(R.drawable.outline_train_24) },
                    Color(0xFF126AB6),
                    1.00f
                ),
                Category(
                    5,
                    "Chăm sóc sắc đẹp",
                    { painterResource(R.drawable.outline_cosmetic) },
                    Color(0xFF0D96DA),
                    1.00f
                ),
                Category(
                    6,
                    "Giao lưu",
                    { painterResource(R.drawable.entertainment) },
                    Color(0xFF4DB218),
                    1.00f
                ),
                Category(
                    7,
                    "Y tế",
                    { painterResource(R.drawable.outline_health_and_safety_24) },
                    Color(0xFFD5CC00),
                    1.00f
                ),
                Category(
                    8,
                    "Học tập",
                    { painterResource(R.drawable.outline_education) },
                    Color(0xFFEE9305),
                    1.00f
                )
            )
        )
    }

    // Hàm cập nhật lại tỷ lệ phần trăm cho các danh mục chi tiêu
    fun updatePercentage(categoryLimits: List<RemainLimit.CategoryLimit>) {
        val updatedCategories = categories.map { category ->
            val updatedLimit = categoryLimits.find { it.categoryId == category.id }
            if (updatedLimit != null) {
                val updatedPercentage = (updatedLimit.remainingPercent / 100.0).toFloat()
                category.copy(percentage = updatedPercentage)
            } else {
                category
            }
        }
        categories = updatedCategories
    }

    // Lần đầu tiên gọi API để lấy thông tin limit
    LaunchedEffect(key1 = true) {
        getRemainLimit.getLimitTransaction(
            onError = {},
            onSuccess = {
                categoryLimits = it
                updatePercentage(categoryLimits)  // Cập nhật lại tỷ lệ phần trăm sau khi nhận dữ liệu
            }
        )
    }

    Scaffold(
        containerColor = Color.White,
        content = { innerPadding ->
            Box(Modifier.fillMaxSize()) {
                // Nội dung của ExpenseContent
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                        .background(Color.White)
                ) {
                    // Chọn ngày
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Ngày ",
                            color = Color.DarkGray,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = myFont
                        )
                        DatePickerButton(
                            onDateSelected = { date -> selectedDate = date },
                            initialDate = ""
                        )
                    }

                    DrawBottomLine(16.dp)
                    // Ghi chú
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Ghi chú ", color = Color.DarkGray, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(8.dp))
                        NoteTextField(
                            textState = textNote,
                            onValueChange = { newValue -> textNote = newValue })
                    }

                    DrawBottomLine(16.dp)

                    // Tiền chi
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Tiền chi ", color = Color.DarkGray, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(16.dp))
                        NumberTextField(
                            amountState = amountValue,
                            onValueChange = { newValue -> amountValue = newValue },
                            onRawValueChange = { newRawValue -> rawAmount = newRawValue }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("₫", color = Color.DarkGray)
                    }

                    DrawBottomLine(16.dp)

                    // Danh mục chi tiêu
                    Text("Danh mục", color = Color.DarkGray, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(24.dp))

                    // Gọi danh mục chi tiêu và truyền callback
                    CategoriesGrid(
                        categories = categories,
                        buttonColor = primaryColor,
                        selectedCategory = selectedCategory,
                        column = 3,
                        onCategorySelected = { category -> selectedCategory = category }
                    )

                    Spacer(Modifier.height(16.dp))

                    // Nút nhập khoản chi
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                successMessage = "Đang gửi dữ liệu..."
                                showPopup = true
                                val amount = rawAmount.toLongOrNull() ?: 0L
                                val transaction = Transaction(
                                    transaction_date = selectedDate.substring(0, 11).trimEnd(),
                                    note = textNote.text,
                                    amount = amount,
                                    category_id = selectedCategory?.id ?: 0
                                )
                                // Gửi dữ liệu giao dịch
                                postViewModel.postTransaction(
                                    transaction,
                                    onSuccess = {
                                        errorMessage = ""  // Reset error message
                                        successMessage = "Nhập khoản chi thành công!"
                                        showPopup = true  // Hiển thị popup thông báo thành công
                                        // Cập nhật lại dữ liệu limit và categories
                                        getRemainLimit.getLimitTransaction(
                                            onError = {},
                                            onSuccess = {
                                                categoryLimits = it
                                                updatePercentage(categoryLimits)
                                            }
                                        )
                                    },
                                    onError = {
                                        successMessage = ""  // Reset success message
                                        errorMessage = "Có lỗi xảy ra vui lòng thử lại!"
                                        showPopup = true  // Hiển thị popup thông báo lỗi
                                    }
                                )

                                // Reset các trường nhập
                                amountValue = TextFieldValue("")
                                textNote = TextFieldValue("")
                                selectedCategory = null
                            },
                            modifier = Modifier.width(248.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                        ) {
                            Text(
                                "Nhập khoản chi",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Hiển thị MessagePopup ở trên cùng
                MessagePopup(
                    showPopup = showPopup,
                    successMessage = successMessage,
                    errorMessage = errorMessage,
                    onDismiss = { showPopup = false }
                )
            }
        }
    )
}


@Preview
@Composable
fun PreviewOutComeContent() {
    ExpenseContent()
}