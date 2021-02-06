package com.task.noteapp.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.task.noteapp.model.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes")
    fun observeNotes(): LiveData<List<Note>>

    @Update
    fun updateNote(note: Note)

    @Query("SELECT * FROM notes WHERE id= :noteID")
    fun observeNote(noteID: Int): LiveData<Note>
}