package com.task.noteapp.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.task.noteapp.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase(){
    abstract fun noteDao(): NoteDao



}
