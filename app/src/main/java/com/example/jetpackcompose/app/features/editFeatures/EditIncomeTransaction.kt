package com.example.jetpackcompose.app.features.editFeatures

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun EditIncomeTransaction(navController: NavHostController) {
    Column {
        Text("Edit Income Transaction", modifier = Modifier.clickable(onClick = {
            navController.popBackStack("mainscreen", inclusive = false)
        }).padding(16.dp))
    }
}