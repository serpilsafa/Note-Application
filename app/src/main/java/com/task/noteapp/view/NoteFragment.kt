package com.task.noteapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.task.noteapp.R
import com.task.noteapp.adapter.NoteRecyclerAdapter
import com.task.noteapp.databinding.FragmentNoteBinding
import com.task.noteapp.viewmodel.NoteViewModel
import javax.inject.Inject

class NoteFragment @Inject constructor(
    val noteRecyclerAdapter: NoteRecyclerAdapter
): Fragment(R.layout.fragment_note) {

    private var fragmentBinding: FragmentNoteBinding? = null
    lateinit var viewModel: NoteViewModel

    private val swipeCallBack = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val layoutPosition = viewHolder.layoutPosition
            val selectedNote = noteRecyclerAdapter.noteList[layoutPosition]
            viewModel.deleteNote(selectedNote)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(NoteViewModel::class.java)

        val binding = FragmentNoteBinding.bind(view)
        fragmentBinding = binding

        subscribeToObservers()
        binding.recyclerViewNote.adapter = noteRecyclerAdapter
        binding.recyclerViewNote.layoutManager = LinearLayoutManager(requireContext())
        ItemTouchHelper(swipeCallBack).attachToRecyclerView(binding.recyclerViewNote)


        binding.fab.setOnClickListener { view ->
            findNavController().navigate(NoteFragmentDirections.actionNoteFragmentToNoteDetailFragment())
        }


    }

    private fun subscribeToObservers(){
        viewModel.noteList.observe(viewLifecycleOwner, Observer {
            noteRecyclerAdapter.noteList = it
        })
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }

}