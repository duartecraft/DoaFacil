package br.com.doafacil.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.doafacil.navigation.Routes
import br.com.doafacil.ui.theme.DoaFacilTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationConfirmationScreen(navController: NavController, ngoName: String, ngoId: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Doação Concluída") },
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
                .padding(top = 32.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Obrigado por sua doação!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Sua contribuição ajudará a ONG $ngoName a continuar seu trabalho incrível.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate(Routes.ngoDetails(ngoId)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Voltar para o perfil da ONG")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DonationConfirmationScreenPreview() {
    val previewNavController = rememberNavController()
    DoaFacilTheme {
        DonationConfirmationScreen(
            navController = previewNavController,
            ngoName = "Amigos do Bem",
            ngoId = "1"
        )
    }
}