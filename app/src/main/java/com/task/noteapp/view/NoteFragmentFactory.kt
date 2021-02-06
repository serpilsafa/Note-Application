package com.task.noteapp.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.task.noteapp.adapter.ImageRecyclerAdapter
import com.task.noteapp.adapter.NoteRecyclerAdapter
import javax.inject.Inject

class NoteFragmentFactory @Inject constructor(
    private val noteRecyclerAdapter: NoteRecyclerAdapter,
    private val glide: RequestManager,
    private val imageRecyclerAdapter: ImageRecyclerAdapter
): FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className) {
            NoteFragment::class.java.name -> NoteFragment(noteRecyclerAdapter)
            NoteDetailFragment::class.java.name -> NoteDetailFragment(glide)
            ImageApiFragment::class.java.name -> ImageApiFragment(imageRecyclerAdapter)
            else -> super.instantiate(classLoader, className)
        }
    }
}