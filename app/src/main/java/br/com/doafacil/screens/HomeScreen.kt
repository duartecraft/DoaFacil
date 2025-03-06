package br.com.doafacil.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
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
            }
            
            item {
                FeaturedNGOsSection(navController)
            }
            
            item {
                QuickActionsSection(navController)
            }
        }
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