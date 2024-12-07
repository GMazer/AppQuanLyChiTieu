package com.example.jetpackcompose.app.screens.anual_sceens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI.DeleteFixedTransactionViewModel
import com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI.FixedTransactionResponse
import com.example.jetpackcompose.app.features.apiService.FixedTransactionAPI.GetFixedTransactionViewModel
import com.example.jetpackcompose.components.montserrat
import com.example.jetpackcompose.ui.theme.colorPrimary
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

    // Hàm gọi lại API để tải lại danh sách giao dịch
    fun reloadTransactions() {
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
        reloadTransactions() // Load dữ liệu ngay khi màn hình được tạo
    }

    Column(modifier = Modifier.fillMaxSize()) {
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
            IconButton(onClick = {
                navController.popBackStack("mainscreen", inclusive = false)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_arrow_back_ios_24),
                    contentDescription = "Back",
                    tint = colorPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                "Thu chi cố định",
                fontSize = 16.sp,
                fontFamily = montserrat,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Text(
                if (isEditing) "Hoàn thành" else "Chỉnh sửa",
                fontSize = 12.sp,
                fontFamily = montserrat,
                fontWeight = FontWeight.Normal,
                color = colorPrimary,
                modifier = Modifier.clickable(onClick = {
                    isEditing = !isEditing
                })
            )

            IconButton(onClick = {
                navController.navigate("inputfixedtab")
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = colorPrimary,
                    modifier = Modifier.size(24.dp)
                )
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
                Log.d("AnualScreen", "Transaction: ${transaction.repeate_frequency}")
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

    val formattedAmount = if (transaction.category_id >= 10) {
        "+${currencyFormatter.format(transaction.amount)}₫"
    } else {
        "${currencyFormatter.format(transaction.amount)}₫"
    }

    // State để hiển thị AlertDialog
    var isDialogVisible by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

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
        // Nếu đang chỉnh sửa, hiển thị dấu trừ, nếu không hiển thị mũi tên
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

        // Cột bên trái: Note và Category Name
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.title ?: "No Title",
                fontWeight = FontWeight.Bold,
                fontFamily = montserrat,
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = transaction.categoryName,
                fontSize = 10.sp,
                fontFamily = montserrat,
            )
        }

        // Cột bên phải: Amount và Mũi tên
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Hiển thị Amount
            Text(
                text = formattedAmount,
                fontWeight = FontWeight.Bold,
                fontFamily = montserrat,
                fontSize = 18.sp,
                color = if (transaction.category_id >= 10) Color(0xff62bbeb) else Color(0xffff4948),
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
                Text(text = "Xác nhận xóa")
            },
            text = {
                Text(text = dialogMessage)
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
                        Text("OK")
                    }
                } else {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp)) // Hiển thị loading
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    isDialogVisible = false // Đóng dialog khi nhấn bỏ qua
                }) {
                    Text("Bỏ qua")
                }
            }
        )
    }
}


@Preview
@Composable
fun PreviewAnualScreen() {
    AnualScreen(navController = NavHostController(context = TODO()))
}
