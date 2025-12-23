package com.example.fo_jump_meter.app.database.types

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "snapshots",
    foreignKeys = [
        ForeignKey(
            entity = Jump::class,
            parentColumns = ["id"],
            childColumns = ["jumpId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Snapshot (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var height: Float,
    var velocity: Float,
    var acceleration: Float,
    var timestamp: Long,
    var jumpId: Long
)