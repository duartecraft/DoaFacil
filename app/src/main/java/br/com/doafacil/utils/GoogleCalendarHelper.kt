package br.com.doafacil.utils

import android.content.Context
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import com.google.api.client.util.DateTime
import java.io.IOException

object GoogleCalendarHelper {

    private fun getCalendarService(context: Context): Calendar? {
        return try {
            val assetManager = context.assets
            val inputStream = assetManager.open("service_account.json")

            val googleCredential = GoogleCredential.fromStream(inputStream)
                .createScoped(listOf("https://www.googleapis.com/auth/calendar"))

            Calendar.Builder(
                NetHttpTransport(),
                GsonFactory(),
                googleCredential
            ).setApplicationName("DoaFacil").build()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun createEvent(
        context: Context,
        title: String,
        location: String,
        description: String,
        startMillis: Long,
        endMillis: Long
    ) {
        val calendarService = getCalendarService(context)
        if (calendarService == null) {
            println("❌ Erro ao obter serviço do Google Calendar!")
            return
        }

        try {
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

            println("Evento criado com sucesso: ${createdEvent.htmlLink}")

        } catch (e: Exception) {
            println("❌ Erro ao criar evento: ${e.localizedMessage}")
            e.printStackTrace()
        }
    }
}