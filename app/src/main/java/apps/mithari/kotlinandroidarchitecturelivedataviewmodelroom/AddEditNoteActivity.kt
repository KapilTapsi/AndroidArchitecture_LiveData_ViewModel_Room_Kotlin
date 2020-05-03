package apps.mithari.kotlinandroidarchitecturelivedataviewmodelroom

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import apps.mithari.kotlinandroidarchitecturelivedataviewmodelroom.Utils.logd
import kotlinx.android.synthetic.main.activity_add_note.*

class AddEditNoteActivity : AppCompatActivity() {
    companion object {
        val Extra_ID = "apps.mithari.kotlinandroidarchitecturelivedataviewmodelroom.ExtraID"
        val Extra_Title = "apps.mithari.kotlinandroidarchitecturelivedataviewmodelroom.ExtraTitle"
        val Extra_Description = "apps.mithari.kotlinandroidarchitecturelivedataviewmodelroom.ExtraDescription"
        val Extra_Priority = "apps.mithari.kotlinandroidarchitecturelivedataviewmodelroom.ExtraPriority"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        number_picker_priority.minValue = 0
        number_picker_priority.maxValue = 10

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close)
        if (intent.hasExtra(Extra_ID)) {
            title = "Edit Note"
            edit_text_title.setText(intent.getStringExtra(Extra_Title))
            edit_text_description.setText(intent.getStringExtra(Extra_Description))
            number_picker_priority.value = intent.getIntExtra(Extra_Priority, 1)
        } else {
            title = "Add Note"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_note -> {
                saveNote()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    private fun saveNote() {
        val title = edit_text_title.text.toString()
        val description = edit_text_description.text.toString()
        val priority = number_picker_priority.value

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please Enter a Title and Description", Toast.LENGTH_SHORT).show()
            return
        }
        val newIntent=Intent()
        newIntent.putExtra(Extra_Title, title)
        newIntent.putExtra(Extra_Description, description)
        newIntent.putExtra(Extra_Priority, priority)
        val id=intent.getIntExtra(Extra_ID,-1)
        if (id!=-1){
            logd(id.toString())
            newIntent.putExtra(Extra_ID,id)
        }
        setResult(Activity.RESULT_OK, newIntent)
//        this checks if all the above things are done and sends result ok with the intent
        finish()
    }
}

