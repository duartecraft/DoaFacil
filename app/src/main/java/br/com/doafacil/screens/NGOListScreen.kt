package br.com.doafacil.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.doafacil.ui.theme.DoaFacilTheme
import br.com.doafacil.navigation.Routes

data class NGO(
    val id: String,
    val name: String,
    val location: String,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NGOListScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    
    // Lista mockada de ONGs
    val ngos = remember {
        listOf(
            NGO("1", "Amigos do Bem", "São Paulo, SP", "Combate à fome e pobreza em regiões carentes"),
            NGO("2", "Médicos Sem Fronteiras", "Rio de Janeiro, RJ", "Assistência médica humanitária em áreas de conflito"),
            NGO("3", "WWF Brasil", "Brasília, DF", "Conservação da natureza e redução do impacto humano"),
            NGO("4", "UNICEF Brasil", "São Paulo, SP", "Defesa dos direitos das crianças e adolescentes"),
            NGO("5", "Teto Brasil", "São Paulo, SP", "Construção de moradias emergenciais para famílias vulneráveis")
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ONGs Parceiras") },
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
        ) {
            // Barra de pesquisa
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Pesquisar ONGs...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Ícone de pesquisa"
                    )
                },
                singleLine = true
            )
            
            // Lista de ONGs
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(ngos.filter {
                    it.name.contains(searchQuery, ignoreCase = true) ||
                    it.description.contains(searchQuery, ignoreCase = true)
                }) { ngo ->
                    NGOCard(ngo = ngo, navController = navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NGOCard(ngo: NGO, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { navController.navigate(Routes.ngoDetails(ngo.id)) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = ngo.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = ngo.location,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = ngo.description,
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { navController.navigate(Routes.ngoDetails(ngo.id)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Detalhes")
            }
        }
    }
}

@Preview(
    name = "Lista de ONGs",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait"
)
@Composable
fun NGOListScreenPreview() {
    val previewNavController = rememberNavController()
    DoaFacilTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NGOListScreen(previewNavController)
        }
    }
} 