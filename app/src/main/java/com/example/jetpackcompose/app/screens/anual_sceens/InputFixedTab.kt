package com.example.jetpackcompose.app.screens.anual_sceens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.jetpackcompose.app.screens.TabItem
import com.example.jetpackcompose.components.FixedTabRow
import kotlinx.coroutines.launch


@Composable
fun InputFixedTab(navController: NavHostController) {

    val tabs = listOf(
        TabItem("Expense", icon = Icons.Default.ArrowBack) {
            FixedExpense(navController = navController)
        },
        TabItem("Income", icon = Icons.Default.ArrowForward) {
            FixedIncome(navController = navController)
        }
    )

    val pagerState = rememberPagerState(
        pageCount = { tabs.size }
    )

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .background(Color(0xFFF1F1F1))
            .fillMaxSize()
    ) {
        FixedTabRow(
            tabIndex = pagerState.currentPage,
            onTabSelected = { tabIndex ->
                coroutineScope.launch {
                    pagerState.scrollToPage(tabIndex)
                }
            },
            titles = listOf("Tiền chi", "Tiền thu"),
            pagerStatement = pagerState,
            coroutineScope = coroutineScope,
            navController = navController
        )

        HorizontalPager(state = pagerState, userScrollEnabled = false) {
            tabs[it].screen()
        }
    }
}




