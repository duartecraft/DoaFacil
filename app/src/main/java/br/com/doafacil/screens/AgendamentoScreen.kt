package br.com.doafacil.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.doafacil.ui.theme.DoaFacilTheme
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendamentoScreen(
    ngoId: String,
    navController: NavController
) {
    val context = LocalContext.current

    val ngo = remember {
        listOf(
            NGO("1", "Amigos do Bem", "São Paulo, SP", "Combate à fome e pobreza"),
            NGO("2", "Médicos Sem Fronteiras", "Rio de Janeiro, RJ", "Assistência médica humanitária"),
            NGO("3", "WWF Brasil", "Brasília, DF", "Conservação da natureza"),
            NGO("4", "UNICEF Brasil", "São Paulo, SP", "Defesa dos direitos das crianças"),
            NGO("5", "Teto Brasil", "São Paulo, SP", "Construção de moradias")
        ).find { it.id == ngoId }
    }

    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar Visita") },
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
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (ngo != null) {
                Text(
                    text = "ONG: ${ngo.name}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Localização: ${ngo.location}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = { selectedDate = it },
                    label = { Text("Escolha a data") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = selectedTime,
                    onValueChange = { selectedTime = it },
                    label = { Text("Escolha o horário") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
                            Toast.makeText(
                                context,
                                "Visita agendada para $selectedDate às $selectedTime!",
                                Toast.LENGTH_LONG
                            ).show()
                            navController.navigateUp()
                        } else {
                            Toast.makeText(
                                context,
                                "Preencha a data e horário!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Confirmar Agendamento")
                }
            } else {
                Text(
                    text = "Erro ao carregar informações da ONG.",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(
    name = "Tela de Agendamento",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait"
)
@Composable
fun AgendamentoScreenPreview() {
    val previewNavController = rememberNavController()
    DoaFacilTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AgendamentoScreen(
                ngoId = "1",
                navController = previewNavController
            )
        }
    }
}
