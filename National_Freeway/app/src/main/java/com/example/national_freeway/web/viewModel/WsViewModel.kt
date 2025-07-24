package com.example.national_freeway.web.viewModel

import android.util.Log
import androidx.compose.ui.text.resolveDefaults
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.national_freeway.web.WebSocketResponse
import com.example.national_freeway.web.websockets.WebSocketsClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

interface WsListener {
    fun onMessage(text:String)
}

class SocketsVM : ViewModel() {
    private val _responseText = MutableStateFlow("Android")
    val responseText: StateFlow<String> = _responseText

    private val _responseItem = MutableStateFlow<WebSocketResponse?>(null)
    val responseItem: StateFlow<WebSocketResponse?> = _responseItem

    private val ws = WebSocketsClient(object : WsListener {
        override fun onMessage(text: String) {
            _responseText.value = text
            val jsonStr = responseText.value
            _responseItem.value = Json.decodeFromString<WebSocketResponse>(jsonStr)
        }
    }).apply { start() }

    fun wsSendText(gateNum: Int) {
//        send 1, 2, 3...
        ws.sendText("$gateNum")
    }
}