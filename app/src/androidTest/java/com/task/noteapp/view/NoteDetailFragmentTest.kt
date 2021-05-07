package com.task.noteapp.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.task.noteapp.R
import com.task.noteapp.getOrAwaitValue
import com.task.noteapp.launchFragmentInHiltContainer
import com.task.noteapp.model.Note
import com.task.noteapp.repo.FakeNoteRepositoryTest
import com.task.noteapp.viewmodel.NoteViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class NoteDetailFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: NoteFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun testNavigationFromNoteDetailToImageAPI(){
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<NoteDetailFragment>(
            factory = fragmentFactory
        ){

            Navigation.setViewNavController(requireView(), navController)
        }

        Espresso.onView(ViewMatchers.withId(R.id.imageView)).perform(click())

        Mockito.verify(navController).navigate(NoteDetailFragmentDirections.actionNoteDetailFragmentToImageApiFragment())
    }

    @Test
    fun testOnBackPressed(){

        val navController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<NoteDetailFragment>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }

        Espresso.pressBack()
        verify(navController).popBackStack()
    }

    @Test
    fun testSaveNote(){
        val testViewModel = NoteViewModel(FakeNoteRepositoryTest())

        launchFragmentInHiltContainer<NoteDetailFragment>(
            factory = fragmentFactory
        ){
            viewModel = testViewModel
        }

        Espresso.onView(withId(R.id.detail_title_textView)).perform(replaceText("Shopping"))
        Espresso.onView(withId(R.id.detail_detail_textView)).perform(replaceText("It is a note"))
        Espresso.onView(withId(R.id.save_button)).perform(click())


        val current = LocalDateTime.now()
        val instantTime= current.toString().substring(0, 10)

         //val instantTime = "2021-02-07"

        assertThat(testViewModel.noteList.getOrAwaitValue()).contains(
            Note(
                title = "Shopping",
                detail = "It is a note",
                date = instantTime,
                edited = false,
                imgURL = ""
            )
        )
    }

}