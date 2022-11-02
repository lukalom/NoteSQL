package com.example.notesql

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "Notes.db"
        private val TABLE_NAME = "Notes"
        private val COLUMN_ID = "id"
        private val COLUMN_TITLE = "title"
        private val COLUMN_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TITLE + " TEXT,"
                + COLUMN_CONTENT + " TEXT" + ")")

        db?.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(note:NoteModel): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()

        contentValues.put(COLUMN_TITLE, note.title)
        contentValues.put(COLUMN_CONTENT, note.content)

        val success = db.insert(TABLE_NAME, null, contentValues)
        db.close();

        return success
    }

    @SuppressLint("Range")
    fun getAllNotes() : ArrayList<NoteModel>{
        val notes: ArrayList<NoteModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase

        val cursor: Cursor?
        try {
             cursor = db.rawQuery(selectQuery, null)
        }catch (e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var title: String
        var content: String

        if (cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                title = cursor.getString(cursor.getColumnIndex("title"))
                content = cursor.getString(cursor.getColumnIndex("content"))

                val note = NoteModel(id = id, title = title, content = content)
                notes.add(note)

            }while (cursor.moveToNext())
        }

        return notes
    }

    fun updateNote(note: NoteModel): Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(COLUMN_ID, note.id)
        contentValues.put(COLUMN_TITLE, note.title)
        contentValues.put(COLUMN_CONTENT, note.content)

        val success = db.update(TABLE_NAME, contentValues, "id=" + note.id, null)
        db.close()

        return success
    }

    fun deleteStudentById(id: Int): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_ID, id)

        val success = db.delete(TABLE_NAME, "id=" + id, null)
        db.close()

        return success
    }
}


