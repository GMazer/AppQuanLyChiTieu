package com.example.jetpackcompose.app.features.inputFeatures

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import com.example.jetpackcompose.components.CategoriesGrid
import com.example.jetpackcompose.components.DrawBottomLine
import com.example.jetpackcompose.components.NoteTextField
import com.example.jetpackcompose.components.NumberTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutComeContent(
    postViewModel: PostTransactionViewModel = PostTransactionViewModel(LocalContext.current),
    getRemainLimit: GetLimitTransactionViewModel = GetLimitTransactionViewModel(LocalContext.current)) {


    var categoryLimits by remember {
        mutableStateOf(listOf(
            RemainLimit.CategoryLimit(categoryId = 1, percentLimit = 0, remainingPercent = 1.00),
            RemainLimit.CategoryLimit(categoryId = 2, percentLimit = 0, remainingPercent = 1.00),
            RemainLimit.CategoryLimit(categoryId = 3, percentLimit = 0, remainingPercent = 1.00),
            RemainLimit.CategoryLimit(categoryId = 4, percentLimit = 0, remainingPercent = 1.00),
            RemainLimit.CategoryLimit(categoryId = 5, percentLimit = 0, remainingPercent = 1.00)
        ))
    }

    var textNote by remember { mutableStateOf(TextFieldValue()) }
    var amountValue by remember { mutableStateOf(TextFieldValue()) }
    var selectedDate by remember { mutableStateOf("Chưa chọn ngày") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    val buttonColor = Color(0xFFF35E17)

    var categories by remember { mutableStateOf(
        listOf(
            Category(1, "Thiết yếu", { painterResource(R.drawable.essentials) }, Color(0xFFfb791d), 1.00f),
            Category(2, "Giải trí", { painterResource(R.drawable.entertainment) }, Color(0xFF37c166), 1.00f),
            Category(3, "Đầu tư", { painterResource(R.drawable.invest) }, Color(0xFF283eaa), 1.00f),
            Category(4, "Phát sinh", { painterResource(R.drawable.hedgefund) }, Color(0xFFf95aa9), 1.00f)
        )
    ) }

    // Hàm cập nhật percentage cho các danh mục
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
        categories = updatedCategories // Cập nhật lại danh sách categories
    }

    // Lần đầu tiên gọi API, cập nhật categoryLimits và percentage
    LaunchedEffect(key1 = true) {
        getRemainLimit.getLimitTransaction(
            onError = {},
            onSuccess = {
                categoryLimits = it
                updatePercentage(categoryLimits)  // Gọi hàm cập nhật percentage sau khi nhận dữ liệu
            }
        )
    }

    Log.i("Categories", "$categories")

    Scaffold(
        containerColor = Color.White,

        ) { innerPadding ->

        // Đưa nội dung vào Column

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .background(Color.White)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Ngày ",
                    color = Color.DarkGray,
                    fontFamily = monsterrat,
                    fontWeight = FontWeight.Bold
                )
                //Gọi Nút Chọn ngày
                DatePickerButton(onDateSelected = { date ->
                    selectedDate = date
                })
            }

            DrawBottomLine(16.dp)

            // Ghi chu
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Ghi chú ",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    fontFamily = com.example.jetpackcompose.components.monsterrat,
                    modifier = Modifier
                        .width(68.dp)
                )
                Spacer(Modifier.width(8.dp))
                NoteTextField(textState = textNote, onValueChange = { newValue ->
                    textNote = newValue
                })
            }

            DrawBottomLine(16.dp)
            // Nhap vao so tien
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Tiền chi ",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    fontFamily = com.example.jetpackcompose.components.monsterrat,
                    modifier = Modifier
                        .width(68.dp)
                )
                Spacer(Modifier.width(8.dp))
                NumberTextField(
                    amountState = amountValue.text,
                    onValueChange = { newValue ->
                        amountValue = TextFieldValue(newValue)
                    }
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "₫",
                    color = Color.DarkGray,
                    fontFamily = monsterrat,
                    fontWeight = FontWeight.Normal,
                )
            }
            DrawBottomLine(16.dp)
            // Danh mục chi tiêu
            Text(
                "Danh mục",
                color = Color.DarkGray,
                fontFamily = monsterrat,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(24.dp))

            // Gọi danh mục chi tiêu và truyền callback

            CategoriesGrid(
                categories = categories,
                buttonColor = buttonColor,
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category
                }
            )

            Spacer(Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
            ) {
                Button(
                    onClick = {
                        val amount = amountValue.text.toLongOrNull() ?: 0L

                        val transaction = Transaction(
                            transaction_date = selectedDate.substring(0, 11),
                            note = textNote.text,
                            amount = amount,
                            category_id = selectedCategory?.id ?: 0
                        )


                        postViewModel.postTransaction(
                            transaction,
                            onSuccess = {
                                errorMessage = it
                            },
                            onError = {
                                successMessage = it
                            }
                        )

                        getRemainLimit.getLimitTransaction(
                            onError = {},
                            onSuccess = {
                                categoryLimits = it
                                updatePercentage(categoryLimits)  // Gọi hàm cập nhật percentage sau khi nhận dữ liệu
                            }
                        )


                        // Ghi log thông tin giao dịch
                        Log.i("ExpenseContent", "Transaction: $transaction")
                    },
                    modifier = Modifier.width(248.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text(
                        "Nhập khoản chi",
                        color = Color.White,
                        fontFamily = monsterrat,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
            ){
                Text(text = errorMessage, color = Color.Red, style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)

                Text(text = successMessage, color = Color.Green, style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
            }
        }
    }
}


@Preview
@Composable
fun PreviewOutComeContent() {
//    OutComeContent()
}