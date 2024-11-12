package com.example.jetpackcompose.app.features


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.Scaffold
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetpackcompose.app.screens.InputScreen
import com.example.jetpackcompose.app.screens.CalendarScreen
import com.example.jetpackcompose.app.screens.ReportScreen
import com.example.jetpackcompose.app.screens.OtherScreen
import com.example.jetpackcompose.navigation.CustomBottomAppBar

@Composable
fun MainScreen() {
    val pagerState = rememberPagerState(
        pageCount = { 4 }
    )
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            CustomBottomAppBar(pagerState = pagerState, coroutineScope = coroutineScope)
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp),
            beyondViewportPageCount = PagerDefaults.BeyondViewportPageCount,
            pageSpacing = 8.dp,
            verticalAlignment = Alignment.CenterVertically,
            userScrollEnabled = true,
            reverseLayout = false
        ) { page ->
            when (page) {
                0 -> InputScreen()
                1 -> CalendarScreen()
                2 -> ReportScreen()
                3 -> OtherScreen()
            }
        }
    }
}



@Preview
@Composable
fun PreviewMainScreen() {
    MainScreen()
}