package br.com.doafacil.screens

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import br.com.doafacil.utils.GamificationAction
import br.com.doafacil.utils.GamificationManager
import br.com.doafacil.utils.UserCalendarHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendamentoScreen(ngoId: String, navController: NavController) {
    val context = LocalContext.current
    val activity = context as? Activity

    // Simulação de busca do objeto ONG
    val ngo = remember {
        listOf(
            NGO("1", "Amigos do Bem", "São Paulo, SP", "Combate à fome e pobreza")
        ).find { it.id == ngoId }
    }

    if (ngo == null) {
        Toast.makeText(context, "ONG não encontrada!", Toast.LENGTH_SHORT).show()
        navController.popBackStack()
        return
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

            // Adiciona pontos para o agendamento
            GamificationManager.addPointsForAction(GamificationAction.APPOINTMENT)

            // Obtém a nova pontuação total do usuário
            val newPoints = GamificationManager.getPoints()

            Toast.makeText(
                context,
                "Agendamento confirmado!\nVocê ganhou ${GamificationAction.APPOINTMENT.points} ponto! Total: $newPoints",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(context, "Preencha a data e horário!", Toast.LENGTH_SHORT).show()
        }
    }

    // Função que cria o evento no calendário usando a conta do usuário
    fun addEventToGoogleCalendar(account: GoogleSignInAccount) {
        // Converte a data e horário selecionados
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

        // Clona o startTime para criar o endTime e adiciona uma hora
        val endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 1)

        try {
            println("🔹 Tentando criar evento no Google Calendar...")
            val calendarService = UserCalendarHelper.createCalendarService(context, account)
            UserCalendarHelper.createEvent(
                calendarService = calendarService,
                title = "Visita à ONG ${ngo.name}",
                location = ngo.location,
                description = "Visita agendada via DoaFacil",
                startMillis = startTime.timeInMillis,
                endMillis = endTime.timeInMillis
            )
            println("✅ Evento criado com sucesso!")
            Toast.makeText(context, "Agendamento enviado ao Google Calendar!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            println("❌ Erro ao criar evento: ${e.message}")
            Toast.makeText(context, "Erro ao adicionar ao Google Calendar: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Agendar Visita") }
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
            Text(text = "ONG: ${ngo.name}")
            Text(text = "Localização: ${ngo.location}")

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
                                if (activity != null) {
                                    // Tenta recuperar a conta já logada
                                    val account = GoogleSignIn.getLastSignedInAccount(context)
                                    if (account != null) {
                                        println("🔹 Usuário já autenticado. Criando evento...")
                                        addEventToGoogleCalendar(account)
                                    } else {
                                        // Se não houver conta logada, inicia o fluxo de login
                                        GoogleAuthHelper.signInGoogle(
                                            activity,
                                            onSuccess = { account ->
                                                println("✅ Autenticação bem-sucedida! Criando evento...")
                                                addEventToGoogleCalendar(account)
                                            },
                                            onFailure = {
                                                println("❌ Falha na autenticação com o Google")
                                                Toast.makeText(context, "Erro ao autenticar com o Google", Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    }
                                } else {
                                    println("❌ Erro: Activity é nula")
                                    Toast.makeText(context, "Erro: Activity é nula!", Toast.LENGTH_SHORT).show()
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
                                navController.popBackStack()
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
