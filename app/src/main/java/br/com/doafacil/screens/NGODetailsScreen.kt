package br.com.doafacil.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.doafacil.navigation.Routes
import br.com.doafacil.ui.theme.DoaFacilTheme

// Importamos a classe NGO do nosso pacote
import br.com.doafacil.screens.NGO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NGODetailsScreen(
    ngoId: String,
    navController: NavController
) {
    var showDonationDialog by remember { mutableStateOf(false) }
    var showFeedbackDialog by remember { mutableStateOf(false) }
    
    // Dados mockados da ONG (em um app real, isso viria de um ViewModel)
    val ngo = remember {
        NGO(
            id = ngoId,
            name = "Amigos do Bem",
            location = "São Paulo, SP",
            description = "Combate à fome e pobreza em regiões carentes do sertão nordestino através de ações continuadas nas áreas de educação, trabalho e renda, água, moradia e saúde."
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(ngo.name) },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Informações básicas
            Text(
                text = ngo.location,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // Descrição completa
            Text(
                text = "Sobre a ONG",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = ngo.description,
                style = MaterialTheme.typography.bodyLarge
            )
            
            // ... resto do código permanece igual
        }
    }
    
    // Dialog de doação
    if (showDonationDialog) {
        DonationDialog(
            onDismiss = { showDonationDialog = false },
            onConfirm = {
                // Aqui seria gerado o QR Code do PIX
                showDonationDialog = false
                navController.navigate(Routes.DONATION_HISTORY)
            }
        )
    }
    
    // Dialog de feedback
    if (showFeedbackDialog) {
        FeedbackDialog(
            onDismiss = { showFeedbackDialog = false },
            onSubmit = { rating, comment ->
                // Aqui seria enviado o feedback para a API
                showFeedbackDialog = false
            }
        )
    }
}

@Composable
private fun DonationDialog(
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var donationAmount by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Fazer Doação") },
        text = {
            Column {
                Text("Digite o valor que deseja doar:")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = donationAmount,
                    onValueChange = { donationAmount = it },
                    label = { Text("Valor (R$)") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    donationAmount.toDoubleOrNull()?.let { onConfirm(it) }
                }
            ) {
                Text("Gerar QR Code PIX")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun FeedbackDialog(
    onDismiss: () -> Unit,
    onSubmit: (Int, String) -> Unit
) {
    var rating by remember { mutableStateOf(5) }
    var comment by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Avaliar ONG") },
        text = {
            Column {
                Text("Sua avaliação é muito importante para nós!")
                Spacer(modifier = Modifier.height(8.dp))
                
                // Rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(5) { index ->
                        IconButton(
                            onClick = { rating = index + 1 }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (index < rating) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Comentário
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Comentário (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(rating, comment) }
            ) {
                Text("Enviar Avaliação")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Preview(
    name = "Detalhes da ONG",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait"
)
@Composable
fun NGODetailsScreenPreview() {
    val previewNavController = rememberNavController()
    DoaFacilTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NGODetailsScreen(
                ngoId = "1",
                navController = previewNavController
            )
        }
    }
} 