package com.task.noteapp.viewmodel

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.StringUtil
import com.task.noteapp.model.ImageResponse
import com.task.noteapp.model.Note
import com.task.noteapp.repo.NoteRepositoryInterface
import com.task.noteapp.util.Resource
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NoteViewModel @ViewModelInject constructor(
    private val repository: NoteRepositoryInterface
): ViewModel() {

    val noteList = repository.getNotes()

    private val images = MutableLiveData<Resource<ImageResponse>>()
    val imageList: LiveData<Resource<ImageResponse>>
        get() = images

    private val selectedImage = MutableLiveData<String>()
    val selectedImageUrl: LiveData<String>
        get() = selectedImage

    private var insertNoteMessage = MutableLiveData<Resource<Note>>()
    val insertNoteMsg: LiveData<Resource<Note>>
        get() = insertNoteMessage

    private var selectedNote = MutableLiveData<Note>()
    val updateNote: LiveData<Note>
        get() = selectedNote

    fun resetInsertNoteMessage(){
        insertNoteMessage = MutableLiveData<Resource<Note>>()
    }

    fun setSelectedImage(url: String){
        selectedImage.postValue(url)
    }

    fun deleteNote(note: Note) = viewModelScope.launch{
        repository.deleteNote(note)
    }

    private fun insertNote(note: Note) = viewModelScope.launch {
        repository.insertNote(note)
    }

    private fun updateNote(note: Note) = viewModelScope.launch {
        repository.insertNote(note)
    }

    fun getNote(noteId: Int): LiveData<Note> {
        return repository.getNote(noteId)
    }

    fun searchForImage(searchString: String){
        if(searchString.isEmpty()){
            return
        }

        images.value = Resource.loading(null)
        viewModelScope.launch {
            val response = repository.searchImage(searchString)
            images.value = response
        }
    }
    fun makeNote(title: String, detail: String ){
        if (title.isEmpty() || detail.isEmpty()){
            insertNoteMessage.postValue(Resource.error("Enter title and your note", null))
            return
        }

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted = current.format(formatter)

        val note = Note(title = title,detail = detail, date = formatted,edited = false,imgURL = selectedImage.value ?: "")
        insertNote(note)
        setSelectedImage("")
        insertNoteMessage.postValue(Resource.success(note))

    }

    fun updateNote(id: Int, title: String, detail: String, url: String){

        if (title.isEmpty() || detail.isEmpty()){
            insertNoteMessage.postValue(Resource.error("Enter title and your note", null))
            return
        }

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted = current.format(formatter)

        var imageURL:String
        if(selectedImage.value == url){
            imageURL = url
        }else if (selectedImage.value == ""){
            imageURL = url
        }else{
            imageURL = selectedImage.value ?: ""
        }
        val note = Note(id= id, title = title, detail = detail, date = formatted,edited = true,imgURL = imageURL)
        updateNote(note)
        setSelectedImage("")
        insertNoteMessage.postValue(Resource.success(note))
    }

}