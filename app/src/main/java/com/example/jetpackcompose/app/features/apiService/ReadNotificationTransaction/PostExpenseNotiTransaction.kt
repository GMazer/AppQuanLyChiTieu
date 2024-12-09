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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
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
import com.example.jetpackcompose.app.features.inputFeatures.Category
import com.example.jetpackcompose.app.features.inputFeatures.DatePickerButton
import com.example.jetpackcompose.app.features.inputFeatures.Transaction
import com.example.jetpackcompose.app.features.inputFeatures.montserrat
import com.example.jetpackcompose.components.CategoriesGrid
import com.example.jetpackcompose.components.DrawBottomLine
import com.example.jetpackcompose.components.NoteTextField
import com.example.jetpackcompose.components.NumberTextField
import com.example.jetpackcompose.ui.theme.colorPrimary
import com.example.jetpackcompose.ui.theme.textColor

@Composable
fun PostExpenseNotiTransaction(
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

                // Nút Xoá
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
                        val validDate = date.split(" ")[0] // Tách theo khoảng trắng và lấy phần ngày "yyyy-MM-dd"
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
                                errorMessage = ""  // Reset error message
                                successMessage = "Nhập khoản chi thành công!"
                                showPopup = true  // Hiển thị popup thông báo thành công
                                transactionStorage.deleteTransactionByIndex(index)
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
                    colors = ButtonDefaults.buttonColors(contentColor = colorPrimary)
                ) {
                    Text("Sửa khoản chi", color = Color.White, fontFamily = montserrat, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

