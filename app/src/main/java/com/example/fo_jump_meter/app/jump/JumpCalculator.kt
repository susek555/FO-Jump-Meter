package com.example.fo_jump_meter.app.jump

import android.hardware.SensorManager
import kotlin.math.abs


class JumpCalculator {
    private var currentHeight: Double = 0.0 // initial height is set to 0
    private var currentVelocity: Double = 0.0 // initial speed is set to 0
    private var currentAcceleration: Double = 0.0 // initial acceleration is set to 0
    private var time: Long = 0 // initial time is set to 0
    var isJumping = false

    fun calculateZAxisAcceleration(acceleration: FloatArray, rotation: FloatArray): Float{
        val rotationMatrix = FloatArray(9)
        SensorManager.getRotationMatrixFromVector(rotationMatrix, rotation)
        val zAcceleration = acceleration[0] * rotationMatrix[6] + acceleration[1] * rotationMatrix[7] + acceleration[2] * rotationMatrix[8]
        return zAcceleration
    }

    fun calculate(acceleration: FloatArray, rotation: FloatArray, timestamp: Long): FloatArray {
        val dt = (timestamp - time)
        if (time == 0L) {
            time = timestamp
            return floatArrayOf(0.0f, 0.0f, 0.0f)
        }
        var acc = calculateZAxisAcceleration(acceleration, rotation)
        if (!isJumping) {
            if (acc > 3.0) {
                isJumping = true
            } else {
                acc = 0.0f
                currentVelocity = 0.0
            }
        } else {
            if (detectLanding(acc)) {
                isJumping = false
                reset()
            } else if (acc < 2.0) {
                acc = (-1) * SensorManager.GRAVITY_EARTH
            }
        }
        computeVerlet(acc, dt)
        val result = floatArrayOf(currentHeight.toFloat(), currentVelocity.toFloat(), currentAcceleration.toFloat())
        time = timestamp
        return result

    }

    fun detectLanding(acc: Float): Boolean {
        return (abs(acc) > 3.0 && currentVelocity < 0.0) || currentHeight <= 0.0
    }
    
    fun computeVerlet(acc: Float, dt: Long) {
        val dtInSeconds = dt / 1000000000.0
        currentHeight += currentVelocity * dtInSeconds + 0.5 * currentAcceleration * dtInSeconds * dtInSeconds
        currentVelocity += (currentAcceleration + acc) * dtInSeconds / 2
        currentAcceleration = acc.toDouble()
    }

    fun reset() {
        currentHeight = 0.0
        currentVelocity = 0.0
        time = 0
    }
}