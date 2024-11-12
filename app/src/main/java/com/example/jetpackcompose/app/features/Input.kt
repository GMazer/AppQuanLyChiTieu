package com.example.jetpackcompose.app.features

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackcompose.R
import com.example.jetpackcompose.components.BottomLine
import com.example.jetpackcompose.components.CategoriesGrid
import com.example.jetpackcompose.components.TabMoney
import com.example.jetpackcompose.navigation.CustomBottomAppBar
import com.example.jetpackcompose.ui.theme.TextColor
import com.example.jetpackcompose.ui.theme.TextColorPrimary
import com.example.jetpackcompose.ui.theme.colorPrimary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TabMoney()
        }
    }
}

data class Category(
    val name: String,
    val iconPainter: @Composable () -> Painter,
    val iconColor: Color
)


val monsterrat = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_bold, FontWeight.Bold),
    Font(R.font.montserrat_light, FontWeight.Light)
)

data class TabItem (val text: String, val icon: ImageVector, val screen: @Composable () -> Unit)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseContent () {
    val customTypography = Typography(
        bodyLarge = TextStyle(fontFamily = monsterrat),
        bodyMedium = TextStyle(fontFamily = monsterrat),
        bodySmall = TextStyle(fontFamily = monsterrat),
        titleLarge = TextStyle(fontFamily = monsterrat),
        titleMedium = TextStyle(fontFamily = monsterrat),
        titleSmall = TextStyle(fontFamily = monsterrat),
        labelLarge = TextStyle(fontFamily = monsterrat),
        labelMedium = TextStyle(fontFamily = monsterrat),
        labelSmall = TextStyle(fontFamily = monsterrat),
        headlineLarge = TextStyle(fontFamily = monsterrat),
        headlineMedium = TextStyle(fontFamily = monsterrat),
        headlineSmall = TextStyle(fontFamily = monsterrat)
    )
    var tabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Tiền chi", "Tiền thu")
    val categories = listOf(
        Category(
            "Lương",
            { painterResource(R.drawable.baseline_monetization_on_24) },
            Color(0xFFfb791d)
        ),
        Category(
            "Thưởng",
            { painterResource(R.drawable.baseline_card_giftcard_24) },
            Color(0xFF37c166)
        ),
        Category(
            "Cướp",
            { painterResource(R.drawable.baseline_person_off_24) },
            Color(0xFFf95aa9)
        ),
        Category(
            "Khác",
            { painterResource(R.drawable.baseline_more_horiz_24) },
            Color(0xFFfba74a)
        )
    )

    var textState by remember { mutableStateOf(TextFieldValue()) }
    val buttonColor = Color(0xFFF35E17) // Color for the buttons
    val textColor = Color(333333)
    val navColor = Color(0xFFF1F1F1)
    val activeColor = Color(0xFFF35E17)
    val inactiveColor = Color(0xFFe1e1e1)
    val backgroundColor = Color(0xFFF1F1F1)

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            CustomBottomAppBar()
        }
        //Bottom Navigation Bar
    ) { innerPadding ->

        // Đưa nội dung vào Column

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .background(Color.White)
        ) {

            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Ngày ", color = Color.DarkGray, fontWeight = FontWeight.Bold)
                //Gọi Nút Chọn ngày
                DatePickerButton()
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Ghi chú ", color = Color.DarkGray, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(16.dp))
                OutlinedTextField(
                    value = textState,
                    onValueChange = { textState = it },
                    label = { Text("Note", color = buttonColor) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = buttonColor,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Color.Black
                    ),
                    placeholder = { Text("Chưa nhập vào", color = Color.LightGray) }
                )
            }
            Spacer(Modifier.height(8.dp))
            // Nhap vao so tien
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Tiền thu ",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.width(16.dp))
                OutlinedTextField(
                    value = textState,
                    onValueChange = { textState = it },
                    label = { Text("Money", color = buttonColor) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = buttonColor,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Color.Black
                    ),
                    placeholder = { Text("0", color = Color.LightGray) }
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "₫",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Normal,
                )
            }
            Spacer(Modifier.height(24.dp))
            // Danh mục chi tiêu
            Text("Danh mục", color = Color.DarkGray, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(24.dp))

            CategoriesGrid(categories, buttonColor) // Gọi danh mục chi tiêu

            Spacer(Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
            ) {
                Button(
                    onClick = { /* Handle save */ },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text("Nhập khoản thu", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeContent () {
    val customTypography = Typography(
        bodyLarge = TextStyle(fontFamily = monsterrat),
        bodyMedium = TextStyle(fontFamily = monsterrat),
        bodySmall = TextStyle(fontFamily = monsterrat),
        titleLarge = TextStyle(fontFamily = monsterrat),
        titleMedium = TextStyle(fontFamily = monsterrat),
        titleSmall = TextStyle(fontFamily = monsterrat),
        labelLarge = TextStyle(fontFamily = monsterrat),
        labelMedium = TextStyle(fontFamily = monsterrat),
        labelSmall = TextStyle(fontFamily = monsterrat),
        headlineLarge = TextStyle(fontFamily = monsterrat),
        headlineMedium = TextStyle(fontFamily = monsterrat),
        headlineSmall = TextStyle(fontFamily = monsterrat)
    )
    var tabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Tiền chi", "Tiền thu")
    val categories = listOf(
        Category(
            "Ăn uống",
            { painterResource(R.drawable.outline_ramen_dining_24) },
            Color(0xFFfb791d)
        ),
        Category(
            "Chi tiêu hàng ngày",
            { painterResource(R.drawable.outline_grocery_24) },
            Color(0xFF37c166)
        ),
        Category(
            "Quần áo",
            { painterResource(R.drawable.outline_apparel_24) },
            Color(0xFF283eaa)),
        Category(
            "Mỹ phẩm",
            { painterResource(R.drawable.outline_cosmetic) },
            Color(0xFFf95aa9)
        ),
        Category(
            "Phí giao lưu",
            { painterResource(R.drawable.outline_liquor_24) },
            Color(0xFFfedc2e)
        ),
        Category(
            "Y tế",
            { painterResource(R.drawable.outline_health_and_safety_24) },
            Color(0xFF6ee1a4)
        ),
        Category(
            "Giáo dục",
            { painterResource(R.drawable.outline_education) },
            Color(0xFFed4f64)
        ),
        Category(
            "Tiền điện",
            { painterResource(R.drawable.outline_electric) },
            Color(0xFF55daf1)
        ),
        Category("Đi lại", { painterResource(R.drawable.outline_train_24) }, Color(0xFFae6d2a)),
        Category(
            "Phí liên lạc",
            { painterResource(R.drawable.outline_phone_iphone_24) },
            Color(0xFF696969)
        ),
        Category(
            "Tiền nhà",
            { painterResource(R.drawable.outline_home_work_24) },
            Color(0xFFfba74a)
        ),
        Category(
            "Khác",
            { painterResource(R.drawable.baseline_more_horiz_24) },
            Color(0xFFfba74a)
        )
    )

    var textState by remember { mutableStateOf(TextFieldValue()) }
    var textOutcome by remember { mutableStateOf(TextFieldValue("0")) }
    val buttonColor = Color(0xFFF35E17) // Color for the buttons
    val textColor = Color(333333)
    val navColor = Color(0xFFF1F1F1)
    val activeColor = Color(0xFFF35E17)
    val inactiveColor = Color(0xFFe1e1e1)
    val backgroundColor = Color(0xFFF1F1F1)

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            CustomBottomAppBar()
        }
        //Bottom Navigation Bar
    ) { innerPadding ->

        // Đưa nội dung vào Column

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .background(Color.White)
        ) {

            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Ngày ", color = Color.DarkGray, fontWeight = FontWeight.Bold)
                //Gọi Nút Chọn ngày
                DatePickerButton()
            }

            BottomLine(8.dp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Ghi chú ", color = Color.DarkGray, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                OutlinedTextField(
                    value = textState,
                    onValueChange = { textState = it },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = buttonColor,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Color.Black
                    ),
                    placeholder = { Text(
                        "Chưa nhập vào",
                        color = Color.LightGray,
                        fontSize = 8.sp
                    ) },
                    modifier = Modifier
                        .height(40.dp)
                        .weight(1f)
                )
            }
            BottomLine(8.dp)
            // Nhap vao so tien
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Tiền chi ",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.width(8.dp))
                OutlinedTextField(
                    value = textOutcome,
                    onValueChange = { textOutcome = it },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color(0xFFe1e1e1),
                        focusedBorderColor = colorPrimary,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = colorPrimary
                    ),
                    modifier = Modifier
                                .heightIn(max = 40.dp)
                                .weight(1f)
                                .padding(0.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "₫",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Normal,
                )
            }
            BottomLine(24.dp)
            // Danh mục chi tiêu
            Text("Danh mục", color = Color.DarkGray, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(24.dp))

            CategoriesGrid(categories, buttonColor) // Gọi danh mục chi tiêu

            Spacer(Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
            ) {
                Button(
                    onClick = { /* Handle save */ },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text("Nhập khoản chi", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

}
//DATE PICKER BUTTON
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerButton() {
    var dateText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var buttonHeight by remember { mutableStateOf(0.dp) }

    //Định dạng ngày tháng năm thứ
    val dateFormat = SimpleDateFormat("dd/MM/yyyy (E)", Locale("vi", "VN"))

    // Cập nhật ngày hiện tại khi khởi tạo
    LaunchedEffect(key1 = true) {
        dateText = dateFormat.format(calendar.time) // Định dạng ngày tháng năm
    }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Nút lùi lịch
        IconButton(
            onClick = {
                calendar.add(Calendar.DAY_OF_MONTH, -1) // Lùi 1 ngày
                dateText = dateFormat.format(calendar.time)
            },
            modifier = Modifier.size(20.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_arrow_back_ios_24),
                contentDescription = "Lùi lịch",
                tint = Color(0xFF444444)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        // Nút chọn ngày
        Button(
            modifier = Modifier
                .weight(1f),
            shape = RoundedCornerShape(5.dp),
            onClick = {
                // Xử lý sự kiện nhấn nút: mở DatePickerDialog
                val datePickerDialog = DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        // Cập nhật ngày khi chọn xong
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, month)
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        dateText = dateFormat.format(calendar.time)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                datePickerDialog.show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFe1e1e1))
        ) {
            Text(
                dateText,
                color = Color(0xFF444444),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        // Nút tiến lịch
        IconButton(
            onClick = {
                calendar.add(Calendar.DAY_OF_MONTH, +1) // Lùi 1 ngày
                dateText = dateFormat.format(calendar.time)
            },
            modifier = Modifier.size(20.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_arrow_forward_ios_24),
                contentDescription = "Lùi lịch",
                tint = Color(0xFF444444)
            )
        }
    }
}
// Tạo custom TabRow
@Composable
fun CustomTabRow(
    tabIndex: Int,
    onTabSelected: (Int) -> Unit,
    titles: List<String>,
    listtab: List<TabItem>,
    pagerStatement: PagerState,
    coroutineScoper: CoroutineScope,
    modifier: Modifier = Modifier
) {

    val backgroundColor = Color(0xFFf1f1f1)
    val activeColor = Color(0xFFF35E17)  // Màu cho tab đang chọn
    val inactiveColor = Color(0xFFe1e1e1)  // Màu cho tab không chọn
    val activeTextColor = Color.White  // Màu văn bản cho tab đang chọn
    val inactiveTextColor = Color(0xFFF35E17)  // Màu văn bản cho tab không chọn


    var listtab = listOf(
        TabItem("Income", icon =  Icons.Default.ArrowBack){
            IncomeContent()
        },
        TabItem("Expense", icon =  Icons.Default.ArrowForward){
            ExpenseContent()
        }
    )

    Row(
        modifier = modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(color = backgroundColor),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center // Căn giữa TabRow
    ) {
        TabRow(
            selectedTabIndex = tabIndex,
            modifier = Modifier
                .background(backgroundColor)
                .width(200.dp),
            indicator = {
            },  // Không có chỉ báo
            divider = {}  // Không có dòng phân cách
        ) {
            titles.forEachIndexed { index, title ->
                val isSelected = tabIndex == index
                val tabColor by animateColorAsState (
                    if (isSelected) activeColor else inactiveColor,
                    animationSpec = tween(500)
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) activeTextColor else inactiveTextColor,
                    animationSpec = tween(durationMillis = 500)
                )
                val shape = when (index) {
                    0 -> RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp)
                    titles.lastIndex -> RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp)
                    else -> RoundedCornerShape(8.dp)
                }
                val tabWidth by animateDpAsState(
                    targetValue = if (isSelected) 100.dp else 200.dp,
                    animationSpec = tween(durationMillis = 500)
                )
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
                            .background(tabColor, shape = RoundedCornerShape(6.dp))
                        else Modifier.width(100.dp),
                        selected = isSelected,
                        onClick = { onTabSelected(index)
                            ;coroutineScoper.launch {
                                pagerStatement.scrollToPage(index) }
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
    }
}

// Tạo grid danh mục chi tiêu bằng LazyVerticalGrid




@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TabMoney()
}
