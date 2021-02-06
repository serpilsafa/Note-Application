package com.task.noteapp.view

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.task.noteapp.R
import com.task.noteapp.databinding.FragmentNoteDetailBinding
import com.task.noteapp.util.Status
import com.task.noteapp.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class NoteDetailFragment @Inject constructor(
    val glide: RequestManager
): Fragment(R.layout.fragment_note_detail) {

    private var fragmentBinding: FragmentNoteDetailBinding? = null
    lateinit var viewModel: NoteViewModel

    private val args: NoteDetailFragmentArgs by navArgs()
    private var noteId:Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(NoteViewModel::class.java)

        val binding = FragmentNoteDetailBinding.bind(view)
        fragmentBinding = binding

        subscribeToObservers()

        binding.imageView.setOnClickListener {
            findNavController().navigate(NoteDetailFragmentDirections.actionNoteDetailFragmentToImageApiFragment())
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        noteId = args.noteId
        if (noteId != 0 && noteId != null){
            println("note id: ${noteId}, noteListsize: ${viewModel.noteList.value}")
            viewModel.getNote(noteId!!).observe(viewLifecycleOwner, Observer {
                println("note: $it, noteid: $noteId")
                glide.load(it.imgURL).into(binding.imageView)
                binding.detailTitleTextView.setText(it.title)
                binding.detailDetailTextView.setText(it.detail)
                binding.urlImageText.setText(it.imgURL)
            })

        }


        binding.saveButton.setOnClickListener {
            if (noteId != 0 && noteId != null){
                viewModel.updateNote(noteId ?: 0,
                        binding.detailTitleTextView.text.toString(),
                        binding.detailDetailTextView.text.toString(),
                        binding.urlImageText.text.toString())
            }else{
                viewModel.makeNote(
                        binding.detailTitleTextView.text.toString(),
                        binding.detailDetailTextView.text.toString())
            }

        }

    }



    private fun subscribeToObservers(){
        viewModel.selectedImageUrl.observe(viewLifecycleOwner, Observer { url ->
            fragmentBinding?.let {
                glide.load(url).into(it.imageView)
            }
        })

        viewModel.insertNoteMsg.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS->{
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                    viewModel.resetInsertNoteMessage()
                }

                Status.ERROR->{
                    Toast.makeText(requireContext(), it.message?: "Error", Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.updateNote.observe(viewLifecycleOwner, Observer { note->
            fragmentBinding?.let {
                it.apply {
                    glide.load(note.imgURL).into(imageView)
                    detailTitleTextView.setText(note.title)
                    detailDetailTextView.setText(note.detail)
                    urlImageText.setText(note.imgURL)

                }
            }

        })
    }
    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }
}