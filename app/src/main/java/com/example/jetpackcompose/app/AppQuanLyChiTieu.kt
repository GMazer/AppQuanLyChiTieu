package com.example.jetpackcompose.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcompose.app.screens.SignUpScreen
import com.example.jetpackcompose.app.screens.SignInScreen
import com.example.jetpackcompose.app.features.MainScreen

@Composable
fun AppQuanLyChiTieu() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "signup") {
        composable("signup") { SignUpScreen(navController) }
        composable("signin") { SignInScreen(navController) }
        composable("mainscreen") { MainScreen() }
    }
}



@Preview
@Composable
fun PreviewAppQuanLyChiTieu() {
    AppQuanLyChiTieu()
}