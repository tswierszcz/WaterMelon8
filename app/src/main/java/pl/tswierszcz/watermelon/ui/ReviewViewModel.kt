package pl.tswierszcz.watermelon.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import pl.tswierszcz.watermelon.data.Flashcard
import pl.tswierszcz.watermelon.data.FlashcardRepository

data class ReviewUiState(
    val flashcards: List<Flashcard> = emptyList(),
    val currentIndex: Int = 0,
    val isFlipped: Boolean = false,
    val isLoading: Boolean = true,
    val title: String = ""
)

class ReviewViewModel(private val repository: FlashcardRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    fun loadReviewSession(type: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val flashcards = when (type) {
                "difficult" -> repository.getDifficultFlashcards().first()
                else -> repository.getRandomFlashcards(15).first()
            }
            val title = if (type == "difficult") "Powtórka trudnych" else "Szybka powtórka"
            _uiState.value = ReviewUiState(
                flashcards = flashcards,
                isLoading = false,
                title = title
            )
        }
    }

    fun flipCard() {
        _uiState.value = _uiState.value.copy(isFlipped = !_uiState.value.isFlipped)
    }

    fun nextCard() {
        val nextIndex = _uiState.value.currentIndex + 1
        if (nextIndex < _uiState.value.flashcards.size) {
            _uiState.value = _uiState.value.copy(
                currentIndex = nextIndex,
                isFlipped = false
            )
        }
    }

    fun updateKnownLevel(isKnown: Boolean) {
        val currentState = _uiState.value
        val currentCard = currentState.flashcards.getOrNull(currentState.currentIndex) ?: return

        viewModelScope.launch {
            val newLevel = if (isKnown) {
                (currentCard.knownLevel + 1).coerceAtMost(5)
            } else {
                (currentCard.knownLevel - 1).coerceAtLeast(0)
            }
            repository.updateKnownLevel(currentCard.id, newLevel)
            
            // Move to next card after marking
            nextCard()
        }
    }
}

class ReviewViewModelFactory(private val repository: FlashcardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReviewViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
