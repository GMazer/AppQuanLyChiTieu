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
import androidx.compose.foundation.layout.widthIn
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
import com.example.jetpackcompose.app.features.inputFeatures.Category
import com.example.jetpackcompose.app.features.inputFeatures.DatePickerButton
import com.example.jetpackcompose.app.features.inputFeatures.Transaction
import com.example.jetpackcompose.app.features.inputFeatures.montserrat
import com.example.jetpackcompose.components.CategoriesGrid
import com.example.jetpackcompose.components.DrawBottomLine
import com.example.jetpackcompose.components.MessagePopup
import com.example.jetpackcompose.components.NoteTextField
import com.example.jetpackcompose.components.NumberTextField
import com.example.jetpackcompose.ui.theme.colorPrimary
import com.example.jetpackcompose.ui.theme.componentShapes
import com.example.jetpackcompose.ui.theme.textColor
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun EditExpenseTransaction(
    navController: NavHostController,
    transactionId: Int,
) {
    val getViewModel: GetTransactionViewModel = GetTransactionViewModel(LocalContext.current)
    val putViewModel: PutTransactionViewModel = PutTransactionViewModel(LocalContext.current)
    val delViewModel: DeleteTransactionViewModel = DeleteTransactionViewModel(LocalContext.current)
    // Trạng thái nhập liệu
    var textNote by remember { mutableStateOf(TextFieldValue()) }
    var amountValue by remember { mutableStateOf(TextFieldValue()) }
    var selectedDate by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }


    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    // Trạng thái hiển thị Popup
    var showPopup by remember { mutableStateOf(false) }

    var showDeleteDialog by remember { mutableStateOf(false) }

    // Định dạng tiền tệ
    val currencyFormatter = remember {
        val symbols = DecimalFormatSymbols(Locale("vi", "VN"))
        symbols.decimalSeparator = '.'
        symbols.groupingSeparator = ','
        DecimalFormat("#,###", symbols)
    }

    val dateFormatter = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Định dạng ngày "yyyy-MM-dd"
    }

    // Danh sách các Category
    val categories = listOf(
        Category(
            1,
            "Chi phí nhà ở",
            { painterResource(R.drawable.outline_home_work_24) },
            Color(0xFFfb791d),
            1.00f
        ),
        Category(
            2,
            "Ăn uống",
            { painterResource(R.drawable.outline_ramen_dining_24) },
            Color(0xFF37c166),
            1.00f
        ),
        Category(
            3,
            "Mua sắm quần áo",
            { painterResource(R.drawable.clothes) },
            Color(0xFF283eaa),
            1.00f
        ),
        Category(
            4,
            "Đi lại",
            { painterResource(R.drawable.outline_train_24) },
            Color(0xFFa06749),
            1.00f
        ),
        Category(
            5,
            "Chăm sóc sắc đẹp",
            { painterResource(R.drawable.outline_cosmetic) },
            Color(0xFFf95aa9),
            1.00f
        ),
        Category(
            6,
            "Giao lưu",
            { painterResource(R.drawable.entertainment) },
            Color(0xFF6a1b9a),
            1.00f
        ),
        Category(
            7,
            "Y tế",
            { painterResource(R.drawable.outline_health_and_safety_24) },
            Color(0xFFfc3d39),
            1.00f
        ),
        Category(
            8,
            "Học tập",
            { painterResource(R.drawable.outline_education) },
            Color(0xFFfc7c1f),
            0.50f
        )
    )

    // Tải danh sách giao dịch và tìm giao dịch cần chỉnh sửa
    LaunchedEffect(transactionId) {
        getViewModel.getTransactions(
            month = 12,
            year = 2024,
            onSuccess1 = { _ ->
                // Sau khi lấy tất cả giao dịch, tìm giao dịch có ID tương ứng
                val transaction = getViewModel.dateTransactionList.values.flatten()
                    .find { it.transaction_id == transactionId }

                if (transaction != null) {
                    // Cập nhật dữ liệu ban đầu vào các trường nhập liệu
                    textNote = transaction.note?.let { TextFieldValue(it) } ?: TextFieldValue("")

                    // Cập nhật amount
                    amountValue = TextFieldValue(transaction.amount.toString())

                    // Cập nhật ngày và loại bỏ phần thông tin " (Th 7)"
                    val year = transaction.transactionDate[0]
                    val month = transaction.transactionDate[1]
                    val day = transaction.transactionDate[2]

                    // Dùng String.format để định dạng tháng và ngày
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
        Box(modifier = Modifier.fillMaxWidth()) {
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

        Divider(modifier = Modifier.padding(vertical = 8.dp))

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
                        val validDate = date.split(" ")[0] // Tách theo khoảng trắng và lấy phần ngày "yyyy-MM-dd"

                        selectedDate = validDate
                    },
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
                        val amount = amountValue.text.toLongOrNull() ?: 0L
                        val updatedTransaction = Transaction(
                            category_id = selectedCategory?.id ?: 1,
                            amount = amount,
                            transaction_date = selectedDate,
                            note = textNote.text
                        )
                        Log.d("EditExpenseTransaction", "Updated Transaction: $updatedTransaction")

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
                    Text("Sửa khoản chi", color = Color.White, fontWeight = FontWeight.Bold)
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
                            .width(150.dp), // Giới hạn chiều ngang tối đa của AlertDialog
                        horizontalArrangement = Arrangement.SpaceBetween // Căn chỉnh 2 nút về 2 phía
                    ) {
                        // Nút Bỏ qua - nằm bên trái
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text(
                                "Bỏ qua",
                                color = Color(0xff3a84fc),
                                fontFamily = montserrat,
                                textAlign = TextAlign.Start
                            )
                        }

                        // Nút Xoá - nằm bên phải
                        TextButton(onClick = {
                            // Gọi API xóa giao dịch
                            delViewModel.deleteTransaction(
                                transactionId = transactionId,
                                onSuccess = { message ->
                                    successMessage = "Xóa giao dịch thành công!"
                                    showPopup = true
                                    showDeleteDialog = false
                                    navController.popBackStack() // Trở lại màn hình Calendar
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
                                color = colorPrimary,
                                fontFamily = montserrat,
                            )
                        }
                    }
                }
            )
        }
    }
}




