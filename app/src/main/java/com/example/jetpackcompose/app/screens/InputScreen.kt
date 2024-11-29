package com.example.jetpackcompose.app.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetpackcompose.app.features.inputFeatures.InputTab

@Composable
fun InputScreen() {
    InputTab()
}

@Preview
@Composable
fun PreviewInputScreen () {
    InputScreen()
}
