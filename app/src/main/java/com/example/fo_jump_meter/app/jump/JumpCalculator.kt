package com.example.fo_jump_meter.app.jump

import android.hardware.SensorManager
import kotlin.math.abs

class JumpCalculator {
    private var currentHeight: Double = 0.0
    private var currentVelocity: Double = 0.0
    private var currentAcceleration: Double = 0.0
    private var time: Long = 0

    private var calibrationOffset = 0.0f
    private var calibrationFrameCount = 0
    private val FRAMES_TO_CALIBRATE = 50
    private var isCalibrated = false
    private var jumpStartCounter = 0
    private var idleCounter = 0
    private val FRAMES_TO_START_JUMP = 5
    private val FRAMES_TO_SOFT_RESET = 5
    private var isJumping = false
    private var isInAir = false
    private var hasLanded = false

    fun calculateZAxisAcceleration(acceleration: FloatArray, rotation: FloatArray): Float{
        val rotationMatrix = FloatArray(9)
        SensorManager.getRotationMatrixFromVector(rotationMatrix, rotation)
        val zAcceleration = acceleration[0] * rotationMatrix[6] + acceleration[1] * rotationMatrix[7] + acceleration[2] * rotationMatrix[8]
        return zAcceleration
    }

    fun calculate(acceleration: FloatArray, rotation: FloatArray, timestamp: Long): FloatArray {
        var acc = calculateZAxisAcceleration(acceleration, rotation)
        if (!isCalibrated) {
            calibrationOffset += acc
            calibrationFrameCount++
            if (calibrationFrameCount >= FRAMES_TO_CALIBRATE) {
                calibrationOffset /= FRAMES_TO_CALIBRATE
                isCalibrated = true
            }
            time = timestamp
            return floatArrayOf(0.0f, 0.0f, 0.0f)
        }
        acc -= calibrationOffset
        if (time == 0L) {
            time = timestamp
            return floatArrayOf(0.0f, 0.0f, 0.0f)
        }
        val dt = (timestamp - time)
        if (!isJumping) {
            if (acc > 3.0f) {
                jumpStartCounter++
                if (jumpStartCounter >= FRAMES_TO_START_JUMP) {
                    isJumping = true
                    isInAir = false
                }
                idleCounter = 0
            } else if (abs(acc) < 0.5f) {
                idleCounter++
                if (idleCounter > FRAMES_TO_SOFT_RESET) {
                    softReset()
                    idleCounter = 0
                }
                time = timestamp
                return floatArrayOf(0.0f, 0.0f, 0.0f)
            } else {
                idleCounter = 0
                jumpStartCounter = 0
            }
        } else {
            if (isInAir){
                if (detectLanding(acc)) {
                    isJumping = false
                    isInAir = false
                    softReset()
                    hasLanded = true
                } else {
                    acc = -9.81f
                }
            } else {
                if (detectTakeoff(acc)) {
                    isInAir = true
                    acc = -9.81f
                }
            }
        }
        if (!hasLanded) {
            computeVerlet(acc, dt)
        }
        val result = floatArrayOf(currentHeight.toFloat(), currentVelocity.toFloat(), currentAcceleration.toFloat())
        time = timestamp
        return result
    }
    fun detectLanding(acc: Float): Boolean {
        return (currentVelocity < -0.5 && abs(acc) > 10.0) || currentHeight <= 0.0
    }

    fun detectTakeoff(acc: Float): Boolean {
        return (acc < 0.2f)
    }

    fun computeVerlet(acc: Float, dt: Long) {
        val dtInSeconds = dt / 1_000_000_000.0
        currentHeight += currentVelocity * dtInSeconds + 0.5 * currentAcceleration * dtInSeconds * dtInSeconds
        currentVelocity += (currentAcceleration + acc) * dtInSeconds / 2
        currentAcceleration = acc.toDouble()
    }

    private fun softReset() {
//        currentHeight = 0.0
        currentVelocity = 0.0
        currentAcceleration = 0.0
        jumpStartCounter = 0
        isInAir = false
    }

    fun reset() {
        currentHeight = 0.0
        currentVelocity = 0.0
        currentAcceleration = 0.0
        time = 0
        jumpStartCounter = 0
        isInAir = false
        hasLanded = false
    }
}