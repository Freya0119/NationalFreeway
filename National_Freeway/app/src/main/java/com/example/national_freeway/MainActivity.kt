package com.example.national_freeway

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.national_freeway.box.BottomBox
import com.example.national_freeway.box.SpeedCard
import com.example.national_freeway.ui.theme.National_FreewayTheme
import com.example.national_freeway.web.viewModel.SocketsVM
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            National_FreewayTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Init(innerPadding)
                }
            }
        }
    }
}

@Composable
fun Init(innerPadding: PaddingValues) {
    Log.d("RECOMPOSE", "Recompose Init.")

    val wsVM = SocketsVM()
    Surface(Modifier
        .fillMaxSize()
        .padding(innerPadding)) {
        MainView(wsVM)
    }
}

@Composable
fun MainView(vm: SocketsVM) {
//fun MainView() {
    Log.d("RECOMPOSE", "Recompose MainView.")

    val item by vm.responseItem.collectAsState()

    Box(Modifier.fillMaxSize()) {
        MapScreen()
        val pd = 4.dp
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .fillMaxHeight(0.25f)
                .padding(pd),
            Arrangement.spacedBy(pd)
        ) {
//            SpeedCard
            Row(Modifier.fillMaxWidth().weight(4f), Arrangement.spacedBy(pd)) {
                val fillWidthModifier = Modifier.weight(1f)
                SpeedCard(fillWidthModifier, item?.Average_Speed ?: "40", SolidColor(Color(0xFFFFCDD2)), SolidColor(Color(0xFFD32F2F)))
                SpeedCard(fillWidthModifier, item?.Quantity ?: "40", SolidColor(Color(0xFFBBDEFB)), SolidColor(Color(0xFF1976D2)))
            }
//            Button
            Row(Modifier.fillMaxWidth().weight(1f),
                Arrangement.spacedBy(pd), Alignment.CenterVertically) {
                Button(onClick = { vm.wsSendText(0) }, modifier = Modifier.weight(1f)) { Text("門架1") }
                Button(onClick = { vm.wsSendText(1) }, modifier = Modifier.weight(1f)) { Text("門架2") }
                Button(onClick = { vm.wsSendText(2) }, modifier = Modifier.weight(1f)) { Text("門架3") }
                Button(onClick = { vm.wsSendText(3) }, modifier = Modifier.weight(1f)) { Text("門架4") }
//                Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("門架1") }
//                Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("門架2") }
//                Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("門架3") }
//                Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("門架4") }
            }
        }

        Box(Modifier.fillMaxSize(), Alignment.BottomCenter) {
            BottomBox(Modifier.fillMaxWidth().fillMaxHeight(0.2f))
        }
    }
}

@Composable
fun MapScreen(modifier: Modifier = Modifier) {
//    Surface(modifier, color = Color.Transparent) {}
    val points = listOf(
        LatLng(25.0330, 121.5654), // 台北101
        LatLng(25.0375, 121.5637), // 世貿站
        LatLng(25.0418, 121.5500)  // 國父紀念館
    )

    val cameraPositionState = rememberCameraPositionState()
    val mapLoaded = remember { mutableStateOf(false) }

    // 只有地圖載入完成後再做鏡頭移動
    LaunchedEffect(mapLoaded.value) {
        if (mapLoaded.value) {
            val boundsBuilder = LatLngBounds.builder()
            points.forEach { boundsBuilder.include(it) }
            val bounds = boundsBuilder.build()
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngBounds(bounds, 100) // padding: 100px
            )
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapLoaded = {
            mapLoaded.value = true
        }
    ) {
        // 標記點
        Marker(state = MarkerState(position = points.first()), title = "起點")
        Marker(state = MarkerState(position = points.last()), title = "終點")

        // 畫 Polyline
        Polyline(
            points = points,
            color = Color.Blue,
            width = 8f
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    National_FreewayTheme {
//        MainView()
//    }
//}