package com.example.jetpackcompose.app.screens.find_calendar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.apiService.TransactionAPI.FindTransactionViewModel
import com.example.jetpackcompose.app.network.FindTransactionResponse
import com.example.jetpackcompose.app.network.TransactionResponse
import com.example.jetpackcompose.components.DayIndex
import com.example.jetpackcompose.components.montserrat
import com.example.jetpackcompose.ui.theme.colorPrimary
import com.example.jetpackcompose.ui.theme.componentShapes
import com.example.jetpackcompose.ui.theme.textColor
import com.example.jetpackcompose.ui.theme.topBarColor

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
    val keyboardController = LocalSoftwareKeyboardController.current

    MaterialTheme {
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
                                    .height(50.dp)
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
                                            painter = painterResource(id = android.R.drawable.ic_search_category_default), // Icon của bạn
                                            contentDescription = "Tìm kiếm",
                                            modifier = Modifier.size(24.dp),
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Tìm kiếm",
                                            fontFamily = montserrat,
                                        )
                                    }
                                },
                                shape = componentShapes.medium,
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = colorPrimary,
                                    unfocusedBorderColor = Color(0xffB0B0B0),
                                    disabledBorderColor = Color.LightGray,
                                    errorBorderColor = Color.Red,
                                    focusedLabelColor = colorPrimary,
                                    unfocusedLabelColor = Color(0xffB0B0B0),
                                ),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        keyboardController?.hide()
                                    }
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


@Composable
@Preview
fun FindCalendarScreenPreview() {
    FindCalendarScreen(navController = NavController(LocalContext.current))
}