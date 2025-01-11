package com.example.jetpackcompose.app.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackcompose.app.features.apiService.TransactionAPI.GetBudgetCategoryViewModel
import com.example.jetpackcompose.app.features.apiService.TransactionAPI.PutLimitTransactionViewModel
import com.example.jetpackcompose.components.BudgetTextField
import com.example.jetpackcompose.components.MyButtonComponent
import com.example.jetpackcompose.components.montserrat
import com.example.jetpackcompose.ui.theme.primaryColor
import com.example.jetpackcompose.ui.theme.textColor
import com.example.jetpackcompose.ui.theme.topBarColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen() {
    val putViewModel = PutLimitTransactionViewModel(LocalContext.current)
    var houseValue by remember { mutableStateOf(TextFieldValue()) }
    var foodValue by remember { mutableStateOf(TextFieldValue()) }
    var shoppingValue by remember { mutableStateOf(TextFieldValue()) }
    var movingValue by remember { mutableStateOf(TextFieldValue()) }
    var cosmeticValue by remember { mutableStateOf(TextFieldValue()) }
    var exchangingValue by remember { mutableStateOf(TextFieldValue()) }
    var medicalValue by remember { mutableStateOf(TextFieldValue()) }
    var educatingValue by remember { mutableStateOf(TextFieldValue()) }
    var saveValue by remember { mutableStateOf(TextFieldValue()) }

    val viewModel: GetBudgetCategoryViewModel = GetBudgetCategoryViewModel(LocalContext.current)
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.getBudgetTransaction(
            onError = { isLoading = false },
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
                isLoading = false
            }
        )
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        title = {
                            Box(
                                modifier = Modifier
                                    .background(color = Color(0xfff5f5f5))
                                    .height(50.dp)
                                    .fillMaxSize()
                                    .padding(start = 16.dp, end = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Ngân sách",
                                    fontFamily = montserrat,
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
                        modifier = Modifier.height(50.dp)
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.LightGray
                    )
                }
            }
        ) { paddingValues ->
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .background(Color(0xfff5f5f5))
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Thiết lập ngân sách hàng tháng: ",
                        fontFamily = montserrat,
                        color = primaryColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                    listOf(
                        "Nhà ở" to houseValue,
                        "Chi phí ăn uống" to foodValue,
                        "Mua sắm quần áo" to shoppingValue,
                        "Đi lại" to movingValue,
                        "Chăm sóc sắc đẹp" to cosmeticValue,
                        "Giao lưu" to exchangingValue,
                        "Y tế" to medicalValue,
                        "Học tập" to educatingValue,
                        "Khoản tiết kiệm" to saveValue
                    ).forEach { (label, value) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = label,
                                fontFamily = montserrat,
                                color = textColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(2f),
                                lineHeight = 12.sp
                            )
                            Box(modifier = Modifier.weight(7f)) {
                                BudgetTextField(
                                    amountState = value.text,
                                    onValueChange = { newValue ->
                                        when (label) {
                                            "Nhà ở" -> houseValue = TextFieldValue(newValue)
                                            "Chi phí ăn uống" -> foodValue = TextFieldValue(newValue)
                                            "Mua sắm quần áo" -> shoppingValue = TextFieldValue(newValue)
                                            "Đi lại" -> movingValue = TextFieldValue(newValue)
                                            "Chăm sóc sắc đẹp" -> cosmeticValue = TextFieldValue(newValue)
                                            "Giao lưu" -> exchangingValue = TextFieldValue(newValue)
                                            "Y tế" -> medicalValue = TextFieldValue(newValue)
                                            "Học tập" -> educatingValue = TextFieldValue(newValue)
                                            "Khoản tiết kiệm" -> saveValue = TextFieldValue(newValue)
                                        }
                                    },
                                    colorPercent = Color.Black
                                )
                            }
                            Text(
                                text = "₫",
                                fontFamily = montserrat,
                                color = textColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    ) {
                        MyButtonComponent(
                            value = "Lưu ngân sách",
                            isLoading = false,
                            onClick = {
                            val categoryLimits = listOf(
                                LimitTransaction.CategoryLimit(1, houseValue.text.toLongOrNull() ?: 0L),
                                LimitTransaction.CategoryLimit(2, foodValue.text.toLongOrNull() ?: 0L),
                                LimitTransaction.CategoryLimit(3, shoppingValue.text.toLongOrNull() ?: 0L),
                                LimitTransaction.CategoryLimit(4, movingValue.text.toLongOrNull() ?: 0L),
                                LimitTransaction.CategoryLimit(5, cosmeticValue.text.toLongOrNull() ?: 0L),
                                LimitTransaction.CategoryLimit(6, exchangingValue.text.toLongOrNull() ?: 0L),
                                LimitTransaction.CategoryLimit(7, medicalValue.text.toLongOrNull() ?: 0L),
                                LimitTransaction.CategoryLimit(8, educatingValue.text.toLongOrNull() ?: 0L),
                                LimitTransaction.CategoryLimit(9, saveValue.text.toLongOrNull() ?: 0L)
                            )

                            val limitTransaction: LimitTransaction = LimitTransaction(categoryLimits)
                            // Gọi ViewModel hoặc logic khác để lưu dữ liệu
                            putViewModel.addLimitTransaction(
                                data = limitTransaction.limits,
                                onError = { },
                                onSuccess = { }
                            )
                        })
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun BudgetScreenPreview() {
    BudgetScreen()
}

