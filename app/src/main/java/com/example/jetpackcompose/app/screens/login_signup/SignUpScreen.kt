package com.example.jetpackcompose.app.screens.login_signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.apiService.LogAPI.SignUpViewModel
import com.example.jetpackcompose.app.network.RegistrationData
import com.example.jetpackcompose.components.CheckboxComponent
import com.example.jetpackcompose.components.ClickableTextComponent
import com.example.jetpackcompose.components.HeadingTextComponent
import com.example.jetpackcompose.components.MyButtonComponent
import com.example.jetpackcompose.components.MyTextFieldComponent
import com.example.jetpackcompose.components.NormalTextComponent
import com.example.jetpackcompose.components.PasswordTextFieldComponent

@Composable
fun SignUpScreen(navController: NavHostController, viewModel: SignUpViewModel = SignUpViewModel()) {
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var retypePassword by remember { mutableStateOf("") }
    var agreeToTerms by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(28.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.logopng),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(120.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(30.dp))
            NormalTextComponent(value = stringResource(id = R.string.hello))
            HeadingTextComponent(value = stringResource(id = R.string.create_account))
            Spacer(modifier = Modifier.height(10.dp))
            MyTextFieldComponent(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                labelValue = stringResource(id = R.string.number),
                painterResource = painterResource(id = R.drawable.profile)
            )
            MyTextFieldComponent(
                value = email,
                onValueChange = { email = it },
                labelValue = stringResource(id = R.string.email),
                painterResource = painterResource(id = R.drawable.profile)
            )
            PasswordTextFieldComponent(
                value = password,
                onValueChange = { password = it },
                labelValue = stringResource(id = R.string.enter_password),
                painterResource = painterResource(id = R.drawable.outline_lock)
            )
            PasswordTextFieldComponent(
                value = retypePassword,
                onValueChange = { retypePassword = it },
                labelValue = stringResource(id = R.string.re_enter_password),
                painterResource = painterResource(id = R.drawable.outline_lock)
            )
            CheckboxComponent(
                text = "Tôi đồng ý với các điều khoản và điều kiện chính sách của ứng dụng",
                checked = agreeToTerms,
                onCheckedChange = { agreeToTerms = it }
            )
            Spacer(modifier = Modifier.height(10.dp))
            MyButtonComponent(
                "Đăng ký",
                isLoading = isLoading,
                onClick = {
                    if (phoneNumber.isEmpty() || email.isEmpty() || password.isEmpty() || retypePassword.isEmpty()) {
                        errorMessage = "Vui lòng điền đầy đủ thông tin."
                    } else if (password != retypePassword) {
                        errorMessage = "Mật khẩu và xác nhận mật khẩu không trùng khớp."
                    } else if (!agreeToTerms) {
                        errorMessage = "Vui lòng đồng ý với điều khoản và chính sách."
                    } else {
                        val registrationData = RegistrationData(
                            phone_number = phoneNumber,
                            email = email,
                            password = password,
                            retype_password = retypePassword
                        )
                        viewModel.registerUser(
                            data = registrationData,
                            onSuccess = {
                                successMessage = it
                                navController.navigate("signin")
                                {
                                    launchSingleTop = true
                                }
                            },
                            onError = {
                                errorMessage = it
                            }
                        )
                    }
                })
            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (successMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Thread.sleep(3000)
                Text(
                    text = successMessage,
                    color = Color.Green,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                ClickableTextComponent("Đã có tài khoản? Đăng nhập ngay", onClick = {
                    navController.navigate("signin") {
                        popUpTo("signin") { inclusive = true }
                    }
                })
            }
        }
    }
}
