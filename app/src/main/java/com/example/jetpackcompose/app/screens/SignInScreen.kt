package com.example.jetpackcompose.app.screens
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.components.CheckboxComponent
import com.example.jetpackcompose.components.ClickableTextComponent
import com.example.jetpackcompose.components.HeadingTextComponent
import com.example.jetpackcompose.components.MyButtonComponent
import com.example.jetpackcompose.components.MyTextFieldComponent
import com.example.jetpackcompose.components.NormalTextComponent
import com.example.jetpackcompose.components.PasswordTextFieldComponent

@Composable
fun SignInScreen(navController: NavHostController) {
    Surface(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(28.dp)

    ) {
        Column(modifier = Modifier.fillMaxSize())
        {
            Image(
                painter = painterResource(id = R.drawable.logopng),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(120.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(40.dp))
            NormalTextComponent(value = stringResource(id = R.string.welcome_back))
            HeadingTextComponent(value = stringResource(id = R.string.sign_in))
            Spacer(modifier = Modifier.height(20.dp))
            MyTextFieldComponent(
                labelValue = stringResource(id = R.string.email_or_nummber),
                painterResource(id = R.drawable.profile)
            )

            Spacer(modifier = Modifier.height(10.dp))
            PasswordTextFieldComponent(
                labelValue = stringResource(id = R.string.enter_password),
                painterResource(id = R.drawable.outline_lock)
            )

            Spacer(modifier = Modifier.height(40.dp))
            MyButtonComponent("Đăng nhập", onClick = { /*TODO*/ })

            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                ClickableTextComponent("Chưa có tài khoản? Đăng ký ngay    ", onClick = {
                    navController.navigate("signup")
                })
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                ClickableTextComponent("Main screen    ", onClick = {
                    navController.navigate("mainscreen")
                })
            }

        }
    }
}

@Preview
@Composable
fun PreviewSignInScreen() {
    val context = LocalContext.current
    SignInScreen(navController = NavHostController(context))
}