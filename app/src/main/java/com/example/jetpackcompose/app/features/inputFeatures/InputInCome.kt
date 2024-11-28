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
import com.example.jetpackcompose.components.CategoriesGrid
import com.example.jetpackcompose.components.DrawBottomLine
import com.example.jetpackcompose.components.NoteTextField
import com.example.jetpackcompose.components.NumberTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeContent (viewModel: PostTransactionViewModel = PostTransactionViewModel(LocalContext.current)) {


    var tabIndex by remember { mutableStateOf(1) }
    var textNote by remember { mutableStateOf(TextFieldValue()) }
    var amountValue by remember { mutableStateOf(TextFieldValue()) }
    var selectedDate by remember { mutableStateOf("Chưa chọn ngày") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) } // Lưu danh mục được chọn
    var selectedIdCategory by remember { mutableStateOf(0) }

    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    val buttonColor = Color(0xFFF35E17)

    val categories = listOf(
        Category(
            6,
            "Lương",
            { painterResource(R.drawable.salary) },
            Color(0xFFfb791d),
            percentage = 0.75f // 75%
        ),
        Category(
            7,
            "Thưởng",
            { painterResource(R.drawable.baseline_card_giftcard_24) },
            Color(0xFF37c166),
            percentage = 0.65f // 90%
        ),
        Category(
            8,
            "Thu nhập phụ",
            { painterResource(R.drawable.secondary) },
            Color(0xFFf95aa9),
            percentage = 0.30f // 30%
        ),
        Category(
            9,
            "Khác",
            { painterResource(R.drawable.baseline_more_horiz_24) },
            Color(0xFFfba74a),
            percentage = 0.50f // 50%
        )
    )


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
                    fontWeight = FontWeight.Bold,
                    fontFamily = com.example.jetpackcompose.components.monsterrat
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
                    "Tiền thu ",
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
                    fontWeight = FontWeight.Normal,
                )
            }
            DrawBottomLine(16.dp)

            // Danh mục chi tiêu
            Text(
                "Danh mục",
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold,
                fontFamily = com.example.jetpackcompose.components.monsterrat
            )
            Spacer(Modifier.height(24.dp))

            // Gọi danh mục chi tiêu và truyền callback
            CategoriesGrid(
                categories = categories,
                buttonColor = buttonColor,
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category
                    selectedIdCategory = category.id
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

                        viewModel.postTransaction(
                            transaction,
                            onSuccess = {
                                successMessage = it
                            },
                            onError = {
                                errorMessage = it
                            }
                        )

//                        // Xóa dữ liệu nhập sau khi lưu
//                        textNote = TextFieldValue("")
//                        amountValue = TextFieldValue("")

                        // Ghi log thông tin giao dịch
                        Log.i("IncomeContent", "Transaction: $transaction")
                    },
                    modifier = Modifier.width(248.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text("Nhập khoản thu", color = Color.White, fontWeight = FontWeight.Bold)
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
fun PreviewIncomeContent() {
//    IncomeContent()
}
