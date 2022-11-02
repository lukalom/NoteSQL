package com.example.notesql

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: SQLiteHelper

    private lateinit var recyclerView: RecyclerView
    private var adapter: NoteAdapter? = null

    private lateinit var title: EditText
    private lateinit var content: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var btnUpdate: Button

    private var note:NoteModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        initRecyclerView()

        dbHelper = SQLiteHelper(this)

        btnAdd.setOnClickListener { addNote() }
        btnView.setOnClickListener { getNotes() }
        btnUpdate.setOnClickListener{ updateNote() }

        adapter?.setOnClickItem { noteModel ->
            Toast.makeText(this, noteModel.title, Toast.LENGTH_SHORT).show()

            btnAdd.isEnabled = false
            btnAdd.isClickable = false

            title.setText(noteModel.title)
            content.setText(noteModel.content)
            note = noteModel
        }

        adapter?.setOnClickDeleteItem { noteModel -> deleteNote(noteModel.id) }
    }

    private fun updateNote() {
        val title = title.text.toString()
        val content = content.text.toString()

        if (title == note?.title && content == note?.content)
            {
                Toast.makeText(this, "Nothing to update", Toast.LENGTH_SHORT).show()
                return
            }

        if (note == null){
            return
        }

        val note = NoteModel(note!!.id, title, content)
        val status = dbHelper.updateNote(note)

        if (status > -1){
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()

            getNotes()
            clearEditText()
            recyclerView.adapter = adapter

        }else{
            Toast.makeText(this, "Note update failed", Toast.LENGTH_SHORT).show()
        }

        btnAdd.isEnabled = true
        btnAdd.isClickable = true
    }

    private fun getNotes() {
        val notes = dbHelper.getAllNotes()
        adapter?.addItems(notes)
        recyclerView.adapter = adapter
    }

    private fun addNote(){
        val title = title.text.toString()
        val content = content.text.toString()

        if (title.isNotEmpty() && content.isNotEmpty()){
            val note = NoteModel(0, title, content)
            val status = dbHelper.insertData(note)

            if (status > -1){
                Toast.makeText(applicationContext, "Note added...", Toast.LENGTH_LONG).show()
                clearEditText()
                getNotes()
            }else{
                Toast.makeText(applicationContext, "Error adding note...", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(applicationContext, "Please fill all fields...", Toast.LENGTH_LONG).show()
        }

    }

    private fun deleteNote(id:Int){
        if (id == null) return

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are u sure u want to delete this item")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes"){dialog, which ->
            dbHelper.deleteStudentById(id)
            getNotes()
            recyclerView.adapter = adapter
            dialog.dismiss()
        }

        builder.setNegativeButton("No"){dialog, which ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }

    private fun clearEditText() {
        title.text.clear()
        content.text.clear()
        title.requestFocus()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NoteAdapter()
        recyclerView.adapter = adapter
    }

    private fun init(){
        title = findViewById(R.id.title)
        content = findViewById(R.id.content)
        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.listView)
    }


}