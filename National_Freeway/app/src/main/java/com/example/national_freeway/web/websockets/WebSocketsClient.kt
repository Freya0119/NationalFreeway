package com.example.national_freeway.web.websockets

import android.util.Log
import com.example.national_freeway.web.viewModel.WsListener
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketsClient(private val wsListener: WsListener) {
    private var client = OkHttpClient()
    private var websockets: WebSocket? = null

    fun start() {
        val url = "https://cf85997e8412.ngrok-free.app/websocket"
        val request = Request.Builder().url(url).build()
        websockets = client.newWebSocket(request, listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)

                Log.d("WEB_SOCKETS", "onOpen\n$response")

                wsListener.onMessage("onOpen $response")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)

                Log.d("WEB_SOCKETS", "onMessage\n$text")

                wsListener.onMessage("onMessage $text")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)

                Log.d("WEB_SOCKETS", "onFailure\nThrowable $t\nResponse $response")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)

                Log.d("WEB_SOCKETS", "onClosed\ncode $code" +
                        "reason $reason")
            }
        })
    }

    fun sendText(text: String) {
        websockets?.send(text)
    }
}