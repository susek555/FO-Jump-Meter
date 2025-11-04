package com.example.fo_jump_meter.app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fo_jump_meter.app.database.types.Jump

@Database(
    entities = [Jump::class],
    version = 1
)
abstract class JumpsDatabase: RoomDatabase() {
    abstract fun jumpsDao(): JumpsDao
}