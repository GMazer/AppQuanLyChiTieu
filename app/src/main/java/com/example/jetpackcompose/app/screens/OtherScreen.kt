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
import com.example.jetpackcompose.components.OtherFunction
import com.example.jetpackcompose.R
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.jetpackcompose.app.features.apiService.LogAPI.SignInViewModel
import com.example.jetpackcompose.app.features.apiService.LogAPI.SignInViewModelFactory
import com.example.jetpackcompose.components.MyButtonComponent
import com.example.jetpackcompose.components.montserrat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtherScreen(navController: NavHostController) {

    // Lấy SignInViewModel từ ViewModelProvider, sử dụng factory để cung cấp context
    val context = LocalContext.current
    val signInViewModel: SignInViewModel = viewModel(factory = SignInViewModelFactory(context))

    // Danh sách các biểu tượng tương ứng với từng mục
    val icons = listOf(
        painterResource(id = R.drawable.preodic),
        painterResource(id = R.drawable.notibalance),
        painterResource(id = R.drawable.logout)
    )

    MaterialTheme {
        Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        title = {
                            Box(
                                modifier = Modifier
                                    .background(color = Color(0xfff5f5f5))
                                    .height(50.dp)
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
                    .background(Color(0xfff5f5f5))
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    // Chuyển danh sách các mục và các hành động vào OtherFunction
                    OtherFunction(
                        items = listOf(
                            "Chi phí cố định và thu nhập định kỳ" to {
                                navController.navigate("anual")
                            },
                            "Danh sách giao dịch ngân hàng" to {
                                navController.navigate("transactionNotification")
                            },

                            "Đăng xuất" to {
                                // Gọi clearToken từ SignInViewModel khi người dùng đăng xuất
                                Log.i("CheckToken", "${signInViewModel.getToken()}")
                                signInViewModel.clearToken()
                                Log.i("CheckToken", "${signInViewModel.getToken()}")
                                navController.navigate("signup") {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                    launchSingleTop = true // Đảm bảo không tạo nhiều bản sao của cùng một màn hình
                                }
                            }
                        ),
                        painters = icons // Truyền danh sách biểu tượng vào
                    )
                }

            }
        }
    }
}




@Preview
@Composable
fun OtherScreenPreview() {
    OtherScreen(navController = NavHostController(LocalContext.current))
}


