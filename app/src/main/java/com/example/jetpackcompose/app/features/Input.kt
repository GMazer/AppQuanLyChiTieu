package com.example.jetpackcompose.app.features

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.lifecycle.ViewModel
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

enum class TransactionType {
    INCOME, // Tiền thu
    EXPENSE // Tiền chi
}

data class Transaction(
    val date: String,
    val note: String,
    val amount: Long,
    val category: String,
    val type: TransactionType // Thêm thuộc tính type
)

class TransactionViewModel : ViewModel() {
    private val _transactions = mutableStateListOf<Transaction>()
    val transactions: List<Transaction> = _transactions

    // Hàm thêm giao dịch
    fun addTransaction(transaction: Transaction) {
        _transactions.add(transaction)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseContent () {

    var tabIndex by remember { mutableStateOf(0) }
    var textState by remember { mutableStateOf(TextFieldValue()) }
    var amountState by remember { mutableStateOf(TextFieldValue()) }
    var selectedDate by remember { mutableStateOf("Chưa chọn ngày") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    val buttonColor = Color(0xFFF35E17)

    var transactionType by remember { mutableStateOf(TransactionType.EXPENSE) }

    LaunchedEffect(tabIndex) {
        transactionType = if (tabIndex == 0) TransactionType.EXPENSE else TransactionType.INCOME
    }


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

    Scaffold(
        containerColor = Color.White,

        //Bottom Navigation Bar
        bottomBar = {
            CustomBottomAppBar()
        }
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
                DatePickerButton(onDateSelected = { date ->
                    selectedDate = date
                })
            }
            Spacer(Modifier.height(8.dp))

            // Ghi chu
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
                    value = amountState,
                    onValueChange = { amountState = it },
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

            // Gọi danh mục chi tiêu và truyền callback
            CategoriesGrid(
                categories = categories,
                buttonColor = buttonColor,
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category
                }
            )

            Spacer(Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
            ) {
                Button(
                    onClick = {
                        val amount = amountState.text.toLongOrNull() ?: 0L
                        val categoryName = selectedCategory?.name ?: "Chưa chọn danh mục"

                        val transaction = Transaction(
                            date = selectedDate,
                            note = textState.text,
                            amount = amount,
                            category = categoryName,
                            type = transactionType // Thêm loại giao dịch
                        )
                        TransactionViewModel().addTransaction(transaction)

                        // Xóa dữ liệu nhập sau khi lưu
                        textState = TextFieldValue("")
                        amountState = TextFieldValue("")

                        // Ghi log thông tin giao dịch
                        Log.i("ExpenseContent", "Transaction: $transaction")
                    },

                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text("Nhập khoản chi", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeContent () {

    var tabIndex by remember { mutableStateOf(1) }
    var textState by remember { mutableStateOf(TextFieldValue()) }
    var amountState by remember { mutableStateOf(TextFieldValue()) }
    var selectedDate by remember { mutableStateOf("Chưa chọn ngày") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) } // Lưu danh mục được chọn

    val buttonColor = Color(0xFFF35E17)

    var transactionType by remember { mutableStateOf(TransactionType.INCOME) }

    LaunchedEffect(tabIndex) {
        transactionType = if (tabIndex == 0) TransactionType.EXPENSE else TransactionType.INCOME
        Log.i("ExpenseContent", "Loại giao dịch hiện tại: $transactionType")
    }


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

    Scaffold(
        containerColor = Color.White,

        //Bottom Navigation Bar
        bottomBar = {
            CustomBottomAppBar()
        }
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
                DatePickerButton(onDateSelected = { date ->
                    selectedDate = date
                })
            }
            Spacer(Modifier.height(8.dp))

            // Ghi chu
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
                    value = amountState,
                    onValueChange = { amountState = it },
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

            // Gọi danh mục chi tiêu và truyền callback
            CategoriesGrid(
                categories = categories,
                buttonColor = buttonColor,
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category
                }
            )

            Spacer(Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
            ) {
                Button(
                    onClick = {
                        val amount = amountState.text.toLongOrNull() ?: 0L
                        val categoryName = selectedCategory?.name ?: "Chưa chọn danh mục"

                        val transaction = Transaction(
                            date = selectedDate,
                            note = textState.text,
                            amount = amount,
                            category = categoryName,
                            type = transactionType // Thêm loại giao dịch
                        )
                        TransactionViewModel().addTransaction(transaction)

                        // Xóa dữ liệu nhập sau khi lưu
                        textState = TextFieldValue("")
                        amountState = TextFieldValue("")

                        // Ghi log thông tin giao dịch
                        Log.i("ExpenseContent", "Transaction: $transaction")
                    },

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
fun DatePickerButton(onDateSelected: (String) -> Unit) {
    var dateText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Định dạng ngày tháng năm thứ
    val dateFormat = SimpleDateFormat("dd/MM/yyyy (E)", Locale("vi", "VN"))

    // Cập nhật ngày hiện tại khi khởi tạo
    LaunchedEffect(key1 = true) {
        dateText = dateFormat.format(calendar.time)
        onDateSelected(dateText) // Gọi callback khi khởi tạo
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        // Nút lùi lịch
        IconButton(
            onClick = {
                calendar.add(Calendar.DAY_OF_MONTH, -1)
                dateText = dateFormat.format(calendar.time)
                onDateSelected(dateText) // Gọi callback khi lùi ngày
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
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(5.dp),
            onClick = {
                val datePickerDialog = DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, month)
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        dateText = dateFormat.format(calendar.time)
                        onDateSelected(dateText) // Gọi callback khi chọn ngày
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
                calendar.add(Calendar.DAY_OF_MONTH, +1)
                dateText = dateFormat.format(calendar.time)
                onDateSelected(dateText) // Gọi callback khi tiến ngày
            },
            modifier = Modifier.size(20.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_arrow_forward_ios_24),
                contentDescription = "Tiến lịch",
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
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TabMoney()
}
