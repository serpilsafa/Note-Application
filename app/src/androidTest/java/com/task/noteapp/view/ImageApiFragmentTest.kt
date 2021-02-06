package com.task.noteapp.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.task.noteapp.R
import com.task.noteapp.adapter.ImageRecyclerAdapter
import com.task.noteapp.getOrAwaitValue
import com.task.noteapp.launchFragmentInHiltContainer
import com.task.noteapp.repo.FakeNoteRepositoryTest
import com.task.noteapp.viewmodel.NoteViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.RecursiveAction
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ImageApiFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: NoteFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun selectedImage(){
        val navController = Mockito.mock(NavController::class.java)
        val selectedImageUrl = "example.com"
        val testViewModel = NoteViewModel(FakeNoteRepositoryTest())

        launchFragmentInHiltContainer<ImageApiFragment>(
            factory = fragmentFactory
        ){
            Navigation.setViewNavController(requireView(),navController)
            viewModel = testViewModel
            imageRecyclerAdapter.imageList = listOf(selectedImageUrl)
        }

        Espresso.onView(withId(R.id.imageRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ImageRecyclerAdapter.ImageViewHolder>(
                0, click()
            )
        )

        Mockito.verify(navController).popBackStack()
        assertThat(testViewModel.selectedImageUrl.getOrAwaitValue()).isEqualTo(selectedImageUrl)
    }

}