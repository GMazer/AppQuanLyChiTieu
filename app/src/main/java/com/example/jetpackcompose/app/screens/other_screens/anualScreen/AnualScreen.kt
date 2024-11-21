package com.example.jetpackcompose.app.screens.other_screens.anualScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.components.OtherTab

@Composable
fun AnualScreen(navController: NavHostController) {
    OtherTab(value = "Back", painter = painterResource(R.drawable.logopng), onClick = {
        navController.popBackStack()
    })
}