package com.example.jetpackcompose.components

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.inputFeatures.Category
import com.example.jetpackcompose.app.network.TransactionResponse
import com.example.jetpackcompose.ui.theme.colorPrimary
import com.example.jetpackcompose.ui.theme.componentShapes
import com.example.jetpackcompose.ui.theme.errorColor
import com.example.jetpackcompose.ui.theme.highGray
import com.example.jetpackcompose.ui.theme.successColor
import com.example.jetpackcompose.ui.theme.textColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
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
                navController.popBackStack(
                    "inputfixedtab",
                    inclusive = true
                ) // Navigate to AnualScreen
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
                        animationSpec = tween(500), label = ""
                    )
                    val textColor by animateColorAsState(
                        targetValue = if (isSelected) Color.White else inactiveTextColor,
                        animationSpec = tween(durationMillis = 500), label = ""
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
        fontFamily = montserrat,
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
fun CategoryIconWithName(
    categoryName: String,
    transactionNote: String,
    transactionAmount: Long,
    transactionType: String
) {
    val currencyFormatter = remember {
        val symbols = DecimalFormatSymbols(Locale("vi", "VN"))
        symbols.decimalSeparator = '.'
        symbols.groupingSeparator = ','

        val format = DecimalFormat("#,###", symbols)
        format
    }

    val amountText = buildAnnotatedString {
        append("${currencyFormatter.format(transactionAmount)}")
        withStyle(style = SpanStyle(fontSize = 10.sp)) {  // Kích thước nhỏ hơn cho ký tự "₫"
            append("₫")
        }
    }

    // Danh sách các Category
    val categories = listOf(
        Category(
            1,
            "Chi phí nhà ở",
            { painterResource(R.drawable.outline_home_work_24) },
            Color(0xFFfb791d),
            1.00f
        ),
        Category(
            2,
            "Ăn uống",
            { painterResource(R.drawable.outline_ramen_dining_24) },
            Color(0xFF37c166),
            1.00f
        ),
        Category(
            3,
            "Mua sắm quần áo",
            { painterResource(R.drawable.clothes) },
            Color(0xFF283eaa),
            1.00f
        ),
        Category(
            4,
            "Đi lại",
            { painterResource(R.drawable.outline_train_24) },
            Color(0xFFa06749),
            1.00f
        ),
        Category(
            5,
            "Chăm sóc sắc đẹp",
            { painterResource(R.drawable.outline_cosmetic) },
            Color(0xFFf95aa9),
            1.00f
        ),
        Category(
            6,
            "Giao lưu",
            { painterResource(R.drawable.entertainment) },
            Color(0xFF6a1b9a),
            1.00f
        ),
        Category(
            7,
            "Y tế",
            { painterResource(R.drawable.outline_health_and_safety_24) },
            Color(0xFFfc3d39),
            1.00f
        ),
        Category(
            8,
            "Học tập",
            { painterResource(R.drawable.outline_education) },
            Color(0xFFfc7c1f),
            1.00f
        ),
        Category(
            10,
            "Tiền lương",
            { painterResource(R.drawable.salary) },
            Color(0xFFfb791d),
            1.00f
        ),
        Category(
            11,
            "Tiền thưởng",
            { painterResource(R.drawable.baseline_card_giftcard_24) },
            Color(0xFF37c166),
            1.00f
        ),
        Category(
            12,
            "Thu nhập phụ",
            { painterResource(R.drawable.secondary) },
            Color(0xFFf95aa9),
            1.00f
        ),
        Category(11, "Trợ cấp", { painterResource(R.drawable.subsidy) }, Color(0xFFfba74a), 1.00f)
    )

    // Tìm Category phù hợp với categoryName
    val category = categories.find { it.name == categoryName }

    // Nếu tìm thấy Category, hiển thị icon và tên
    category?.let {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Hiển thị icon
            Icon(
                painter = it.iconPainter(),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp) // Điều chỉnh kích thước icon
                    .padding(end = 8.dp),
                tint = it.iconColor // Sử dụng màu sắc của Category
            )

            // Hiển thị tên danh mục
            Text(
                text = it.name,
                fontFamily = montserrat,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF444444)
            )
            Spacer(modifier = Modifier.width(8.dp))
            if (transactionNote.isNotEmpty()) {
                Text(
                    text = "(${transactionNote})",
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    color = Color(0xFF444444),
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
            Text(
                text = amountText,
                fontFamily = montserrat,
                fontWeight = FontWeight.Bold,
                color = if (transactionType == "expense") textColor else Color(0xff37c8ec),
                textAlign = TextAlign.End
            )

            // Mũi tên ở cuối Row
            Icon(
                painter = painterResource(id = R.drawable.outline_arrow_forward_ios_24),
                contentDescription = "Arrow Right",
                modifier = Modifier
                    .size(16.dp)
                    .padding(start = 8.dp),
                tint = Color(0xFF444444)
            )
        }
    }
}


@Composable
fun DayIndex(
    dateTransactionList: Map<String, List<TransactionResponse.TransactionDetail>>,
    selectedDate: String = "",
    navController: NavController
) {
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale("vi", "VN")) }
    val displayDateFormat = remember { SimpleDateFormat("dd/MM/yyyy (E)", Locale("vi", "VN")) }

    val processedSelectedDate = if (selectedDate.isNotEmpty()) {
        val dateParts = selectedDate.split("-")
        if (dateParts.size == 3 && dateParts[2].startsWith("0")) {
            "${dateParts[0]}-${dateParts[1]}-${dateParts[2].drop(1)}"
        } else {
            selectedDate
        }
    } else {
        selectedDate
    }

    dateTransactionList.forEach { (date, transactions) ->
        if (processedSelectedDate.isEmpty() || date == processedSelectedDate) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                val formattedDate = try {
                    val dateParsed = dateFormat.parse(date)
                    displayDateFormat.format(dateParsed)
                } catch (e: Exception) {
                    date // Nếu có lỗi thì hiển thị ngày gốc
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0xfff1f1f1))
                        .height(35.dp)
                ) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = formattedDate,
                        fontFamily = montserrat,
                        color = Color(0xFF444444),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                Divider(
                    color = highGray,
                    thickness = 0.7.dp
                )

                transactions.forEach { transaction ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .height(50.dp)
                            .padding(horizontal = 16.dp)
                            .clickable {
                                val id = transaction.transaction_id
                                if (transaction.type == "expense") {
                                    navController.navigate("editExpense/$id")
                                }
                                else {
                                    navController.navigate("editIncome/$id")
                                }
                            }
                    ) {
                        Log.d("DayIndex", "Transaction Amount: ${transaction.amount}")
                        Log.d("DayIndex", "Transaction Date: ${transaction.transactionDate}")
                        Log.d("DayIndex", "Transaction ID: ${transaction.transaction_id}")
                        transaction.transaction_id?.let { id ->
                            transaction.note?.let {
                                transaction.type?.let { it1 ->
                                    CategoryIconWithName(
                                        categoryName = transaction.categoryName,
                                        transactionNote = it,
                                        transactionAmount = transaction.amount,
                                        transactionType = it1
                                    )
                                }
                            }
                        }
                    }

                    Divider(
                        color = highGray,
                        thickness = 0.7.dp
                    )
                }
            }
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
            fontFamily = montserrat,
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
                    fontFamily = montserrat,
                )
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontFamily = montserrat,
                fontWeight = FontWeight.Normal,
                color = textColor
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
            fontFamily = montserrat,
            color = textColor,
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
                fontFamily = montserrat
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
            title = {
                Text(
                    text = "Chọn $label",
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold
                )
            },
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
                            Text(
                                text = option.second,
                                fontWeight = FontWeight.Normal,
                                fontFamily = montserrat
                            )
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
            fontFamily = montserrat,
            color = textColor,
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
                text = when (selectedOption.toString()) {
                    "daily" -> "Hàng ngày"
                    "weekly" -> "Hàng tuần"
                    "monthly" -> "Hàng tháng"
                    "yearly" -> "Hàng năm"
                    else -> ""
                }, // Hiển thị tên enum dưới dạng chuỗi
                fontWeight = FontWeight.Normal,
                fontFamily = montserrat
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
            title = {
                Text(
                    text = "Chọn ${label.toLowerCase()}",
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold
                )
            },
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
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = when (option.second.toString()) {
                                    "daily" -> "Hàng ngày"
                                    "weekly" -> "Hàng tuần"
                                    "monthly" -> "Hàng tháng"
                                    "yearly" -> "Hàng năm"
                                    else -> ""
                                },
                                fontFamily = montserrat,
                                fontWeight = FontWeight.Normal
                            ) // Hiển thị tên chuỗi (displayName) từ enum
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
    onDateSelected: (String) -> Unit // Callback để trả ngày được chọn dưới dạng chuỗi yyyy-MM-dd
) {
    // State để lưu trữ ngày được chọn
    var selectedDate by remember { mutableStateOf(initialDate) }
    val context = LocalContext.current

    // Sử dụng DateTimeFormatter để định dạng ngày theo yyyy-MM-dd
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Chuyển đổi ngày thành chuỗi hiển thị
    val formattedDate = remember(selectedDate) {
        selectedDate.format(dateFormatter)
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
            color = textColor,
            fontFamily = montserrat,
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
                            onDateSelected(newDate.format(dateFormatter)) // Trả về định dạng yyyy-MM-dd
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
                fontFamily = montserrat,
                textAlign = TextAlign.Center,
                color = textColor
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
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontFamily = montserrat,
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
                // Lọc chỉ cho phép nhập số
                val filteredText = newValue.text.filter { it.isDigit() }

                // Kiểm tra xem chuỗi có rỗng không hoặc không bắt đầu bằng '0' nếu không phải là số duy nhất
                if (filteredText.isEmpty() || (filteredText != "0" && filteredText.first() != '0')) {
                    // Nếu chuỗi không bắt đầu bằng 0 hoặc là 0 duy nhất, cập nhật giá trị
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
                    fontFamily = montserrat,
                )
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontFamily = montserrat,
                fontWeight = FontWeight.Normal,
                color = textColor
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


@RequiresApi(Build.VERSION_CODES.O)
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
    val displayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

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
            fontFamily = montserrat,
            color = textColor,
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
                fontFamily = montserrat
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
            title = {
                Text(
                    text = "Chọn ${label.toLowerCase()}",
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                LazyColumn {
                    items(options) { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedOption = option
                                    if (option == "Không") {
                                        selectedDate =
                                            null.toString() // Reset ngày nếu chọn "Không"
                                        onDateSelected("Không")
                                    } else {
                                        showDatePicker =
                                            true // Hiển thị DatePickerDialog nếu chọn "Ngày chỉ định"
                                    }
                                    showDialog = false
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = option,
                                fontWeight = FontWeight.Normal,
                                fontFamily = montserrat
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
                selectedDate =
                    SimpleDateFormat("yyyy-MM-dd", vietnamLocale).format(newDate) // Cập nhật ngày
                onDateSelected(selectedDate) // Gửi ngày đã chọn qua callback
                showDatePicker = false // Tắt trạng thái hiển thị DatePickerDialog
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setOnDismissListener {
            showDatePicker = false
        } // Đảm bảo tắt DatePickerDialog khi đóng
        datePickerDialog.show()
    }
}

@Composable
fun MessagePopup(
    showPopup: Boolean,
    successMessage: String,
    errorMessage: String,
    onDismiss: () -> Unit
) {
    if (showPopup) {
        LaunchedEffect(key1 = showPopup) {
            delay(800)
            onDismiss() //
        }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = null,
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth() // Chiếm đầy chiều rộng
                        .wrapContentHeight(), // Chiều cao tự động theo nội dung
                    horizontalAlignment = Alignment.CenterHorizontally, // Căn giữa theo chiều ngang
                    verticalArrangement = Arrangement.Center // Căn giữa theo chiều dọc
                ) {
                    if (errorMessage.isNotEmpty()) {
                        // Icon lỗi
//                        Icon(
//                            painter = painterResource(id = R.drawable.error), // Thay bằng icon lỗi của bạn
//                            contentDescription = "Error icon",
//                            tint = errorColor,
//                            modifier = Modifier.size(48.dp)
//                        )
                        Spacer(modifier = Modifier.height(8.dp)) // Khoảng cách giữa icon và message
                        Text(
                            text = errorMessage,
                            color = errorColor,
                            fontFamily = montserrat,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center // Căn giữa nội dung text
                        )
                    } else if (successMessage.isNotEmpty()) {
                        // Icon thành công
//                        Icon(
//                            painter = painterResource(id = R.drawable.success), // Thay bằng icon thành công của bạn
//                            contentDescription = "Success icon",
//                            tint = successColor,
//                            modifier = Modifier.size(48.dp)
//                        )
                        Spacer(modifier = Modifier.height(8.dp)) // Khoảng cách giữa icon và message
                        Text(
                            text = successMessage,
                            color = successColor,
                            fontFamily = montserrat,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center // Căn giữa nội dung text
                        )
                    }
                }
            },
            buttons = {},
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .heightIn(min = 70.dp)
                .fillMaxWidth(0.8f)
        )
    }
}

@Preview
@Composable
fun PreviewMessagePopup() {
    MessagePopup(
        showPopup = true,
        successMessage = "Gửi giao dịch thành công!",
        errorMessage = "",
        onDismiss = {}
    )
}












