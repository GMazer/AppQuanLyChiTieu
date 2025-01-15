package com.example.jetpackcompose.app.features.inputFeatures

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.jetpackcompose.app.features.apiService.TransactionAPI.PostTransactionViewModel
import com.example.jetpackcompose.app.screens.Category
import com.example.jetpackcompose.app.screens.Transaction
import com.example.jetpackcompose.components.CategoriesGrid
import com.example.jetpackcompose.components.DatePickerButton
import com.example.jetpackcompose.components.DrawBottomLine
import com.example.jetpackcompose.components.MessagePopup
import com.example.jetpackcompose.components.NoteTextField
import com.example.jetpackcompose.components.NumberTextField
import com.example.jetpackcompose.components.montserrat
import com.example.jetpackcompose.ui.theme.primaryColor
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeContent(postViewModel: PostTransactionViewModel = PostTransactionViewModel(LocalContext.current)) {

    val currentDate = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd (E)", Locale("vi", "VN"))
    val formattedDate = dateFormat.format(currentDate)
    var textNote by remember { mutableStateOf(TextFieldValue()) }
    var amountValue by remember { mutableStateOf(TextFieldValue()) }
    var selectedDate by remember { mutableStateOf(formattedDate) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    var showPopup by remember { mutableStateOf(false) }  // Trạng thái popup

    val categories = listOf(
        Category(
            10,
            "Tiền lương",
            { painterResource(R.drawable.salary) },
            Color(0xFFfb791d),
            percentage = 1.00f // 75%
        ),
        Category(
            11,
            "Tiền thưởng",
            { painterResource(R.drawable.baseline_card_giftcard_24) },
            Color(0xFF37c166),
            percentage = 1.00f // 90%
        ),
        Category(
            12,
            "Thu nhập phụ",
            { painterResource(R.drawable.secondary) },
            Color(0xFFf95aa9),
            percentage = 1.00f // 30%
        ),
        Category(
            13,
            "Trợ cấp",
            { painterResource(R.drawable.subsidy) },
            Color(0xFF0000FF),
            percentage = 1.00f // 50%
        )
    )

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
                            fontWeight = FontWeight.Bold,
                            fontFamily = montserrat
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
                        NoteTextField(textState = textNote, onValueChange = { newValue -> textNote = newValue })
                    }

                    DrawBottomLine(16.dp)

                    // Tiền chi
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Tiền thu ", color = Color.DarkGray, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(8.dp))
                        NumberTextField(amountState = amountValue.text, onValueChange = { newValue -> amountValue = TextFieldValue(newValue) })
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
                        column = 2,
                        onCategorySelected = { category -> selectedCategory = category }
                    )

                    Spacer(Modifier.height(32.dp))

                    // Nút nhập khoản chi
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {
                                successMessage = "Đang gửi dữ liệu..."
                                showPopup = true
                                val amount = amountValue.text.toLongOrNull() ?: 0L

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
                                        successMessage = "Nhập khoản thu thành công!"
                                        showPopup = true  // Hiển thị popup thông báo thành công
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
                            Text("Nhập khoản thu", color = Color.White, fontWeight = FontWeight.Bold)
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
fun PreviewIncomeContent() {
//    IncomeContent()
}
