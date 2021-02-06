package com.task.noteapp.roomdb

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Root
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.task.noteapp.getOrAwaitValue
import com.task.noteapp.model.Note
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.internal.matchers.Not
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class NoteDaoTest {

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var dao: NoteDao

    @Inject
    @Named("testDatabase")
    lateinit var database: NoteDatabase

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup(){
        hiltRule.inject()
        dao = database.noteDao()
    }


    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun insertNoteTesting() = runBlockingTest{
        val exampleNote = Note(1, "Shopping","Water, Egg", "6/02/2021", false,"example.com")
        dao.insertNote(exampleNote)

        val list = dao.observeNotes().getOrAwaitValue()
        assertThat(list).contains(exampleNote)
    }

    @Test
    fun deleteNoteTesting() = runBlockingTest{
        val exampleNote = Note(1, "Shopping","Water, Egg", "6/02/2021", false,"example.com")

        dao.insertNote(exampleNote)
        dao.deleteNote(exampleNote)

        val list = dao.observeNotes().getOrAwaitValue()
        assertThat(list).doesNotContain(exampleNote)

    }

}