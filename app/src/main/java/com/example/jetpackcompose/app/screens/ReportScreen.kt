package com.example.jetpackcompose.app.screens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import com.example.jetpackcompose.app.features.apiService.ReportAPI.GetReportExpenseViewModel
import com.example.jetpackcompose.app.features.apiService.ReportAPI.GetReportIncomeViewModel
import com.example.jetpackcompose.components.CategoryIconWithName
import com.example.jetpackcompose.components.DonutChartIncome
import com.example.jetpackcompose.components.DonutChartWithProgress
import com.example.jetpackcompose.components.MessagePopup
import com.example.jetpackcompose.components.MonthPickerButton
import com.example.jetpackcompose.components.ReportTable
import com.example.jetpackcompose.components.montserrat
import com.example.jetpackcompose.ui.theme.primaryColor
import com.example.jetpackcompose.ui.theme.textColor
import com.example.jetpackcompose.ui.theme.topBarColor
import java.util.Calendar

data class ReportDataExpense(
    var name: String,
    var amount: Long
)

data class ReportDataIncome(
    var name: String,
    var amount: Long
)

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen() {

    val reportExpenseViewModel: GetReportExpenseViewModel =
        GetReportExpenseViewModel(LocalContext.current)
    val reportIncomeViewModel: GetReportIncomeViewModel =
        GetReportIncomeViewModel(LocalContext.current)
    var showPopup by remember { mutableStateOf(false) }

    var successMessage by remember { mutableStateOf("") }
    val errorMessage by remember { mutableStateOf("") }

    var percentSpent by remember { mutableStateOf(listOf<Float>()) }
    var percentIncome by remember { mutableStateOf(listOf<Float>()) }
    var percentLimit by remember { mutableStateOf(listOf<Int>()) }
    var expense by remember { mutableStateOf(listOf<String>()) }
    var income by remember { mutableStateOf(listOf<String>()) }
    var colorExpense by remember { mutableStateOf(listOf<Color>()) }
    var colorIncome by remember { mutableStateOf(listOf<Color>()) }
    var totalIncome by remember { mutableLongStateOf(0) }
    var totalExpense by remember { mutableLongStateOf(0) }
    var netAmount by remember { mutableLongStateOf(0) }
    var listReportExpense by remember { mutableStateOf<List<ReportDataExpense>>(emptyList()) }
    var listReportIncome by remember { mutableStateOf<List<ReportDataIncome>>(emptyList()) }

    val currentMonthYear = remember {
        val calendar = Calendar.getInstance()
        val month = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
        val year = calendar.get(Calendar.YEAR)
        "$month/$year"
    }

    var selectedMonthYear by remember { mutableStateOf(currentMonthYear) }
    var selectedTabIndex by remember { mutableStateOf(0) }

    MessagePopup(
        showPopup = showPopup,
        successMessage = successMessage,
        errorMessage = errorMessage,
        onDismiss = { showPopup = false } // Đóng popup khi nhấn ngoài
    )

    LaunchedEffect(selectedMonthYear) {

        successMessage = "Đang tải dữ liệu..."
        showPopup = true
        val monthYear = selectedMonthYear.substring(0, 7)
        Log.d("MainActivity", "Selected month year: $monthYear")
        val (month, year) = monthYear.split("/").map { it.toInt() }.let { it[0] to it[1] }

        reportExpenseViewModel.getExpenseReport(
            month = month,
            year = year,
            onSuccess = { reportExpense ->
                Log.d("MainActivity", "Report data: $selectedMonthYear")

                percentLimit = reportExpense.categoryExpenseReports.map { it.percentLimit.toInt() }
                percentSpent =
                    reportExpense.categoryExpenseReports.map { (it.percentSpent / 100).toFloat() }
                expense = reportExpense.categoryExpenseReports.map { it.categoryName }
                totalIncome = reportExpense.totalIncome
                totalExpense = reportExpense.totalExpense
                netAmount = reportExpense.netAmount
                listReportExpense = reportExpense.categoryExpenseReports.map {
                    ReportDataExpense(
                        it.categoryName,
                        it.spentAmount
                    )
                }

                colorExpense = listOf(
                    Color(0xFFB40300),
                    Color(0xFF911294),
                    Color(0xFF0C326E),
                    Color(0xFF126AB6),
                    Color(0xFF0D96DA),
                    Color(0xFF4DB218),
                    Color(0xFFD5CC00),
                    Color(0xFFEE9305),
                    Color(0xFFD94E0F),
                )
            },
            onError = { error ->
                Log.e("MainActivity", "Error: $error")
            }
        )
        reportIncomeViewModel.getIncomeReport(
            month = month,
            year = year,
            onSuccess = { reportIncome ->
                Log.d("MainActivity", "Report data: $selectedMonthYear")
                percentIncome =
                    reportIncome.categoryIncomeReports.map { (it.percentIncome / 100).toFloat() }
                income = reportIncome.categoryIncomeReports.map { it.categoryName }
                listReportIncome = reportIncome.categoryIncomeReports.map {
                    ReportDataIncome(
                        it.categoryName,
                        it.categoryIncome
                    )
                }
                colorIncome = listOf(
                    Color(0xFFfb791d),
                    Color(0xFF37c166),
                    Color(0xFFf95aa9),
                    Color(0xFFfba74a),
                )
            },
            onError = { error ->
                Log.e("MainActivity", "Error: $error")
            }
        )
    }
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .height(50.dp)
                                .padding(start = 16.dp, end = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Báo cáo",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = montserrat,
                                    fontSize = 16.sp,
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    },
                    navigationIcon = {},
                    actions = {},
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = topBarColor
                    ),
                    modifier = Modifier
                        .height(50.dp)
                )
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp
                )
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
        ) {
            item {
                // Phần header
                Column(
                    modifier = Modifier
                        .background(color = Color.White)
                ) {
                    // Spacer giữa TopAppBar và biểu đồ
                    Spacer(modifier = Modifier.height(8.dp))

                    MonthPickerButton(onDateSelected = { month ->
                        selectedMonthYear = month
                    })

                    Spacer(modifier = Modifier.height(16.dp))

                }
            }

//            item {
//                Spacer(modifier = Modifier.height(24.dp))
//            }

            item {
                ReportTable(totalIncome, totalExpense, netAmount)
            }

//            item {
//                Spacer(modifier = Modifier.height(24.dp))
//            }
            item {
                // Tabs
                val tabs = listOf("Chi tiêu", "Thu nhập")
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier
                        .background(color = Color.White)
                        .fillMaxWidth(),
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = primaryColor
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 16.dp), // Đảm bảo tab sử dụng toàn bộ chiều rộng
                            text = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center // Căn giữa nội dung
                                ) {
                                    Text(
                                        title,
                                        fontFamily = montserrat,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = if (selectedTabIndex == index) primaryColor else textColor,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        )
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                when (selectedTabIndex) {
                    0 -> {
                        if (percentLimit.isNotEmpty() && percentSpent.isNotEmpty() && expense.isNotEmpty()) {
                            DonutChartWithProgress(
                                percentLimit,
                                colorExpense,
                                expense,
                                percentSpent
                            )
                        } else {
                            Text(
                                text = "Đang tải dữ liệu.....",
                                textAlign = TextAlign.Center,
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    }

                    1 -> {
                        if (percentIncome.isNotEmpty() && expense.isNotEmpty()) {
                            DonutChartIncome(colorIncome, income, percentIncome)
                        } else {
                            Text(
                                text = "Đang tải dữ liệu.....",
                                textAlign = TextAlign.Center,
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )
            }

            if (selectedTabIndex == 0) {
                for (item in listReportExpense) {
                    if (item.name != "Tiết kiệm") {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = Color.White)
                                    .height(50.dp)
                                    .padding(horizontal = 16.dp)
                            ) {
                                CategoryIconWithName(item.name, "", item.amount, "expense")
                            }
                        }
                        item {
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                            )
                        }
                    }
                }
            }
            else {
                for(item in listReportIncome) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color.White)
                                .height(50.dp)
                                .padding(horizontal = 16.dp)
                        ) {
                            CategoryIconWithName(item.name, "", item.amount, "income")
                        }
                    }
                    item {
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                        )
                    }
                }
            }
        }
    }
}




@Preview
@Composable
fun PreviewReportScreen() {
    MaterialTheme {
        ReportScreen()
    }
}
