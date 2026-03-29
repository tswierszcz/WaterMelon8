package pl.tswierszcz.watermelon

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import pl.tswierszcz.watermelon.data.AppDatabase
import pl.tswierszcz.watermelon.data.FlashcardRepository
import pl.tswierszcz.watermelon.data.JsonAssetLoader

class FlashcardApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    private val jsonAssetLoader by lazy { JsonAssetLoader(this) }
    val repository by lazy { FlashcardRepository(database.flashcardDao(), jsonAssetLoader) }

    override fun onCreate() {
        super.onCreate()
        // Initialize data if needed
        val scope = CoroutineScope(SupervisorJob())
        scope.launch {
            repository.insertInitialData()
        }
    }
}
