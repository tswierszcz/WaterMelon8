package pl.tswierszcz.watermelon.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class JsonAssetLoader(private val context: Context) {

    private val gson = Gson()

    /**
     * Reads a JSON file from assets and parses it into a list of Flashcards.
     */
    fun loadFlashcardsFromAsset(fileName: String): List<Flashcard> {
        return try {
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val listType = object : TypeToken<List<Flashcard>>() {}.type
            gson.fromJson(jsonString, listType)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            emptyList()
        }
    }

    /**
     * Loads flashcards from all specified category files.
     */
    fun loadAllFlashcards(): List<Flashcard> {
        val files = listOf(
            "szkola.json",
            "jedzenie.json",
            "czlowiek.json",
            "przydatne_zwroty.json"
        )
        return files.flatMap { loadFlashcardsFromAsset(it) }
    }
}
