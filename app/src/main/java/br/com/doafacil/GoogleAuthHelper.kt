package br.com.doafacil.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.api.services.calendar.CalendarScopes

object GoogleAuthHelper {
    private lateinit var googleSignInClient: GoogleSignInClient
    private const val RC_SIGN_IN = 1001

    fun signInGoogle(context: Context, activity: Activity) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(com.google.android.gms.common.api.Scope(CalendarScopes.CALENDAR)) // Permissão para acessar o Google Calendar
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun handleSignInResult(context: Context, data: Intent?) {
        val account = GoogleSignIn.getSignedInAccountFromIntent(data)
        if (account.isSuccessful) {
            val userAccount: GoogleSignInAccount? = account.result
            Toast.makeText(context, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Falha ao autenticar.", Toast.LENGTH_SHORT).show()
        }
    }

    fun getSignedInAccount(context: Context): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }

    fun signOutGoogle(context: Context) {
        googleSignInClient.signOut().addOnCompleteListener {
            Toast.makeText(context, "Desconectado do Google.", Toast.LENGTH_SHORT).show()
        }
    }
}

