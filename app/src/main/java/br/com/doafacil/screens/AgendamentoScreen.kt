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
import br.com.doafacil.auth.GoogleAuthHelper
import br.com.doafacil.utils.GoogleCalendarHelper
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendamentoScreen(ngoId: String, navController: NavController) {
    val context = LocalContext.current
    val activity = context as? Activity

    val ngo = remember {
        listOf(
            NGO("1", "Amigos do Bem", "São Paulo, SP", "Combate à fome e pobreza")
        ).find { it.id == ngoId }
    }

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

    fun addEventToGoogleCalendar(context: Context) {
        if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
            val dateParts = selectedDate.split("/")
            val day = dateParts[0].toInt()
            val month = dateParts[1].toInt() - 1
            val year = dateParts[2].toInt()

            val timeParts = selectedTime.split(":")
            val hour = timeParts[0].toInt()
            val minute = timeParts[1].toInt()

            val startTime = Calendar.getInstance().apply {
                set(year, month, day, hour, minute)
            }

            val endTime = Calendar.getInstance().apply {
                set(year, month, day, hour + 1, minute)
            }

            GoogleCalendarHelper.createEvent(
                title = "Visita à ONG ${ngo?.name}",
                location = "${ngo?.location}",
                description = "Visita agendada via DoaFacil",
                startMillis = startTime.timeInMillis,
                endMillis = endTime.timeInMillis
            )

            Toast.makeText(context, "Agendamento enviado ao Google Calendar!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Preencha a data e horário!", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {Text("Agendar Visita") }
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
                Text("Confirmar Agendamento")
            }

            if (showConfirmationDialog) {
                AlertDialog(
                    onDismissRequest = { showConfirmationDialog = false },
                    title = { Text("Agendamento Confirmado!") },
                    text = {
                        Column {
                            Text("Sua visita foi agendada com sucesso!")
                            Text("Deseja adicionar esse evento automaticamente ao Google Calendar?")
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                val userAccount = GoogleAuthHelper.getSignedInAccount(context)
                                if (userAccount == null) {
                                    GoogleAuthHelper.signInGoogle(context, activity!!)
                                } else {
                                    addEventToGoogleCalendar(context)
                                }
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
