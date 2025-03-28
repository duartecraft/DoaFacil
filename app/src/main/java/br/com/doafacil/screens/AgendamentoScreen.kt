package br.com.doafacil.screens

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import br.com.doafacil.ui.theme.DoaFacilTheme
import br.com.doafacil.auth.GoogleAuthHelper
import br.com.doafacil.utils.GoogleCalendarHelper
import br.com.doafacil.data.NGORepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import java.util.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendamentoScreen(ngoId: String, navController: NavController) {
    val context = LocalContext.current
    val activity = context as? Activity

    // Obtém a ONG da lista de ONGs com base no ID recebido
    val ngo = remember { NGORepository.getNGOById(ngoId) }

    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()

    fun showDatePicker() {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                selectedDate = "$dayOfMonth/${month + 1}/$year"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun showTimePicker() {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    fun confirmarAgendamento() {
        if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
            showConfirmationDialog = true
        } else {
            Toast.makeText(context, "Preencha a data e horário!", Toast.LENGTH_SHORT).show()
        }
    }

    fun addEventToGoogleCalendar(
        context: Context,
        selectedDate: String,
        selectedTime: String
    ) {
        val activity = context as? Activity
        val account = GoogleAuthHelper.getSignedInAccount(context)

        if (account != null) {

            GoogleCalendarHelper.createEvent(
                context = context,
                title = "Visita à ONG",
                location = "Local",
                description = "Visita agendada via DoaFacil",
                startMillis = obterStartTime(selectedDate, selectedTime),
                endMillis = obterEndTime(selectedDate, selectedTime)
            )
            Toast.makeText(context, "Agendamento enviado ao Google Calendar!", Toast.LENGTH_SHORT).show()
        } else {
            // Se não está logado, inicia o login
            GoogleAuthHelper.signInGoogle(
                activity!!,
                onSuccess = { loggedInAccount ->
                    GoogleCalendarHelper.createEvent(
                        context = context,
                        title = "Visita à ONG",
                        location = "Local",
                        description = "Visita agendada via DoaFacil",
                        startMillis = obterStartTime(selectedDate, selectedTime),
                        endMillis = obterEndTime(selectedDate, selectedTime)
                    )
                    Toast.makeText(context, "Agendamento enviado ao Google Calendar!", Toast.LENGTH_SHORT).show()
                },
                onFailure = {
                    Toast.makeText(context, "Erro ao fazer login no Google!", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar visita") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "ONG: ${ngo?.name}")
            Text(text = "Localização: ${ngo?.location}")

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { showDatePicker() }, modifier = Modifier.fillMaxWidth()) {
                Text(text = if (selectedDate.isEmpty()) "Escolha a data" else "Data: $selectedDate")
            }

            Button(onClick = { showTimePicker() }, modifier = Modifier.fillMaxWidth()) {
                Text(text = if (selectedTime.isEmpty()) "Escolha o horário" else "Horário: $selectedTime")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { confirmarAgendamento() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar agendamento")
            }

            if (showConfirmationDialog) {
                AlertDialog(
                    onDismissRequest = { showConfirmationDialog = false },
                    title = { Text("Agendamento confirmado!") },
                    text = {
                        Column {
                            Text("Sua visita foi agendada com sucesso!")
                            Text("Deseja adicionar esse evento automaticamente ao Google Calendar?")
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                addEventToGoogleCalendar(context, selectedDate, selectedTime)
                                showConfirmationDialog = false
                            }
                        ) {
                            Text("Adicionar ao Google Calendar")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showConfirmationDialog = false
                                navController.popBackStack() // Volta para a tela da ONG
                            }
                        ) {
                            Text("Seguir no app")
                        }
                    }
                )
            }
        }
    }
}

fun obterStartTime(selectedDate: String, selectedTime: String): Long {
    val dateParts = selectedDate.split("/")
    val day = dateParts[0].toInt()
    val month = dateParts[1].toInt() - 1
    val year = dateParts[2].toInt()

    val timeParts = selectedTime.split(":")
    val hour = timeParts[0].toInt()
    val minute = timeParts[1].toInt()

    return Calendar.getInstance().apply {
        set(year, month, day, hour, minute)
    }.timeInMillis
}

fun obterEndTime(selectedDate: String, selectedTime: String): Long {
    return obterStartTime(selectedDate, selectedTime) + 60 * 60 * 1000
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AgendamentoScreenPreview() {
    val fakeNavController = rememberNavController()
    DoaFacilTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AgendamentoScreen(ngoId = "1", navController = fakeNavController)
        }
    }
}
