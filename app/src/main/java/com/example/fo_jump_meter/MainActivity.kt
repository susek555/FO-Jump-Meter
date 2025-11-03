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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fo_jump_meter.sensors.SensorsService
import com.example.fo_jump_meter.ui.theme.FOJumpMeterTheme

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
        enableEdgeToEdge()
        setContent {
            FOJumpMeterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FOJumpMeterTheme {
        Greeting("Android")
    }
}