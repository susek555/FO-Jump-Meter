package com.example.fo_jump_meter.app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fo_jump_meter.app.database.types.Jump
import com.example.fo_jump_meter.app.database.types.Snapshot

@Database(
    entities = [Jump::class, Snapshot::class],
    version = 6
)
abstract class JumpsDatabase: RoomDatabase() {
    abstract fun jumpsDao(): JumpsDao
    abstract fun snapshotsDao(): SnapshotsDao
}