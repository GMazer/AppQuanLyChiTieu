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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
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
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Hàm để tính giá trị còn lại và kiểm tra giới hạn 100%
    fun calculateRemainingValue() {
        val entertainment = entertainmentValue.text.toDoubleOrNull() ?: 0.0
        val invest = investValue.text.toDoubleOrNull() ?: 0.0
        val incidental = incidentalValue.text.toDoubleOrNull() ?: 0.0

        if (entertainment > 100 || invest > 100 || incidental > 100) {
            errorMessage = "Giá trị không được vượt quá 100%"
            if (entertainment > 100) entertainmentValue = TextFieldValue("")
            if (invest > 100) investValue = TextFieldValue("")
            if (incidental > 100) incidentalValue = TextFieldValue("")
            return
        } else {
            errorMessage = null // Xóa thông báo lỗi nếu không có lỗi
        }

        val totalEntered = entertainment + invest + incidental
        if (totalEntered > 100) {
            return // Đảm bảo rằng tổng không vượt quá 100%
        }

        val emptyFields = listOf(
            entertainmentValue.text.isBlank(),
            investValue.text.isBlank(),
            incidentalValue.text.isBlank()
        ).count { it }

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
                    Box(modifier = Modifier.weight(5.5f)) {
                        NumberTextField(
                            amountState = basicValue.text,
                            onValueChange = { newValue ->
                                basicValue = TextFieldValue(newValue)
                            },
                        )
                    }

                    Text(
                        text = "đ",
                        fontFamily = monsterrat,
                        color = TextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(0.5f),
                        textAlign = TextAlign.Center
                    )
                }

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
                                calculateRemainingValue()
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
                        PercentTextField (
                            amountState = investValue.text,
                            onValueChange = { newValue ->
                                investValue = TextFieldValue(newValue)
                                calculateRemainingValue()
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
                                calculateRemainingValue()
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
fun PercentTextField(amountState: String, onValueChange: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = amountState,
        onValueChange = { newInput ->
            if (newInput == "0" && amountState.isEmpty()) {
                // do nothing to block the first '0'
            } else {
                val filteredInput = newInput.filter { it.isDigit() || it == '.' }
                onValueChange(
                    if (filteredInput.isNotEmpty() && filteredInput != "0" && filteredInput != ".") {
                        filteredInput
                    } else if (filteredInput == "0" && !amountState.isEmpty()) {
                        filteredInput
                    } else {
                        ""
                    }
                )
            }
        },
        singleLine = true,
        modifier = Modifier
            .focusRequester(focusRequester)
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
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()  // Clear focus from the text field
                keyboardController?.hide()  // Hide the keyboard
            }
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                if (amountState.isEmpty()) {
                    androidx.compose.material3.Text(
                        if (isFocused) "" else "0",
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




@Preview
@Composable
fun PreviewMonthPickerDialog() {
    PopUpSetValueDialog {  }
}