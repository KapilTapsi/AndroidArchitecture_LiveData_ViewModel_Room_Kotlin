package apps.mithari.kotlinandroidarchitecturelivedataviewmodelroom

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
//table name is customizable and optional
//you can customize the table name using @Entity(tableName="someTableName")
//the default table name is the class name
data class Note(
        val title: String,      //we can have custom table name using @ColumnInfo(name = "myTitle")
        val description: String,
        val priority: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}