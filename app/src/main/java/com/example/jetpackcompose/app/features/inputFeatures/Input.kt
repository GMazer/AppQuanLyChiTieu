package com.example.jetpackcompose.app.features.inputFeatures

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.apiService.ReadNotificationTransaction.TransactionNotificationViewModel
import com.example.jetpackcompose.app.features.apiService.TransactionAPI.PostLimitTransactionViewModel
import com.example.jetpackcompose.components.PopUpSetValueDialog
import com.example.jetpackcompose.ui.theme.colorPrimary
import com.example.jetpackcompose.ui.theme.componentShapes
import com.example.jetpackcompose.ui.theme.topBarColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InputTab()
        }
    }
}

enum class TransactionType {
    INCOME, // Tiền thu
    EXPENSE // Tiền chi
}

data class LimitTransaction(
    val limits: List<CategoryLimit>
) {
    data class CategoryLimit(
        val categoryId: Int,
        val percentLimit: Int
    )
}

data class RemainLimit(
    val limits: List<CategoryLimit>
) {
    data class CategoryLimit(
        val categoryId: Int,
        val percentLimit: Int,
        val remainingPercent: Double
    )
}

data class Category(
    val id: Int,
    val name: String,
    val iconPainter: @Composable () -> Painter,
    val iconColor: Color,
    val percentage: Float
)


val montserrat = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_bold, FontWeight.Bold),
    Font(R.font.montserrat_light, FontWeight.Light)
)

data class TabItem(val text: String, val icon: ImageVector, val screen: @Composable () -> Unit)

data class Transaction(
    val category_id: Int,
    val amount: Long,
    val transaction_date: String,
    val note: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerButton(onDateSelected: (String) -> Unit) {
    var dateText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Định dạng ngày tháng năm thứ
    val dateFormat = SimpleDateFormat("yyyy-MM-dd (E)", Locale("vi", "VN"))

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
            modifier = Modifier
                .weight(1f)
                .size(20.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_arrow_back_ios_24),
                contentDescription = "Lùi lịch",
                tint = Color(0xFF444444)
            )
        }

        // Nút chọn ngày
        Button(
            modifier = Modifier.weight(8f),
            shape = componentShapes.medium,
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

        // Nút tiến lịch
        IconButton(
            onClick = {
                calendar.add(Calendar.DAY_OF_MONTH, +1)
                dateText = dateFormat.format(calendar.time)
                onDateSelected(dateText) // Gọi callback khi tiến ngày
            },
            modifier = Modifier
                .weight(1f)
                .size(20.dp)
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
    pagerStatement: PagerState,
    coroutineScoper: CoroutineScope,
    onLimitTransactionUpdated: (LimitTransaction) -> Unit,
    modifier: Modifier = Modifier
) {
    var isDialogOpen by remember { mutableStateOf(false) }
    val inactiveColor = Color(0xFFe1e1e1)  // Màu cho tab không chọn
    val inactiveTextColor = Color(0xFFF35E17)  // Màu văn bản cho tab không chọn




    Column {
        Row(
            modifier = modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(topBarColor),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center // Căn giữa TabRow
        ) {
            // Nút ảo
            IconButton(
                onClick = {},
                enabled = false // Đặt enabled = false để không thể nhấn được
            ) {
                // Không thêm Icon hoặc Text nào
            }

            TabRow(
                selectedTabIndex = tabIndex,
                modifier = Modifier
                    .background(topBarColor)
                    .width(200.dp),
                indicator = {
                },  // Không có chỉ báo
                divider = {}  // Không có dòng phân cách
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
                    val tabWidth by animateDpAsState(
                        targetValue = if (isSelected) 100.dp else 200.dp,
                        animationSpec = tween(durationMillis = 500)
                    )
                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .height(36.dp)
                            .background(topBarColor)
                            .background(inactiveColor, shape = shape)
                            .clip(shape),
                        contentAlignment = Alignment.Center
                    ) {
                        Tab(
                            modifier = if (isSelected) Modifier
                                .width(100.dp)
                                .height(32.dp)
                                .padding(horizontal = 2.dp)
                                .background(tabColor, shape = componentShapes.medium)
                                .clip(componentShapes.medium)
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

            IconButton(
                onClick = { isDialogOpen = true }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.edit_value_dialog), // Icon mặc định, bạn có thể thay đổi thành icon khác
                    contentDescription = "Mở Pop-up Form",
                    tint = colorPrimary
                )
            }

            // Hiển thị PopUpSetValueDialog khi isDialogOpen là true
            if (isDialogOpen) {
                PopUpSetValueDialog(
                    onDismiss = { isDialogOpen = false } ,
                    onConfirm = { newLimitTransaction ->
                        onLimitTransactionUpdated(newLimitTransaction)
                        isDialogOpen = false // Đóng dialog sau khi nhận giá trị
                    }

                )
            }
        }
    }
    Divider(
        color = Color.LightGray,
        thickness = 1.dp
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTab(viewModel: PostLimitTransactionViewModel = PostLimitTransactionViewModel(LocalContext.current)) {
    val transactionViewModel : TransactionNotificationViewModel = TransactionNotificationViewModel()
    val tabs = listOf(
        TabItem("Expense", icon = Icons.Default.ArrowBack) {
            ExpenseContent()
        },
        TabItem("Income", icon = Icons.Default.ArrowForward) {
            IncomeContent()
        }
    )

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    // Lấy danh sách giao dịch từ TransactionNotificationViewModel
    val transactionList by transactionViewModel.transactionList.observeAsState(emptyList())
    Log.d("InputTab", "Danh sách giao dịch InputTab: $transactionList")

    // Hiển thị AlertDialog nếu transactionList không rỗng
    var showDialog by remember { mutableStateOf(false) }

    if (transactionList.isNotEmpty()) {
        showDialog = true
    }

    // Khi showDialog là true, hiển thị AlertDialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Có giao dịch mới") },
            text = {
                Text("Bạn có muốn thêm giao dịch này không?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Thực hiện hành động khi người dùng đồng ý thêm giao dịch
                        // Ví dụ: thêm giao dịch vào danh sách giao dịch
                        Log.d("InputTab", "Giao dịch đã được thêm!")
                        showDialog = false
                    }
                ) {
                    Text("Thêm")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    // Nội dung chính của màn hình
    MaterialTheme {
        var tabIndex by rememberSaveable { mutableStateOf(0) }
        val tabTitles = listOf("Tiền chi", "Tiền thu")

        Column(modifier = Modifier
            .background(Color(0xFFF1F1F1))
            .fillMaxSize()
        ) {
            // Đặt CustomTabRow bên ngoài Scaffold
            CustomTabRow(
                tabIndex = tabIndex,
                onTabSelected = { tabIndex = it },
                titles = tabTitles,
                pagerStatement = pagerState,
                coroutineScoper = coroutineScope,
                onLimitTransactionUpdated = { newLimitTransaction ->
                    val limitTransaction: LimitTransaction = newLimitTransaction // Cập nhật limitTransaction từ dialog
                    viewModel.addLimitTransaction(
                        limitTransaction.limits,
                        onSuccess = {},
                        onError = {}
                    )
                    Log.i("limitTransaction", "$limitTransaction")
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            HorizontalPager(state = pagerState, userScrollEnabled = false) {
                tabs[it].screen()
            }
        }
    }
}




@Preview
@Composable
fun PreviewInputTab() {
    InputTab()
}