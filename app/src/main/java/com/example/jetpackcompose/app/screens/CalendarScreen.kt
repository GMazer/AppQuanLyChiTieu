package com.example.jetpackcompose.app.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackcompose.app.features.apiService.TransactionAPI.GetTransactionViewModel
import com.example.jetpackcompose.components.CustomCalendar
import com.example.jetpackcompose.components.DayIndex
import com.example.jetpackcompose.components.MonthPickerButton
import com.example.jetpackcompose.components.monsterrat
import com.example.jetpackcompose.ui.theme.TextColor
import com.example.jetpackcompose.ui.theme.colorPrimary
import com.example.jetpackcompose.ui.theme.topBarColor
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Calendar
import java.util.Locale

data class DailyTransaction(
    val date: String,
    val amountIncome: Long,
    val amountExpense: Long
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen() {
    val viewModel: GetTransactionViewModel = GetTransactionViewModel(LocalContext.current)

    // Lấy tháng và năm hiện tại làm giá trị mặc định
    val currentMonthYear = remember {
        val calendar = Calendar.getInstance()
        val month = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
        val year = calendar.get(Calendar.YEAR)
        "$month/$year"
    }

    val currencyFormatter = remember {
        // Lấy DecimalFormatSymbols mặc định cho Việt Nam
        val symbols = DecimalFormatSymbols(Locale("vi", "VN"))

        // Thay đổi dấu phân cách thập phân và phân cách hàng nghìn
        symbols.decimalSeparator = '.'
        symbols.groupingSeparator = ','

        // Tạo một DecimalFormat mới sử dụng các biểu tượng đã thay đổi
        val format = DecimalFormat("#,###", symbols)

        format
    }

    var selectedMonthYear by remember { mutableStateOf(currentMonthYear) }
    var transactionList by remember { mutableStateOf(listOf<DailyTransaction>()) }
    var errorMessage by remember { mutableStateOf("") }

    // Gọi API để lấy danh sách giao dịch
    LaunchedEffect(selectedMonthYear) {
        // Tách tháng và năm từ selectedMonthYear
        val (month, year) = selectedMonthYear.split("/").map { it.toInt() }

        viewModel.getTransactions(
            month = month,
            year = year,
            onSuccess = { transactions ->
                // Chuyển đổi danh sách giao dịch từ API thành danh sách `DailyTransaction`
                transactionList = transactions.map { transaction ->
                    DailyTransaction(
                        date = transaction.date,
                        amountIncome = transaction.amountIncome,
                        amountExpense = transaction.amountExpense
                    )
                }
            },
            onError = { error ->
                errorMessage = error
            }
        )
    }

    val totalExpense = transactionList.sumOf { it.amountExpense }
    val totalIncome = transactionList.sumOf { it.amountIncome }
    val totalBalance = totalIncome - totalExpense
    val formattedBalance = if (totalBalance >= 0) {
        "+${currencyFormatter.format(totalBalance)}₫"
    } else {
        "${currencyFormatter.format(totalBalance)}₫"
    }

    MaterialTheme(
        typography = Typography(
            bodyLarge = TextStyle(fontWeight = FontWeight.Normal),
            titleLarge = TextStyle(fontWeight = FontWeight.Bold)
        )
    ) {
        Scaffold(
            topBar = {
                Column(modifier = Modifier
                    .padding(0.dp)
                    .fillMaxWidth()) {
                    TopAppBar(
                        title = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 16.dp, end = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Lịch",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                    ),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                        },
                        colors = TopAppBarDefaults.smallTopAppBarColors(
                            containerColor = topBarColor
                        ),
                        modifier = Modifier
                            .height(50.dp)
                    )
                    // Divider ngay dưới TopAppBar
                    Divider(
                        color = Color.LightGray,
                        thickness = 1.dp
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    MonthPickerButton(onDateSelected = { month ->
                        selectedMonthYear = month
                    })
                }

                Spacer(modifier = Modifier.height(16.dp))

                CustomCalendar(selectedMonthYear = selectedMonthYear, transactionList)

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Thu nhập",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                ),
                                color = TextColor,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Text(
                                text = "${currencyFormatter.format(totalIncome)}₫",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                ),
                                color = Color(0xff37c8ec),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Chi tiêu",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                ),
                                color = TextColor,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Text(
                                text = "${currencyFormatter.format(totalExpense)}₫",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                ),
                                color = colorPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Tổng",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                ),
                                color = TextColor,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Text(
                                text = formattedBalance,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                ),
                                color = if (totalBalance >= 0) Color(0xff37c8ec) else colorPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                    }
                }
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontFamily = monsterrat,
                        style = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                } else {

                    Spacer(modifier = Modifier.height(8.dp))

                    Divider(
                        color = Color.LightGray,
                        thickness = 2.dp
                    )

                    LazyColumn {
                        item {
                            DayIndex(transactionList)
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewCalendarScreen() {
    CalendarScreen()
}
