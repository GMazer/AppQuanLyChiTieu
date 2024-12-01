package com.example.jetpackcompose.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Text
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.inputFeatures.Category
import com.example.jetpackcompose.ui.theme.textColor
import com.example.jetpackcompose.ui.theme.TextColorPrimary
import com.example.jetpackcompose.ui.theme.bgColor
import com.example.jetpackcompose.ui.theme.bgItemColor
import com.example.jetpackcompose.ui.theme.colorPrimary
import com.example.jetpackcompose.ui.theme.colorSecondary
import com.example.jetpackcompose.ui.theme.componentShapes
import com.example.jetpackcompose.ui.theme.highGray
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.onSizeChanged
import com.example.jetpackcompose.app.screens.CalendarScreen
import com.example.jetpackcompose.app.screens.DailyTransaction
import com.example.jetpackcompose.ui.theme.SaturDayColor
import com.example.jetpackcompose.ui.theme.SundayColor
import java.lang.StrictMath.PI
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.sin


val monsterrat = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_bold, FontWeight.Bold),
    Font(R.font.montserrat_light, FontWeight.Light),
    Font(R.font.montserrat_medium, FontWeight.Medium)
)

@Composable
fun NormalTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .heightIn(min = 32.dp)
            .fillMaxWidth(),
        style = TextStyle(
            fontSize = 18.sp,
            fontFamily = monsterrat,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
        ),
        color = textColor,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun HeadingTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .heightIn(min = 48.dp)
            .fillMaxWidth(),
        style = TextStyle(
            fontSize = 28.sp,
            fontFamily = monsterrat,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
        ),
        color = TextColorPrimary,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun MyTextFieldComponent(
    value: String,
    onValueChange: (String) -> Unit,
    labelValue: String,
    painterResource: Painter,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    isPassword: Boolean = false
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val isFocused = remember { mutableStateOf(false) }
    val isPasswordVisible = remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(componentShapes.small)
            .onFocusChanged { focusState ->
                isFocused.value = focusState.isFocused
            },
        shape = RoundedCornerShape(10.dp),
        label = {
            Text(
                text = labelValue,
                fontFamily = monsterrat,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = if (isFocused.value) colorPrimary else Color.LightGray
            )
        },
        value = value,
        onValueChange = onValueChange,
        visualTransformation = if (isPassword && !isPasswordVisible.value) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorPrimary,
            focusedLabelColor = colorPrimary,
            unfocusedLabelColor = Color.LightGray,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = colorPrimary,
            textColor = textColor,
            backgroundColor = bgColor
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()  // Clear focus from the text field
                keyboardController?.hide()  // Hide the keyboard
            }
        ),
        leadingIcon = {
            Icon(
                painter = painterResource,
                contentDescription = "Leading icon for $labelValue",
                tint = highGray
            )
        },
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
                    Icon(
                        painter = painterResource(
                            if (isPasswordVisible.value) R.drawable.baseline_key_24 else R.drawable.baseline_key_off_24
                        ),
                        contentDescription = if (isPasswordVisible.value) "Hide password" else "Show password",
                        tint = highGray
                    )
                }
            }
        } else null
    )
}


@Composable
fun PasswordTextFieldComponent(
    value: String,
    onValueChange: (String) -> Unit,
    labelValue: String,
    painterResource: Painter
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val isFocused = remember { mutableStateOf(false) }
    val passwordVisibility = remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(componentShapes.small)
            .onFocusChanged { focusState ->
                isFocused.value = focusState.isFocused
            },
        shape = RoundedCornerShape(10.dp),
        label = {
            Text(
                text = labelValue,
                fontFamily = monsterrat,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = if (isFocused.value) colorPrimary else Color.LightGray
            )
        },
        value = value,
        onValueChange = onValueChange,
        visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorPrimary,
            focusedLabelColor = colorPrimary,
            unfocusedLabelColor = Color.LightGray,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = colorPrimary,
            textColor = textColor,
            backgroundColor = bgColor
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        ),
        leadingIcon = {
            Icon(
                painter = painterResource,
                contentDescription = "Leading icon for $labelValue",
                tint = highGray
            )
        },
        trailingIcon = {
            val icon = if (passwordVisibility.value) {
                painterResource(R.drawable.outline_visibility_off_24)
            } else {
                painterResource(R.drawable.outline_visibility)
            }
            val description = if (passwordVisibility.value) {
                "Ẩn mật khẩu"
            } else {
                "Hiện mật khẩu"
            }
            IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                Icon(
                    painter = icon,
                    contentDescription = description,
                    tint = highGray
                )
            }
        }
    )
}


@Composable
fun CheckboxComponent(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // Cách đều trên dưới
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = colorPrimary,
                uncheckedColor = Color.Gray,
                checkmarkColor = Color.White
            )
        )
        Spacer(modifier = Modifier.width(8.dp)) // Tạo khoảng cách giữa checkbox và text
        Text(
            text = text,
            fontFamily = monsterrat, // Đảm bảo khai báo `monsterrat`
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Color(0xFF777777),
            modifier = Modifier.weight(1f) // Tự động chiếm không gian còn lại
        )
    }
}



@Composable
fun DrawBottomLine(height: Dp) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .drawBehind {
                val strokeWidth = 0.8.dp.toPx()
                val y = size.height / 2
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
    )
}

@Composable
fun DrawTopLine(height: Dp) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .drawBehind {
                val strokeWidth = 0.8.dp.toPx()
                val y = strokeWidth / 2
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
    )
}

//Tạo layout với LazyColumn
@Composable
fun CategoriesGrid(
    categories: List<Category>,
    buttonColor: Color,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(categories) { category ->
            CategoryItem(
                id = category.id,
                category = category,
                buttonColor = buttonColor,
                isSelected = (category == selectedCategory),
                onClick = { onCategorySelected(category) },
                percentage = category.percentage
            )
        }
    }
}

@Composable
fun CategoryItem(
    id: Int,
    category: Category,
    buttonColor: Color,
    isSelected: Boolean,
    percentage: Float,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) buttonColor else Color.Transparent

    // State to hold the size of the Box
    var boxHeight by remember { mutableStateOf(0f) }

    // Tạo Animatable để điều khiển offset cho sóng
    val waveOffset = remember { Animatable(0f) }

    // Tạo hiệu ứng sóng uốn lượn
    LaunchedEffect(key1 = true) {
        waveOffset.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2500, // Tăng durationMillis để làm chậm animation
                    easing = LinearEasing // Sử dụng LinearEasing để chuyển động mượt mà
                ),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    // Tạo giá trị sóng thay đổi theo percentage (nâng lên hạ xuống)
    val waveYOffset by animateFloatAsState(
        targetValue = boxHeight * (1 - percentage - 0.04f), // Tính giá trị y cho sóng theo percentage
        animationSpec = tween(durationMillis = 500, easing = LinearEasing)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                BorderStroke(2.dp, borderColor),
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .clip(RoundedCornerShape(8.dp))
            .background(color = Color.White)
            .onSizeChanged { size ->
                // Lưu chiều cao của Box khi thay đổi kích thước
                boxHeight = size.height.toFloat()
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val waveHeight = size.height * 0.04f // Giảm waveHeight để sóng nhỏ hơn
            val waveLength = size.width * 0.8f // Giảm waveLength để sóng dày hơn
            val waveY = waveYOffset // Vị trí sóng thay đổi theo hiệu ứng từ animateFloatAsState
            val offset = waveOffset.value * waveLength // Di chuyển sóng theo phương ngang

            // Vẽ sóng uốn lượn
            val wavePath = Path().apply {
                moveTo(0f, waveY)
                for (x in 0..size.width.toInt() step 20) {
                    val y = waveY + waveHeight * sin(2.0f * PI.toFloat() * (x.toFloat() + offset) / waveLength)
                    lineTo(x.toFloat(), y)
                }
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }

            // Vẽ nền
            drawRect(
                color = bgItemColor,
                size = size
            )

            // Vẽ path sóng
            drawPath(
                path = wavePath,
                color = Color(0xFFB3E5FC) // Màu xanh nhạt cho nước
            )
        }

        // Hiển thị icon và tên danh mục
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.align(Alignment.Center)
        ) {
            // Icon
            IconButton(
                onClick = { onClick() },
                colors = IconButtonDefaults.iconButtonColors(contentColor = category.iconColor),
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 2.dp)
                    .size(32.dp)
            ) {
                androidx.compose.material3.Icon(painter = category.iconPainter(), contentDescription = category.name)
            }

            // Tên danh mục
            androidx.compose.material3.Text(
                category.name,
                color = Color.Gray,
                fontWeight = FontWeight.W500,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun MyButtonComponent(value: String, onClick: () -> Unit)
{
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = colorPrimary),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(top = 16.dp),
    ) {
        Text(
            value,
            color = Color.White,
            fontFamily = monsterrat,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp)
    }
}

@Composable
fun ClickableTextComponent(value: String, onClick: () -> Unit) {
    Text(
        value,
        color = colorSecondary,
        fontFamily = com.example.jetpackcompose.components.monsterrat,
        fontWeight = FontWeight.Light,
        fontSize = 8.sp,
        modifier = Modifier
            .padding(top = 5.dp)
            .clickable(onClick = onClick)
    )
}


@Composable
fun NumberTextField(amountState: String, onValueChange: (String) -> Unit) {
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
                val filteredInput = newInput.filter { it.isDigit() }
                onValueChange(
                    if (filteredInput.isNotEmpty() && filteredInput != "0") {
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
                    Text(
                        if (isFocused) "" else "0",
                        color = Color.Black,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteTextField(textState: TextFieldValue, onValueChange: (TextFieldValue) -> Unit)
{
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var textNote by remember { mutableStateOf("") }
    androidx.compose.material3.OutlinedTextField(
        modifier = Modifier
            .width(250.dp),
        value = textState,
        onValueChange = onValueChange,
        colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorPrimary,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = colorPrimary
        ),
        placeholder = { Text(
            "Chưa nhập vào",
            color = Color.LightGray,
            fontFamily = monsterrat,
        ) },
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontFamily = monsterrat,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                textNote = textState.toString()
                focusManager.clearFocus()  // Clear focus from the text field
                keyboardController?.hide()  // Hide the keyboard
            }
        ),
    )
}

@SuppressLint("DiscouragedApi")
@Composable
fun MonthPickerButton(onDateSelected: (String) -> Unit) {
    var dateText by remember { mutableStateOf("") }
    var showMonthPicker by remember { mutableStateOf(false) }
    val calendar = remember { mutableStateOf(Calendar.getInstance()) }

    val monthYearFormat = SimpleDateFormat("MM/yyyy", Locale("vi", "VN"))

    fun updateDateText() {
        dateText = monthYearFormat.format(calendar.value.time)
        onDateSelected(dateText)
    }

    LaunchedEffect(key1 = Unit) {
        updateDateText()
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(
            onClick = {
                calendar.value.add(Calendar.MONTH, -1)
                updateDateText()
            },
            modifier = Modifier
                .weight(1f)
                .size(20.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_arrow_back_ios_24),
                modifier = Modifier.size(16.dp),
                contentDescription = "Previous Month",
                tint = Color(0xFF444444))
        }
        Button(
            onClick = { showMonthPicker = true },
            shape = componentShapes.medium,
            modifier = Modifier.weight(8f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFe1e1e1))
        ) {
            Text(
                dateText,
                fontWeight = FontWeight.Bold,
                fontFamily = monsterrat,
                fontSize = 18.sp,
                color = Color(0xFF444444)
            )
        }
        IconButton(
            onClick = {
                calendar.value.add(Calendar.MONTH, +1)
                updateDateText()
            },
            modifier = Modifier
                .weight(1f)
                .size(20.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_arrow_forward_ios_24),
                contentDescription = "Next Month",
                modifier = Modifier.size(16.dp),
                tint = Color(0xFF444444)
            )
        }
    }

    if (showMonthPicker) {
        MonthPickerDialog(
            currentYear = calendar.value.get(Calendar.YEAR),
            currentMonth = calendar.value.get(Calendar.MONTH),
            onDismiss = { showMonthPicker = false },
            onMonthYearSelected = { selectedMonth, selectedYear ->
                calendar.value.set(Calendar.YEAR, selectedYear)
                calendar.value.set(Calendar.MONTH, selectedMonth)
                updateDateText()
                showMonthPicker = false
            }
        )
    }
}



@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CustomCalendar(
    selectedMonthYear: String,
    transactionList: List<DailyTransaction> // Nhận transactionList từ bên ngoài
) {
    val calendar = Calendar.getInstance()
    val currencyFormatter = remember {
        val symbols = DecimalFormatSymbols(Locale("vi", "VN"))
        symbols.decimalSeparator = '.'
        symbols.groupingSeparator = ','
        val format = DecimalFormat("#,###", symbols)
        format
    }

    // Tách tháng và năm từ chuỗi truyền vào
    val parts = selectedMonthYear.split("/")
    val month = parts[0].toInt() - 1 // Tháng trong Calendar là từ 0 đến 11
    val year = parts[1].toInt()

    // Thiết lập tháng và năm cho Calendar
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    // Tính toán số ngày trong tháng và các ngày cần hiển thị
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7 // Điều chỉnh ngày đầu tuần để bắt đầu từ Thứ Hai

    // Tính toán số ngày trong tháng sau
    calendar.add(Calendar.MONTH, 1)
    val daysInNextMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    calendar.add(Calendar.MONTH, -1) // Quay lại tháng hiện tại

    val days = mutableListOf<String>()
    val leadingEmptyDays = firstDayOfWeek // Số ngày trống trước khi bắt đầu tháng
    for (i in 1..leadingEmptyDays) {
        days.add("") // Thêm ô trống cho các ngày của tháng trước
    }

    // Thêm ngày trong tháng hiện tại
    for (i in 1..daysInMonth) {
        days.add(i.toString())
    }

    // Thêm ngày của tháng sau vào cuối lịch
    val trailingEmptyDays = (7 - (days.size % 7)) % 7 // Tính số ô trống cần thêm sau ngày cuối của tháng hiện tại
    for (i in 1..trailingEmptyDays) {
        days.add("") // Thêm ô trống cho các ngày của tháng sau
    }

    // Header cho lịch (Các ngày trong tuần)
    val daysOfWeek = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")

    // Tính số hàng của lịch (5 hoặc 6 hàng)
    val rows = days.chunked(7) // Chia thành các hàng, mỗi hàng 7 ngày
    val calendarHeight = if (rows.size == 6) 230.dp else 200.dp // Nếu có 6 hàng, chiều cao là 230.dp

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(calendarHeight)
            .background(Color.White)
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Hàng các ngày trong tuần
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                daysOfWeek.forEach { day ->
                    Box(
                        modifier = Modifier
                            .weight(1f) // Mỗi ngày trong tuần có cùng trọng số
                            .height(10.dp)
                            .background(Color(0xFFe1e1e1))
                            .border(0.25.dp, Color(0xFFd4d4d4)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day,
                            fontWeight = FontWeight.Normal,
                            fontSize = 8.sp,
                            fontFamily = monsterrat,
                            color = if (day == "CN") SundayColor else if (day == "T7") SaturDayColor else textColor,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Lưới ngày
            rows.forEachIndexed { rowIndex, week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    week.forEachIndexed { columnIndex, day ->
                        Box(
                            modifier = Modifier
                                .weight(1f) // Mỗi ô ngày có cùng trọng số
                                .height(35.dp)
                                .background(
                                    when {
                                        day.isEmpty() -> Color(0xfff1f1f1) // Màu nền cho ngày của tháng trước và tháng sau
                                        else -> Color.White // Nền trắng nếu là ngày của tháng hiện tại
                                    }
                                )
                                .border(0.25.dp, Color(0xFFd4d4d4)),
                            contentAlignment = Alignment.TopStart
                        ) {
                            Column(
                                modifier = Modifier.padding(4.dp)
                            ) {
                                if (day.isNotEmpty()) {
                                    // Hiển thị ngày
                                    Text(
                                        text = day,
                                        color = when {
                                            columnIndex == 6 -> SundayColor // Chủ nhật
                                            columnIndex == 5 -> SaturDayColor // Thứ 7
                                            else -> Color.Black // Các ngày trong tuần
                                        },
                                        fontFamily = monsterrat,
                                        fontSize = 8.sp,
                                        textAlign = TextAlign.Start
                                    )

                                    // Chuyển ngày thành định dạng yyyy-MM-dd để so sánh
                                    val formattedDay = String.format("%02d", day.toInt())
                                    val transactionDate = "$year-${month + 1}-$formattedDay"

                                    // Kiểm tra xem ngày có giao dịch không và hiển thị amountIncome và amountExpense
                                    val transaction = transactionList.find { it.date == transactionDate }
                                    transaction?.let {
                                        // Hiển thị amountIncome nếu không bằng 0
                                        if (it.amountIncome > 0) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End // Căn lề phải
                                            ) {
                                                Text(
                                                    text = "${currencyFormatter.format(it.amountIncome)}",
                                                    color = Color(0xff37c8ec),
                                                    fontSize = 7.sp,
                                                    textAlign = TextAlign.End,
                                                    fontFamily = monsterrat
                                                )
                                            }
                                        }
                                        // Hiển thị amountExpense nếu không bằng 0
                                        if (it.amountExpense > 0) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End // Căn lề phải
                                            ) {
                                                Text(
                                                    text = "${currencyFormatter.format(it.amountExpense)}",
                                                    color = colorPrimary,
                                                    fontSize = 7.sp,
                                                    textAlign = TextAlign.End,
                                                    fontFamily = monsterrat
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}















@Preview
@Composable
fun PreviewInputScreen() {
    CalendarScreen()
}






