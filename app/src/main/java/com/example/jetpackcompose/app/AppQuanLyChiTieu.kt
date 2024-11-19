package com.example.jetpackcompose.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcompose.app.screens.login_signup.SignUpScreen
import com.example.jetpackcompose.app.screens.login_signup.SignInScreen
import com.example.jetpackcompose.app.features.MainScreen

@Composable
fun AppQuanLyChiTieu() {
    val navController = rememberNavController()
    val context = LocalContext.current // Lấy context từ LocalContext

    NavHost(navController = navController, startDestination = "signup") {
        composable("signup") { SignUpScreen(navController) }
        composable("signin") { SignInScreen(navController, context) }
        composable("mainscreen") { MainScreen() }
    }
}



@Preview
@Composable
fun PreviewAppQuanLyChiTieu() {
    AppQuanLyChiTieu()
}