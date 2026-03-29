package pl.tswierszcz.watermelon.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Query("SELECT DISTINCT category FROM flashcards")
    fun getCategories(): Flow<List<String>>

    @Query("SELECT * FROM flashcards WHERE category = :category")
    fun getFlashcardsByCategory(category: String): Flow<List<Flashcard>>

    @Query("SELECT * FROM flashcards")
    fun getAllFlashcards(): Flow<List<Flashcard>>

    /** NEW: Get difficult flashcards (level 0 or 1) */
    @Query("SELECT * FROM flashcards WHERE knownLevel <= 1")
    fun getDifficultFlashcards(): Flow<List<Flashcard>>

    /** NEW: Get random flashcards */
    @Query("SELECT * FROM flashcards ORDER BY RANDOM() LIMIT :limit")
    fun getRandomFlashcards(limit: Int): Flow<List<Flashcard>>

    @Query("UPDATE flashcards SET knownLevel = :knownLevel WHERE id = :id")
    suspend fun updateKnownLevel(id: Int, knownLevel: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cards: List<Flashcard>)
}
