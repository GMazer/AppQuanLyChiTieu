package com.example.jetpackcompose.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackcompose.R
import com.example.jetpackcompose.components.montserrat
import com.example.jetpackcompose.ui.theme.colorPrimary
import com.example.jetpackcompose.ui.theme.textColor
import com.example.jetpackcompose.ui.theme.topBarColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CustomBottomAppBar(pagerState: PagerState, coroutineScope: CoroutineScope) {
    var selectedPage by rememberSaveable { mutableStateOf(0) }


    Box(
        modifier = Modifier
            .background(topBarColor)
            .fillMaxWidth()
            .height(60.dp)
            .border(1.dp, Color.LightGray, shape = RectangleShape)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Các mục
            BottomBarItem(
                title = "Nhập vào",
                iconRes = R.drawable.edit,
                isSelected = selectedPage == 0,
                onClick = {
                    selectedPage = 0
                    coroutineScope.launch { pagerState.scrollToPage(0) }
                }
            )

            BottomBarItem(
                title = "Lịch",
                iconRes = R.drawable.outline_calendar_month_24,
                isSelected = selectedPage == 1,
                onClick = {
                    selectedPage = 1
                    coroutineScope.launch { pagerState.scrollToPage(1) }
                }
            )

            BottomBarItem(
                title = "Báo cáo",
                iconRes = R.drawable.chart_donut,
                isSelected = selectedPage == 2,
                onClick = {
                    selectedPage = 2
                    coroutineScope.launch { pagerState.scrollToPage(2) }
                }
            )

            BottomBarItem(
                title = "Khác",
                iconRes = R.drawable.baseline_more_horiz_24, // Giả sử bạn có icon cho mục "Khác"
                isSelected = selectedPage == 3,
                onClick = {
                    selectedPage = 3
                    coroutineScope.launch { pagerState.scrollToPage(3) }
                }
            )
        }
    }
}

@Composable
fun BottomBarItem(
    title: String,
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Đảm bảo các thành phần căn chỉnh đều ở giữa
    ) {
        IconButton(
            onClick = onClick,
            colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Transparent),
            modifier = Modifier.padding(0.dp)
                .size(30.dp)// Giảm khoảng cách giữa icon và text
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                tint = if (isSelected) colorPrimary else textColor,
                modifier = Modifier.size(20.dp)
                        .padding(0.dp)
            )
        }
        // Nothing
        Text(
            text = title,
            color = if (isSelected) colorPrimary else textColor,
            fontFamily = montserrat,
            fontSize = 8.sp,
            modifier = Modifier.padding(top = 0.dp) // Giảm khoảng cách giữa text và icon
        )
    }
}



