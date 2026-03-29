package pl.tswierszcz.watermelon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pl.tswierszcz.watermelon.ui.*
import pl.tswierszcz.watermelon.ui.theme.WaterMelonTheme

class MainActivity : ComponentActivity() {
    private val viewModel: FlashcardViewModel by viewModels {
        FlashcardViewModelFactory((application as FlashcardApplication).repository)
    }

    /** NEW: Review ViewModel */
    private val reviewViewModel: ReviewViewModel by viewModels {
        ReviewViewModelFactory((application as FlashcardApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WaterMelonTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable("home") { HomeScreen(navController) }
                    composable("categories/{type}") { backStackEntry ->
                        val type = backStackEntry.arguments?.getString("type") ?: "study"
                        CategoryScreen(navController, type, viewModel)
                    }
                    composable("flashcards/{category}") { backStackEntry ->
                        val category = backStackEntry.arguments?.getString("category") ?: ""
                        FlashcardScreen(navController, category, viewModel)
                    }
                    composable("quiz/{category}") { backStackEntry ->
                        val category = backStackEntry.arguments?.getString("category") ?: ""
                        QuizScreen(navController, category, viewModel)
                    }
                    composable("progress") { ProgressScreen(viewModel, navController) }

                    /** NEW: Review Navigation Routes */
                    composable("review/{type}") { backStackEntry ->
                        val type = backStackEntry.arguments?.getString("type") ?: "random"
                        ReviewScreen(navController, type, reviewViewModel)
                    }
                }
            }
        }
    }
}
