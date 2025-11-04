package com.example.fo_jump_meter.app.data

data class SensorData(
    val sensorType: Int = 0,
    val values: FloatArray = floatArrayOf(),
    val timestamp: Long = 0 //nanoseconds
)
