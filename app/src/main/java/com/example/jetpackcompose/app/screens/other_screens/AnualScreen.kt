package com.example.jetpackcompose.app.screens.other_screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.components.OtherTab

@Composable
fun AnualScreen(navController: NavHostController) {
    OtherTab(value = "Back", painter = painterResource(R.drawable.logopng), onClick = {
        navController.popBackStack()
    })
}