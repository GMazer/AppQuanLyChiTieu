package com.example.jetpackcompose.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetpackcompose.app.screens.login_signup.SignUpScreen
import com.example.jetpackcompose.app.screens.login_signup.SignInScreen
import com.example.jetpackcompose.app.features.MainScreen
import com.example.jetpackcompose.app.features.apiService.LogAPI.SignInViewModel
import com.example.jetpackcompose.app.features.apiService.LogAPI.SignInViewModelFactory
import com.example.jetpackcompose.app.features.editFeatures.EditExpenseTransaction
import com.example.jetpackcompose.app.features.editFeatures.EditIncomeTransaction
import com.example.jetpackcompose.app.screens.CalendarScreen
import com.example.jetpackcompose.app.screens.anual_sceens.InputFixedTab
import com.example.jetpackcompose.app.screens.OtherScreen
import com.example.jetpackcompose.app.screens.anual_sceens.AnualScreen

@Composable
fun AppQuanLyChiTieu() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val signInViewModel: SignInViewModel = viewModel(
        factory = SignInViewModelFactory(context)
    )

    // Kiểm tra nếu Token đã được xác nhận hay không
    if (signInViewModel.isTokenCleared()) {
        NavHost(navController = navController, startDestination = "signup") {
            composable("signup") { SignUpScreen(navController) }
            composable("signin") { SignInScreen(navController) }
            composable("mainscreen") { MainScreen(navController) }
            composable("anual") { AnualScreen(navController) }
            composable("other") { OtherScreen(navController) }
            composable("inputfixedtab") { InputFixedTab(navController) }
            composable("calendar") { CalendarScreen(navController) }

            // Chỉnh sửa giao dịch (truyền transactionId)
            composable(
                "editExpense/{transactionId}",
                arguments = listOf(navArgument("transactionId") { type = NavType.IntType })
            ) { backStackEntry ->
                // Lấy transactionId từ NavArgument
                val transactionId = backStackEntry.arguments?.getInt("transactionId") ?: 0
                EditExpenseTransaction(navController = navController, transactionId = transactionId)
            }

            composable(
                "editIncome/{transactionId}",
                arguments = listOf(navArgument("transactionId") { type = NavType.IntType })
            ) { backStackEntry ->
                // Lấy transactionId từ NavArgument
                val transactionId = backStackEntry.arguments?.getInt("transactionId") ?: 0
                EditIncomeTransaction(navController = navController, transactionId = transactionId)
            }


        }
    } else {
        NavHost(navController = navController, startDestination = "mainscreen") {
            composable("signup") { SignUpScreen(navController) }
            composable("signin") { SignInScreen(navController) }
            composable("mainscreen") { MainScreen(navController) }
            composable("anual") { AnualScreen(navController) }
            composable("other") { OtherScreen(navController) }
            composable("inputfixedtab") { InputFixedTab(navController) }
            composable("calendar") { CalendarScreen(navController) }

            // Chỉnh sửa giao dịch (truyền transactionId)
            composable(
                "editExpense/{transactionId}",
                arguments = listOf(navArgument("transactionId") { type = NavType.IntType })
            ) { backStackEntry ->
                // Lấy transactionId từ NavArgument
                val transactionId = backStackEntry.arguments?.getInt("transactionId") ?: 0
                EditExpenseTransaction(navController = navController, transactionId = transactionId)
            }
            composable(
                "editIncome/{transactionId}",
                arguments = listOf(navArgument("transactionId") { type = NavType.IntType })
            ) { backStackEntry ->
                // Lấy transactionId từ NavArgument
                val transactionId = backStackEntry.arguments?.getInt("transactionId") ?: 0
                EditIncomeTransaction(navController = navController, transactionId = transactionId)
            }
        }
    }
}





@Preview
@Composable
fun PreviewAppQuanLyChiTieu() {
    AppQuanLyChiTieu()
}