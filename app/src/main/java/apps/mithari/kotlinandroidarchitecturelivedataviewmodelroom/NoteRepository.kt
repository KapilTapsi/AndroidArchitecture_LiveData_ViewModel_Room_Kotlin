package apps.mithari.kotlinandroidarchitecturelivedataviewmodelroom

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class NoteRepository(application: Application) {
    var noteDao:NoteDao
    var allNotess:LiveData<List<Note>>
    init {
        var database=NoteDatabase.getInstance(application)
//        here we created instance of the database
        noteDao=database.noteDao()
        allNotess=noteDao.getAllNotes()
    }

    fun insert(note: Note){
    InsertAsyncTask(noteDao).execute(note)
    }

    fun update(note: Note){
       UpdateAsyncTask(noteDao).execute(note)
    }

    fun delete(note: Note){
        DeleteAsyncTask(noteDao).execute(note)
    }

    fun deleteAllNotes(){
        DeleteAllAsyncTask(noteDao).execute()
    }

    fun getAllNotes():LiveData<List<Note>>{
        return allNotess
    }

    open class InsertAsyncTask(val noteDao: NoteDao):AsyncTask<Note,Void,Void>(){

        override fun doInBackground(vararg params: Note?): Void? {
        noteDao.insert(params[0]!!)
            return null
        }
    }
    class UpdateAsyncTask(val noteDao: NoteDao):AsyncTask<Note,Void,Void>(){

        override fun doInBackground(vararg params: Note?): Void? {
            noteDao.update(params[0]!!)
            return null
        }
    }
    class DeleteAsyncTask(val noteDao: NoteDao):AsyncTask<Note,Void,Void>(){

        override fun doInBackground(vararg params: Note?): Void? {
            noteDao.delete(params[0]!!)
            return null
        }
    }
    class DeleteAllAsyncTask(val noteDao: NoteDao):AsyncTask<Void,Void,Void>(){

        override fun doInBackground(vararg params: Void?): Void? {
            noteDao.deleteAllNotes()
            return null
        }
    }
}