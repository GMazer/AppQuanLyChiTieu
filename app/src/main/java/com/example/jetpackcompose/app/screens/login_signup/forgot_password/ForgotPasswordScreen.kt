package com.example.jetpackcompose.app.screens.login_signup.forgot_password

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.apiService.ForgotPasswordAPI.SendOtpViewModel
import com.example.jetpackcompose.app.network.SendOtp
import com.example.jetpackcompose.components.MyButtonComponent
import com.example.jetpackcompose.components.MyTextFieldComponent
import com.example.jetpackcompose.components.montserrat
import com.example.jetpackcompose.ui.theme.colorPrimary
import com.example.jetpackcompose.ui.theme.textColor

@Composable
fun ForgotPasswordScreen(navController: NavHostController) {

    val sendOtpViewModel: SendOtpViewModel = SendOtpViewModel(LocalContext.current)
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }


    Surface(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(28.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .border(0.5.dp, Color(0xffd5d5d5), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.fingerprint),
                    contentDescription = "Fingerprint Icon",
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Quên mật khẩu?",
                fontFamily = montserrat,
                color = textColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Đừng lo lắng, chúng tôi sẽ hướng dẫn bạn khôi phục.",
                fontFamily = montserrat,
                color = Color.Gray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            MyTextFieldComponent(
                value = email,
                onValueChange = { email = it },
                labelValue = "Nhập email của bạn",
                painterResource = painterResource(id = R.drawable.email)
            )

            Spacer(modifier = Modifier.height(20.dp))

            MyButtonComponent(
                value = "Đăt lại mật khẩu",
                isLoading = isLoading,
                onClick = {
                    val sendOtp = SendOtp(email = email)
                    if (email.isEmpty()) {
                        errorMessage = "Vui lòng nhập email của bạn."
                    } else {
                        isLoading = true

                        sendOtpViewModel.sendOtp(
                            data = sendOtp,
                            onSuccess = {
                                successMessage = "Gửi OTP thành công"
                                isLoading = false
                            },
                            onError = {
                                errorMessage = "Email không tồn tại"
                                isLoading = false
                            }
                        )

                        navController.navigate("otp/$email") {
                            popUpTo("otp") { inclusive = true }
                        }
                        successMessage = "Reset instructions sent to $email."
                        isLoading = false
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            BackToLogin(navController)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressIndicator(steps = 3, currentStep = 1, spacing = 8.dp)
        }
    }
}

@Composable
fun BackToLogin(navController: NavHostController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            navController.navigate("signin") {
                popUpTo("signin") { inclusive = true }
            }
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.arrow_back),
            contentDescription = "Back Arrow",
            tint = Color.Gray,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Trở lại đăng nhập",
            fontFamily = montserrat,
            color = Color.Gray,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ProgressIndicator(steps: Int, currentStep: Int, spacing: Dp = 4.dp) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        repeat(steps) { step ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .background(
                        color = if (step < currentStep) colorPrimary else Color.LightGray,
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
    }
}



