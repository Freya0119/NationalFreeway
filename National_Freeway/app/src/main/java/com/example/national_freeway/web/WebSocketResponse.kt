package com.example.national_freeway.web

import kotlinx.serialization.Serializable

@Serializable
data class WebSocketResponse(
    val datatime: String,
    val O_gate: String,
    val D_gate: String,
    val Average_Speed: String,
    val Quantity: String
)