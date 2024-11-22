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
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.jetpackcompose.components.CustomCalendar
import com.example.jetpackcompose.components.DayIndex
import com.example.jetpackcompose.components.MonthPickerButton
import com.example.jetpackcompose.ui.theme.topBarColor
import java.util.Calendar

data class DailyTransaction(
    val date: String,
    val amountIncome: Long,
    val amountExpense: Long
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen() {

    // Lấy tháng và năm hiện tại làm giá trị mặc định
    val currentMonthYear = remember {
        val calendar = Calendar.getInstance()
        val month = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
        val year = calendar.get(Calendar.YEAR)
        "$month/$year"
    }

    val firstTransaction = DailyTransaction(
        date = "12/2023",
        amountIncome = 100000,
        amountExpense = 50000
    )

    val listTransaction = listOf<DailyTransaction>(
        firstTransaction
    )

    var selectedMonthYear by remember { mutableStateOf(currentMonthYear) }

    val customTypography = Typography(
        bodyLarge = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        bodyMedium = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        bodySmall = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        titleLarge = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        titleMedium = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        titleSmall = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        labelLarge = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        labelMedium = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        labelSmall = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        headlineLarge = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        headlineMedium = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        headlineSmall = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat)
    )

    MaterialTheme(typography = customTypography) {
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

                Row(verticalAlignment = Alignment.CenterVertically)  {
                    MonthPickerButton(onDateSelected = { month ->
                        selectedMonthYear = month
                    })
                }

                Spacer(modifier = Modifier.height(16.dp))
                CustomCalendar(selectedMonthYear = selectedMonthYear)

                LazyColumn {
                    item {
                        DayIndex(listTransaction)
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
