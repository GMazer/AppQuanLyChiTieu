package com.example.jetpackcompose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.jetpackcompose.ui.theme.textColor
import com.example.jetpackcompose.ui.theme.colorPrimary
import java.util.Calendar
import androidx.compose.foundation.Canvas
import androidx.compose.material.Divider
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.apiService.TransactionAPI.GetLimitTransactionViewModel
import com.example.jetpackcompose.app.features.inputFeatures.LimitTransaction
import com.example.jetpackcompose.app.features.inputFeatures.montserrat
import com.example.jetpackcompose.ui.theme.componentShapes
import com.example.jetpackcompose.ui.theme.highGray
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
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

    var progressWidth by remember { mutableStateOf(0f) }
    var progressRadius by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val maxStrokeWidth = 150f  // Chiều rộng tối đa của vòng tròn donut
            val radius = size.minDimension / 2

            // Vẽ nền lightGray
//            drawArc(
//                color = Color.LightGray,
//                startAngle = 0f,
//                sweepAngle = 360f,
//                useCenter = false,
//                style = Stroke(width = maxStrokeWidth + 20f, cap = StrokeCap.Butt)
//            )

            var startAngle = -90f

            // Vẽ các phần của donut chart
            angles.forEachIndexed { index, sweepAngle ->
                // Vẽ phần chính của donut chart
                drawArc(
                    color = colors[index].copy(alpha = 0.2f),
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = maxStrokeWidth, cap = StrokeCap.Butt)
                )


                if (progresses[index] >= 1) {
                    progressWidth = maxStrokeWidth // Tỷ lệ chiều rộng mực nước
                } else {
                    progressWidth = maxStrokeWidth * progresses[index] // Tỷ lệ chiều rộng mực nước
                }

                // Tính toán bán kính cho phần "mực nước" để áp vào lề trong
                progressRadius = radius - (maxStrokeWidth - progressWidth) / 2

                // Chiều rộng vòng trong
                val innerWidth = maxStrokeWidth * 0.2f // Tỷ lệ chiều rộng mực nước

                // Bán kính vòng trong
                val innerRadius = radius - (maxStrokeWidth - innerWidth) / 2 - innerWidth

                // Vẽ "mực nước" (progress) với chiều rộng theo tỷ lệ hoàn thành và bán kính được điều chỉnh
                drawArc(
                    color = colors[index].copy(alpha = 0.8f),
                    startAngle = startAngle,
                    sweepAngle = sweepAngle, // Giữ nguyên góc
                    useCenter = false,
                    style = Stroke(
                        width = progressWidth, // Chiều rộng mực nước theo tỷ lệ
                        cap = StrokeCap.Butt
                    ),
                    topLeft = Offset(center.x - progressRadius, center.y - progressRadius),
                    size = Size(progressRadius * 2, progressRadius * 2) // Sử dụng bán kính trong để vẽ mực nước
                )

                // Vòng trong
                drawArc(
                    color = colors[index].copy(alpha = 0.4f), // Làm nhạt màu
                    startAngle = startAngle,
                    sweepAngle = sweepAngle, // Giữ nguyên góc
                    useCenter = false,
                    style = Stroke(
                        width = innerWidth, // Chiều rộng mực nước theo tỷ lệ
                        cap = StrokeCap.Butt
                    ),
                    topLeft = Offset(center.x - innerRadius, center.y - innerRadius),
                    size = Size(innerRadius * 2, innerRadius * 2) // Sử dụng bán kính trong để vẽ mực nước
                )

                if (progresses[index] >= 1) {
                    val outerWidth = maxStrokeWidth * 0.2f // Tỷ lệ chiều rộng mực nước
                    val outerRadius = radius + (maxStrokeWidth - outerWidth) / 2

                    // Vòng trong
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
                        size = Size(outerRadius * 2, outerRadius * 2) // Sử dụng bán kính trong để vẽ mực nước
                    )
                }

                startAngle += sweepAngle
            }

            startAngle = -90f
            angles.forEach { sweepAngle ->
                val angleInRadians = Math.toRadians(startAngle.toDouble())
                val extendedOffset = maxStrokeWidth * 0.2f  // Tăng chiều dài đường ngăn thêm giá trị này

                // Điều chỉnh bán kính để tăng chiều dài đường ngăn
                val lineStart = Offset(
                    x = center.x + (radius - maxStrokeWidth / 2 - extendedOffset) * cos(angleInRadians).toFloat(),
                    y = center.y + (radius - maxStrokeWidth / 2 - extendedOffset) * sin(angleInRadians).toFloat()
                )
                val lineEnd = Offset(
                    x = center.x + (radius + maxStrokeWidth / 2) * cos(angleInRadians).toFloat(),
                    y = center.y + (radius + maxStrokeWidth / 2) * sin(angleInRadians).toFloat()
                )

                // Vẽ đường ngăn
                drawLine(
                    color = Color.White,
                    start = lineStart,
                    end = lineEnd,
                    strokeWidth = 15f // Độ rộng của đường ngăn
                )

                startAngle += sweepAngle
            }
        }
    }
}





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
                            imageVector = Icons.Default.ArrowBack,
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
                            imageVector = Icons.Default.ArrowForward,
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
                                        color = if (isSelected) colorPrimary else Color.Transparent,
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
                        Text(text = "Ok", fontFamily = montserrat, color = colorPrimary)
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
                                color = if (year == selectedYear) colorPrimary else textColor,
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
                        Text(text = "Ok", fontFamily = montserrat, color = colorPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun PopUpSetValueDialog(
    onDismiss: () -> Unit,
    onConfirm: (LimitTransaction) -> Unit // Hàm callback nhận LimitTransaction
) {

    val viewModel: GetLimitTransactionViewModel = GetLimitTransactionViewModel(LocalContext.current)
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

    viewModel.getLimitTransaction(
        onError = {
            isLoading = false
        },
        onSuccess = {
            houseValue = TextFieldValue(it[0].percentLimit.toString())
            foodValue = TextFieldValue(it[1].percentLimit.toString())
            shoppingValue = TextFieldValue(it[2].percentLimit.toString())
            movingValue = TextFieldValue(it[3].percentLimit.toString())
            cosmeticValue = TextFieldValue(it[4].percentLimit.toString())
            exchangingValue = TextFieldValue(it[5].percentLimit.toString())
            medicalValue = TextFieldValue(it[6].percentLimit.toString())
            educatingValue = TextFieldValue(it[7].percentLimit.toString())
            saveValue = TextFieldValue(it[8].percentLimit.toString())
            isLoading = true
        }
    )

    // Hàm để tính giá trị còn lại và kiểm tra giới hạn 100%
    fun calculateRemainingValue() {
        val food = foodValue.text.toIntOrNull()
        val shopping = shoppingValue.text.toIntOrNull()
        val moving = movingValue.text.toIntOrNull()
        val house = houseValue.text.toIntOrNull()
        val cosmetic = cosmeticValue.text.toIntOrNull()
        val exchanging = exchangingValue.text.toIntOrNull()
        val medical = medicalValue.text.toIntOrNull()
        val educating = educatingValue.text.toIntOrNull()
        val save = saveValue.text.toIntOrNull()

        // Kiểm tra nếu giá trị nhập vào vượt quá 100%
        if ((food ?: 0) > 100 || (shopping ?: 0) > 100 || (moving ?: 0) > 100 || (house ?: 0) > 100 || (save ?: 0) > 100 || (cosmetic ?: 0) > 100 || (exchanging ?: 0) > 100 || (medical ?: 0) > 100 || (educating ?: 0) > 100) {
            errorMessage = "Giá trị không được vượt quá 100%"
            return
        } else {
            errorMessage = null.toString()
        }

        // Tính tổng giá trị đã nhập
        val enteredValues = listOf(food, shopping, moving, house, cosmetic, exchanging, medical, educating)
        val totalEntered = enteredValues.filterNotNull().sum()

        // Kiểm tra nếu tổng vượt quá 100%
        if (totalEntered > 100) {
            errorMessage = "Tổng không được vượt quá 100%"
            return
        }

        // Đếm số trường nhập liệu trống
        val emptyFields = listOf(
            foodValue.text.isBlank(),
            shoppingValue.text.isBlank(),
            movingValue.text.isBlank(),
            houseValue.text.isBlank(),
            cosmeticValue.text.isBlank(),
            exchangingValue.text.isBlank(),
            medicalValue.text.isBlank(),
            educatingValue.text.isBlank(),
            saveValue.text.isBlank()
        ).count { it }

        // Nếu có 1 trường trống, tính toán giá trị còn lại
        if (emptyFields != 0) {
            val remainingValue = (100 - totalEntered) / emptyFields
            when {
                foodValue.text.isBlank() -> {
                    foodValue = TextFieldValue(remainingValue.toString())
                }
                shoppingValue.text.isBlank() -> {
                    shoppingValue = TextFieldValue(remainingValue.toString())
                }
                movingValue.text.isBlank() -> {
                    movingValue = TextFieldValue(remainingValue.toString())
                }
                houseValue.text.isBlank() -> {
                    houseValue = TextFieldValue(remainingValue.toString())
                }
                cosmeticValue.text.isBlank() -> {
                    cosmeticValue = TextFieldValue(remainingValue.toString())
                }
                exchangingValue.text.isBlank() -> {
                    exchangingValue = TextFieldValue(remainingValue.toString())
                }
                medicalValue.text.isBlank() -> {
                    medicalValue = TextFieldValue(remainingValue.toString())
                }
                educatingValue.text.isBlank() -> {
                    educatingValue = TextFieldValue(remainingValue.toString())
                }
            }
        } else {
            val remainingValue = (100 - totalEntered)
            saveValue = TextFieldValue(remainingValue.toString())
        }

    }

    if (isLoading) {
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
                    errorMessage?.let {
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
                        color = colorPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    // Ô thứ nhất
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Chi phí nhà ở",
                            fontFamily = montserrat,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(2f)
                        )
                        Box(modifier = Modifier.weight(5.5f)) {
                            PercentTextField(
                                amountState = houseValue.text,
                                onValueChange = { newValue ->
                                    houseValue = TextFieldValue(newValue)
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number, // Chỉ hiển thị bàn phím số
                                    imeAction = ImeAction.Next, // Hiển thị nút "Done" hoặc "Tiếp tục"
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        calculateRemainingValue() // Gọi logic tính toán
                                        defaultKeyboardAction(ImeAction.Next) // Gọi hành động Next mặc định
                                    }
                                ),
                                colorPercent = Color.Black
                            )
                        }

                        Text(
                            text = " %",
                            fontFamily = montserrat,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(0.5f),
                            textAlign = TextAlign.Start
                        )
                    }

                    // Ô thứ hai
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Chi phí ăn uống",
                            fontFamily = montserrat,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(2f)
                        )
                        Box(modifier = Modifier.weight(5.5f)) {
                            PercentTextField(
                                amountState = foodValue.text,
                                onValueChange = { newValue ->
                                    foodValue = TextFieldValue(newValue)
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number, // Chỉ hiển thị bàn phím số
                                    imeAction = ImeAction.Next, // Hiển thị nút "Done" hoặc "Tiếp tục"
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        calculateRemainingValue() // Gọi logic tính toán
                                        defaultKeyboardAction(ImeAction.Next) // Gọi hành động Next mặc định
                                    }
                                ),
                                colorPercent = Color.Black
                            )
                        }

                        Text(
                            text = " %",
                            fontFamily = montserrat,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(0.5f),
                            textAlign = TextAlign.Start
                        )
                    }

                    // Ô thứ ba
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Mua sắm quần áo",
                            fontFamily = montserrat,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(2f)
                        )
                        Box(modifier = Modifier.weight(5.5f)) {
                            PercentTextField(
                                amountState = shoppingValue.text,
                                onValueChange = { newValue ->
                                    shoppingValue = TextFieldValue(newValue)
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number, // Chỉ hiển thị bàn phím số
                                    imeAction = ImeAction.Next, // Hiển thị nút "Done" hoặc "Tiếp tục"
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        calculateRemainingValue() // Gọi logic tính toán
                                        defaultKeyboardAction(ImeAction.Next) // Gọi hành động Next mặc định
                                    }
                                ),
                                colorPercent = Color.Black
                            )
                        }

                        Text(
                            text = " %",
                            fontFamily = montserrat,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(0.5f),
                            textAlign = TextAlign.Start
                        )
                    }

                    // Ô thứ tư
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Đi lại",
                            fontFamily = montserrat,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(2f)
                        )
                        Box(modifier = Modifier.weight(5.5f)) {
                            PercentTextField(
                                amountState = movingValue.text,
                                onValueChange = { newValue ->
                                    movingValue = TextFieldValue(newValue)
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number, // Chỉ hiển thị bàn phím số
                                    imeAction = ImeAction.Next, // Hiển thị nút "Done" hoặc "Tiếp tục"
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        calculateRemainingValue() // Gọi logic tính toán
                                        defaultKeyboardAction(ImeAction.Next) // Gọi hành động Next mặc định
                                    }
                                ),
                                colorPercent = Color.Black
                            )
                        }

                        Text(
                            text = " %",
                            fontFamily = montserrat,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(0.5f),
                            textAlign = TextAlign.Start
                        )
                    }

                    // Ô thứ năm
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Chăm sóc sắc đẹp",
                            fontFamily = montserrat,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(2f)
                        )
                        Box(modifier = Modifier.weight(5.5f)) {
                            PercentTextField(
                                amountState = cosmeticValue.text,
                                onValueChange = { newValue ->
                                    cosmeticValue = TextFieldValue(newValue)
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number, // Chỉ hiển thị bàn phím số
                                    imeAction = ImeAction.Next, // Hiển thị nút "Done" hoặc "Tiếp tục"
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        calculateRemainingValue() // Gọi logic tính toán
                                        defaultKeyboardAction(ImeAction.Next) // Gọi hành động Next mặc định
                                    }
                                ),
                                colorPercent = Color.Black
                            )
                        }

                        Text(
                            text = " %",
                            fontFamily = montserrat,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(0.5f),
                            textAlign = TextAlign.Start
                        )
                    }

                    // Ô thứ sáu
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Giao lưu",
                            fontFamily = montserrat,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(2f)
                        )
                        Box(modifier = Modifier.weight(5.5f)) {
                            PercentTextField(
                                amountState = exchangingValue.text,
                                onValueChange = { newValue ->
                                    exchangingValue = TextFieldValue(newValue)
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number, // Chỉ hiển thị bàn phím số
                                    imeAction = ImeAction.Next, // Hiển thị nút "Done" hoặc "Tiếp tục"
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        calculateRemainingValue() // Gọi logic tính toán
                                        defaultKeyboardAction(ImeAction.Next) // Gọi hành động Next mặc định
                                    }
                                ),
                                colorPercent = Color.Black
                            )
                        }

                        Text(
                            text = " %",
                            fontFamily = montserrat,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(0.5f),
                            textAlign = TextAlign.Start
                        )
                    }

                    // Ô thứ bảy
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Y tế",
                            fontFamily = montserrat,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(2f)
                        )
                        Box(modifier = Modifier.weight(5.5f)) {
                            PercentTextField(
                                amountState = medicalValue.text,
                                onValueChange = { newValue ->
                                    medicalValue = TextFieldValue(newValue)
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number, // Chỉ hiển thị bàn phím số
                                    imeAction = ImeAction.Next, // Hiển thị nút "Done" hoặc "Tiếp tục"
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        calculateRemainingValue() // Gọi logic tính toán
                                        defaultKeyboardAction(ImeAction.Next) // Gọi hành động Next mặc định
                                    }
                                ),
                                colorPercent = Color.Black
                            )
                        }

                        Text(
                            text = " %",
                            fontFamily = montserrat,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(0.5f),
                            textAlign = TextAlign.Start
                        )
                    }

                    // Ô thứ tám với KeyboardActions
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Học tập",
                            fontFamily = montserrat,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(2f)
                        )
                        Box(modifier = Modifier.weight(5.5f)) {
                            PercentTextField(
                                amountState = educatingValue.text,
                                onValueChange = { newValue ->
                                    educatingValue = TextFieldValue(newValue)
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number, // Chỉ hiển thị bàn phím số
                                    imeAction = ImeAction.Next, // Hiển thị nút "Done" hoặc "Tiếp tục"
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        calculateRemainingValue() // Gọi logic tính toán
                                        defaultKeyboardAction(ImeAction.Next) // Gọi hành động Next mặc định
                                    }
                                ),
                                colorPercent = Color.Black
                            )
                        }

                        Text(
                            text = " %",
                            fontFamily = montserrat,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(0.5f),
                            textAlign = TextAlign.Start
                        )
                    }

                    // Ô thứ chín (sẽ được tự động điền)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Khoản tiết kiệm",
                            fontFamily = montserrat,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(2f)
                        )
                        Box(modifier = Modifier.weight(5.5f)) {
                            PercentTextField(
                                modifier = Modifier
                                    .border(
                                        width = 2.dp, // Độ dày của viền
                                        color = highGray, // Màu sắc của viền
                                        shape = RoundedCornerShape(8.dp) // Bo tròn các góc nếu cần
                                    ),
                                amountState = saveValue.text,
                                onValueChange = { newValue ->
                                    saveValue = TextFieldValue(newValue)
                                },
                                enabled = false, // Không cho phép người dùng nhập vào ô này
                                colorPercent = colorPrimary
                            )
                        }

                        Text(
                            text = " %",
                            color = textColor,
                            fontFamily = montserrat,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(0.5f),
                            textAlign = TextAlign.Start
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = { onDismiss() }) {
                            Text(text = "Huỷ bỏ", fontFamily = montserrat, color = textColor)
                        }
                        TextButton(onClick = {
                            // Lấy dữ liệu và gửi qua callback
                            val categoryLimits = listOf(
                                LimitTransaction.CategoryLimit(1, houseValue.text.toIntOrNull() ?: 0),
                                LimitTransaction.CategoryLimit(2, foodValue.text.toIntOrNull() ?: 0),
                                LimitTransaction.CategoryLimit(3, shoppingValue.text.toIntOrNull() ?: 0),
                                LimitTransaction.CategoryLimit(4, movingValue.text.toIntOrNull() ?: 0),
                                LimitTransaction.CategoryLimit(5, cosmeticValue.text.toIntOrNull() ?: 0),
                                LimitTransaction.CategoryLimit(6, exchangingValue.text.toIntOrNull() ?: 0),
                                LimitTransaction.CategoryLimit(7, medicalValue.text.toIntOrNull() ?: 0),
                                LimitTransaction.CategoryLimit(8, educatingValue.text.toIntOrNull() ?: 0),
                                LimitTransaction.CategoryLimit(9, saveValue.text.toIntOrNull() ?: 0)
                            )
                            onConfirm(LimitTransaction(categoryLimits))
                            onDismiss()
                        }) {
                            Text(text = "OK", fontFamily = montserrat, color = colorPrimary)
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
fun PercentTextField(
    amountState: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Next // Mặc định là "Next", có thể tùy chỉnh
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    colorPercent: Color
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = amountState,
        onValueChange = { newInput ->
            if (newInput == "0" && amountState.isEmpty()) {
                // Không làm gì để chặn '0' đầu tiên
            } else {
                val filteredInput = newInput.filter { it.isDigit() }
                onValueChange(
                    if (filteredInput.isNotEmpty() && filteredInput != "0") {
                        filteredInput
                    } else if (filteredInput == "0" && amountState.isNotEmpty()) {
                        filteredInput
                    } else {
                        ""
                    }
                )
            }
        },
        singleLine = true,
        enabled = enabled,
        modifier = modifier
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .height(45.dp)
            .fillMaxWidth()
            .background(Color(0xFFe1e1e1), shape = RoundedCornerShape(8.dp))
            .border(
                1.dp,
                if (isFocused) colorPrimary else Color.Transparent,
                RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp),
        textStyle = TextStyle(
            textAlign = TextAlign.Start,
            fontSize = 20.sp,
            fontFamily = montserrat,
            fontWeight = FontWeight.Bold,
            color = colorPercent,
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                if (amountState.isEmpty()) {
                    Text(
                        text = if (isFocused) "" else "0",
                        color = textColor,
                        fontWeight = FontWeight.Bold,
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
    items: List<Pair<String, () -> Unit>>, // Danh sách các mục với mô tả và chức năng không phải Composable
    painters: List<Painter> // Danh sách các biểu tượng (painter) tương ứng với từng mục
) {
    // Kiểm tra độ dài của painters và items có khớp không
    require(items.size == painters.size) { "The number of items and painters must match." }

    Column(modifier = Modifier.fillMaxWidth()) {
        items.forEachIndexed { index, item ->
            // Xác định bo góc cho phần tử dựa vào vị trí của nó
            val cornerShape = when (index) {
                0 -> RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                1 -> RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                else -> RoundedCornerShape(0.dp)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.White, shape = cornerShape) // Bo góc tùy vào phần tử
                    .clickable(onClick = item.second), // Gọi onClick khi nhấn
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
                        tint = colorPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = item.first,
                        fontFamily = montserrat,
                        color = textColor,
                        fontWeight = FontWeight.Normal,
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
        withStyle(style = SpanStyle(fontSize = 12.sp)) {  // Kích thước nhỏ hơn cho ký tự "₫"
            append("₫")
        }
    }

    val formattedExpense = buildAnnotatedString {
        append("-${currencyFormatter.format(expense)}")
        withStyle(style = SpanStyle(fontSize = 12.sp)) {  // Kích thước nhỏ hơn cho ký tự "₫"
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
            .background(Color.White),
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
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formattedIncome,
                        fontFamily = montserrat,
                        color = Color(0xff1d9fca),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }

                // Divider dọc giữa các cột thu nhập và chi tiêu
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp) // Chiều rộng của divider
                        .background(Color.LightGray) // Màu của divider
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
                        fontFamily = montserrat,
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formattedExpense,
                        fontFamily = montserrat,
                        color = Color(0xffff6561),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
            }

            Divider(Modifier.fillMaxWidth(0.8f))

            Row(
                verticalAlignment = Alignment.CenterVertically,  // Căn giữa theo chiều dọc
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(
                    text = "Balance",
                    fontFamily = montserrat,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
                Text(
                    text = formattedBalance,
                    fontFamily = montserrat,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                )
            }

        }
    }
}

