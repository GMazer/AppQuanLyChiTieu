package com.example.jetpackcompose.app.features.apiService.ReadNotificationTransaction

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
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.apiService.TransactionAPI.PostTransactionViewModel
import com.example.jetpackcompose.app.screens.Category
import com.example.jetpackcompose.app.screens.Transaction
import com.example.jetpackcompose.components.CategoriesGrid
import com.example.jetpackcompose.components.DatePickerButton
import com.example.jetpackcompose.components.DrawBottomLine
import com.example.jetpackcompose.components.MessagePopup
import com.example.jetpackcompose.components.MyButtonComponent
import com.example.jetpackcompose.components.NoteTextField
import com.example.jetpackcompose.components.NumberTextField
import com.example.jetpackcompose.components.montserrat
import com.example.jetpackcompose.ui.theme.primaryColor
import com.example.jetpackcompose.ui.theme.textColor

@Composable
fun PostIncomeNotiTransaction(
    navController: NavHostController,
    amount: Long,
    selectedDate: String,
    index: Int
) {
    val postViewModel: PostTransactionViewModel = PostTransactionViewModel(LocalContext.current)

    val context = LocalContext.current

    // Tạo đối tượng TransactionStorage
    val transactionStorage = TransactionStorage(context)

    // Trạng thái nhập liệu
    var textNote by remember { mutableStateOf(TextFieldValue()) }
    var amountValue by remember { mutableStateOf(TextFieldValue(amount.toString())) }
    var selectedDateState by remember { mutableStateOf(selectedDate) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    // Thông báo lỗi và thành công
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    var showPopup by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Danh sách các Category
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
            Color(0xFFfba74a),
            percentage = 1.00f // 50%
        )
    )

    // Giao diện người dùng
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Thanh tiêu đề với nút Quay lại và Xóa
        Box(
            modifier = Modifier
                .background(Color(0xfff1f1f1))
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Nút Quay lại
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_arrow_back_ios_24),
                        contentDescription = "Back",
                        tint = textColor
                    )
                }

                // Tiêu đề "Nhập khoản chi"
                Text(
                    text = "Nhập khoản thu",
                    fontSize = 16.sp,
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                // Nút Xoá
                Text(
                    text = "Xoá",
                    fontSize = 16.sp,
                    color = primaryColor,
                    fontFamily = montserrat,
                    modifier = Modifier.clickable {
                        showDeleteDialog = true
                    }
                )
            }
        }

        Divider(color = Color.LightGray, thickness = 1.dp)

        MessagePopup(
            successMessage = successMessage,
            errorMessage = errorMessage,
            showPopup = showPopup,
            onDismiss = { showPopup = false }
        )

        // Các trường nhập liệu
        Column(
            modifier = Modifier
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
                    initialDate = selectedDateState,
                    onDateSelected = { date ->
                        val validDate = date.split(" ")[0]
                        selectedDateState = validDate
                    },
                )
            }

            DrawBottomLine(16.dp)

            // Ghi chú
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Ghi chú ",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(8.dp))
                NoteTextField(
                    textState = textNote,
                    onValueChange = { newValue -> textNote = newValue }
                )
            }

            DrawBottomLine(16.dp)

            // Tiền chi
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Tiền chi ",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(8.dp))
                NumberTextField(
                    amountState = amountValue.text,
                    onValueChange = { newValue -> amountValue = TextFieldValue(newValue) }
                )
                Spacer(Modifier.width(8.dp))
                Text("₫", color = Color.DarkGray)
            }

            DrawBottomLine(16.dp)

            // Danh mục chi tiêu
            Text("Danh mục", color = Color.DarkGray, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(24.dp))

            // Grid chọn danh mục
            CategoriesGrid(
                categories = categories,
                buttonColor = Color(0xFFF35E17),
                selectedCategory = selectedCategory,
                column = 3,
                onCategorySelected = { category -> selectedCategory = category }
            )

            Spacer(Modifier.height(32.dp))

            // Nút Nhập giao dịch
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                MyButtonComponent(
                    value = "Nhập khoản thu",
                    onClick = {
                        successMessage = "Đang gửi dữ liệu..."
                        showPopup = true
                        val amount = amountValue.text.toLongOrNull() ?: 0L
                        val transaction = Transaction(
                            transaction_date = selectedDateState,
                            note = textNote.text,
                            amount = amount,
                            category_id = selectedCategory?.id ?: 0
                        )

                        // Gửi dữ liệu giao dịch
                        postViewModel.postTransaction(
                            transaction,
                            onSuccess = {
                                errorMessage = ""
                                successMessage = "Nhập khoản chi thành công!"
                                showPopup = true
                                transactionStorage.deleteTransactionByIndex(index)
                                navController.popBackStack()
                            },
                            onError = {
                                successMessage = ""
                                errorMessage = "Có lỗi xảy ra vui lòng thử lại!"
                                showPopup = true
                            }
                        )

                        amountValue = TextFieldValue("")
                        textNote = TextFieldValue("")
                        selectedCategory = null
                    },
                    isLoading = false
                )
            }
        }
    }

    // Dialog xác nhận xóa
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    "Xác nhận xóa",
                    fontFamily = montserrat,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
            },
            text = { Text(
                "Bạn có chắc chắn muốn xóa giao dịch mà không thêm không?",
                fontFamily = montserrat,
                fontWeight = FontWeight.SemiBold,
            ) },
            confirmButton = {
                TextButton(onClick = {
                    transactionStorage.deleteTransactionByIndex(index)
                    showDeleteDialog = false
                    navController.popBackStack() // Trở về màn hình trước
                }) {
                    Text(
                        "OK",
                        fontFamily = montserrat,
                        color = Color.Red
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(
                        "Bỏ qua",
                        color = Color.Gray,
                        fontFamily = montserrat
                    )
                }
            }
        )
    }
}
