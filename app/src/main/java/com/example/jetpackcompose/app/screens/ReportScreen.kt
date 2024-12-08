package com.example.jetpackcompose.app.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackcompose.ui.theme.topBarColor
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.jetpackcompose.app.features.apiService.ReportAPI.GetReportViewModel
import com.example.jetpackcompose.components.CategoryIconWithName
import com.example.jetpackcompose.components.DonutChartWithProgress
import com.example.jetpackcompose.components.MessagePopup
import com.example.jetpackcompose.components.MonthPickerButton
import com.example.jetpackcompose.components.ReportTable
import com.example.jetpackcompose.components.montserrat
import com.example.jetpackcompose.ui.theme.lightGray
import java.util.Calendar

data class ReportData(
    var name: String,
    var amount: Long
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(viewModel: GetReportViewModel = GetReportViewModel(LocalContext.current)) {

    var showPopup by remember { mutableStateOf(false) }

    var successMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    var percentSpent by remember { mutableStateOf(listOf<Float>()) }
    var percentLimit by remember { mutableStateOf(listOf<Int>()) }
    var labels by remember { mutableStateOf(listOf<String>()) }
    var colors by remember { mutableStateOf(listOf<Color>()) }
    var totalIncome by remember { mutableStateOf<Long>(0) }
    var totalExpense by remember { mutableStateOf<Long>(0) }
    var netAmount by remember { mutableStateOf<Long>(0) }
    var listReport by remember { mutableStateOf<List<ReportData>>(emptyList()) }

    val currentMonthYear = remember {
        val calendar = Calendar.getInstance()
        val month = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
        val year = calendar.get(Calendar.YEAR)
        "$month/$year"
    }

    var selectedMonthYear by remember { mutableStateOf(currentMonthYear) }

    MessagePopup(
        showPopup = showPopup,
        successMessage = successMessage,
        errorMessage = errorMessage,
        onDismiss = { showPopup = false } // Đóng popup khi nhấn ngoài
    )

    // Dùng LaunchedEffect để gọi lại API khi selectedMonthYear thay đổi
    LaunchedEffect(selectedMonthYear) {

        successMessage = "Đang tải dữ liệu..."
        showPopup = true

        // Lấy tháng và năm từ selectedMonthYear
        val (month, year) = selectedMonthYear.split("/").map { it.toInt() }.let { it[0] to it[1] }

        viewModel.getReport(
            month = month,
            year = year,
            onSuccess = { report ->
                successMessage = "Thành công"
                showPopup = true
                Log.d("MainActivity", "Report data: $selectedMonthYear")

                // Lấy giá trị từ report và gán vào các danh sách
                percentLimit = report.categoryReports.map { it.percentLimit.toInt() }
                percentSpent = report.categoryReports.map { (it.percentSpent / 100).toFloat() }
                labels = report.categoryReports.map { it.categoryName }
                totalIncome = report.totalIncome
                totalExpense = report.totalExpense
                netAmount = report.netAmount
                listReport = report.categoryReports.map { ReportData(it.categoryName, it.spentAmount) }

                // Màu sắc cho biểu đồ (có thể mở rộng theo số lượng danh mục)
                colors = listOf(
                    Color(0xFFD5CC00),
                    Color(0xFFEE9305),
                    Color(0xFFD94E0F),
                    Color(0xFFB40300),
                    Color(0xFF911294),
                    Color(0xFF0C326E),
                    Color(0xFF126AB6),
                    Color(0xFF0D96DA),
                    Color(0xFF4DB218)
                )
            },
            onError = { error ->
                Log.e("MainActivity", "Error: $error")
            }
        )
    }

    val customTypography = Typography(
        bodyLarge = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        bodyMedium = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        bodySmall = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        titleLarge = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        titleMedium = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        titleSmall = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        labelLarge = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        labelMedium = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        labelSmall = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        headlineLarge = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        headlineMedium = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        headlineSmall = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat)
    )

    MaterialTheme(typography = customTypography) {
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

            // Sử dụng LazyColumn để có thể cuộn được
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxHeight()
                    .background(Color(0xFFF5F5F5))
            ) {
                item {
                    // Phần header
                    Column(
                        modifier = Modifier
                            .background(color = Color.White)
                    ) {
                        // Spacer giữa TopAppBar và biểu đồ
                        Spacer(modifier = Modifier.height(16.dp))

                        MonthPickerButton(onDateSelected = { month ->
                            selectedMonthYear = month
                        })

                        Spacer(modifier = Modifier.height(16.dp))

                    }
                }

                item{
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item{
                    ReportTable(totalIncome, totalExpense, netAmount)
                }

                item{
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Column(
                        modifier = Modifier
                            .background(color = Color.White)
                            .fillMaxWidth()
                    ){
                        // Hiển thị biểu đồ nếu đã có dữ liệu
                        if (percentLimit.isNotEmpty() && percentSpent.isNotEmpty() && labels.isNotEmpty()) {
                            DonutChartWithProgress(percentLimit, colors, labels, percentSpent)
                        } else {
                            Text(
                                text = "Loading data...",
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    }
                }


                item{
                    Divider(modifier = Modifier.fillMaxWidth().height(2.dp))
                }

                for (item in listReport) {
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
                            Divider(modifier = Modifier.fillMaxWidth().height(2.dp))
                        }
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
