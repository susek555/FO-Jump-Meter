package com.gps_usage.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.fo_jump_meter.sensors.SensorsClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class DefaultSensorClient(
    private val context: Context
) : SensorsClient {

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    override fun getSensorUpdates(sensorType: Int, rate: Int): Flow<SensorEvent> {
        return callbackFlow {
            val sensor = sensorManager.getDefaultSensor(sensorType)
                ?: throw SensorsClient.SensorsException("Sensor type $sensorType not available")

            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    launch { send(event) }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            sensorManager.registerListener(listener, sensor, rate)

            awaitClose {
                sensorManager.unregisterListener(listener)
            }
        }
    }
}
