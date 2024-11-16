// MainActivity.kt
package com.example.jetpackcompose

import PostList
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetpackcompose.app.AppQuanLyChiTieu

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppQuanLyChiTieu()
//          PostList()
        }
    }
}


