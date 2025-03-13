package br.com.doafacil.utils

import android.content.Context
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.UserCredentials
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.auth.http.HttpCredentialsAdapter

object GoogleCalendarHelper {

    fun createEvent(
        context: Context,
        title: String,
        location: String,
        description: String,
        startMillis: Long,
        endMillis: Long
    ) {
        try {
            val clientId = System.getenv("GOOGLE_CLIENT_ID") ?: error("GOOGLE_CLIENT_ID not set")
            val clientSecret = System.getenv("GOOGLE_CLIENT_SECRET") ?: error("GOOGLE_CLIENT_SECRET not set")
            val refreshToken = System.getenv("GOOGLE_REFRESH_TOKEN") ?: error("GOOGLE_REFRESH_TOKEN not set")

            val userCredentials = UserCredentials.newBuilder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRefreshToken(refreshToken)
                .build()

            val calendarService = Calendar.Builder(
                NetHttpTransport(),
                GsonFactory(),
                HttpCredentialsAdapter(userCredentials)
            ).setApplicationName("DoaFacil").build()


            val event = Event()
                .setSummary(title)
                .setLocation(location)
                .setDescription(description)

            val start = EventDateTime()
                .setDateTime(com.google.api.client.util.DateTime(startMillis))
                .setTimeZone("America/Sao_Paulo")

            val end = EventDateTime()
                .setDateTime(com.google.api.client.util.DateTime(endMillis))
                .setTimeZone("America/Sao_Paulo")

            event.start = start
            event.end = end

            val createdEvent = calendarService.events()
                .insert("primary", event)
                .execute()

            println("Evento criado: ${createdEvent.htmlLink}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
