package com.example.jetpackcompose.app.screens.login_signup.forgot_password

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.apiService.ForgotPasswordAPI.ResetPasswordViewModel
import com.example.jetpackcompose.app.network.ResetPassword
import com.example.jetpackcompose.components.MessagePopup
import com.example.jetpackcompose.components.MyButtonComponent
import com.example.jetpackcompose.components.montserrat
import com.example.jetpackcompose.ui.theme.primaryColor
import com.example.jetpackcompose.ui.theme.textColor

@Composable
fun SetPasswordContent(navController: NavHostController, email: String) {

    val resetPasswordViewModel: ResetPasswordViewModel =
        ResetPasswordViewModel(LocalContext.current)

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordFocused by remember { mutableStateOf(false) }
    var isConfirmPasswordFocused by remember { mutableStateOf(false) }
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
                    painter = painterResource(id = R.drawable.password),
                    contentDescription = "Password Icon",
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Đặt mật khẩu mới",
                fontFamily = montserrat,
                color = textColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Mât khẩu mới của bạn phải chứa ít nhất 8 ký tự.",
                fontFamily = montserrat,
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Mật khẩu",
                fontFamily = montserrat,
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = if (!isPasswordFocused) {
                    {
                        Text(
                            "Nhập mật khẩu của bạn",
                            fontFamily = montserrat,
                            fontSize = 10.sp,
                            color = Color.LightGray
                        )
                    }
                } else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (isPasswordFocused) primaryColor else Color.LightGray,
                        RoundedCornerShape(8.dp)
                    )
                    .background(Color.Transparent)
                    .onFocusChanged { isPasswordFocused = it.isFocused },
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = montserrat
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = primaryColor
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                ),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Xác nhận mật khẩu",
                fontFamily = montserrat,
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = if (!isConfirmPasswordFocused) {
                    {
                        Text(
                            "Xác nhận mật khẩu của bạn",
                            fontFamily = montserrat,
                            fontSize = 10.sp,
                            color = Color.LightGray
                        )
                    }
                } else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (isConfirmPasswordFocused) primaryColor else Color.LightGray,
                        RoundedCornerShape(8.dp)
                    )
                    .background(Color.Transparent)
                    .onFocusChanged { isConfirmPasswordFocused = it.isFocused },
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = montserrat
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = primaryColor
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                ),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(20.dp))

            MyButtonComponent(
                value = "Đặt lại mật khẩu",
                isLoading = false,
                onClick = {
                    if (password.length < 8) {
                        errorMessage = "Mật khẩu phải chứa ít nhất 8 ký tự."
                        successMessage = ""
                        showPopup = true
                    } else if (password != confirmPassword) {
                        errorMessage = "Mật khẩu không khớp."
                        successMessage = ""
                        showPopup = true
                    } else if (password.isEmpty() || confirmPassword.isEmpty()) {
                        errorMessage = "Vui lòng nhập mật khẩu."
                        successMessage = ""
                        showPopup = true
                    } else {
                        val resetPasswordData = ResetPassword(
                            email = email,
                            newPassword = password,
                            confirmPassword = confirmPassword
                        )
                        Log.d("ResetPassword", "ResetPasswordData: $resetPasswordData")
                        resetPasswordViewModel.resetPassword(
                            data = resetPasswordData,
                            onSuccess = {
                                successMessage = "Mật khẩu đã được đặt lại, vui lòng đăng nhập."
                                errorMessage = ""
                                showPopup = true
                                navController.navigate("signin")
                                {
                                    popUpTo("signin") {
                                        inclusive = true
                                    }
                                }
                            },
                            onError = {
                                errorMessage = it
                                successMessage = ""
                                showPopup = true
                            }
                        )

                    }
                }
            )

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
            ProgressIndicator(steps = 3, currentStep = 3, spacing = 8.dp)
        }
    }
}


