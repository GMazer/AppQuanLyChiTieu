package com.example.jetpackcompose.app.screens.anual_sceens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI.DeleteFixedTransactionViewModel
import com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI.FixedTransactionResponse
import com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI.GetFixedTransactionViewModel
import com.example.jetpackcompose.app.screens.Category
import com.example.jetpackcompose.components.MessagePopup
import com.example.jetpackcompose.components.montserrat
import com.example.jetpackcompose.ui.theme.primaryColor
import com.example.jetpackcompose.ui.theme.componentShapes
import com.example.jetpackcompose.ui.theme.textColor
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun AnualScreen(navController: NavHostController) {


    var isEditing by remember { mutableStateOf(false) }
    val viewModel: GetFixedTransactionViewModel = GetFixedTransactionViewModel(LocalContext.current)
    var fixedTransactions by remember { mutableStateOf<List<FixedTransactionResponse>>(emptyList()) }
    var showPopup by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    MessagePopup(
        showPopup = showPopup,
        successMessage = successMessage,
        errorMessage = errorMessage,
        onDismiss = { showPopup = false } // Đóng popup khi nhấn ngoài
    )

    // Hàm gọi lại API để tải lại danh sách giao dịch
    fun reloadTransactions() {
        successMessage = "Đang tải dữ liệu..."
        showPopup = true
        viewModel.getFixedTransactions(
            onSuccess = { transactions ->
                fixedTransactions = transactions
            },
            onError = { errorMessage ->
                Log.e("AnualScreen", errorMessage)
            }
        )
    }

    LaunchedEffect(Unit) {
        successMessage = "Đang tải dữ liệu..."
        showPopup = true
        reloadTransactions() // Load dữ liệu ngay khi màn hình được tạo
    }

    Column(modifier = Modifier
        .background(Color(0xfff5f5f5))
        .fillMaxSize()) {
        // Hàng chứa nút trở về và nút chuyển hướng
        Row(
            modifier = Modifier
                .height(50.dp)
                .background(Color(0xfff1f1f1))
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Nút quay lại
            Box(modifier = Modifier.weight(1.5f)) {
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

            // Chữ "Thu chi cố định" luôn nằm giữa và ẩn nếu diện tích không đủ
            Box(modifier = Modifier.weight(2f)) {
                Text(
                    "Thu chi cố định",
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
                Text(
                    if (isEditing) "Hoàn thành" else "Chỉnh sửa",
                    fontSize = 12.sp,
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Normal,
                    color = primaryColor,
                    modifier = Modifier
                        .clickable(onClick = {
                            isEditing = !isEditing
                        })
                )
            }

            // Nút thêm mới
            Box(modifier = Modifier.weight(0.5f)) {
                IconButton(onClick = {
                    navController.navigate("inputfixedtab")
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = primaryColor,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }
        }

        Divider(color = Color.LightGray, thickness = 1.dp)

        Spacer(modifier = Modifier.height(16.dp))
        // Hiển thị danh sách FixedTransaction
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .background(Color(0xfff5f5f5))
                .fillMaxSize()
        ) {
            items(fixedTransactions) { transaction ->
                FixedTransactionRow(
                    transaction = transaction,
                    isEditing = isEditing,
                    onTransactionDeleted = { reloadTransactions() },
                    navController = navController
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun FixedTransactionRow(
    transaction: FixedTransactionResponse,
    isEditing: Boolean,
    navController: NavHostController,
    onTransactionDeleted: () -> Unit // Hàm gọi lại để reload dữ liệu sau khi xóa
) {
    val viewModel: DeleteFixedTransactionViewModel =
        DeleteFixedTransactionViewModel(LocalContext.current)
    val currencyFormatter = remember {
        val symbols = DecimalFormatSymbols(Locale("vi", "VN"))
        symbols.decimalSeparator = '.'
        symbols.groupingSeparator = ','
        val format = DecimalFormat("#,###", symbols)
        format
    }

    val amountText = buildAnnotatedString {
        append(
            if (transaction.category_id >= 10) {
                "+${currencyFormatter.format(transaction.amount)}"
            } else {
                "${currencyFormatter.format(transaction.amount)}"
            }
        )
        withStyle(style = SpanStyle(fontSize = 12.sp)) {  // Kích thước nhỏ hơn cho ký tự "₫"
            append("₫")
        }
    }

    // State để hiển thị AlertDialog
    var isDialogVisible by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

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

    Row(
        modifier = Modifier
            .clip(componentShapes.small)
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp)
            .clickable {
                if (transaction.category_id >= 10) {
                    navController.navigate("editFixedIncome/${transaction.fixed_transaction_id}")
                } else {
                    navController.navigate("editFixedExpense/${transaction.fixed_transaction_id}")
                }
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (isEditing) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_remove_circle_24),
                contentDescription = "Remove",
                tint = Color(0xffff3c28),
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        // Hiển thị AlertDialog xác nhận xoá
                        isDialogVisible = true
                        dialogMessage = "Bạn có muốn xoá giao dịch này không?"
                    }
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        val category = categories.find { it.id == transaction.category_id }
        if (category != null) {
            Icon(
                painter = category.iconPainter(),
                contentDescription = category.name,
                tint = category.iconColor,
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.title ?: "No Title",
                fontWeight = FontWeight.SemiBold,
                fontFamily = montserrat,
                color = textColor,
                fontSize = 12.sp,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(
                    text = transaction.categoryName,
                    fontSize = 8.sp,
                    fontFamily = montserrat,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "\u2022",
                    fontSize = 8.sp,
                    fontFamily = montserrat,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = when (transaction.repeate_frequency) {
                        "daily" -> "Hàng ngày"
                        "weekly" -> "Hàng tuần"
                        "monthly" -> "Hàng tháng"
                        "yearly" -> "Hàng năm"
                        else -> "Không xác định"
                    },
                    fontSize = 8.sp,
                    fontFamily = montserrat,
                )
            }
        }

        // Cột bên phải: Amount và Mũi tên
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Hiển thị Amount
            Text(
                text = amountText,
                fontWeight = FontWeight.SemiBold,
                fontFamily = montserrat,
                fontSize = 16.sp,
                color = if (transaction.category_id >= 10) Color(0xff62bbeb) else Color(0xffff5c46),
            )
            Spacer(modifier = Modifier.width(6.dp))

            // Nếu không phải chế độ chỉnh sửa, hiển thị mũi tên
            if (!isEditing) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_arrow_forward_ios_24),
                    contentDescription = "Next",
                    tint = Color.LightGray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }


    // Hiển thị AlertDialog khi isDialogVisible = true
    if (isDialogVisible) {
        AlertDialog(
            onDismissRequest = {
                isDialogVisible = false // Đóng dialog khi nhấn ra ngoài
            },
            title = {
                Text(
                    text = "Xác nhận xóa",
                    fontFamily = montserrat,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            },
            text = {
                Text(
                    text = dialogMessage,
                    fontFamily = montserrat,
                    color = textColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            },
            confirmButton = {
                if (!isLoading) {
                    TextButton(onClick = {
                        isLoading = true
                        // Gọi hàm deleteFixedTransaction của ViewModel
                        viewModel.deleteFixedTransaction(
                            fixed_transaction_id = transaction.fixed_transaction_id,
                            onSuccess = {
                                isLoading = false
                                isDialogVisible = false
                                onTransactionDeleted()
                            },
                            onError = {
                                isLoading = false
                                isDialogVisible = false
                                // Hiển thị thông báo lỗi nếu cần
                            }
                        )
                    }) {
                        Text(
                            "Xoá",
                            fontFamily = montserrat,
                            color = primaryColor,
                        )
                    }
                } else {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp)) // Hiển thị loading
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    isDialogVisible = false // Đóng dialog khi nhấn bỏ qua
                }) {
                    Text(
                        "Bỏ qua",
                        fontFamily = montserrat,
                        color = Color(0xFF62B7E5),
                    )
                }
            }
        )
    }
}


@Preview
@Composable
fun PreviewAnualScreen() {
    val context = LocalContext.current
    AnualScreen(navController = NavHostController(context = context))
}
