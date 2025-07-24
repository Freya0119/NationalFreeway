package com.example.national_freeway.box

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.national_freeway.ui.theme.National_FreewayTheme

@Composable
fun RectangleBox(
    modifier: Modifier = Modifier,
    bgBrush: Brush = SolidColor(Color.White),
    borderBrush: Brush = SolidColor(Color.Black),
    pd: Dp = 8.dp, content: @Composable () -> Unit
) {
    val shape = RectangleShape
    Surface(
        modifier = modifier
            .border(1.dp, borderBrush, shape)
            .background(bgBrush, shape)
            .padding(pd),
        color= Color.Transparent) {
        content()
    }
}

@Composable
fun RoundCornerBox(
    modifier: Modifier = Modifier,
    bgBrush: Brush = SolidColor(Color.White),
    borderBrush: Brush = SolidColor(Color.Black),
    pd: Dp = 8.dp, content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(15.dp)
    Surface(
        modifier = modifier
            .border(1.dp, borderBrush, shape)
            .background(bgBrush, shape)
            .padding(pd),
        color= Color.Transparent) {
        content()
    }
}

@Composable
fun BottomBox(
    modifier: Modifier = Modifier,
    timeText: String = "現在時間 2025-07-08 12:09:12",
    suggestionText: String = "建議用路人可避開尖峰時段（約早上7點至9點）的交通高峰，或考慮改道"
) {
    val paddingValue = 8.dp
    Column(modifier) {
        RectangleBox(pd = paddingValue) { Text(timeText, Modifier.fillMaxWidth()) }
        Spacer(Modifier.height(4.dp))
        RoundCornerBox(pd = paddingValue) { Text(suggestionText, Modifier.fillMaxSize()) }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    National_FreewayTheme {
        BottomBox()
    }
}