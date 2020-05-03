package apps.mithari.kotlinandroidarchitecturelivedataviewmodelroom

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {


    //     the annotated property as volatile, meaning
//     that writes to this field are immediately made visible to other threads.

    abstract fun noteDao(): NoteDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: NoteDatabase? = null // it is singleton pattern
        fun getInstance(context: Context): NoteDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "word_database"
                ).addCallback(roomCallback).build()
                INSTANCE = instance
                return instance
            }
        }
        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(INSTANCE!!).execute()
            }
        }

        class PopulateDbAsyncTask(db: NoteDatabase) : AsyncTask<Void, Void, Void>() {
            private val noteDao = db.noteDao()
            override fun doInBackground(vararg params: Void?): Void? {
                noteDao.insert(Note( "Title1", "Description1", 1))
                noteDao.insert(Note( "Title2", "Description2", 2))
                noteDao.insert(Note( "Title3", "Description3", 3))
                noteDao.insert(Note( "Title4", "Description4", 4))
                return null
            }
        }
    }


}