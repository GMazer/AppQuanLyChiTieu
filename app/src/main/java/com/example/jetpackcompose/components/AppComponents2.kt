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
import com.example.jetpackcompose.ui.theme.TextColor
import com.example.jetpackcompose.ui.theme.colorPrimary
import java.util.Calendar
import com.example.jetpackcompose.ui.theme.bgColor
import com.example.jetpackcompose.ui.theme.topBarColor
import androidx.compose.material3.TopAppBar
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.motionEventSpy
import com.example.jetpackcompose.components.YearPickerDialog
import com.example.jetpackcompose.components.YearPickerButton
import com.example.jetpackcompose.ui.theme.colorContrast
import com.example.jetpackcompose.ui.theme.colorPrimary
import androidx.compose.ui.graphics.Color as ComposeColor


@Composable
fun BarChartWithLine(values: List<Int>, index: List<Int>, months: List<String>) {
    val canvasHeight = 500f
    val barWidth = 40f
    val spaceBetweenBars = 40f  // Khoảng cách giữa các cột

    // Tính chiều rộng của canvas dựa trên số lượng cột và khoảng cách giữa các cột
    val chartWidth = (barWidth + spaceBetweenBars) * values.size - spaceBetweenBars + 50f

    Box(
        modifier = Modifier
            .fillMaxWidth()  // Lấp đầy chiều rộng màn hình
            .height(240.dp)  // Chiều cao của biểu đồ
            .wrapContentSize(Alignment.Center)  // Căn giữa canvas
    ) {
        Canvas(modifier = Modifier
            .width(chartWidth.dp)  // Thiết lập chiều rộng biểu đồ theo chiều rộng tính toán
            .height(240.dp) // Chiều cao của canvas
            .padding(start = 28.dp) // Padding vào trục Y, không ảnh hưởng đến các đường ngang
        ) {
            val maxValue = (values.maxOrNull() ?: 0 ) * 1.2f
            val scaleFactor = canvasHeight / maxValue

            // Vẽ nền
            drawRect(
                color = Color.White,
                size = size,
                topLeft = Offset(0f, 0f)
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
                        textSize = 36f
                        color = android.graphics.Color.BLACK
                        textAlign = android.graphics.Paint.Align.RIGHT
                    }
                )
            }

            // Vẽ các cột màu cam
            values.forEachIndexed { index, value ->
                val left = (barWidth + spaceBetweenBars) * index.toFloat() + 50f  // Thêm padding vào trục Y
                val top = canvasHeight - value * scaleFactor
                val right = left + barWidth
                val bottom = canvasHeight // Đảm bảo đáy cột nằm ở trục X

                drawRoundRect(
                    color = colorPrimary, // Màu cam
                    topLeft = Offset(left, top),
                    size = androidx.compose.ui.geometry.Size(barWidth, bottom - top),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius.Zero
                )
            }

            // Vẽ đường trục X
            drawLine(
                color = Color.LightGray,
                start = Offset(0f, canvasHeight),
                end = Offset(chartWidth, canvasHeight),
                strokeWidth = 4f
            )

            // Tạo các điểm để vẽ đường uốn lượn
            val points = mutableListOf<Offset>()
            index.forEachIndexed { index, value ->
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
                color = colorContrast, // Màu xanh dương cho đường nối
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
            )

            // Vẽ các điểm tròn tại các điểm trên đường nối
            points.forEach { point ->
                drawCircle(
                    color = colorContrast, // Màu xanh dương
                    radius = 8f, // Kích thước của điểm tròn
                    center = point
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
                            tint = TextColor
                        )
                    }
                    Text(
                        text = selectedYear.value.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = monsterrat,
                        color = TextColor

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
                                fontFamily = monsterrat,
                                color = if (isSelected) Color.White else TextColor,
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
                        Text(text = "Cancel", fontFamily = monsterrat, color = TextColor)
                    }
                    TextButton(onClick = {
                        onMonthYearSelected(selectedMonth.value, selectedYear.value)
                        onDismiss()
                    }) {
                        Text(text = "Ok", fontFamily = monsterrat, color = colorPrimary)
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
                    fontFamily = monsterrat,
                    color = TextColor,
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
                                fontFamily = monsterrat,
                                fontWeight = if (year == selectedYear) FontWeight.Bold else FontWeight.Normal,
                                color = if (year == selectedYear) colorPrimary else TextColor,
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
                        Text(text = "Cancel", fontFamily = monsterrat, color = TextColor)
                    }
                    TextButton(onClick = {
                        onYearSelected(selectedYear)
                        onDismiss()
                    }) {
                        Text(text = "Ok", fontFamily = monsterrat, color = colorPrimary)
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
                fontFamily = monsterrat,
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
    onDismiss: () -> Unit
) {
    var basicValue by remember { mutableStateOf(TextFieldValue()) }
    var entertainmentValue by remember { mutableStateOf(TextFieldValue()) }
    var investValue by remember { mutableStateOf(TextFieldValue()) }
    var incidentalValue by remember { mutableStateOf(TextFieldValue()) }
    var saveValue by remember { mutableStateOf(TextFieldValue()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Hàm để tính giá trị còn lại và kiểm tra giới hạn 100%
    fun calculateRemainingValue() {
        // Sử dụng Int thay vì Double
        val entertainment = entertainmentValue.text.toIntOrNull()
        val invest = investValue.text.toIntOrNull()
        val incidental = incidentalValue.text.toIntOrNull()
        val basic = basicValue.text.toIntOrNull()
        val save = saveValue.text.toIntOrNull()

        // Kiểm tra nếu giá trị nhập vào vượt quá 100%
        if ((entertainment ?: 0) > 100 || (invest ?: 0) > 100 || (incidental ?: 0) > 100 || (basic ?: 0) > 100 || (save ?: 0) > 100) {
            errorMessage = "Giá trị không được vượt quá 100%"
            // Reset lại các trường nhập liệu nếu giá trị vượt quá 100
            if ((entertainment ?: 0) > 100) entertainmentValue = TextFieldValue("")
            if ((invest ?: 0) > 100) investValue = TextFieldValue("")
            if ((incidental ?: 0) > 100) incidentalValue = TextFieldValue("")
            if ((basic ?: 0) > 100) basicValue = TextFieldValue("")
            if ((save ?: 0) > 100) saveValue = TextFieldValue("")
            return
        } else {
            errorMessage = null // Xóa thông báo lỗi nếu không có lỗi
        }

        // Tính tổng giá trị đã nhập
        val enteredValues = listOf(entertainment, invest, incidental, basic, save)
        val totalEntered = enteredValues.filterNotNull().sum()

        // Kiểm tra nếu tổng vượt quá 100%
        if (totalEntered > 100) {
            errorMessage = "Tổng không được vượt quá 100%"
            return // Đảm bảo tổng không vượt quá 100%
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
                    fontFamily = monsterrat,
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
                        fontFamily = monsterrat,
                        color = TextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(2f)
                    )
                    Box(modifier = Modifier.weight(2.5f)) {
                        PercentTextField(
                            amountState = basicValue.text,
                            onValueChange = { newValue ->
                                basicValue = TextFieldValue(newValue)
                                // Loại bỏ việc gọi calculateRemainingValue()
                            },
                        )
                    }

                    Text(
                        text = " %",
                        fontFamily = monsterrat,
                        color = TextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(3.5f),
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
                        fontFamily = monsterrat,
                        color = TextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(2f)
                    )
                    Box(modifier = Modifier.weight(2.5f)) {
                        PercentTextField(
                            amountState = entertainmentValue.text,
                            onValueChange = { newValue ->
                                entertainmentValue = TextFieldValue(newValue)
                                // Loại bỏ việc gọi calculateRemainingValue()
                            },
                        )
                    }

                    Text(
                        text = " %",
                        fontFamily = monsterrat,
                        color = TextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(3.5f),
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
                        fontFamily = monsterrat,
                        color = TextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(2f)
                    )
                    Box(modifier = Modifier.weight(2.5f)) {
                        PercentTextField(
                            amountState = investValue.text,
                            onValueChange = { newValue ->
                                investValue = TextFieldValue(newValue)
                                // Loại bỏ việc gọi calculateRemainingValue()
                            },
                        )
                    }

                    Text(
                        text = " %",
                        fontFamily = monsterrat,
                        color = TextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(3.5f),
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
                        fontFamily = monsterrat,
                        color = TextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(2f)
                    )
                    Box(modifier = Modifier.weight(2.5f)) {
                        PercentTextField(
                            amountState = incidentalValue.text,
                            onValueChange = { newValue ->
                                incidentalValue = TextFieldValue(newValue)
                                // Không gọi calculateRemainingValue() ở đây
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    calculateRemainingValue()
                                    // Ẩn bàn phím sau khi hoàn thành
                                    // Bạn cần truyền `focusManager` vào nếu muốn sử dụng
                                }
                            )
                        )
                    }

                    Text(
                        text = " %",
                        fontFamily = monsterrat,
                        color = TextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(3.5f),
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
                        fontFamily = monsterrat,
                        color = TextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(2f)
                    )
                    Box(modifier = Modifier.weight(2.5f)) {
                        PercentTextField(
                            amountState = saveValue.text,
                            onValueChange = { newValue ->
                                saveValue = TextFieldValue(newValue)
                                // Loại bỏ việc gọi calculateRemainingValue()
                            },
                            enabled = false // Không cho phép người dùng nhập vào ô này
                        )
                    }

                    Text(
                        text = " %",
                        fontFamily = monsterrat,
                        color = TextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(3.5f),
                        textAlign = TextAlign.Start
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text(text = "Huỷ bỏ", fontFamily = monsterrat, color = TextColor)
                    }
                    TextButton(onClick = {
                        onDismiss()
                    }) {
                        Text(text = "OK", fontFamily = monsterrat, color = colorPrimary)
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
            .width(250.dp)
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
            fontFamily = monsterrat,
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
                        color = TextColor,
                        fontWeight = FontWeight.Bold,
                        fontFamily = monsterrat,
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
            Spacer(modifier = Modifier.width(24.dp))
            Icon(
                painter = painter,
                contentDescription = "Icon",
                tint = colorPrimary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = value,
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
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
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                modifier = Modifier.width(48.dp)
            )
            Spacer(modifier = Modifier.width(240.dp))
            Text(
                text = "$value",
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                modifier = Modifier.width(48.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "₫",
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        }
    }
}




@Preview
@Composable
fun PreviewMonthPickerDialog() {
    PopUpSetValueDialog {  }
}