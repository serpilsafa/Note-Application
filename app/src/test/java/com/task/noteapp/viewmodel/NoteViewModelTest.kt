package com.task.noteapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.task.noteapp.MainCoroutineRule
import com.task.noteapp.getOrAwaitValueTest
import com.task.noteapp.repo.FakeNoteRepository
import com.task.noteapp.util.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NoteViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: NoteViewModel

    @Before
    fun setup(){
        viewModel = NoteViewModel(FakeNoteRepository())
    }

    @Test
    fun `insert note without title return error`(){
        viewModel.makeNote("Shopping List","")
        val value = viewModel.insertNoteMsg.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert note without note return error`(){
        viewModel.makeNote("","This is a note")
        val value = viewModel.insertNoteMsg.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)

    }
}