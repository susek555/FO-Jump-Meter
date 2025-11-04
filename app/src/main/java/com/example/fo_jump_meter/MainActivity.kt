package com.example.fo_jump_meter

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.fo_jump_meter.app.di.databaseModule
import com.example.fo_jump_meter.app.di.repositoryModule
import com.example.fo_jump_meter.app.navigation.NavigationController
import com.example.fo_jump_meter.sensors.SensorsService
import com.example.fo_jump_meter.ui.theme.FOJumpMeterTheme
import dagger.hilt.android.AndroidEntryPoint
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var sensorsService: SensorsService
    private var isSensorsServiceBound: Boolean = false

    //bind service
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as SensorsService.SensorsBinder
            sensorsService = binder.getService()
            isSensorsServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isSensorsServiceBound = false
        }
    }

    @SuppressLint("UnsafeIntentLaunch")
    override fun onStart() {
        super.onStart()
        Intent(this, SensorsService::class.java).also {
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    //Utils to dynamically start and stop service
    private fun startSensorsService() {
        if (!SensorsService.isRunning) {
            Intent(this, SensorsService::class.java).apply {
                action = SensorsService.ACTION_START
                startService(this)
            }
        }
    }

    private fun stopSensorsService()  {
        Intent(this, SensorsService::class.java).apply {
            action = SensorsService.ACTION_STOP
            startService(this)
        }
    }

    //Main activity TODO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin{
            androidContext(this@MainActivity)
            modules(databaseModule, repositoryModule)

            logger(AndroidLogger(Level.DEBUG))
        }

        enableEdgeToEdge()
        setContent {
            FOJumpMeterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationController(
                        startSensorsService = { startSensorsService() },
                        stopSensorsService =  { stopSensorsService() },
                        innerPadding = innerPadding
                    )
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (isSensorsServiceBound) {
            unbindService(connection)
            isSensorsServiceBound = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSensorsService()
    }
}
