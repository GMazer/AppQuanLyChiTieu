package com.example.jetpackcompose.components

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Text
//noinspection UsingMaterialAndMaterial3Libraries
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import com.example.jetpackcompose.R
import com.example.jetpackcompose.app.features.inputFeatures.Category
import com.example.jetpackcompose.app.features.inputFeatures.CustomTabRow
import com.example.jetpackcompose.app.features.inputFeatures.OutComeContent
import com.example.jetpackcompose.app.features.inputFeatures.IncomeContent
import com.example.jetpackcompose.app.features.inputFeatures.TabItem
import com.example.jetpackcompose.ui.theme.TextColor
import com.example.jetpackcompose.ui.theme.TextColorPrimary
import com.example.jetpackcompose.ui.theme.bgColor
import com.example.jetpackcompose.ui.theme.colorPrimary
import com.example.jetpackcompose.ui.theme.colorSecondary
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
            fontSize = 22.sp,
            fontFamily = monsterrat,
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
            .heightIn(min = 60.dp)
            .fillMaxWidth(),
        style = TextStyle(
            fontSize = 36.sp,
            fontFamily = monsterrat,
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
        label = { Text(
            text = labelValue,
            fontFamily = monsterrat,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = Color.LightGray
        ) },
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
        label = { Text(
            text = labelValue,
            fontFamily = monsterrat,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = Color.LightGray
        ) },
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
fun CheckboxComponent(value: String) {
    val checkedState = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkedState.value,
            onCheckedChange = { checkedState.value = it },
            colors =  CheckboxDefaults.colors(
                checkedColor = colorPrimary,
            )
        )
        Text(
            value,
            fontFamily = monsterrat,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = Color(0xFF777777),
        )
    }
}

@Composable
fun DrawBottomLine(height: Dp) {
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
fun DrawTopLine(height: Dp) {
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

//Tạo layout với LazyColumn
@Composable
fun CategoriesGrid(
    categories: List<Category>,
    buttonColor: Color,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(categories) { category ->
            CategoryItem(
                category = category,
                buttonColor = buttonColor,
                isSelected = (category == selectedCategory),
                onClick = { onCategorySelected(category) }
            )
        }
    }
}



// Tạo một item trong grid danh mục chi tiêu
@Composable
fun CategoryItem(
    category: Category,
    buttonColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Màu viền sẽ là màu cam nếu item được chọn, ngược lại là màu xám nhạt
    val borderColor = if (isSelected) buttonColor else Color.LightGray

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .padding(4.dp)
            .border(
                BorderStroke(1.dp, borderColor),
                RoundedCornerShape(4.dp)
            )
            .clickable { onClick() }
    ) {
        IconButton(
            onClick = { onClick() },
            colors = IconButtonDefaults.iconButtonColors(contentColor = category.iconColor),
            modifier = Modifier
                .padding(top = 4.dp, bottom = 2.dp)
                .size(24.dp)
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
fun InputTab() {
    val customTypography = Typography(
        bodyLarge = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        bodyMedium = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        bodySmall = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        titleLarge = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        titleMedium = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        titleSmall = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        labelLarge = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        labelMedium = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        labelSmall = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        headlineLarge = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        headlineMedium = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat),
        headlineSmall = TextStyle(fontFamily = com.example.jetpackcompose.app.features.inputFeatures.monsterrat)
    )

    var tabs = listOf(
        TabItem("Expense", icon =  Icons.Default.ArrowBack){
            OutComeContent()
        },
        TabItem("Income", icon =  Icons.Default.ArrowForward){
            IncomeContent()
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
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = colorPrimary),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(top = 16.dp),
    ) {
        Text(
            value,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp)
    }
}

@Composable
fun ClickableTextComponent(value: String, onClick: () -> Unit) {
    Text(
        value,
        color = colorSecondary,
        fontFamily = monsterrat,
        fontWeight = FontWeight.Light,
        fontSize = 8.sp,
        modifier = Modifier
            .padding(top = 5.dp)
            .clickable(onClick = onClick)
    )
}



@Composable
fun NumberTextField(amountState: String, onValueChange: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = amountState,
        onValueChange = { newInput ->
            if (newInput == "0" && amountState.isEmpty()) {
                // do nothing to block the first '0'
            } else {
                val filteredInput = newInput.filter { it.isDigit() }
                onValueChange(
                    if (filteredInput.isNotEmpty() && filteredInput != "0") {
                        filteredInput
                    } else if (filteredInput == "0" && !amountState.isEmpty()) {
                        filteredInput
                    } else {
                        ""
                    }
                )
            }
        },
        singleLine = true,
        modifier = Modifier
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .height(45.dp)
            .width(250.dp)
            .background(Color(0xFFe1e1e1), shape = RoundedCornerShape(8.dp))
            .border(1.dp, if (isFocused) colorPrimary else Color.Transparent, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp),
        textStyle = TextStyle(
            textAlign = TextAlign.Start,
            fontSize = 20.sp,
            fontFamily = monsterrat,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()  // Clear focus from the text field
                keyboardController?.hide()  // Hide the keyboard
            }
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                if (amountState.isEmpty()) {
                    Text(
                        if (isFocused) "" else "0",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontFamily = monsterrat,
                        fontSize = 20.sp,
                        style = LocalTextStyle.current
                    )
                }
                innerTextField()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteTextField(textState: TextFieldValue, onValueChange: (TextFieldValue) -> Unit)
{
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var textNote by remember { mutableStateOf("") }
    androidx.compose.material3.OutlinedTextField(
        modifier = Modifier
            .width(250.dp),
        value = textState,
        onValueChange = onValueChange,
        colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorPrimary,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = colorPrimary
        ),
        placeholder = { Text(
            "Chưa nhập vào",
            color = Color.LightGray,
            fontFamily = monsterrat,
        ) },
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontFamily = monsterrat,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                textNote = textState.toString()
                focusManager.clearFocus()  // Clear focus from the text field
                keyboardController?.hide()  // Hide the keyboard
            }
        ),
    )
}

@Preview
@Composable
fun PreviewInputScreen() {
    InputTab()
}






