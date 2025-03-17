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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.doafacil.ui.theme.DoaFacilTheme
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
fun DonationHistoryScreen(navController: NavController, userPoints: Int, userLevel: String) {
    val donations = remember {
        listOf(
            Donation("1", "Amigos do Bem", 100.0, Date(), "Confirmado"),
            Donation("2", "Médicos Sem Fronteiras", 50.0, Date(System.currentTimeMillis() - 86400000), "Confirmado"),
            Donation("3", "WWF Brasil", 75.0, Date(System.currentTimeMillis() - 172800000), "Confirmado"),
            Donation("4", "Criança Esperança", 120.0, Date(System.currentTimeMillis() - 259200000), "Confirmado"),
            Donation("5", "AACD", 200.0, Date(System.currentTimeMillis() - 345600000), "Confirmado")
        )
    }

    val donationCount = donations.size
    val motivationalMessage = getMotivationalMessage(donationCount)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Histórico de Doações") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
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

            if (motivationalMessage.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = motivationalMessage,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 12.dp, bottom = 12.dp)
                        )
                    }
                }
            }

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

    CardSection(
        title = "Total doado",
        content = "${numberFormat.format(totalDonated)} - ${donations.size} doações realizadas"
    )

    CardSection(
        title = "Nível e pontuação",
        content = "Nível: $userLevel\nPontuação: $userPoints pontos\n",
        showProgress = true,
        progress = userPoints.toFloat() / 100f,
        progressText = "Faltam $pointsForNextLevel pontos para atingir *$nextLevel*"
    )
}

@Composable
fun CardSection(title: String, content: String, showProgress: Boolean = false, progress: Float = 0f, progressText: String = "") {
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

            if (showProgress) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = progressText,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

// Retorna a mensagem motivacional com base no número de doações
fun getMotivationalMessage(donationCount: Int): String {
    return when {
        donationCount >= 15 -> "Você está mudando o mundo com suas doações!"
        donationCount >= 7 -> "Você é um doador frequente! Seu impacto é incrível!"
        donationCount >= 1 -> "Parabéns! Você deu o primeiro passo para transformar vidas!"
        else -> ""
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
    val fakeUserPoints = 30
    val fakeUserLevel = "Iniciante"

    DoaFacilTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DonationHistoryScreen(navController = fakeNavController, userPoints = fakeUserPoints, userLevel = fakeUserLevel)
        }
    }
}