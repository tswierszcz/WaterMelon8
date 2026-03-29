package pl.tswierszcz.watermelon.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class FlashcardRepository(
    private val flashcardDao: FlashcardDao,
    private val jsonAssetLoader: JsonAssetLoader
) {
    val allCategories: Flow<List<String>> = flashcardDao.getCategories()
    val allFlashcards: Flow<List<Flashcard>> = flashcardDao.getAllFlashcards()

    fun getFlashcardsByCategory(category: String): Flow<List<Flashcard>> {
        return flashcardDao.getFlashcardsByCategory(category)
    }

    /** NEW: Repository extensions for repetition system */
    fun getDifficultFlashcards(): Flow<List<Flashcard>> {
        return flashcardDao.getDifficultFlashcards()
    }

    fun getRandomFlashcards(limit: Int): Flow<List<Flashcard>> {
        return flashcardDao.getRandomFlashcards(limit)
    }

    suspend fun updateKnownLevel(id: Int, knownLevel: Int) {
        flashcardDao.updateKnownLevel(id, knownLevel)
    }

    /**
     * Stage: JSON Refactoring
     * Loads data from JSON assets if the database is empty.
     */
    suspend fun insertInitialData() {
        val currentCards = flashcardDao.getAllFlashcards().first()
        if (currentCards.isEmpty()) {
            val initialData = jsonAssetLoader.loadAllFlashcards()
            flashcardDao.insertAll(initialData)
        }
    }
}
