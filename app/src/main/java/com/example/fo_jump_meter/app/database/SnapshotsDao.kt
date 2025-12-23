package com.example.fo_jump_meter.app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.example.fo_jump_meter.app.database.types.Snapshot

@Dao
interface SnapshotsDao {
    @Insert
    suspend fun insertSnapshot(snapshot: Snapshot): Long

    @Insert
    suspend fun insertSnapshots(snapshots: List<Snapshot>)


    @Delete
    suspend fun deleteSnapshot(snapshot: Snapshot)
}