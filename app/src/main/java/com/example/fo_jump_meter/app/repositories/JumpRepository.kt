package com.example.fo_jump_meter.app.repositories

import com.example.fo_jump_meter.app.database.JumpsDao
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class JumpRepository: KoinComponent {
    private val jumpsDao: JumpsDao by inject()

    //TODO
}