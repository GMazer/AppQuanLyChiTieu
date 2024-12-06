package com.example.jetpackcompose.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackcompose.ui.theme.topBarColor
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.jetpackcompose.components.OtherTab
import com.example.jetpackcompose.R
import android.util.Log
import androidx.compose.foundation.background
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.jetpackcompose.app.features.apiService.LogAPI.SignInViewModel
import com.example.jetpackcompose.app.features.apiService.LogAPI.SignInViewModelFactory
import com.example.jetpackcompose.components.montserrat
import com.example.jetpackcompose.ui.theme.lightGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtherScreen(navController: NavHostController) {

    // Lấy SignInViewModel từ ViewModelProvider, sử dụng factory để cung cấp context
    val context = LocalContext.current
    val signInViewModel: SignInViewModel = viewModel(factory = SignInViewModelFactory(context))

    val customTypography = Typography(
        bodyLarge = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        bodyMedium = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        bodySmall = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        titleLarge = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        titleMedium = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        titleSmall = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        labelLarge = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        labelMedium = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        labelSmall = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        headlineLarge = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        headlineMedium = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat),
        headlineSmall = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.montserrat)
    )

    MaterialTheme(typography = customTypography) {
        Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        title = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 16.dp, end = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Khác",
                                    fontFamily = montserrat,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                    ),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                        },
                        colors = TopAppBarDefaults.smallTopAppBarColors(
                            containerColor = topBarColor
                        ),
                        modifier = Modifier
                            .height(50.dp)
                    )
                    Divider(
                        color = Color.LightGray,
                        thickness = 1.dp
                    )
                }
            }
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxHeight()
                    .background(color = lightGray)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                OtherTab(value = "Chi phí cố định và thu nhập định kỳ", painter = painterResource(id = R.drawable.preodic), onClick = {
                    navController.navigate("anual")
                })
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(24.dp))

                OtherTab(value = "Log out", painter = painterResource(id = R.drawable.logout), onClick = {
                    // Gọi clearToken từ SignInViewModel khi người dùng đăng xuất
                    Log.i("CheckToken", "${signInViewModel.getToken()}")
                    signInViewModel.clearToken()
                    Log.i("CheckToken", "${signInViewModel.getToken()}")
                    navController.navigate("signup")
                })
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp
                )

            }
        }
    }
}


