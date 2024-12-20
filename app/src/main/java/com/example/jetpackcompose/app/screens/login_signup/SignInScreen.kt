// SignInScreen.kt
package com.example.jetpackcompose.app.screens.login_signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.apiService.LogAPI.SignInViewModel
import com.example.jetpackcompose.app.features.apiService.LogAPI.SignInViewModelFactory
import com.example.jetpackcompose.app.network.LoginData
import com.example.jetpackcompose.components.ClickableTextComponent
import com.example.jetpackcompose.components.HeadingTextComponent
import com.example.jetpackcompose.components.MessagePopup
import com.example.jetpackcompose.components.MyButtonComponent
import com.example.jetpackcompose.components.MyTextFieldComponent
import com.example.jetpackcompose.components.NormalTextComponent
import com.example.jetpackcompose.components.PasswordTextFieldComponent
import com.example.jetpackcompose.components.montserrat
import com.example.jetpackcompose.ui.theme.colorPrimary
import com.example.jetpackcompose.ui.theme.textColor

@Composable
fun SignInScreen(navController: NavHostController) {
    // Lấy Context từ Composable
    val context = LocalContext.current

    // Khởi tạo SignInViewModel với Factory
    val signInViewModel: SignInViewModel = viewModel(
        factory = SignInViewModelFactory(context) // Cung cấp Context cho ViewModel
    )

    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showPopup by remember { mutableStateOf(false) }

    MessagePopup(
        showPopup = showPopup,
        successMessage = successMessage,
        errorMessage = errorMessage,
        onDismiss = { showPopup = false } // Đóng popup khi nhấn ngoài
    )

    Surface(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(28.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.logopng),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(120.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(40.dp))
            NormalTextComponent(value = stringResource(id = R.string.hello))
            HeadingTextComponent(value = stringResource(id = R.string.sign_in))
            Spacer(modifier = Modifier.height(20.dp))

            MyTextFieldComponent(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                labelValue = stringResource(id = R.string.email_or_nummber),
                painterResource = painterResource(id = R.drawable.profile)
            )

            Spacer(modifier = Modifier.height(10.dp))
            PasswordTextFieldComponent(
                value = password,
                onValueChange = { password = it },
                labelValue = stringResource(id = R.string.enter_password),
                painterResource = painterResource(id = R.drawable.outline_lock)
            )

            Spacer(modifier = Modifier.height(20.dp))
            MyButtonComponent(
                "Đăng nhập",
                isLoading = isLoading,
                onClick = {
                    isLoading = true
                    if (phoneNumber.isEmpty() || password.isEmpty()) {
                        errorMessage = "Vui lòng điền đầy đủ thông tin."
                        isLoading = false
                    } else {
                        val loginData = LoginData(phone_number = phoneNumber, password = password)
                        signInViewModel.signInUser(
                            data = loginData,
                            onSuccess = {
                                errorMessage = ""
                                successMessage = it
                                showPopup = true
                                navController.navigate("mainscreen")
                            },
                            onError = {
                                errorMessage = it
                                showPopup = true
                                isLoading = false
                            }
                        )
                    }
                }
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                ClickableTextComponent("Quên mật khẩu?", onClick = {
                    navController.navigate("forgotpassword") {
                        popUpTo("forgotpassword") { inclusive = true }
                    }
                })
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = "Chưa có tài khoản? ",
                        fontFamily = montserrat,
                        fontSize = 10.sp,
                        color = Color(0xFFB0BEC5), // Light gray color
                    )
                    Text(
                        text = "Đăng ký",
                        color = Color.Gray, // Highlight color
                        fontFamily = montserrat,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable {
                            navController.navigate("signup") {
                                popUpTo("signup") { inclusive = true }
                            }
                        })
                }
            }
        }
    }
}


