package com.example.national_freeway.box

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.national_freeway.ui.theme.National_FreewayTheme
import kotlin.math.min

@Composable
fun TextInCenter(modifier: Modifier, content: @Composable () -> Unit) {
    BoxWithConstraints(modifier) {
        val offsetX = maxWidth * 0.5f
        Box(Modifier
            .fillMaxWidth()
            .offset(offsetX), Alignment.Center) {
            content()
        }
    }
}

@Composable
fun AutoText(modifier: Modifier = Modifier, content: @Composable (size: TextUnit) -> Unit) {
//    共用容器的情況讓不同的字在相對的位置？
    BoxWithConstraints(modifier.fillMaxSize()) {
//        轉成sp，讓字體固定為畫面比例
        val density = LocalDensity.current
        val minSize = with(density) { min(maxWidth.toPx(), maxHeight.toPx()) }
        val fontSize = with(density) { (minSize * 0.5f).toSp() }
        content(fontSize)
    }
}

@Composable
fun SpeedBox(modifier: Modifier = Modifier, speedText: String="24") {
    Box(modifier, Alignment.Center) {
//        限制字體可用空間0.7f
        AutoText(Modifier.fillMaxSize(0.8f)) { fontSize ->
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Red, fontSize = fontSize * 0.9)) {
                        append(speedText)
                    }
                    withStyle(style = SpanStyle(color = Color.Black, fontSize = fontSize * 0.3)) {
                        append(" km/h")
                    }
                })
            }
        }
    }
}

@Composable
fun SmileyFace(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Yellow,
    mouthCurve: Float = 0f // 負值微笑，正值變平或變苦瓜臉
) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // 背景
        drawCircle(
            color = backgroundColor,
            radius = size.minDimension / 2,
            center = Offset(canvasWidth / 2, canvasHeight / 2)
        )

        val eyeRadius = size.minDimension / 12

        // 左眼
        drawCircle(
            color = Color.Black,
            radius = eyeRadius,
            center = Offset(x = canvasWidth * 0.35f, y = canvasHeight * 0.35f)
        )
        // 右眼
        drawCircle(
            color = Color.Black,
            radius = eyeRadius,
            center = Offset(x = canvasWidth * 0.65f, y = canvasHeight * 0.35f)
        )

        // 嘴巴（用 quadratic curve）
        val mouthStart = Offset(x = canvasWidth * 0.3f, y = canvasHeight * 0.65f)
        val mouthEnd = Offset(x = canvasWidth * 0.7f, y = canvasHeight * 0.65f)
        val controlY = canvasHeight * (0.65f + mouthCurve) // 可控嘴角高度

        drawPath(
            path = Path().apply {
                moveTo(mouthStart.x, mouthStart.y)
                quadraticBezierTo(
                    x1 = canvasWidth / 2,
                    y1 = controlY,
                    x2 = mouthEnd.x,
                    y2 = mouthEnd.y
                )
            },
            color = Color.Black,
            style = Stroke(width = 8f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun Progress(modifier: Modifier = Modifier, speed: Float = 20f) {
    val shape = RoundedCornerShape(25.dp)
    Box(
        modifier
            .padding(8.dp)
            .background(Brush.horizontalGradient(listOf(Color.Green, Color.Red)), shape)
            .drawWithContent {
                drawContent() //  保留原本內容（背景）再繪製線條
                val sizeWidth = size.width
                val sizeHeight = size.height
//                val shortLineHeight = 5f
//                val longLineHeight = 20f
                val space = sizeWidth / 4 / 5 // 4個區間，每個區間5個小刻度
                val start = 0f
                var count = 0
                while (start + space * count < sizeWidth) {
//                    刻度綫，暫時不用
//                    val lineHeight = if (count % 5 == 0) longLineHeight else shortLineHeight
//                    val color = if (count % 5 == 0) Color.Red else Color.Black
//                    val x = start + space * count
//                    drawLine(color, Offset(x, 0f), Offset(x, lineHeight))
//                    drawLine(color, Offset(x, sizeHeight - lineHeight), Offset(x, sizeHeight))
//                    畫三角形
                    // 定義一個朝上的三角形
                    val speedRatio = speed / 120f
                    val topPointY = sizeHeight - 15f
                    val trianglePath = Path().apply {
                        moveTo(size.width * speedRatio, topPointY)              // 頂點（上方中央）
                        lineTo(size.width * speedRatio - 15f, topPointY + 30f)       // 左下角
                        lineTo(size.width * speedRatio + 15f, topPointY + 30f)       // 右下角
                        close()
                    }
                    drawPath(trianglePath, Color.Black)

                    count++
                }
//                畫兩條baseline
                drawLine(Color.Black, Offset(sizeWidth / 3 * 1, 0f), Offset(sizeWidth / 3 * 1, sizeHeight), 1f)
                drawLine(Color.Black, Offset(sizeWidth / 3 * 2, 0f), Offset(sizeWidth / 3 * 2, sizeHeight), 1f)
            },
        Alignment.Center
    ) {
        val spaceCount = 3
        val max = 120
        val space = max / spaceCount
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            for (i in 1..spaceCount) {
//                weight還是要佔3個
                TextInCenter(Modifier.weight(1f)) {
//                    最後一個不顯示
                    if (i < spaceCount) {
                        AutoText(Modifier.fillMaxSize()) { fontSize ->
                            Box(Modifier.fillMaxHeight(), Alignment.Center) {
                                Text("${space * i}", Modifier.fillMaxWidth(), fontSize = fontSize, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun transMouthCurve(speed: Float): Float {
    val maxSpeed = 120f
    val ratio = speed / maxSpeed
    return ratio - 0.33f
}

@Composable
fun SpeedCard(
    modifier: Modifier = Modifier,
    speedText: String = "40",
    bgBrush: Brush = SolidColor(Color.White),
    borderBrush: Brush = SolidColor(Color.Black)
) {
    val speed = speedText.toFloatOrNull() ?: 40f
    val rowModifier = Modifier.fillMaxWidth()
    val pd = 4.dp
    Column(modifier, Arrangement.spacedBy(pd)) {
        RectangleBox(rowModifier.weight(1f), bgBrush, borderBrush) {
            AutoText(Modifier.fillMaxSize()) { fz ->
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text("當前平均車速 $speedText", Modifier
                        .align(Alignment.Center)
                        .offset(y = (-5).dp), fontSize = fz * 1.2)
                }
            }
        }
        RoundCornerBox(rowModifier.weight(4f), bgBrush, borderBrush) {
            Column(verticalArrangement = Arrangement.spacedBy(pd)) {
                Row(rowModifier.weight(3f), Arrangement.spacedBy(pd)) {
                    SpeedBox(Modifier
                        .fillMaxSize()
                        .weight(1f), speedText)
                    val mouthCurve = transMouthCurve(speed)
                    SmileyFace(Modifier
                        .fillMaxSize()
                        .weight(1f), mouthCurve = mouthCurve)
                }
                Progress(rowModifier.weight(1f), speed = speed)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SpeedCardPreview() {
    National_FreewayTheme {
        SpeedCard(Modifier
            .fillMaxWidth()
            .height(250.dp))
    }
}