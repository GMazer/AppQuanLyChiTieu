package com.example.jetpackcompose.app.features.apiService.ReadNotificationTransaction

// TransactionNotificationScreen.kt
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.components.montserrat
import com.example.jetpackcompose.ui.theme.primaryColor
import com.example.jetpackcompose.ui.theme.textColor
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun TransactionNotificationScreen(navController: NavController) {
    val context = LocalContext.current

    // Tạo đối tượng TransactionStorage
    val transactionStorage = TransactionStorage(context)

    // Tải danh sách giao dịch từ bộ nhớ trong
    val transactions = transactionStorage.loadTransactions()

    // Nhóm các giao dịch theo ngày
    val groupedTransactions = transactions.groupBy { it.date }.toSortedMap(reverseOrder())

    Log.d("TransactionNotificationScreen", "Danh sách giao dịch đã tải: $transactions")

    // Hiển thị giao dịch đã nhóm
    Column(
        modifier = Modifier
            .background(Color(0xfff5f5f5))
            .fillMaxSize()
    ) {
        Row(modifier = Modifier
            .height(50.dp)
            .background(Color(0xfff1f1f1))
            .fillMaxWidth()
            .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {

            Box(modifier = Modifier.weight(1f)) {
                IconButton(onClick = {
                    navController.popBackStack("mainscreen", inclusive = false)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_arrow_back_ios_24),
                        contentDescription = "Back",
                        tint = primaryColor,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }

            Box(modifier = Modifier.weight(2f)) {
                androidx.compose.material.Text(
                    "Giao dịch từ thông báo",
                    fontSize = 14.sp,
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier
                        .requiredWidthIn(min = 0.dp) // Ẩn chữ khi không đủ diện tích
                        .fillMaxWidth(), // Đảm bảo chiếm toàn bộ không gian của Row
                    textAlign = TextAlign.Center
                )
            }

            // Chữ "Chỉnh sửa" hoặc "Hoàn thành"
            Box(modifier = Modifier.weight(1f)) {

            }
        }

        Divider(color = Color.LightGray, thickness = 1.dp)
        // Hàng chứa nút trở về và nút chuyển hướng

            // Nút quay lại

            // Chữ "Thu chi cố định" luôn nằm giữa và ẩn nếu diện tích không đủ

        // Hiển thị các nhóm giao dịch
        LazyColumn {
            groupedTransactions.forEach { (date, transactionsForDate) ->
                item{
                    Column(modifier = Modifier.background(Color(0xfff5f5f5))) {
                        // Hiển thị ngày
                        Divider(color = Color.LightGray, thickness = 0.7.dp)
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .height(35.dp)
                                .background(Color(0xfff1f1f1))
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = date,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = montserrat,
                                fontSize = 12.sp,
                            )
                        }
                        Divider(color = Color.LightGray, thickness = 0.7.dp)
                        // Hiển thị danh sách giao dịch cho ngày này
                        transactionsForDate.reversed().forEach { transaction ->
                            TransactionRow(
                                transaction = transaction,
                                index = transactions.indexOf(transaction),
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionRow(transaction: TransactionReadNoti, index: Int, navController: NavController) {

    val currencyFormatter = remember {
        val symbols = DecimalFormatSymbols(Locale("vi", "VN"))
        symbols.decimalSeparator = '.'
        symbols.groupingSeparator = ','

        val format = DecimalFormat("#,###", symbols)
        format
    }

    val amountText = buildAnnotatedString {
        if(transaction.type == "income") {
            append("+${currencyFormatter.format(transaction.amount)}")
            withStyle(style = SpanStyle(fontSize = 10.sp)) {  // Kích thước nhỏ hơn cho ký tự "₫"
                append("₫")
            }
        }
        else if(transaction.type == "expense") {
            append("-${currencyFormatter.format(transaction.amount)}")
            withStyle(style = SpanStyle(fontSize = 10.sp)) {  // Kích thước nhỏ hơn cho ký tự "₫"
                append("₫")
            }
        }
    }
    // Hiển thị thông tin giao dịch
    Row(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(start = 16.dp)
            .clickable {
                // Điều hướng đến màn hình tương ứng với type là 'expense' hoặc 'income' và truyền thêm index
                if (transaction.type == "expense") {
                    // Chuyển hướng tới trang chi tiêu với amount, ngày và index đã chọn
                    navController.navigate("postExpenseNotiTransaction/${transaction.amount}/${transaction.date}/$index")
                } else if (transaction.type == "income") {
                    // Chuyển hướng tới trang thu nhập với amount, ngày và index đã chọn
                    navController.navigate("postIncomeNotiTransaction/${transaction.amount}/${transaction.date}/$index")
                }
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Hiển thị Tiền chi hoặc Tiền thu
        Text(
            text = when (transaction.type) {
                "income" -> "Tiền thu"
                "expense" -> "Tiền chi"
                else -> "Không xác định"
            },
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            fontFamily = montserrat,
            color = textColor,
            modifier = Modifier.weight(1f)
        )

        // Hiển thị số tiền
        Text(
            text = amountText,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            fontFamily = montserrat,
            color = if(transaction.type == "income") Color(0xff62bbeb) else Color(0xffff5c46),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )

        // Nút "Arrow Forward"
        IconButton(
            onClick = { /* Xử lý sự kiện nhấn vào nút Arrow Forward */ }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_arrow_forward_ios_24),
                contentDescription = "Arrow Forward",
                tint = Color.LightGray,
                modifier = Modifier
                    .size(16.dp)
            )
        }
    }
    Divider(color = Color.LightGray, thickness = 0.7.dp)
}



