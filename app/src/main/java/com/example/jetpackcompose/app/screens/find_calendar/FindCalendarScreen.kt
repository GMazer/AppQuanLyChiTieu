package com.example.jetpackcompose.app.screens.find_calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jetpackcompose.app.features.apiService.TransactionAPI.GetTransactionViewModel
import com.example.jetpackcompose.components.montserrat
import com.example.jetpackcompose.ui.theme.topBarColor
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackcompose.ui.theme.topBarColor
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.apiService.ReportAPI.GetReportViewModel
import com.example.jetpackcompose.app.features.apiService.TransactionAPI.FindTransactionViewModel
import com.example.jetpackcompose.app.network.FindTransactionResponse
import com.example.jetpackcompose.app.network.TransactionResponse
import com.example.jetpackcompose.components.CategoryIconWithName
import com.example.jetpackcompose.components.DayIndex
import com.example.jetpackcompose.components.DonutChartWithProgress
import com.example.jetpackcompose.components.MessagePopup
import com.example.jetpackcompose.components.MonthPickerButton
import com.example.jetpackcompose.components.NoteTextField
import com.example.jetpackcompose.components.ReportTable
import com.example.jetpackcompose.components.montserrat
import com.example.jetpackcompose.ui.theme.colorPrimary
import com.example.jetpackcompose.ui.theme.lightGray
import com.example.jetpackcompose.ui.theme.textColor
import java.util.Calendar

fun mapFindTransactionToTransactionResponse(
    findTransactions: List<FindTransactionResponse>
): Map<String, List<TransactionResponse.TransactionDetail>> {
    return findTransactions.groupBy { response ->
        // Chuyển transactionDate (List<Int>) thành định dạng chuỗi "yyyy-MM-dd"
        response.transactionDate.joinToString("-")
    }.mapValues { (_, transactions) ->
        transactions.map { findTransaction ->
            TransactionResponse.TransactionDetail(
                categoryName = findTransaction.categoryName,
                amount = findTransaction.amount.toLong(),
                transactionDate = findTransaction.transactionDate,
                note = findTransaction.note,
                type = findTransaction.type,
                transaction_id = findTransaction.transaction_id.toInt()
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindCalendarScreen(navController: NavController) {
    val viewModel: FindTransactionViewModel = FindTransactionViewModel(LocalContext.current)

    var transactions by remember { mutableStateOf<List<FindTransactionResponse>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }

    var textNote by remember { mutableStateOf(TextFieldValue()) }

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
                Column(
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth()
                ) {
                    TopAppBar(
                        title = {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                            ) {
                                IconButton(
                                    onClick = {
                                        navController.popBackStack(
                                            "findtransaction",
                                            inclusive = true
                                        )
                                    },
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .offset(x = (-8).dp)
                                        .offset(y = (1).dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.outline_arrow_back_ios_24),
                                        contentDescription = "Back",
                                        tint = textColor,
                                    )
                                }

                                // Text "Lịch" căn giữa
                                androidx.compose.material.Text(
                                    text = "Lịch",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = montserrat,
                                        fontSize = 16.sp,
                                    ),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .offset(x = (-8).dp)
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

            // Sử dụng LazyColumn để có thể cuộn được
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                item {
                    // Phần header
                    Column(
                        modifier = Modifier
                            .background(color = Color.White)
                    ) {

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        ) {
                            OutlinedTextField(
                                value = textNote, // Truyền vào TextFieldValue
                                onValueChange = { newValue ->
                                    textNote = newValue // Cập nhật TextFieldValue
                                    viewModel.findTransactions(
                                        note = newValue.text, // Lấy chuỗi từ TextFieldValue
                                        onSuccess = {
                                            transactions = it
                                            Log.d("transactions", transactions.toString())
                                        },
                                        onError = {
                                            errorMessage = it
                                        }
                                    )
                                },
                                label = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_find_in_page_24), // Icon của bạn
                                            contentDescription = "Tìm kiếm",
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = "Tìm kiếm")
                                    }
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = colorPrimary, // Màu viền khi đang focus
                                    unfocusedBorderColor = Color.Gray, // Màu viền khi không focus
                                    disabledBorderColor = Color.LightGray, // Màu viền khi disabled
                                    errorBorderColor = Color.Red, // Màu viền khi có lỗi
                                    focusedLabelColor = colorPrimary, // Thay đổi màu label khi focus
                                    unfocusedLabelColor = Color.Gray // Thay đổi màu label khi không focus
                                )
                            )
                        }
                    }
                }

                item {
                    if (transactions.isNotEmpty()) {
                        val mappedTransactions = mapFindTransactionToTransactionResponse(transactions)
                        Spacer(modifier = Modifier.height(16.dp))
                        DayIndex(
                            dateTransactionList = mappedTransactions,
                            selectedDate = "",
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}