package br.com.doafacil.utils

import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.UserCredentials
import com.google.api.client.util.DateTime

// Importando corretamente o BuildConfig
import br.com.doafacil.BuildConfig

object GoogleCalendarHelper {

    fun createEvent(
        title: String,
        location: String,
        description: String,
        startMillis: Long,
        endMillis: Long
    ) {
        try {
            // Obtendo as credenciais do BuildConfig
            val clientId = BuildConfig.GOOGLE_CLIENT_ID.takeIf { it.isNotEmpty() }
                ?: throw IllegalStateException("⚠ GOOGLE_CLIENT_ID não configurado corretamente!")

            val clientSecret = BuildConfig.GOOGLE_CLIENT_SECRET.takeIf { it.isNotEmpty() }
                ?: throw IllegalStateException("⚠ GOOGLE_CLIENT_SECRET não configurado corretamente!")

            val refreshToken = BuildConfig.GOOGLE_REFRESH_TOKEN.takeIf { it.isNotEmpty() }
                ?: throw IllegalStateException("⚠ GOOGLE_REFRESH_TOKEN não configurado corretamente!")

            // Criando credenciais do usuário
            val userCredentials = UserCredentials.newBuilder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRefreshToken(refreshToken)
                .build()

            // Criando serviço do Google Calendar
            val calendarService = Calendar.Builder(
                NetHttpTransport(),
                GsonFactory(),
                HttpCredentialsAdapter(userCredentials)
            ).setApplicationName("DoaFacil").build()

            // Criando evento no Google Calendar
            val event = Event()
                .setSummary(title)
                .setLocation(location)
                .setDescription(description)

            val start = EventDateTime()
                .setDateTime(DateTime(startMillis))
                .setTimeZone("America/Sao_Paulo")

            val end = EventDateTime()
                .setDateTime(DateTime(endMillis))
                .setTimeZone("America/Sao_Paulo")

            event.start = start
            event.end = end

            val createdEvent = calendarService.events()
                .insert("primary", event)
                .execute()

            println("✅ Evento criado com sucesso: ${createdEvent.htmlLink}")

        } catch (e: IllegalStateException) {
            println("❌ Erro de configuração: ${e.message}")
        } catch (e: Exception) {
            println("❌ Erro ao criar evento: ${e.localizedMessage}")
            e.printStackTrace()
        }
    }
}
