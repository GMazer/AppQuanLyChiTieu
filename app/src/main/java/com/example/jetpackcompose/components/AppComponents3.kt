package com.example.jetpackcompose.components

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jetpackcompose.app.screens.DailyTransaction
import com.example.jetpackcompose.ui.theme.TextColor
import com.example.jetpackcompose.ui.theme.colorPrimary
import com.example.jetpackcompose.ui.theme.componentShapes
import com.example.jetpackcompose.ui.theme.highGray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun FixedTabRow(
    tabIndex: Int,
    onTabSelected: (Int) -> Unit,
    titles: List<String>,
    coroutineScoper: CoroutineScope,
    pagerStatement: PagerState,
    navController: NavHostController, // Add NavHostController parameter
    modifier: Modifier = Modifier
) {
    val inactiveColor = Color(0xFFe1e1e1)
    val inactiveTextColor = Color(0xFFF35E17)

    Column {
        Row(
            modifier = modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(color = Color.Transparent),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Nút "Trở lại" ở ngoài cùng bên trái
            ClickableText("Trở lại") {
                navController.popBackStack("inputfixedtab", inclusive = true) // Navigate to AnualScreen
                navController.navigate("anual") // Navigate to AnualScreen
            }

            Spacer(modifier = Modifier.weight(1f))

            TabRow(
                selectedTabIndex = tabIndex,
                modifier = Modifier
                    .background(Color.Transparent)
                    .width(200.dp),
                indicator = {},
                divider = {}
            ) {
                titles.forEachIndexed { index, title ->
                    val isSelected = tabIndex == index
                    val tabColor by animateColorAsState(
                        if (isSelected) colorPrimary else inactiveColor,
                        animationSpec = tween(500)
                    )
                    val textColor by animateColorAsState(
                        targetValue = if (isSelected) Color.White else inactiveTextColor,
                        animationSpec = tween(durationMillis = 500)
                    )
                    val shape = when (index) {
                        0 -> RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                        titles.lastIndex -> RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                        else -> RoundedCornerShape(8.dp)
                    }
                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .height(36.dp)
                            .background(
                                color = inactiveColor,
                                shape = shape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Tab(
                            modifier = if (isSelected) Modifier
                                .width(100.dp)
                                .height(32.dp)
                                .padding(horizontal = 2.dp)
                                .background(tabColor, shape = componentShapes.medium)
                            else Modifier.width(100.dp),
                            selected = isSelected,
                            onClick = {
                                onTabSelected(index)
                                coroutineScoper.launch {
                                    pagerStatement.scrollToPage(index)
                                }
                            },
                            text = {
                                Text(
                                    title,
                                    color = textColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Nút "Lưu"
            ClickableText("  ") {
            //
            }

        }
    }
    Divider(
        color = Color.LightGray,
        thickness = 1.dp
    )
}


@Composable
fun ClickableText(
    text: String,
    onBack: () -> Unit
) {
    Text(
        text = text,
        fontFamily = monsterrat,
        color = colorPrimary,
        fontSize = 12.sp,
        textAlign = TextAlign.Start,
        fontWeight = FontWeight.Normal,
        modifier = Modifier
            .clickable {
                onBack() // Gọi hàm xử lý khi text được nhấn
            }
            .padding(8.dp), // Tùy chỉnh khoảng cách xung quanh text
    )
}


@Composable
fun DayIndex(DailyTransactions: List<DailyTransaction>) {

    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale("vi", "VN")) }
    val displayDateFormat = remember { SimpleDateFormat("dd/MM/yyyy (E)", Locale("vi", "VN")) }
    val currencyFormatter = remember {
        // Lấy DecimalFormatSymbols mặc định cho Việt Nam
        val symbols = DecimalFormatSymbols(Locale("vi", "VN"))

        // Thay đổi dấu phân cách thập phân và phân cách hàng nghìn
        symbols.decimalSeparator = '.'
        symbols.groupingSeparator = ','

        // Tạo một DecimalFormat mới sử dụng các biểu tượng đã thay đổi
        val format = DecimalFormat("#,###", symbols)

        format
    }

    for (transaction in DailyTransactions) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val formattedDate = try {
                val date = dateFormat.parse(transaction.date)
                displayDateFormat.format(date)
            } catch (e: Exception) {
                transaction.date
            }

            Row(
                verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
                horizontalArrangement = Arrangement.Start, // Căn trái theo chiều ngang
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xfff1f1f1))
                    .height(35.dp)
            ) {
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = formattedDate,
                    fontFamily = monsterrat,
                    color = Color(0xFF444444),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Divider(
                color = highGray,
                thickness = 0.7.dp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
                horizontalArrangement = Arrangement.Start, // Căn trái theo chiều ngang
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .height(50.dp)
            ) {
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = "Tiền thu:",
                    fontFamily = monsterrat,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF444444),
                    modifier = Modifier
                        .width(64.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${currencyFormatter.format(transaction.amountIncome)}₫",
                    fontFamily = monsterrat,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF444444)
                )
            }

            Divider(
                color = highGray,
                thickness = 0.7.dp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
                horizontalArrangement = Arrangement.Start, // Căn trái theo chiều ngang
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .height(50.dp)
            ) {
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = "Tiền chi:",
                    fontFamily = monsterrat,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF444444),
                    modifier = Modifier
                        .width(64.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${currencyFormatter.format(transaction.amountExpense)}₫",
                    fontFamily = monsterrat,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF444444)
                )
            }

            Divider(
                color = highGray,
                thickness = 0.7.dp
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowTextField(
    label: String,
    textState: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    Row(
        modifier = Modifier.height(60.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color.DarkGray,
            fontWeight = FontWeight.Bold,
            fontFamily = monsterrat,
            modifier = Modifier
                .weight(1.5f)
                .padding(start = 16.dp)
        )
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        var textNote by remember { mutableStateOf("") }
        androidx.compose.material3.OutlinedTextField(
            modifier = Modifier
                .weight(3.5f),
            value = textState,
            onValueChange = onValueChange,
            colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                cursorColor = colorPrimary
            ),
            placeholder = {
                Text(
                    "Chưa nhập",
                    color = Color.LightGray,
                    fontFamily = monsterrat,
                )
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontFamily = monsterrat,
                fontWeight = FontWeight.Normal,
                color = TextColor
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
}

@Composable
fun DropdownRow(
    label: String,
    options: List<Pair<Int?, String>>, // Icon (Int?) và tên danh mục (String)
    onChangeValue: (String) -> Unit // Callback chỉ trả về String
) {
    var selectedOption by remember { mutableStateOf(options[0].second) }
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Label
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontFamily = monsterrat,
            color = TextColor,
            modifier = Modifier.weight(4f)
        )

        // Nội dung chính
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(6f)
                .clickable { showDialog = true }
        ) {
            Text(
                text = selectedOption,
                fontWeight = FontWeight.Normal,
                fontFamily = monsterrat
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Icon mũi tên
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Chọn $label") },
            text = {
                LazyColumn {
                    items(options) { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedOption = option.second
                                    onChangeValue(option.second) // Trả về String
                                    showDialog = false
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            option.first?.let { iconId ->
                                Icon(
                                    painter = painterResource(id = iconId),
                                    contentDescription = null,
                                    tint = Color(0xff4caf50),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(text = option.second)
                        }
                    }
                }
            },
            buttons = {}
        )
    }
}

@Composable
fun <T : Enum<T>> DropdownRepeat(
    label: String,
    options: List<Pair<String, T>>, // Truyền vào danh sách các giá trị enum dưới dạng cặp (displayName, enumValue)
    onChangeValue: (T) -> Unit // Trả về enum thay vì String
) {
    var selectedOption by remember { mutableStateOf(options[0].second) } // Lưu enum thay vì String
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Label
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontFamily = monsterrat,
            color = TextColor,
            modifier = Modifier.weight(4f)
        )

        // Nội dung chính
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(6f)
                .clickable { showDialog = true }
        ) {
            Text(
                text = selectedOption.toString(), // Hiển thị tên enum dưới dạng chuỗi
                fontWeight = FontWeight.Normal,
                fontFamily = monsterrat
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Icon mũi tên
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Chọn $label") },
            text = {
                LazyColumn {
                    items(options) { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedOption = option.second
                                    onChangeValue(option.second) // Trả về enum thay vì String
                                    showDialog = false
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = option.first) // Hiển thị tên chuỗi (displayName) từ enum
                        }
                    }
                }
            },
            buttons = {}
        )
    }
}



@SuppressLint("NewApi")
@Composable
fun DatePickerRow(
    label: String, // Label của hàng
    initialDate: LocalDate = LocalDate.now(), // Ngày ban đầu, mặc định là ngày hiện tại
    onDateSelected: (LocalDate) -> Unit // Callback để trả ngày được chọn
) {
    // State để lưu trữ ngày được chọn
    var selectedDate by remember { mutableStateOf(initialDate) }
    val context = LocalContext.current

    // Sử dụng SimpleDateFormat với Locale Việt Nam
    val vietnamLocale = Locale("vi", "VN")
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", vietnamLocale)

    // Chuyển đổi ngày thành chuỗi hiển thị
    val formattedDate = remember(selectedDate) {
        dateFormat.format(
            Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        )
    }

    // Row hiển thị
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Label
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = TextColor,
            fontFamily = monsterrat,
            modifier = Modifier.weight(3f)
        )

        // Nội dung hiển thị ngày tháng
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(4f)
                .clickable {
                    // Hiển thị DatePickerDialog
                    val datePicker = DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            // Cập nhật ngày khi người dùng chọn
                            val newDate = LocalDate.of(year, month + 1, dayOfMonth)
                            selectedDate = newDate
                            onDateSelected(newDate) // Trả ngày được chọn qua callback
                        },
                        selectedDate.year,
                        selectedDate.monthValue - 1,
                        selectedDate.dayOfMonth
                    )
                    datePicker.show()
                }
        ) {
            Text(
                text = formattedDate,
                fontWeight = FontWeight.Normal,
                fontFamily = monsterrat,
                textAlign = TextAlign.Center,
                color = TextColor
            )
        }

        // Spacer để đẩy mũi tên sát cạnh phải
        Spacer(modifier = Modifier.weight(1f))

        // Mũi tên luôn nằm sát ngoài cùng bên phải
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowNumberField(
    label: String = "Số tiền", // Label mặc định
    textState: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    Row(
        modifier = Modifier.height(60.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = TextColor,
            fontWeight = FontWeight.Bold,
            fontFamily = monsterrat,
            modifier = Modifier
                .weight(1.5f)
                .padding(start = 16.dp)
        )
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        androidx.compose.material3.OutlinedTextField(
            modifier = Modifier
                .weight(3.5f),
            value = textState,
            onValueChange = { newValue ->
                // Lọc chỉ cho phép nhập số và xử lý vị trí con trỏ
                val filteredText = newValue.text.filter { it.isDigit() }

                // Nếu ô đang rỗng, không cho phép số 0 làm ký tự đầu tiên
                if (filteredText.isNotEmpty() || (filteredText == "0" && textState.text.isNotEmpty())) {
                    onValueChange(
                        TextFieldValue(
                            text = filteredText,
                            selection = TextRange(filteredText.length) // Đặt con trỏ ở cuối
                        )
                    )
                }
            },
            colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                cursorColor = colorPrimary
            ),
            placeholder = {
                Text(
                    "Nhập số tiền",
                    color = Color.LightGray,
                    fontFamily = monsterrat,
                )
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontFamily = monsterrat,
                fontWeight = FontWeight.Normal,
                color = TextColor
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, // Chỉ cho phép nhập số
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()  // Clear focus from the text field
                    keyboardController?.hide()  // Hide the keyboard
                }
            ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndDateRow(
    label: String = "Kết thúc",
    onDateSelected: (String) -> Unit // Callback trả về giá trị (Không hoặc ngày được chọn)
) {
    val options = listOf("Không", "Ngày chỉ định") // Các tùy chọn
    var selectedOption by remember { mutableStateOf(options[0]) } // Lựa chọn mặc định là "Không"
    var selectedDate by remember { mutableStateOf("") } // Ngày được chọn
    var showDialog by remember { mutableStateOf(false) } // Hiển thị AlertDialog
    var showDatePicker by remember { mutableStateOf(false) } // Điều khiển hiển thị DatePickerDialog

    val vietnamLocale = Locale("vi", "VN") // Locale Việt Nam

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Label
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontFamily = monsterrat,
            color = TextColor,
            modifier = Modifier.weight(4f)
        )

        // Nội dung hiển thị lựa chọn
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(6f)
                .clickable { showDialog = true }
        ) {
            Text(
                text = if (selectedOption == "Ngày chỉ định" && selectedDate.isNotEmpty()) selectedDate else selectedOption,
                fontWeight = FontWeight.Normal,
                fontFamily = monsterrat
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Icon mũi tên
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }

    // AlertDialog cho lựa chọn
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Chọn $label") },
            text = {
                LazyColumn {
                    items(options) { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedOption = option
                                    if (option == "Không") {
                                        selectedDate = "" // Reset ngày nếu chọn "Không"
                                        onDateSelected("Không")
                                    } else {
                                        showDatePicker = true // Hiển thị DatePickerDialog nếu chọn "Ngày chỉ định"
                                    }
                                    showDialog = false
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = option,
                                fontWeight = FontWeight.Normal,
                                fontFamily = monsterrat
                            )
                        }
                    }
                }
            },
            buttons = {}
        )
    }

    // Hiển thị DatePickerDialog nếu chọn "Ngày chỉ định"
    if (showDatePicker) {
        val context = LocalContext.current
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val newDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }.time
                selectedDate = SimpleDateFormat("yyyy-MM-dd", vietnamLocale).format(newDate) // Cập nhật ngày
                onDateSelected(selectedDate) // Gửi ngày đã chọn qua callback
                showDatePicker = false // Tắt trạng thái hiển thị DatePickerDialog
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setOnDismissListener { showDatePicker = false } // Đảm bảo tắt DatePickerDialog khi đóng
        datePickerDialog.show()
    }
}





