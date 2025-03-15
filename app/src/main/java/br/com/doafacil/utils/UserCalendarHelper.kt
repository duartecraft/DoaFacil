package br.com.doafacil.utils

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes

object UserCalendarHelper {

    fun createCalendarService(context: Context, account: GoogleSignInAccount): Calendar {
        val credential = GoogleAccountCredential.usingOAuth2(
            context,
            listOf(CalendarScopes.CALENDAR)
        ).apply {
            selectedAccount = account.account
        }

        return Calendar.Builder(
            NetHttpTransport(),
            GsonFactory(),
            credential
        ).setApplicationName("DoaFacil")
            .build()
    }

    fun createEvent(
        calendarService: Calendar,
        title: String,
        location: String,
        description: String,
        startMillis: Long,
        endMillis: Long
    ) {
        try {
            val event = com.google.api.services.calendar.model.Event().apply {
                summary = title
                this.location = location
                this.description = description

                start = com.google.api.services.calendar.model.EventDateTime().apply {
                    dateTime = com.google.api.client.util.DateTime(startMillis)
                    timeZone = "America/Sao_Paulo"
                }
                end = com.google.api.services.calendar.model.EventDateTime().apply {
                    dateTime = com.google.api.client.util.DateTime(endMillis)
                    timeZone = "America/Sao_Paulo"
                }
            }

            val createdEvent = calendarService.events()
                .insert("primary", event)
                .execute()

            println("✅ Evento criado com sucesso: ${createdEvent.htmlLink}")

        } catch (e: Exception) {
            println("❌ Erro ao criar evento: ${e.localizedMessage}")
            e.printStackTrace()
        }
    }
}
