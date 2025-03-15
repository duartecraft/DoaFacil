package br.com.doafacil.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

@SuppressLint("AutoboxingStateCreation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        GamificationManager.init(context)
    }

    val userPoints by remember { mutableIntStateOf(GamificationManager.getPoints()) }
    val userLevel by remember { mutableStateOf(GamificationManager.getLevel()) }
    val nextLevelThreshold = getNextLevelThreshold(userPoints)

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
                GamificationSection(userPoints, userLevel, nextLevelThreshold)
            }

            item {
                WelcomeSection()
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
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Sua Pontuação: $userPoints",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Status: $userLevel",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Barra de progresso da pontuação até o próximo nível
        LinearProgressIndicator(
            progress = { userPoints.toFloat() / nextLevelThreshold.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
        )

        Text(
            text = "Próximo nível em ${nextLevelThreshold - userPoints} pontos",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))
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
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.featured_ngos),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Lista de ONGs em destaque (mockada por enquanto)
        val featuredNGOs = listOf(
            "Amigos do Bem" to "Combate à fome e pobreza",
            "Médicos Sem Fronteiras" to "Assistência médica humanitária",
            "WWF Brasil" to "Conservação da natureza"
        )

        featuredNGOs.forEach { (name, description) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                onClick = { navController.navigate(Routes.ngoDetails("1")) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = name, fontWeight = FontWeight.Bold)
                    Text(text = description)
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
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait"
)
@Composable
fun HomeScreenPreview() {
    val previewNavController = rememberNavController()
    DoaFacilTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreen(previewNavController)
        }
    }
}
