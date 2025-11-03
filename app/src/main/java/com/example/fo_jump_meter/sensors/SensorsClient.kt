package com.example.fo_jump_meter.sensors

import android.hardware.SensorEvent
import kotlinx.coroutines.flow.Flow

interface SensorsClient {
    fun getSensorUpdates(sensorType: Int, rate: Int): Flow<SensorEvent>

    class SensorsException(message: String): Exception()
}
