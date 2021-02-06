package com.task.noteapp.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.task.noteapp.model.ImageResponse
import com.task.noteapp.model.Note
import com.task.noteapp.util.Resource

class FakeNoteRepositoryTest: NoteRepositoryInterface {

    private val notes = mutableListOf<Note>()
    private val notesLiveData = MutableLiveData<List<Note>>(notes)


    override suspend fun insertNote(note: Note) {
        notes.add(note)
        refreshData()
    }

    override suspend fun deleteNote(note: Note) {
        notes.remove(note)
        refreshData()
    }

    override suspend fun updateNote(note: Note) {
        note.id?.let { notes.set(it, note) }
        refreshData()
    }

    override fun getNotes(): LiveData<List<Note>> {
        return notesLiveData
    }

    override fun getNote(noteId: Int): LiveData<Note> {
        var noteIndex: Int= 0
        for (i in 0..notes.size){
            if (notes[i].id == noteId)
                noteIndex = i
        }

        return MutableLiveData(notesLiveData.value?.get(noteIndex))
    }

    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {
        return Resource.success(ImageResponse(listOf(), 0,0))
    }

    private fun refreshData(){
        notesLiveData.postValue(notes)
    }
}