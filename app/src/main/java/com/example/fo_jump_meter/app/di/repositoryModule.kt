package com.example.fo_jump_meter.app.di

import com.example.fo_jump_meter.app.repositories.JumpRepository
import com.example.fo_jump_meter.app.repositories.SensorsRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { SensorsRepository() }
    single { JumpRepository() }
}