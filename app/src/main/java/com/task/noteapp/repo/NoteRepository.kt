package com.task.noteapp.repo

import androidx.lifecycle.LiveData
import com.task.noteapp.api.RetrofitAPI
import com.task.noteapp.model.ImageResponse
import com.task.noteapp.model.Note
import com.task.noteapp.roomdb.NoteDao
import com.task.noteapp.util.Resource
import java.lang.Exception
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDao: NoteDao,
                                         private val retrofitApi: RetrofitAPI
                                         ): NoteRepositoryInterface {
    override suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    override fun getNotes(): LiveData<List<Note>> {
        return noteDao.observeNotes()
    }

    override fun getNote(noteId: Int): LiveData<Note> {
        return  noteDao.observeNote(noteId)
    }


    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {
        return try {
            val response = retrofitApi.imageSearch(imageString)
            if (response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                }?: Resource.error("Error", null)
            }else{
                Resource.error("Error", null)
            }
        }catch (e:Exception){
            Resource.error("Data not found !!!", null)
        }
    }
}