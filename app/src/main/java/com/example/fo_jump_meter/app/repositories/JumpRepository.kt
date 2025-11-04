package com.example.fo_jump_meter.app.repositories

import com.example.fo_jump_meter.app.database.JumpsDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JumpRepository @Inject constructor(
    private var jumpsDao: JumpsDao
)  {

    //TODO
}