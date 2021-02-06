package com.task.noteapp.repo

import androidx.lifecycle.LiveData
import com.task.noteapp.model.ImageResponse
import com.task.noteapp.model.Note
import com.task.noteapp.util.Resource

interface NoteRepositoryInterface {

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)

    suspend fun updateNote(note: Note)

    fun getNotes(): LiveData<List<Note>>

    fun getNote(noteId: Int): LiveData<Note>


    suspend fun searchImage(imageString: String): Resource<ImageResponse>
}