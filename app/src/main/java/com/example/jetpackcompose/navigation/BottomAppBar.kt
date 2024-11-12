package com.example.jetpackcompose.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.jetpackcompose.R // Đảm bảo import đúng R class
import com.example.jetpackcompose.ui.theme.colorPrimary
import androidx.compose.material3.IconButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetpackcompose.app.screens.SignUpScreen


@Composable
fun CustomBottomAppBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .border(1.dp, Color.LightGray, shape = RectangleShape)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { /* Chua co chuc nang */ },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = colorPrimary)
                ) {
                    Icon(Icons.Outlined.Edit, contentDescription = "Nhập vào")
                }
                Text(
                    "Nhập vào",
                    color = colorPrimary,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { /* Chua co chuc nang */ },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Gray)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_calendar_month_24),
                        contentDescription = "Lịch"
                    )
                }
                Text(
                    "Lịch",
                    color = Color.Gray,
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { /* Chua co chuc nang */ },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Gray)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.chart_donut),
                        contentDescription = "Báo cáo"
                    )
                }
                Text(
                    "Báo cáo",
                    color = Color.Gray,
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { /* Chua co chuc nang */ },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Gray)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_more_horiz_24),
                        contentDescription = "Khác"
                    )
                }
                Text(
                    "Khác",
                    color = Color.Gray,
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewSignUpScreen() {
    CustomBottomAppBar()
}