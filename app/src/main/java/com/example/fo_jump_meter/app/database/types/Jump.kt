package com.example.fo_jump_meter.app.database.types

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "jumps"
)
data class Jump(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var height: Double,
    var airTime: Long,
    var date: Long,
    var name: String,
    var weight: Short
)