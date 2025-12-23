package com.example.fo_jump_meter.app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fo_jump_meter.app.database.types.Jump
import com.example.fo_jump_meter.app.database.types.Snapshot

@Dao
interface JumpsDao {
    @Insert
    suspend fun insertJump(jump: Jump) : Long

    @Delete
    suspend fun deleteJump(jump: Jump)

    @Update
    suspend fun updateJump(jump: Jump)


    @Query("SELECT * FROM jumps ORDER BY id DESC")
    suspend fun getAllJumps(): List<Jump>

    @Query("SELECT * FROM jumps WHERE id = :id")
    suspend fun getJumpById(id: Long): Jump

    @Query("SELECT * FROM snapshots WHERE jumpId = :jumpId")
    suspend fun getSnapshotsByJumpId(jumpId: Long): List<Snapshot>



}