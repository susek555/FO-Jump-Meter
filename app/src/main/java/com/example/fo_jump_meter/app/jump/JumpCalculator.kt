package com.example.fo_jump_meter.app.jump


class JumpCalculator {
    private var currentHeight: Double = 0.0 // initial height is set to 0
    private var currentVelocity: Double = 0.0 // initial speed is set to 0
    private var time: Long = 0 // initial time is set to 0

    fun calculate(acceleration: FloatArray, rotation: FloatArray, timestamp: Long): Double {
        return currentHeight
    }

    fun reset() {
        currentHeight = 0.0
        currentVelocity = 0.0
        time = 0
    }
}