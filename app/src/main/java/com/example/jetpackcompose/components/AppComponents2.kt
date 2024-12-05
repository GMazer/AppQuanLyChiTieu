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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
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
import com.example.jetpackcompose.R
import com.example.jetpackcompose.ui.theme.textColor
import com.example.jetpackcompose.ui.theme.colorPrimary
import java.util.Calendar
import com.example.jetpackcompose.ui.theme.bgColor
import com.example.jetpackcompose.ui.theme.topBarColor
import androidx.compose.material3.TopAppBar
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.motionEventSpy
import com.example.jetpackcompose.app.features.inputFeatures.LimitTransaction
import com.example.jetpackcompose.app.features.inputFeatures.monsterrat
import com.example.jetpackcompose.components.YearPickerDialog
import com.example.jetpackcompose.components.YearPickerButton
import com.example.jetpackcompose.ui.theme.colorContrast
import com.example.jetpackcompose.ui.theme.colorPrimary
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.graphics.Color as ComposeColor

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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val maxStrokeWidth = 150f  // Chiều rộng tối đa của vòng tròn donut
            val radius = size.minDimension / 2

            // Vẽ nền lightGray
            drawArc(
                color = Color.LightGray,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = maxStrokeWidth + 20f, cap = StrokeCap.Butt)
            )

            var startAngle = -90f

            // Vẽ các phần của donut chart
            angles.forEachIndexed { index, sweepAngle ->
                // Vẽ phần chính của donut chart
                drawArc(
                    color = colors[index].copy(alpha = 0.1f),
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = maxStrokeWidth, cap = StrokeCap.Butt)
                )

                // Tính toán chiều rộng stroke cho phần "mực nước" dựa vào tỷ lệ hoàn thành
                val progressWidth = maxStrokeWidth * progresses[index] // Tỷ lệ chiều rộng mực nước

                // Tính toán bán kính cho phần "mực nước" để áp vào lề trong
                val progressRadius = radius - (maxStrokeWidth - progressWidth) / 2

                // Vẽ "mực nước" (progress) với chiều rộng theo tỷ lệ hoàn thành và bán kính được điều chỉnh
                drawArc(
                    color = colors[index].copy(alpha = 0.5f), // Làm nhạt màu
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

                startAngle += sweepAngle
            }

            // Vẽ các đường ngăn cách màu trắng
            startAngle = -90f
            angles.forEach { sweepAngle ->
                val angleInRadians = Math.toRadians(startAngle.toDouble())
                val lineStart = Offset(
                    x = center.x + (radius - maxStrokeWidth / 2) * cos(angleInRadians).toFloat(),
                    y = center.y + (radius - maxStrokeWidth / 2) * sin(angleInRadians).toFloat()
                )
                val lineEnd = Offset(
                    x = center.x + (radius + maxStrokeWidth / 2) * cos(angleInRadians).toFloat(),
                    y = center.y + (radius + maxStrokeWidth / 2) * sin(angleInRadians).toFloat()
                )
                drawLine(
                    color = Color.White,
                    start = lineStart,
                    end = lineEnd,
                    strokeWidth = 5f // Độ rộng của đường ngăn
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
fun YearPickerButton(onYearSelected: (String) -> Unit) {
    var yearText by remember { mutableStateOf("") }
    var showYearPicker by remember { mutableStateOf(false) }
    val calendar = remember { mutableStateOf(Calendar.getInstance()) }

    // Hàm để cập nhật chuỗi hiển thị năm
    fun updateYearText() {
        val year = calendar.value.get(Calendar.YEAR).toString()
        yearText = year // Cập nhật năm
        onYearSelected(year) // Gọi callback với giá trị năm
    }

    // Cập nhật năm hiện tại khi khởi tạo
    LaunchedEffect(key1 = true) {
        updateYearText()
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        // Nút lùi năm
        IconButton(
            onClick = {
                calendar.value.add(Calendar.YEAR, -1)
                updateYearText() // Gọi callback khi lùi năm
            },
            modifier = Modifier
                .weight(1f)
                .size(20.dp)
        ) {
            androidx.compose.material3.Icon(
                painter = painterResource(id = R.drawable.outline_arrow_back_ios_24),
                contentDescription = "Lùi năm",
                tint = Color(0xFF444444)
            )
        }
        // Nút chọn năm
        Button(
            modifier = Modifier.weight(8f),
            shape = RoundedCornerShape(8.dp),
            onClick = { showYearPicker = true }, // Hiển thị YearPickerDialog
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFe1e1e1))
        ) {
            Text(
                yearText,
                fontFamily = montserrat,
                color = Color(0xFF444444),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        // Nút tiến năm
        IconButton(
            onClick = {
                calendar.value.add(Calendar.YEAR, +1)
                updateYearText() // Gọi callback khi tiến năm
            },
            modifier = Modifier
                .weight(1f)
                .size(20.dp)
        ) {
            androidx.compose.material3.Icon(
                painter = painterResource(id = R.drawable.outline_arrow_forward_ios_24),
                contentDescription = "Tiến năm",
                tint = Color(0xFF444444)
            )
        }
    }

    // Hiển thị YearPickerDialog khi cần
    if (showYearPicker) {
        YearPickerDialog(
            initialYear = calendar.value.get(Calendar.YEAR),
            onDismiss = { showYearPicker = false },
            onYearSelected = { selectedYear ->
                calendar.value.set(Calendar.YEAR, selectedYear)
                updateYearText() // Cập nhật năm sau khi chọn
                showYearPicker = false
            }
        )
    }
}

@Composable
fun PopUpSetValueDialog(
    onDismiss: () -> Unit,
    onConfirm: (LimitTransaction) -> Unit // Hàm callback nhận LimitTransaction
) {
    var basicValue by remember { mutableStateOf(TextFieldValue()) }
    var entertainmentValue by remember { mutableStateOf(TextFieldValue()) }
    var investValue by remember { mutableStateOf(TextFieldValue()) }
    var incidentalValue by remember { mutableStateOf(TextFieldValue()) }
    var saveValue by remember { mutableStateOf(TextFieldValue()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Hàm để tính giá trị còn lại và kiểm tra giới hạn 100%
    fun calculateRemainingValue() {
        val entertainment = entertainmentValue.text.toIntOrNull()
        val invest = investValue.text.toIntOrNull()
        val incidental = incidentalValue.text.toIntOrNull()
        val basic = basicValue.text.toIntOrNull()
        val save = saveValue.text.toIntOrNull()

        // Kiểm tra nếu giá trị nhập vào vượt quá 100%
        if ((entertainment ?: 0) > 100 || (invest ?: 0) > 100 || (incidental ?: 0) > 100 || (basic ?: 0) > 100 || (save ?: 0) > 100) {
            errorMessage = "Giá trị không được vượt quá 100%"
            return
        } else {
            errorMessage = null
        }

        // Tính tổng giá trị đã nhập
        val enteredValues = listOf(entertainment, invest, incidental, basic, save)
        val totalEntered = enteredValues.filterNotNull().sum()

        // Kiểm tra nếu tổng vượt quá 100%
        if (totalEntered > 100) {
            errorMessage = "Tổng không được vượt quá 100%"
            return
        }

        // Đếm số trường nhập liệu trống
        val emptyFields = listOf(
            entertainmentValue.text.isBlank(),
            investValue.text.isBlank(),
            incidentalValue.text.isBlank(),
            basicValue.text.isBlank(),
            saveValue.text.isBlank()
        ).count { it }

        // Nếu có 1 trường trống, tính toán giá trị còn lại
        if (emptyFields == 1) {
            val remainingValue = 100 - totalEntered
            when {
                entertainmentValue.text.isBlank() -> {
                    entertainmentValue = TextFieldValue(remainingValue.toString())
                }
                investValue.text.isBlank() -> {
                    investValue = TextFieldValue(remainingValue.toString())
                }
                incidentalValue.text.isBlank() -> {
                    incidentalValue = TextFieldValue(remainingValue.toString())
                }
                basicValue.text.isBlank() -> {
                    basicValue = TextFieldValue(remainingValue.toString())
                }
                saveValue.text.isBlank() -> {
                    saveValue = TextFieldValue(remainingValue.toString())
                }
            }
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
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Chi phí thiết yếu",
                        fontFamily = montserrat,
                        color = textColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(2f)
                    )
                    Box(modifier = Modifier.weight(5.5f)) {
                        PercentTextField(
                            amountState = basicValue.text,
                            onValueChange = { newValue ->
                                basicValue = TextFieldValue(newValue)
                            },
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
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Ngân sách giải trí",
                        fontFamily = montserrat,
                        color = textColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(2f)
                    )
                    Box(modifier = Modifier.weight(5.5f)) {
                        PercentTextField(
                            amountState = entertainmentValue.text,
                            onValueChange = { newValue ->
                                entertainmentValue = TextFieldValue(newValue)
                            },
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
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Ngân sách đầu tư",
                        fontFamily = montserrat,
                        color = textColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(2f)
                    )
                    Box(modifier = Modifier.weight(5.5f)) {
                        PercentTextField(
                            amountState = investValue.text,
                            onValueChange = { newValue ->
                                investValue = TextFieldValue(newValue)
                            },
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

                // Ô thứ tư với KeyboardActions
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Quỹ dự phòng",
                        fontFamily = montserrat,
                        color = textColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(2f)
                    )
                    Box(modifier = Modifier.weight(5.5f)) {
                        PercentTextField(
                            amountState = incidentalValue.text,
                            onValueChange = { newValue ->
                                incidentalValue = TextFieldValue(newValue)
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    calculateRemainingValue()
                                }
                            )
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

                // Ô thứ năm (sẽ được tự động điền)
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
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(2f)
                    )
                    Box(modifier = Modifier.weight(5.5f)) {
                        PercentTextField(
                            amountState = saveValue.text,
                            onValueChange = { newValue ->
                                saveValue = TextFieldValue(newValue)
                            },
                            enabled = false // Không cho phép người dùng nhập vào ô này
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
                            LimitTransaction.CategoryLimit(1, basicValue.text.toIntOrNull() ?: 0),
                            LimitTransaction.CategoryLimit(2, entertainmentValue.text.toIntOrNull() ?: 0),
                            LimitTransaction.CategoryLimit(3, investValue.text.toIntOrNull() ?: 0),
                            LimitTransaction.CategoryLimit(4, incidentalValue.text.toIntOrNull() ?: 0),
                            LimitTransaction.CategoryLimit(5, saveValue.text.toIntOrNull() ?: 0)
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
    keyboardActions: KeyboardActions = KeyboardActions.Default
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
            color = Color.Black
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
fun OtherTab(value: String, onClick: () -> Unit, painter: Painter) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(top = 16.dp)
            .background(Color.White) // Thêm màu nền
            .clickable(onClick = onClick), // Thêm chức năng click
        contentAlignment = Alignment.CenterStart // Căn chỉnh nội dung
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
            horizontalArrangement = Arrangement.Start, // Căn trái theo chiều ngang
            modifier = Modifier.fillMaxWidth() // Chiếm toàn bộ chiều rộng của Box
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                painter = painter,
                contentDescription = "Icon",
                tint = colorPrimary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = value,
                fontFamily = montserrat,
                color = Color(0xFF444444),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun ReportMonth(tag: String, value: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(Color.White), // Thêm màu nền
        contentAlignment = Alignment.CenterStart // Căn chỉnh nội dung
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
            horizontalArrangement = Arrangement.Start, // Căn trái theo chiều ngang
            modifier = Modifier.fillMaxWidth() // Chiếm toàn bộ chiều rộng của Box
        ) {
            Spacer(modifier = Modifier.width(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = tag,
                fontFamily = montserrat,
                color = Color(0xFF444444),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                modifier = Modifier.width(48.dp)
            )
            Spacer(modifier = Modifier.width(170.dp))
            Text(
                text = "$value",
                fontFamily = montserrat,
                color = Color(0xFF444444),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                modifier = Modifier.width(100.dp),
                textAlign = TextAlign.End,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "₫",
                fontFamily = montserrat,
                color = Color(0xFF444444),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        }
    }
}




@Preview
@Composable
fun PreviewMonthPickerDialog() {
//    PopUpSetValueDialog {  }
}