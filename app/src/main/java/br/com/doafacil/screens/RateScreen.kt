package br.com.doafacil.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.doafacil.R
import br.com.doafacil.ui.theme.DoaFacilTheme
import br.com.doafacil.utils.GamificationAction
import br.com.doafacil.utils.GamificationManager
import kotlinx.coroutines.delay
import android.widget.Toast

@SuppressLint("AutoboxingStateCreation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateScreen(navController: NavController) {
    val context = LocalContext.current
    val isInPreview = LocalInspectionMode.current  // <- Correção aqui

    var rating by remember { mutableIntStateOf(0) }
    var feedback by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val hasRatedBefore by remember { mutableStateOf(GamificationManager.hasRatedBefore()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("HOME") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.onPrimary
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            Text(
                text = stringResource(R.string.rate),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                for (i in 1..5) {
                    IconButton(onClick = { rating = i }) {
                        Icon(
                            painter = painterResource(
                                id = if (i <= rating) R.drawable.ic_star_filled else R.drawable.ic_star_outfilled
                            ),
                            contentDescription = "Nota $i"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = feedback,
                onValueChange = { feedback = it },
                label = { Text("Deixe um comentário") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { })
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (rating > 0) {
                        showDialog = true
                        if (!hasRatedBefore) {
                            GamificationManager.addPointsForAction(GamificationAction.FIRST_REVIEW)

                            // Somente exibe Toast se não estiver no Preview
                            if (!isInPreview) {
                                Toast.makeText(
                                    context,
                                    "Obrigado pelo feedback!\nVocê ganhou ${GamificationAction.FIRST_REVIEW.points} pontos!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            GamificationManager.markAsRated()
                        }
                    }
                },
                enabled = rating > 0
            ) {
                Text("Enviar Avaliação")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Avaliação enviada") },
            text = { Text("Obrigado pelo seu feedback!") },
            confirmButton = {}
        )

        LaunchedEffect(Unit) {
            delay(3000)
            showDialog = false
            navController.navigate("HOME")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RateScreenPreview() {

    DoaFacilTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            RateScreen(navController = rememberNavController())
        }
    }
}


