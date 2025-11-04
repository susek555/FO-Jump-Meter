package com.example.fo_jump_meter.app.repositories

import com.example.fo_jump_meter.app.data.SensorData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SensorsRepository @Inject constructor() {
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _accelerometerFlow = MutableSharedFlow<SensorData>(replay = 1)
    val accelerometerFlow = _accelerometerFlow.asSharedFlow()

    private val _rotationVectorFlow = MutableSharedFlow<SensorData>(replay = 1)
    val rotationVectorFlow = _rotationVectorFlow.asSharedFlow()

    fun updateAccelerometer(data: SensorData) {
        repositoryScope.launch {
            _accelerometerFlow.tryEmit(data)
        }
    }

    fun updateRotationVector(data: SensorData) {
        repositoryScope.launch {
            _rotationVectorFlow.tryEmit(data)
        }
    }

    fun clear() {
        repositoryScope.cancel()
    }

}