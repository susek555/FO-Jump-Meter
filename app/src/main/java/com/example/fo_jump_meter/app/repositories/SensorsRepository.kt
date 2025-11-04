package com.example.fo_jump_meter.app.repositories

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class SensorsRepository(): KoinComponent {
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

//    private val _acceleratorFlow = MutableSharedFlow<[type]>(replay = 1)
//    val acceleratorFlow = _acceleratorFlow.asSharedFlow()
//
//    private val _gyroscopeFlow = MutableSharedFlow<[type]>(replay = 1)
//    val gyroscopeFlow = _gyroscopeFlow.asSharedFlow()
//
//    fun updateAccelerator([type]) {
//        repositoryScope.launch {
//            _acceleratorFlow.tryEmit([type])
//        }
//    }
//
//    fun updateGyroscope([type]) {
//        repositoryScope.launch {
//            _acceleratorFlow.tryEmit([type])
//        }
//    }
    
    //TODO replace [type] with actual type

    fun clear() {
        repositoryScope.cancel()
    }

}