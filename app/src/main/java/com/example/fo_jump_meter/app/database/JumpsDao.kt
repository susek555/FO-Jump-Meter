package com.example.fo_jump_meter.app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fo_jump_meter.app.database.types.Jump

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
}