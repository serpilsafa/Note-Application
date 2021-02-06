package com.task.noteapp.dependencyInjection

import android.content.Context
import androidx.room.Room
import com.task.noteapp.roomdb.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named

@Module
@InstallIn(ApplicationComponent::class)
object TestAppModule {

    @Provides
    @Named("testDatabase")
    fun injectInMemoryRoom(
        @ApplicationContext context:Context
    ) = Room.inMemoryDatabaseBuilder(context,NoteDatabase::class.java)
            .allowMainThreadQueries()
            .build()

}