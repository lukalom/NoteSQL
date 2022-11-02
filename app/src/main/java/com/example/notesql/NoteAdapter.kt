package com.example.notesql

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter: RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var notes = emptyList<NoteModel>()

    private var onClickItem:((noteModel: NoteModel) -> Unit)? = null
    private var onClickDeleteItem:((noteModel: NoteModel) -> Unit)? = null

    fun addItems(notes: List<NoteModel>){
        this.notes = notes
    }

    fun setOnClickDeleteItem(callback: (noteModel: NoteModel) -> Unit){
        this.onClickDeleteItem = callback
    }

    fun setOnClickItem(callback: (noteModel: NoteModel) -> Unit){
        this.onClickItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NoteViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items_std, parent, false)
    )

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bindView(note)
        holder.itemView.setOnClickListener{ onClickItem?.invoke(note) }

        holder.btnDelete.setOnClickListener{ onClickDeleteItem?.invoke(note) }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class NoteViewHolder(private var view: View): RecyclerView.ViewHolder(view) {
        private var id = view.findViewById<TextView>(R.id.noteId)
        private var title = view.findViewById<TextView>(R.id.noteTitle)
        private var content = view.findViewById<TextView>(R.id.noteContent)

        var btnDelete = view.findViewById<Button>(R.id.btnDelete)


        fun bindView(note: NoteModel){
            id.text = note.id.toString()
            title.text = note.title
            content.text = note.content
        }
    }

}