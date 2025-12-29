package com.example.fo_jump_meter.app.di

import android.content.Context
import androidx.room.Room
import com.example.fo_jump_meter.app.database.JumpsDao
import com.example.fo_jump_meter.app.database.JumpsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): JumpsDatabase {
        return Room.databaseBuilder(
            context,
            JumpsDatabase::class.java,
            "jumps_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideJumpsDao(db: JumpsDatabase): JumpsDao = db.jumpsDao()

    @Provides
    fun provideSnapshotsDao(db: JumpsDatabase) = db.snapshotsDao()

}