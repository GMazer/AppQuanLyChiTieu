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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.Color as ComposeColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen() {
    val values = listOf(10, 20, 30, 25, 40, 35, 45, 50, 60, 55, 70, 65) // Dữ liệu cho các tháng
    val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    val canvasHeight = 300f
    val barWidth = 40f
    val spaceBetweenBars = 40f

    var selectedMonthYear by remember { mutableStateOf("01/2024") }

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
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxHeight()
            ) {
                // Spacer giữa TopAppBar và biểu đồ
                Spacer(modifier = Modifier.height(32.dp))

                BarChartWithLine(values = values, months = months) // Hiển thị biểu đồ cột với các tháng
            }
        }
    }
}

@Composable
fun BarChartWithLine(values: List<Int>, months: List<String>) {
    val canvasHeight = 300f
    val barWidth = 40f
    val spaceBetweenBars = 40f  // Khoảng cách giữa các cột

    // Tính chiều rộng của canvas dựa trên số lượng cột và khoảng cách giữa các cột
    val chartWidth = (barWidth + spaceBetweenBars) * values.size - spaceBetweenBars + 50f

    Box(
        modifier = Modifier
            .fillMaxWidth()  // Lấp đầy chiều rộng màn hình
            .height(350.dp)  // Chiều cao của biểu đồ
            .wrapContentSize(Alignment.Center)  // Căn giữa canvas
    ) {
        Canvas(modifier = Modifier
            .width(chartWidth.dp)  // Thiết lập chiều rộng biểu đồ theo chiều rộng tính toán
            .height(350.dp) // Chiều cao của canvas
            .padding(start = 28.dp) // Padding vào trục Y, không ảnh hưởng đến các đường ngang
        ) {
            val maxValue = values.maxOrNull() ?: 0
            val scaleFactor = canvasHeight / maxValue

            // Vẽ nền
            drawRect(
                color = Color.White,
                size = size,
                topLeft = Offset(0f, 0f)
            )

            // Vẽ trục Y
            drawLine(
                color = Color.Black,
                start = Offset(0f, 0f),  // Điểm bắt đầu của trục Y
                end = Offset(0f, canvasHeight),
                strokeWidth = 2f
            )

            // Vẽ các cột màu cam
            values.forEachIndexed { index, value ->
                val left = (barWidth + spaceBetweenBars) * index.toFloat() + 50f  // Thêm padding vào trục Y
                val top = canvasHeight - value * scaleFactor
                val right = left + barWidth
                val bottom = canvasHeight // Đảm bảo đáy cột nằm ở trục X

                drawRoundRect(
                    color = ComposeColor(0xFFFF5722), // Màu cam
                    topLeft = Offset(left, top),
                    size = androidx.compose.ui.geometry.Size(barWidth, bottom - top),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius.Zero
                )
            }

            // Vẽ đường trục X
            drawLine(
                color = Color.Black,
                start = Offset(0f, canvasHeight),
                end = Offset(chartWidth, canvasHeight),
                strokeWidth = 2f
            )

            // Tạo các điểm để vẽ đường uốn lượn
            val points = mutableListOf<Offset>()
            values.forEachIndexed { index, value ->
                val x = (barWidth + spaceBetweenBars) * index.toFloat() + barWidth / 2 + 50f  // Thêm padding vào trục Y
                val y = canvasHeight - value * scaleFactor
                points.add(Offset(x, y))
            }

            // Vẽ đường uốn lượn (parabol) giữa các điểm
            drawPath(
                path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(points.first().x, points.first().y)  // Bắt đầu từ điểm đầu tiên

                    for (i in 1 until points.size) {
                        val p0 = points[i - 1]
                        val p1 = points[i]
                        val middleX = (p0.x + p1.x) / 2f
                        val middleY = (p0.y + p1.y) / 2f

                        // Vẽ Bezier curve (parabol) giữa các điểm
                        cubicTo(
                            p0.x + (p1.x - p0.x) / 3f, p0.y,  // Điểm kiểm soát 1
                            p1.x - (p1.x - p0.x) / 3f, p1.y,  // Điểm kiểm soát 2
                            p1.x, p1.y                        // Điểm kết thúc
                        )
                    }
                },
                color = ComposeColor(0xFF2196F3), // Màu xanh dương cho đường nối
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
            )

            // Vẽ các đường ngang song song với trục X
            val stepSize = maxValue / 5f
            for (i in 0..5) {
                val yPos = canvasHeight - (i * stepSize * scaleFactor)

                // Vẽ đường ngang từ trục Y tới hết chiều rộng
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, yPos),
                    end = Offset(chartWidth, yPos),  // Đảm bảo đường ngang kéo dài đến hết biểu đồ
                    strokeWidth = 1f
                )

                // Vẽ giá trị trục Y bên trái
                drawContext.canvas.nativeCanvas.drawText(
                    "${(i * stepSize).toInt()}",
                    -10f,  // Vị trí căn trái cho giá trị
                    yPos,
                    android.graphics.Paint().apply {
                        textSize = 30f
                        color = android.graphics.Color.BLACK
                        textAlign = android.graphics.Paint.Align.RIGHT
                    }
                )
            }

            // Vẽ tên các tháng trên trục X
            months.forEachIndexed { index, month ->
                val xPos = (barWidth + spaceBetweenBars) * index.toFloat() + barWidth / 2 + 50f  // Thêm padding vào trục Y
                drawContext.canvas.nativeCanvas.drawText(
                    month,
                    xPos,
                    canvasHeight + 30f,
                    android.graphics.Paint().apply {
                        textSize = 30f
                        color = android.graphics.Color.BLACK
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
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
