package pl.tswierszcz.watermelon.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pl.tswierszcz.watermelon.data.Flashcard
import pl.tswierszcz.watermelon.ui.theme.OnSuccessContainerGreen
import pl.tswierszcz.watermelon.ui.theme.SuccessContainerGreen

/**
 * REUSABLE COMPONENTS
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArbuzTopAppBar(
    title: String,
    navController: NavController? = null,
    showBack: Boolean = true
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            if (showBack && navController != null) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Cofnij"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun ModernCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            content = content
        )
    }
}

@Composable
fun ActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    containerColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.2f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }
        }
    }
}

/**
 * SCREENS
 */

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        // App Identity
        Text(
            text = "🍉 Arbuz 8",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Fiszki do egzaminu ósmoklasisty",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        ActionCard(
            title = "Ucz się",
            subtitle = "Przeglądaj fiszki",
            icon = Icons.Default.School,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            onClick = { navController.navigate("categories/study") }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        ActionCard(
            title = "Quiz",
            subtitle = "Sprawdź swoją wiedzę",
            icon = Icons.Default.Star,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            onClick = { navController.navigate("categories/quiz") }
        )

        /** NEW: Repetition Entry Points */
        Spacer(modifier = Modifier.height(16.dp))
        ActionCard(
            title = "Powtórz trudne",
            subtitle = "Słówka, których nie umiesz",
            icon = Icons.Default.Warning,
            containerColor = MaterialTheme.colorScheme.errorContainer,
            onClick = { navController.navigate("review/difficult") }
        )

        Spacer(modifier = Modifier.height(16.dp))
        ActionCard(
            title = "Szybka powtórka",
            subtitle = "Losowe 15 słówek",
            icon = Icons.Default.Refresh,
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
            onClick = { navController.navigate("review/random") }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        ActionCard(
            title = "Postęp",
            subtitle = "Twoje statystyki",
            icon = Icons.Default.TrendingUp,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            onClick = { navController.navigate("progress") }
        )
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun CategoryScreen(navController: NavController, type: String, viewModel: FlashcardViewModel) {
    val categories by viewModel.categories.collectAsState()

    Scaffold(
        topBar = { ArbuzTopAppBar(title = "Kategorie", navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { category ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val dest = if (type == "study") "flashcards" else "quiz"
                                navController.navigate("$dest/$category")
                            },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FlashcardScreen(navController: NavController, category: String, viewModel: FlashcardViewModel) {
    val flashcards by viewModel.getFlashcardsByCategory(category).collectAsState(initial = emptyList())
    var currentIndex by remember { mutableStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    // Animation for flipping
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400)
    )

    Scaffold(
        topBar = { ArbuzTopAppBar(title = category, navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Screen Progress Info
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Kategoria: $category",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${currentIndex + 1}/${flashcards.size}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (flashcards.isNotEmpty()) {
                val currentCard = flashcards[currentIndex]

                // Flashcard with flip animation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 12f * density
                        }
                        .clickable { isFlipped = !isFlipped },
                    contentAlignment = Alignment.Center
                ) {
                    if (rotation <= 90f) {
                        // FRONT
                        Card(
                            modifier = Modifier.fillMaxSize(),
                            shape = RoundedCornerShape(32.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = currentCard.english,
                                    style = MaterialTheme.typography.displayMedium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Dotknij, aby odwrócić",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp)
                                )
                            }
                        }
                    } else {
                        // BACK
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { rotationY = 180f },
                            shape = RoundedCornerShape(32.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = currentCard.polish,
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f))
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                    text = currentCard.example,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                                    textAlign = TextAlign.Center,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Controls
            if (flashcards.isNotEmpty()) {
                val currentCard = flashcards[currentIndex]
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // "Nie umiem" Button
                    Button(
                        onClick = {
                            viewModel.updateKnownLevel(currentCard.id, currentCard.knownLevel, false)
                            isFlipped = false
                            if (currentIndex < flashcards.size - 1) currentIndex++
                        },
                        modifier = Modifier.weight(1f).height(64.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Zle", fontWeight = FontWeight.Bold)
                    }

                    // "Umiem" Button
                    Button(
                        onClick = {
                            viewModel.updateKnownLevel(currentCard.id, currentCard.knownLevel, true)
                            isFlipped = false
                            if (currentIndex < flashcards.size - 1) currentIndex++
                        },
                        modifier = Modifier.weight(1f).height(64.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SuccessContainerGreen,
                            contentColor = OnSuccessContainerGreen
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Dobrze", fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                TextButton(
                    onClick = {
                        isFlipped = false
                        if (currentIndex < flashcards.size - 1) currentIndex++
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = currentIndex < flashcards.size - 1
                ) {
                    Text("Pomiń słówko")
                }
            }
        }
    }
}

/** NEW: Reusable Review Screen */
@Composable
fun ReviewScreen(navController: NavController, type: String, viewModel: ReviewViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(type) {
        viewModel.loadReviewSession(type)
    }

    Scaffold(
        topBar = { ArbuzTopAppBar(title = uiState.title, navController = navController) }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.flashcards.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Brak słówek do powtórki! 🎉",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.popBackStack() }) {
                    Text("Wróć")
                }
            }
        } else {
            val currentCard = uiState.flashcards[uiState.currentIndex]
            val rotation by animateFloatAsState(
                targetValue = if (uiState.isFlipped) 180f else 0f,
                animationSpec = tween(durationMillis = 400)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Progress
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Słówko",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${uiState.currentIndex + 1}/${uiState.flashcards.size}",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 12f * density
                        }
                        .clickable { viewModel.flipCard() },
                    contentAlignment = Alignment.Center
                ) {
                    if (rotation <= 90f) {
                        Card(
                            modifier = Modifier.fillMaxSize(),
                            shape = RoundedCornerShape(32.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = currentCard.english,
                                    style = MaterialTheme.typography.displayMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        Card(
                            modifier = Modifier.fillMaxSize().graphicsLayer { rotationY = 180f },
                            shape = RoundedCornerShape(32.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = currentCard.polish,
                                    style = MaterialTheme.typography.headlineMedium,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                    text = currentCard.example,
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { viewModel.updateKnownLevel(false) },
                        modifier = Modifier.weight(1f).height(64.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer, contentColor = MaterialTheme.colorScheme.onErrorContainer),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Nie umiem", fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { viewModel.updateKnownLevel(true) },
                        modifier = Modifier.weight(1f).height(64.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessContainerGreen, contentColor = OnSuccessContainerGreen),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Umiem", fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                TextButton(
                    onClick = { viewModel.nextCard() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.currentIndex < uiState.flashcards.size - 1
                ) {
                    Text("Następna")
                }
            }
        }
    }
}

@Composable
fun QuizScreen(navController: NavController, category: String, viewModel: FlashcardViewModel) {
    val flashcards by viewModel.getFlashcardsByCategory(category).collectAsState(initial = emptyList())
    var currentIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = { ArbuzTopAppBar(title = "Quiz", navController = navController) }
    ) { innerPadding ->
        if (flashcards.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (currentIndex >= flashcards.size) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                QuizResultScreen(score, flashcards.size, onBack = { navController.popBackStack() })
            }
        } else {
            val currentCard = flashcards[currentIndex]
            val options = remember(currentCard) {
                val correct = currentCard.polish
                val otherPool = flashcards.filter { it.polish != correct }.map { it.polish }
                val wrong = if (otherPool.size >= 3) otherPool.shuffled().take(3) else otherPool
                (wrong + correct).shuffled()
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
            ) {
                // Progress Header
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LinearProgressIndicator(
                        progress = { (currentIndex + 1).toFloat() / flashcards.size },
                        modifier = Modifier.weight(1f).padding(end = 24.dp).height(8.dp).clip(RoundedCornerShape(4.dp))
                    )
                    Text("${currentIndex + 1}/${flashcards.size}", style = MaterialTheme.typography.labelLarge)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Co oznacza to słowo?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = currentCard.english,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(48.dp))

                options.forEach { option ->
                    val isSelected = selectedAnswer == option
                    val isCorrect = option == currentCard.polish
                    
                    val borderColor = when {
                        selectedAnswer == null -> Color.Transparent
                        isSelected && isCorrect -> SuccessContainerGreen
                        isSelected && !isCorrect -> MaterialTheme.colorScheme.error
                        !isSelected && isCorrect -> SuccessContainerGreen
                        else -> Color.Transparent
                    }

                    val containerColor by animateColorAsState(
                        targetValue = when {
                            selectedAnswer == null -> MaterialTheme.colorScheme.surface
                            isSelected && isCorrect -> SuccessContainerGreen.copy(alpha = 0.2f)
                            isSelected && !isCorrect -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                            else -> MaterialTheme.colorScheme.surface
                        }
                    )

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable(enabled = selectedAnswer == null) {
                                selectedAnswer = option
                                if (isCorrect) score++
                            },
                        shape = RoundedCornerShape(20.dp),
                        color = containerColor,
                        border = if (borderColor != Color.Transparent) androidx.compose.foundation.BorderStroke(2.dp, borderColor) else null,
                        shadowElevation = 1.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = option,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.weight(1f)
                            )
                            if (selectedAnswer != null) {
                                if (isCorrect) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = SuccessContainerGreen)
                                } else if (isSelected) {
                                    Icon(Icons.Default.Close, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                if (selectedAnswer != null) {
                    Button(
                        onClick = {
                            selectedAnswer = null
                            currentIndex++
                        },
                        modifier = Modifier.fillMaxWidth().height(64.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Następne pytanie", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun QuizResultScreen(score: Int, total: Int, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(120.dp),
            shape = RoundedCornerShape(40.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                Icons.Default.Star, 
                contentDescription = null,
                modifier = Modifier.padding(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Koniec Quizu!",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Twój wynik to",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Text(
            text = "$score / $total",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Black
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().height(64.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Wróć do menu", style = MaterialTheme.typography.titleMedium)
        }
    }
}

/**
 * Stage 1: Extended Progress Screen
 */
@Composable
fun ProgressScreen(viewModel: FlashcardViewModel, navController: NavController) {
    val uiState by viewModel.progressUiState.collectAsState()

    Scaffold(
        topBar = { ArbuzTopAppBar(title = "Postęp", navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            // 1. Summary Section
            ModernCard(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                Text(
                    text = "Opanowane słówka",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = { uiState.progressPercent / 100f },
                    modifier = Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(6.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "${uiState.progressPercent}% gotowości",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "${uiState.learned} / ${uiState.total}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Detailed Grid Stats
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard(
                    label = "Wszystkie",
                    value = "${uiState.total}",
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Średnie",
                    value = "${uiState.medium}",
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard(
                    label = "Opanowane",
                    value = "${uiState.learned}",
                    color = SuccessContainerGreen.copy(alpha = 0.4f),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Trudne",
                    value = "${uiState.difficult}",
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 2. Category Stats Section
            Text(
                text = "Postęp według kategorii",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            uiState.categoryStats.forEach { stat ->
                CategoryProgressItem(stat)
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 3. Difficult Words Section
            if (uiState.difficultWords.isNotEmpty()) {
                Text(
                    text = "Najtrudniejsze słowa",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                uiState.difficultWords.forEach { word ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = word.english, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(text = word.polish, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Icon(
                                imageVector = Icons.Default.Close, 
                                contentDescription = null, 
                                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun CategoryProgressItem(stat: CategoryStat) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = stat.category, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "${stat.percent}%", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { stat.percent / 100f },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp))
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${stat.learned} z ${stat.total} słówek",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StatCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(24.dp),
        color = color
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = label, style = MaterialTheme.typography.labelLarge)
            Text(text = value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }
    }
}
