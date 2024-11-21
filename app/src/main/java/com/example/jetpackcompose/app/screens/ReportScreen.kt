package com.example.jetpackcompose.app.screens

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
import androidx.compose.material3.TopAppBar
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.motionEventSpy
import com.example.jetpackcompose.components.YearPickerDialog
import com.example.jetpackcompose.components.YearPickerButton
import com.example.jetpackcompose.components.BarChartWithLine
import com.example.jetpackcompose.components.ReportMonth
import com.example.jetpackcompose.ui.theme.colorPrimary
import com.example.jetpackcompose.ui.theme.highGray
import java.util.Calendar
import androidx.compose.ui.graphics.Color as ComposeColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen() {
    val values = listOf(10, 20, 30, 25, 40, 35, 45, 50, 60, 55, 70, 65) // Dữ liệu cho các tháng
    val indexs = listOf(12, 22, 33, 24, 45, 36, 47, 58, 69, 50, 71, 62) // Mốc cho mỗi tháng
    val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    val currentYear = remember {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        "$year"
    }

    var selectedYear by remember { mutableStateOf(currentYear) }

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
                Column {
                    TopAppBar(
                        title = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Báo cáo",
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
                    .background(color = Color.LightGray)
            ) {
                item {
                    // Phần header
                    Column(
                        modifier = Modifier
                            .background(color = Color.White)
                    ) {
                        // Spacer giữa TopAppBar và biểu đồ
                        Spacer(modifier = Modifier.height(16.dp))

                        YearPickerButton(onYearSelected = { year ->
                            selectedYear = year
                        })

                        Spacer(modifier = Modifier.height(24.dp))

                        BarChartWithLine(values = values, index = indexs, months = months) // Hiển thị biểu đồ cột với các tháng
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Thêm các phần tử khác vào LazyColumn
                item {
                    ReportMonth("Total", 1000)
                    Spacer(modifier = Modifier.height(16.dp))
                }


                for(month in months) {
                    item {
                        ReportMonth(month, 100)
                        Divider(
                            color = highGray,
                            thickness = 1.dp
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
