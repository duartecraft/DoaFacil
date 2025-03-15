package br.com.doafacil.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.doafacil.ui.theme.DoaFacilTheme
import br.com.doafacil.utils.GamificationManager
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

data class Donation(
    val id: String,
    val ngoName: String,
    val amount: Double,
    val date: Date,
    val status: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationHistoryScreen(navController: NavController) {
    val isPreview = LocalInspectionMode.current

    val donations = remember {
        listOf(
            Donation("1", "Amigos do Bem", 100.0, Date(), "Confirmado"),
            Donation("2", "Médicos Sem Fronteiras", 50.0, Date(System.currentTimeMillis() - 86400000), "Confirmado"),
            Donation("3", "WWF Brasil", 75.0, Date(System.currentTimeMillis() - 172800000), "Confirmado")
        )
    }

    val userPoints by remember {
        mutableIntStateOf(
            if (isPreview) donations.size * 10 else GamificationManager.getPoints()
        )
    }

    val userLevel by remember {
        mutableStateOf(
            when {
                userPoints >= 50000 -> "Rubi"
                userPoints >= 10000 -> "Safira"
                userPoints >= 5000 -> "Diamante"
                userPoints >= 1000 -> "Ouro"
                userPoints >= 500 -> "Prata"
                userPoints >= 100 -> "Bronze"
                else -> "Iniciante"
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Histórico de Doações") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Visão Geral das Doações",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            DonationDashboard(donations, userPoints, userLevel)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Últimas doações",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(0.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(donations) { donation ->
                    DonationCard(donation)
                }
            }
        }
    }
}

@Composable
fun DonationDashboard(donations: List<Donation>, userPoints: Int, userLevel: String) {
    val totalDonated = donations.sumOf { it.amount }
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    val nextLevel = when {
        userPoints < 100 -> "Bronze"
        userPoints < 500 -> "Prata"
        userPoints < 1000 -> "Ouro"
        userPoints < 5000 -> "Diamante"
        userPoints < 10000 -> "Safira"
        else -> "Rubi"
    }

    val pointsForNextLevel = when {
        userPoints < 100 -> 100 - userPoints
        userPoints < 500 -> 500 - userPoints
        userPoints < 1000 -> 1000 - userPoints
        userPoints < 5000 -> 5000 - userPoints
        else -> 0
    }

    val motivationalMessage = if (userPoints < 100)
        "Parabéns! Você deu o primeiro passo para transformar vidas!" else ""

    CardSection("Total doado", numberFormat.format(totalDonated))
    CardSection(
        "Nível e Pontuação",
        "Nível: $userLevel\nPontuação: $userPoints pontos (+10 por doação)\nFaltam $pointsForNextLevel pontos para atingir *$nextLevel*"
    )
    if (motivationalMessage.isNotEmpty()) {
        CardSection("Mensagem Motivacional", motivationalMessage)
    }
}

@Composable
fun CardSection(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(title, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(content)
        }
    }
}

@Composable
fun DonationCard(donation: Donation) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(donation.ngoName, fontWeight = FontWeight.Bold)
            Text(numberFormat.format(donation.amount), color = MaterialTheme.colorScheme.primary)
            Text(dateFormat.format(donation.date))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DonationHistoryScreenPreview() {
    val fakeNavController = rememberNavController()
    DoaFacilTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DonationHistoryScreen(navController = fakeNavController)
        }
    }
}
