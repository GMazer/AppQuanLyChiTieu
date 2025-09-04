package com.example.jetpackcompose.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
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
import com.example.jetpackcompose.ui.theme.primaryColor
import com.example.jetpackcompose.ui.theme.textColor
import com.example.jetpackcompose.ui.theme.topBarColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CustomBottomAppBar(
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    selectedPage: Int,
    onPageSelected: (Int) -> Unit
) {
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
            BottomBarItem(
                title = "Báo cáo",
                iconRes = R.drawable.chart,
                isSelected = selectedPage == 0,
                onClick = { onPageSelected(0) }
            )

            BottomBarItem(
                title = "Lịch",
                iconRes = R.drawable.calendar,
                isSelected = selectedPage == 1,
                onClick = { onPageSelected(1) }
            )

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(primaryColor, shape = CircleShape)
                    .clickable {
                        onPageSelected(2)
                        coroutineScope.launch { pagerState.scrollToPage(2) }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_24),
                    contentDescription = "Nhập vào",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            BottomBarItem(
                title = "Ngân sách",
                iconRes = R.drawable.budget__2_,
                isSelected = selectedPage == 4,
                onClick = { onPageSelected(4) }
            )

            BottomBarItem(
                title = "Khác",
                iconRes = R.drawable.baseline_more_horiz_24,
                isSelected = selectedPage == 3,
                onClick = { onPageSelected(3) }
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
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onClick,
            colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Transparent),
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                tint = if (isSelected) primaryColor else textColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = title,
            color = if (isSelected) primaryColor else textColor,
            fontFamily = montserrat,
            fontSize = 8.sp
        )
    }
}

@Preview
@Composable
fun CustomBottomAppBarPreview() {
    val pagerState = rememberPagerState(
        pageCount = { 5 }
    )
    val coroutineScope = rememberCoroutineScope()
    CustomBottomAppBar(pagerState, coroutineScope, 0, {})
}



