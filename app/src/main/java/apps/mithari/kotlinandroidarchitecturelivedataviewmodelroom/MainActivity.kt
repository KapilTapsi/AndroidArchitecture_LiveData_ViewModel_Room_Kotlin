package apps.mithari.kotlinandroidarchitecturelivedataviewmodelroom

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.mithari.kotlinandroidarchitecturelivedataviewmodelroom.Utils.logd
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    companion object {
        val ADD_NOTE_REQUEST = 1
        val EDIT_NOTE_REQUEST = 2
    }

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteViewModelFactory: NoteViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        floatingActionButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.hasFixedSize()
        val adapter = NoteAdapter()
        recycler_view.adapter = adapter
        val application = requireNotNull(this).application
        noteViewModelFactory = NoteViewModelFactory(application)
        noteViewModel = ViewModelProvider(this, noteViewModelFactory).get(NoteViewModel::class.java)
        noteViewModel.getAllNotes().observe(this,
                Observer {
                    adapter.submitList(it)
//                    submit list is used because we are using List adapter instead of recycler view adapter
                })

        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(this@MainActivity, "Note Deleted", Toast.LENGTH_SHORT).show()
            }

        }
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recycler_view)

        adapter.setOnClickListener(object : NoteAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
                intent.putExtra(AddEditNoteActivity.Extra_ID, note.id)
                intent.putExtra(AddEditNoteActivity.Extra_Title, note.title)
                intent.putExtra(AddEditNoteActivity.Extra_Description, note.description)
                intent.putExtra(AddEditNoteActivity.Extra_Priority, note.priority)
                startActivityForResult(intent, EDIT_NOTE_REQUEST)
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val title = data?.getStringExtra(AddEditNoteActivity.Extra_Title)
            val description = data?.getStringExtra(AddEditNoteActivity.Extra_Description)
            val priority = data?.getIntExtra(AddEditNoteActivity.Extra_Priority, 1)
            val note = Note(title!!, description!!, priority!!)
            noteViewModel.insert(note)
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val id = data?.getIntExtra(AddEditNoteActivity.Extra_ID, -1)
            if (id == -1) {
                Toast.makeText(this, "Note Can't be updated", Toast.LENGTH_SHORT).show()
                return
            }
            val title = data?.getStringExtra(AddEditNoteActivity.Extra_Title)
            val description = data?.getStringExtra(AddEditNoteActivity.Extra_Description)
            val priority = data?.getIntExtra(AddEditNoteActivity.Extra_Priority, 1)
            val note = Note(title!!, description!!, priority!!)
            note.id = id!!
            noteViewModel.update(note)
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No Note Saved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_notes -> {
                noteViewModel.deleteAllNotes()
                Toast.makeText(this, "All Notes Deleted", Toast.LENGTH_SHORT).show()
                true

            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}
