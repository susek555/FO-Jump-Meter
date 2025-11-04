package com.example.fo_jump_meter.app.di

import androidx.room.Room
import com.example.fo_jump_meter.app.database.JumpsDatabase
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(get(), JumpsDatabase::class.java, "jumps_database")
            .fallbackToDestructiveMigration(false)
    }
    single { get<JumpsDatabase>().jumpsDao() }
}