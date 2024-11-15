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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose.R
import com.example.jetpackcompose.components.CategoriesGrid
import com.example.jetpackcompose.components.DrawBottomLine
import com.example.jetpackcompose.components.NoteTextField
import com.example.jetpackcompose.components.NumberTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeContent () {

    var tabIndex by remember { mutableStateOf(1) }
    var textNote by remember { mutableStateOf(TextFieldValue()) }
    var amountValue by remember { mutableStateOf(TextFieldValue()) }
    var selectedDate by remember { mutableStateOf("Chưa chọn ngày") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) } // Lưu danh mục được chọn

    val buttonColor = Color(0xFFF35E17)

    var transactionType by remember { mutableStateOf(TransactionType.INCOME) }

    LaunchedEffect(tabIndex) {
        transactionType = if (tabIndex == 0) TransactionType.EXPENSE else TransactionType.INCOME
        Log.i("ExpenseContent", "Loại giao dịch hiện tại: $transactionType")
    }

    val categories = listOf(
        Category(
            "Lương",
            { painterResource(R.drawable.baseline_monetization_on_24) },
            Color(0xFFfb791d)
        ),
        Category(
            "Thưởng",
            { painterResource(R.drawable.baseline_card_giftcard_24) },
            Color(0xFF37c166)
        ),
        Category(
            "Cướp",
            { painterResource(R.drawable.baseline_person_off_24) },
            Color(0xFFf95aa9)
        ),
        Category(
            "Khác",
            { painterResource(R.drawable.baseline_more_horiz_24) },
            Color(0xFFfba74a)
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
            Spacer(Modifier.height(8.dp))

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

            DrawBottomLine(8.dp)
            Spacer(Modifier.height(8.dp))

            // Ghi chu
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Ghi chú ",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    fontFamily = com.example.jetpackcompose.components.monsterrat
                )
                Spacer(Modifier.width(8.dp))
                NoteTextField(textState = textNote, onValueChange = { newValue ->
                    textNote = newValue
                })
            }
            DrawBottomLine(8.dp)
            // Nhap vao so tien
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Tiền thu ",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    fontFamily = com.example.jetpackcompose.components.monsterrat
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
            DrawBottomLine(24.dp)

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
                        val categoryName = selectedCategory?.name ?: "Chưa chọn danh mục"

                        val transaction = Transaction(
                            date = selectedDate,
                            note = textNote.text,
                            amount = amount,
                            category = categoryName,
                            type = transactionType // Thêm loại giao dịch
                        )

                        TransactionViewModel().addTransaction(transaction)

                        // Xóa dữ liệu nhập sau khi lưu
                        textNote = TextFieldValue("")
                        amountValue = TextFieldValue("")

                        // Ghi log thông tin giao dịch
                        Log.i("ExpenseContent", "Transaction: $transaction")
                    },

                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text("Nhập khoản thu", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewIncomeContent() {
    IncomeContent()
}
