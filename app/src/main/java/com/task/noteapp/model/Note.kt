package com.task.noteapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var title:String,
    var detail:String,
    var date: String,
    var edited:Boolean,
    var imgURL: String
)
