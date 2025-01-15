package com.example.jetpackcompose.app.features.editFeatures

import android.util.Log
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
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.jetpackcompose.app.features.apiService.TransactionAPI.DeleteTransactionViewModel
import com.example.jetpackcompose.app.features.apiService.TransactionAPI.GetTransactionViewModel
import com.example.jetpackcompose.app.features.apiService.TransactionAPI.PutTransactionViewModel
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
import com.example.jetpackcompose.ui.theme.componentShapes
import com.example.jetpackcompose.ui.theme.textColor
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun EditIncomeTransaction(
    navController: NavHostController,
    transactionId: Int,
    transactionDate: String,
) {
    val getViewModel: GetTransactionViewModel = GetTransactionViewModel(LocalContext.current)
    val putViewModel: PutTransactionViewModel = PutTransactionViewModel(LocalContext.current)
    val delViewModel: DeleteTransactionViewModel = DeleteTransactionViewModel(LocalContext.current)

    // Trạng thái nhập liệu
    var textNote by remember { mutableStateOf(TextFieldValue()) }
    var amountValue by remember { mutableStateOf(TextFieldValue()) }
    var selectedDate by remember { mutableStateOf("") }
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
            Color(0xFF0000FF),
            percentage = 1.00f // 50%
        )
    )

    val dateParts = transactionDate.split("-")
    val year = dateParts[0].toInt()
    val month = dateParts[1].toInt()
    // Tải danh sách giao dịch và tìm giao dịch cần chỉnh sửa
    LaunchedEffect(transactionId) {
        getViewModel.getTransactions(
            month = month,
            year = year,
            onSuccess1 = { _ ->
                // Sau khi lấy tất cả giao dịch, tìm giao dịch có ID tương ứng
                val transaction = getViewModel.dateTransactionList.values.flatten()
                    .find { it.transaction_id == transactionId }

                if (transaction != null) {
                    // Cập nhật dữ liệu ban đầu vào các trường nhập liệu
                    textNote = transaction.note?.let { TextFieldValue(it) } ?: TextFieldValue("")


                    amountValue = TextFieldValue(transaction.amount.toString())


                    val year = transaction.transactionDate[0]
                    val month = transaction.transactionDate[1]
                    val day = transaction.transactionDate[2]


                    val formattedMonth = String.format("%02d", month)
                    val formattedDay = String.format("%02d", day)

                    selectedDate = "$year-$formattedMonth-$formattedDay"
                    selectedDate = selectedDate.split(" ")[0]
                    selectedCategory = categories.find { it.name == transaction.categoryName }
                } else {
                    errorMessage = "Không tìm thấy giao dịch!"
                    showPopup = true
                }
            },
            onSuccess2 = { _ -> },
            onError = { error ->
                errorMessage = error
                showPopup = true
            }
        )
    }

    // Giao diện người dùng
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Thanh tiêu đề với nút Quay lại và Xóa
        Box(modifier = Modifier
            .background(Color(0xfff1f1f1))
            .fillMaxWidth()) {
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

                // Tiêu đề "Chỉnh sửa"
                Text(
                    text = "Chỉnh sửa",
                    fontSize = 16.sp,
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                // Nút Xoá (Chưa thực hiện logic)
                Text(
                    text = "Xoá",
                    fontSize = 16.sp,
                    color = Color.Red,
                    modifier = Modifier.clickable {
                        showDeleteDialog = true
                    }
                )
            }
        }

        Divider(color = Color.LightGray, thickness = 1.dp)

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
                    onDateSelected = { date ->
                        // Lấy phần ngày hợp lệ bằng cách tách chuỗi
                        val validDate =
                            date.split(" ")[0] // Tách theo khoảng trắng và lấy phần ngày "yyyy-MM-dd"
                        selectedDate = validDate
                    },
                    initialDate = transactionDate,
                )
            }

            // Kiểm tra selectedDate có hợp lệ không trước khi parse

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

            // Nút Sửa giao dịch
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        successMessage = "Đang gửi dữ liệu..."
                        showPopup = true
                        val amount = amountValue.text.toLongOrNull() ?: 0L
                        val updatedTransaction = Transaction(
                            category_id = selectedCategory?.id ?: 10,
                            amount = amount,
                            transaction_date = selectedDate,
                            note = textNote.text
                        )
                        Log.d("EditIncomeTransaction", "Updated Transaction: $updatedTransaction")

                        // Sửa dữ liệu giao dịch
                        putViewModel.putTransaction(
                            transactionId = transactionId,
                            data = updatedTransaction,
                            onSuccess = { message ->
                                successMessage = "Chỉnh sửa thành công!"
                                showPopup = true
                                navController.popBackStack()
                            },
                            onError = { error ->
                                errorMessage = error
                                showPopup = true
                            }
                        )
                    },
                    modifier = Modifier.width(248.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF35E17))
                ) {
                    Text("Sửa khoản thu", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        MessagePopup(
            showPopup = showPopup,
            successMessage = successMessage,
            errorMessage = errorMessage,
            onDismiss = { showPopup = false }
        )

        // Popup thông báo
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                shape = componentShapes.medium,
                text = {
                    Text(
                        "Bạn có chắc chắn muốn xóa không?",
                        fontFamily = montserrat,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                buttons = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(48.dp)
                            .width(150.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text(
                                "Bỏ qua",
                                color = Color(0xff3a84fc),
                                fontFamily = montserrat,
                                textAlign = TextAlign.Start
                            )
                        }

                        TextButton(onClick = {
                            delViewModel.deleteTransaction(
                                transactionId = transactionId,
                                onSuccess = {
                                    successMessage = "Xóa giao dịch thành công!"
                                    showPopup = true
                                    showDeleteDialog = false
                                    navController.popBackStack()
                                },
                                onError = { error ->
                                    errorMessage = error
                                    showPopup = true
                                    showDeleteDialog = false
                                }
                            )
                        }) {
                            Text(
                                "Xoá",
                                color = primaryColor,
                                fontFamily = montserrat,
                            )
                        }
                    }
                }
            )
        }
    }
}