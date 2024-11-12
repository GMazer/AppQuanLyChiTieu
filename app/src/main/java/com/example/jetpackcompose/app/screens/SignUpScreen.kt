package com.example.jetpackcompose.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.AppQuanLyChiTieu
import com.example.jetpackcompose.components.CheckboxComponent
import com.example.jetpackcompose.components.HeadingTextComponent
import com.example.jetpackcompose.components.MyTextFieldComponent
import com.example.jetpackcompose.components.NormalTextComponent
import com.example.jetpackcompose.components.PasswordTextFieldComponent

@Composable
fun SignUpScreen() {
    Surface(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(28.dp)

    ) {
        Column(modifier = Modifier.fillMaxSize())
        {
            NormalTextComponent(value = stringResource(id = R.string.hello))
            HeadingTextComponent(value = stringResource(id = R.string.create_account))
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

            Spacer(modifier = Modifier.height(10.dp))
            PasswordTextFieldComponent(
                labelValue = stringResource(id = R.string.re_enter_password),
                painterResource(id = R.drawable.outline_lock)
            )
            CheckboxComponent("Tôi đồng ý với các điều khoản và điều kiện chính sách của ứng dụng")
        }

    }
}



@Preview
@Composable
fun PreviewSignUpScreen() {
    SignUpScreen()
}