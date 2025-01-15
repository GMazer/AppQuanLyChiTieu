package com.example.jetpackcompose.app.screens


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jetpackcompose.navigation.CustomBottomAppBar
import kotlinx.coroutines.launch

@Composable
fun MainScreen(navController: NavHostController) {
    val pagerState = rememberPagerState(
        pageCount = { 5 }
    )
    val coroutineScope = rememberCoroutineScope()

    var selectedPage by rememberSaveable { mutableStateOf(0) } // Quản lý selectedPage tại MainScreen

    Scaffold(
        bottomBar = {
            CustomBottomAppBar(
                pagerState = pagerState,
                coroutineScope = coroutineScope,
                selectedPage = selectedPage,
                onPageSelected = { page ->
                    selectedPage = page // Cập nhật selectedPage khi bấm vào các mục trong BottomAppBar
                    coroutineScope.launch { pagerState.scrollToPage(page) }
                })
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 0.dp),
            beyondViewportPageCount = 0, // Đặt giá trị là 0
            pageSpacing = 16.dp,
            verticalAlignment = Alignment.CenterVertically,
            userScrollEnabled = false,
            reverseLayout = false
        ) { page ->
            when (page) {
                0 -> ReportScreen(navController, pagerState, coroutineScope, onPageSelected = { selectedPage = it })
                1 -> CalendarScreen(navController)
                2 -> InputScreen(navController)
                3 -> OtherScreen(navController)
                4 -> BudgetScreen(navController)
            }
        }
    }
}
