package com.task.noteapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.task.noteapp.R
import com.task.noteapp.model.Note
import com.task.noteapp.view.NoteFragmentDirections
import javax.inject.Inject

class NoteRecyclerAdapter @Inject constructor(
    val glide: RequestManager
): RecyclerView.Adapter<NoteRecyclerAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffUtil = object : DiffUtil.ItemCallback<Note>(){
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

    }

    private val recyclerViewDiffer = AsyncListDiffer(this, diffUtil)
    var noteList: List<Note>
        get() = recyclerViewDiffer.currentList
        set(value) = recyclerViewDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_row, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.note_row_imageView)
        val titleText = holder.itemView.findViewById<TextView>(R.id.title_textView)
        val detailText = holder.itemView.findViewById<TextView>(R.id.detail_textView)
        val dateText = holder.itemView.findViewById<TextView>(R.id.date_textView)
        val editedImage = holder.itemView.findViewById<ImageView>(R.id.edited_imageView)

        val note = noteList[position]

        holder.itemView.apply {
            titleText.text = note.title
            detailText.text = note.detail
            dateText.text = "Date: ${note.date}"
            if (note.edited) editedImage.visibility = View.VISIBLE
            glide.load(note.imgURL).into(imageView)

            setOnClickListener {
                val action = NoteFragmentDirections.actionNoteFragmentToNoteDetailFragment(noteId = note.id ?: 0)
                findNavController().navigate(action)
            }
        }

    }

    override fun getItemCount(): Int {
        return noteList.size
    }
}