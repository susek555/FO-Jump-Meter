package com.example.fo_jump_meter.sensors

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Binder
import android.os.IBinder
import com.gps_usage.sensors.DefaultSensorClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive

class SensorsService: Service() {
    private var serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var sensorsClient: SensorsClient
    private val binder = SensorsBinder()
    //    private val repository: SensorsRepository by inject()
    //TODO make sensors repository

    inner class SensorsBinder: Binder() {
        fun getService(): SensorsService = this@SensorsService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        sensorsClient = DefaultSensorClient(
            context = applicationContext
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        isRunning = true
        if (serviceScope.isActive.not()) {
            serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        }
        sensorsClient
            .getSensorUpdates(
                Sensor.TYPE_ACCELEROMETER,
                SensorManager.SENSOR_DELAY_FASTEST
            )
            .catch{e -> e.printStackTrace()}
            .onEach {
                //TODO upload to a repository
            }
            .launchIn(serviceScope)

        sensorsClient
            .getSensorUpdates(
                Sensor.TYPE_GYROSCOPE,
                SensorManager.SENSOR_DELAY_FASTEST
            )
            .catch{e -> e.printStackTrace()}
            .onEach {
                //TODO upload to a repository
            }
            .launchIn(serviceScope)
    }

    private fun stop() {
        serviceScope.cancel()
        stopSelf()
        isRunning = false
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        isRunning = false
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"

        @Volatile
        var isRunning = false
            private set
    }
}