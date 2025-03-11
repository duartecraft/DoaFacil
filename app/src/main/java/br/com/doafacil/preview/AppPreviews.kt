package br.com.doafacil.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import br.com.doafacil.screens.*
import br.com.doafacil.ui.theme.DoaFacilTheme

/**
 * Esta classe agrupa todos os previews do aplicativo para facilitar
 * a visualização no Android Studio.
 */
@Preview(
    name = "Todos os Previews",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun AllPreviews() {
    DoaFacilTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                PreviewSection(
                    title = "Tela de Login",
                    content = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()

                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            LoginScreen(rememberNavController())
                        }
                    }
                )

                PreviewSection(
                    title = "Tela de Avaliação",
                    content = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()

                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            RateScreen(rememberNavController())
                        }
                    }
                )

                PreviewSection(
                    title = "Tela Inicial",
                    content = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            HomeScreen(rememberNavController())
                        }
                    }
                )
                
                PreviewSection(
                    title = "Lista de ONGs",
                    content = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            NGOListScreen(rememberNavController())
                        }
                    }
                )
                
                PreviewSection(
                    title = "Detalhes da ONG",
                    content = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            NGODetailsScreen(
                                ngoId = "1",
                                navController = rememberNavController()
                            )
                        }
                    }
                )
                
                PreviewSection(
                    title = "Histórico de Doações",
                    content = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            DonationHistoryScreen(rememberNavController())
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun PreviewSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(9f / 16f) // Proporção de tela de celular
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }
} 