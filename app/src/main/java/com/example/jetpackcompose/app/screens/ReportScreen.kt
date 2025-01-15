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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcompose.app.features.apiService.ReportAPI.GetReportExpenseViewModel
import com.example.jetpackcompose.app.features.apiService.ReportAPI.GetReportIncomeViewModel
import com.example.jetpackcompose.app.features.apiService.TransactionAPI.GetBudgetCategoryViewModel
import com.example.jetpackcompose.components.CategoryProgress
import com.example.jetpackcompose.components.DonutChartIncome
import com.example.jetpackcompose.components.DonutChartWithProgress
import com.example.jetpackcompose.components.MessagePopup
import com.example.jetpackcompose.components.MonthPickerButton
import com.example.jetpackcompose.components.ReportTable
import com.example.jetpackcompose.components.montserrat
import com.example.jetpackcompose.ui.theme.primaryColor
import com.example.jetpackcompose.ui.theme.textColor
import com.example.jetpackcompose.ui.theme.topBarColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar

data class ReportDataExpense(
    var name: String,
    var amount: Long,
    var process: Float
)

data class ReportDataIncome(
    var name: String,
    var amount: Long,
    var process: Float
)

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    navController: NavController,
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    onPageSelected: (Int) -> Unit
) {

    val getBudgetCategoryViewModel : GetBudgetCategoryViewModel =
        GetBudgetCategoryViewModel(LocalContext.current)

    var houseValue by remember { mutableStateOf(TextFieldValue()) }
    var foodValue by remember { mutableStateOf(TextFieldValue()) }
    var shoppingValue by remember { mutableStateOf(TextFieldValue()) }
    var movingValue by remember { mutableStateOf(TextFieldValue()) }
    var cosmeticValue by remember { mutableStateOf(TextFieldValue()) }
    var exchangingValue by remember { mutableStateOf(TextFieldValue()) }
    var medicalValue by remember { mutableStateOf(TextFieldValue()) }
    var educatingValue by remember { mutableStateOf(TextFieldValue()) }
    var saveValue by remember { mutableStateOf(TextFieldValue()) }

    val reportExpenseViewModel: GetReportExpenseViewModel =
        GetReportExpenseViewModel(LocalContext.current)
    val reportIncomeViewModel: GetReportIncomeViewModel =
        GetReportIncomeViewModel(LocalContext.current)

    var showPopup by remember { mutableStateOf(false) }
    var recommendBudget by remember { mutableStateOf(false) }

    var successMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    var isDataLoaded1 by remember { mutableStateOf(false) }
    var isDataLoaded2 by remember { mutableStateOf(false) }
    var isBudgetLoaded by remember { mutableStateOf(false) }

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
    var isLoading by rememberSaveable { mutableStateOf(false) }

    val currentMonthYear = rememberSaveable {
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

    Log.d("ReportScreen", "selectedMonthYear: $selectedMonthYear")
    LaunchedEffect(selectedMonthYear) {

        isDataLoaded1 = false
        isDataLoaded2 = false

        getBudgetCategoryViewModel.getBudgetTransaction(
            onSuccess = {
                houseValue = TextFieldValue(it[0].limitExpense.toString())
                foodValue = TextFieldValue(it[1].limitExpense.toString())
                shoppingValue = TextFieldValue(it[2].limitExpense.toString())
                movingValue = TextFieldValue(it[3].limitExpense.toString())
                cosmeticValue = TextFieldValue(it[4].limitExpense.toString())
                exchangingValue = TextFieldValue(it[5].limitExpense.toString())
                medicalValue = TextFieldValue(it[6].limitExpense.toString())
                educatingValue = TextFieldValue(it[7].limitExpense.toString())
                saveValue = TextFieldValue(it[8].limitExpense.toString())

                // Kiểm tra nếu tất cả các giá trị đều bằng 0
                val allZero = listOf(
                    houseValue.text, foodValue.text, shoppingValue.text, movingValue.text,
                    cosmeticValue.text, exchangingValue.text, medicalValue.text,
                    educatingValue.text, saveValue.text
                ).all { it == "0" } // Kiểm tra nếu tất cả đều là "0"

                if (allZero) {
                    recommendBudget = true
                } else {
                    isBudgetLoaded = true
                }

            },
            onError = {

            }
        )

        errorMessage = ""
        if (!isLoading && selectedMonthYear != currentMonthYear && !recommendBudget) {
            isLoading = true
            successMessage = "Đang tải dữ liệu..."
            showPopup = true

            val monthYear = selectedMonthYear.substring(0, 7)
            val (month, year) = monthYear.split("/").map { it.toInt() }

            reportExpenseViewModel.getExpenseReport(
                month = month,
                year = year,
                onSuccess = { reportExpense ->
                    percentLimit =
                        reportExpense.categoryExpenseReports.map { it.percentLimit.toInt() }
                    percentSpent =
                        reportExpense.categoryExpenseReports.map { (it.percentSpent / 100).toFloat() }
                    expense = reportExpense.categoryExpenseReports.map { it.categoryName }
                    totalIncome = reportExpense.totalIncome
                    totalExpense = reportExpense.totalExpense
                    netAmount = reportExpense.netAmount
                    listReportExpense = reportExpense.categoryExpenseReports.map {
                        ReportDataExpense(it.categoryName, it.spentAmount, (it.percentSpent / 100).toFloat())
                    }
                    colorExpense = listOf(
                        Color(0xFFB40300), Color(0xFF911294), Color(0xFF0C326E),
                        Color(0xFF126AB6), Color(0xFF0D96DA), Color(0xFF4DB218),
                        Color(0xFFD5CC00), Color(0xFFEE9305), Color(0xFFD94E0F)
                    )
                    isLoading = false
                    isDataLoaded1 = true
                },
                onError = { error ->
                    isLoading = false
                    errorMessage = "Có lỗi xảy ra khi tải dữ liệu."
                    showPopup = true
                }
            )

            reportIncomeViewModel.getIncomeReport(
                month = month,
                year = year,
                onSuccess = { reportIncome ->
                    percentIncome =
                        reportIncome.categoryIncomeReports.map { (it.percentIncome / 100).toFloat() }
                    income = reportIncome.categoryIncomeReports.map { it.categoryName }
                    listReportIncome = reportIncome.categoryIncomeReports.map {
                        ReportDataIncome(it.categoryName, it.categoryIncome, (it.percentIncome / 100).toFloat())
                    }
                    colorIncome = listOf(
                        Color(0xFFfb791d), Color(0xFF37c166),
                        Color(0xFFf95aa9), Color(0xFF0000FF)
                    )
                    isLoading = false
                    isDataLoaded2 = true
                },
                onError = { error ->
                    isLoading = false
                    errorMessage = "Có lỗi xảy ra khi tải dữ liệu."
                    showPopup = true
                }
            )
        } else {
            showPopup = false
            isDataLoaded1 = false
            isDataLoaded2 = false
        }
    }
    LaunchedEffect(isDataLoaded1, isDataLoaded2) {
        if (isDataLoaded1 && isDataLoaded2) {
            showPopup = false
            isDataLoaded1 = false
            isDataLoaded2 = false
        }
    }

    if (recommendBudget) {
        AlertDialog(
            onDismissRequest = { recommendBudget = false },
            title = {
                Text(
                    "Chưa phân bổ ngân sách!",
                    fontFamily = montserrat,
                    color = Color(0xff222222),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Text(
                    "Bạn chưa phân bổ ngân sách để thống kê báo cáo, nhập phân bổ ngân sách ngay?",
                    fontFamily = montserrat,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    recommendBudget = false
                    onPageSelected(4)  // Cập nhật selectedPage thành 4 khi bấm nút
                    coroutineScope.launch {
                        pagerState.scrollToPage(4) // Điều hướng đến BudgetScreen (trang 4)
                    }
                }) {
                    Text(
                        "OK",
                        fontFamily = montserrat,
                        color = primaryColor
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    recommendBudget = false
                    // Không cần lưu lại trạng thái "bỏ qua" vì muốn dialog hiển thị lại khi mở lại app
                }) {
                    Text(
                        "Bỏ qua",
                        color = Color.Gray,
                        fontFamily = montserrat
                    )
                }
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

        // Phần header
        Column(
            modifier = Modifier
                .background(color = Color.White)
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Spacer giữa TopAppBar và biểu đồ
            Spacer(modifier = Modifier
                .height(16.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                MonthPickerButton(onDateSelected = { month ->
                    selectedMonthYear = month
                    Log.d("CalendarScreen", "selectedMonthYear: $selectedMonthYear")
                })
            }

            Spacer(modifier = Modifier
                .height(16.dp)
            )

            ReportTable(totalIncome, totalExpense, netAmount)

            // Tabs
            val tabs = listOf("Chi tiêu", "Thu nhập")
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .background(color = Color.White)
                    .fillMaxWidth(),
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier
                            .width(15.dp)
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .height(4.dp)
                            .clip(RoundedCornerShape(50))
                            .background(primaryColor),
                        color = primaryColor,
                        height = 2.dp,
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        text = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    when (selectedTabIndex) {
                        0 -> {
                            if (percentLimit.isNotEmpty() && percentSpent.isNotEmpty() && expense.isNotEmpty() && isBudgetLoaded) {
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
                                    fontSize = 16.sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        1 -> {
                            if (percentIncome.isNotEmpty() && expense.isNotEmpty() && isBudgetLoaded) {
                                DonutChartIncome(colorIncome, income, percentIncome)
                            } else {
                                Text(
                                    text = "Đang tải dữ liệu.....",
                                    textAlign = TextAlign.Center,
                                    color = Color.Gray,
                                    fontSize = 16.sp,
                                    modifier = Modifier.fillMaxWidth()
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

                if (selectedTabIndex == 0 && isBudgetLoaded) {
                    for (item in listReportExpense) {
                        if (item.name != "Tiết kiệm" && item.amount != 0L) {
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
                                    CategoryProgress(item.name, "", item.amount, "expense", item.process)
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
                } else if (selectedTabIndex == 1 && isBudgetLoaded) {
                    for (item in listReportIncome) {
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
                                Log.d("ReportScreen", "item: $item")
                                CategoryProgress(item.name, "", item.amount, "income", item.process)
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
}


@Preview
@Composable
fun PreviewReportScreen() {
    MaterialTheme {
//        ReportScreen()
    }
}
