package pl.tswierszcz.watermelon.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pl.tswierszcz.watermelon.data.Flashcard
import pl.tswierszcz.watermelon.data.FlashcardRepository

/**
 * Stage 1: Statistics Data Classes
 */
data class CategoryStat(
    val category: String,
    val total: Int,
    val learned: Int,
    val percent: Int
)

data class ProgressUiState(
    val total: Int = 0,
    val learned: Int = 0,
    val difficult: Int = 0,
    val medium: Int = 0,
    val progressPercent: Int = 0,
    val categoryStats: List<CategoryStat> = emptyList(),
    val difficultWords: List<Flashcard> = emptyList()
)

class FlashcardViewModel(private val repository: FlashcardRepository) : ViewModel() {

    val categories = repository.allCategories.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val allFlashcards = repository.allFlashcards.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    /**
     * Stage 1: Statistics Logic
     * Combining allFlashcards flow to compute the current statistics state.
     */
    val progressUiState: StateFlow<ProgressUiState> = allFlashcards.map { flashcards ->
        if (flashcards.isEmpty()) return@map ProgressUiState()

        val total = flashcards.size
        val learned = flashcards.count { it.knownLevel >= 4 }
        val difficult = flashcards.count { it.knownLevel <= 1 }
        val medium = flashcards.count { it.knownLevel in 2..3 }
        val progressPercent = (learned.toFloat() / total * 100).toInt()

        val categoryGroups = flashcards.groupBy { it.category }
        val catStats = categoryGroups.map { (cat, cards) ->
            val catTotal = cards.size
            val catLearned = cards.count { it.knownLevel >= 4 }
            CategoryStat(
                category = cat,
                total = catTotal,
                learned = catLearned,
                percent = (catLearned.toFloat() / catTotal * 100).toInt()
            )
        }.sortedByDescending { it.percent }

        val difficultWords = flashcards.filter { it.knownLevel <= 1 }
            .shuffled() // Show a mix
            .take(10)

        ProgressUiState(
            total = total,
            learned = learned,
            difficult = difficult,
            medium = medium,
            progressPercent = progressPercent,
            categoryStats = catStats,
            difficultWords = difficultWords
        )
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), ProgressUiState()
    )

    fun getFlashcardsByCategory(category: String): Flow<List<Flashcard>> {
        return repository.getFlashcardsByCategory(category)
    }

    fun updateKnownLevel(id: Int, currentLevel: Int, increase: Boolean) {
        viewModelScope.launch {
            val newLevel = if (increase) {
                (currentLevel + 1).coerceAtMost(5)
            } else {
                (currentLevel - 1).coerceAtLeast(0)
            }
            repository.updateKnownLevel(id, newLevel)
        }
    }
}

class FlashcardViewModelFactory(private val repository: FlashcardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlashcardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FlashcardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
