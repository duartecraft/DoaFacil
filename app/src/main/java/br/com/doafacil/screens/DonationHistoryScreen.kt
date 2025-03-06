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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
fun DonationHistoryScreen(navController: NavController) {
    // Lista mockada de doações (em um app real, viria de um ViewModel)
    val donations = remember {
        listOf(
            Donation(
                "1",
                "Amigos do Bem",
                100.0,
                Date(),
                "Confirmado"
            ),
            Donation(
                "2",
                "Médicos Sem Fronteiras",
                50.0,
                Date(System.currentTimeMillis() - 86400000), // Ontem
                "Confirmado"
            ),
            Donation(
                "3",
                "WWF Brasil",
                75.0,
                Date(System.currentTimeMillis() - 172800000), // 2 dias atrás
                "Confirmado"
            )
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
        if (donations.isEmpty()) {
            // Estado vazio
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Você ainda não fez nenhuma doação.\nQue tal começar agora?",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            // Lista de doações
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    DonationSummary(donations)
                }
                
                items(donations) { donation ->
                    DonationCard(donation)
                }
            }
        }
    }
}

@Composable
private fun DonationSummary(donations: List<Donation>) {
    val totalDonated = donations.sumOf { it.amount }
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    
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
            Text(
                text = "Resumo das Doações",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Total doado: ${numberFormat.format(totalDonated)}",
                style = MaterialTheme.typography.bodyLarge
            )
            
            Text(
                text = "Número de doações: ${donations.size}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DonationCard(donation: Donation) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { /* Navegar para detalhes da doação */ }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = donation.ngoName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = numberFormat.format(donation.amount),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = dateFormat.format(donation.date),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = donation.status,
                    style = MaterialTheme.typography.bodyMedium,
                    color = when (donation.status) {
                        "Confirmado" -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.error
                    }
                )
            }
        }
    }
}

@Preview(
    name = "Histórico de Doações",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait"
)
@Composable
fun DonationHistoryScreenPreview() {
    val previewNavController = rememberNavController()
    DoaFacilTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DonationHistoryScreen(previewNavController)
        }
    }
} 