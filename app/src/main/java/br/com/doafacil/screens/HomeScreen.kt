package br.com.doafacil.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.doafacil.R
import br.com.doafacil.navigation.Routes
import br.com.doafacil.ui.theme.DoaFacilTheme
import br.com.doafacil.utils.GamificationManager
import br.com.doafacil.data.NGORepository
import br.com.doafacil.model.NGO

@SuppressLint("AutoboxingStateCreation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    userPoints: Int? = null,
    userLevel: String? = null
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        GamificationManager.init(context)
    }

    val actualUserPoints = userPoints ?: GamificationManager.getPoints()
    val actualUserLevel = userLevel ?: GamificationManager.getLevel()
    val nextLevelThreshold = getNextLevelThreshold(actualUserPoints)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                WelcomeSection()
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                GamificationSection(
                    userPoints = actualUserPoints,
                    userLevel = actualUserLevel,
                    nextLevelThreshold = nextLevelThreshold
                )
            }

            item {
                FeaturedNGOsSection(navController)
            }

            item {
                QuickActionsSection(navController)
            }

            item {
                EvaluationSection(navController)
            }
        }
    }
}

/**
 * Seção de Gamificação: Exibe pontos, status e progresso do usuário
 */
@Composable
fun GamificationSection(userPoints: Int, userLevel: String, nextLevelThreshold: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Sua pontuação: $userPoints",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "Nível: $userLevel",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Barra de progresso
            LinearProgressIndicator(
                progress = { userPoints.toFloat() / nextLevelThreshold.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Próximo nível em ${nextLevelThreshold - userPoints} pontos",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

/**
 * Retorna o limite de pontos necessário para o próximo nível
 */
fun getNextLevelThreshold(userPoints: Int): Int {
    return when {
        userPoints >= 50000 -> 50000
        userPoints >= 10000 -> 50000
        userPoints >= 5000 -> 10000
        userPoints >= 1000 -> 5000
        userPoints >= 500 -> 1000
        userPoints >= 100 -> 500
        else -> 100
    }
}

@Composable
private fun WelcomeSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.welcome),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.slogan),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun EvaluationSection(navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { navController.navigate("RATE") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.rate))
        }
    }
}

@Composable
private fun FeaturedNGOsSection(navController: NavController) {
    var ngos by remember { mutableStateOf<List<NGO>>(emptyList()) }

    // Carrega os dados apenas uma vez
    LaunchedEffect(Unit) {
        ngos = NGORepository.getAllNGOs().take(3)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.featured_ngos),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ngos.forEach { ngo ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                onClick = { navController.navigate(Routes.ngoDetails(ngo.id)) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = ngo.name, fontWeight = FontWeight.Bold)
                    Text(text = ngo.description)
                }
            }
        }
    }
}

@Composable
private fun QuickActionsSection(navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.quick_actions),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.navigate(Routes.NGO_LIST) },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(stringResource(R.string.view_all_ngos))
            }

            Button(
                onClick = { navController.navigate(Routes.DONATION_HISTORY) },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(stringResource(R.string.my_donations))
            }
        }
    }
}

@Preview(
    name = "Tela Inicial",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HomeScreenPreview() {
    val previewNavController = rememberNavController()

    val fakeUserPoints = 30
    val fakeUserLevel = "Iniciante"

    DoaFacilTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            HomeScreen(
                navController = previewNavController,
                userPoints = fakeUserPoints,
                userLevel = fakeUserLevel
            )
        }
    }
}