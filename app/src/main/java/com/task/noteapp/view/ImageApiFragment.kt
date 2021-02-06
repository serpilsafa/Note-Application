package com.task.noteapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.task.noteapp.R
import com.task.noteapp.adapter.ImageRecyclerAdapter
import com.task.noteapp.databinding.FragmentImageApiBinding
import com.task.noteapp.util.Status
import com.task.noteapp.viewmodel.NoteViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageApiFragment @Inject constructor(
    val imageRecyclerAdapter: ImageRecyclerAdapter
): Fragment(R.layout.fragment_image_api) {

    lateinit var viewModel: NoteViewModel
    private var fragmentBinding: FragmentImageApiBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(NoteViewModel::class.java)

        val binding = FragmentImageApiBinding.bind(view)
        fragmentBinding = binding

        var job: Job? = null
        binding.searchText.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(1000)
                it?.let {
                    if(it.toString().isNotEmpty()){
                        viewModel.searchForImage(it.toString())
                    }
                }
            }
        }

        subscribeToObservers()

        binding.imageRecyclerView.adapter = imageRecyclerAdapter
        binding.imageRecyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        imageRecyclerAdapter.setOnItemClickListener {
            viewModel.setSelectedImage(it)
            findNavController().popBackStack()
        }

    }

    private fun subscribeToObservers(){
        viewModel.imageList.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    val urls = it.data?.hits?.map { imageResult ->
                        imageResult.previewURL
                    }

                    imageRecyclerAdapter.imageList = urls ?: listOf()

                    fragmentBinding?.progressBar?.visibility = View.GONE

                }

                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message ?: "Error", Toast.LENGTH_LONG)
                    fragmentBinding?.progressBar?.visibility = View.GONE
                }

                Status.LOADING -> {
                    fragmentBinding?.progressBar?.visibility = View.VISIBLE
                }
            }
        })
    }
}