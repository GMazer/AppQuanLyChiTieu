package com.example.jetpackcompose.app.screens.anual_sceens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.jetpackcompose.app.features.inputFeatures.TabItem
import com.example.jetpackcompose.app.screens.anual_sceens.ViewModel.FixedExpenseViewModel
import com.example.jetpackcompose.app.screens.anual_sceens.ViewModel.FixedExpenseViewModelFactory
import com.example.jetpackcompose.app.screens.anual_sceens.ViewModel.FixedIncomeViewModel
import com.example.jetpackcompose.app.screens.anual_sceens.ViewModel.FixedIncomeViewModelFactory
import com.example.jetpackcompose.app.screens.anual_sceens.ViewModel.PeriodicTransaction
import com.example.jetpackcompose.components.FixedTabRow
import kotlinx.coroutines.launch


@Composable
fun InputFixedTab(navController: NavHostController) {
    val context = LocalContext.current

    // Tạo instance của FixedExpenseViewModel với Factory
    val fixedExpenseViewModel: FixedExpenseViewModel = viewModel(
        factory = FixedExpenseViewModelFactory(context)
    )

    val fixedIncomeViewModel: FixedIncomeViewModel = viewModel(
        factory = FixedIncomeViewModelFactory(context)
    )

    var expenseData by rememberSaveable { mutableStateOf<PeriodicTransaction?>(null) }
    var incomeData by rememberSaveable { mutableStateOf<PeriodicTransaction?>(null) }

    val tabs = listOf(
        TabItem("Expense", icon = Icons.Default.ArrowBack) {
            FixedExpense { data ->
                expenseData = data
            }
        },
        TabItem("Income", icon = Icons.Default.ArrowForward) {
            FixedIncome { data ->
                incomeData = data
            }
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
            coroutineScoper = coroutineScope,
            navController = navController,
            onSaveData = { tabIndex ->
                when (tabIndex) {
                    0 -> {
                        expenseData?.let { transaction ->
                            fixedExpenseViewModel.addFixedExpense(
                                transaction,
                                onSuccess = { successMessage ->
                                    Log.d("InputFixedTab", successMessage)
                                },
                                onError = { errorMessage ->
                                    Log.e("InputFixedTab", errorMessage)
                                }
                            )
                        }
                    }
                    1 -> {
                        incomeData?.let { transaction ->
                            fixedIncomeViewModel.addFixedIncome(
                                transaction,
                                onSuccess = { successMessage ->
                                    Log.d("InputFixedTab", successMessage)
                                },
                                onError = { errorMessage ->
                                    Log.e("InputFixedTab", errorMessage)
                                }
                            )
                        }
                    }
                }
            }
        )

        HorizontalPager(state = pagerState, userScrollEnabled = false) {
            tabs[it].screen()
        }
    }
}




