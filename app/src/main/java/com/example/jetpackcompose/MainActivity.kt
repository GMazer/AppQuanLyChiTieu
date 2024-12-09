// MainActivity.kt
package com.example.jetpackcompose


import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import com.example.jetpackcompose.app.AppQuanLyChiTieu
import com.example.jetpackcompose.app.features.apiService.ReadNotificationTransaction.ReadTransactionNoti


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppQuanLyChiTieu()
//          PostList()
        }
    }
}


