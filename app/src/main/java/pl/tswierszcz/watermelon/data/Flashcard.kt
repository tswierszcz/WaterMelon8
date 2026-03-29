package pl.tswierszcz.watermelon.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
data class Flashcard(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val english: String,
    val polish: String,
    val example: String,
    val knownLevel: Int = 0 // 0-5
)
