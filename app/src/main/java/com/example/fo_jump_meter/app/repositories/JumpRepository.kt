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
        val firstJumpFrameIndex =
            snapshots.indexOfFirst { it.height !=  0.0f } - 1
        val filteredSnapshots = snapshots.filterIndexed {
            index, _ -> index >= firstJumpFrameIndex
        }
        val height = filteredSnapshots.maxOfOrNull { it.height } ?: 0f
        var airTime =
            (filteredSnapshots.maxOfOrNull { it.timestamp } ?: 0L) - (filteredSnapshots
                .asSequence()
                .zipWithNext()
                .filter{(_, s2)-> s2.height>0}
                .map { (s1, _) -> (s1.timestamp) }
                .minOrNull() ?: 0L)
        if (height <= 0.0f) {
            airTime = 0L
        }
        jump.height = height.toDouble()
        jump.airTime = airTime
        jump.date = System.currentTimeMillis()
        val jumpId = jumpsDao.insertJump(jump)
        filteredSnapshots.forEach { snapshot ->
            snapshot.jumpId = jumpId
        }
        snapshotsDao.insertSnapshots(filteredSnapshots)
    }

    suspend fun getJumps(): List<Jump> {
        return jumpsDao.getAllJumps()
    }

    suspend fun deleteJump(jump: Jump) {
        return jumpsDao.deleteJump(jump)
    }

    suspend fun updateJump(jump: Jump) {
        return jumpsDao.updateJump(jump)
    }

    suspend fun getJumpById(id: Long): Jump {
        return jumpsDao.getJumpById(id)
    }

    suspend fun getSnapshotsByJumpId(jumpId: Long): List<Snapshot> {
        return jumpsDao.getSnapshotsByJumpId(jumpId)
    }
}