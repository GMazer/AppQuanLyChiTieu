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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.jetpackcompose.R
import com.example.jetpackcompose.components.DrawBottomLine
import com.example.jetpackcompose.components.CategoriesGrid
import com.example.jetpackcompose.components.NoteTextField
import com.example.jetpackcompose.components.NumberTextField
import com.example.jetpackcompose.components.InputTab
import com.example.jetpackcompose.app.features.inputFeatures.OutComeContent
import com.example.jetpackcompose.ui.theme.colorPrimary
import com.example.jetpackcompose.ui.theme.componentShapes
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

data class Category(
    val name: String,
    val iconPainter: @Composable () -> Painter,
    val iconColor: Color,
    val percentage: Float
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
            modifier = Modifier.width(250.dp),
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
            OutComeContent()
        }
    )

    Column {
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
        }
        Divider(
            color = Color.LightGray,
            thickness = 1.dp
        )
    }

}