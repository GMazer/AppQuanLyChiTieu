package com.example.jetpackcompose.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcompose.app.screens.login_signup.SignUpScreen
import com.example.jetpackcompose.app.screens.login_signup.SignInScreen
import com.example.jetpackcompose.app.features.MainScreen
import com.example.jetpackcompose.app.screens.OtherScreen
import com.example.jetpackcompose.app.screens.other_screens.anualScreen.AnualScreen

@Composable
fun AppQuanLyChiTieu() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "signup") {
        composable("signup") { SignUpScreen(navController) }
        composable("signin") { SignInScreen(navController) }
        composable("mainscreen") { MainScreen(navController)}
        composable("anual") { AnualScreen(navController) }
        composable("other") { OtherScreen(navController)}
    }
}



@Preview
@Composable
fun PreviewAppQuanLyChiTieu() {
    AppQuanLyChiTieu()
}