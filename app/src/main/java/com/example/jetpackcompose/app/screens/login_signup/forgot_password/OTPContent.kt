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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.components.MyButtonComponent
import com.example.jetpackcompose.components.montserrat
import com.example.jetpackcompose.ui.theme.colorPrimary
import com.example.jetpackcompose.ui.theme.textColor

@Composable
fun OTPContent(navController: NavHostController) {
    var otpValue by remember { mutableStateOf("") }

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
                text = "Password reset",
                fontFamily = montserrat,
                color = textColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We sent a code to your email",
                fontFamily = montserrat,
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
                repeat(4) { index ->
                    TextField(
                        value = if (otpValue.length > index) otpValue[index].toString() else "",
                        onValueChange = { newValue ->
                            if (newValue.length == 1 && otpValue.length <= index) {
                                otpValue += newValue
                            }
                        },
                        modifier = Modifier
                            .width(60.dp)
                            .height(60.dp)
                            .padding(4.dp)
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .border(0.5.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                        maxLines = 1,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(8.dp),
                        textStyle = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = montserrat,
                            fontWeight = FontWeight.Medium,
                            color = textColor
                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            MyButtonComponent(
                value = "Continue",
                isLoading = false,
                onClick = {
                    // Handle OTP submission
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Didn't receive the email? Click to resend",
                fontFamily = montserrat,
                color = Color.Blue,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable {
                    // Resend OTP logic
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            BackToLogin(navController = navController)
        }
    }
}

@Preview
@Composable
fun OTPContentPreview() {
    OTPContent(
        navController = NavHostController(
            context = LocalContext.current
        )
    )
}
