package br.com.doafacil.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.api.services.calendar.CalendarScopes

object GoogleAuthHelper {
    const val RC_SIGN_IN = 1001

    private var successCallback: ((GoogleSignInAccount) -> Unit)? = null
    private var failureCallback: ((String) -> Unit)? = null

    fun signInGoogle(
        activity: Activity?,
        onSuccess: (GoogleSignInAccount) -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (activity == null) {
            onFailure("Erro: Não foi possível obter a Activity!")
            return
        }

        successCallback = onSuccess
        failureCallback = onFailure

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken("YOUR_WEB_CLIENT_ID") // Substitua pelo seu Client ID OAuth
            .requestScopes(Scope(CalendarScopes.CALENDAR))
            .build()

        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        val signInIntent = googleSignInClient.signInIntent

        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun handleSignInResult(data: Intent?) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                successCallback?.invoke(account)
            } else {
                failureCallback?.invoke("Conta não encontrada ou inválida.")
            }
        } catch (e: ApiException) {
            failureCallback?.invoke("Erro ao processar sign-in: ${e.localizedMessage}")
        } catch (e: Exception) {
            failureCallback?.invoke("Erro inesperado: ${e.localizedMessage}")
        } finally {
            successCallback = null
            failureCallback = null
        }
    }

    fun getSignedInAccount(context: Context): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }
}