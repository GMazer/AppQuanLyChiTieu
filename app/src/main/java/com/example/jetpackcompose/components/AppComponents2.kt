package com.example.jetpackcompose.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.jetpackcompose.app.features.apiService.TransactionAPI.GetBudgetCategoryViewModel
import com.example.jetpackcompose.app.screens.LimitTransaction
import com.example.jetpackcompose.ui.theme.componentShapes
import com.example.jetpackcompose.ui.theme.primaryColor
import com.example.jetpackcompose.ui.theme.textColor
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Calendar
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DonutChartWithProgress(
    values: List<Int>,
    colors: List<Color>,
    labels: List<String>,
    progresses: List<Float> // Tỉ lệ hoàn thành (0.0 đến 1.0) cho từng phần
) {
    val totalValue = values.sum()
    val proportions = values.map { it.toFloat() / totalValue }
    val angles = proportions.map { it * 360f }


    var selectedSegment by remember { mutableStateOf(0) } // Lưu trữ mảnh được chọn

    var progressWidth by remember { mutableStateOf(0f) }
    var progressRadius by remember { mutableStateOf(0f) }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(200.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        // Xử lý chạm
                        val clickedAngle = calculateAngleFromCenter(offset, size)
                        val segmentIndex = findSegmentIndex(clickedAngle, angles)
                        selectedSegment = segmentIndex
                    }
                }
        ) {
            val maxStrokeWidth = 150f
            val radius = size.minDimension / 2
            var startAngle = -90f

            // Vẽ các mảnh donut chart
            angles.forEachIndexed { index, sweepAngle ->

                // Kiểm tra xem mảnh hiện tại có phải là mảnh được chọn không
                val isSelected = selectedSegment == index
                val segmentRadius =
                    if (isSelected) radius + 20f else radius // Tăng bán kính nếu được chọn
                val segmentStrokeWidth =
                    if (isSelected) maxStrokeWidth + 10f else maxStrokeWidth // Tăng độ rộng nét nếu được chọn

                // Vẽ phần chính của donut chart
                drawArc(
                    color = colors[index].copy(alpha = 0.2f),
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = segmentStrokeWidth, cap = StrokeCap.Butt),
                    topLeft = Offset(center.x - segmentRadius, center.y - segmentRadius),
                    size = Size(segmentRadius * 2, segmentRadius * 2) // Điều chỉnh kích thước
                )

                // Tương tự, điều chỉnh các phần khác như progress và inner circle cho mảnh được chọn
                if (progresses[index] >= 1) {
                    progressWidth = maxStrokeWidth // Tỷ lệ chiều rộng mực nước
                } else {
                    progressWidth = maxStrokeWidth * progresses[index] // Tỷ lệ chiều rộng mực nước
                }
                // Vẽ "mực nước" (progress)
                progressRadius =
                    if (isSelected) segmentRadius - (maxStrokeWidth - progressWidth) / 2 else radius - (maxStrokeWidth - progressWidth) / 2
                drawArc(
                    color = colors[index].copy(alpha = 0.8f),
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(
                        width = progressWidth,
                        cap = StrokeCap.Butt
                    ),
                    topLeft = Offset(center.x - progressRadius, center.y - progressRadius),
                    size = Size(progressRadius * 2, progressRadius * 2)
                )

                // Chiều rộng vòng trong
                val innerWidth = maxStrokeWidth * 0.2f // Tỷ lệ chiều rộng mực nước

                // Bán kính vòng trong
                val innerRadius =
                    if (isSelected) segmentRadius - (maxStrokeWidth - innerWidth) / 2 - innerWidth - 0.25f else radius - (maxStrokeWidth - innerWidth) / 2 - innerWidth

                // Vòng trong
                drawArc(
                    color = colors[index].copy(alpha = 0.6f), // Làm nhạt màu
                    startAngle = startAngle,
                    sweepAngle = sweepAngle, // Giữ nguyên góc
                    useCenter = false,
                    style = Stroke(
                        width = innerWidth, // Chiều rộng mực nước theo tỷ lệ
                        cap = StrokeCap.Butt
                    ),
                    topLeft = Offset(center.x - innerRadius, center.y - innerRadius),
                    size = Size(
                        innerRadius * 2,
                        innerRadius * 2
                    ) // Sử dụng bán kính trong để vẽ mực nước
                )

                if (progresses[index] >= 1) {
                    val outerWidth = maxStrokeWidth * 0.2f // Tỷ lệ chiều rộng mực nước
                    val outerRadius =
                        if (isSelected) segmentRadius + (maxStrokeWidth - outerWidth) / 2 + 2.5f else radius + (maxStrokeWidth - outerWidth) / 2

                    // Vòng ngoài
                    drawArc(
                        color = colors[index],
                        startAngle = startAngle,
                        sweepAngle = sweepAngle, // Giữ nguyên góc
                        useCenter = false,
                        style = Stroke(
                            width = outerWidth, // Chiều rộng mực nước theo tỷ lệ
                            cap = StrokeCap.Butt
                        ),
                        topLeft = Offset(center.x - outerRadius, center.y - outerRadius),
                        size = Size(
                            outerRadius * 2,
                            outerRadius * 2
                        ) // Sử dụng bán kính trong để vẽ mực nước
                    )
                }

                startAngle += sweepAngle
            }
            startAngle = -90f
            angles.forEach { sweepAngle ->
                val angleInRadians = Math.toRadians(startAngle.toDouble())
                val extendedOffset =
                    maxStrokeWidth * 0.2f  // Tăng chiều dài đường ngăn thêm giá trị này

                // Điều chỉnh bán kính để tăng chiều dài đường ngăn
                val lineStart = Offset(
                    x = center.x + (radius - maxStrokeWidth / 2 - extendedOffset) * cos(
                        angleInRadians
                    ).toFloat(),
                    y = center.y + (radius - maxStrokeWidth / 2 - extendedOffset) * sin(
                        angleInRadians
                    ).toFloat()
                )
                val lineEnd = Offset(
                    x = center.x + (radius + maxStrokeWidth / 2 + extendedOffset) * cos(
                        angleInRadians
                    ).toFloat(),
                    y = center.y + (radius + maxStrokeWidth / 2 + extendedOffset) * sin(
                        angleInRadians
                    ).toFloat()
                )

                // Vẽ đường ngăn
                drawLine(
                    color = Color.White,
                    start = lineStart,
                    end = lineEnd,
                    strokeWidth = 10f // Độ rộng của đường ngăn
                )

                startAngle += sweepAngle
            }
        }

        // Hiển thị thông tin khi click vào mảnh
        if (selectedSegment != -1 && selectedSegment != 8) {
            Column {
                Text(
                    text = "${labels[selectedSegment]}",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold,
                    color = colors[selectedSegment],
                    fontSize = 12.sp
                )
                Text(
                    text = "Phân bổ: ${values[selectedSegment]}%",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Normal,
                    color = colors[selectedSegment],
                    fontSize = 12.sp
                )
                Text(
                    text = "Chi tiêu: ${(progresses[selectedSegment] * 100).toInt()}%",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Normal,
                    color = colors[selectedSegment],
                    fontSize = 12.sp
                )
            }
        } else {
            Column {
                Text(
                    text = "${labels[selectedSegment]}",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold,
                    color = colors[selectedSegment],
                    fontSize = 12.sp
                )
                Text(
                    text = "Phân bổ: ${values[selectedSegment]}%",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Normal,
                    color = colors[selectedSegment],
                    fontSize = 12.sp
                )
                Text(
                    text = "Còn lại: ${(progresses[selectedSegment] * 100).toInt()}%",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Normal,
                    color = colors[selectedSegment],
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun DonutChartIncome(
    colors: List<Color>,
    labels: List<String>,
    progresses: List<Float> // Tỉ lệ hoàn thành (0.0 đến 1.0) cho từng phần
) {
    var selectedSegment by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(200.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        // Xử lý chạm vào segment
                        val clickedAngle = calculateAngleFromCenter(offset, size)
                        val segmentIndex =
                            findSegmentIndex(clickedAngle, progresses.map { it * 360f })
                        selectedSegment = segmentIndex
                    }
                }
        ) {
            val maxStrokeWidth = 150f
            val radius = size.minDimension / 2
            var startAngle = -90f

            // Vẽ các mảnh donut chart
            progresses.forEachIndexed { index, progress ->
                val sweepAngle = progress * 360f

                // Kiểm tra nếu mảnh hiện tại được chọn
                val isSelected = selectedSegment == index
                val segmentRadius =
                    if (isSelected) radius + 20f else radius // Tăng bán kính nếu được chọn
                val segmentStrokeWidth =
                    if (isSelected) maxStrokeWidth + 10f else maxStrokeWidth // Tăng độ rộng nét nếu được chọn

                // Vẽ phần chính của donut chart
                drawArc(
                    color = colors[index],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = segmentStrokeWidth, cap = StrokeCap.Butt),
                    topLeft = Offset(center.x - segmentRadius, center.y - segmentRadius),
                    size = Size(segmentRadius * 2, segmentRadius * 2)
                )

                // Chiều rộng vòng trong
                val innerWidth = maxStrokeWidth * 0.2f // Tỷ lệ chiều rộng mực nước

                // Bán kính vòng trong
                val innerRadius =
                    if (isSelected) segmentRadius - (maxStrokeWidth - innerWidth) / 2 - innerWidth - 0.25f else radius - (maxStrokeWidth - innerWidth) / 2 - innerWidth

                // Vòng trong
                drawArc(
                    color = colors[index].copy(alpha = 0.6f), // Làm nhạt màu
                    startAngle = startAngle,
                    sweepAngle = sweepAngle, // Giữ nguyên góc
                    useCenter = false,
                    style = Stroke(
                        width = innerWidth, // Chiều rộng mực nước theo tỷ lệ
                        cap = StrokeCap.Butt
                    ),
                    topLeft = Offset(center.x - innerRadius, center.y - innerRadius),
                    size = Size(
                        innerRadius * 2,
                        innerRadius * 2
                    ) // Sử dụng bán kính trong để vẽ mực nước
                )

                startAngle += sweepAngle
            }

            // Vẽ đường ngăn giữa các mảnh
            startAngle = -90f
            progresses.forEach { progress ->
                val sweepAngle = progress * 360f
                val angleInRadians = Math.toRadians(startAngle.toDouble())
                val extendedOffset = maxStrokeWidth * 0.2f  // Tăng chiều dài đường ngăn thêm giá trị này

                // Điều chỉnh bán kính để tăng chiều dài đường ngăn
                val lineStart = Offset(
                    x = center.x + (radius - maxStrokeWidth / 2 - extendedOffset) * cos(
                        angleInRadians
                    ).toFloat(),
                    y = center.y + (radius - maxStrokeWidth / 2 - extendedOffset) * sin(
                        angleInRadians
                    ).toFloat()
                )
                val lineEnd = Offset(
                    x = center.x + (radius + maxStrokeWidth / 2 + extendedOffset) * cos(
                        angleInRadians
                    ).toFloat(),
                    y = center.y + (radius + maxStrokeWidth / 2 + extendedOffset) * sin(
                        angleInRadians
                    ).toFloat()
                )

                // Vẽ đường ngăn
                drawLine(
                    color = Color.White,
                    start = lineStart,
                    end = lineEnd,
                    strokeWidth = 10f // Độ rộng của đường ngăn
                )

                startAngle += sweepAngle
            }
        }

        for (it in progresses) {
            if (it == 0f) {
                selectedSegment += 1
            } else {
                break
            }
        }
        // Hiển thị thông tin khi click vào mảnh
        if (selectedSegment != -1 && selectedSegment < progresses.size) {
            Column {
                Text(
                    text = "${labels[selectedSegment]}",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold,
                    color = colors[selectedSegment],
                    fontSize = 12.sp
                )
                Text(
                    text = "Thu nhập: ${(progresses[selectedSegment] * 100).toInt()}%",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Normal,
                    color = colors[selectedSegment],
                    fontSize = 12.sp
                )
            }
        } else {
            Column {
                Text(
                    text = "Không có khoản thu",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}


// Tính góc từ tâm dựa trên tọa độ
private fun calculateAngleFromCenter(tapOffset: Offset, canvasSize: IntSize): Float {
    val centerX = canvasSize.width / 2
    val centerY = canvasSize.height / 2
    val dx = tapOffset.x - centerX
    val dy = tapOffset.y - centerY
    val angle = atan2(dy, dx).toDegrees() + 90f // Điều chỉnh để bắt đầu từ góc trên
    return if (angle < 0) angle + 360f else angle
}

// Tìm mảnh chứa góc
private fun findSegmentIndex(angle: Float, angles: List<Float>): Int {
    var cumulativeAngle = 0f
    for ((index, sweepAngle) in angles.withIndex()) {
        cumulativeAngle += sweepAngle
        if (angle <= cumulativeAngle) {
            return index
        }
    }
    return -1
}

// Chuyển đổi radian sang độ
private fun Float.toDegrees(): Float = this * 180f / Math.PI.toFloat()

@Composable
fun MonthPickerDialog(
    currentYear: Int,
    currentMonth: Int,
    onDismiss: () -> Unit,
    onMonthYearSelected: (Int, Int) -> Unit
) {
    val selectedYear = remember { mutableStateOf(currentYear) }
    val selectedMonth = remember { mutableStateOf(currentMonth) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header: Chọn năm với các mũi tên
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { selectedYear.value-- }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous Year",
                            tint = textColor
                        )
                    }
                    Text(
                        text = selectedYear.value.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = montserrat,
                        color = textColor

                    )
                    IconButton(onClick = { selectedYear.value++ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next Year"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Grid layout để hiển thị các tháng
                val months = listOf(
                    "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
                    "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
                )
                val columns = 3

                for (row in 0 until months.size / columns) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (col in 0 until columns) {
                            val monthIndex = row * columns + col
                            val isSelected = monthIndex == selectedMonth.value

                            Text(
                                text = months[monthIndex],
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = montserrat,
                                color = if (isSelected) Color.White else textColor,
                                modifier = Modifier
                                    .background(
                                        color = if (isSelected) primaryColor else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .padding(8.dp)
                                    .clickable {
                                        selectedMonth.value = monthIndex
                                    },
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nút Cancel và Ok
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text(text = "Cancel", fontFamily = montserrat, color = textColor)
                    }
                    TextButton(onClick = {
                        onMonthYearSelected(selectedMonth.value, selectedYear.value)
                        onDismiss()
                    }) {
                        Text(text = "Ok", fontFamily = montserrat, color = primaryColor)
                    }
                }
            }
        }
    }
}

@Composable
fun YearPickerDialog(
    initialYear: Int,
    onDismiss: () -> Unit,
    onYearSelected: (Int) -> Unit
) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val years = (1900..currentYear + 50).toList() // Danh sách các năm, có thể tùy chỉnh
    var selectedYear by remember { mutableStateOf(initialYear) }
    val listState = rememberLazyListState()

    // Tự động cuộn tới năm đã chọn và đặt nó ở giữa
    LaunchedEffect(Unit) {
        val index = years.indexOf(initialYear)
        if (index != -1) {
            // Cuộn để đưa năm được chọn vào giữa danh sách
            listState.scrollToItem(index - 2.coerceAtLeast(0))
        }
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Chọn năm",
                    fontSize = 20.sp,
                    fontFamily = montserrat,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Danh sách năm cuộn
                Box(
                    modifier = Modifier
                        .height(200.dp) // Chiều cao của danh sách cuộn
                        .fillMaxWidth()
                ) {
                    LazyColumn(
                        state = listState
                    ) {
                        items(years) { year ->
                            Text(
                                text = year.toString(),
                                fontSize = 18.sp,
                                fontFamily = montserrat,
                                fontWeight = if (year == selectedYear) FontWeight.Bold else FontWeight.Normal,
                                color = if (year == selectedYear) primaryColor else textColor,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable { selectedYear = year },
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nút Cancel và OK
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text(text = "Cancel", fontFamily = montserrat, color = textColor)
                    }
                    TextButton(onClick = {
                        onYearSelected(selectedYear)
                        onDismiss()
                    }) {
                        Text(text = "Ok", fontFamily = montserrat, color = primaryColor)
                    }
                }
            }
        }
    }
}

@Composable
fun PopupSetBudgetDialog(
    onDismiss: () -> Unit,
    onConfirm: (LimitTransaction) -> Unit // Hàm callback nhận LimitTransaction
) {

    LocalSoftwareKeyboardController.current
    val viewModel: GetBudgetCategoryViewModel = GetBudgetCategoryViewModel(LocalContext.current)
    var isLoading by remember { mutableStateOf(false) } // Trạng thái loading

    var houseValue by remember { mutableStateOf(TextFieldValue()) }
    var foodValue by remember { mutableStateOf(TextFieldValue()) }
    var shoppingValue by remember { mutableStateOf(TextFieldValue()) }
    var movingValue by remember { mutableStateOf(TextFieldValue()) }
    var cosmeticValue by remember { mutableStateOf(TextFieldValue()) }
    var exchangingValue by remember { mutableStateOf(TextFieldValue()) }
    var medicalValue by remember { mutableStateOf(TextFieldValue()) }
    var educatingValue by remember { mutableStateOf(TextFieldValue()) }
    var saveValue by remember { mutableStateOf(TextFieldValue()) }

    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var showPopup by remember { mutableStateOf(false) }

    MessagePopup(
        showPopup = showPopup,
        successMessage = successMessage,
        errorMessage = errorMessage,
        onDismiss = { showPopup = false } // Đóng popup khi nhấn ngoài
    )

    viewModel.getBudgetTransaction(
        onError = {
            isLoading = false
        },
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
            isLoading = true
        }
    )

    if (isLoading) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    val maxHeight = this.maxHeight
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .heightIn(max = maxHeight),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        errorMessage.let {
                            Text(
                                text = it,
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        Text(
                            text = "Phân bổ ngân sách",
                            fontFamily = montserrat,
                            color = primaryColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        listOf(
                            "Chi phí nhà ở" to houseValue,
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
                                    .padding(bottom = 8.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = label,
                                    fontFamily = montserrat,
                                    color = textColor,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.weight(2f)
                                )
                                Box(modifier = Modifier.weight(7f)) {
                                    BudgetTextField(
                                        amountState = value.text,
                                        onValueChange = { newValue ->
                                            when (label) {
                                                "Chi phí nhà ở" -> houseValue =
                                                    TextFieldValue(newValue)

                                                "Chi phí ăn uống" -> foodValue =
                                                    TextFieldValue(newValue)

                                                "Mua sắm quần áo" -> shoppingValue =
                                                    TextFieldValue(newValue)

                                                "Đi lại" -> movingValue = TextFieldValue(newValue)
                                                "Chăm sóc sắc đẹp" -> cosmeticValue =
                                                    TextFieldValue(newValue)

                                                "Giao lưu" -> exchangingValue =
                                                    TextFieldValue(newValue)

                                                "Y tế" -> medicalValue = TextFieldValue(newValue)
                                                "Học tập" -> educatingValue =
                                                    TextFieldValue(newValue)

                                                "Khoản tiết kiệm" -> saveValue =
                                                    TextFieldValue(newValue)
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

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(onClick = { onDismiss() }) {
                                Text(text = "Huỷ bỏ", fontFamily = montserrat, color = textColor)
                            }
                            TextButton(onClick = {
                                val categoryLimits = listOf(
                                    LimitTransaction.CategoryLimit(
                                        1,
                                        houseValue.text.toLongOrNull() ?: 0L
                                    ),
                                    LimitTransaction.CategoryLimit(
                                        2,
                                        foodValue.text.toLongOrNull() ?: 0L
                                    ),
                                    LimitTransaction.CategoryLimit(
                                        3,
                                        shoppingValue.text.toLongOrNull() ?: 0L
                                    ),
                                    LimitTransaction.CategoryLimit(
                                        4,
                                        movingValue.text.toLongOrNull() ?: 0L
                                    ),
                                    LimitTransaction.CategoryLimit(
                                        5,
                                        cosmeticValue.text.toLongOrNull() ?: 0L
                                    ),
                                    LimitTransaction.CategoryLimit(
                                        6,
                                        exchangingValue.text.toLongOrNull() ?: 0L
                                    ),
                                    LimitTransaction.CategoryLimit(
                                        7,
                                        medicalValue.text.toLongOrNull() ?: 0L
                                    ),
                                    LimitTransaction.CategoryLimit(
                                        8,
                                        educatingValue.text.toLongOrNull() ?: 0L
                                    ),
                                    LimitTransaction.CategoryLimit(
                                        9,
                                        saveValue.text.toLongOrNull() ?: 0L
                                    )
                                )
                                onConfirm(LimitTransaction(categoryLimits))
                                onDismiss()
                            }) {
                                Text(text = "OK", fontFamily = montserrat, color = primaryColor)
                            }
                        }
                    }
                }
            }
        }
    } else {
        showPopup = true
        successMessage = "Đang tải dữ liệu..."
    }
}

@Composable
fun BudgetTextField(
    amountState: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colorPercent: Color
) {

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = formatNumber(amountState), // Định dạng số với dấu phẩy
        onValueChange = { newInput ->
            val unformattedInput = newInput.replace(",", "") // Loại bỏ dấu phẩy trước khi xử lý
            if (unformattedInput == "0" && amountState.isEmpty()) {
                // Không làm gì để chặn '0' đầu tiên
            } else {
                val filteredInput = unformattedInput.filter { it.isDigit() }
                onValueChange(filteredInput)
            }
        },
        singleLine = true,
        enabled = enabled,
        modifier = modifier
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .height(30.dp)
            .fillMaxWidth()
            .background(Color(0xFFe7e7e7), componentShapes.small)
            .border(
                1.dp,
                if (isFocused) primaryColor else Color.Transparent,
                componentShapes.small
            )
            .padding(horizontal = 8.dp),
        textStyle = TextStyle(
            textAlign = TextAlign.Start,
            fontSize = 20.sp,
            fontFamily = montserrat,
            fontWeight = FontWeight.Bold,
            color = colorPercent,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                keyboardController?.hide() // Ẩn bàn phím khi nhấn Done
            }
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (amountState.isEmpty()) {
                    Text(
                        text = if (isFocused) "" else "0",
                        color = textColor,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = montserrat,
                        fontSize = 20.sp,
                        style = LocalTextStyle.current
                    )
                }
                innerTextField()
            }
        }
    )
}

@Composable
fun OtherFunction(
    items: List<Pair<String, () -> Unit>>,
    painters: List<Painter>
) {
    // Kiểm tra độ dài của painters và items có khớp không
    require(items.size == painters.size) { "The number of items and painters must match." }

    Column(modifier = Modifier.fillMaxWidth()) {
        items.forEachIndexed { index, item ->
            // Xác định bo góc cho phần tử đầu hoặc cuối
            val cornerShape = when (index) {
                0 -> RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp) // Phần tử đầu tiên
                items.size - 1 -> RoundedCornerShape(
                    bottomStart = 8.dp,
                    bottomEnd = 8.dp
                ) // Phần tử cuối cùng
                else -> RoundedCornerShape(0.dp) // Không bo góc cho các phần tử khác
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.White, shape = cornerShape)
                    .clickable(onClick = item.second),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        painter = painters[index], // Sử dụng painter tương ứng với index
                        contentDescription = "Icon",
                        tint = primaryColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = item.first,
                        fontFamily = montserrat,
                        color = textColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                }
            }

            // Thêm Divider giữa các mục, trừ mục cuối cùng
            if (index < items.size - 1) {
                Divider(
                    color = Color.LightGray,
                    thickness = 0.5.dp
                )
            }
        }
    }
}

@Composable
fun ReportTable(income: Long, expense: Long, net: Long) {

    val currencyFormatter = remember {
        val symbols = DecimalFormatSymbols(Locale("vi", "VN"))
        symbols.decimalSeparator = '.'
        symbols.groupingSeparator = ','
        val format = DecimalFormat("#,###", symbols)
        format
    }

    val formattedIncome = buildAnnotatedString {
        append("+${currencyFormatter.format(income)}")
        withStyle(style = SpanStyle(fontSize = 12.sp)) {
            append("₫")
        }
    }

    val formattedExpense = buildAnnotatedString {
        append("-${currencyFormatter.format(expense)}")
        withStyle(style = SpanStyle(fontSize = 12.sp)) {
            append("₫")
        }
    }

    val formattedBalance = buildAnnotatedString {
        append(
            if (net >= 0) {
                "+${currencyFormatter.format(net)}"
            } else {
                "${currencyFormatter.format(net)}"
            }
        )
        withStyle(style = SpanStyle(fontSize = 14.sp)) {  // Kích thước nhỏ hơn cho ký tự "₫"
            append("₫")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .height(120.dp)
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFFF35E17), Color(0xFFFFA573)),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 0f)
                )

            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween, // Căn giữa, phân bổ đều không gian
                modifier = Modifier.fillMaxWidth()
            ) {
                // Tổng thu nhập
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp), // Chia đều chiều rộng giữa các cột
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Thu nhập",
                        fontFamily = montserrat,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formattedIncome,
                        fontFamily = montserrat,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                // Divider dọc giữa các cột thu nhập và chi tiêu
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp) // Chiều rộng của divider
                        .background(Color.White) // Màu của divider
                )

                // Tổng chi tiêu
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp), // Chia đều chiều rộng giữa các cột
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Chi tiêu",
                        style = TextStyle(
                            fontFamily = montserrat,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                        ),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formattedExpense,
                        style = TextStyle(
                            fontFamily = montserrat,
                            color = Color.White,
//                            shadow = Shadow(
//                                color = Color.White,
//                                offset = Offset(2f, 2f),
//                                blurRadius = 4f
//                            ),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                }
            }

            Divider(
                Modifier
                    .background(Color.White)
                    .fillMaxWidth(0.8f)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,  // Căn giữa theo chiều dọc
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(
                    text = "Số dư",
                    fontFamily = montserrat,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                )
                Text(
                    text = formattedBalance,
                    fontFamily = montserrat,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
            }

        }
    }
}

@Preview
@Composable
fun BudgetTextField() {
    var amountState by remember { mutableStateOf("0") }
    BudgetTextField(
        amountState = amountState,
        onValueChange = { amountState = it },
        modifier = Modifier.fillMaxWidth(),
        enabled = true,
        colorPercent = Color.Black
    )
}

