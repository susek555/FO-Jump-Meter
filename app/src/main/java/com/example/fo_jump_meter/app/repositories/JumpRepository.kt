package com.example.fo_jump_meter.app.repositories

import com.example.fo_jump_meter.app.database.JumpsDao
import com.example.fo_jump_meter.app.database.SnapshotsDao
import com.example.fo_jump_meter.app.database.types.Jump
import com.example.fo_jump_meter.app.database.types.Snapshot
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JumpRepository @Inject constructor(
    private var jumpsDao: JumpsDao,
    private var snapshotsDao: SnapshotsDao
)  {
    suspend fun saveJumpWithSnapshots (jump: Jump, snapshots: List<Snapshot>) {
        val height = snapshots.maxOfOrNull { it.height } ?: 0f
        val airTime =
            (snapshots.maxOfOrNull { it.timestamp } ?: 0) - (snapshots.minOfOrNull { it.timestamp } ?: 0)
        jump.height = height.toDouble()
        jump.airTime = airTime
        jump.date = System.currentTimeMillis()
        val jumpId = jumpsDao.insertJump(jump)
        snapshots.forEach { snapshot ->
            snapshot.jumpId = jumpId
        }
        snapshotsDao.insertSnapshots(snapshots)
    }

    suspend fun getJumps(): List<Jump> {
        return jumpsDao.getAllJumps()
    }
}