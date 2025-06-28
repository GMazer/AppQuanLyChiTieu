package com.example.jetpackcompose.app.screens.login_signup.forgot_password

import android.util.Log
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.apiService.ForgotPasswordAPI.SendOtpViewModel
import com.example.jetpackcompose.app.features.apiService.ForgotPasswordAPI.VerifyOtpViewModel
import com.example.jetpackcompose.app.network.SendOtp
import com.example.jetpackcompose.app.network.VerifyOtp
import com.example.jetpackcompose.components.MessagePopup
import com.example.jetpackcompose.components.MyButtonComponent
import com.example.jetpackcompose.components.myFont
import com.example.jetpackcompose.ui.theme.primaryColor
import com.example.jetpackcompose.ui.theme.textColor

@Composable
fun OTPContent(navController: NavHostController, email: String) {

    val sendOtpViewModel: SendOtpViewModel = SendOtpViewModel(LocalContext.current)
    val verifyOTP: VerifyOtpViewModel = VerifyOtpViewModel(LocalContext.current)

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var otpValues by remember { mutableStateOf(List(4) { "" }) }
    val focusRequesters = remember { List(4) { FocusRequester() } }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var showPopup by remember { mutableStateOf(false) }

    MessagePopup(
        successMessage = successMessage,
        errorMessage = errorMessage,
        showPopup = showPopup,
        onDismiss = { showPopup = false }
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
                    painter = painterResource(id = R.drawable.email),
                    contentDescription = "Email Icon",
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Khôi phục mật khẩu",
                fontFamily = myFont,
                color = textColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Chúng tôi đã gửi mã xác nhận đến email.",
                fontFamily = myFont,
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                otpValues.forEachIndexed { index, value ->
                    TextField(
                        value = value,
                        onValueChange = { newValue ->
                            if (newValue.length <= 1) {
                                otpValues = otpValues.toMutableList().apply {
                                    this[index] = newValue
                                }
                                if (newValue.isNotEmpty() && index < 3) {
                                    focusRequesters[index + 1].requestFocus()
                                }
                            }
                        },
                        modifier = Modifier
                            .focusRequester(focusRequesters[index])
                            .onKeyEvent { event ->
                                if (event.type == KeyEventType.KeyDown && event.key == Key.Backspace) {
                                    if (value.isEmpty() && index > 0) {
                                        otpValues = otpValues
                                            .toMutableList()
                                            .apply {
                                                this[index - 1] = ""
                                            }
                                        focusRequesters[index - 1].requestFocus()
                                    } else if (value.isNotEmpty()) {
                                        otpValues = otpValues
                                            .toMutableList()
                                            .apply {
                                                this[index] = ""
                                            }
                                    }
                                    true
                                } else {
                                    false
                                }
                            }
                            .width(75.dp)
                            .height(75.dp)
                            .padding(4.dp)
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .border(
                                2.dp,
                                if (value.isNotEmpty()) primaryColor else Color.LightGray,
                                RoundedCornerShape(8.dp)
                            ),
                        maxLines = 1,
                        textStyle = TextStyle(
                            fontSize = 28.sp,
                            fontFamily = myFont,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            textAlign = TextAlign.Center,
                            lineHeight = 70.sp // Ensures text is vertically centered
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = primaryColor
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            MyButtonComponent(
                value = "Tiếp tục",
                isLoading = false,
                onClick = {
                    val otp = otpValues.joinToString("")
                    val verifyOtpData = VerifyOtp(email = email, otp = otp)
                    if (otp.length != 4) {
                        errorMessage = "Vui lòng nhập mã OTP gồm 4 số"
                        successMessage = ""
                        showPopup = true
                        return@MyButtonComponent
                    } else {
                        verifyOTP.verifyOtp(
                            data = verifyOtpData,
                            onSuccess = {
                                successMessage = "Xác thực OTP thành công"
                                errorMessage = ""
                                showPopup = true
                                Log.d("OTP", "OTP verified successfully: $verifyOtpData")
                                navController.navigate("setPassword/$email") {
                                    popUpTo("setPassword") { inclusive = true }
                                }
                            },
                            onError = {
                                errorMessage = it
                                successMessage = ""
                                showPopup = true
                                Log.d("OTP", "OTP verification failed: $verifyOtpData")
                            }
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Không nhận được mã? ",
                    fontFamily = myFont,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Gửi lại",
                    fontFamily = myFont,
                    color = primaryColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable {
                        val sendOtp = SendOtp(email = email)
                        sendOtpViewModel.sendOtp(
                            data = sendOtp,
                            onSuccess = {
                                successMessage = "Gửi OTP thành công"
                                errorMessage = ""
                            },
                            onError = {
                                errorMessage = "Email không tồn tại"
                                successMessage = ""
                            }
                        )

                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            BackToLogin(navController = navController)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressIndicator(steps = 3, currentStep = 2, spacing = 8.dp)
        }
    }
}


