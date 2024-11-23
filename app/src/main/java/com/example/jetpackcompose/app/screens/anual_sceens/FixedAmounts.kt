package com.example.jetpackcompose.app.features.fixedIncomeAndExpenses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jetpackcompose.app.features.inputFeatures.IncomeContent
import com.example.jetpackcompose.app.features.inputFeatures.OutComeContent
import com.example.jetpackcompose.app.features.inputFeatures.TabItem
import com.example.jetpackcompose.app.features.inputFeatures.monsterrat
import com.example.jetpackcompose.components.FixedExpense
import com.example.jetpackcompose.components.FixedIncome
import com.example.jetpackcompose.components.FixedTabRow
import com.example.jetpackcompose.components.OtherTab


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputFixedTab(navController: NavHostController) {
//    val customTypography = Typography(
//        bodyLarge = TextStyle(fontFamily = monsterrat),
//        bodyMedium = TextStyle(fontFamily = monsterrat),
//        bodySmall = TextStyle(fontFamily = monsterrat),
//        titleLarge = TextStyle(fontFamily = monsterrat),
//        titleMedium = TextStyle(fontFamily = monsterrat),
//        titleSmall = TextStyle(fontFamily = monsterrat),
//        labelLarge = TextStyle(fontFamily = monsterrat),
//        labelMedium = TextStyle(fontFamily = monsterrat),
//        labelSmall = TextStyle(fontFamily = monsterrat),
//        headlineLarge = TextStyle(fontFamily = monsterrat),
//        headlineMedium = TextStyle(fontFamily = monsterrat),
//        headlineSmall = TextStyle(fontFamily = monsterrat)
//    )
//
//    val tabs = listOf(
//        TabItem("Expense", icon = Icons.Default.ArrowBack) {
//            FixedExpense()
//        },
//        TabItem("Income", icon = Icons.Default.ArrowForward) {
//            FixedIncome()
//        }
//    )
//
//    val pagerState = rememberPagerState(
//        pageCount = { tabs.size }
//    )
//
//    val coroutineScope = rememberCoroutineScope()
//
//    MaterialTheme(
//        typography = customTypography
//    ) {
//        var tabIndex by rememberSaveable { mutableStateOf(0) }
//        val tabTitles = listOf("Tiền chi", "Tiền thu")
//
//        Column(
//            modifier = Modifier
//                .background(Color(0xFFF1F1F1))
//                .fillMaxSize()
//        ) {
//            FixedTabRow(
//                tabIndex = tabIndex,
//                onTabSelected = { tabIndex = it }, // Sửa cách gán giá trị tabIndex
//                titles = tabTitles,
//                pagerStatement = pagerState,
//                coroutineScoper = coroutineScope,
//                modifier = Modifier.padding(horizontal = 16.dp)
//            )
//            HorizontalPager(state = pagerState, userScrollEnabled = false) {
//                tabs[it].screen()
//            }
//        }
//    }
    OtherTab(value = "Fixed", painter = painterResource(id = android.R.drawable.ic_dialog_info), onClick = {
        navController.navigate("anual")
    })
}

@Preview
@Composable
fun InputFixedTabPreview() {
    InputFixedTab(navController = NavHostController(
        context = TODO()
    ))
}
