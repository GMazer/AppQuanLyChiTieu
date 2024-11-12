package com.example.jetpackcompose.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.CheckboxDefaults.colors
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.Category
import com.example.jetpackcompose.app.features.CustomTabRow
import com.example.jetpackcompose.app.features.ExpenseContent
import com.example.jetpackcompose.app.features.IncomeContent
import com.example.jetpackcompose.app.features.TabItem
import com.example.jetpackcompose.app.screens.SignInScreen
import com.example.jetpackcompose.ui.theme.TextColor
import com.example.jetpackcompose.ui.theme.TextColorPrimary
import com.example.jetpackcompose.ui.theme.bgColor
import com.example.jetpackcompose.ui.theme.colorPrimary
import com.example.jetpackcompose.ui.theme.colorSecondary
import com.example.jetpackcompose.ui.theme.componentShapes
import com.example.jetpackcompose.ui.theme.highGray
import com.example.jetpackcompose.app.screens.InputScreen
import com.example.jetpackcompose.app.screens.CalendarScreen
import com.example.jetpackcompose.app.screens.ReportScreen
import com.example.jetpackcompose.app.screens.OtherScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.InputStream

@Composable
fun CustomBottomAppBar(pagerState: PagerState, coroutineScope: CoroutineScope) {
    // Trạng thái để theo dõi màu sắc khi nhấn vào các mục
    var selectedPage by remember { mutableStateOf(0) }

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
        ) {
            // Mục Nhập vào
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {
                        selectedPage = 0 // Đánh dấu là nút "Nhập vào" được nhấn
                        coroutineScope.launch { pagerState.scrollToPage(0) }
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.Transparent // Loại bỏ màu nền
                    )
                ) {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = "Nhập vào",
                        tint = if (selectedPage == 0) colorPrimary else Color.Gray // Sử dụng tint thay vì contentColor
                    )
                }
                Text("Nhập vào", color = if (selectedPage == 0) colorPrimary else Color.Gray, fontSize = 12.sp)
            }

            // Mục Lịch
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {
                        selectedPage = 1 // Đánh dấu là nút "Lịch" được nhấn
                        coroutineScope.launch { pagerState.scrollToPage(1) }
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.Transparent // Loại bỏ màu nền
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_calendar_month_24),
                        contentDescription = "Lịch",
                        tint = if (selectedPage == 1) colorPrimary else Color.Gray // Sử dụng tint thay vì contentColor
                    )
                }
                Text("Lịch", color = if (selectedPage == 1) colorPrimary else Color.Gray, fontSize = 12.sp)
            }

            // Mục Báo cáo
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {
                        selectedPage = 2 // Đánh dấu là nút "Báo cáo" được nhấn
                        coroutineScope.launch { pagerState.scrollToPage(2) }
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.Transparent // Loại bỏ màu nền
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.chart_donut),
                        contentDescription = "Báo cáo",
                        tint = if (selectedPage == 2) colorPrimary else Color.Gray // Sử dụng tint thay vì contentColor
                    )
                }
                Text("Báo cáo", color = if (selectedPage == 2) colorPrimary else Color.Gray, fontSize = 12.sp)
            }

            // Mục Khác
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {
                        selectedPage = 3 // Đánh dấu là nút "Khác" được nhấn
                        coroutineScope.launch { pagerState.scrollToPage(3) }
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.Transparent // Loại bỏ màu nền
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_more_horiz_24),
                        contentDescription = "Khác",
                        tint = if (selectedPage == 3) colorPrimary else Color.Gray // Sử dụng tint thay vì contentColor
                    )
                }
                Text("Khác", color = if (selectedPage == 3) colorPrimary else Color.Gray, fontSize = 12.sp)
            }
        }
    }
}
