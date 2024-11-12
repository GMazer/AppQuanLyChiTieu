package com.example.jetpackcompose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
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
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.Category
import com.example.jetpackcompose.app.features.CustomTabRow
import com.example.jetpackcompose.app.features.ExpenseContent
import com.example.jetpackcompose.app.features.IncomeContent
import com.example.jetpackcompose.app.features.TabItem
import com.example.jetpackcompose.ui.theme.TextColor
import com.example.jetpackcompose.ui.theme.TextColorPrimary
import com.example.jetpackcompose.ui.theme.bgColor
import com.example.jetpackcompose.ui.theme.colorPrimary
import com.example.jetpackcompose.ui.theme.componentShapes
import com.example.jetpackcompose.ui.theme.highGray
val monsterrat = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_bold, FontWeight.Bold),
    Font(R.font.montserrat_light, FontWeight.Light)
)

@Composable
fun NormalTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .heightIn(min = 40.dp)
            .fillMaxWidth(),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
        ),
        color = TextColor,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun HeadingTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .heightIn(min = 80.dp)
            .fillMaxWidth(),
        style = TextStyle(
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
        ),
        color = TextColorPrimary,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun MyTextFieldComponent(labelValue: String, painterResource: Painter) {
    val textValue = remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth().clip(componentShapes.small),
        shape = RoundedCornerShape(10.dp),
        label = { Text(text = labelValue) },
        value = textValue.value,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorPrimary,
            focusedLabelColor = colorPrimary,
            unfocusedLabelColor = Color.LightGray,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = colorPrimary,
            textColor = TextColor,
            backgroundColor = bgColor
        ),
        keyboardOptions = KeyboardOptions.Default,
        onValueChange = {
            textValue.value = it
        },
        leadingIcon = {
            Icon(
                painter = painterResource,
                contentDescription = (""),
                tint = highGray
            )
        }
    )
}

@Composable
fun PasswordTextFieldComponent(labelValue: String, painterResource: Painter) {

    val password = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth().clip(componentShapes.small),
        shape = RoundedCornerShape(10.dp),
        label = { Text(text = labelValue) },
        value = password.value,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorPrimary,
            focusedLabelColor = colorPrimary,
            unfocusedLabelColor = Color.LightGray,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = colorPrimary,
            textColor = TextColor,
            backgroundColor = bgColor
        ),
        keyboardOptions = KeyboardOptions.Default,
        onValueChange = {
            password.value = it
        },
        leadingIcon = {
            Icon(
                painter = painterResource,
                contentDescription = (""),
                tint = highGray
            )
        },
        trailingIcon = {
            val icon = if (passwordVisibility.value) {
                painterResource(R.drawable.outline_visibility_off_24)
            } else {
                painterResource(R.drawable.outline_visibility)
            }
            var description = if (passwordVisibility.value) {
                "Ẩn mật khẩu"
            } else {
                "Hiện mật khẩu"
            }
            Icon(
                painter = icon,
                contentDescription = (""),
                tint = highGray,
                modifier = Modifier
                    .clickable {
                        passwordVisibility.value = !passwordVisibility.value
                    }
            )
        },
        visualTransformation = if (passwordVisibility.value) VisualTransformation.None else
        PasswordVisualTransformation()
    )
}

@Composable
fun CheckboxComponent(value: String){
    val checkedState = remember { mutableStateOf(false) }
    Row(modifier = Modifier
        .fillMaxWidth()
        .heightIn(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ){

        val checkedState = remember { mutableStateOf(false) }
        Checkbox(checked = checkedState.value,
            onCheckedChange = {
                checkedState.value != checkedState.value
            })
        Text(value,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = Color(0xFF777777),
        )
    }
}

@Composable
fun BottomLine(height: Dp) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .drawBehind {
                val strokeWidth = 0.8.dp.toPx()
                val y = size.height / 2
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
    )
}

@Composable
fun TopLine(height: Dp) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .drawBehind {
                val strokeWidth = 0.8.dp.toPx()
                val y = strokeWidth / 2
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
    )
}

@Composable
fun CategoriesGrid(categories: List<Category>, buttonColor: Color) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),  // Số cột
        contentPadding = PaddingValues(4.dp),  // Padding xung quanh grid
        horizontalArrangement = Arrangement.spacedBy(4.dp),  // Khoảng cách ngang giữa các items
        verticalArrangement = Arrangement.spacedBy(4.dp)  // Khoảng cách dọc giữa các items
    ) {
        items(categories) { category ->
            CategoryItem(category, buttonColor)
        }
    }
}

// Tạo một item trong grid danh mục chi tiêu
@Composable
fun CategoryItem(category: Category, buttonColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .padding(4.dp)
            .border(
                BorderStroke(1.dp, Color.LightGray),
                RoundedCornerShape(4.dp)
            )  // Viền và bo góc
    ) {
        IconButton(
            onClick = { /* Chưa có chức năng */ },
            colors = IconButtonDefaults.iconButtonColors(contentColor = category.iconColor),
            modifier = Modifier
                .padding(top = 4.dp, bottom = 2.dp) // Khoảng cách giữa icon và text
                .size(24.dp) // Kích thước icon
        ) {
            androidx.compose.material3.Icon(painter = category.iconPainter(), contentDescription = category.name)
        }
        androidx.compose.material3.Text(
            category.name,
            color = Color.Gray,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis  // Ẩn bớt phần text nếu quá dài
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabMoney() {
    val customTypography = Typography(
        bodyLarge = TextStyle(fontFamily = com.example.jetpackcompose.app.features.monsterrat),
        bodyMedium = TextStyle(fontFamily = com.example.jetpackcompose.app.features.monsterrat),
        bodySmall = TextStyle(fontFamily = com.example.jetpackcompose.app.features.monsterrat),
        titleLarge = TextStyle(fontFamily = com.example.jetpackcompose.app.features.monsterrat),
        titleMedium = TextStyle(fontFamily = com.example.jetpackcompose.app.features.monsterrat),
        titleSmall = TextStyle(fontFamily = com.example.jetpackcompose.app.features.monsterrat),
        labelLarge = TextStyle(fontFamily = com.example.jetpackcompose.app.features.monsterrat),
        labelMedium = TextStyle(fontFamily = com.example.jetpackcompose.app.features.monsterrat),
        labelSmall = TextStyle(fontFamily = com.example.jetpackcompose.app.features.monsterrat),
        headlineLarge = TextStyle(fontFamily = com.example.jetpackcompose.app.features.monsterrat),
        headlineMedium = TextStyle(fontFamily = com.example.jetpackcompose.app.features.monsterrat),
        headlineSmall = TextStyle(fontFamily = com.example.jetpackcompose.app.features.monsterrat)
    )

    var tabs = listOf(
        TabItem("Income", icon =  Icons.Default.ArrowBack){
            IncomeContent()
        },
        TabItem("Expense", icon =  Icons.Default.ArrowForward){
            ExpenseContent()
        }
    )

    var pagerState = rememberPagerState (
        pageCount = {tabs.size}
    )

    var coroutineScope = rememberCoroutineScope()

    MaterialTheme(
        typography = customTypography
    ) {
        var tabIndex by remember { mutableStateOf(0) }
        val tabTitles = listOf("Tiền chi", "Tiền thu")


        Column(modifier = Modifier
            .background(Color(0xFFF1F1F1))
            .fillMaxSize()) {
            // Đặt CustomTabRow bên ngoài Scaffold
            CustomTabRow(
                tabIndex = tabIndex,
                onTabSelected = { tabIndex = it },
                titles = tabTitles,
                listtab = tabs,
                pagerStatement = pagerState,
                coroutineScoper = coroutineScope,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            HorizontalPager(state = pagerState) {
                tabs[it].screen()
            }

        }
    }
}

@Composable
fun MyButtonComponent(value: String, onClick: () -> Unit)
{
    Button(
        onClick = { /* Handle save */ },
        colors = ButtonDefaults.buttonColors(containerColor = colorPrimary)
    ) {
        androidx.compose.material3.Text(
            value,
            color = Color.White,
            fontWeight = FontWeight.Bold)
    }
}