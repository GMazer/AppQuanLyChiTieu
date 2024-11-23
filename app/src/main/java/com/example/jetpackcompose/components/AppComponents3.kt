package com.example.jetpackcompose.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcompose.app.features.fixedIncomeAndExpenses.InputFixedTab
import com.example.jetpackcompose.app.screens.DailyTransaction
import com.example.jetpackcompose.app.screens.anual_sceens.AnualScreen
import com.example.jetpackcompose.ui.theme.TextColor
import com.example.jetpackcompose.ui.theme.colorPrimary
import com.example.jetpackcompose.ui.theme.componentShapes
import com.example.jetpackcompose.ui.theme.highGray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun FixedTabRow(
    tabIndex: Int,
    onTabSelected: (Int) -> Unit,
    titles: List<String>,
    pagerStatement: PagerState,
    coroutineScoper: CoroutineScope,
    modifier: Modifier = Modifier
) {
    val inactiveColor = Color(0xFFe1e1e1)  // Màu cho tab không chọn
    val inactiveTextColor = Color(0xFFF35E17)  // Màu văn bản cho tab không chọn




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
                coroutineScoper.launch {
                    pagerStatement.scrollToPage(3)
                }
            }

            // Spacer để đẩy TabRow ra giữa
            Spacer(modifier = Modifier.weight(1f))

            // TabRow căn giữa
            TabRow(
                selectedTabIndex = tabIndex,
                modifier = Modifier
                    .background(Color.Transparent)
                    .width(200.dp),
                indicator = {}, // Không có chỉ báo
                divider = {} // Không có dòng phân cách
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
                                onTabSelected(index);
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

            // Spacer để giữ TabRow ở giữa
            Spacer(modifier = Modifier.weight(1f))
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
fun FixedExpense() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth() // Chiều rộng tối đa (có thể tùy chỉnh)
                .padding(16.dp) // Khoảng cách xung quanh Box
                .background(
                    color = Color.White, // Màu nền trắng
                    shape = RoundedCornerShape(8.dp) // Bo góc 8.dp
                )
        ) {
            Column() {
                RowTextField(
                    label = "Tiêu đề",
                    textState = TextFieldValue(""),
                    onValueChange = {}
                )
                Divider(
                    color = Color(0xFFd4d4d4), // Màu của đường chia tách
                    thickness = 0.5.dp // Độ dày của đường chia tách
                )
                RowTextField(
                    label = "Số tiền",
                    textState = TextFieldValue(""),
                    onValueChange = {}
                )
                Divider(
                    color = Color(0xFFd4d4d4), // Màu của đường chia tách
                    thickness = 0.5.dp // Độ dày của đường chia tách
                )
                DropdownRow(
                    label = "Danh mục",
                    options = listOf(
                        Pair(0, "Thiết yếu"),
                        Pair(0, "Giải trí"),
                        Pair(0,"Đầu tư"),
                        Pair(0, "Dự phòng"),
                    )
                ) { selectedCategory ->
                    // Xử lý khi danh mục được chọn thay đổi
                }
            }
        }
    }
}

@Composable
fun FixedIncome() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Fixed Income",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorPrimary
        )
    }
}


@Composable
fun DayIndex(DailyTransactions: List<DailyTransaction>) {
    for (transaction in DailyTransactions) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
                horizontalArrangement = Arrangement.Start, // Căn trái theo chiều ngang
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.LightGray)
                    .height(40.dp)
            ) {
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "${transaction.date}",
                    fontFamily = monsterrat,
                    color = Color(0xFF444444),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Divider(
                color = highGray,
                thickness = 1.dp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
                horizontalArrangement = Arrangement.Start, // Căn trái theo chiều ngang
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .height(64.dp)
            ) {
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = "Tiền thu:",
                    fontFamily = monsterrat,
                    color = Color(0xFF444444),
                    modifier = Modifier
                        .width(64.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${transaction.amountIncome}",
                    fontFamily = monsterrat,
                    color = Color(0xFF444444)
                )
            }

            Divider(
                color = highGray,
                thickness = 1.dp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
                horizontalArrangement = Arrangement.Start, // Căn trái theo chiều ngang
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .height(64.dp)
            ) {
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = "Tiền chi:",
                    fontFamily = monsterrat,
                    color = Color(0xFF444444),
                    modifier = Modifier
                        .width(64.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${transaction.amountExpense}",
                    fontFamily = monsterrat,
                    color = Color(0xFF444444)
                )
            }

            Divider(
                color = highGray,
                thickness = 1.dp
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
                cursorColor = colorPrimary
            ),
            placeholder = { Text(
                "Chưa nhập",
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
}

@Composable
fun DropdownRow(
    label: String, // Label của hàng
    options: List<Pair<Int, String>>, // Danh sách lựa chọn (Icon và Text)
    onChangeValue: (String) -> Unit // Callback khi giá trị được chọn thay đổi
) {
    // State để lưu giá trị được chọn
    val (selectedOption, setSelectedOption) = remember { mutableStateOf(options[0].second) }
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }

    // Thành phần chính
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Label
        Text(
            text = label,
            fontWeight = FontWeight.Normal,
            fontFamily = monsterrat,
            modifier = Modifier.weight(1f) // Chiếm không gian bên trái
        )

        // Nút hiển thị danh sách
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(2f)
                .clickable { setShowDialog(true) }
        ) {
            // Icon của danh mục (nếu có)
            Icon(
                painter = painterResource(id = options.first { it.second == selectedOption }.first),
                contentDescription = null,
                tint = Color.Green, // Màu sắc của icon
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp)) // Khoảng cách giữa Icon và Text

            // Text hiển thị danh mục được chọn
            Text(
                text = selectedOption,
                fontWeight = FontWeight.Normal,
                fontFamily = monsterrat,
                color = TextColor
            )
        }

        // Mũi tên
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }

    // Dialog trượt
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { setShowDialog(false) },
            title = { Text(text = "Chọn $label") },
            text = {
                LazyColumn {
                    items(options) { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    setSelectedOption(option.second)
                                    onChangeValue(option.second) // Trả giá trị về qua callback
                                    setShowDialog(false)
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = option.first),
                                contentDescription = null,
                                tint = Color.Green,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = option.second)
                        }
                    }
                }
            },
            buttons = {}
        )
    }
}

@Preview
@Composable
fun PreviewRowTextField() {
    RowTextField(
        label = "Ghi chú",
        textState = TextFieldValue(""),
        onValueChange = {}
    )
}