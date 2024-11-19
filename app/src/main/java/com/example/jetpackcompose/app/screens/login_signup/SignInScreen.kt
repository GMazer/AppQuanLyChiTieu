package com.example.jetpackcompose.app.screens.login_signup

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.apiService.SignInViewModel
import com.example.jetpackcompose.app.network.LoginData
import com.example.jetpackcompose.components.CheckboxComponent
import com.example.jetpackcompose.components.ClickableTextComponent
import com.example.jetpackcompose.components.HeadingTextComponent
import com.example.jetpackcompose.components.MyButtonComponent
import com.example.jetpackcompose.components.MyTextFieldComponent
import com.example.jetpackcompose.components.NormalTextComponent
import com.example.jetpackcompose.components.PasswordTextFieldComponent

@Composable
fun SignInScreen(navController: NavHostController, context: Context, viewModel: SignInViewModel = SignInViewModel(context)) {
    val context = LocalContext.current
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

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

            Spacer(modifier = Modifier.height(40.dp))
            MyButtonComponent("Đăng nhập", onClick = {
                if (phoneNumber.isEmpty() || password.isEmpty()) {
                    errorMessage = "Vui lòng điền đầy đủ thông tin."
                } else {
                    val loginData = LoginData(phone_number = phoneNumber, password = password)
                    viewModel.signInUser(
                        data = loginData,
                        onSuccess = {
                            successMessage = it
                            navController.navigate("mainscreen")
                        },
                        onError = {
                            errorMessage = it
                        }
                    )
                }
            })

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = errorMessage, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
            }

            if (successMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Thread.sleep(3000)
                Text(text = successMessage, color = Color.Green, style = MaterialTheme.typography.bodyMedium)
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                ClickableTextComponent("Chưa có tài khoản? Đăng ký ngay", onClick = {
                    navController.navigate("signup")
                })
            }
        }
    }
}
