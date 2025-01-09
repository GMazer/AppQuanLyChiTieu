package com.example.jetpackcompose.app.screens

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.apiService.TransactionAPI.PutLimitTransactionViewModel
import com.example.jetpackcompose.app.features.inputFeatures.ExpenseContent
import com.example.jetpackcompose.app.features.inputFeatures.IncomeContent
import com.example.jetpackcompose.components.CustomTabRow


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = NavController(this)
            InputScreen(navController)
        }
    }
}

data class LimitTransaction(
    val limits: List<CategoryLimit>
) {
    data class CategoryLimit(
        val categoryId: Int,
        val limitExpense: Long
    )
}

data class RemainLimit(
    val limits: List<CategoryLimit>
) {
    data class CategoryLimit(
        val categoryId: Int,
        val limitExpense: Int,
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




@Composable
fun InputScreen(navController: NavController) {
    val tabs = listOf(
        TabItem("Expense", icon = Icons.AutoMirrored.Filled.ArrowBack) {
            ExpenseContent()
        },
        TabItem("Income", icon = Icons.AutoMirrored.Filled.ArrowForward) {
            IncomeContent()
        }
    )

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    // Nội dung chính của màn hình
    MaterialTheme {
        var tabIndex by rememberSaveable { mutableIntStateOf(0) }
        val tabTitles = listOf("Tiền chi", "Tiền thu")

        Column(modifier = Modifier
            .background(Color(0xFFF1F1F1))
            .fillMaxSize()
        ) {
            CustomTabRow(
                tabIndex = tabIndex,
                onTabSelected = { tabIndex = it },
                titles = tabTitles,
                pagerStatement = pagerState,
                coroutineScoper = coroutineScope,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            HorizontalPager(state = pagerState, userScrollEnabled = false) {
                tabs[it].screen()
            }
        }
    }
}




